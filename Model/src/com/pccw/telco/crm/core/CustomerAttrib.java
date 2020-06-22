package com.pccw.telco.crm.core;

import biz.shujutech.db.object.Clasz;
import biz.shujutech.db.relational.FieldType;
import biz.shujutech.reflect.ReflectField;

public class CustomerAttrib extends Clasz {
	@ReflectField(type=FieldType.STRING, size=32, displayPosition=5) public static String Label;
	@ReflectField(type=FieldType.STRING, size=64, displayPosition=10) public static String Description;
	@ReflectField(type=FieldType.OBJECT, deleteAsMember=false, clasz="com.pccw.ui.Widget", lookup=true, displayPosition=15) public static String WidgetType;
	@ReflectField(type=FieldType.OBJECT, deleteAsMember=false, clasz="com.pccw.telco.crm.core.CustomerPanel", lookup=true, displayPosition=20) public static String FormPanel;
	@ReflectField(type=FieldType.BOOLEAN, displayPosition=25) public static String Mandatory;
	@ReflectField(type=FieldType.BOOLEAN, displayPosition=30) public static String Visible;



	
}
