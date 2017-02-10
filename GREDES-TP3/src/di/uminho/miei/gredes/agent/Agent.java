package di.uminho.miei.gredes.agent;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.snmp4j.TransportMapping;
import org.snmp4j.agent.BaseAgent;
import org.snmp4j.agent.CommandProcessor;
import org.snmp4j.agent.DuplicateRegistrationException;
import org.snmp4j.agent.MOGroup;
import org.snmp4j.agent.ManagedObject;
import org.snmp4j.agent.mo.DefaultMOFactory;
import org.snmp4j.agent.mo.snmp.RowStatus;
import org.snmp4j.agent.mo.snmp.SnmpCommunityMIB;
import org.snmp4j.agent.mo.snmp.SnmpNotificationMIB;
import org.snmp4j.agent.mo.snmp.SnmpTargetMIB;
import org.snmp4j.agent.mo.snmp.StorageType;
import org.snmp4j.agent.mo.snmp.VacmMIB;
import org.snmp4j.agent.security.MutableVACM;
import org.snmp4j.mp.MPv3;
import org.snmp4j.security.SecurityLevel;
import org.snmp4j.security.SecurityModel;
import org.snmp4j.security.USM;
import org.snmp4j.smi.Address;
import org.snmp4j.smi.GenericAddress;
import org.snmp4j.smi.Integer32;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.Variable;
import org.snmp4j.transport.TransportMappings;

import di.uminho.miei.gredes.scalars.MOScalarFactory;
import di.uminho.miei.gredes.tables.MOTableBuilder;

/**
 * 
 * @author Bruno Pereira
 * 
 * @date 2017 
 *
 */
public class Agent extends BaseAgent {

	private String address;

	private String communityString;

	/**
	 * 
	 * @param address
	 * @param communityString
	 * @throws IOException
	 */
	public Agent(String address, String communityString) throws IOException {

		
		super(new File("conf.agent"), new File("bootCounter.agent"),
				new CommandProcessor(new OctetString(MPv3.createLocalEngineID())));
		this.address = address;
		this.communityString = communityString;
	}

	/**
	 * 
	 */
	@Override
	protected void registerManagedObjects() {
	}

	/**
	 * 
	 * @param mo
	 */
	public void registerManagedObject(ManagedObject mo) {
		try {
			server.register(mo, null);
		} catch (DuplicateRegistrationException ex) {
			throw new RuntimeException(ex);
		}
	}

	/**
	 * 
	 * @param moGroup
	 */
	public void registerManagedObject(MOGroup moGroup) {
		try {
			moGroup.registerMOs(server, null);
		} catch (DuplicateRegistrationException ex) {
			throw new RuntimeException(ex);
		}
	}

	/**
	 * 
	 * @param moGroup
	 */
	public void unregisterManagedObject(MOGroup moGroup) {
		moGroup.unregisterMOs(server, getContext(moGroup));
	}

	/**
	 * 
	 */
	@Override
	protected void addNotificationTargets(SnmpTargetMIB targetMIB, SnmpNotificationMIB notificationMIB) {
	}

	/**
	 * http://www.faqs.org/rfcs/rfc2575.html
	 */
	@Override
	protected void addViews(VacmMIB vacm) {

		vacm.addGroup(SecurityModel.SECURITY_MODEL_SNMPv2c, new OctetString("c" + this.communityString),
				new OctetString("v1v2group"), StorageType.nonVolatile);

		vacm.addAccess(new OctetString("v1v2group"), new OctetString(this.communityString),
				SecurityModel.SECURITY_MODEL_ANY, SecurityLevel.NOAUTH_NOPRIV, MutableVACM.VACM_MATCH_EXACT,
				new OctetString("fullReadView"), new OctetString("fullWriteView"), new OctetString("fullNotifyView"),
				StorageType.nonVolatile);

		vacm.addViewTreeFamily(new OctetString("fullReadView"), new OID("1.3.6"), new OctetString(),
				VacmMIB.vacmViewIncluded, StorageType.nonVolatile);
		
		vacm.addViewTreeFamily(new OctetString("fullReadView"), new OID("1.3.6.1.3.99.3.4"), new OctetString(),
				VacmMIB.vacmViewExcluded, StorageType.nonVolatile);

		vacm.addViewTreeFamily(new OctetString("fullWriteView"), new OID("1.3.6.1.3.99.3.4"), new OctetString(),
				VacmMIB.vacmViewIncluded, StorageType.nonVolatile);


	}
	
	/**
	 * 
	 */
	@Override
	protected void unregisterManagedObjects() {
	
		
	}

	/**
	 * 
	 */
	protected void addUsmUser(USM usm) {
	}

	/**
	 * 
	 */
	protected void initTransportMappings() throws IOException {
		transportMappings = new TransportMapping<?>[1];
		Address addr = GenericAddress.parse(address);
		TransportMapping<?> tm = TransportMappings.getInstance().createTransportMapping(addr);
		transportMappings[0] = tm;
	}

	/**
	 * 
	 * @throws IOException
	 */
	public void start() throws IOException {

		init();
		
		addShutdownHook();
		getServer().addContext(new OctetString(this.communityString));
		finishInit();
		run();
		sendColdStartNotification();
	}

	
	/**
	 * 
	 */
	protected void addCommunities(SnmpCommunityMIB communityMIB) {
		Variable[] com2sec = new Variable[] { new OctetString(this.communityString), // community
				// name
				new OctetString("c" + this.communityString), // security
																// name
				getAgent().getContextEngineID(), // local engine ID
				new OctetString(this.communityString), // default
														// context
														// name
				new OctetString(), // transport tag
				new Integer32(StorageType.nonVolatile), // storage type
				new Integer32(RowStatus.active) // row status
		};

		communityMIB.getSnmpCommunityEntry().addRow(communityMIB.getSnmpCommunityEntry().createRow(
				new OctetString(this.communityString + "2" + this.communityString).toSubIndex(true), com2sec));
	}

	public static void main(String[] args) throws IOException, InterruptedException {

		AgentHelper agentHelper = new AgentHelper(args[1], args[0]);

		String address = "0.0.0.0/" + agentHelper.getUdpPort();
		Agent agent = new Agent(address, agentHelper.getCommunityString());
		agent.start();

		
		agent.unregisterManagedObject(agent.getSnmpv2MIB());

		ArrayList<String> seed = agentHelper.loadSeed();

		MOTableBuilder moTableBuilder = new MOTableBuilder(DefaultMOFactory.getInstance());
		for (String string : seed) {
			moTableBuilder.addRowValue((Variable) new OctetString(string));
		}

		moTableBuilder.build();

		agent.registerManagedObject(moTableBuilder);

		MOScalarFactory moScalarFactory = new MOScalarFactory(DefaultMOFactory.getInstance(),
				agentHelper.getFrequencyR(), agentHelper.getEntryN(), agentHelper.getNumberD(),
				agentHelper.getResetKey(), agentHelper, agent, moTableBuilder);

		agent.registerManagedObject(moScalarFactory);

		

		while (true) {
			System.out.println("Agente em funcionamento...");
			agentHelper.refresh(moTableBuilder);
			Thread.sleep(agentHelper.getWaitingTime());
			agentHelper.setWaitingTime(0);
			Thread.sleep(agentHelper.getRefreshingTime());
		}
	}

	
}
