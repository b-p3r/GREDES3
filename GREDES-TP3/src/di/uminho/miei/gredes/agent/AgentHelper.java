package di.uminho.miei.gredes.agent;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

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

	public AgentHelper(String pathToConfFile, String resetKey) {
		super();
		this.pathToConfFile = pathToConfFile;
		this.resetKey = resetKey;
		this.load(this.pathToConfFile);
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

	public void load(String pathToConf) {
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
