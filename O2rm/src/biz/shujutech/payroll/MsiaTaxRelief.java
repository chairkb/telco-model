package biz.shujutech.payroll;

import biz.shujutech.base.App;
import biz.shujutech.base.Connection;
import biz.shujutech.base.Hinderance;
import biz.shujutech.bznes.Country;
import biz.shujutech.bznes.Marital;
import biz.shujutech.bznes.Money;
import biz.shujutech.util.Generic;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class MsiaTaxRelief {
	private String code;
	private double limitOrRate;
	private String desc;
	private double entitle;

	MsiaTaxRelief(String aCode, double aLimit, String aDesc, double aEntitle) {
		this.code = aCode;
		this.limitOrRate = aLimit;
		this.desc = aDesc;
		this.entitle = aEntitle;
	}
	
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public double getLimitOrRate() {
		return limitOrRate;
	}

	public void setLimitOrRate(double limitOrRate) {
		this.limitOrRate = limitOrRate;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public double getEntitle() {
		return entitle;
	}

	public void setEntitle(double entitle) {
		this.entitle = entitle;
	}
	
	public static List<MsiaTaxRelief> CreateReliefList(Integer aForYear) throws Exception {
		if (aForYear >= 2019) {
			List<MsiaTaxRelief> reliefList = new CopyOnWriteArrayList<>();
			reliefList.add(new MsiaTaxRelief("self", 9000D, "", 9000D)); // automatically eligible
			reliefList.add(new MsiaTaxRelief("parents_medical", 5000D, "", 0D));
			reliefList.add(new MsiaTaxRelief("father_relief", 1500D, "", 0D));
			reliefList.add(new MsiaTaxRelief("mother_relief", 1500D, "", 0D));
			reliefList.add(new MsiaTaxRelief("equip_for_disabled", 6000D, "", 0D));
			reliefList.add(new MsiaTaxRelief("disabled_indv", 6000D, "", 0D));
			reliefList.add(new MsiaTaxRelief("self_educat", 7000D, "", 0D));
			reliefList.add(new MsiaTaxRelief("medical_expenses", 6000D, "", 0D));
			reliefList.add(new MsiaTaxRelief("lifestyle", 2500D, "", 0D));
			reliefList.add(new MsiaTaxRelief("feeding_equip", 1000D, "", 0D));
			reliefList.add(new MsiaTaxRelief("sspn_scheme", 8000D, "", 0D));
			reliefList.add(new MsiaTaxRelief("child_care", 1000D, "", 0D));
			reliefList.add(new MsiaTaxRelief("alimony", 4000D, "", 0D));
			reliefList.add(new MsiaTaxRelief("disable_spouse", 3500D, "", 0D));
			reliefList.add(new MsiaTaxRelief("child_relief", 2000D, "", 0D));
			reliefList.add(new MsiaTaxRelief("child_matriculaton", 2000D, "", 0D));
			reliefList.add(new MsiaTaxRelief("child_diploma", 8000D, "", 0D));
			reliefList.add(new MsiaTaxRelief("disabled_child", 6000D, "", 0D));
			reliefList.add(new MsiaTaxRelief("disabled_child_diploma", 8000D, "", 0D));
			reliefList.add(new MsiaTaxRelief("epf", 4000D, "", 0D));
			reliefList.add(new MsiaTaxRelief("life_insurance", 3000D, "", 0D));
			reliefList.add(new MsiaTaxRelief("prs", 3000D, "", 0D));
			reliefList.add(new MsiaTaxRelief("insure_educat_medical", 3000D, "", 0D));
			reliefList.add(new MsiaTaxRelief("socso", 250D, "", 0D));
			return(reliefList);
		}

		throw new Hinderance("Fail to create tax relief list for year: " + aForYear);
	}

	public static double TotalUp2Str(String aStr1, String aStr2) throws Exception {
		double dbl1 = Generic.String2DoubleOrZero(aStr1);
		double dbl2 = Generic.String2DoubleOrZero(aStr2);
		return(dbl1 + dbl2);
	}

	public static void SetEligibleRelief(Connection aConn, List<MsiaTaxRelief> aReliefList, MsiaLhdnPcbTp1 pcbTp1, MsiaLhdnPcbTp3 pcbTp3, Money aPaidEpf, Money aPaidSocso, Marital aMarital, Boolean aSpouseWorking, int aTotalKid) throws Exception {
		MsiaTaxRelief epfRelief = GetRelief(aReliefList, "epf");
		double epfLimit = epfRelief.getLimitOrRate();
		if (aPaidEpf.getValueDouble() > epfLimit) {
			epfRelief.setEntitle(epfLimit);
		} else {
			epfRelief.setEntitle(aPaidEpf.getValueDouble());
		}
		
		MsiaTaxRelief socsoRelief = GetRelief(aReliefList, "socso");
		double socsoLimit = socsoRelief.getLimitOrRate();
		if (aPaidSocso.getValueDouble() > socsoLimit) {
			socsoRelief.setEntitle(socsoLimit);
		} else {
			socsoRelief.setEntitle(aPaidSocso.getValueDouble());
		}

		if (aTotalKid > 0) {
			MsiaTaxRelief childRelief = GetRelief(aReliefList, "child_relief");
			Double reliefAmount = childRelief.getLimitOrRate() * aTotalKid;
			childRelief.setEntitle(reliefAmount);
		}

		if (pcbTp1 != null) {
			App.logInfo(MsiaTaxRelief.class, "Using TP1 for relief, company: " + pcbTp1.getCompanyName() + " - " + pcbTp1.getCompanyAlias() 
			+ ", employee: " + pcbTp1.getEmployeeName() + " - " + pcbTp1.getEmployeeAlias()
			+ ", year: " + pcbTp1.getTahunPotongan() + ", month: " + pcbTp1.getBulanPotongan());
			SetEligibleClaim(aReliefList, "parents_medical", TotalUp2Str(pcbTp1.getC1RawatanIbuBapa(), pcbTp1.getC1RawatanIbuBapaSemasa()));
			//SetEligibleClaim(aReliefList, "father_relief", TotalUp2Str(pcbTp1.getC1bPelepasanBapa(), pcbTp1.getC1bPelepasanBapaSemasa()));
			//SetEligibleClaim(aReliefList, "mother_relief", TotalUp2Str(pcbTp1.getC1bPelepasanIbu(), pcbTp1.getC1bPelepasanIbuSemasa()));
			SetEligibleClaim(aReliefList, "equip_for_disabled", TotalUp2Str(pcbTp1.getC2AlatKurangUpaya(), pcbTp1.getC2AlatKurangUpayaSemasa()));
			SetEligibleClaim(aReliefList, "self_educat", TotalUp2Str(pcbTp1.getC3YuranPendidikan(), pcbTp1.getC3YuranPendidikanSemasa()));

			double totalC4C5 = Generic.String2DoubleOrZero(pcbTp1.getC4SukarDiUbati()) + Generic.String2DoubleOrZero(pcbTp1.getC5PeriksaKesihatan());
			totalC4C5 += Generic.String2DoubleOrZero(pcbTp1.getC4SukarDiUbatiSemasa()) + Generic.String2DoubleOrZero(pcbTp1.getC5PeriksaKesihatanSemasa());
			SetEligibleClaim(aReliefList, "medical_expenses", (new Double(totalC4C5)).toString());

			SetEligibleClaim(aReliefList, "sspn_scheme", TotalUp2Str(pcbTp1.getC6SSPN(), pcbTp1.getC6SSPNSemasa()));
			SetEligibleClaim(aReliefList, "alimony", TotalUp2Str(pcbTp1.getC7Alimoni(), pcbTp1.getC7AlimoniSemasa()));
			SetEligibleClaim(aReliefList, "insure_educat_medical", TotalUp2Str(pcbTp1.getC9InsuranPendidikanNyawa(), pcbTp1.getC9InsuranPendidikanNyawaSemasa()));
			SetEligibleClaim(aReliefList, "prs", TotalUp2Str(pcbTp1.getC10SkimPersaraan(), pcbTp1.getC10SkimPersaraanSemasa()));
			SetEligibleClaim(aReliefList, "socso", TotalUp2Str(pcbTp1.getC11Perkeso(), pcbTp1.getC11PerkesoSemasa()));
			SetEligibleClaim(aReliefList, "lifestyle", TotalUp2Str(pcbTp1.getC12CaraHidup(), pcbTp1.getC12CaraHidupSemasa()));
			SetEligibleClaim(aReliefList, "feeding_equip", TotalUp2Str(pcbTp1.getC13AlatPenyusuan(), pcbTp1.getC13AlatPenyusuanSemasa()));
			SetEligibleClaim(aReliefList, "child_care", TotalUp2Str(pcbTp1.getC14YuranAsuhan(), pcbTp1.getC14YuranAsuhanSemasa()));
		} else if (pcbTp3 != null) {
			App.logInfo(MsiaTaxRelief.class, "No TP1, found TP3, using TP3 for relief calculation"); 
			SetEligibleClaim(aReliefList, "parents_medical", pcbTp3.getD1RawatanPerubatan());
			SetEligibleClaim(aReliefList, "father_relief", pcbTp3.getD1bPelepasanBapa());
			SetEligibleClaim(aReliefList, "mother_relief", pcbTp3.getD1bPelepasanIbu());
			SetEligibleClaim(aReliefList, "equip_for_disabled", pcbTp3.getD2AlatKurangUpaya());
			SetEligibleClaim(aReliefList, "self_educat", pcbTp3.getD3YuranPendidikan());

			double totalD4D5 = Generic.String2DoubleOrZero(pcbTp3.getD4PenyakitSukarUbati()) + Generic.String2DoubleOrZero(pcbTp3.getD5PemeriksaanKesihatan());
			SetEligibleClaim(aReliefList, "medical_expenses", (new Double(totalD4D5)).toString());

			SetEligibleClaim(aReliefList, "sspn_scheme", pcbTp3.getD6Sspn());
			SetEligibleClaim(aReliefList, "alimony", pcbTp3.getD7AlimoniIsteri());
			//SetEligibleClaim(aReliefList, "", pcbTp3.getD8aInsuranNyawaPencen());
			//SetEligibleClaim(aReliefList, "", pcbTp3.getD8bInsuranNyawaBukanPencen());
			SetEligibleClaim(aReliefList, "insure_educat_medical", pcbTp3.getD9InsuranDidikUbat());
			SetEligibleClaim(aReliefList, "prs", pcbTp3.getD10SkimAnuiti());
			SetEligibleClaim(aReliefList, "socso", pcbTp3.getD11Perkeso());
			SetEligibleClaim(aReliefList, "lifestyle", pcbTp3.getD12GayaHidup());
			SetEligibleClaim(aReliefList, "feeding_equip", pcbTp3.getD13AlatSusu());
			SetEligibleClaim(aReliefList, "child_care", pcbTp3.getD14PusatAsuhan());
		}
	}

	private static void SetEligibleClaim(List<MsiaTaxRelief> aReliefList, String aReliefName, String aClaimAmount) throws Exception {
		double reliefClaim = Generic.String2DoubleOrZero(aClaimAmount);
		SetEligibleClaim(aReliefList, aReliefName, reliefClaim);
	}

	private static void SetEligibleClaim(List<MsiaTaxRelief> aReliefList, String aReliefName, double reliefClaim) throws Exception {
		MsiaTaxRelief theRelief = GetRelief(aReliefList, aReliefName);
		double reliefLimit = theRelief.getLimitOrRate();
		if (reliefClaim > reliefLimit) {
			theRelief.setEntitle(reliefLimit);
		} else {
			theRelief.setEntitle(reliefClaim);
		}
	}

	public static MsiaTaxRelief GetRelief(List<MsiaTaxRelief> aReliefList, String aCode) throws Exception {
		for(MsiaTaxRelief eachRelief : aReliefList) {
			if (eachRelief.getCode().equals(aCode)) {
				return(eachRelief);
			}
		}
		throw new Hinderance("Error, for Msia, no such tax relief for code: " + aCode);
	}

	public static Money TotalReliefAmount(Connection aConn, List<MsiaTaxRelief> aReliefList) throws Exception {
		Money result = Money.CreateMoney(aConn, Country.Malaysia.getCurrencyCode());
		for(MsiaTaxRelief eachRelief : aReliefList) {
			if (eachRelief.getEntitle() > 0D) {
				Money eachReliefAmount = Money.CreateMoney(aConn, Country.Malaysia.getCurrencyCode());
				eachReliefAmount.setAmount(eachRelief.getEntitle());
				result.addAmount(eachReliefAmount);
			}
		}
		return(result);
	}
}

