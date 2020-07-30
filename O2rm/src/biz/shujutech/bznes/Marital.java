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


public class Marital extends Clasz implements Lookup {
	@ReflectField(type=FieldType.STRING, size=32, indexes={@ReflectIndex(indexName="idx_martl_descr", indexNo=0, indexOrder=SortOrder.ASC, isUnique=true)}) public static String Descr;  // short Descr gives error, its mysql reserve word
	public static CopyOnWriteArrayList<Lookup> LookupList = new CopyOnWriteArrayList<>();

	public static String MarriedDescr = "Married";
	public static Marital Married = null;

	public static String SingleDescr = "Single";
	public static Marital Single = null;

	public static String DivorceDescr = "Divorce";
	public static Marital Divorce = null;

	public static String WidowDescr = "Widow";
	public static Marital Widow = null;

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
		ObjectBase.CreateObject(aConn, Marital.class); // check if table already exist, if not create it
		Lookup.ClearAndLoadList(aConn, Marital.class, LookupList);
		Married = (Marital) Lookup.InsertDefaultList(aConn, Married, Marital.class, MarriedDescr, LookupList);
		Single = (Marital) Lookup.InsertDefaultList(aConn, Single, Marital.class, SingleDescr, LookupList);
		Divorce = (Marital) Lookup.InsertDefaultList(aConn, Divorce, Marital.class, DivorceDescr, LookupList);
		Widow = (Marital) Lookup.InsertDefaultList(aConn, Widow, Marital.class, WidowDescr, LookupList);
	}
}