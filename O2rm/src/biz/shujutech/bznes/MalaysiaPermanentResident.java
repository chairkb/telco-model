package biz.shujutech.bznes;

import biz.shujutech.base.Connection;
import biz.shujutech.base.Hinderance;
import biz.shujutech.db.relational.FieldType;
import biz.shujutech.db.relational.SortOrder;
import biz.shujutech.reflect.ReflectField;
import biz.shujutech.reflect.ReflectIndex;

public class MalaysiaPermanentResident extends Identity implements PermanentResident {
	@ReflectField(type=FieldType.STRING, size=20, indexes={@ReflectIndex(indexName="idx_msian_pr", indexNo=0, indexOrder=SortOrder.ASC, isUnique=true)}, displayPosition=205, updateable=false) public static String PermanentResidentNo;
	@ReflectField(type=FieldType.STRING, size=32, displayPosition=210) public static String CountryOfOrigin;

	@Override
	public void initBeforePopulate() throws Exception {
		this.setIssuingCountry("Malaysia");
		this.setIdentityName("MyPR");
	}

	@Override
	public String getPermanentResidentNo() throws Exception {
		return(this.getValueStr(PermanentResidentNo));
	}

	public void setPermanentResidentNo(String aNo) throws Exception {
		this.setValueStr(PermanentResidentNo, aNo);
	}

	@Override
	public String getCountryOfOrigin() throws Exception {
		return(this.getValueStr(CountryOfOrigin));
	}

	public void setCountryOfOrigin(String aNo) throws Exception {
		this.setValueStr(CountryOfOrigin, aNo);
	}

	@Override
	public void validateBeforePersist(Connection aConn) throws Exception {
		super.validateBeforePersist(aConn);
		if (this.getPermanentResidentNo() != null) {
			if (!this.getPermanentResidentNo().trim().isEmpty()) {
				this.setPermanentResidentNo(this.getPermanentResidentNo().trim());
			} else {
				throw new Hinderance("Malaysia permanent resident number cannot be empty");
			}
		}
	}

	
}
