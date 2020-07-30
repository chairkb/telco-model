package biz.shujutech.bznes;

import biz.shujutech.base.App;
import biz.shujutech.base.Connection;
import biz.shujutech.base.Hinderance;
import biz.shujutech.db.object.Clasz;
import biz.shujutech.db.object.ObjectBase;
import biz.shujutech.db.relational.FieldType;
import biz.shujutech.util.NumberToWord;
import biz.shujutech.reflect.ReflectField;
import java.text.DecimalFormat;


public class Money extends Clasz {
	public static final DecimalFormat FORMAT_AMOUNT_WITH_COMMA = new DecimalFormat("#,##0.00");
	public static final DecimalFormat FORMAT_AMOUNT_NO_COMMA = new DecimalFormat("###0.00");

	@ReflectField(type=FieldType.STRING, size=8) public static String Currency;
	@ReflectField(type=FieldType.STRING, size=32) public static String Amount;

	public Double getValueDouble() throws Exception {
		Double result = null;
		String strAmount = this.getAmount();
		if (strAmount != null && strAmount.isEmpty() == false) {
			strAmount = strAmount.replaceAll(",", "");
			result = Double.parseDouble(strAmount);
		}
		return(result);
	}


	@Override
	public String getValueStr() throws Exception {
		String result = "";
		if (this.getAmount() != null && this.getAmount().isEmpty() == false) {
			Double dblAmount = Double.parseDouble(this.getAmount());
			String strAmount = FORMAT_AMOUNT_WITH_COMMA.format(dblAmount);
			result = this.getCurrencyCode() + " " + strAmount;
		}
		return(result.trim());
	}

	public String getMoneyStr() throws Exception {
		String result = this.getValueStr();
		return(result);
	}

	public String getAmountWithComma() throws Exception {
		return(GetAmountWithComma(this.getAmount()));
	}

	public String getAmountNoComma() throws Exception {
		String result = "";
		if (this.getAmount() != null && this.getAmount().isEmpty() == false) {
			Double dblAmount = Double.parseDouble(this.getAmount());
			result = FORMAT_AMOUNT_NO_COMMA.format(dblAmount);
		}
		return(result.trim());
	}

	public static String GetAmountWithComma(String aAmount) throws Exception {
		String result = "";
		if (aAmount != null && aAmount.isEmpty() == false) {
			String strAmount = aAmount.replaceAll(",", "");
			Double dblAmount = Double.parseDouble(strAmount);
			result = FORMAT_AMOUNT_WITH_COMMA.format(dblAmount);
		}
		return(result.trim());
	}

	public void setAmount(Money aMoney) throws Exception {
		this.setCurrencyCode(aMoney.getCurrencyCode());
		this.setAmount(aMoney.getValueDouble());
	}

	public void setAmount(String aAmount) throws Exception {
		String strAmount = aAmount.replaceAll(",", "");
		Double dblAmount = Double.parseDouble(strAmount);
		this.setAmount(dblAmount);

	}
	public void setAmount(Double aAmount) throws Exception {
		String amount = Double.toString(aAmount);
		this.setValueStr(Amount, amount);
	}	

	public String getAmount() throws Exception {
		return(this.getValueStr(Amount));
	}

	public void setCurrencyCode(String aValue) throws Exception {
		this.setValueStr(Currency, aValue);
	}

	public String getCurrencyCode() throws Exception {
		return(this.getValueStr(Currency));
	}

	public void setValue(String aCurrency, Double aAmount) throws Exception {
		this.setValue(aCurrency, String.valueOf(aAmount));
	}

	public void setValue(String aCurrency, String aAmount) throws Exception {
		this.setCurrencyCode(aCurrency);
		this.setAmount(aAmount);
	}

	public void setValue(String aCurrency, String aDollar, String aCent) throws Exception {
		String strAmount = aDollar + "." + aCent;
		this.setValue(aCurrency, strAmount);
	}

	public String getDollarPart() throws Exception {
		String strResult = "";
		if (this.getAmount() != null) {
			int dotPos = this.getAmount().indexOf(".");
			if (dotPos >= 0) {
				strResult = this.getAmount().substring(0, dotPos);
			} else {
				strResult = this.getAmount();
			}
		}
		return(strResult);
	}

	public String getCentPart() throws Exception {
		String strResult = "";
		if (this.getAmount() != null) {
			int dotPos = this.getAmount().indexOf(".");
			if (dotPos >= 0) {
				strResult = this.getAmount().substring(dotPos + 1);
			} else {
				strResult = "0";
			}
		}
		return(strResult);
	}

	@Override
	public void setValueStr(String aValue) throws Exception {
		if (aValue != null && aValue.trim().isEmpty() == false) {
			String[] moneyParts = aValue.split(" ");
			String currencyCode = moneyParts[0];
			String valuePart = moneyParts[1];

			String[] valueSplit = valuePart.split("\\.");
			String dollarPart = valueSplit[0].replaceAll(",", "");
			String centPart = "00";
			if (valueSplit.length == 2) {
				centPart = valueSplit[1];
			}

			Country country = Country.getCountryByCurrencyCode(currencyCode);
			this.setValue(country.getCurrencyCode(), dollarPart, centPart);
		}
	}

	public void roundCent() throws Exception {
		String strCent = this.getCentPart();
		if (strCent.length() > 2) {
			int lastCent = Integer.parseInt(strCent.substring(0, 2)) % 10;
			if (lastCent < 6) {
				lastCent = 0;
			} else {
				lastCent = 5;
			}
			String fullCent = strCent.substring(0, 1) + String.valueOf(lastCent);
			this.setValue(this.getCurrencyCode(), this.getDollarPart(), fullCent);
		}
	}

	public String inWord() throws Exception {
		long dollar = Long.parseLong(this.getDollarPart());
		int cent = Integer.parseInt(this.getCentPart());
		String inWord = this.getCurrencyCode() + " " +  NumberToWord.convert(dollar) + " &" + " Cents "  + NumberToWord.convert(cent) + " Only.";
		return(inWord.toUpperCase());
	}

	public void minusAmount(final Money aAmount) throws Exception {
		this.amountAddOrMinus(aAmount, false);
	}

	public void addAmount(Money aAmount) throws Exception {
		this.amountAddOrMinus(aAmount, true);
	}

	public void amountAddOrMinus(final Money aAmount, boolean aAdd) throws Exception {
		if (this.getCurrencyCode().equals(aAmount.getCurrencyCode())) {
			Double dblLeft = this.getValueDouble();
			Double dblRight = aAmount.getValueDouble();
			Double total = 0D;
			if (aAdd == true) {
				total = dblLeft + dblRight;
			} else {
				total = dblLeft - dblRight;
			}
			this.setValue(this.getCurrencyCode(), total);
		} else {
			throw new Hinderance("Cannot add money of 2 different currency type: '" + this.getCurrencyCode() + "', '" + aAmount.getCurrencyCode() + "'");
		}
	}

	public boolean hasProperValue() throws Exception {
		if (this.getCurrencyCode().isEmpty() == false && this.getAmount() != null) {
			return(true);
		}
		return(false);
	}

	public void setZeroIfNull(Money aBaseOn) throws Exception {
		if (this.hasProperValue() == false) {
			this.setValue(aBaseOn.getCurrencyCode(), "0.0");
		}
	}

	public static Money CreateMoney(Connection aConn, String aCurrencyCode) throws Exception {
		return CreateMoney(aConn, aCurrencyCode, 0.0D);
	}

	public static Money CreateMoney(Connection aConn, String aCurrencyCode, Double aValue) throws Exception {
		Money newMoney = (Money) ObjectBase.CreateObject(aConn, Money.class);
		newMoney.setValue(aCurrencyCode, aValue);
		return(newMoney);
	}

	public static void main(String[] args) {
		try {
			ObjectBase objectDb = new ObjectBase();
			objectDb.setupApp(args);
			//App.setLogLevel("NONE");
			objectDb.setupDb();
			Connection conn = objectDb.getConnPool().getConnection();
			Country.InitList(conn);

			String currencyCode = Country.getCountry(conn, "MYS").getCurrencyCode();
			Money money = (Money) objectDb.createObject(Money.class);
			money.setValue(currencyCode, "30012", "2532");
			App.logInfo("Money: " + money.getCurrencyCode() + "" + money.getDollarPart() + "." + money.getCentPart());
			money.setValue(currencyCode, "300", "25");
			App.logInfo("Money: " + money.getCurrencyCode() + "" + money.getDollarPart() + "." + money.getCentPart());
			money.setValue(currencyCode, "-300", "25");
			App.logInfo("Money: " + money.getCurrencyCode() + "" + money.getDollarPart() + "." + money.getCentPart());
			money.setValue(currencyCode, "-300", "256");
			App.logInfo("Money: " + money.getCurrencyCode() + "" + money.getDollarPart() + "." + money.getCentPart());
			money.setValue(currencyCode, "-300", "253");
			App.logInfo("Money: " + money.getCurrencyCode() + "" + money.getDollarPart() + "." + money.getCentPart());
			money.setValue(currencyCode, "-300", "02530");
			App.logInfo("Money: " + money.getCurrencyCode() + "" + money.getDollarPart() + "." + money.getCentPart());

			objectDb.getConnPool().freeConnection(conn);
		} catch(Exception ex) {
			App.logEror(new Hinderance(ex, "System is aborting"));
		}
	}
	
	
}
