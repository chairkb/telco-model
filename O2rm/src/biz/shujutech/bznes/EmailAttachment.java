package biz.shujutech.bznes;

import biz.shujutech.db.object.Clasz;
import biz.shujutech.db.relational.FieldType;
import biz.shujutech.reflect.ReflectField;

public class EmailAttachment extends Clasz {
	@ReflectField(type=FieldType.STRING, size=64, displayPosition=10) public static String Name;
	@ReflectField(type=FieldType.STRING, size=64, displayPosition=10) public static String Type;
	@ReflectField(type=FieldType.BASE64, displayPosition=20) public static String Attachment;

	public String getAttachment() throws Exception {
		return((String) this.getValueStr(Attachment));
	}

	public void setAttachment(String aAttachment) throws Exception {
		this.setValueStr(Attachment, aAttachment);
	}

	public String getName() throws Exception {
		return((String) this.getValueStr(Name));
	}

	public void setName(String aName) throws Exception {
		this.setValueStr(Name, aName);
	}

	public String getType() throws Exception {
		return((String) this.getValueStr(Type));
	}

	public void setType(String aType) throws Exception {
		this.setValueStr(Type, aType);
	}
	
}
