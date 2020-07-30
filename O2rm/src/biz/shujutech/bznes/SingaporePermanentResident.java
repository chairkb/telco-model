package biz.shujutech.bznes;

import biz.shujutech.base.Connection;
import biz.shujutech.base.Hinderance;
import biz.shujutech.db.relational.FieldType;
import biz.shujutech.db.relational.SortOrder;
import biz.shujutech.reflect.ReflectField;
import biz.shujutech.reflect.ReflectIndex;

public class SingaporePermanentResident extends Identity implements PermanentResident {
	@ReflectField(type=FieldType.STRING, size=20, indexes={@ReflectIndex(indexName="idx_spore_pr", indexNo=0, indexOrder=SortOrder.ASC, isUnique=true)}, displayPosition=205, updateable=false) public static String IdentityCardNo;
	@ReflectField(type=FieldType.STRING, size=32, displayPosition=210) public static String CountryOfBirth;

	@Override
	public void initBeforePopulate() throws Exception {
		this.setIssuingCountry("Singapore");
		this.setIdentityName(Identity.IDENTITY_PR);
	}


	public String getIdentityCardNo() throws Exception {
		return(this.getValueStr(IdentityCardNo));
	}

	public void setIdentityCardNo(String aNo) throws Exception {
		this.setValueStr(IdentityCardNo, aNo);
	}
	
	public String getCountryOfBirth() throws Exception {
		return(this.getValueStr(CountryOfBirth));
	}

	public void setCountryOfBirth(String aNo) throws Exception {
		this.setValueStr(CountryOfBirth, aNo);
	}

	@Override
	public void validateBeforePersist(Connection aConn) throws Exception {
		super.validateBeforePersist(aConn);
		if (this.getIdentityCardNo() != null) {
			if (!this.getIdentityCardNo().trim().isEmpty()) {
				this.setIdentityCardNo(this.getIdentityCardNo().trim());
			} else {
				throw new Hinderance("Singapore permanent resident number cannot be empty");
			}
		}
	}

	@Override
	public String getCountryOfOrigin() throws Exception {
		return(this.getCountryOfBirth());
	}

	@Override
	public String getPermanentResidentNo() throws Exception {
		return(this.getIdentityCardNo());
	}

}
