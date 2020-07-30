package biz.shujutech.bznes;

import biz.shujutech.db.relational.FieldType;
import biz.shujutech.db.object.ObjectBase;
import biz.shujutech.base.App;
import biz.shujutech.base.Connection;
import biz.shujutech.base.Hinderance;
import biz.shujutech.reflect.ReflectField;

public class Organization extends Contact implements Customer {

	@ReflectField(type=FieldType.OBJECTBOX, deleteAsMember=true, clasz="biz.shujutech.bznes.OrganizationGotAddr", displayPosition=5) public static String Address; 
	@ReflectField(type=FieldType.OBJECTBOX, deleteAsMember=true, clasz="biz.shujutech.bznes.OrganizationGotEmail", displayPosition=10) public static String Email; 
	@ReflectField(type=FieldType.OBJECTBOX, deleteAsMember=true, clasz="biz.shujutech.bznes.OrganizationGotMobile", displayPosition=15) public static String MobilePhone; 
	@ReflectField(type=FieldType.OBJECTBOX, deleteAsMember=true, clasz="biz.shujutech.bznes.OrganizationGotTelephone", displayPosition=20) public static String Telephone; 
	@ReflectField(type=FieldType.OBJECTBOX, deleteAsMember=true, clasz="biz.shujutech.bznes.OrganizationGotFax", displayPosition=25) public static String Fax; 

	public Organization() {
		super();
	}

	@Override
	public String getStaticEmail() throws Exception {
		return(Email);
	}

	@Override
	public Class getEmailClass() {
		return(OrganizationGotEmail.class);
	}

	@Override
	public String getStaticAddr() throws Exception {
		return(Address);
	}

	@Override
	public Class getAddrClass() {
		return(OrganizationGotAddr.class);
	}

	@Override
	public String getStaticMobilePhone() throws Exception {
		return(MobilePhone);
	}

	@Override
	public Class getMobilePhoneClass() {
		return(OrganizationGotMobile.class);
	}

	@Override
	public String getStaticTelephone() throws Exception {
		return(Telephone);
	}

	@Override
	public Class getTelephoneClass() {
		return(OrganizationGotTelephone.class);
	}

	public String getPreferedTelephone(Connection conn) throws Exception {
		Telephone telephone = this.getTelephone(conn, TelephoneTypeOrganization.Main);
		if (telephone != null) {
			return(telephone.asString());
		} else {
			telephone = this.getTelephone(conn, TelephoneTypeOrganization.Secondary);
			if (telephone != null) {
				return(telephone.asString());
			} else {
				return("");
			}
		}
	}

	public Addr getPreferedAddr(Connection conn) throws Exception {
		Addr addr = this.getAddr(conn, AddrTypeOrganization.Headquarter);
		if (addr != null) {
			return(addr);
		} else {
			addr = this.getAddr(conn, AddrTypeOrganization.Secondary);
			if (addr != null) {
				return(addr);
			} else {
				return(null);
			}
		}
	}

	public static void main(String args[]) {
		try {
			ObjectBase objectDatabase = new ObjectBase();
			objectDatabase.setupApp(args);
			objectDatabase.setupDb();

			{
				objectDatabase.createObject(Person.class);
			}
		} catch(Exception ex) {
			App.logEror(0, new Hinderance(ex, "Application encounter fatal error, application is aborting...."));
		}
	}

}
