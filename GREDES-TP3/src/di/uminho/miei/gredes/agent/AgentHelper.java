package di.uminho.miei.gredes.agent;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.snmp4j.agent.mo.DefaultMOMutableRow2PC;
import org.snmp4j.smi.Integer32;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.Variable;

import di.uminho.miei.gredes.tables.MOTableBuilder;

/**
 * 
 * @author Bruno Pereira
 * 
 * @date 2017
 *
 */
public class AgentHelper {

	private int udpPort;
	private String communityString;
	private int frequencyR;
	private int entryN;
	private int numberD;
	private String pathToInitSeed;
	private String pathToConfFile;

	private String resetKey;
	private boolean isReset = false;

	private long refreshingTime;
	private long waitingTime;

	private Lock lock = new ReentrantLock();

	private Condition onReset = lock.newCondition();

	/**
	 * 
	 * @param pathToConfFile
	 * @param resetKey
	 */
	public AgentHelper(String pathToConfFile, String resetKey) {
		super();
		this.pathToConfFile = pathToConfFile;
		this.resetKey = resetKey;
		this.refreshingTime = 0;
		this.waitingTime = 0;
		this.loadConf(this.pathToConfFile);

	}

	/**
	 * 
	 * @param moTableBuilder
	 * @throws InterruptedException
	 */
	public synchronized void refresh(MOTableBuilder moTableBuilder) throws InterruptedException {
		synchronized (moTableBuilder) {
			while (this.isReset == true)
				onReset.await();

			int N = this.getEntryN();
			int D = this.getNumberD();
			int X = 0;
			int S1 = 0;
			int S2 = 0;
			int m[][] = new int[N][D];
			String[] seed = new String[N];

			int a = 0;
			for (Iterator<?> iterator = moTableBuilder.getUnpredictableTableEntry().getColumns()[1].getTable()
					.getModel().iterator(); iterator.hasNext();) {
				DefaultMOMutableRow2PC type = (DefaultMOMutableRow2PC) iterator.next();
				if (type.getValue(1) != null) {
					seed[a] = type.getValue(1).toString();
					a++;
				} else
					return;

			}

			for (int i = 0; i < this.getResetKey().length(); i++) {
				X += this.getResetKey().charAt(i);
			}

			for (int i = 0; i < N; i++) {
				for (int j = 0; j < D; j++) {

					m[i][j] = Integer.parseInt(seed[i].charAt(j) + "", 16);
					if (((i % 2) == 0) && ((j % 2) == 0)) {
						S1 += Integer.parseInt(seed[i].charAt(j) + "", 16);
					} else if (((i % 2) != 0) && ((j % 2) != 0)) {
						S2 += Integer.parseInt(seed[i].charAt(j) + "", 16);
					}

				}

			}

			int C = (S1 + X) % D;
			int L = (S2 + X) % N;

			vertical(C + 1, 1, N, m);
			horizontal(L + 1, 1, D, m);
			substitution(L + 1, C + 2, N, D, m);

			String[] toReturn = new String[N];

			for (int i = 0; i < N; i++) {

				StringBuilder sb = new StringBuilder();
				for (int j = 0; j < D; j++) {
					sb.append(Integer.toHexString(m[i][j]));

				}

				toReturn[i] = sb.toString();

			}

			System.out.println("{");
			for (int i = 0; i < N; i++) {
				System.out.println("\t" + toReturn[i] + "\t");
			}
			System.out.println("}");

			a = 0;
			for (Iterator<?> iterator = moTableBuilder.getUnpredictableTableEntry().getColumns()[1].getTable()
					.getModel().iterator(); iterator.hasNext();) {
				DefaultMOMutableRow2PC type = (DefaultMOMutableRow2PC) iterator.next();
				type.setValue(0, new Integer32(a + 1));
				type.setValue(1, new OctetString(toReturn[a]));
				a++;

			}
		}

	}

	/**
	 * 
	 * @param moTableBuilder
	 * @param agent
	 */
	public void reset(MOTableBuilder moTableBuilder, Agent agent) {

		synchronized (moTableBuilder) {
			this.setReset(true);

			agent.unregisterManagedObject(moTableBuilder);

			ArrayList<String> seed = loadSeed();

			for (String string : seed) {
				moTableBuilder.addRowValue((Variable) new OctetString(string));

			}

			moTableBuilder.build();

			agent.registerManagedObject(moTableBuilder);

			this.setReset(false);
			this.setWaitingTime(this.calcWaitingTime());
			onReset.signal();
		}

	}

	/**
	 * 
	 * @return
	 */
	public ArrayList<String> loadSeed() {

		ArrayList<String> seed = new ArrayList<>();
		try {

			BufferedReader br = new BufferedReader(new FileReader(getPathToInitSeed()));

			try {

				String line = br.readLine();

				while (line != null) {

					seed.add(line);
					line = br.readLine();

				}

			} finally {
				br.close();
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		return seed;

	}

	/**
	 * 
	 * @param pathToConf
	 */
	public void loadConf(String pathToConf) {
		try {
			BufferedReader br = new BufferedReader(new FileReader(pathToConf));
			try {
				String[] data = new String[6];
				int i = 0;

				String line = br.readLine();

				while (line != null) {
					String[] token = line.split(" ");
					data[i] = token[1];
					line = br.readLine();
					i++;
				}

				this.setUdpPort(new Integer(data[0]));
				this.setCommunityString(data[1]);
				this.setFrequencyR(new Integer(data[2]));
				this.setEntryN(new Integer(data[3]));
				this.setNumberD(new Integer(data[4]));
				this.setPathToInitSeed(data[5]);

			} finally {
				br.close();
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		this.setReset(false);
		this.setRefreshingTime(this.calcRefreshingTime());
		this.setWaitingTime(0);

	}

	/**
	 * 
	 * @param i
	 * @param j
	 * @param n
	 * @param d
	 * @param b
	 */
	private void substitution(int i, int j, int n, int d, int[][] b) {

		/**
		 * Conversão de índices para índices base 0 e salvaguardas de introdução
		 * de dados. Numa parametrização errada de 0, ou de um valor maior que o
		 * número máximo de linhas ou colunas, o índíce dá a volta. Por exemplo,
		 * suponhamos D = 8 e j = 10. Então a coluna escolhida será j = |D - j|
		 * = | -2 | = 2. Noutros casos o índice é decrementado.
		 */
		if (i == 0) {
			i = (n - 1);
		} else if (i >= n) {
			i = Math.abs(n - i);
		} else {
			i = i - 1;
		}

		if (j == 0) {
			j = (d - 1);
		} else if (j >= d) {
			j = Math.abs(d - j);
		} else {
			j = j - 1;
		}

		int backcol = 0, frontcol = 0, toprow = 0, belowrow = 0;

		/**
		 * Se a linha escolhida for a 0, então a linha de cima escolhida para o
		 * XOR será o N -1 (índice base 0). Caso contrário, é escolhida a linha
		 * de cima
		 */
		toprow = (i == 0) ? (n - 1) : i - 1;
		/**
		 * Se a linha escolhida for a N - 1 (índice base 0), então a linha de
		 * baixo escolhida para o XOR será o 0 (índice base 0). Caso contrário,
		 * é escolhida a linha de baixo
		 */
		belowrow = (i >= (n - 1)) ? 0 : i + 1;
		backcol = (j == 0) ? (d - 1) : j - 1;
		frontcol = (j >= (d - 1)) ? 0 : j + 1;

		ArrayList<Integer> colToCalc = new ArrayList<>();
		ArrayList<Integer> rowToCalc = new ArrayList<>();

		for (int k = 0; k < d; k++) {

			rowToCalc.add(b[toprow][k] ^ b[belowrow][k]);

		}

		for (int k = 0; k < d; k++) {
			b[i][k] = rowToCalc.get(k);

		}

		for (int k = 0; k < n; k++) {

			colToCalc.add(b[k][backcol] ^ b[k][frontcol]);

		}
		for (int k = 0; k < n; k++) {
			b[k][j] = colToCalc.get(k);

		}

	}

	/**
	 * 
	 * @param i
	 * @param j
	 * @param d
	 * @param m
	 */
	private void horizontal(int i, int j, int d, int[][] m) {

		i--;
		j = (j == d || j == 0) ? 0 : Math.abs(d - j);

		ArrayList<Integer> rowToDeslocate = new ArrayList<>();
		for (int k = 0; k < d; k++) {

			rowToDeslocate.add(m[i][j]);
			j = (j + 1) % d;

		}

		for (int k = 0; k < d; k++) {
			m[i][k] = rowToDeslocate.get(k);

		}

	}

	/**
	 * 
	 * @param i
	 * @param j
	 * @param n
	 * @param m
	 */
	private void vertical(int i, int j, int n, int[][] m) {
		i--;
		j = (j == n || j == 0) ? 0 : Math.abs(n - j);

		ArrayList<Integer> columnToDeslocate = new ArrayList<>();
		for (int k = 0; k < n; k++) {

			columnToDeslocate.add(m[j][i]);
			j = (j + 1) % n;

		}

		for (int k = 0; k < n; k++) {
			m[k][i] = columnToDeslocate.get(k);

		}

	}

	/**
	 * 
	 * @return
	 */
	public long calcRefreshingTime() {
		return (1 / this.frequencyR) * 1000;
	}

	/**
	 * 
	 * @return
	 */
	public long calcWaitingTime() {
		return this.frequencyR * 10 * 1000;
	}

	/**
	 * 
	 * @return
	 */
	public int getUdpPort() {
		return udpPort;
	}

	/**
	 * 
	 * @param udpPort
	 */
	public void setUdpPort(int udpPort) {
		this.udpPort = udpPort;
	}

	/**
	 * 
	 * @return
	 */
	public String getCommunityString() {
		return communityString;
	}

	/**
	 * 
	 * @param communityString
	 */
	public void setCommunityString(String communityString) {
		this.communityString = communityString;
	}

	/**
	 * 
	 * @return
	 */
	public int getFrequencyR() {
		return frequencyR;
	}

	/**
	 * 
	 * @param frequencyR
	 */
	public void setFrequencyR(int frequencyR) {
		this.frequencyR = frequencyR;
	}

	/**
	 * 
	 * @return
	 */
	public int getEntryN() {
		return entryN;
	}

	/**
	 * 
	 * @param entryN
	 */
	public void setEntryN(int entryN) {
		this.entryN = entryN;
	}

	/**
	 * 
	 * @return
	 */
	public int getNumberD() {
		return numberD;
	}

	/**
	 * 
	 */
	public void setNumberD(int numberD) {
		this.numberD = numberD;
	}

	/**
	 * 
	 * @return
	 */
	public String getPathToInitSeed() {
		return pathToInitSeed;
	}

	/**
	 * 
	 * @param pathToInitSeed
	 */
	public void setPathToInitSeed(String pathToInitSeed) {
		this.pathToInitSeed = pathToInitSeed;
	}

	/**
	 * 
	 * @return
	 */
	public String getResetKey() {
		return resetKey;
	}

	/**
	 * 
	 * @param resetKey
	 */
	public void setResetKey(String resetKey) {
		this.resetKey = resetKey;
	}

	/**
	 * 
	 * @return
	 */
	public boolean isReset() {
		return isReset;
	}

	/**
	 * 
	 * @param reset
	 */
	public void setReset(boolean reset) {
		isReset = reset;
	}

	/**
	 * 
	 * @return
	 */
	public long getRefreshingTime() {
		return refreshingTime;
	}

	/**
	 * 
	 * @param refreshingTime
	 */
	public void setRefreshingTime(long refreshingTime) {
		this.refreshingTime = refreshingTime;
	}

	/**
	 * 
	 * @return
	 */
	public long getWaitingTime() {
		return waitingTime;
	}

	/**
	 * 
	 * @param waitingTime
	 */
	public void setWaitingTime(long waitingTime) {
		this.waitingTime = waitingTime;
	}

	/**
	 * @return the pathToConfFile
	 */
	public String getPathToConfFile() {
		return pathToConfFile;
	}

	/**
	 * @param pathToConfFile
	 *            the pathToConfFile to set
	 */
	public void setPathToConfFile(String pathToConfFile) {
		this.pathToConfFile = pathToConfFile;
	}

}
