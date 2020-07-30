package biz.shujutech.bznes;

import biz.shujutech.db.object.Clasz;
import biz.shujutech.db.relational.FieldType;
import biz.shujutech.reflect.ReflectField;

public class LeaveApprover extends Clasz {
	@ReflectField(type=FieldType.OBJECT, deleteAsMember=false, clasz="biz.shujutech.bznes.Person", polymorphic=true, displayPosition=10, prefetch=false) public static String ApprovedBy;
	@ReflectField(type=FieldType.BOOLEAN, displayPosition=20) public static String Approve;
	@ReflectField(type=FieldType.STRING, size=64, displayPosition=30) public static String Remark;
	
}
