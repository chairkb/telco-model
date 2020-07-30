package biz.shujutech.bznes;

import biz.shujutech.base.Connection;
import biz.shujutech.db.object.Clasz;
import biz.shujutech.db.object.Lookup;
import biz.shujutech.db.object.ObjectBase;
import biz.shujutech.db.relational.FieldType;
import biz.shujutech.reflect.ReflectField;
import java.util.concurrent.CopyOnWriteArrayList;

public class LeaveStatus extends Clasz implements Lookup {
	@ReflectField(type=FieldType.STRING, size=32, displayPosition=10) public static String Descr;  // short Descr gives error, its mysql reserve word

	public static CopyOnWriteArrayList<Lookup> LookupList = new CopyOnWriteArrayList<>();

	public static String NewDescr = "New";
	public static LeaveStatus New = null;

	public static String ApprovalPendingDescr = "Approval pending";
	public static LeaveStatus ApprovalPending = null;

	public static String LeaveApprovedDescr = "Leave approved";
	public static LeaveStatus LeaveApproved = null;

	public static String LeaveRejectedDescr = "Leave rejected";
	public static LeaveStatus LeaveRejected= null;

	public static String CancelingDescr = "Cancelling";
	public static LeaveStatus Canceling = null;

	public static String CancelingPendingDescr = "Cancellation pending";
	public static LeaveStatus CancelingPending = null;

	public static String CancelApprovedDescr = "Cancellation approved";
	public static LeaveStatus CancelApproved = null;

	@Override
	public String getDescr() throws Exception {
		return(this.getValueStr(Descr));
	}

	@Override
	public String getValueStr() throws Exception {
		return(this.getDescr());
	}

	@Override
	public void setDescr(String aDescr) throws Exception {
		this.setValueStr(Descr, aDescr);
	}

	@Override
	public CopyOnWriteArrayList<Lookup> getLookupList() throws Exception {
		return(LookupList);
	}

	@Override
	public void initialize(Connection aConn) throws Exception {
		ObjectBase.CreateObject(aConn, LeaveStatus.class); // check if table already exist, if not create it
		Lookup.ClearAndLoadList(aConn, LeaveStatus.class, LookupList);
		New = (LeaveStatus) Lookup.InsertDefaultList(aConn, New, LeaveStatus.class, NewDescr, LookupList);
		ApprovalPending = (LeaveStatus) Lookup.InsertDefaultList(aConn, ApprovalPending, LeaveStatus.class, ApprovalPendingDescr, LookupList);
		LeaveApproved = (LeaveStatus) Lookup.InsertDefaultList(aConn, LeaveApproved, LeaveStatus.class, LeaveApprovedDescr, LookupList);
		Canceling = (LeaveStatus) Lookup.InsertDefaultList(aConn, Canceling, LeaveStatus.class, CancelingDescr, LookupList);
		CancelApproved = (LeaveStatus) Lookup.InsertDefaultList(aConn, CancelApproved, LeaveStatus.class, CancelApprovedDescr, LookupList);
	}

	
}
