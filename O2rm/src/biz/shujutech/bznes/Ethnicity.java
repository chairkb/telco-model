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

public class Ethnicity extends Clasz implements Lookup {
	@ReflectField(type=FieldType.STRING, size=32, indexes={@ReflectIndex(indexName="idx_ethn_descr", indexNo=0, indexOrder=SortOrder.ASC, isUnique=true)}) public static String Descr;  // short Descr gives error, its mysql reserve word
	public static CopyOnWriteArrayList<Lookup> LookupList = new CopyOnWriteArrayList<>();

	public static String ChineseDescr = "Chinese";
	public static Ethnicity Chinese = null;

	public static String IndianDescr = "Indian";
	public static Ethnicity Indian = null;

	public static String MalayDescr = "Malay";
	public static Ethnicity Malay = null;

	public static String CaucasianDescr = "Caucasian";
	public static Ethnicity Caucasian = null;

	public static String OtherDescr = "Other";
	public static Ethnicity Other = null;

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
		ObjectBase.CreateObject(aConn, Ethnicity.class); // check if table already exist, if not create it
		Lookup.ClearAndLoadList(aConn, Ethnicity.class, LookupList);
		Chinese = (Ethnicity) Lookup.InsertDefaultList(aConn, Chinese, Ethnicity.class, ChineseDescr, LookupList);
		Indian = (Ethnicity) Lookup.InsertDefaultList(aConn, Indian, Ethnicity.class, IndianDescr, LookupList);
		Malay = (Ethnicity) Lookup.InsertDefaultList(aConn, Malay, Ethnicity.class, MalayDescr, LookupList);
		Caucasian = (Ethnicity) Lookup.InsertDefaultList(aConn, Caucasian, Ethnicity.class, CaucasianDescr, LookupList);
		Other = (Ethnicity) Lookup.InsertDefaultList(aConn, Other, Ethnicity.class, OtherDescr, LookupList);
	}

}
