package com.jinhs.fetch.bo;

public class CacheNoteBo {
	private String identity_key;
	private NoteBo noteBo;
	private int sequenceId;
	public String getIdentity_key() {
		return identity_key;
	}
	public void setIdentity_key(String identity_key) {
		this.identity_key = identity_key;
	}
	public NoteBo getNoteBo() {
		return noteBo;
	}
	public void setNoteBo(NoteBo noteBo) {
		this.noteBo = noteBo;
	}
	public int getSequenceId() {
		return sequenceId;
	}
	public void setSequenceId(int sequenceId) {
		this.sequenceId = sequenceId;
	}
}
