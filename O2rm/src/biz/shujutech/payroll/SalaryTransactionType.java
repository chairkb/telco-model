package biz.shujutech.payroll;

import biz.shujutech.base.Connection;
import biz.shujutech.db.object.Clasz;
import biz.shujutech.db.object.Lookup;
import biz.shujutech.db.relational.FieldType;
import biz.shujutech.db.relational.SortOrder;
import biz.shujutech.reflect.ReflectField;
import biz.shujutech.reflect.ReflectIndex;
import java.util.concurrent.CopyOnWriteArrayList;

public class SalaryTransactionType extends Clasz implements Lookup {
	@ReflectField(type=FieldType.STRING, size=16, indexes={@ReflectIndex(indexName="idx_stt_descr", indexNo=0, indexOrder=SortOrder.ASC, isUnique=true)}) public static String Descr;
	public static CopyOnWriteArrayList<Lookup> LookupList = new CopyOnWriteArrayList<>();

	@Override
	public String getDescr() throws Exception {
		return(this.getValueStr(Descr));
	}

	@Override
	public void setDescr(String aDescr) throws Exception {
		this.setValueStr(Descr, aDescr);
	}

	@Override
	public CopyOnWriteArrayList<Lookup> getLookupList() throws Exception {
		return(LookupList);
	}

	@Override
	public void initialize(Connection aConn) throws Exception {
		// do nothing
	}

	@Override
	public String getValueStr() throws Exception { // this is also needed to be abstract actually
		return(this.getDescr());
	}
	
}
