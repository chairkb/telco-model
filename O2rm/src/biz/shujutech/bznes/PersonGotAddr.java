package biz.shujutech.bznes;

import biz.shujutech.db.object.Clasz;
import biz.shujutech.db.object.ObjectBase;
import biz.shujutech.db.relational.FieldType;
import biz.shujutech.base.Connection;
import biz.shujutech.reflect.ReflectField;
import biz.shujutech.db.object.Lookup;

public class PersonGotAddr extends GotIntf {
	@ReflectField(type=FieldType.OBJECT, displayPosition=0, deleteAsMember=false, clasz="biz.shujutech.bznes.AddrTypePerson", lookup=true) public static String AddrType; 
	@ReflectField(type=FieldType.OBJECT, displayPosition=4, deleteAsMember=true, clasz="biz.shujutech.bznes.Addr") public static String Addr; 

	public PersonGotAddr() {
		super();
	}

	/*
	public void setType(ObjectBase aDb, String aValue) throws Exception {
		AddrTypePerson addrType = null;
		if (aValue.equals(AddrTypePerson.HomeDescr)) {
			addrType = AddrTypePerson.Home;
		} else if (aValue.equals(AddrTypePerson.OfficeDescr)) {
			addrType = AddrTypePerson.Office;
		} else {
			throw new Hinderance("The person address type: " + aValue.toUpperCase() + ", is not a pre-define type");
		}
		this.setValueObject(AddrType, addrType);
	}
	*/

	/**
	 * Before a Got interface type can be persistCommit, the type must first be retrieve
 	 * from db. KIV, may re-factor this since all Got interface, must validate its
	 * type...
	 * 
	 * @param aConn
	 * @throws Exception 
	 */
	@Override
	public void validateBeforePersist(Connection aConn) throws Exception {
		/*
		String strType;
		if (this.getDescr(aConn).isEmpty() == false) {
			strType = this.getDescr(aConn);
		} else {
			strType = this.getAddr(aConn).getType().getDescr();
		}

		if (this.getType(aConn).isPopulated() == false) {
			AddrTypePerson fetchCriteria = (AddrTypePerson) Clasz.createValue(this.getDb(), aConn, AddrTypePerson.class); 
			fetchCriteria.setDescr(strType);
			AddrTypePerson addrType = (AddrTypePerson) Clasz.Fetch(aConn, fetchCriteria);
			if (addrType != null && addrType.isPopulated()) {
				this.setType(addrType);
			} else {
				throw new Hinderance("Cannot persistCommit object: " + this.getClaszName().toUpperCase() + ", invalid address type: " + strType);
			}
		}
		*/
	}

	@Override
	public void setType(Lookup aValue) throws Exception {
		this.setValueObject(AddrType, (Clasz) aValue);
	}

	@Override
	public void setType(String aLookupName) throws Exception {
		Lookup chosen = AddrTypePerson.GetFromList(aLookupName);
		this.setType(chosen);
	}

	public void setAddr(Addr aValue) throws Exception {
		this.setValueObject(Addr, aValue);
	}

	@Override
	public AddrTypePerson getType() throws Exception {
		AddrTypePerson type = (AddrTypePerson) this.getFieldObject(AddrType).getValueObj();
		return(type);
	}

	public AddrTypePerson getType(Connection aConn) throws Exception {
		AddrTypePerson type = (AddrTypePerson) this.getFieldObject(AddrType).getValueObj(aConn);
		return(type);
	}

	@Override
	public String getDescr(Connection aConn) throws Exception {
		return(this.gotDescr(aConn, AddrType));
	}

	public Addr getAddr() throws Exception {
		Addr addr = (Addr) this.getField(PersonGotAddr.Addr).getValueObj();
		return(addr);
	}

	public Addr getAddr(Connection aConn) throws Exception {
		Addr addr = (Addr) this.getField(PersonGotAddr.Addr).getValueObj(aConn);
		return(addr);
	}

	@Override
	public String getValueStr() throws Exception {
		String result = "";
		if (this.getType().getDescr().equals(AddrTypePerson.HomeDescr) || this.getType().getDescr().equals(AddrTypePerson.OfficeDescr)) {
			result = this.getAddr().getValueStr();
		}
		return(result);
	}

	@Override
	public Object getValue() throws Exception {
		Addr addr = (Addr) this.getField(PersonGotAddr.Addr).getValueObj();
		return(addr);
	}

	@Override
	public Object getValue(Connection aConn) throws Exception {
		Addr addr = (Addr) this.getField(PersonGotAddr.Addr).getValueObj(aConn);
		return(addr);
	}

	@Override
	public Object createValue(Connection conn) throws Exception {
		Object addr = ObjectBase.CreateObject(conn, Addr.class);
		this.setValueObject(Addr, (Clasz) addr);
		return(addr);
	}

	@Override
	public void setValue(Object aObj) throws Exception {
		this.setValueObject(Addr, (Clasz) aObj);
	}


}
