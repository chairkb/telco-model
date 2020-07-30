package biz.shujutech.payroll;

import biz.shujutech.base.App;
import biz.shujutech.base.Connection;
import biz.shujutech.base.Hinderance;
import biz.shujutech.bznes.Money;
import biz.shujutech.db.object.ObjectBase;
import biz.shujutech.db.relational.BaseDb;
import biz.shujutech.db.relational.Database;
import biz.shujutech.db.relational.Database.DbType;
import biz.shujutech.db.relational.FieldType;
import biz.shujutech.db.relational.Record;
import biz.shujutech.db.relational.Table;
import static biz.shujutech.payroll.MsiaTaxCalculator.CreateField;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

public class MsiaEisCalculator {
	private static final String EIS_TABLE = Table.PREFIX_NON_ORM + "malaysia_eis";

	public final static int WAGE_TO = 0;
	public final static int CONTRIBUTION_BOSS = 1;
	public final static int CONTRIBUTION_WORKER = 2;

	public static void GetEisAmount(Connection aConn, String aYear, SalarySlip aPayslip, Money aEisBoss, Money aEisWorker) throws Exception {
		int numYear = Integer.parseInt(aYear);
		if (numYear >= 2018) {
			Money eisableAmount = GetEisableAmount(aConn, aPayslip);
			GetEisAmount(aConn, aYear, eisableAmount, aEisBoss, aEisWorker, 0);
		}
	}

	public static Money GetEisableAmount(Connection aConn, SalarySlip aPayslip) throws Exception {
		return(SalarySlipMalaysia.GetAffectedAmount(aConn, aPayslip, SalarySlipMalaysia.AFFECT_EIS));
	}

	private static void GetEisAmount(Connection aConn, String aYear, Money aEisableAmount, Money aEisBoss, Money aEisWorker, Integer aRecursiveDepth) throws Exception {
		if (Database.TableExist(aConn, EIS_TABLE) == false) {
			CreateEisTable(aConn);
			InsertEisTable(aConn, "2018");
		}

		Double dblWage = aEisableAmount.getValueDouble();
		String strWage = String.valueOf(dblWage);

		String strSql;
		if (Database.GetDbType(aConn) == DbType.MYSQL || Database.GetDbType(aConn) == Database.DbType.ORACLE) {
			strSql = "select year, wage_to, contribution_boss, contribution_worker from " + EIS_TABLE + " where cast(wage_to as unsigned) = ("
			+ "select max(cast(wage_to as unsigned)) from " + EIS_TABLE + " where cast(wage_to as unsigned) < " + strWage + ")";
		} else if (Database.GetDbType(aConn) == Database.DbType.POSTGRESQL) {
			strSql = "select year, wage_to, contribution_boss, contribution_worker from " + EIS_TABLE + " where wage_to::numeric = ("
			+ "select max(wage_to::numeric) from " + EIS_TABLE + " where wage_to::numeric::integer < " + strWage + ")";
		} else {
			throw new Hinderance("Unsupported DB type whilst determining EIS amount");
		}

		Table tblEisAmount = new Table((ObjectBase) aConn.getBaseDb(), EIS_TABLE);
		tblEisAmount.createField("YEAR", FieldType.STRING, 32);
		tblEisAmount.createField("WAGE_TO", FieldType.STRING, 32);
		tblEisAmount.createField("CONTRIBUTION_BOSS", FieldType.STRING, 32);
		tblEisAmount.createField("CONTRIBUTION_WORKER", FieldType.STRING, 32);
		tblEisAmount.fetch(aConn, strSql);
		if (tblEisAmount.getRecord(0) == null) { // no match, maybe eis-able-amount is too small
			if (Database.GetDbType(aConn) == DbType.MYSQL || Database.GetDbType(aConn) == Database.DbType.ORACLE) {
				strSql = "select year, wage_to, contribution_boss, contribution_worker from " + EIS_TABLE + " where cast(wage_to as unsigned) = ("
				+ "select min(cast(wage_to as unsigned)) from " + EIS_TABLE + ")";
			} else if (Database.GetDbType(aConn) == Database.DbType.POSTGRESQL) {
				strSql = "select year, wage_to, contribution_boss, contribution_worker from " + EIS_TABLE + " where wage_to::numeric = ("
				+ "select min(wage_to::numeric) from " + EIS_TABLE + ")";
			} else {
				throw new Hinderance("Unsupported DB type whilst determining EIS amount");
			}

			tblEisAmount.fetch(aConn, strSql);
			if (tblEisAmount.getRecord(0) == null) {
				throw new Hinderance("Fail to determine EIS amount from schedule: " + EIS_TABLE + ", for salary: " + aEisableAmount.getMoneyStr());
			}
		}

		String eisAmountBoss = tblEisAmount.getRecord(0).getField("CONTRIBUTION_BOSS").getValueStr();
		String eisAmountWorker = tblEisAmount.getRecord(0).getField("CONTRIBUTION_WORKER").getValueStr();
		aEisBoss.setValue(aEisableAmount.getCurrencyCode(), eisAmountBoss);
		aEisWorker.setValue(aEisableAmount.getCurrencyCode(), eisAmountWorker);
	}
	
	public static void CreateEisTable(Connection aConn) throws Exception {
		ObjectBase dbMaster = ((ObjectBase) aConn.getBaseDb());
		Table tblEisSchedule = new Table(dbMaster, EIS_TABLE);
		if (dbMaster.tableExist(aConn, EIS_TABLE) == true) {
			throw new Hinderance("EIS table already exist: " + EIS_TABLE);
		} else {
			App.logInfo(MsiaEisCalculator.class, "Creating table: " + EIS_TABLE);
			tblEisSchedule.createField("YEAR", FieldType.STRING, 32);
			tblEisSchedule.createField("WAGE_TO", FieldType.STRING, 32);
			tblEisSchedule.createField("CONTRIBUTION_BOSS", FieldType.STRING, 32);
			tblEisSchedule.createField("CONTRIBUTION_WORKER", FieldType.STRING, 32);

			dbMaster.createTable(aConn, tblEisSchedule);
			App.logInfo(MsiaEisCalculator.class, "Created table:  " + EIS_TABLE);
		}
	}
	
	public static void InsertEisTable(Connection aConn, String aYear) throws Exception {
		App.logInfo(MsiaEisCalculator.class, "Inserting EIS record for year: " + aYear + ", this will take few minutes...");
		String urlPath = MsiaEisCalculator.class.getClassLoader().getResource("resources/" + "MalaysiaEisSchedule" + aYear + ".txt").getPath();
		InputStream is = new FileInputStream(urlPath); 
		BufferedReader buf = new BufferedReader(new InputStreamReader(is));
		
		int totalInserted = 0;
		String line = buf.readLine(); 
		StringBuilder sb = new StringBuilder(); 
		ObjectBase dbMaster = ((ObjectBase) aConn.getBaseDb());
		Table tblEisSchedule = new Table(dbMaster, EIS_TABLE);

		try {
			while(line != null) {
				sb.append(line).append("\n"); 
				line = buf.readLine(); 
				if (line == null) continue;

				String[] aryStr = line.split("\\s+");

				Record record = new Record();
				record.createField("YEAR", FieldType.STRING, 32).setValueStr(aYear);
				CreateField(record, "WAGE_TO", aryStr, WAGE_TO);
				CreateField(record, "CONTRIBUTION_BOSS", aryStr, CONTRIBUTION_BOSS);
				CreateField(record, "CONTRIBUTION_WORKER", aryStr, CONTRIBUTION_WORKER);

				tblEisSchedule.insert(aConn, record); 
				totalInserted++;
			}
			App.logInfo(MsiaEisCalculator.class, "Inserted a total of : " + totalInserted);
		} catch(Exception ex) {
			throw new Hinderance(ex, "Fail at line no: " + totalInserted + ", data: " + line);
		}
	}

	public static void main(String[] argv) throws Exception {
		Connection conn = null;
		ObjectBase dbMaster = null;
		try {
			dbMaster = new ObjectBase();
			String[] args1 = { BaseDb.PROPERTY_LOCATION_APP };
			dbMaster.setupApp(args1);
			dbMaster.setupDb(); // setup connection pooling
			conn = dbMaster.getConnPool().getConnection();

			CreateEisTable(conn); // make sure the text file numbers do not have comma, else need to use replace(',', '') before cast
			InsertEisTable(conn, "2018");
		} catch(Exception ex) {
			App.logEror(0, new Hinderance(ex, "Application encounter fatal error, application is aborting...."));
		} finally {
			if (dbMaster != null && conn != null) {
				dbMaster.getConnPool().freeConnection(conn);
			}
		}
	}

}
