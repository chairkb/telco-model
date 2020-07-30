package biz.shujutech.bznes;

import biz.shujutech.db.object.ObjectBase;
import biz.shujutech.base.App;
import biz.shujutech.base.Connection;
import biz.shujutech.base.Hinderance;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class BillElectric extends Bill {

	public BillElectric() {
		super();
	}

	public static void main(String args[]) {
		Connection conn = null;
		ObjectBase objectDatabase = null;
		try {
			objectDatabase = new ObjectBase();
			objectDatabase.setupApp(args);
			objectDatabase.setupDb();
			conn = objectDatabase.getConnPool().getConnection();

			Country.InitList(conn);

			/**
			 * Testing inserting abstract class object into the database
			 */
			int TOTAL_TEST_RECORD = 2;
			{
				App.logInfo("Starting to test insertion of object into the database");
				for (int cntr = 0; cntr < TOTAL_TEST_RECORD; cntr++) {
					BillElectric billTest = (BillElectric) objectDatabase.createObject(BillElectric.class);
	
					billTest.setBillNo("BillNo" + cntr);
					billTest.setBillDesc("Bill description for bill: " + cntr);
					billTest.getBillAmt().setValue(Country.Malaysia.getCurrencyCode(), 6.12);
					ObjectBase.PersistCommit(conn, billTest);
					App.logInfo("Inserted 'bill' object into database, bill name: " + billTest.getBillNo());
				}
			}

			/**
			 * Testing fetching the inserted abstract class object
			 */
			{
				App.logInfo("Starting to test fetching of object from the database");
				PreparedStatement stmt = null;
				ResultSet rset = null;
				try {
					rset = objectDatabase.fetchAllByChrono(BillElectric.class, stmt);
					BillElectric billFetched = null;
					while((billFetched = (BillElectric) objectDatabase.fetchNext(BillElectric.class, rset)) != null) {
						App.logInfo(billFetched.asString(true));
					}
				} finally {
					if (rset != null) rset.close();
					if (stmt != null) stmt.close();
				}
			}

			/**
			 * Testing updating the inserted abstract class object
			 */
			{
				App.logInfo("Starting to test update member object from the database and save");
				PreparedStatement stmt = null;
				ResultSet rset = null;
				try {
					rset = objectDatabase.fetchAllByChrono(BillElectric.class, stmt);
					BillElectric billFetched = null;
					while((billFetched = (BillElectric) objectDatabase.fetchNext(BillElectric.class, rset)) != null) {
						billFetched.getBillAmt().setValue(Country.Usa.getCurrencyCode(), 200.3485);
						ObjectBase.PersistCommit(conn, billFetched);
						App.logInfo("Successfully updated object bill amount instant variable: " + billFetched.getObjectId());
					}
				} finally {
					if (rset != null) rset.close();
					if (stmt != null) stmt.close();
				}
			}


			/**
			 * Testing deleting the inserted abstract class object
			 */
			{
				App.logInfo("Starting to test delete object, the member object is ignore");
				PreparedStatement stmt = null;
				ResultSet rset = null;
				try {
					rset = objectDatabase.fetchAllByChrono(BillElectric.class, stmt);
					BillElectric billFetched = null;
					while((billFetched = (BillElectric) objectDatabase.fetchNext(BillElectric.class, rset)) != null) {
						ObjectBase.DeleteCommit(conn, billFetched);
						App.logInfo("Successfully deleted object bill with amount instant variable: " + billFetched.getObjectId());
					}
				} finally {
					if (rset != null) rset.close();
					if (stmt != null) stmt.close();
				}
			}
		} catch (Exception ex) {
			App.logEror(0, new Hinderance(ex, "Application encounter fatal error, application is aborting...."));
		} finally {
			objectDatabase.getConnPool().freeConnection(conn);
		}
	}
}
