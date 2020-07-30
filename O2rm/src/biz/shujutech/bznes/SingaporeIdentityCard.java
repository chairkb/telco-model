
package biz.shujutech.bznes;

import biz.shujutech.base.Connection;
import biz.shujutech.base.Hinderance;
import biz.shujutech.db.relational.FieldType;
import biz.shujutech.db.relational.SortOrder;
import biz.shujutech.reflect.ReflectField;
import biz.shujutech.reflect.ReflectIndex;

public class SingaporeIdentityCard extends Identity {
	@ReflectField(type=FieldType.STRING, size=20, indexes={@ReflectIndex(indexName="idx_spore_ic", indexNo=0, indexOrder=SortOrder.ASC, isUnique=true)}, displayPosition=205, updateable=false) public static String NewIdentityCardNo;
	@ReflectField(type=FieldType.STRING, size=20, displayPosition=200) public static String CountryOfBirth;
	@ReflectField(type=FieldType.DATE, displayPosition=15) public static String DateOfIssue;

	@Override
	public void initBeforePopulate() throws Exception {
		this.setIssuingCountry(Identity.IDENTITY_NRIC);
		this.setIdentityName(Identity.IDENTITY_NRIC);
	}

	public String getCountryOfBirth() throws Exception {
		return(this.getValueStr(CountryOfBirth));
	}

	public void setCountryOfBirth(String aNo) throws Exception {
		this.setValueStr(CountryOfBirth, aNo);
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
			if (!this.getNewIdentityCardNo().trim().isEmpty()) {
				this.setNewIdentityCardNo(this.getNewIdentityCardNo().trim());
			} else {
				throw new Hinderance("Singapore new identity card number cannot be empty");
			}
		}
	}

	
}
