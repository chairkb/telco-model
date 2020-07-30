package biz.shujutech.payroll;

import biz.shujutech.technical.LambdaDouble;
import biz.shujutech.base.App;
import biz.shujutech.base.Connection;
import biz.shujutech.base.DateAndTime;
import biz.shujutech.base.Hinderance;
import biz.shujutech.bznes.AddrTypeOrganization;
import biz.shujutech.bznes.CompanyMalaysia;
import biz.shujutech.bznes.ContactBankAccountType;
import biz.shujutech.bznes.Country;
import biz.shujutech.bznes.EmploymentMalaysia;
import biz.shujutech.bznes.Person;
import biz.shujutech.db.object.Clasz;
import biz.shujutech.db.object.ObjectBase;
import biz.shujutech.db.relational.BaseDb;
import biz.shujutech.technical.LambdaCounter;
import biz.shujutech.util.Generic;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import org.joda.time.DateTime;


public class MsiaLhdnEDataPcb {

	public class Header {
		private String recordType;
		private String employerNoHq;
		private String employerNo;
		private String yearOfDeduction;
		private String monthOfDeduction;
		private String totalMtdAmount;
		private String totalMtdRecords;
		private String totalCp38Amount;
		private String totalCp38Records;

		public String getRecordType() {
			return recordType;
		}

		public void setRecordType(String recordType) {
			this.recordType = recordType;
		}

		public String getEmployerNoHq() {
			return employerNoHq;
		}

		public void setEmployerNoHq(String employerNoHq) {
			this.employerNoHq = employerNoHq;
		}

		public String getEmployerNo() {
			return employerNo;
		}

		public void setEmployerNo(String employerNo) {
			this.employerNo = employerNo;
		}

		public String getYearOfDeduction() {
			return yearOfDeduction;
		}

		public void setYearOfDeduction(String yearOfDeduction) {
			this.yearOfDeduction = yearOfDeduction;
		}

		public String getMonthOfDeduction() {
			return monthOfDeduction;
		}

		public void setMonthOfDeduction(String monthOfDeduction) {
			this.monthOfDeduction = monthOfDeduction;
		}

		public String getTotalMtdAmount() {
			return totalMtdAmount;
		}

		public void setTotalMtdAmount(String totalMtdAmount) {
			this.totalMtdAmount = totalMtdAmount;
		}

		public String getTotalMtdRecords() {
			return totalMtdRecords;
		}

		public void setTotalMtdRecords(String totalMtdRecords) {
			this.totalMtdRecords = totalMtdRecords;
		}

		public String getTotalCp38Amount() {
			return totalCp38Amount;
		}

		public void setTotalCp38Amount(String totalCp38Amount) {
			this.totalCp38Amount = totalCp38Amount;
		}

		public String getTotalCp38Records() {
			return totalCp38Records;
		}

		public void setTotalCp38Records(String totalCp38Records) {
			this.totalCp38Records = totalCp38Records;
		}


	}

	public class Detail {
		private String recordType;
		private String incomeTaxNo;
		private String wifeCode;
		private String employeeName;
		private String oldIcNo;
		private String newIcNo;
		private String passportNo;
		private String countryCode;
		private String mtdAmount;
		private String cp38Amount;
		private String employeeNo;

		public String getRecordType() {
			return recordType;
		}

		public void setRecordType(String recordType) {
			this.recordType = recordType;
		}

		public String getIncomeTaxNo() {
			return incomeTaxNo;
		}

		public void setIncomeTaxNo(String incomeTaxNo) {
			this.incomeTaxNo = incomeTaxNo;
		}

		public String getWifeCode() {
			return wifeCode;
		}

		public void setWifeCode(String wifeCode) {
			this.wifeCode = wifeCode;
		}

		public String getEmployeeName() {
			return employeeName;
		}

		public void setEmployeeName(String employeeName) {
			this.employeeName = employeeName;
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

		public String getCountryCode() {
			return countryCode;
		}

		public void setCountryCode(String countryCode) {
			this.countryCode = countryCode;
		}

		public String getMtdAmount() {
			return mtdAmount;
		}

		public void setMtdAmount(String mtdAmount) {
			this.mtdAmount = mtdAmount;
		}

		public String getCp38Amount() {
			return cp38Amount;
		}

		public void setCp38Amount(String cp38Amount) {
			this.cp38Amount = cp38Amount;
		}

		public String getEmployeeNo() {
			return employeeNo;
		}

		public void setEmployeeNo(String employeeNo) {
			this.employeeNo = employeeNo;
		}
	}

	public static String ProcessPayslipHeader(Connection aConn, DateTime aMonth, Header aHeader, CompanyMalaysia aCompany
	, Double aTotalMtdAmount
	, int aTotalMtdRecords
	, Double aTotalCp38Amount
	, int aTotalCp38Records
	) throws Exception {

		String employerNoHq = MsiaGeneric.ZeroIfBlank(Generic.TrimPadVerifyLen("LHDN Employer No", aCompany.getLhdnNoMajikanE(), 10));
		String employerNo = employerNoHq;
		String yearOfDeduction = DateAndTime.AsString(aMonth, MsiaGeneric.FORMAT_DATE_YYYY);
		String monthOfDeduction = DateAndTime.AsString(aMonth, MsiaGeneric.FORMAT_DATE_MM);
		String totalMtdAmount = MsiaGeneric.FormatMoneyWithDot(aTotalMtdAmount, 10);
		String totalMtdRecords = String.format("%1$" + "5" + "s", aTotalMtdRecords).replace(' ', '0');
		String totalCp38Amount = MsiaGeneric.FormatMoneyWithDot(aTotalCp38Amount, 10);
		String totalCp38Records = String.format("%1$" + "5" + "s", aTotalCp38Records).replace(' ', '0');;

		aHeader.setRecordType("H");
		aHeader.setEmployerNoHq(employerNoHq);
		aHeader.setEmployerNo(employerNo);
		aHeader.setYearOfDeduction(yearOfDeduction);
		aHeader.setMonthOfDeduction(monthOfDeduction);
		aHeader.setTotalMtdAmount(totalMtdAmount);
		aHeader.setTotalMtdRecords(totalMtdRecords);
		aHeader.setTotalCp38Amount(totalCp38Amount);
		aHeader.setTotalCp38Records(totalCp38Records);

		String result = aHeader.getRecordType()
		+ aHeader.getEmployerNoHq()
		+ aHeader.getEmployerNo()
		+ aHeader.getYearOfDeduction()
		+ aHeader.getMonthOfDeduction()
		+ aHeader.getTotalMtdAmount()
		+ aHeader.getTotalMtdRecords()
		+ aHeader.getTotalCp38Amount()
		+ aHeader.getTotalCp38Records();
		return(result);
	}

	public static String ProcessPayslipDetail(Connection aConn, SalarySlipMalaysia payslip, Detail aDetail, CompanyMalaysia aCompany) throws Exception {
		EmploymentMalaysia msiaJob = (EmploymentMalaysia) payslip.getJob(aConn);
		Person worker = aCompany.getEmployee(aConn, payslip.getEmployee(), payslip.getEmployeeAlias());

		String msiaIcNew = "";
		String msiaIcOld = "";
		if (worker.getMalaysiaIc(aConn) != null) {
			msiaIcNew = MsiaGeneric.NoDashSlash(worker.getMalaysiaIc(aConn).getNewIdentityCardNo());
			msiaIcOld = MsiaGeneric.NoDashSlash(worker.getMalaysiaIc(aConn).getOldIdentityCardNo());
		}
		msiaIcNew = Generic.TrimPadVerifyLen("New IC No", MsiaGeneric.NoDashSlash(msiaIcNew), 12);
		msiaIcOld = Generic.TrimPadVerifyLen("Old IC No", msiaIcOld, 12);

		String taxNo = Generic.TrimPadVerifyLen("Tax No", msiaJob.getTaxNo(), 10);
		String wifeCode = Generic.TrimPadVerifyLen("Wife Code", MsiaGeneric.GetWifeCode(aConn, msiaJob), 1);
		String employeeName = Generic.TrimPadVerifyLen("Employee Name", worker.getName(), 60);
		String passportNo = Generic.TrimPadVerifyLen("Passport No", MsiaGeneric.NoDashSlash(worker.getPassportNo(aConn)), 12);
		String countryCode = MsiaGeneric.GetCountryCode(aConn, worker);
		String mtdAmount = MsiaGeneric.FormatMoneyWithDot(payslip.getTax(aConn).getValueDouble(), 8);
		String cp38Amount = MsiaGeneric.FormatMoneyWithDot(0D, 8);
		String staffNo = Generic.TrimPadVerifyLen(" ", 10);

		aDetail.setRecordType("D");
		aDetail.setIncomeTaxNo(taxNo);
		aDetail.setWifeCode(wifeCode);
		aDetail.setEmployeeName(employeeName);
		aDetail.setOldIcNo(msiaIcOld);
		aDetail.setNewIcNo(msiaIcNew);
		aDetail.setPassportNo(passportNo);
		aDetail.setCountryCode(countryCode);
		aDetail.setMtdAmount(mtdAmount);
		aDetail.setCp38Amount(cp38Amount);
		aDetail.setEmployeeNo(staffNo);

		String result = aDetail.getRecordType()
		+ aDetail.getIncomeTaxNo()
		+ aDetail.getWifeCode()
		+ aDetail.getEmployeeName()
		+ aDetail.getOldIcNo()
		+ aDetail.getNewIcNo()
		+ aDetail.getPassportNo()
		+ aDetail.getCountryCode()
		+ aDetail.getMtdAmount()
		+ aDetail.getCp38Amount()
		+ aDetail.getEmployeeNo();
		return(result);
	}

	public static ByteArrayOutputStream CreateEDataPcbStream(Connection aConn, CompanyMalaysia aCompany, String aHeader, List<String> aDetailList) throws Exception {
		String resultRec = aHeader + System.lineSeparator();
		for(String eachStr : aDetailList) {
			resultRec += eachStr + System.lineSeparator();
		}

		String csvFileName = MsiaGeneric.CreateBaseFileName(aCompany.getName(), "edatapcb") + ".txt"; 
		ByteArrayInputStream streamCsv = new ByteArrayInputStream(resultRec.getBytes());
		Map<String, ByteArrayInputStream> zippingMap = new ConcurrentHashMap<>();
		zippingMap.put(csvFileName, streamCsv); 
		ByteArrayOutputStream zippedStream = MsiaGeneric.ZipFile(zippingMap);
		
		if (MsiaGeneric.CreateLocalFile()) {
			String zipFileName = MsiaGeneric.CreateBaseFileName(aCompany.getName(), "edatapcb") + ".zip"; // create file name for bulk/salary payment
			String zipFileNamePath = "c:/temp/" + zipFileName;
			FileOutputStream fosZip = new FileOutputStream(zipFileNamePath);
			App.logInfo("Output zip file written to: " + zipFileNamePath);
			zippedStream.writeTo(fosZip);
			fosZip.flush();
			fosZip.close();
		}

		return(zippedStream);
	}

	public ByteArrayOutputStream createEDataPcb(Connection aConn, CompanyMalaysia aCompany, DateTime aMonth) throws Exception {
		List<String> detailList = new CopyOnWriteArrayList();
		LambdaCounter totalEmployee = new LambdaCounter();
		LambdaCounter totalPayslip = new LambdaCounter();
		LambdaDouble totalMtdAmount = new LambdaDouble();
		LambdaCounter totalMtdRecords = new LambdaCounter();
		LambdaDouble totalCp38Amount = new LambdaDouble();
		LambdaCounter totalCp38Records = new LambdaCounter();
		aCompany.getEmployee().forEachMember(aConn, ((Connection bConn, Clasz aClasz) -> {
			totalEmployee.increment();
			Person employee = (Person) aClasz;
			employee.fetchEmploymentOfCompany(bConn, aCompany); // FetchUnique the employment for this company
			employee.getEmployment().resetIterator();
			while (employee.getEmployment().hasNext(aConn)) {
				EmploymentMalaysia employment = (EmploymentMalaysia) employee.getEmployment().getNext();
				employment.fetchSalarySlipByMonth(aConn, aMonth);// FetchUnique the payslip for this employment
				employment.getSalarySlip().resetIterator();
				while (employment.getSalarySlip().hasNext(aConn)) {
					totalPayslip.increment();
					SalarySlipMalaysia payslip = (SalarySlipMalaysia) employment.getSalarySlip().getNext();

					MsiaLhdnEDataPcb.Detail detail = new MsiaLhdnEDataPcb.Detail();
					String recLine = ProcessPayslipDetail(aConn, payslip, detail, aCompany); // create the bank detail record from payslip

					totalMtdAmount.sum(Double.valueOf(detail.getMtdAmount()));
					totalCp38Amount.sum(Double.valueOf(detail.getCp38Amount()));
					totalMtdRecords.increment();
					totalCp38Records.increment();
					detailList.add(recLine);
				}
			}
			return(true);
		}));

		Header header = new Header();
		String strHeader = ProcessPayslipHeader(aConn, aMonth, header, aCompany, totalMtdAmount.getDouble(), totalMtdRecords.getCntr(), totalCp38Amount.getDouble(), totalCp38Records.getCntr());
		return CreateEDataPcbStream(aConn, aCompany, strHeader, detailList);
	}

	public static ByteArrayOutputStream CreateEDataPcb(Connection aConn, CompanyMalaysia aCompany, DateTime aMonth) throws Exception {
		MsiaLhdnEDataPcb eDataPcb = new MsiaLhdnEDataPcb();
		return eDataPcb.createEDataPcb(aConn, aCompany, aMonth);
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
				DateTime eDataPcb = new DateTime(2019, 01, 1, 0, 0, 0, 0);
				CreateEDataPcb(conn, company, eDataPcb);
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
