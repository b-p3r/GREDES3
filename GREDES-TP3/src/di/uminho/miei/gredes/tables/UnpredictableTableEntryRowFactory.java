package di.uminho.miei.gredes.tables;

import org.snmp4j.agent.mo.MOTableRowFactory;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.Variable;

/**
 * 
 * @author Bruno Pereira
 * 
 * @date 2017 
 *
 */
public class UnpredictableTableEntryRowFactory implements MOTableRowFactory<UnpredictableTableEntryRow> {

	/**
	 * 
	 */
	public UnpredictableTableEntryRow createRow(OID index, Variable[] values) throws UnsupportedOperationException {
		UnpredictableTableEntryRow row = new UnpredictableTableEntryRow(index, values);
		return row;
	}


	/**
	 * 
	 */
	public void freeRow(UnpredictableTableEntryRow row) {
		
		// TODO Auto-generated method stub

	}

}
