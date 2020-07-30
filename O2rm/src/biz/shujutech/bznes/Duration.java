package biz.shujutech.bznes;

import biz.shujutech.base.Connection;
import biz.shujutech.db.object.Clasz;
import biz.shujutech.db.object.ObjectBase;
import biz.shujutech.db.relational.FieldType;
import biz.shujutech.db.relational.SortOrder;
import java.util.concurrent.CopyOnWriteArrayList;
import biz.shujutech.reflect.ReflectField;
import biz.shujutech.reflect.ReflectIndex;
import biz.shujutech.db.object.Lookup;

public class Duration extends Clasz implements Lookup {
	@ReflectField(type=FieldType.STRING, size=32, indexes={@ReflectIndex(indexName="idx_drtn_descr", indexNo=0, indexOrder=SortOrder.ASC, isUnique=true)}) public static String Descr;  // short Descr gives error, its mysql reserve word
	public static CopyOnWriteArrayList<Lookup> LookupList = new CopyOnWriteArrayList<>();

	public static String MonthDescr = "Monthly";
	public static Duration Month = null;

	public static String WeekDescr = "Weekly";
	public static Duration Week = null;

	public static String BiweekDescr = "Biweekly";
	public static Duration Biweek = null;

	public static String HourDescr = "Hourly";
	public static Duration Hour = null;

	@Override
	public void initialize(Connection aConn) throws Exception {
		// do nothing
	}

	public Duration() {
		super(); // do this, else the error java.lang.InstantiationException
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

	public static void InitList(Connection aConn) throws Exception {
		ObjectBase.CreateObject(aConn, Duration.class); // check if table already exist, if not create it
		Lookup.ClearAndLoadList(aConn, Duration.class, LookupList);
		Month = (Duration) Lookup.InsertDefaultList(aConn, Month, Duration.class, MonthDescr, LookupList);
		Week = (Duration) Lookup.InsertDefaultList(aConn, Week, Duration.class, WeekDescr, LookupList);
		Biweek = (Duration) Lookup.InsertDefaultList(aConn, Biweek, Duration.class, BiweekDescr, LookupList);
		Hour = (Duration) Lookup.InsertDefaultList(aConn, Hour, Duration.class, HourDescr, LookupList);
	}
}
