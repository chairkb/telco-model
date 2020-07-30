package biz.shujutech.payroll;

import biz.shujutech.base.Connection;
import biz.shujutech.base.Hinderance;
import biz.shujutech.bznes.Marital;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class MsiaTaxRatePMRB {
	private static final Map<Integer, List<MsiaTaxRatePMRB>> RATE_MAP = new ConcurrentHashMap<>();

	private double P_yearlySalaryFrom = 0;
	private double P_yearlySalaryTo = 0;
	private double M_yearlySalaryBase = 0;
	private double R_rate = 0;
	private double B_cat1cat3 = 0;
	private double B_cat2 = 0;

	MsiaTaxRatePMRB(double yearlySalaryFrom, double yearlySalaryTo, double yearlySalaryBase, double rate, double cat1cat3, double cat2) {
		P_yearlySalaryFrom = yearlySalaryFrom;
		P_yearlySalaryTo = yearlySalaryTo;
		M_yearlySalaryBase = yearlySalaryBase ;
		R_rate = rate;
		B_cat1cat3 = cat1cat3;
		B_cat2 = cat2;
	}

	
	public static List<MsiaTaxRatePMRB> CreateTaxRateList(Integer aForYear) throws Exception {
		List<MsiaTaxRatePMRB> taxRateList = null;
		if (aForYear >= 2019) {
			taxRateList = RATE_MAP.get(aForYear);
			if (taxRateList == null) {
				taxRateList = new CopyOnWriteArrayList<>();
				taxRateList.add(new MsiaTaxRatePMRB(5001, 20000, 5000, 1, -400, -800));
				taxRateList.add(new MsiaTaxRatePMRB(20001, 35000, 20000, 3, -250, -650));
				taxRateList.add(new MsiaTaxRatePMRB(35001, 50000, 35000, 8, 600, 600));
				taxRateList.add(new MsiaTaxRatePMRB(50001, 70000, 50000, 14, 1800, 1800));
				taxRateList.add(new MsiaTaxRatePMRB(70001, 100000, 70000, 21, 4600, 4600));
				taxRateList.add(new MsiaTaxRatePMRB(100001, 250000, 100000, 24, 10900, 10900));
				taxRateList.add(new MsiaTaxRatePMRB(250001, 400000, 250000, 24.5, 46900, 46900));
				taxRateList.add(new MsiaTaxRatePMRB(400001, 600000, 400000, 25, 83650, 83650));
				taxRateList.add(new MsiaTaxRatePMRB(600001, 1000000, 600000, 26, 133650, 133650));
				taxRateList.add(new MsiaTaxRatePMRB(1000000, Double.MAX_VALUE, 1000000, 28, 237650, 237650));				
				RATE_MAP.put(aForYear, taxRateList);
			}
		}
		return(taxRateList);
	}
	
	public static MsiaTaxRatePMRB GetTaxRate(List<MsiaTaxRatePMRB> aRateList, double aYearRemuneration) throws Exception {
		for(MsiaTaxRatePMRB eachRate : aRateList) {
			if (aYearRemuneration >= eachRate.getP_yearlySalaryFrom() && aYearRemuneration < eachRate.getP_yearlySalaryTo())  {
				return(eachRate);
			}
		}
		
		MsiaTaxRatePMRB firstRate = aRateList.get(0);
		if (aYearRemuneration < firstRate.getP_yearlySalaryFrom()) {
			return(null);
		} else {
			throw new Hinderance("Fail to locate tax rate from MsiaTaxRate for: " + aYearRemuneration);
		}
	}

	public static String GetCategory(Connection aConn, List<MsiaTaxRatePMRB> aTaxRateList, Marital aMarital, Boolean aSpouseWorking, int aTotalKid) throws Exception {
		String result = "cat1";
		if (aMarital.getDescr().equals(Marital.Single.getDescr())) {
			result = "cat1";
		} else if (aSpouseWorking == false) {
			result = "cat2";
		} else if (aSpouseWorking) {
			result = "cat3";
		}
		return(result);
	}

	public double getP_yearlySalaryFrom() {
		return P_yearlySalaryFrom;
	}

	public void setP_yearlySalaryFrom(double P_yearlySalaryFrom) {
		this.P_yearlySalaryFrom = P_yearlySalaryFrom;
	}

	public double getP_yearlySalaryTo() {
		return P_yearlySalaryTo;
	}

	public void setP_yearlySalaryTo(double P_yearlySalaryTo) {
		this.P_yearlySalaryTo = P_yearlySalaryTo;
	}

	public double getM_yearlySalaryBase() {
		return M_yearlySalaryBase;
	}

	public void setM_yearlySalaryBase(double M_yearlySalaryBase) {
		this.M_yearlySalaryBase = M_yearlySalaryBase;
	}

	public double getR_rate() {
		return R_rate;
	}

	public void setR_rate(double R_rate) {
		this.R_rate = R_rate;
	}

	public double getB_cat1cat3() {
		return B_cat1cat3;
	}

	public void setB_cat1cat3(double B_cat1cat3) {
		this.B_cat1cat3 = B_cat1cat3;
	}

	public double getB_cat2() {
		return B_cat2;
	}

	public void setB_cat2(double B_cat2) {
		this.B_cat2 = B_cat2;
	}



}

