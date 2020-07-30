package biz.shujutech.bznes;

import biz.shujutech.db.object.Clasz;
import biz.shujutech.db.relational.FieldType;
import biz.shujutech.db.relational.SortOrder;
import biz.shujutech.reflect.ReflectField;
import biz.shujutech.reflect.ReflectIndex;

public class NetworkDestinationCode extends Clasz {
	@ReflectField(type=FieldType.STRING, size=8, indexes={@ReflectIndex(indexName="idx_ndc_descr", indexNo=0, indexOrder=SortOrder.ASC, isUnique=false)}) public static String Descr;

	public NetworkDestinationCode() throws Exception {
		super();
	}

	public NetworkDestinationCode(String aNdc) throws Exception {
		this.setNdc(aNdc);
	}

	@Override
	public String getValueStr() throws Exception {
		return(this.getValueStr(Descr));
	}

	public String getNdc() throws Exception {
		return(this.getValueStr(Descr));
	}

	public void setNdc(String aNdc) throws Exception {
		this.setValueStr(Descr, aNdc);
	}

	
}
