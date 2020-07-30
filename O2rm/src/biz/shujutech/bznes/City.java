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

public class City extends Clasz implements Lookup {
	@ReflectField(type=FieldType.STRING, size=64, indexes={@ReflectIndex(indexName="idx_city_descr", indexNo=0, indexOrder=SortOrder.ASC, isUnique=false)}) public static String Descr;
	public static CopyOnWriteArrayList<Lookup> LookupList = new CopyOnWriteArrayList<>();

	public City() throws Exception {
		super();
	}

	public City(String aCity) throws Exception {
		this.setDescr(aCity);
	}

	@Override
	public void initialize(Connection aConn) throws Exception {
		// do nothing
	}

	@Override
	public String getValueStr() throws Exception {
		return(this.getValueStr(Descr));
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
	public CopyOnWriteArrayList<Lookup> getLookupList() {
		return(LookupList);
	}

	public static void InitList(Connection aConn) throws Exception {
		ObjectBase.CreateObject(aConn, City.class); // check if table already exist, if not create it
		Lookup.ClearAndLoadList(aConn, City.class, LookupList);
	}

	public String getName() throws Exception {
		return this.getDescr();
	}	 
	
}
