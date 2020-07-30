package biz.shujutech.bznes;

import biz.shujutech.base.Connection;
import biz.shujutech.db.object.FieldObjectBox;
import biz.shujutech.db.object.Clasz;
import biz.shujutech.db.relational.FieldType;
import biz.shujutech.base.Hinderance;
import biz.shujutech.reflect.ReflectField;
import biz.shujutech.db.object.Lookup;
import biz.shujutech.db.object.ObjectBase;
import biz.shujutech.db.relational.SortOrder;
import biz.shujutech.reflect.ReflectIndex;
import biz.shujutech.technical.LambdaObject;

public class Contact extends Clasz {
	@ReflectField(type=FieldType.STRING, size=64, indexes={@ReflectIndex(indexName="idx_contact_namealias", indexNo=0, indexOrder=SortOrder.ASC, isUnique=true)}, updateable=false, displayPosition=5) public static String Name;
	@ReflectField(type=FieldType.STRING, size=32, indexes={@ReflectIndex(indexName="idx_contact_namealias", indexNo=1, indexOrder=SortOrder.ASC, isUnique=true)}, updateable=false, displayPosition=10) public static String Alias;
	@ReflectField(type=FieldType.STRING, size=64, displayPosition=15) public static String Website;
	@ReflectField(type=FieldType.OBJECTBOX, clasz="biz.shujutech.bznes.Contact", polymorphic=true, displayPosition=30) public static String Customer;

	public Class getAddrClass() throws Exception { throw new Hinderance("The getAddrClass must be implemented by subclass"); };
	public String getStaticAddr() throws Exception { throw new Hinderance("The getAddrClass must be implemented by subclass"); }; 

	public Class getEmailClass() throws Exception { throw new Hinderance("The getEmailClass must be implemented by subclass"); }; 
	public String getStaticEmail() throws Exception { throw new Hinderance("The getStaticEmail must be implemented by subclass"); }; 

	public Class getMobilePhoneClass() throws Exception { throw new Hinderance("The getMobilePhoneClass must be implemented by subclass"); };
	public String getStaticMobilePhone() throws Exception { throw new Hinderance("The getStaticMobilePhone must be implemented by subclass"); };

	public Class getTelephoneClass() throws Exception { throw new Hinderance("The getTelephoneClass must be implemented by subclass"); };
	public String getStaticTelephone() throws Exception { throw new Hinderance("The getStaticTelephon must be implemented by subclass"); }; 

	public void setName(String aValue) throws Exception {
		this.setValueStr(Name, aValue);
	}

	public String getName() throws Exception {
		return(this.getValueStr(Name));
	}

	public void setAlias(String aValue) throws Exception {
		this.setValueStr(Alias, aValue);
	}

	public String getAlias() throws Exception {
		return (this.getValueStr(Alias));
	}

	public void setWebsite(String aValue) throws Exception {
		this.setValueStr(Website, aValue);
	}

	public String getWebsite() throws Exception {
		return(this.getValueStr(Website));
	}

	public void createContactField(Connection conn) throws Exception {
		this.addEmail(conn);
		this.addMobilePhone(conn);
		this.addTelephone(conn);
		this.addAddr(conn);
	}

	//
	// email
	//
	public GotIntf addEmail(Connection conn) throws Exception {
		GotIntf gotObj = this.addGot(conn, this.getEmailClass(), this.getEmailBox(), this.getStaticEmail());
		return(gotObj);
	}

	public GotIntf addEmail(Connection aConn, Lookup aType, String aEmail) throws Exception {
		GotIntf gotObj = this.addGot(aConn, this.getEmailClass(), aType, this.getEmailBox(), this.getStaticEmail());
		gotObj.setValue(aEmail); // need to setObj as type, value, the value is String which cannot be change by modifying its object
		return(gotObj);
	}

	public String getEmail(Lookup aType) throws Exception {
		Connection conn = this.getDb().connPool.getConnection();
		try {
			return(this.getEmail(conn, aType));
		} finally {
			if (conn != null) {
				this.getDb().connPool.freeConnection( conn);
			}
		}		
	}

	public String getEmail(Connection aConn, Lookup aType) throws Exception {
		String result = (String) this.getGotObj(aConn, aType, this.getEmailBox());
		return(result);
	}

	public FieldObjectBox getEmailBox() throws Exception {
		return((FieldObjectBox) this.getField(this.getStaticEmail()));
	}

	//
	// mobile phone
	//
	public GotIntf addMobilePhone(Connection conn) throws Exception {
		GotIntf gotObj = this.addGot(conn, this.getMobilePhoneClass(), this.getMobilePhoneBox(), this.getStaticMobilePhone());
		return(gotObj);
	}

	public void addMobilePhone(Lookup aType, String aCallDesc, String aNdc, String aSubscrNo) throws Exception {
		Connection conn = this.getDb().connPool.getConnection();
		try {
			this.addMobilePhone(conn, aType, aCallDesc, aNdc, aSubscrNo);
		} finally {
			if (conn != null) {
				this.getDb().connPool.freeConnection( conn);
			}
		}		
	}

	public void addMobilePhone(Connection aConn, Lookup aType, String aCallDesc, String aNdc, String aSubscrNo) throws Exception {
		this.addGot(aConn, this.getMobilePhoneClass(), aType, this.getMobilePhoneBox(), this.getStaticMobilePhone());
		MobilePhone mobilePhone = null;
		mobilePhone = this.getMobilePhone(aConn, aType);
		mobilePhone.setCountryCallingCode(aCallDesc);
		mobilePhone.setNdc(aNdc);
		mobilePhone.setSubscrNo(aSubscrNo);
		// don't need to setObj as return object is pass by reference
	}

	public MobilePhone getMobilePhone(Connection conn, Lookup aType) throws Exception {
		MobilePhone result = (MobilePhone) this.getGotObj(conn, aType, this.getMobilePhoneBox());
		return(result);
	}

	public FieldObjectBox getMobilePhoneBox() throws Exception {
		return((FieldObjectBox) this.getField(this.getStaticMobilePhone()));
	}

	//
	// telephone 
	//
	public GotIntf addTelephone(Connection conn) throws Exception {
		GotIntf gotObj = this.addGot(conn, this.getTelephoneClass(), this.getTelephoneBox(), this.getStaticTelephone());
		return(gotObj);
	}

	public void addTelephone(Lookup aType, String aCallDesc, String aAreaCode, String aSubscrNo) throws Exception {
		Connection conn = this.getDb().connPool.getConnection();
		try {
			this.addTelephone(conn, aType, aCallDesc, aAreaCode, aSubscrNo);
		} finally {
			if (conn != null) {
				this.getDb().connPool.freeConnection( conn);
			}
		}		
	}

	public void addTelephone(Connection conn, Lookup aType, String aCallDesc, String aAreaCode, String aSubscrNo) throws Exception {
		this.addGot(conn, this.getTelephoneClass(), aType, this.getTelephoneBox(), this.getStaticTelephone());
		Telephone telephone = this.getTelephone(conn, aType);
		telephone.setCountryCallingCode(aCallDesc);
		telephone.setAreaCode(aAreaCode);
		telephone.setSubscrNo(aSubscrNo);
	}

	public Telephone getTelephone(Connection conn, Lookup aType) throws Exception {
		Telephone result = (Telephone) this.getGotObj(conn, aType, this.getTelephoneBox());
		return(result);
	}

	public FieldObjectBox getTelephoneBox() throws Exception {
		return((FieldObjectBox) this.getField(this.getStaticTelephone()));
	}

	//
	// address
	//
	public GotIntf addAddr(Connection conn) throws Exception {
		GotIntf gotObj = this.addGot(conn, this.getAddrClass(), this.getAddressBox(), this.getStaticAddr());
		return(gotObj);
	}

	public Addr addAddr(Connection aConn, Lookup aAddrType) throws Exception {
		this.addGot(aConn, this.getAddrClass(), aAddrType, this.getAddressBox(), this.getStaticAddr());
		Addr result = this.getAddr(aConn, aAddrType);
		return(result);
	}

	public Addr getAddr(Connection aConn, long aIndex) throws Exception {
		GotIntf got = (GotIntf) this.getValueObject(this.getStaticAddr(), aIndex);
		Addr result = (Addr) got.getValue();
		if (got.getType() != null && got.getType().getDescr().isEmpty() == false) {
			result.setType(got.getType());
		}
		return(result);
	}

	public Addr getAddr(Lookup aAddrType) throws Exception {
		Connection conn = this.getDb().connPool.getConnection();
		try {
			return(this.getAddr(conn, aAddrType));
		} finally {
			if (conn != null) {
				this.getDb().connPool.freeConnection( conn);
			}
		}		
	}

	public Addr getAddr(Connection conn, Lookup aAddrType) throws Exception {
		Addr result = (Addr) this.getGotObj(conn, aAddrType, this.getAddressBox());
		return(result);
	}


	public FieldObjectBox getAddressBox() throws Exception {
		return((FieldObjectBox) this.getField(this.getStaticAddr()));
	}


	//
	// generic methods
	//

	public GotIntf addGot(Connection conn, Class aGotClass, Lookup aType, FieldObjectBox aGotBox, String aStaticName) throws Exception {
		if (getGot(conn, aType, aGotBox) != null) {
			throw new Hinderance("Fail to create " + aGotClass.getSimpleName() + " of type: " + aType.getDescr().toUpperCase() + ", such lookup already exist");
		}
		GotIntf gotObj = (GotIntf) ObjectBase.CreateObject(conn, aGotClass);
		//GotIntf gotObj = (GotIntf) this.getDb().createObject(aGotClass, conn);
		gotObj.setType(aType);
		gotObj.createValue(conn);
		this.addValueObject(conn, aStaticName, (Clasz) gotObj);
		return(gotObj);
	}

	public GotIntf addGot(Connection conn, Class aGotClass, FieldObjectBox aGotBox, String aStaticName) throws Exception {
		GotIntf gotObj = (GotIntf) ObjectBase.CreateObject(conn, aGotClass);
		//GotIntf gotObj = (GotIntf) this.getDb().createObject(aGotClass, conn);
		gotObj.createValue(conn);
		gotObj.populateLookupField(gotObj.getDb(), conn);
		this.addValueObject(conn, aStaticName, (Clasz) gotObj);
		return(gotObj);
	}

	public GotIntf getGot(Connection conn, Lookup aType, FieldObjectBox aAddrBox) throws Exception {
		GotIntf result = null;
		aAddrBox.resetIterator();
		while(aAddrBox.hasNext(conn)) {
			GotIntf eachGot = (GotIntf) aAddrBox.getNext();
			String strDescr = eachGot.getDescr(conn);
			if (strDescr.equals(aType.getDescr())) {
				result = (GotIntf) eachGot;
				break;
			}
		}
		return(result);
	}

	public Object getGotObj(Connection conn, Lookup aType, FieldObjectBox aObjBox) throws Exception {
		Object result = null;
		GotIntf got = this.getGot(conn, aType, aObjBox);
		if (got != null) {
			result = got.getValue(conn);
			if (result == null) {
				throw new Hinderance("The GOT interface require data object be precreated with the GOT object, no data object found");
			}
		}
		return(result);
	}

	@Override
	public void validateBeforePersist(Connection aConn) throws Exception {
		super.validateBeforePersist(aConn);
		if (this.getName() != null) {
			this.setName(this.getName().trim());
			if (this.getName().trim().isEmpty()) {
				throw new Hinderance("Contact name cannot be empty");
			}
		}

		if (this.getAlias() != null) this.setAlias(this.getAlias().trim());
		if (this.getWebsite() != null) this.setWebsite(this.getWebsite().trim());
	}

	public FieldObjectBox getCustomer() throws Exception {
		return(this.getFieldObjectBox(Contact.Customer));
	}

	public Contact fetchCustomer(Connection aConn, String aCompanyName, String aCompanyAlias) throws Exception {
		LambdaObject contactLd = new LambdaObject();
		this.getCustomer().forEachMember(aConn, ((Connection bConn, Clasz eachClasz) -> {
			Contact eachContact = (Contact) eachClasz;
			if (eachContact.getName().equals(aCompanyName) && eachContact.getAlias().equals(aCompanyAlias)) {
				contactLd.setTheObject(eachContact);
				return(false);
			}
			return(true);
		}));
		return((Company) contactLd.getTheObject());
	}

	public Contact getCustomer(Connection aConn, String aObjectId, String aObjectClasz) throws Exception {
		Contact result = (Contact) this.getCustomer().fetchByObjectId(aConn, aObjectId, aObjectClasz);
		return(result);
	}

	public Contact getCustomer(Connection aConn, Long aObjectId, String aObjectClasz) throws Exception {
		Contact result = (Contact) this.getCustomer().fetchByObjectId(aConn, aObjectId, aObjectClasz);
		return(result);
	}

	public void addCustomer(Connection aConn, Contact aCustomer) throws Exception {
		this.addValueObject(aConn, Customer, (Clasz) aCustomer);
	}

	public void removeCustomer(Connection aConn, String aCustomerName, String aCustomerAlias) throws Exception {
		this.getFieldObjectBox(Contact.Customer).resetIterator(); // always do a reset before starting to loop for the objects
		while (this.getFieldObjectBox(Contact.Customer).hasNext(aConn)) {
			Contact contact = (Contact) this.getFieldObjectBox(Contact.Customer).getNext();
			if (contact.getName().equals(aCustomerName) && contact.getAlias().equals(aCustomerAlias)) {
				contact.setForDelete(true);
			}
		}

	}

	public String getDescr() throws Exception {
		return(this.getValueStr(Name));
	}

	public void setDescr(String aDescr) throws Exception {
		this.setValueStr(Name, aDescr);
	}
	
	public FieldObjectBox getEmail() throws Exception {
		return(this.getFieldObjectBox(Person.Email));
	}

	public String getEmail(Connection aConn, String aType) throws Exception {
		String workEmail = "";
		this.getFieldObjectBox(Person.Email).resetIterator();
		while (this.getFieldObjectBox(Person.Email).hasNext(aConn)){
			GotIntf email = (GotIntf) this.getFieldObjectBox(Person.Email).getNext();
			String type = email.getDescr(aConn);
			if (type.toLowerCase().equals(aType.toLowerCase())) {
				workEmail = (String) email.getValue();
				break;
			} else {
				
			}
		}
		return(workEmail);
	}

	public void fetchAllEmail(Connection aConn) throws Exception {
		GotIntf.PopulateGotIntf(aConn, this, Person.Email);
	}
}

