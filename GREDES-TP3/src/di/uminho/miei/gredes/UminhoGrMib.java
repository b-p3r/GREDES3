
package di.uminho.miei.gredes;
//--AgentGen BEGIN=_BEGIN

import org.snmp4j.agent.mo.DefaultMOFactory;
import org.snmp4j.agent.mo.MOFactory;
import org.snmp4j.log.LogAdapter;
import org.snmp4j.log.LogFactory;

//--AgentGen END
import org.snmp4j.smi.OID;



public class UminhoGrMib

	//	implements MOGroup

{

	@SuppressWarnings("unused")
	private static final LogAdapter LOGGER = LogFactory.getLogger(UminhoGrMib.class);

	// Factory
	@SuppressWarnings("unused")
	private MOFactory moFactory = DefaultMOFactory.getInstance();

	// Constants

	/**
	 * OID of this MIB module for usage which can be used for its
	 * identification.
	 */
	public static final OID oidUminhoGrMib = new OID(new int[] { 1, 3, 6, 1, 3, 99 });

	// Identities
	// Scalars
	public static final OID oidParamR = new OID(new int[] { 1, 3, 6, 1, 3, 99, 3, 1, 0 });
	public static final OID oidParamN = new OID(new int[] { 1, 3, 6, 1, 3, 99, 3, 2, 0 });
	public static final OID oidParamD = new OID(new int[] { 1, 3, 6, 1, 3, 99, 3, 3, 0 });
	public static final OID oidParamAuthReset = new OID(new int[] { 1, 3, 6, 1, 3, 99, 3, 4, 0 });
	// Tables

	// Notifications

	// Enumerations

	

	

	// Tables
	public static final OID oidUnpredictableTableEntry = new OID(new int[] { 1, 3, 6, 1, 3, 99, 5, 1 });

	// Index OID definitions
	public static final OID oidIndexRandHexNumber = new OID(new int[] { 1, 3, 6, 1, 3, 99, 5, 1, 1 });


	// Column sub-identifier definitions for unpredictableTableEntry:
	public static final int colIndexRandHexNumber = 1;
	public static final int colRandomHexadecimalNumber = 2;

	// Column index definitions for unpredictableTableEntry:
	public static final int idxIndexRandHexNumber = 0;
	public static final int idxRandomHexadecimalNumber = 1;

	
	/**
	 * Constructs a UminhoGrMib instance without actually creating its
	 * <code>ManagedObject</code> instances. This has to be done in a sub-class
	 * constructor or after construction by calling
	 * {@link #createMO(MOFactory moFactory)}.
	 */
	protected UminhoGrMib() {
		
	}

	/**
	 * Constructs a UminhoGrMib instance and actually creates its
	 * <code>ManagedObject</code> instances using the supplied
	 * <code>MOFactory</code> (by calling {@link #createMO(MOFactory moFactory)}
	 * ).
	 * 
	 * @param moFactory
	 *            the <code>MOFactory</code> to be used to create the managed
	 *            objects for this module.
	 */
//	public UminhoGrMib(MOFactory moFactory) {
//		this();
//		createMO(moFactory);
//	}

	/**
	 * Create the ManagedObjects defined for this MIB module using the specified
	 * {@link MOFactory}.
	 * 
	 * @param moFactory
	 *            the <code>MOFactory</code> instance to use for object
	 *            creation.
	 */
//	protected void createMO(MOFactory moFactory) {
//		addTCsToFactory(moFactory);
//		
//		createUnpredictableTableEntry(moFactory);
//	}

	

	

//	public void registerMOs(MOServer server, OctetString context) throws DuplicateRegistrationException {
//		// Scalar Objects
//		server.register(this.paramR, context);
//		server.register(this.paramN, context);
//		server.register(this.paramD, context);
//		server.register(this.paramAuthReset, context);
//		server.register(this.unpredictableTableEntry, context);
//		
//	}
//
//	public void unregisterMOs(MOServer server, OctetString context) {
//		// Scalar Objects
//		server.unregister(this.paramR, context);
//		server.unregister(this.paramN, context);
//		server.unregister(this.paramD, context);
//		server.unregister(this.paramAuthReset, context);
//		server.unregister(this.unpredictableTableEntry, context);
//		
//	}

	// Notifications

	

	// Rows and Factories

	

	// Textual Definitions of MIB module UminhoGrMib
	protected void addTCsToFactory(MOFactory moFactory) {
	}

	// Textual Definitions of other MIB modules
	public void addImportedTCsToFactory(MOFactory moFactory) {
	}

}
