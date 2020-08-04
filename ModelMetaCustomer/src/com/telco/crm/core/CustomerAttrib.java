package com.telco.crm.core;

import biz.shujutech.base.App;
import biz.shujutech.base.Connection;
import biz.shujutech.base.Hinderance;
import biz.shujutech.db.object.Clasz;
import biz.shujutech.db.object.ObjectBase;
import biz.shujutech.db.relational.BaseDb;
import biz.shujutech.db.relational.FieldType;
import biz.shujutech.reflect.ReflectField;
import com.telco.crm.util.JsonProcessor;
import com.ui.Panel;
import com.ui.WidgetType;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class CustomerAttrib extends Clasz {
	@ReflectField(type=FieldType.DATETIME, displayPosition=100) public static String CreateAt;
	@ReflectField(type=FieldType.STRING, size=64, displayPosition=100) public static String CreateBy;
	@ReflectField(type=FieldType.DATETIME, displayPosition=100) public static String UpdateAt;
	@ReflectField(type=FieldType.STRING, size=64, displayPosition=100) public static String UpdateBy;

	@ReflectField(type=FieldType.STRING, size=32, displayPosition=5) public static String Label;
	@ReflectField(type=FieldType.STRING, size=64, displayPosition=10) public static String Description;
	@ReflectField(type=FieldType.OBJECT, deleteAsMember=false, clasz="com.ui.WidgetType", lookup=true, displayPosition=15) public static String Type;
	@ReflectField(type=FieldType.OBJECT, deleteAsMember=false, clasz="com.telco.crm.core.CustomerPanel", lookup=true, displayPosition=20) public static String Panel;
	@ReflectField(type=FieldType.BOOLEAN, displayPosition=25) public static String Mandatory;
	@ReflectField(type=FieldType.BOOLEAN, displayPosition=30) public static String Visible;
	@ReflectField(type=FieldType.BOOLEAN, displayPosition=100) public static String Deleted;


	public void setLabel(String aValue) throws Exception {
		this.setValueStr(Label, aValue);
	}

	public void setDescription(String aValue) throws Exception {
		this.setValueStr(Description, aValue);
	}

	public void setWidgetType(WidgetType aValue) throws Exception {
		this.setValueObject(Type, aValue);
	}

	public void setFormPanel(Panel aValue) throws Exception {
		this.setValueObject(Panel, aValue);
	}

	public void setMandatory(boolean aValue) throws Exception {
		this.setValueBoolean(Mandatory, aValue);
	}

	public void setVisible(boolean aValue) throws Exception {
		this.setValueBoolean(Visible, aValue);
	}

	//public static final String PROPERTY_LOCATION_APP = "C:\\Users\\User8\\github\\telco-model\\Model\\src\\com\\telco\\crm\\config\\telco.properties";
	public static final String PROPERTY_LOCATION_APP = "modeller.properties";
	public static void main(String[] args) throws Exception {
		ObjectBase objectDb = new ObjectBase();
		Connection conn = null;
		try {
			String[] args1 = { PROPERTY_LOCATION_APP };
			objectDb.setupApp(args1);
			objectDb.setupDb();
			conn = objectDb.getConnPool().getConnection();

			InitList(conn);

			//CustomerAttrib customerAttrib;
			//customerAttrib = (CustomerAttrib) ObjectBase.CreateObject(conn, CustomerAttrib.class);
			PopulateAttrib(conn);
				

		} catch (Exception ex) {
			App.logEror(new Hinderance(ex, "Application encounter fatal error, application is aborting...."));
		} finally {
			if (conn != null) {
				objectDb.getConnPool().freeConnection(conn);
			}
		}		
	}

	public static void InitList(Connection aConn) throws Exception {
		WidgetType.InitList(aConn);
		CustomerPanel.InitList(aConn);
	}

	public static void PopulateAttrib(Connection aConn) throws Exception {
		List<String> allRow = new CopyOnWriteArrayList<String>();
		allRow.add("Title,Salutation,Drop Down,Customer,Y,Y");
		allRow.add("First Name,First Name,Text Field,Customer,Y,Y");
		allRow.add("Last Name,Last Name,Text Field,Customer,Y,Y");
		allRow.add("Registered Mobile Number,Main registered mobile number registered to the telco company,Number,Customer,Y,Y");
		allRow.add("Country Code,Country code of phone number,Drop Down,Contact,Y,Y");
		allRow.add("UEN,Customer identification card number,Text Field,Customer,Y,Y");
		allRow.add("Customer Type,Customer Type,Drop Down,Customer,Y,Y");
		allRow.add("Preferred Contact Method,Preferred Contact Method,Tickbox,Contact,N,Y");
		allRow.add("Contact Type,List from contact type,Drop Down,Contact,N,Y");
		allRow.add("Contact Number,Contact Number,Number,Contact,N,Y");
		allRow.add("Email Address,Email Address,Text Field,Contact,N,Y");
		allRow.add("Address Type,Address Type,Drop Down,Address,Y,Y");
		allRow.add("Address Line 1,Address Line 1,Text Field,Address,Y,Y");
		allRow.add("Address Line 2,Address Line 2,Text Field,Address,N,Y");
		allRow.add("Postcode,Postcode,Number,Address,Y,Y");

		for(String eachStrRow : allRow) {
			String[] allField = eachStrRow.split(",");
			int colIdx = 0;
			CustomerAttrib eachRow = (CustomerAttrib) ObjectBase.CreateObject(aConn, CustomerAttrib.class);
			eachRow.setLabel(allField[colIdx++]);
			eachRow.setDescription(allField[colIdx++]);
			eachRow.setWidgetType(Str2Widget(allField[colIdx++]));
			eachRow.setFormPanel(Str2Panel(allField[colIdx++]));
			eachRow.setMandatory(Str2Bool(allField[colIdx++]));
			eachRow.setVisible(Str2Bool(allField[colIdx++]));

			eachRow.persistCommit(aConn);
			String strRow = JsonProcessor.AsJsonStrObj(aConn, eachRow);
			System.out.println(strRow);
		}
	}

	public static boolean Str2Bool(String aValue) {
		if (aValue.toLowerCase().trim().equals("y")) {
			return true;
		} else {
			return false;
		}
	}

	public static CustomerPanel Str2Panel(String aValue) throws Exception {
		CustomerPanel result = null;
		if (aValue.toLowerCase().trim().startsWith(CustomerPanel.CustomerDescr.toLowerCase())) {
			result = CustomerPanel.Customer;
		} else if (aValue.toLowerCase().trim().startsWith(CustomerPanel.ContactDescr.toLowerCase())) {
			result = CustomerPanel.Contact;
		} else if (aValue.toLowerCase().trim().startsWith(CustomerPanel.AddressDescr.toLowerCase())) {
			result = CustomerPanel.Address;
		} else {
			result = null;
		}
		return(result);
	}

	public static WidgetType Str2Widget(String aValue) throws Exception {
		WidgetType result = null;
		if (aValue.toLowerCase().trim().startsWith(WidgetType.DropDownDescr.toLowerCase())) {
			result = WidgetType.DropDown;
		} else if (aValue.toLowerCase().trim().startsWith(WidgetType.TextFieldDescr.toLowerCase())) {
			result = WidgetType.TextField;
		} else if (aValue.toLowerCase().trim().startsWith(WidgetType.NumberFieldDescr.toLowerCase())) {
			result = WidgetType.NumberField;
		} else if (aValue.toLowerCase().trim().startsWith(WidgetType.TickboxDescr.toLowerCase())) {
			result = WidgetType.Tickbox;
		} else {
			result = null;
		}
		return(result);
	}
}
