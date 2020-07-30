package biz.shujutech.bznes;

import biz.shujutech.db.object.Clasz;
import biz.shujutech.db.relational.FieldType;
import biz.shujutech.base.Connection;
import biz.shujutech.reflect.ReflectField;
import biz.shujutech.db.object.Lookup;
import biz.shujutech.db.object.ObjectBase;

public class PersonGotBankAccount extends GotIntf {
	@ReflectField(type=FieldType.OBJECT, deleteAsMember=false, clasz="biz.shujutech.bznes.ContactBankAccountType", displayPosition=5, lookup=true) public static String AccountType;
	@ReflectField(type=FieldType.OBJECT, deleteAsMember=false, clasz="biz.shujutech.bznes.BankAccount", displayPosition=10) public static String BankAccount;

	@Override
	public void setType(Lookup aValue) throws Exception {
		this.setValueObject(AccountType, (Clasz) aValue);
	}

	@Override
	public void setType(String aLookupName) throws Exception {
		Lookup chosen = ContactBankAccountType.GetFromList(aLookupName);
		this.setType(chosen);
	}

	@Override
	public Lookup getType() throws Exception {
		ContactBankAccountType type = (ContactBankAccountType) this.getFieldObject(AccountType).getValueObj();
		return((Lookup) type);
	}

	@Override
	public String getDescr(Connection aConn) throws Exception {
		return(this.gotDescr(aConn, AccountType));
	}

	@Deprecated
	@Override
	public Object getValue() throws Exception {
		BankAccount bankAccount = (BankAccount) this.getField(PersonGotBankAccount.BankAccount).getValueObj();
		return(bankAccount);
	}

	@Override
	public Object getValue(Connection aConn) throws Exception {
		BankAccount bankAccount = (BankAccount) this.getField(PersonGotBankAccount.BankAccount).getValueObj(aConn);
		return(bankAccount);
	}

	@Override
	public Object createValue(Connection conn) throws Exception {
		Object bankAccount = ObjectBase.CreateObject(conn, BankAccount.class);
		this.setValueObject(BankAccount, (Clasz) bankAccount);
		return(bankAccount);
	}

	@Override
	public void setValue(Object aObj) throws Exception {
		this.setValueObject(BankAccount, (Clasz) aObj);
	}
 
}
