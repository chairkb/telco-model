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

public class AddrTypePerson extends Clasz implements Lookup {
	@ReflectField(type=FieldType.STRING, size=32, indexes={@ReflectIndex(indexName="idx_atp_descr", indexNo=0, indexOrder=SortOrder.ASC, isUnique=true)}) public static String Descr;  // short Descr gives error, its mysql reserve word
	public static CopyOnWriteArrayList<Lookup> LookupList = new CopyOnWriteArrayList<>();

	public static String HomeDescr = "Home";
	public static AddrTypePerson Home = null;

	public static String OfficeDescr = "Office";
	public static AddrTypePerson Office = null;

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
		ObjectBase.CreateObject(aConn, AddrTypePerson.class); // check if table already exist, if not create it
		Lookup.ClearAndLoadList(aConn, AddrTypePerson.class, LookupList);
		Home = (AddrTypePerson) Lookup.InsertDefaultList(aConn, Home, AddrTypePerson.class, HomeDescr, LookupList);
		Office = (AddrTypePerson) Lookup.InsertDefaultList(aConn, Office, AddrTypePerson.class, OfficeDescr, LookupList);
	}

	public static Lookup GetFromList(String aLookupName) throws Exception {
		AddrTypePerson result = null;
		if (Home == null || Office == null) {
			throw new Hinderance("The InitList of Gender must be called first");
		} else {
			if (aLookupName.toLowerCase().equals(HomeDescr.toLowerCase())) {
				result = Home;
			} else if (aLookupName.toLowerCase().equals(OfficeDescr.toLowerCase())) {
				result = Office;
			}
		}
		return(result);
	}
}