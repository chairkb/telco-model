package biz.shujutech.bznes;

import biz.shujutech.db.object.FieldObjectBox;
import biz.shujutech.db.object.Clasz;
import biz.shujutech.db.relational.SortOrder;
import biz.shujutech.db.relational.FieldType;
import biz.shujutech.db.object.ObjectBase;
import biz.shujutech.base.App;
import biz.shujutech.base.Connection;
import biz.shujutech.base.Hinderance;
import biz.shujutech.db.relational.Database;
import biz.shujutech.db.relational.Table;
import biz.shujutech.intf.EmailSentRecord;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import org.joda.time.DateTime;
import org.joda.time.Period;
import biz.shujutech.reflect.ReflectField;
import biz.shujutech.technical.LambdaObject;
import biz.shujutech.technical.ResultSetFetchIntf;
import com.google.gson.JsonArray;

public class Person extends Contact implements Customer {

	@ReflectField(type=FieldType.DATE, displayPosition=15) public static String BirthDate;
	@ReflectField(type=FieldType.OBJECT, deleteAsMember=false, clasz="biz.shujutech.bznes.Gender", displayPosition=20, lookup=true) public static String Gender;
	@ReflectField(type=FieldType.OBJECT, deleteAsMember=false, clasz="biz.shujutech.bznes.Country", displayPosition=25, lookup=true) public static String Nationality;
	//@ReflectField(type=FieldType.STRING, size=32, displayPosition=30) public static String PassportNo;
	@ReflectField(type=FieldType.OBJECT, deleteAsMember=false, clasz="biz.shujutech.bznes.Ethnicity", displayPosition=35, lookup=true) public static String Ethnic;
	@ReflectField(type=FieldType.OBJECT, deleteAsMember=false, clasz="biz.shujutech.bznes.Religion", displayPosition=45, lookup=true) public static String Religion;
	@ReflectField(type=FieldType.OBJECT, deleteAsMember=false, clasz="biz.shujutech.bznes.Marital", displayPosition=50, lookup=true) public static String MaritalStatus;
	@ReflectField(type=FieldType.INTEGER, displayPosition=55) public static String NoOfChildren;
	@ReflectField(type=FieldType.OBJECTBOX, deleteAsMember=true, clasz="biz.shujutech.bznes.Identity", polymorphic=true, displayPosition=55, uiMaster=true) public static String Identification;
	@ReflectField(type=FieldType.OBJECTBOX, deleteAsMember=true, clasz="biz.shujutech.bznes.Employment", polymorphic=true, displayPosition=60, uiMaster=true) public static String Employment;
	@ReflectField(type=FieldType.OBJECTBOX, deleteAsMember=true, clasz="biz.shujutech.bznes.PersonGotBankAccount", displayPosition=65) public static String BankAccount; 
	@ReflectField(type=FieldType.OBJECTBOX, deleteAsMember=true, clasz="biz.shujutech.bznes.PersonGotAddr", displayPosition=75) public static String Address;
	@ReflectField(type=FieldType.OBJECTBOX, deleteAsMember=true, clasz="biz.shujutech.bznes.PersonGotMobile", inline=true, size=3, displayPosition=80) public static String MobilePhone;
	@ReflectField(type=FieldType.OBJECTBOX, deleteAsMember=true, clasz="biz.shujutech.bznes.PersonGotEmail", inline=true, size=5, displayPosition=85) public static String Email;
	@ReflectField(type=FieldType.OBJECTBOX, deleteAsMember=true, clasz="biz.shujutech.bznes.PersonGotTelephone", inline=true, size=3, displayPosition=90) public static String Telephone;
	@ReflectField(type=FieldType.OBJECTBOX, deleteAsMember=true, clasz="biz.shujutech.bznes.PersonGotFax", inline=true, size=3, displayPosition=95) public static String Fax;
	@ReflectField(type=FieldType.OBJECT, deleteAsMember=false, clasz="biz.shujutech.bznes.Spouse", displayPosition=100, uiMaster=true) public static String Spouse;
	@ReflectField(type=FieldType.OBJECTBOX, deleteAsMember=false, clasz="biz.shujutech.bznes.Person", polymorphic=true, displayPosition=105) public static String Children;
	@ReflectField(type=FieldType.OBJECT, deleteAsMember=true, clasz="biz.shujutech.bznes.PersonGotRelative", displayPosition=110, uiMaster=true) public static String EmergencyContact;

	/*
	If the user keyed in the email address, use the keyed address
	else use the email address in the aEachClasz EmailNotification field,
	if there is no EmailNotification, then use the Person personal email
	address
	 */
	public static String GetEffectiveEmailAddress(Connection bConn, JsonArray aClaszListWithKeyedEmail, EmailSentRecord aEmailSentRec, Clasz aEachClasz, String aEachClaszName, String aEachClaszAlias) throws Exception {
		// did user overwrite the email addr, find out in this loop
		String emailAddr = null;
		for (int cntr = 0; cntr < aClaszListWithKeyedEmail.size(); cntr++) {
			String payslipId = aClaszListWithKeyedEmail.get(cntr).getAsJsonObject().get("objectId").getAsString();
			String payslipClasz = aClaszListWithKeyedEmail.get(cntr).getAsJsonObject().get("clasz").getAsString();
			if (payslipId.equals(Long.toString(aEachClasz.getObjectId())) && aEachClasz.getClass().getName().equals(payslipClasz)) {
				emailAddr = aClaszListWithKeyedEmail.get(cntr).getAsJsonObject().get("emailAddr").getAsString();
			}
		}
		// no emailAddr from user, get default employee Personal email
		if (emailAddr == null) {
			if (aEmailSentRec == null) {
				// no email record for this payslip, use the default personal email
				Person worker = (Person) ObjectBase.CreateObject(bConn, Person.class);
				worker.setName(aEachClaszName);
				worker.setAlias(aEachClaszAlias);
				if (worker.populate(bConn)) {
					worker.fetchAllEmail(bConn);
					worker.getEmail().resetIterator();
					while (worker.getEmail().hasNext(bConn)) {
						PersonGotEmail eachEmail = (PersonGotEmail) worker.getEmail().getNext();
						if (eachEmail.getType().getDescr().equals(EmailTypePerson.PersonalDescr)) {
							emailAddr = eachEmail.getEmailAddr();
						}
					}
				}
			} else {
				emailAddr = aEmailSentRec.getEmailAddress();
			}
		}
		return emailAddr;
	}

	private boolean childrenSorted = false;

	@Override
	public String getStaticEmail() throws Exception {
		return (Email);
	}

	@Override
	public Class getEmailClass() {
		return (PersonGotEmail.class);
	}

	@Override
	public String getStaticAddr() throws Exception {
		return (Address);
	}

	@Override
	public Class getAddrClass() {
		return (PersonGotAddr.class);
	}

	@Override
	public String getStaticMobilePhone() throws Exception {
		return (MobilePhone);
	}

	@Override
	public Class getMobilePhoneClass() {
		return (PersonGotMobile.class);
	}

	@Override
	public Class getTelephoneClass() {
		return (PersonGotTelephone.class);
	}

	@Override
	public String getStaticTelephone() throws Exception {
		return(Telephone);
	}

	public void setBirthDate(DateTime aDob) throws Exception {
		this.setValueDate(BirthDate, aDob);
	}

	public DateTime getBirthDate() throws Exception {
		return (this.getValueDate(BirthDate));
	}

	public Gender getGender(Connection aConn) throws Exception {
		return((Gender) this.getValueObject(aConn, Gender));
	}

	public void setGender(Gender aGender) throws Exception {
		this.setValueObject(Gender, aGender);
	}

	public void setNationality(Country aCountry) throws Exception {
		this.setValueObject(Nationality, aCountry);
	}

	public Marital getMaritalStatus(Connection aConn) throws Exception {
		return((Marital) this.getValueObject(aConn, MaritalStatus));
	}
	
	public boolean gotMaritalStatus(Connection aConn) throws Exception {
		boolean result = false;
		Clasz clasz = this.gotValueObject(aConn, MaritalStatus);
		if (clasz != null) result = true;
		return(result);
	}

	public void setMaritalStatus(Marital aMarital) throws Exception {
		this.setValueObject(MaritalStatus, aMarital);
	}

	public void setEthnic(Ethnicity aEthnicity) throws Exception {
		this.setValueObject(Ethnic, aEthnicity);
	}

	public void setReligion(Religion aReligion) throws Exception {
		this.setValueObject(Religion, aReligion);
	}

	public Integer getNoOfChild() throws Exception {
		return(this.getValueInt(NoOfChildren));
	}

	public void setNoOfChild(int aTotal) throws Exception {
		this.setValueInt(NoOfChildren, aTotal);
	}

	public void addChildren(Connection aConn, Person aChild) throws Exception {
		this.addValueObject(aConn, Children, aChild);
	}

	public Person addChildren(Connection aConn, String aChildName) throws Exception {
		Person child = (Person) this.getDb().createObject(Person.class);
		child.setName(aChildName);
		this.addValueObject(aConn, Children, child);
		return(child);
	}

	public void removeChildren(Connection aConn, String aName) throws Exception {
		this.getFieldObjectBox(Children).resetIterator(); // always do a reset before starting to loop for the objects
		while(this.getFieldObjectBox(Children).hasNext(aConn)) {
			Person eachChild = (Person) this.getFieldObjectBox(Children).getNext();
			if (eachChild.getName().equalsIgnoreCase(aName)) {
				Long key = this.getFieldObjectBox(Children).getKey(); // should replace this with setDelete instead?
				this.getFieldObjectBox(Children).remove(key);
				break;
			}
		}
	}

	public void sortChildrenByBirthDate() throws Exception {
		this.getFieldObjectBox(Children).getMetaField(Person.BirthDate).setSortKey(0, SortOrder.ASC);
		this.getFieldObjectBox(Children).sort();
		this.childrenSorted = true;
	}

	public boolean gotChildren(int aNo) throws Exception {
		if (this.getFieldObjectBox(Children).containsKey(new Long(aNo))) {
			return (true);
		}
		return (false);
	}

	public Person getChildren(Connection aConn, int aChildNo) throws Exception {
		this.getChildren().fetchAll(aConn);
		if (this.childrenSorted == false) {
			this.sortChildrenByBirthDate();
		}
		Person result = (Person) this.getFieldObjectBox(Children).getValue(new Long(aChildNo));
		return (result);
	}

	public Person getChildren(Connection aConn, String aChildName) throws Exception {
		Person result = null;
		this.getFieldObjectBox(Children).resetIterator(); // always do a reset before starting to loop for the objects
		while(this.getFieldObjectBox(Children).hasNext(aConn)) {
			Person eachChild = (Person) this.getFieldObjectBox(Children).getNext();
			if (eachChild.getName().trim().toUpperCase().endsWith(aChildName.trim().toUpperCase())) {
				result = eachChild;
				break;
			}
		}
		return (result);
	}

	public FieldObjectBox getChildren() throws Exception {
		return (this.getFieldObjectBox(Children));
	}

	public Map getChildrenMap() throws Exception {
		return (this.getFieldObjectBox(Children).getObjectMap());
	}

	public void setSpouse(Spouse aSpouse) throws Exception {
		this.setValueObject(Spouse, aSpouse);
	}

	public boolean gotSpouse(Connection aConn) throws Exception {
		if (this.getSpouse(aConn) != null) {
			return(true);
		} else {
			return(false);
		}
	}

	public Spouse getSpouse(Connection aConn) throws Exception {
		Spouse spouse = (Spouse) this.getFieldObject(Spouse).getValueObj(aConn);
		return (spouse);
	}

	public Spouse createSpouse(String aSpouseName) throws Exception {
		Spouse spouse = (Spouse) this.getDb().createObject(Spouse.class);
		spouse.setName(aSpouseName);
		this.setValueObject(Spouse, spouse);
		return (spouse);
	}

	public void setEmergencyContact(Connection aConn, Person aContact) throws Exception {
		this.addValueObject(aConn, EmergencyContact, aContact);
	}

	public FieldObjectBox getEmployment() throws Exception {
		return (this.getFieldObjectBox(Person.Employment));
	}

	public Map<Long, Clasz> getEmploymentMap() throws Exception {
		return (((FieldObjectBox) this.getField(Employment)).getObjectMap());
	}

	public void fetchEmploymentOfCompany(Connection aConn, Contact aEmployer) throws Exception {
		this.fetchEmploymentOfCompany(aConn, String.valueOf(aEmployer.getObjectId()), aEmployer.getClass().getName());
	}

	private void fetchEmploymentOfCompany(Connection aConn, String aCompanyOid, String aCompanyLeafClass) throws Exception {
		String personOid = String.valueOf(this.getObjectId(Person.class));
		List<String> arrayParam = new CopyOnWriteArrayList<>();
		arrayParam.add(personOid);
		arrayParam.add(aCompanyOid);
		arrayParam.add(aCompanyLeafClass);
		this.fetchByFilter(aConn, Person.Employment, arrayParam, new Clasz.GetFetchChildSql<Object>() {
			@Override
			public Object execute(Object aParam) throws Exception {
				String sqlPersonEmployment = "";
				List<String> param = (CopyOnWriteArrayList<String>) aParam;
				String personOid = param.get(0);
				String companyPolymorphicOid = param.get(1);
				String employerLeafClass = param.get(2);
				if (((Database) aConn.getBaseDb()).getDbType() == Database.DbType.MYSQL  || Database.GetDbType(aConn) == Database.DbType.POSTGRESQL) {
					String strSql = "select leaf_class from iv_person_employment where cz_person_pk = " + personOid + " group by leaf_class";
					Table tblJobCountry = new Table((ObjectBase) aConn.getBaseDb(), "iv_person_employment");
					tblJobCountry.createField(ObjectBase.LEAF_CLASS, FieldType.STRING, ObjectBase.CLASS_NAME_LEN);
					tblJobCountry.fetch(aConn, strSql);
					for(int cntr = 0; cntr < tblJobCountry.totalRecord(); cntr++) { // for each country job this person has
						if (tblJobCountry.getRecord(cntr) != null) {
							String strLeafClass = tblJobCountry.getRecord(cntr).getField(ObjectBase.LEAF_CLASS).getValueStr();

							Class clsJobCountry = Class.forName(strLeafClass);
							// kiv, from the strLeafClass, traverse up until the parent class is Employment, then use the child class to get the pk name
							String jobCountryPk = Clasz.CreatePkName(clsJobCountry);
							String jobCountryIh = Clasz.GetInheritanceTableName(clsJobCountry); // the immediate parent class of strLeafClass must be Employment.class
							if (!sqlPersonEmployment.isEmpty()) sqlPersonEmployment += " union ";
							sqlPersonEmployment = "select employment, iv_person_employment.leaf_class"
							+ " from iv_person_employment"
							+ " where cz_person_pk = " + personOid
							+ " and leaf_class = '" + strLeafClass + "'"
							+ " and employment in "
							+ " (select " + jobCountryPk
							+ " from " + jobCountryIh
							+ " where " + jobCountryPk + " in"
							+ " (select cz_employment_pk from iv_employment where iv_employment.employer = " + companyPolymorphicOid
							+ " and employer_leaf_class = '" + employerLeafClass + "'))";
						}
					}
				}
				return(sqlPersonEmployment);
			}
		});
	}

	public boolean gotEmployment() throws Exception {
		boolean result = false;
		for (Clasz eachClasz : this.getEmploymentMap().values()) {
			Employment job = (Employment) eachClasz;
			if (job.getEndDate() == null) {
				result = true;
			}
		}
		return (result);
	}

	public void setEmploymentEmployer(Connection aConn, Company aEmployer) throws Exception {
		for (Clasz eachClasz : this.getEmploymentMap().values()) {
			Employment employment = (Employment) eachClasz;
			if (employment.getEmployer(aConn) == null) {
				employment.setEmployer(aEmployer);
			}
		}
	}

	public Employment createEmployment(Connection aConn, Class aEmploymentClass) throws Exception {
		Clasz result = this.getFieldObjectBox(Person.Employment).addObject(aConn, aEmploymentClass);
		result.populateLookupField(aConn);
		return((Employment) result);
	}

	public void addEmployment(Connection aConn, Employment aJob) throws Exception {
		this.addValueObject(aConn, Employment, (Clasz) aJob);
	}

	public FieldObjectBox getMobilePhone() throws Exception {
		return(this.getFieldObjectBox(Person.MobilePhone));
	}


	@Deprecated
	public void fetchAllMobile(Connection aConn) throws Exception {
		this.getMobilePhone().fetchAll(aConn);
		this.getMobilePhone().resetIterator();
		while (this.getMobilePhone().hasNext(aConn)){
			GotIntf pgm = (GotIntf) this.getMobilePhone().getNext();
			pgm.getValue();
		}
	}

	public String getMobile(Connection aConn, String aType) throws Exception {
		String workMobile = "";
		String otherMobile = "";
		this.getFieldObjectBox(Person.MobilePhone).resetIterator();
		while (this.getFieldObjectBox(Person.MobilePhone).hasNext(aConn)){
			GotIntf pgm = (GotIntf) this.getFieldObjectBox(Person.MobilePhone).getNext();
			String type = pgm.getDescr(aConn);
			if (type.toLowerCase().equals(aType.toLowerCase())) {
				MobilePhone mobilePhone = (MobilePhone) pgm.getValue();
				workMobile = mobilePhone.getValueStr();
				break;
			} else {
				MobilePhone mobilePhone = (MobilePhone) pgm.getValue();
				otherMobile = mobilePhone.getValueStr();
			}
		}
		if (workMobile.isEmpty()) workMobile = otherMobile;
		return(removeMobileCountry(workMobile));
	}

	public String removeMobileCountry(String aNumber) throws Exception {
		String result = aNumber;
		String[] ary = aNumber.split(" ");
		if (ary.length == 2) {
			result = ary[1];
		}
		return(result);
	}

	public static void main(String args[]) {
		ObjectBase objectDb = new ObjectBase();
		Connection conn = null;
		try {
			objectDb.setupApp(args);
			objectDb.setupDb();
			conn = objectDb.getConnPool().getConnection();

			// setup all the reference table
			Country.InitList(conn);
			Ethnicity.InitList(conn);
			biz.shujutech.bznes.Religion.InitList(conn);
			Marital.InitList(conn);
			biz.shujutech.bznes.Gender.InitList(conn);
			AddrTypePerson.InitList(conn);
			AddrTypeOrganization.InitList(conn);
			LeavePolicy.InitList(conn);

			{
				Person person = (Person) objectDb.createObject(Person.class); // remove previous Person testing data if exist
				person.setName("Edward Yourdon");
				if (person.populate(conn) == true) {
					if (person.deleteCommit(conn)) {
						App.logInfo("Deleted person Edward Yourdon");
					} else {
						throw new Hinderance("Fail to delete person Edward Yourdon");
					}
				}
			}
			{
				Spouse spouse = (Spouse) objectDb.createObject(Spouse.class); // remove previous wife testing data if exist, if not remove by deleteAsMember flag, must use leaf class when deleteCommit i.e. Spouse, else nothing be deleted
				spouse.setName("Pauline Yourdon");
				if (spouse.populate(conn)) {
					if (spouse.deleteCommit(conn)) {
						App.logInfo("Deleted person' wife Pauline Yourdon");
					} else {
						throw new Hinderance("Fail to delete wife Pauline Yourdon");
					}
				}
			}
			{
				Person son = (Person) objectDb.createObject(Person.class); // remove previous son testing data if exist
				son.setName("Jeremy Yourdon");
				if (son.populate(conn)) {
					if (son.deleteCommit(conn)) {
						App.logInfo("Deleted person' son Jeremy Yourdon");
					} else {
						throw new Hinderance( "Fail to delete person son Jeremy Yourdon");
					}
				}
			}
			{
				Person daugther = (Person) objectDb.createObject(Person.class); // remove previous daugther testing data if exist
				daugther.setName("Jenny Yourdon");
				if (daugther.populate(conn)) {
					if (daugther.deleteCommit(conn)) {
						App.logInfo("Deleted person' daughter Jeremy Yourdon");
					} else {
						throw new Hinderance( "Fail to delete person' daughter Jeremy Yourdon");
					}
				}
			}

			{
				// create a new person
				App.logInfo("Creating a person for this company");
				Person person = (Person) objectDb.createObject(Person.class);
				person.setName("Edward Yourdon");
				person.setBirthDate(new DateTime());
				person.setGender(biz.shujutech.bznes.Gender.Male);
				person.setNationality(Country.Malaysia);
				//person.setPassportNo("S1845840");
				person.setMaritalStatus(Marital.Married);
				person.setEthnic(Ethnicity.Caucasian);
				person.setReligion(biz.shujutech.bznes.Religion.Christian);
				//person.addLeavePolicy(objectDb, new DateTime(), LeavePolicy.Malaysia);
	
				// create person home address
				App.logInfo("Creating the person's address");
				Addr homeAddr = person.addAddr(conn, AddrTypePerson.Home);
				homeAddr.setType(AddrTypePerson.Home);
				homeAddr.setAddr1("SD-35-6, Jalan Rungkup");
				homeAddr.setAddr2("Taman Uu Lian");
				homeAddr.setPostalCode("51200");
				homeAddr.setState(Country.Malaysia.getState(conn, "Wilayah Perseketuan"));
				homeAddr.setCity("Kuala Lumpur");
				homeAddr.setCountry(Country.Malaysia);

				// create email
				App.logInfo("Creating the person's email");
				person.addEmail(conn, EmailTypePerson.Personal, "edward.yourdon@oodb.com");
	
				// create a person spouse
				App.logInfo("Creating the person's spouse object");
				Spouse spouse = person.createSpouse("Pauline Yourdon");
				spouse.setName("Pauline Yourdon");
				spouse.setBirthDate(new DateTime());
				spouse.setGender(biz.shujutech.bznes.Gender.Female);
				spouse.setNationality(Country.Malaysia);
				//spouse.setIdentityCardNo("A1845940");
				//spouse.setPassportNo("S1835840");
				spouse.setMaritalStatus(Marital.Married);
				spouse.setEthnic(Ethnicity.Caucasian);
				spouse.setReligion(biz.shujutech.bznes.Religion.Christian);
	
				// create a son of the person
				App.logInfo("Creating the person's son object into the person list of children");
				Person eldestSon = person.addChildren(conn, "Jeremy Yourdon");
				eldestSon.setBirthDate((new DateTime()));
				eldestSon.setGender(biz.shujutech.bznes.Gender.Male);
				eldestSon.setNationality(Country.Malaysia);
				//eldestSon.setIdentityCardNo("M1845940");
				//eldestSon.setPassportNo("X1835840");
				eldestSon.setMaritalStatus(Marital.Single);
				eldestSon.setEthnic(Ethnicity.Caucasian);
				eldestSon.setReligion(biz.shujutech.bznes.Religion.Christian);
	
				// create a daughther of the person
				App.logInfo("Creating the person's daughther object into the person list of children");
				Person eldestDaughther = person.addChildren(conn, "Jenny Yourdon");
				eldestDaughther.setName("Jenny Yourdon");
				//eldestDaughther.setBirthDate(getDate("01-JAN-1980 01:01:01"));
				eldestDaughther.setBirthDate(new DateTime());
				eldestDaughther.setGender(biz.shujutech.bznes.Gender.Female);
				eldestDaughther.setNationality(Country.Malaysia);
				//eldestDaughther.setNationalId("F1845940");
				//eldestDaughther.setPassportNo("J1335840");
				eldestDaughther.setMaritalStatus(Marital.Single);
				eldestDaughther.setEthnic(Ethnicity.Caucasian);
				eldestDaughther.setReligion(biz.shujutech.bznes.Religion.Christian);
	
				ObjectBase.PersistCommit(conn, person);
			}

			{
				Person person = (Person) objectDb.createObject(Person.class);
				person.setName("Edward Yourdon");
				if (person.populate(conn) == true) {
					// do nothing, remove commented codes
				} else {
					throw new Hinderance("Fail to retrieve the previously inserted Person object");
				}
			}

			// test removing the spouse by setting it to null and persistCommit it, this member field relationship should be remove from the iv_ table
			{
				App.logInfo("Removing the wife from Edward Yourdon by setting the wife field to null");
				Person person = (Person) objectDb.createObject(Person.class);
				person.setName("Edward Yourdon");
				if (person.populate(conn) == true) {
					person.getFieldObject(Spouse).setForDelete(true);
					ObjectBase.PersistCommit(conn, person);
				}
			}
			
			{
				App.logInfo("Will attempt to delete wife directly i.e. NOT via field cacasde deletion");
				Spouse spouse = (Spouse) objectDb.createObject(Spouse.class); 
				spouse.setName("Pauline Yourdon");
				if (spouse.populate(conn)) {
					if (spouse.deleteCommit(conn)) { // if deleteAsMember is false, here will deleteCommit the spouse
						App.logInfo("Deleted person' wife Pauline Yourdon");
					} else {
						throw new Hinderance("Fail to delete person' wife Pauline Yourdon");
					}
				} else {
					App.logInfo("No wife need to be deleted, deleteAsMember must have been set to true");
				}
			}
	
			{
				Person person = (Person) objectDb.createObject(Person.class);
				person.setName("Edward Yourdon");
				if (person.populate(conn) == true) {
					Person spouse = person.getSpouse(conn); 
					if (spouse == null) {
						App.logInfo("Spouse is correctly deleted and fetching it got nothing populated, see iv_person table, the spouse relationship is still there but silently ignore");
					} else {
						throw new Hinderance("Spouse is not correctly deleted from the database");
					}
				} else {
					throw new Hinderance("Fail to retrieve the previously inserted Person object");
				}
			}

			// test removing one child and persistCommit it, this test the array member field relationship
			{
				Person person = (Person) objectDb.createObject(Person.class);
				person.setName("Edward Yourdon");
				if (person.populate(conn) == true) {
					person.removeChildren(conn, "Jenny Yourdon");
					ObjectBase.PersistCommit(conn, person);
					App.logInfo("One of the person's child have been removed from the database");
				}

				person.getFieldObjectBox(Children).resetIterator(); // always do a reset before starting to loop for the objects
				if (person.getFieldObjectBox(Children).hasNext(conn)) {
					Person child = (Person) person.getFieldObjectBox(Children).getNext();
					if (child.getName().equalsIgnoreCase("Jeremy Yourdon")) {
						App.logInfo("Retrieved the only child from memory: " + child.getName() + ", is ok");
					} else {
						throw new Hinderance("Error when deleting one of the child, wrong child: " + child.getName());
					}
				} else {
					throw new Hinderance("Error, there should be one child in the children field, none is found");
				}

				if (person.getChildrenMap().size() == 1) {
					App.logInfo("Correct, there's one children left in memory");
				} else {
					throw new Hinderance("Error, there should be one children, but there is: " + person.getChildrenMap().size());
				}
			}
	
			{
				Person person = (Person) objectDb.createObject(Person.class);
				person.setName("Edward Yourdon");
				if (person.populate(conn) == true) {
					Person spouse = person.getSpouse(conn); 
					if (spouse == null) {
						App.logInfo("The person's spouse have been removed: " + person.getName());
					} else {
						throw new Hinderance("Employee's spouse relationship is not correctly removed from the database");
					}

					person.getFieldObjectBox(Children).resetIterator(); // always do a reset before starting to loop for the objects
					while(person.getFieldObjectBox(Children).hasNext(conn)) {
						Person child = (Person) person.getFieldObjectBox(Children).getNext();
						if (child.getName().equalsIgnoreCase("Jeremy Yourdon")) {
							App.logInfo("Retrieved the only child from database: " + child.getName() + ", is ok");
						} else {
							throw new Hinderance("Error when deleting one of the child, wrong child: " + child.getName());
						}
					}

					if (person.getChildrenMap().isEmpty()) {
						throw new Hinderance("Error when deleting one of the child, there should be one child but none found");
					}

				} else {
					throw new Hinderance("Fail to retrieve the previously inserted Person object");
				}
			}

			App.logInfo("End of test execution");

		} catch (Exception ex) {
			App.logEror(0, new Hinderance(ex, "Application encounter fatal error, application is aborting...."));
		} finally {
			if (conn != null) {
				objectDb.getConnPool().freeConnection(conn);
			}
		}		
	}

	public Double getAge() throws Exception {
		Double result = CalculateAge(this.getBirthDate());
		return(result);
	}

	public static Double CalculateAge(DateTime birthDate) throws Exception {
		DateTime now = DateTime.now();
		Period age = new Period(now, birthDate);
		Double result = (double) (age.getYears() + (age.getMonths()/12) + (age.getDays()/365));
		return(result);
	}

	public BankAccount addBankAccount(Connection aConn, ContactBankAccountType aType) throws Exception {
		this.addGot(aConn, PersonGotBankAccount.class, aType, this.getBankAccount(), BankAccount);
		BankAccount result = this.getBankAccount(aConn, aType);
		return(result);
	}

	public FieldObjectBox getBankAccount() throws Exception {
		return(this.getFieldObjectBox(BankAccount));
	}

	public BankAccount getPayrollBankAccount(Connection aConn) throws Exception {
		BankAccount bankAccount = this.getBankAccount(aConn, ContactBankAccountType.Payroll);
		return(bankAccount);
	}

	public BankAccount getBankAccount(Connection aConn, ContactBankAccountType aType) throws Exception {
		BankAccount result = (BankAccount) this.getGotObj(aConn, aType, this.getBankAccount());
		return(result);
	}

	public FieldObjectBox getIdentification() throws Exception {
		return (this.getFieldObjectBox(Person.Identification));
	}

	public void addIdentification(Connection aConn, Identity aIdentity) throws Exception {
		this.addValueObject(aConn, Identification, aIdentity);
	}

	public Identity addPassport(Connection aConn, Country aCountry, String aPassportNo) throws Exception {
		Identity passport = null;
		if (Country.Malaysia.getCode().equals(aCountry.getCode())) {
			passport = (Identity) ObjectBase.CreateObject(aConn, MalaysiaPassport.class);
		} else if (Country.China.getCode().equals(aCountry.getCode())) {
			passport = (Identity) ObjectBase.CreateObject(aConn, ChinaPassport.class);
		} else if (Country.Singapore.getCode().equals(aCountry.getCode())) {
			passport = (Identity) ObjectBase.CreateObject(aConn, SingaporePassport.class);
		} else if (Country.Vietnam.getCode().equals(aCountry.getCode())) {
			passport = (Identity) ObjectBase.CreateObject(aConn, VietnamPassport.class);
		} else if (Country.Indonesia.getCode().equals(aCountry.getCode())) {
			passport = (Identity) ObjectBase.CreateObject(aConn, IndonesiaPassport.class);
		} else if (Country.Myanmar.getCode().equals(aCountry.getCode())) {
			passport = (Identity) ObjectBase.CreateObject(aConn, MyanmarPassport.class);
		}
		if (passport != null && aPassportNo != null && !aPassportNo.isEmpty()) {
			((Passport) passport).setPassportNo(aPassportNo);
		}
		this.addIdentification(aConn, passport);
		return(passport);
	}

	public void addMalaysiaIc(Connection aConn, String aNewIcNo, String aOldIcNo) throws Exception {
		if (!this.isMalaysiaCitizen(aConn)) {
			MalaysiaIdentityCard msiaIc = (MalaysiaIdentityCard) ObjectBase.CreateObject(aConn, MalaysiaIdentityCard.class);
			if (aNewIcNo != null) msiaIc.setNewIdentityCardNo(aNewIcNo);
			if (aOldIcNo != null) msiaIc.setOldIdentityCardNo(aOldIcNo);
			this.addIdentification(aConn, msiaIc);
		} else {
			throw new Hinderance("This person already have a Malaysia Identity Card");
		}
	}

	public boolean isMalaysiaCitizenOrPr(Connection aConn) throws Exception {
		boolean result = false;
		this.getIdentification().fetchAll(aConn);
		this.getIdentification().resetIterator(); // always do a reset before starting to loop for the objects
		while(this.getIdentification().hasNext(aConn)) {
			Identity eachIdentity = (Identity) this.getIdentification().getNext();
			if (eachIdentity instanceof MalaysiaIdentityCard || eachIdentity instanceof MalaysiaPermanentResident) {
				result = true;
				break;
			}
		}
		return(result);
	}

	public boolean isMalaysiaCitizen(Connection aConn) throws Exception {
		boolean result = false;
		this.getIdentification().fetchAll(aConn);
		this.getIdentification().resetIterator(); // always do a reset before starting to loop for the objects
		while(this.getIdentification().hasNext(aConn)) {
			Identity eachIdentity = (Identity) this.getIdentification().getNext();
			if (eachIdentity instanceof MalaysiaIdentityCard) {
				result = true;
				break;
			}
		}
		return(result);
	}

	public boolean isMalaysiaPr(Connection aConn) throws Exception {
		boolean result = false;
		this.getIdentification().fetchAll(aConn);
		this.getIdentification().resetIterator(); // always do a reset before starting to loop for the objects
		while(this.getIdentification().hasNext(aConn)) {
			Identity eachIdentity = (Identity) this.getIdentification().getNext();
			if (eachIdentity instanceof MalaysiaPermanentResident) {
				result = true;
				break;
			}
		}
		return(result);
	}

	public MalaysiaIdentityCard getMalaysiaIc(Connection aConn) throws Exception {
		MalaysiaIdentityCard msiaIc = null;
		this.getIdentification().fetchAll(aConn);
		this.getIdentification().resetIterator(); // always do a reset before starting to loop for the objects
		while(this.getIdentification().hasNext(aConn)) {
			Identity eachIdentity = (Identity) this.getIdentification().getNext();
			if (eachIdentity instanceof MalaysiaIdentityCard) {
				msiaIc = (MalaysiaIdentityCard) eachIdentity;
				break;
			}
		}
		return(msiaIc);
	}

	public MalaysiaPermanentResident getMalaysiaPr(Connection aConn) throws Exception {
		MalaysiaPermanentResident msiaPr = null;
		this.getIdentification().fetchAll(aConn);
		this.getIdentification().resetIterator(); // always do a reset before starting to loop for the objects
		while(this.getIdentification().hasNext(aConn)) {
			Identity eachIdentity = (Identity) this.getIdentification().getNext();
			if (eachIdentity instanceof MalaysiaPermanentResident) {
				msiaPr = (MalaysiaPermanentResident) eachIdentity;
				break;
			}
		}
		return(msiaPr);
	}

	public Passport getPassport(Connection aConn) throws Exception {
		int cntrPassport = 0;
		Passport passport = null;
		this.getIdentification().fetchAll(aConn);
		this.getIdentification().resetIterator(); // always do a reset before starting to loop for the objects
		while(this.getIdentification().hasNext(aConn)) {
			Identity eachIdentity = (Identity) this.getIdentification().getNext();
			if (eachIdentity instanceof Passport) {
				passport = (Passport) eachIdentity;
				cntrPassport++;
			}
		}
		if (cntrPassport > 1) {
			App.logWarn(this, "Person: " + this.getName() + ", has more then one passport, arbirtarily using passport: " + passport.getClass().getSimpleName());
		}
		return(passport);
	}

	public String getIdNoForMsia(Connection aConn) throws Exception {
		String icOrPassportNo;
		if (this.isMalaysiaCitizen(aConn)) {
			MalaysiaIdentityCard msiaIc = this.getMalaysiaIc(aConn);
			icOrPassportNo = msiaIc.getNewIdentityCardNo();
		} else if (this.isMalaysiaPr(aConn)) {
			MalaysiaPermanentResident msiaPr = this.getMalaysiaPr(aConn);
			icOrPassportNo = msiaPr.getPermanentResidentNo();
		} else {
			Passport passport = this.getPassport(aConn);
			icOrPassportNo = passport.getPassportNo();
		}
		return(icOrPassportNo);
	}

	public String getPassportNo(Connection aConn) throws Exception {
		String passportNo = "";
		Passport passport = this.getPassport(aConn);
		if (passport != null) {
			passportNo = passport.getPassportNo();
		}
		return(passportNo);
	}

	public String getPayrollBankDescr(Connection aConn) throws Exception {
		String bankName = "";
		String bankAcctNo = "";
		BankAccount bankAccount = this.getPayrollBankAccount(aConn);
		if (bankAccount != null) {
			Company payrollBank = bankAccount.getBank(aConn);
			bankName = payrollBank.getName();
			bankAcctNo = bankAccount.getAccountNo();
		}

		String result = "";
		if (!bankName.isEmpty()) {
			result = bankName;
		}
		if (!bankAcctNo.isEmpty()) {
			result += " - " + bankAcctNo;

		}
		return(result);
	}

	public String getPayrollBankName(Connection aConn) throws Exception {
		String bankName = "";
		BankAccount bankAccount = this.getPayrollBankAccount(aConn);
		if (bankAccount != null) {
			Company payrollBank = bankAccount.getBank(aConn);
			if (payrollBank != null) {
				bankName = payrollBank.getName();
			}
		}
		return(bankName);
	}

	public String getPayrollBankAcctNo(Connection aConn) throws Exception {
		String bankAcctNo = "";
		BankAccount bankAccount = this.getPayrollBankAccount(aConn);
		if (bankAccount != null) {
			Company payrollBank = bankAccount.getBank(aConn);
			if (payrollBank != null) {
				bankAcctNo = bankAccount.getAccountNo();
			}
		}
		return(bankAcctNo);
	}

	public void setPayrollBankAcct(Connection aConn, Company aBank, String aAcctNo) throws Exception {
		BankAccount bankAccount = this.getPayrollBankAccount(aConn);
		if (bankAccount == null) {
			this.addBankAccount(aConn, ContactBankAccountType.Payroll);
			bankAccount = this.getBankAccount(aConn, ContactBankAccountType.Payroll);
			bankAccount.setBank(aBank);
			bankAccount.setAccountNo(aAcctNo);
		} else {
			bankAccount.setBank(aBank);
			bankAccount.setAccountNo(aAcctNo);
		}
	}

	public static Person FetchPerson(Connection aConn, String aName, String aAlias) throws Exception {
		Person criteria = (Person) ObjectBase.CreateObject(aConn, Person.class);
		criteria.setName(aName);
		criteria.setAlias(aAlias);
		Person person = (Person) ObjectBase.FetchObject(aConn, criteria);
		return(person);
	}

	/*
	public Employment fetchLatestEmployment(Connection aConn, Company aCompany) throws Exception {
		LambdaObject lambdaObject = new LambdaObject();
		this.getEmployment().forEachMember(aConn, ((Connection bConn, Clasz aClasz) -> {
			Employment employment = (Employment) aClasz;
			if (employment.getEmployer(bConn).isSame(aCompany)) {
				Employment latestJob = (Employment) lambdaObject.getTheObject();
				if (latestJob == null) {
					lambdaObject.setTheObject(employment);
				} else {
					if (latestJob.getStartDate() != null && employment.getStartDate() != null) {
						if (latestJob.getStartDate().isBefore(employment.getStartDate())) {
							lambdaObject.setTheObject(employment);
						}
					}
				}
			}
			return(true);
		}));
		return((Employment) lambdaObject.getTheObject());	
	}
	*/

	public Employment fetchLatestEmploymentInCompany(Connection aConn, Company aCompany) throws Exception {
		LambdaObject lambdaObject = new LambdaObject();
		this.forEachEmploymentOfCompany(aConn, aCompany, ((Connection bConn, Clasz aClasz) -> {
			Employment eachJob = (Employment) aClasz;
			Employment latestJob = (Employment) lambdaObject.getTheObject();
			if (latestJob == null) {
				lambdaObject.setTheObject(eachJob);
			} else {
				if (latestJob.getStartDate() != null && eachJob.getStartDate() != null) {
					if (latestJob.getStartDate().isBefore(eachJob.getStartDate())) {
						lambdaObject.setTheObject(eachJob);
					}
				}
			}
			return(true);
		}));
		return((Employment) lambdaObject.getTheObject());	
	}

	public void forEachEmploymentOfCompany(Connection aConn, Company aCompany, ResultSetFetchIntf aCallback) throws Exception {
		this.getEmployment().forEachMember(aConn, ((Connection bConn, Clasz aClasz) -> {
			Employment employment = (Employment) aClasz;
			if (employment.getEmployer(bConn).isSame(aCompany)) {
				if (aCallback != null) {
					if (aCallback.callback(aConn, employment) == false) {
						return false;
					}
				} else {
					return false;
				}
			}
			return(true);
		}));
	}
}