package biz.shujutech.bznes;

import biz.shujutech.base.Hinderance;
import biz.shujutech.db.relational.FieldType;
import biz.shujutech.base.Connection;
import biz.shujutech.db.object.Clasz;
import javax.mail.internet.InternetAddress;
import biz.shujutech.reflect.ReflectField;
import biz.shujutech.db.object.Lookup;

public class PersonGotEmail extends GotIntf {
	@ReflectField(type=FieldType.OBJECT, deleteAsMember=false, clasz="biz.shujutech.bznes.EmailTypePerson", displayPosition=5, lookup=true) public static String EmailType;
	@ReflectField(type=FieldType.STRING, size=40, displayPosition=10) public static String Email;
	
	public PersonGotEmail() {}

	public void setEmailAddr(String aEmail) throws Exception {
		if (isValid(aEmail)) {
			this.setValueStr(Email, aEmail);
		} else {
			throw new Hinderance("Ïnvalid email address: " + aEmail);
		}
	}

	@Override
	public String asString() throws Exception {
		return(this.getEmailAddr());
	}

	@Override
	public String getValueStr() throws Exception {
		return(this.getValueStr(Email));
	}

	public String getEmailAddr() throws Exception {
		String result = this.getValueStr(Email);
		return(result);
	}

	public static boolean isValid(String email) {
		boolean result = true;
		try {
			InternetAddress emailAddr = new InternetAddress(email);
			emailAddr.validate();
		} catch (Exception ex) {
			result = false;
		}
		return result;
	}

	@Override
	public void setType(Lookup aValue) throws Exception {
		this.setValueObject(EmailType, (Clasz) aValue);
	}

	@Override
	public void setType(String aLookupName) throws Exception {
		Lookup chosen = EmailTypePerson.GetFromList(aLookupName);
		this.setType(chosen);
	}

	@Override
	public Lookup getType() throws Exception {
		EmailTypePerson type = (EmailTypePerson) this.getFieldObject(EmailType).getValueObj();
		return((Lookup) type);
	}

	@Override
	public String getDescr(Connection aConn) throws Exception {
		//return(((EmailTypePerson) this.getValueObject(aConn, EmailType)).getDescr());
		return(this.gotDescr(aConn, EmailType));
	}

	@Override
	public Object getValue() throws Exception {
		Object email = this.getEmailAddr();
		return(email);
	}

	@Override
	public Object getValue(Connection aConn) throws Exception {
		return(this.getValue());
	}

	@Override
	public Object createValue(Connection conn) throws Exception {
		String email = new String("");
		this.setValueStr(Email, email);
		return(email);
	}

	@Override
	public void setValue(Object aObject) throws Exception {
		this.setValueStr(Email, (String) aObject);
	}
}
