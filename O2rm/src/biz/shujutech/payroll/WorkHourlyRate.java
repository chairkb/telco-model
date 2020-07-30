package biz.shujutech.payroll;

import biz.shujutech.bznes.Money;
import biz.shujutech.db.object.Clasz;
import biz.shujutech.db.relational.FieldType;
import biz.shujutech.reflect.ReflectField;

public class WorkHourlyRate extends Clasz {
	@ReflectField(type=FieldType.OBJECT, clasz="biz.shujutech.bznes.Money", deleteAsMember=true, inline=true, prefetch=true, displayPosition=5) public static String HourlyRate;
	@ReflectField(type=FieldType.INTEGER, displayPosition=10) public static String HourlyStart;

	public WorkHourlyRate() throws Exception {
	}

	public WorkHourlyRate(int aHourlyStart) throws Exception {
		this.setHourlyStart(aHourlyStart);
	}

	public Money getHourlyRate() throws Exception {
		return((Money) this.getValueObject(HourlyRate));
	}

	public void setHourlyRate(Money aHourlyRate) throws Exception {
		this.setValueObject(HourlyRate, aHourlyRate);
	}

	public Integer getHourlyStart() throws Exception {
		return(this.getValueInt(HourlyStart));
	}

	public void setHourlyStart(int aHourlyStart) throws Exception {
		this.setValueInt(HourlyStart, aHourlyStart);
	}
}
