package biz.shujutech.bznes;

import biz.shujutech.base.Connection;
import biz.shujutech.base.Hinderance;
import biz.shujutech.db.object.Clasz;
import biz.shujutech.db.object.Lookup;
import biz.shujutech.db.object.ObjectBase;
import biz.shujutech.db.relational.FieldType;
import biz.shujutech.db.relational.SortOrder;
import biz.shujutech.reflect.ReflectField;
import biz.shujutech.reflect.ReflectIndex;
import java.util.concurrent.CopyOnWriteArrayList;

public class IdentityType extends Clasz implements Lookup {
	@ReflectField(type=FieldType.STRING, size=32, indexes={@ReflectIndex(indexName="idx_idtf_descr", indexNo=0, indexOrder=SortOrder.ASC, isUnique=true)}) public static String Descr;
	public static CopyOnWriteArrayList<Lookup> LookupList = new CopyOnWriteArrayList<>();

	public static String MalaysiaIcDescr = "Malaysia NRIC";
	public static IdentityType MalaysiaIc = null;

	public static String MalaysiaPrDescr = "Malaysia PR";
	public static IdentityType MalaysiaPr = null;

	public static String SingaporeIcDescr = "Singapore NRIC";
	public static IdentityType SingaporeIc = null;

	public static String SingaporePrDescr = "Singapore PR";
	public static IdentityType SingaporePr = null;

	public static String SingaporePassportDescr = "Singapore Passport";
	public static IdentityType SingaporePassport = null;

	public static String ChinaPassportDescr = "China Passport";
	public static IdentityType ChinaPassport = null;

	public static String IndonesiaPassportDescr = "Indonesia Passport";
	public static IdentityType IndonesiaPassport = null;

	public static String MyanmarPassportDescr = "Myanmar Passport";
	public static IdentityType MyanmarPassport = null;

	public static String VietnamPassportDescr = "Vietnam Passport";
	public static IdentityType VietnamPassport = null;

	public static String IndiaPassportDescr = "India Passport";
	public static IdentityType IndiaPassport = null;

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
	public String getValueStr() throws Exception {
		return(this.getDescr());
	}

	public static void InitList(Connection aConn) throws Exception {
		ObjectBase.CreateObject(aConn, IdentityType.class); // check if table already exist, if not create it
		Lookup.ClearAndLoadList(aConn, IdentityType.class, LookupList);
		MalaysiaIc = (IdentityType) Lookup.InsertDefaultList(aConn, MalaysiaIc, IdentityType.class, MalaysiaIcDescr, LookupList);
		MalaysiaPr = (IdentityType) Lookup.InsertDefaultList(aConn, MalaysiaPr, IdentityType.class, MalaysiaPrDescr, LookupList);
		SingaporePassport = (IdentityType) Lookup.InsertDefaultList(aConn, SingaporePassport, IdentityType.class, SingaporePassportDescr, LookupList);
		ChinaPassport = (IdentityType) Lookup.InsertDefaultList(aConn, ChinaPassport, IdentityType.class, ChinaPassportDescr, LookupList);
		IndonesiaPassport = (IdentityType) Lookup.InsertDefaultList(aConn, IndonesiaPassport, IdentityType.class, IndonesiaPassportDescr, LookupList);
		MyanmarPassport = (IdentityType) Lookup.InsertDefaultList(aConn, MyanmarPassport, IdentityType.class, MyanmarPassportDescr, LookupList);
		VietnamPassport = (IdentityType) Lookup.InsertDefaultList(aConn, VietnamPassport, IdentityType.class, VietnamPassportDescr, LookupList);
		IndiaPassport = (IdentityType) Lookup.InsertDefaultList(aConn, IndiaPassport, IdentityType.class, IndiaPassportDescr, LookupList);
	}

	public static IdentityType GetClaszFromDescr(String aDescr) throws Exception {
		IdentityType result;
		if (aDescr.trim().equalsIgnoreCase(IdentityType.MalaysiaIcDescr)) {
			result = IdentityType.MalaysiaIc;
		} else if (aDescr.trim().equalsIgnoreCase(IdentityType.MalaysiaPrDescr)) {
			result = IdentityType.MalaysiaPr;
		} else if (aDescr.trim().equalsIgnoreCase(IdentityType.VietnamPassportDescr)) {
			result = IdentityType.VietnamPassport;
		} else if (aDescr.trim().equalsIgnoreCase(IdentityType.IndonesiaPassportDescr)) {
			result = IdentityType.IndonesiaPassport;
		} else if (aDescr.trim().equalsIgnoreCase(IdentityType.MyanmarPassportDescr)) {
			result = IdentityType.MyanmarPassport;
		} else if (aDescr.trim().equalsIgnoreCase(IdentityType.ChinaPassportDescr)) {
			result = IdentityType.ChinaPassport;
		} else if (aDescr.trim().equalsIgnoreCase(IdentityType.IndiaPassportDescr)) {
			result = IdentityType.IndiaPassport;
		} else if (aDescr.trim().equalsIgnoreCase(IdentityType.IndonesiaPassportDescr)) {
			result = IdentityType.IndonesiaPassport;
		} else if (aDescr.isEmpty()) {
			throw new Hinderance("Invalid/empty employee domicile identity");
		} else {
			throw new Hinderance("No support for employee of: " + aDescr + ", please upgrade your system!");
		}
		return(result);
	}

}
