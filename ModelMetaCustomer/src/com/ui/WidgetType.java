package com.ui;

import biz.shujutech.base.Connection;
import biz.shujutech.db.object.Clasz;
import biz.shujutech.db.object.Lookup;
import biz.shujutech.db.object.ObjectBase;
import biz.shujutech.db.relational.FieldType;
import biz.shujutech.db.relational.SortOrder;
import biz.shujutech.reflect.ReflectField;
import biz.shujutech.reflect.ReflectIndex;
import java.util.concurrent.CopyOnWriteArrayList;

public class WidgetType extends Clasz implements Lookup {
	@ReflectField(type=FieldType.STRING, size=32, indexes={@ReflectIndex(indexName="idx_wdgtyp_descr", indexNo=0, indexOrder=SortOrder.ASC, isUnique=true)}) public static String Descr;  

	public static CopyOnWriteArrayList<Lookup> LookupList = new CopyOnWriteArrayList<>();

	public static String DropDownDescr = "Drop Down";
	public static WidgetType DropDown = null;

	public static String TextFieldDescr = "Text Field";
	public static WidgetType TextField = null;

	public static String NumberFieldDescr = "Number";
	public static WidgetType NumberField = null;

	public static String TickboxDescr = "Tickbox";
	public static WidgetType Tickbox = null;

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
		ObjectBase.CreateObject(aConn, WidgetType.class); // check if table already exist, if not create it
		Lookup.ClearAndLoadList(aConn, WidgetType.class, LookupList);
		DropDown = (WidgetType) Lookup.InsertDefaultList(aConn, DropDown, WidgetType.class, DropDownDescr, LookupList);
		TextField = (WidgetType) Lookup.InsertDefaultList(aConn, TextField, WidgetType.class, TextFieldDescr, LookupList);
		NumberField = (WidgetType) Lookup.InsertDefaultList(aConn, NumberField, WidgetType.class, NumberFieldDescr, LookupList);
		Tickbox = (WidgetType) Lookup.InsertDefaultList(aConn, Tickbox, WidgetType.class, TickboxDescr, LookupList);
	}
	
	
}
