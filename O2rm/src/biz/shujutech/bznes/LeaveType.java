package biz.shujutech.bznes;

import biz.shujutech.base.Connection;
import biz.shujutech.db.object.Clasz;
import biz.shujutech.db.object.ObjectBase;
import biz.shujutech.db.relational.FieldType;
import biz.shujutech.db.relational.SortOrder;
import java.util.concurrent.CopyOnWriteArrayList;
import biz.shujutech.reflect.ReflectField;
import biz.shujutech.reflect.ReflectIndex;
import biz.shujutech.db.object.Lookup;

public class LeaveType extends Clasz implements Lookup {
	@ReflectField(type=FieldType.STRING, size=16, indexes={@ReflectIndex(indexName="idx_lt_descr", indexNo=0, indexOrder=SortOrder.ASC, isUnique=true)}) public static String Descr;
	public static CopyOnWriteArrayList<Lookup> LookupList = new CopyOnWriteArrayList<>();

	public static String AnnualDescr = "Annual";
	public static LeaveType Annual = null;

	public static String MedicalDescr = "Medical";
	public static LeaveType Medical = null;

	public static String HospitalizedDescr = "Hospitalized";
	public static LeaveType Hospitalized = null;

	public static String MaternityDescr = "Maternity";
	public static LeaveType Maternity = null;

	public static String PaternityDescr = "Paternity";
	public static LeaveType Paternity = null;

	public static String CompassionateDescr = "Compassionate";
	public static LeaveType Compassionate = null;

	public static String EmergencyDescr = "Emergency";
	public static LeaveType Emergency = null;

	public static String ExamDescr = "Exam";
	public static LeaveType Exam = null;

	public static String MarriageDescr = "Marriage";
	public static LeaveType Marriage = null;

	public static String BroughtForwardDescr = "Brought Forward";
	public static LeaveType BroughtForward = null;

	public static String ReplacementDescr = "Replacement";
	public static LeaveType Replacement = null;

	public static String NoPayDescr = "Unpaid";
	public static LeaveType NoPay = null;

	@Override
	public void initialize(Connection aConn) throws Exception {
		// do nothing
	}

	@Override
	public String getDescr() throws Exception {
		return(this.getValueStr(Descr));
	}

	@Override
	public void setDescr(String aDescr) throws Exception {
		this.setValueStr(Descr, aDescr);
	}

	@Override
	public String getValueStr() throws Exception {
		return(this.getDescr());
	}

	@Override
	public CopyOnWriteArrayList<Lookup> getLookupList() {
		return(LookupList);
	}

	public static void InitList(Connection aConn) throws Exception {
		ObjectBase.CreateObject(aConn, LeaveType.class); // check if table already exist, if not create it
		Lookup.ClearAndLoadList(aConn, LeaveType.class, LookupList);
		Annual = (LeaveType) Lookup.InsertDefaultList(aConn, Annual, LeaveType.class, AnnualDescr, LookupList);
		Medical = (LeaveType) Lookup.InsertDefaultList(aConn, Medical, LeaveType.class, MedicalDescr, LookupList);
		Hospitalized = (LeaveType) Lookup.InsertDefaultList(aConn, Hospitalized, LeaveType.class, HospitalizedDescr, LookupList);
		Maternity = (LeaveType) Lookup.InsertDefaultList(aConn, Maternity, LeaveType.class, MaternityDescr, LookupList);
		Paternity = (LeaveType) Lookup.InsertDefaultList(aConn, Paternity, LeaveType.class, PaternityDescr, LookupList);
		Compassionate = (LeaveType) Lookup.InsertDefaultList(aConn, Compassionate, LeaveType.class, CompassionateDescr, LookupList);
		Emergency = (LeaveType) Lookup.InsertDefaultList(aConn, Emergency, LeaveType.class, EmergencyDescr, LookupList);
		Exam = (LeaveType) Lookup.InsertDefaultList(aConn, Exam, LeaveType.class, ExamDescr, LookupList);
		Marriage = (LeaveType) Lookup.InsertDefaultList(aConn, Marriage, LeaveType.class, MarriageDescr, LookupList);
		BroughtForward = (LeaveType) Lookup.InsertDefaultList(aConn, BroughtForward, LeaveType.class, BroughtForwardDescr, LookupList);
		Replacement = (LeaveType) Lookup.InsertDefaultList(aConn, Replacement, LeaveType.class, ReplacementDescr, LookupList);
		NoPay = (LeaveType) Lookup.InsertDefaultList(aConn, NoPay, LeaveType.class, NoPayDescr, LookupList);
	}

	public boolean isAnnualLeave() throws Exception {
		boolean result = false;
		if (this.getDescr().equals(AnnualDescr)) {
			result = true;
		}
		return(result);
	}
	

}
