package biz.shujutech.bznes;

import biz.shujutech.base.Connection;
import biz.shujutech.base.Hinderance;
import biz.shujutech.db.object.Clasz;
import biz.shujutech.db.object.ClaszDual;
import biz.shujutech.db.object.FieldObjectBox;
import biz.shujutech.db.relational.FieldType;
import biz.shujutech.db.relational.SortOrder;
import biz.shujutech.reflect.ReflectField;
import biz.shujutech.reflect.ReflectIndex;
import java.util.concurrent.CopyOnWriteArrayList;
import biz.shujutech.db.object.Lookup;
import biz.shujutech.db.object.ObjectBase;

/*
	Admin - by default, no accessible checks, all is allow
	Worker or Non Admin - by default, all is not accessible, need to create a Permission object to access
*/
public class UserRole extends Clasz implements Lookup {
	@ReflectField(type=FieldType.STRING, size=32, indexes={@ReflectIndex(indexName="idx_usrrole_descr", indexNo=0, indexOrder=SortOrder.ASC, isUnique=true)}) public static String Descr;  
	@ReflectField(type=FieldType.OBJECTBOX, deleteAsMember=false, clasz="biz.shujutech.bznes.UserRolePermission") public static String Permission;
	@ReflectField(type=FieldType.INTEGER) public static String Ranking;

	public static final String FIELD_ALL_ROLE = "all_role";
	public static CopyOnWriteArrayList<Lookup> LookupList = new CopyOnWriteArrayList<>();

	public static String AdminDescr = "Admin";
	public static UserRole Admin = null;
	public static int AdminRanking = 10; // use interval of 10, admin is the most privilege role

	public static String WorkerDescr = "EmployeE";
	public static UserRole Worker = null;
	public static int WorkerRanking = 50; 

	@Override
	public String getValueStr() throws Exception {
		return(this.getDescr());
	}

	@Override
	public String getDescr() throws Exception {
		return(this.getValueStr(Descr));
	}

	@Override
	public void setDescr(String aDescr) throws Exception {
		this.setValueStr(Descr, aDescr);
	}

	@Override
	public CopyOnWriteArrayList<Lookup> getLookupList() {
		return(LookupList);
	}

	public FieldObjectBox getPermission() throws Exception {
		return (this.getFieldObjectBox(Permission));
	}

	public boolean isAdmin() throws Exception {
		boolean result = false;
		if (this.getDescr().equalsIgnoreCase(AdminDescr)) {
			result = true;
		}
		return(result);
	}

	public boolean isEmployee() throws Exception {
		boolean result = false;
		if (this.getDescr().equalsIgnoreCase(WorkerDescr)) {
			result = true;
		}
		return(result);
	}

	public int getRanking () throws Exception {
		return(this.getValueInt(Ranking));
	}

	public void setRanking(int aRanking) throws Exception {
		this.setValueInt(Ranking, aRanking);
	}

	public boolean addPermission(Connection aConn, String aItemName, String aItemType, String aPermissionType) throws Exception {
		boolean newAdd = false;
		UserRolePermission permissionToAdd = (UserRolePermission) ObjectBase.CreateObject(aConn, UserRolePermission.class);
		permissionToAdd.setItemName(aItemName);
		if (permissionToAdd.populate(aConn) == false) {
			permissionToAdd.setItemType(aItemType);
			permissionToAdd.setPermissionType(aPermissionType);
			this.addValueObject(aConn, Permission, permissionToAdd);
			newAdd = true;
		}
		return(newAdd);
	}

	@Override
	public void initialize(Connection aConn) throws Exception {
		if (this.getValueStr().equals(UserRole.AdminDescr)) {
			this.setRanking(AdminRanking);
		} else if (this.getValueStr().equals(UserRole.WorkerDescr)) {
			boolean newAdd = false;
			this.setRanking(WorkerRanking);
			if (this.addPermission(aConn, "WorkerPortalMyInfo", UserRolePermission.ITEM_TYPE_URL, UserRolePermission.PERMISSION_URL_ACCESSIBLE)) newAdd = true;
			if (this.addPermission(aConn, "WorkerPortalEmployment", UserRolePermission.ITEM_TYPE_URL, UserRolePermission.PERMISSION_URL_ACCESSIBLE)) newAdd = true;
			if (this.addPermission(aConn, "EmployeeForm", UserRolePermission.ITEM_TYPE_URL, UserRolePermission.PERMISSION_URL_ACCESSIBLE)) newAdd = true;
			if (this.addPermission(aConn, "PayslipForm", UserRolePermission.ITEM_TYPE_URL, UserRolePermission.PERMISSION_URL_ACCESSIBLE)) newAdd = true;
			if (this.addPermission(aConn, "WorkerPortalDownloadPayslip", UserRolePermission.ITEM_TYPE_URL, UserRolePermission.PERMISSION_URL_ACCESSIBLE)) newAdd = true;
			if (this.addPermission(aConn, "WorkerPortalDownloadEa", UserRolePermission.ITEM_TYPE_URL, UserRolePermission.PERMISSION_URL_ACCESSIBLE)) newAdd = true;
			if (this.addPermission(aConn, "MsiaLhdnPcbTp3Parent", UserRolePermission.ITEM_TYPE_URL, UserRolePermission.PERMISSION_URL_ACCESSIBLE)) newAdd = true;
			if (this.addPermission(aConn, "MsiaLhdnPcbTp3_\\d{4}", UserRolePermission.ITEM_TYPE_URL, UserRolePermission.PERMISSION_URL_ACCESSIBLE)) newAdd = true;
			//if (this.addPermission(aConn, "EmployeePortal", UserRolePermission.ITEM_TYPE_URL, UserRolePermission.PERMISSION_URL_ACCESSIBLE)) newAdd = true;
			if (this.addPermission(aConn, "WorkerPortalMenu", UserRolePermission.ITEM_TYPE_URL, UserRolePermission.PERMISSION_URL_ACCESSIBLE)) newAdd = true;
			if (this.addPermission(aConn, "ContactUs", UserRolePermission.ITEM_TYPE_URL, UserRolePermission.PERMISSION_URL_ACCESSIBLE)) newAdd = true;
			if (newAdd) this.persistCommit(aConn);
		} else {
			throw new Hinderance("Fail to create lookup instant, class UserRole for: " + this.getValueStr());
		}
	}


	public static void InitList(Connection aConn) throws Exception {
		ObjectBase.CreateObject(aConn, UserRole.class); // check if table already exist, if not create it
		Lookup.ClearAndLoadList(aConn, UserRole.class, LookupList);
		Admin = (UserRole) Lookup.InsertDefaultList(aConn, Admin, UserRole.class, AdminDescr, LookupList);
		Worker = (UserRole) Lookup.InsertDefaultList(aConn, Worker, UserRole.class, WorkerDescr, LookupList);
	}

	public static ClaszDual GetAllLookupTransient(Connection aConn) throws Exception {
		ClaszDual allRoleClasz = (ClaszDual) ObjectBase.CreateObjectTransient(aConn, ClaszDual.class);
		allRoleClasz.createFieldObjectBoxTransient(aConn, FIELD_ALL_ROLE, UserRole.class);
		for (Lookup aEachRole : UserRole.LookupList) {
			((Clasz) aEachRole).getField(UserRole.Descr).forRemove(false);
			((Clasz) aEachRole).removeMarkField();
			allRoleClasz.addValueObject(aConn, FIELD_ALL_ROLE, (Clasz) aEachRole);
		}
		return(allRoleClasz);
	}
}
