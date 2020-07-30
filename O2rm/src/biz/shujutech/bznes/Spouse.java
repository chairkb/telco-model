package biz.shujutech.bznes;

import biz.shujutech.db.relational.FieldType;
import biz.shujutech.reflect.ReflectField;

public class Spouse extends Person {
	
	@ReflectField(type=FieldType.BOOLEAN, displayPosition=36) public static String IsWorking;
	
	public Spouse() {
		super();
	}

	public Boolean isWorking() throws Exception {
		return(this.getValueBoolean(IsWorking));
	}

	public void isWorking(boolean aIsWorking) throws Exception {
		this.setValueBoolean(IsWorking, aIsWorking);
	}

	/*
	@Override
	public void createBasicField(Connection conn) throws Exception {
		this.createContactField(conn);
		this.removeField(MaritalStatus);
		this.removeField(NoOfChildren);
		this.removeField(Employment);
		this.removeField(Children);
		this.removeField(Spouse);
		this.removeField(EmergencyContact);
	}
	*/

}
