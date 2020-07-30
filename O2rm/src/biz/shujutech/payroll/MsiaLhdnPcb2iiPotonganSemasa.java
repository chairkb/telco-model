package biz.shujutech.payroll;

import biz.shujutech.db.object.Clasz;
import biz.shujutech.db.relational.FieldType;
import biz.shujutech.reflect.ReflectField;

public class MsiaLhdnPcb2iiPotonganSemasa extends Clasz {
	
	@ReflectField(type=FieldType.STRING, size=32) public static String Bulan;
	@ReflectField(type=FieldType.STRING, size=32) public static String AmaunPcb;
	@ReflectField(type=FieldType.STRING, size=32) public static String AmaunCp38;
	@ReflectField(type=FieldType.STRING, size=32) public static String ResitPcb;
	@ReflectField(type=FieldType.STRING, size=32) public static String ResitCp38;
	@ReflectField(type=FieldType.STRING, size=32) public static String TarikhPcb;
	@ReflectField(type=FieldType.STRING, size=32) public static String TarikhCp38;

	public String getBulan() throws Exception {
		return(this.getValueStr(Bulan));
	}

	public void setBulan(String aBulan) throws Exception {
		this.setValueStr(MsiaLhdnPcb2iiPotonganSemasa.Bulan, aBulan);
	}

	public String getAmaunPcb() throws Exception {
		return(this.getValueStr(AmaunPcb));
	}

	public void setAmaunPcb(String aAmaunPcb) throws Exception {
		this.setValueStr(MsiaLhdnPcb2iiPotonganSemasa.AmaunPcb, aAmaunPcb);
	}

	public String getAmaunCp38() throws Exception {
		return(this.getValueStr(AmaunCp38));
	}

	public void setAmaunCp38(String aAmaunCp38) throws Exception {
		this.setValueStr(MsiaLhdnPcb2iiPotonganSemasa.AmaunCp38, aAmaunCp38);
	}

	public String getResitPcb() throws Exception {
		return(this.getValueStr(ResitPcb));
	}

	public void setResitPcb(String aResitPcb) throws Exception {
		this.setValueStr(MsiaLhdnPcb2iiPotonganSemasa.ResitPcb, aResitPcb);
	}

	public String getResitCp38() throws Exception {
		return(this.getValueStr(ResitCp38));
	}

	public void setResitCp38(String aResitCp38) throws Exception {
		this.setValueStr(MsiaLhdnPcb2iiPotonganSemasa.ResitCp38, aResitCp38);
	}

	public String getTarikhPcb() throws Exception {
		return(this.getValueStr(TarikhPcb));
	}

	public void setTarikhPcb(String aTarikhPcb) throws Exception {
		this.setValueStr(MsiaLhdnPcb2iiPotonganSemasa.TarikhPcb, aTarikhPcb);
	}

	public String getTarikhCp38() throws Exception {
		return(this.getValueStr(TarikhCp38));
	}

	public void setTarikhCp38(String aTarikhCp38) throws Exception {
		this.setValueStr(MsiaLhdnPcb2iiPotonganSemasa.TarikhCp38, aTarikhCp38);
	}

}
