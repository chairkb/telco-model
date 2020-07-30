package biz.shujutech.bznes;

import biz.shujutech.db.object.Clasz;
import biz.shujutech.db.relational.FieldType;
import biz.shujutech.base.Connection;
import biz.shujutech.reflect.ReflectField;
import biz.shujutech.db.object.Lookup;

public class OrganizationGotFax extends GotIntf {
	@ReflectField(type=FieldType.OBJECT, deleteAsMember = false, clasz="biz.shujutech.bznes.TelephoneTypeOrganization", displayPosition=0, lookup=true) public static String FaxType;
	@ReflectField(type=FieldType.OBJECT, deleteAsMember=true, clasz="biz.shujutech.bznes.Telephone", displayPosition=1) public static String Fax; 
	
	public Telephone getFax() throws Exception {
		Telephone telephone = (Telephone) this.getFieldObject(Fax).getValueObj();
		return(telephone);
	}
	
	public void setFaxNo(String aCountryCode, String aAreaCode, String aSubscrNo) throws Exception {
		this.getFax().setTelephoneNo(aCountryCode, aAreaCode, aSubscrNo);
	}

	public String getFaxNo() throws Exception {
		return(this.getFax().asString());
	}

	@Override
	public void setType(Lookup aValue) throws Exception {
		this.setValueObject(FaxType, (Clasz) aValue);
	}

	@Override
	public void setType(String aLookupName) throws Exception {
		Lookup chosen = TelephoneTypeOrganization.GetFromList(aLookupName);
		this.setType(chosen);
	}

	@Override
	public Lookup getType() throws Exception {
		TelephoneTypeOrganization type = (TelephoneTypeOrganization) this.getFieldObject(FaxType).getValueObj();
		return(type);
	}

	@Override
	public String getDescr(Connection aConn) throws Exception {
		//return(((TelephoneTypeOrganization) this.getValueObject(aConn, FaxType)).getDescr());
		return(this.gotDescr(aConn, FaxType));
	}

	@Override
	public Object getValue() throws Exception {
		return((Telephone) this.getValueObject(Fax));
	}

	@Override
	public Object getValue(Connection aConn) throws Exception {
		return((Telephone) this.getValueObject(aConn, Fax));
	}

	@Override
	public Object createValue(Connection conn) throws Exception {
		Object telephone = this.getDb().createObject(biz.shujutech.bznes.Telephone.class, conn);
		this.setValueObject(Fax, (Clasz) telephone);
		return(telephone);
	}
	
	@Override
	public void setValue(Object aObj) throws Exception {
		this.setValueObject(Fax, (Clasz) aObj);
	}
	
}
