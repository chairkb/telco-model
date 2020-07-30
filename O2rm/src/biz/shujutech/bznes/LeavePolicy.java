package biz.shujutech.bznes;

import biz.shujutech.base.Connection;
import biz.shujutech.base.Hinderance;
import biz.shujutech.db.object.Clasz;
import biz.shujutech.db.object.FieldObjectBox;
import biz.shujutech.db.object.ObjectBase;
import biz.shujutech.db.relational.FieldType;
import biz.shujutech.db.relational.SortOrder;
import java.util.concurrent.CopyOnWriteArrayList;
import biz.shujutech.reflect.ReflectField;
import biz.shujutech.reflect.ReflectIndex;
import biz.shujutech.db.object.Lookup;

public class LeavePolicy extends Clasz implements Lookup {

	@ReflectField(type = FieldType.STRING, size = 16, indexes={@ReflectIndex(indexName="idx_lp_descr", indexNo=0, indexOrder=SortOrder.ASC, isUnique=true)}) public static String Descr;
	@ReflectField(type = FieldType.OBJECTBOX, deleteAsMember=true, clasz = "biz.shujutech.bznes.LeaveEligible") public static String Eligibility;

	public static CopyOnWriteArrayList<Lookup> LookupList = new CopyOnWriteArrayList<>();

	public static String MalaysiaDefaultDescr = "Malaysia Default";
	public static LeavePolicy MalaysiaDefault = null;

	@Override
	public void initialize(Connection aConn) throws Exception {
		if (this.getEligibility().getObjectMap().isEmpty()) {
			if (this.getDescr().equals(LeavePolicy.MalaysiaDefaultDescr)) {
				LeaveEligible annualLeave = LeaveEligible.CreateLeave(aConn, LeaveType.Annual, 14);
				this.addLeaveEligibility(aConn, annualLeave);

				LeaveEligible medicalLeave = LeaveEligible.CreateLeave(aConn, LeaveType.Medical, 12);
				this.addLeaveEligibility(aConn, medicalLeave);

				LeaveEligible hospitalLeave = LeaveEligible.CreateLeave(aConn, LeaveType.Hospitalized, 30);
				this.addLeaveEligibility(aConn, hospitalLeave);

				LeaveEligible unpaidLeave = LeaveEligible.CreateLeave(aConn, LeaveType.NoPay, 200);
				this.addLeaveEligibility(aConn, unpaidLeave);

				LeaveEligible compassionateLeave = LeaveEligible.CreateLeave(aConn, LeaveType.Compassionate, 3);
				this.addLeaveEligibility(aConn, compassionateLeave);

				LeaveEligible marriageLeave = LeaveEligible.CreateLeave(aConn, LeaveType.Marriage, 3);
				this.addLeaveEligibility(aConn, marriageLeave);

				LeaveEligible maternityLeave = LeaveEligible.CreateLeave(aConn, LeaveType.Maternity, 90);
				this.addLeaveEligibility(aConn, maternityLeave);

				LeaveEligible paternityLeave = LeaveEligible.CreateLeave(aConn, LeaveType.Paternity, 2);
				this.addLeaveEligibility(aConn, paternityLeave);

				LeaveEligible examLeave = LeaveEligible.CreateLeave(aConn, LeaveType.Exam, 2);
				this.addLeaveEligibility(aConn, examLeave);
			} else {
				throw new Hinderance("Can't create leave policy for: " + this.getDescr().toUpperCase());
			}
		}
	}

	public LeavePolicy() {
		super();
	}

	public FieldObjectBox getEligibility() throws Exception {
		return ((FieldObjectBox) this.getField(Eligibility));
	}

	@Override
	public String getValueStr() throws Exception {
		return(this.getDescr());
	}

	@Override
	public String getDescr() throws Exception {
		return (this.getValueStr(Descr));
	}

	@Override
	public void setDescr(String aDescr) throws Exception {
		this.setValueStr(Descr, aDescr);
	}

	@Override
	public CopyOnWriteArrayList<Lookup> getLookupList() {
		return (LookupList);
	}

	public static void InitList(Connection aConn) throws Exception {
		LeaveType.InitList(aConn); // leave policy needs leave type class
		ObjectBase.CreateObject(aConn, LeavePolicy.class); // check if table already exist, if not create it
		Lookup.ClearAndLoadList(aConn, LeavePolicy.class, LookupList);
		MalaysiaDefault = (LeavePolicy) Lookup.InsertDefaultList(aConn, MalaysiaDefault, LeavePolicy.class, MalaysiaDefaultDescr, LookupList);
	}

	public void addLeaveEligibility(Connection aConn, LeaveEligible aLeave) throws Exception {
		this.addValueObject(aConn, Eligibility, aLeave);
	}
}
