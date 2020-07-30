package biz.shujutech.bznes;

import biz.shujutech.db.object.Clasz;
import biz.shujutech.db.relational.FieldType;
import biz.shujutech.db.relational.SortOrder;
import biz.shujutech.reflect.ReflectField;
import biz.shujutech.reflect.ReflectIndex;

public class AreaCode extends Clasz {
	@ReflectField(type=FieldType.STRING, size=8, indexes={@ReflectIndex(indexName="idx_ac_descr", indexNo=0, indexOrder=SortOrder.ASC, isUnique=false)}) public static String Descr;

	public AreaCode() throws Exception {
		super();
	}

	public AreaCode(String aArea) throws Exception {
		this.setAreaCode(aArea);
	}

	@Override
	public String getValueStr() throws Exception {
		return(this.getValueStr(Descr));
	}

	public String getAreaCode() throws Exception {
		return(this.getValueStr(Descr));
	}

	public void setAreaCode(String aArea) throws Exception {
		this.setValueStr(Descr, aArea);
	}

	
}

