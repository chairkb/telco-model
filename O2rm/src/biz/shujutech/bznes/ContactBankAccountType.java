package biz.shujutech.bznes;

import biz.shujutech.base.Connection;
import biz.shujutech.base.Hinderance;
import biz.shujutech.db.object.Clasz;
import biz.shujutech.db.object.ObjectBase;
import biz.shujutech.db.relational.FieldType;
import biz.shujutech.db.relational.SortOrder;
import java.util.concurrent.CopyOnWriteArrayList;
import biz.shujutech.reflect.ReflectField;
import biz.shujutech.reflect.ReflectIndex;
import biz.shujutech.db.object.Lookup;

/**
 *
 */
public class ContactBankAccountType extends Clasz implements Lookup {
	@ReflectField(type=FieldType.STRING, size=32, indexes={@ReflectIndex(indexName="idx_cbat_descr", indexNo=0, indexOrder=SortOrder.ASC, isUnique=true)}) public static String Descr;  // short Descr gives error, its mysql reserve word
	public static CopyOnWriteArrayList<Lookup> LookupList = new CopyOnWriteArrayList<>();
	
	public static String PayrollDescr = "Payroll";
	public static ContactBankAccountType Payroll = null;

	@Override
	public void initialize(Connection aConn) throws Exception {
		// do nothing
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
		ObjectBase.CreateObject(aConn, ContactBankAccountType.class); // check if table already exist, if not create it
		Lookup.ClearAndLoadList(aConn, ContactBankAccountType.class, LookupList);
		Payroll = (ContactBankAccountType) Lookup.InsertDefaultList(aConn, Payroll, ContactBankAccountType.class, PayrollDescr, LookupList);
	}

	public static Lookup GetFromList(String aLookupName) throws Exception {
		ContactBankAccountType result = null;
		if (Payroll == null) {
			throw new Hinderance("The InitList of ContactBankAccountType must be called first");
		} else {
			if (aLookupName.toLowerCase().equals(PayrollDescr.toLowerCase())) {
				result = Payroll;
			}
		}
		return(result);
	}
}
