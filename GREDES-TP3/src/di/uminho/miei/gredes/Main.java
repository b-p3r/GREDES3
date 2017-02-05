package di.uminho.miei.gredes;


import java.io.IOException;

import org.snmp4j.smi.OID;

public class Main {

	private static SimpleSnmpClient client;

	// These are both standard in RFC-1213
	static final OID sysDescr = new OID(".1.3.6.1.2.1.1.1.0");
	static final OID interfacesTable = new OID(".1.3.6.1.2.1.2.2.1");
	
	

	public static void main(String[] args) throws IOException {
		client = new SimpleSnmpClient("udp:127.0.0.1/2001");

		String syStringR = client.getAsString(UminhoGrMib.oidParamR);
		String syStringN = client.getAsString(UminhoGrMib.oidParamN);
		String syStringD = client.getAsString(UminhoGrMib.oidParamD);
		String syStringRes = client.getAsString(UminhoGrMib.oidParamAuthReset);

//		String taString = client
//				.getTableAsStrings(new OID[] { new OID(interfacesTable.toString() + ".2"),
//						new OID(interfacesTable.toString() + ".6"), new OID(interfacesTable.toString() + ".8") })
//				.toString();
		String syString = client.getAsString(sysDescr);
		
		System.out.println("sysDescr :"+syString);
		
		System.out.println("sysDescr :"+syStringR);
		System.out.println("sysDescr :"+syStringN);
		System.out.println("sysDescr :"+syStringD);
		System.out.println("sysDescr :"+syStringRes);
//		
//		System.out.println("interfacesTable :"+taString);

	}

}
