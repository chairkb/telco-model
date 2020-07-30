package biz.shujutech.payroll;

import biz.shujutech.base.Connection;
import biz.shujutech.base.Hinderance;
import biz.shujutech.bznes.Marital;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;


public class MsiaTaxRateIndividual {
	private static final Map<Integer, List<MsiaTaxRateIndividual>> RATE_MAP = new ConcurrentHashMap<>();

	private double annualCeiling;
	private double taxAmount;
	private double afterCeilingRate;


	MsiaTaxRateIndividual(double aAnnualCeiling, double aTaxAmount, double aAfterCeilingRate) {
		annualCeiling = aAnnualCeiling;
		taxAmount = aTaxAmount;
		afterCeilingRate = aAfterCeilingRate;
	}

	public static List<MsiaTaxRateIndividual> CreateTaxRateList(Integer aForYear) throws Exception {
		List<MsiaTaxRateIndividual> taxRateList = null;
		if (aForYear >= 2019) {
			taxRateList = RATE_MAP.get(aForYear);
			if (taxRateList == null) {
				taxRateList = new CopyOnWriteArrayList<>();
				taxRateList.add(new MsiaTaxRateIndividual(5000, 0, 1));
				taxRateList.add(new MsiaTaxRateIndividual(20000, 150, 3));
				taxRateList.add(new MsiaTaxRateIndividual(35000, 600, 8));
				taxRateList.add(new MsiaTaxRateIndividual(50000, 1800, 14));
				taxRateList.add(new MsiaTaxRateIndividual(70000, 4600, 21));
				taxRateList.add(new MsiaTaxRateIndividual(100000, 10900, 24));
				taxRateList.add(new MsiaTaxRateIndividual(250000, 46900, 24.5));
				taxRateList.add(new MsiaTaxRateIndividual(400000, 83650, 25));
				taxRateList.add(new MsiaTaxRateIndividual(600000, 133650, 26));
				taxRateList.add(new MsiaTaxRateIndividual(1000000, 237650, 28));
				RATE_MAP.put(aForYear, taxRateList);
			}
		}
		return(taxRateList);
	}

	public static MsiaTaxRateIndividual GetTaxRate(List<MsiaTaxRateIndividual> aRateList, double aYearRemuneration) throws Exception {
		for(int cntr = 0; cntr < aRateList.size(); cntr++) {
			MsiaTaxRateIndividual eachRate = aRateList.get(cntr);
			if (aYearRemuneration < eachRate.getAnnualCeiling())  {
				if (cntr == 0) {
					return(aRateList.get(0));
				} else {
					return(aRateList.get(cntr - 1));
				}
			}
		}

		MsiaTaxRateIndividual maxTaxRate = aRateList.get(aRateList.size() - 1);
		if (aYearRemuneration >= maxTaxRate.getAnnualCeiling()) {
			return(maxTaxRate);
		}

		throw new Hinderance("Fail to locate tax rate from " + MsiaTaxRateIndividual.class.getSimpleName() + ", for yearly remuneration of: " + aYearRemuneration);
	}

	public static Double GetAnnualTaxAmount(List<MsiaTaxRateIndividual> aRateList, double aYearRemuneration) throws Exception {
		double result = 0;
		MsiaTaxRateIndividual taxRate = GetTaxRate(aRateList, aYearRemuneration);
		result = taxRate.getTaxAmount();
		double afterCeilingTax = (taxRate.getAfterCeilingRate()/100) * (aYearRemuneration - taxRate.getAnnualCeiling());
		result += afterCeilingTax;
		return(result);
	}

	public double getAnnualCeiling() {
		return annualCeiling;
	}

	public void setAnnualCeiling(double annualCeiling) {
		this.annualCeiling = annualCeiling;
	}

	public double getTaxAmount() {
		return taxAmount;
	}

	public void setTaxAmount(double taxAmount) {
		this.taxAmount = taxAmount;
	}

	public double getAfterCeilingRate() {
		return afterCeilingRate;
	}

	public void setAfterCeilingRate(double afterCeilingRate) {
		this.afterCeilingRate = afterCeilingRate;
	}
	
	
	
}
