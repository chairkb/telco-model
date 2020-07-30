package biz.shujutech.payroll;

import biz.shujutech.db.object.Clasz;
import biz.shujutech.db.relational.FieldType;
import org.joda.time.DateTime;
import biz.shujutech.reflect.ReflectField;

public class SalarySlipStatus extends Clasz {
	@ReflectField(type=FieldType.STRING, size=32, displayPosition=05) public static String Descr;  // short Descr gives error, its mysql reserve word
	@ReflectField(type=FieldType.DATETIME, displayPosition=10) public static String StatusDate;

	public String getDescr() throws Exception {
		return(this.getValueStr(Descr));
	}

	public void setDescr(String aDescr) throws Exception {
		this.setValueStr(Descr, aDescr);
	}

	public void setStatusDate(DateTime aStatusDate) throws Exception {
		this.setValueDateTime(StatusDate, aStatusDate);
	}

	public DateTime getStatusDate() throws Exception {
		return (this.getValueDateTime(StatusDate));
	}

	
}
