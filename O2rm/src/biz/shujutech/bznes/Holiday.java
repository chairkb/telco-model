package biz.shujutech.bznes;

import biz.shujutech.base.App;
import biz.shujutech.base.Connection;
import biz.shujutech.base.Hinderance;
import biz.shujutech.db.object.Clasz;
import biz.shujutech.db.object.FieldClasz.FetchStatus;
import biz.shujutech.db.object.ObjectBase;
import biz.shujutech.db.relational.FieldType;
import biz.shujutech.db.relational.SortOrder;
import static biz.shujutech.bznes.Country.Holiday;
import biz.shujutech.reflect.ReflectField;
import biz.shujutech.reflect.ReflectIndex;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Iterator;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

/**
 *
 */

public final class Holiday extends Clasz {
	@ReflectField(type=FieldType.DATE, indexes={@ReflectIndex(indexName="idx_holiday_date", indexNo=0, indexOrder=SortOrder.ASC, isUnique=false)}) public static String Date;
	@ReflectField(type=FieldType.STRING, size=64) public static String Descr;
	@ReflectField(type=FieldType.STRING, size=16) public static String Type;

	public static enum Type {
		NATIONAL, STATE, SCHOOL;
	}

	
	public Holiday() {
		super();
	}

	public Holiday(String aDescr, DateTime aDate, String aType) throws Exception {
		this.setDescr(aDescr);
		this.setDate(aDate);
		this.setType(aType);
	}

	public String getDescr() throws Exception {
		return(this.getValueStr(Descr));
	}

	public void setDescr(String aDescr) throws Exception {
		this.setValueStr(Descr, aDescr);
	}

	public DateTime getDate() throws Exception {
		return(this.getValueDate(Date));
	}

	public void setDate(DateTime aDate) throws Exception {
		this.setValueDate(Date, aDate);
	}

	public String getType() throws Exception {
		return(this.getValueStr(Type));
	}

	public void setType(String aType) throws Exception {
		this.setValueStr(Type, aType);
	}

	public static boolean IsPublicOrStateHoliday(Connection aConn, Country aCountry, State aState, DateTime aDate) throws Exception {
		boolean result = false;
		Holiday holiday = GetHoliday(aConn, aCountry, aDate);
		if (holiday == null) {
			holiday = GetHoliday(aConn, aState, aDate);
		}

		if (holiday != null) {
			result = true;
		}

		return(result);
	}

	public static Holiday GetHoliday(Connection aConn, Clasz aParent, DateTime aHolidayDate) throws Exception {
		Holiday result = null;
		aParent.getFieldObjectBox(Holiday).resetIterator();
		while (aParent.getFieldObjectBox(Holiday).hasNext(aConn)) {
			Holiday holiday = (Holiday) aParent.getFieldObjectBox(Holiday).getNext();
			if (holiday.getDate().equals(aHolidayDate)) {
				result = holiday;
				break;
			}
		}
		return(result);
	}

	public static void PopulateHoliday(Connection aConn, String aInputFileName) throws Exception {
		final int POSITION_HOLIDAY_COUNTRY = 0;
		final int POSITION_HOLIDAY_STATE = 1;
		final int POSITION_HOLIDAY_TYPE = 2;
		final int POSITION_HOLIDAY_DATE = 3;
		final int POSITION_HOLIDAY_DESCR = 4;

		Country.InitList(aConn);

		App.logInfo("Inserting/updating holiday record, file: " + aInputFileName);
		//final String YEAR = "2017";
		//String strPath = "C:\\Users\\chairkb\\Documents\\Shujutech\\ShujuApp\\O2rm\\src\\biz\\shujutech\\o2rm\\";
		//InputStream is = new FileInputStream(strPath + "Holiday_" + YEAR + ".txt"); 
		InputStream is = new FileInputStream(aInputFileName); 
		BufferedReader buf = new BufferedReader(new InputStreamReader(is));

		int totalInsertedCtry = 0;
		int totalInsertedState = 0;
		int totalUpdatedCtry = 0;
		int totalUpdatedState = 0;
		int totalLine = 0;
		String line = buf.readLine(); 
		StringBuilder sb = new StringBuilder(); 
		while(line != null) { 
			sb.append(line).append("\n"); 
			line = buf.readLine(); 
			totalLine++;
			if (line == null) continue;

			String[] aryStr = line.split("\\|"); // format: Malaysia|Selangor|National|20170101|New Year
			DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyymmdd");
			String holidayCountry = aryStr[POSITION_HOLIDAY_COUNTRY];
			String holidayState = aryStr[POSITION_HOLIDAY_STATE];
			String holidayType = aryStr[POSITION_HOLIDAY_TYPE];
			DateTime holidayDate = formatter.parseDateTime(aryStr[POSITION_HOLIDAY_DATE]);
			String holidayDescr = aryStr[POSITION_HOLIDAY_DESCR];

			Holiday holidayFile = (Holiday) ObjectBase.CreateObject(aConn, Holiday.class);
			holidayFile.setDescr(holidayDescr);
			holidayFile.setDate(holidayDate);
			holidayFile.setType(holidayType);

			Country country = Country.GetCountryByName(aConn, holidayCountry);
			if (country.getFieldObjectBox(Holiday).getFetchStatus() == FetchStatus.SOF) {
				country.getFieldObjectBox(Holiday).fetchAll(aConn);
			}

			if (holidayType.equalsIgnoreCase("NATIONAL")) {
				Holiday countryHoliday = country.getHoliday(aConn, holidayDate);
				if (countryHoliday == null) {
					country.addHoliday(aConn, holidayFile);
					totalInsertedCtry++;
				} else {
					countryHoliday.copyValue(holidayFile);
					totalUpdatedCtry++;
				}
				country.persistCommit(aConn);
			} else if (holidayType.equalsIgnoreCase("STATE")) {
				State state = country.getState(aConn, holidayState);
				if (state.getFieldObjectBox(Holiday).getFetchStatus() == FetchStatus.SOF) {
					state.getFieldObjectBox(Holiday).fetchAll(aConn);
				}
				Holiday stateHoliday = state.getHoliday(aConn, holidayDate);
				if (stateHoliday == null) {
					state.addHoliday(aConn, holidayFile);
					totalInsertedState++;
				} else {
					stateHoliday.copyValue(holidayFile);
					totalUpdatedState++;
				}
				state.persistCommit(aConn);
			} else {
				throw new Hinderance("The correct COUNTRY and STATE must be specify at record line: " + totalLine + ", " + line);
			}
		}
		App.logInfo("Input file: " + aInputFileName + ", total line processed: " + totalLine);
		App.logInfo("Completed inserting/updating holiday record, for file: " + aInputFileName);
	}

	public static String EachWordCapitalize(String aInput) {
		StringBuilder sb = new StringBuilder(aInput); 
		String s = sb.toString();
		int last = s.length() - 1;

		for (int i = 0; i <= last; ++i)
		if (Character.isSpaceChar(s.charAt(i)) && i < last) { 
			++i; 
			sb.setCharAt(i, Character.toUpperCase(s.charAt(i))); 
		}
		else if (i == 0) sb.setCharAt(i, Character.toUpperCase(s.charAt(i)));
		else sb.setCharAt(i, Character.toLowerCase(s.charAt(i)));
		return(sb.toString());
	}

	public static void main(String args[]) {
		ObjectBase objectDb = new ObjectBase();
		Connection conn = null;
		Options options = null;
		try {
			// setup command line
			CommandLineParser cliParser = new DefaultParser(); // apache cli 
			options = new Options();
			Option optUrl = Option.builder("url").desc("import holday dates for country and state from http://www.onestopmalaysia.com/holidays-2017.html").hasArg().argName("year").build();
			options.addOption(optUrl);
			Option optProp = Option.builder("prop").desc("property file to databse setting and etc").hasArg().argName("property file").required(true).build();
			options.addOption(optProp);
			Option optImp = Option.builder("imp").desc("import holiday dates for country and state").hasArg().argName("input file name").build();
			options.addOption(optImp);

			String strPropFile = "";
			CommandLine cliLine = cliParser.parse(options, args);
			if (cliLine.hasOption(optProp.getOpt())) {
				strPropFile = cliLine.getOptionValue(optProp.getOpt());
				if (strPropFile == null) {
					throw new ParseException("error, no property file name was specify, see usage below:"); // apparently this will never happen, as the parser will ensure there's argument
				}
			} else {
				throw new ParseException("error, no property option was specify, see usage below:"); // apparently this will never happen, as the parser will ensure there's argument
			}

			// process each command line
			if (cliLine.hasOption(optImp.getOpt())) {
				String strImportFile = cliLine.getOptionValue(optImp.getOpt());
				if (strImportFile == null) {
					throw new ParseException("error, no import file name was specify, see usage below:"); // apparently this will never happen, as the parser will ensure there's argument
				}

				App.Setup(strPropFile);
				objectDb.setupDb();
				conn = objectDb.getConnPool().getConnection();

				App.logInfo("The import file name is: " + strImportFile);
				biz.shujutech.bznes.Holiday.PopulateHoliday(conn, strImportFile);
			} else if (cliLine.hasOption("url")) {
				String strYear = cliLine.getOptionValue(optUrl.getOpt());
				if (strYear == null) {
					throw new ParseException("error, no year was specify, see usage below:"); // apparently this will never happen, as the parser will ensure there's argument
				}

				App.Setup(strPropFile);
				objectDb.setupDb();
				conn = objectDb.getConnPool().getConnection();

				Country.InitList(conn);
				String strUrl = "http://www.onestopmalaysia.com/holidays-" + strYear + ".html";
				App.logInfo("Starting to import from: " + strUrl);
				URL url = new URL(strUrl);
				Document doc = Jsoup.parse(url, 3000);
				App.logInfo("Looking for table with: " + strUrl);
				Element htmlTable = doc.select("table[class=holidays]").first();
				Iterator<Element> iteTr = htmlTable.select("tr").iterator();
				int cntrRow = 1;
				while(iteTr.hasNext()) {
					Element elemTr = iteTr.next();
					//App.logInfo("Processing row: " + cntrRow + ", record: [" + elemTr.text() + "]");
					if (cntrRow > 1) {
						Iterator<Element> iterTd = elemTr.select("td").iterator();
						String outputColDate = "";
						String outputColDesc = "";
						int cntrCol = 0;
						while(iterTd.hasNext()) {
							String strColValue = iterTd.next().text();
							//App.logInfo("Processing col value: " + strColValue);
							if (cntrCol == 0) {
								DateTimeFormatter formatter = DateTimeFormat.forPattern("d MMM yyyy");
								strColValue += " " + strYear;
								DateTime holidayDate = DateTime.parse(strColValue, formatter);
								DateTimeFormatter dtfOut = DateTimeFormat.forPattern("yyyyMMdd");
								outputColDate = dtfOut.print(holidayDate);
							} else if (cntrCol == 2) {
								outputColDesc = strColValue;
							} else if (cntrCol == 3) {
								Country country = Country.Malaysia;
								if (country.getFieldObjectBox(Country.State).getFetchStatus() == FetchStatus.SOF) {
									country.getFieldObjectBox(Country.State).fetchAll(conn);
								}

								String strState = strColValue;
								boolean isExcept = strState.toLowerCase().contains("except");
								strState = EachWordCapitalize(strState);
								strState = strState.replaceAll("Kuala Lumpur", "Wilayah Perseketuan");
								strState = strState.replaceAll("Federal Territory", "Wilayah Perseketuan");
								if (isExcept == false) {
									for (String outputColState : strState.split("[\\s+;,]+")) {
										if (country.getState(conn, outputColState) != null) {
											String outputLine = country.getName() + "|" + outputColState + "|" + "State" + "|" + outputColDate + "|" + outputColDesc;
											//App.logInfo(outputLine);
											System.out.println(outputLine);
										} else if (outputColState.contains("National")) {
											String outputLine = country.getName() + "|" + outputColState + "|" + "National" + "|" + outputColDate + "|" + outputColDesc;
											//App.logInfo(outputLine);
											System.out.println(outputLine);
										}
									}
								} else {
									country.getFieldObjectBox(Country.State).resetIterator(); // always do a reset before starting to loop for the objects
									while (country.getFieldObjectBox(Country.State).hasNext(conn)) {
										State state = (State) country.getFieldObjectBox(Country.State).getNext();
										if (strState.toLowerCase().contains(state.getDescr().toLowerCase()) == false) {
											String outputLine = country.getName() + "|" + state.getDescr() + "|" + "State" + "|" + outputColDate + "|" + outputColDesc;
											//App.logInfo(outputLine);
											System.out.println(outputLine);
										}
									}
								}
							}
							cntrCol++;
						}
					}
					cntrRow++;
				}
			} else {
				throw new ParseException("error, no argument was specify, see usage below:");
			}

		} catch (Exception ex) {
			if (ex instanceof ParseException && options != null) {
				HelpFormatter formatter = new HelpFormatter();
				App.logEror(0, ex.getMessage());
				formatter.setWidth(255);
				formatter.printHelp("java -jar O2rm.jar Holiday [-option <option> <argument>]...", options); // should print to string and uses logEror, but HelpFormatter don't seem to be able to print to string
			} else {
				App.logEror(0, new Hinderance(ex, "Application encounter fatal error, application is aborting...."));
			}
		} finally {
			if (conn != null) {
				objectDb.getConnPool().freeConnection(conn);
			}
		}		
	}

}
