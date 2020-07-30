package biz.shujutech.bznes;

import biz.shujutech.db.relational.FieldType;
import biz.shujutech.db.relational.SortOrder;
import biz.shujutech.reflect.ReflectField;
import biz.shujutech.reflect.ReflectIndex;

public class SingaporePassport extends Identity implements Passport {
	@ReflectField(type=FieldType.STRING, size=32, indexes={@ReflectIndex(indexName="idx_spore_paspt", indexNo=0, indexOrder=SortOrder.ASC, isUnique=true)}, displayPosition=300, updateable=false) public static String PassportNo;
	
	@Override
	public void initBeforePopulate() throws Exception {
		this.setIssuingCountry("Singapore");
		this.setIdentityName(Identity.IDENTITY_PASSPORT);
	}

	@Override
	public String getPassportNo() throws Exception {
		return(this.getValueStr(PassportNo));
	}

	public void setPassportNo(String aNo) throws Exception {
		this.setValueStr(PassportNo, aNo);
	}
}
