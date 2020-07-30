package biz.shujutech.payroll;

import biz.shujutech.base.Connection;
import biz.shujutech.db.object.Clasz;
import biz.shujutech.db.object.ObjectBase;
import biz.shujutech.db.relational.FieldType;
import biz.shujutech.reflect.ReflectField;
import java.util.Map;

public class WorkConfigDay extends Clasz {
	public static int FULL_DAY_WORK = 100;
	public static int HALF_DAY_WORK = 50;
	public static int NOT_WORK_DAY = 0;

	@ReflectField(type=FieldType.DATETIME, displayPosition=10) public static String WorkStartTime;
	@ReflectField(type=FieldType.DATETIME, displayPosition=20) public static String WorkEndTime;
	@ReflectField(type=FieldType.INTEGER, displayPosition=30) public static String DayNumber;
	@ReflectField(type=FieldType.INTEGER, displayPosition=40) public static String DayWorkPercentage;
	@ReflectField(type=FieldType.OBJECTBOX, clasz="biz.shujutech.payroll.WorkHourlyRate", deleteAsMember=true, prefetch=true, displayPosition=50) public static String HourlyRate;

	public WorkConfigDay() throws Exception {
	}

	public WorkConfigDay(Connection aConn, int aDayNumber) throws Exception {
		this.setDayNumber(aDayNumber);
		//this.createDailyHourlyRate(aConn, aDayNumber);
	}

	public Integer getDayNumber() throws Exception {
		return(this.getValueInt(DayNumber));
	}

	public void setDayNumber(int aDayNumber) throws Exception {
		this.setValueInt(DayNumber, aDayNumber);
	}
	
	public Map getHourlyRate() throws Exception {
		return (this.getFieldObjectBox(HourlyRate).getObjectMap());
	}

	public void addHourlyRate(Connection aConn, WorkHourlyRate aWorkHourlyRate) throws Exception {
		this.addValueObject(aConn, HourlyRate, aWorkHourlyRate);
	}
	
	public Integer getDayWorkPercentage() throws Exception {
		return(this.getValueInt(DayWorkPercentage));
	}

	public void setDayWorkPercentage(int aDayWorkPercentage) throws Exception {
		this.setValueInt(DayWorkPercentage, aDayWorkPercentage);
	}
	
	public void createDailyHourlyRate(Connection aConn, int aDayNumber) throws Exception {
		for(int intHour = 0; intHour < 24; intHour++ ) {
			WorkHourlyRate hourlyRate = (WorkHourlyRate) ObjectBase.CreateObject(aConn, WorkHourlyRate.class);
			hourlyRate.setHourlyStart(intHour);
			this.addHourlyRate(aConn, hourlyRate);
		}
	}

	public void setFullWorkDay() throws Exception {
		this.setDayWorkPercentage(FULL_DAY_WORK);
	}

	public void setHalfWorkDay() throws Exception {
		this.setDayWorkPercentage(HALF_DAY_WORK);
	}

	public void setNonWorkDay() throws Exception {
		this.setDayWorkPercentage(NOT_WORK_DAY);
	}

}
