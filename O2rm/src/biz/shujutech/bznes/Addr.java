package biz.shujutech.bznes;

import biz.shujutech.db.object.Clasz;
import biz.shujutech.db.relational.FieldType;
import biz.shujutech.reflect.ReflectField;
import biz.shujutech.db.object.Lookup;

public class Addr extends Clasz {
	@ReflectField(type=FieldType.STRING, size=32, displayPosition=5) public static String Addr1;
	@ReflectField(type=FieldType.STRING, size=32, displayPosition=10) public static String Addr2;
	@ReflectField(type=FieldType.STRING, size=32, displayPosition=15) public static String Addr3;
	@ReflectField(type=FieldType.STRING, size=8, displayPosition=20) public static String PostalCode;
	@ReflectField(type=FieldType.OBJECT, deleteAsMember=false, clasz="biz.shujutech.bznes.Country", displayPosition=35, prefetch=true, lookup=true) public static String Country; 
	@ReflectField(type=FieldType.OBJECT, deleteAsMember=false, clasz="biz.shujutech.bznes.State", displayPosition=40, prefetch=true, lookup=true) public static String State; 
	@ReflectField(type=FieldType.OBJECT, deleteAsMember=false, clasz="biz.shujutech.bznes.City", displayPosition=45, prefetch=true, lookup=true) public static String City; 

	public Lookup addrType;

	public void setType(Lookup addrType) {
		this.addrType = addrType;
	}

	public Lookup getType() {
		return(this.addrType);
	}

	public void setAddr1(String aValue) throws Exception {
		this.setValueStr(Addr1, aValue);
	}

	public String getAddr1() throws Exception {
		return(this.getValueStr(Addr1));
	}

	public void setAddr2(String aValue) throws Exception {
		this.setValueStr(Addr2, aValue);
	}

	public String getAddr2() throws Exception {
		return(this.getValueStr(Addr2));
	}

	public void setAddr3(String aValue) throws Exception {
		this.setValueStr(Addr3, aValue);
	}

	public String getAddr3() throws Exception {
		return(this.getValueStr(Addr3));
	}

	public void setPostalCode(String aValue) throws Exception {
		this.setValueStr(PostalCode, aValue);
	}

	public String getPostalCode() throws Exception {
		return(this.getValueStr(PostalCode));
	}

	public void setCity(String aValue) throws Exception {
		City city = (City) Lookup.GetListItem(aValue, biz.shujutech.bznes.City.LookupList);
		this.setCity(city);
	}

	public void setCity(City aValue) throws Exception {
		this.setValueObject(City, aValue);
	}

	public City getCity() throws Exception {
		return((City) this.getValueObject(City));
	}

	public void setState(String aValue) throws Exception {
		State state = (State) Lookup.GetListItem(aValue, biz.shujutech.bznes.State.LookupList);
		this.setState(state);
	}

	public void setState(Clasz aValue) throws Exception {
		this.setValueObject(State, aValue);
	}

	public State getState() throws Exception {
		return((State) this.getValueObject(State));
	}

	public void setCountry(Country aValue) throws Exception {
		this.setValueObject(Country, aValue);
	}

	public Country getCountry() throws Exception {
		return((Country) this.getValueObject(Country));
	}

	@Override
	public String getValueStr() throws Exception {
		String result = this.getAddr1() + CommaTheValue(this.getAddr2()) + CommaTheValue(this.getAddr3());
		result += CommaTheValue(this.getPostalCode()) + CommaTheValue(this.getCity().getValueStr()) + CommaTheValue(this.getState().getValueStr()) + CommaTheValue(this.getCountry().getValueStr());
		return(result);
	}

	public static String CommaTheValue(String aValue) {
		if (aValue != null && aValue.isEmpty() == false) {
			return(", " + aValue);
		} else {
			return("");
		}
	}

	/*
	@Override
	public void createBasicField(Connection conn) throws Exception {
		Country country = (Country) this.getDb().CreateObject(Country.class, conn);
		this.setCountry(country);
		State state = (State) this.getDb().CreateObject(State.class, conn);
		this.setState(state);
	}
	*/
	
}
