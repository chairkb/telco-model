package biz.shujutech.bznes;

import biz.shujutech.base.Connection;
import biz.shujutech.db.object.ObjectBase;
import biz.shujutech.db.relational.FieldType;
import biz.shujutech.db.relational.SortOrder;
import biz.shujutech.reflect.ReflectField;
import biz.shujutech.reflect.ReflectIndex;
import java.util.concurrent.CopyOnWriteArrayList;
import biz.shujutech.db.object.Lookup;

public class BankMalaysia extends CompanyMalaysia implements Lookup {
	@ReflectField(type=FieldType.STRING, size=64, indexes={@ReflectIndex(indexName="idx_bankmsia_descr", indexNo=0, indexOrder=SortOrder.ASC, isUnique=true)}) public static String Descr; 
	private static final CopyOnWriteArrayList<Lookup> LookupList = new CopyOnWriteArrayList<>();

	public static String AffinDescr = "Affin Bank Berhad";
	public static BankMalaysia Affin = null;
	
	public static String AllianceDescr = "Alliance Bank Malaysia Berhad";
	public static BankMalaysia Alliance = null;
	
	public static String AmBankDescr = "AmBank (M) Berhad";
	public static BankMalaysia AmBank = null;
	
	public static String CIMBDescr = "CIMB Bank Berhad";
	public static BankMalaysia CIMB = null;
	
	public static String HongDescr = "Hong Leong Bank Berhad";
	public static BankMalaysia Hong = null;
	
	public static String MalayanDescr = "Malayan Banking Berhad";
	public static BankMalaysia Malayan = null;
	
	public static String PublicDescr = "Public Bank Berhad";
	public static BankMalaysia Public = null;
	
	public static String RHBDescr = "RHB Bank Berhad";
	public static BankMalaysia RHB = null;
	
	public static String BNPDescr = "BNP Paribas Malaysia Berhad";
	public static BankMalaysia BNP = null;
	
	public static String BangkokDescr = "Bangkok Bank Berhad";
	public static BankMalaysia Bangkok = null;
	
	public static String BankOfAmericaDescr = "Bank of America Malaysia Berhad";
	public static BankMalaysia BankOfAmerica = null;
	
	public static String BankOfChinaDescr = "Bank of China (Malaysia) Berhad";
	public static BankMalaysia BankOfChina = null;
	
	public static String BankOfTokyoDescr = "Bank of Tokyo-Mitsubishi UFJ (Malaysia) Berhad";
	public static BankMalaysia BankOfTokyo = null;
	
	public static String ChinaDescr = "China Construction Bank (Malaysia) Berhad";
	public static BankMalaysia China = null;
	
	public static String CitibankDescr = "Citibank Berhad";
	public static BankMalaysia Citibank = null;
	
	public static String DeutscheDescr = "Deutsche Bank (Malaysia) Berhad";
	public static BankMalaysia Deutsche = null;
	
	public static String HSBCDescr = "HSBC Bank Malaysia Berhad";
	public static BankMalaysia HSBC = null;
	
	public static String IndiaDescr = "India International Bank (Malaysia) Berhad";
	public static BankMalaysia India = null;
	
	public static String IndustrialDescr = "Industrial and Commercial Bank of China (Malaysia) Berhad";
	public static BankMalaysia Industrial = null;
	
	public static String JPMorganDescr = "J.P. Morgan Chase Bank Berhad";
	public static BankMalaysia JPMorgan = null;
	
	public static String MizuhoDescr = "Mizuho Bank (Malaysia) Berhad";
	public static BankMalaysia Mizuho = null;
	
	public static String NationalDescr = "National Bank of Abu Dhabi Malaysia Berhad";
	public static BankMalaysia National = null;
	
	public static String OCBCDescr = "OCBC Bank (Malaysia) Berhad";
	public static BankMalaysia OCBC = null;
	
	public static String StandardDescr = "Standard Chartered Bank Malaysia Berhad";
	public static BankMalaysia Standard = null;
	
	public static String SumitomoDescr = "Sumitomo Mitsui Banking Corporation Malaysia Berhad";
	public static BankMalaysia Sumitomo = null;
	
	public static String TheDescr = "The Bank of Nova Scotia Berhad";
	public static BankMalaysia The = null;
	
	public static String UnitedDescr = "United Overseas Bank (Malaysia) Bhd";
	public static BankMalaysia United = null;
	
	

	@Override
	public void initialize(Connection aConn) throws Exception {
		// do nothing
	}
	
	@Override
	public String getValueStr() throws Exception {
		return(this.getDescr());
	}

	@Override
	public CopyOnWriteArrayList<Lookup> getLookupList() {
		return(LookupList);
	}

	public static void InitList(Connection aConn) throws Exception {
		ObjectBase.CreateObject(aConn, BankMalaysia.class); // check if table already exist, if not create it
		Lookup.ClearAndLoadList(aConn, BankMalaysia.class, LookupList);
		Malayan = (BankMalaysia) Lookup.InsertDefaultList(aConn, Malayan, BankMalaysia.class, MalayanDescr, LookupList);
		CIMB = (BankMalaysia) Lookup.InsertDefaultList(aConn, CIMB, BankMalaysia.class, CIMBDescr, LookupList);
		Affin = (BankMalaysia) Lookup.InsertDefaultList(aConn, Affin, BankMalaysia.class, AffinDescr, LookupList);
		Alliance = (BankMalaysia) Lookup.InsertDefaultList(aConn, Alliance, BankMalaysia.class, AllianceDescr, LookupList);
		AmBank = (BankMalaysia) Lookup.InsertDefaultList(aConn, AmBank, BankMalaysia.class, AmBankDescr, LookupList);
		CIMB = (BankMalaysia) Lookup.InsertDefaultList(aConn, CIMB, BankMalaysia.class, CIMBDescr, LookupList);
		Hong = (BankMalaysia) Lookup.InsertDefaultList(aConn, Hong, BankMalaysia.class, HongDescr, LookupList);
		Malayan = (BankMalaysia) Lookup.InsertDefaultList(aConn, Malayan, BankMalaysia.class, MalayanDescr, LookupList);
		Public = (BankMalaysia) Lookup.InsertDefaultList(aConn, Public, BankMalaysia.class, PublicDescr, LookupList);
		RHB = (BankMalaysia) Lookup.InsertDefaultList(aConn, RHB, BankMalaysia.class, RHBDescr, LookupList);
		BNP = (BankMalaysia) Lookup.InsertDefaultList(aConn, BNP, BankMalaysia.class, BNPDescr, LookupList);
		Bangkok = (BankMalaysia) Lookup.InsertDefaultList(aConn, Bangkok, BankMalaysia.class, BangkokDescr, LookupList);
		BankOfAmerica = (BankMalaysia) Lookup.InsertDefaultList(aConn, BankOfAmerica, BankMalaysia.class, BankOfAmericaDescr, LookupList);
		BankOfChina = (BankMalaysia) Lookup.InsertDefaultList(aConn, BankOfChina, BankMalaysia.class, BankOfChinaDescr, LookupList);
		BankOfTokyo = (BankMalaysia) Lookup.InsertDefaultList(aConn, BankOfTokyo, BankMalaysia.class, BankOfTokyoDescr, LookupList);
		China = (BankMalaysia) Lookup.InsertDefaultList(aConn, China, BankMalaysia.class, ChinaDescr, LookupList);
		Citibank = (BankMalaysia) Lookup.InsertDefaultList(aConn, Citibank, BankMalaysia.class, CitibankDescr, LookupList);
		Deutsche = (BankMalaysia) Lookup.InsertDefaultList(aConn, Deutsche, BankMalaysia.class, DeutscheDescr, LookupList);
		HSBC = (BankMalaysia) Lookup.InsertDefaultList(aConn, HSBC, BankMalaysia.class, HSBCDescr, LookupList);
		India = (BankMalaysia) Lookup.InsertDefaultList(aConn, India, BankMalaysia.class, IndiaDescr, LookupList);
		Industrial = (BankMalaysia) Lookup.InsertDefaultList(aConn, Industrial, BankMalaysia.class, IndustrialDescr, LookupList);
		JPMorgan = (BankMalaysia) Lookup.InsertDefaultList(aConn, JPMorgan, BankMalaysia.class, JPMorganDescr, LookupList);
		Mizuho = (BankMalaysia) Lookup.InsertDefaultList(aConn, Mizuho, BankMalaysia.class, MizuhoDescr, LookupList);
		National = (BankMalaysia) Lookup.InsertDefaultList(aConn, National, BankMalaysia.class, NationalDescr, LookupList);
		OCBC = (BankMalaysia) Lookup.InsertDefaultList(aConn, OCBC, BankMalaysia.class, OCBCDescr, LookupList);
		Standard = (BankMalaysia) Lookup.InsertDefaultList(aConn, Standard, BankMalaysia.class, StandardDescr, LookupList);
		Sumitomo = (BankMalaysia) Lookup.InsertDefaultList(aConn, Sumitomo, BankMalaysia.class, SumitomoDescr, LookupList);
		The = (BankMalaysia) Lookup.InsertDefaultList(aConn, The, BankMalaysia.class, TheDescr, LookupList);
		United = (BankMalaysia) Lookup.InsertDefaultList(aConn, United, BankMalaysia.class, UnitedDescr, LookupList);
		
	}
}


