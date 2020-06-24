package com.pccw.telco.crm.core;

import com.pccw.ui.Panel;
import biz.shujutech.base.Connection;
import biz.shujutech.db.object.Lookup;
import biz.shujutech.db.object.ObjectBase;
import biz.shujutech.db.relational.FieldType;
import biz.shujutech.db.relational.SortOrder;
import biz.shujutech.reflect.ReflectField;
import biz.shujutech.reflect.ReflectIndex;
import java.util.concurrent.CopyOnWriteArrayList;

public class CustomerPanel extends Panel implements Lookup {
	@ReflectField(type=FieldType.STRING, size=32, indexes={@ReflectIndex(indexName="idx_custpanel_descr", indexNo=0, indexOrder=SortOrder.ASC, isUnique=true)}) public static String Descr;  

	public static CopyOnWriteArrayList<Lookup> LookupList = new CopyOnWriteArrayList<>();

	public static String CustomerDescr = "Customer";
	public static CustomerPanel Customer = null;

	public static String ContactDescr = "Contact";
	public static CustomerPanel Contact = null;

	public static String AddressDescr = "Address";
	public static CustomerPanel Address = null;

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
		ObjectBase.CreateObject(aConn, CustomerPanel.class); // check if table already exist, if not create it
		Lookup.ClearAndLoadList(aConn, CustomerPanel.class, LookupList);
		Customer = (CustomerPanel) Lookup.InsertDefaultList(aConn, Customer, CustomerPanel.class, CustomerDescr, LookupList);
		Contact = (CustomerPanel) Lookup.InsertDefaultList(aConn, Contact, CustomerPanel.class, ContactDescr, LookupList);
		Address = (CustomerPanel) Lookup.InsertDefaultList(aConn, Address, CustomerPanel.class, AddressDescr, LookupList);
	}
	
}
