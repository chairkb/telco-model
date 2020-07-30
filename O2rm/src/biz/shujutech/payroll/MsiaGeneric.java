package biz.shujutech.payroll;

import biz.shujutech.base.App;
import biz.shujutech.base.Connection;
import biz.shujutech.base.DateAndTime;
import biz.shujutech.base.Hinderance;
import biz.shujutech.db.object.Clasz;
import biz.shujutech.bznes.Addr;
import biz.shujutech.bznes.ChinaPassport;
import biz.shujutech.bznes.EmploymentMalaysia;
import biz.shujutech.bznes.IndonesiaPassport;
import biz.shujutech.bznes.MyanmarPassport;
import biz.shujutech.bznes.Passport;
import biz.shujutech.bznes.Person;
import biz.shujutech.bznes.SingaporePassport;
import biz.shujutech.bznes.VietnamPassport;
import biz.shujutech.technical.LambdaObject;
import biz.shujutech.util.Generic;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import org.joda.time.DateTime;

public class MsiaGeneric {

	public static final String FORMAT_DATE_MM = "MM";
	public static final String FORMAT_DATE_FILE = "ddMMyyyyHHmmss";
	public static final String FORMAT_DATE_YYYYMMDD = "yyyyMMdd";
	public static final String FORMAT_DATE_MMYYYY = "MMyyyy";
	public static final String FORMAT_TIME_BATCH = "HHmmss";
	public static final String FORMAT_DATE_ONLY = "dd/MM/yyyy";
	public static final String FORMAT_DATE_YYYY = "yyyy";
	public static final String FORMAT_DATE_MMYY = "MMyy";
	public static final String FORMAT_DATE_DDMMYYYY = "ddMMyyyy";

	//public static boolean CREATE_LOCAL_FILE = true;
	public static final String[] TaxFileTypeArray = { "SG", "OG", "D", "C", "J", "F", "TP", "TA", "TC", "CS", "TR", "PT", "TN", "LE" };
	public static final List<String> TaxFileTypeList = Arrays.asList(TaxFileTypeArray);
	public static final String FORMAT_MSIA_DATE = "dd-MM-yyyy";

	public static boolean CreateLocalFile() {
		return System.getProperty("os.name").startsWith("Win");
	}

	public static String GetWifeCode(Connection aConn, EmploymentMalaysia msiaJob) throws Exception {
		String result = "";
		String taxNo = msiaJob.getTaxNo();
		if (taxNo != null && taxNo.isEmpty() == false) {
			result = taxNo.substring(taxNo.length() - 1);
		}
		return result;
	}

	public static String GetCountryCode(Connection aConn, Person aWorker) throws Exception {
		String result = "";
		if (aWorker.isMalaysiaCitizenOrPr(aConn)) {
			result = "MY";
		} else {
			Passport passport = aWorker.getPassport(aConn);
			if (passport instanceof SingaporePassport) {
				result = "SG";
			} else if (passport instanceof ChinaPassport) {
				result = "CN";
			} else if (passport instanceof VietnamPassport) {
				result = "VN";
			} else if (passport instanceof IndonesiaPassport) {
				result = "ID";
			} else if (passport instanceof MyanmarPassport) {
				result = "MM";
			} else {
				App.logEror(BankPaymentCimb.class, "Not supported employee country code");
			}
		}
		return result;
	}

	public static String CreateZipFileName(String companyName) throws Exception {
		String generateDate = DateAndTime.AsString(DateTime.now(), FORMAT_DATE_FILE);
		String result = Generic.TrimLen(companyName, 10).replace(" ", "").replace("/", "").replace("-", "") + "_full_" + generateDate + ".zip";
		return result;
	}

	public static ByteArrayOutputStream ZipFile(Map<String, ByteArrayInputStream> aStream2Zip) throws Exception {
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		ZipOutputStream zipOut = new ZipOutputStream(new BufferedOutputStream(outStream));
		for (Map.Entry<String, ByteArrayInputStream> entry : aStream2Zip.entrySet()) {
			String fileName = entry.getKey();
			ByteArrayInputStream fis = entry.getValue();
			ZipEntry ze = new ZipEntry(fileName);
			zipOut.putNextEntry(ze);
			byte[] tmp = new byte[4 * 1024];
			int size;
			while ((size = fis.read(tmp)) != -1) {
				zipOut.write(tmp, 0, size);
			}
			zipOut.flush();
			fis.close();
		}
		zipOut.flush();
		zipOut.close();
		outStream.flush();
		return outStream;
	}

	public static void PopulateEpfStateMap(Map<String, String> epfStateMap) {
		epfStateMap.put("Johor", "01");
		epfStateMap.put("Kedah", "02");
		epfStateMap.put("Kelantan", "03");
		epfStateMap.put("Melaka", "04");
		epfStateMap.put("Negeri Sembilan", "05");
		epfStateMap.put("Pahang", "06");
		epfStateMap.put("Pulau Pinang", "07");
		epfStateMap.put("Perak", "08");
		epfStateMap.put("Perlis", "09");
		epfStateMap.put("Selangor", "10");
		epfStateMap.put("Terengganu", "11");
		epfStateMap.put("Sabah", "12");
		epfStateMap.put("Sarawak", "13");
		epfStateMap.put("Kuala Lumpur", "14");
		epfStateMap.put("Labuan", "15");
		epfStateMap.put("Putrajaya", "16");
	}

	public static String GetEpfStateCode(Addr aAddr, Map<String, String> aEpfStateMap) throws Exception {
		if (aAddr == null) {
			throw new Hinderance("Error, company address is required but not available!");
		}
		String result = "";
		for (Map.Entry<String, String> entry : aEpfStateMap.entrySet()) {
			String stateName = entry.getKey().toLowerCase().trim();
			String stateCode = entry.getValue();
			String stateNameShort = Generic.TrimLen(aAddr.getState().getName(), 10).toLowerCase().trim();
			if (!stateNameShort.isEmpty() && !stateName.isEmpty()) {
				if (stateName.contains(stateNameShort)) {
					result = stateCode;
					break;
				} else {
					String cityNameShort = Generic.TrimLen(aAddr.getCity().getName(), 10).toLowerCase().trim();
					if (stateName.contains(cityNameShort)) {
						result = stateCode;
						break;
					}
				}
			}
		}
		return result;
	}

	public static String FormatMoneyNoDot(Double aDouble, int aLen) {
		DecimalFormat df2 = new DecimalFormat("0.00");
		DecimalFormat df3 = new DecimalFormat("0");
		df2.setRoundingMode(RoundingMode.DOWN);
		df3.setRoundingMode(RoundingMode.DOWN);
		String strDouble = df3.format(Double.parseDouble(df2.format(aDouble)) * 100);
		String result = String.format("%1$" + aLen + "s", strDouble).replace(' ', '0');
		return result;
	}

	public static String FormatMoneyWithDot(Double aDouble, int aLen) {
		DecimalFormat df2 = new DecimalFormat("0.00");
		DecimalFormat df3 = new DecimalFormat("0");
		df2.setRoundingMode(RoundingMode.DOWN);
		df3.setRoundingMode(RoundingMode.DOWN);
		String strDouble = df3.format(Double.parseDouble(df2.format(aDouble)) * 1);
		String result = String.format("%1$" + aLen + "s", strDouble).replace(' ', '0');
		return result;
	}

	public static DateTime GetContributionMonth(Connection aConn, List<PayslipRun> aPayslipList) throws Exception {
		LambdaObject lambdaResult = new LambdaObject();
		for (PayslipRun eachPayslipRun : aPayslipList) {
			eachPayslipRun.getGeneratedPayslips().forEachMember(aConn, (Connection bConn, Clasz aPayslip) -> {
				SalarySlipMalaysia payslip = (SalarySlipMalaysia) aPayslip;
				lambdaResult.setTheObject(payslip.getPeriodFrom());
				return false;
			});
		}
		return (DateTime) lambdaResult.getTheObject();
	}

	public static String NoDashSlash(String aStr) {
		return aStr.replace("-", "").replace("/", "");
	}

	public static String ZeroIfBlank(String aData) {
		if (aData == null || aData.trim().isEmpty()) {
			return "0";
		} else {
			return aData;
		}
	}

	public static String CreateBaseFileName(String aCompanyName, String aType) throws Exception {
		String companyName = Generic.TrimLen(aCompanyName, 10).replace(" ", "").replace("/", "").replace("-", "");
		String generateDate = DateAndTime.AsString(DateTime.now(), FORMAT_DATE_FILE);
		String csvFileName = companyName + "_" + aType + "_" + generateDate;
		return csvFileName;
	}

	public static String GetTaxFileType(String aStr) {
		String fileType = Generic.ExtractStartingChar(aStr);
		if (TaxFileTypeList.contains(fileType)) {
			return fileType;
		}
		return("");
	}
}
