package biz.shujutech.bznes;

import biz.shujutech.db.object.Clasz;
import biz.shujutech.db.relational.FieldType;
import biz.shujutech.base.Connection;
import biz.shujutech.reflect.ReflectField;
import biz.shujutech.db.object.Lookup;

public class PersonGotTelephone extends GotIntf {
	@ReflectField(type=FieldType.OBJECT, deleteAsMember = false, clasz="biz.shujutech.bznes.TelephoneTypePerson", displayPosition=0, lookup=true) public static String TelephoneType;
	@ReflectField(type=FieldType.OBJECT, deleteAsMember=true, clasz="biz.shujutech.bznes.Telephone", displayPosition=1) public static String Telephone; 
	
	public Telephone getTelephone() throws Exception {
		Telephone telephone = (Telephone) this.getFieldObject(Telephone).getValueObj();
		return(telephone);
	}
	
	public void setPhoneNo(String aCountryCode, String aAreaCode, String aSubscrNo) throws Exception {
		this.getTelephone().setTelephoneNo(aCountryCode, aAreaCode, aSubscrNo);
	}

	public String getPhoneNo() throws Exception {
		return(this.getTelephone().asString());
	}

	@Override
	public void setType(Lookup aValue) throws Exception {
		this.setValueObject(TelephoneType, (Clasz) aValue);
	}

	@Override
	public void setType(String aLookupName) throws Exception {
		Lookup chosen = TelephoneTypePerson.GetFromList(aLookupName);
		this.setType(chosen);
	}

	@Override
	public Lookup getType() throws Exception {
		TelephoneTypePerson type = (TelephoneTypePerson) this.getFieldObject(TelephoneType).getValueObj();
		return((Lookup) type);
	}

	@Override
	public String getDescr(Connection aConn) throws Exception {
		//return(((TelephoneTypePerson) this.getValueObject(aConn, TelephoneType)).getDescr());
		return(this.gotDescr(aConn, TelephoneType));
	}

	@Override
	public Object getValue() throws Exception {
		return((Telephone) this.getValueObject(Telephone));
	}

	@Override
	public Object getValue(Connection aConn) throws Exception {
		return((Telephone) this.getValueObject(aConn, Telephone));
	}

	@Override
	public Object createValue(Connection conn) throws Exception {
		Object telephone = this.getDb().createObject(biz.shujutech.bznes.Telephone.class, conn);
		this.setValueObject(Telephone, (Clasz) telephone);
		return(telephone);
	}
	
	@Override
	public void setValue(Object aObj) throws Exception {
		this.setValueObject(Telephone, (Clasz) aObj);
	}
}
