package biz.shujutech.payroll;

import biz.shujutech.base.App;
import biz.shujutech.base.Connection;
import biz.shujutech.base.DateAndTime;
import biz.shujutech.base.Hinderance;
import biz.shujutech.db.object.Clasz;
import biz.shujutech.db.object.ObjectBase;
import biz.shujutech.db.relational.BaseDb;
import biz.shujutech.db.relational.FieldType;
import biz.shujutech.bznes.AddrTypeOrganization;
import biz.shujutech.bznes.CompanyMalaysia;
import biz.shujutech.bznes.ContactBankAccountType;
import biz.shujutech.bznes.Country;
import biz.shujutech.bznes.EmploymentMalaysia;
import biz.shujutech.bznes.Person;
import biz.shujutech.reflect.ReflectField;
import biz.shujutech.technical.LambdaCounter;
import biz.shujutech.technical.LambdaDouble;
import biz.shujutech.technical.LambdaLong;
import biz.shujutech.util.Generic;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import org.joda.time.DateTime;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class BankPaymentCimb extends Clasz {
	@ReflectField(type=FieldType.STRING, size=64, displayPosition=5) public static String PayrollCompanyName;
	@ReflectField(type=FieldType.STRING, size=32, displayPosition=10) public static String PayrollCompanyAlias;
	@ReflectField(type=FieldType.STRING, size=7, displayPosition=20) public static String OrganisationCode;
	@ReflectField(type=FieldType.STRING, size=70, displayPosition=25) public static String ContactPerson;
	@ReflectField(type=FieldType.STRING, size=30, displayPosition=30) public static String ContactPhone; 
	@ReflectField(type=FieldType.STRING, size=40, displayPosition=35) public static String ContactEmail;

	//public static final String XHTML_FILE = "C:/Shujutech/StApp/StEMS/src/java/biz/shujutech/payroll/BankPaymentCimb.html";
	public static final String XHTML_FILE = "C:/Shujutech/StApp/StEMS/web/v01/BankPaymentCimb.html";

	private String bnmCode;
	private String epfStateCode;
	private String epfWorkerName1;
	private String epfWorkerName2;

	private String companyName;
	private String autopayCode;

	private String beneficiaryName;
	private String beneficiaryId;
	private String bnm;
	private String accountNo;
	private Double paymentAmountDouble;
	private String paymentAmount;
	private String referenceNo;
	private String paymentDescription;

	private String lhdnEmployerNo;
	private String lhdnEmailAddr;
	private String epfEmployerNo;
	private String epfContactPerson;
	private String epfContactNo;
	private String socsoEmployerCode;
	private String mycoId;
	private String zakatEmployerRef;

	private Double employeeContributionDouble;
	private Double employerContributionDouble;
	private Double employeeWagesDouble;
	private Double socsoAmountDouble;
	private Double pcbAmountDouble;
	private Double cp38AmountDouble;
	private Double zakatAmountDouble;

	private String staffName;
	private String staffNo;
	private String oldIcNo;
	private String newIcNo;
	private String passportNo;
	private DateTime contributionMonth;
	private String employeeEpfNo;
	private String employeeWages;
	private String employeeContribution;
	private String employerContribution;
	private String employeeSocsoIdNo;
	private String socsoAmount;
	private String employmentDate;
	private String employmentStatus;
	private String taxReferenceNo;
	private String countryCode;
	private String countryName;
	private String cp38Amount;
	private String wifeCode;
	private String pcbAmount;
	private String zakatAmount;
	private String zakatPaymentClassification;
	private static Map<String, String> BnmMap = new ConcurrentHashMap<>();
	private static Map<String, String> EpfStateMap = new ConcurrentHashMap<>();

	public static void PopulateBnmMap(Map<String, String> bnmMap) {
		bnmMap.put("Affin Bank Berhad", "32");
		bnmMap.put("Alliance Bank Malaysia Berhad", "12");
		bnmMap.put("AmBank (M) Berhad", "08");
		bnmMap.put("Bangkok Bank Berhad", "");
		bnmMap.put("Bank of America Malaysia Berhad", "");
		bnmMap.put("Bank of China (Malaysia) Berhad", "");
		bnmMap.put("Bank of Tokyo-Mitsubishi UFJ (Malaysia) Berhad", "");
		bnmMap.put("BNP Paribas Malaysia Berhad", "");
		bnmMap.put("China Construction Bank (Malaysia) Berhad", "");
		bnmMap.put("CIMB Bank Berhad", "35");
		bnmMap.put("Citibank Berhad", "17");
		bnmMap.put("Deutsche Bank (Malaysia) Berhad", "19");
		bnmMap.put("Hong Leong Bank Berhad", "24");
		bnmMap.put("HSBC Bank Malaysia Berhad", "22");
		bnmMap.put("India International Bank (Malaysia) Berhad", "");
		bnmMap.put("Industrial and Commercial Bank of China (Malaysia) Berhad", "59");
		bnmMap.put("J.P. Morgan Chase Bank Berhad", "48");
		bnmMap.put("Malayan Banking Berhad", "27");
		bnmMap.put("Mizuho Bank (Malaysia) Berhad", "73");
		bnmMap.put("National Bank of Abu Dhabi Malaysia Berhad", "");
		bnmMap.put("OCBC Bank (Malaysia) Berhad", "29");
		bnmMap.put("Public Bank Berhad", "33");
		bnmMap.put("RHB Bank Berhad", "18");
		bnmMap.put("Standard Chartered Bank Malaysia Berhad", "14");
		bnmMap.put("Sumitomo Mitsui Banking Corporation Malaysia Berhad", "51");
		bnmMap.put("The Bank of Nova Scotia Berhad", "");
		bnmMap.put("United Overseas Bank (Malaysia) Bhd", "26");
	}

	public static void ProcessBankFileHeader(Connection aConn, CompanyMalaysia aCompany, BankPaymentCimb bankPaymentCimb, String aAutopayCode, String aContactEmail, String aContactPerson, String aContactPhone) throws Exception {
		//if (aAutopayCode.length() != 5) throw new Exception("Autopay code provided by CIMB must be 5 char");
		bankPaymentCimb.setCompanyName(Generic.TrimPadVerifyLen("Company Name", aCompany.getName(), 40));
		bankPaymentCimb.setAutopayCode(Generic.TrimPadVerifyLen("Organisation Code", aAutopayCode, 5));

		bankPaymentCimb.setLhdnEmployerNo(Generic.TrimPadVerifyLen("LHDN Employer No", aCompany.getLhdnNoMajikanE(), 10));
		bankPaymentCimb.setLhdnEmailAddr(Generic.TrimPadVerifyLen("LHDN Email Addr", aContactEmail, 40));
		bankPaymentCimb.setEpfEmployerNo(aCompany.getKwspNoMajikan());
		bankPaymentCimb.setEpfContactPerson(Generic.TrimPadVerifyLen("Contact Person", aContactPerson, 40));
		bankPaymentCimb.setEpfContactNo(Generic.TrimPadVerifyLen("Contact No", MsiaGeneric.NoDashSlash(aContactPhone), 20));
		bankPaymentCimb.setSocsoEmployerCode(Generic.TrimPadVerifyLen("SOCSO Employer Code", aCompany.getSocsoEmployerCode(), 12));
		bankPaymentCimb.setMycoId(MsiaGeneric.NoDashSlash(aCompany.getMycoId()));
		bankPaymentCimb.setZakatEmployerRef(MsiaGeneric.NoDashSlash(aCompany.getZakatEmployerRef()));

	}

	public static void ProcessPayslipDetail(Connection aConn, SalarySlipMalaysia payslip, BankPaymentCimb bankPaymentCimb, CompanyMalaysia aCompany) throws Exception {
		EmploymentMalaysia msiaJob = (EmploymentMalaysia) payslip.getJob(aConn);
		Person worker = aCompany.getEmployee(aConn, payslip.getEmployee(), payslip.getEmployeeAlias());
		String msiaIcNew = "";
		String msiaIcOld = "";
		if (worker.getMalaysiaIc(aConn) != null) {
			msiaIcNew = MsiaGeneric.NoDashSlash(worker.getMalaysiaIc(aConn).getNewIdentityCardNo());
			msiaIcOld = MsiaGeneric.NoDashSlash(worker.getMalaysiaIc(aConn).getOldIdentityCardNo());
			if (msiaIcNew == null) msiaIcNew = "";
			if (msiaIcOld == null) msiaIcOld = "";
		}

		String socsoNum;
		if (msiaJob.getSocsoNo().trim().isEmpty()) {
			socsoNum = MsiaGeneric.NoDashSlash(worker.getIdNoForMsia(aConn));
		} else {
			socsoNum = MsiaGeneric.NoDashSlash(msiaJob.getSocsoNo());
		}

		String countryCode = MsiaGeneric.GetCountryCode(aConn, worker);

		// bulk payment salary payment 
		String strId = Generic.TrimLen(worker.getIdNoForMsia(aConn), 5) + Generic.GetRandom(5);
		String strRef = MsiaGeneric.NoDashSlash(strId + DateAndTime.FormatDisplayNoTime(payslip.getPeriodTo()).replaceAll(" ", ""));
		String bnmCode = Generic.TrimLen(GetBnmCode(GetBnmMap(), worker.getPayrollBankName(aConn)), 2);
		String bnmCodeAndName = "";
		if (bnmCode != null && !bnmCode.isEmpty()) {
			bnmCodeAndName = bnmCode + "-" + worker.getPayrollBankName(aConn);
		}
		bankPaymentCimb.setBnmCode(bnmCode);

		bankPaymentCimb.setBeneficiaryName(Generic.TrimPadVerifyLen("Employee Name", worker.getName(), 40));
		bankPaymentCimb.setBeneficiaryId(Generic.TrimPadVerifyLen("Employee Id", MsiaGeneric.NoDashSlash(worker.getIdNoForMsia(aConn)), 20));
		bankPaymentCimb.setBnm(Generic.TrimPadLen(bnmCodeAndName, 30));
		bankPaymentCimb.setAccountNo(Generic.TrimPadVerifyLen("Bank Account No", MsiaGeneric.NoDashSlash(worker.getPayrollBankAcctNo(aConn)), 16));
		bankPaymentCimb.setPaymentAmountDouble(payslip.getNetPay(aConn).getValueDouble());
		bankPaymentCimb.setPaymentAmount(Generic.TrimPadVerifyLen("Net Pay", payslip.getNetPay(aConn).getAmountWithComma(), 10));
		bankPaymentCimb.setReferenceNo(Generic.TrimPadVerifyLen("Ref No", strRef, 30));
		bankPaymentCimb.setPaymentDescription(Generic.TrimPadVerifyLen("Payment Description", "Salary", 20));

		String epfWorkerName1 = Generic.TrimLen(worker.getName(), 40);
		String epfWorkerName2 = Generic.SubstrFromPosition(worker.getName(), 40);
		bankPaymentCimb.setEpfWorkerName1(Generic.TrimPadVerifyLen("Employee Name1", epfWorkerName1, 40));
		bankPaymentCimb.setEpfWorkerName2(Generic.TrimPadVerifyLen("Employee Name2", epfWorkerName2, 40));

		// statutory payment 
		bankPaymentCimb.setStaffName(Generic.TrimPadVerifyLen("Employee Name", worker.getName(), 60));
		bankPaymentCimb.setStaffNo(Generic.TrimPadVerifyLen("Employee Id", worker.getIdNoForMsia(aConn), 20));
		bankPaymentCimb.setOldIcNo(Generic.TrimPadVerifyLen("Old Ic No", msiaIcOld, 15));
		bankPaymentCimb.setNewIcNo(Generic.TrimPadVerifyLen("New Ic No", msiaIcNew, 15));
		bankPaymentCimb.setPassportNo(Generic.TrimPadVerifyLen("Passport No", MsiaGeneric.NoDashSlash(worker.getPassportNo(aConn)), 12));
		bankPaymentCimb.setEmployeeEpfNo(msiaJob.getEpfNo());
		bankPaymentCimb.setEmployeeWages(Generic.TrimPadVerifyLen("Salary", msiaJob.getSalary(aConn).getSalaryAmount(aConn).getAmountWithComma(), 15));
		bankPaymentCimb.setEmployeeWagesDouble(msiaJob.getSalary(aConn).getSalaryAmount(aConn).getValueDouble());
		bankPaymentCimb.setEmployeeContribution(Generic.TrimPadVerifyLen("EPF Employee Amount", payslip.getEpfWorkerAmountWithComma(aConn), 15));
		bankPaymentCimb.setEmployerContribution(Generic.TrimPadVerifyLen("EPF Employer Amount", payslip.getEpfBossAmountWithComma(aConn), 15));
		bankPaymentCimb.setEmployeeContributionDouble(payslip.getEpfWorker(aConn).getValueDouble());
		bankPaymentCimb.setEmployerContributionDouble(payslip.getEpfBoss(aConn).getValueDouble());
		bankPaymentCimb.setEmployeeSocsoIdNo(Generic.TrimPadVerifyLen("Employee SOCSO No", socsoNum, 12));
		bankPaymentCimb.setSocsoAmount(Generic.TrimPadVerifyLen("Total SOCSO Amount", payslip.getSocsoTotalAmountWithComma(aConn), 5));
		bankPaymentCimb.setSocsoAmountDouble(payslip.getSocsoTotalAmount(aConn).getValueDouble());
		bankPaymentCimb.setEmploymentDate(Generic.TrimPadVerifyLen("Start Date", DateAndTime.AsString(msiaJob.getStartDate(), MsiaGeneric.FORMAT_DATE_ONLY), 10));
		bankPaymentCimb.setEmploymentStatus(Generic.TrimPadVerifyLen("Employment Status", "", 1));
		bankPaymentCimb.setTaxReferenceNo(Generic.TrimPadVerifyLen("Tax No", msiaJob.getTaxNo(), 10));
		bankPaymentCimb.setCountryCode(Generic.TrimPadVerifyLen("Country Code", countryCode, 2));
		bankPaymentCimb.setCountryName(countryCode); // put in country desc e.g. MY-MALAYSIA
		bankPaymentCimb.setCp38Amount(Generic.TrimPadVerifyLen("CP38 Amount", "", 8));
		bankPaymentCimb.setCp38AmountDouble(0D);
		bankPaymentCimb.setWifeCode(Generic.TrimPadVerifyLen("Wife Code", MsiaGeneric.GetWifeCode(aConn, msiaJob), 1));
		bankPaymentCimb.setPcbAmount(Generic.TrimPadVerifyLen("Tax Amount", payslip.getTaxAmountWithComma(aConn), 9));
		bankPaymentCimb.setPcbAmountDouble(payslip.getTax(aConn).getValueDouble());
		bankPaymentCimb.setZakatAmount(Generic.TrimPadVerifyLen("Zakat Amount", "", 15));
		bankPaymentCimb.setZakatAmountDouble(0D);
		bankPaymentCimb.setZakatPaymentClassification(Generic.TrimPadVerifyLen("Zakat Classification", "", 20));
	}

	public static String CreateBankHtml(Connection aConn, CompanyMalaysia aCompany, List<PayslipRun> aPayslipList, String aAutopayCode, String aContactEmail, String aContactPerson, String aContactPhone) throws Exception {
		String urlPath = BankPaymentCimb.class.getClassLoader().getResource("resources/BankPaymentCimb.html").getPath();

		// statutory payment company info
		BankPaymentCimb bankPaymentCimb = new BankPaymentCimb();
		PopulateBnmMap(GetBnmMap());
		//PopulateEpfStateMap(GetEpfStateMap());

		ProcessBankFileHeader(aConn, aCompany, bankPaymentCimb, aAutopayCode, aContactEmail, aContactPerson, aContactPhone); // create the header data for bank bulk and statutory 

		List<List<String>> statCompanyInfoList = new CopyOnWriteArrayList<>();
		List<String> statCompanyInfo = new CopyOnWriteArrayList();
		statCompanyInfo.add(MsiaGeneric.ZeroIfBlank(bankPaymentCimb.getCompanyName()));
		statCompanyInfo.add(MsiaGeneric.ZeroIfBlank(bankPaymentCimb.getLhdnEmployerNo()));
		statCompanyInfo.add(MsiaGeneric.ZeroIfBlank(bankPaymentCimb.getLhdnEmailAddr()));
		statCompanyInfo.add(MsiaGeneric.ZeroIfBlank(bankPaymentCimb.getEpfEmployerNo()));
		statCompanyInfo.add(MsiaGeneric.ZeroIfBlank(bankPaymentCimb.getEpfContactPerson()));
		statCompanyInfo.add(MsiaGeneric.ZeroIfBlank(bankPaymentCimb.getEpfContactNo()));
		statCompanyInfo.add(MsiaGeneric.ZeroIfBlank(bankPaymentCimb.getSocsoEmployerCode()));
		statCompanyInfo.add(MsiaGeneric.ZeroIfBlank(bankPaymentCimb.getMycoId()));
		statCompanyInfo.add(MsiaGeneric.ZeroIfBlank(bankPaymentCimb.getZakatEmployerRef()));
		statCompanyInfoList.add(statCompanyInfo);

		Document htmlDoc = ReportHtml.CreateHtmlDoc(urlPath);
		ReportHtml.SetReportText(htmlDoc, "company-name", bankPaymentCimb.getCompanyName());
		ReportHtml.SetReportText(htmlDoc, "autopay-code", bankPaymentCimb.getAutopayCode());
		Element tblStatCompanyInfo = ReportHtml.GetHtmlTable(htmlDoc, "tbl-stat-company-info");
		ReportHtml.PopulateTBodyData(tblStatCompanyInfo, statCompanyInfoList);

		List<List<String>> bulkPaymentList = new CopyOnWriteArrayList<>();
		List<List<String>> statutoryPaymentList = new CopyOnWriteArrayList<>();
		for(PayslipRun eachPayslipRun: aPayslipList) {
			eachPayslipRun.getGeneratedPayslips().forEachMember(aConn, ((Connection bConn, Clasz aPayslip) -> {
				SalarySlipMalaysia payslip = (SalarySlipMalaysia) aPayslip;
				payslip.fetchAllTrx(aConn);
				ProcessPayslipDetail(aConn, payslip, bankPaymentCimb, aCompany); // create the bank detail record from payslip

				// detail salary / net pay listing
				List<String> bulkPaymentRec = new CopyOnWriteArrayList();
				bulkPaymentRec.add(bankPaymentCimb.getBeneficiaryName());
				bulkPaymentRec.add(bankPaymentCimb.getBeneficiaryId());
				bulkPaymentRec.add(bankPaymentCimb.getBnm());
				bulkPaymentRec.add(bankPaymentCimb.getAccountNo());
				bulkPaymentRec.add(bankPaymentCimb.getPaymentAmount());
				bulkPaymentRec.add(bankPaymentCimb.getReferenceNo());
				bulkPaymentRec.add(bankPaymentCimb.getPaymentDescription());
				bulkPaymentList.add(bulkPaymentRec);

				// detail statutory payments
				List<String> statutoryPaymentRec = new CopyOnWriteArrayList();
				statutoryPaymentRec.add(bankPaymentCimb.getStaffName());
				statutoryPaymentRec.add(bankPaymentCimb.getStaffNo());
				statutoryPaymentRec.add(bankPaymentCimb.getOldIcNo());
				statutoryPaymentRec.add(bankPaymentCimb.getNewIcNo());
				statutoryPaymentRec.add(bankPaymentCimb.getPassportNo());
				statutoryPaymentRec.add(bankPaymentCimb.getEmployeeEpfNo());
				statutoryPaymentRec.add(bankPaymentCimb.getEmployeeWages());
				statutoryPaymentRec.add(bankPaymentCimb.getEmployeeContribution());
				statutoryPaymentRec.add(bankPaymentCimb.getEmployerContribution());
				statutoryPaymentRec.add(bankPaymentCimb.getEmployeeSocsoIdNo());
				statutoryPaymentRec.add(bankPaymentCimb.getSocsoAmount());
				statutoryPaymentRec.add(bankPaymentCimb.getEmploymentDate());
				statutoryPaymentRec.add(bankPaymentCimb.getEmploymentStatus());
				statutoryPaymentRec.add(bankPaymentCimb.getTaxReferenceNo());
				statutoryPaymentRec.add(bankPaymentCimb.getCountryName());
				statutoryPaymentRec.add(bankPaymentCimb.getCp38Amount());
				statutoryPaymentRec.add(bankPaymentCimb.getWifeCode());
				statutoryPaymentRec.add(bankPaymentCimb.getPcbAmount());
				statutoryPaymentRec.add(bankPaymentCimb.getZakatAmount());
				statutoryPaymentRec.add(bankPaymentCimb.getZakatPaymentClassification());
				statutoryPaymentList.add(statutoryPaymentRec);

				ClearTransactionalData(bankPaymentCimb);
				return(true);
			}));
	
			Element tblBulkPaymentData = ReportHtml.GetHtmlTable(htmlDoc, "tbl-bulk-payment-data");
			ReportHtml.PopulateTBodyData(tblBulkPaymentData, bulkPaymentList);
	
			Element tblStatutoryPaymentData = ReportHtml.GetHtmlTable(htmlDoc, "tbl-stat-payment-data");
			ReportHtml.PopulateTBodyData(tblStatutoryPaymentData, statutoryPaymentList);
		}
	
		return(htmlDoc.toString());
	}

	public static ByteArrayOutputStream CreateBankFile(Connection aConn, CompanyMalaysia aCompany, List<PayslipRun> aPayslipList, String aAutopayCode, String aContactEmail, String aContactPerson, String aContactPhone) throws Exception {
		Map<String, FileInputStream> result = new ConcurrentHashMap<>();

		String companyName = Generic.TrimLen(aCompany.getName(), 10).replace(" ", "").replace("/", "").replace("-", "");
		String generateDate = DateAndTime.AsString(DateTime.now(), MsiaGeneric.FORMAT_DATE_FILE);
		String strSalaryFileName = companyName + "_salary_" + generateDate + ".txt"; // create file name for bulk/salary payment
		String strEpfFileName = companyName + "_epf_" + generateDate + ".txt"; // create file name for statutory payment
		String strSocsoFileName = companyName + "_socso_" + generateDate + ".txt"; // create file name for statutory payment
		String strPcbFileName = companyName + "_pcb_" + generateDate + ".txt"; // create file name for statutory payment
		String strZakatFileName = companyName + "_zakat_" + generateDate + ".txt"; // create file name for statutory payment

		ByteArrayOutputStream	streamSalary = new ByteArrayOutputStream();
		ByteArrayOutputStream	streamEpf = new ByteArrayOutputStream();
		ByteArrayOutputStream	streamSocso = new ByteArrayOutputStream();
		ByteArrayOutputStream	streamPcb = new ByteArrayOutputStream();
		ByteArrayOutputStream	streamZakat = new ByteArrayOutputStream();

		ByteArrayOutputStream	streamPcbDetail = new ByteArrayOutputStream(); // both pcb and zakat got summary information in the header
		ByteArrayOutputStream	streamZakatDetail = new ByteArrayOutputStream();

		// statutory payment company info
		BankPaymentCimb bankPaymentCimb = new BankPaymentCimb();
		PopulateBnmMap(GetBnmMap());
		MsiaGeneric.PopulateEpfStateMap(GetEpfStateMap());

		String stateCode = MsiaGeneric.GetEpfStateCode(aCompany.getPreferedAddr(aConn), GetEpfStateMap());
		bankPaymentCimb.setEpfStateCode(stateCode);
		ProcessBankFileHeader(aConn, aCompany, bankPaymentCimb, aAutopayCode, aContactEmail, aContactPerson, aContactPhone); // create the header data for bank bulk and statutory 
		DateTime contributionDate = MsiaGeneric.GetContributionMonth(aConn, aPayslipList);
		bankPaymentCimb.setContributionMonth(contributionDate);

		// net pay / salary
		String bulkHeader = CreateSalaryBatchHeader(bankPaymentCimb.getAutopayCode(), bankPaymentCimb.getCompanyName(), DateTime.now());
		streamSalary.write(bulkHeader.getBytes());

		// epf
		String epfHeader = CreateEpfBatchHeader(bankPaymentCimb);
		streamEpf.write(epfHeader.getBytes());

		// socso no header

		// pcba and zakat header at bottom

		LambdaCounter totalRec = new LambdaCounter();
		LambdaDouble totalAmount = new LambdaDouble();	
		LambdaDouble epfBossContribution = new LambdaDouble();	
		LambdaDouble epfWorkerContribution = new LambdaDouble();	
		LambdaLong epfTotalHash = new LambdaLong();	
		LambdaDouble pcbTotalAmount = new LambdaDouble();	
		LambdaDouble zakatTotalAmount = new LambdaDouble();	
		for(PayslipRun eachPayslipRun: aPayslipList) {
			eachPayslipRun.getGeneratedPayslips().forEachMember(aConn, ((Connection bConn, Clasz aPayslip) -> {
				SalarySlipMalaysia payslip = (SalarySlipMalaysia) aPayslip;
				payslip.fetchAllTrx(aConn);

				ProcessPayslipDetail(aConn, payslip, bankPaymentCimb, aCompany); // create the bank detail record from payslip

				// salary / net pay
				String salaryPayment = CreateSalaryBatchDetail(bankPaymentCimb);
				streamSalary.write(salaryPayment.getBytes());

				// epf
				String epfPayment = CreateEpfBatchDetail(bankPaymentCimb);
				streamEpf.write(epfPayment.getBytes());

				// socso
				String socsoPayment = CreateSocsoBatchDetail(bankPaymentCimb);
				streamSocso.write(socsoPayment.getBytes());

				// pcb
				String pcbPayment = CreatePcbBatchDetail(bankPaymentCimb);
				streamPcbDetail.write(pcbPayment.getBytes());

				// zakat
				String zakatPayment = CreateZakatBatchDetail(bankPaymentCimb);
				streamZakatDetail.write(zakatPayment.getBytes());

				// sum and totals
				totalRec.increment();
				totalAmount.sum(bankPaymentCimb.getPaymentAmountDouble());

				epfBossContribution.sum(bankPaymentCimb.getEmployerContributionDouble());
				epfWorkerContribution.sum(bankPaymentCimb.getEmployeeContributionDouble());

				if (bankPaymentCimb.getEmployeeEpfNo().isEmpty() == false) {
					Long workerEpfNum = Long.valueOf(bankPaymentCimb.getEmployeeEpfNo());
					epfTotalHash.sum(workerEpfNum);
				}

				pcbTotalAmount.sum(bankPaymentCimb.getEmployerContributionDouble());
				pcbTotalAmount.sum(bankPaymentCimb.getEmployeeContributionDouble());

				zakatTotalAmount.sum(bankPaymentCimb.getZakatAmountDouble());

				ClearTransactionalData(bankPaymentCimb);
				return(true);
			}));
		}

		// salary / net pay
		String trailerSalaryRec = CreateSalaryBatchTrailer(totalRec.getCntr(), totalAmount.getDouble());
		streamSalary.write(trailerSalaryRec.getBytes());

		// epf
		String trailerEpfRec = CreateEpfBatchTrailer(totalRec.getCntr(), epfBossContribution.getDouble(), epfWorkerContribution.getDouble(), epfTotalHash.getLong());
		streamEpf.write(trailerEpfRec.getBytes());

		// pcb
		String pcbHeader = CreatePcbBatchHeader(bankPaymentCimb, pcbTotalAmount.getDouble(), totalRec.getCntr());
		streamPcb.write(pcbHeader.getBytes());
		streamPcb.write(streamPcbDetail.toByteArray());

		// zakat
		String zakatHeader = CreateZakatBatchHeader(bankPaymentCimb, zakatTotalAmount.getDouble(), totalRec.getCntr());
		streamZakat.write(zakatHeader.getBytes());
		streamZakat.write(streamZakatDetail.toByteArray());
	
		// testing output files
		if (MsiaGeneric.CreateLocalFile()) {
			FileOutputStream fosSalary = new FileOutputStream("c:/temp/" + strSalaryFileName);
			streamSalary.writeTo(fosSalary);
			fosSalary.flush();
			fosSalary.close();

			FileOutputStream fosEpf = new FileOutputStream("c:/temp/" + strEpfFileName);
			streamEpf.writeTo(fosEpf);
			fosEpf.flush();
			fosEpf.close();

			FileOutputStream fosSocso = new FileOutputStream("c:/temp/" + strSocsoFileName);
			streamSocso.writeTo(fosSocso);
			fosSocso.flush();
			fosSocso.close();

			FileOutputStream fosPcb = new FileOutputStream("c:/temp/" + strPcbFileName);
			streamPcb.writeTo(fosPcb);
			fosPcb.flush();
			fosPcb.close();

			FileOutputStream fosZakat = new FileOutputStream("c:/temp/" + strZakatFileName);
			streamZakat.writeTo(fosZakat);
			fosZakat.flush();
			fosZakat.close();
		}

		// create zip file
		ByteArrayInputStream streamSalaryInput = new ByteArrayInputStream(streamSalary.toByteArray());
		ByteArrayInputStream streamEpfInput = new ByteArrayInputStream(streamEpf.toByteArray());
		ByteArrayInputStream streamSocsoInput = new ByteArrayInputStream(streamSocso.toByteArray());
		ByteArrayInputStream streamPcbInput = new ByteArrayInputStream(streamPcb.toByteArray());
		ByteArrayInputStream streamZakatInput = new ByteArrayInputStream(streamZakat.toByteArray());

		Map<String, ByteArrayInputStream> zippingMap = new ConcurrentHashMap<>();
		zippingMap.put(strSalaryFileName, streamSalaryInput); 
		zippingMap.put(strEpfFileName, streamEpfInput);
		zippingMap.put(strSocsoFileName, streamSocsoInput);
		zippingMap.put(strPcbFileName, streamPcbInput);
		zippingMap.put(strZakatFileName, streamZakatInput);
		ByteArrayOutputStream zippedStream = MsiaGeneric.ZipFile(zippingMap);

		if (MsiaGeneric.CreateLocalFile()) {
			FileOutputStream fosZip = new FileOutputStream("c:/temp/statutory.zip");
			//fosZip.write(zippedStream.toByteArray());
			zippedStream.writeTo(fosZip);
			fosZip.flush();
			fosZip.close();
		}

		return(zippedStream);
	}


	public static String GetBnmCode(Map<String, String> aBnmMap, String aBankName) throws Exception {
		String result = "";
		for (Map.Entry<String, String> entry : aBnmMap.entrySet()) {
			String bankName = entry.getKey();
			String bankCode = entry.getValue();

			String bankNameShort = Generic.TrimLen(bankName, 10).toLowerCase().trim();
			String workerBankName = Generic.TrimLen(aBankName, 10).toLowerCase().trim();
			if (!bankNameShort.isEmpty() && !workerBankName.isEmpty()) {
				if (bankNameShort.contains(workerBankName)) {
					//result = bankCode + "-" + bankName;
					result = bankCode;
					break;
				}
			}
		}

		return(result);
	}


	public static void ClearTransactionalData(BankPaymentCimb bankPaymentCimb) {
		bankPaymentCimb.setCompanyName("");
		bankPaymentCimb.setBeneficiaryName("");
		bankPaymentCimb.setBeneficiaryId("");
		bankPaymentCimb.setBnmCode("");
		bankPaymentCimb.setEpfStateCode("");
		bankPaymentCimb.setBnm("");
		bankPaymentCimb.setAccountNo("");
		bankPaymentCimb.setPaymentAmount("");
		bankPaymentCimb.setPaymentAmountDouble(0D);
		bankPaymentCimb.setReferenceNo("");
		bankPaymentCimb.setPaymentDescription("");

		bankPaymentCimb.setEpfWorkerName1("");
		bankPaymentCimb.setEpfWorkerName2("");

		bankPaymentCimb.setStaffName("");
		bankPaymentCimb.setStaffNo("");
		bankPaymentCimb.setOldIcNo("");
		bankPaymentCimb.setNewIcNo("");
		bankPaymentCimb.setPassportNo("");
		bankPaymentCimb.setEmployeeEpfNo("");
		bankPaymentCimb.setEmployeeWages("");
		bankPaymentCimb.setEmployeeContribution("");
		bankPaymentCimb.setEmployerContribution("");
		bankPaymentCimb.setEmployeeContributionDouble(0D);
		bankPaymentCimb.setEmployerContributionDouble(0D);
		bankPaymentCimb.setEmployeeSocsoIdNo("");
		bankPaymentCimb.setSocsoAmount("");
		bankPaymentCimb.setEmploymentDate("");
		bankPaymentCimb.setEmploymentStatus("");
		bankPaymentCimb.setTaxReferenceNo("");
		bankPaymentCimb.setCountryCode("");
		bankPaymentCimb.setCp38Amount("");
		bankPaymentCimb.setCp38AmountDouble(0D);
		bankPaymentCimb.setWifeCode("");
		bankPaymentCimb.setPcbAmount("");
		bankPaymentCimb.setPcbAmountDouble(0D);
		bankPaymentCimb.setZakatAmount("");
		bankPaymentCimb.setZakatAmountDouble(0D);
		bankPaymentCimb.setZakatPaymentClassification("");
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getAutopayCode() {
		return autopayCode;
	}

	public void setAutopayCode(String autopayCode) {
		this.autopayCode = autopayCode;
	}

	public String getBeneficiaryName() {
		return beneficiaryName;
	}

	public void setBeneficiaryName(String beneficiaryName) {
		this.beneficiaryName = beneficiaryName;
	}

	public String getBeneficiaryId() {
		return beneficiaryId;
	}

	public void setBeneficiaryId(String beneficiaryId) {
		this.beneficiaryId = beneficiaryId;
	}

	public String getBnm() {
		return bnm;
	}

	public void setBnm(String bnm) {
		this.bnm = bnm;
	}

	public String getAccountNo() {
		return accountNo;
	}

	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}

	public String getPaymentAmount() {
		return paymentAmount;
	}

	public void setPaymentAmount(String paymentAmount) {
		this.paymentAmount = paymentAmount;
	}

	public Double getPaymentAmountDouble() {
		return paymentAmountDouble;
	}

	public void setPaymentAmountDouble(Double paymentAmountDouble) {
		this.paymentAmountDouble = paymentAmountDouble;
	}

	public String getReferenceNo() {
		return referenceNo;
	}

	public void setReferenceNo(String referenceNo) {
		this.referenceNo = referenceNo;
	}

	public String getPaymentDescription() {
		return paymentDescription;
	}

	public void setPaymentDescription(String paymentDescription) {
		this.paymentDescription = paymentDescription;
	}

	public String getLhdnEmployerNo() {
		return lhdnEmployerNo;
	}

	public void setLhdnEmployerNo(String lhdnEmployerNo) {
		this.lhdnEmployerNo = lhdnEmployerNo;
	}

	public String getLhdnEmailAddr() {
		return lhdnEmailAddr;
	}

	public void setLhdnEmailAddr(String lhdnEmailAddr) {
		this.lhdnEmailAddr = lhdnEmailAddr;
	}

	public String getEpfEmployerNo() {
		return epfEmployerNo;
	}

	public void setEpfEmployerNo(String epfEmployerNo) {
		this.epfEmployerNo = epfEmployerNo;
	}

	public String getEpfContactPerson() {
		return epfContactPerson;
	}

	public void setEpfContactPerson(String epfContactPerson) {
		this.epfContactPerson = epfContactPerson;
	}

	public String getEpfContactNo() {
		return epfContactNo;
	}

	public void setEpfContactNo(String epfContactNo) {
		this.epfContactNo = epfContactNo;
	}

	public String getSocsoEmployerCode() {
		return socsoEmployerCode;
	}

	public void setSocsoEmployerCode(String socsoEmployerCode) {
		this.socsoEmployerCode = socsoEmployerCode;
	}

	public String getMycoId() {
		return mycoId;
	}

	public void setMycoId(String mycoId) {
		this.mycoId = mycoId;
	}

	public String getZakatEmployerRef() {
		return zakatEmployerRef;
	}

	public void setZakatEmployerRef(String zakatEmployerRef) {
		this.zakatEmployerRef = zakatEmployerRef;
	}

	public String getStaffName() {
		return staffName;
	}

	public void setStaffName(String staffName) {
		this.staffName = staffName;
	}

	public String getStaffNo() {
		return staffNo;
	}

	public void setStaffNo(String staffNo) {
		this.staffNo = staffNo;
	}

	public String getOldIcNo() {
		return oldIcNo;
	}

	public void setOldIcNo(String oldIcNo) {
		this.oldIcNo = oldIcNo;
	}

	public String getNewIcNo() {
		return newIcNo;
	}

	public void setNewIcNo(String newIcNo) {
		this.newIcNo = newIcNo;
	}

	public String getPassportNo() {
		return passportNo;
	}

	public void setPassportNo(String passportNo) {
		this.passportNo = passportNo;
	}

	public DateTime getContributionMonth() {
		return contributionMonth;
	}

	public void setContributionMonth(DateTime epfContributionMonth) {
		this.contributionMonth = epfContributionMonth;
	}

	public String getEmployeeEpfNo() {
		return employeeEpfNo;
	}

	public void setEmployeeEpfNo(String employeeEpfNo) {
		this.employeeEpfNo = employeeEpfNo;
	}

	public String getEmployeeWages() {
		return employeeWages;
	}

	public void setEmployeeWages(String employeeWages) {
		this.employeeWages = employeeWages;
	}

	public String getEmployeeContribution() {
		return employeeContribution;
	}

	public void setEmployeeContribution(String employeeContribution) {
		this.employeeContribution = employeeContribution;
	}

	public String getEmployerContribution() {
		return employerContribution;
	}

	public void setEmployerContribution(String employerContribution) {
		this.employerContribution = employerContribution;
	}

	public Double getEmployeeContributionDouble() {
		return employeeContributionDouble;
	}

	public void setEmployeeContributionDouble(Double employeeContributionDouble) {
		this.employeeContributionDouble = employeeContributionDouble;
	}

	public Double getEmployerContributionDouble() {
		return employerContributionDouble;
	}

	public void setEmployerContributionDouble(Double employerContributionDouble) {
		this.employerContributionDouble = employerContributionDouble;
	}

	public Double getEmployeeWagesDouble() {
		return employeeWagesDouble;
	}

	public void setEmployeeWagesDouble(Double employeeWagesDouble) {
		this.employeeWagesDouble = employeeWagesDouble;
	}

	public String getEmployeeSocsoIdNo() {
		return employeeSocsoIdNo;
	}

	public void setEmployeeSocsoIdNo(String employeeSocsoIdNo) {
		this.employeeSocsoIdNo = employeeSocsoIdNo;
	}

	public String getSocsoAmount() {
		return socsoAmount;
	}

	public void setSocsoAmount(String socsoAmount) {
		this.socsoAmount = socsoAmount;
	}

	public Double getSocsoAmountDouble() {
		return socsoAmountDouble;
	}

	public void setSocsoAmountDouble(Double socsoAmountDouble) {
		this.socsoAmountDouble = socsoAmountDouble;
	}

	public String getEmploymentDate() {
		return employmentDate;
	}

	public void setEmploymentDate(String employmentDate) {
		this.employmentDate = employmentDate;
	}

	public String getEmploymentStatus() {
		return employmentStatus;
	}

	public void setEmploymentStatus(String employmentStatus) {
		this.employmentStatus = employmentStatus;
	}

	public String getTaxReferenceNo() {
		return taxReferenceNo;
	}

	public void setTaxReferenceNo(String taxReferenceNo) {
		this.taxReferenceNo = taxReferenceNo;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public String getCountryName() {
		return countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	public String getCp38Amount() {
		return cp38Amount;
	}

	public void setCp38Amount(String cp38Amount) {
		this.cp38Amount = cp38Amount;
	}

	public Double getCp38AmountDouble() {
		return cp38AmountDouble;
	}

	public void setCp38AmountDouble(Double cp38AmountDouble) {
		this.cp38AmountDouble = cp38AmountDouble;
	}

	public String getWifeCode() {
		return wifeCode;
	}

	public void setWifeCode(String wifeCode) {
		this.wifeCode = wifeCode;
	}

	public String getPcbAmount() {
		return pcbAmount;
	}

	public void setPcbAmount(String pcbAmount) {
		this.pcbAmount = pcbAmount;
	}

	public Double getPcbAmountDouble() {
		return pcbAmountDouble;
	}

	public void setPcbAmountDouble(Double pcbAmountDouble) {
		this.pcbAmountDouble = pcbAmountDouble;
	}

	public String getZakatAmount() {
		return zakatAmount;
	}

	public void setZakatAmount(String zakatAmount) {
		this.zakatAmount = zakatAmount;
	}

	public Double getZakatAmountDouble() {
		return zakatAmountDouble;
	}

	public void setZakatAmountDouble(Double zakatAmountDouble) {
		this.zakatAmountDouble = zakatAmountDouble;
	}

	public String getZakatPaymentClassification() {
		return zakatPaymentClassification;
	}

	public void setZakatPaymentClassification(String zakatPaymentClassification) {
		this.zakatPaymentClassification = zakatPaymentClassification;
	}

	public static Map<String, String> GetBnmMap() {
		return BnmMap;
	}

	public static void SetBnmMap(Map<String, String> bnmMap) {
		BankPaymentCimb.BnmMap = bnmMap;
	}

	public String getBnmCode() {
		return bnmCode;
	}

	public void setBnmCode(String bnmCode) {
		this.bnmCode = bnmCode;
	}

	public String getEpfStateCode() {
		return epfStateCode;
	}

	public void setEpfStateCode(String epfStateCode) {
		this.epfStateCode = epfStateCode;
	}

	public String getEpfWorkerName1() {
		return epfWorkerName1;
	}

	public void setEpfWorkerName1(String epfWorkerName1) {
		this.epfWorkerName1 = epfWorkerName1;
	}

	public String getEpfWorkerName2() {
		return epfWorkerName2;
	}

	public void setEpfWorkerName2(String epfWorkerName2) {
		this.epfWorkerName2 = epfWorkerName2;
	}

	public static Map<String, String> GetEpfStateMap() {
		return EpfStateMap;
	}

	public static void SetEpfStateMap(Map<String, String> epfStateMap) {
		BankPaymentCimb.EpfStateMap = epfStateMap;
	}



	// clasz fields

	public void setPayrollCompanyName(String aValue) throws Exception {
		this.setValueStr(PayrollCompanyName, aValue);
	}

	public String getPayrollCompanyName() throws Exception {
		return(this.getValueStr(PayrollCompanyName));
	}

	public void setPayrollCompanyAlias(String aValue) throws Exception {
		this.setValueStr(PayrollCompanyAlias, aValue);
	}

	public String getPayrollCompanyAlias() throws Exception {
		return (this.getValueStr(PayrollCompanyAlias));
	}

	public void setOrganisationCode(String aValue) throws Exception {
		this.setValueStr(OrganisationCode, aValue);
	}

	public String getOrganisationCode() throws Exception {
		return (this.getValueStr(OrganisationCode));
	}

	public void setContactPerson(String aValue) throws Exception {
		this.setValueStr(ContactPerson, aValue);
	}

	public String getContactPerson() throws Exception {
		return (this.getValueStr(ContactPerson));
	}

	public void setContactEmail(String aValue) throws Exception {
		this.setValueStr(ContactEmail, aValue);
	}

	public String getContactEmail() throws Exception {
		return (this.getValueStr(ContactEmail));
	}

	public void setContactPhone(String aValue) throws Exception {
		this.setValueStr(ContactPhone, aValue);
	}

	public String getContactPhone() throws Exception {
		return (this.getValueStr(ContactPhone));
	}

	/*
	public Telephone getContactPhone(Connection aConn) throws Exception {
		return ((Telephone) this.gotValueObject(aConn, ContactPhone));
	}

	public void setContactPhone(Telephone aValue) throws Exception {
		this.setValueObject(ContactPhone, aValue);
	}
	*/

	// Salary / Net pay

	public static String CreateSalaryBatchHeader(String aOrgCode, String aOrgName, DateTime aCreditDate) throws Exception {
		String recordType = "01";
		String creditDate = DateAndTime.AsString(aCreditDate, MsiaGeneric.FORMAT_DATE_DDMMYYYY);
		String securityCode = String.format("%1$" + "16" + "s", "0").replace(' ', '0');
		String filler = "  ";
		String theRec = recordType + aOrgCode + aOrgName + creditDate + securityCode + filler;
		return(theRec + System.lineSeparator());
	}
	public static String CreateSalaryBatchDetail(BankPaymentCimb aBankPaymentCimb) {
		String recordType = "02";
		String bnmCode = Generic.TrimPadLen(aBankPaymentCimb.getBnmCode(), 7).replace(" ", "0");
		String accountNo = aBankPaymentCimb.getAccountNo();
		String beneficiaryName = aBankPaymentCimb.getBeneficiaryName();
		String paymentAmount = MsiaGeneric.FormatMoneyNoDot(aBankPaymentCimb.getPaymentAmountDouble(), 11);
		String refNo = aBankPaymentCimb.getReferenceNo();
		String benefId = aBankPaymentCimb.getBeneficiaryId();
		String trxType = "2";
		String paymentDesc = aBankPaymentCimb.getPaymentDescription();
		String result = recordType + bnmCode + accountNo + beneficiaryName + paymentAmount + refNo + benefId + trxType + paymentDesc;
		return(result + System.lineSeparator());
	}

	public static String CreateSalaryBatchTrailer(int aTotalRec, Double aTotalAmount) {
		String recordType = "03";
		String strTotalRec = Integer.toString(aTotalRec);

		String totalRec = String.format("%1$" + "6" + "s", strTotalRec).replace(' ', '0');
		String totalAmount = MsiaGeneric.FormatMoneyNoDot(aTotalAmount, 13);

		String theRec = recordType + totalRec + totalAmount; 
		return(theRec + System.lineSeparator());
	}

	// EPF

	public static String CreateEpfBatchHeader(BankPaymentCimb aBankPaymentCimb) throws Exception {
		String recordId = "01";
		String trxDesc = Generic.TrimPadVerifyLen("EPF Trx Desc", "EPF MONTHLY FORM A", 20);
		String bossEpfNo = Generic.TrimPadVerifyLen("Employer EPF No", aBankPaymentCimb.getEpfEmployerNo(), 19, true).replace(" ", "0");
		String contributionMonth = DateAndTime.AsString(aBankPaymentCimb.getContributionMonth(), MsiaGeneric.FORMAT_DATE_MMYYYY);
		String submissionSource = "ITB";
		String bankCode = Generic.TrimPadVerifyLen("Bank Code", "03", 9, true).replace(" ", "0");
		String stateCode = Generic.TrimPadVerifyLen("EPF State Code", aBankPaymentCimb.getEpfStateCode(), 3, true).replace(" ", "0");
		String contactName = aBankPaymentCimb.getEpfContactPerson();
		String contactPhone = aBankPaymentCimb.getEpfContactNo();
		String paymentIndicator = "N";
		String sequenceNum = "00"; // what is this, running sequence? need to track? impossible
		String trxDate = DateAndTime.AsString(DateTime.now(), MsiaGeneric.FORMAT_DATE_YYYYMMDD);
		String trxTime = Generic.TrimPadVerifyLen("Trx Date", DateAndTime.AsString(DateTime.now(), MsiaGeneric.FORMAT_TIME_BATCH), 8).replace(" ", "0");
		String fileRefNum = trxDate + trxTime;
		String testingMode = "N";

		String result = recordId + trxDesc + bossEpfNo + contributionMonth 
		+ submissionSource + bankCode + stateCode + contactName + contactPhone 
		+ paymentIndicator + sequenceNum + trxDate + trxTime + fileRefNum + testingMode;
		return(result + System.lineSeparator());
	}

	public static String CreateEpfBatchDetail(BankPaymentCimb aBankPaymentCimb) throws Exception {
		String recordId = "02";
		String workerEpfNum = Generic.TrimPadVerifyLen("Employee EPF No", aBankPaymentCimb.getEmployeeEpfNo(), 19, true).replace(" ", "0");
		String workerIcNum = aBankPaymentCimb.getNewIcNo(); // what if worker no IC no?
		String workerName1 = aBankPaymentCimb.getEpfWorkerName1();
		String workerName2 = aBankPaymentCimb.getEpfWorkerName2();
		String workerId = aBankPaymentCimb.getStaffNo();
		String bossContribution = MsiaGeneric.FormatMoneyNoDot(aBankPaymentCimb.getEmployerContributionDouble(), 15);
		String workerContribution = MsiaGeneric.FormatMoneyNoDot(aBankPaymentCimb.getEmployeeContributionDouble(), 15);
		String workerWages = MsiaGeneric.FormatMoneyNoDot(aBankPaymentCimb.getEmployeeWagesDouble(), 15);

		String result = recordId + workerEpfNum + workerIcNum + workerName1 + workerName2 + workerId + bossContribution + workerContribution + workerWages;
		return(result + System.lineSeparator());
	}

	public static String CreateEpfBatchTrailer(int aTotalRec, Double aTotalBossContribution, Double aTotalWorkerContribution, Long totalHash) throws Exception {
		String strTotalRec = Integer.toString(aTotalRec);

		String recordId = "99";
		String totalWorker = String.format("%1$" + "7" + "s", strTotalRec).replace(' ', '0');
		String totalBossContribution = MsiaGeneric.FormatMoneyNoDot(aTotalBossContribution, 15);
		String totalWorkerContribution = MsiaGeneric.FormatMoneyNoDot(aTotalWorkerContribution, 15);
		String hashTotal = Generic.TrimPadVerifyLen("EPF Hash Total", totalHash.toString(), 21, true).replace(" ", "0");

		String result = recordId + totalWorker + totalBossContribution + totalWorkerContribution + hashTotal;
		return(result + System.lineSeparator());
	}

	// Socso

	public static String CreateSocsoBatchDetail(BankPaymentCimb aBankPaymentCimb) throws Exception {
		String employerCode = Generic.TrimPadVerifyLen("SOCSO Employer No", aBankPaymentCimb.getSocsoEmployerCode().trim(), 9);
		String employeeIcNo = Generic.TrimPadVerifyLen("New IC No", aBankPaymentCimb.getNewIcNo().trim().replace("/", "").replace("-", ""), 12);
		String employeeSocsoNo = Generic.TrimPadLen("", 9);  // this is 9 char, ic is 12, by right socso now use ic, so will not use this since it can be 12 char new ic
		String contributionMonth = DateAndTime.AsString(aBankPaymentCimb.getContributionMonth(), MsiaGeneric.FORMAT_DATE_MMYY);
		String employeeName = Generic.TrimPadVerifyLen("Employee Name", aBankPaymentCimb.getBeneficiaryName().trim().toUpperCase(), 45);
		String totalSocsoAmount = MsiaGeneric.FormatMoneyNoDot(aBankPaymentCimb.getSocsoAmountDouble(), 4);
		
		String result = employerCode + employeeIcNo + employeeSocsoNo + contributionMonth + employeeName + totalSocsoAmount;
		return(result + System.lineSeparator());
	}

	// Pcb

	public static String CreatePcbBatchHeader(BankPaymentCimb aBankPaymentCimb, Double aPcbTotalAmount, int aTotalRec) throws Exception {
		String strTotalRec = Integer.toString(aTotalRec);
		String recordType = "H";
		String filler = Generic.TrimPadLen(" ", 10);
		String employerNo = Generic.TrimPadVerifyLen("LHDN Employer No", aBankPaymentCimb.getLhdnEmployerNo().trim(), 10).replace(" ", "0");
		String yearOfDeduction = DateAndTime.AsString(aBankPaymentCimb.getContributionMonth(), MsiaGeneric.FORMAT_DATE_YYYY);
		String monthOfDeduction = DateAndTime.AsString(aBankPaymentCimb.getContributionMonth(), MsiaGeneric.FORMAT_DATE_MM);
		String pcbTotalAmount = MsiaGeneric.FormatMoneyNoDot(aPcbTotalAmount, 10);
		String pcbTotalRec = String.format("%1$" + "5" + "s", strTotalRec).replace(' ', '0');
		String cp38TotalAmount = MsiaGeneric.FormatMoneyNoDot(aBankPaymentCimb.getCp38AmountDouble(), 10);
		String cp38TotalRec = pcbTotalRec;
		String emailAddress = aBankPaymentCimb.getLhdnEmailAddr();
		String phoneNum = Generic.TrimPadVerifyLen("Contact Phone", aBankPaymentCimb.getEpfContactNo().trim(), 15);
		String contactPerson = Generic.TrimPadVerifyLen("Contact Person", aBankPaymentCimb.getEpfContactPerson().trim(), 25);

		String result = recordType + filler + employerNo + yearOfDeduction + monthOfDeduction + pcbTotalAmount + pcbTotalRec + cp38TotalAmount + cp38TotalRec + emailAddress + phoneNum + contactPerson;
		return(result + System.lineSeparator());
	}


	public static String CreatePcbBatchDetail(BankPaymentCimb aBankPaymentCimb) throws Exception {
		String recordType = "D";
		String taxRefNo = Generic.TrimPadVerifyLen("LHDN Tax Ref", aBankPaymentCimb.getTaxReferenceNo().trim(), 10, true).replace(" ","0");
		String wifeCode = aBankPaymentCimb.getWifeCode();
		String employeeName = Generic.TrimPadVerifyLen("Employee Name", aBankPaymentCimb.getBeneficiaryName().trim(), 60);
		String oldIcNo = Generic.TrimPadVerifyLen("Old IC No", aBankPaymentCimb.getOldIcNo().trim(), 9);
		String filler = Generic.TrimPadVerifyLen(" ", 3);
		String newIcNo = Generic.TrimPadVerifyLen("New IC No", MsiaGeneric.NoDashSlash(aBankPaymentCimb.getNewIcNo().trim()), 12);
		String passportNo = aBankPaymentCimb.getPassportNo();
		String countryCode = aBankPaymentCimb.getCountryCode();
		String pcbAmount = MsiaGeneric.FormatMoneyNoDot(aBankPaymentCimb.getPcbAmountDouble(), 8);
		String cp38Amount = MsiaGeneric.FormatMoneyNoDot(aBankPaymentCimb.getCp38AmountDouble(), 8);
		String staffNo = Generic.TrimPadVerifyLen(" ", 10);

		String result = recordType + taxRefNo + wifeCode + employeeName + oldIcNo + filler + newIcNo + passportNo + countryCode + pcbAmount + cp38Amount + staffNo;
		return(result + System.lineSeparator());
	}

	// ZAKAT

	public static String CreateZakatBatchHeader(BankPaymentCimb aBankPaymentCimb, Double aZakatTotalAmount, int aTotalRec) throws Exception {
		String recordType = "H";
		String employerName = Generic.TrimPadVerifyLen("Company Name", aBankPaymentCimb.getCompanyName().trim(), 50);
		String monthOfContribution = DateAndTime.AsString(aBankPaymentCimb.getContributionMonth(), MsiaGeneric.FORMAT_DATE_MM);
		String yearOfContribution = DateAndTime.AsString(aBankPaymentCimb.getContributionMonth(), MsiaGeneric.FORMAT_DATE_YYYY);
		String zakatTotalAmount = MsiaGeneric.FormatMoneyNoDot(aZakatTotalAmount, 15);
		String zakatEmployerRef = Generic.TrimPadVerifyLen("Employer Zakat Ref", aBankPaymentCimb.getZakatEmployerRef(), 10);

		String result = recordType + employerName + monthOfContribution + yearOfContribution + zakatTotalAmount + zakatEmployerRef;
		return(result + System.lineSeparator());
	}

	public static String CreateZakatBatchDetail(BankPaymentCimb aBankPaymentCimb) throws Exception {
		String recordType = "D";
		String oldIcNo = Generic.TrimPadVerifyLen("Old IC No", aBankPaymentCimb.getOldIcNo().trim(), 14);
		String newIcNo = Generic.TrimPadVerifyLen("New IC No", MsiaGeneric.NoDashSlash(aBankPaymentCimb.getNewIcNo().trim()), 14);
		String employeeName = Generic.TrimPadVerifyLen("Employee Name", aBankPaymentCimb.getBeneficiaryName().trim(), 40);
		String amount = MsiaGeneric.FormatMoneyNoDot(aBankPaymentCimb.getZakatAmountDouble(), 15);
		String classification = Generic.TrimPadVerifyLen("Zakat Classification", aBankPaymentCimb.getZakatPaymentClassification(), 20);

		String result = recordType + oldIcNo + newIcNo + employeeName + amount + classification;
		return(result + System.lineSeparator());
	}

	public static void main(String[] args) throws Exception {
		ObjectBase objectDb = new ObjectBase();
		Connection conn = null;
		try {
			String[] args1 = { BaseDb.PROPERTY_LOCATION_APP };
			objectDb.setupApp(args1);
			objectDb.setupDb();
			conn = objectDb.getConnPool().getConnection();

			Country.InitList(conn);
			ContactBankAccountType.InitList(conn);
			AddrTypeOrganization.InitList(conn);

			CompanyMalaysia company = (CompanyMalaysia) ObjectBase.CreateObject(conn, CompanyMalaysia.class);
			company.setName("Demo Sdn Bhd");
			if (company.populate(conn)) {
				PayslipRunList payslipRunList = (PayslipRunList) ObjectBase.CreateObject(conn, PayslipRunList.class);
				payslipRunList.setCompany(company);
				if (payslipRunList.populateByCompany(conn)) {  // populate payslipRunList from the given/set company
					String batchId = payslipRunList.getLatestBatchId(conn);
					PayslipRun payslipRun = payslipRunList.getPayslipRun(conn, batchId);
					if (payslipRun != null) {
						List<PayslipRun> payslipList = new CopyOnWriteArrayList<>();
						payslipList.add(payslipRun);

						String htmlStr = CreateBankHtml(conn, company, payslipList, "J323L", "test@email.com", "Paul Doe", "60-29399829948");
						String outputFile = "c:/temp/bankPaymentCimb00.html";
						FileOutputStream fos = new FileOutputStream(outputFile);
						fos.write(htmlStr.getBytes());
						App.logInfo("Output HTML file written to: " + outputFile);

						CreateBankFile(conn, company, payslipList, "J323L", "test@email.com", "Paul Doe", "60-29399829948");
						App.logInfo("Output TEXT file written to: c:/temp");
					} else {
						throw new Hinderance("Company don't have any payslip run of batch: " + batchId);
					}
				} else {
					throw new Hinderance("Company don't have any payslip run list");
				}

			} else {
				throw new Hinderance("Fail to populate company: " + company.getName());
			}
		} catch (Exception ex) {
			App.logEror(0, new Hinderance(ex, "Application encounter fatal error, application is aborting...."));
		} finally {
			if (conn != null) {
				objectDb.getConnPool().freeConnection(conn);
			}
		}
	}
}
