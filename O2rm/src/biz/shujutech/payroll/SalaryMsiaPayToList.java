package biz.shujutech.payroll;

import biz.shujutech.base.Connection;
import biz.shujutech.db.object.Clasz;
import biz.shujutech.db.object.Lookup;
import biz.shujutech.db.object.ObjectBase;
import biz.shujutech.db.relational.FieldType;
import biz.shujutech.db.relational.SortOrder;
import biz.shujutech.reflect.ReflectField;
import biz.shujutech.reflect.ReflectIndex;
import java.util.concurrent.CopyOnWriteArrayList;

public class SalaryMsiaPayToList extends Clasz implements Lookup {
	@ReflectField(type=FieldType.STRING, size=16, indexes={@ReflectIndex(indexName="idx_smptl_descr", indexNo=0, indexOrder=SortOrder.ASC, isUnique=true)}) public static String Descr;
	public static CopyOnWriteArrayList<Lookup> LookupList = new CopyOnWriteArrayList<>();

	public static String LHDNDescr = "LHDN";
	public static SalaryMsiaPayToList LHDN = null;

	public static String EPFDescr = "EPF";
	public static SalaryMsiaPayToList EPF = null;

	public static String SOCSODescr = "SOCSO";
	public static SalaryMsiaPayToList SOCSO = null;

	public static String EISDescr = "EIS";
	public static SalaryMsiaPayToList EIS = null;

	public static String EmployeRDescr = "EmployeR";
	public static SalaryMsiaPayToList EmployeR = null;


	@Override
	public String getDescr() throws Exception {
		return(this.getValueStr(Descr));
	}

	@Override
	public void setDescr(String aDescr) throws Exception {
		this.setValueStr(Descr, aDescr);
	}

	@Override
	public CopyOnWriteArrayList<Lookup> getLookupList() throws Exception {
		return(LookupList);
	}

	@Override
	public void initialize(Connection aConn) throws Exception {
		// do nothing
	}

	@Override
	public String getValueStr() throws Exception { // this is also needed to be abstract actually
		return(this.getDescr());
	}
	
	public static void InitList(Connection aConn) throws Exception {
		ObjectBase.CreateObject(aConn, SalaryMsiaPayToList.class); // check if table already exist, if not create it
		Lookup.ClearAndLoadList(aConn, SalaryMsiaPayToList.class, LookupList);
		LHDN = (SalaryMsiaPayToList) Lookup.InsertDefaultList(aConn, LHDN, SalaryMsiaPayToList.class, LHDNDescr, LookupList);
		EPF = (SalaryMsiaPayToList) Lookup.InsertDefaultList(aConn, EPF, SalaryMsiaPayToList.class, EPFDescr, LookupList);
		SOCSO = (SalaryMsiaPayToList) Lookup.InsertDefaultList(aConn, SOCSO, SalaryMsiaPayToList.class, SOCSODescr, LookupList);
		EIS = (SalaryMsiaPayToList) Lookup.InsertDefaultList(aConn, EIS, SalaryMsiaPayToList.class, EISDescr, LookupList);
		EmployeR = (SalaryMsiaPayToList) Lookup.InsertDefaultList(aConn, EmployeR, SalaryMsiaPayToList.class, EmployeRDescr, LookupList);
	}
}
