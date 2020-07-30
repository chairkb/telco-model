package biz.shujutech.payroll;

import biz.shujutech.base.Connection;
import biz.shujutech.db.object.Clasz;
import biz.shujutech.db.object.ObjectBase;
import biz.shujutech.db.relational.FieldType;
import biz.shujutech.db.relational.Record;
import biz.shujutech.db.relational.SortOrder;
import biz.shujutech.reflect.ReflectField;
import biz.shujutech.reflect.ReflectIndex;
import org.joda.time.DateTime;
import org.jsoup.nodes.Document;

/*
	uses the following regex to replace setter and getter:
		.,$s/return \(.*\);/return((String) this.getValueStr(\1));/
		.,$s/\(M.*\) = \(.*\);/this.setValueStr(\1, \2);/
*/

public class MsiaLhdnPcbTp1 extends Clasz {
	public static final String HTML_PATH = "C:/Shujutech/StApp/StEMS/src/java/resources/";
	public static final String HTML_NAME = "MsiaLhdnPcbTp1_2019.html";
	public static final String HTML_FILE = HTML_PATH + HTML_NAME;
	public static final String PDF_NAME = "MsiaLhdnPcbTp1_2019.pdf";
	public static final String PDF_FILE = HTML_PATH + PDF_NAME;
	public static final String HTML_RESOURCE = "resources/"+ HTML_NAME;

	@ReflectField(type=FieldType.STRING, size=64, indexes={@ReflectIndex(indexName="idx_pcbtp1_agr", indexNo=0, indexOrder=SortOrder.ASC, isUnique=false)}) public static String CompanyName;
	@ReflectField(type=FieldType.STRING, size=32, indexes={@ReflectIndex(indexName="idx_pcbtp1_agr", indexNo=1, indexOrder=SortOrder.ASC, isUnique=false)}) public static String CompanyAlias;
	@ReflectField(type=FieldType.STRING, size=64, indexes={@ReflectIndex(indexName="idx_pcbtp1_agr", indexNo=2, indexOrder=SortOrder.ASC, isUnique=false)}) public static String EmployeeName;
	@ReflectField(type=FieldType.STRING, size=32, indexes={@ReflectIndex(indexName="idx_pcbtp1_agr", indexNo=3, indexOrder=SortOrder.ASC, isUnique=false)}) public static String EmployeeAlias;
	@ReflectField(type=FieldType.STRING, size=04, indexes={@ReflectIndex(indexName="idx_pcbtp1_agr", indexNo=4, indexOrder=SortOrder.ASC, isUnique=false)}) public static String TahunPotongan;
	@ReflectField(type=FieldType.STRING, size=02, indexes={@ReflectIndex(indexName="idx_pcbtp1_agr", indexNo=5, indexOrder=SortOrder.ASC, isUnique=false)}) public static String BulanPotongan;

	@ReflectField(type=FieldType.DATETIME) public static String CreateDate; // create date
	@ReflectField(type=FieldType.DATETIME) public static String LastModifyDate; // last modify date
	@ReflectField(type=FieldType.BASE64) public static String SignedPdf; // uploaded signed copy
	@ReflectField(type=FieldType.DATETIME) public static String LastUploadDate; // last upload date
	@ReflectField(type=FieldType.BOOLEAN) public static String Signed;
	@ReflectField(type=FieldType.OBJECT, clasz="biz.shujutech.payroll.MsiaLhdnPcbTp1EmailRecord", inline=true, prefetch=true) public static String EmailNotification;
	
	// Bahagian A
	@ReflectField(type=FieldType.STRING, size=64) public static String A1NamaMajikan;
	@ReflectField(type=FieldType.STRING, size=64) public static String A2NoMajikan;

	// Bahagian B
	@ReflectField(type=FieldType.STRING, size=64) public static String B1Nama;
	@ReflectField(type=FieldType.STRING, size=16) public static String B2NoPengenalan;
	@ReflectField(type=FieldType.STRING, size=16) public static String B3NoPasport;
	@ReflectField(type=FieldType.STRING, size=16) public static String B4NoCukaiPendapatan;
	@ReflectField(type=FieldType.STRING, size=16) public static String B5NoPekerja;

	// Bahagian C, terkumpul
	@ReflectField(type=FieldType.STRING, size=8) public static String C1RawatanIbuBapa;
	@ReflectField(type=FieldType.STRING, size=8) public static String C1aRawatanIbuBapa;
	@ReflectField(type=FieldType.STRING, size=8) public static String C1bPelepasanBapa;
	@ReflectField(type=FieldType.STRING, size=8) public static String C1bPelepasanIbu;
	@ReflectField(type=FieldType.STRING, size=8) public static String C2AlatKurangUpaya;
	@ReflectField(type=FieldType.STRING, size=8) public static String C3YuranPendidikan;
	@ReflectField(type=FieldType.STRING, size=8) public static String C4SukarDiUbati;
	@ReflectField(type=FieldType.STRING, size=8) public static String C4iPenyakitSerius;
	@ReflectField(type=FieldType.STRING, size=8) public static String C4iiRawatanKesuburan;
	@ReflectField(type=FieldType.STRING, size=8) public static String C5PeriksaKesihatan;
	@ReflectField(type=FieldType.STRING, size=8) public static String C6SSPN;
	@ReflectField(type=FieldType.STRING, size=8) public static String C7Alimoni;
	@ReflectField(type=FieldType.STRING, size=8) public static String C8aInsuranNyawaPenjawatAwam;
	@ReflectField(type=FieldType.STRING, size=8) public static String C8bInsuranNyawaPenjawatLain;
	@ReflectField(type=FieldType.STRING, size=8) public static String C9InsuranPendidikanNyawa;
	@ReflectField(type=FieldType.STRING, size=8) public static String C10SkimPersaraan;
	@ReflectField(type=FieldType.STRING, size=8) public static String C11Perkeso;
	@ReflectField(type=FieldType.STRING, size=8) public static String C12CaraHidup;
	@ReflectField(type=FieldType.STRING, size=8) public static String C13AlatPenyusuan;
	@ReflectField(type=FieldType.STRING, size=8) public static String C14YuranAsuhan;

	// Bahagian D
	@ReflectField(type=FieldType.STRING, size=8) public static String D1Zakat;

	// Bahagian E
	@ReflectField(type=FieldType.STRING, size=16) public static String ETarikhAkuan;

	// Bahagian F
	@ReflectField(type=FieldType.STRING, size=16) public static String FTarikhPersetujuan;
	@ReflectField(type=FieldType.STRING, size=16) public static String FNama;
	@ReflectField(type=FieldType.STRING, size=16) public static String FJawatan;
	@ReflectField(type=FieldType.STRING, size=128) public static String FAlamatMajikan;

	// Bahagian C, semasa
	@ReflectField(type=FieldType.STRING, size=8) public static String C1RawatanIbuBapaSemasa;
	//@ReflectField(type=FieldType.STRING, size=8) public static String C1aPelepasanBapaSemasa;
	//@ReflectField(type=FieldType.STRING, size=8) public static String C1bPelepasanIbuSemasa;
	@ReflectField(type=FieldType.STRING, size=8) public static String C2AlatKurangUpayaSemasa;
	@ReflectField(type=FieldType.STRING, size=8) public static String C3YuranPendidikanSemasa;
	@ReflectField(type=FieldType.STRING, size=8) public static String C4SukarDiUbatiSemasa;
	@ReflectField(type=FieldType.STRING, size=8) public static String C5PeriksaKesihatanSemasa;
	@ReflectField(type=FieldType.STRING, size=8) public static String C6SSPNSemasa;
	@ReflectField(type=FieldType.STRING, size=8) public static String C7AlimoniSemasa;
	@ReflectField(type=FieldType.STRING, size=8) public static String C8aInsuranNyawaPenjawatAwamSemasa;
	@ReflectField(type=FieldType.STRING, size=8) public static String C8bInsuranNyawaPenjawatLainSemasa;
	@ReflectField(type=FieldType.STRING, size=8) public static String C9InsuranPendidikanNyawaSemasa;
	@ReflectField(type=FieldType.STRING, size=8) public static String C10SkimPersaraanSemasa;
	@ReflectField(type=FieldType.STRING, size=8) public static String C11PerkesoSemasa;
	@ReflectField(type=FieldType.STRING, size=8) public static String C12CaraHidupSemasa;
	@ReflectField(type=FieldType.STRING, size=8) public static String C13AlatPenyusuanSemasa;
	@ReflectField(type=FieldType.STRING, size=8) public static String C14YuranAsuhanSemasa;

	public String getBulanPotongan() throws Exception {
		return((String) this.getValueStr(BulanPotongan));
	}

	public void setBulanPotongan(String BulanPotongan) throws Exception {
		this.setValueStr(MsiaLhdnPcbTp1.BulanPotongan, BulanPotongan);
	}

	public String getTahunPotongan() throws Exception {
		return((String) this.getValueStr(TahunPotongan));
	}

	public void setTahunPotongan(String TahunPotongan) throws Exception {
		this.setValueStr(MsiaLhdnPcbTp1.TahunPotongan, TahunPotongan);
	}

	public String getCompanyName() throws Exception {
		return((String) this.getValueStr(CompanyName));
	}

	public void setCompanyName(String aCompanyName) throws Exception {
		this.setValueStr(CompanyName, aCompanyName);
	}

	public String getCompanyAlias() throws Exception {
		return((String) this.getValueStr(CompanyAlias));
	}

	public void setCompanyAlias(String CompanyAlias) throws Exception {
		this.setValueStr(MsiaLhdnPcbTp1.CompanyAlias, CompanyAlias);
	}

	public String getEmployeeName() throws Exception {
		return((String) this.getValueStr(EmployeeName));
	}

	public void setEmployeeName(String EmployeeName) throws Exception {
		this.setValueStr(MsiaLhdnPcbTp1.EmployeeName, EmployeeName);
	}

	public String getEmployeeAlias() throws Exception {
		return((String) this.getValueStr(EmployeeAlias));
	}

	public void setEmployeeAlias(String EmployeeAlias) throws Exception {
		this.setValueStr(MsiaLhdnPcbTp1.EmployeeAlias, EmployeeAlias);
	}

	public DateTime getCreateDate() throws Exception {
		return (this.getValueDateTime(CreateDate));
	}

	public void setCreateDate(DateTime aCreateDate) throws Exception {
		this.setValueDateTime(CreateDate, aCreateDate);
	}

	public DateTime getLastModifyDate() throws Exception {
		return (this.getValueDateTime(LastModifyDate));
	}

	public void setLastModifyDate(DateTime aLastModifyDate) throws Exception {
		this.setValueDateTime(MsiaLhdnPcbTp1.LastModifyDate, aLastModifyDate);
	}

	public String getSignedPdf() throws Exception {
		return((String) this.getValueStr(SignedPdf));
	}

	public void setSignedPdf(String SignedPdf) throws Exception {
		this.setValueStr(MsiaLhdnPcbTp1.SignedPdf, SignedPdf);
	}

	public String getLastUploadDate() throws Exception {
		return((String) this.getValueStr(LastUploadDate));
	}

	public void setLastUploadDate(String LastUploadDate) throws Exception {
		this.setValueStr(MsiaLhdnPcbTp1.LastUploadDate, LastUploadDate);
	}

	public String getSigned() throws Exception {
		return((String) this.getValueStr(Signed));
	}

	public void setSigned(String Signed) throws Exception {
		this.setValueStr(MsiaLhdnPcbTp1.Signed, Signed);
	}

	public String getEmailNotification() throws Exception {
		return((String) this.getValueStr(EmailNotification));
	}

	public void setEmailNotification(String EmailNotification) throws Exception {
		this.setValueStr(MsiaLhdnPcbTp1.EmailNotification, EmailNotification);
	}

	public String getA1NamaMajikan() throws Exception {
		return((String) this.getValueStr(A1NamaMajikan));
	}

	public void setA1NamaMajikan(String A1NamaMajikan) throws Exception {
		this.setValueStr(MsiaLhdnPcbTp1.A1NamaMajikan, A1NamaMajikan);
	}

	public String getA2NoMajikan() throws Exception {
		return((String) this.getValueStr(A2NoMajikan));
	}

	public void setA2NoMajikan(String A2NoMajikan) throws Exception {
		this.setValueStr(MsiaLhdnPcbTp1.A2NoMajikan, A2NoMajikan);
	}

	public String getB1Nama() throws Exception {
		return((String) this.getValueStr(B1Nama));
	}

	public void setB1Nama(String B1Nama) throws Exception {
		this.setValueStr(MsiaLhdnPcbTp1.B1Nama, B1Nama);
	}

	public String getB2NoPengenalan() throws Exception {
		return((String) this.getValueStr(B2NoPengenalan));
	}

	public void setB2NoPengenalan(String B2NoPengenalan) throws Exception {
		this.setValueStr(MsiaLhdnPcbTp1.B2NoPengenalan, B2NoPengenalan);
	}

	public String getB3NoPasport() throws Exception {
		return((String) this.getValueStr(B3NoPasport));
	}

	public void setB3NoPasport(String B3NoPasport) throws Exception {
		this.setValueStr(MsiaLhdnPcbTp1.B3NoPasport, B3NoPasport);
	}

	public String getB4NoCukaiPendapatan() throws Exception {
		return((String) this.getValueStr(B4NoCukaiPendapatan));
	}

	public void setB4NoCukaiPendapatan(String B4NoCukaiPendapatan) throws Exception {
		this.setValueStr(MsiaLhdnPcbTp1.B4NoCukaiPendapatan, B4NoCukaiPendapatan);
	}

	public String getB5NoPekerja() throws Exception {
		return((String) this.getValueStr(B5NoPekerja));
	}

	public void setB5NoPekerja(String B5NoPekerja) throws Exception {
		this.setValueStr(MsiaLhdnPcbTp1.B5NoPekerja, B5NoPekerja);
	}

	public String getC1RawatanIbuBapa() throws Exception {
		return((String) this.getValueStr(C1RawatanIbuBapa));
	}

	public void setC1RawatanIbuBapa(String C1RawatanIbuBapa) throws Exception {
		this.setValueStr(MsiaLhdnPcbTp1.C1RawatanIbuBapa, C1RawatanIbuBapa);
	}

	public String getC1bPelepasanBapa() throws Exception {
		return((String) this.getValueStr(C1bPelepasanBapa));
	}

	public void setC1aPelepasanBapa(String C1aPelepasanBapa) throws Exception {
		this.setValueStr(MsiaLhdnPcbTp1.C1bPelepasanBapa, C1aPelepasanBapa);
	}

	public String getC1bPelepasanIbu() throws Exception {
		return((String) this.getValueStr(C1bPelepasanIbu));
	}

	public void setC1bPelepasanIbu(String C1bPelepasanIbu) throws Exception {
		this.setValueStr(MsiaLhdnPcbTp1.C1bPelepasanIbu, C1bPelepasanIbu);
	}

	public String getC2AlatKurangUpaya() throws Exception {
		return((String) this.getValueStr(C2AlatKurangUpaya));
	}

	public void setC2AlatKurangUpaya(String C2AlatKurangUpaya) throws Exception {
		this.setValueStr(MsiaLhdnPcbTp1.C2AlatKurangUpaya, C2AlatKurangUpaya);
	}

	public String getC3YuranPendidikan() throws Exception {
		return((String) this.getValueStr(C3YuranPendidikan));
	}

	public void setC3YuranPendidikan(String C3YuranPendidikan) throws Exception {
		this.setValueStr(MsiaLhdnPcbTp1.C3YuranPendidikan, C3YuranPendidikan);
	}

	public String getC4SukarDiUbati() throws Exception {
		return((String) this.getValueStr(C4SukarDiUbati));
	}

	public void setC4SukarDiUbati(String C4SukarDiUbati) throws Exception {
		this.setValueStr(MsiaLhdnPcbTp1.C4SukarDiUbati, C4SukarDiUbati);
	}

	public String getC4iPenyakitSerius() throws Exception {
		return((String) this.getValueStr(C4iPenyakitSerius));
	}

	public void setC4iPenyakitSerius(String C4RawatanKesuburan) throws Exception {
		this.setValueStr(MsiaLhdnPcbTp1.C4iPenyakitSerius, C4RawatanKesuburan);
	}

	public String getC4iiRawatanKesuburan() throws Exception {
		return((String) this.getValueStr(C4iiRawatanKesuburan));
	}

	public void setC4iiRawatanKesuburan(String C4RawatanKesuburan) throws Exception {
		this.setValueStr(MsiaLhdnPcbTp1.C4iiRawatanKesuburan, C4RawatanKesuburan);
	}

	public String getC5PeriksaKesihatan() throws Exception {
		return((String) this.getValueStr(C5PeriksaKesihatan));
	}

	public void setC5PeriksaKesihatan(String C5PeriksaKesihatan) throws Exception {
		this.setValueStr(MsiaLhdnPcbTp1.C5PeriksaKesihatan, C5PeriksaKesihatan);
	}

	public String getC6SSPN() throws Exception {
		return((String) this.getValueStr(C6SSPN));
	}

	public void setC6SSPN(String C6SSPN) throws Exception {
		this.setValueStr(MsiaLhdnPcbTp1.C6SSPN, C6SSPN);
	}

	public String getC7Alimoni() throws Exception {
		return((String) this.getValueStr(C7Alimoni));
	}

	public void setC7Alimoni(String C7Alimoni) throws Exception {
		this.setValueStr(MsiaLhdnPcbTp1.C7Alimoni, C7Alimoni);
	}

	public String getC8aInsuranNyawaPenjawatAwam() throws Exception {
		return((String) this.getValueStr(C8aInsuranNyawaPenjawatAwam));
	}

	public void setC8aInsuranNyawaPenjawatAwam(String C8aInsuranNyawaPenjawatAwam) throws Exception {
		this.setValueStr(MsiaLhdnPcbTp1.C8aInsuranNyawaPenjawatAwam, C8aInsuranNyawaPenjawatAwam);
	}

	public String getC8bInsuranNyawaPenjawatLain() throws Exception {
		return((String) this.getValueStr(C8bInsuranNyawaPenjawatLain));
	}

	public void setC8bInsuranNyawaPenjawatLain(String C8bInsuranNyawaPenjawatLain) throws Exception {
		this.setValueStr(MsiaLhdnPcbTp1.C8bInsuranNyawaPenjawatLain, C8bInsuranNyawaPenjawatLain);
	}

	public String getC9InsuranPendidikanNyawa() throws Exception {
		return((String) this.getValueStr(C9InsuranPendidikanNyawa));
	}

	public void setC9InsuranPendidikanNyawa(String C9InsuranPendidikanNyawa) throws Exception {
		this.setValueStr(MsiaLhdnPcbTp1.C9InsuranPendidikanNyawa, C9InsuranPendidikanNyawa);
	}

	public String getC10SkimPersaraan() throws Exception {
		return((String) this.getValueStr(C10SkimPersaraan));
	}

	public void setC10SkimPersaraan(String C10SkimPersaraan) throws Exception {
		this.setValueStr(MsiaLhdnPcbTp1.C10SkimPersaraan, C10SkimPersaraan);
	}

	public String getC11Perkeso() throws Exception {
		return((String) this.getValueStr(C11Perkeso));
	}

	public void setC11Perkeso(String C11Perkeso) throws Exception {
		this.setValueStr(MsiaLhdnPcbTp1.C11Perkeso, C11Perkeso);
	}

	public String getC12CaraHidup() throws Exception {
		return((String) this.getValueStr(C12CaraHidup));
	}

	public void setC12CaraHidup(String C12CaraHidup) throws Exception {
		this.setValueStr(MsiaLhdnPcbTp1.C12CaraHidup, C12CaraHidup);
	}

	public String getC13AlatPenyusuan() throws Exception {
		return((String) this.getValueStr(C13AlatPenyusuan));
	}

	public void setC13AlatPenyusuan(String C13AlatPenyusuan) throws Exception {
		this.setValueStr(MsiaLhdnPcbTp1.C13AlatPenyusuan, C13AlatPenyusuan);
	}

	public String getC14YuranAsuhan() throws Exception {
		return((String) this.getValueStr(C14YuranAsuhan));
	}

	public void setC14YuranAsuhan(String C14YuranAsuhan) throws Exception {
		this.setValueStr(MsiaLhdnPcbTp1.C14YuranAsuhan, C14YuranAsuhan);
	}

	public String getD1Zakat() throws Exception {
		return((String) this.getValueStr(D1Zakat));
	}

	public void setD1Zakat(String D1Zakat) throws Exception {
		this.setValueStr(MsiaLhdnPcbTp1.D1Zakat, D1Zakat);
	}

	public String getETarikhAkuan() throws Exception {
		return((String) this.getValueStr(ETarikhAkuan));
	}

	public void setETarikhAkuan(String ETarikhAkuan) throws Exception {
		this.setValueStr(MsiaLhdnPcbTp1.ETarikhAkuan, ETarikhAkuan);
	}

	public String getFTarikhPersetujuan() throws Exception {
		return((String) this.getValueStr(FTarikhPersetujuan));
	}

	public void setFTarikhPersetujuan(String FTarikhPersetujuan) throws Exception {
		this.setValueStr(MsiaLhdnPcbTp1.FTarikhPersetujuan, FTarikhPersetujuan);
	}
	public String getFNama() throws Exception {
		return((String) this.getValueStr(FNama));
	}

	public void setFNama(String FNama) throws Exception {
		this.setValueStr(MsiaLhdnPcbTp1.FNama, FNama);
	}

	public String getFJawatan() throws Exception {
		return((String) this.getValueStr(FJawatan));
	}

	public void setFJawatan(String FJawatan) throws Exception {
		this.setValueStr(MsiaLhdnPcbTp1.FJawatan, FJawatan);
	}

	public String getFAlamatMajikan() throws Exception {
		return((String) this.getValueStr(FAlamatMajikan));
	}

	public void setFAlamatMajikan(String FAlamatMajikan) throws Exception {
		this.setValueStr(MsiaLhdnPcbTp1.FAlamatMajikan, FAlamatMajikan);
	}

	// Bahagian C, semasa
	public String getC1RawatanIbuBapaSemasa() throws Exception {
		return((String) this.getValueStr(C1RawatanIbuBapaSemasa));
	}

	public void setC1RawatanIbuBapaSemasa(String C1RawatanIbuBapa) throws Exception {
		this.setValueStr(MsiaLhdnPcbTp1.C1RawatanIbuBapaSemasa, C1RawatanIbuBapa);
	}

	/*
	public String getC1aPelepasanBapaSemasa() throws Exception {
		return((String) this.getValueStr(C1aPelepasanBapaSemasa));
	}

	public void setC1aPelepasanBapaSemasa(String C1aPelepasanBapa) throws Exception {
		this.setValueStr(MsiaLhdnPcbTp1.C1aPelepasanBapaSemasa, C1aPelepasanBapa);
	}

	public String getC1bPelepasanIbuSemasa() throws Exception {
		return((String) this.getValueStr(C1bPelepasanIbuSemasa));
	}

	public void setC1bPelepasanIbuSemasa(String C1bPelepasanIbu) throws Exception {
		this.setValueStr(MsiaLhdnPcbTp1.C1bPelepasanIbuSemasa, C1bPelepasanIbu);
	}
	*/

	public String getC2AlatKurangUpayaSemasa() throws Exception {
		return((String) this.getValueStr(C2AlatKurangUpayaSemasa));
	}

	public void setC2AlatKurangUpayaSemasa(String C2AlatKurangUpaya) throws Exception {
		this.setValueStr(MsiaLhdnPcbTp1.C2AlatKurangUpayaSemasa, C2AlatKurangUpaya);
	}

	public String getC3YuranPendidikanSemasa() throws Exception {
		return((String) this.getValueStr(C3YuranPendidikanSemasa));
	}

	public void setC3YuranPendidikanSemasa(String C3YuranPendidikan) throws Exception {
		this.setValueStr(MsiaLhdnPcbTp1.C3YuranPendidikanSemasa, C3YuranPendidikan);
	}

	public String getC4SukarDiUbatiSemasa() throws Exception {
		return((String) this.getValueStr(C4SukarDiUbatiSemasa));
	}

	public void setC4SukarDiUbatiSemasa(String C4SukarDiUbati) throws Exception {
		this.setValueStr(MsiaLhdnPcbTp1.C4SukarDiUbatiSemasa, C4SukarDiUbati);
	}

	public String getC5PeriksaKesihatanSemasa() throws Exception {
		return((String) this.getValueStr(C5PeriksaKesihatanSemasa));
	}

	public void setC5PeriksaKesihatanSemasa(String C5PeriksaKesihatan) throws Exception {
		this.setValueStr(MsiaLhdnPcbTp1.C5PeriksaKesihatanSemasa, C5PeriksaKesihatan);
	}

	public String getC6SSPNSemasa() throws Exception {
		return((String) this.getValueStr(C6SSPNSemasa));
	}

	public void setC6SSPNSemasa(String C6SSPN) throws Exception {
		this.setValueStr(MsiaLhdnPcbTp1.C6SSPNSemasa, C6SSPN);
	}

	public String getC7AlimoniSemasa() throws Exception {
		return((String) this.getValueStr(C7AlimoniSemasa));
	}

	public void setC7AlimoniSemasa(String C7Alimoni) throws Exception {
		this.setValueStr(MsiaLhdnPcbTp1.C7AlimoniSemasa, C7Alimoni);
	}

	public String getC8aInsuranNyawaPenjawatAwamSemasa() throws Exception {
		return((String) this.getValueStr(C8aInsuranNyawaPenjawatAwamSemasa));
	}

	public void setC8aInsuranNyawaPenjawatAwamSemasa(String C8aInsuranNyawaPenjawatAwam) throws Exception {
		this.setValueStr(MsiaLhdnPcbTp1.C8aInsuranNyawaPenjawatAwamSemasa, C8aInsuranNyawaPenjawatAwam);
	}

	public String getC8bInsuranNyawaPenjawatLainSemasa() throws Exception {
		return((String) this.getValueStr(C8bInsuranNyawaPenjawatLainSemasa));
	}

	public void setC8bInsuranNyawaPenjawatLainSemasa(String C8bInsuranNyawaPenjawatLain) throws Exception {
		this.setValueStr(MsiaLhdnPcbTp1.C8bInsuranNyawaPenjawatLainSemasa, C8bInsuranNyawaPenjawatLain);
	}

	public String getC9InsuranPendidikanNyawaSemasa() throws Exception {
		return((String) this.getValueStr(C9InsuranPendidikanNyawaSemasa));
	}

	public void setC9InsuranPendidikanNyawaSemasa(String C9InsuranPendidikanNyawa) throws Exception {
		this.setValueStr(MsiaLhdnPcbTp1.C9InsuranPendidikanNyawaSemasa, C9InsuranPendidikanNyawa);
	}

	public String getC10SkimPersaraanSemasa() throws Exception {
		return((String) this.getValueStr(C10SkimPersaraanSemasa));
	}

	public void setC10SkimPersaraanSemasa(String C10SkimPersaraan) throws Exception {
		this.setValueStr(MsiaLhdnPcbTp1.C10SkimPersaraanSemasa, C10SkimPersaraan);
	}

	public String getC11PerkesoSemasa() throws Exception {
		return((String) this.getValueStr(C11PerkesoSemasa));
	}

	public void setC11PerkesoSemasa(String C11Perkeso) throws Exception {
		this.setValueStr(MsiaLhdnPcbTp1.C11PerkesoSemasa, C11Perkeso);
	}

	public String getC12CaraHidupSemasa() throws Exception {
		return((String) this.getValueStr(C12CaraHidupSemasa));
	}

	public void setC12CaraHidupSemasa(String C12CaraHidup) throws Exception {
		this.setValueStr(MsiaLhdnPcbTp1.C12CaraHidupSemasa, C12CaraHidup);
	}

	public String getC13AlatPenyusuanSemasa() throws Exception {
		return((String) this.getValueStr(C13AlatPenyusuanSemasa));
	}

	public void setC13AlatPenyusuanSemasa(String C13AlatPenyusuan) throws Exception {
		this.setValueStr(MsiaLhdnPcbTp1.C13AlatPenyusuanSemasa, C13AlatPenyusuan);
	}

	public String getC14YuranAsuhanSemasa() throws Exception {
		return((String) this.getValueStr(C14YuranAsuhanSemasa));
	}

	public void setC14YuranAsuhanSemasa(String C14YuranAsuhan) throws Exception {
		this.setValueStr(MsiaLhdnPcbTp1.C14YuranAsuhanSemasa, C14YuranAsuhan);
	}

	public static String GetPcbTp1Html() throws Exception {
		String urlPath = MsiaLhdnPcbTp1.class.getClassLoader().getResource(HTML_RESOURCE).getPath();
		Document htmlDoc = ReportHtml.CreateHtmlDoc(urlPath);
		return(htmlDoc.html());
	}

	public static MsiaLhdnPcbTp1 GetLatestPcbTp1FromDb(Connection aConn, SalarySlip aPayslip) throws Exception {
		String companyName = aPayslip.getCompany();
		String companyAlias = aPayslip.getCompanyAlias();
		String workerName = aPayslip.getEmployee();
		String workerAlias = aPayslip.getEmployeeAlias();
		return(GetLatestPcbTp1FromDb(aConn, companyName, companyAlias, workerName, workerAlias));
	}

	public static MsiaLhdnPcbTp1 GetLatestPcbTp1FromDb(Connection aConn, String aCompanyName, String aCompanyAlias, String aEmployeeName, String aEmployeeAlias) throws Exception {
		MsiaLhdnPcbTp1 pcbTp1 = (MsiaLhdnPcbTp1) ObjectBase.CreateObject(aConn, MsiaLhdnPcbTp1.class);
		pcbTp1.setCompanyName(aCompanyName);
		pcbTp1.setCompanyAlias(aCompanyAlias);
		pcbTp1.setEmployeeName(aEmployeeName);
		pcbTp1.setEmployeeAlias(aEmployeeAlias);

		Record recWhere = new Record();
		Clasz.SetWhereField(recWhere, pcbTp1.getField(MsiaLhdnPcbTp1.CompanyName));
		Clasz.SetWhereField(recWhere, pcbTp1.getField(MsiaLhdnPcbTp1.CompanyAlias));
		Clasz.SetWhereField(recWhere, pcbTp1.getField(MsiaLhdnPcbTp1.EmployeeName));
		Clasz.SetWhereField(recWhere, pcbTp1.getField(MsiaLhdnPcbTp1.EmployeeAlias));
		MsiaLhdnPcbTp1 latestPcbTp1 = (MsiaLhdnPcbTp1) Clasz.FetchUnique(aConn, pcbTp1, recWhere, MsiaLhdnPcbTp1.CreateDate, SortOrder.DSC);
		return(latestPcbTp1);	
	}

	public MsiaLhdnPcbTp1EmailRecord getEmailNotification(Connection aConn) throws Exception {
		return((MsiaLhdnPcbTp1EmailRecord) this.getValueObject(aConn, EmailNotification));
	}

	public MsiaLhdnPcbTp1EmailRecord gotEmailNotification(Connection aConn) throws Exception {
		return((MsiaLhdnPcbTp1EmailRecord) this.gotValueObject(aConn, EmailNotification));
	}

	public void setEmailNotification(MsiaLhdnPcbTp1EmailRecord aEmailNotification) throws Exception {
		this.setValueObject(EmailNotification, aEmailNotification);
	}

}