/**
 * 
 */
package di.uminho.miei.gredes;

import org.snmp4j.agent.mo.DefaultMOMutableRow2PC;
import org.snmp4j.agent.mo.MOAccessImpl;
import org.snmp4j.agent.mo.MOColumn;
import org.snmp4j.agent.mo.MOFactory;
import org.snmp4j.agent.mo.MOMutableColumn;
import org.snmp4j.agent.mo.MOMutableTableModel;
import org.snmp4j.agent.mo.MOTable;
import org.snmp4j.agent.mo.MOTableIndex;
import org.snmp4j.agent.mo.MOTableIndexValidator;
import org.snmp4j.agent.mo.MOTableModel;
import org.snmp4j.agent.mo.MOTableRowFactory;
import org.snmp4j.agent.mo.MOTableSubIndex;
import org.snmp4j.agent.mo.MOValueValidationEvent;
import org.snmp4j.agent.mo.MOValueValidationListener;
import org.snmp4j.agent.mo.snmp.DisplayString;
import org.snmp4j.agent.mo.snmp.smi.Constraint;
import org.snmp4j.agent.mo.snmp.smi.ConstraintsImpl;
import org.snmp4j.agent.mo.snmp.smi.ValueConstraint;
import org.snmp4j.agent.mo.snmp.smi.ValueConstraintValidator;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.Integer32;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.SMIConstants;
import org.snmp4j.smi.Variable;

/**
 * @author bpereira
 *
 */
public class UnpredictableTableEntryRow extends DefaultMOMutableRow2PC {

	private MOTableSubIndex[] unpredictableTableEntryIndexes;
	private MOTableIndex unpredictableTableEntryIndex;

	@SuppressWarnings("rawtypes")
	private MOTable<UnpredictableTableEntryRow, MOColumn, MOTableModel<UnpredictableTableEntryRow>> unpredictableTableEntry;
	private MOTableModel<UnpredictableTableEntryRow> unpredictableTableEntryModel;

	/**
	 * @param index
	 * @param values
	 */
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

	@SuppressWarnings("rawtypes")
	public MOTable<UnpredictableTableEntryRow, MOColumn, MOTableModel<UnpredictableTableEntryRow>> getUnpredictableTableEntry() {
		return unpredictableTableEntry;
	}

	public void createUnpredictableTableEntry(MOFactory moFactory) {
		// Index definition
		unpredictableTableEntryIndexes = new MOTableSubIndex[] {
				moFactory.createSubIndex(UminhoGrMib.oidIndexRandHexNumber, SMIConstants.SYNTAX_INTEGER, 1, 1) };

		unpredictableTableEntryIndex = moFactory.createIndex(unpredictableTableEntryIndexes, false,
				new MOTableIndexValidator() {
					public boolean isValidIndex(OID index) {
						boolean isValidIndex = true;
						return isValidIndex;
					}
				});

		// Columns
		MOColumn<?>[] unpredictableTableEntryColumns = new MOColumn[2];
		unpredictableTableEntryColumns[UminhoGrMib.idxIndexRandHexNumber] = moFactory.createColumn(
				UminhoGrMib.colIndexRandHexNumber, SMIConstants.SYNTAX_INTEGER,
				moFactory.createAccess(MOAccessImpl.ACCESSIBLE_FOR_READ_ONLY));
		unpredictableTableEntryColumns[UminhoGrMib.idxRandomHexadecimalNumber] = new DisplayString<OctetString>(
				UminhoGrMib.colRandomHexadecimalNumber, moFactory.createAccess(MOAccessImpl.ACCESSIBLE_FOR_READ_WRITE),
				(OctetString) null);
		ValueConstraint randomHexadecimalNumberVC = new ConstraintsImpl();
		((ConstraintsImpl) randomHexadecimalNumberVC).add(new Constraint(0L, 255L));
		((MOMutableColumn<?>) unpredictableTableEntryColumns[UminhoGrMib.idxRandomHexadecimalNumber])
				.addMOValueValidationListener(new ValueConstraintValidator(randomHexadecimalNumberVC));
		((MOMutableColumn<?>) unpredictableTableEntryColumns[UminhoGrMib.idxRandomHexadecimalNumber])
				.addMOValueValidationListener(new RandomHexadecimalNumberValidator());
		// Table model
		unpredictableTableEntryModel = moFactory.createTableModel(UminhoGrMib.oidUnpredictableTableEntry,
				unpredictableTableEntryIndex, unpredictableTableEntryColumns);
		((MOMutableTableModel<UnpredictableTableEntryRow>) unpredictableTableEntryModel)
				.setRowFactory(new UnpredictableTableEntryRowFactory());
		unpredictableTableEntry = moFactory.createTable(UminhoGrMib.oidUnpredictableTableEntry,
				unpredictableTableEntryIndex, unpredictableTableEntryColumns, unpredictableTableEntryModel);
	}

	class UnpredictableTableEntryRowFactory implements MOTableRowFactory<UnpredictableTableEntryRow> {
		public synchronized UnpredictableTableEntryRow createRow(OID index, Variable[] values)
				throws UnsupportedOperationException {
			UnpredictableTableEntryRow row = new UnpredictableTableEntryRow(index, values);

			return row;
		}

		public synchronized void freeRow(UnpredictableTableEntryRow row) {

		}

	}

	/**
	 * The <code>RandomHexadecimalNumberValidator</code> implements the value
	 * validation for <code>RandomHexadecimalNumber</code>.
	 */
	static class RandomHexadecimalNumberValidator implements MOValueValidationListener {

		public void validate(MOValueValidationEvent validationEvent) {
			Variable newValue = validationEvent.getNewValue();
			OctetString os = (OctetString) newValue;
			if (!(((os.length() >= 0) && (os.length() <= 255)))) {
				validationEvent.setValidationStatus(SnmpConstants.SNMP_ERROR_WRONG_LENGTH);
				return;
			}
			
		}
	}

}
