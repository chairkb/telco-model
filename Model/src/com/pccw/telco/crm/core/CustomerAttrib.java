package com.pccw.telco.crm.core;

import biz.shujutech.base.App;
import biz.shujutech.base.Connection;
import biz.shujutech.base.Hinderance;
import biz.shujutech.db.object.Clasz;
import biz.shujutech.db.object.ObjectBase;
import biz.shujutech.db.relational.BaseDb;
import biz.shujutech.db.relational.FieldType;
import biz.shujutech.reflect.ReflectField;

public class CustomerAttrib extends Clasz {
	@ReflectField(type=FieldType.STRING, size=32, displayPosition=5) public static String Label;
	@ReflectField(type=FieldType.STRING, size=64, displayPosition=10) public static String Description;
	@ReflectField(type=FieldType.OBJECT, deleteAsMember=false, clasz="com.pccw.ui.Widget", lookup=true, displayPosition=15) public static String WidgetType;
	@ReflectField(type=FieldType.OBJECT, deleteAsMember=false, clasz="com.pccw.telco.crm.core.CustomerPanel", lookup=true, displayPosition=20) public static String FormPanel;
	@ReflectField(type=FieldType.BOOLEAN, displayPosition=25) public static String Mandatory;
	@ReflectField(type=FieldType.BOOLEAN, displayPosition=30) public static String Visible;







	public static final String PROPERTY_LOCATION_APP = "C:\\Users\\USER4\\pccw\\crm-ddl\\Model\\src\\com\\pccw\\telco\\crm\\core\\telco-core.properties";
	public static void main(String[] args) throws Exception {
		ObjectBase objectDb = new ObjectBase();
		Connection conn = null;
		try {
			String[] args1 = { PROPERTY_LOCATION_APP };
			objectDb.setupApp(args1);
			objectDb.setupDb();
			conn = objectDb.getConnPool().getConnection();

			String INIT_SCHEMA = "INIT_SCHEMA";
			String CREATE_MODEL = "CREATE_MODEL";

			String actionType = INIT_SCHEMA;

			if (actionType.equals(INIT_SCHEMA)) {

				CustomerAttrib customerAttrib;
				customerAttrib = (CustomerAttrib) ObjectBase.CreateObject(conn, CustomerAttrib.class);
				
			}

		} catch (Exception ex) {
			App.logEror(new Hinderance(ex, "Application encounter fatal error, application is aborting...."));
		} finally {
			if (conn != null) {
				objectDb.getConnPool().freeConnection(conn);
			}
		}		
	}

	
}
