package biz.shujutech.payroll;

import biz.shujutech.base.Connection;
import biz.shujutech.base.Hinderance;
import biz.shujutech.bznes.Contact;
import biz.shujutech.bznes.Country;
import biz.shujutech.bznes.Money;
import biz.shujutech.db.object.Clasz;
import biz.shujutech.db.object.ObjectBase;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;
import org.joda.time.DateTime;


public class SalarySlipMalaysia extends SalarySlip {

	public static final String EPF_EMPLOYEE = "Employee EPF";
	public static final String EPF_EMPLOYER = "Employer EPF";
	public static final String SOCSO_EMPLOYEE = "Employee Socso";
	public static final String SOCSO_EMPLOYER = "Employer Socso";
	public static final String TAX = "PCB Deduction";
	public static final String EIS_EMPLOYEE = "Employee EIS";
	public static final String EIS_EMPLOYER = "Employer EIS";

	public static final String CP38 = "CP38";
	public static final String ZAKAT = "Zakat";

	public static final String AFFECT_TAX = "AFFECT_TAX";
	public static final String AFFECT_EPF = "AFFECT_EPF";
	public static final String AFFECT_SOCSO = "AFFECT_SOCSO";
	public static final String AFFECT_EIS = "AFFECT_EIS";
	
	@Override
	public SalaryTransactionMalaysia createBasicSalary(Connection aConn, Contact aPaidBy, Contact aPaidTo, String aTrxName, Money aTrxAmount, DateTime aTrxDate) throws Exception {
		SalaryTransactionMalaysia salaryTrx = (SalaryTransactionMalaysia) this.addEarningTrx(aConn, aTrxName, aTrxAmount);
		salaryTrx.setCreateDate(new DateTime());
		salaryTrx.setTransactionDate(aTrxDate);
		salaryTrx.setPaidFrom(aPaidBy);
		salaryTrx.setPaidTo(aPaidTo);
		salaryTrx.setSystemGenerated(true);
		SetAffect(salaryTrx);
		return(salaryTrx);
	}

	public SalaryTransactionMalaysia createEpfWorkerTrx(Connection aConn, Contact aPaidBy, Contact aPaidTo, String aTrxName, Money aTrxAmount, DateTime aTrxDate) throws Exception {
		SalaryTransactionMalaysia salaryTrx = (SalaryTransactionMalaysia) this.addDeductionTrx(aConn, aTrxName, aTrxAmount);
		salaryTrx.setCreateDate(new DateTime());
		salaryTrx.setTransactionDate(aTrxDate);
		salaryTrx.setPaidFrom(aPaidBy);
		salaryTrx.setPaidTo(aPaidTo);
		salaryTrx.setSystemGenerated(true);
		SetAffect(salaryTrx);
		return(salaryTrx);
	}

	public SalaryTransactionMalaysia createEpfBossTrx(Connection aConn, Contact aPaidBy, Contact aPaidTo, String aTrxName, Money aTrxAmount, DateTime aTrxDate) throws Exception {
		SalaryTransactionMalaysia salaryTrx = (SalaryTransactionMalaysia) this.addOtherTrx(aConn, aTrxName, aTrxAmount);
		salaryTrx.setCreateDate(new DateTime());
		salaryTrx.setTransactionDate(aTrxDate);
		salaryTrx.setPaidFrom(aPaidBy);
		salaryTrx.setPaidTo(aPaidTo);
		salaryTrx.setSystemGenerated(true);
		SetAffect(salaryTrx);
		return(salaryTrx);
	}
	
	public SalaryTransactionMalaysia createSocsoWorkerTrx(Connection aConn, Contact aPaidBy, Contact aPaidTo, String aTrxName, Money aTrxAmount, DateTime aTrxDate) throws Exception {
		SalaryTransactionMalaysia salaryTrx = (SalaryTransactionMalaysia) this.addDeductionTrx(aConn, aTrxName, aTrxAmount);
		salaryTrx.setCreateDate(new DateTime());
		salaryTrx.setTransactionDate(aTrxDate);
		salaryTrx.setPaidFrom(aPaidBy);
		salaryTrx.setPaidTo(aPaidTo);
		salaryTrx.setSystemGenerated(true);
		SetAffect(salaryTrx);
		return(salaryTrx);
	}

	public SalaryTransactionMalaysia createSocsoBossTrx(Connection aConn, Contact aPaidBy, Contact aPaidTo, String aTrxName, Money aTrxAmount, DateTime aTrxDate) throws Exception {
		SalaryTransactionMalaysia salaryTrx = (SalaryTransactionMalaysia) this.addOtherTrx(aConn, aTrxName, aTrxAmount);
		salaryTrx.setCreateDate(new DateTime());
		salaryTrx.setTransactionDate(aTrxDate);
		salaryTrx.setPaidFrom(aPaidBy);
		salaryTrx.setPaidTo(aPaidTo);
		salaryTrx.setSystemGenerated(true);
		SetAffect(salaryTrx);
		return(salaryTrx);
	}

	public SalaryTransactionMalaysia createEisWorkerTrx(Connection aConn, Contact aPaidBy, Contact aPaidTo, String aTrxName, Money aTrxAmount, DateTime aTrxDate) throws Exception {
		SalaryTransactionMalaysia salaryTrx = (SalaryTransactionMalaysia) this.addDeductionTrx(aConn, aTrxName, aTrxAmount);
		salaryTrx.setCreateDate(new DateTime());
		salaryTrx.setTransactionDate(aTrxDate);
		salaryTrx.setPaidFrom(aPaidBy);
		salaryTrx.setPaidTo(aPaidTo);
		salaryTrx.setSystemGenerated(true);
		SetAffect(salaryTrx);
		return(salaryTrx);
	}

	public SalaryTransactionMalaysia createEisBossTrx(Connection aConn, Contact aPaidBy, Contact aPaidTo, String aTrxName, Money aTrxAmount, DateTime aTrxDate) throws Exception {
		SalaryTransactionMalaysia salaryTrx = (SalaryTransactionMalaysia) this.addOtherTrx(aConn, aTrxName, aTrxAmount);
		salaryTrx.setCreateDate(new DateTime());
		salaryTrx.setTransactionDate(aTrxDate);
		salaryTrx.setPaidFrom(aPaidBy);
		salaryTrx.setPaidTo(aPaidTo);
		salaryTrx.setSystemGenerated(true);
		SetAffect(salaryTrx);
		return(salaryTrx);
	}

	public SalaryTransactionMalaysia createTaxTrx(Connection aConn, Contact aPaidBy, Contact aPaidTo, String aTrxName, Money aTrxAmount, DateTime aTrxDate) throws Exception {
		SalaryTransactionMalaysia salaryTrx = (SalaryTransactionMalaysia) this.addDeductionTrx(aConn, aTrxName, aTrxAmount);
		salaryTrx.setCreateDate(new DateTime());
		salaryTrx.setTransactionDate(aTrxDate);
		salaryTrx.setPaidFrom(aPaidBy);
		salaryTrx.setPaidTo(aPaidTo);
		salaryTrx.setSystemGenerated(true);
		SetAffect(salaryTrx);
		return(salaryTrx);
	}

	public static void SetAffect(SalaryTransaction aTrx) throws Exception {
		SalaryTransactionMalaysia salaryTrx = (SalaryTransactionMalaysia) aTrx;
		if (salaryTrx.getDescr().contains(TAX)) {
			salaryTrx.isAffectEpf(false);
			salaryTrx.isAffectEis(false);
			salaryTrx.isAffectTax(false);
			salaryTrx.isAffectEis(false);
		} else if (salaryTrx.getDescr().contains(EPF_EMPLOYER)) {
			salaryTrx.isAffectEpf(false);
			salaryTrx.isAffectSocso(false);
			salaryTrx.isAffectTax(false);
			salaryTrx.isAffectEis(false);
		} else if (salaryTrx.getDescr().contains(EPF_EMPLOYEE)) {
			salaryTrx.isAffectEpf(false);
			salaryTrx.isAffectSocso(false);
			salaryTrx.isAffectTax(false);
			salaryTrx.isAffectEis(false);
		} else if (salaryTrx.getDescr().contains(EIS_EMPLOYER)) {
			salaryTrx.isAffectEpf(false);
			salaryTrx.isAffectEis(false);
			salaryTrx.isAffectTax(false);
			salaryTrx.isAffectEis(false);
		} else if (salaryTrx.getDescr().contains(EIS_EMPLOYEE)) {
			salaryTrx.isAffectEpf(false);
			salaryTrx.isAffectEis(false);
			salaryTrx.isAffectTax(false);
			salaryTrx.isAffectEis(false);
		} else if (salaryTrx.getDescr().contains(SOCSO_EMPLOYER)) {
			salaryTrx.isAffectEpf(false);
			salaryTrx.isAffectSocso(false);
			salaryTrx.isAffectTax(false);
			salaryTrx.isAffectEis(false);
		} else if (salaryTrx.getDescr().contains(SOCSO_EMPLOYEE)) {
			salaryTrx.isAffectEpf(false);
			salaryTrx.isAffectSocso(false);
			salaryTrx.isAffectTax(false);
			salaryTrx.isAffectEis(false);
		} else if (salaryTrx.getDescr().contains(SalaryTransaction.BASIC_SALARY)) {
			salaryTrx.isAffectEpf(true);
			salaryTrx.isAffectSocso(true);
			salaryTrx.isAffectTax(true);
			salaryTrx.isAffectEis(true);
		}
	}	


	@Override
	public SalaryTransactionMalaysia createSalaryTrx(Connection aConn, String aType) throws Exception {
		SalaryTransaction salaryTrx;
		if (aType.equals(CAT_DEDUCTIONS)){
			salaryTrx = (SalaryTransaction) ObjectBase.CreateObject(aConn, SalaryTrxMsiaDeduct.class);
		} else {
			salaryTrx = (SalaryTransaction) ObjectBase.CreateObject(aConn, SalaryTransactionMalaysia.class);
		}
		return(SalaryTransactionMalaysia) (salaryTrx);
	}

	public static Money TotalAffectedAmount(Connection aConn, SalarySlip aPayslip, String aAffectedType, String aEarnOrDeduct) throws Exception {
		Money totalAffectedAmount = (Money) ObjectBase.CreateObject(aConn, Money.class);
		totalAffectedAmount.setValue(Country.Malaysia.getCurrencyCode(), "0.00");
		aPayslip.getFieldObjectBox(aEarnOrDeduct).resetIterator(); // always do a reset before starting to loop for the objects
		while(aPayslip.getFieldObjectBox(aEarnOrDeduct).hasNext(aConn)) {
			Boolean isAffected = false;
			SalaryTransactionMalaysia eachTrx = (SalaryTransactionMalaysia) aPayslip.getFieldObjectBox(aEarnOrDeduct).getNext();
			if (!eachTrx.getForDelete()) {
				if (aAffectedType.equals(SalarySlipMalaysia.AFFECT_TAX)) {
					if (eachTrx.isAffectTax() != null && eachTrx.isAffectTax() == true) isAffected = true;
				} else if (aAffectedType.equals(SalarySlipMalaysia.AFFECT_EPF)) {
					if (eachTrx.isAffectEpf() != null && eachTrx.isAffectEpf() == true) isAffected = true;
				} else if (aAffectedType.equals(SalarySlipMalaysia.AFFECT_SOCSO)) {
					if (eachTrx.isAffectSocso() != null && eachTrx.isAffectSocso() == true) isAffected = true;
				} else if (aAffectedType.equals(SalarySlipMalaysia.AFFECT_EIS)) {
					if (eachTrx.isAffectEis() != null && eachTrx.isAffectEis() == true) isAffected = true;
				} else {
					throw new Hinderance("Unknown type when computing if salary transaction will affect TAX, EPF, SOCSO or EIS");
				}

				if (isAffected) {
					if (eachTrx.getAmount(aConn).getCurrencyCode().equals(Country.Malaysia.getCurrencyCode())) {
						if (totalAffectedAmount.hasProperValue() == false) totalAffectedAmount.setValue(eachTrx.getAmount(aConn).getCurrencyCode(), "0.0");
						if (eachTrx.getAmount(aConn).hasProperValue()) {
							//App.logDebg("ZZZ: " + aEarnOrDeduct + ", " + eachTrx.getDescr() + ", " + eachTrx.getAmount(aConn).getValueStr());
							totalAffectedAmount.addAmount(eachTrx.getAmount(aConn));
						}
					} else {
						throw new Hinderance("For transactions in Malaysia payslip, all transactions must be in " + Country.Malaysia.getCurrencyCode());
					}
				}
			}
		}
		return(totalAffectedAmount);
	}

	public static Money GetAffectedAmount(Connection aConn, SalarySlip aPayslip, String aAffectedType) throws Exception {
		Money totalEarning = TotalAffectedAmount(aConn, aPayslip, aAffectedType, SalarySlip.Earnings);
		Money totalDeduction = TotalAffectedAmount(aConn, aPayslip, aAffectedType, SalarySlip.Deductions);
		totalEarning.minusAmount(totalDeduction);
		return(totalEarning);
	}

	@Override
	public void removeComputedTrx(Connection aConn) throws Exception {
		RemoveComputedTrx(aConn, this);
	}

	public static void RemoveComputedTrx(Connection aConn, SalarySlipMalaysia aSalarySlip) throws Exception {
		RemoveTrx(aConn, aSalarySlip, SalarySlip.Deductions, EPF_EMPLOYEE);
		RemoveTrx(aConn, aSalarySlip, SalarySlip.Deductions, SOCSO_EMPLOYEE);
		RemoveTrx(aConn, aSalarySlip, SalarySlip.Deductions, TAX);
		RemoveTrx(aConn, aSalarySlip, SalarySlip.Deductions, EIS_EMPLOYEE);

		RemoveTrx(aConn, aSalarySlip, SalarySlip.Others, EPF_EMPLOYER);
		RemoveTrx(aConn, aSalarySlip, SalarySlip.Others, SOCSO_EMPLOYER);
		RemoveTrx(aConn, aSalarySlip, SalarySlip.Others, EIS_EMPLOYER);
	}

	public static void RemoveTrx(Connection aConn, SalarySlipMalaysia aSalarySlip, String aType, String aTrxName2Remove) throws Exception {
		Set<Entry<Long, Clasz>> entrySet = aSalarySlip.getFieldObjectBox(aType).getObjectMap().entrySet();
		for (Iterator iterator = entrySet.iterator(); iterator.hasNext();) {
			Entry<Long, Clasz> entry = (Entry<Long, Clasz>) iterator.next();
			SalaryTransaction eachTrx = (SalaryTransaction) entry.getValue();
			if (eachTrx.getDescr().contains(aTrxName2Remove)) {
				aSalarySlip.getFieldObjectBox(aType).remove(eachTrx);
				break;
			}
		}
	}
	public static SalaryTransaction GetEpfEmployee(Connection aConn, SalarySlip aSalarySlip) throws Exception {
		SalaryTransaction result = GetSalaryTrx(aConn, aSalarySlip, SalarySlip.Deductions, EPF_EMPLOYEE);
		return(result);
	}

	public static SalaryTransaction GetEpfEmployer(Connection aConn, SalarySlip aSalarySlip) throws Exception {
		SalaryTransaction result = GetSalaryTrx(aConn, aSalarySlip, SalarySlip.Others, EPF_EMPLOYER);
		return(result);
	}

	public static SalaryTransaction GetEisEmployee(Connection aConn, SalarySlip aSalarySlip) throws Exception {
		SalaryTransaction result = GetSalaryTrx(aConn, aSalarySlip, SalarySlip.Deductions, EIS_EMPLOYEE);
		return(result);
	}

	public static SalaryTransaction GetEisEmployer(Connection aConn, SalarySlip aSalarySlip) throws Exception {
		SalaryTransaction result = GetSalaryTrx(aConn, aSalarySlip, SalarySlip.Others, EIS_EMPLOYER);
		return(result);
	}
	
	public static SalaryTransaction GetSocsoEmployee(Connection aConn, SalarySlip aSalarySlip) throws Exception {
		SalaryTransaction result = GetSalaryTrx(aConn, aSalarySlip, SalarySlip.Deductions, SOCSO_EMPLOYEE);
		return(result);
	}

	public static SalaryTransaction GetSocsoEmployer(Connection aConn, SalarySlip aSalarySlip) throws Exception {
		SalaryTransaction result = GetSalaryTrx(aConn, aSalarySlip, SalarySlip.Others, SOCSO_EMPLOYER);
		return(result);
	}

	public static SalaryTransaction GetTax(Connection aConn, SalarySlip aSalarySlip) throws Exception {
		SalaryTransaction result = GetSalaryTrx(aConn, aSalarySlip, SalarySlip.Deductions, TAX);
		return(result);
	}

	@Override
	public String getDefaultCurrencyCode(Connection aConn) throws Exception {
		return(GetDefaultCurrencyCode(aConn));
	}

	public static String GetDefaultCurrencyCode(Connection aConn) throws Exception {
		return(Country.Malaysia.getCurrencyCode());
	}

	public void totalTaxableTrx(Connection aConn, Money aTotalBasic, Money aTotalBonus, Money aTotalOther, String aOtherText) throws Exception {
		this.getEarnings().resetIterator();
		while(this.getEarnings().hasNext(aConn)) {
			SalaryTransactionMalaysia earnTrx = (SalaryTransactionMalaysia) this.getEarnings().getNext();
			if (earnTrx.isAffectTax()) {
				if (earnTrx.getDescr().startsWith(SalaryTransaction.BASIC_SALARY) 
				|| earnTrx.getDescr().startsWith(SalaryTransactionMalaysia.OVERTIME_1X_DESC)
				|| earnTrx.getDescr().startsWith(SalaryTransactionMalaysia.OVERTIME_1_5X_DESC)
				|| earnTrx.getDescr().startsWith(SalaryTransactionMalaysia.OVERTIME_2X_DESC)
				|| earnTrx.getDescr().startsWith(SalaryTransactionMalaysia.OVERTIME_3X_DESC)
				) {
					aTotalBasic.addAmount(earnTrx.getAmount(aConn));
				} else if (earnTrx.getDescr().startsWith(SalaryTransactionMalaysia.CLAIM_BONUS_DESC)
				|| earnTrx.getDescr().startsWith(SalaryTransactionMalaysia.CLAIM_COMMISSION_DESC)
				) {
					aTotalBonus.addAmount(earnTrx.getAmount(aConn));
				} else {
					aTotalOther.addAmount(earnTrx.getAmount(aConn));
					if (!aOtherText.isEmpty()) aOtherText += ", ";
					aOtherText += earnTrx.getDescr();
				}
			}
		}

	}

	public void totalWorkerPartEpfSocsoTrx(Connection aConn, Money aTotalEpf, Money aTotalSocso) throws Exception {
		this.getDeductions().resetIterator();
		while(this.getDeductions().hasNext(aConn)) {
			SalaryTransactionMalaysia deductTrx = (SalaryTransactionMalaysia) this.getDeductions().getNext();
			if (deductTrx.getDescr().startsWith(SalarySlipMalaysia.EPF_EMPLOYEE)) {
				aTotalEpf.addAmount(deductTrx.getAmount(aConn));
			} else if (deductTrx.getDescr().startsWith(SalarySlipMalaysia.SOCSO_EMPLOYEE)) {
				aTotalSocso.addAmount(deductTrx.getAmount(aConn));
			}
		}
	}

	public void totalPcbCp38Zakat(Connection aConn, Money aTotalPcb, Money aTotalCp38, Money aTotalZakat) throws Exception {
		this.getDeductions().resetIterator();
		while(this.getDeductions().hasNext(aConn)) {
			SalaryTransactionMalaysia deductTrx = (SalaryTransactionMalaysia) this.getDeductions().getNext();
			if (deductTrx.getDescr().startsWith(SalarySlipMalaysia.CP38)) {
				aTotalCp38.addAmount(deductTrx.getAmount(aConn));
			} else if (deductTrx.getDescr().startsWith(SalarySlipMalaysia.ZAKAT)) {
				aTotalZakat.addAmount(deductTrx.getAmount(aConn));
			} else if (deductTrx.getDescr().startsWith(SalarySlipMalaysia.TAX)) {
				aTotalPcb.addAmount(deductTrx.getAmount(aConn));
			}
		}
	}

	public String getEpfWorkerAmountWithComma(Connection aConn) throws Exception {
		Money epfWorkerMoney = this.getEpfWorker(aConn);
		String result = epfWorkerMoney.getAmountWithComma();
		return(result);
	}

	public Money getEpfWorker(Connection aConn) throws Exception {
		Money epfWorkerMoney = Money.CreateMoney(aConn, Country.Malaysia.getCurrencyCode());
		SalaryTransaction epfWorkerTrx = SalarySlipMalaysia.GetEpfEmployee(aConn, this);
		if (epfWorkerTrx != null) {
			epfWorkerMoney.setAmount(epfWorkerTrx.getAmount(aConn));
		}
		return(epfWorkerMoney);
	}

	public String getEpfBossAmountWithComma(Connection aConn) throws Exception {
		Money epfBossMoney = this.getEpfBoss(aConn);
		String result = epfBossMoney.getAmountWithComma();
		return(result);
	}

	public Money getEpfBoss(Connection aConn) throws Exception {
		Money epfBossMoney = Money.CreateMoney(aConn, Country.Malaysia.getCurrencyCode());
		SalaryTransaction epfBossTrx = SalarySlipMalaysia.GetEpfEmployer(aConn, this);
		if (epfBossTrx != null) {
			epfBossMoney.setAmount(epfBossTrx.getAmount(aConn));
		}
		return(epfBossMoney);
	}

	public Money getEpfTotalAmount(Connection aConn) throws Exception {
		Money result = Money.CreateMoney(aConn, Country.Malaysia.getCurrencyCode());

		Money epfBoss = this.getEpfBoss(aConn);
		if (epfBoss != null) result.addAmount(epfBoss);

		Money epfWorker = this.getEpfWorker(aConn);
		if (epfWorker != null) result.addAmount(epfWorker);

		return(result);
	}

	public String getSocsoWorkerAmountWithComma(Connection aConn) throws Exception {
		Money socsoWorkerMoney = this.getSocsoWorker(aConn);
		String result = socsoWorkerMoney.getAmountWithComma();
		return(result);
	}

	public Money getSocsoWorker(Connection aConn) throws Exception {
		Money socsoWorkerMoney = Money.CreateMoney(aConn, Country.Malaysia.getCurrencyCode());
		SalaryTransaction socsoWorkerTrx = SalarySlipMalaysia.GetSocsoEmployee(aConn, this);
		if (socsoWorkerTrx != null) {
			socsoWorkerMoney.setAmount(socsoWorkerTrx.getAmount(aConn));
		}
		return(socsoWorkerMoney);
	}

	public String getSocsoBossAmountWithComma(Connection aConn) throws Exception {
		Money socsoBossMoney = this.getSocsoBoss(aConn);
		String result = socsoBossMoney.getAmountWithComma();
		return(result);
	}

	public Money getSocsoBoss(Connection aConn) throws Exception {
		Money socsoBossMoney = Money.CreateMoney(aConn, Country.Malaysia.getCurrencyCode());
		SalaryTransaction socsoBossTrx = SalarySlipMalaysia.GetSocsoEmployer(aConn, this);
		if (socsoBossTrx != null) {
			socsoBossMoney.setAmount(socsoBossTrx.getAmount(aConn));
		}
		return(socsoBossMoney);
	}

	public String getSocsoTotalAmountWithComma(Connection aConn) throws Exception {
		Money totalSocso = this.getSocsoTotalAmount(aConn);
		String result = totalSocso.getAmountWithComma();
		return(result);
	}

	public Money getSocsoTotalAmount(Connection aConn) throws Exception {
		Money result = Money.CreateMoney(aConn, Country.Malaysia.getCurrencyCode());

		Money socsoBoss = this.getSocsoBoss(aConn);
		if (socsoBoss != null) result.addAmount(socsoBoss);

		Money socsoWorker = this.getSocsoWorker(aConn);
		if (socsoWorker != null) result.addAmount(socsoWorker);

		return(result);
	}

	@Override
	public Money getTax(Connection aConn) throws Exception {
		Money taxMoney = Money.CreateMoney(aConn, Country.Malaysia.getCurrencyCode());
		SalaryTransaction taxTrx = SalarySlipMalaysia.GetTax(aConn, this);
		if (taxTrx != null) {
			taxMoney.setAmount(taxTrx.getAmount(aConn));
		}
		return(taxMoney);
	}

	public String getTaxAmountWithComma(Connection aConn) throws Exception {
		Money taxAmount = this.getTax(aConn);
		String result = taxAmount.getAmountWithComma();
		return(result);
	}

	public Money getCp38(Connection aConn) throws Exception {
		// TODO
		Money cp38  = Money.CreateMoney(aConn, Country.Malaysia.getCurrencyCode());
		return(cp38);
	}

	public Money getZakat(Connection aConn) throws Exception {
		// TODO
		Money zakat = Money.CreateMoney(aConn, Country.Malaysia.getCurrencyCode());
		return(zakat);
	}

	@Override
	public String getTaxDesc() {
		return TAX;
	}
}	
