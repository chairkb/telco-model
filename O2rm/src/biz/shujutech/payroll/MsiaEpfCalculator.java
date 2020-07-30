package biz.shujutech.payroll;

import biz.shujutech.base.App;
import biz.shujutech.base.Connection;
import biz.shujutech.base.DateAndTime;
import biz.shujutech.base.Hinderance;
import biz.shujutech.bznes.Money;
import biz.shujutech.bznes.Person;
import biz.shujutech.db.object.Clasz;
import biz.shujutech.db.object.ObjectBase;
import biz.shujutech.db.relational.FieldType;
import biz.shujutech.db.relational.SortOrder;
import java.util.concurrent.CopyOnWriteArrayList;
import biz.shujutech.reflect.ReflectField;
import biz.shujutech.reflect.ReflectIndex;
import biz.shujutech.db.object.Lookup;
import biz.shujutech.db.relational.BaseDb;
import biz.shujutech.db.relational.Database;
import biz.shujutech.db.relational.Database.DbType;
import biz.shujutech.db.relational.Field;
import biz.shujutech.db.relational.Record;
import biz.shujutech.db.relational.Table;
import static biz.shujutech.payroll.MsiaTaxCalculator.CreateField;
import biz.shujutech.util.Generic;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

public class MsiaEpfCalculator extends Clasz implements Lookup {
	
	@ReflectField(type=FieldType.STRING, size=40, indexes={@ReflectIndex(indexName="idx_mer_descr", indexNo=0, indexOrder=SortOrder.ASC, isUnique=true)}, displayPosition=5) public static String Descr; // determine who this payroll rules apples to i.e. citizen, foreigner, etc
	@ReflectField(type=FieldType.INTEGER, displayPosition=10) public static String EmployerEpfRate; 
	@ReflectField(type=FieldType.INTEGER, displayPosition=15) public static String EmployeeEpfRate; 
	public static CopyOnWriteArrayList<Lookup> LookupList = new CopyOnWriteArrayList<>();

	public static String EpfRegulationDescr = "As per current statutory regulation";
	public static MsiaEpfCalculator EpfRegulation = null;

	public static String Epf2017_11PercentDescr = "2017: 11% Employee";
	public static MsiaEpfCalculator Epf2017_11Percent = null;

	public static String EpfNoneDescr = "No EPF Contribution";
	public static MsiaEpfCalculator EpfNone = null;

	public static String Epf2020_11PercentDescr = "2020: 11% Employee";
	public static MsiaEpfCalculator Epf2020_11Percent = null;

	@Override
	public void initialize(Connection aConn) throws Exception {
		if (this.getEpfType().equals(MsiaEpfCalculator.EpfRegulationDescr)) {
			this.setEmployeeRate(11);
			this.setEmployerRate(12);
		} else if (this.getEpfType().equals(MsiaEpfCalculator.Epf2017_11PercentDescr)) {
			this.setEmployeeRate(8);
			this.setEmployerRate(12);
		} else if (this.getEpfType().equals(MsiaEpfCalculator.Epf2020_11PercentDescr)) {
			this.setEmployeeRate(11);
			this.setEmployerRate(12);
		} else if (this.getEpfType().equals(MsiaEpfCalculator.EpfNoneDescr)) {
			this.setEmployeeRate(0);
			this.setEmployerRate(0);
		}
	}

	@Override
	public String getDescr() throws Exception {
		return(this.getValueStr(Descr));
	}

	@Override
	public void setDescr(String aDescr) throws Exception {
		this.setValueStr(Descr, aDescr);
	}

	@Override
	public String getValueStr() throws Exception {
		return(this.getDescr());
	}

	public int getEmployerRate() throws Exception {
		return(this.getValueInt(EmployerEpfRate));
	}

	public String getEmployerRateAsStr() throws Exception {
		return(this.getValueStr(EmployerEpfRate));
	}

	public int getEmployeeRate() throws Exception {
		return(this.getValueInt(EmployeeEpfRate));
	}

	public String getEmployeeRateAsStr() throws Exception {
		return(this.getValueStr(EmployeeEpfRate));
	}

	public void setEmployerRate(int aRate) throws Exception {
		this.setValueInt(EmployerEpfRate, aRate);
	}

	public void setEmployeeRate(int aRate) throws Exception {
		this.setValueInt(EmployeeEpfRate, aRate);
	}

	@Override
	public CopyOnWriteArrayList<Lookup> getLookupList() {
		return(LookupList);
	}

	public static void InitList(Connection aConn) throws Exception {
		ObjectBase.CreateObject(aConn, MsiaEpfCalculator.class); // check if table already exist, if not create it
		Lookup.ClearAndLoadList(aConn, MsiaEpfCalculator.class, LookupList);
		EpfRegulation = (MsiaEpfCalculator) Lookup.InsertDefaultList(aConn, EpfRegulation, MsiaEpfCalculator.class, EpfRegulationDescr, LookupList);
		Epf2017_11Percent = (MsiaEpfCalculator) Lookup.InsertDefaultList(aConn, Epf2017_11Percent, MsiaEpfCalculator.class, Epf2017_11PercentDescr, LookupList);
		Epf2020_11Percent = (MsiaEpfCalculator) Lookup.InsertDefaultList(aConn, Epf2020_11Percent, MsiaEpfCalculator.class, Epf2020_11PercentDescr, LookupList);
		EpfNone = (MsiaEpfCalculator) Lookup.InsertDefaultList(aConn, EpfNone, MsiaEpfCalculator.class, EpfNoneDescr, LookupList);
	}

	public String getEpfType() throws Exception {
		return(this.getValueStr(Descr));
	}

	private static final String EPF_TABLE = Table.PREFIX_NON_ORM + "malaysia_epf";

	public final static int WAGE_FROM = 1;
	public final static int WAGE_TO = 3;
	public final static int CONTRIBUTION_BOSS = 4;
	public final static int CONTRIBUTION_WORKER = 5;

	public static void CreateEpfTable(Connection aConn) throws Exception {
		ObjectBase dbMaster = ((ObjectBase) aConn.getBaseDb());
		Table tblEpfRate = new Table(dbMaster, EPF_TABLE);
		if (dbMaster.tableExist(aConn, EPF_TABLE) == true) {
			throw new Hinderance("EPF table already exist: " + EPF_TABLE); // we will not allow this to run since it's taking too long time so just throw and let user knows
		} else {
			App.logInfo(MsiaEpfCalculator.class, "Creating table: " + EPF_TABLE);
			tblEpfRate.createField("YEAR", FieldType.STRING, 32);
			tblEpfRate.createField("TYPE", FieldType.STRING, 32);
			tblEpfRate.createField("WAGE_FROM", FieldType.STRING, 32);
			tblEpfRate.createField("WAGE_TO", FieldType.STRING, 32);
			tblEpfRate.createField("CONTRIBUTION_BOSS", FieldType.STRING, 32);
			tblEpfRate.createField("CONTRIBUTION_WORKER", FieldType.STRING, 32);

			dbMaster.createTable(aConn, tblEpfRate);
			App.logInfo(MsiaEpfCalculator.class, "Created table:  " + EPF_TABLE);
		}
	}

	public static void InsertEpfTable(Connection aConn, String aYear) throws Exception {
		App.logInfo(MsiaEpfCalculator.class, "Inserting EPF record for year: " + aYear + ", this will take few minutes...");
		String urlPath = MsiaEpfCalculator.class.getClassLoader().getResource("resources/" + "MalaysiaEpfSchedule" + aYear + ".txt").getPath();
		InputStream is = new FileInputStream(urlPath); 
		BufferedReader buf = new BufferedReader(new InputStreamReader(is));
		
		int totalInserted = 0;
		String line = buf.readLine(); 
		StringBuilder sb = new StringBuilder(); 
		ObjectBase dbMaster = ((ObjectBase) aConn.getBaseDb());
		Table tblEpfSchedule = new Table(dbMaster, EPF_TABLE);

		try {
			String scheduleType = "UNKNOWN";
			while(line != null) {
				sb.append(line).append("\n"); 
				line = buf.readLine(); 
				if (line == null) continue;

				String[] aryStr = line.split("\\s+");
				if (line.startsWith("Dari")) {
					Record record = new Record();
					record.createField("YEAR", FieldType.STRING, 32).setValueStr(aYear);
					record.createField("TYPE", FieldType.STRING, 32).setValueStr(scheduleType);
					CreateField(record, "WAGE_FROM", aryStr, WAGE_FROM);
					CreateField(record, "WAGE_TO", aryStr, WAGE_TO);
					CreateField(record, "CONTRIBUTION_BOSS", aryStr, CONTRIBUTION_BOSS);
					CreateField(record, "CONTRIBUTION_WORKER", aryStr, CONTRIBUTION_WORKER);

					tblEpfSchedule.insert(aConn, record); 
					totalInserted++;
				} else if (line.trim().toUpperCase().startsWith("BAHAGIAN")) {
					scheduleType = line.trim().toUpperCase().replaceAll(" ", "");
				}
			}
			App.logInfo(MsiaEpfCalculator.class, "Inserted a total of : " + totalInserted);
		} catch(Exception ex) {
			throw new Hinderance(ex, "Fail at line no: " + totalInserted + ", data: " + line);
		}
	}

	public static void IndexEpfTable(Connection aConn) throws Exception {
		ObjectBase dbMaster = ((ObjectBase) aConn.getBaseDb());
		Table tblEpfSchedule = new Table(dbMaster, EPF_TABLE);
		Field fieldYear = tblEpfSchedule.createField("YEAR", FieldType.STRING, 32);
		Field fieldType = tblEpfSchedule.createField("TYPE", FieldType.STRING, 32);
		Field fieldWageFrom = tblEpfSchedule.createField("WAGE_FROM", FieldType.STRING, 32);

		fieldYear.setObjectKey(true);
		fieldType.setObjectKey(true);
		fieldWageFrom.setObjectKey(true);

		fieldYear.setObjectKeyNo(0);
		fieldType.setObjectKeyNo(1);
		fieldWageFrom.setObjectKeyNo(2);

		Database.CreateIndex(aConn, tblEpfSchedule);
		App.logInfo(MsiaTaxCalculator.class, "Successfully created index for EPF table: " + EPF_TABLE);
	}

	public static Money GetEpfableAmount(Connection aConn, SalarySlip aPayslip) throws Exception {
		// total up epfable earnings, total up epfable deductions, epafableAmount = epfable earnings - epfable deductions
		return(SalarySlipMalaysia.GetAffectedAmount(aConn, aPayslip, SalarySlipMalaysia.AFFECT_EPF));
	}

	public static void GetEpfAmount(Connection aConn, Person aPerson, MsiaEpfCalculator aEpfCalculator, String aYear, SalarySlip aPayslip, Money aEpfBoss, Money aEpfWorker) throws Exception {
		Money epfableAmount = GetEpfableAmount(aConn, aPayslip);
		if (aEpfCalculator.getDescr().equals(EpfRegulation.getDescr())) {
			if (aYear.equals("2020") && (aPayslip.getPeriodFrom().isAfter(DateAndTime.StrToDateTime("31-Mar-2020", DateAndTime.FORMAT_DISPLAY_NO_TIME)) && aPayslip.getPeriodFrom().isBefore(DateAndTime.StrToDateTime("01-Jan-2021", DateAndTime.FORMAT_DISPLAY_NO_TIME)))) {
				GetEpfAmount(aConn, aPerson, aEpfCalculator, aYear, epfableAmount, aEpfBoss, aEpfWorker, 0);
				double workerEpf = (double) aEpfWorker.getValueDouble();
				double sevenPercent = 7/11D;
				aEpfWorker.setAmount(workerEpf * sevenPercent);
			} else {
				GetEpfAmount(aConn, aPerson, aEpfCalculator, aYear, epfableAmount, aEpfBoss, aEpfWorker, 0);
			}
		} else if (aEpfCalculator.getDescr().equals(EpfNone.getDescr())) { // if epf scheme is "No EPF" set all to zero and return
			aEpfBoss.setValue(epfableAmount.getCurrencyCode(), "0.00");
			aEpfWorker.setValue(epfableAmount.getCurrencyCode(), "0.00");
		} else if (aEpfCalculator.getDescr().equals(Epf2017_11Percent.getDescr())) {
			GetEpfAmount(aConn, aPerson, aEpfCalculator, "2018", epfableAmount, aEpfBoss, aEpfWorker, 0); // in 2017, regulation is 8% employee but user choose to retain 11% employee, so we use 2018 tax schedule
		} else if (aEpfCalculator.getDescr().equals(Epf2020_11Percent.getDescr()) && aYear.equals("2020")) {
			GetEpfAmount(aConn, aPerson, aEpfCalculator, aYear, epfableAmount, aEpfBoss, aEpfWorker, 0);
		} else {
			throw new Hinderance("No EPF Calculator have been specified");
		}
	}

	public static Double GetMinWage(Connection aConn, String aYear, String aScheduleType) throws Exception {
		Double result = null;
		String strSql;
		if (Database.GetDbType(aConn) == DbType.MYSQL || Database.GetDbType(aConn) == Database.DbType.ORACLE) {
			strSql = "select min(cast(replace(wage_from, ',', '') as decimal(10,2))) as wage_from from " + EPF_TABLE 
			+ " where cast(year as unsigned) = " + aYear
			+ " and type = '" + aScheduleType + "'";
		} else if (Database.GetDbType(aConn) == Database.DbType.POSTGRESQL) {
			strSql = "select min(cast(replace(wage_from, ',', '') as decimal(10,2))) as wage_from from " + EPF_TABLE 
			+ " where year::integer  = " + aYear
			+ " and type = '" + aScheduleType + "'";
		} else {
			throw new Hinderance("Unsupported DB type whilst getting the MIN wage bracket for malaysia EPF calculation");
		}
		Table tblTaxMin = new Table((ObjectBase) aConn.getBaseDb(), EPF_TABLE);
		tblTaxMin.createField("WAGE_FROM", FieldType.STRING, 32);
		tblTaxMin.fetch(aConn, strSql);
		if (tblTaxMin.getRecord(0) != null) {
			String minWage = tblTaxMin.getRecord(0).getField("WAGE_FROM").getValueStr();
			if (Generic.IsNumber(minWage)) {
				result = Double.parseDouble(minWage);
			}
		}
		
		if (result == null) {
			int intYear = Integer.parseInt(aYear) - 1;
			result = GetMinWage(aConn, String.valueOf(intYear), aScheduleType);
		}

		return(result);
	}

	private static void GetEpfAmount(Connection aConn, Person aPerson, MsiaEpfCalculator aEpfCalculator, String aYear, Money aBasicSalary, Money aEpfBoss, Money aEpfWorker, Integer aRecursiveDepth) throws Exception {
		if (Database.TableExist(aConn, EPF_TABLE) == false) {
			CreateEpfTable(aConn);
			InsertEpfTable(aConn, "2019");
			InsertEpfTable(aConn, "2018");
			InsertEpfTable(aConn, "2017");
			IndexEpfTable(aConn);
		}

		Double dblWage = aBasicSalary.getValueDouble();
		String strWage = String.valueOf(dblWage);

		// determine schedule type, e.g. for employee age above 60
		// determine if employee is foreigner
		String scheduleType = "BAHAGIANA";

		Double minEpfWage = GetMinWage(aConn, aYear, scheduleType); // if salary less then MYR10, no epf is needed
		if (dblWage < minEpfWage) {
			aEpfBoss.setValue(aBasicSalary.getCurrencyCode(), "0.00");
			aEpfWorker.setValue(aBasicSalary.getCurrencyCode(), "0.00");
		} else {
			Double maxEpfWage = GetMaxWage(aConn, aYear, scheduleType);
			if (dblWage > maxEpfWage) { // if salary is more then $20,000, use calculation instead of the schedule
				aEpfWorker.setAmount(dblWage * 0.11);
				aEpfBoss.setAmount(dblWage * 0.12);
			} else {
				String strSql;
				if (Database.GetDbType(aConn) == DbType.MYSQL || Database.GetDbType(aConn) == Database.DbType.ORACLE) {
					strSql = "select year, wage_from, wage_to, contribution_boss, contribution_worker"
					+ " from " + EPF_TABLE
					+ " where cast(replace(wage_from, ',', '') as decimal(10, 2)) <= " + strWage
					+ " and cast(replace(wage_to, ',', '') as decimal(10, 2)) >= " + strWage
					+ " and cast(year as unsigned) = " + aYear
					+ " and type = '" + scheduleType + "'";
				} else if (Database.GetDbType(aConn) == Database.DbType.POSTGRESQL) {
					strSql = "select year, wage_from, wage_to, contribution_boss, contribution_worker"
					+ " from " + EPF_TABLE
					+ " where cast(replace(wage_from, ',', '') as decimal(10, 2)) <= " + strWage
					+ " and cast(replace(wage_to, ',', '') as decimal(10, 2)) >= " + strWage
					+ " and year::integer = " + aYear
					+ " and type = '" + scheduleType + "'";
				} else {
					throw new Hinderance("Unsupported DB type whilst determining EPF amount");
				}


				// if payslip is for 2018 and Epf calculator is 2017 8% scheme, revert employee to 11% scheme

				Table tblEpfAmount = new Table((ObjectBase) aConn.getBaseDb(), EPF_TABLE);
				tblEpfAmount.createField("CONTRIBUTION_BOSS", FieldType.STRING, 32);
				tblEpfAmount.createField("CONTRIBUTION_WORKER", FieldType.STRING, 32);
				tblEpfAmount.fetch(aConn, strSql);
				if (tblEpfAmount.getRecord(0) != null) {
					String epfAmountBoss = tblEpfAmount.getRecord(0).getField("CONTRIBUTION_BOSS").getValueStr();
					String epfAmountWorker = tblEpfAmount.getRecord(0).getField("CONTRIBUTION_WORKER").getValueStr();
					aEpfBoss.setValue(aBasicSalary.getCurrencyCode(), epfAmountBoss);
					aEpfWorker.setValue(aBasicSalary.getCurrencyCode(), epfAmountWorker);
				} else {
					if (aRecursiveDepth >= MsiaTaxCalculator.MAX_PREVIOUS_TAX_TABLE_YEAR) {
						throw new Hinderance("Cannot find EPF for current and prior year, table: " + EPF_TABLE + ", year: " + aYear + ", wage from: " + dblWage + ", sql: " + strSql);
					} else {
						App.logWarn(MsiaTaxCalculator.class, "Fail to find EPF entry in table: " + EPF_TABLE + ", year: " + aYear + ", wage from: " + dblWage + ", using previous year");
						int intYear = Integer.parseInt(aYear) - 1;
						aRecursiveDepth++;
						GetEpfAmount(aConn, aPerson, aEpfCalculator, String.valueOf(intYear), aBasicSalary, aEpfBoss, aEpfWorker, aRecursiveDepth);
					}
				}
			}
		}
	}

	public static Double GetMaxWage(Connection aConn, String aYear, String aScheduleType) throws Exception {
		Double result = null;
		String strSql;
		if (Database.GetDbType(aConn) == DbType.MYSQL || Database.GetDbType(aConn) == Database.DbType.ORACLE) {
			strSql = "select max(cast(replace(wage_to, ',', '') as decimal(10,2))) as wage_to from " + EPF_TABLE 
			+ " where cast(year as unsigned) = " + aYear
			+ " and type = '" + aScheduleType + "'";
		} else if (Database.GetDbType(aConn) == Database.DbType.POSTGRESQL) {
			strSql = "select max(cast(replace(wage_to, ',', '') as decimal(10,2))) as wage_to from " + EPF_TABLE 
			+ " where year::integer = " + aYear
			+ " and type = '" + aScheduleType + "'";
		} else {
			throw new Hinderance("Unsupported DB type whilst getting the MAX wage bracket for malaysia EPF calculation");
		}
		Table tblTaxMax = new Table((ObjectBase) aConn.getBaseDb(), EPF_TABLE);
		tblTaxMax.createField("WAGE_TO", FieldType.STRING, 32);
		tblTaxMax.fetch(aConn, strSql);
		if (tblTaxMax.getRecord(0) != null) {
			String maxWage = tblTaxMax.getRecord(0).getField("WAGE_TO").getValueStr();
			if (Generic.IsNumber(maxWage)) {
				result = Double.parseDouble(maxWage);
			}
		} 
		
		if (result == null) {
			int intYear = Integer.parseInt(aYear) - 1;
			result = GetMaxWage(aConn, String.valueOf(intYear), aScheduleType);
		}

		return(result);
	}

	public static void main(String[] argv) {
		Connection conn = null;
		ObjectBase dbMaster = null;
		try {
			dbMaster = new ObjectBase();
			String[] args1 = { BaseDb.PROPERTY_LOCATION_APP };
			dbMaster.setupApp(args1);
			dbMaster.setupDb(); // setup connection pooling
			conn = dbMaster.getConnPool().getConnection();

			//CreateEpfTable(conn);
			InsertEpfTable(conn, "2019");
			//InsertEpfTable(conn, "2018");
			//InsertEpfTable(conn, "2017");
			//IndexEpfTable(conn);
		} catch(Exception ex) {
			App.logEror(0, new Hinderance(ex, "Application encounter fatal error, application is aborting...."));
		} finally {
			if (dbMaster != null && conn != null) {
				dbMaster.getConnPool().freeConnection(conn);
			}
		}
	}

}
