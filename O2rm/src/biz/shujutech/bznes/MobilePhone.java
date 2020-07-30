package biz.shujutech.bznes;

import biz.shujutech.db.object.Clasz;
import biz.shujutech.db.relational.FieldType;
import biz.shujutech.reflect.ReflectField;

public class MobilePhone extends Clasz {
	@ReflectField(type=FieldType.STRING, size=32) public static String CountryCallingCode;
	@ReflectField(type=FieldType.STRING, size=8) public static String Ndc; // national destination code or number planning area
	@ReflectField(type=FieldType.STRING, size=32) public static String SubscrNo; 


	public String getCountryCallingCode() throws Exception {
		String result = this.getValueStr(CountryCallingCode);
		return(result);
	}

	public void setCountryCallingCode(String aCode) throws Exception {
		this.setValueStr(CountryCallingCode, aCode);
	}

	public String getNdc() throws Exception {
		String result = this.getValueStr(Ndc);
		return(result);
	}

	public void setNdc(String a_Ndc) throws Exception {
		this.setValueStr(Ndc, a_Ndc);
	}

	public String getSubscrNo() throws Exception {
		String result = this.getValueStr(SubscrNo);
		return(result);
	}

	public void setSubscrNo(String a_SubscrNo) throws Exception {
		this.setValueStr(SubscrNo, a_SubscrNo);
	}

	public void setMobileNo(String aCountryCode, String aNdc, String aSubscrNo) throws Exception {
		this.setCountryCallingCode(aCountryCode);
		this.setNdc(aNdc);
		this.setSubscrNo(aSubscrNo);
	}

	@Override
	public String getValueStr() throws Exception {
		String result = "";
		if (this.getSubscrNo().isEmpty() == false) {
			result = this.getCountryCallingCode() + "-" + this.getNdc() + "-" + this.getSubscrNo();
		}
		return(result);
	}

	@Override
	public String asString() throws Exception {
		String result = "+";
		result += this.getCountryCallingCode();
		result += "-";
		result += this.getNdc();
		result += "-";
		result += this.getSubscrNo();
		return(result);
	}
	
	/**
	 * Malaysia +60-(0)17-16319388
	 * 
	 * @param aValue
	 * @return
	 * @throws Exception 
	 */
	@Override
	public void setValueStr(String aValue) throws Exception {
		String[] phoneParts = aValue.split("-");
		String countryName = phoneParts[0];
		String ndc = phoneParts[1];
		String subscrNo = phoneParts[2];
		this.setMobileNo(countryName, ndc, subscrNo);
	}
}
