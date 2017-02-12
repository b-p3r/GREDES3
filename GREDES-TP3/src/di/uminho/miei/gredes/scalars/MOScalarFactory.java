package di.uminho.miei.gredes.scalars;

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

import di.uminho.miei.gredes.UminhoGrMib;
import di.uminho.miei.gredes.agent.Agent;
import di.uminho.miei.gredes.agent.AgentHelper;
import di.uminho.miei.gredes.tables.MOTableBuilder;


/**
 * 
 * @author Bruno Pereira
 * 
 * @date 2017 
 *
 */
public class MOScalarFactory implements MOGroup {

	// Scalars
	private MOScalar<Integer32> paramR;
	private MOScalar<Integer32> paramN;
	private MOScalar<Integer32> paramD;
	private MOScalar<OctetString> paramAuthReset;


	/**
	 * 
	 * @param moFactory
	 * @param paramR
	 * @param paramN
	 * @param paramD
	 * @param paramAuthReset
	 * @param agentHelper
	 * @param agent
	 * @param moTableBuilder
	 */
	public MOScalarFactory(MOFactory moFactory, int paramR, int paramN, int paramD, String paramAuthReset,
			AgentHelper agentHelper, Agent agent, MOTableBuilder moTableBuilder) {

		createMOGroup(moFactory, paramR, paramN, paramD, paramAuthReset, agentHelper, agent, moTableBuilder);

	}

	/**
	 * 
	 * @param moFactory
	 * @param paramR
	 * @param paramN
	 * @param paramD
	 * @param paramAuthReset
	 * @param agentHelper
	 * @param agent
	 * @param moTableBuilder
	 */
	public void createMOGroup(MOFactory moFactory, int paramR, int paramN, int paramD, String paramAuthReset, AgentHelper agentHelper, Agent agent, MOTableBuilder moTableBuilder) {

		this.paramR = moFactory.createScalar(UminhoGrMib.oidParamR,
				moFactory.createAccess(MOAccessImpl.ACCESSIBLE_FOR_READ_ONLY), (Integer32) getVariable(paramR));
		this.paramN = moFactory.createScalar(UminhoGrMib.oidParamN,
				moFactory.createAccess(MOAccessImpl.ACCESSIBLE_FOR_READ_ONLY), (Integer32) getVariable(paramN));
		this.paramD = moFactory.createScalar(UminhoGrMib.oidParamD,
				moFactory.createAccess(MOAccessImpl.ACCESSIBLE_FOR_READ_ONLY), (Integer32) getVariable(paramD));
		this.paramAuthReset = new ParamAuthReset(UminhoGrMib.oidParamAuthReset,
				moFactory.createAccess(MOAccessImpl.ACCESSIBLE_FOR_WRITE), agentHelper, agent,
				moTableBuilder);
		this.paramAuthReset.addMOValueValidationListener(new ParamAuthResetValidator());
		this.paramAuthReset.setValue((OctetString) getVariable(paramAuthReset));

	}

	

	/**
	 * 
	 * @param oid
	 * @param value
	 * @return
	 */
	public static MOScalar<Variable> createReadOnly(OID oid, Object value) {
		return new MOScalar<Variable>(oid, MOAccessImpl.ACCESS_READ_ONLY, getVariable(value));
	}

	/**
	 * 
	 * @param value
	 * @return
	 */
	private static Variable getVariable(Object value) {
		if (value instanceof String) {
			return new OctetString((String) value);
		} else if (value instanceof Integer)
			return new Integer32((Integer) value);
		throw new IllegalArgumentException("Unmanaged Type: " + value.getClass());
	}

	/**
	 * 
	 * @return
	 */
	public MOScalar<Integer32> getParamR() {
		return paramR;
	}

	/**
	 * 
	 * @return
	 */
	public MOScalar<Integer32> getParamN() {
		return paramN;
	}

	/**
	 * 
	 * @return
	 */
	public MOScalar<Integer32> getParamD() {
		return paramD;
	}

	/**
	 * 
	 * @return
	 */
	public MOScalar<OctetString> getParamAuthReset() {
		return paramAuthReset;
	}

	/**
	 * 
	 */
	@Override
	public void registerMOs(MOServer server, OctetString context) throws DuplicateRegistrationException {
		server.register(this.paramR, context);
		server.register(this.paramN, context);
		server.register(this.paramD, context);
		server.register(this.paramAuthReset, context);

	}

	/**
	 * 
	 */
	@Override
	public void unregisterMOs(MOServer server, OctetString context) {
		server.unregister(this.paramR, context);
		server.unregister(this.paramN, context);
		server.unregister(this.paramD, context);
		server.unregister(this.paramAuthReset, context);

	}

}
