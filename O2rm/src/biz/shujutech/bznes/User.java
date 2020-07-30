package biz.shujutech.bznes;

import biz.shujutech.base.App;
import biz.shujutech.base.Connection;
import biz.shujutech.base.Hinderance;
import biz.shujutech.db.object.Clasz;
import biz.shujutech.db.object.FieldObjectBox;
import biz.shujutech.db.object.ObjectBase;
import biz.shujutech.db.relational.FieldType;
import biz.shujutech.db.relational.SortOrder;
import org.joda.time.DateTime;
import biz.shujutech.reflect.ReflectField;
import biz.shujutech.reflect.ReflectIndex;
import biz.shujutech.technical.LambdaBoolean;
import biz.shujutech.technical.LambdaLong;
import biz.shujutech.technical.LambdaObject;
import biz.shujutech.util.Generic;

public class User extends Clasz {
	@ReflectField(type=FieldType.STRING, size=64, indexes={@ReflectIndex(indexName="idx_usr_lgid", indexNo=0, indexOrder=SortOrder.ASC, isUnique=true)}, displayPosition=10) public static String LoginId;
	@ReflectField(type=FieldType.ENCRYPT, displayPosition=20) public static String Password;
	@ReflectField(type=FieldType.OBJECT, deleteAsMember=false, clasz="biz.shujutech.bznes.UserStatus", lookup=true, displayPosition=30) public static String Status;
	@ReflectField(type=FieldType.OBJECTBOX, deleteAsMember=false, clasz="biz.shujutech.bznes.Company", polymorphic=true) public static String Company; 
	@ReflectField(type=FieldType.OBJECT, deleteAsMember=false, clasz="biz.shujutech.bznes.Person") public static String LoginPerson;
	@ReflectField(type=FieldType.STRING, size=32) public static String ValidateEmail; // if contain value, indicate this user email needs verification
	@ReflectField(type=FieldType.STRING, size=32) public static String ResetPassword; // if contain value, indicate this user need to reset it's password
	@ReflectField(type=FieldType.DATETIME, displayPosition=40) public static String CreateDate;
	@ReflectField(type=FieldType.DATETIME, displayPosition=50) public static String FirstLogin;
	@ReflectField(type=FieldType.DATETIME, displayPosition=60) public static String LastLogin;
	@ReflectField(type=FieldType.DATETIME, displayPosition=65) public static String LastResetEmail; // the latest date email was sent out to set or reset user password
	@ReflectField(type=FieldType.INTEGER) public static String FailAttempt;
	@ReflectField(type=FieldType.OBJECTBOX, deleteAsMember=false, clasz="biz.shujutech.bznes.UserRole", displayPosition=80) public static String Role; // a user can have one or more role, so this is not a lookup 
	@ReflectField(type=FieldType.OBJECTBOX, deleteAsMember=true, clasz="biz.shujutech.bznes.Session") public static String Session;

	private Company sessionCompany;

	public static boolean ValidLoginPerson(Connection aConn, User aUser, Person aNewLoginPerson, String aCurrentLoginId) throws Exception {
		boolean result = false;
		if (aNewLoginPerson.isPopulated()) {
			if (aUser.isPopulated()) {
				Person diskLoginPerson = aUser.getLoginPerson(aConn);
				if (diskLoginPerson.isSame(aNewLoginPerson)) {
					result = true;
				} else {
					App.logWarn("Invalid login person, attempting to update User that have a different login person, by: " + aCurrentLoginId);
				}
			} else {
				throw new Hinderance("Cannot validate User, the pass in User is not populated!");
			}
		} else {
			throw new Hinderance("Cannot validate User, the pass in login person is not populated!");
		}
		return result;
	}

	public User() {
	}

	public DateTime getCreateDate() throws Exception {
		return(this.getValueDateTime(CreateDate));
	}

	public DateTime getFirstLogin() throws Exception {
		return(this.getValueDateTime(FirstLogin));
	}

	public Integer getFailAttempt() throws Exception {
		return(this.getValueInt(FailAttempt));
	}

	public DateTime getLastLogin() throws Exception {
		return(this.getValueDateTime(LastLogin));
	}

	public DateTime getLastResetEmail() throws Exception {
		return(this.getValueDateTime(LastResetEmail));
	}

	public String getLoginId() throws Exception {
		return(this.getValueStr(LoginId));
	}

	public String getPassword() throws Exception {
		return(this.getValueStr(Password));
	}

	public void setCreateDate(DateTime aValue) throws Exception {
		this.setValueDateTime(CreateDate, aValue);
	}

	public void setFirstLogin(DateTime aValue) throws Exception {
		this.setValueDateTime(FirstLogin, aValue);
	}

	public void setFailAttempt(Integer aValue) throws Exception {
		this.setValueInt(FailAttempt, aValue);
	}

	public void setLastLogin(DateTime aValue) throws Exception {
		this.setValueDateTime(LastLogin, aValue);
	}

	public void setLastResetEmail(DateTime aValue) throws Exception {
		this.setValueDateTime(LastResetEmail, aValue);
	}

	public void setLoginId(String aValue) throws Exception {
		this.setValueStr(LoginId, aValue);
	}

	public void setPassword(String aValue) throws Exception {
		this.setValueStr(Password, aValue);
	}

	public UserStatus getStatus(Connection aConn) throws Exception {
		return((UserStatus) this.getValueObject(aConn, Status));
	}

	public UserStatus gotStatus(Connection aConn) throws Exception {
		return((UserStatus) this.gotValueObject(aConn, Status));
	}

	public void setStatus(UserStatus aStatus) throws Exception {
		this.setValueObject(Status, aStatus);
	}

	/*
	public void fetchStatus(Connection aConn) throws Exception {
		if (this.gotStatus(aConn) == null || this.getStatus(aConn).getDescr().isEmpty()) {
			this.setStatus(UserStatus.Disable);
		}
	};
	*/

	public boolean isEnable(Connection aConn) throws Exception {
		boolean result = false;
		if (this.getStatus(aConn).isSame(UserStatus.Enable)) {
			result = true;
		}
		return(result);
	}

	public void getAllCompany(Connection aConn) throws Exception {
		this.getFieldObjectBox(User.Company).fetchAll(aConn);
	}

	public void addRole(Connection aConn, UserRole aRole) throws Exception {
		this.addValueObject(aConn, Role, (Clasz) aRole);
	}

	public void setLoginPerson(Person aPerson) throws Exception {
		this.setValueObject(LoginPerson, aPerson);
	}

	public Person getLoginPerson(Connection aConn) throws Exception {
		return((Person) this.getValueObject(aConn, LoginPerson));
	}

	public Person gotLoginPerson(Connection aConn) throws Exception {
		return((Person) this.gotValueObject(aConn, LoginPerson));
	}

	public Company getSessionCompany() {
		return sessionCompany;
	}

	public void setSessionCompany(Company sessionCompany) {
		this.sessionCompany = sessionCompany;
	}




	public Company createInitialCompany(Connection aConn) throws Exception {
		Company firstCompany = (Company) ObjectBase.CreateObject(aConn, biz.shujutech.bznes.Company.class);
		firstCompany.populateLookupField(aConn);
		this.addCompany(aConn, firstCompany);
		return((Company) firstCompany);
	}

	public void removeCompany(Connection aConn, String aCompanyName, String aCompanyAlias) throws Exception {
		this.getFieldObjectBox(User.Company).resetIterator(); // always do a reset before starting to loop for the objects
		while (this.getFieldObjectBox(User.Company).hasNext(aConn)) {
			Company company = (Company) this.getFieldObjectBox(User.Company).getNext();
			if (company.getName().equals(aCompanyName) && company.getAlias().equals(aCompanyAlias)) {
				company.setForDelete(true);
			}
		}
	}

	public FieldObjectBox getCompany() throws Exception {
		return(this.getFieldObjectBox(User.Company));
	}

	public void addCompany(Connection aConn, Company aCompany) throws Exception {
		if (this.gotCompany(aConn, aCompany) == false) {
			this.addValueObject(aConn, User.Company, (Clasz) aCompany);
		}
	}

	public boolean gotCompany(Connection aConn, Company aCompany) throws Exception {
		if (this.getFieldObjectBox(User.Company).gotMember(aConn, aCompany))
			return(true);
		else 
			return(false);
	}

	public Company getInitialCompany(Connection aConn) throws Exception {
		Company company = null;
		this.getCompany().resetIterator(); // always do a reset before starting to loop for the objects
		if (this.getCompany().hasNext(aConn)) {
			company = (Company) this.getCompany().getNext();
		}
		return(company);
	}

	public static boolean CanLogin(Connection aConn, String aUsername, String aPassword) throws Exception {
		boolean result = false;
		
		User loginUserDb = (User) ObjectBase.CreateObject(aConn, User.class); // remove previous User testing data if exist
		loginUserDb.setLoginId(aUsername);
		loginUserDb.setPassword(aPassword);
		if (ObjectBase.ObjectExist(aConn, loginUserDb)) {
			result = true;
		}

		return(result);
	}

	public FieldObjectBox getSessions() throws Exception {
		return(this.getFieldObjectBox(Session));
	}

	public boolean isNewSession(Connection aConn, String aSessionId) throws Exception {
		boolean result = true;
		this.getSessions().resetIterator(); // always do a reset before starting to loop for the objects
		while (this.getSessions().hasNext(aConn)) {
			Session session = (Session) this.getSessions().getNext();
			if (session.getSessionId().equals(aSessionId)) {
				result = false;
				break;
			}
		}
		return(result);
	}

	public boolean isValidExistingSession(Connection aConn, String aSessionId, String aIpAddress) throws Exception {
		boolean result = false;
		this.getSessions().resetIterator(); // always do a reset before starting to loop for the objects
		while (this.getSessions().hasNext(aConn)) {
			Session session = (Session) this.getSessions().getNext();
			if (session.getSessionId().equals(aSessionId)) {
				result = true;
				break;
			}
		}
		return(result);
	}

	public Company canAccessCompany(Connection aConn, Company aCompany) throws Exception {
		LambdaObject companyLd = new LambdaObject();
		this.getCompany().forEachMember(aConn, ((Connection bConn, Clasz eachClasz) -> {
			Company eachCompany = (Company) eachClasz;
			if (aCompany.isSame(eachCompany)) {
				companyLd.setTheObject(eachCompany);
				return(false);
			}
			return(true);
		}));
		return((Company) companyLd.getTheObject());
	}

	@Deprecated
	public Company canAccessCompany(Connection aConn, String aCompanyClass, String aCompanyOid) throws Exception {
		Company result = null;
		if (aCompanyClass != null && aCompanyClass.isEmpty() == false && aCompanyOid != null && aCompanyOid.isEmpty() == false) {
			Class companyClass = Class.forName(aCompanyClass);
			Long companyOid = Long.parseLong(aCompanyOid);
			this.getCompany().resetIterator(); // always do a reset before starting to loop for the objects
			while (this.getCompany().hasNext(aConn)) {
				Company company = (Company) this.getCompany().getNext();
				if (company.getClass() == companyClass && company.getObjectId().compareTo(companyOid) == 0) {
					result = company;
					break;
				}
			}
		}
		return(result);
	}

	public void addSession(Connection aConn, Session aSession) throws Exception {
		this.addValueObject(aConn, Session, aSession);
	}

	public Session getSession(Connection aConn, String aSessionId) throws Exception {
		Session result = null;
		this.getFieldObjectBox(Session).resetIterator(); // always do a reset before starting to loop for the objects
		while(this.getFieldObjectBox(Session).hasNext(aConn)) {
			Session eachSession = (Session) this.getFieldObjectBox(Session).getNext();
			if (eachSession.getSessionId().equalsIgnoreCase(aSessionId)) {
				result = eachSession;
				break;
			}
		}
		return(result);
	}

	public void removeSession(Connection aConn, String aSessionId, String aErrorMsg) throws Exception {
		this.removeSession(aConn, aSessionId);
		this.persistCommit(aConn);
		App.logEror(this, aErrorMsg);
	}

	public void removeSession(Connection aConn, String aSessionId) throws Exception {
		Session session = this.getSession(aConn, aSessionId);
		session.setForDelete(true);
	}

	public void setValidateEmail(String aNo) throws Exception {
		this.setValueStr(ValidateEmail, aNo);
	}

	public String getValidateEmail() throws Exception {
		return (this.getValueStr(ValidateEmail));
	}

	public void setResetPassword(String aNo) throws Exception {
		this.setValueStr(ResetPassword, aNo);
	}

	public String getResetPassword() throws Exception {
		return (this.getValueStr(ResetPassword));
	}

	public boolean mustValidateEmail() throws Exception {
		boolean result = false;
		if (this.getValidateEmail() != null && this.getValidateEmail().isEmpty() == false) {
			result = true;
		}
		return(result);
	}

	public void increaseFailAttempt() throws Exception {
		if (this.getFailAttempt() == null) {
			this.setFailAttempt(1);
		} else {
			this.setFailAttempt(this.getFailAttempt() + 1);
		}
	}

	public static void UpdateFailAttempt(Connection aConn, User aUser) throws Exception {
		User userInDb = (User) ObjectBase.CreateObject(aConn, User.class);
		userInDb.setLoginId(aUser.getLoginId()); // just use the loginId to see if user exist
		if (userInDb.populate(aConn)) {
			userInDb.increaseFailAttempt();
			userInDb.persistCommit(aConn);
		}
	}

	public FieldObjectBox getRole() throws Exception {
		return (this.getFieldObjectBox(Role));
	}

	public boolean isAdmin(Connection aConn) throws Exception {
		LambdaBoolean result = new LambdaBoolean(false);
		this.getRole().forEachMember(aConn, ((Connection bConn, Clasz eachClasz) -> {
			UserRole eachRole = (UserRole) eachClasz;
			if (eachRole.isAdmin()) {
				result.setBoolean(true); // this is admin
				return(false); // break the loop
			}
			return(true);
		}));
		return(result.getBoolean());
	}

	public boolean accessibleUrl(Connection aConn, String aRequestPath, String aGotoParamValue) throws Exception {
		LambdaBoolean result = new LambdaBoolean(false);
		this.getRole().forEachMember(aConn, ((Connection bConn, Clasz eachClasz) -> {
			UserRole eachRole = (UserRole) eachClasz;
			if (eachRole.isAdmin() == false) {
				eachRole.getPermission().forEachMember(bConn, ((Connection cConn, Clasz theClasz) -> {
					UserRolePermission eachPermission = (UserRolePermission) theClasz;
					if (eachPermission.getItemType().equals(UserRolePermission.ITEM_TYPE_URL)) {
						String strRequestWithoutParam = Generic.GetUrlLeafNameWithoutParam(aRequestPath).trim();
						String strAccessibleUrl = eachPermission.getItemName().trim();
						String strGotoParam = aGotoParamValue.trim();
						if (strRequestWithoutParam.matches(strAccessibleUrl)
						|| strGotoParam.matches(strAccessibleUrl)
						) { // to also accept parameterise url, use regex
							if (eachPermission.getPermissionType().equals(UserRolePermission.PERMISSION_URL_ACCESSIBLE)) {
								result.setBoolean(true);
								return(false); // break the loop
							}
						}
					}
					return(true);
				}));
			}
			return(true);
		}));
		return(result.getBoolean());
	}

	public void addUserRole(Connection aConn, UserRole aRole2Add) throws Exception {
		LambdaBoolean gotThisRole = new LambdaBoolean(false);
		this.getRole().forEachMember(aConn, ((Connection bConn, Clasz eachClasz) -> {
			UserRole eachRole = (UserRole) eachClasz;
			if (eachRole.getDescr().equals(aRole2Add.getDescr())) {
				gotThisRole.setBoolean(true);
				return(false); // break
			}
			return(true);
		}));
		if (gotThisRole.getBoolean() == false) {
			this.addRole(aConn, aRole2Add);
		}
	}

	private void addRoleAdmin(Connection aConn) throws Exception {
		this.addUserRole(aConn, biz.shujutech.bznes.UserRole.Admin);
	}

	private void addRoleEmployee(Connection aConn) throws Exception {
		this.addUserRole(aConn, biz.shujutech.bznes.UserRole.Worker);
	}

	public static void ValidateAccess2Company(Connection aConn, String aLoginEmail, Company aCompany) throws Exception {
		User loginUser = (User) ObjectBase.CreateObject(aConn, User.class);
		loginUser.setLoginId(aLoginEmail);
		if (loginUser.populate(aConn)) {
			if (loginUser.gotCompany(aConn, aCompany)) {
			} else {
				throw new Hinderance("No privilege to access company:" + aCompany.getName() + " by login: " + aLoginEmail);
			}
		} else {
			throw new Hinderance("Attempting to access using to a non existing user account by: " + aLoginEmail);
		}
	}

	public UserRole createRoleOrGetMostPrivilegeRole(Connection aConn, String aFieldName) throws Exception {
		return CreateMostPrivilegeRole(aConn, this, aFieldName);
	}

	public static UserRole CreateMostPrivilegeRole(Connection aConn, User aUser, String aFieldName) throws Exception {
		UserRole mostPriviRole = GetMostPrivilegeRole(aConn, aUser);
		if (mostPriviRole == null) {
			mostPriviRole = UserRole.Worker;
		}
		aUser.createFieldObject(aFieldName, mostPriviRole, UserRole.class); // if clasz is null, createFieldObject will not know the field type
		return(mostPriviRole);
	}

	public static UserRole GetMostPrivilegeRole(Connection aConn, User aUser) throws Exception {
		LambdaObject userRoleLd = new LambdaObject();
		LambdaLong prevRankingLd = new LambdaLong();
		prevRankingLd.setLong(-1L);
		aUser.getRole().forEachMember(aConn, ((Connection bConn, Clasz eachClasz) -> {
			UserRole eachRole = (UserRole) eachClasz;
			if (prevRankingLd.getLong().equals(-1L)) {
				prevRankingLd.setLong(new Long(eachRole.getRanking()));
			}
			if (eachRole.getRanking() <= prevRankingLd.getLong()) {
				userRoleLd.setTheObject(eachRole);
			}
			return(true);
		}));
		
		return((UserRole) userRoleLd.getTheObject());
	}

	public String setLoginIdWithWorkEmail(Connection aConn, Person aLoginPerson, boolean aUseOnlyWorkEmail) throws Exception {
		if (aLoginPerson == null) {
			this.setLoginId("");
			return("");
		} else {
			return(SetLoginIdWithWorkEmail(aConn, aLoginPerson, this, aUseOnlyWorkEmail));
		}
	}

	public static String SetLoginIdWithWorkEmail(Connection aConn, Person aWorker, User aLoginUser, boolean aUseOnlyWorkEmail) throws Exception {
		LambdaObject emailAddrLd = new LambdaObject();
		aWorker.getEmail().forEachMember(aConn, ((Connection bConn, Clasz eachClasz) -> {
			PersonGotEmail eachEmail = (PersonGotEmail) eachClasz;
			if (eachEmail.getType().getDescr().equals(EmailTypePerson.WorkDescr)) { 
				emailAddrLd.setTheObject(eachEmail.getEmailAddr());
				return(false);
			} else {
				if (aUseOnlyWorkEmail == false) {
					emailAddrLd.setTheObject(eachEmail.getEmailAddr()); // take anything until we find work email, else take whatever comes
				}
			}
			return(true);
		}));

		String emailAddr = (String) emailAddrLd.getTheObject();
		if (emailAddr != null) {
			aLoginUser.setLoginId(emailAddr);
		} else {
			aLoginUser.setLoginId("");
		}
		return(emailAddr);
	}
}
