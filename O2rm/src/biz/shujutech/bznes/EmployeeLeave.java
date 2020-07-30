package biz.shujutech.bznes;

import biz.shujutech.db.object.Clasz;
import biz.shujutech.db.relational.FieldType;
import biz.shujutech.reflect.ReflectField;

public class EmployeeLeave extends Clasz {
	@ReflectField(type=FieldType.INTEGER, displayPosition=20) public static String Year; 
	@ReflectField(type = FieldType.OBJECTBOX, deleteAsMember=true, clasz = "biz.shujutech.bznes.LeaveSummary") public static String MyLeave;
	
}
