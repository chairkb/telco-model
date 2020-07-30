package biz.shujutech.payroll;

import biz.shujutech.base.Hinderance;
import java.util.List;
import java.util.Map;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.File;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import org.jsoup.Jsoup;

public class ReportHtml {

	public static Element GetHtmlTable(Document aDoc, String aTableId) throws Exception {
		Element table = aDoc.select("#" + aTableId).get(0); //select the first table.
		return table;
	}

	public static void PopulateHtmlData(Element htmlTable, ReportInterface aBorangA) throws Exception {
		List<String> headerList = aBorangA.getDataHeader();
		List<List<String>> recordList = aBorangA.getRecordList();
		for (int cntrRec = 0; cntrRec < recordList.size(); cntrRec++) {
			// for each record
			List<String> eachRecord = recordList.get(cntrRec);
			for (int cntrField = 0; cntrField < headerList.size(); cntrField++) {
				// for each field
				String fieldName = headerList.get(cntrField);
				String fieldValue = eachRecord.get(cntrField);
				Element htmlRow = GetHtmlRow(htmlTable, cntrRec + 1);
				Elements htmlCols = htmlRow.select("td");
				htmlCols.get(cntrField).text(fieldValue);
			}
		}
	}

	public static void PopulateHtmlTitle(Element htmlTable, ReportInterface aBorangA) throws Exception {
		Map<String, String> headerMap = aBorangA.getReportHeader();
		Elements htmlRows = htmlTable.select("tr");
		for (int cntrRow = 0; cntrRow < htmlRows.size(); cntrRow++) {
			// for each row
			Element htmlRow = htmlRows.get(cntrRow);
			Elements htmlCols = htmlRow.select("td");
			String fieldNameHtml = htmlCols.get(0).text();
			String fieldValueMap = headerMap.get(fieldNameHtml);
			if (fieldValueMap != null && !fieldValueMap.isEmpty()) {
				htmlCols.get(1).text(fieldValueMap);
			}
		}
	}

	public static Element GetHtmlRow(Element aTable, int aRowNum) throws Exception {
		Element result = null;
		Elements rows = aTable.select("tr");
		for (int cntrRow = 1; cntrRow < rows.size(); cntrRow++) {
			//first result is the col names so skip it.
			if (cntrRow == aRowNum) {
				result = rows.get(cntrRow);
			}
		}
		if (result == null) {
			aTable.select("tbody").append(rows.get(1).toString());
			result = GetHtmlRow(aTable, aRowNum);
		}
		return result;
	}

	public static void SetReportText(Document aDoc, String aIdElem, String aText) throws Exception {
		Elements elemRpts = aDoc.select("#" + aIdElem);
		Element elemRpt = elemRpts.get(0);
		if (elemRpt.tagName().equals("input")) {
			elemRpt.val(aText);
		} else {
			elemRpt.text(aText);
		}
	}

	public static Document CreateHtmlDoc(String aUri) throws Exception {
		File in = new File(aUri);
		Document doc = Jsoup.parse(in, null);
		return doc;
	}

	public static Document CreateHtmlDoc(File aIn) throws Exception {
		Document doc = Jsoup.parse(aIn, null);
		return doc;
	}

	public static Document CreateHtmlDoc(InputStream aIn) throws Exception {
		Document doc = Jsoup.parse(aIn, null, "file:///");
		return doc;
	}
	
	public static void PopulateHtmlData(Element htmlTable, List<List<String>> recordList) throws Exception {
		for (int cntrRec = 0; cntrRec < recordList.size(); cntrRec++) {
			// for each record
			List<String> eachRecord = recordList.get(cntrRec);
			for (int cntrField = 0; cntrField < eachRecord.size(); cntrField++) {
				// for each field
				String fieldValue = eachRecord.get(cntrField);
				Element htmlRow = GetHtmlRow(htmlTable, cntrRec + 1);
				Elements htmlCols = htmlRow.select("td");
				htmlCols.get(cntrField).text(fieldValue);
			}
		}
	}

	public static void PopulateTBodyData(Element htmlTable, List<List<String>> recordList) throws Exception {
		for (int cntrRec = 0; cntrRec < recordList.size(); cntrRec++) {
			// for each record
			List<String> eachRecord = recordList.get(cntrRec);
			for (int cntrField = 0; cntrField < eachRecord.size(); cntrField++) {
				// for each field
				String fieldValue = eachRecord.get(cntrField);
				Element htmlRow = GetTBodyRow(htmlTable, cntrRec);
				Elements htmlCols = htmlRow.select("td");
				htmlCols.get(cntrField).text(fieldValue);
			}
		}
	}

	public static Element GetTBodyRow(Element aTable, int aRowNum) throws Exception {
		Element row = null;
		//Elements tbody = aTable.select("tbody");
		//Elements rowList = tbody.select("tr");
		Elements rowList = aTable.select("tbody>tr");
		for (int cntrRow = 0; cntrRow < rowList.size(); cntrRow++) {
			//first result is the col names so skip it.
			if (cntrRow == aRowNum) {
				row = rowList.get(aRowNum);
			}
		}

		if (row == null) {
			Element existingRow = rowList.get(0);
			Element newRow = existingRow.clone();
			Elements allCell = newRow.select("td");
			for (int cntrField = 0; cntrField < allCell.size(); cntrField++) {
				Element eachCell = allCell.get(cntrField);
				eachCell.text("");
			}

			//tbody = aTable.select("tbody");
			//tbody.append(newRow.toString());
			//Elements rowAfterAppend = tbody.select("tr");
			aTable.select("tbody").get(0).append(newRow.toString());
			Elements rowAfterAppend = aTable.select("tr");
			if (aRowNum >= rowAfterAppend.size()) {
				throw new Hinderance("Fail to get or create new row for the HTML table for row: " + aRowNum + ", current total row: " + rowAfterAppend.size() + ", HTML body: " + rowAfterAppend.outerHtml());
			}
			row = GetTBodyRow(aTable, aRowNum);
		}
		return row;
	}

	public static void PopulateDivTable(Element htmlTable, List<List<String>> recordList, String rowClass, String colClass) throws Exception {
		for (int cntrRec = 0; cntrRec < recordList.size(); cntrRec++) { // for each record
			List<String> eachRecord = recordList.get(cntrRec);
			Element htmlRow = GetDivRow(htmlTable, cntrRec, rowClass);
			if (htmlRow != null) {
				Elements htmlCols = htmlRow.select(colClass);
				for (int cntrField = 0; cntrField < eachRecord.size(); cntrField++) { // for each field
					String fieldValue = eachRecord.get(cntrField);
					htmlCols.get(cntrField).text(fieldValue);
				}
			}
		}
	}

	public static Element GetDivRow(Element aTable, int aRowNum, String rowClass) throws Exception {
		Element row = null;
		Elements rowList = aTable.select(rowClass);
		for (int cntrRow = 0; cntrRow < rowList.size(); cntrRow++) {
			if (cntrRow == aRowNum) {
				row = rowList.get(aRowNum);
				break;
			}
		}

		return row;
	}
}
