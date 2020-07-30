package biz.shujutech.payroll;

import biz.shujutech.base.Connection;
import biz.shujutech.db.relational.FieldType;
import biz.shujutech.reflect.ReflectField;

public class SalaryTrxMsiaDeduct extends SalaryTransactionMalaysia {
	@ReflectField(type=FieldType.OBJECT, deleteAsMember=false, clasz="biz.shujutech.payroll.SalaryMsiaPayToList", lookup=true, displayPosition=21) public static String DeductAndPayTo;  

	public SalaryMsiaPayToList getDeductAndPayTo(Connection aConn) throws Exception {
		return((SalaryMsiaPayToList) this.getValueObject(aConn, DeductAndPayTo));
	}

	public void setDeductAndPayTo(SalaryMsiaPayToList aDeductAndPayTo) throws Exception {
		this.setValueObject(DeductAndPayTo, aDeductAndPayTo);
	}

	
}
