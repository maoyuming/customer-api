package com.fangbaba.api.domain;

public class OrderExtend {
	/**
	 * 渠道id
	 */
    private Integer channelId;
    /**
     * 结算方式
     */
    private Integer scType;
	public Integer getChannelId() {
		return channelId;
	}
	public void setChannelId(Integer channelId) {
		this.channelId = channelId;
	}
	public Integer getScType() {
		return scType;
	}
	public void setScType(Integer scType) {
		this.scType = scType;
	}
    
    
}
