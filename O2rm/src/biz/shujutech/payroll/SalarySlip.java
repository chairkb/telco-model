package biz.shujutech.payroll;

import biz.shujutech.db.object.FieldObjectBox;
import biz.shujutech.db.relational.FieldType;
import biz.shujutech.reflect.ReflectField;
import biz.shujutech.base.Connection;
import biz.shujutech.base.Hinderance;
import biz.shujutech.bznes.Contact;
import biz.shujutech.bznes.Employment;
import biz.shujutech.bznes.Money;
import biz.shujutech.db.object.Clasz;
import biz.shujutech.db.object.ObjectBase;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;
import org.joda.time.DateTime;


public class SalarySlip extends Clasz {
	public static final String TAX = "Tax Deduction";

	@ReflectField(type=FieldType.STRING, size=32, displayPosition=5) public static String BatchId; 
	@ReflectField(type=FieldType.OBJECTBOX, displayPosition=10, deleteAsMember=true, clasz="biz.shujutech.payroll.SalarySlipStatus") public static String Status; 
	@ReflectField(type=FieldType.STRING, size=32, updateable=false, displayPosition=15) public static String Company;
	@ReflectField(type=FieldType.STRING, size=32, updateable=false, displayPosition=15) public static String CompanyAlias;
	@ReflectField(type=FieldType.STRING, size=32, updateable=false, displayPosition=20) public static String Employee;
	@ReflectField(type=FieldType.STRING, size=32, updateable=false, displayPosition=20) public static String EmployeeAlias;
	//@ReflectField(type=FieldType.STRING, size=32, updateable=false, displayPosition=25) public static String Designation;
	@ReflectField(type=FieldType.STRING, size=32, updateable=false, displayPosition=35) public static String SalaryPeriod;

	@ReflectField(type=FieldType.OBJECT, clasz="biz.shujutech.bznes.Money", inline=true, prefetch=true, displayPosition=45) public static String EmployerCost;
	@ReflectField(type=FieldType.OBJECT, clasz="biz.shujutech.bznes.Money", inline=true, prefetch=true, displayPosition=55) public static String NetPay;

	String designation;
	Money totalEarning;
	Money totalDeduction;
	Money totalOther;
	Money roundedNetPay;
	String netPayInWord;

	@ReflectField(type=FieldType.DATE, displayPosition=50) public static String PeriodFrom;
	@ReflectField(type=FieldType.DATE, displayPosition=55) public static String PeriodTo;
	@ReflectField(type=FieldType.OBJECTBOX, deleteAsMember=true, uiMaster=false, clasz="biz.shujutech.payroll.SalaryTransaction", polymorphic=true, displayPosition=60) public static String Earnings; 
	@ReflectField(type=FieldType.OBJECTBOX, deleteAsMember=true, uiMaster=false, clasz="biz.shujutech.payroll.SalaryTransaction", polymorphic=true, displayPosition=65) public static String Deductions; 
	@ReflectField(type=FieldType.OBJECTBOX, deleteAsMember=true, uiMaster=false, clasz="biz.shujutech.payroll.SalaryTransaction", polymorphic=true, displayPosition=70) public static String Others; 

	@ReflectField(type=FieldType.OBJECT, clasz="biz.shujutech.bznes.Employment", displayPosition=80, polymorphic=true) public static String Job;
	@ReflectField(type=FieldType.OBJECT, clasz="biz.shujutech.payroll.SalarySlipEmailRecord", inline=true, prefetch=true, displayPosition=90) public static String EmailNotification;


	public static final String CAT_EARNINGS = "Earnings";
	public static final String CAT_DEDUCTIONS = "Deductions";
	public static final String CAT_OTHERS = "Others";

	public SalarySlip() {
		super(); // create basic salary
	}

	@Override
	public void validateBeforePersist(Connection aConn) throws Exception {
		this.checkTrxDate(aConn, Earnings);
		this.checkTrxDate(aConn, Deductions);
		this.checkTrxDate(aConn, Others);
	}

	public void checkTrxDate(Connection aConn, String aType) throws Exception {
		this.getFieldObjectBox(aType).resetIterator(); // always do a reset before starting to loop for the objects
		while(this.getFieldObjectBox(aType).hasNext(aConn)) {
			SalaryTransaction eachTrx = (SalaryTransaction) this.getFieldObjectBox(aType).getNext();
			if (eachTrx.getObjectId() == Clasz.NOT_INITIALIZE_OBJECT_ID) {
				if (eachTrx.getCreateDate() == null) {
					eachTrx.setCreateDate(new DateTime());
				}
				if (eachTrx.getTransactionDate()== null) {
					eachTrx.setTransactionDate(this.getPeriodTo());
				}
			}
		}
	}

	public Money totalUpTrx(Connection aConn, String aType) throws Exception {
		//Money totalTrx = (Money) ObjectBase.CreateObject(aConn, Money.class);
		Money totalTrx = Money.CreateMoney(aConn, this.getDefaultCurrencyCode(aConn));
		this.getFieldObjectBox(aType).resetIterator(); // always do a reset before starting to loop for the objects
		while(this.getFieldObjectBox(aType).hasNext(aConn)) {
			SalaryTransaction eachTrx = (SalaryTransaction) this.getFieldObjectBox(aType).getNext();
			if (!eachTrx.getForDelete()) {
				if (totalTrx.hasProperValue() == false) totalTrx.setValue(eachTrx.getAmount(aConn).getCurrencyCode(), "0.0");
				if (eachTrx.getAmount(aConn).hasProperValue()) totalTrx.addAmount(eachTrx.getAmount(aConn));
			}
		}
		return(totalTrx);
	}

	public Money computeNetPay(Connection aConn) throws Exception {
		Money totalEarn = totalUpTrx(aConn, Earnings);
		Money totalDeduct = totalUpTrx(aConn, Deductions);
		this.setTotalEarning(aConn, totalEarn);
		this.setTotalDeduction(aConn, totalDeduct);

		//Money netPay = (Money) ObjectBase.CreateObject(aConn, Money.class);
		Money netPay = Money.CreateMoney(aConn, this.getDefaultCurrencyCode(aConn));
		netPay.setAmount(this.getTotalEarning());
		netPay.minusAmount(this.getTotalDeduction());
		this.setNetPay(netPay);
		return(netPay);
	}

	public SalaryTransaction addOtherTrx(Connection aConn, String aTrxName, Money aTrxAmount) throws Exception {
		return(this.addSalaryTrx(aConn, aTrxName, aTrxAmount, Others));
	}

	public SalaryTransaction addDeductionTrx(Connection aConn, String aTrxName, Money aTrxAmount) throws Exception {
		return(this.addSalaryTrx(aConn, aTrxName, aTrxAmount, Deductions));
	}

	public SalaryTransaction addEarningTrx(Connection aConn, String aTrxName, Money aTrxAmount) throws Exception {
		return(this.addSalaryTrx(aConn, aTrxName, aTrxAmount, Earnings));
	}

	private SalaryTransaction addSalaryTrx(Connection aConn, String aTrxName, Money aTrxAmount, String aStrType) throws Exception {
		SalaryTransaction salaryTrx;
		salaryTrx = (SalaryTransaction) this.createSalaryTrx(aConn, aStrType);
		Integer seq = GetNextSeq(aConn, this.getFieldObjectBox(aStrType)) + 1;
		salaryTrx.setSequence(seq);
		salaryTrx.setDescr(aTrxName);
		salaryTrx.setAmount(aTrxAmount);
		this.addValueObject(aConn, aStrType, salaryTrx);
		return(salaryTrx);
	}

	public SalaryTransaction modifySalaryTrx(Connection aConn, String aTrxName, Money aTrxAmount, String aStrType) throws Exception {
		SalaryTransaction modifiedTrx = null;
		this.getFieldObjectBox(aStrType).resetIterator(); // always do a reset before starting to loop for the objects
		while(this.getFieldObjectBox(aStrType).hasNext(aConn)) {
			SalaryTransaction eachTrx = (SalaryTransaction) this.getFieldObjectBox(aStrType).getNext();
			if (eachTrx.getDescr().contains(aTrxName)) {
				modifiedTrx = eachTrx;
				eachTrx.setAmount(aTrxAmount);
				SalarySlipMalaysia.SetAffect(eachTrx);
				//this.addValueObject(aConn, aStrType, eachTrx);
				break;
			}
		}
		return(modifiedTrx);
	}

	public void addEarning(Connection aConn, SalaryTransaction aTrx) throws Exception {
		this.addValueObject(aConn, Earnings, aTrx);
	}

	public void addOther(Connection aConn, SalaryTransaction aTrx) throws Exception {
		this.addValueObject(aConn, Others, aTrx);
	}

	public void addStatusCreate(Connection aConn) throws Exception {
		SalarySlipStatus statusCreate = (SalarySlipStatus) ObjectBase.CreateObject(aConn, SalarySlipStatus.class);
		statusCreate.setDescr("Create");
		statusCreate.setStatusDate(new DateTime());
		this.addStatus(aConn, statusCreate);
	}

	public void addStatusEmailed(Connection aConn) throws Exception {
		SalarySlipStatus statusEmailed = (SalarySlipStatus) ObjectBase.CreateObject(aConn, SalarySlipStatus.class);
		statusEmailed.setDescr("Emailed");
		statusEmailed.setStatusDate(new DateTime());
		this.addStatus(aConn, statusEmailed);
	}

	public void addStatusPrinted(Connection aConn) throws Exception {
		SalarySlipStatus statusPrinted = (SalarySlipStatus) ObjectBase.CreateObject(aConn, SalarySlipStatus.class);
		statusPrinted.setDescr("Printed");
		statusPrinted.setStatusDate(new DateTime());
		this.addStatus(aConn, statusPrinted);
	}

	public void addStatusEdited(Connection aConn) throws Exception {
		SalarySlipStatus statusEdited = (SalarySlipStatus) ObjectBase.CreateObject(aConn, SalarySlipStatus.class);
		statusEdited.setDescr("Edited");
		statusEdited.setStatusDate(new DateTime());
		this.addStatus(aConn, statusEdited);
	}

	public static int GetNextSeq(Connection aConn, FieldObjectBox aTrxType) throws Exception {
		int largestSeq = 0;
		aTrxType.resetIterator();
		while (aTrxType.hasNext(aConn)) {
			SalaryTransaction trx = (SalaryTransaction) aTrxType.getNext();
			Integer seq = trx.getSequence();
			if (seq != null) {
				if (largestSeq < seq) {
					largestSeq = seq;
				}
			} else {
				throw new Hinderance("Salary transaction sequence cannot be null: " + trx.getDescr());
			}
		}
		return(largestSeq + 5);
	}

	public void fetchLatestStatus(Connection aConn) throws Exception {
		this.fetchLatestMemberFromFob(aConn, SalarySlip.Status, biz.shujutech.payroll.SalarySlipStatus.StatusDate);
	}

	public void addStatus(Connection aConn, SalarySlipStatus aStatus) throws Exception {
		this.addValueObject(aConn, Status, aStatus);
	}

	public void setCompany(String aCompany) throws Exception {
		this.setValueStr(Company, aCompany);
	}

	public String getCompany() throws Exception {
		return (this.getValueStr(Company));
	}

	public void setEmployee(String aEmployee) throws Exception {
		this.setValueStr(Employee, aEmployee);
	}

	public String getEmployee() throws Exception {
		return (this.getValueStr(Employee));
	}

	public void setPeriodFrom(DateTime aPeriodFrom) throws Exception {
		this.setValueDate(PeriodFrom, aPeriodFrom);
	}

	public DateTime getPeriodFrom() throws Exception {
		return (this.getValueDate(PeriodFrom));
	}

	public void setPeriodTo(DateTime aPeriodTo) throws Exception {
		this.setValueDate(PeriodTo, aPeriodTo);
	}

	public DateTime getPeriodTo() throws Exception {
		return (this.getValueDate(PeriodTo));
	}

	public Money getNetPay(Connection aConn) throws Exception {
		Money result = (Money) this.getValueObject(aConn, NetPay);
		return(result);
	}

	public void setNetPay(Money aNetPay) throws Exception {
		this.setValueObject(NetPay, aNetPay);
	}

	public void setNetPay(String aCurrency, String aDollar, String aCent) throws Exception {
		Money netPay = (Money) this.getDb().createObject(Money.class);
		netPay.setValue(aCurrency, aDollar, aCent);
		this.setNetPay(netPay);
	}

	public void setEmployerCost(Money aAmount) throws Exception {
		this.setValueObject(EmployerCost, aAmount);
	}

	public void setTotalEarning(Connection aConn, Money aAmount) throws Exception {
		if (this.totalEarning == null) {
			//this.totalEarning = (Money) ObjectBase.CreateObject(aConn, Money.class);
			this.totalEarning = Money.CreateMoney(aConn, this.getDefaultCurrencyCode(aConn));
		}
		this.totalEarning.setAmount(aAmount);
	}

	public void setTotalDeduction(Connection aConn, Money aAmount) throws Exception {
		if (this.totalDeduction == null) {
			//this.totalDeduction = (Money) ObjectBase.CreateObject(aConn, Money.class);
			this.totalDeduction = Money.CreateMoney(aConn, this.getDefaultCurrencyCode(aConn));
		}
		this.totalDeduction.setAmount(aAmount);
	}

	public void setTotalOther(Connection aConn, Money aAmount) throws Exception {
		if (this.totalOther == null) {
			//this.totalOther = (Money) ObjectBase.CreateObject(aConn, Money.class);
			this.totalOther = Money.CreateMoney(aConn, this.getDefaultCurrencyCode(aConn));
		}
		this.totalOther.setAmount(aAmount);
	}

	public Money getEmployerCost() throws Exception {
		return((Money) this.getValueObject(EmployerCost));
	}

	public Money getTotalEarning() throws Exception {
		return(this.totalEarning);
	}

	public Money getTotalDeduction() throws Exception {
		return(this.totalDeduction);
	}

	public Money getTotalOther() throws Exception {
		return(this.totalOther);
	}

	public void setRoundedNetPay(Connection aConn, Money aRoundedNetPay) throws Exception {
		if (this.roundedNetPay == null) {
			this.roundedNetPay = Money.CreateMoney(aConn, this.getDefaultCurrencyCode(aConn));
		}
		this.roundedNetPay.setAmount(aRoundedNetPay);
	}

	public void setNetPayInWord(String aNetPayInWord) throws Exception {
		this.netPayInWord = aNetPayInWord;
	}

	public String getNetPayInWord() throws Exception {
		return(this.netPayInWord);
	}

	/*
	@Deprecated
	public void setDesignation(String aDesignation) throws Exception {
	}

	@Deprecated
	public String getDesignation() throws Exception {
		return "";
	}
	*/

	public String getDesignation(Connection aConn) throws Exception {
		if (this.gotValueObject(aConn, Job) != null) {
			return this.getJob(aConn).getDesignation();
		} else {
			return("");
		}
	}

	public void setDesignation(Connection aConn, String aDesignation) throws Exception {
		this.getJob(aConn).setDesignation(aDesignation);
	}

	public void setJob(Employment aJob) throws Exception {
		this.setValueObject(Job , aJob);
	}

	public Employment getJob(Connection aConn) throws Exception {
		return ((Employment) this.getValueObject(aConn, Job));
	}

	public void setBatchId(String aBatchId) throws Exception {
		this.setValueStr(BatchId, aBatchId);
	}

	public String getBatchId() throws Exception {
		return (this.getValueStr(BatchId));
	}


	public void setSalaryPeriod(String aSalaryPeriod) throws Exception {
		this.setValueStr(SalaryPeriod, aSalaryPeriod);
	}

	public String getSalaryPeriod() throws Exception {
		return (this.getValueStr(SalaryPeriod));
	}

	public SalaryTransaction createBasicSalary(Connection aConn, Contact aPaidBy, Contact aPaidTo, String aTrxName, Money aTrxAmount, DateTime aTrxDate) throws Exception {
		throw new Hinderance("Cannot create basic salary at " + SalarySlip.class.getSimpleName() + ", class should be treated as abstract");
	}

	public SalaryTransaction createSalaryTrx(Connection aConn, String aType) throws Exception {
		throw new Hinderance("Cannot create Salary Transaction at " + SalarySlip.class.getSimpleName() + ", class should be treated as abstract");
	}

	public void removeComputedTrx(Connection aConn) throws Exception {
		throw new Hinderance("Cannot remove Salary Transaction at " + SalarySlip.class.getSimpleName() + ", class should be treated as abstract");
	}

	public String getDefaultCurrencyCode(Connection aConn) throws Exception {
		throw new Hinderance("No default currency code at " + SalarySlip.class.getSimpleName() + ", class should be treated as abstract");
	}

	public FieldObjectBox getEarnings() throws Exception {
		return (this.getFieldObjectBox(Earnings));
	}

	public void addEarnings(Connection aConn, SalaryTransaction aTrx) throws Exception {
		this.addValueObject(aConn, Earnings, aTrx);
	}

	public FieldObjectBox getDeductions() throws Exception {
		return (this.getFieldObjectBox(Deductions));
	}

	public void addDeductions(Connection aConn, SalaryTransaction aTrx) throws Exception {
		this.addValueObject(aConn, Deductions, aTrx);
	}

	public FieldObjectBox getOthers() throws Exception {
		return (this.getFieldObjectBox(Others));
	}

	public void addOthers(Connection aConn, SalaryTransaction aTrx) throws Exception {
		this.addValueObject(aConn, Others, aTrx);
	}

	public void computeAllTotal(Connection aConn, Contact aBoss) throws Exception {
		//Money totalEarn = totalUpTrx(aConn, Earnings);
		//this.setTotalEarning(aConn, totalEarn); 
		Money totalOther = totalUpTrx(aConn, Others);
		this.setTotalOther(aConn, totalOther); // this will total all earnings

		this.computeNetPay(aConn);

		//Money employerCost = (Money) ObjectBase.CreateObject(aConn, Money.class);
		Money employerCost = Money.CreateMoney(aConn, this.getDefaultCurrencyCode(aConn));
		this.setEmployerCost(employerCost);

		this.getEmployerCost().setAmount(this.getTotalEarning());
		this.getDeductions().resetIterator(); // always do a reset before starting to loop for the objects
		while (this.getDeductions().hasNext(aConn)) {
			SalaryTransaction eachTrx = (SalaryTransaction) this.getDeductions().getNext();
			if (!eachTrx.getForDelete()) {
				if (eachTrx.getPaidTo(aConn).isSame(aBoss)) { // it's paid by employer 
					this.getEmployerCost().minusAmount(eachTrx.getAmount(aConn));
				}
			}
		}

		this.getEmployerCost().addAmount(this.getTotalOther());
	}

	public void setEmployeeAlias(String aAlias) throws Exception {
		this.setValueStr(EmployeeAlias, aAlias);
	}

	public String getEmployeeAlias() throws Exception {
		return (this.getValueStr(EmployeeAlias));
	}

	public void setCompanyAlias(String aAlias) throws Exception {
		this.setValueStr(CompanyAlias, aAlias);
	}

	public String getCompanyAlias() throws Exception {
		return (this.getValueStr(CompanyAlias));
	}

	public void fetchAllTrx(Connection aConn) throws Exception {
		this.getEarnings().fetchAll(aConn);
		this.getDeductions().fetchAll(aConn);
		this.getOthers().fetchAll(aConn);
	}

	public SalaryTransaction getSalaryTrx(Connection aConn, String aType, String aTrxName) throws Exception {
		SalaryTransaction salaryTrx = GetSalaryTrx(aConn, this, aType, aTrxName);
		return(salaryTrx);
	}

	public static SalaryTransaction GetSalaryTrx(Connection aConn, SalarySlip aSalarySlip, String aType, String aTrxName) throws Exception {
		SalaryTransaction result = null;
		Set<Entry<Long, Clasz>> entrySet = aSalarySlip.getFieldObjectBox(aType).getObjectMap().entrySet();
		for (Iterator iterator = entrySet.iterator(); iterator.hasNext();) {
			Entry<Long, Clasz> entry = (Entry<Long, Clasz>) iterator.next();
			SalaryTransaction eachTrx = (SalaryTransaction) entry.getValue();
			if (eachTrx.getDescr().equals(aTrxName)) {
				result = eachTrx;
				break;
			}
		}
		return(result);
	}

	public SalarySlipEmailRecord getEmailNotification(Connection aConn) throws Exception {
		return((SalarySlipEmailRecord) this.getValueObject(aConn, EmailNotification));
	}

	public SalarySlipEmailRecord gotEmailNotification(Connection aConn) throws Exception {
		return((SalarySlipEmailRecord) this.gotValueObject(aConn, EmailNotification));
	}

	public void setEmailNotification(SalarySlipEmailRecord aEmailNotification) throws Exception {
		this.setValueObject(EmailNotification, aEmailNotification);
	}

	public String getTaxDesc() {
		return TAX;
	}

	public Money getTax(Connection aConn) throws Exception {
		throw new Hinderance("The method getTax must be implemented in the country specific SalarSlip class!");
	}
}


