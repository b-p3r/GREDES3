package di.uminho.miei.gredes.scalars;

import org.snmp4j.agent.MOAccess;
import org.snmp4j.agent.mo.MOValueValidationEvent;
import org.snmp4j.agent.mo.MOValueValidationListener;
import org.snmp4j.agent.mo.snmp.DisplayStringScalar;
import org.snmp4j.agent.request.SubRequest;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.Variable;

public class ParamAuthReset extends DisplayStringScalar<OctetString> {

	ParamAuthReset(OID oid, MOAccess access) {
		super(oid, access, new OctetString(), 0, 255);

	}
	

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

	public OctetString getValue() {

		return super.getValue();
	}

	public int setValue(OctetString newValue) {

		return super.setValue(newValue);
	}


	/* (non-Javadoc)
	 * @see org.snmp4j.agent.mo.MOScalar#commit(org.snmp4j.agent.request.SubRequest)
	 */
	@Override
	public void commit(SubRequest request) {
		// TODO Auto-generated method stub
		
		Variable var = request.getRequest().get(0).getVariableBinding().getVariable();
		System.out.println("commit :"+ var.toString());
		//super.commit(request);
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
