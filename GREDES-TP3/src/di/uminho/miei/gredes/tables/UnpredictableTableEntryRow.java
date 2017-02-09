package di.uminho.miei.gredes.tables;

import org.snmp4j.agent.mo.DefaultMOMutableRow2PC;
import org.snmp4j.smi.Integer32;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.Variable;

public class UnpredictableTableEntryRow extends DefaultMOMutableRow2PC {

	

	public UnpredictableTableEntryRow(OID index, Variable[] values) {
		super(index, values);
		
	}

	public Integer32 getIndexRandHexNumber() {
		
		return (Integer32) super.getValue(MOTableBuilder.idxIndexRandHexNumber);
	}

	public void setIndexRandHexNumber(Integer32 newColValue) {
		
		super.setValue(MOTableBuilder.idxIndexRandHexNumber, newColValue);
	}

	public OctetString getRandomHexadecimalNumber() {
		return (OctetString) super.getValue(MOTableBuilder.idxRandomHexadecimalNumber);
	}

	public void setRandomHexadecimalNumber(OctetString newColValue) {
	
		super.setValue(MOTableBuilder.idxRandomHexadecimalNumber, newColValue);
	}

	public Variable getValue(int column) {
		
		switch (column) {
		case MOTableBuilder.idxIndexRandHexNumber:
			return getIndexRandHexNumber();
		case MOTableBuilder.idxRandomHexadecimalNumber:
			return getRandomHexadecimalNumber();
		default:
			return super.getValue(column);
		}
	}

	public void setValue(int column, Variable value) {
		
		switch (column) {
		case MOTableBuilder.idxIndexRandHexNumber:
			setIndexRandHexNumber((Integer32) value);
			break;
		case MOTableBuilder.idxRandomHexadecimalNumber:
			setRandomHexadecimalNumber((OctetString) value);
			break;
		default:
			super.setValue(column, value);
		}
	}

	

}
