package biz.shujutech.bznes;

import biz.shujutech.base.Connection;
import biz.shujutech.base.DateAndTime;
import biz.shujutech.db.object.Clasz;
import biz.shujutech.db.relational.FieldType;
import biz.shujutech.payroll.MsiaLhdnPcbTp3;
import biz.shujutech.payroll.MsiaPayslipHtml;
import biz.shujutech.payroll.SalarySlip;
import biz.shujutech.pdf.PdfEngine;
import biz.shujutech.reflect.ReflectField;

public class CompanyMalaysia extends Company {
	//@ReflectField(type=FieldType.STRING, size=32, displayPosition=300, indexes={@ReflectIndex(indexName="idx_cmrn_regno", indexNo=1, indexOrder=SortOrder.ASC, isUnique=true)}, updateable=false) public static String SsmRegistrationNumber;
	@ReflectField(type=FieldType.STRING, size=32, displayPosition=300) public static String SsmRegistrationNumber;
	@ReflectField(type=FieldType.STRING, size=32, displayPosition=310) public static String KwspNoMajikan;
	@ReflectField(type=FieldType.STRING, size=32, displayPosition=320) public static String LhdnNoMajikanE;
	@ReflectField(type=FieldType.STRING, size=32, displayPosition=330) public static String SocsoEmployerCode;
	@ReflectField(type=FieldType.STRING, size=32, displayPosition=340) public static String EisKodMajikan;
	@ReflectField(type=FieldType.STRING, size=32, displayPosition=340) public static String MycoId;
	@ReflectField(type=FieldType.STRING, size=32, displayPosition=340) public static String ZakatEmployerRef;

	public static biz.shujutech.bznes.Email ComposePayslipEmail(Connection aConn, CompanyMalaysia aCompany, Person aEmployee, EmploymentMalaysia aJob, SalarySlip aPayslip, String aLoginId, String aSendTo) throws Exception {
		String xhtmlPayslip = MsiaPayslipHtml.GeneratePayslip(aConn, aCompany, aEmployee, aJob, aPayslip);
		String urlPath = MsiaPayslipHtml.class.getClassLoader().getResource(MsiaPayslipHtml.XHTML_FILE).getPath();
		String attachmentPassword = Company.CreateWorkerAttachmentPassword(aConn, aCompany, aEmployee, aJob);
		String base64Payslip = PdfEngine.GeneratePdfAsBase64(xhtmlPayslip, "file:///" + urlPath, attachmentPassword);
		//String payslipPeriod = DateAndTime.FormatForMonth(aPayslip.getPeriodFrom()).replace(", ", "");
		String payslipPeriod = DateAndTime.FormatDisplayNoTime(aPayslip.getPeriodFrom()) + "To" + DateAndTime.FormatDisplayNoTime(aPayslip.getPeriodTo());
		String aEmailSubject = "Your payslip is ready - " + payslipPeriod;
		String aAttachmentName = "Payslip_" + aEmployee.getName().replaceAll(" ", "_") + "_" + payslipPeriod + ".pdf";
		String aAttachmentType = "application/pdf";
		String aEmailBody = "<div>Dear " + aEmployee.getName() + "," + "<br/><br/>Please find attached your payslip." + "<br/><br/>Please note that the attachment is in PDF and it has been password protected for security purpose." + "<br/><br/>The password to access the PDF is the first 2 character of your employer name follow by the first 2 character of your name and follow by your full salary amount." + "<br/><br/>The password is all in lower case." + "<br/><br/>E.g. If your company name is: Shujutech, your name is: John, your salary is: MYR 3,500.30 per month/week/day, your password is then shjo3500.30." + "<br/><br/>Best Regards," + "<br/> Payroll Team</div>";
		biz.shujutech.bznes.Email email;
		if (aSendTo == null) {
			email = Company.CreateEmail2Worker(aConn, aEmployee, aLoginId, aEmailSubject, aEmailBody, aAttachmentName, aAttachmentType, base64Payslip);
		} else {
			email = biz.shujutech.bznes.Email.CreateEmail(aConn, aSendTo, aLoginId, aEmailSubject, aEmailBody, aAttachmentName, aAttachmentType, base64Payslip);
		}
		return email;
	}

	@Override
	public Class getEmploymentClass() {
		Class result = EmploymentMalaysia.class;
		return(result);
	}

	@Override
	public Clasz getMe() {
		return(this);
	}

	public String getLhdnNoMajikanE() throws Exception {
		return(this.getValueStr(LhdnNoMajikanE));
	}

	public void setLhdnNoMajikanE(String aLhdnNoMajikanE) throws Exception {
		this.setValueStr(LhdnNoMajikanE, aLhdnNoMajikanE);
	}

	public String getKwspNoMajikan() throws Exception {
		return(this.getValueStr(KwspNoMajikan));
	}

	public void setKwspNoMajikan(String aKwspNoMajikan) throws Exception {
		this.setValueStr(KwspNoMajikan, aKwspNoMajikan);
	}

	public String getSocsoEmployerCode() throws Exception {
		return(this.getValueStr(SocsoEmployerCode));
	}

	public void setSocsoEmployerCode(String aSocsoEmployerCode) throws Exception {
		this.setValueStr(SocsoEmployerCode, aSocsoEmployerCode);
	}

	public String getEisKodMajikan() throws Exception {
		return(this.getValueStr(EisKodMajikan));
	}

	public void setEisKodMajikan(String aEisKodMajikan) throws Exception {
		this.setValueStr(EisKodMajikan, aEisKodMajikan);
	}

	public String getSsmRegistrationNumber() throws Exception {
		return(this.getValueStr(SsmRegistrationNumber));
	}

	public void setSsmRegistrationNumber(String aSsmRegistrationNumber) throws Exception {
		this.setValueStr(SsmRegistrationNumber, aSsmRegistrationNumber);
	}

	public String getMycoId() throws Exception {
		return(this.getValueStr(MycoId));
	}

	public void setMycoId(String aMycoId) throws Exception {
		this.setValueStr(MycoId, aMycoId);
	}

	public String getZakatEmployerRef() throws Exception {
		return(this.getValueStr(ZakatEmployerRef));
	}

	public void setZakatEmployerRef(String aZakatEmployerRef) throws Exception {
		this.setValueStr(ZakatEmployerRef, aZakatEmployerRef);
	}

	public boolean isMyTp3(MsiaLhdnPcbTp3 pcbTp3) throws Exception {
		boolean result = false;
		if (this.getName().trim().toLowerCase().equals(pcbTp3.getCompanyName().trim().toLowerCase()) 
		&& this.getAlias().trim().toLowerCase().equals(pcbTp3.getCompanyAlias().trim().toLowerCase())) {
			result = true;
		}
		return(result);
	}
}
