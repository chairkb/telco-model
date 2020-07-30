package biz.shujutech.bznes;

import biz.shujutech.base.Connection;
import biz.shujutech.base.Hinderance;
import biz.shujutech.db.relational.FieldType;
import biz.shujutech.db.relational.SortOrder;
import biz.shujutech.reflect.ReflectField;
import biz.shujutech.reflect.ReflectIndex;

public class ChinaPassport extends Identity implements Passport {
	@ReflectField(type=FieldType.STRING, size=32, indexes={@ReflectIndex(indexName="idx_chna_paspt", indexNo=0, indexOrder=SortOrder.ASC, isUnique=true)}, displayPosition=300, updateable=false) public static String PassportNo;
	
	@Override
	public void initBeforePopulate() throws Exception {
		this.setIssuingCountry("China");
		this.setIdentityName(Identity.IDENTITY_PASSPORT);
	}

	@Override
	public String getPassportNo() throws Exception {
		return(this.getValueStr(PassportNo));
	}

	@Override
	public void setPassportNo(String aNo) throws Exception {
		this.setValueStr(PassportNo, aNo);
	}

	@Override
	public void validateBeforePersist(Connection aConn) throws Exception {
		super.validateBeforePersist(aConn);
		if (this.getPassportNo() != null) {
			if (!this.getPassportNo().trim().isEmpty()) {
				this.setPassportNo(this.getPassportNo().trim());
			} else {
				throw new Hinderance("China passport number cannot be empty");
			}
		}
	}
}
