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

public class Agent extends BaseAgent {

	static Agent agent;

	private String address;

	public Agent(String address) throws IOException {

		// These files does not exist and are not used but has to be specified
		// Read snmp4j docs for more info
		super(new File("conf.agent"), new File("bootCounter.agent"),
				new CommandProcessor(new OctetString(MPv3.createLocalEngineID())));
		this.address = address;
	}

	/**
	 * We let clients of this agent register the MO they need so this method
	 * does nothing
	 */
	@Override
	protected void registerManagedObjects() {
	}

	/**
	 * Clients can register the MO they need
	 */
	public void registerManagedObject(ManagedObject mo) {
		try {
			server.register(mo, null);
		} catch (DuplicateRegistrationException ex) {
			throw new RuntimeException(ex);
		}
	}

	public void registerManagedObject(MOGroup moGroup) {
		try {
			moGroup.registerMOs(server, null);
		} catch (DuplicateRegistrationException ex) {
			throw new RuntimeException(ex);
		}
	}

	public void unregisterManagedObject(MOGroup moGroup) {
		moGroup.unregisterMOs(server, getContext(moGroup));
	}

	/*
	 * Empty implementation
	 */
	@Override
	protected void addNotificationTargets(SnmpTargetMIB targetMIB, SnmpNotificationMIB notificationMIB) {
	}

	/**
	 * Minimal View based Access Control
	 * 
	 * http://www.faqs.org/rfcs/rfc2575.html
	 */
	@Override
	protected void addViews(VacmMIB vacm) {

		vacm.addGroup(SecurityModel.SECURITY_MODEL_SNMPv2c, new OctetString("cunpredictable"),
				new OctetString("v1v2group"), StorageType.nonVolatile);

		vacm.addAccess(new OctetString("v1v2group"), new OctetString("unpredictable"), SecurityModel.SECURITY_MODEL_ANY,
				SecurityLevel.NOAUTH_NOPRIV, MutableVACM.VACM_MATCH_EXACT, new OctetString("fullReadView"),
				new OctetString("fullWriteView"), new OctetString("fullNotifyView"), StorageType.nonVolatile);

		vacm.addViewTreeFamily(new OctetString("fullReadView"), new OID("1.3.6.1.3.99.5"), new OctetString(),
				VacmMIB.vacmViewIncluded, StorageType.nonVolatile);

		vacm.addViewTreeFamily(new OctetString("fullWriteView"), new OID("1.3.6.1.3.99.3.4"), new OctetString(),
				VacmMIB.vacmViewIncluded, StorageType.nonVolatile);

		vacm.addViewTreeFamily(new OctetString("fullReadView"), new OID("1.3.6.1.3.99.3.1"), new OctetString(),
				VacmMIB.vacmViewIncluded, StorageType.nonVolatile);
		vacm.addViewTreeFamily(new OctetString("fullReadView"), new OID("1.3.6.1.3.99.3.2"), new OctetString(),
				VacmMIB.vacmViewIncluded, StorageType.nonVolatile);
		vacm.addViewTreeFamily(new OctetString("fullReadView"), new OID("1.3.6.1.3.99.3.3"), new OctetString(),
				VacmMIB.vacmViewIncluded, StorageType.nonVolatile);

	}

	/**
	 * User based Security Model, only applicable to SNMP v.3
	 * 
	 */
	protected void addUsmUser(USM usm) {
	}

	protected void initTransportMappings() throws IOException {
		transportMappings = new TransportMapping<?>[1];
		Address addr = GenericAddress.parse(address);
		TransportMapping<?> tm = TransportMappings.getInstance().createTransportMapping(addr);
		transportMappings[0] = tm;
	}

	/**
	 * Start method invokes some initialization methods needed to start the
	 * agent
	 * 
	 * @throws IOException
	 */
	public void start() throws IOException {

		init();
		// This method reads some old config from a file and causes
		// unexpected behavior.
		// loadConfig(ImportModes.REPLACE_CREATE);
		addShutdownHook();
		getServer().addContext(new OctetString("unpredictable"));
		finishInit();
		run();
		sendColdStartNotification();
	}

	protected void unregisterManagedObjects() {
		// here we should unregister those objects previously registered...
	}

	/**
	 * The table of community strings configured in the SNMP engine's Local
	 * Configuration Datastore (LCD).
	 * 
	 * We only configure one, "unpredictable".
	 */
	protected void addCommunities(SnmpCommunityMIB communityMIB) {
		Variable[] com2sec = new Variable[] { new OctetString("unpredictable"), // community
																				// name
				new OctetString("cunpredictable"), // security name
				getAgent().getContextEngineID(), // local engine ID
				new OctetString("unpredictable"), // default context name
				new OctetString(), // transport tag
				new Integer32(StorageType.nonVolatile), // storage type
				new Integer32(RowStatus.active) // row status
		};

		communityMIB.getSnmpCommunityEntry().addRow(communityMIB.getSnmpCommunityEntry()
				.createRow(new OctetString("unpredictable2unpredictable").toSubIndex(true), com2sec));
	}

	public static void main(String[] args) throws IOException, InterruptedException {

		AgentHelper agentHelper = new AgentHelper("resources/unpredictable-conf.txt", args[0]);

		String address = "0.0.0.0/" + agentHelper.getUdpPort();
		Agent agent = new Agent(address);
		agent.start();

		// Since BaseAgent registers some mibs by default we need to unregister
		// one before we register our own sysDescr. Normally you would
		// override that method and register the mibs that you need
		agent.unregisterManagedObject(agent.getSnmpv2MIB());

		MOScalarFactory moScalarFactory = new MOScalarFactory(DefaultMOFactory.getInstance(),
				agentHelper.getFrequencyR(), agentHelper.getEntryN(), agentHelper.getNumberD(),
				agentHelper.getResetKey());

		agent.registerManagedObject(moScalarFactory);

		ArrayList<String> seed = agentHelper.loadSeed();

		MOTableBuilder moTableBuilder = new MOTableBuilder(DefaultMOFactory.getInstance());
		for (String string : seed) {
			moTableBuilder.addRowValue((Variable) new OctetString(string));
		}

		moTableBuilder.build();

		agent.registerManagedObject(moTableBuilder);

		while (true) {
			System.out.println("Agent running...");
			Thread.sleep(5000);
		}
	}
}
