package biz.shujutech.bznes;

import biz.shujutech.db.object.Clasz;
import biz.shujutech.db.relational.FieldType;
import biz.shujutech.reflect.ReflectField;

/**
 * o2rm will never create a physical table for abstract class. 
 * 
 */
public abstract class Bill extends Clasz {
	//@ReflectField(type=FieldType.OBJECT, clasz="Contact") public static String BillIssuer; // class contact is java interface
	@ReflectField(type=FieldType.STRING, size=32) public static String BillNo;
	@ReflectField(type=FieldType.DATE) public static String BillDate;
	@ReflectField(type=FieldType.STRING, size=128) public static String BillDesc;
	@ReflectField(type=FieldType.OBJECT, clasz="biz.shujutech.bznes.Money", inline=true) public static String BillAmt; // inline will create columns in the table for this class
	@ReflectField(type=FieldType.OBJECT, clasz="biz.shujutech.bznes.Contact") public static String BillIssueBy; // TODO must support abstract member variable
	//@ReflectField(type=FieldType.OBJECT, clasz="biz.shujutech.bznes.Money") public static String BillAmt; // inline will create columns in the table for this class
	//@ReflectField(type=FieldType.OBJECT, clasz="Person") public static String BillIsTo; 

	public Bill() {
		super();
	}

	public String getBillNo() throws Exception {
		return(this.getValueStr(BillNo));
	}

	public void setBillNo(String aValue) throws Exception {
		this.setValueStr(BillNo, aValue);
	}

	public String getBillDesc() throws Exception {
		return(this.getValueStr(BillDesc));
	}

	public void setBillDesc(String aValue) throws Exception {
		this.setValueStr(BillDesc, aValue);
	}

	public Money getBillAmt() throws Exception {
		return((Money) this.getValueObject(BillAmt));
	}


}
