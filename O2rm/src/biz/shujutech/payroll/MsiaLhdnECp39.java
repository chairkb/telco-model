package biz.shujutech.payroll;

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


public class MsiaLhdnECp39 {

	public class Detail {
		private String newIcNo;
		private String oldIcNo;
		private String fileType; // og or sg
		private String incomeTaxNo; 
		private String fullName; 
		private String employeeNo; 
		private String passportNo; 
		private String dateOfBirth; 
		private String countryCode; 
		private String pcbAmount; 
		private String cp38Amount; 
	
		public String getNewIcNo() {
			return newIcNo;
		}
	
		public void setNewIcNo(String newIcNo) {
			this.newIcNo = newIcNo;
		}
	
		public String getOldIcNo() {
			return oldIcNo;
		}
	
		public void setOldIcNo(String oldIcNo) {
			this.oldIcNo = oldIcNo;
		}
	
		public String getFileType() {
			return fileType;
		}
	
		public void setFileType(String fileType) {
			this.fileType = fileType;
		}
	
		public String getIncomeTaxNo() {
			return incomeTaxNo;
		}
	
		public void setIncomeTaxNo(String incomeTaxNo) {
			this.incomeTaxNo = incomeTaxNo;
		}
	
		public String getFullName() {
			return fullName;
		}
	
		public void setFullName(String fullName) {
			this.fullName = fullName;
		}
	
		public String getEmployeeNo() {
			return employeeNo;
		}
	
		public void setEmployeeNo(String employeeNo) {
			this.employeeNo = employeeNo;
		}
	
		public String getPassportNo() {
			return passportNo;
		}
	
		public void setPassportNo(String passportNo) {
			this.passportNo = passportNo;
		}
	
		public String getDateOfBirth() {
			return dateOfBirth;
		}
	
		public void setDateOfBirth(String dateOfBirth) {
			this.dateOfBirth = dateOfBirth;
		}
	
		public String getCountryCode() {
			return countryCode;
		}
	
		public void setCountryCode(String countryCode) {
			this.countryCode = countryCode;
		}
	
		public String getPcbAmount() {
			return pcbAmount;
		}
	
		public void setPcbAmount(String pcbAmount) {
			this.pcbAmount = pcbAmount;
		}
	
		public String getCp38Amount() {
			return cp38Amount;
		}
	
		public void setCp38Amount(String cp38Amount) {
			this.cp38Amount = cp38Amount;
		}
	}

	public static void ProcessPayslipDetail(Connection aConn, SalarySlipMalaysia payslip, Detail aCp39, CompanyMalaysia aCompany) throws Exception {
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

		String countryCode = MsiaGeneric.GetCountryCode(aConn, worker);
		String taxNo = msiaJob.getTaxNo();
		String taxCharPart = MsiaGeneric.GetTaxFileType(taxNo);
		int numStartAt = taxCharPart.length();
		String taxNumPart =	taxNo.substring(numStartAt);
		String dateOfBirth = DateAndTime.AsString(worker.getBirthDate(), MsiaGeneric.FORMAT_MSIA_DATE);

		aCp39.setNewIcNo(msiaIcNew);
		aCp39.setOldIcNo(msiaIcOld);
		aCp39.setFileType(taxCharPart);
		aCp39.setIncomeTaxNo(taxNumPart);
		aCp39.setFullName(worker.getName());
		aCp39.setEmployeeNo(worker.getIdNoForMsia(aConn));

		if (msiaIcNew.isEmpty()) {
			aCp39.setPassportNo(Generic.TrimPadVerifyLen("Passport No", MsiaGeneric.NoDashSlash(worker.getPassportNo(aConn)), 12));
			aCp39.setDateOfBirth(dateOfBirth);
			aCp39.setCountryCode(countryCode);
		} else {
			aCp39.setPassportNo("");
			aCp39.setDateOfBirth("");
			aCp39.setCountryCode("");
		}

		aCp39.setPcbAmount(payslip.getTax(aConn).getAmountNoComma());
		aCp39.setCp38Amount("");
	}

	public static String RemoveComa(String aStr) {
		String result = aStr.replaceAll(",", ".");
		return(result);
	}

	public static String Convert2Csv(Detail aCp39) {
		String result = RemoveComa(aCp39.getNewIcNo())
		+ "," + RemoveComa(aCp39.getOldIcNo())
		+ "," + RemoveComa(aCp39.getFileType())
		+ "," + RemoveComa(aCp39.getIncomeTaxNo())
		+ "," + RemoveComa(aCp39.getEmployeeNo())
		+ "," + RemoveComa(aCp39.getFullName())
		+ "," + RemoveComa(aCp39.getPassportNo())
		+ "," + RemoveComa(aCp39.getDateOfBirth())
		+ "," + RemoveComa(aCp39.getCountryCode())
		+ "," + RemoveComa(aCp39.getPcbAmount())
		+ "," + RemoveComa(aCp39.getCp38Amount());
		return(result);
	}
	
	public static ByteArrayOutputStream CreateECp39Stream(Connection aConn, CompanyMalaysia aCompany, List<String> cp39List) throws Exception {
		String resultCsv = "--- header ---" + System.lineSeparator();
		for(String eachStr : cp39List) {
			resultCsv += eachStr + System.lineSeparator();
		}

		String csvFileName = MsiaGeneric.CreateBaseFileName(aCompany.getName(), "ecp39") + ".txt"; // create file name for bulk/salary payment
		ByteArrayInputStream streamCsv = new ByteArrayInputStream(resultCsv.getBytes());
		Map<String, ByteArrayInputStream> zippingMap = new ConcurrentHashMap<>();
		zippingMap.put(csvFileName, streamCsv); 
		ByteArrayOutputStream zippedStream = MsiaGeneric.ZipFile(zippingMap);
		
		if (MsiaGeneric.CreateLocalFile()) {
			String zipFileName = MsiaGeneric.CreateBaseFileName(aCompany.getName(), "ecp39") + ".zip"; // create file name for bulk/salary payment
			String zipFilePathName = "c:/Temp/" + zipFileName;
			FileOutputStream fosZip = new FileOutputStream(zipFilePathName);
			App.logInfo("Output zip file written to: " + zipFileName);
			zippedStream.writeTo(fosZip);
			fosZip.flush();
			fosZip.close();
		}

		return(zippedStream);
	}

	public ByteArrayOutputStream createECp39Data(Connection aConn, CompanyMalaysia aCompany, DateTime aMonth) throws Exception {
		List<String> cp39List = new CopyOnWriteArrayList();
		LambdaCounter totalEmployee = new LambdaCounter();
		LambdaCounter totalPayslip = new LambdaCounter();
		aCompany.getEmployee().forEachMember(aConn, ((Connection bConn, Clasz aClasz) -> {
			totalEmployee.increment();
			Person employee = (Person) aClasz;
			App.logDebg(this, "Generating CP39 for company: " + aCompany.getName() + ", employee: " + employee.getName());
			employee.fetchEmploymentOfCompany(bConn, aCompany); // FetchUnique the employment for this company
			employee.getEmployment().resetIterator();
			while (employee.getEmployment().hasNext(aConn)) {
				EmploymentMalaysia employment = (EmploymentMalaysia) employee.getEmployment().getNext();
				employment.fetchSalarySlipByMonth(aConn, aMonth);// FetchUnique the payslip for this employment
				employment.getSalarySlip().resetIterator();
				while (employment.getSalarySlip().hasNext(aConn)) {
					totalPayslip.increment();
					SalarySlipMalaysia payslip = (SalarySlipMalaysia) employment.getSalarySlip().getNext();

					Detail cp39 = new Detail();
					ProcessPayslipDetail(aConn, payslip, cp39, aCompany); // create the bank detail record from payslip
					String csvCp39 = Convert2Csv(cp39);
					cp39List.add(csvCp39);
				}
			}
			return(true);
		}));

		return CreateECp39Stream(aConn, aCompany, cp39List);
	}

	public static ByteArrayOutputStream CreateECp39Data(Connection aConn, CompanyMalaysia aCompany, DateTime aMonth) throws Exception {
		MsiaLhdnECp39 msiaLhdnECp39 = new MsiaLhdnECp39();
		return msiaLhdnECp39.createECp39Data(aConn, aCompany, aMonth);
	}

	/*
	public static ByteArrayOutputStream CreateECp39Data(Connection aConn, CompanyMalaysia aCompany, List<PayslipRun> aPayslipList) throws Exception {
		List<String> cp39List = new CopyOnWriteArrayList();
		for(PayslipRun eachPayslipRun: aPayslipList) {
			eachPayslipRun.getGeneratedPayslips().forEachMember(aConn, ((Connection bConn, Clasz aPayslip) -> {
				SalarySlipMalaysia payslip = (SalarySlipMalaysia) aPayslip;
				payslip.fetchAllTrx(aConn);

				MsiaLhdnECp39 cp39 = new MsiaLhdnECp39();
				ProcessPayslipDetail(aConn, payslip, cp39, aCompany); // create the bank detail record from payslip
				String csvCp39 = Convert2Csv(cp39);
				cp39List.add(csvCp39);
				return(true);
			}));
		}

		return CreateECp39Stream(aConn, aCompany, cp39List);
	}
	*/


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
				DateTime cp39Month = new DateTime(2019, 01, 1, 0, 0, 0, 0);
				CreateECp39Data(conn, company, cp39Month);
				
				/*
				PayslipRunList payslipRunList = (PayslipRunList) ObjectBase.CreateObject(conn, PayslipRunList.class);
				payslipRunList.setCompany(company);
				if (payslipRunList.populateByCompany(conn)) {  // populate payslipRunList from the given/set company
					String batchId = payslipRunList.getLatestBatchId(conn);
					PayslipRun payslipRun = payslipRunList.getPayslipRun(conn, batchId);
					if (payslipRun != null) {
						List<PayslipRun> payslipList = new CopyOnWriteArrayList<>();
						payslipList.add(payslipRun);
						CreateECp39Data(conn, company, payslipList);
					} else {
						throw new Hinderance("Company don't have any payslip run of batch: " + batchId);
					}
				} else {
					throw new Hinderance("Company don't have any payslip run list");
				}
				*/
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
