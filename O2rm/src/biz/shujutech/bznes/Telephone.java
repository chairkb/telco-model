package biz.shujutech.bznes;

import biz.shujutech.db.object.Clasz;
import biz.shujutech.db.relational.FieldType;
import biz.shujutech.reflect.ReflectField;

public class Telephone extends Clasz {
	@ReflectField(type=FieldType.STRING, size=32) public static String CountryCallingCode;
	@ReflectField(type=FieldType.STRING, size=8) public static String AreaCode; // national destination code or number planning area
	@ReflectField(type=FieldType.STRING, size=32) public static String SubscrNo; 

	public String getCountryCallingCode() throws Exception {
		String result = this.getValueStr(CountryCallingCode);
		return(result);
	}

	public void setCountryCallingCode(String aCode) throws Exception {
		this.setValueStr(CountryCallingCode, aCode);
	}

	public String getAreaCode() throws Exception {
		String result = this.getValueStr(AreaCode);
		return(result);
	}

	public void setAreaCode(String aAreaCode) throws Exception {
		this.setValueStr(AreaCode, aAreaCode);
	}

	public String getSubscrNo() throws Exception {
		String result = this.getValueStr(SubscrNo);
		return(result);
	}

	public void setSubscrNo(String aSubscrNo) throws Exception {
		this.setValueStr(SubscrNo, aSubscrNo);
	}

	public void setTelephoneNo(String aCountryCode, String aAreaCode, String aSubscrNo) throws Exception {
		this.setCountryCallingCode(aCountryCode);
		this.setAreaCode(aAreaCode);
		this.setSubscrNo(aSubscrNo);
	}

	@Override
	public String getValueStr() throws Exception {
		String result = "";
		if (this.getSubscrNo().isEmpty() == false) {
			result = this.getCountryCallingCode() + "-" + this.getAreaCode() + "-" + this.getSubscrNo();
		}
		return(result);
	}

	@Override
	public String asString() throws Exception {
		String result = "+";
		result += this.getCountryCallingCode();
		result += "-";
		result += this.getAreaCode();
		result += "-";
		result += this.getSubscrNo();
		return(result);
	}

	@Override
	public void setValueStr(String aValue) throws Exception {
		String[] phoneParts = aValue.split("-");
		String countryName = phoneParts[0];
		String ndc = phoneParts[1];
		String subscrNo = phoneParts[2];
		this.setTelephoneNo(countryName, ndc, subscrNo);
	}
	
}
