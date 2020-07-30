package biz.shujutech.payroll;

import biz.shujutech.base.App;
import biz.shujutech.base.Connection;
import biz.shujutech.base.Hinderance;
import biz.shujutech.db.object.Clasz;
import biz.shujutech.db.object.ObjectBase;
import biz.shujutech.db.relational.FieldType;
import biz.shujutech.db.relational.SortOrder;
import biz.shujutech.reflect.ReflectField;
import biz.shujutech.reflect.ReflectIndex;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import biz.shujutech.db.object.Lookup;
import biz.shujutech.db.relational.BaseDb;

public class WorkConfigWeek extends Clasz implements Lookup {
	@ReflectField(type=FieldType.STRING, size=64, indexes={@ReflectIndex(indexName="idx_wcw_descr", indexNo=0, indexOrder=SortOrder.ASC, isUnique=true)}) public static String Descr;
	@ReflectField(type=FieldType.OBJECTBOX, clasz="biz.shujutech.payroll.WorkConfigDay", deleteAsMember=true, prefetch=true, displayPosition=10) public static String WeekDay;
	@ReflectField(type=FieldType.DATETIME, displayPosition=20) public static String WorkStartTime;
	@ReflectField(type=FieldType.DATETIME, displayPosition=30) public static String WorkEndTime;

	public static CopyOnWriteArrayList<Lookup> LookupList = new CopyOnWriteArrayList<>();

	public static String FiveDayWeekSatSunDescr = "5 Day Week With Sat/Sun as Holiday";
	public static WorkConfigWeek FiveDayWeekSatSun = null;

	public static String FiveHalfDayWeekSunDescr = "5 Day Week and Half Day Work on Saturday, Sun is Holiday";
	public static WorkConfigWeek FiveHalfDayWeekSun = null;

	@Override
	public void initialize(Connection aConn) throws Exception {
		if (this.getValueStr().equals(WorkConfigWeek.FiveDayWeekSatSunDescr)) {
			this.config5DayWeek(aConn);
		} else if (this.getValueStr().equals(WorkConfigWeek.FiveHalfDayWeekSunDescr)) {
			this.config5AndHalfDayWeek(aConn);
		} else {
			throw new Hinderance("Fail to create instant of the class: " + this.getClass().getSimpleName() + " for: " + this.getValueStr());
		}
	}

	@Override
	public String getDescr() throws Exception {
		return(this.getValueStr(Descr));
	}

	@Override
	public void setDescr(String aDescr) throws Exception {
		this.setValueStr(Descr, aDescr);
	}

	@Override
	public String getValueStr() throws Exception {
		return(this.getDescr());
	}

	@Override
	public CopyOnWriteArrayList<Lookup> getLookupList() {
		return(LookupList);
	}

	public Map getWeekDay() throws Exception {
		return (this.getFieldObjectBox(WeekDay).getObjectMap());
	}

	public void addWeekDay(Connection aConn, WorkConfigDay aWeekDay) throws Exception {
		this.addValueObject(aConn, WeekDay, aWeekDay);
	}

	public void createDay(Connection aConn) throws Exception {
		for(int intDay = DateTimeConstants.MONDAY; intDay <= DateTimeConstants.SUNDAY; intDay++) {
			WorkConfigDay workConfigDay = (WorkConfigDay) ObjectBase.CreateObject(aConn, WorkConfigDay.class);
			workConfigDay.setDayNumber(intDay);
			this.addWeekDay(aConn, workConfigDay);
		}
	}

	public void config5DayWeek(Connection aConn) throws Exception {
		if (this.getFieldObjectBox(WeekDay).getTotalMember() == 0) {
			this.createDay(aConn);
			this.getFieldObjectBox(WeekDay).resetIterator();
			while (this.getFieldObjectBox(WeekDay).hasNext(aConn)) {
				WorkConfigDay configDay = (WorkConfigDay) this.getFieldObjectBox(WeekDay).getNext();
				if (configDay.getDayNumber() == DateTimeConstants.SATURDAY || configDay.getDayNumber() == DateTimeConstants.SUNDAY) {
					configDay.setNonWorkDay();
				} else {
					configDay.setFullWorkDay();
				}
			}
		} else {
			// do nothing, all WorkConfigDay properties are retrieve from the database during ClearAndLoadList
		}
	}

	public void config5AndHalfDayWeek(Connection aConn) throws Exception {
		if (this.getFieldObjectBox(WeekDay).getTotalMember() == 0) {
			this.createDay(aConn);
			this.getFieldObjectBox(WeekDay).resetIterator();
			while (this.getFieldObjectBox(WeekDay).hasNext(aConn)) {
				WorkConfigDay configDay = (WorkConfigDay) this.getFieldObjectBox(WeekDay).getNext();
				if (configDay.getDayNumber() == DateTimeConstants.SATURDAY) {
					configDay.setHalfWorkDay();
				} else if (configDay.getDayNumber() == DateTimeConstants.SUNDAY) {
					configDay.setNonWorkDay();
				} else {
					configDay.setFullWorkDay();
				}
			}
		} else {
			// do nothing, all WorkConfigDay properties are retrieve from the database during ClearAndLoadList
		}
	}

	public WorkConfigDay getWorkConfigDay(Connection aConn, int aDayNum) throws Exception {
		WorkConfigDay result = null;
		this.getFieldObjectBox(WeekDay).fetchAll(aConn);
		this.getFieldObjectBox(WeekDay).resetIterator();
		while (this.getFieldObjectBox(WeekDay).hasNext(aConn)) {
			WorkConfigDay configDay = (WorkConfigDay) this.getFieldObjectBox(WeekDay).getNext();
			if (configDay.getDayNumber().equals(aDayNum)) {
				result = configDay;
				break;
			}
		}
		if (result == null) throw new Hinderance("Fail to get WorkConfigDay for day number: " + aDayNum);
		return(result);
	}

	public int getWorkDayPercentage(Connection aConn, DateTime aDateTime) throws Exception {
		int result = 0;
		int dayOfWeek = aDateTime.getDayOfWeek(); // get the date number
		WorkConfigDay configDay = this.getWorkConfigDay(aConn, dayOfWeek);
		result = configDay.getDayWorkPercentage();
		return(result);
	}

	public static void InitList(Connection aConn) throws Exception {
		ObjectBase.CreateObject(aConn, WorkConfigWeek.class); // check if table already exist, if not create it
		Lookup.ClearAndLoadList(aConn, WorkConfigWeek.class, LookupList);

		FiveDayWeekSatSun = (WorkConfigWeek) Lookup.InsertDefaultList(aConn, FiveDayWeekSatSun, WorkConfigWeek.class, FiveDayWeekSatSunDescr, LookupList);
		FiveHalfDayWeekSun = (WorkConfigWeek) Lookup.InsertDefaultList(aConn, FiveHalfDayWeekSun, WorkConfigWeek.class, FiveHalfDayWeekSunDescr, LookupList);
	}

	public static void main(String[] args) {
		ObjectBase objectDb = new ObjectBase();
		Connection conn = null;
		try {

			String[] args1 = { BaseDb.PROPERTY_LOCATION_APP };
			objectDb.setupApp(args1);
			objectDb.setupDb();
			conn = objectDb.getConnPool().getConnection();

			// remove all working days for both WorkConfigWeek type to fix errors
			WorkConfigWeek.InitList(conn);

			WorkConfigWeek fiveDayWeekSatSun = WorkConfigWeek.FiveDayWeekSatSun;
			DeleteWorkConfigWeek(conn, fiveDayWeekSatSun);

			WorkConfigWeek fiveHalfDayWeekSun = WorkConfigWeek.FiveHalfDayWeekSun;
			DeleteWorkConfigWeek(conn, fiveHalfDayWeekSun);

		} catch (Exception ex) {
			App.logEror(ex, "Application is aborting..........");
		}
	}

	public static void DeleteWorkConfigWeek(Connection conn, WorkConfigWeek workConfigWeek) throws Exception {
		conn.setAutoCommit(false);
		workConfigWeek.getFieldObjectBox(WeekDay).resetIterator();
		while (workConfigWeek.getFieldObjectBox(WeekDay).hasNext(conn)) {
			WorkConfigDay configDay = (WorkConfigDay) workConfigWeek.getFieldObjectBox(WeekDay).getNext();
			configDay.setForDelete(true);
		}

		workConfigWeek.getFieldObjectBox(WeekDay).resetIterator();
		while (workConfigWeek.getFieldObjectBox(WeekDay).hasNext(conn)) {
			WorkConfigDay configDay = (WorkConfigDay) workConfigWeek.getFieldObjectBox(WeekDay).getNext();
			ObjectBase.DeleteNoCommit(conn, configDay);
		}
		ObjectBase.DeleteNoCommit(conn, workConfigWeek);
		conn.commit();
	}


}
