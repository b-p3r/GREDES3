
package di.uminho.miei.gredes;

import org.snmp4j.smi.OID;

public class UminhoGrMib

{

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
	public static final OID oidUnpredictableTableEntry = new OID(new int[] { 1, 3, 6, 1, 3, 99, 5, 1 });

	// Index OID definitions
	public static final OID oidIndexRandHexNumber = new OID(new int[] { 1, 3, 6, 1, 3, 99, 5, 1, 1 });

}
