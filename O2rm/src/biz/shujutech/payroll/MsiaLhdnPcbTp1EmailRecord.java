package biz.shujutech.payroll;

import biz.shujutech.db.object.Clasz;
import biz.shujutech.db.relational.FieldType;
import biz.shujutech.intf.EmailSentRecord;
import biz.shujutech.reflect.ReflectField;
import org.joda.time.DateTime;

public class MsiaLhdnPcbTp1EmailRecord extends Clasz implements EmailSentRecord {
	@ReflectField(type=FieldType.DATETIME, displayPosition=10) public static String SentDate;
	@ReflectField(type=FieldType.STRING, displayPosition=20, size=128) public static String EmailAddress;

	@Override
	public DateTime getSentDate() throws Exception {
		return (this.getValueDate(SentDate));
	}

	@Override
	public void setSentDate(DateTime aSendDate) throws Exception {
		this.setValueDateTime(SentDate, aSendDate);
	}

	@Override
	public String getEmailAddress() throws Exception {
		return (this.getValueStr(EmailAddress));
	}

	@Override
	public void setEmailAddress(String aEmailAddress) throws Exception {
		this.setValueStr(EmailAddress, aEmailAddress);
	}
}
