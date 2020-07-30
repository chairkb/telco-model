package biz.shujutech.bznes;

import biz.shujutech.base.Connection;
import biz.shujutech.base.Hinderance;
import biz.shujutech.db.relational.FieldType;
import biz.shujutech.db.relational.SortOrder;
import biz.shujutech.reflect.ReflectField;
import biz.shujutech.reflect.ReflectIndex;

public class MalaysiaIdentityCard extends Identity implements Nric {
	@ReflectField(type=FieldType.STRING, size=20, indexes={@ReflectIndex(indexName="idx_msian_ic", indexNo=0, indexOrder=SortOrder.ASC, isUnique=true)}, displayPosition=205, updateable=false, mask="######-##-####") public static String NewIdentityCardNo;
	@ReflectField(type=FieldType.STRING, size=20, displayPosition=200) public static String OldIdentityCardNo;

	@Override
	public void initBeforePopulate() throws Exception {
		this.setIssuingCountry("Malaysia");
		this.setIdentityName("MyKad");
	}

	public String getOldIdentityCardNo() throws Exception {
		return(this.getValueStr(OldIdentityCardNo));
	}

	public void setOldIdentityCardNo(String aNo) throws Exception {
		this.setValueStr(OldIdentityCardNo, aNo);
	}

	public String getNewIdentityCardNo() throws Exception {
		return(this.getValueStr(NewIdentityCardNo));
	}

	public void setNewIdentityCardNo(String aNo) throws Exception {
		this.setValueStr(NewIdentityCardNo, aNo);
	}

	@Override
	public void validateBeforePersist(Connection aConn) throws Exception {
		super.validateBeforePersist(aConn);
		if (this.getNewIdentityCardNo() != null) {
			String numOnlyIc = this.getNewIdentityCardNo().replaceAll("-", "").trim();
			if (numOnlyIc.isEmpty() == false) {
				//this.setNewIdentityCardNo(this.getNewIdentityCardNo().replaceAll("-", "").trim());
			} else {
				throw new Hinderance("Malaysian new identity card number cannot be empty");
			}
		}
	}

	@Override
	public String getNricNo() throws Exception {
		return(this.getNewIdentityCardNo());
	}

	public static String FormatNricNo(String aIcNo) throws Exception {
		String result = aIcNo;
		String numOnlyIc = aIcNo.replaceAll("-", "").trim();
		if (numOnlyIc.isEmpty() == false) {
		try {
			String partFirst = numOnlyIc.substring(0, 6);
			String partSecond = numOnlyIc.substring(6, 8);
			String partThree = numOnlyIc.substring(8);
			result = partFirst + "-" + partSecond + "-" + partThree;
		 } catch(Exception ex) {
			 // do nothing
		 }
		}
		return(result);
	}
	
}

