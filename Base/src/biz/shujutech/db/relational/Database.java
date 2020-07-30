package biz.shujutech.db.relational;

import biz.shujutech.base.App;
import biz.shujutech.base.Connection;
import biz.shujutech.base.DateAndTime;
import biz.shujutech.base.Hinderance;
import biz.shujutech.reflect.AttribIndex;
import java.io.ByteArrayInputStream;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.sql.Types;
import java.util.Collections;
import java.util.Comparator;
import org.joda.time.DateTime;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;


public class Database extends BaseDb {
	private static final int COMPARE_KEY_OBJECT_INDEX = 1; 
	private static final int COMPARE_KEY_TABLE_INDEX = 2;

	public enum DbType {
		ORACLE, MYSQL, POSTGRESQL
	}

	public Database() {
		super();
	}

	public static void createIndex(Connection aConn, Table aTable) throws Exception {
		CreateIndex(aConn, aTable, false);
	}

	public static void createUniqueIndex(Connection aConn, Table aTable) throws Exception {
		CreateIndex(aConn, aTable, true);
	}

	private static class FieldIndexComparator implements Comparator{
		private int compareType = 0;
		public FieldIndexComparator(int aCompareType) {
			this.compareType = aCompareType;
		}

		@Override
		public int compare(Object aField1, Object aField2) {
			int result = 0;
			Field sp1 = (Field) aField1;
			Field sp2 = (Field) aField2;
			if (this.compareType == COMPARE_KEY_OBJECT_INDEX) {
				result = (sp1.getObjectKeyNo() < sp2.getObjectKeyNo()) ? -1: (sp1.getObjectKeyNo() > sp2.getObjectKeyNo()) ? 1:0 ;
			} else if (this.compareType == COMPARE_KEY_TABLE_INDEX) {
				result = (sp1.getIndexKeyNo() < sp2.getIndexKeyNo()) ? -1: (sp1.getIndexKeyNo() > sp2.getIndexKeyNo()) ? 1:0 ;
			} else {
				App.logEror("When comparing fields, must specify if the field to be compare is for indexing object or table!");
			}
			return(result);
		}
	}

	public static void CreateIndexes(Connection aConn, String aTableName, Record aRecord) throws Exception { // y not use meta record
		List<String> doneIndexName = new CopyOnWriteArrayList<>();
		List<Field> groupedIndexField = new CopyOnWriteArrayList<>();
		String targetedIndexName = "";
		do { // loop thru each field
			targetedIndexName = "";
			for (Field eachField : aRecord.getFieldBox().values()) {
				if (eachField.isInline() == false && eachField.isFlatten() == false) { // inline field and ignore flatten field cannot be index
					for (AttribIndex eachAttrib : eachField.indexes) { // one field may contain multiple index attributes
						String idxName = eachAttrib.indexName;
						if (idxName.isEmpty() == false) {
							if (doneIndexName.contains(idxName) == false) { // index never been process before
								if (targetedIndexName.isEmpty()) targetedIndexName = idxName; // found a new index, place it as target
								if (targetedIndexName.equals(idxName)) {
									Field field2Index = Field.CreateField(eachField);
									field2Index.setIndexKey(true);
									field2Index.setIndexKeyNo(eachAttrib.indexNo);
									field2Index.setIndexKeyOrder(eachAttrib.indexOrder);
									field2Index.setUniqueKey(eachAttrib.isUnique);
									groupedIndexField.add(field2Index);
								}
							}
						} else {
							throw new Hinderance("Index name in annotation must be specify, error at: " + aTableName + "." + eachField.getFieldName());
						}
					}
				} 
			}

			if (targetedIndexName.isEmpty() == false) {
				Database.CreateIndex(aConn, aTableName, targetedIndexName, groupedIndexField); // create the index
				doneIndexName.add(targetedIndexName);
			}
			groupedIndexField.clear();
		} while(targetedIndexName.isEmpty() == false);
	}

	public static void CreateIndex(Connection aConn, String aTableName, List<Field> aField2Index) throws Exception {
		CreateIndex(aConn, aTableName, "", aField2Index);
	}

	public static void CreateIndex(Connection aConn, String aTableName, String indexName, List<Field> aField2Index) throws Exception {
		String unique = "";
		for (Field eachField : aField2Index) {
			if (eachField.isUniqueKey()) {
				unique = "unique ";
				break;
			}
		}

		if (indexName == null || indexName.isEmpty()) {
			indexName = Table.GetIndexName(aTableName, aField2Index);
		}

		String strSql = "create " + unique  + "index " + indexName + " on " + aTableName;
		int totalField = 0;
		for (Field eachField : aField2Index) {
			if (totalField == 0) {  
				strSql += "(";
			} else {
				strSql += ", ";
			}
			String keyOrder = SortOrder.AsString(eachField.getIndexKeyOrder());
			strSql += eachField.getFieldName() + " " + keyOrder; 
			totalField++;
		}
		if (totalField != 0) strSql += ")";
		App.logDebg(Database.class, "Creating index: " + strSql);
		ExecuteDdl(aConn, strSql);
	}

	public static void CreateIndex(Connection aConn, Table aTable) throws Exception {
		CreateIndex(aConn, aTable, false);
	}

	public static void CreateUniqueIndex(Connection aConn, Table aTable) throws Exception {
		CreateIndex(aConn, aTable, true);
	}

	public static void CreateIndex(Connection aConn, Table aTable, boolean aUnique) throws Exception {
		String unique = "";
		String indexName = aTable.getIndexName();

		// get the fields in the table according to its IndexKeyNo first
		List<Field> aryField = new CopyOnWriteArrayList<>();
		for (Field eachField : aTable.getMetaRec().getFieldBox().values()) {
			aryField.add(eachField);
		}
		Collections.sort(aryField,new FieldIndexComparator(COMPARE_KEY_OBJECT_INDEX));

		if (aUnique) {
			unique = "unique ";
			//indexName = aTable.getUniqueIndexName();
			indexName = indexName + "_unq";
		} 
		String strSql = "create " + unique  + "index " + indexName + " on " + aTable.getTableName();
		int totalField = 0;
		for (Field eachField : aryField) {
			boolean isKey;
			if (aUnique) {
				isKey = eachField.isUniqueKey();
			} else {
				isKey = eachField.isObjectKey();
			}
			if (isKey) {
				if (totalField == 0) {  
					strSql += "(";
				} else {
					strSql += ", ";
				}
				String keyOrder = SortOrder.AsString(eachField.getObjectKeyOrder());
				strSql += eachField.getFieldName() + " " + keyOrder; 
				totalField++;
			}
		}
		if (totalField != 0) {
			strSql += ")";
		}
		//App.logInfo(Database.class, "ddl_create_index: " + strSql);
		ExecuteDdl(aConn, strSql);
	}

	@Deprecated
	public void createTable(Table aTable) throws Exception {
		Connection conn = this.connPool.getConnection();
		try {
			CreateTable(conn, aTable);
			aTable.setDb(this);
		} finally {
			if (conn != null) {
				this.connPool.freeConnection(conn);
			}
		}		
	}

	public void createTable(Connection aConn, Table aTable) throws Exception {
		CreateTable(aConn, aTable);
		aTable.setDb(this);
	}

	public static void CreateTable(Connection aConn, Table aTable) throws Exception {
		String strSql = "create table " + aTable.getTableName();
		int totalField = 0;
		for (Field eachField : aTable.getMetaRec().getFieldBox().values()) {
			if (eachField.getFieldType() == FieldType.OBJECT) {
				continue;
			} // ignore and don't do anything to OBJECT field types
			if (totalField == 0) {  
				strSql += "(";
			} else {
				strSql += ", ";
			}
			strSql += eachField.getFieldName(); 
			strSql += " " + DdlFieldType(aConn, eachField.getFieldType(), eachField.getFieldSize());
			if (eachField.getDefaultValue().isEmpty() == false) {
				strSql += " default " + eachField.getDefaultValue();
			}
			totalField++;
		}
		if (totalField != 0) {
			strSql += ")";
		}
		strSql += CreateTablePostfix(aConn);
		//App.logInfo(Database.class, "ddl_create_table: " + strSql);
		ExecuteDdl(aConn, strSql);
	}

	public void createPrimaryKey(Table aTable) throws Exception {
		Connection conn = this.connPool.getConnection();
		try {
			CreatePrimaryKey(conn, aTable);
		} finally {
			if (conn != null) {
				this.connPool.freeConnection(conn);
			}
		}		
	}

	public static List<Field> GetPkInSeq(Table aTable) throws Exception {
		List<Field> pkOrder = new CopyOnWriteArrayList<>();
		for (Field eachField : aTable.getMetaRec().getFieldBox().values()) {
			int insertAt = 0;
			for(int cntr = 0; cntr < pkOrder.size(); cntr++) {
				Field currField = pkOrder.get(cntr);
				if (currField.getPrimaryKeySeq() >= eachField.getPrimaryKeySeq()) {
					insertAt = cntr;
					break;
				} else {
					insertAt = cntr + 1;
				}
			}
			pkOrder.add(insertAt, eachField);
		}
		return(pkOrder);
	}

	public static void CreatePrimaryKey(Connection aConn, Table aTable) throws Exception {
		String strSql = "alter table " + aTable.getTableName() + " add primary key ";
		int totalField = 0;
		//for (Field eachField : aTable.getMetaRec().getFieldBox().values()) {
		for (Field eachField : GetPkInSeq(aTable)) {
			if (eachField.isPrimaryKey()) {
				if (totalField == 0) {  
					strSql += "(";
				} else {
					strSql += ", ";
				}
				strSql += eachField.getFieldName(); 
				totalField++;
			}
		}
		if (totalField != 0) {
			strSql += ")";
		}
		ExecuteDdl(aConn, strSql);
	}

	public void createSequence(String aSeqName) throws Exception {
		Connection conn = this.connPool.getConnection();
		try {
			createSequence(aSeqName);
		} finally {
			if (conn != null) {
				this.connPool.freeConnection(conn);
			}
		}		
	}

	public static void CreateSequence(Connection aConn, String aSeqName) throws Exception {
		if (GetDbType(aConn) == DbType.MYSQL || GetDbType(aConn) == DbType.ORACLE || GetDbType(aConn) == DbType.POSTGRESQL) {
			String sqlCreate = "create table " + aSeqName + " (id bigint not null)";
			ExecuteDdl(aConn, sqlCreate);
			String sqlInsert = "insert into " + aSeqName + " values(0)";
			ExecuteDdl(aConn, sqlInsert);
		}
	}

	public static long GetNextSequence(Connection aConn, String aSeqName) throws Exception {
		long result = -1;
		if (GetDbType(aConn) == DbType.MYSQL || GetDbType(aConn) == DbType.ORACLE || GetDbType(aConn) == DbType.POSTGRESQL) {
			String sqlUpdate = "update " + aSeqName + " set id = last_insert_id(id + 1)";
			String sqlSelect = "select last_insert_id() next_seq";
			if (GetDbType(aConn) == DbType.POSTGRESQL)  {
				sqlUpdate = "update " + aSeqName + " set id = id + 1";
				sqlSelect = "select id next_seq from " + aSeqName;
			}
			ExecuteDdl(aConn, sqlUpdate);

			PreparedStatement stmt = null;
			ResultSet rset = null;
			try {
				//App.logDebg(sqlSelect);
				stmt = aConn.prepareStatement(sqlSelect);
				rset = stmt.executeQuery();
				if (rset.next()) { 
					result = rset.getLong("next_seq");
				} else {
					throw new Hinderance("Fail to get the next sequence for sequence: " + aSeqName.toUpperCase());
				}
			} finally {
				if (rset != null) {
					rset.close();
				}
				if (stmt != null) {
					stmt.close();
				}
			}		
		}
		return(result);
	}


	public void executeDdl(String strSql) throws Exception {
		Connection conn = this.connPool.getConnection();
		try {
			ExecuteDdl(conn, strSql);
		} finally {
			if (conn != null) {
				this.connPool.freeConnection(conn);
			}
		}		
	}

	public static void ExecuteDdl(Connection aConn, String strSql) throws Exception {
		PreparedStatement stmt = null;
		try {
			stmt = aConn.prepareStatement(strSql);
			stmt.executeUpdate();
			//App.logInfo(Database.class, "ddl_create: " + strSql);
		} catch(Exception ex) {
			throw new Hinderance(ex, "Fail sql ddl: " + strSql);
		} finally {
			if (stmt != null) {
				stmt.close();
			}
		}		
	}

	@Deprecated
	public boolean tableExist(String aName) throws Exception {
		boolean result = false;
		Connection conn = this.connPool.getConnection();
		try {
			result = TableExist(conn, aName);
		} finally {
			if (conn != null) {
				this.connPool.freeConnection(conn);
			}
		}		
		return(result);
	}

	public boolean tableExist(Connection aConn, String aName) throws Exception {
		return(TableExist(aConn, aName));
	}

	public static boolean TableExist(Connection conn, String aName) throws Exception {
		boolean result = false;
		DatabaseMetaData meta = conn.getMetaData();
		ResultSet res = meta.getTables(null, null, aName, new String[] {"TABLE"});
		while (res.next()) {
			String tableName = res.getString("TABLE_NAME");
			if (tableName.toLowerCase().trim().equals(aName.toLowerCase().trim())) {
				result = true;
				break;
			}
 	 	}
		return(result);
	}

	public String ddlFieldPk(boolean aOid) throws Exception {
		String result = "";
		if (this.getDbType() == DbType.MYSQL || this.getDbType() == DbType.ORACLE || this.getDbType() == DbType.POSTGRESQL) {
			if (aOid) {
				result = "not null auto_increment";
			} else {
				result = "not null";
			}
		} else {
			throw new Hinderance("Unknonwn database type, jdbc url: " + this.connPool.cpUrl);
		}
		return(result);
	}

	public String ddlPk(Table aTable) throws Exception {
		String result = "";
		if (this.getDbType() == DbType.MYSQL || this.getDbType() == DbType.ORACLE || this.getDbType() == DbType.POSTGRESQL) {
			result = "primary key (" + aTable.getPkName() + ")";
		} else {
			throw new Hinderance("Unknonwn database type, jdbc url: " + this.connPool.cpUrl);
		}
		return(result);
	}

	public static String DdlFieldType(Connection aConn, FieldType aType, int aSize) throws Exception {
		String result = "";
		int sizeInt = String.valueOf(Integer.MAX_VALUE).length();
		int sizeLong = String.valueOf(Long.MAX_VALUE).length();
		if (GetDbType(aConn) == DbType.ORACLE) {
			if (aType == FieldType.STRING) {
				result = "varchar2(" + aSize + ")";
			} else if (aType == FieldType.INTEGER) {

			} else {
				throw new Hinderance("Unsupported and unknown column type: " + aType);
			}
		} else if (GetDbType(aConn) == DbType.MYSQL) {
			if (aType == FieldType.STRING) {
				result = "varchar(" + aSize + ")";
			} else if (aType == FieldType.HTML) {
				result = "varchar(" + aSize + ")";
			} else if (aType == FieldType.INTEGER) {
				result = "int(" + sizeInt + ")";
			} else if (aType == FieldType.BOOLEAN) {
				result = "tinyint(1)";
			} else if (aType == FieldType.ENCRYPT) {
				result = "blob";
			} else if (aType == FieldType.BASE64) {
				result = "blob";
			} else if (aType == FieldType.DATETIME) {
				result = "timestamp null";
			} else if (aType == FieldType.DATE) {
				result = "date null";
			} else if (aType == FieldType.LONG) {
				result = "bigint(" + sizeLong + ")";
			} else {
				throw new Hinderance("Unsupported and unknown column type: " + aType);
			}
		} else if (GetDbType(aConn) == DbType.POSTGRESQL) {
			if (aType == FieldType.STRING) {
				result = "varchar(" + aSize + ")";
			} else if (aType == FieldType.HTML) {
				result = "varchar(" + aSize + ")";
			} else if (aType == FieldType.INTEGER) {
				result = "integer";
			} else if (aType == FieldType.BOOLEAN) {
				result = "boolean";
			} else if (aType == FieldType.ENCRYPT) {
				result = "bytea";
			} else if (aType == FieldType.BASE64) {
				result = "bytea";
			} else if (aType == FieldType.DATETIME) {
				result = "timestamptz null";
			} else if (aType == FieldType.DATE) {
				result = "date null";
			} else if (aType == FieldType.LONG) {
				result = "bigint";
			} else {
				throw new Hinderance("Unsupported and unknown column type: " + aType);
			}
		}
		return(result);
	}

	public static String CreateTablePostfix(Connection aConn) throws Exception {
		String result = "";
		if (GetDbType(aConn) == DbType.ORACLE) {
		} else if (GetDbType(aConn) == DbType.MYSQL) {
			result = " engine = innodb";
		}
		return(result);
	}

	public DbType getDbType() throws Exception {
		DbType result;
		if (this.connPool.cpUrl.toLowerCase().contains("oracle")) {
			result = DbType.ORACLE;
		} else if (this.connPool.cpUrl.toLowerCase().contains("mysql")) {
			result = DbType.MYSQL;
		} else if (this.connPool.cpUrl.toLowerCase().contains("postgresql")) {      
			result = DbType.POSTGRESQL;
		} else {
			throw new Hinderance("Fail to determine the database type for connection: " + this.connPool.cpUrl);
		}
		return(result);
	}

	public static DbType GetDbType(Connection aConn) throws Exception {
		DbType result;

		DatabaseMetaData dmd = aConn.getMetaData();
		String urlMeta = dmd.getURL();
		if (urlMeta.toLowerCase().contains("oracle")) {
			result = DbType.ORACLE;
		} else if (urlMeta.toLowerCase().contains("mysql")) {
			result = DbType.MYSQL;
		} else if (urlMeta.toLowerCase().contains("postgresql")) {
			result = DbType.POSTGRESQL;
		} else {
			throw new Hinderance("Fail to determine the database type for connection: " + urlMeta);
		}
		return(result);
	}

	public FieldType java2DbFieldType(Class aType) throws Hinderance {
		FieldType result;
		if (aType == String.class) {
			result = FieldType.STRING;
		} else if (aType == Integer.class) {
			result = FieldType.INTEGER;
		} else if (aType == int.class) {
			result = FieldType.INTEGER;
		} else if (aType == DateTime.class) {
			result = FieldType.DATETIME;
		} else if (aType == Long.class) {
			result = FieldType.LONG;
		} else {
			throw new Hinderance("Unsupported and unknown column class: " + aType);
		}
		return(result);
	}

	public static String Java2DbFieldName(String dbFieldName) {
		return(Java2DbName(dbFieldName));
	}

	public static String Java2DbTableName(String dbFieldName) {
		String firstChar = (new Character(dbFieldName.charAt(0))).toString();
/*
int count = StringUtils.countMatches(dbFieldName, firstChar.toUpperCase());
if (count > 1) {
	App.logDebg(Database.class, "ZZZZZZZZZZZZZZZZZZZZZZZZZZZZZ: " + dbFieldName);
}
*/
		String javaName = dbFieldName.replaceFirst(firstChar, firstChar.toLowerCase());
		return(Java2DbName(javaName));
	}

	private static String Java2DbName(String aFieldName) {
		String result = aFieldName.substring(0, 1).toLowerCase();
		char[] charArray = aFieldName.substring(1).toCharArray();
		for (Character eachChar : charArray) {
			if (Character.isUpperCase(eachChar)) {
				result += "_" + Character.toLowerCase(eachChar);
			}
			else {
				result += eachChar;
			}
		}
		return(result);
	}

	public static FieldType Sql2DbFieldType(int aType) throws Hinderance {
		FieldType result;
		if (aType == Types.VARCHAR || aType == Types.CHAR || aType == Types.LONGNVARCHAR || aType == Types.LONGVARCHAR || aType == Types.LONGVARCHAR || aType == Types.NCHAR || aType == Types.NVARCHAR) {
			result = FieldType.STRING;
		} else if (aType == Types.INTEGER) {
			result = FieldType.INTEGER;
		} else if (aType == Types.DATE) {
			result = FieldType.DATE;
		} else if (aType == Types.TIMESTAMP) {
			result = FieldType.DATETIME;
		} else if (aType == Types.BIGINT) {
			result = FieldType.LONG;
		} else if (aType == Types.BIT) {
			result = FieldType.BOOLEAN;
		} else if (aType == Types.LONGVARBINARY) {
			result = FieldType.BASE64;
		} else if (aType == Types.BINARY) {
			result = FieldType.BASE64;
		} else {
			throw new Hinderance("Unsupported and unknown java sql type: " + aType);
		}
		return(result);
	}

	public static void ShowSetStmt(List<Field> fieldArr) throws Exception {
		for(int cntrField = 0, cntrStmt = 0; cntrField < fieldArr.size(); cntrField++, cntrStmt++) {
			Field eachField = fieldArr.get(cntrField);
			if (eachField.getFieldType() == FieldType.OBJECT) { // ignore and don't do anything to OBJECT field types
				continue;
			} 
			if (eachField.getFieldType() == FieldType.OBJECTBOX) { // ignore and don't do anything to OBJECT field types
				continue;
			} 
			String valueDb = eachField.getValueStr();
			App.logInfo("Setting preparedstatement for col: " + eachField.getFieldName().toUpperCase() + ", value: " + valueDb);
		}
	}

	public static void SetStmtValue(PreparedStatement stmt, List<Field> fieldArr) throws Exception {
		SetStmtValue(stmt, fieldArr, 0);
	}


	public static void SetStmtValue(PreparedStatement stmt, List<Field> fieldArr, int aStartIndex) throws Exception {
		for(int cntrField = 0, cntrStmt = aStartIndex; cntrField < fieldArr.size(); cntrField++, cntrStmt++) {
			Field eachField = fieldArr.get(cntrField);
			if (eachField != null) {
				try {
					if (eachField.getFieldType() == FieldType.OBJECT) { // ignore and don't do anything to OBJECT field types
						continue;
					} 
					if (eachField.getFieldType() == FieldType.OBJECTBOX) { // ignore and don't do anything to OBJECT field types
						continue;
					} 
					Object valueDb = eachField.getValueObj();
		
					if (valueDb == null) {
						if (eachField.getFieldType() == FieldType.DATETIME) {
							stmt.setTimestamp(cntrStmt + 1, null);					
						} else {
							stmt.setObject(cntrStmt + 1, null);					
						}
					} else	if (eachField.getFieldType() == FieldType.STRING) {
						stmt.setString(cntrStmt + 1, eachField.getValueStr());
					} else	if (eachField.getFieldType() == FieldType.HTML) {
						stmt.setString(cntrStmt + 1, eachField.getValueStr());
					} else	if (eachField.getFieldType() == FieldType.DATETIME) {
						String dbDate = DateAndTime.FormatForJdbcTimestamp(((FieldDateTime) eachField).getValueDateTime());
						java.sql.Timestamp dateValue = java.sql.Timestamp.valueOf(dbDate); // format must be in "2005-04-06 09:01:10"
						stmt.setTimestamp(cntrStmt + 1, dateValue);
					} else	if (eachField.getFieldType() == FieldType.DATE) {
						String dbDate = DateAndTime.FormatForJdbcDate(((FieldDate) eachField).getValueDate());
						java.sql.Date dateValue = java.sql.Date.valueOf(dbDate); // format must be in "2005-04-06 09:01:10"
						stmt.setDate(cntrStmt + 1, dateValue);
					} else	if (eachField.getFieldType() == FieldType.LONG) {
						stmt.setLong(cntrStmt + 1, ((FieldLong) eachField).getValueLong());
					} else	if (eachField.getFieldType() == FieldType.INTEGER) {
						stmt.setInt(cntrStmt + 1, ((FieldInt) eachField).getValueInt());
					} else	if (eachField.getFieldType() == FieldType.BOOLEAN) {
						stmt.setBoolean(cntrStmt + 1, ((FieldBoolean) eachField).getValueBoolean());
					} else	if (eachField.getFieldType() == FieldType.ENCRYPT) {
						//stmt.setString(cntrStmt + 1, (String) eachField.getValueObj());
						stmt.setBytes(cntrStmt + 1, ((String) eachField.getValueObj()).getBytes());
					} else	if (eachField.getFieldType() == FieldType.BASE64) {
						//stmt.setString(cntrStmt + 1, (String) eachField.getValueObj());
						byte[] strAsByte = eachField.getValueStr().getBytes();
						ByteArrayInputStream bais = new ByteArrayInputStream(strAsByte);
						stmt.setBinaryStream(cntrStmt + 1, bais, strAsByte.length);
					} else {
						throw new Hinderance("Unknown type for field: " + eachField.getFieldName().toUpperCase() + " when attempting to set it for SQL operation");
					}
				} catch(Exception ex) {
					throw new Hinderance(ex, "Fail to place value into sql stmt, field: " + eachField.getFieldName().toUpperCase() + ", sql: " + stmt.toString());
				}
			}
		}
	}

	public static List<Field> GetWhereClause(String aTableName, Record aWhere, StringBuffer aWhereStr) {
		int cntrWhere = 0;
		String sqlWhere = "";
		List<Field> fieldArr = new CopyOnWriteArrayList<>();
		if (aWhere != null && aWhere.totalField() != 0) {
			for (Field eachField : aWhere.getFieldBox().values()) {
				if (cntrWhere != 0) {
					sqlWhere += " and ";
				}
				if (eachField.getFormulaStr().isEmpty()) {
					sqlWhere += aTableName + "." + eachField.getFieldName() + " = ?";
				} else {
					sqlWhere += eachField.getFormulaStr();
				}
				fieldArr.add(eachField); // place in array for resultSet.setObject according to ? position
				cntrWhere++;
			}
		}
		aWhereStr.append(sqlWhere);
		return(fieldArr);
	}

	public static List<Field> GetFieldToUpdate(Connection aConn, String aTableName, Record aField2Update, StringBuffer aField2UpdateStr) throws Exception {
		String sqlField2Update = "";
		List<Field> fieldArr = new CopyOnWriteArrayList<>();
		if (aField2Update != null && aField2Update.totalField() != 0) {
			for (Field eachField : aField2Update.getFieldBox().values()) {
				if (eachField.isModified() == false) { // ignore fields that was never changed
					continue;
				}

				if (eachField.getFieldType() == FieldType.OBJECT) { // ignore and don't do anything to OBJECT field types
					continue;
				}
				if (eachField.getFieldType() == FieldType.OBJECTBOX) { // ignore and don't do anything to OBJECT field types
					continue;
				} 

				if (sqlField2Update.isEmpty() == false) { // only append comma in between columns
					sqlField2Update += ", ";
				} 
				if (eachField.getFormulaStr().isEmpty()) {
					if (Database.GetDbType(aConn) == DbType.MYSQL  || Database.GetDbType(aConn) == Database.DbType.ORACLE) {
						sqlField2Update += aTableName + "." + eachField.getFieldName() + " = ?";
					} else {
						sqlField2Update += eachField.getFieldName() + " = ?";
					}
				}
				else {
					if (Database.GetDbType(aConn) == DbType.MYSQL  || Database.GetDbType(aConn) == Database.DbType.ORACLE) {
						sqlField2Update += aTableName + "." + eachField.getFieldName() + " = " + eachField.getFormulaStr();
					} else {
						sqlField2Update += eachField.getFieldName() + " = " + eachField.getFormulaStr();
					}
				}
				fieldArr.add(eachField); // place in array for resultSet.setObject according to ? position
				//App.logDebg("Field to update: " + eachField.getFieldName() + ", value: " + eachField.getValueStr());
			}
		}
		aField2UpdateStr.append(sqlField2Update);
		return(fieldArr);
	}

	public static List<Field> GetFieldToInsert(String aTableName, Record aRec2Insert, StringBuffer aField, StringBuffer aHolder) throws Exception {
		String strField = "";
		String strHolder = "";
		List<Field> fieldArr = new CopyOnWriteArrayList<>();
		if (aRec2Insert != null && aRec2Insert.totalField() != 0) {
			for (Field eachField : aRec2Insert.getFieldBox().values()) {

				if (eachField.getFieldType() == FieldType.OBJECT) {
					continue;
				} // ignore and don't do anything to OBJECT field types
				if (eachField.getFieldType() == FieldType.OBJECTBOX) {
					continue;
				} // ignore and don't do anything to OBJECT field types

				if (strField.isEmpty() == false) {
					strField += ", ";
				} // only append comma in between columns
				if (strHolder.isEmpty() == false) {
					strHolder += ", ";
				} // only append comma in between columns

				//strField += aTableName + "." + eachField.getFieldName();
				strField += eachField.getFieldName();
				if (eachField.getFormulaStr().isEmpty()) {
					strHolder += "?";
				} else {
					strHolder += eachField.getFormulaStr();
				}
				fieldArr.add(eachField); // place in array for resultSet.setObject according to ? position
				//App.logDebg("Field to Insert: " + eachField.getFieldName() + ", value: " + eachField.getValueStr());
			}
		}
		aField.append(strField);
		aHolder.append(strHolder);
		return(fieldArr);
	}

	public static String DdlForDecrypt(String aFieldName) {
		String result = aFieldName;
		return(result);
	}
	
	public int fetch(Table aTable, Record aSelect, Record aWhere) throws Exception {
		Connection conn = this.connPool.getConnection();
		try {
			return(aTable.fetch(conn, aSelect, aWhere));
		} finally {
			if (conn != null) {
				this.connPool.freeConnection(conn);
			}
		}		
	}
	
	public void alterTableAddColumn(Table aTable) throws Exception {
		Connection conn = this.connPool.getConnection();
		try {
			AlterTableAddColumn(conn, aTable);
		} finally {
			if (conn != null) {
				this.connPool.freeConnection(conn);
			}
		}		
	}

	public static void AlterTableAddColumn(Connection aConn, Table aTable) throws Exception {
		String strSql = "alter table " + aTable.getTableName() + " add ";
		int totalField = 0;
		for (Field eachField : aTable.getMetaRec().getFieldBox().values()) {
			if (eachField.getFieldType() == FieldType.OBJECT) {
				continue;
			} // ignore and don't do anything to OBJECT field types
			if (eachField.getFieldType() == FieldType.OBJECTBOX) {
				continue;
			} // ignore and don't do anything to OBJECTBOX field types

			if (totalField == 0) {  
				if (Database.GetDbType(aConn) == DbType.MYSQL || Database.GetDbType(aConn) == DbType.ORACLE) {
					strSql += "(";
				} else {
					// do nothing
				}
			} else {
				if (Database.GetDbType(aConn) == DbType.MYSQL || Database.GetDbType(aConn) == DbType.ORACLE) {
					strSql += ", ";
				} else {
					strSql += ", add ";
				}
			}
			strSql += eachField.getFieldName(); 
			strSql += " " + DdlFieldType(aConn, eachField.getFieldType(), eachField.getFieldSize());
			totalField++;
		}
		if (totalField != 0) {
			if (Database.GetDbType(aConn) == DbType.MYSQL || Database.GetDbType(aConn) == DbType.ORACLE) {
				strSql += ")";
			} else {
				// do nothing
			}
		}
		ExecuteDdl(aConn, strSql);
	}

	public boolean anyFieldExist(String aName, Record aRecord) throws Exception {
		Connection conn = this.connPool.getConnection();
		try {
			return(AnyFieldExist(conn, aName, aRecord));
		} finally {
			if (conn != null) {
				this.connPool.freeConnection(conn);
			}
		}		
	}

	public static boolean AnyFieldExist(Connection conn, String aName, Record aRecord) throws Exception {
		boolean result = false;
		Statement stmt = null;
		ResultSet rset = null;
		try {
			stmt = conn.createStatement();
			String sql = "select * from " + aName + " where 1 = 2";
			rset = stmt.executeQuery(sql);
			ResultSetMetaData metaData = rset.getMetaData();
			for(int cntrCol = 0; cntrCol < metaData.getColumnCount(); cntrCol++) {
				String fieldName = metaData.getColumnName(cntrCol + 1).toLowerCase().trim();
				for(Field eachField : aRecord.getFieldBox().values()) {
					if (eachField.getFieldName().toLowerCase().trim().equals(fieldName)) {
						result = true;
						break;
					}
				}
			}
		} finally {
			if (rset != null) {
				rset.close();
			}
			if (stmt!= null) {
				stmt.close();
			}
		}
		return(result);
	}

	public static List<List> ExecuteSQL(Connection aConn, String aSqlStr, List<Field> aBindField) throws Exception {
		PreparedStatement stmt = aConn.prepareStatement(aSqlStr);
		return(ExecuteSQL(aConn, stmt, aBindField));
	}

	public static List<List> ExecuteSQL(Connection aConn, PreparedStatement aStmt, List<Field> aBindField) throws Exception {
		List<List> listOfLists = new CopyOnWriteArrayList<>();
		Database.SetStmtValue(aStmt, aBindField);
		ResultSet rset = aStmt.executeQuery();
		ResultSetMetaData metaData = rset.getMetaData();
		int columns = metaData.getColumnCount();
		while (rset.next()) {
			List<String> list = new CopyOnWriteArrayList<String>();
			for (int cntr = 0; cntr < columns; cntr++) {
				list.add(rset.getString(cntr + 1));
			}
			listOfLists.add(list);
		}

		return listOfLists;
	}

	public static boolean DuplicateError(Exception ex) {
		boolean result = false;
		String strStack;
		if (ex instanceof Hinderance) {
			strStack = ((Hinderance) ex).getExMsg();
		} else {
			strStack = ExceptionUtils.getStackTrace(ex);
		}
		if (strStack.contains("Duplicate entry")) {
			result = true;
		}
		return(result);
	}
}
