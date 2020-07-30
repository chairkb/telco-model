package biz.shujutech.bznes;

import biz.shujutech.base.Connection;
import biz.shujutech.db.object.Clasz;
import biz.shujutech.db.relational.FieldType;
import biz.shujutech.reflect.ReflectField;

public class BankAccount extends Clasz {
	@ReflectField(type=FieldType.OBJECT, displayPosition=5, deleteAsMember=false, clasz="biz.shujutech.bznes.BankMalaysia", lookup=true) public static String Bank;  // BankMalaysia can be replace by BankSingapore etc, by setBank
	@ReflectField(type=FieldType.STRING, displayPosition=10, size=32) public static String AccountNo; 

	public BankAccount() {
		super();
	}

	public void setAccountNo(String aAccNo) throws Exception {
		this.setValueStr(AccountNo, aAccNo);
	}

	public String getAccountNo() throws Exception {
		String result = this.getValueStr(AccountNo);
		return(result);
	}

	public void setBank(Company aBank) throws Exception {
		this.setValueObject(Bank, aBank);
	}
	
	public BankMalaysia getBank(Connection aConn) throws Exception {
		BankMalaysia result = (BankMalaysia) this.gotValueObject(aConn, Bank);
		return(result);
	}
}
