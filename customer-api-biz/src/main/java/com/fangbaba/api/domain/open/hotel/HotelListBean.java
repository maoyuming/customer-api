/**
 * 2016年3月25日下午3:31:17
 * zhaochuanbin
 */
package com.fangbaba.api.domain.open.hotel;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author zhaochuanbin
 *
 */
public class HotelListBean  implements Serializable{
	private Long id;
    private String hotelname;
    private String detailaddr;
    private Integer hoteltype;
    private BigDecimal longitude;
    private BigDecimal latitude;
    private String retentiontime;
    private String defaultleavetime;
    private Integer discode;
    private Integer citycode;
    private Integer provcode;
    private String introduction;
    private String provincename;// 省名称
    private String cityname;// 市
    private String districtname;// 县
    private String hotelpic;// 图片
    private String hotelpics;// 图片数组
    
    private String hotelphone;
    private String opentime;

	private String repairtime;
	
	private Integer roomnum;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getHotelname() {
		return hotelname;
	}

	public void setHotelname(String hotelname) {
		this.hotelname = hotelname;
	}

	public String getDetailaddr() {
		return detailaddr;
	}

	public void setDetailaddr(String detailaddr) {
		this.detailaddr = detailaddr;
	}

	public Integer getHoteltype() {
		return hoteltype;
	}

	public void setHoteltype(Integer hoteltype) {
		this.hoteltype = hoteltype;
	}

	public BigDecimal getLongitude() {
		return longitude;
	}

	public void setLongitude(BigDecimal longitude) {
		this.longitude = longitude;
	}

	public BigDecimal getLatitude() {
		return latitude;
	}

	public void setLatitude(BigDecimal latitude) {
		this.latitude = latitude;
	}

	public String getRetentiontime() {
		return retentiontime;
	}

	public void setRetentiontime(String retentiontime) {
		this.retentiontime = retentiontime;
	}

	public String getDefaultleavetime() {
		return defaultleavetime;
	}

	public void setDefaultleavetime(String defaultleavetime) {
		this.defaultleavetime = defaultleavetime;
	}

	public Integer getDiscode() {
		return discode;
	}

	public void setDiscode(Integer discode) {
		this.discode = discode;
	}

	public Integer getCitycode() {
		return citycode;
	}

	public void setCitycode(Integer citycode) {
		this.citycode = citycode;
	}

	public Integer getProvcode() {
		return provcode;
	}

	public void setProvcode(Integer provcode) {
		this.provcode = provcode;
	}

	public String getIntroduction() {
		return introduction;
	}

	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}

	public String getProvincename() {
		return provincename;
	}

	public void setProvincename(String provincename) {
		this.provincename = provincename;
	}

	public String getCityname() {
		return cityname;
	}

	public void setCityname(String cityname) {
		this.cityname = cityname;
	}

	public String getDistrictname() {
		return districtname;
	}

	public void setDistrictname(String districtname) {
		this.districtname = districtname;
	}

	public String getHotelpic() {
		return hotelpic;
	}

	public void setHotelpic(String hotelpic) {
		this.hotelpic = hotelpic;
	}

	public String getHotelpics() {
		return hotelpics;
	}

	public void setHotelpics(String hotelpics) {
		this.hotelpics = hotelpics;
	}

	public String getHotelphone() {
		return hotelphone;
	}

	public void setHotelphone(String hotelphone) {
		this.hotelphone = hotelphone;
	}

	public String getOpentime() {
		return opentime;
	}

	public void setOpentime(String opentime) {
		this.opentime = opentime;
	}

	public String getRepairtime() {
		return repairtime;
	}

	public void setRepairtime(String repairtime) {
		this.repairtime = repairtime;
	}

	public Integer getRoomnum() {
		return roomnum;
	}

	public void setRoomnum(Integer roomnum) {
		this.roomnum = roomnum;
	}
	
	
}
