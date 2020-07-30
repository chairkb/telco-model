package biz.shujutech.payroll;

import biz.shujutech.base.App;
import biz.shujutech.base.Hinderance;
import biz.shujutech.db.object.Clasz;
import biz.shujutech.db.object.ObjectBase;
import biz.shujutech.db.relational.FieldType;
import biz.shujutech.base.Connection;
import biz.shujutech.bznes.Country;
import biz.shujutech.bznes.Duration;
import biz.shujutech.bznes.Money;
import biz.shujutech.reflect.ReflectField;

public class Salary extends Clasz {
	@ReflectField(type=FieldType.OBJECT, clasz="biz.shujutech.bznes.Money", prefetch=true, displayPosition=5) public static String Salary;
	@ReflectField(type=FieldType.OBJECT, clasz="biz.shujutech.bznes.Duration", prefetch=true, lookup=true, displayPosition=10) public static String SalaryPeriod;

	public Money getSalaryAmount(Connection aConn) throws Exception {
		return((Money) this.getValueObject(aConn, Salary));
	}

	public Duration getDuration(Connection aConn) throws Exception {
		return((Duration) this.getValueObject(aConn, SalaryPeriod));
	}

	public void setSalary(Money aValue) throws Exception {
		this.setValueObject(Salary, aValue);
	}

	public Duration getSalaryPeriod(Connection aConn) throws Exception {
		Duration salaryPeriod = (Duration) this.getValueObject(aConn, SalaryPeriod);
		return(salaryPeriod);
	}

	public boolean gotSalaryPeriod(Connection aConn) throws Exception {
		boolean result = false;
		Clasz clasz = this.gotValueObject(aConn, SalaryPeriod);
		if (clasz != null) result = true;
		return(result);
	}

	public void setSalaryPeriod(Duration aValue) throws Exception {
		this.setValueObject(SalaryPeriod, aValue);
	}

	public void setSalary(Connection aConn, String aCurrency, String aDollar, String aCent, Duration aDuration) throws Exception {
		Money amount = (Money) ObjectBase.CreateObject(aConn, Money.class);
		amount.setValue(aCurrency, aDollar, aCent);
		this.setSalary(amount, aDuration);
	}

	public void setSalary(Money aAmount, Duration aDuration) throws Exception {
		this.setSalary(aAmount);
		this.setSalaryPeriod(aDuration);
	}
	
	@Override
	public String getValueStr(Connection aConn) throws Exception {
		return this.getSalaryAmount(aConn).getValueStr(aConn) + " " + this.getSalaryPeriod(aConn).getValueStr(aConn);
	}

	@Override
	public void setValueStr(Connection aConn, String aValue) throws Exception {
		String[] partSalary = aValue.split(" ");
		String partAmount = partSalary[0] + " " + partSalary[1];
		String partPeriod = partSalary[2];
		this.getSalaryAmount(aConn).setValueStr(partAmount);
		this.getSalaryPeriod(aConn).setValueStr(partPeriod);
	}

	public static void main(String[] args) {
		try {
			ObjectBase objectDb = new ObjectBase();
			objectDb.setupApp(args);
			objectDb.setupDb();
			Connection conn = objectDb.getConnPool().getConnection();
	
			Country.InitList(conn);
			objectDb.getConnPool().freeConnection(conn);
			Money money = (Money) objectDb.createObject(Money.class);
			money.setValue(Country.Malaysia.getCurrencyCode(), "5000", "25");

			Salary salary = (Salary) objectDb.createObject(Salary.class);
			salary.setSalary(money, Duration.Month);

		} catch(Exception ex) {
			App.logEror(new Hinderance(ex, "System is aborting"));
		}
	}
}
