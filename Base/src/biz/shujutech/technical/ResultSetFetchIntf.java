package biz.shujutech.technical;

import biz.shujutech.base.Connection;
import biz.shujutech.db.object.Clasz;

@FunctionalInterface
public interface ResultSetFetchIntf {
	public boolean callback(Connection aConn, Clasz aClasz) throws Exception;
}

