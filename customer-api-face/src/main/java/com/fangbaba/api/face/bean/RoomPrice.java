package com.fangbaba.api.face.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class RoomPrice implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6509180501935722921L;

	private Long id;

	private Date createtime;

	private Long orderdetailid;
	
	private Long orderid;

	private Date actiondate;

	private BigDecimal price;

	private Integer type;

	private Date updatetime;

	public Integer getType() {
		return type;
	}

	public Long getOrderdetailid() {
		return orderdetailid;
	}

	public void setOrderdetailid(Long orderdetailid) {
		this.orderdetailid = orderdetailid;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getCreatetime() {
		return createtime;
	}

	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}

	public Date getActiondate() {
		return actiondate;
	}

	public void setActiondate(Date actiondate) {
		this.actiondate = actiondate;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public Date getUpdatetime() {
		return updatetime;
	}

	public void setUpdatetime(Date updatetime) {
		this.updatetime = updatetime;
	}

	public Long getOrderid() {
		return orderid;
	}

	public void setOrderid(Long orderid) {
		this.orderid = orderid;
	}
	
	
}