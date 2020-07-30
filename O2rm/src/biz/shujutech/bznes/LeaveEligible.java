package biz.shujutech.bznes;

import biz.shujutech.base.Connection;
import biz.shujutech.db.object.Clasz;
import biz.shujutech.db.object.ObjectBase;
import biz.shujutech.db.relational.FieldType;
import biz.shujutech.reflect.ReflectField;


public class LeaveEligible extends Clasz {
	@ReflectField(type=FieldType.OBJECT, deleteAsMember=false, clasz="biz.shujutech.bznes.LeaveType", lookup=true, displayPosition=10) public static String LeaveType; 
	@ReflectField(type=FieldType.INTEGER, displayPosition=20) public static String EligibleDaysPerYear; 

	public int getEligibility() throws Exception {
		return(this.getValueInt(EligibleDaysPerYear));
	}

	public void setEligibility(int aEntitle) throws Exception {
		this.setValueInt(EligibleDaysPerYear, aEntitle);
	}

	public void setLeaveType(LeaveType aLeaveType) throws Exception {
		this.setValueObject(LeaveType, aLeaveType);
	}

	public LeaveType getLeaveType() throws Exception {
		return((LeaveType) this.getValueObject(LeaveType));
	}

	public static LeaveEligible CreateLeave(Connection aConn, LeaveType aType, int aDayEntitle) throws Exception {
		LeaveEligible leaveEligible = (LeaveEligible) ObjectBase.CreateObject(aConn, LeaveEligible.class);
		leaveEligible.setLeaveType(aType);
		leaveEligible.setEligibility(aDayEntitle);
		return(leaveEligible);
	}

	/**
	 * Use by FieldObjectBox to return the default value whenever the field 
	 * for this class is ask for. In this case though there can be multiple
	 * leave type in a FieldObjectBox, it will return the annual leave 
	 * entitlement number.
	 * 
	 * @return
	 * @throws Exception 
	 */
	@Override
	public String getValueStr() throws Exception {
		String result = "";
		if (this.getLeaveType().isAnnualLeave()) {
			result = Integer.toString(this.getEligibility());
		}
		return(result);
	}

	
}
