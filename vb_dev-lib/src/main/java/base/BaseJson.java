package base;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BaseJson<T> {

	    private String returnMsg;
	    private String returnCode;
		private T data;

		public T getData() {
			return data;
		}

		@JsonProperty("data")
		public void setData(T data) {
		this.data = data;
	}

	public String getReturnMsg() {
		return returnMsg;
	}

	@JsonProperty("returnMsg")
	public void setReturnMsg(String returnMsg) {
		this.returnMsg = returnMsg;
	}

	public String getReturnCode() {
		return returnCode;
	}

	@JsonProperty("returnCode")
	public void setReturnCode(String returnCode) {
		this.returnCode = returnCode;
	}
}
