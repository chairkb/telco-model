package biz.shujutech.payroll;

import biz.shujutech.base.App;
import biz.shujutech.base.Connection;
import biz.shujutech.base.Hinderance;
import biz.shujutech.bznes.Company;
import biz.shujutech.bznes.Email;
import biz.shujutech.bznes.Employment;
import biz.shujutech.bznes.Person;
import biz.shujutech.db.object.Clasz;
import biz.shujutech.db.object.ObjectBase;
import biz.shujutech.db.relational.FieldType;
import biz.shujutech.db.relational.SortOrder;
import biz.shujutech.pdf.PdfEngine;
import biz.shujutech.reflect.ReflectField;
import biz.shujutech.reflect.ReflectIndex;
import biz.shujutech.util.Generic;
import org.joda.time.DateTime;
import org.jsoup.nodes.Document;


public class MsiaLhdnPcbTp3 extends Clasz {

	public static final String HTML_PATH = "C:/Shujutech/StApp/StEMS/src/java/resources/";
	public static final String HTML_NAME_2019 = "MsiaLhdnPcbTp3_2019.html";
	public static final String HTML_NAME_2020 = "MsiaLhdnPcbTp3_2020.html";
	public static final String HTML_RESOURCE_2019 = "resources/"+ HTML_NAME_2019;
	public static final String HTML_RESOURCE_2020 = "resources/"+ HTML_NAME_2020;
	//public static final String HTML_FILE = HTML_PATH + HTML_NAME_2019;
	//public static final String PDF_NAME = "MsiaLhdnPcbTp3_2019.pdf";
	//public static final String PDF_FILE = HTML_PATH + PDF_NAME;

	@ReflectField(type=FieldType.STRING, size=64, indexes={@ReflectIndex(indexName="idx_lhdnpcbtp3_yr", indexNo=0, indexOrder=SortOrder.ASC, isUnique=false)}) public static String CompanyName;
	@ReflectField(type=FieldType.STRING, size=32, indexes={@ReflectIndex(indexName="idx_lhdnpcbtp3_yr", indexNo=1, indexOrder=SortOrder.ASC, isUnique=false)}) public static String CompanyAlias;
	@ReflectField(type=FieldType.STRING, size=64, indexes={@ReflectIndex(indexName="idx_lhdnpcbtp3_yr", indexNo=2, indexOrder=SortOrder.ASC, isUnique=false)}) public static String EmployeeName;
	@ReflectField(type=FieldType.STRING, size=32, indexes={@ReflectIndex(indexName="idx_lhdnpcbtp3_yr", indexNo=3, indexOrder=SortOrder.ASC, isUnique=false)}) public static String EmployeeAlias;
	@ReflectField(type=FieldType.DATETIME) public static String CreateDate; // create date
	@ReflectField(type=FieldType.DATETIME) public static String LastModifyDate; // last modify date
	@ReflectField(type=FieldType.BASE64) public static String SignedPdf; // uploaded signed copy
	@ReflectField(type=FieldType.DATETIME) public static String LastUploadDate; // last upload date
	@ReflectField(type=FieldType.BOOLEAN) public static String Signed;
	@ReflectField(type=FieldType.INTEGER) public static String EmployeeJoinYear;
	@ReflectField(type=FieldType.OBJECT, clasz="biz.shujutech.payroll.MsiaLhdnPcbTp3EmailRecord", inline=true, prefetch=true) public static String EmailNotification;

	// Bahagian A
	@ReflectField(type=FieldType.STRING, size=64) public static String A1MajikanTerdahulu1;
	@ReflectField(type=FieldType.STRING, size=64) public static String A1MajikanTerdahulu1Cont;
	@ReflectField(type=FieldType.STRING, size=16) public static String A2NoMajikan1;
	@ReflectField(type=FieldType.STRING, size=64) public static String A3MajikanTerdahulu2;
	@ReflectField(type=FieldType.STRING, size=64) public static String A3MajikanTerdahulu2Cont;
	@ReflectField(type=FieldType.STRING, size=16) public static String A4NoMajikan2;

	// Bahagian B
	@ReflectField(type=FieldType.STRING, size=16) public static String B1EmployeeNameCont;
	@ReflectField(type=FieldType.STRING, size=16) public static String B2NoPengenalan;
	@ReflectField(type=FieldType.STRING, size=16) public static String B3NoPassport;
	@ReflectField(type=FieldType.STRING, size=16) public static String B4NoCukaiPendapatan;

	// Bahagian C
	@ReflectField(type=FieldType.STRING, size=32) public static String C1JumlahKasarBercukai;
	@ReflectField(type=FieldType.STRING, size=32) public static String C2iElaunPetrol;
	@ReflectField(type=FieldType.STRING, size=32) public static String C2iiElaunAnak;
	@ReflectField(type=FieldType.STRING, size=32) public static String C2iiiProdukPercuma;
	@ReflectField(type=FieldType.STRING, size=32) public static String C2ivAnugerah;
	@ReflectField(type=FieldType.STRING, size=32) public static String C2vLainlain;
	@ReflectField(type=FieldType.STRING, size=32) public static String C3Kwsp;
	@ReflectField(type=FieldType.STRING, size=32) public static String C4Zakat;
	@ReflectField(type=FieldType.STRING, size=32) public static String C5Pcb;

	// Bahagian D
	@ReflectField(type=FieldType.STRING, size=32) public static String D1RawatanPerubatan;
	@ReflectField(type=FieldType.STRING, size=32) public static String D1bPelepasanBapa;
	@ReflectField(type=FieldType.STRING, size=32) public static String D1bPelepasanIbu;
	@ReflectField(type=FieldType.STRING, size=32) public static String D2AlatKurangUpaya;
	@ReflectField(type=FieldType.STRING, size=32) public static String D3YuranPendidikan;
	@ReflectField(type=FieldType.STRING, size=32) public static String D4PenyakitSukarUbati;
	@ReflectField(type=FieldType.STRING, size=32) public static String D4RawatanKesuburan;
	@ReflectField(type=FieldType.STRING, size=32) public static String D5PemeriksaanKesihatan;
	@ReflectField(type=FieldType.STRING, size=32) public static String D6Sspn;
	@ReflectField(type=FieldType.STRING, size=32) public static String D7AlimoniIsteri;
	@ReflectField(type=FieldType.STRING, size=32) public static String D8aInsuranNyawaPencen;
	@ReflectField(type=FieldType.STRING, size=32) public static String D8bInsuranNyawaBukanPencen;
	@ReflectField(type=FieldType.STRING, size=32) public static String D9InsuranDidikUbat;
	@ReflectField(type=FieldType.STRING, size=32) public static String D10SkimAnuiti;
	@ReflectField(type=FieldType.STRING, size=32) public static String D11Perkeso;
	@ReflectField(type=FieldType.STRING, size=32) public static String D12GayaHidup;
	@ReflectField(type=FieldType.STRING, size=32) public static String D13AlatSusu;
	@ReflectField(type=FieldType.STRING, size=32) public static String D14PusatAsuhan;

	@ReflectField(type=FieldType.DATE) public static String TarikhAkuan;

	public static Email ComposePcbTp3Email(Connection aConn, String aSendTo, String strLoginId, String aPcbTp3Html, String aEmployeeName, String aAttachmentPassword, String aUrlPath) throws Exception {
		String aEmailSubject = "PCB TP3 Form - information of previous employment remuneration";
		String aAttachmentName = "PCBTP3_" + aEmployeeName.replaceAll(" ", "_") + ".pdf";
		String aAttachmentType = "application/pdf";
		String aEmailBody = "<div>Dear " + aEmployeeName + "," + "<br/><br/>Attached is a PCB TP3 Form. Please filled your previous employment information if any and return the filled/signed copy to: " + strLoginId + "<br/><br/>Please note that the attachment is in PDF and it has been password protected for security purpose." + "<br/><br/>The password to access the PDF is the first 2 character of your employer name follow by the first 2 character of your name and follow by your full salary amount." + "<br/><br/>The password is all in lower case." + "<br/><br/>E.g. If your company name is: Shujutech, your name is: John, your salary is: MYR 3,500.30 per month/week/day, your password is then shjo3500.30." + "<br/><br/>Best Regards," + "<br/> Payroll Team</div>";
		//String urlPath = MsiaLhdnPcbTp3.class.getClassLoader().getResource(MsiaLhdnPcbTp3.HTML_RESOURCE_2019).getPath();
		String pcbTp3Form = PdfEngine.GeneratePdfAsBase64(aPcbTp3Html, "file:///" + aUrlPath, aAttachmentPassword);
		Email email = Email.CreateEmail(aConn, aSendTo, strLoginId, aEmailSubject, aEmailBody, aAttachmentName, aAttachmentType, pcbTp3Form);
		return email;
	}

	public static Email SendPcbTp3Email(Connection aConn, Company aCompany, MsiaLhdnPcbTp3 eachPcbTp3, String strSender, String emailAddr) throws Exception {
		App.logInfo(MsiaLhdnPcbTp3.class, "Sending PCB TP3 for company: " + eachPcbTp3.getCompanyName() + ", for employee: " + eachPcbTp3.getEmployeeName());
		MsiaLhdnPcbTp3EmailRecord emailNotification = (MsiaLhdnPcbTp3EmailRecord) eachPcbTp3.gotEmailNotification(aConn);
		if (emailNotification == null) {
			emailNotification = (MsiaLhdnPcbTp3EmailRecord) ObjectBase.CreateObject(aConn, MsiaLhdnPcbTp3EmailRecord.class);
			eachPcbTp3.setEmailNotification(emailNotification);
		}
		emailNotification.setEmailAddress(emailAddr);
		emailNotification.setSentDate(new DateTime());
		String strEmployeeName = eachPcbTp3.getEmployeeName();
		String strEmployeeAlias = eachPcbTp3.getEmployeeAlias();
		Person employee = (Person) ObjectBase.CreateObject(aConn, Person.class);
		employee.setName(strEmployeeName);
		employee.setAlias(strEmployeeAlias);
		if (employee.populate(aConn)) {
			Employment job = employee.fetchLatestEmploymentInCompany(aConn, aCompany);
			String attachmentPassword = Company.CreateWorkerAttachmentPassword(aConn, aCompany, employee, job);
			String urlPath = MsiaLhdnPcbTp3.GetPcbTp3Url(eachPcbTp3);
			String htmlPcbTp3 = MsiaLhdnPcbTp3.PopulatePcbTp3Html(aConn, eachPcbTp3);
			Email email = MsiaLhdnPcbTp3.ComposePcbTp3Email(aConn, emailAddr, strSender, htmlPcbTp3, strEmployeeName, attachmentPassword, urlPath);
			try {
				aConn.setAutoCommit(false);
				email.sendEmail(aConn);
				ObjectBase.PersistNoCommit(aConn, eachPcbTp3);
				aConn.commit();
			} catch (Exception ex) {
				aConn.rollback();
				throw new Hinderance(ex, "Failed to send PCB TP3 for company: " + eachPcbTp3.getCompanyName() + ", email: " + emailAddr);
			}
			return email;
		} else {
			throw new Hinderance("Email payslip error, fail to populate employee data!");
		}
	}


	public MsiaLhdnPcbTp3EmailRecord getEmailNotification(Connection aConn) throws Exception {
		return((MsiaLhdnPcbTp3EmailRecord) this.getValueObject(aConn, EmailNotification));
	}

	public MsiaLhdnPcbTp3EmailRecord gotEmailNotification(Connection aConn) throws Exception {
		return((MsiaLhdnPcbTp3EmailRecord) this.gotValueObject(aConn, EmailNotification));
	}

	public void setEmailNotification(MsiaLhdnPcbTp3EmailRecord aEmailNotification) throws Exception {
		this.setValueObject(EmailNotification, aEmailNotification);
	}

	public String getCompanyName() throws Exception {
		return((String) this.getValueStr(CompanyName));
	}

	public void setCompanyName(String aCompany) throws Exception {
		this.setValueStr(CompanyName, aCompany);
	}

	public String getCompanyAlias() throws Exception {
		return((String) this.getValueStr(CompanyAlias));
	}

	public void setCompanyAlias(String aCompanyAlias) throws Exception {
		this.setValueStr(CompanyAlias, aCompanyAlias);
	}

	public String getEmployeeName() throws Exception {
		return((String) this.getValueStr(EmployeeName));
	}

	public void setEmployeeName(String aEmployee) throws Exception {
		this.setValueStr(EmployeeName, aEmployee);
	}

	public String getB1EmployeeNameCont() throws Exception {
		return((String) this.getValueStr(B1EmployeeNameCont));
	}

	public void setB1EmployeeNameCont(String aEmployee) throws Exception {
		this.setValueStr(B1EmployeeNameCont, aEmployee);
	}

	public String getEmployeeAlias() throws Exception {
		return((String) this.getValueStr(EmployeeAlias));
	}

	public void setEmployeeAlias(String aEmployeeAlias) throws Exception {
		this.setValueStr(EmployeeAlias, aEmployeeAlias);
	}

	public void setCreateDate(DateTime aCreateDate) throws Exception {
		this.setValueDateTime(CreateDate, aCreateDate);
	}

	public DateTime getCreateDate() throws Exception {
		return (this.getValueDateTime(CreateDate));
	}

	public void setLastModifyDate(DateTime aLastModifyDate) throws Exception {
		this.setValueDateTime(LastModifyDate, aLastModifyDate);
	}

	public DateTime getLastModifyDate() throws Exception {
		return (this.getValueDateTime(LastModifyDate));
	}

	public void setEmployeeJoinYear(Integer aEmployeeJoinYear) throws Exception {
		this.setValueInt(EmployeeJoinYear, aEmployeeJoinYear);
	}

	public Integer getEmployeeJoinYear() throws Exception {
		return (this.getValueInt(EmployeeJoinYear));
	}





	public String getB2NoPengenalan() throws Exception {
		return(this.getValueStr(B2NoPengenalan));
	}

	public void setB2NoPengenalan(String B2NoPengenalan) throws Exception {
		this.setValueStr(MsiaLhdnPcbTp3.B2NoPengenalan, B2NoPengenalan);
	}

	public String getB3NoPassport() throws Exception {
		return(this.getValueStr(B3NoPassport));
	}

	public void setB3NoPassport(String B3NoPassport) throws Exception {
		this.setValueStr(MsiaLhdnPcbTp3.B3NoPassport, B3NoPassport);
	}

	public String getB4NoCukaiPendapatan() throws Exception {
		return(this.getValueStr(B4NoCukaiPendapatan));
	}

	public void setB4NoCukaiPendapatan(String B4NoCukaiPendapatan) throws Exception {
		this.setValueStr(MsiaLhdnPcbTp3.B4NoCukaiPendapatan, B4NoCukaiPendapatan);
	}

	public String getA1MajikanTerdahulu1() throws Exception {
		return (this.getValueStr(A1MajikanTerdahulu1));
	}

	public void setA1MajikanTerdahulu1(String A1MajikanTerdahulu1) throws Exception {
		this.setValueStr(MsiaLhdnPcbTp3.A1MajikanTerdahulu1, A1MajikanTerdahulu1);
	}

	public String getA1MajikanTerdahulu1Cont() throws Exception {
		return (this.getValueStr(A1MajikanTerdahulu1Cont));
	}

	public void setA1MajikanTerdahulu1Cont(String A1MajikanTerdahulu1Cont) throws Exception {
		this.setValueStr(MsiaLhdnPcbTp3.A1MajikanTerdahulu1Cont, A1MajikanTerdahulu1Cont);
	}

	public String getA2NoMajikan1() throws Exception {
		return(this.getValueStr(A2NoMajikan1));
	}

	public void setA2NoMajikan1(String A2NoMajikan1) throws Exception {
		this.setValueStr(MsiaLhdnPcbTp3.A2NoMajikan1, A2NoMajikan1);
	}

	public String getA3MajikanTerdahulu2() throws Exception {
		return(this.getValueStr(A3MajikanTerdahulu2));
	}

	public void setA3MajikanTerdahulu2(String A3MajikanTerdahulu2) throws Exception {
		this.setValueStr(MsiaLhdnPcbTp3.A3MajikanTerdahulu2, A3MajikanTerdahulu2);
	}

	public String getA3MajikanTerdahulu2Cont() throws Exception {
		return(this.getValueStr(A3MajikanTerdahulu2Cont));
	}

	public void setA3MajikanTerdahulu2Cont(String A3MajikanTerdahulu2Cont) throws Exception {
		this.setValueStr(MsiaLhdnPcbTp3.A3MajikanTerdahulu2Cont, A3MajikanTerdahulu2Cont);
	}

	public String getA4NoMajikan2() throws Exception {
		return(this.getValueStr(A4NoMajikan2));
	}

	public void setA4NoMajikan2(String A4NoMajikan2) throws Exception {
		this.setValueStr(MsiaLhdnPcbTp3.A4NoMajikan2, A4NoMajikan2);
	}

	public String getC1JumlahKasarBercukai() throws Exception {
		return(this.getValueStr(C1JumlahKasarBercukai));
	}

	public void setC1JumlahKasarBercukai(String C1JumlahKasarBercukai) throws Exception {
		this.setValueStr(MsiaLhdnPcbTp3.C1JumlahKasarBercukai, C1JumlahKasarBercukai);
	}

	public String getC2iElaunPetrol() throws Exception {
		return(this.getValueStr(C2iElaunPetrol));
	}

	public void setC2iElaunPetrol(String C2iElaunPetrol) throws Exception {
		this.setValueStr(MsiaLhdnPcbTp3.C2iElaunPetrol, C2iElaunPetrol);
	}

	public String getC2iiElaunAnak() throws Exception {
		return(this.getValueStr(C2iiElaunAnak));
	}

	public void setC2iiElaunAnak(String C2iiElaunAnak) throws Exception {
		this.setValueStr(MsiaLhdnPcbTp3.C2iiElaunAnak, C2iiElaunAnak);
	}

	public String getC2iiiProdukPercuma() throws Exception {
		return(this.getValueStr(C2iiiProdukPercuma));
	}

	public void setC2iiiProdukPercuma(String C2iiiProdukPercuma) throws Exception {
		this.setValueStr(MsiaLhdnPcbTp3.C2iiiProdukPercuma, C2iiiProdukPercuma);
	}

	public String getC2ivAnugerah() throws Exception {
		return(this.getValueStr(C2ivAnugerah));
	}

	public void setC2ivAnugerah(String C2ivAnugerah) throws Exception {
		this.setValueStr(MsiaLhdnPcbTp3.C2ivAnugerah, C2ivAnugerah);
	}

	public String getC2vLainlain() throws Exception {
		return(this.getValueStr(C2vLainlain));
	}

	public void setC2vLainlain(String C2vLainlain) throws Exception {
		this.setValueStr(MsiaLhdnPcbTp3.C2vLainlain, C2vLainlain);
	}

	public String getC3Kwsp() throws Exception {
		return(this.getValueStr(C3Kwsp));
	}

	public void setC3Kwsp(String C3Kwsp) throws Exception {
		this.setValueStr(MsiaLhdnPcbTp3.C3Kwsp, C3Kwsp);
	}

	public String getC4Zakat() throws Exception {
		return(this.getValueStr(C4Zakat));
	}

	public void setC4Zakat(String C4Zakat) throws Exception {
		this.setValueStr(MsiaLhdnPcbTp3.C4Zakat, C4Zakat);
	}

	public String getC5Pcb() throws Exception {
		return(this.getValueStr(C5Pcb));
	}

	public void setC5Pcb(String C5Pcb) throws Exception {
		this.setValueStr(MsiaLhdnPcbTp3.C5Pcb, C5Pcb);
	}

	public String getD1RawatanPerubatan() throws Exception {
		return(this.getValueStr(D1RawatanPerubatan));
	}

	public void setD1RawatanPerubatan(String D1RawatanPerubatan) throws Exception {
		this.setValueStr(MsiaLhdnPcbTp3.D1RawatanPerubatan, D1RawatanPerubatan);
	}

	public String getD1bPelepasanBapa() throws Exception {
		return(this.getValueStr(D1bPelepasanBapa));
	}

	public void setD1bPelepasanBapa(String D1bPelepasanBapa) throws Exception {
		this.setValueStr(MsiaLhdnPcbTp3.D1bPelepasanBapa, D1bPelepasanBapa);
	}

	public String getD1bPelepasanIbu() throws Exception {
		return(this.getValueStr(D1bPelepasanIbu));
	}

	public void setD1bPelepasanIbu(String D1bPelepasanIbu) throws Exception {
		this.setValueStr(MsiaLhdnPcbTp3.D1bPelepasanIbu, D1bPelepasanIbu);
	}

	public String getD2AlatKurangUpaya() throws Exception {
		return(this.getValueStr(D2AlatKurangUpaya));
	}

	public void setD2AlatKurangUpaya(String D2AlatKurangUpaya) throws Exception {
		this.setValueStr(MsiaLhdnPcbTp3.D2AlatKurangUpaya, D2AlatKurangUpaya);
	}

	public String getD3YuranPendidikan() throws Exception {
		return(this.getValueStr(D3YuranPendidikan));
	}

	public void setD3YuranPendidikan(String D3YuranPendidikan) throws Exception {
		this.setValueStr(MsiaLhdnPcbTp3.D3YuranPendidikan, D3YuranPendidikan);
	}

	public String getD4PenyakitSukarUbati() throws Exception {
		return(this.getValueStr(D4PenyakitSukarUbati));
	}

	public void setD4PenyakitSukarUbati(String D4PenyakitSukarUbati) throws Exception {
		this.setValueStr(MsiaLhdnPcbTp3.D4PenyakitSukarUbati, D4PenyakitSukarUbati);
	}

	public String getD4RawatanKesuburan() throws Exception {
		return(this.getValueStr(D4RawatanKesuburan));
	}

	public void setD4RawatanKesuburan(String D4RawatanKesuburan) throws Exception {
		this.setValueStr(MsiaLhdnPcbTp3.D4RawatanKesuburan, D4RawatanKesuburan);
	}

	public String getD5PemeriksaanKesihatan() throws Exception {
		return(this.getValueStr(D5PemeriksaanKesihatan));
	}

	public void setD5PemeriksaanKesihatan(String D5PemeriksaanKesihatan) throws Exception {
		this.setValueStr(MsiaLhdnPcbTp3.D5PemeriksaanKesihatan, D5PemeriksaanKesihatan);
	}

	public String getD6Sspn() throws Exception {
		return(this.getValueStr(D6Sspn));
	}

	public void setD6Sspn(String D6Sspn) throws Exception {
		this.setValueStr(MsiaLhdnPcbTp3.D6Sspn, D6Sspn);
	}

	public String getD7AlimoniIsteri() throws Exception {
		return(this.getValueStr(D7AlimoniIsteri));
	}

	public void setD7AlimoniIsteri(String D7AlimoniIsteri) throws Exception {
		this.setValueStr(MsiaLhdnPcbTp3.D7AlimoniIsteri, D7AlimoniIsteri);
	}

	public String getD8aInsuranNyawaPencen() throws Exception {
		return(this.getValueStr(D8aInsuranNyawaPencen));
	}

	public void setD8aInsuranNyawaPencen(String D8aInsuranNyawaPencen) throws Exception {
		this.setValueStr(MsiaLhdnPcbTp3.D8aInsuranNyawaPencen, D8aInsuranNyawaPencen);
	}

	public String getD8bInsuranNyawaBukanPencen() throws Exception {
		return(this.getValueStr(D8bInsuranNyawaBukanPencen));
	}

	public void setD8bInsuranNyawaBukanPencen(String D8bInsuranNyawaBukanPencen) throws Exception {
		this.setValueStr(MsiaLhdnPcbTp3.D8bInsuranNyawaBukanPencen, D8bInsuranNyawaBukanPencen);
	}

	public String getD9InsuranDidikUbat() throws Exception {
		return(this.getValueStr(D9InsuranDidikUbat));
	}

	public void setD9InsuranDidikUbat(String D9InsuranDidikUbat) throws Exception {
		this.setValueStr(MsiaLhdnPcbTp3.D9InsuranDidikUbat, D9InsuranDidikUbat);
	}

	public String getD10SkimAnuiti() throws Exception {
		return(this.getValueStr(D10SkimAnuiti));
	}

	public void setD10SkimAnuiti(String D10SkimAnuiti) throws Exception {
		this.setValueStr(MsiaLhdnPcbTp3.D10SkimAnuiti, D10SkimAnuiti);
	}

	public String getD11Perkeso() throws Exception {
		return(this.getValueStr(D11Perkeso));
	}

	public void setD11Perkeso(String D11Perkeso) throws Exception {
		this.setValueStr(MsiaLhdnPcbTp3.D11Perkeso, D11Perkeso);
	}

	public String getD12GayaHidup() throws Exception {
		return(this.getValueStr(D12GayaHidup));
	}

	public void setD12GayaHidup(String D12GayaHidup) throws Exception {
		this.setValueStr(MsiaLhdnPcbTp3.D12GayaHidup, D12GayaHidup);
	}

	public String getD13AlatSusu() throws Exception {
		return(this.getValueStr(D13AlatSusu));
	}

	public void setD13AlatSusu(String D13AlatSusu) throws Exception {
		this.setValueStr(MsiaLhdnPcbTp3.D13AlatSusu, D13AlatSusu);
	}

	public String getD14PusatAsuhan() throws Exception {
		return(this.getValueStr(D14PusatAsuhan));
	}

	public void setD14PusatAsuhan(String D14PusatAsuhan) throws Exception {
		this.setValueStr(MsiaLhdnPcbTp3.D14PusatAsuhan, D14PusatAsuhan);
	}

	public void setTarikhAkuan(DateTime aTarikhAkuan) throws Exception {
		this.setValueDate(TarikhAkuan, aTarikhAkuan);
	}

	public DateTime getTarikhAkuan() throws Exception {
		return(this.getValueDate(TarikhAkuan));
	}


	double C1AccumulatedEarning = 0;
	double C3AccumulatedPaidEpf = 0;
	double C4AccumulatedPaidZakat = 0;
	double C5AccumulatedPaidPcb = 0;
	double AccumulatedPaidSocso = 0;

	public double getC1AccumulatedEarning() throws Exception {
		this.C1AccumulatedEarning = Generic.String2DoubleOrZero(this.getC1JumlahKasarBercukai());
		return C1AccumulatedEarning;
	}

	public void setC1AccumulatedEarning(double aRemuneration) {
		this.C1AccumulatedEarning = aRemuneration;
	}

	public double getC3AccumulatedPaidEpf() throws Exception {
		this.C3AccumulatedPaidEpf = Generic.String2DoubleOrZero(this.getC3Kwsp());
		return C3AccumulatedPaidEpf;
	}

	public void setC3AccumulatedPaidEpf(double aPaidEpf) {
		this.C3AccumulatedPaidEpf = aPaidEpf;
	}

	public double getC4AccumulatedPaidZakat() throws Exception {
		this.C4AccumulatedPaidZakat = Generic.String2DoubleOrZero(this.getC4Zakat());
		return C4AccumulatedPaidZakat;
	}

	public void setC4AccumulatedPaidZakat(double aPaidZakat) {
		this.C4AccumulatedPaidZakat = aPaidZakat;
	}

	public double getC5AccumulatedPaidPcb() throws Exception {
		this.C5AccumulatedPaidPcb = Generic.String2DoubleOrZero(this.getC5Pcb());
		return C5AccumulatedPaidPcb;
	}

	public void setC5AccumulatedPaidPcb(double aPaidPcb) {
		this.C5AccumulatedPaidPcb = aPaidPcb;
	}

	public double getAccumulatedPaidSocso() throws Exception {
		return AccumulatedPaidSocso;
	}

	public void setAccumulatedPaidSocso(double aPaidSocso) {
		this.AccumulatedPaidSocso = aPaidSocso;
	}

	public static String GetPcbTp3Url(MsiaLhdnPcbTp3 aPcbTp3) throws Exception {
		String urlPath;
		if (aPcbTp3.getEmployeeJoinYear() == 2019) {
			urlPath = MsiaLhdnPcbTp3.class.getClassLoader().getResource(HTML_RESOURCE_2019).getPath();
		} else if (aPcbTp3.getEmployeeJoinYear() == 2020) {
			urlPath = MsiaLhdnPcbTp3.class.getClassLoader().getResource(HTML_RESOURCE_2020).getPath();
		} else {
			throw new Hinderance("Fail to populate TP3, no join year in MsiaLhdnPcbTp3, company: " + aPcbTp3.getCompanyName() + ", employee: " + aPcbTp3.getEmployeeName());
		}
		return(urlPath);
	}

	public static String PopulatePcbTp3Html(Connection aConn, MsiaLhdnPcbTp3 aPcbTp3) throws Exception {
		String urlPath = GetPcbTp3Url(aPcbTp3);
		Document htmlDoc = ReportHtml.CreateHtmlDoc(urlPath);
		ReportHtml.SetReportText(htmlDoc, "wt_A1_Majikan_Terdahulu1", aPcbTp3.getA1MajikanTerdahulu1());
		ReportHtml.SetReportText(htmlDoc, "wt_A1_Majikan_Terdahulu1_Cont", aPcbTp3.getA1MajikanTerdahulu1Cont());
		ReportHtml.SetReportText(htmlDoc, "wt_A2_No_Majikan1", aPcbTp3.getA2NoMajikan1());
		ReportHtml.SetReportText(htmlDoc, "wt_A3_Majikan_Terdahulu2", aPcbTp3.getA3MajikanTerdahulu2());
		ReportHtml.SetReportText(htmlDoc, "wt_A3_Majikan_Terdahulu2_Cont", aPcbTp3.getA3MajikanTerdahulu2Cont());
		ReportHtml.SetReportText(htmlDoc, "wt_A4_No_Majikan2", aPcbTp3.getA4NoMajikan2());
		ReportHtml.SetReportText(htmlDoc, "wt_Employee_Name", aPcbTp3.getEmployeeName());
		ReportHtml.SetReportText(htmlDoc, "wt_Employee_Name_Cont", aPcbTp3.getB1EmployeeNameCont());
		ReportHtml.SetReportText(htmlDoc, "wt_B2_No_Pengenalan", aPcbTp3.getB2NoPengenalan());
		ReportHtml.SetReportText(htmlDoc, "wt_B3_No_Passport", aPcbTp3.getB3NoPassport());
		ReportHtml.SetReportText(htmlDoc, "wt_B4_No_Cukai_Pendapatan", aPcbTp3.getB4NoCukaiPendapatan());
		ReportHtml.SetReportText(htmlDoc, "wt_C1_Jumlah_Kasar_Bercukai", aPcbTp3.getC1JumlahKasarBercukai());
		ReportHtml.SetReportText(htmlDoc, "wt_C2i_Elaun_Petrol", aPcbTp3.getC2iElaunPetrol());
		ReportHtml.SetReportText(htmlDoc, "wt_C2ii_Elaun_Anak", aPcbTp3.getC2iiElaunAnak());
		ReportHtml.SetReportText(htmlDoc, "wt_C2iii_Produk_Percuma", aPcbTp3.getC2iiiProdukPercuma());
		ReportHtml.SetReportText(htmlDoc, "wt_C2iv_Anugerah", aPcbTp3.getC2ivAnugerah());
		ReportHtml.SetReportText(htmlDoc, "wt_C2v_Lainlain", aPcbTp3.getC2vLainlain());
		ReportHtml.SetReportText(htmlDoc, "wt_C3_Kwsp", aPcbTp3.getC3Kwsp());
		ReportHtml.SetReportText(htmlDoc, "wt_C4_Zakat", aPcbTp3.getC4Zakat());
		ReportHtml.SetReportText(htmlDoc, "wt_C5_Pcb", aPcbTp3.getC5Pcb());
		ReportHtml.SetReportText(htmlDoc, "wt_D1_Rawatan_Perubatan", aPcbTp3.getD1RawatanPerubatan());
		ReportHtml.SetReportText(htmlDoc, "wt_D1b_Pelepasan_Bapa", aPcbTp3.getD1bPelepasanBapa());
		ReportHtml.SetReportText(htmlDoc, "wt_D1b_Pelepasan_Ibu", aPcbTp3.getD1bPelepasanIbu());
		ReportHtml.SetReportText(htmlDoc, "wt_D2_Alat_Kurang_Upaya", aPcbTp3.getD2AlatKurangUpaya());
		ReportHtml.SetReportText(htmlDoc, "wt_D3_Yuran_Pendidikan", aPcbTp3.getD3YuranPendidikan());
		ReportHtml.SetReportText(htmlDoc, "wt_D4_Penyakit_Sukar_Ubati", aPcbTp3.getD4PenyakitSukarUbati());
		ReportHtml.SetReportText(htmlDoc, "wt_D5_Pemeriksaan_Kesihatan", aPcbTp3.getD5PemeriksaanKesihatan());
		//ReportHtml.SetReportText(htmlDoc, "wt_D4_D5_Total", aPcbTp3.getD4D5Total());
		ReportHtml.SetReportText(htmlDoc, "wt_D6_Sspn", aPcbTp3.getD6Sspn());
		ReportHtml.SetReportText(htmlDoc, "wt_D7_Alimoni_Isteri", aPcbTp3.getD7AlimoniIsteri());
		ReportHtml.SetReportText(htmlDoc, "wt_D8a_Insuran_Nyawa_Pencen", aPcbTp3.getD8aInsuranNyawaPencen());
		ReportHtml.SetReportText(htmlDoc, "wt_D8b_Insuran_Nyawa_Bukan_Pencen", aPcbTp3.getD8bInsuranNyawaBukanPencen());
		ReportHtml.SetReportText(htmlDoc, "wt_D9_Insuran_Didik_Ubat", aPcbTp3.getD9InsuranDidikUbat());
		ReportHtml.SetReportText(htmlDoc, "wt_D10_Skim_Anuiti", aPcbTp3.getD10SkimAnuiti());
		ReportHtml.SetReportText(htmlDoc, "wt_D11_Perkeso", aPcbTp3.getD11Perkeso());
		ReportHtml.SetReportText(htmlDoc, "wt_D12_Gaya_Hidup", aPcbTp3.getD12GayaHidup());
		ReportHtml.SetReportText(htmlDoc, "wt_D13_Alat_Susu", aPcbTp3.getD13AlatSusu());
		ReportHtml.SetReportText(htmlDoc, "wt_D14_Pusat_Asuhan", aPcbTp3.getD14PusatAsuhan());
		ReportHtml.SetReportText(htmlDoc, "wt_D14_Pusat_Asuhan", aPcbTp3.getD14PusatAsuhan());

		DateTime tarikhAkuan = aPcbTp3.getTarikhAkuan();
		if (tarikhAkuan != null) {
			String akuanDay = String.format("%02d", tarikhAkuan.getDayOfMonth());
			String akuanMth = String.format("%02d", tarikhAkuan.getMonthOfYear());
			String akuanYear = String.format("%04d", tarikhAkuan.getYear());
			ReportHtml.SetReportText(htmlDoc, "wt_aku_dd", akuanDay);
			ReportHtml.SetReportText(htmlDoc, "wt_aku_mm", akuanMth);
			ReportHtml.SetReportText(htmlDoc, "wt_aku_yy", akuanYear);
		}

		return(htmlDoc.html());
	}

	public static String GetPcbTp3Html() throws Exception {
		String urlPath = MsiaLhdnPcbTp3.class.getClassLoader().getResource(HTML_RESOURCE_2019).getPath();
		Document htmlDoc = ReportHtml.CreateHtmlDoc(urlPath);
		return(htmlDoc.html());
	}

	public static MsiaLhdnPcbTp3 GetPcbTp3FromDb(Connection aConn, SalarySlip aPayslip, int aYear) throws Exception {
		String companyName = aPayslip.getCompany();
		String companyAlias = aPayslip.getCompanyAlias();
		String workerName = aPayslip.getEmployee();
		String workerAlias = aPayslip.getEmployeeAlias();
		return(GetPcbTp3FromDb(aConn, companyName, companyAlias, workerName, workerAlias, aYear));
	}

	public static MsiaLhdnPcbTp3 GetPcbTp3FromDb(Connection aConn, String aCompanyName, String aCompanyAlias, String aEmployeeName, String aEmployeeAlias, int aYear) throws Exception {
		MsiaLhdnPcbTp3 pcbTp3 = (MsiaLhdnPcbTp3) ObjectBase.CreateObject(aConn, MsiaLhdnPcbTp3.class);
		pcbTp3.setCompanyName(aCompanyName);
		pcbTp3.setCompanyAlias(aCompanyAlias);
		pcbTp3.setEmployeeName(aEmployeeName);
		pcbTp3.setEmployeeAlias(aEmployeeAlias);
		pcbTp3.setEmployeeJoinYear(aYear);
		if (pcbTp3.populate(aConn)) {
			return(pcbTp3);	
		} else {
			return(null);
		}
	}

	/* convert HTML to OPENPDFHTML format
	public static void main(String[] argv) throws Exception {
		Document htmlDoc = ReportHtml.CreateHtmlDoc(HTML_FILE);

		Elements elInput = htmlDoc.select("input");
		elInput.removeAttr("disabled");
		elInput.tagName("div");

		Elements elScript = htmlDoc.select("script");
		elScript.remove();

		Elements elLink = htmlDoc.select("link");
		elLink.remove();

		Elements elTitle = htmlDoc.select("title");
		elTitle.remove();

		Elements elMeta = htmlDoc.select("meta");
		elMeta.remove();

		htmlDoc.outputSettings().syntax(Document.OutputSettings.Syntax.xml).escapeMode(Entities.EscapeMode.xhtml);
		String strHtml = htmlDoc.html().replace("<br>", "<br/>");
		System.out.println(strHtml);
	}

	public static void main(String[] args) throws Exception {
		Document htmlDoc = ReportHtml.CreateHtmlDoc(HTML_FILE);
		System.out.println(htmlDoc.toString());
		PdfEngine.GeneratePdf(PDF_FILE, htmlDoc.toString(), "file:///" + HTML_PATH);
	}

	public static void main(String[] args) throws Exception {
		String HTML_PATH_UI = "C:/Shujutech/StApp/StEMS/web/v01/";
		String htmlUi = HTML_PATH_UI + HTML_NAME;
		Document htmlDoc = ReportHtml.CreateHtmlDoc(htmlUi);
		Elements elInput = htmlDoc.select("input");
		for(Element eachEl : elInput) {
			String inputId = eachEl.attr("id");
			System.out.println(inputId);
		}
	}
	*/


}

