package biz.shujutech.bznes;

public enum UnitTime {
	Year("y"), Month("M"), Week("w"), Day("d"), Hour("h"), Minute("m"), Second("s"), Microsecond("l"), Millisecond("S"), Nanosecond("n");

	public String Code;

	public String getCode() {
		return Code;
	}

	UnitTime(String aCode) {
		this.Code = aCode;
	}
}
