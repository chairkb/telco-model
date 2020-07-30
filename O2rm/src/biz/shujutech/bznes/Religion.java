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

public class Religion extends Clasz implements Lookup {
	@ReflectField(type=FieldType.STRING, size=32, indexes={@ReflectIndex(indexName="idx_rlgn_descr", indexNo=0, indexOrder=SortOrder.ASC, isUnique=true)}) public static String Descr;  // short Descr gives error, its mysql reserve word
	public static CopyOnWriteArrayList<Lookup> LookupList = new CopyOnWriteArrayList<>();

	public static String BuddhistDescr = "Buddhist";
	public static Religion Buddhist = null;

	public static String HinduDescr = "Hindu";
	public static Religion Hindu = null;

	public static String IslamDescr = "Islam";
	public static Religion Islam = null;

	public static String ChristianDescr = "Christian";
	public static Religion Christian = null;

	public static String OtherDescr = "Other";
	public static Religion Other = null;

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
		ObjectBase.CreateObject(aConn, Religion.class); // check if table already exist, if not create it
		Lookup.ClearAndLoadList(aConn, Religion.class, LookupList);
		Buddhist = (Religion) Lookup.InsertDefaultList(aConn, Buddhist, Religion.class, BuddhistDescr, LookupList);
		Hindu = (Religion) Lookup.InsertDefaultList(aConn, Hindu, Religion.class, HinduDescr, LookupList);
		Islam = (Religion) Lookup.InsertDefaultList(aConn, Islam, Religion.class, IslamDescr, LookupList);
		Christian = (Religion) Lookup.InsertDefaultList(aConn, Christian, Religion.class, ChristianDescr, LookupList);
		Other = (Religion) Lookup.InsertDefaultList(aConn, Other, Religion.class, OtherDescr, LookupList);
	}
}
