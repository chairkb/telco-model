package biz.shujutech.payroll;

import biz.shujutech.bznes.Transaction;
import biz.shujutech.db.relational.FieldType;
import biz.shujutech.reflect.ReflectField;

public class SalaryTransaction extends Transaction {
	@ReflectField(type=FieldType.INTEGER, displayPosition=10) public static String Sequence; 
	@ReflectField(type=FieldType.OBJECT, deleteAsMember=false, clasz="biz.shujutech.payroll.SalaryTransactionType", displayPosition=25, lookup=true) public static String Type; // for internal use, currently not use

	public static final String BASIC_SALARY = "Basic Salary";


	public void setSequence(Integer aValue) throws Exception {
		this.setValueInt(Sequence, aValue);
	}

	public Integer getSequence() throws Exception {
		return(this.getValueInt(Sequence));
	}

}
