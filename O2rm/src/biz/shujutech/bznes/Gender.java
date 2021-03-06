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


public class Gender extends Clasz implements Lookup {
	@ReflectField(type=FieldType.STRING, size=32, indexes={@ReflectIndex(indexName="idx_gdr_descr", indexNo=0, indexOrder=SortOrder.ASC, isUnique=true)}) public static String Descr;  // short Descr gives error, its mysql reserve word
	public static CopyOnWriteArrayList<Lookup> LookupList = new CopyOnWriteArrayList<>();

	public static String MaleDescr = "Male";
	public static Gender Male = null;

	public static String FemaleDescr = "Female";
	public static Gender Female = null;

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
		ObjectBase.CreateObject(aConn, Gender.class); // check if table already exist, if not create it
		Lookup.ClearAndLoadList(aConn, Gender.class, LookupList);
		Male = (Gender) Lookup.InsertDefaultList(aConn, Male, Gender.class, MaleDescr, LookupList);
		Female = (Gender) Lookup.InsertDefaultList(aConn, Female, Gender.class, FemaleDescr, LookupList);
	}

}