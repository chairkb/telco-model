package biz.shujutech.bznes;

import biz.shujutech.base.Connection;
import biz.shujutech.db.object.Clasz;
import biz.shujutech.db.relational.FieldType;
import biz.shujutech.reflect.ReflectField;
import org.joda.time.DateTime;

public class Transaction extends Clasz {
	@ReflectField(type=FieldType.STRING, size=50, displayPosition=50) public static String Descr;  
	@ReflectField(type=FieldType.OBJECT, deleteAsMember=true, inline=true, prefetch=true, clasz="biz.shujutech.bznes.Money", displayPosition=60) public static String Amount; 
	@ReflectField(type=FieldType.DATETIME, displayPosition=70) public static String CreateDate;
	@ReflectField(type=FieldType.DATETIME, displayPosition=80) public static String TransactionDate;
	@ReflectField(type=FieldType.DATETIME, displayPosition=90) public static String PostedDate;
	@ReflectField(type=FieldType.BOOLEAN, displayPosition=100) public static String SystemGenerated;
	@ReflectField(type=FieldType.OBJECT, deleteAsMember=false, clasz="biz.shujutech.bznes.Contact", polymorphic=true, displayPosition=110) public static String PaidFrom;
	@ReflectField(type=FieldType.OBJECT, deleteAsMember=false, clasz="biz.shujutech.bznes.Contact", polymorphic=true, displayPosition=120) public static String PaidTo;
	
	public void setDescr(String aDescr) throws Exception {
		this.setValueStr(Descr, aDescr);
	}

	public String getDescr() throws Exception {
		return(this.getValueStr(Descr));
	}

	public void setAmount(Money aAmount) throws Exception {
		this.setValueObject(Amount, aAmount);
	}

	public Money getAmount(Connection aConn) throws Exception {
		return((Money) this.getValueObject(aConn, Amount));
	}

	public void setCreateDate(DateTime aValue) throws Exception {
		this.setValueDateTime(CreateDate, aValue);
	}

	public DateTime getCreateDate() throws Exception {
		return(this.getValueDateTime(CreateDate));
	}

	public void setTransactionDate(DateTime aValue) throws Exception {
		this.setValueDateTime(TransactionDate, aValue);
	}

	public DateTime getTransactionDate() throws Exception {
		return(this.getValueDateTime(TransactionDate));
	}

	public void setPostedDate(DateTime aValue) throws Exception {
		this.setValueDateTime(PostedDate, aValue);
	}

	public DateTime getPostedDate() throws Exception {
		return(this.getValueDateTime(PostedDate));
	}

	public void setPaidFrom(Contact aPaidBy) throws Exception {
		this.setValueObject(PaidFrom, aPaidBy);
	}

	public Contact getPaidFrom(Connection aConn) throws Exception {
		Contact paidBy = (Contact) this.getFieldObject(PaidFrom).getValueObj(aConn);
		return (paidBy);
	}

	public void setPaidTo(Contact aPaidTo) throws Exception {
		this.setValueObject(PaidTo, aPaidTo);
	}

	public Contact getPaidTo(Connection aConn) throws Exception {
		Contact paidTo = (Contact) this.getFieldObject(PaidTo).getValueObj(aConn);
		return (paidTo);
	}

	public Boolean isSystemGenerated() throws Exception {
		return(this.getValueBoolean(SystemGenerated));
	}

	public void setSystemGenerated(boolean aBool) throws Exception {
		this.setValueBoolean(SystemGenerated, aBool);
	}

}
