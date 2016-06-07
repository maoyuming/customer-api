package com.fangbaba.api.domain.fangcang.order;

import com.fangbaba.api.domain.fangcang.Request;
import com.thoughtworks.xstream.annotations.XStreamAlias;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by nolan on 16/4/15.
 * description:
 */
public class SyncRateInfoRequest extends Request {
    @XStreamAlias("SyncRateInfoRequest")
    private SyncRateInfoBodyRequest syncRateInfoBodyRequest;

    public SyncRateInfoBodyRequest getSyncRateInfoBodyRequest() {
        return syncRateInfoBodyRequest;
    }

    public void setSyncRateInfoBodyRequest(SyncRateInfoBodyRequest syncRateInfoBodyRequest) {
        this.syncRateInfoBodyRequest = syncRateInfoBodyRequest;
    }

    public SyncRateInfoBodyRequest newSyncRateInfoBodyRequest() {
        syncRateInfoBodyRequest = new SyncRateInfoBodyRequest();
        return this.syncRateInfoBodyRequest;
    }

    public SaleInfo newSaleInfo(){
        return new SaleInfo();
    }

    public static class SyncRateInfoBodyRequest {
        private String SpHotelId;
        private String SpRoomTypeId;
        private String SpRatePlanId;
        private List<SaleInfo> SaleInfoList;

        public String getSpHotelId() {
            return SpHotelId;
        }

        public void setSpHotelId(String spHotelId) {
            SpHotelId = spHotelId;
        }

        public String getSpRoomTypeId() {
            return SpRoomTypeId;
        }

        public void setSpRoomTypeId(String spRoomTypeId) {
            SpRoomTypeId = spRoomTypeId;
        }

        public String getSpRatePlanId() {
            return SpRatePlanId;
        }

        public void setSpRatePlanId(String spRatePlanId) {
            SpRatePlanId = spRatePlanId;
        }

        public List<SaleInfo> getSaleInfoList() {
            return SaleInfoList;
        }

        public void setSaleInfoList(List<SaleInfo> saleInfoList) {
            SaleInfoList = saleInfoList;
        }
    }

    @XStreamAlias("SaleInfo")
    public static class SaleInfo {
        private String SaleDate;
        private BigDecimal SalePrice;
        private String BreakfastType;
        private Integer BreakfastNum;
        private String FreeSale;
        private String RoomState;
        private String Overdraft;
        private String OverDraftNum;
        private Integer QuotaNum;
        private Integer MinAdvHours;
        private Integer MinDays;
        private String MaxDays;
        private Integer MinRooms;
        private Integer MinAdvCancelHours;
        private String CancelDescription;

        public String getSaleDate() {
            return SaleDate;
        }

        public void setSaleDate(String saleDate) {
            SaleDate = saleDate;
        }

        public BigDecimal getSalePrice() {
            return SalePrice;
        }

        public void setSalePrice(BigDecimal salePrice) {
            SalePrice = salePrice;
        }

        public String getBreakfastType() {
            return BreakfastType;
        }

        public void setBreakfastType(String breakfastType) {
            BreakfastType = breakfastType;
        }

        public Integer getBreakfastNum() {
            return BreakfastNum;
        }

        public void setBreakfastNum(Integer breakfastNum) {
            BreakfastNum = breakfastNum;
        }

        public String getFreeSale() {
            return FreeSale;
        }

        public void setFreeSale(String freeSale) {
            FreeSale = freeSale;
        }

        public String getRoomState() {
            return RoomState;
        }

        public void setRoomState(String roomState) {
            RoomState = roomState;
        }

        public String getOverdraft() {
            return Overdraft;
        }

        public void setOverdraft(String overdraft) {
            Overdraft = overdraft;
        }

        public String getOverDraftNum() {
            return OverDraftNum;
        }

        public void setOverDraftNum(String overDraftNum) {
            OverDraftNum = overDraftNum;
        }

        public Integer getQuotaNum() {
            return QuotaNum;
        }

        public void setQuotaNum(Integer quotaNum) {
            QuotaNum = quotaNum;
        }

        public Integer getMinAdvHours() {
            return MinAdvHours;
        }

        public void setMinAdvHours(Integer minAdvHours) {
            MinAdvHours = minAdvHours;
        }

        public Integer getMinDays() {
            return MinDays;
        }

        public void setMinDays(Integer minDays) {
            MinDays = minDays;
        }

        public String getMaxDays() {
            return MaxDays;
        }

        public void setMaxDays(String maxDays) {
            MaxDays = maxDays;
        }

        public Integer getMinRooms() {
            return MinRooms;
        }

        public void setMinRooms(Integer minRooms) {
            MinRooms = minRooms;
        }

        public Integer getMinAdvCancelHours() {
            return MinAdvCancelHours;
        }

        public void setMinAdvCancelHours(Integer minAdvCancelHours) {
            MinAdvCancelHours = minAdvCancelHours;
        }

        public String getCancelDescription() {
            return CancelDescription;
        }

        public void setCancelDescription(String cancelDescription) {
            CancelDescription = cancelDescription;
        }
    }
}
