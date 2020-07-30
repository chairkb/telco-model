package biz.shujutech.bznes;

import biz.shujutech.payroll.WorkConfigWeek;
import biz.shujutech.payroll.SalaryTransaction;
import biz.shujutech.payroll.SalarySlipMalaysia;
import biz.shujutech.payroll.SalarySlip;
import biz.shujutech.payroll.Salary;
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
import biz.shujutech.db.relational.FieldDateTime;
import biz.shujutech.db.relational.FieldLong;
import biz.shujutech.db.relational.FieldType;
import biz.shujutech.db.relational.SortOrder;
import org.joda.time.DateTime;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import biz.shujutech.reflect.ReflectField;
import biz.shujutech.technical.LambdaBoolean;
import biz.shujutech.technical.ResultSetFetchIntf;
import java.sql.PreparedStatement;
import org.joda.time.Days;

public class Employment extends Clasz {

	@ReflectField(type=FieldType.OBJECT, deleteAsMember=false, clasz="biz.shujutech.bznes.Company", polymorphic=true, displayPosition=0) public static String Employer; // must have the combination of employer id, company, start and departure date as the primary key
	@ReflectField(type=FieldType.DATE, displayPosition=5) public static String StartDate;
	@ReflectField(type=FieldType.DATE, displayPosition=10) public static String EndDate;
	@ReflectField(type=FieldType.OBJECT, deleteAsMember=false, clasz="biz.shujutech.bznes.EmployeeType", displayPosition=15, lookup=true) public static String EmploymentType; // permanent, contract, intern, part-time, under probation
	@ReflectField(type=FieldType.STRING, size=32, displayPosition=20) public static String Designation;
	@ReflectField(type=FieldType.OBJECT, deleteAsMember=true, inline=true, clasz="biz.shujutech.payroll.Salary", displayPosition=25, prefetch=true) public static String Salary; // try inline=true for this
	@ReflectField(type=FieldType.HTML, size=255, displayPosition=90) public static String Remark;
	@ReflectField(type=FieldType.OBJECTBOX, deleteAsMember=true, clasz="biz.shujutech.payroll.SalarySlip", polymorphic=true, displayPosition=50) public static String SalarySlip; 

	@ReflectField(type=FieldType.OBJECT, deleteAsMember=true, clasz="biz.shujutech.payroll.WorkConfigWeek", displayPosition=20, prefetch=false) public static String WorkScheduleAndRate;
	// @ReflectField(type=FieldType.OBJECT, deleteAsMember=true, clasz="biz.shujutech.bznes.Timesheet", displayPosition=20, prefetch=false) public static String WorkScheduleAndRate; // kiv, to create this class
	@ReflectField(type=FieldType.OBJECT, deleteAsMember=true, clasz="biz.shujutech.bznes.LeavePolicy", displayPosition=45) public static String LeavePolicy; // if not specified use company leave policy
	@ReflectField(type=FieldType.OBJECTBOX, deleteAsMember=true, clasz="biz.shujutech.bznes.LeaveForm", displayPosition=55) public static String LeaveForm; 

	public static final int MAX_PAYSLIP_PER_EMPLOYMENT_PER_PERIOD = 10;
	public static final int MAX_PAYSLIP_PER_MTH = 5;

	/*
	@Deprecated
	public static boolean JoinYearIsTp3Year(Connection conn, CompanyMalaysia company, Person aWorker, int aForYear) throws Exception {
	boolean result = false;
	aWorker.fetchEmploymentOfCompany(conn, company);
	aWorker.getEmployment().resetIterator();
	while (aWorker.getEmployment().hasNext(conn)) {
	Employment eachJob = (Employment) aWorker.getEmployment().getNext();
	int jobYear = eachJob.getStartDate().getYear();
	if (jobYear == aForYear) {
	result = true;
	break;
	}
	}
	return(result);
	}
	 */
	public static boolean IsContinuumJob(Connection aConn, Company aCompany, Person aWorker, Employment aJob) throws Exception {
		LambdaBoolean result = new LambdaBoolean(false);
		final DateTime aStartDate = aJob.getStartDate();
		if (aStartDate != null) {
			aWorker.forEachEmploymentOfCompany(aConn, aCompany, (Connection bConn, Clasz aClasz) -> {
				Employment eachJob = (Employment) aClasz;
				if (eachJob.isSame(aJob) == false) {
					// don't compare to itself
					if (eachJob.getEndDate().plusDays(1).toLocalDate().equals(aStartDate.toLocalDate())) {
						// end date flows from previous job start date, meaning is continuum job
						result.setBoolean(true); // the start date is continuum from previous job of the same company
						return false; // break from the loop
					}
				}
				return true;
			});
		} else {
			App.logWarn("Attempting to deterimin of a job continuum in a company with a job with null start date, company: " + aCompany.getName());
		}
		return result.getBoolean();
	}

	public Employment() {
		super();
	}

	@Override
	public String getValueStr() throws Exception {
		return(this.getDesignation());
	}

	public void setType(EmployeeType aType) throws Exception {
		this.setValueObject(EmploymentType, aType);
	}

	@Override
	public void validateBeforePersist(Connection aConn) throws Exception {
		if (this.getEmployer(aConn) == null) {
			throw new Hinderance("Error, attempting to persist Employment object without any Employer value!");
		}
	}

	public Salary getSalary(Connection aConn) throws Exception {
		return((Salary) this.getValueObject(aConn, Salary));
	}

	public boolean gotSalary(Connection aConn) throws Exception {
		boolean result = false;
		Clasz clasz =  this.gotValueObject(aConn, Salary);
		if (clasz != null) result = true;
		return(result);
	}

	public void fetchLatestSalarySlip(Connection aConn) throws Exception {
		this.fetchLatestMemberFromFob(aConn, Employment.SalarySlip, biz.shujutech.payroll.SalarySlip.PeriodFrom);
	}

	public String getDesignation() throws Exception {
		return((String) this.getValueStr(Designation));
	}

	public void setDesignation(String aDesignation) throws Exception {
		this.setValueStr(Designation, aDesignation);
	}

	public LeaveForm getLeaveTaken(String aType) throws Exception {
		LeaveForm result = null;
		for(Clasz eachClasz : this.getLeaveUse().getObjectMap().values()) {
			LeaveForm emplLeave = (LeaveForm) eachClasz;
			if (emplLeave.getLeaveType().getDescr().equals(aType)) {
				result = emplLeave;
			}
		}
		if (result == null) {
			throw new Hinderance("There is no leave type: " + aType.toUpperCase() + " assign to this employee");
		}

		return(result);
	}

	public FieldObjectBox getLeaveUse() throws Exception {
		return((FieldObjectBox) this.getField(LeaveForm));
	}

	public void addLeaveUse(Connection aConn, LeaveForm aLeave) throws Exception {
		this.addValueObject(aConn, LeaveForm, aLeave);
	}

	/**
	 * Return true if this employee is entitle to the specific leave type
	 * 
	 * @param aType
	 * @return
	 * @throws Exception 
	 */
	public boolean entitleToLeaveType(LeaveType aType) throws Exception {
		return(this.entitleToLeaveType(aType.getDescr()));
	}

	public boolean entitleToLeaveType(String aType) throws Exception {
		boolean result = false;
		for(Clasz eachClasz : this.getLeaveUse().getObjectMap().values()) {
			LeaveForm entitleLeave = (LeaveForm) eachClasz;
			if (entitleLeave.getLeaveType().getDescr().equals(aType)) {
				result = true;
			}
		}
		return(result);
	}

	public void setEmployer(Company aValue) throws Exception {
		this.setValueObject(Employer, aValue);
	}

	public Company getEmployer(Connection conn) throws Exception {
		Company employer = (Company) this.getFieldObject(Employer).getValueObj(conn);
		return (employer);
	}

	public void setStartDate(DateTime aValue) throws Exception {
		this.setValueDate(StartDate, aValue);
	}

	public DateTime getStartDate() throws Exception {
		return(this.getValueDate(StartDate));
	}

	public void setEndDate(DateTime aValue) throws Exception {
		this.setValueDate(EndDate, aValue);
	}

	public DateTime getEndDate() throws Exception {
		return(this.getValueDate(EndDate));
	}

	public void setRemark(String aRemark) throws Exception {
		this.setValueStr(Remark, aRemark);
	}

	public void addSalarySlip(Connection aConn, SalarySlip aSlip) throws Exception {
		this.addValueObject(aConn, SalarySlip, aSlip);
	}

	public void setSalary(Salary aValue) throws Exception {
		this.setValueObject(Salary, aValue);
	}

	public SalarySlip createSalarySlip(Connection aConn) throws Exception {
		throw new Hinderance("Employment must be treated as polymorphic, hence do not create SalarySlip from it");
	}

	public static void ObjectIndexOnSalarySlipStartPeriodPopulate(Connection aConn, String aIndexName) throws Exception {
			App.logInfo(Employment.class, "Populating object index: " + aIndexName + ", on its salary slips");
			String sqlGetEmployment = "select " + Clasz.CreatePkName(Employment.class) + " from " + Clasz.CreateTableName(Employment.class);
			Clasz.ForEachClasz(aConn, Employment.class, sqlGetEmployment, ((Connection conn1, Clasz clasz) -> {
				Employment employment = (Employment) clasz;
				//employment.fetchSalarySlipAll(conn1);
				ObjectIndex.UpdateIndex(conn1, employment, aIndexName, true);
				return(true);
			}));
			App.logInfo(Employment.class, "Completed populating employment object index: " + aIndexName);
	}

	public static String ObjectIndexOnSalarySlipStartPeriod(Connection aConn) throws Exception {
		Employment claszEmployment = (Employment) ObjectBase.CreateObject(aConn, Employment.class);
		claszEmployment.clearAllIndexKey();
		claszEmployment.getSalarySlip().setObjectKey(true);
		claszEmployment.getSalarySlip().getMetaObj().getField(biz.shujutech.payroll.SalarySlip.PeriodFrom).setObjectKey(true);
		String indexName = ObjectIndex.GetObjectIndexName(aConn, claszEmployment);
		if (Database.TableExist(aConn, indexName) == false) {
			App.logInfo(Employment.class, "Creating object index for: " + Employment.class.getName() + ", on payslip start period");
			ObjectIndex.CreateObjectIndex(aConn, claszEmployment); // create field index by name
			ObjectIndexOnSalarySlipStartPeriodPopulate(aConn, indexName);
		}
		return(indexName);
	}

	public FieldObjectBox getSalarySlip() throws Exception {
		return(this.getFieldObjectBox(Employment.SalarySlip));
	}


	public PreparedStatement sqlStmtGoss= null;
	public boolean GotOverlapSalarySlipDb(Connection aConn, Long aEmploymentOid, DateTime aFrom, DateTime aTo) throws Exception {
		boolean result = false;
		if (this.sqlStmtGoss == null) {
			String strSql = "select count(*)"
			+ " from iv_employment_salary_slip, cz_salary_slip"
			+ " where cz_employment_pk = ?"
			+ " and cz_salary_slip.cz_salary_slip_pk = iv_employment_salary_slip.salary_slip"
			+ " and ? <= cz_salary_slip.period_to and ? >= cz_salary_slip.period_from";
			this.sqlStmtGoss = aConn.prepareStatement(strSql);
		}
		List<Field> param = new CopyOnWriteArrayList<>();
		param.add(new FieldLong("EmploymentPK", aEmploymentOid));
		param.add(new FieldDateTime("DateStart", aFrom));
		param.add(new FieldDateTime("DateEnd", aTo));
		List<List> overlapList = Database.ExecuteSQL(aConn, sqlStmtGoss, param); // not optimize ExecuteSQL, for optimize SQL, make stmt instant variable to avoid repetative sql string parsing
		List<String> fieldList = (List) overlapList.get(0);
		String strTotalOverlap = (String) fieldList.get(0);
		int totalOverlap = Integer.parseInt(strTotalOverlap);
		if (totalOverlap > 0) result = true;
		return(result);
	}

	public boolean GotOverlapSalarySlipAry(Connection aConn, List<SalarySlip> aPayslipList, Long aEmploymentOid, DateTime aFrom, DateTime aTo) throws Exception {
		boolean result = false;
		DateTime newPayslipFrom = aFrom.withTimeAtStartOfDay();
		DateTime newPayslipTo = aTo.withTimeAtStartOfDay();
		for(SalarySlip payslip : aPayslipList) {
			DateTime oldPayslipStartDate = payslip.getPeriodFrom().withTimeAtStartOfDay();
			DateTime oldPayslipEndDate = payslip.getPeriodTo().withTimeAtStartOfDay();
			if (DateAndTime.IsBetween(oldPayslipStartDate, newPayslipFrom, newPayslipTo) || DateAndTime.IsBetween(oldPayslipEndDate, newPayslipFrom, newPayslipTo)) {
				result = true;
				break;
			}
		}
		return(result);
	}

	/*
	public boolean GotOverlapSalarySlipAry(Connection aConn, Long aEmploymentOid, DateTime aFrom, DateTime aTo) throws Exception {
		boolean result = false;
		DateTime newPayslipFrom = aFrom.withTimeAtStartOfDay();
		DateTime newPayslipTo = aTo.withTimeAtStartOfDay();
		this.getSalarySlip().resetIterator();
		while(this.getSalarySlip().hasNext(aConn)) {
			SalarySlip payslip = (SalarySlip) this.getSalarySlip().getNext();
			DateTime oldPayslipStartDate = payslip.getPeriodFrom().withTimeAtStartOfDay();
			DateTime oldPayslipEndDate = payslip.getPeriodTo().withTimeAtStartOfDay();
			if (DateAndTime.IsBetween(oldPayslipStartDate, newPayslipFrom, newPayslipTo) || DateAndTime.IsBetween(oldPayslipEndDate, newPayslipFrom, newPayslipTo)) {
				result = true;
				break;
			}
		}
		return(result);
	}
	*/

	public List<SalarySlip> generateSalarySlip(Connection aConn, String aBatchId, DateTime aGenerateFrom, DateTime aGenerateTo, Person aEmployee) throws Exception {
		List<SalarySlip> newPayslipList = new CopyOnWriteArrayList<>();
		return this.generateSalarySlip(aConn, aBatchId, aGenerateFrom, aGenerateTo, aEmployee, newPayslipList);
	}

	public List<SalarySlip> generateSalarySlip(Connection aConn, String aBatchId, DateTime aGenerateFrom, DateTime aGenerateTo, Person aEmployee, List<SalarySlip> aNewPayslip) throws Exception {
		if (aGenerateTo.isBefore(aGenerateFrom)) {
			throw new Hinderance("Error, generating payslip, date from: " + DateAndTime.FormatForDisplayWithTime(aGenerateFrom) + ", is later then date to: " + DateAndTime.FormatForDisplayWithTime(aGenerateTo));
		}

		List<SalarySlip> generatedPayslipList = new CopyOnWriteArrayList<>();
		DateTime generateFrom = aGenerateFrom.withTimeAtStartOfDay();
		DateTime generateTo = aGenerateTo.withTimeAtStartOfDay();
		DateTime workStart = generateFrom;
		DateTime workEnd = generateTo;

		// handle employment start date
		DateTime employmentStartDate = this.getStartDate();
		if (employmentStartDate != null) {
			employmentStartDate.withTimeAtStartOfDay();
			if (employmentStartDate.isAfter(generateFrom)) {
				workStart = employmentStartDate;
			} else {
				// do nothing, just use generateFrom date as workStart
			}
		} else {
			App.logWarn("Employee start date not stated, no payslip will be generated for: " + aEmployee.getName());
			return(generatedPayslipList);
		}

		// handle employment end date
		DateTime employmentEndDate = this.getEndDate();
		if (employmentEndDate != null) {
			employmentEndDate.withTimeAtStartOfDay();
			if (employmentEndDate.isBefore(aGenerateTo)) {
				workEnd = employmentEndDate;
			}
		}

		if (workStart.isAfter(workEnd)) {
			App.logWarn("No payslip to be generated, employee: " + aEmployee.getName() 
			+ ", start/end dates: " + employmentStartDate + " - " + employmentEndDate 
			+ ", generate for: " + generateFrom + " - " + generateTo);
		}

		if (this.getSalary(aConn).getDuration(aConn).getDescr().equals(Duration.Month.getDescr())) { // monthly employment
			int payslipYearPrev = 0;
			while(workStart.isBefore(generateTo) || workStart.equals(generateTo)) {
				if (DateAndTime.IsDifferentMonthAndYear(workStart, generateTo)) {
					workEnd = DateAndTime.GetMonthEnd(workStart); // continue to generate payslip
				} else {
					workEnd = generateTo; // is at the last payslip to generate
				}

				if (GotOverlapSalarySlipDb(aConn, this.getObjectId(), workStart, workEnd) == false && 
						GotOverlapSalarySlipAry(aConn, generatedPayslipList, this.getObjectId(), workStart, workEnd) == false) {
					SalarySlip payslip = (SalarySlip) this.createSalarySlip(aConn);
					payslip.setJob(this);
					payslip.setBatchId(aBatchId);
					payslip.setSalaryPeriod(this.getSalary(aConn).getDuration(aConn).getDescr());
					payslip.addStatusCreate(aConn);
					payslip.setCompany(this.getEmployer(aConn).getName());
					payslip.setCompanyAlias(this.getEmployer(aConn).getAlias());
					payslip.setEmployee(aEmployee.getName());
					payslip.setEmployeeAlias(aEmployee.getAlias());
					payslip.setPeriodFrom(workStart);
					payslip.setPeriodTo(workEnd);

					String rateStr = this.getSalary(aConn).getDuration(aConn).getDescr();
					String trxName = SalaryTransaction.BASIC_SALARY + " (" + rateStr + ")";
					//WorkConfigWeek workConfigWeek = this.getEmployer(aConn).getWorkSchedule(aConn);
					Money basicSalary = this.getFullOrPartialMonthSalary(aConn, workStart, workEnd);
					DateTime trxDate = DateAndTime.GetMonthEnd(workStart); // continue to generate payslip
					payslip.createBasicSalary(aConn, this.getEmployer(aConn), aEmployee, trxName, basicSalary, trxDate);
					this.generateSalarySlipByMonth(aConn, payslip, aEmployee, aNewPayslip);
					payslip.computeAllTotal(aConn, this.getEmployer(aConn)); // this is computed in SalarySlip.validateBeforePersist
					//App.logDebg(this, "PCB amount: " + ((SalarySlipMalaysia) payslip).getTaxAmountWithComma(aConn));
					if (payslip.getNetPay(aConn).getValueDouble() < 0) {
						Double zeroUpNetPay = payslip.getTax(aConn).getValueDouble() + (payslip.getNetPay(aConn).getValueDouble());
						App.logWarn(this, "Found negative net pay, company: " + payslip.getCompany() + ", worker: " + payslip.getEmployee() 
						+ ", mthly tax: " + payslip.getTax(aConn).getValueDouble() + ", negative net pay: " + payslip.getNetPay(aConn).getValueDouble()
						+ ", to zero up net pay: " + zeroUpNetPay);
						Money negativeNetPay = Money.CreateMoney(aConn, payslip.getNetPay(aConn).getCurrencyCode(), zeroUpNetPay);
						payslip.modifySalaryTrx(aConn, payslip.getTaxDesc(), negativeNetPay, biz.shujutech.payroll.SalarySlip.Deductions);
						payslip.computeAllTotal(aConn, this.getEmployer(aConn)); 
					}

					generatedPayslipList.add(payslip);

					if (payslipYearPrev != payslip.getPeriodFrom().getYear()) aNewPayslip.clear(); // new tax year
					aNewPayslip.add(payslip);
					payslipYearPrev = payslip.getPeriodFrom().getYear();
				} else {
					App.logWarn("Skipping payslip, overlap date: " + DateAndTime.FormatForDisplayWithTime(workStart) + ", for employment oid: " + this.getObjectId());
				}
				workStart = workEnd.plusDays(1);
			}
		} else {
			throw new Hinderance("Payroll information not setup correctly, possibly salary not stated for: " + aEmployee.getName());
		}

		// after completion only add into array, because the field is disturb by generateSalarySlipByMonth
		for (SalarySlip payslip : generatedPayslipList) {
			this.addSalarySlip(aConn, payslip);
		}

		return(generatedPayslipList);
	}

	public static final Double WORKING_DAYS_PER_MONTH = 26.0D;
	public static final Double WORKING_HOURS_PER_DAY = 8.0D;
	public Money getFullOrPartialMonthSalary(Connection aConn, DateTime aFrom, DateTime aTo) throws Exception {
		//Integer totalDaysOfMonth = DateAndTime.GetTotalDaysInMonth(aTo);
		Double totalDaysOfMonth = WORKING_DAYS_PER_MONTH;
		return(this.getFullOrPartialMonthSalary(aConn, aFrom, aTo, totalDaysOfMonth)); // simple partial mth calculation by dividing number of days in that month
	}

	public Money getFullOrPartialMonthSalary(Connection aConn, DateTime aFrom, DateTime aTo, Double aFullMonthDays) throws Exception {
		if (aFrom.getMonthOfYear() != aTo.getMonthOfYear()) {
			throw new Hinderance("To calculate parital month salary, both from and to date must be in the same month");
		}

		Money basicSalary = (Money) this.getSalary(aConn).getSalaryAmount(aConn);
		Money computedSalary = Money.CreateMoney(aConn, basicSalary.getCurrencyCode());
		if (DateAndTime.IsFullMonth(aFrom, aTo)) {
			computedSalary = this.getSalary(aConn).getSalaryAmount(aConn); // is full month salary, otherwise compute the partial month salary 
		} else {
			int totalWorkDays = Days.daysBetween(aFrom, aTo).getDays() + 1;

			// compute salary amount for the partial month
			//Double netSalary = (totalWorkDays / 26) * basicSalary.getValueDouble(); // this is java bug? result is 0
			Double workInMonth = ((double) totalWorkDays) / aFullMonthDays;
			Double netSalary = workInMonth * basicSalary.getValueDouble();
			computedSalary.setAmount(netSalary);
		}

		return(computedSalary);
	}

	public Money getSalaryAmountMonthly(Connection aConn, DateTime aFrom, DateTime aTo, WorkConfigWeek aWorkConfigWeek) throws Exception {
		if (DateAndTime.IsFullMonth(aFrom, aTo)) {
			return(this.getSalary(aConn).getSalaryAmount(aConn)); // is full month salary, otherwise compute the partial month salary 
		}

		// get the country, state of the employment
		Country country = this.getEmployer(aConn).getAddr(aConn, AddrTypeOrganization.Headquarter).getCountry();
		State state = this.getEmployer(aConn).getAddr(aConn, AddrTypeOrganization.Headquarter).getState();

		// compute working days in the month, taking into consideration public/state holidays
		int totalWorkDayForMonthPercentage = 0;
		DateTime monthEndDatePlusOne = DateAndTime.GetMonthEnd(aFrom).plusDays(1);
		for(DateTime eachDay = aFrom; eachDay.isBefore(monthEndDatePlusOne); eachDay = eachDay.plusDays(1)) {
			int workPercentage = aWorkConfigWeek.getWorkDayPercentage(aConn, eachDay);
			if (workPercentage != 0) {
				if (Holiday.IsPublicOrStateHoliday(aConn, country, state, eachDay) == false) {
					totalWorkDayForMonthPercentage += workPercentage;
				}
			}
		}

		// compute number of working days in the month by the employee work till last day
		int totalWorkPercentage = 0;
		for(DateTime eachDay = aFrom; eachDay.isBefore(aTo); eachDay = eachDay.plusDays(1)) {
			int workPercentage = aWorkConfigWeek.getWorkDayPercentage(aConn, eachDay);
			if (workPercentage != 0) {
				if (Holiday.IsPublicOrStateHoliday(aConn, country, state, eachDay) == false) {
					totalWorkPercentage += workPercentage;
				}
			}
		}

		Money basicSalary = ((Salary) this.getValueObject(Salary)).getSalaryAmount(aConn); 

		Money computedSalary = (Money) ObjectBase.CreateObject(aConn, Money.class);
		computedSalary.setValue(basicSalary.getCurrencyCode(), 0.0D);

		// compute salary amount for the partial month
		Double netSalary = (totalWorkPercentage / totalWorkDayForMonthPercentage) * this.getSalary(aConn).getSalaryAmount(aConn).getValueDouble();
		computedSalary.setAmount(netSalary);

		return(computedSalary);
	}

	public SalarySlip generateSalarySlipByMonth(Connection aConn, SalarySlip aPayslip, Person aEmployee) throws Exception {
		throw new Hinderance("Generating payslip should be implemented by subclass of this abstract: " + Employment.class.getSimpleName());
	}

	public SalarySlip generateSalarySlipByMonth(Connection aConn, SalarySlip salarySlip, Person aEmployee, List<SalarySlip> aNewPayslip) throws Exception {
		throw new Hinderance("Generating payslip should be implemented by subclass of this abstract: " + Employment.class.getSimpleName());
	}

	public void fetchSalarySlipAll(Connection aConn) throws Exception {
		this.getSalarySlip().fetchAll(aConn);
	}

	public FetchStatus fetchSalarySlipBySectionAsc(Connection aConn, String aPageDirection, int aFetchSize, String aSortField, String aSortValue) throws Exception {
		return(fetchSalarySlipBySection(aConn, aPageDirection, aFetchSize, aSortField, aSortValue, SortOrder.ASC));
	}

	public FetchStatus fetchSalarySlipBySectionDsc(Connection aConn, String aPageDirection, int aFetchSize, String aSortField, String aSortValue) throws Exception {
		return(fetchSalarySlipBySection(aConn, aPageDirection, aFetchSize, aSortField, aSortValue, SortOrder.DSC));
	}

	public FetchStatus fetchSalarySlipBySection(Connection aConn, String aPageDirection, int aFetchSize, String aSortField, String aSortValue, SortOrder aOrder) throws Exception {
		String idxName = ObjectIndexOnSalarySlipStartPeriod(aConn); 
		FetchStatus result = this.getSalarySlip().fetchBySection(aConn, aPageDirection, aFetchSize, aSortField, aSortValue, idxName, aOrder);
		return(result);
	}

	public FetchStatus fetchSalarySlipByMonth(Connection aConn, DateTime aMonth) throws Exception {
		DateTime startDate = DateAndTime.GetMonthStart(aMonth);
		DateTime endDate = DateAndTime.GetMonthEnd(aMonth);
		FetchStatus result = this.fetchSalarySlipByPeriod(aConn, startDate, endDate, MAX_PAYSLIP_PER_MTH, null);
		if (this.getSalarySlip().getTotalMember() >= MAX_PAYSLIP_PER_MTH) {
			throw new Hinderance("Too many payslip for month: " + aMonth + ", max allowed: " + MAX_PAYSLIP_PER_MTH + ", found: " + this.getSalarySlip().getTotalMember());
		}
		return(result);
	}

	public FetchStatus fetchSalarySlipByPeriod(Connection aConn, DateTime aPeriodFrom, DateTime aPeriodTo, int aBatchSize, String aSortValue) throws Exception {
		String idxName = ObjectIndexOnSalarySlipStartPeriod(aConn); 
		String whereCondition = "";
		if (Database.GetDbType(aConn) == Database.DbType.MYSQL) {
			whereCondition = "(" + "date_format(salary_slip$period_from, '%Y%m%d') >= '" + DateAndTime.FormatForCompareNoTime(aPeriodFrom) + "'"
			+ " and date_format(salary_slip$period_from, '%Y%m%d') <= '" + DateAndTime.FormatForCompareNoTime(aPeriodTo) + "'" + ")";
		} else if (Database.GetDbType(aConn) == Database.DbType.POSTGRESQL) {	
			whereCondition = "(" + "to_char(salary_slip$period_from, 'YYYYMMDD') >= '" + DateAndTime.FormatForCompareNoTime(aPeriodFrom) + "'"
			+ " and to_char(salary_slip$period_from, 'YYYYMMDD') <= '" + DateAndTime.FormatForCompareNoTime(aPeriodTo) + "'" + ")";			
		} else {
			throw new Hinderance("Unsupported database type: " +Database.GetDbType(aConn));
		}
		//this.getSalarySlip().fetchByObjectIndex(aConn, "salary_slip$period_from", aBatchSize, idxName, whereCondition);
		FetchStatus result = this.getSalarySlip().fetchBySection(aConn, "next", aBatchSize, "salary_slip$period_from", aSortValue, idxName, whereCondition);
		return(result);
	}

	public static void main(String args[]) {
		try {
			ObjectBase objectDb= new ObjectBase();
			objectDb.setupApp(args);
			objectDb.setupDb();

			Connection conn = objectDb.getConnPool().getConnection();
			try {
				Employment employment = (Employment) ObjectBase.CreateObject(conn, Employment.class);
				App.logInfo("Employment: " + employment.getClaszName());
			} finally {
				if (conn != null) {
					objectDb.getConnPool().freeConnection(conn);
				}
			}		
		} catch(Exception ex) {
			App.logEror(0, new Hinderance(ex, "Application encounter fatal error, application is aborting...."));
		}
	}

	public Money computeHourlyWage(Connection aConn) throws Exception {
		Money basicSalary = (Money) this.getSalary(aConn).getSalaryAmount(aConn);
		Money hourlyWage = Money.CreateMoney(aConn, basicSalary.getCurrencyCode());
		if (this.getSalary(aConn).getDuration(aConn).getDescr().equals(Duration.Month.getDescr())) { 
			Double hourlyWageDbl = basicSalary.getValueDouble() / (WORKING_DAYS_PER_MONTH * WORKING_HOURS_PER_DAY);
			hourlyWage.setAmount(hourlyWageDbl);
		} else {
			throw new Hinderance("Cannot compute hourly wage, only can compute from monthly salary" + App.ERROR_COMPLAIN);
		}
		return(hourlyWage);
	}

	public void forEachPayslip(Connection aConn, DateTime aDateFrom, DateTime aDateTo, ResultSetFetchIntf aCallback) throws Exception {
		DateTime dateFrom = DateAndTime.SetZeroTime(aDateFrom).minusDays(1);
		DateTime dateTo = DateAndTime.SetZeroTime(aDateTo);
		String lastPayslipDate = DateAndTime.FormatDisplayNoTime(dateFrom);
		FetchStatus fetchStatus = this.fetchSalarySlipBySectionAsc(aConn, "next", 13, "salary_slip$period_from", lastPayslipDate);
		for(;;) {
			this.getSalarySlip().resetIterator();
			while(this.getSalarySlip().hasNext(aConn)) {
				SalarySlipMalaysia payslip = (SalarySlipMalaysia) this.getSalarySlip().getNext();
				lastPayslipDate = DateAndTime.FormatDisplayNoTime(payslip.getPeriodFrom()); 
				if (payslip.getPeriodFrom().isAfter(dateTo)) {
					fetchStatus = FetchStatus.EOF;
					break;
				} else {
					if (aCallback != null) aCallback.callback(aConn, payslip);
				}
			}

			if (fetchStatus != FetchStatus.EOF) {
				fetchStatus = this.fetchSalarySlipBySectionAsc(aConn, "next", 13, "salary_slip$period_from", lastPayslipDate);
			} else {
				break;
			}
		}
	}

}

