package com.ui;

import biz.shujutech.db.relational.FieldType;
import biz.shujutech.reflect.ReflectField;

public class Panel extends Widget {
	@ReflectField(type=FieldType.OBJECTBOX, deleteAsMember=true, clasz="com.ui.Widget", displayPosition=10) public static String Widgets;
	
}
