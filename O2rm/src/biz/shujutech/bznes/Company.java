package biz.shujutech.bznes;

import biz.shujutech.payroll.WorkConfigWeek;
import biz.shujutech.base.App;
import biz.shujutech.base.Connection;
import biz.shujutech.base.DateAndTime;
import biz.shujutech.base.Hinderance;
import biz.shujutech.db.object.Clasz;
import biz.shujutech.db.object.FieldClasz.FetchStatus;
import biz.shujutech.db.object.FieldObjectBox;
import biz.shujutech.db.object.ObjectBase;
import biz.shujutech.db.object.ObjectIndex;
import biz.shujutech.db.relational.Database;
import biz.shujutech.db.relational.Field;
import biz.shujutech.db.relational.FieldDate;
import biz.shujutech.db.relational.FieldDateTime;
import biz.shujutech.db.relational.FieldType;
import biz.shujutech.db.relational.SortOrder;
import biz.shujutech.db.relational.Table;
import biz.shujutech.payroll.SalarySlip;
import org.joda.time.DateTime;
import java.util.Random;
import biz.shujutech.reflect.ReflectField;
import biz.shujutech.technical.LambdaArray;
import biz.shujutech.technical.LambdaCounter;
import biz.shujutech.technical.LambdaObject;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Company extends Organization {

	@ReflectField(type=FieldType.OBJECTBOX, deleteAsMember=false, clasz="biz.shujutech.bznes.Person", polymorphic=true, displayPosition=10, prefetch=false) public static String Employee;
	@ReflectField(type=FieldType.OBJECT, deleteAsMember=false, clasz="biz.shujutech.bznes.Country", displayPosition=40, lookup=true) public static String CountryOfIncorporation;
	@ReflectField(type=FieldType.OBJECT, deleteAsMember=false, clasz="biz.shujutech.bznes.Email", displayPosition=50) public static String EmailTemplateSalarySlip;

	@ReflectField(type=FieldType.OBJECT, deleteAsMember=false, clasz="biz.shujutech.payroll.WorkConfigWeek", displayPosition=20, prefetch=false) public static String WorkWeek;

	public static String CreateWorkerAttachmentPassword(Connection aConn, Company aCompany, Person aEmployee, Employment aJob) throws Exception {
		String abbrvCompany = aCompany.getName().substring(0, 2);
		String abbrvEmployee = aEmployee.getName().substring(0, 2);
		String abbrvSalary = aJob.getSalary(aConn).getSalaryAmount(aConn).getAmountNoComma(); // in ###0.00
		String thePassword = abbrvCompany.toLowerCase() + abbrvEmployee.toLowerCase() + abbrvSalary;
		return thePassword;
	}

	public static biz.shujutech.bznes.Email CreateEmail2Worker(Connection aConn, Person aWorker, String aLoginId, String aEmailSubject, String aEmailBody, String aAttachmentName, String aAttachmentType, String aAttachmentContent) throws Exception {
		return biz.shujutech.bznes.Email.CreateEmail(aConn, aWorker.getEmail(EmailTypePerson.Personal), aLoginId, aEmailSubject, aEmailBody, aAttachmentName, aAttachmentType, aAttachmentContent);
	}

	public static void SendPayslipEmail(Connection aConn, biz.shujutech.bznes.Email email, SalarySlip payslip) throws Exception {
		email.sendEmail(aConn);
		payslip.addStatusEmailed(aConn);
		payslip.persistCommit(aConn);
	}

	public FieldObjectBox getEmployee() throws Exception {
		return(this.getFieldObjectBox(Employee));
	}

	public Person getEmployee(Connection aConn, String aName, String aAlias) throws Exception {
		Person theWorker = null;
		LambdaArray workerBox = new LambdaArray();
		this.getEmployee().forEachMember(aConn, ((Connection bConn, Clasz aEmployee) -> {
			Person eachWorker = (Person) aEmployee;
			if (eachWorker.getName().equals(aName) && eachWorker.getAlias().equals(aAlias)) {
				workerBox.addElement(eachWorker);
				return(false);
			}
			return(true);
		}));

		if (workerBox.getSize() != 0) {
			theWorker = (Person) workerBox.getElement(0);
		}
		return(theWorker);
	}

	public WorkConfigWeek getWorkSchedule(Connection aConn) throws Exception {
		return((WorkConfigWeek) this.getValueObject(aConn, Company.WorkWeek));
	}

	public void setWorkSchedule(WorkConfigWeek aValue) throws Exception {
		this.setValueObject(Company.WorkWeek, aValue);
	}

	/*
	@Deprecated
	public SalarySlip fetchEmployeeLatestPayslip(Connection aConn, Person aWorker) throws Exception {
		aWorker.fetchEmploymentOfCompany(aConn, this); // get all the employment for this worker in this company
		SalarySlip latestPayslip = null;
		aWorker.getEmployment().resetIterator();
		while (aWorker.getEmployment().hasNext(aConn)) { // for every employment of this worker in this company
			Employment eachJob = (Employment) aWorker.getEmployment().getNext();
			eachJob.fetchLatestSalarySlip(aConn); // get the latest payslip for this worker's job
			SalarySlip latestJobPayslip = (SalarySlip) eachJob.getSalarySlip().getFirstMember();
			if (latestJobPayslip != null) {
				if (latestPayslip == null) {
					latestPayslip = latestJobPayslip;
				} else {
					if (latestPayslip.getPeriodFrom() != null && latestJobPayslip.getPeriodFrom() != null) {
						if (latestPayslip.getPeriodFrom().isBefore(latestJobPayslip.getPeriodFrom())) {
							latestPayslip = latestJobPayslip;
						}
					}
				}
			}
		}
		return(latestPayslip);
	}
	*/

	public SalarySlip fetchEmployeeLatestPayslip(Connection aConn, Person aWorker) throws Exception {
		LambdaObject lambdaObject = new LambdaObject();
		aWorker.forEachEmploymentOfCompany(aConn, this, (Connection bConn, Clasz aClasz) -> {
			Employment eachJob = (Employment) aClasz;
			eachJob.fetchLatestSalarySlip(aConn); // get the latest payslip for this worker's job for this company
			SalarySlip latestJobPayslip = (SalarySlip) eachJob.getSalarySlip().getFirstMember();
			if (latestJobPayslip != null) {
				SalarySlip latestWorkerPayslip = (SalarySlip) lambdaObject.getTheObject();
				if (latestWorkerPayslip == null) {
					lambdaObject.setTheObject(latestJobPayslip);
				} else {
					if (latestWorkerPayslip.getPeriodFrom() != null && latestJobPayslip.getPeriodFrom() != null) {
						if (latestWorkerPayslip.getPeriodFrom().isBefore(latestJobPayslip.getPeriodFrom())) {
							lambdaObject.setTheObject(latestJobPayslip);
						}
					}
				}
			}
			return(true);
		});

		return((SalarySlip) lambdaObject.getTheObject());
	}

	public List<SalarySlip> fetchEmployeeLatestPayslipList(Connection aConn, Person aWorker, int aNumberOfPayslip2Fetch) throws Exception {
		LambdaCounter cntrAdded = new LambdaCounter();
		final int maxPayslip = aNumberOfPayslip2Fetch;
		CopyOnWriteArrayList<SalarySlip> listOfPayslip = new CopyOnWriteArrayList<>();
		aWorker.forEachEmploymentOfCompany(aConn, this, (Connection bConn, Clasz aClasz) -> {
			Employment eachJob = (Employment) aClasz;
			eachJob.getSalarySlip().forEachMember(aConn, (Connection cConn, Clasz eachClasz) -> {
				SalarySlip eachPayslip = (SalarySlip) eachClasz;
				if (listOfPayslip.isEmpty()) {
					if (eachPayslip.getPeriodFrom() != null) {
						listOfPayslip.add(eachPayslip);
					}
				} else {
					// which ever array payslip period from date is earlier then eachPayslip will get replace and itself and others down the array will be push down the array
					for(int cntrPayslip = 0; cntrPayslip < listOfPayslip.size(); cntrPayslip++) {
						SalarySlip payslipFromList = listOfPayslip.get(cntrPayslip);
						if (eachPayslip.getPeriodFrom() != null) {
							if (payslipFromList.getPeriodFrom().isBefore(eachPayslip.getPeriodFrom())) {
								listOfPayslip.add(cntrPayslip, eachPayslip);
								cntrAdded.increment();
								if (cntrAdded.getCntr() >= maxPayslip) {
									return(false);
								} else {
									break;
								}
							}
						}
					}
				}
				return(true);
			});
			return(true);
		});
		return(listOfPayslip);
	}

	public void fetchAllEmployeeLatestEmployment(Connection aConn) throws Exception {
		this.getEmployee().resetIterator();
		while (this.getEmployee().hasNext(aConn)) {
			Person employee = (Person) this.getEmployee().getNext();
			employee.fetchEmploymentOfCompany(aConn, this);
			employee.getEmployment().resetIterator();
			Employment latest = null;
			while (employee.getEmployment().hasNext(aConn)) {
				Employment employment = (Employment) employee.getEmployment().getNext();
				if (latest == null) {
					latest = employment;
				} else {
					if (latest.getStartDate() != null && employment.getStartDate() != null) {
						if (latest.getStartDate().isBefore(employment.getStartDate())) {
							latest = employment;
						}
					}
				}
			}
			employee.getEmployment().removeAll();
			if (latest != null) {
				employee.addEmployment(aConn, latest);
			}
		}
	}

	public void fetchEmployeeEmployment(Connection aConn) throws Exception {
		this.getEmployee().resetIterator();
		while (this.getEmployee().hasNext(aConn)) {
			Person employee = (Person) this.getEmployee().getNext();
			employee.fetchEmploymentOfCompany(aConn, (this)); // FetchUnique the company of this employee for this company
		}
	}

	public static void ObjectIndexOnEmployeeNamePopulate(Connection aConn, String aIndexName) throws Exception {
			App.logInfo(Company.class, "Populating object index: " + aIndexName + ", on its employees name");
			String sqlGetCompany = "select " + Clasz.CreatePkName(Company.class) + " from " + Clasz.CreateTableName(Company.class);
			Clasz.ForEachClasz(aConn, Company.class, sqlGetCompany, ((Connection conn1, Clasz clasz) -> {
				Company company = (Company) clasz;
				//company.fetchEmployeeAll(conn1);
				ObjectIndex.UpdateIndex(conn1, company, aIndexName, true);
				return(true);
			}));
			App.logInfo(Company.class, "Completed populating company object index: " + aIndexName);
	}

	public static String ObjectIndexOnEmployeeName(Connection aConn) throws Exception {
		Company claszCompany = (Company) ObjectBase.CreateObject(aConn, Company.class);
		claszCompany.clearAllIndexKey();
		claszCompany.getEmployee().setObjectKey(true);
		claszCompany.getEmployee().getMetaObj().getField(Contact.Name).setObjectKey(true);
		String indexName = ObjectIndex.GetObjectIndexName(aConn, claszCompany);
		if (Database.TableExist(aConn, indexName) == false) {
			App.logInfo(Company.class, "Creating object index for: " + Company.class.getName() + ", on its employees name");
			ObjectIndex.CreateObjectIndex(aConn, claszCompany); // create field index by name
			ObjectIndexOnEmployeeNamePopulate(aConn, indexName);
		}
		return(indexName);
	}

	public FetchStatus fetchEmployeeBySection(Connection aConn, String aPageDirection, int aFetchSize, List<String> aSortFieldList, List<String> aSortValueList, SortOrder aSortOrder) throws Exception {
		List<SortOrder> orderList = new CopyOnWriteArrayList<>();
		for(int cntr = 0; cntr < aSortFieldList.size(); cntr++) {
			orderList.add(aSortOrder);
		}

		String idxName = ObjectIndexOnEmployeeName(aConn); // if sort field is name, use index by name, else if other index create the index appropriately
		FetchStatus result = this.getEmployee().fetchBySection(aConn, aPageDirection, aFetchSize, aSortFieldList, aSortValueList, orderList, idxName);
		return(result);
	}
	
	@Deprecated
	public void fetchEmployeeBySection(Connection aConn, String aPageDirection, int aFetchSize, String aSortField, String aSortValue) throws Exception {
		String idxName = ObjectIndexOnEmployeeName(aConn); // if sort field is name, use index by name, else if other index create the index appropriately
		this.getEmployee().fetchBySection(aConn, aPageDirection, aFetchSize, aSortField, aSortValue, idxName);
	}

	public void addEmployee(Connection aConn, Person aEmployee) throws Exception {
		this.addValueObject(aConn, Employee, (Clasz) aEmployee);
	}

	public FetchStatus fetchEmployeeByName(Connection aConn, String aPageDirection, String aSortValue, String aIndexName) throws Exception {
		int pageSize = ((FieldObjectBox) this.getField(Employee)).getFetchSize();
		String sortField = "employee$name";
		FetchStatus result = ((FieldObjectBox) this.getField(Employee)).fetchBySection(aConn, aPageDirection, pageSize, sortField, aSortValue, aIndexName);
		return(result);
	}

	public Person fetchEmployeeWithOidAndClasz(Connection aConn, String aEmployeeOid, String aEmployeeClasz) throws Exception {
		Person employee = (Person) this.getEmployee().fetchByObjectId(aConn, aEmployeeOid, aEmployeeClasz);
		return(employee);
	}

	public Person fetchEmployeeWithNameAndAlias(Connection aConn, String aEmployeeName, String aEmployeeAlias) throws Exception {
		Person worker = (Person) ObjectBase.CreateObjectTransient(aConn, Person.class);
		worker.setName(aEmployeeName);
		worker.setAlias(aEmployeeAlias);
		if (worker.populate(aConn)) {
			fetchEmployeeWithOidAndClasz(aConn, worker.getObjectId().toString(), worker.getClass().getName());
			return(worker);
		}
		return(null);
	}

	public void fetchEmployeeAll(Connection aConn) throws Exception {
		this.getEmployee().fetchAll(aConn);
	}

	public void removeEmployee(Connection aConn, Long aEmployeeOid, String aEmployeeClasz) throws Exception {
		this.removeFieldObjectBoxMember(aConn, Employee, aEmployeeOid, aEmployeeClasz);
	}

	public Clasz getMe() {
		return(this);
	}

	public Employment createEmployment(Connection aConn) throws Exception {
		Employment result = (Employment) ObjectBase.CreateObject(aConn, getEmploymentClass());
		result.populateLookupField(aConn);
		Company boss = (Company) this.getMe();
		result.setEmployer(boss);
		return(result);
	}

	public Class getEmploymentClass() {
		Class result = Employment.class;
		return(result);
	}

	/*
	public static String ObjectIndexOnCompanyName(Connection aConn) throws Exception {
		Company idxCompanyName = (Company) ObjectBase.CreateObject(aConn, Company.class);
		idxCompanyName.clearAllIndexKey();
		idxCompanyName.getField(Name).setObjectKey(true);
		String indexName = ObjectBase.CreateObjectIndex(aConn, idxCompanyName); // create field index by name
		return(indexName);
	}

	public static List<Company> FetchCompanyBySection(ObjectBase aDb, Connection aConn, String aPageDirection, int aPageSize, String aSortField, String aSortValue, SortOrder aDisplayOrder) throws Exception {
		String idxName = ObjectIndexOnCompanyName(aConn); 
		return(FetchObjectBySection(aDb, aConn, aPageDirection, aPageSize, aSortField, aSortValue, aDisplayOrder, idxName));
	}
	*/

	public static List<Company> FetchObjectBySection(ObjectBase aDb, Connection aConn, String aPageDirection, int aPageSize, String aSortField, String aSortValue, SortOrder aDisplayOrder, String aIndexName) throws Exception {
		return(FetchObjectBySection(aDb, aConn, Company.class , aPageDirection, aPageSize, aSortField, aSortValue, aDisplayOrder, aIndexName));
	}

	public static List<Company> FetchObjectBySection(ObjectBase aDb, Connection aConn, Class aClass, String aPageDirection, int aPageSize, String aSortField, String aSortValue, SortOrder aDisplayOrder, String aIndexName) throws Exception {
		List<Company> result = new CopyOnWriteArrayList<Company>();
		PreparedStatement stmt = null;
		ResultSet rset = null;
		try {
			if (aDisplayOrder == SortOrder.DSC) {
				if (aPageDirection.equals("next")) { // reverse the direction if display by descending order
					aPageDirection = "prev";
				} else {
					aPageDirection = "next";
				}
			}

			String whereClause = "";
			String sortOrder = "asc";
			Table oiTable = new Table(aIndexName);
			oiTable.initMeta(aConn);
			Field keyField = oiTable.getField(aSortField);
			if (keyField.getFieldType() == FieldType.STRING) {
				if (aPageDirection.equals("next")) {
					if (aSortValue != null) whereClause += "lower(" + aSortField + ") > lower('" + aSortValue + "')";
					sortOrder = "asc";
				} else if (aPageDirection.equals("prev")) {
					if (aSortValue != null) whereClause += "lower(" + aSortField + ") < lower('" + aSortValue + "')";
					sortOrder = "desc";
				} else if (aPageDirection.equals("first")) {
					sortOrder = "asc";
				} else if (aPageDirection.equals("last")) {
					sortOrder = "desc";
				}
			} else if (keyField.getFieldType() == FieldType.DATETIME ||keyField.getFieldType() == FieldType.DATE) {
				if (aPageDirection.equals("next")) {
					if (aSortValue != null) whereClause += aSortField + " > ?";
					sortOrder = "asc";
				} else if (aPageDirection.equals("prev")) {
					if (aSortValue != null) whereClause += aSortField + " < ?";
					sortOrder = "desc";
				} else if (aPageDirection.equals("first")) {
					sortOrder = "asc";
				} else if (aPageDirection.equals("last")) {
					sortOrder = "desc";
				}
			} else {
				throw new Hinderance("The field type: " + keyField.getFieldType() + ", is not supported for FetchObjectBySection!");
			}

			String strSql = SqlStrOfObjectIndex(aIndexName, whereClause, aSortField + " " + sortOrder, aClass);
			stmt = aConn.prepareStatement(strSql);
			if (keyField.getFieldType() == FieldType.DATETIME && whereClause.isEmpty() == false) {
				java.sql.Timestamp dateValue;
				FieldDateTime fieldDt = (FieldDateTime) keyField;
				if (aSortValue != null) {
					fieldDt.setValueStr(aSortValue);
					String dbDate = DateAndTime.FormatForJdbcTimestamp(fieldDt.getValueDateTime());
					dateValue = java.sql.Timestamp.valueOf(dbDate); // format must be in "2005-04-06 09:01:10"
				} else {
					dateValue = new java.sql.Timestamp(Long.MIN_VALUE); 
				}
				stmt.setTimestamp(1, dateValue);
			} else if (keyField.getFieldType() == FieldType.DATE && whereClause.isEmpty() == false) {
				java.sql.Date dateValue;
				FieldDate fieldDt = (FieldDate) keyField;
				if (aSortValue != null) {
					fieldDt.setValueStr(aSortValue);
					String dbDate = DateAndTime.FormatForJdbcTimestamp(fieldDt.getValueDate());
					dateValue = java.sql.Date.valueOf(dbDate); // format must be in "2005-04-06 09:01:10"
				} else {
					dateValue = new java.sql.Date(Long.MIN_VALUE); 
				}
				stmt.setDate(1, dateValue);
			}
			rset = stmt.executeQuery();
			result.clear();
			int cntrRow = 0;
			Integer[] cntrThreadPassAsRef = {0};
			List<Thread> threadPool = new CopyOnWriteArrayList<>();
			while (rset.next()) {
				if (cntrRow < aPageSize) {
					cntrRow++;
					long pk = rset.getLong(1);

					// can refactor as a spawner pattern in future
					if (App.getMaxThread() == 1) { // no threading
						cntrThreadPassAsRef[0]++;
						(new PopulateObjectThreadPk(cntrThreadPassAsRef, aDb, aConn, result, aClass, pk)).join();
					} else {
						int cntrAttempt = 0;
						int maxAttempt = App.MAX_GET_CONNECTION_ATTEMPT;
						Connection conn = null;
						while(true) {
							try {
								cntrAttempt++;
								conn = aConn.getBaseDb().getConnPool().getConnection(); // here will throw exception from connection pool, if too many attempts
								if (cntrThreadPassAsRef[0] >= App.getMaxThread()) { 
									aConn.getBaseDb().getConnPool().freeConnection(conn); 
									App.ShowThreadingStatus(Company.class, "FetchObjectBySection", cntrThreadPassAsRef[0], App.getMaxThread(), cntrAttempt, maxAttempt);
									Thread.sleep(App.MAX_THREAD_OR_CONN_WAIT); // if max thread reached, wait and continue the loop
								} else { 
									cntrThreadPassAsRef[0]++;
									Thread theThread = new PopulateObjectThreadPk(cntrThreadPassAsRef, aDb, conn, result, aClass, pk);
									threadPool.add(theThread);
									break;
								}
							} catch(Exception ex) {
								App.ShowThreadingStatus(Company.class, "FetchObjectBySection", cntrThreadPassAsRef[0], App.getMaxThread(), cntrAttempt, maxAttempt);
								if (cntrAttempt >= maxAttempt) {
									throw new Hinderance(ex, "[FetchObjectBySection] Give up threading due to insufficent db connection");
								} else  {
									Thread.sleep(App.MAX_THREAD_OR_CONN_WAIT); // wait for other db conn to free up
								}
							} 
						}
					}
					// end of spawner

				} else {
					break;
				}
			}
			for(Thread eachThread : threadPool) {
				eachThread.join(); // for each spawn thread, call join to wait for them to complete
			}

			// threading result is not sorted, now sort it
			String keyFieldName = keyField.getFieldName();
			Collections.sort(result, (obj1, obj2) -> {
				try {
					return obj1.getField(keyFieldName).getValueStr().compareTo(obj2.getField(keyFieldName).getValueStr());
				} catch (Exception ex) {
					throw new AssertionError("Comparator error, fail to sort list on key: " + keyFieldName + App.LineFeed + ex.getMessage());
				}
			});
			
		} catch (Exception ex) {
			if (stmt != null) {
				throw new Hinderance(ex, "Fail to fetch object by section: " + stmt.toString());
			} else {
				throw new Hinderance(ex, "Fail to fetch object by section!");
			}
		} finally {
			if (rset != null) {
				rset.close();
			}
			if (stmt != null) {
				stmt.close();
			}
		}

		return(result);
	}

	public static String SqlStrOfObjectIndex(String aIndexName, String aWhereClause, String aOrderDirection, Class aObjectKey) throws Exception {
		String result = "";
		String objectKey = Clasz.CreatePkName(aObjectKey);
		String sqlSelect = "select " + objectKey + " from " + aIndexName;
		String sqlWhere = "";
		String sqlOrderBy = "";
		if (aWhereClause.isEmpty() == false) {
			sqlWhere += " and " + aWhereClause;
		}
		if (aOrderDirection.isEmpty() == false) {
			sqlOrderBy = " order by " + aOrderDirection;
		}
		result = sqlSelect + sqlWhere + sqlOrderBy;
		return(result);
	}

	static class PopulateObjectThreadPk extends Thread {
		Integer[] cntrThread;
		ObjectBase dbase;
		Connection conn;
		Class clasz;
		Long pk;

		List<Company> resultList;

		public PopulateObjectThreadPk(Integer[] aCntrThread, ObjectBase aDb, Connection aConn, List<Company> aResultList, Class aClass, Long aPk) throws Exception {
			this.cntrThread = aCntrThread;
			this.dbase = aDb;
			this.conn = aConn;
			this.resultList = aResultList;
			this.clasz = aClass;
			this.pk = aPk;
			start();
		}

		@Override
		public void run() {
			try {
				Clasz fetchMember = Clasz.fetchObjectByPk(this.dbase, this.conn, this.clasz, this.pk);
				this.resultList.add((Company) fetchMember);  // now put the new member into the field object box
			} catch (Exception ex) {
				App.logEror(this, ex, "Fail thread: " + this.cntrThread + ", in FetchObjectThreadPk for class: " + this.clasz.getSimpleName() + ", of pk: " + this.pk);
			} finally {
				if (App.getMaxThread() != 1) this.conn.getBaseDb().getConnPool().freeConnection(this.conn);
				decreThreadCount(this.cntrThread);
			}
		}

		public synchronized void decreThreadCount(Integer[] aCntrThread) {
			aCntrThread[0]--;
		}
	}

	public Country getCountryOfIncoporation(Connection aConn) throws Exception {
		return((Country) this.getFieldObject(CountryOfIncorporation).getValueObj(aConn));
	}

	public static Company CreateCompany(Connection aConn, String aCompanyName, Class aCompanyClass) throws Exception {
		Company company = (Company) ObjectBase.CreateObject(aConn, aCompanyClass);
		company.setName(aCompanyName);
		company.persistCommit(aConn);
		App.logInfo("Successfully created company: " + company.getName());
		return(company);	
	}

	public static Company FetchCompany(Connection aConn, Class aCompanyClass, String aCompanyName) throws Exception {
		Company result = (Company) ObjectBase.CreateObject(aConn, aCompanyClass);
		result.setName(aCompanyName);
		if (!result.populate(aConn)) {
			throw new Hinderance("Fail to fetch or populate company: " + aCompanyName);
		}
		return(result);
	}

	public Integer getTotalActiveEmployee(Connection aConn) throws Exception {
		Integer totalActiveEmployee = 0;
		this.fetchEmployeeAll(aConn);
		this.fetchEmployeeEmployment(aConn);
		this.getEmployee().resetIterator();
		while (this.getEmployee().hasNext(aConn)) {
			Person employee = (Person) this.getEmployee().getNext();
			employee.getEmployment().resetIterator();
			while (employee.getEmployment().hasNext(aConn)) {
				Employment employment = (Employment) employee.getEmployment().getNext();
				if (employment.getEndDate() == null) {
					totalActiveEmployee++;
					break;
				}
			}
		}
		return(totalActiveEmployee);
	}

	public static void main(String[] args) {
		Connection conn = null;
		ObjectBase objectDb = new ObjectBase();
		try {
			objectDb.setupApp(args);
			objectDb.setupDb();
			conn = objectDb.getConnPool().getConnection();

			// setup all the reference table
			App.logInfo("Initializing all reference data");
			Country.InitList(conn);
			Ethnicity.InitList(conn);
			Religion.InitList(conn);
			Marital.InitList(conn);
			Gender.InitList(conn);
			AddrTypePerson.InitList(conn);
			AddrTypeOrganization.InitList(conn);

			// setup the object index on employee to test fetchBySection method
			/*
			App.logInfo("Setting up object index");
			Person setupEmployee = (Person) objectDb.CreateObject(Person.class);
			setupEmployee.clearAllIndexKey();
			setupEmployee.getField("name").setObjectKey(true); // must use lower case for field name
			String indexName = objectDb.createObjectIndex(setupEmployee); // create object index by name
			App.logInfo("Created object index name: " + indexName);
			*/

			// create field index for company's employee
			App.logInfo("Setting up field index");
			String indexName = Company.ObjectIndexOnEmployeeName(conn);

			int MAX_RECORD = 20;
			String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

			// deleteCommit the company and employee so the below test cases can be rerun
			App.logInfo("Starting to remove previous test data...");
			{
				App.logInfo("Checking if company have any employee, if yes, delete them....");
				App.logInfo("Removing the employee first");
				Company company = (Company) ObjectBase.CreateObject(conn, Company.class); // remove previous Company testing data if exist
				company.setName("Shujutech Inc.");
				if (company.populate(conn)) {
					String nextFetchValue = "";
					while(company.getEmployee().getFetchStatus() != FetchStatus.EOF) {
						App.logInfo("Retrieving the employee subsequent batch");
						company.fetchEmployeeByName(conn, "next", nextFetchValue, indexName);
						for(long cntrObj = 0; cntrObj < company.getEmployee().getTotalMember(); cntrObj++) {
							Person employee = (Person) company.getValueObject(Employee, cntrObj);
							nextFetchValue = employee.getName();
							if (employee.deleteCommit(conn)) {
								App.logInfo("Deleted employee: " + nextFetchValue);
							} else {
								throw new Hinderance("Fail to delete company's employee: " + employee.getName());
							}
						}
					}
				}
			}
			{
				App.logInfo("With no employee in the company, remove the company");
				Company company = (Company) ObjectBase.CreateObject(conn, Company.class); // remove previous Company testing data if exist
				company.setName("Shujutech Inc.");
				if (company.populate(conn)) {
					if (company.deleteCommit(conn)) {
						App.logInfo("Deleted the company: " + company.getName());
					} else {
						throw new Hinderance("Fail to delete company: " + company.getName());
					}
				} else {
					App.logWarn("Fail to fetch company from database to delete, nothing to delete");
				}
			}
			{
				App.logInfo("With no employee in the company, remove the bank");
				CompanyMalaysia bank = (CompanyMalaysia) ObjectBase.CreateObject(conn, CompanyMalaysia.class); // remove previous Company testing data if exist
				bank.setName("CIMB Bank");
				if (bank.populate(conn)) {
					if (bank.deleteCommit(conn)) {
						App.logInfo("Deleted the bank: " + bank.getName());
					} else {
						throw new Hinderance("Fail to remove bank: " + bank.getName());
					}
				}
			}

			// start testing, create a new company, then add employee to this company
			{
				App.logInfo("Creating a new company");
				Company company = (Company) ObjectBase.CreateObject(conn, Company.class);
				company.setName("Shujutech Inc.");
				Addr headquarterAddr = company.addAddr(conn, AddrTypeOrganization.Headquarter);
				headquarterAddr.setType(AddrTypeOrganization.Headquarter);
				headquarterAddr.setAddr1("116-32, Menara Zeon");
				headquarterAddr.setAddr2("Jalan Sultan Ismail");
				headquarterAddr.setPostalCode("51300");
				headquarterAddr.setState(Country.Malaysia.getState(conn, "Wilayah Perseketuan"));
				headquarterAddr.setCity("Kuala Lumpur");
				headquarterAddr.setCountry(Country.Malaysia);

				App.logInfo("Starting to create " + MAX_RECORD + " employee test record....");
				for (int cntr = 0; cntr < MAX_RECORD; cntr++) {
					App.logInfo("Creating employee record no: " + cntr);
					Character firstChar = alphabet.charAt((new Random()).nextInt(alphabet.length()));
					Person employee = (Person) ObjectBase.CreateObject(conn, Person.class);
					employee.setName(firstChar + "Ken" + cntr + " Miria");
					employee.setBirthDate(new DateTime());
					employee.setGender(Gender.Male);
					employee.setNationality(Country.Malaysia);
					//employee.setPassportNo("S184584" + cntr);
					employee.setMaritalStatus(Marital.Married);
					employee.setEthnic(Ethnicity.Caucasian);
					employee.setReligion(Religion.Christian);
					company.addEmployee(conn, employee);
				}

				App.logInfo("Validating address in memory i.e. before saving to db");
				Addr addr = company.getAddr(AddrTypeOrganization.Headquarter);
				if (addr != null) {
					if (addr.getAddr1().equals("116-32, Menara Zeon")) {
						App.logInfo("Retrieved company address1 from the memory is ok");
					} else {
						throw new Hinderance("Addresss1 value is wrongly fetch, value: " + addr.getAddr1());
					}
				} else {
					throw new Hinderance("Fail to retrieve company Headquarter address");
				}

				ObjectBase.PersistCommit(conn, company);
			}
			
			{
				// Fetch the company from database
				App.logInfo("Starting to retrieve company object from the database");
				Company company = (Company) ObjectBase.CreateObject(conn, Company.class);
				company.setName("Shujutech Inc.");
				if (company.populate(conn)) {
					Addr headquarterAddr = company.getAddr(AddrTypeOrganization.Headquarter);
					if (headquarterAddr != null) {
						if (headquarterAddr.getAddr1().equals("116-32, Menara Zeon")) {
							App.logInfo("Retrieved company address1 from the database is ok");
						} else {
							throw new Hinderance("Addresss1 value is wrongly fetch, value: " + headquarterAddr.getAddr1());
						}
					} else {
						throw new Hinderance("Fail to retrieve company Headquarter address");
					}
				} else {
					throw new Hinderance("Fail to retrieve the previously inserted Company object");
				}

				// testing fetching all in one go, the prefetch must set to false to test this
				/*
				App.logInfo("Starting to retrieve all employee information");
				company.getEmployee().resetIterator(); // always do a reset before starting to loop for the objects
				while(company.getEmployee().hasNext()) {
					Person employee = (Person) company.getEmployee().getNext();
					App.logInfo("Retrieved employee, name is: " + employee.getName());
				}
				*/

				// testing fetching by batch, the prefetch must set to true to test this
				String nextFetchValue = "";
				while(company.getEmployee().getFetchStatus() != FetchStatus.EOF) {
					App.logInfo("Retrieving the employee subsequent batch");
					company.fetchEmployeeByName(conn, "next", nextFetchValue, indexName); // retrieve the next batch
					//while(company.hasEmployee()) {
					for(long cntrObj = 0; cntrObj < company.getEmployee().getTotalMember(); cntrObj++) { // there is getTotalMember for each batch
						Person employee = (Person) company.getValueObject(Employee, cntrObj);
						nextFetchValue = employee.getName();
						App.logInfo("Retrieved employee, name is: " + employee.getName());
					}
				}
			}

			App.logInfo("End of test execution");
		} catch(Exception ex) {
			App.logEror(ex, "Application is aborting..........");
		} finally {
			if (conn != null) {
				objectDb.getConnPool().freeConnection(conn);
			}
		}		
	}

	public void searchEmployeeBySection(Connection aConn, String aPageDirection, int aFetchSize, String aSortField, String aSortValue, String aEmployeeName) throws Exception {
		String idxName = ObjectIndexOnEmployeeName(aConn); // if sort field is name, use index by name, else if other index create the index appropriately
		this.getEmployee().fetchBySection(aConn, aPageDirection, aFetchSize, aSortField, aSortValue, idxName, "lower(employee$name) like '%" + aEmployeeName.toLowerCase() + "%'");
	}

	public void getEmployeeLogin(Connection aConn) throws Exception {
		this.getEmployee().forEachMember(aConn, ((Connection bConn, Clasz eachClasz) -> {
			Person eachPerson = (Person) eachClasz;

			return(true);
		}));
	}

	/* must do polymorphic fetch, so this method is deprecated
	public static Company FetchCorporateCustomer(Connection aConn, String aCompanyName, String aCompanyAlias) throws Exception {
		Company result = null;
		Company customer = (Company) ObjectBase.CreateObject(aConn, Company.class);
		customer.setName(aCompanyName);
		customer.setAlias(aCompanyAlias);
		if (customer.populate(aConn)) {
			result = customer;
		}
		return(result);
	}
	*/

}
