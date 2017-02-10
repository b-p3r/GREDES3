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
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.Variable;

import di.uminho.miei.gredes.tables.MOTableBuilder;

public class AgentHelper {

	private int udpPort;
	private String communityString;
	private int frequencyR;
	private int entryN;
	private int numberD;
	private String pathToInitSeed;
	private String pathToConfFile;

	private String resetKey;
	private boolean isReset;

	private long refreshingTime;
	private long waitingTime;

	private Lock lock = new ReentrantLock();
	

	private Condition onReset = lock.newCondition();

	public AgentHelper(String pathToConfFile, String resetKey) {
		super();
		this.pathToConfFile = pathToConfFile;
		this.resetKey = resetKey;
		this.loadConf(this.pathToConfFile);
	}

	public void reset(MOTableBuilder moTableBuilder, Agent agent) {
		
		this.setReset(true);

		agent.unregisterManagedObject(moTableBuilder);

		
		ArrayList<String> seed = loadSeed();

		for (String string : seed) {
			moTableBuilder.addRowValue((Variable) new OctetString(string));

		}

		moTableBuilder.build();

		agent.registerManagedObject(moTableBuilder);
		
		
		this.setReset(false);
		onReset.signalAll();

	

	}

	public ArrayList<String> loadSeed() {

		// String[] seed = new String[this.entryN+1];
		ArrayList<String> seed = new ArrayList<>();
		try {
			// TODO: aplicar caminho do ficheiro de conf
			// BufferedReader br = new BufferedReader(new
			// FileReader(getPathToInitSeed()));
			BufferedReader br = new BufferedReader(new FileReader("resources/first-map-table.txt"));
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
		this.setWaitingTime(this.calcWaitingTime());

	}

	public void refresh(MOTableBuilder moTableBuilder) throws InterruptedException {
		
		while (this.isReset == true) {
			onReset.await();
		}
		System.out.println("refreshing ..................");

		int N = this.getEntryN();
		int D = this.getNumberD();
		int X = 0;
		int S1 = 0;
		int S2 = 0;
		int m[][] = new int[N][D];
		String[] seed = new String[N];


		int a = 0;
		for (Iterator<?> iterator = moTableBuilder.getUnpredictableTableEntry().getColumns()[1].getTable().getModel()
				.iterator(); iterator.hasNext();) {
			DefaultMOMutableRow2PC type = (DefaultMOMutableRow2PC) iterator.next();
			seed[a] = type.getValue(1).toString();
			a++;
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

		System.out.println("X\t" + X);
		System.out.println("S1\t" + S1);
		System.out.println("S2\t" + S2);
		System.out.println("C\t" + C);
		System.out.println("L\t" + L);
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
		for (Iterator<?> iterator = moTableBuilder.getUnpredictableTableEntry().getColumns()[1].getTable().getModel()
				.iterator(); iterator.hasNext();) {
			DefaultMOMutableRow2PC type = (DefaultMOMutableRow2PC) iterator.next();
			type.setValue(1, new OctetString(toReturn[a]));
			a++;

		}
		if(!this.isReset()){
			System.out.println("Paused for :"+this.getWaitingTime());
			Thread.sleep(this.getWaitingTime());
		}
		

	}

	private void substitution(int i, int j, int n, int d, int[][] b) {
		System.out.println(".... substitution");

		/**
		 * Conversão de índices para índices base 0 e salvaguardas de introdução
		 * de dados. Numa parametrização errada de 0, ou de um valor maior que o
		 * número máximo de linhas ou colunas, o índíce dá a volta. Por exemplo,
		 * suponhamos D = 8 e j = 10. Então a coluna escolhida será j = |D - j|
		 * = | -2 | = 2. Noutros casos o índice é decrementado.
		 */
		i = (i == 0) ? (n - 1) : i - 1;
		i = (i >= n) ? Math.abs(n - i) : i - 1;
		j = (j == 0) ? (d - 1) : j - 1;
		j = (j >= d) ? Math.abs(d - j) : j - 1;

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

		// System.out.println("\ttop row\t" + toprow);
		//
		// System.out.println("\tbelowrow\t" + belowrow);
		//
		// System.out.println("\tback col\t" + backcol);
		//
		// System.out.println("\tfrontcol\t" + frontcol);

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

	private void horizontal(int i, int j, int d, int[][] m) {
		System.out.println("..horizontal");
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

	private void vertical(int i, int j, int n, int[][] m) {
		System.out.println("..vertical");

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

	public long calcRefreshingTime() {
		return (1 / this.frequencyR) * 1000;
	}

	public long calcWaitingTime() {
		return this.frequencyR * 10 * 1000;
	}

	public int getUdpPort() {
		return udpPort;
	}

	public void setUdpPort(int udpPort) {
		this.udpPort = udpPort;
	}

	public String getCommunityString() {
		return communityString;
	}

	public void setCommunityString(String communityString) {
		this.communityString = communityString;
	}

	public int getFrequencyR() {
		return frequencyR;
	}

	public void setFrequencyR(int frequencyR) {
		this.frequencyR = frequencyR;
	}

	public int getEntryN() {
		return entryN;
	}

	public void setEntryN(int entryN) {
		this.entryN = entryN;
	}

	public int getNumberD() {
		return numberD;
	}

	public void setNumberD(int numberD) {
		this.numberD = numberD;
	}

	public String getPathToInitSeed() {
		return pathToInitSeed;
	}

	public void setPathToInitSeed(String pathToInitSeed) {
		this.pathToInitSeed = pathToInitSeed;
	}

	public String getResetKey() {
		return resetKey;
	}

	public void setResetKey(String resetRay) {
		this.resetKey = resetRay;
	}

	public boolean isReset() {
		return isReset;
	}

	public void setReset(boolean reset) {
		isReset = reset;
	}

	public long getRefreshingTime() {
		return refreshingTime;
	}

	public void setRefreshingTime(long refreshingTime) {
		this.refreshingTime = refreshingTime;
	}

	public long getWaitingTime() {
		return waitingTime;
	}

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
