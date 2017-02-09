package di.uminho.miei.gredes.tables;

import java.util.ArrayList;
import java.util.List;

import org.snmp4j.agent.DuplicateRegistrationException;
import org.snmp4j.agent.MOGroup;
import org.snmp4j.agent.MOServer;
import org.snmp4j.agent.mo.MOAccessImpl;
import org.snmp4j.agent.mo.MOColumn;
import org.snmp4j.agent.mo.MOFactory;
import org.snmp4j.agent.mo.MOMutableColumn;
import org.snmp4j.agent.mo.MOMutableTableModel;
import org.snmp4j.agent.mo.MOTable;
import org.snmp4j.agent.mo.MOTableIndex;
import org.snmp4j.agent.mo.MOTableIndexValidator;
import org.snmp4j.agent.mo.MOTableModel;
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

import di.uminho.miei.gredes.UminhoGrMib;

/**
 * 
 * @author bpereira
 *
 */
@SuppressWarnings("rawtypes")
public class MOTableBuilder implements MOGroup{

	private MOTableSubIndex[] unpredictableTableEntryIndexes;
	private MOTableIndex unpredictableTableEntryIndex;


	private MOTable<UnpredictableTableEntryRow, MOColumn, MOTableModel<UnpredictableTableEntryRow>> unpredictableTableEntry;
	private MOTableModel<UnpredictableTableEntryRow> unpredictableTableEntryModel;

	// Column sub-identifier definitions for unpredictableTableEntry:
	public static final int colIndexRandHexNumber = 1;
	public static final int colRandomHexadecimalNumber = 2;

	// Column index definitions for unpredictableTableEntry:
	public static final int idxIndexRandHexNumber = 0;
	public static final int idxRandomHexadecimalNumber = 1;

	private final List<Variable[]> tableRows = new ArrayList<Variable[]>();
	private int currentRow = 0;
	private MOFactory moFactory;

	/**
	 * Specified oid is the root oid of this table
	 * 
	 * @param moFactory
	 */
	public MOTableBuilder(MOFactory moFactory) {
		this.moFactory = moFactory;
	}

	public void addRowValue(Variable variable) {
		
		tableRows.add(new Variable[2]);
		tableRows.get(currentRow)[UminhoGrMib.idxIndexRandHexNumber] = new Integer32(currentRow+1);
		tableRows.get(currentRow)[UminhoGrMib.idxRandomHexadecimalNumber] = variable;

		currentRow++;

	}
	
	

	public void build() {
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

		// Columns1,3,6,1,3,99,3,1,0
		MOColumn<?>[] unpredictableTableEntryColumns = new MOColumn[2];
		unpredictableTableEntryColumns[idxIndexRandHexNumber] = moFactory.createColumn(colIndexRandHexNumber,
				SMIConstants.SYNTAX_INTEGER, moFactory.createAccess(MOAccessImpl.ACCESSIBLE_FOR_READ_ONLY));

		DisplayString<OctetString> displayString = new DisplayString<OctetString>(colRandomHexadecimalNumber,
				moFactory.createAccess(MOAccessImpl.ACCESSIBLE_FOR_READ_WRITE), (OctetString) null);

		unpredictableTableEntryColumns[idxRandomHexadecimalNumber] = displayString;

		ValueConstraint randomHexadecimalNumberVC = new ConstraintsImpl();
		((ConstraintsImpl) randomHexadecimalNumberVC).add(new Constraint(0L, 255L));
		((MOMutableColumn<?>) unpredictableTableEntryColumns[idxRandomHexadecimalNumber])
				.addMOValueValidationListener(new ValueConstraintValidator(randomHexadecimalNumberVC));
		((MOMutableColumn<?>) unpredictableTableEntryColumns[idxRandomHexadecimalNumber])
				.addMOValueValidationListener(new RandomHexadecimalNumberValidator());
		// Table model
		unpredictableTableEntryModel = moFactory.createTableModel(UminhoGrMib.oidUnpredictableTableEntry,
				unpredictableTableEntryIndex, unpredictableTableEntryColumns);
		((MOMutableTableModel<UnpredictableTableEntryRow>) unpredictableTableEntryModel)
				.setRowFactory(new UnpredictableTableEntryRowFactory());
		unpredictableTableEntry = moFactory.createTable(UminhoGrMib.oidUnpredictableTableEntry,
				unpredictableTableEntryIndex, unpredictableTableEntryColumns, unpredictableTableEntryModel);

		int i = 1;

		for (Variable[] variables : tableRows) {
			unpredictableTableEntry.addRow(new UnpredictableTableEntryRow(new OID(String.valueOf(i)), variables));

			i++;
		}
		
		currentRow = 0;

	}
	
	

	public MOTable<UnpredictableTableEntryRow,MOColumn,MOTableModel<UnpredictableTableEntryRow>> getUnpredictableTableEntry() {
	    return unpredictableTableEntry;
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

	@Override
	public void registerMOs(MOServer server, OctetString context) throws DuplicateRegistrationException {
		server.register(this.unpredictableTableEntry, context);
		
	}

	@Override
	public void unregisterMOs(MOServer server, OctetString context) {
		server.unregister(this.unpredictableTableEntry, context);
		
	}

}
