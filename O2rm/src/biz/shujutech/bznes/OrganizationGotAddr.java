package biz.shujutech.bznes;

import biz.shujutech.base.Connection;
import biz.shujutech.base.Hinderance;
import biz.shujutech.db.object.Clasz;
import biz.shujutech.db.object.ObjectBase;
import biz.shujutech.db.relational.FieldType;
import biz.shujutech.reflect.ReflectField;
import biz.shujutech.db.object.Lookup;

public class OrganizationGotAddr extends GotIntf {
	@ReflectField(type=FieldType.OBJECT, deleteAsMember=false, clasz="biz.shujutech.bznes.AddrTypeOrganization", displayPosition=0, lookup=true) public static String AddrType; 
	@ReflectField(type=FieldType.OBJECT, deleteAsMember=true, clasz="biz.shujutech.bznes.Addr", displayPosition=1) public static String Addr; 

	public OrganizationGotAddr() {
		super();
	}

	public void setType(ObjectBase aDb, String aValue) throws Exception {
		AddrTypeOrganization addrType = null;
		if (aValue.equals(AddrTypeOrganization.HeadquarterDescr)) {
			addrType = AddrTypeOrganization.Headquarter;
		} else if (aValue.equals(AddrTypeOrganization.SecondaryDescr)) {
			addrType = AddrTypeOrganization.Secondary;
		} else {
			throw new Hinderance("The person address type: " + aValue.toUpperCase() + ", is not a pre-define type");
		}
		this.setValueObject(AddrType, addrType);
	}

	@Override
	public void validateBeforePersist(Connection aConn) throws Exception {
		String strType;
		if (this.getDescr(aConn).isEmpty() == false) {
			strType = this.getDescr(aConn);
		} else {
			strType = this.getAddr(aConn).getType().getDescr();
		}

		if (this.getType(aConn).isPopulated() == false) {
			AddrTypeOrganization fetchCriteria = (AddrTypeOrganization) Clasz.CreateObject(this.getDb(), aConn, AddrTypeOrganization.class); 
			fetchCriteria.setDescr(this.getDescr(aConn));
			AddrTypeOrganization addrType = (AddrTypeOrganization) Clasz.Fetch(aConn, fetchCriteria);
			if (addrType != null && addrType.isPopulated()) {
				this.setType(addrType);
			} else {
				throw new Hinderance("Cannot persist object: " + this.getClaszName().toUpperCase() + ", invalid address type: " + strType);
			}
		}
	}

	public void setType(AddrTypeOrganization aValue) throws Exception {
		this.setValueObject(AddrType, aValue);
	}

	public void setType(String aValue) throws Exception {
		Lookup lookup = AddrTypeOrganization.GetFromList(aValue);
		this.setType(lookup);
	}

	public Addr getAddr(Connection aConn) throws Exception {
		Addr addr = (Addr) this.getFieldObject(Addr).getValueObj(aConn);
		return(addr);
	}

	@Override
	public AddrTypeOrganization getType() throws Exception {
		AddrTypeOrganization type = (AddrTypeOrganization) this.getFieldObject(AddrType).getValueObj();
		return(type);
	}

	public AddrTypeOrganization getType(Connection aConn) throws Exception {
		AddrTypeOrganization type = (AddrTypeOrganization) this.getFieldObject(AddrType).getValueObj(aConn);
		return(type);
	}

	@Override
	public String getDescr(Connection aConn) throws Exception {
		//return(((AddrTypeOrganization) this.getValueObject(aConn, AddrType)).getDescr());
		return(this.gotDescr(aConn, AddrType));
	}

	public Addr getAddr() throws Exception {
		Addr addr = (Addr) this.getFieldObject(Addr).getValueObj();
		return(addr);
	}

	@Override
	public String getValueStr() throws Exception {
		String result = "";
		if (this.getType().getDescr().equals(AddrTypeOrganization.HeadquarterDescr) || this.getType().getDescr().equals(AddrTypeOrganization.Secondary)) {
			result = this.getAddr().getValueStr();
		}
		return(result);
	}

	@Override
	public void setType(Lookup aValue) throws Exception {
		this.setValueObject(AddrType, (Clasz) aValue);
	}

	@Override
	public Clasz getValue() throws Exception {
		Addr addr = (Addr) this.gotValueObject(Addr);
		return(addr);
	}

	@Override
	public Clasz getValue(Connection aConn) throws Exception {
		Addr addr = (Addr) this.gotValueObject(aConn, Addr);
		return(addr);
	}

	@Override
	public Object createValue(Connection conn) throws Exception {
		Object addr = this.getDb().createObject(biz.shujutech.bznes.Addr.class, conn);
		this.setValueObject(Addr, (Clasz) addr);
		return(addr);
	}

	@Override
	public void setValue(Object aObj) throws Exception {
		this.setValueObject(Addr, (Clasz) aObj);
	}
}
