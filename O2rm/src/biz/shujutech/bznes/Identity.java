package biz.shujutech.bznes;

import biz.shujutech.base.Connection;
import biz.shujutech.base.Hinderance;
import biz.shujutech.db.object.Clasz;
import biz.shujutech.db.object.ObjectBase;
import biz.shujutech.db.relational.FieldType;
import biz.shujutech.reflect.ReflectField;

public class Identity extends Clasz {
	public static final String IDENTITY_NRIC = "NRIC";
	public static final String IDENTITY_PR = "Permanent Resident";
	public static final String IDENTITY_PASSPORT = "Passport";

	@ReflectField(type=FieldType.STRING, size=20, displayPosition=10, changeable=false) public static String IssuingCountry; 
	@ReflectField(type=FieldType.STRING, size=32, displayPosition=10, changeable=false) public static String IdentityName; 

	public String getIssuingCountry() throws Exception {
		return(this.getValueStr(IssuingCountry));
	}

	public void setIssuingCountry(String aNo) throws Exception {
		this.setValueStr(IssuingCountry, aNo);
	}

	public String getIdentityName() throws Exception {
		return(this.getValueStr(IdentityName));
	}

	public void setIdentityName(String aNo) throws Exception {
		this.setValueStr(IdentityName, aNo);
	}

	public static Identity CreateIdentificationByCountry(Connection aConn, IdentityType aIdentityType) throws Exception {
		Identity idCardOrPassport = null;
		if (aIdentityType.equals(IdentityType.MalaysiaIc)) {
			idCardOrPassport = (MalaysiaIdentityCard) ObjectBase.CreateObject(aConn, MalaysiaIdentityCard.class);
			idCardOrPassport.getField(MalaysiaIdentityCard.NewIdentityCardNo).forRemove(false);
			idCardOrPassport.getField(MalaysiaIdentityCard.OldIdentityCardNo).forRemove(false);
		} else if (aIdentityType.equals(IdentityType.MalaysiaPr)) {
			idCardOrPassport = (MalaysiaPermanentResident) ObjectBase.CreateObject(aConn, MalaysiaPermanentResident.class);
			idCardOrPassport.getField(MalaysiaPermanentResident.PermanentResidentNo).forRemove(false);
			idCardOrPassport.getField(MalaysiaPermanentResident.CountryOfOrigin).forRemove(false);
		} else if (aIdentityType.equals(IdentityType.ChinaPassport)) {
			idCardOrPassport = (ChinaPassport) ObjectBase.CreateObject(aConn, ChinaPassport.class);
			idCardOrPassport.getField(ChinaPassport.PassportNo).forRemove(false);
		} else if (aIdentityType.equals(IdentityType.IndiaPassport)) {
			idCardOrPassport = (IndiaPassport) ObjectBase.CreateObject(aConn, IndiaPassport.class);
			idCardOrPassport.getField(IndiaPassport.PassportNo).forRemove(false);
		} else if (aIdentityType.equals(IdentityType.VietnamPassport)) {
			idCardOrPassport = (VietnamPassport) ObjectBase.CreateObject(aConn, VietnamPassport.class);
			idCardOrPassport.getField(VietnamPassport.PassportNo).forRemove(false);
		} else if (aIdentityType.equals(IdentityType.IndonesiaPassport)) {
			idCardOrPassport = (IndonesiaPassport) ObjectBase.CreateObject(aConn, IndonesiaPassport.class);
			idCardOrPassport.getField(IndonesiaPassport.PassportNo).forRemove(false);
		} else if (aIdentityType.equals(IdentityType.MyanmarPassport)) {
			idCardOrPassport = (MyanmarPassport) ObjectBase.CreateObject(aConn, MyanmarPassport.class);
			idCardOrPassport.getField(MyanmarPassport.PassportNo).forRemove(false);
		} else if (aIdentityType.equals(IdentityType.SingaporePassport)) {
			idCardOrPassport = (SingaporePassport) ObjectBase.CreateObject(aConn, SingaporePassport.class);
			idCardOrPassport.getField(SingaporePassport.PassportNo).forRemove(false);
		} else {
			throw new Hinderance("Unsupported domicile identity type");
		}

		idCardOrPassport.getField(Identity.IssuingCountry).forRemove(false);
		idCardOrPassport.getField(Identity.IdentityName).forRemove(false);
		idCardOrPassport.removeMarkField();
		return(idCardOrPassport);
	}
}
