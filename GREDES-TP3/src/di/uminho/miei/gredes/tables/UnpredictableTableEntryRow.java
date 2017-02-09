package di.uminho.miei.gredes.tables;

import org.snmp4j.agent.mo.DefaultMOMutableRow2PC;
import org.snmp4j.smi.Integer32;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.Variable;

import di.uminho.miei.gredes.UminhoGrMib;

public class UnpredictableTableEntryRow extends DefaultMOMutableRow2PC {

	

	public UnpredictableTableEntryRow(OID index, Variable[] values) {
		super(index, values);
		
	}

	public Integer32 getIndexRandHexNumber() {
		
		return (Integer32) super.getValue(UminhoGrMib.idxIndexRandHexNumber);
	}

	public void setIndexRandHexNumber(Integer32 newColValue) {
		
		super.setValue(UminhoGrMib.idxIndexRandHexNumber, newColValue);
	}

	public OctetString getRandomHexadecimalNumber() {
		return (OctetString) super.getValue(UminhoGrMib.idxRandomHexadecimalNumber);
	}

	public void setRandomHexadecimalNumber(OctetString newColValue) {
	
		super.setValue(UminhoGrMib.idxRandomHexadecimalNumber, newColValue);
	}

	public Variable getValue(int column) {
		
		switch (column) {
		case UminhoGrMib.idxIndexRandHexNumber:
			return getIndexRandHexNumber();
		case UminhoGrMib.idxRandomHexadecimalNumber:
			return getRandomHexadecimalNumber();
		default:
			return super.getValue(column);
		}
	}

	public void setValue(int column, Variable value) {
		
		switch (column) {
		case UminhoGrMib.idxIndexRandHexNumber:
			setIndexRandHexNumber((Integer32) value);
			break;
		case UminhoGrMib.idxRandomHexadecimalNumber:
			setRandomHexadecimalNumber((OctetString) value);
			break;
		default:
			super.setValue(column, value);
		}
	}

	

}
