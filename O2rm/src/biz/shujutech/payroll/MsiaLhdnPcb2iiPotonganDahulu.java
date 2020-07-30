package biz.shujutech.payroll;

import biz.shujutech.db.object.Clasz;
import biz.shujutech.db.relational.FieldType;
import biz.shujutech.reflect.ReflectField;

public class MsiaLhdnPcb2iiPotonganDahulu extends Clasz {
	@ReflectField(type=FieldType.STRING, size=32) public static String Jenis;
	@ReflectField(type=FieldType.STRING, size=32) public static String Bulan;
	@ReflectField(type=FieldType.STRING, size=32) public static String Tahun;
	@ReflectField(type=FieldType.STRING, size=32) public static String AmaunPcb;
	@ReflectField(type=FieldType.STRING, size=32) public static String NoResit;
	@ReflectField(type=FieldType.STRING, size=32) public static String TarikhResit;

	public String getJenis() throws Exception {
		return(this.getValueStr(Jenis));
	}

	public void setJenis(String aJenis) throws Exception {
		this.setValueStr(MsiaLhdnPcb2iiPotonganDahulu.Jenis, aJenis);
	}

	public String getBulan() throws Exception {
		return(this.getValueStr(Bulan));
	}

	public void setBulan(String aBulan) throws Exception {
		this.setValueStr(MsiaLhdnPcb2iiPotonganDahulu.Bulan, aBulan);
	}

	public String getTahun() throws Exception {
		return(this.getValueStr(Tahun));
	}

	public void setTahun(String aTahun) throws Exception {
		this.setValueStr(MsiaLhdnPcb2iiPotonganDahulu.Tahun, aTahun);
	}

	public String getAmaunPcb() throws Exception {
		return(this.getValueStr(AmaunPcb));
	}

	public void setAmaunPcb(String aAmaunPcb) throws Exception {
		this.setValueStr(MsiaLhdnPcb2iiPotonganDahulu.AmaunPcb, aAmaunPcb);
	}

	public String getTarikhResit() throws Exception {
		return(this.getValueStr(TarikhResit));
	}

	public void setTarikhResit(String aTarikhResit) throws Exception {
		this.setValueStr(MsiaLhdnPcb2iiPotonganDahulu.TarikhResit, aTarikhResit);
	}

	public String getNoResit() throws Exception {
		return(this.getValueStr(NoResit));
	}

	public void setNoResit(String aNoResit) throws Exception {
		this.setValueStr(MsiaLhdnPcb2iiPotonganDahulu.NoResit, aNoResit);
	}
}
