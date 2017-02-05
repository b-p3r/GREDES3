package di.uminho.miei.gredes;

import org.snmp4j.agent.DuplicateRegistrationException;
import org.snmp4j.agent.MOGroup;
import org.snmp4j.agent.MOServer;
import org.snmp4j.agent.mo.MOAccessImpl;
import org.snmp4j.agent.mo.MOFactory;
import org.snmp4j.agent.mo.MOScalar;
import org.snmp4j.smi.Integer32;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.Variable;

public class MOScalarFactory implements MOGroup{

	// Scalars
	private MOScalar<Integer32> paramR;
	private MOScalar<Integer32> paramN;
	private MOScalar<Integer32> paramD;
	private MOScalar<OctetString> paramAuthReset;

	public MOScalarFactory(MOFactory moFactory, int paramR, int paramN, int paramD, String paramAuthReset) {

		createMOGroup(moFactory, paramR, paramN, paramD, paramAuthReset);
	}

	public void createMOGroup(MOFactory moFactory, int paramR, int paramN, int paramD, String paramAuthReset) {

		this.paramR = moFactory.createScalar(UminhoGrMib.oidParamR,
				moFactory.createAccess(MOAccessImpl.ACCESSIBLE_FOR_READ_ONLY), (Integer32) getVariable(paramR));
		this.paramN = moFactory.createScalar(UminhoGrMib.oidParamN,
				moFactory.createAccess(MOAccessImpl.ACCESSIBLE_FOR_READ_ONLY), (Integer32) getVariable(paramN));
		this.paramD = moFactory.createScalar(UminhoGrMib.oidParamD,
				moFactory.createAccess(MOAccessImpl.ACCESSIBLE_FOR_READ_ONLY), (Integer32) getVariable(paramD));
		this.paramAuthReset = new ParamAuthReset(UminhoGrMib.oidParamAuthReset,
				moFactory.createAccess(MOAccessImpl.ACCESSIBLE_FOR_READ_WRITE));
		this.paramAuthReset.addMOValueValidationListener(new ParamAuthResetValidator());
		this.paramAuthReset.setValue((OctetString) getVariable(paramAuthReset));

		System.out.println("create");

	}

	public void createGroup(MOFactory moFactory) {

		paramR = moFactory.createScalar(UminhoGrMib.oidParamR,
				moFactory.createAccess(MOAccessImpl.ACCESSIBLE_FOR_READ_ONLY), new Integer32());
		paramN = moFactory.createScalar(UminhoGrMib.oidParamN,
				moFactory.createAccess(MOAccessImpl.ACCESSIBLE_FOR_READ_ONLY), new Integer32());
		paramD = moFactory.createScalar(UminhoGrMib.oidParamD,
				moFactory.createAccess(MOAccessImpl.ACCESSIBLE_FOR_READ_ONLY), new Integer32());
		paramAuthReset = new ParamAuthReset(UminhoGrMib.oidParamAuthReset,
				moFactory.createAccess(MOAccessImpl.ACCESSIBLE_FOR_READ_WRITE));
		paramAuthReset.addMOValueValidationListener(new ParamAuthResetValidator());

	}

	 public static MOScalar<Variable> createReadOnly(OID oid, Object value) {
	 return new MOScalar<Variable>(oid, MOAccessImpl.ACCESS_READ_ONLY,
	 getVariable(value));
	 }
	
	private static Variable getVariable(Object value) {
		if (value instanceof String) {
			return new OctetString((String) value);
		} else if (value instanceof Integer)
			return new Integer32((Integer) value);
		throw new IllegalArgumentException("Unmanaged Type: " + value.getClass());
	}

	public MOScalar<Integer32> getParamR() {
		return paramR;
	}

	public MOScalar<Integer32> getParamN() {
		return paramN;
	}

	public MOScalar<Integer32> getParamD() {
		return paramD;
	}

	public MOScalar<OctetString> getParamAuthReset() {
		return paramAuthReset;
	}

	@Override
	public void registerMOs(MOServer server, OctetString context) throws DuplicateRegistrationException {
		server.register(this.paramR, context);
		server.register(this.paramN, context);
		server.register(this.paramD, context);
		server.register(this.paramAuthReset, context);

		
	}

	@Override
	public void unregisterMOs(MOServer server, OctetString context) {
		server.unregister(this.paramR, context);
		server.unregister(this.paramN, context);
		server.unregister(this.paramD, context);
		server.unregister(this.paramAuthReset, context);
		
	}

}
