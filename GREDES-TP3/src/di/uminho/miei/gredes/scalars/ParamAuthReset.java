package di.uminho.miei.gredes.scalars;

import org.snmp4j.PDU;
import org.snmp4j.agent.MOAccess;
import org.snmp4j.agent.mo.MOValueValidationEvent;
import org.snmp4j.agent.mo.MOValueValidationListener;
import org.snmp4j.agent.mo.snmp.DisplayStringScalar;
import org.snmp4j.agent.request.RequestStatus;
import org.snmp4j.agent.request.SubRequest;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.Variable;

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
public class ParamAuthReset extends DisplayStringScalar<OctetString> {

	private AgentHelper agentHelper;
	private MOTableBuilder moTableBuilder;
	private Agent agent;

	/**
	 * 
	 * @param oid
	 * @param access
	 * @param agentHelper
	 * @param agent
	 * @param moTableBuilder
	 */
	ParamAuthReset(OID oid, MOAccess access, AgentHelper agentHelper, Agent agent, MOTableBuilder moTableBuilder) {
		super(oid, access, new OctetString(), 0, 255);
		this.agentHelper = agentHelper;
		this.moTableBuilder = moTableBuilder;
		this.agent = agent;

	}

	/**
	 * 
	 */
	public int isValueOK(SubRequest request) {
		Variable newValue = request.getVariableBinding().getVariable();
		int valueOK = super.isValueOK(request);
		if (valueOK != SnmpConstants.SNMP_ERROR_SUCCESS) {
			return valueOK;
		}
		OctetString os = (OctetString) newValue;
		if (!(((os.length() >= 0) && (os.length() <= 255)))) {
			valueOK = SnmpConstants.SNMP_ERROR_WRONG_LENGTH;
		}

		return valueOK;
	}

	/**
	 * 
	 */
	public OctetString getValue() {

		return super.getValue();
	}

	/**
	 * 
	 */
	public int setValue(OctetString newValue) {
		return super.setValue(newValue);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.snmp4j.agent.mo.MOScalar#prepare(org.snmp4j.agent.request.SubRequest)
	 */
	@Override
	public void prepare(SubRequest request) {
		super.prepare(request);
		RequestStatus status = request.getStatus();
		if (status.getErrorStatus() == 0) {
			String key = agentHelper.getResetKey();
			Variable var = request.getRequest().get(0).getVariableBinding().getVariable();
			String receivedKey = var.toString();

			System.out.println("RECEIVED KEY:\t" + receivedKey);
			System.out.println("CONFIG   KEY:\t" + key);

			if (!key.equals(receivedKey))
				status.setErrorStatus(PDU.wrongValue);

		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.snmp4j.agent.mo.MOScalar#commit(org.snmp4j.agent.request.SubRequest)
	 */
	@Override
	public void commit(SubRequest request) {
		super.commit(request);

		System.out.println("RESETING .....");

		request.setErrorStatus(0);

		new Runnable() {

			public void run() {
				agentHelper.reset(moTableBuilder, agent);

			}
		}.run();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.snmp4j.agent.mo.MOScalar#cleanup(org.snmp4j.agent.request.SubRequest)
	 */
	@Override
	public void cleanup(SubRequest request) {
		super.cleanup(request);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.snmp4j.agent.mo.MOScalar#undo(org.snmp4j.agent.request.SubRequest)
	 */
	@Override
	public void undo(SubRequest request) {
		super.undo(request);

	}

}

// Value Validators
/**
 * The <code>ParamAuthResetValidator</code> implements the value validation for
 * <code>ParamAuthReset</code>.
 */
class ParamAuthResetValidator implements MOValueValidationListener {

	public void validate(MOValueValidationEvent validationEvent) {
		Variable newValue = validationEvent.getNewValue();
		OctetString os = (OctetString) newValue;
		if (!(((os.length() >= 0) && (os.length() <= 255)))) {
			validationEvent.setValidationStatus(SnmpConstants.SNMP_ERROR_WRONG_LENGTH);
			return;
		}
	}
}
