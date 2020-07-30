package biz.shujutech.bznes;

import biz.shujutech.db.relational.FieldType;
import biz.shujutech.db.object.Clasz;
import biz.shujutech.base.Connection;
import biz.shujutech.reflect.ReflectField;
import biz.shujutech.db.object.Lookup;

public class PersonGotMobile extends GotIntf {
	@ReflectField(type=FieldType.OBJECT, deleteAsMember = false, clasz="biz.shujutech.bznes.MobileTypePerson", displayPosition=0, lookup=true) public static String MobileType;
	@ReflectField(type=FieldType.OBJECT, deleteAsMember=true, clasz="biz.shujutech.bznes.MobilePhone", displayPosition=1, prefetch=true) public static String MobilePhone; 
	
	public MobilePhone getMobilePhone(Connection aConn) throws Exception {
		MobilePhone mobile = (MobilePhone) this.getFieldObject(MobilePhone).getValueObj(aConn);
		return(mobile);
	}
	
	public void setMobileNo(Connection aConn, String aCountryCode, String aNdc, String aSubscrNo) throws Exception {
		this.getMobilePhone(aConn).setMobileNo(aCountryCode, aNdc, aSubscrNo);
	}

	public String getMobileNo(Connection aConn) throws Exception {
		return(this.getMobilePhone(aConn).asString());
	}

	@Override
	public void setType(Lookup aValue) throws Exception {
		this.setValueObject(MobileType, (Clasz) aValue);
	}

	@Override
	public void setType(String aLookupName) throws Exception {
		Lookup chosen = MobileTypePerson.GetFromList(aLookupName);
		this.setType(chosen);
	}

	@Override
	public Lookup getType() throws Exception {
		MobileTypePerson type = (MobileTypePerson) this.getFieldObject(MobileType).getValueObj();
		return((Lookup) type);
	}

	@Override
	public String getDescr(Connection aConn) throws Exception {
		//return(((MobileTypePerson) this.getValueObject(aConn, MobileType)).getDescr());
		return(this.gotDescr(aConn, MobileType));
	}

	@Override
	public Object getValue() throws Exception {
		return((MobilePhone) this.getValueObject(MobilePhone));
	}

	@Override
	public Object getValue(Connection aConn) throws Exception {
		return((MobilePhone) this.getValueObject(aConn, MobilePhone));
	}

	@Override
	public Object createValue(Connection conn) throws Exception {
		Object mobile = this.getDb().createObject(biz.shujutech.bznes.MobilePhone.class, conn);
		this.setValueObject(MobilePhone, (Clasz) mobile);
		return(mobile);
	}

	@Override
	public void setValue(Object aObj) throws Exception {
		this.setValueObject(MobilePhone, (Clasz) aObj);
	}
}
