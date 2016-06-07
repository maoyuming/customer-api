package com.fangbaba.api.domain.fangcang.order;

import com.fangbaba.api.domain.fangcang.Request;
import com.thoughtworks.xstream.annotations.XStreamAlias;

import java.util.List;

/**
 * Created by nolan on 16/4/15.
 * description:
 */
public class DeleteRatePlanRequest extends Request {

    @XStreamAlias("DeleteRatePlanRequest")
    private DeleteRatePlanBodyRequest deleteRatePlanBodyRequest;

    public DeleteRatePlanBodyRequest getDeleteRatePlanBodyRequest() {
        return deleteRatePlanBodyRequest;
    }

    public void setDeleteRatePlanBodyRequest(DeleteRatePlanBodyRequest deleteRatePlanBodyRequest) {
        this.deleteRatePlanBodyRequest = deleteRatePlanBodyRequest;
    }

    public DeleteRatePlanBodyRequest newDeleteRatePlanBodyRequest() {
        this.deleteRatePlanBodyRequest = new DeleteRatePlanBodyRequest();
        return this.deleteRatePlanBodyRequest;
    }

    public RatePlanInfo newRatePlanInfo(){
        return new RatePlanInfo();
    }

    public static class DeleteRatePlanBodyRequest {
        @XStreamAlias("SpHotelId")
        private String spHotelId;
        @XStreamAlias("SpRoomTypeId")
        private String spRoomTypeId;
        @XStreamAlias("RatePlanInfoList")
        private List<RatePlanInfo> ratePlanInfoList;

        public String getSpHotelId() {
            return spHotelId;
        }

        public void setSpHotelId(String spHotelId) {
            this.spHotelId = spHotelId;
        }

        public String getSpRoomTypeId() {
            return spRoomTypeId;
        }

        public void setSpRoomTypeId(String spRoomTypeId) {
            this.spRoomTypeId = spRoomTypeId;
        }

        public List<RatePlanInfo> getRatePlanInfoList() {
            return ratePlanInfoList;
        }

        public void setRatePlanInfoList(List<RatePlanInfo> ratePlanInfoList) {
            this.ratePlanInfoList = ratePlanInfoList;
        }
    }

    @XStreamAlias("RatePlanInfo")
    public static class RatePlanInfo {
        @XStreamAlias("SpRatePlanId")
        private String spRatePlanId;

        public String getSpRatePlanId() {
            return spRatePlanId;
        }

        public void setSpRatePlanId(String spRatePlanId) {
            this.spRatePlanId = spRatePlanId;
        }
    }
}
