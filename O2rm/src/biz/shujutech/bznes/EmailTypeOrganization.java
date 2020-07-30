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

public class EmailTypeOrganization extends Clasz implements Lookup {
	@ReflectField(type=FieldType.STRING, size=32, indexes={@ReflectIndex(indexName="idx_eto_descr", indexNo=0, indexOrder=SortOrder.ASC, isUnique=true)}) public static String Descr;  // short Descr gives error, its mysql reserve word
	public static CopyOnWriteArrayList<Lookup> LookupList = new CopyOnWriteArrayList<>();

	public static String MainDescr = "Main";
	public static EmailTypeOrganization Main = null;

	public static String SalesDescr = "Sales";
	public static EmailTypeOrganization Sales = null;

	public static String SupportDescr = "Support";
	public static EmailTypeOrganization Support = null;

	public static String HumanResourceDescr = "Human Resource";
	public static EmailTypeOrganization HumanResource = null;

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
		ObjectBase.CreateObject(aConn, EmailTypeOrganization.class); // check if table already exist, if not create it
		Lookup.ClearAndLoadList(aConn, EmailTypeOrganization.class, LookupList);
		Main = (EmailTypeOrganization) Lookup.InsertDefaultList(aConn, Main, EmailTypeOrganization.class, MainDescr, LookupList);
		Sales = (EmailTypeOrganization) Lookup.InsertDefaultList(aConn, Sales, EmailTypeOrganization.class, SalesDescr, LookupList);
		Support = (EmailTypeOrganization) Lookup.InsertDefaultList(aConn, Support, EmailTypeOrganization.class, SupportDescr, LookupList);
		HumanResource = (EmailTypeOrganization) Lookup.InsertDefaultList(aConn, HumanResource, EmailTypeOrganization.class, HumanResourceDescr, LookupList);
	}

	public static Lookup GetFromList(String aLookupName) throws Exception {
		EmailTypeOrganization result = null;
		if (Main == null || Sales == null && Sales == null && HumanResource == null) {
			throw new Hinderance("The InitList of Gender must be called first");
		} else {
			if (aLookupName.toLowerCase().equals(MainDescr.toLowerCase())) {
				result = Main;
			} else if (aLookupName.toLowerCase().equals(SalesDescr.toLowerCase())) {
				result = Sales;
			} else if (aLookupName.toLowerCase().equals(SupportDescr.toLowerCase())) {
				result = Support;
			} else if (aLookupName.toLowerCase().equals(HumanResourceDescr.toLowerCase())) {
				result = HumanResource;
			}
		}
		return(result);
	}
}
