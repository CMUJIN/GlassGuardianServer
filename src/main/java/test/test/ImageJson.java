package test;

import java.util.List;

public class ImageJson {
	private String objectId;
	private List<ByteData> data;
	public String getObjectId() {
		return objectId;
	}
	public void setObjectId(String objectId) {
		this.objectId = objectId;
	}
	public List<ByteData> getData() {
		return data;
	}
	public void setData(List<ByteData> data) {
		this.data = data;
	}
}
