package biz.shujutech.bznes;

import biz.shujutech.base.App;
import biz.shujutech.base.Connection;
import biz.shujutech.base.Hinderance;
import biz.shujutech.db.object.Clasz;
import biz.shujutech.db.object.FieldObjectBox;
import biz.shujutech.db.object.ObjectBase;
import biz.shujutech.db.relational.FieldType;
import biz.shujutech.db.relational.SortOrder;
import java.text.Collator;
import java.util.Comparator;
import java.util.Currency;
import java.util.Locale;
import java.util.concurrent.CopyOnWriteArrayList;
import biz.shujutech.reflect.ReflectField;
import biz.shujutech.reflect.ReflectIndex;
import org.joda.time.DateTime;
import biz.shujutech.db.object.Lookup;
 
/**
 * http://www.kodejava.org/examples/126.html
 * 
 */
public class Country extends Clasz implements Lookup {
	@ReflectField(type=FieldType.STRING, size=64, indexes={@ReflectIndex(indexName="idx_ctry_descr", indexNo=0, indexOrder=SortOrder.ASC, isUnique=true)}) public static String Descr;
	@ReflectField(type=FieldType.STRING, size=8, indexes={@ReflectIndex(indexName="idx_ctry_ccc", indexNo=0, indexOrder=SortOrder.ASC, isUnique=true)}) public static String CountryCallingCode;
	@ReflectField(type=FieldType.OBJECTBOX, deleteAsMember=true, clasz="biz.shujutech.bznes.NetworkDestinationCode", prefetch=true) public static String Ndc; 
	@ReflectField(type=FieldType.OBJECTBOX, deleteAsMember=true, clasz="biz.shujutech.bznes.AreaCode", prefetch=true) public static String AreaCode; 
	@ReflectField(type=FieldType.OBJECTBOX, deleteAsMember=true, clasz="biz.shujutech.bznes.State", prefetch=true, lookup=true) public static String State; 
	@ReflectField(type=FieldType.OBJECTBOX, deleteAsMember=true, clasz="biz.shujutech.bznes.Holiday") public static String Holiday; 

	public String iso3;
	public String code;

	public Currency currency;

	public String dollarName = "Dollars";
	public String centName = "Cents";

	public static CopyOnWriteArrayList<Lookup> LookupList = new CopyOnWriteArrayList<>();

	public static String MalaysiaDescr = "Malaysia";
	public static Country Malaysia = null;

	public static String SingaporeDescr = "Singapore";
	public static Country Singapore = null;

	public static String UsaDescr = "United States"; // this name must use iso name
	public static Country Usa = null;

	public static String VietnamDescr = "Vietnam"; // this name must use iso name
	public static Country Vietnam = null;

	public static String ChinaDescr = "China"; // this name must use iso name
	public static Country China = null;

	public static String IndonesiaDescr = "Indonesia"; // this name must use iso name
	public static Country Indonesia = null;

	public static String MyanmarDescr = "Myanmar"; // this name must use iso name
	public static Country Myanmar = null;

	public Country() {
		super();
	}

	public Country(String aIso) throws Exception {
		try {
			this.setLocaleInfo(aIso);
		} catch(Exception ex) {
			throw new Hinderance(ex, "Cannot create COUNTRY object from country code: " + aIso.toUpperCase());
		}
	}
 
	@Override
	public String getValueStr() throws Exception {
		return(this.getValueStr(Descr));
	}

	@Override
	public String getDescr() throws Exception {
		return(this.getValueStr(Descr));
	}

	@Override
	public void setDescr(String aDescr) throws Exception {
		this.setValueStr(Descr, aDescr);
	}

	@Override
	public CopyOnWriteArrayList<Lookup> getLookupList() {
		return(LookupList);
	}

	@Override
	public void initialize(Connection aConn) throws Exception {
		this.initialize();
		if (this.getValueStr().equals(Country.MalaysiaDescr)) {
			this.setCountryCallingCode("+60");
			this.addNdc(aConn, "(0)10");
			this.addNdc(aConn, "(0)11");
			this.addNdc(aConn, "(0)12");
			this.addNdc(aConn, "(0)13");
			this.addNdc(aConn, "(0)14");
			this.addNdc(aConn, "(0)16");
			this.addNdc(aConn, "(0)17");
			this.addNdc(aConn, "(0)18");
			this.addNdc(aConn, "(0)19");
			this.addAreaCode(aConn, "(0)3");
			this.addAreaCode(aConn, "(0)4");
			this.addAreaCode(aConn, "(0)5");
			this.addAreaCode(aConn, "(0)6");
			this.addAreaCode(aConn, "(0)7");
			this.addState(aConn, "Wilayah Perseketuan");
			this.addState(aConn, "Penang");
			this.addState(aConn, "Sarawak");
			this.addState(aConn, "Selangor");
			this.addState(aConn, "Negeri Sembilan");
			this.addState(aConn, "Melaka");
			this.addState(aConn, "Pahang");
			this.addState(aConn, "Johor");
			this.addState(aConn, "Terengganu");
			this.addState(aConn, "Sabah");
			this.addState(aConn, "Perak");
			this.addState(aConn, "Perlis");
			this.addState(aConn, "Kedah");
			this.addState(aConn, "Kelantan");
			this.addState(aConn, "Wilayah Putrajaya");
			this.addState(aConn, "Wilayah Labuan");
		} else if (this.getValueStr().equals(Country.SingaporeDescr)) {
			this.setCountryCallingCode("+65");
			this.addNdc(aConn, "(0)8");
			this.addNdc(aConn, "(0)9");
			this.addState(aConn, "Singapore");
		} else if (this.getValueStr().equals(Country.UsaDescr)) {
			this.setCountryCallingCode("+1");
			this.addState(aConn, "Alabama ");
			this.addState(aConn, "Alaska ");
			this.addState(aConn, "Arizona ");
			this.addState(aConn, "Arkansas ");
			this.addState(aConn, "California ");
			this.addState(aConn, "Colorado ");
			this.addState(aConn, "Connecticut ");
			this.addState(aConn, "Delaware ");
			this.addState(aConn, "Florida ");
			this.addState(aConn, "Georgia ");
			this.addState(aConn, "Hawaii ");
			this.addState(aConn, "Idaho ");
			this.addState(aConn, "Illinois Indiana ");
			this.addState(aConn, "Iowa ");
			this.addState(aConn, "Kansas ");
			this.addState(aConn, "Kentucky ");
			this.addState(aConn, "Louisiana ");
			this.addState(aConn, "Maine ");
			this.addState(aConn, "Maryland ");
			this.addState(aConn, "Massachusetts ");
			this.addState(aConn, "Michigan ");
			this.addState(aConn, "Minnesota ");
			this.addState(aConn, "Mississippi ");
			this.addState(aConn, "Missouri ");
			this.addState(aConn, "Montana Nebraska ");
			this.addState(aConn, "Nevada ");
			this.addState(aConn, "New Hampshire ");
			this.addState(aConn, "New Jersey ");
			this.addState(aConn, "New Mexico ");
			this.addState(aConn, "New York ");
			this.addState(aConn, "North Carolina ");
			this.addState(aConn, "North Dakota ");
			this.addState(aConn, "Ohio ");
			this.addState(aConn, "Oklahoma ");
			this.addState(aConn, "Oregon ");
			this.addState(aConn, "Pennsylvania Rhode Island ");
			this.addState(aConn, "South Carolina ");
			this.addState(aConn, "South Dakota ");
			this.addState(aConn, "Tennessee ");
			this.addState(aConn, "Texas ");
			this.addState(aConn, "Utah ");
			this.addState(aConn, "Vermont ");
			this.addState(aConn, "Virginia ");
			this.addState(aConn, "Washington ");
			this.addState(aConn, "West Virginia ");
			this.addState(aConn, "Wisconsin ");
			this.addState(aConn, "Wyoming");
		} else if (this.getValueStr().equals(Country.VietnamDescr)) {
		} else {
			throw new Hinderance("Fail to create instant of the class COUNTRY for: " + this.getValueStr());
		}
	}

	public void initialize() throws Exception {
		if (this.getValueStr().equals(Country.MalaysiaDescr)) {
			this.setLocaleInfo("MYS");
			this.setDollarName("Ringgit");
			this.setCentName("Sen");
		} else if (this.getValueStr().equals(Country.SingaporeDescr)) {
			this.setLocaleInfo("SGP");
			this.setDollarName("Dollars");
			this.setCentName("Cents");
		} else if (this.getValueStr().equals(Country.UsaDescr)) {
			this.setLocaleInfo("USA");
			this.setDollarName("Dollars");
			this.setCentName("Cents");
		} else if (this.getValueStr().equals(Country.VietnamDescr)) {
			this.setLocaleInfo("VNM");
			this.setDollarName("Dong");
			this.setCentName("Xu");
		} else {
			throw new Hinderance("Fail to create instant of the class COUNTRY for: " + this.getValueStr());
		}
	}

	public static CopyOnWriteArrayList<Lookup> getCountryList() {
		return(LookupList);
	}

	public static Country getCountryByCurrencyCode(String aCurrencyCode) {
		Country result = null;
		if (aCurrencyCode.equals(Malaysia.getCurrencyCode())) {
			result = Malaysia;
		} else if (aCurrencyCode.equals(Singapore.getCurrencyCode())) {
			result = Singapore;
		} else if (aCurrencyCode.equals(Usa.getCurrencyCode())) {
			result = Usa;
		} else if (aCurrencyCode.equals(Vietnam.getCurrencyCode())) {
			result = Vietnam;
		}
		return(result);
	}

	public static void InitList(Connection aConn) throws Exception {
		ObjectBase.CreateObject(aConn, Country.class);
		Lookup.ClearAndLoadList(aConn, Country.class, LookupList);
		Malaysia = (Country) Lookup.InsertDefaultList(aConn, Malaysia, Country.class, MalaysiaDescr, LookupList);
		Singapore = (Country) Lookup.InsertDefaultList(aConn, Singapore, Country.class, SingaporeDescr, LookupList);
		//Vietnam = (Country) Lookup.InsertDefaultList(aConn, Vietnam, Country.class, VietnamDescr, LookupList);
	}

	public Currency getCurrency() {
		return currency;
	}

	public void setCurrency(Currency currency) {
		this.currency = currency;
	}

	public String getCurrencySymbol() {
		return(this.getCurrency().getSymbol());
	}

	public String getCurrencyCode() {
		return(this.getCurrency().getCurrencyCode());
	}

	public String getDollarName() {
		return dollarName;
	}

	public void setDollarName(String dollarName) {
		this.dollarName = dollarName;
	}

	public String getCentName() {
		return centName;
	}

	public void setCentName(String centName) {
		this.centName = centName;
	}

	public String getIso() throws Exception {
		return(this.iso3);
	}

	public void setIso(String iso) throws Exception {
		this.iso3 = iso;
	}

	public String getCode() throws Exception {
		return(this.code);
	}

	public void setCode(String code) throws Exception {
		this.code = code;
	}

	public String getName() throws Exception {
		return(this.getValueStr(Descr));
	}

	public void setName(String name) throws Exception {
		this.setValueStr(Descr, name);
	}
 
	public void setLocaleInfo(String aIso) throws Exception {
		boolean found = false;
		String[] isoCountries = Locale.getISOCountries();
		for (String country : isoCountries) {
			Locale locale = new Locale("en", country);
			if (locale.getISO3Country().equals(aIso)) {
				this.setIso(locale.getISO3Country());
				this.setCode(locale.getCountry());
				this.setName(locale.getDisplayCountry());

				Currency tmpCurrency = Currency.getInstance(locale);
				if (tmpCurrency != null) {
					this.setCurrency(tmpCurrency);
				}

				found = true;
				break;
			}
		}

		if (found == false) {
			throw new Hinderance("Fail to create instant of the class COUNTRY");
		}
	}

	static class CountryComparator implements Comparator<Country> {
			private Comparator comparator;
 
			CountryComparator() {
				comparator = Collator.getInstance();
			}
 
			@SuppressWarnings("unchecked")
			public int compare(Country c1, Country c2) {
				int result = 0;
				try {
					result = comparator.compare(c1.getName(), c2.getName());
				} catch(Exception ex) {
					try {
						App.logEror(ex, "Fail to compare country: " + c1.getName() + " and " + c2.getName());
					} catch(Exception exc) {
					}
				}
				return(result);
			}
	}

	public static Country getCountry(Connection aConn, String aIso) throws Exception {
		Country result = (Country) ObjectBase.CreateObject(aConn, Country.class);
		result.setLocaleInfo(aIso);
		return(result);
	}

	public static Country GetCountryByName(Connection aConn, String aName) throws Exception {
		Country result = null;
		for(Lookup eachLookup : Country.LookupList) {
			Country eachCountry = (Country) eachLookup;
			if (eachCountry.getName().equals(aName)) {
				result = eachCountry;
				break;
			}
		}
		return(result);
	}

	public String getCountryCallingCode() throws Exception {
		return(this.getValueStr(CountryCallingCode));
	}

	public String getCountryCallingDesc() throws Exception {
		String result = this.getDescr() + " " + this.getCountryCallingCode();
		return(result);
	}

	public void setCountryCallingCode(String aCode) throws Exception {
		this.setValueStr(CountryCallingCode, aCode);
	}

	public void addNdc(Connection aConn, String aCode) throws Exception {
		NetworkDestinationCode dbNdc = (NetworkDestinationCode) ObjectBase.CreateObject(aConn, NetworkDestinationCode.class);
		dbNdc.setNdc(aCode);
		if (dbNdc.populate(aConn) == false) {
			this.addValueObject(aConn, Ndc, dbNdc);
		}
	}

	public FieldObjectBox getNdc() throws Exception {
		return((FieldObjectBox) this.getField(Ndc));
	}

	public NetworkDestinationCode getNdc(int aIndex) throws Exception {
		FieldObjectBox fob = (FieldObjectBox) this.getField(Ndc);
		NetworkDestinationCode ndc = (NetworkDestinationCode) fob.getObject(aIndex);
		return(ndc);
	}

	public void addAreaCode(Connection aConn, String aCode) throws Exception {
		AreaCode dbAreaCode = (AreaCode) ObjectBase.CreateObject(aConn, AreaCode.class);
		dbAreaCode.setAreaCode(aCode);
		if (dbAreaCode.populate(aConn) == false) {
			this.addValueObject(aConn, AreaCode, dbAreaCode);
		}
	}

	public FieldObjectBox getAreaCode() throws Exception {
		return((FieldObjectBox) this.getField(AreaCode));
	}

	public AreaCode getAreaCode(int aIndex) throws Exception {
		FieldObjectBox fob = (FieldObjectBox) this.getField(AreaCode);
		AreaCode areaCode = (AreaCode) fob.getObject(aIndex);
		return(areaCode);
	}

	public void addState(Connection aConn, String aState) throws Exception {
		State dbState = (State) ObjectBase.CreateObject(aConn, State.class);
		dbState.setDescr(aState);
		if (dbState.populate(aConn) == false) {
			this.addValueObject(aConn, State, dbState);
		}
		Lookup.Add2LookupList(dbState);
		dbState.addCities(aConn);
	}

	public State getState(Connection aConn, String aState) throws Exception {
		State result = null;
		this.getFieldObjectBox(State).resetIterator(); // always do a reset before starting to loop for the objects
		while(this.getFieldObjectBox(State).hasNext(aConn)) {
			State eachState = (State) this.getFieldObjectBox(State).getNext();
			if (eachState.getDescr().trim().toUpperCase().endsWith(aState.trim().toUpperCase())) {
				result = eachState;
				break;
			}
		}
		return (result);
	}

	public FieldObjectBox getState() throws Exception {
		return((FieldObjectBox) this.getField(State));
	}

	public State getState(int aIndex) throws Exception {
		FieldObjectBox fob = (FieldObjectBox) this.getField(State);
		State areaCode = (State) fob.getObject(aIndex);
		return(areaCode);
	}

	public FieldObjectBox getHoliday() throws Exception {
		return((FieldObjectBox) this.getField(Holiday));
	}

	public void addHoliday(Connection aConn, Holiday aHoliday) throws Exception {
		this.addValueObject(aConn, Holiday, aHoliday);
	}

	public Holiday getHoliday(Connection aConn, DateTime aHolidayDate) throws Exception {
		return (biz.shujutech.bznes.Holiday.GetHoliday(aConn, this, aHolidayDate));
	}

	public static void main(String[] args) {
		try {
			String[] isoCountries = Locale.getISOCountries();
			for (String country : isoCountries) {
				Locale locale = new Locale("en", country);
				Currency currency = Currency.getInstance(locale);
				String strCurrency = "";
				if (currency != null) {
					strCurrency = currency.getCurrencyCode();
				}
				App.logInfo(locale.getISO3Country() + " - " + locale.getCountry() + " - " + locale.getDisplayCountry() + " - " + strCurrency);
			}

			ObjectBase objectDb = new ObjectBase();
			objectDb.setupApp(args);
			objectDb.setupDb();
			Connection conn = objectDb.getConnPool().getConnection();

			Country country = Country.getCountry(conn, "MYS");
			App.logInfo(country.getIso() + " - " + country.getCode() + " - " + country.getName().toUpperCase() + " - " + country.getCurrencySymbol());

			Country.InitList(conn);
			Country sgp = (Country) Country.getCountryList().get(1);
			App.logInfo(sgp.getIso() + " - " + sgp.getCode() + " - " + sgp.getName().toUpperCase() + " - " + sgp.getCurrencySymbol());
			objectDb.getConnPool().freeConnection(conn);
		} catch(Exception ex) {
			App.logEror(new Hinderance(ex, "System is aborting"));
		}
	}
}



