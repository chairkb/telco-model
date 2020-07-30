/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package biz.shujutech.bznes;

import biz.shujutech.base.Connection;
import biz.shujutech.db.object.Clasz;
import biz.shujutech.db.relational.FieldType;
import biz.shujutech.db.relational.SortOrder;
import biz.shujutech.reflect.ReflectField;
import biz.shujutech.reflect.ReflectIndex;
import java.util.concurrent.CopyOnWriteArrayList;
import biz.shujutech.db.object.Lookup;
import biz.shujutech.db.object.ObjectBase;


/**
 *
 */
public class UserStatus extends Clasz implements Lookup {
	@ReflectField(type=FieldType.STRING, size=32, indexes={@ReflectIndex(indexName="idx_usrstatus_descr", indexNo=0, indexOrder=SortOrder.ASC, isUnique=true)}) public static String Descr;  

	public static CopyOnWriteArrayList<Lookup> LookupList = new CopyOnWriteArrayList<>();

	public static String DisableDescr = "Disable";
	public static UserStatus Disable = null;

	public static String EnableDescr = "Enable";
	public static UserStatus Enable = null;

	@Override
	public void initialize(Connection aConn) throws Exception {
		// do nothing
	}

	@Override
	public String getValueStr() throws Exception {
		return(this.getDescr());
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
		ObjectBase.CreateObject(aConn, UserStatus.class); // check if table already exist, if not create it
		Lookup.ClearAndLoadList(aConn, UserStatus.class, LookupList);
		Disable = (UserStatus) Lookup.InsertDefaultList(aConn, Disable, UserStatus.class, DisableDescr, LookupList);
		Enable = (UserStatus) Lookup.InsertDefaultList(aConn, Enable, UserStatus.class, EnableDescr, LookupList);
	}
	
}
