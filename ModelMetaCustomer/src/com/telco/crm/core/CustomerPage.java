package com.telco.crm.core;

import biz.shujutech.db.relational.FieldType;
import biz.shujutech.reflect.ReflectField;
import com.ui.Widget;

public class CustomerPage extends Widget {
	@ReflectField(type=FieldType.DATETIME, displayPosition=100) public static String CreateAt;
	@ReflectField(type=FieldType.STRING, size=64, displayPosition=100) public static String CreateBy;
	@ReflectField(type=FieldType.DATETIME, displayPosition=100) public static String UpdateAt;
	@ReflectField(type=FieldType.STRING, size=64, displayPosition=100) public static String UpdateBy;

	@ReflectField(type=FieldType.OBJECTBOX, deleteAsMember=true, clasz="com.ui.Widget", displayPosition=10) public static String Widgets;
	@ReflectField(type=FieldType.OBJECTBOX, deleteAsMember=true, clasz="com.ui.Panel", displayPosition=10) public static String Panels;
	
}
