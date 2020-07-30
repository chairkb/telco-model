package biz.shujutech.bznes;

import biz.shujutech.db.object.Clasz;
import biz.shujutech.db.relational.FieldType;
import biz.shujutech.reflect.ReflectField;

public class LeaveForm extends Clasz {

	@ReflectField(type=FieldType.OBJECT, deleteAsMember=false, clasz="biz.shujutech.bznes.LeaveStatus", lookup=true, displayPosition=10) public static String LeaveStatus;  
	@ReflectField(type=FieldType.OBJECT, deleteAsMember=false, clasz="biz.shujutech.bznes.LeaveType", lookup=true, displayPosition=20) public static String LeaveType;  
	@ReflectField(type=FieldType.DATETIME, displayPosition=30) public static String LeaveStart;
	@ReflectField(type=FieldType.DATETIME, displayPosition=40) public static String LeaveEnd;
	@ReflectField(type=FieldType.DATETIME, displayPosition=50) public static String DateApplied;
	@ReflectField(type=FieldType.DATETIME, displayPosition=60) public static String DateApproved;
	@ReflectField(type=FieldType.BOOLEAN, displayPosition=65) public static String LeaveTaken;
	@ReflectField(type=FieldType.OBJECTBOX, deleteAsMember=false, clasz="biz.shujutech.bznes.LeaveApprover", displayPosition=70, prefetch=false) public static String ApprovedBy;
	@ReflectField(type=FieldType.STRING, size=255, displayPosition=80) public static String Cc;
	@ReflectField(type=FieldType.BASE64, displayPosition=85) public static String Attachment;
	@ReflectField(type=FieldType.HTML, size=255, displayPosition=90) public static String Remark;


	public LeaveType getLeaveType() throws Exception {
		return((LeaveType) this.getValueObject(LeaveType));
	}

	public void setLeaveType(LeaveType aLeaveType) throws Exception {
		this.setValueObject(LeaveType, aLeaveType);
	}


}
