package biz.shujutech.payroll;

import java.util.List;
import java.util.Map;

public interface ReportInterface {
	public Map<String, String> getReportHeader();
	public List<String> getDataHeader();
	public List<List<String>> getRecordList();
	
}
