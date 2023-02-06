package subway.global.error.exception;

public enum ErrorCode {

	//station
	MISMATCHED_THIS_DOWN_STATION(400, "해당 노선에 등록되어있는 하행 종점역과 같지 않습니다."),
	MISMATCHED_STATION(400, "해당 노선에 이미 등록되어있는 역입니다."),
	STATION_NOT_FINAL(400, "마지막 구간이 아닙니다"),
	STATION_LESS_THAN_TWO(400, "노선에 역이 두개 이하입니다."),
	STATION_NOT_EXISTS(400, "해당 역은 존재하지 않습니다."),

	//line
	LINE_NOT_EXISTS(400, "해당 노선은 존재하지 않습니다."),

	//section
	SECTION_NOT_EXISTS(400, "해당 구간은 존재하지 않습니다.");

	private int status;
	private String message;

	ErrorCode(int status, String message) {
		this.status = status;
		this.message = message;
	}

	public int getStatus() {
		return status;
	}

	public String getMessage() {
		return message;
	}
}