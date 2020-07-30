package biz.shujutech.payroll;

import biz.shujutech.pdf.PdfEngine;
import biz.shujutech.base.App;
import biz.shujutech.db.relational.BaseDb;
import biz.shujutech.bznes.CompanyMalaysia;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import biz.shujutech.base.Connection;
import biz.shujutech.base.DateAndTime;
import biz.shujutech.base.Hinderance;
import biz.shujutech.db.object.ObjectBase;
import biz.shujutech.bznes.AddrTypeOrganization;
import biz.shujutech.bznes.Country;
import biz.shujutech.bznes.EmploymentMalaysia;
import biz.shujutech.bznes.MalaysiaIdentityCard;
import biz.shujutech.bznes.Person;
import biz.shujutech.bznes.TelephoneTypeOrganization;
import biz.shujutech.util.Generic;
import java.io.File;
import java.io.InputStream;
import org.jsoup.select.Elements;

public class MsiaPayslipHtml {
	public static final String PDF_FILE = "C:\\Shujutech\\StApp\\O2rm\\src\\biz\\shujutech\\payroll\\MsiaPayslipHtml.pdf"; // use for testing creating the pdf at the same location of this class
	public static final String XHTML_FILE = "resources/MsiaPayslipHtml.html";

	public String companyName = "";
	public String workerName = "";
	public String workerIc = "";
	public String workerGender = "";
	public String workerDesignation = "";
	public String payslipPeriodFrom = "";
	public String payslipPeriodTo = "";
	public String workerNum = "";
	public String deptName = "";

	public String bossEpfNum = "";
	public String bossSocsoNum = "";
	public String bossEisNum = "";
	public String workerEpfNum = "";
	public String workerSocsoNum = "";
	public String workerTaxNum = "";
	public String workerBankAcctNum = "";

	public String totalEarning = "";
	public String totalDeduction = "";
	public String totalNetPay = "";
	
	public static void AddTrxRow(Connection aConn, Elements aMaster, Elements rowTemplate, int rowPosition, Elements rows, String aTrxType, String aTrxDescr, String aTrxAmount) throws Exception {
		boolean newRow = false;
		Element row;
		if (rows.size() > rowPosition) {
			row = rows.get(rowPosition);
		} else {
			row = rowTemplate.clone().get(0);
			newRow = true;
		}

		Elements cols = row.select(".table-cell");
		cols.get(0).text(aTrxDescr);
		cols.get(1).text(aTrxAmount);
		if (newRow) aMaster.append(row.outerHtml());
	}

	public static String GeneratePayslip(Connection aConn, CompanyMalaysia aCompany, Person aWorker, EmploymentMalaysia aJob, SalarySlip aPayslip) throws Exception {
		//String urlPath = MsiaPayslipHtml.class.getClassLoader().getResource(XHTML_FILE).getPath();
		//Document htmlDoc = ReportHtml.CreateHtmlDoc(urlPath);
		//File inFile = new File(MsiaPayslipHtml.class.getClassLoader().getResource(XHTML_FILE).getFile());
		InputStream inStream = MsiaPayslipHtml.class.getClassLoader().getResourceAsStream(XHTML_FILE);
		Document htmlDoc = ReportHtml.CreateHtmlDoc(inStream);

		MsiaPayslipHtml payslipData = new MsiaPayslipHtml();
		
		payslipData.companyName = Generic.Null2Blank(aPayslip.getCompany());

		payslipData.workerName = Generic.Null2Blank(aPayslip.getEmployee());
		payslipData.workerNum = Generic.Null2Blank(MsiaLhdnEa.GetEmployeeNo(aConn, aWorker));
		if (aWorker.isMalaysiaCitizen(aConn)) {
			MalaysiaIdentityCard msiaIc = aWorker.getMalaysiaIc(aConn);
			payslipData.workerIc = Generic.Null2Blank(msiaIc.getNewIdentityCardNo());
		} else {
			payslipData.workerIc = "";
		}
		payslipData.payslipPeriodFrom = DateAndTime.FormatDisplayNoTime(aPayslip.getPeriodFrom());
		payslipData.payslipPeriodTo = DateAndTime.FormatDisplayNoTime(aPayslip.getPeriodTo());
		payslipData.deptName = "";

		payslipData.workerDesignation = aPayslip.getDesignation(aConn);

		ReportHtml.SetReportText(htmlDoc, "wt_companyName", payslipData.companyName);
		ReportHtml.SetReportText(htmlDoc, "wt_workerName", payslipData.workerName);
		ReportHtml.SetReportText(htmlDoc, "wt_workerDesignation", payslipData.workerDesignation);

		ReportHtml.SetReportText(htmlDoc, "wt_payslipPeriodFrom", payslipData.payslipPeriodFrom);
		ReportHtml.SetReportText(htmlDoc, "wt_payslipPeriodTo", payslipData.payslipPeriodTo);

		Elements table = htmlDoc.children().select("#wt_tblEarnings");
		Elements rowTemplate = htmlDoc.select("#wt_trxEarnings_0").clone();
		Elements rows = table.select("#wt_trxEarnings_0.wt_clsEarnings");
		aPayslip.getEarnings().fetchAll(aConn);
		aPayslip.getEarnings().resetIterator();
		int cntr = 0;
		while(aPayslip.getEarnings().hasNext(aConn)) {
			SalaryTransaction eachEarning = (SalaryTransaction) aPayslip.getEarnings().getNext();
			AddTrxRow(aConn, table, rowTemplate, cntr, rows, "Earnings", eachEarning.getDescr(), eachEarning.getAmount(aConn).getAmountWithComma());
			cntr++;
		}

		table = htmlDoc.children().select("#wt_tblDeductions");
		rows = table.select("#wt_trxDeductions_0.wt_clsDeductions");
		aPayslip.getDeductions().fetchAll(aConn);
		aPayslip.getDeductions().resetIterator();
		cntr = 0;
		while(aPayslip.getDeductions().hasNext(aConn)) {
			SalaryTransaction eachDeduction = (SalaryTransaction) aPayslip.getDeductions().getNext();
			AddTrxRow(aConn, table, rowTemplate, cntr, rows, "Deductions", eachDeduction.getDescr(), eachDeduction.getAmount(aConn).getAmountWithComma());
			cntr++;
		}

		table = htmlDoc.children().select("#wt_tblOthers");
		rows = table.select("#wt_trxDeductions_0.wt_clsDeductions");
		aPayslip.getOthers().fetchAll(aConn);
		aPayslip.getOthers().resetIterator();
		cntr = 0;
		while(aPayslip.getOthers().hasNext(aConn)) {
			SalaryTransaction eachOther = (SalaryTransaction) aPayslip.getOthers().getNext();
			AddTrxRow(aConn, table, rowTemplate, cntr, rows, "Others", eachOther.getDescr(), eachOther.getAmount(aConn).getAmountWithComma());
			cntr++;
		}

		aPayslip.computeAllTotal(aConn, aCompany);
		payslipData.totalEarning = aPayslip.getTotalEarning().getAmountWithComma();
		payslipData.totalDeduction = aPayslip.getTotalDeduction().getAmountWithComma();
		payslipData.totalNetPay = aPayslip.getNetPay(aConn).getAmountWithComma();

		ReportHtml.SetReportText(htmlDoc, "wt_grossPay", payslipData.totalEarning);
		ReportHtml.SetReportText(htmlDoc, "wt_totalDeduction", payslipData.totalDeduction);
		ReportHtml.SetReportText(htmlDoc, "wt_totalNetPay", payslipData.totalNetPay);
		
		return(htmlDoc.toString());
	}

	//public static final int TEST_CASE = 0;
	public static final int TEST_CASE = 1;
	public static void main(String[] args) throws Exception {
		if (TEST_CASE == 0) {
			File inFile = new File(MsiaPayslipHtml.class.getClassLoader().getResource(XHTML_FILE).getFile());
			Document htmlDoc = ReportHtml.CreateHtmlDoc(inFile);
			//System.out.println(htmlDoc.toString());
			PdfEngine.GeneratePdf(PDF_FILE, htmlDoc.toString());
		} else {
			ObjectBase objectDb = new ObjectBase();
			Connection conn = null;
			try {
				//String urlPath = MsiaPayslipHtml.class.getClassLoader().getResource(XHTML_FILE).getPath();
				String[] args1 = { BaseDb.PROPERTY_LOCATION_APP };
				objectDb.setupApp(args1);
				objectDb.setupDb();
				conn = objectDb.getConnPool().getConnection();

				Country.InitList(conn);
				AddrTypeOrganization.InitList(conn);
				TelephoneTypeOrganization.InitList(conn);

				CompanyMalaysia company = (CompanyMalaysia) ObjectBase.CreateObject(conn, CompanyMalaysia.class);
				company.setName("Demo Sdn Bhd");
				if (company.populate(conn)) {
					company.getEmployee().fetchAll(conn);
					company.getEmployee().resetIterator();
					while(company.getEmployee().hasNext(conn)) {
						Person worker = (Person) company.getEmployee().getNext();
						worker.getEmployment().fetchAll(conn);
						worker.getEmployment().resetIterator();
						while(worker.getEmployment().hasNext(conn)) {
							EmploymentMalaysia job = (EmploymentMalaysia) worker.getEmployment().getNext();
							job.getSalarySlip().fetchAll(conn);
							job.getSalarySlip().resetIterator();
							while(job.getSalarySlip().hasNext(conn)) {
								SalarySlip payslip = (SalarySlip) job.getSalarySlip().getNext();
								String xhtmlPayslip = GeneratePayslip(conn, company, worker, job, payslip);
								PdfEngine.GeneratePdf(PDF_FILE, xhtmlPayslip);
								App.logInfo("Generated pdf file at: " + PDF_FILE);
								break;
							}
							break;
						}
						break;
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
}
