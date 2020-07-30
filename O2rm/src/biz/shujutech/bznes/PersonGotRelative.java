package biz.shujutech.bznes;

import biz.shujutech.db.relational.FieldType;
import biz.shujutech.db.object.Clasz;
import biz.shujutech.base.Connection;
import biz.shujutech.reflect.ReflectField;
import biz.shujutech.db.object.Lookup;

public class PersonGotRelative extends GotIntf {
	@ReflectField(type=FieldType.OBJECT, deleteAsMember = false, clasz="biz.shujutech.bznes.Relationship", displayPosition=0, lookup=true) public static String Relationship;
	@ReflectField(type=FieldType.OBJECT, deleteAsMember=false, clasz="biz.shujutech.bznes.Person", displayPosition=1) public static String Relative; 
	
	public Person getRelative(Connection aConn) throws Exception {
		Person relative = (Person) this.getFieldObject(Relative).getValueObj(aConn);
		return(relative);
	}
	
	@Override
	public void setType(Lookup aValue) throws Exception {
		this.setValueObject(Relationship, (Clasz) aValue);
	}

	@Override
	public void setType(String aLookupName) throws Exception {
		Lookup chosen = biz.shujutech.bznes.Relationship.GetFromList(aLookupName);
		this.setType(chosen);
	}

	@Override
	public Lookup getType() throws Exception {
		Relationship type = (Relationship) this.getFieldObject(Relationship).getValueObj();
		return(type);
	}

	@Override
	public String getDescr(Connection aConn) throws Exception {
		//return(((Relationship) this.getValueObject(aConn, Relationship)).getDescr());
		return(this.gotDescr(aConn, Relationship));
	}

	@Override
	public Object getValue() throws Exception {
		return((Person) this.getValueObject(Relative));
	}

	@Override
	public Object getValue(Connection aConn) throws Exception {
		return((Person) this.getValueObject(aConn, Relative));
	}

	@Override
	public Object createValue(Connection conn) throws Exception {
		Object relative = this.getDb().createObject(biz.shujutech.bznes.Person.class, conn);
		this.setValueObject(Relative, (Clasz) relative);
		//this.createFieldObject(conn);
		return(relative);
	}

	@Override
	public void setValue(Object aObj) throws Exception {
		this.setValueObject(Relative, (Clasz) aObj);
	}

	/*
	public void createFieldObject(Connection conn) throws Exception {
		this.getRelative().createContactField(conn);
		this.getRelative().removeField(BirthDate);
		this.getRelative().removeField(Gender);
		this.getRelative().removeField(Nationality);
		this.getRelative().removeField(PassportNo);
		this.getRelative().removeField(Ethnic);
		this.getRelative().removeField(Religion);

		this.getRelative().removeField(MaritalStatus);
		this.getRelative().removeField(NoOfChildren);
		this.getRelative().removeField(Employment);
		this.getRelative().removeField(Children);
		this.getRelative().removeField(Spouse);
		this.getRelative().removeField(EmergencyContact);
	}
*/
}
