package com.fangbaba.api.face.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class Order implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 425639839220536686L;

	public Order(Long id) {
		this.id = id;
	}

	public Order() {
	}

	private Long id;

	private Long hotelid;

	private String hotelname;

	private String hotelpms;

	private Long channelid;
	
	private String channelname;
	
	private String salerid;

	private Integer paytype;

	private Date begintime;

	private Date endtime;
	@Deprecated
	private Integer status;
	private Integer qunarStatus;

	private BigDecimal totalprice;

	private String contacts;
	/**
	 * 入住人
	 */
	private String customerName;

	private String contactsphone;

	private Integer paystatus;

	private String orderno;

	private Integer ordertype;

	private Date createtime;

	private Date updatetime;

	private String note;


	private String grouporderid;

	private String teamname;

	private String pmsorderid;
	
	/**
	 * 最后一次更新时间
	 */
	private Long lastUpdateTime;
	
	private List<OrderDetail> details;
	
	private String extend;
	
	private String request;
	private List<String> remark;

	public String getPmsorderid() {
		return pmsorderid;
	}

	public void setPmsorderid(String pmsorderid) {
		this.pmsorderid = pmsorderid;
	}

	public String getTeamname() {
		return teamname;
	}

	public void setTeamname(String teamname) {
		this.teamname = teamname;
	}

	public String getGrouporderid() {
		return grouporderid;
	}

	public void setGrouporderid(String grouporderid) {
		this.grouporderid = grouporderid;
	}


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getHotelid() {
		return hotelid;
	}

	public void setHotelid(Long hotelid) {
		this.hotelid = hotelid;
	}

	public String getHotelname() {
		return hotelname;
	}

	public void setHotelname(String hotelname) {
		this.hotelname = hotelname == null ? null : hotelname.trim();
	}

	public String getHotelpms() {
		return hotelpms;
	}

	public void setHotelpms(String hotelpms) {
		this.hotelpms = hotelpms == null ? null : hotelpms.trim();
	}

	public Long getChannelid() {
		return channelid;
	}

	public void setChannelid(Long channelid) {
		this.channelid = channelid;
	}

	
	/**
	 * @return the channelname
	 */
	public String getChannelname() {
		return channelname;
	}

	/**
	 * @param channelname the channelname to set
	 */
	public void setChannelname(String channelname) {
		this.channelname = channelname;
	}

	public Integer getPaytype() {
		return paytype;
	}

	public void setPaytype(Integer paytype) {
		this.paytype = paytype;
	}

	public Date getBegintime() {
		return begintime;
	}

	public void setBegintime(Date begintime) {
		this.begintime = begintime;
	}

	public Date getEndtime() {
		return endtime;
	}

	public void setEndtime(Date endtime) {
		this.endtime = endtime;
	}

	@Deprecated
	public Integer getStatus() {
		return status;
	}
	@Deprecated
	public void setStatus(Integer status) {
		this.status = status;
	}

	public BigDecimal getTotalprice() {
		return totalprice;
	}

	public void setTotalprice(BigDecimal totalprice) {
		this.totalprice = totalprice;
	}

	public String getContacts() {
		return contacts;
	}

	public void setContacts(String contacts) {
		this.contacts = contacts == null ? null : contacts.trim();
	}

	public String getContactsphone() {
		return contactsphone;
	}

	public void setContactsphone(String contactsphone) {
		this.contactsphone = contactsphone == null ? null : contactsphone.trim();
	}

	public Integer getPaystatus() {
		return paystatus;
	}

	public void setPaystatus(Integer paystatus) {
		this.paystatus = paystatus;
	}

	public String getOrderno() {
		return orderno;
	}

	public void setOrderno(String orderno) {
		this.orderno = orderno == null ? null : orderno.trim();
	}

	public Integer getOrdertype() {
		return ordertype;
	}

	public void setOrdertype(Integer ordertype) {
		this.ordertype = ordertype;
	}

	public Date getCreatetime() {
		return createtime;
	}

	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}

	public Date getUpdatetime() {
		return updatetime;
	}

	public void setUpdatetime(Date updatetime) {
		this.updatetime = updatetime;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note == null ? null : note.trim();
	}

	/**
	 * @return the salerid
	 */
	public String getSalerid() {
		return salerid;
	}

	/**
	 * @param salerid the salerid to set
	 */
	public void setSalerid(String salerid) {
		this.salerid = salerid;
	}

	public List<OrderDetail> getDetails() {
		return details;
	}

	public void setDetails(List<OrderDetail> details) {
		this.details = details;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public Long getLastUpdateTime() {
		return lastUpdateTime;
	}

	public void setLastUpdateTime(Long lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}

	public String getExtend() {
		return extend;
	}

	public void setExtend(String extend) {
		this.extend = extend;
	}

	public Integer getQunarStatus() {
		return qunarStatus;
	}

	public void setQunarStatus(Integer qunarStatus) {
		this.qunarStatus = qunarStatus;
	}

	public String getRequest() {
		return request;
	}

	public void setRequest(String request) {
		this.request = request;
	}

	public List<String> getRemark() {
		return remark;
	}

	public void setRemark(List<String> remark) {
		this.remark = remark;
	}

	
	
	
}