package biz.shujutech.bznes;

import biz.shujutech.base.Connection;
import biz.shujutech.db.object.Clasz;
import biz.shujutech.db.object.Lookup;

public abstract class GotIntf extends Clasz {
	abstract public void setType(Lookup aValue) throws Exception; 
	abstract public void setType(String aLookupName) throws Exception; 
	abstract public Lookup getType() throws Exception;
	abstract public String getDescr(Connection aConn) throws Exception;
	abstract public Object getValue() throws Exception;
	abstract public Object getValue(Connection aConn) throws Exception;
	abstract public Object createValue(Connection conn) throws Exception;
	abstract public void setValue(Object aObj) throws Exception;


	public static void PopulateGotIntf(Connection aConn, Clasz aMaster, String aFieldName) throws Exception {
		aMaster.getFieldObjectBox(aFieldName).fetchAll(aConn);
		aMaster.getFieldObjectBox(aFieldName).resetIterator();
		while (aMaster.getFieldObjectBox(aFieldName).hasNext(aConn)) {
			GotIntf pgad = (GotIntf) aMaster.getFieldObjectBox(aFieldName).getNext();
			if (pgad != null) {
				pgad.populateLookupField(aConn);
				if (pgad.getValue(aConn) != null && pgad.getValue(aConn) instanceof Clasz) {
					Clasz fieldObj = (Clasz) pgad.getValue();
					fieldObj.populateLookupField(aConn);
				}
			}
		}
	}

	public Object createValue() throws Exception {
		Connection conn = null;
		try {
			conn = this.getDb().getConnPool().getConnection();
			Object object = GotIntf.this.createValue(conn);
			return(object);
		} finally {
			if (conn != null) {
				db.getConnPool().freeConnection(conn);
			}
		}
	}


	public String gotDescr(Connection aConn, String aFieldName) throws Exception {
		Clasz gotLookup = (Clasz) this.gotValueObject(aConn, aFieldName);
		if (gotLookup == null) {
			return("");
		} else {
			return(((Lookup) gotLookup).getDescr());
		}
	}
	
}
