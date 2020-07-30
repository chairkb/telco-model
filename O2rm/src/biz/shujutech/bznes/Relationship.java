package biz.shujutech.bznes;

import biz.shujutech.base.Connection;
import biz.shujutech.base.Hinderance;
import biz.shujutech.db.object.Clasz;
import biz.shujutech.db.object.ObjectBase;
import biz.shujutech.db.relational.FieldType;
import biz.shujutech.db.relational.SortOrder;
import java.util.concurrent.CopyOnWriteArrayList;
import biz.shujutech.reflect.ReflectField;
import biz.shujutech.reflect.ReflectIndex;
import biz.shujutech.db.object.Lookup;

public class Relationship extends Clasz implements Lookup {
	@ReflectField(type=FieldType.STRING, size=32, indexes={@ReflectIndex(indexName="idx_rlstp_descr", indexNo=0, indexOrder=SortOrder.ASC, isUnique=true)}) public static String Descr;  // short Descr gives error, its mysql reserve word
	public static CopyOnWriteArrayList<Lookup> LookupList = new CopyOnWriteArrayList<>();

	public static String HusbandDescr = "Husband";
	public static Relationship Husband= null;
	public static String WifeDescr = "Wife";
	public static Relationship Wife= null;
	public static String SonDescr = "Son";
	public static Relationship Son = null;
	public static String DaughterDescr = "Daughter";
	public static Relationship Daughter = null;
	public static String BrotherDescr = "Brother";
	public static Relationship Brother = null;
	public static String SisterDescr = "Sister";
	public static Relationship Sister = null;
	public static String MotherDescr = "Mother";
	public static Relationship Mother = null;
	public static String FatherDescr = "Father";
	public static Relationship Father = null;
	public static String AuntyDescr = "Aunty";
	public static Relationship Aunty = null;
	public static String UncleDescr = "Uncle";
	public static Relationship Uncle = null;
	public static String NieceDescr = "Niece";
	public static Relationship Niece = null;
	public static String CousinDescr = "Cousin";
	public static Relationship Cousin = null;
	public static String FriendDescr = "Friend";
	public static Relationship Friend = null;
	public static String OthersDescr = "Others";
	public static Relationship Others = null;

	@Override
	public void initialize(Connection aConn) throws Exception {
		// do nothing
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
	public String getValueStr() throws Exception {
		return(this.getDescr());
	}

	@Override
	public CopyOnWriteArrayList<Lookup> getLookupList() {
		return(LookupList);
	}

	public static void InitList(Connection aConn) throws Exception {
		ObjectBase.CreateObject(aConn, Relationship.class); // check if table already exist, if not create it
		Lookup.ClearAndLoadList(aConn, Relationship.class, LookupList);

		Husband = (Relationship) Lookup.InsertDefaultList(aConn, Husband, Relationship.class, HusbandDescr, LookupList);
		Wife = (Relationship) Lookup.InsertDefaultList(aConn, Wife, Relationship.class, WifeDescr, LookupList);
		Son = (Relationship) Lookup.InsertDefaultList(aConn, Son, Relationship.class, SonDescr, LookupList);
		Daughter = (Relationship) Lookup.InsertDefaultList(aConn, Daughter, Relationship.class, DaughterDescr, LookupList);
		Brother = (Relationship) Lookup.InsertDefaultList(aConn, Brother, Relationship.class, BrotherDescr, LookupList);
		Sister = (Relationship) Lookup.InsertDefaultList(aConn, Sister, Relationship.class, SisterDescr, LookupList);
		Mother = (Relationship) Lookup.InsertDefaultList(aConn, Mother, Relationship.class, MotherDescr, LookupList);
		Father = (Relationship) Lookup.InsertDefaultList(aConn, Father, Relationship.class, FatherDescr, LookupList);
		Aunty = (Relationship) Lookup.InsertDefaultList(aConn, Aunty, Relationship.class, AuntyDescr, LookupList);
		Uncle = (Relationship) Lookup.InsertDefaultList(aConn, Uncle, Relationship.class, UncleDescr, LookupList);
		Niece = (Relationship) Lookup.InsertDefaultList(aConn, Niece, Relationship.class, NieceDescr, LookupList);
		Cousin = (Relationship) Lookup.InsertDefaultList(aConn, Cousin, Relationship.class, CousinDescr, LookupList);
		Friend = (Relationship) Lookup.InsertDefaultList(aConn, Friend, Relationship.class, FriendDescr, LookupList);
		Others = (Relationship) Lookup.InsertDefaultList(aConn, Others, Relationship.class, OthersDescr, LookupList);
	}
	
	public static Lookup GetFromList(String aLookupName) throws Exception {
		Lookup result = null;
		boolean notInit = false;

		if (Son == null) { notInit = true; }
		else if (Husband == null) { notInit = true; }
		else if (Wife == null) { notInit = true; }
		else if (Daughter == null) { notInit = true; }
		else if (Brother == null) { notInit = true; }
		else if (Sister == null) { notInit = true; }
		else if (Mother == null) { notInit = true; }
		else if (Father == null) { notInit = true; }
		else if (Aunty == null) { notInit = true; }
		else if (Uncle == null) { notInit = true; }
		else if (Niece == null) { notInit = true; }
		else if (Cousin == null) { notInit = true; }
		else if (Friend == null) { notInit = true; }
		else if (Others == null) { notInit = true; }

		if (notInit) {
			throw new Hinderance("The InitList method of Relationship must be called first");
		} else {
			if (aLookupName.toLowerCase().equals(SonDescr.toLowerCase())) { result = Son; }
			else if (aLookupName.toLowerCase().equals(HusbandDescr.toLowerCase())) { result = Husband; }
			else if (aLookupName.toLowerCase().equals(WifeDescr.toLowerCase())) { result = Wife; }
			else if (aLookupName.toLowerCase().equals(DaughterDescr.toLowerCase())) { result = Daughter; }
			else if (aLookupName.toLowerCase().equals(BrotherDescr.toLowerCase())) { result = Brother; }
			else if (aLookupName.toLowerCase().equals(SisterDescr.toLowerCase())) { result = Sister; }
			else if (aLookupName.toLowerCase().equals(MotherDescr.toLowerCase())) { result = Mother; }
			else if (aLookupName.toLowerCase().equals(FatherDescr.toLowerCase())) { result = Father; }
			else if (aLookupName.toLowerCase().equals(AuntyDescr.toLowerCase())) { result = Aunty; }
			else if (aLookupName.toLowerCase().equals(UncleDescr.toLowerCase())) { result = Uncle; }
			else if (aLookupName.toLowerCase().equals(NieceDescr.toLowerCase())) { result = Niece; }
			else if (aLookupName.toLowerCase().equals(CousinDescr.toLowerCase())) { result = Cousin; }
			else if (aLookupName.toLowerCase().equals(FriendDescr.toLowerCase())) { result = Friend; }
			else if (aLookupName.toLowerCase().equals(OthersDescr.toLowerCase())) { result = Others; }
		}
		return(result);
	}

	/*
	public Lookup getSelectedObj(String aLookupName) throws Exception {
		return(GetFromList(aLookupName));
	}
	*/
}
