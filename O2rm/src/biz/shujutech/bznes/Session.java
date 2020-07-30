package biz.shujutech.bznes;

import java.util.concurrent.CopyOnWriteArrayList;
import biz.shujutech.base.Connection;
import biz.shujutech.db.object.Clasz;
import biz.shujutech.db.relational.FieldType;
import biz.shujutech.db.relational.SortOrder;
import biz.shujutech.reflect.ReflectField;
import biz.shujutech.reflect.ReflectIndex;
import org.joda.time.DateTime;

public class Session extends Clasz {
	@ReflectField(type=FieldType.STRING, size=128, indexes={@ReflectIndex(indexName="idx_sess_id", indexNo=0, indexOrder=SortOrder.ASC, isUnique=true)}) public static String SessionId;
	@ReflectField(type=FieldType.STRING, size=32) public static String SessionIp;
	@ReflectField(type=FieldType.DATETIME) public static String LoginTime;
	@ReflectField(type=FieldType.DATETIME) public static String LogoutTime;
	
	public String getSessionId() throws Exception {
		return(this.getValueStr(SessionId));
	}

	public void setSessionId(String aValue) throws Exception {
		this.setValueStr(SessionId, aValue);
	}

	public String getSessionIp() throws Exception {
		return(this.getValueStr(SessionIp));
	}

	public void setSessionIp(String aValue) throws Exception {
		this.setValueStr(SessionIp, aValue);
	}

	public void setLoginTime(DateTime aValue) throws Exception {
		this.setValueDateTime(LoginTime, aValue);
	}

	public void setLogoutDate(DateTime aValue) throws Exception {
		this.setValueDateTime(LogoutTime, aValue);
	}

	public User getUser(Connection aConn, String aSessionId) throws Exception {
		User user = null;
		CopyOnWriteArrayList<Clasz> users = this.getAllParentOfMemberFieldBox(aConn, User.class, "Session");
		for(Clasz eachClasz: users) {
			user = (User) eachClasz;
			break; // a sesssion can only be assigned to one user
		}
		return(user);
	}


}
