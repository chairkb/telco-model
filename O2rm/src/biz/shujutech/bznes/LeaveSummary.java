package biz.shujutech.bznes;

import biz.shujutech.db.object.Clasz;
import biz.shujutech.db.relational.FieldType;
import biz.shujutech.reflect.ReflectField;

public class LeaveSummary extends Clasz {
	@ReflectField(type=FieldType.OBJECT, deleteAsMember=false, clasz="biz.shujutech.bznes.LeaveType", lookup=true, displayPosition=10) public static String LeaveType; 
	@ReflectField(type=FieldType.INTEGER, displayPosition=20) public static String EligibleDaysPerYear; 
	@ReflectField(type=FieldType.FLOAT, displayPosition=20) public static String Taken;
	@ReflectField(type=FieldType.FLOAT, displayPosition=20) public static String Balance;
	@ReflectField(type=FieldType.DATETIME, displayPosition=50) public static String ExpiryDate;
	
}
