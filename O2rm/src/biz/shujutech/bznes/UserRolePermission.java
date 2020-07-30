/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package biz.shujutech.bznes;

import biz.shujutech.db.object.Clasz;
import biz.shujutech.db.relational.FieldType;
import biz.shujutech.db.relational.SortOrder;
import biz.shujutech.reflect.ReflectField;
import biz.shujutech.reflect.ReflectIndex;

/**
 *
 */
public class UserRolePermission extends Clasz {

	/**
	 *
	 */
	@ReflectField(type=FieldType.STRING, size=128, indexes={@ReflectIndex(indexName="idx_urp_descr", indexNo=0, indexOrder=SortOrder.ASC, isUnique=true)}) public static String ItemName;
	@ReflectField(type=FieldType.STRING, size=16) public static String ItemType;
	@ReflectField(type=FieldType.STRING, size=32) public static String PermissionType;

	public static final String ITEM_TYPE_URL = "Url";
	public static final String ITEM_TYPE_WIDGET = "Widget";

	public static final String PERMISSION_URL_ACCESSIBLE = "Accessible";
	public static final String PERMISSION_URL_BLOCK = "Block";
	public static final String PERMISSION_WIDGET_READ_ONLY = "Read Only";
	public static final String PERMISSION_WIDGET_HIDDEN = "Hidden";

	public String getItemName() throws Exception {
		return(this.getValueStr(ItemName));
	}

	public String getItemType() throws Exception {
		return(this.getValueStr(ItemType));
	}

	public void setItemName(String aValue) throws Exception {
		this.setValueStr(ItemName, aValue);
	}

	public void setItemType(String aValue) throws Exception {
		this.setValueStr(ItemType, aValue);
	}
	
	public String getPermissionType() throws Exception {
		return(this.getValueStr(PermissionType));
	}

	public void setPermissionType(String aValue) throws Exception {
		this.setValueStr(PermissionType, aValue);
	}

	
	
}
