package biz.shujutech.bznes;

import biz.shujutech.base.App;
import biz.shujutech.payroll.SalarySlipMalaysia;
import biz.shujutech.payroll.SalarySlip;
import biz.shujutech.payroll.Salary;
import biz.shujutech.payroll.MsiaEpfCalculator;
import biz.shujutech.payroll.MsiaEisCalculator;
import biz.shujutech.payroll.MsiaTaxCalculator;
import biz.shujutech.payroll.MsiaSocsoCalculator;
import biz.shujutech.base.Connection;
import biz.shujutech.base.DateAndTime;
import biz.shujutech.base.Hinderance;
import biz.shujutech.db.object.Clasz;
import biz.shujutech.db.object.ObjectBase;
import biz.shujutech.db.relational.FieldType;
import biz.shujutech.payroll.MsiaLhdnPcbTp3;
import biz.shujutech.reflect.ReflectField;
import biz.shujutech.technical.LambdaBoolean;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import org.joda.time.DateTime;

public class EmploymentMalaysia extends Employment {
	@ReflectField(type=FieldType.OBJECT, clasz="biz.shujutech.payroll.MsiaEpfCalculator", displayPosition=30, lookup=true) public static String EpfCalculator; 
	@ReflectField(type=FieldType.STRING, size=20, displayPosition=35, mask="####################") public static String EpfNo;
	@ReflectField(type=FieldType.STRING, size=16, displayPosition=40) public static String TaxNo;
	@ReflectField(type=FieldType.STRING, size=16, displayPosition=45) public static String TaxBranch;
	@ReflectField(type=FieldType.STRING, size=16, displayPosition=50) public static String SocsoNo;
	@ReflectField(type=FieldType.BOOLEAN, displayPosition=55) public static String NewSocsoMember;
	@ReflectField(type=FieldType.OBJECT, clasz="biz.shujutech.bznes.Money", inline=true, prefetch=true, displayPosition=60) public static String WageBeforeInvalidity;

	public static final String EPF_NAME = "Kumpulan Wang Simpanan Pekerja";
	public static final String SOCSO_NAME = "Kumpulan Wang Keselamatan Sosial";
	public static final String IRBM_NAME = "Lembaga Hasil Dalam Negeri Malaysia";

	public static Company EPF = null;
	public static Company SOCSO = null;
	public static Company LHDN = null;

	public static void InitStatutoryCompany(Connection aConn) throws Exception {
		if (EPF == null) EPF = Company.FetchCompany(aConn, CompanyMalaysia.class, EPF_NAME);
		if (SOCSO == null) SOCSO = Company.FetchCompany(aConn, CompanyMalaysia.class, SOCSO_NAME);
		if (LHDN == null) LHDN = Company.FetchCompany(aConn, CompanyMalaysia.class, IRBM_NAME);
	}

	public static boolean JoinYearIsTp3Year(Connection aConn, Company aCompany, Person aWorker, int aForYear) throws Exception {
		LambdaBoolean result = new LambdaBoolean(false);
		aWorker.forEachEmploymentOfCompany(aConn, aCompany, (Connection bConn, Clasz aClasz) -> {
			Employment eachJob = (Employment) aClasz;
			int jobYear = eachJob.getStartDate().getYear();
			if (jobYear == aForYear) {
				// user have multiple job, maybe is promotion and not a new joiner
				if (Employment.IsContinuumJob(aConn, aCompany, aWorker, eachJob) == false) {
					// not continue from previous job e.g promotion of this company
					result.setBoolean(true);
					return false;
				}
			}
			return true;
		});
		return result.getBoolean();
	}

	public static MsiaLhdnPcbTp3 GetOrCreatePcbTp3Form(Connection conn, CompanyMalaysia company, Person aWorker) throws Exception {
		MsiaLhdnPcbTp3 formPcbTp3 = (MsiaLhdnPcbTp3) ObjectBase.CreateObject(conn, MsiaLhdnPcbTp3.class);
		formPcbTp3.setCompanyName(company.getName());
		formPcbTp3.setCompanyAlias(company.getAlias());
		formPcbTp3.setEmployeeName(aWorker.getName());
		formPcbTp3.setEmployeeAlias(aWorker.getAlias());
		if (formPcbTp3.populate(conn)) {
			// do nothing, return the populated PCB TP3 form
		} else {
			EmploymentMalaysia job = (EmploymentMalaysia) aWorker.fetchLatestEmploymentInCompany(conn, company);
			formPcbTp3.setEmployeeJoinYear(job.getStartDate().getYear());
			formPcbTp3.setCreateDate(new DateTime());
			formPcbTp3.setLastModifyDate(new DateTime());
			formPcbTp3.setB4NoCukaiPendapatan(job.getTaxNo());
			if (aWorker.isMalaysiaCitizenOrPr(conn)) {
				formPcbTp3.setB2NoPengenalan(aWorker.getIdNoForMsia(conn));
			}
			formPcbTp3.setB3NoPassport(aWorker.getPassportNo(conn));
			/*
			formPcbTp3.getField(MsiaLhdnPcbTp3.CompanyName).forRemove(false);
			formPcbTp3.getField(MsiaLhdnPcbTp3.CompanyAlias).forRemove(false);
			formPcbTp3.getField(MsiaLhdnPcbTp3.EmployeeName).forRemove(false);
			formPcbTp3.getField(MsiaLhdnPcbTp3.EmployeeAlias).forRemove(false);
			formPcbTp3.getField(MsiaLhdnPcbTp3.EmployeeJoinYear).forRemove(false);
			formPcbTp3.getField(MsiaLhdnPcbTp3.B4NoCukaiPendapatan).forRemove(false);
			formPcbTp3.getField(MsiaLhdnPcbTp3.B2NoPengenalan).forRemove(false);
			formPcbTp3.getField(MsiaLhdnPcbTp3.B3NoPassport).forRemove(false);
			formPcbTp3.removeMarkField();
			*/
		}
		return formPcbTp3;
	}

	public Boolean isNewSocsoMember() throws Exception {
		return(this.getValueBoolean(NewSocsoMember));
	}

	public void setNewSocsoMember(boolean aBool) throws Exception {
		this.setValueBoolean(NewSocsoMember, aBool);
	}

	public Money getWageBeforeInvalidity(Connection aConn) throws Exception {
		return((Money) this.getValueObject(aConn, WageBeforeInvalidity));
	}

	public String getEpfNo() throws Exception {
		return((String) this.getValueStr(EpfNo));
	}

	public void setEpfNo(String aEpfNo) throws Exception {
		this.setValueStr(EpfNo, aEpfNo);
	}

	public String getSocsoNo() throws Exception {
		return((String) this.getValueStr(SocsoNo));
	}

	public void setSocsoNo(String aSocsoNo) throws Exception {
		this.setValueStr(SocsoNo, aSocsoNo);
	}

	public String getTaxNo() throws Exception {
		return((String) this.getValueStr(TaxNo));
	}

	public void setTaxNo(String aTaxNo) throws Exception {
		this.setValueStr(TaxNo, aTaxNo);
	}

	public String getTaxBranch() throws Exception {
		return((String) this.getValueStr(TaxBranch));
	}

	@Override
	public SalarySlipMalaysia createSalarySlip(Connection aConn) throws Exception {
		SalarySlipMalaysia paySlip = (SalarySlipMalaysia) ObjectBase.CreateObject(aConn, SalarySlipMalaysia.class);
		return(paySlip);
	}

	public Money generateEpf(Connection aConn, Person aEmployee, SalarySlipMalaysia msiaPayslip, String salarySlipYear, DateTime trxDate) throws Exception {
		// worker epf
		String strEpfEmployee = SalarySlipMalaysia.EPF_EMPLOYEE;
		Money amtEpfEmployer = Money.CreateMoney(aConn, msiaPayslip.getDefaultCurrencyCode(aConn)); 
		Money amtEpfEmployee = Money.CreateMoney(aConn, msiaPayslip.getDefaultCurrencyCode(aConn)); 
		msiaPayslip.modifySalaryTrx(aConn, strEpfEmployee, amtEpfEmployee, biz.shujutech.payroll.SalarySlip.Deductions);

		MsiaEpfCalculator epfCalculator;
		if (this.gotEpfCalculator(aConn)) {
			epfCalculator = this.getEpfCalculator(aConn);
		} else {
			throw new Hinderance("Payroll information not setup correctly, employee EPF not stated");
		}

		if (epfCalculator.getDescr().equals(MsiaEpfCalculator.Epf2017_11Percent.getDescr())) {
			strEpfEmployee = SalarySlipMalaysia.EPF_EMPLOYEE + " - 11%";
		}

		MsiaEpfCalculator.GetEpfAmount(aConn, aEmployee, epfCalculator, salarySlipYear, msiaPayslip, amtEpfEmployer, amtEpfEmployee);
		if (msiaPayslip.modifySalaryTrx(aConn, strEpfEmployee, amtEpfEmployee, biz.shujutech.payroll.SalarySlip.Deductions) == null) {
			//aPayslip.addDeductionTrx(aConn, strEpfEmployee, amtEpfEmployee);
			msiaPayslip.createEpfWorkerTrx(aConn, aEmployee, EmploymentMalaysia.EPF, strEpfEmployee, amtEpfEmployee, trxDate);
		}

		// boss epf
		String strEpfEmployer = SalarySlipMalaysia.EPF_EMPLOYER;
		if (msiaPayslip.modifySalaryTrx(aConn, strEpfEmployer, amtEpfEmployer, biz.shujutech.payroll.SalarySlip.Others) == null) {
			//aPayslip.addOtherTrx(aConn, strEpfEmployer, amtEpfEmployer);
			msiaPayslip.createEpfBossTrx(aConn, this.getEmployer(aConn), EmploymentMalaysia.EPF, strEpfEmployer, amtEpfEmployer, trxDate);
		}
		
		return(amtEpfEmployee);
	}

	public void generateSocso(Connection aConn, Person aEmployee, SalarySlipMalaysia msiaPayslip, Money basicSalary, DateTime trxDate) throws Exception {
		Double employeeAge = aEmployee.getAge();
		Boolean isNewSocsoMember = this.isNewSocsoMember();
		Double wageBeforeInvalidity = null;
		if (this.getWageBeforeInvalidity(aConn) != null && this.getWageBeforeInvalidity(aConn).getValueDouble() != null && this.getWageBeforeInvalidity(aConn).getValueDouble() != 0D) {
			wageBeforeInvalidity = this.getWageBeforeInvalidity(aConn).getValueDouble();
		}
		this.generateSocsoTrx(aConn, msiaPayslip, basicSalary, employeeAge, isNewSocsoMember, wageBeforeInvalidity, trxDate, aEmployee);
	}

	public void generateEis(Connection aConn, Person aEmployee, SalarySlipMalaysia msiaPayslip, String salarySlipYear, DateTime trxDate) throws Exception {
		if (Integer.parseInt(salarySlipYear) > 2017) {
			// employment insurance scheme, eis
			Money eisAmtBoss = Money.CreateMoney(aConn, msiaPayslip.getDefaultCurrencyCode(aConn));
			Money eisAmtWorker = Money.CreateMoney(aConn, msiaPayslip.getDefaultCurrencyCode(aConn));
			String strEisBoss = SalarySlipMalaysia.EIS_EMPLOYER;
			String strEisWorker = SalarySlipMalaysia.EIS_EMPLOYEE;
			msiaPayslip.modifySalaryTrx(aConn, strEisBoss, eisAmtBoss, biz.shujutech.payroll.SalarySlip.Others);
			msiaPayslip.modifySalaryTrx(aConn, strEisWorker, eisAmtWorker, biz.shujutech.payroll.SalarySlip.Deductions);

			MsiaEisCalculator.GetEisAmount(aConn, salarySlipYear, msiaPayslip, eisAmtBoss, eisAmtWorker);

			// boss eis
			if (msiaPayslip.modifySalaryTrx(aConn, strEisBoss, eisAmtBoss, biz.shujutech.payroll.SalarySlip.Others) == null) {
				//msiaPayslip.addOtherTrx(aConn, strEisBoss, eisAmtBoss);
				msiaPayslip.createEisBossTrx(aConn, this.getEmployer(aConn), EmploymentMalaysia.SOCSO, strEisBoss, eisAmtBoss, trxDate);
			}

			// worker eis
			if (msiaPayslip.modifySalaryTrx(aConn, strEisWorker, eisAmtWorker, biz.shujutech.payroll.SalarySlip.Deductions) == null) {
				//msiaPayslip.addDeductionTrx(aConn, strEisWorker, eisAmtWorker);
				msiaPayslip.createEisWorkerTrx(aConn, aEmployee, EmploymentMalaysia.SOCSO, strEisWorker, eisAmtWorker, trxDate);
			}
		}
	}

	public void generateTax(Connection aConn, Person aEmployee, SalarySlipMalaysia msiaPayslip, String salarySlipYear, Money amtEpfEmployee, DateTime trxDate, List<SalarySlip> aNewPayslip) throws Exception {
		Money amtTax = Money.CreateMoney(aConn, msiaPayslip.getDefaultCurrencyCode(aConn));
		String strTax = SalarySlipMalaysia.TAX;
		msiaPayslip.modifySalaryTrx(aConn, strTax, amtTax, biz.shujutech.payroll.SalarySlip.Deductions);

		Marital employeeMarital;
		if (aEmployee.gotMaritalStatus(aConn)) {
			employeeMarital = aEmployee.getMaritalStatus(aConn);
		} else {
			//employeeMarital = Marital.Single;
			throw new Hinderance("Payroll information not setup correctly, employee marital status not stated");
		}

		Integer noOfChild = aEmployee.getNoOfChild();
		if (noOfChild == null) noOfChild = 0;
		Boolean isSpouseWorking = false;
		if (aEmployee.getSpouse(aConn) != null) {
			isSpouseWorking = aEmployee.getSpouse(aConn).isWorking();
		}

		boolean isMalaysianOrPr = aEmployee.isMalaysiaCitizenOrPr(aConn);

		amtTax = MsiaTaxCalculator.GetMonthlyTaxAmount(aConn, this, salarySlipYear, msiaPayslip, amtEpfEmployee, employeeMarital, isSpouseWorking, noOfChild, isMalaysianOrPr, aNewPayslip);
		//App.logDebg(this, "PCB amount: " + msiaPayslip.getTaxAmountWithComma(aConn));
		if (msiaPayslip.modifySalaryTrx(aConn, strTax, amtTax, biz.shujutech.payroll.SalarySlip.Deductions) == null) {
			msiaPayslip.createTaxTrx(aConn, aEmployee, EmploymentMalaysia.LHDN, strTax, amtTax, trxDate);
		}
	}

	@Override
	public SalarySlip generateSalarySlipByMonth(Connection aConn, SalarySlip aPayslip, Person aEmployee) throws Exception {
		List<SalarySlip> newPayslipList = new CopyOnWriteArrayList<>();
		return(this.generateSalarySlipByMonth(aConn, aPayslip, aEmployee, newPayslipList));
	}

	@Override
	public SalarySlip generateSalarySlipByMonth(Connection aConn, SalarySlip aPayslip, Person aEmployee, List<SalarySlip> aNewPayslip) throws Exception {
		SalarySlipMalaysia msiaPayslip = (SalarySlipMalaysia) aPayslip;

		InitStatutoryCompany(aConn); // FetchUnique EPF, SOCSO, LHDN organization if haven't
		DateTime trxDate = DateAndTime.GetMonthEnd(msiaPayslip.getPeriodTo());

		String salarySlipYear = String.valueOf(msiaPayslip.getPeriodFrom().getYear());
		Money basicSalary = getSalary(aConn).getSalaryAmount(aConn);

		// epf
		Money amtEpfEmployee = Money.CreateMoney(aConn, aPayslip.getDefaultCurrencyCode(aConn));
		amtEpfEmployee.setValue(Country.Malaysia.getCurrencyCode(), "0.00");
		if (!(aEmployee.isMalaysiaCitizenOrPr(aConn)) && (this.getEpfNo() == null || this.getEpfNo().isEmpty())) {
			// is not malaysian and epf no is not provided, will not do epf, so do nothing
		} else {
			amtEpfEmployee = this.generateEpf(aConn, aEmployee, msiaPayslip, salarySlipYear, trxDate);
		}

		// socso
		if (!(aEmployee.isMalaysiaCitizenOrPr(aConn)) && (this.getSocsoNo() == null || this.getSocsoNo().isEmpty())) {
			// is not malaysian and socso no is not provided, will not do socso, so do nothing
		} else {
			this.generateSocso(aConn, aEmployee, msiaPayslip, basicSalary, trxDate);
		}

		// EIS, only for Malaysian?
		if (aEmployee.isMalaysiaCitizenOrPr(aConn)) {
			this.generateEis(aConn, aEmployee, msiaPayslip, salarySlipYear, trxDate);
		}

		// pcb, compute taxable wage from total annual income and epf, this is the smplest method, later do the complext accurete mthly deduction
		this.generateTax(aConn, aEmployee, msiaPayslip, salarySlipYear, amtEpfEmployee, trxDate, aNewPayslip);

		return(msiaPayslip);
	}

	public Money computeEpfEmployee(Connection aConn, MsiaEpfCalculator aEpfCalculator, Salary aBasicSalary, SalarySlip aSlip) throws Exception {
		int employeeRate = aEpfCalculator.getEmployeeRate();
		Money result = this.computeEpf(aConn, aBasicSalary, employeeRate);
		return(result);
	}

	public Money computeEpfEmployer(Connection aConn, MsiaEpfCalculator aEpfCalculator, Salary aBasicSalary, SalarySlip aSlip) throws Exception {
		int employerRate = aEpfCalculator.getEmployerRate();
		Money result = this.computeEpf(aConn, aBasicSalary, employerRate);
		return(result);
	}

	private Money computeEpf(Connection aConn, Salary aBasicSalary, int aRate) throws Exception {
		double basicSalary =  aBasicSalary.getSalaryAmount(aConn).getValueDouble();
		double amount = ((double) (aRate / 100.0)) * basicSalary;
		Money result = (Money) ObjectBase.CreateObject(aConn, Money.class);
		result.setValue(aBasicSalary.getSalaryAmount(aConn).getCurrencyCode(), amount);
		return(result);
	}

	public MsiaEpfCalculator getEpfCalculator(Connection aConn) throws Exception {
		MsiaEpfCalculator epfCalculator = (MsiaEpfCalculator) this.getFieldObject(EpfCalculator).getValueObj(aConn);
		return (epfCalculator);
	}

	public boolean gotEpfCalculator(Connection aConn) throws Exception {
		boolean result = false;
		Clasz clasz = this.gotValueObject(aConn, EpfCalculator);
		if (clasz != null) result = true;
		return(result);
	}

	public void setEpfCalculator(MsiaEpfCalculator aRate) throws Exception {
		this.setValueObject(EpfCalculator, aRate);
	}

	public void generateSocsoTrx(Connection aConn, SalarySlipMalaysia aSlip, Money aBasicSalary, Double aAge, Boolean aIsNewMember, Double aWageBeforeInvalidity, DateTime aTrxDate, Person aEmployee) throws Exception {
		Money socsoAmtBoss = Money.CreateMoney(aConn, aSlip.getDefaultCurrencyCode(aConn)); 
		Money socsoAmtWorker = Money.CreateMoney(aConn, aSlip.getDefaultCurrencyCode(aConn)); 
		String trxNameEmployer = SalarySlipMalaysia.SOCSO_EMPLOYER;
		String trxNameEmployee = SalarySlipMalaysia.SOCSO_EMPLOYEE;
		aSlip.modifySalaryTrx(aConn, trxNameEmployer, socsoAmtBoss, biz.shujutech.payroll.SalarySlip.Others);
		aSlip.modifySalaryTrx(aConn, trxNameEmployee, socsoAmtWorker, biz.shujutech.payroll.SalarySlip.Deductions);


		String whatCategory = MsiaSocsoCalculator.GetSocsoAmount(aConn, aSlip, aBasicSalary, aAge, aIsNewMember, aWageBeforeInvalidity, socsoAmtBoss, socsoAmtWorker);
		if (whatCategory.equals("First Category")) {
			if (aSlip.modifySalaryTrx(aConn, trxNameEmployer, socsoAmtBoss, biz.shujutech.payroll.SalarySlip.Others) == null) {
				//aSlip.addOtherTrx(aConn, trxNameEmployer, socsoAmtBoss);
				aSlip.createSocsoBossTrx(aConn, this.getEmployer(aConn), EmploymentMalaysia.SOCSO, trxNameEmployer, socsoAmtBoss, aTrxDate);

			}

			if (aSlip.modifySalaryTrx(aConn, trxNameEmployee, socsoAmtWorker, biz.shujutech.payroll.SalarySlip.Deductions) == null) {
				//aSlip.addDeductionTrx(aConn, trxNameEmployee, socsoAmtWorker);
				aSlip.createSocsoWorkerTrx(aConn, aEmployee, EmploymentMalaysia.SOCSO, trxNameEmployee, socsoAmtWorker, aTrxDate);
			}
		} else {
			if (aSlip.modifySalaryTrx(aConn, trxNameEmployer, socsoAmtBoss, biz.shujutech.payroll.SalarySlip.Others) == null) {
				//aSlip.addOtherTrx(aConn, trxNameEmployer, socsoAmtBoss);
				aSlip.createSocsoBossTrx(aConn, this.getEmployer(aConn), EmploymentMalaysia.SOCSO, trxNameEmployer, socsoAmtBoss, aTrxDate);
			}
		}
	}



}
