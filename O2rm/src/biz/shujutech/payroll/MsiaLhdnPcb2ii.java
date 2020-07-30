package biz.shujutech.payroll;

import biz.shujutech.base.Connection;
import biz.shujutech.base.DateAndTime;
import biz.shujutech.db.object.Clasz;
import biz.shujutech.db.relational.FieldType;
import biz.shujutech.db.relational.SortOrder;
import biz.shujutech.bznes.CompanyMalaysia;
import biz.shujutech.bznes.EmploymentMalaysia;
import biz.shujutech.bznes.Money;
import biz.shujutech.bznes.Person;
import biz.shujutech.db.object.FieldObjectBox;
import biz.shujutech.db.object.ObjectBase;
import biz.shujutech.reflect.ReflectField;
import biz.shujutech.reflect.ReflectIndex;
import biz.shujutech.util.Generic;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import org.joda.time.DateTime;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class MsiaLhdnPcb2ii extends Clasz {
	public static final String HTML_PATH = "C:/Shujutech/StApp/StEMS/src/java/resources/";
	public static final String HTML_NAME = "MsiaLhdnPcb2ii_2012.html";
	public static final String HTML_FILE = HTML_PATH + HTML_NAME;
	public static final String PDF_NAME = "MsiaLhdnPcb2ii_2012.pdf";
	public static final String PDF_FILE = HTML_PATH + PDF_NAME;
	public static final String HTML_RESOURCE = "resources/MsiaLhdnPcb2ii_2012.html";

	@ReflectField(type=FieldType.STRING, size=32, displayPosition=10, indexes={@ReflectIndex(indexName="idx_lhdnpcb", indexNo=0, indexOrder=SortOrder.ASC, isUnique=true)}) public static String CompanyName;
	@ReflectField(type=FieldType.STRING, size=32, displayPosition=20, indexes={@ReflectIndex(indexName="idx_lhdnpcb", indexNo=1, indexOrder=SortOrder.ASC, isUnique=true)}) public static String CompanyAlias;
	@ReflectField(type=FieldType.INTEGER, displayPosition=60, indexes={@ReflectIndex(indexName="idx_lhdnpcb", indexNo=2, indexOrder=SortOrder.ASC, isUnique=true)}) public static String ForYear;
	@ReflectField(type=FieldType.STRING, size=32, displayPosition=30, indexes={@ReflectIndex(indexName="idx_lhdnpcb", indexNo=3, indexOrder=SortOrder.ASC, isUnique=true)}) public static String EmployeeName;
	@ReflectField(type=FieldType.STRING, size=32, displayPosition=40, indexes={@ReflectIndex(indexName="idx_lhdnpcb", indexNo=4, indexOrder=SortOrder.ASC, isUnique=true)}) public static String EmployeeAlias;

	@ReflectField(type=FieldType.STRING, size=32, displayPosition=500) public static String PcbTarikh;
	@ReflectField(type=FieldType.STRING, size=32, displayPosition=510) public static String PcbCawangan;
	@ReflectField(type=FieldType.STRING, size=32, displayPosition=520) public static String PcbTahun;
	@ReflectField(type=FieldType.STRING, size=32, displayPosition=530) public static String PcbNamaPekerja;
	@ReflectField(type=FieldType.STRING, size=32, displayPosition=540) public static String PcbNoKadPengenalan;
	@ReflectField(type=FieldType.STRING, size=32, displayPosition=550) public static String PcbNoCukai;
	@ReflectField(type=FieldType.STRING, size=32, displayPosition=560) public static String PcbNoPekerja;
	@ReflectField(type=FieldType.STRING, size=32, displayPosition=570) public static String PcbNoMajikan;
	@ReflectField(type=FieldType.STRING, size=32, displayPosition=580) public static String PcbNamaPegawai;
	@ReflectField(type=FieldType.STRING, size=32, displayPosition=590) public static String PcbJawatan;
	@ReflectField(type=FieldType.STRING, size=32, displayPosition=600) public static String PcbNoTelefon;
	@ReflectField(type=FieldType.STRING, size=64, displayPosition=610) public static String PcbNamaMajikan;
	@ReflectField(type=FieldType.STRING, size=64, displayPosition=620) public static String PcbAlamatMajikan01;
	@ReflectField(type=FieldType.STRING, size=64, displayPosition=630) public static String PcbAlamatMajikan02;

	@ReflectField(type=FieldType.OBJECTBOX, deleteAsMember=true, clasz="biz.shujutech.payroll.MsiaLhdnPcb2iiPotonganSemasa", polymorphic=false, displayPosition=650, prefetch=false) public static String PcbPotonganSemasa;
	@ReflectField(type=FieldType.OBJECTBOX, deleteAsMember=true, clasz="biz.shujutech.payroll.MsiaLhdnPcb2iiPotonganDahulu", polymorphic=false, displayPosition=660, prefetch=false) public static String PcbPotonganDahulu;

	public String getCompanyName() throws Exception {
		return(this.getValueStr(CompanyName));
	}

	public void setCompanyName(String aCompany) throws Exception {
		this.setValueStr(CompanyName, aCompany);
	}

	public String getCompanyAlias() throws Exception {
		return(this.getValueStr(CompanyAlias));
	}

	public void setCompanyAlias(String aCompanyAlias) throws Exception {
		this.setValueStr(CompanyAlias, aCompanyAlias);
	}

	public String getEmployeeName() throws Exception {
		return(this.getValueStr(EmployeeName));
	}

	public void setEmployeeName(String aEmployee) throws Exception {
		this.setValueStr(EmployeeName, aEmployee);
	}

	public String getEmployeeAlias() throws Exception {
		return(this.getValueStr(EmployeeAlias));
	}

	public void setEmployeeAlias(String aEmployeeAlias) throws Exception {
		this.setValueStr(EmployeeAlias, aEmployeeAlias);
	}

	public Integer getForYear() throws Exception {
		return(this.getValueInt(ForYear));
	}

	public void setForYear(Integer aValue) throws Exception {
		this.setValueInt(ForYear, aValue);
	}





	public String getPcbTarikh() throws Exception {
		return(this.getValueStr(PcbTarikh));
	}

	public void setPcbTarikh(String PcbTarikh) throws Exception {
		this.setValueStr(MsiaLhdnPcb2ii.PcbTarikh, PcbTarikh);
	}

	public String getPcbCawangan() throws Exception {
		return(this.getValueStr(PcbCawangan));
	}

	public void setPcbCawangan(String PcbCawangan) throws Exception {
		this.setValueStr(MsiaLhdnPcb2ii.PcbCawangan, PcbCawangan);
	}

	public String getPcbTahun() throws Exception {
		return(this.getValueStr(PcbTahun));
	}

	public void setPcbTahun(String PcbTahun) throws Exception {
		this.setValueStr(MsiaLhdnPcb2ii.PcbTahun, PcbTahun);
	}

	public String getPcbNamaPekerja() throws Exception {
		return(this.getValueStr(PcbNamaPekerja));
	}

	public void setPcbNamaPekerja(String PcbNamaPekerja) throws Exception {
		this.setValueStr(MsiaLhdnPcb2ii.PcbNamaPekerja, PcbNamaPekerja);
	}

	public String getPcbNoKadPengenalan() throws Exception {
		return(this.getValueStr(PcbNoKadPengenalan));
	}

	public void setPcbNoKadPengenalan(String PcbNoKadPengenalan) throws Exception {
		this.setValueStr(MsiaLhdnPcb2ii.PcbNoKadPengenalan, PcbNoKadPengenalan);
	}

	public String getPcbNoCukai() throws Exception {
		return(this.getValueStr(PcbNoCukai));
	}

	public void setPcbNoCukai(String PcbNoCukai) throws Exception {
		this.setValueStr(MsiaLhdnPcb2ii.PcbNoCukai, PcbNoCukai);
	}

	public String getPcbNoPekerja() throws Exception {
		return(this.getValueStr(PcbNoPekerja));
	}

	public void setPcbNoPekerja(String PcbNoPekerja) throws Exception {
		this.setValueStr(MsiaLhdnPcb2ii.PcbNoPekerja, PcbNoPekerja);
	}

	public String getPcbNoMajikan() throws Exception {
		return(this.getValueStr(PcbNoMajikan));
	}

	public void setPcbNoMajikan(String PcbNoMajikan) throws Exception {
		this.setValueStr(MsiaLhdnPcb2ii.PcbNoMajikan, PcbNoMajikan);
	}

	public String getPcbNamaPegawai() throws Exception {
		return(this.getValueStr(PcbNamaPegawai));
	}

	public void setPcbNamaPegawai(String PcbNamaPegawai) throws Exception {
		this.setValueStr(MsiaLhdnPcb2ii.PcbNamaPegawai, PcbNamaPegawai);
	}

	public String getPcbJawatan() throws Exception {
		return(this.getValueStr(PcbJawatan));
	}

	public void setPcbJawatan(String PcbJawatan) throws Exception {
		this.setValueStr(MsiaLhdnPcb2ii.PcbJawatan, PcbJawatan);
	}

	public String getPcbNoTelefon() throws Exception {
		return(this.getValueStr(PcbNoTelefon));
	}

	public void setPcbNoTelefon(String PcbNoTelefon) throws Exception {
		this.setValueStr(MsiaLhdnPcb2ii.PcbNoTelefon, PcbNoTelefon);
	}

	public String getPcbNamaMajikan() throws Exception {
		return(this.getValueStr(PcbNamaMajikan));
	}

	public void setPcbNamaMajikan(String PcbNamaMajikan) throws Exception {
		this.setValueStr(MsiaLhdnPcb2ii.PcbNamaMajikan, PcbNamaMajikan);
	}

	public String getPcbAlamatMajikan01() throws Exception {
		return(this.getValueStr(PcbAlamatMajikan01));
	}

	public void setPcbAlamatMajikan01(String PcbAlamatMajikan01) throws Exception {
		this.setValueStr(MsiaLhdnPcb2ii.PcbAlamatMajikan01, PcbAlamatMajikan01);
	}

	public String getPcbAlamatMajikan02() throws Exception {
		return(this.getValueStr(PcbAlamatMajikan02));
	}

	public void setPcbAlamatMajikan02(String PcbAlamatMajikan02) throws Exception {
		this.setValueStr(MsiaLhdnPcb2ii.PcbAlamatMajikan02, PcbAlamatMajikan02);
	}

	public FieldObjectBox getPcbPotonganSemasa() throws Exception {
		return(this.getFieldObjectBox(MsiaLhdnPcb2ii.PcbPotonganSemasa));
	}

	public void addPcbPotonganSemasa(MsiaLhdnPcb2iiPotonganSemasa aPotongan) throws Exception {
		this.getPcbPotonganSemasa().addValueObject(aPotongan);
	}

	public FieldObjectBox getPcbPotonganDahulu() throws Exception {
		return(this.getFieldObjectBox(MsiaLhdnPcb2ii.PcbPotonganDahulu));
	}

	public void addPcbPotonganDahulu(MsiaLhdnPcb2iiPotonganSemasa aPotongan) throws Exception {
		this.getPcbPotonganDahulu().addValueObject(aPotongan);
	}

	public static List<String> GetPcbRec(List<List<String>> aRecList, String aMth) {
		for(int cntr = 0; cntr < aRecList.size(); cntr++) {
			List<String> theRec = aRecList.get(cntr);
			String theMth = theRec.get(0);
			if (theMth.equals(aMth)) {
				return(theRec);
			}
		}
		return(null);
	}

	public static String GetMalayMonth(int cntr) {
		String strMth = "";
		switch (cntr) {
			case 0:
				strMth = "Januari";
				break;
			case 1:
				strMth = "Februari";
				break;
			case 2:
				strMth = "Mac";
				break;
			case 3:
				strMth = "April";
				break;
			case 4:
				strMth = "Mei";
				break;
			case 5:
				strMth = "Jun";
				break;
			case 6:
				strMth = "Julai";
				break;
			case 7:
				strMth = "Ogos";
				break;
			case 8:
				strMth = "September";
				break;
			case 9:
				strMth = "Oktober";
				break;
			case 10:
				strMth = "November";
				break;
			case 11:
				strMth = "Disember";
				break;
			default:
				break;
		}
		return(strMth);
	}

	public static void SplitStrAtSpace(List<String> aResult, String aStr, int aLen) {
		if (aStr.length() < aLen) {
			aResult.add(aStr);
		} else {
			int findSpaceCntr = aLen;
			for(; findSpaceCntr >= 0; --findSpaceCntr) {
				char atChar = aStr.charAt(findSpaceCntr);
				if (Character.isWhitespace(atChar)) {
					break;
				}
			}

			String splittedStr = aStr.substring(0, findSpaceCntr + 1);
			aResult.add(splittedStr);
			if (findSpaceCntr + 1 < aLen) {
				String remainderStr = aStr.substring(findSpaceCntr + 1);
				SplitStrAtSpace(aResult, remainderStr, aLen);
			}
		}
	}

	public static void SumPcbCp38Amount(List<String> aRowRec, Money aAmtFromPayslip, int aColPosition) throws Exception {
		int lastIndex = aRowRec.size() - 1;
		for(int cntr = lastIndex; cntr < aColPosition; cntr++) {
			aRowRec.add("");
		}

		String oldAmt = aRowRec.get(aColPosition);
		if (oldAmt.isEmpty() == false) {
			String onlyNum = oldAmt.replaceAll(",", "");
			double dblAmt = Double.parseDouble(onlyNum);
			double totalAmt = aAmtFromPayslip.getValueDouble() + dblAmt;
			aAmtFromPayslip.setAmount(totalAmt);
		}
		aRowRec.set(aColPosition, aAmtFromPayslip.getAmountWithComma());
	}

	public static void PopulateNewPcb2ii(Connection aConn, MsiaLhdnPcb2ii aPcb2ii, CompanyMalaysia aCompany, Person aWorker, int aYear, String aNamaPegawai, String aJawatanPegawai) throws Exception {
		aPcb2ii.setCompanyName(aCompany.getName());
		aPcb2ii.setCompanyAlias(aCompany.getAlias());
		aPcb2ii.setForYear(aYear);
		aPcb2ii.setPcbTahun((new Integer(aYear)).toString());
		aPcb2ii.setEmployeeName(aWorker.getName());
		aPcb2ii.setEmployeeAlias(aWorker.getAlias());

		aPcb2ii.setPcbNamaPegawai(aNamaPegawai);
		aPcb2ii.setPcbJawatan(aJawatanPegawai);
		aPcb2ii.setPcbTarikh(DateAndTime.FormatDisplayNoTime(new DateTime()));

		String fullAddr = Generic.Null2Blank(aCompany.getPreferedAddr(aConn).getValueStr());
		List<String> addrList = new CopyOnWriteArrayList<>();
		SplitStrAtSpace(addrList, fullAddr, 64);
		String addr01 = "";
		String addr02 = "";
		if (addrList.size() > 0) addr01 = Generic.Null2Blank(addrList.get(0));
		if (addrList.size() > 1) addr02 = Generic.Null2Blank(addrList.get(1));

		aPcb2ii.setPcbNoMajikan(Generic.Null2Blank(aCompany.getLhdnNoMajikanE()));
		aPcb2ii.setPcbNamaMajikan(Generic.Null2Blank(aCompany.getName()));
		aPcb2ii.setPcbAlamatMajikan01(addr01);
		aPcb2ii.setPcbAlamatMajikan02(addr02);
		aPcb2ii.setPcbNoTelefon(Generic.Null2Blank(aCompany.getPreferedTelephone(aConn)));

		String icOrPassportNo = aWorker.getIdNoForMsia(aConn);
		if (icOrPassportNo.trim().isEmpty()) {
			icOrPassportNo = aWorker.getPassportNo(aConn);
		}

		aPcb2ii.setPcbNamaPekerja(Generic.Null2Blank(aWorker.getName()));
		aPcb2ii.setPcbNoKadPengenalan(Generic.Null2Blank(icOrPassportNo));
		aPcb2ii.setPcbNoPekerja(Generic.Null2Blank(icOrPassportNo));

		List<List<String>> potonganSemasaList = CreateMonthlyList();
		boolean jobInfoDone = false;
		aWorker.getEmployment().resetIterator();
		while (aWorker.getEmployment().hasNext(aConn)) {
			EmploymentMalaysia job = (EmploymentMalaysia) aWorker.getEmployment().getNext();
			if (job.getEndDate() == null || jobInfoDone == false) {
				jobInfoDone = true;
				aPcb2ii.setPcbCawangan(Generic.Null2Blank(job.getTaxBranch()));
				aPcb2ii.setPcbNoCukai(Generic.Null2Blank(job.getTaxNo()));
			}

			//DateTime startOfYear = DateAndTime.CreateDateTime(aYear, 1, 1).minusDays(1);
			//DateTime endOfYear = DateAndTime.CreateDateTime(aYear, 12, 31);
			DateTime startOfYear = DateAndTime.GetYearStart(aYear);
			DateTime endOfYear = DateAndTime.GetYearEnd(aYear);
			job.forEachPayslip(aConn, startOfYear, endOfYear, (Connection bConn, Clasz aPayslip) -> {
				SalarySlipMalaysia payslip = (SalarySlipMalaysia) aPayslip;
				payslip.fetchAllTrx(bConn);

				// get from potonganSemasaList
				DateTime payslipDate = payslip.getPeriodFrom();
				int intMth = payslipDate.getMonthOfYear();
				String strMth = GetMalayMonth(intMth - 1);
				List<String> pcbCp38Rec = GetPcbRec(potonganSemasaList, strMth);

				// get PCB amount and sum it
				Money pcbAmt = payslip.getTax(aConn);
				SumPcbCp38Amount(pcbCp38Rec, pcbAmt, 1);

				// get CP38 amount and sum it
				Money cp38Amt = payslip.getCp38(aConn);
				SumPcbCp38Amount(pcbCp38Rec, cp38Amt, 2);

				return true;
			});
		}

		for(int cntrMth = 0; cntrMth < 12; cntrMth++) {
			MsiaLhdnPcb2iiPotonganSemasa potonganSemasa = (MsiaLhdnPcb2iiPotonganSemasa) ObjectBase.CreateObject(aConn, MsiaLhdnPcb2iiPotonganSemasa.class);
			List<String> pcbCp38Rec = potonganSemasaList.get(cntrMth);
			if (pcbCp38Rec.size() > 2) {
				String month = pcbCp38Rec.get(0);
				String pcbAmt = pcbCp38Rec.get(1);
				String cp38Amt = pcbCp38Rec.get(2);
				potonganSemasa.setBulan(Generic.Null2Blank(month));
				potonganSemasa.setAmaunPcb(Generic.Null2Blank(pcbAmt));
				potonganSemasa.setAmaunCp38(Generic.Null2Blank(cp38Amt));
				aPcb2ii.addPcbPotonganSemasa(potonganSemasa);
			}
		}
	}
	
	public static List<List<String>> CreateMonthlyList() {
		List<List<String>> potonganSemasaRec = new CopyOnWriteArrayList<>();
		for(int cntr = 0; cntr < 12; cntr++) {
			String strMth = GetMalayMonth(cntr);
			List<String> potonganSemasaField = new CopyOnWriteArrayList<>(); 
			potonganSemasaField.add(strMth); 
			potonganSemasaRec.add(potonganSemasaField);
		}
		return(potonganSemasaRec);
	}

	/**
	 * Populate xhtml generated by pdftohtml for OpenHTMLtoPDF
	 * 
	 * @param aConn
	 * @param aPcb2ii
	 * @param aHtmlDoc
	 * @return
	 * @throws Exception 
	 */
	public static String PopulatePcb2ii(Connection aConn, MsiaLhdnPcb2ii aPcb2ii, Document aHtmlDoc) throws Exception {
		ReportHtml.SetReportText(aHtmlDoc, "wt_Pcb_Tarikh", aPcb2ii.getPcbTarikh());
		ReportHtml.SetReportText(aHtmlDoc, "wt_Pcb_Tahun", aPcb2ii.getPcbTahun());
		ReportHtml.SetReportText(aHtmlDoc, "wt_Pcb_Cawangan" , aPcb2ii.getPcbCawangan());
		ReportHtml.SetReportText(aHtmlDoc, "wt_Pcb_Tahun" , aPcb2ii.getPcbTahun());
		ReportHtml.SetReportText(aHtmlDoc, "wt_Pcb_Nama_Pekerja" , aPcb2ii.getPcbNamaPekerja());
		ReportHtml.SetReportText(aHtmlDoc, "wt_Pcb_No_Kad_Pengenalan" , aPcb2ii.getPcbNoKadPengenalan());
		ReportHtml.SetReportText(aHtmlDoc, "wt_Pcb_No_Cukai" , aPcb2ii.getPcbNoCukai());
		ReportHtml.SetReportText(aHtmlDoc, "wt_Pcb_No_Pekerja" , aPcb2ii.getPcbNoPekerja());
		ReportHtml.SetReportText(aHtmlDoc, "wt_Pcb_No_Majikan" , aPcb2ii.getPcbNoMajikan());
		ReportHtml.SetReportText(aHtmlDoc, "wt_Pcb_Nama_Pegawai" , aPcb2ii.getPcbNamaPegawai());
		ReportHtml.SetReportText(aHtmlDoc, "wt_Pcb_Jawatan" , aPcb2ii.getPcbJawatan());
		ReportHtml.SetReportText(aHtmlDoc, "wt_Pcb_No_Telefon" , aPcb2ii.getPcbNoTelefon());
		ReportHtml.SetReportText(aHtmlDoc, "wt_Pcb_Nama_Majikan" , aPcb2ii.getPcbNamaMajikan());
		ReportHtml.SetReportText(aHtmlDoc, "wt_Pcb_Alamat_Majikan01" , aPcb2ii.getPcbAlamatMajikan01());
		ReportHtml.SetReportText(aHtmlDoc, "wt_Pcb_Alamat_Majikan02" , aPcb2ii.getPcbAlamatMajikan02());

		// potongan semasa 
		List<List<String>> potonganSemasaRec = CreateMonthlyList();
		aPcb2ii.getPcbPotonganSemasa().fetchAll(aConn);
		aPcb2ii.getPcbPotonganSemasa().resetIterator();
		Double totalPcb = 0D;
		Double totalCp38 = 0D;
		while(aPcb2ii.getPcbPotonganSemasa().hasNext(aConn)) {
			MsiaLhdnPcb2iiPotonganSemasa eachPotongan = (MsiaLhdnPcb2iiPotonganSemasa) aPcb2ii.getPcbPotonganSemasa().getNext();
			String targetMonth = Generic.Null2Blank(eachPotongan.getBulan());
			for(int cntr = 0; cntr < 12; cntr++) {
				List<String> theRec = potonganSemasaRec.get(cntr);
				String theMonth = Generic.Null2Blank(theRec.get(0));
				if (theMonth.equals(targetMonth)) {
					theRec.add(Generic.Null2Blank(eachPotongan.getAmaunPcb()));
					theRec.add(Generic.Null2Blank(eachPotongan.getAmaunCp38()));
					theRec.add(Generic.Null2Blank(eachPotongan.getResitPcb()));
					theRec.add(Generic.Null2Blank(eachPotongan.getResitCp38()));
					theRec.add(Generic.Null2Blank(eachPotongan.getTarikhPcb()));
					theRec.add(Generic.Null2Blank(eachPotongan.getTarikhCp38()));

					Double amountPcb = Generic.String2Double(eachPotongan.getAmaunPcb());
					Double amountCp38 = Generic.String2Double(eachPotongan.getAmaunCp38());
					totalPcb += amountPcb;
					totalCp38 += amountCp38;
				}
			}
		}
		String totalPcbStr = Money.GetAmountWithComma(totalPcb.toString());
		String totalCp38Str = Money.GetAmountWithComma(totalCp38.toString());
		List<String> lastRec = new CopyOnWriteArrayList<>();
		lastRec.add("Jumlah");
		lastRec.add(totalPcbStr);
		lastRec.add(totalCp38Str);
		potonganSemasaRec.add(lastRec);

		Element tblPotonganSemasa = ReportHtml.GetHtmlTable(aHtmlDoc, "div-potongan-semasa");
		ReportHtml.PopulateDivTable(tblPotonganSemasa, potonganSemasaRec, ".clsRow", ".clsCol");

		// potongan dahulu
		List<List<String>> potonganDahuluRec = new CopyOnWriteArrayList<>();
		aPcb2ii.getPcbPotonganDahulu().fetchAll(aConn);
		aPcb2ii.getPcbPotonganDahulu().resetIterator();
		while(aPcb2ii.getPcbPotonganDahulu().hasNext(aConn)) {
			MsiaLhdnPcb2iiPotonganDahulu eachPotongan = (MsiaLhdnPcb2iiPotonganDahulu) aPcb2ii.getPcbPotonganDahulu().getNext();
			List<String> potonganDahuluField = new CopyOnWriteArrayList<>(); 
			potonganDahuluField.add(Generic.Null2Blank(eachPotongan.getJenis()));
			potonganDahuluField.add(Generic.Null2Blank(eachPotongan.getBulan()));
			potonganDahuluField.add(Generic.Null2Blank(eachPotongan.getTahun()));
			potonganDahuluField.add(Generic.Null2Blank(eachPotongan.getAmaunPcb()));
			potonganDahuluField.add(Generic.Null2Blank(eachPotongan.getTarikhResit()));
			potonganDahuluField.add(Generic.Null2Blank(eachPotongan.getNoResit()));
			potonganDahuluRec.add(potonganDahuluField);
		}
		Element tblPotonganDahulu = ReportHtml.GetHtmlTable(aHtmlDoc, "div-potongan-dahulu");
		ReportHtml.PopulateDivTable(tblPotonganDahulu, potonganDahuluRec, ".clsRow", ".clsCol");

		return(aHtmlDoc.toString());
	}

}

