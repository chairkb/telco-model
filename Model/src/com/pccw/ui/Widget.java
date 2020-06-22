package com.pccw.ui;

import biz.shujutech.base.Connection;
import biz.shujutech.db.object.Clasz;
import biz.shujutech.db.object.Lookup;
import biz.shujutech.db.object.ObjectBase;
import biz.shujutech.db.relational.FieldType;
import biz.shujutech.db.relational.SortOrder;
import biz.shujutech.reflect.ReflectField;
import biz.shujutech.reflect.ReflectIndex;
import java.util.concurrent.CopyOnWriteArrayList;

public class Widget extends Clasz implements Lookup {
	@ReflectField(type=FieldType.STRING, size=32, indexes={@ReflectIndex(indexName="idx_usrstatus_descr", indexNo=0, indexOrder=SortOrder.ASC, isUnique=true)}) public static String Descr;  

	public static CopyOnWriteArrayList<Lookup> LookupList = new CopyOnWriteArrayList<>();

	public static String DropDownDescr = "Drop Down";
	public static Widget DropDown = null;

	public static String TextFieldDescr = "Text Field";
	public static Widget TextField = null;

	public static String NumberFieldDescr = "Text Field";
	public static Widget NumberField = null;

	public static String TickboxDescr = "Text Field";
	public static Widget Tickbox = null;

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
		ObjectBase.CreateObject(aConn, Widget.class); // check if table already exist, if not create it
		Lookup.ClearAndLoadList(aConn, Widget.class, LookupList);
		DropDown = (Widget) Lookup.InsertDefaultList(aConn, DropDown, Widget.class, DropDownDescr, LookupList);
		TextField = (Widget) Lookup.InsertDefaultList(aConn, TextField, Widget.class, TextFieldDescr, LookupList);
		NumberField = (Widget) Lookup.InsertDefaultList(aConn, NumberField, Widget.class, NumberFieldDescr, LookupList);
		Tickbox = (Widget) Lookup.InsertDefaultList(aConn, Tickbox, Widget.class, TickboxDescr, LookupList);
	}
	
	
}
