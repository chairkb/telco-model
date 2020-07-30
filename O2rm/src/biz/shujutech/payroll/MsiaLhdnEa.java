package biz.shujutech.payroll;

import biz.shujutech.base.App;
import biz.shujutech.base.DateAndTime;
import biz.shujutech.base.Hinderance;
import biz.shujutech.bznes.AddrTypeOrganization;
import biz.shujutech.db.object.Clasz;
import biz.shujutech.db.object.ObjectBase;
import biz.shujutech.db.relational.Field;
import biz.shujutech.db.relational.FieldType;
import biz.shujutech.db.relational.Record;
import biz.shujutech.db.relational.SortOrder;
import biz.shujutech.bznes.Company;
import biz.shujutech.bznes.CompanyMalaysia;
import biz.shujutech.bznes.Country;
import biz.shujutech.bznes.Employment;
import biz.shujutech.bznes.EmploymentMalaysia;
import biz.shujutech.bznes.Identity;
import biz.shujutech.bznes.MalaysiaIdentityCard;
import biz.shujutech.bznes.MalaysiaPermanentResident;
import biz.shujutech.bznes.Money;
import biz.shujutech.bznes.Passport;
import biz.shujutech.bznes.Person;
import biz.shujutech.bznes.TelephoneTypeOrganization;
import biz.shujutech.db.relational.BaseDb;
import biz.shujutech.pdf.PdfEngine;
import biz.shujutech.reflect.ReflectField;
import biz.shujutech.reflect.ReflectIndex;
import biz.shujutech.util.Generic;
import java.util.Calendar;
import org.joda.time.DateTime;
import org.jsoup.nodes.Document;
import biz.shujutech.base.Connection;
import biz.shujutech.db.object.FieldObjectBox;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class MsiaLhdnEa extends Clasz {
	public static final String HTML_PATH = "C:/Shujutech/StApp/StEMS/src/java/resources/";
	public static final String HTML_NAME = "MsiaLhdnEa_2017.html";
	public static final String HTML_FILE = HTML_PATH + HTML_NAME;
	public static final String PDF_NAME = "MsiaLhdnEa_2017.pdf";
	public static final String PDF_FILE = HTML_PATH + PDF_NAME;
	public static final String HTML_RESOURCE = "resources/MsiaLhdnEa_2017.html";

	@ReflectField(type=FieldType.STRING, size=64, displayPosition=10, indexes={@ReflectIndex(indexName="idx_lhdnea", indexNo=0, indexOrder=SortOrder.ASC, isUnique=true)}) public static String CompanyName;
	@ReflectField(type=FieldType.STRING, size=32, displayPosition=20, indexes={@ReflectIndex(indexName="idx_lhdnea", indexNo=1, indexOrder=SortOrder.ASC, isUnique=true)}) public static String CompanyAlias;
	@ReflectField(type=FieldType.INTEGER, displayPosition=60, indexes={@ReflectIndex(indexName="idx_lhdnea", indexNo=2, indexOrder=SortOrder.ASC, isUnique=true)}) public static String ForYear;
	@ReflectField(type=FieldType.STRING, size=64, displayPosition=30, indexes={@ReflectIndex(indexName="idx_lhdnea", indexNo=3, indexOrder=SortOrder.ASC, isUnique=true)}) public static String EmployeeName;
	@ReflectField(type=FieldType.STRING, size=32, displayPosition=40, indexes={@ReflectIndex(indexName="idx_lhdnea", indexNo=4, indexOrder=SortOrder.ASC, isUnique=true)}) public static String EmployeeAlias;
	@ReflectField(type=FieldType.DATETIME, displayPosition=50) public static String CreateDate;
	//@ReflectField(type=FieldType.HTML, size=51200, displayPosition=70) public static String BorangEa;
	@ReflectField(type=FieldType.OBJECT, clasz="biz.shujutech.payroll.MsiaLhdnEaEmailRecord", inline=true, prefetch=true, displayPosition=90) public static String EmailNotification;

	@ReflectField(type=FieldType.STRING, size=5, displayPosition=80) public static String NoSiri; // new
	@ReflectField(type=FieldType.STRING, size=32, displayPosition=80) public static String NoMajikanE;

	@ReflectField(type=FieldType.STRING, size=64, displayPosition=130) public static String NamaPekerja;
	@ReflectField(type=FieldType.STRING, size=32, displayPosition=140) public static String JawatanPekerja;
	@ReflectField(type=FieldType.STRING, size=32, displayPosition=150) public static String NoKakitangan;
	@ReflectField(type=FieldType.STRING, size=32, displayPosition=160) public static String NoKpBaru;
	@ReflectField(type=FieldType.STRING, size=32, displayPosition=170) public static String NoPassport;
	@ReflectField(type=FieldType.STRING, size=2, displayPosition=180) public static String BilanganAnak;

	@ReflectField(type=FieldType.STRING, size=32, displayPosition=190) public static String NoCukaiPekerja;
	@ReflectField(type=FieldType.STRING, size=32, displayPosition=200) public static String CawanganLhdn;
	@ReflectField(type=FieldType.STRING, size=32, displayPosition=210) public static String NoKwsp;
	@ReflectField(type=FieldType.STRING, size=32, displayPosition=220) public static String NoPerkeso;
	@ReflectField(type=FieldType.STRING, size=32, displayPosition=230) public static String TarikhMulaKerja;
	@ReflectField(type=FieldType.STRING, size=32, displayPosition=240) public static String TarikhHentiKerja;

	@ReflectField(type=FieldType.STRING, size=32, displayPosition=250) public static String PerihalElaun;
	@ReflectField(type=FieldType.STRING, size=32, displayPosition=260) public static String GajiKasar;
	@ReflectField(type=FieldType.STRING, size=32, displayPosition=270) public static String KomisenBonus;
	@ReflectField(type=FieldType.STRING, size=32, displayPosition=280) public static String ElaunLain;
	@ReflectField(type=FieldType.STRING, size=32, displayPosition=280) public static String CukaiMajikanBayar;
	@ReflectField(type=FieldType.STRING, size=32, displayPosition=280) public static String Esos;
	@ReflectField(type=FieldType.STRING, size=32, displayPosition=280) public static String GanjaranDari;
	@ReflectField(type=FieldType.STRING, size=32, displayPosition=280) public static String GanjaranHingga;
	@ReflectField(type=FieldType.STRING, size=32, displayPosition=280) public static String Ganjaran;
	@ReflectField(type=FieldType.STRING, size=32, displayPosition=280) public static String PendapatanJenisA;
	@ReflectField(type=FieldType.STRING, size=32, displayPosition=280) public static String PendapatanJenisB;
	@ReflectField(type=FieldType.STRING, size=32, displayPosition=280) public static String PendapatanDulu;
	@ReflectField(type=FieldType.STRING, size=32, displayPosition=280) public static String ManfaatNyatakan;
	@ReflectField(type=FieldType.STRING, size=32, displayPosition=280) public static String ManfaatBarangan;
	@ReflectField(type=FieldType.STRING, size=128, displayPosition=280) public static String AlamatManfaat;
	@ReflectField(type=FieldType.STRING, size=32, displayPosition=280) public static String AlamatNilai;
	@ReflectField(type=FieldType.STRING, size=32, displayPosition=280) public static String KwspPencenDiTolak;
	@ReflectField(type=FieldType.STRING, size=32, displayPosition=280) public static String PampasanHilangKerja;
	@ReflectField(type=FieldType.STRING, size=32, displayPosition=280) public static String Pencen;
	@ReflectField(type=FieldType.STRING, size=32, displayPosition=280) public static String Anuiti;
	@ReflectField(type=FieldType.STRING, size=32, displayPosition=290) public static String JumlahPendapatan;
	@ReflectField(type=FieldType.STRING, size=32, displayPosition=310) public static String PotonganPcb;
	@ReflectField(type=FieldType.STRING, size=32, displayPosition=310) public static String PotonganCp38;
	@ReflectField(type=FieldType.STRING, size=32, displayPosition=310) public static String PotonganZakat;
	@ReflectField(type=FieldType.STRING, size=32, displayPosition=310) public static String Tp1Pelepasan;
	@ReflectField(type=FieldType.STRING, size=32, displayPosition=310) public static String Tp1Zakat;
	@ReflectField(type=FieldType.STRING, size=32, displayPosition=310) public static String PelepasanAnak;

	@ReflectField(type=FieldType.STRING, size=32, displayPosition=320) public static String NamaKwsp; // new
	@ReflectField(type=FieldType.STRING, size=32, displayPosition=320) public static String CarumanKwsp;
	@ReflectField(type=FieldType.STRING, size=32, displayPosition=330) public static String CarumanPerkeso;
	@ReflectField(type=FieldType.STRING, size=32, displayPosition=340) public static String ManfaatNoCukai;

	@ReflectField(type=FieldType.STRING, size=32, displayPosition=100) public static String Tarikh;
	@ReflectField(type=FieldType.STRING, size=32, displayPosition=30) public static String NamaPegawai;
	@ReflectField(type=FieldType.STRING, size=32, displayPosition=40) public static String JawatanPegawai;
	@ReflectField(type=FieldType.STRING, size=64, displayPosition=90) public static String NamaMajikan;
	@ReflectField(type=FieldType.STRING, size=128, displayPosition=110) public static String AlamatMajikan;
	@ReflectField(type=FieldType.STRING, size=32, displayPosition=120) public static String TaliponMajikan;

	@ReflectField(type=FieldType.OBJECT, clasz="biz.shujutech.payroll.MsiaLhdnPcb2ii", deleteAsMember=true, prefetch=false, displayPosition=130) public static String PenyataPcb2ii;

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

	public void setCreateDate(DateTime aCreateDate) throws Exception {
		this.setValueDateTime(CreateDate, aCreateDate);
	}

	public DateTime getCreateDate() throws Exception {
		return (this.getValueDateTime(CreateDate));
	}

	public Integer getForYear() throws Exception {
		return(this.getValueInt(ForYear));
	}

	public void setForYear(Integer aValue) throws Exception {
		this.setValueInt(ForYear, aValue);
	}

	/*
	public String getBorangEa() throws Exception {
		return(this.getValueStr(BorangEa));
	}

	public void setBorangEa(String aBorangEa) throws Exception {
		this.setValueStr(BorangEa, aBorangEa);
	}
	*/

	public String getNamaPegawai() throws Exception {
		return(this.getValueStr(NamaPegawai));
	}

	public void setNamaPegawai(String aNamaPegawai) throws Exception {
		this.setValueStr(NamaPegawai, aNamaPegawai);
	}

	public String getJawatanPegawai() throws Exception {
		return(this.getValueStr(JawatanPegawai));
	}

	public void setJawatanPegawai(String aJawatanPegawai) throws Exception {
		this.setValueStr(JawatanPegawai, aJawatanPegawai);
	}

	public String getNoSiri() throws Exception {
		return(this.getValueStr(NoSiri));
	}

	public void setNoSiri(String noSiri) throws Exception {
		this.setValueStr(NoSiri, noSiri);
	}

	public String getNoMajikanE() throws Exception {
		return(this.getValueStr(NoMajikanE));
	}

	public void setNoMajikanE(String noMajikanE) throws Exception {
		this.setValueStr(NoMajikanE, noMajikanE);
	}

	public String getNamaMajikan() throws Exception {
		return(this.getValueStr(NamaMajikan));
	}

	public void setNamaMajikan(String namaMajikan) throws Exception {
		this.setValueStr(NamaMajikan, namaMajikan);
	}

	public String getTarikh() throws Exception {
		return(this.getValueStr(Tarikh));
	}

	public void setTarikh(String tarikh) throws Exception {
		this.setValueStr(Tarikh, tarikh);
	}

	public String getAlamatMajikan() throws Exception {
		return(this.getValueStr(AlamatMajikan));
	}

	public void setAlamatMajikan(String alamatMajikan) throws Exception {
		this.setValueStr(AlamatMajikan, alamatMajikan);
	}

	public String getTaliponMajikan() throws Exception {
		return(this.getValueStr(TaliponMajikan));
	}

	public void setTaliponMajikan(String taliponMajikan) throws Exception {
		this.setValueStr(TaliponMajikan, taliponMajikan);
	}

	public String getNamaPekerja() throws Exception {
		return(this.getValueStr(NamaPekerja));
	}

	public void setNamaPekerja(String namaPekerja) throws Exception {
		this.setValueStr(NamaPekerja, namaPekerja);
	}

	public String getJawatanPekerja() throws Exception {
		return(this.getValueStr(JawatanPekerja));
	}

	public void setJawatanPekerja(String jawatanPekerja) throws Exception {
		this.setValueStr(JawatanPekerja, jawatanPekerja);
	}

	public String getNoKakitangan() throws Exception {
		return(this.getValueStr(NoKakitangan));
	}

	public void setNoKakitangan(String noKakitangan) throws Exception {
		this.setValueStr(NoKakitangan, noKakitangan);
	}

	public String getNoKpBaru() throws Exception {
		return(this.getValueStr(NoKpBaru));
	}

	public void setNoKpBaru(String noKpBaru) throws Exception {
		this.setValueStr(NoKpBaru, noKpBaru);
	}

	public String getNoPassport() throws Exception {
		return(this.getValueStr(NoPassport));
	}

	public void setNoPassport(String noPassport) throws Exception {
		this.setValueStr(NoPassport, noPassport);
	}

	public String getBilanganAnak() throws Exception {
		return(this.getValueStr(BilanganAnak));
	}

	public void setBilanganAnak(String bilanganAnak) throws Exception {
		this.setValueStr(BilanganAnak, bilanganAnak);
	}

	public String getNoCukaiPekerja() throws Exception {
		return(this.getValueStr(NoCukaiPekerja));
	}

	public void setNoCukaiPekerja(String noCukaiPekerja) throws Exception {
		this.setValueStr(NoCukaiPekerja, noCukaiPekerja);
	}

	public String getCawanganLhdn() throws Exception {
		return(this.getValueStr(CawanganLhdn));
	}

	public void setCawanganLhdn(String cawanganLhdn) throws Exception {
		this.setValueStr(CawanganLhdn, cawanganLhdn);
	}

	public String getNoKwsp() throws Exception {
		return(this.getValueStr(NoKwsp));
	}

	public void setNoKwsp(String noKwsp) throws Exception {
		this.setValueStr(NoKwsp, noKwsp);
	}

	public String getNoPerkeso() throws Exception {
		return(this.getValueStr(NoPerkeso));
	}

	public void setNoPerkeso(String noPerkeso) throws Exception {
		this.setValueStr(NoPerkeso, noPerkeso);
	}

	public String getTarikhMulaKerja() throws Exception {
		return(this.getValueStr(TarikhMulaKerja));
	}

	public void setTarikhMulaKerja(String tarikhMulaKerja) throws Exception {
		this.setValueStr(TarikhMulaKerja, tarikhMulaKerja);
	}

	public String getTarikhHentiKerja() throws Exception {
		return(this.getValueStr(TarikhHentiKerja));
	}

	public void setTarikhHentiKerja(String tarikhHentiKerja) throws Exception {
		this.setValueStr(TarikhHentiKerja, tarikhHentiKerja);
	}

	public String getPerihalElaun() throws Exception {
		return(this.getValueStr(PerihalElaun));
	}

	public void setPerihalElaun(String perihalElaun) throws Exception {
		this.setValueStr(PerihalElaun, perihalElaun);
	}

	public String getGajiKasar() throws Exception {
		return(this.getValueStr(GajiKasar));
	}

	public void setGajiKasar(String gajiKasar) throws Exception {
		this.setValueStr(GajiKasar, gajiKasar);
	}

	public String getKomisenBonus() throws Exception {
		return(this.getValueStr(KomisenBonus));
	}

	public void setKomisenBonus(String komisenBonus) throws Exception {
		this.setValueStr(KomisenBonus, komisenBonus);
	}

	public String getElaunLain() throws Exception {
		return(this.getValueStr(ElaunLain));
	}

	public void setElaunLain(String elaunLain) throws Exception {
		this.setValueStr(ElaunLain, elaunLain);
	}

	public String getCukaiMajikanBayar() throws Exception {
		return(this.getValueStr(CukaiMajikanBayar));
	}

	public void setCukaiMajikanBayar(String cukaiMajikanBayar) throws Exception {
		this.setValueStr(CukaiMajikanBayar, cukaiMajikanBayar);
	}

	public void setEsos(String esos) throws Exception {
		this.setValueStr(Esos, esos);
	}

	public String getEsos() throws Exception {
		return(this.getValueStr(Esos));
	}

	public String getGanjaranDari() throws Exception {
		return(this.getValueStr(GanjaranDari));
	}

	public void setGanjaranDari(String ganjaranDari) throws Exception {
		this.setValueStr(GanjaranDari, ganjaranDari);
	}

	public String getGanjaranHingga() throws Exception {
		return(this.getValueStr(GanjaranHingga));
	}

	public void setGanjaranHingga(String ganjaranHingga) throws Exception {
		this.setValueStr(GanjaranHingga, ganjaranHingga);
	}

	public String getGanjaran() throws Exception {
		return(this.getValueStr(Ganjaran));
	}

	public void setGanjaran(String ganjaran) throws Exception {
		this.setValueStr(Ganjaran, ganjaran);
	}

	public String getPendapatanJenisA() throws Exception {
		return(this.getValueStr(PendapatanJenisA));
	}

	public void setPendapatanJenisA(String pendapatanJenisA) throws Exception {
		this.setValueStr(PendapatanJenisA, pendapatanJenisA);
	}

	public String getPendapatanJenisB() throws Exception {
		return(this.getValueStr(PendapatanJenisB));
	}

	public void setPendapatanJenisB(String pendapatanJenisB) throws Exception {
		this.setValueStr(PendapatanJenisB, pendapatanJenisB);
	}

	public String getPendapatanDulu() throws Exception {
		return(this.getValueStr(PendapatanDulu));
	}

	public void setPendapatanDulu(String pendapatanDulu) throws Exception {
		this.setValueStr(PendapatanDulu, pendapatanDulu);
	}

	public String getManfaatNyatakan() throws Exception {
		return(this.getValueStr(ManfaatNyatakan));
	}

	public void setManfaatNyatakan(String manfaatNyatakan) throws Exception {
		this.setValueStr(ManfaatNyatakan, manfaatNyatakan);
	}

	public String getManfaatBarangan() throws Exception {
		return(this.getValueStr(ManfaatBarangan));
	}

	public void setManfaatBarangan(String manfaatBarangan) throws Exception {
		this.setValueStr(ManfaatBarangan, manfaatBarangan);
	}

	public String getAlamatManfaat() throws Exception {
		return(this.getValueStr(AlamatManfaat));
	}

	public void setAlamatManfaat(String alamatManfaat) throws Exception {
		this.setValueStr(AlamatManfaat, alamatManfaat);
	}

	public String getAlamatNilai() throws Exception {
		return(this.getValueStr(AlamatNilai));
	}

	public void setAlamatNilai(String alamatNilai) throws Exception {
		this.setValueStr(AlamatNilai, alamatNilai);
	}

	public String getKwspPencenDiTolak() throws Exception {
		return(this.getValueStr(KwspPencenDiTolak));
	}

	public void setKwspPencenDiTolak(String kwspPencenDiTolak) throws Exception {
		this.setValueStr(KwspPencenDiTolak, kwspPencenDiTolak);
	}

	public String getPampasanHilangKerja() throws Exception {
		return(this.getValueStr(PampasanHilangKerja));
	}

	public void setPampasanHilangKerja(String pampasanHilangKerja) throws Exception {
		this.setValueStr(PampasanHilangKerja, pampasanHilangKerja);
	}

	public String getPencen() throws Exception {
		return(this.getValueStr(Pencen));
	}

	public void setPencen(String pencen) throws Exception {
		this.setValueStr(Pencen, pencen);
	}

	public String getAnuiti() throws Exception {
		return(this.getValueStr(Anuiti));
	}

	public void setAnuiti(String anuiti) throws Exception {
		this.setValueStr(Anuiti, anuiti);
	}

	public String getJumlahPendapatan() throws Exception {
		return(this.getValueStr(JumlahPendapatan));
	}

	public void setJumlahPendapatan(String jumlahPendapatan) throws Exception {
		this.setValueStr(JumlahPendapatan, jumlahPendapatan);
	}

	public String getPotonganPcb() throws Exception {
		return(this.getValueStr(PotonganPcb));
	}

	public void setPotonganPcb(String potonganPcb) throws Exception {
		this.setValueStr(PotonganPcb, potonganPcb);
	}

	public String getPotonganCp38() throws Exception {
		return(this.getValueStr(PotonganCp38));
	}

	public void setPotonganCp38(String potonganCp38) throws Exception {
		this.setValueStr(PotonganCp38, potonganCp38);
	}

	public String getPotonganZakat() throws Exception {
		return(this.getValueStr(PotonganZakat));
	}

	public void setPotonganZakat(String potonganZakat) throws Exception {
		this.setValueStr(PotonganZakat, potonganZakat);
	}

	public String getTp1Pelepasan() throws Exception {
		return(this.getValueStr(Tp1Pelepasan));
	}

	public void setTp1Pelepasan(String potonganZakat) throws Exception {
		this.setValueStr(Tp1Pelepasan, potonganZakat);
	}

	public String getTp1Zakat() throws Exception {
		return(this.getValueStr(Tp1Zakat));
	}

	public void setTp1Zakat(String tp1Zakat) throws Exception {
		this.setValueStr(Tp1Zakat, tp1Zakat);
	}

	public String getPelepasanAnak() throws Exception {
		return(this.getValueStr(PelepasanAnak));
	}

	public void setPelepasanAnak(String pelepasanAnak) throws Exception {
		this.setValueStr(PelepasanAnak, pelepasanAnak);
	}

	public String getNamaKwsp() throws Exception {
		return(this.getValueStr(NamaKwsp));
	}

	public void setNamaKwsp(String namaKwsp) throws Exception {
		this.setValueStr(NamaKwsp, namaKwsp);
	}

	public String getCarumanKwsp() throws Exception {
		return(this.getValueStr(CarumanKwsp));
	}

	public void setCarumanKwsp(String carumanKwsp) throws Exception {
		this.setValueStr(CarumanKwsp, carumanKwsp);
	}

	public String getCarumanPerkeso() throws Exception {
		return(this.getValueStr(CarumanPerkeso));
	}

	public void setCarumanPerkeso(String carumanPerkeso) throws Exception {
		this.setValueStr(CarumanPerkeso, carumanPerkeso);
	}

	public String getManfaatNoCukai() throws Exception {
		return(this.getValueStr(ManfaatNoCukai));
	}

	public void setManfaatNoCukai(String manfaatNoCukai) throws Exception {
		this.setValueStr(ManfaatNoCukai, manfaatNoCukai);
	}

	public MsiaLhdnEaEmailRecord getEmailNotification(Connection aConn) throws Exception {
		return((MsiaLhdnEaEmailRecord) this.getValueObject(aConn, EmailNotification));
	}

	public MsiaLhdnEaEmailRecord gotEmailNotification(Connection aConn) throws Exception {
		return((MsiaLhdnEaEmailRecord) this.gotValueObject(aConn, EmailNotification));
	}

	public void setEmailNotification(MsiaLhdnEaEmailRecord aEmailNotification) throws Exception {
		this.setValueObject(EmailNotification, aEmailNotification);
	}

	public MsiaLhdnPcb2ii getPenyataPcb2ii(Connection aConn) throws Exception {
		return((MsiaLhdnPcb2ii) this.gotValueObject(aConn, PenyataPcb2ii));
	}

	public void setPenyataPcb2ii(MsiaLhdnPcb2ii aPenyataPcb2ii) throws Exception {
		this.setValueObject(PenyataPcb2ii, aPenyataPcb2ii);
	}

	public static void PopulateNewEa(Connection aConn, MsiaLhdnEa borangEa, CompanyMalaysia aCompany, Person aWorker, int aYear, String aNamaPegawai, String aJawatanPegawai) throws Exception {
		// about the boss
		borangEa.setForYear(aYear);
		borangEa.setNoMajikanE(Generic.Null2Blank(aCompany.getLhdnNoMajikanE()));
		borangEa.setNamaMajikan(Generic.Null2Blank(aCompany.getName()));
		borangEa.setNamaPegawai(aNamaPegawai);
		borangEa.setJawatanPegawai(aJawatanPegawai);
		borangEa.setTarikh(DateAndTime.FormatDisplayNoTime(new DateTime()));
		borangEa.setAlamatMajikan(Generic.Null2Blank(aCompany.getPreferedAddr(aConn).getValueStr()));
		borangEa.setTaliponMajikan(Generic.Null2Blank(aCompany.getPreferedTelephone(aConn)));
		borangEa.setNamaKwsp("Kumpulan Wang Simpanan Pekerja");

		// about the worker
		borangEa.setNamaPekerja(Generic.Null2Blank(aWorker.getName()));
		borangEa.setNoKakitangan(Generic.Null2Blank(GetEmployeeNo(aConn, aWorker)));
		borangEa.setNoPassport(Generic.Null2Blank(aWorker.getPassportNo(aConn)));
		borangEa.setBilanganAnak(Generic.Null2Zero(aWorker.getNoOfChild()));
		borangEa.setNoKpBaru(Generic.Null2Blank(aWorker.getIdNoForMsia(aConn)));

		// about the job
		DateTime workerStartDate = null;
		DateTime workerQuitDate = null;
		Money totalTaxable = Money.CreateMoney(aConn, SalarySlipMalaysia.GetDefaultCurrencyCode(aConn));
		Money totalBasic = Money.CreateMoney(aConn, SalarySlipMalaysia.GetDefaultCurrencyCode(aConn));
		Money totalBonus = Money.CreateMoney(aConn, SalarySlipMalaysia.GetDefaultCurrencyCode(aConn));
		Money totalOther = Money.CreateMoney(aConn, SalarySlipMalaysia.GetDefaultCurrencyCode(aConn));
		Money totalEpf = Money.CreateMoney(aConn, SalarySlipMalaysia.GetDefaultCurrencyCode(aConn));
		Money totalSocso = Money.CreateMoney(aConn, SalarySlipMalaysia.GetDefaultCurrencyCode(aConn));
		Money totalPcb = Money.CreateMoney(aConn, SalarySlipMalaysia.GetDefaultCurrencyCode(aConn));
		Money totalCp38 = Money.CreateMoney(aConn, SalarySlipMalaysia.GetDefaultCurrencyCode(aConn));
		Money totalZakat= Money.CreateMoney(aConn, SalarySlipMalaysia.GetDefaultCurrencyCode(aConn));
		aWorker.fetchEmploymentOfCompany(aConn, aCompany); // for this worker, FetchUnique the job held for this company
		aWorker.getEmployment().getMetaField(Employment.StartDate).setSortKey(0, SortOrder.ASC);
		aWorker.getEmployment().sort(); // for multiple employment, we only use the data of the latest employment but total up trx of both employment
		aWorker.getEmployment().resetIterator();
		boolean jobInfoDone = false;
		while (aWorker.getEmployment().hasNext(aConn)) {
			EmploymentMalaysia job = (EmploymentMalaysia) aWorker.getEmployment().getNext();

			if (job.getEndDate() == null || jobInfoDone == false) {
				jobInfoDone = true;
				borangEa.setJawatanPekerja(Generic.Null2Blank(job.getDesignation()));
				borangEa.setNoCukaiPekerja(Generic.Null2Blank(job.getTaxNo()));
				borangEa.setCawanganLhdn(Generic.Null2Blank(job.getTaxBranch()));
				borangEa.setNoKwsp(Generic.Null2Blank(job.getEpfNo()));
				borangEa.setNoPerkeso(Generic.Null2Blank(job.getSocsoNo()));
				workerStartDate = null;
				workerQuitDate = null;
				if (job.getStartDate() != null) workerStartDate = job.getStartDate();
				if (job.getEndDate() != null) workerQuitDate = job.getEndDate();
			}

			//DateTime startOfYear = DateAndTime.CreateDateTime(aYear, 1, 1).minusDays(1);
			//DateTime endOfYear = DateAndTime.CreateDateTime(aYear, 12, 31);
			DateTime startOfYear = DateAndTime.GetYearStart(aYear);
			DateTime endOfYear = DateAndTime.GetYearEnd(aYear);
			String perihalElaun = "";
			job.forEachPayslip(aConn, startOfYear, endOfYear, (Connection bConn, Clasz aPayslip) -> {
				SalarySlipMalaysia payslip = (SalarySlipMalaysia) aPayslip;
				payslip.fetchAllTrx(bConn);

				// total up basic/overtime, bonus/comission and other
				payslip.totalTaxableTrx(bConn, totalBasic, totalBonus, totalOther, perihalElaun);

				// total up epf and socso
				payslip.totalWorkerPartEpfSocsoTrx(bConn, totalEpf, totalSocso);

				// total up cp38, zakat
				payslip.totalPcbCp38Zakat(bConn, totalPcb, totalCp38, totalZakat);

				return true;
			});
		} // for each employment

		totalTaxable.addAmount(totalBasic);
		totalTaxable.addAmount(totalBonus);
		totalTaxable.addAmount(totalOther);

		borangEa.setGajiKasar(totalBasic.getAmountWithComma());
		if (!totalBonus.getValueDouble().equals(0.0)) borangEa.setKomisenBonus(totalBonus.getAmountWithComma());
		if (!totalOther.getValueDouble().equals(0.0)) borangEa.setElaunLain(totalOther.getAmountWithComma());
		if (!totalTaxable.getValueDouble().equals(0.0)) borangEa.setJumlahPendapatan(totalTaxable.getAmountWithComma());
		if (!totalEpf.getValueDouble().equals(0.0)) borangEa.setCarumanKwsp(totalEpf.getAmountWithComma());
		if (!totalSocso.getValueDouble().equals(0.0)) borangEa.setCarumanPerkeso(totalSocso.getAmountWithComma());
		if (!totalPcb.getValueDouble().equals(0.0)) borangEa.setPotonganPcb(totalPcb.getAmountWithComma());
		if (!totalCp38.getValueDouble().equals(0.0)) borangEa.setPotonganCp38(totalCp38.getAmountWithComma());
		if (!totalZakat.getValueDouble().equals(0.0)) borangEa.setPotonganZakat(totalZakat.getAmountWithComma());

		borangEa.setTarikhMulaKerja("");
		borangEa.setTarikhHentiKerja("");
		if (workerQuitDate != null) {
			int forYear = workerQuitDate.getYear();
			if (aYear == forYear) {
				if (workerStartDate != null) borangEa.setTarikhMulaKerja(Generic.Null2Blank(DateAndTime.FormatDisplayNoTime(workerStartDate)));
				borangEa.setTarikhHentiKerja(Generic.Null2Blank(DateAndTime.FormatDisplayNoTime(workerQuitDate)));
			}
		}

		MsiaLhdnPcb2ii pcb2ii = (MsiaLhdnPcb2ii) ObjectBase.CreateObject(aConn, MsiaLhdnPcb2ii.class);
		borangEa.setPenyataPcb2ii(pcb2ii);
		MsiaLhdnPcb2ii.PopulateNewPcb2ii(aConn, borangEa.getPenyataPcb2ii(aConn), aCompany, aWorker, aYear, aNamaPegawai, aJawatanPegawai);
	}


	/**
	 * Use ot populate xhtml generated by pdftohtml for OpenHTMLtoPDF
	 * 
	 * @param aConn
	 * @param borangEa
	 * @param aHtmlDoc
	 * @return
	 * @throws Exception 
	 */
	public static String PopulateEa(Connection aConn, MsiaLhdnEa borangEa, Document aHtmlDoc) throws Exception {
		ReportHtml.SetReportText(aHtmlDoc, "wt_No_Siri", borangEa.getNoSiri());
		ReportHtml.SetReportText(aHtmlDoc, "wt_No_Cukai_Pekerja", borangEa.getNoCukaiPekerja());
		ReportHtml.SetReportText(aHtmlDoc, "wt_No_Majikan_E", borangEa.getNoMajikanE());
		ReportHtml.SetReportText(aHtmlDoc, "wt_For_Year", borangEa.getForYear().toString());
		ReportHtml.SetReportText(aHtmlDoc, "wt_Cawangan_Lhdn", borangEa.getCawanganLhdn());

		ReportHtml.SetReportText(aHtmlDoc, "wt_Tarikh", borangEa.getTarikh());
		ReportHtml.SetReportText(aHtmlDoc, "wt_Nama_Pekerja", borangEa.getNamaPekerja());
		ReportHtml.SetReportText(aHtmlDoc, "wt_Jawatan_Pekerja", borangEa.getJawatanPekerja());
		ReportHtml.SetReportText(aHtmlDoc, "wt_No_Kakitangan", borangEa.getNoKakitangan());
		ReportHtml.SetReportText(aHtmlDoc, "wt_No_Kp_Baru", borangEa.getNoKpBaru());
		ReportHtml.SetReportText(aHtmlDoc, "wt_No_Passport", borangEa.getNoPassport());
		ReportHtml.SetReportText(aHtmlDoc, "wt_Bilangan_Anak", borangEa.getBilanganAnak());

		ReportHtml.SetReportText(aHtmlDoc, "wt_No_Kwsp", borangEa.getNoKwsp());
		ReportHtml.SetReportText(aHtmlDoc, "wt_No_Perkeso", borangEa.getNoPerkeso());
		ReportHtml.SetReportText(aHtmlDoc, "wt_Tarikh_Mula_Kerja", borangEa.getTarikhMulaKerja());
		ReportHtml.SetReportText(aHtmlDoc, "wt_Tarikh_Henti_Kerja", borangEa.getTarikhHentiKerja());

		ReportHtml.SetReportText(aHtmlDoc, "wt_Gaji_Kasar", Money.GetAmountWithComma(borangEa.getGajiKasar()));
		ReportHtml.SetReportText(aHtmlDoc, "wt_Komisen_Bonus", Money.GetAmountWithComma(borangEa.getKomisenBonus()));
		ReportHtml.SetReportText(aHtmlDoc, "wt_Perihal_Elaun", Generic.Substr(borangEa.getPerihalElaun(), 0, 18));
		ReportHtml.SetReportText(aHtmlDoc, "wt_Elaun_Lain", Money.GetAmountWithComma(borangEa.getElaunLain()));
		ReportHtml.SetReportText(aHtmlDoc, "wt_Cukai_Majikan_Bayar", Money.GetAmountWithComma(borangEa.getCukaiMajikanBayar()));
		ReportHtml.SetReportText(aHtmlDoc, "wt_Esos", Money.GetAmountWithComma(borangEa.getEsos()));
		ReportHtml.SetReportText(aHtmlDoc, "wt_Ganjaran_Dari", borangEa.getGanjaranDari());
		ReportHtml.SetReportText(aHtmlDoc, "wt_Ganjaran_Hingga", borangEa.getGanjaranHingga());
		ReportHtml.SetReportText(aHtmlDoc, "wt_Ganjaran", Money.GetAmountWithComma(borangEa.getGanjaran()));
		ReportHtml.SetReportText(aHtmlDoc, "wt_Pendapatan_Jenis_A", borangEa.getPendapatanJenisA());
		ReportHtml.SetReportText(aHtmlDoc, "wt_Pendapatan_Jenis_B", borangEa.getPendapatanJenisB());
		ReportHtml.SetReportText(aHtmlDoc, "wt_Pendapatan_Dulu", Money.GetAmountWithComma(borangEa.getPendapatanDulu()));
		ReportHtml.SetReportText(aHtmlDoc, "wt_Manfaat_Nyatakan", borangEa.getManfaatNyatakan());
		ReportHtml.SetReportText(aHtmlDoc, "wt_Manfaat_Barangan", Money.GetAmountWithComma(borangEa.getManfaatBarangan()));
		ReportHtml.SetReportText(aHtmlDoc, "wt_Alamat_Manfaat", borangEa.getAlamatManfaat());
		ReportHtml.SetReportText(aHtmlDoc, "wt_Alamat_Nilai", Money.GetAmountWithComma(borangEa.getAlamatNilai()));
		ReportHtml.SetReportText(aHtmlDoc, "wt_Kwsp_Pencen_Di_Tolak", Money.GetAmountWithComma(borangEa.getKwspPencenDiTolak()));
		ReportHtml.SetReportText(aHtmlDoc, "wt_Pampasan_Hilang_Kerja", Money.GetAmountWithComma(borangEa.getPampasanHilangKerja()));
		ReportHtml.SetReportText(aHtmlDoc, "wt_Pencen", Money.GetAmountWithComma(borangEa.getPencen()));
		ReportHtml.SetReportText(aHtmlDoc, "wt_Anuiti", Money.GetAmountWithComma(borangEa.getAnuiti()));
		ReportHtml.SetReportText(aHtmlDoc, "wt_Jumlah_Pendapatan", Money.GetAmountWithComma(borangEa.getJumlahPendapatan()));
		ReportHtml.SetReportText(aHtmlDoc, "wt_Potongan_Pcb", Money.GetAmountWithComma(borangEa.getPotonganPcb()));
		ReportHtml.SetReportText(aHtmlDoc, "wt_Potongan_Cp38", Money.GetAmountWithComma(borangEa.getPotonganCp38()));
		ReportHtml.SetReportText(aHtmlDoc, "wt_Potongan_Zakat", Money.GetAmountWithComma(borangEa.getPotonganZakat()));
		ReportHtml.SetReportText(aHtmlDoc, "wt_Tp1_Pelepasan", Money.GetAmountWithComma(borangEa.getTp1Pelepasan()));
		ReportHtml.SetReportText(aHtmlDoc, "wt_Tp1_Zakat", Money.GetAmountWithComma(borangEa.getTp1Zakat()));
		ReportHtml.SetReportText(aHtmlDoc, "wt_Pelepasan_Anak", Money.GetAmountWithComma(borangEa.getPelepasanAnak()));

		ReportHtml.SetReportText(aHtmlDoc, "wt_Nama_Kwsp", borangEa.getNamaKwsp());
		ReportHtml.SetReportText(aHtmlDoc, "wt_Caruman_Kwsp", Money.GetAmountWithComma(borangEa.getCarumanKwsp()));
		ReportHtml.SetReportText(aHtmlDoc, "wt_Caruman_Perkeso", Money.GetAmountWithComma(borangEa.getCarumanPerkeso()));
		ReportHtml.SetReportText(aHtmlDoc, "wt_Manfaat_No_Cukai", Money.GetAmountWithComma(borangEa.getManfaatNoCukai()));
		ReportHtml.SetReportText(aHtmlDoc, "wt_Nama_Pegawai", borangEa.getNamaPegawai());
		ReportHtml.SetReportText(aHtmlDoc, "wt_Jawatan_Pegawai", borangEa.getJawatanPegawai());
		ReportHtml.SetReportText(aHtmlDoc, "wt_Nama_Majikan", borangEa.getNamaMajikan());
		ReportHtml.SetReportText(aHtmlDoc, "wt_Alamat_Majikan", borangEa.getAlamatMajikan());
		ReportHtml.SetReportText(aHtmlDoc, "wt_Talipon_Majikan", borangEa.getTaliponMajikan());

		borangEa.getPenyataPcb2ii(aConn).getPcbPotonganSemasa().fetchAll(aConn);
		borangEa.getPenyataPcb2ii(aConn).getPcbPotonganDahulu().fetchAll(aConn);
		MsiaLhdnPcb2ii.PopulatePcb2ii(aConn, borangEa.getPenyataPcb2ii(aConn), aHtmlDoc);
		return aHtmlDoc.toString();
	}

	public static String GetEmployeeNo(Connection aConn, Person employee) throws Exception {
		String result;
		String identityNo;
		String kodNegara;
		if (employee.isMalaysiaCitizen(aConn)) {
			MalaysiaIdentityCard msiaIc = employee.getMalaysiaIc(aConn);
			identityNo = msiaIc.getNewIdentityCardNo();
			kodNegara = Country.Malaysia.getCode();
		} else if (employee.isMalaysiaPr(aConn)) {
			MalaysiaPermanentResident msiaPr = employee.getMalaysiaPr(aConn);
			identityNo = msiaPr.getPermanentResidentNo();
			kodNegara = Country.Malaysia.getCode();
		} else {
			Passport passport = employee.getPassport(aConn);
			identityNo = passport.getPassportNo();
			kodNegara = ((Identity) passport).getIssuingCountry();
			if (kodNegara.length() > 3) {
				kodNegara = kodNegara.substring(0, 3);
			}
			kodNegara = kodNegara.toUpperCase();
		}
		result = kodNegara + identityNo;
		return result;
	}


	public static int GetLatestYear(Connection aConn, Company aCompany) throws Exception {
		MsiaLhdnEa typeBorangEa = (MsiaLhdnEa) ObjectBase.CreateObject(aConn, MsiaLhdnEa.class);
		Record recWhere = new Record();
		Field fieldName = typeBorangEa.getField(MsiaLhdnEa.CompanyName);
		recWhere.createField(fieldName.getFieldName(), fieldName.getFieldType(), fieldName.getFieldSize());
		recWhere.getField(fieldName.getFieldName()).setValueStr(aCompany.getName());

		Field fieldAlias = typeBorangEa.getField(MsiaLhdnEa.CompanyAlias);
		recWhere.createField(fieldAlias.getFieldName(), FieldType.STRING, fieldAlias.getFieldSize());
		recWhere.getField(fieldAlias.getFieldName()).setValueStr(aCompany.getAlias());

		MsiaLhdnEa latestEa = (MsiaLhdnEa) Clasz.FetchUnique(aConn, typeBorangEa, recWhere, MsiaLhdnEa.ForYear, SortOrder.DSC);
		int latestYear;
		if (latestEa != null) {
			latestYear = latestEa.getForYear();
		} else {
			latestYear = Calendar.getInstance().get(Calendar.YEAR);
		}
		return(latestYear);
	}

	public static String GetEaFormHtml() throws Exception {
		String urlPath = MsiaLhdnEa.class.getClassLoader().getResource(HTML_RESOURCE).getPath();
		Document htmlDoc = ReportHtml.CreateHtmlDoc(urlPath);
		return(htmlDoc.html());
	}

	//public static final int TEST_CASE = 0;
	public static final int TEST_CASE = 1;
	public static void main(String[] args) throws Exception {
		if (TEST_CASE == 0) {
			Document htmlDoc = ReportHtml.CreateHtmlDoc(HTML_FILE);
			System.out.println(htmlDoc.toString());
			PdfEngine.GeneratePdf(PDF_FILE, htmlDoc.toString(), "file:///" + HTML_PATH);
		} else {
			ObjectBase objectDb = new ObjectBase();
			Connection conn = null;
			try {
				String urlPath = MsiaLhdnEa.class.getClassLoader().getResource(HTML_RESOURCE).getPath();
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
						MsiaLhdnEa generatedEa = (MsiaLhdnEa) ObjectBase.CreateObject(conn, MsiaLhdnEa.class);
						MsiaLhdnEa.PopulateNewEa(conn, generatedEa, company, worker, 2017, "KOH MEE SIN", "HUMAN RESOURCE DIRECTOR");
						//PdfEngine.GeneratePdf(PDF_FILE, htmlEa, "file:///" + urlPath);
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

	public static List<MsiaLhdnEa> FetchEmployeeLatestEaList(Connection aConn, Company aCompany, Person aWorker, int aNumberOfEa2Fetch) throws Exception {
		final int maxPayslip = aNumberOfEa2Fetch;

		MsiaLhdnEa eaForm = (MsiaLhdnEa) ObjectBase.CreateObject(aConn, MsiaLhdnEa.class);
		FieldObjectBox fobEaForm = new FieldObjectBox(eaForm);
		eaForm.setCompanyName(aCompany.getName());
		eaForm.setCompanyAlias(aCompany.getAlias());
		eaForm.setEmployeeName(aWorker.getName());
		eaForm.setEmployeeAlias(aWorker.getAlias());

		Record recWhere = new Record();
		Clasz.SetWhereField(recWhere, eaForm.getField(MsiaLhdnEa.CompanyName));
		Clasz.SetWhereField(recWhere, eaForm.getField(MsiaLhdnEa.CompanyAlias));
		Clasz.SetWhereField(recWhere, eaForm.getField(MsiaLhdnEa.EmployeeName));
		Clasz.SetWhereField(recWhere, eaForm.getField(MsiaLhdnEa.EmployeeAlias));

		List<String> keyField = new CopyOnWriteArrayList<>();
		keyField.add(MsiaLhdnEa.ForYear);
		List<String> keyValue = new CopyOnWriteArrayList<>();
		keyValue.add("");

		Clasz.FetchBySection(aConn, MsiaLhdnEa.class, keyField, keyValue, SortOrder.DSC, recWhere, fobEaForm, maxPayslip);

		List<MsiaLhdnEa> result = new CopyOnWriteArrayList<>();
		fobEaForm.resetIterator();
		while (fobEaForm.hasNext(aConn)) {
			result.add((MsiaLhdnEa) fobEaForm.getNext());
		}
		return(result);
	}
}
