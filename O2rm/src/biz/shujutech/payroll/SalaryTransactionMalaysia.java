package biz.shujutech.payroll;

import biz.shujutech.db.relational.FieldType;
import biz.shujutech.reflect.ReflectField;

public class SalaryTransactionMalaysia extends SalaryTransaction {
	@ReflectField(type=FieldType.BOOLEAN, displayPosition=205) public static String AffectTax;
	@ReflectField(type=FieldType.BOOLEAN, displayPosition=210) public static String AffectEpf;
	@ReflectField(type=FieldType.BOOLEAN, displayPosition=215) public static String AffectSocso;
	@ReflectField(type=FieldType.BOOLEAN, displayPosition=220) public static String AffectEis;

	public static final String OVERTIME_1X_DESC = "Overtime 1x";
	public static final String OVERTIME_1_5X_DESC = "Overtime 1.5x";
	public static final String OVERTIME_2X_DESC = "Overtime 2x";
	public static final String OVERTIME_3X_DESC = "Overtime 3x";

	public static final String CLAIM_COMMISSION_DESC = "Commission";
	public static final String CLAIM_MEDICAL_DESC = "Medical";
	public static final String CLAIM_PHONE_DESC = "Phone";
	public static final String CLAIM_LODGING_DESC = "Lodging";
	public static final String CLAIM_PETROL_DESC = "Petrol";
	public static final String CLAIM_TOLL_DESC = "Toll";
	public static final String CLAIM_PARKING_DESC = "Parking";
	public static final String CLAIM_BONUS_DESC = "Bonus";
	
	public Boolean isAffectTax() throws Exception {
		return(this.getValueBoolean(AffectTax));
	}

	public void isAffectTax(boolean aBool) throws Exception {
		this.setValueBoolean(AffectTax, aBool);
	}

	public Boolean isAffectEpf() throws Exception {
		return(this.getValueBoolean(AffectEpf));
	}

	public void isAffectEpf(boolean aBool) throws Exception {
		this.setValueBoolean(AffectEpf, aBool);
	}

	public Boolean isAffectSocso() throws Exception {
		return(this.getValueBoolean(AffectSocso));
	}

	public void isAffectSocso(boolean aBool) throws Exception {
		this.setValueBoolean(AffectSocso, aBool);
	}

	public Boolean isAffectEis() throws Exception {
		return(this.getValueBoolean(AffectEis));
	}

	public void isAffectEis(boolean aBool) throws Exception {
		this.setValueBoolean(AffectEis, aBool);
	}
}
