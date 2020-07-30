package com.ui;

import biz.shujutech.db.object.Clasz;
import biz.shujutech.db.relational.FieldType;
import biz.shujutech.reflect.ReflectField;
import com.ui.WidgetType;

/**
 */
public class Widget extends Clasz {
	@ReflectField(type=FieldType.OBJECT, deleteAsMember=false, clasz="com.ui.WidgetType", lookup=true, displayPosition=10) public static String WidgetType;
	@ReflectField(type=FieldType.STRING, size=32, displayPosition=5) public static String Label;
	@ReflectField(type=FieldType.STRING, size=64, displayPosition=10) public static String Description;
	@ReflectField(type=FieldType.BOOLEAN, displayPosition=25) public static String Mandatory;
	@ReflectField(type=FieldType.BOOLEAN, displayPosition=30) public static String Visible;


	public void setLabel(String aValue) throws Exception {
		this.setValueStr(Label, aValue);
	}

	public void setDescription(String aValue) throws Exception {
		this.setValueStr(Description, aValue);
	}

	public void setWidgetType(WidgetType aValue) throws Exception {
		this.setValueObject(WidgetType, aValue);
	}

	public void setMandatory(boolean aValue) throws Exception {
		this.setValueBoolean(Mandatory, aValue);
	}

	public void setVisible(boolean aValue) throws Exception {
		this.setValueBoolean(Visible, aValue);
	}

	
}
