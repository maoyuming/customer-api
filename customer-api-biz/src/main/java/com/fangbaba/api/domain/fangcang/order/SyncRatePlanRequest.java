package com.fangbaba.api.domain.fangcang.order;

import com.fangbaba.api.domain.fangcang.Header;
import com.fangbaba.api.domain.fangcang.Request;
import com.google.common.collect.Lists;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * Created by nolan on 16/4/15.
 * description:
 */
@XmlRootElement
public class SyncRatePlanRequest extends Request {
    @XStreamAlias("SyncRatePlanRequest")
    private SyncRatePlanBodyRequest syncRatePlanBodyRequest;

    public static void main(String[] args) {
        SyncRatePlanRequest request = new SyncRatePlanRequest();
        Header header = new Header();
        header.setPartnerCode("123");
        request.setHeader(header);
        SyncRatePlanBodyRequest syncRatePlanBodyRequest = request.newSyncRatePlanBody();
        syncRatePlanBodyRequest.setOperateType("NEW");
        syncRatePlanBodyRequest.setSpHotelId("HT123");
        syncRatePlanBodyRequest.setSpRoomTypeId("sp2015072101");

        List<RatePlan> itemList = Lists.newArrayList();
        RatePlan ratePlan = request.newRatePlan();
        ratePlan.setSpRatePlanId("HT123");
        ratePlan.setSpRatePlanName("");
        ratePlan.setPayMethod("2");
        ratePlan.setCurrency("CNY");
        itemList.add(ratePlan);
        syncRatePlanBodyRequest.setRatePlanList(itemList);


        XStream xstream = new XStream();
        xstream.alias("request", SyncRatePlanRequest.class);
        xstream.autodetectAnnotations(true);
        xstream.ignoreUnknownElements();
        String xml = xstream.toXML(request);
        System.out.printf(xml);
    }

    public SyncRatePlanBodyRequest getSyncRatePlanBodyRequest() {
        return syncRatePlanBodyRequest;
    }

    public void setSyncRatePlanBodyRequest(SyncRatePlanBodyRequest syncRatePlanBodyRequest) {
        this.syncRatePlanBodyRequest = syncRatePlanBodyRequest;
    }

    public SyncRatePlanBodyRequest newSyncRatePlanBody() {
        this.syncRatePlanBodyRequest = new SyncRatePlanBodyRequest();
        return this.syncRatePlanBodyRequest;
    }

    public RatePlan newRatePlan() {
        return new RatePlan();
    }

    public static class SyncRatePlanBodyRequest {
        @XStreamAlias("SpHotelId")
        private String spHotelId;
        @XStreamAlias("OperateType")
        private String operateType;
        @XStreamAlias("SpRoomTypeId")
        private String spRoomTypeId;
        @XStreamAlias("RatePlanList")

        private List<RatePlan> ratePlanList;

        public String getSpHotelId() {
            return spHotelId;
        }

        public void setSpHotelId(String spHotelId) {
            this.spHotelId = spHotelId;
        }

        public String getOperateType() {
            return operateType;
        }

        public void setOperateType(String operateType) {
            this.operateType = operateType;
        }

        public String getSpRoomTypeId() {
            return spRoomTypeId;
        }

        public void setSpRoomTypeId(String spRoomTypeId) {
            this.spRoomTypeId = spRoomTypeId;
        }

        public List<RatePlan> getRatePlanList() {
            return ratePlanList;
        }

        public void setRatePlanList(List<RatePlan> ratePlanList) {
            this.ratePlanList = ratePlanList;
        }
    }

    @XStreamAlias("RatePlan")
    public static class RatePlan {
        @XStreamAlias("SpRatePlanId")
        private String spRatePlanId;
        @XStreamAlias("SpRatePlanName")
        private String spRatePlanName;
        @XStreamAlias("SpBedType")
        private String spBedType;
        @XStreamAlias("PayMethod")
        private String payMethod;
        @XStreamAlias("Currency")
        private String currency;

        public String getSpRatePlanId() {
            return spRatePlanId;
        }

        public void setSpRatePlanId(String spRatePlanId) {
            this.spRatePlanId = spRatePlanId;
        }

        public String getSpRatePlanName() {
            return spRatePlanName;
        }

        public void setSpRatePlanName(String spRatePlanName) {
            this.spRatePlanName = spRatePlanName;
        }

        public String getSpBedType() {
            return spBedType;
        }

        public void setSpBedType(String spBedType) {
            this.spBedType = spBedType;
        }

        public String getPayMethod() {
            return payMethod;
        }

        public void setPayMethod(String payMethod) {
            this.payMethod = payMethod;
        }

        public String getCurrency() {
            return currency;
        }

        public void setCurrency(String currency) {
            this.currency = currency;
        }
    }
}
