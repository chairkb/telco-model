
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

public class MobileTypePerson extends Clasz implements Lookup {
	@ReflectField(type=FieldType.STRING, size=32, indexes={@ReflectIndex(indexName="idx_mtp_descr", indexNo=0, indexOrder=SortOrder.ASC, isUnique=true)}) public static String Descr;  // short Descr gives error, its mysql reserve word
	public static CopyOnWriteArrayList<Lookup> LookupList = new CopyOnWriteArrayList<>();

	public static String PersonalDescr = "Personal";
	public static MobileTypePerson Personal = null;

	public static String WorkDescr = "Work";
	public static MobileTypePerson Work = null;

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
		ObjectBase.CreateObject(aConn, MobileTypePerson.class); // check if table already exist, if not create it
		Lookup.ClearAndLoadList(aConn, MobileTypePerson.class, LookupList);
		Personal = (MobileTypePerson) Lookup.InsertDefaultList(aConn, Personal, MobileTypePerson.class, PersonalDescr, LookupList);
		Work = (MobileTypePerson) Lookup.InsertDefaultList(aConn, Work, MobileTypePerson.class, WorkDescr, LookupList);
	}

	public static Lookup GetFromList(String aLookupName) throws Exception {
		MobileTypePerson result = null;
		if (Personal == null || Work == null) {
			throw new Hinderance("The InitList of MobileTypePerson must be called first");
		} else {
			if (aLookupName.toLowerCase().equals(PersonalDescr.toLowerCase())) {
				result = Personal;
			} else if (aLookupName.toLowerCase().equals(WorkDescr.toLowerCase())) {
				result = Work;
			}
		}
		return(result);
	}

	
}
