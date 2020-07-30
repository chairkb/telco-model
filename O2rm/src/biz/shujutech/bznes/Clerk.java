package biz.shujutech.bznes;

import biz.shujutech.db.relational.FieldType;
import biz.shujutech.reflect.ReflectField;

public class Clerk extends Person {
	@ReflectField(type=FieldType.BOOLEAN) public static String IsTemp;
	
	public Clerk() {
		super();
	}

	public void setIsTemp(Boolean aValue) throws Exception {
		this.setValueBoolean(IsTemp, aValue);
	}

	public String getIsTemp() throws Exception {
		return(this.getValueStr(IsTemp));
	}

}
