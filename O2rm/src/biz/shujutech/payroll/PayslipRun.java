package biz.shujutech.payroll;

import biz.shujutech.base.Connection;
import biz.shujutech.bznes.Company;
import biz.shujutech.bznes.Money;
import biz.shujutech.db.object.Clasz;
import biz.shujutech.db.object.FieldClasz.FetchStatus;
import biz.shujutech.db.object.FieldObjectBox;
import biz.shujutech.db.object.ObjectBase;
import biz.shujutech.db.relational.FieldType;
import biz.shujutech.db.relational.SortOrder;
import biz.shujutech.reflect.ReflectField;
import biz.shujutech.reflect.ReflectIndex;
import java.util.List;
import java.util.Random;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import biz.shujutech.db.object.ObjectIndex;
import java.text.DecimalFormat;
import java.util.concurrent.CopyOnWriteArrayList;

public class PayslipRun extends Clasz {
	@ReflectField(type=FieldType.STRING, displayPosition=10, size=64, indexes={@ReflectIndex(indexName="idx_psr_batchid", indexNo=0, indexOrder=SortOrder.DSC, isUnique=true)}) public static String BatchId;
	@ReflectField(type=FieldType.DATETIME, displayPosition=20, indexes={@ReflectIndex(indexName="idx_psr_rundate", indexNo=0, indexOrder=SortOrder.DSC, isUnique=false)}) public static String RunDate;
	@ReflectField(type=FieldType.DATE, displayPosition=30) public static String PayslipFrom;
	@ReflectField(type=FieldType.DATE, displayPosition=40) public static String PayslipTo;
	@ReflectField(type=FieldType.INTEGER, displayPosition=50) public static String TotalPayslipGenerated;
	@ReflectField(type=FieldType.OBJECT, clasz="biz.shujutech.bznes.Money", inline=true, prefetch=true, displayPosition=60) public static String EmployerCost;
	@ReflectField(type=FieldType.INTEGER, displayPosition=70) public static String TotalEmployeeProcessed;
	@ReflectField(type=FieldType.INTEGER, displayPosition=80) public static String TotalEmploymentProcessed;
	@ReflectField(type=FieldType.INTEGER, displayPosition=90) public static String TotalActiveEmployees;
	@ReflectField(type=FieldType.OBJECTBOX, deleteAsMember=true, clasz="biz.shujutech.payroll.SalarySlip", polymorphic=true, displayPosition=110) public static String GeneratedPayslips;
	@ReflectField(type=FieldType.HTML, displayPosition=120, size=1024) public static String RunReport;

	public String getBatchId() throws Exception {
		return (this.getValueStr(BatchId));
	}

	public void setBatchId(String aBatchId) throws Exception {
		this.setValueStr(BatchId, aBatchId);
	}

	public DateTime getRunDate() throws Exception {
		return (this.getValueDateTime(RunDate));
	}

	public void setRunDate(DateTime aRunDate) throws Exception {
		this.setValueDateTime(RunDate, aRunDate);
	}

	public DateTime getPayslipFrom() throws Exception {
		return (this.getValueDate(PayslipFrom));
	}

	public void setPayslipFrom(DateTime aPayslipFrom) throws Exception {
		this.setValueDate(PayslipFrom, aPayslipFrom);
	}

	public DateTime getPayslipTo() throws Exception {
		return (this.getValueDate(PayslipTo));
	}

	public void setPayslipTo(DateTime aPayslipTo) throws Exception {
		this.setValueDate(PayslipTo, aPayslipTo);
	}

	public Integer getTotalPayslipGenerated() throws Exception {
		return (this.getValueInt(TotalPayslipGenerated));
	}

	public void setTotalPayslipGenerated(Integer aTotalPayslipGenerated) throws Exception {
		this.setValueInt(TotalPayslipGenerated, aTotalPayslipGenerated);
	}

	public Money getEmployerCost(Connection aConn) throws Exception {
		return((Money) this.getValueObject(aConn, EmployerCost));
	}

	public void setEmployerCost(Money aEmployerCost) throws Exception {
		this.setValueObject(EmployerCost, aEmployerCost);
	}

	public Integer getTotalEmployeeProcessed() throws Exception {
		return (this.getValueInt(TotalEmployeeProcessed));
	}

	public void setTotalEmployeeProcessed(Integer aTotalEmployeeProcessed) throws Exception {
		this.setValueInt(TotalEmployeeProcessed, aTotalEmployeeProcessed);
	}

	public Integer getTotalEmploymentProcessed() throws Exception {
		return (this.getValueInt(TotalEmploymentProcessed));
	}

	public void setTotalEmploymentProcessed(Integer aTotalEmploymentProcessed) throws Exception {
		this.setValueInt(TotalEmploymentProcessed, aTotalEmploymentProcessed);
	}

	public Integer getTotalActiveEmployees() throws Exception {
		return (this.getValueInt(TotalActiveEmployees));
	}

	public void setTotalActiveEmployees(Integer aTotalActiveEmployees) throws Exception {
		this.setValueInt(TotalActiveEmployees, aTotalActiveEmployees);
	}

	public FieldObjectBox getGeneratedPayslips() throws Exception {
		return (this.getFieldObjectBox(GeneratedPayslips));
	}

	public String createBatchId() throws Exception {
		this.setBatchId(GenerateBatchId());
		return(this.getBatchId());
	}

	public void addPayslip(List<SalarySlip> aPayslipList) throws Exception {
		for(SalarySlip eachPayslip : aPayslipList) {
			this.getGeneratedPayslips().addValueObject(eachPayslip);
		}
	}

	public static String GenerateBatchId() {
		DateTimeFormatter dtfOut = DateTimeFormat.forPattern("yyyyMMddHHmmss");
		String strBatchIdDate = dtfOut.print(DateTime.now());
		int intRandom = Math.abs((new Random()).nextInt());
		DecimalFormat formater = new DecimalFormat("000000");
		String strRandom = formater.format(intRandom);
		String result = strBatchIdDate + "-" + strRandom.substring(0, 5);
		return result;
	}

	public static Integer PlusOne(Integer aInt2PlusOne) throws Exception {
		if (aInt2PlusOne == null) {
			aInt2PlusOne = 0;
		}
		aInt2PlusOne++;
		return(aInt2PlusOne);
	}

	public Integer incTotalEmployeeProcessed() throws Exception {
		this.setTotalEmployeeProcessed(PlusOne(this.getTotalEmployeeProcessed()));
		return(this.getTotalEmployeeProcessed());
	}
	
	public Integer incTotalEmploymentProcessed() throws Exception {
		this.setTotalEmploymentProcessed(PlusOne(this.getTotalEmploymentProcessed()));
		return(this.getTotalEmploymentProcessed());
	}

	public Integer incTotalPayslipGenerated() throws Exception {
		this.setTotalPayslipGenerated(PlusOne(this.getTotalPayslipGenerated()));
		return(this.getTotalPayslipGenerated());
	}

	public Integer accumTotalPayslipGenerated(Integer aInt2Accum) throws Exception {
		if (this.getTotalPayslipGenerated() == null) this.setTotalPayslipGenerated(0);
		this.setTotalPayslipGenerated(this.getTotalPayslipGenerated() + aInt2Accum);
		return(this.getTotalPayslipGenerated());
	}

	public Money accumEmployerCost(Connection aConn, Money aAmount) throws Exception {
		this.getEmployerCost(aConn).setZeroIfNull(aAmount);
		this.getEmployerCost(aConn).addAmount(aAmount);
		return(this.getEmployerCost(aConn));
	}

	public Money totalUpEmployerCost(Connection aConn, Company aCompany) throws Exception {
		this.getEmployerCost(aConn).setAmount(0D);
		this.getGeneratedPayslips().forEachMember(aConn, ((Connection bConn, Clasz aClasz) -> {
			SalarySlip eachPayslip = (SalarySlip) aClasz;
			eachPayslip.computeAllTotal(bConn, aCompany);
			this.accumEmployerCost(bConn, eachPayslip.getEmployerCost());
			return(true);
		}));
		return(this.getEmployerCost(aConn));
	}

	public Money getEmployerTotalCost(Connection aConn, Company aCompany, List<SalarySlip> aPayslipList) throws Exception {
		for(SalarySlip eachPayslip : aPayslipList) {
			eachPayslip.computeAllTotal(aConn, aCompany);
			this.accumEmployerCost(aConn, eachPayslip.getEmployerCost());
		}
		return(this.getEmployerCost(aConn));
	}

	@Deprecated
	public FetchStatus fetchPayslipBySectionAsc(Connection aConn, String aPageDirection, int aFetchSize, String aSortField, String aSortValue) throws Exception {
		return(fetchPayslipBySection(aConn, aPageDirection, aFetchSize, aSortField, aSortValue, SortOrder.ASC));
	}

	public FetchStatus fetchPayslipBySectionAsc(Connection aConn, String aPageDirection, int aFetchSize, String aEmployeeName, String aEmployeeValue, String aPayslipStartName, String aPayslipStartValue) throws Exception {
		List<String> sortFieldList = new CopyOnWriteArrayList<>(); 
		List<String> sortValueList = new CopyOnWriteArrayList<>(); 
		List<SortOrder> sortOrderList = new CopyOnWriteArrayList<>(); 
		sortFieldList.add(aEmployeeName);
		sortFieldList.add(aPayslipStartName);
		sortValueList.add(aEmployeeValue);
		sortValueList.add(aPayslipStartValue);
		sortOrderList.add(SortOrder.ASC);
		sortOrderList.add(SortOrder.DSC);
		return(fetchPayslipBySection(aConn, aPageDirection, aFetchSize, sortFieldList, sortValueList, sortOrderList));
	}

	public FetchStatus fetchPayslipBySectionDsc(Connection aConn, String aPageDirection, int aFetchSize, String aSortField, String aSortValue) throws Exception {
		return(fetchPayslipBySection(aConn, aPageDirection, aFetchSize, aSortField, aSortValue, SortOrder.DSC));
	}

	public FetchStatus fetchPayslipBySection(Connection aConn, String aPageDirection, int aFetchSize, String aSortField, String aSortValue, SortOrder aOrder) throws Exception {
		String idxName = ObjectIndexOnPayslipNameAndPeriodFrom(aConn); 
		FetchStatus result = this.getGeneratedPayslips().fetchBySection(aConn, aPageDirection, aFetchSize, aSortField, aSortValue, idxName, aOrder);
		return(result);
	}	

	public FetchStatus fetchPayslipBySection(Connection aConn, String aPageDirection, int aFetchSize, List<String> aSortFieldList, List<String> aSortValueList, List<SortOrder> aSortOrderList) throws Exception {
		String idxName = ObjectIndexOnPayslipNameAndPeriodFrom(aConn); 
		FetchStatus result = this.getGeneratedPayslips().fetchBySection(aConn, aPageDirection, aFetchSize, aSortFieldList, aSortValueList, aSortOrderList, idxName);
		return(result);
	}	

	@Deprecated
	public static String ObjectIndexOnPayslipName(Connection aConn) throws Exception {
		PayslipRun payslipRun = (PayslipRun) ObjectBase.CreateObject(aConn, PayslipRun.class);
		return ObjectIndex.ObjectIndexOnField(aConn, payslipRun, GeneratedPayslips, SalarySlip.Employee);
	}

	public static String ObjectIndexOnPayslipNameAndPeriodFrom(Connection aConn) throws Exception {
		PayslipRun payslipRun = (PayslipRun) ObjectBase.CreateObject(aConn, PayslipRun.class);
		List<String> indexFieldList = new CopyOnWriteArrayList<>();
		indexFieldList.add(SalarySlip.Employee);
		indexFieldList.add(SalarySlip.PeriodFrom);
		return ObjectIndex.ObjectIndexOnField(aConn, payslipRun, GeneratedPayslips, indexFieldList);
	}

	public static PayslipRun GetPayslipRun(Connection aConn, SalarySlip aPayslip) throws Exception {
		PayslipRun payslipRun = (PayslipRun) ObjectBase.GetMasterInstantUnique(aConn, PayslipRun.class, GeneratedPayslips, aPayslip);
		return(payslipRun);
	}
}
