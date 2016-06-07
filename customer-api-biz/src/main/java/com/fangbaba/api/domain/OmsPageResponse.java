package com.fangbaba.api.domain;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by nolan on 16/2/15.
 * description:
 */
public class OmsPageResponse<T> extends OmsResponse {
	@JSONField(name = "_PAGE_")
    private Page _PAGE_;

	@JSONField(name = "_OKCOUNT_")
    private int _OKCOUNT_;

    public OmsPageResponse(int shownum, int nowpage, int _OKCOUNT_) {
        super();
        this._PAGE_ = new Page(shownum, nowpage);
        this._OKCOUNT_ = _OKCOUNT_;
    }

    public Page get_PAGE_() {
        return _PAGE_;
    }

    public void set_PAGE_(Page _PAGE_) {
        this._PAGE_ = _PAGE_;
    }

    public int get_OKCOUNT_() {
        return _OKCOUNT_;
    }

    public void set_OKCOUNT_(int _OKCOUNT_) {
        this._OKCOUNT_ = _OKCOUNT_;
    }

    @JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE)
    class Page {
        @JsonProperty("SHOWNUM")
        private int SHOWNUM;
        @JsonProperty("NOWPAGE")
        private int NOWPAGE;

        public Page(int SHOWNUM, int NOWPAGE) {
            this.SHOWNUM = SHOWNUM;
            this.NOWPAGE = NOWPAGE;
        }

        public int getSHOWNUM() {
            return SHOWNUM;
        }

        public void setSHOWNUM(int SHOWNUM) {
            this.SHOWNUM = SHOWNUM;
        }

        public int getNOWPAGE() {
            return NOWPAGE;
        }

        public void setNOWPAGE(int NOWPAGE) {
            this.NOWPAGE = NOWPAGE;
        }
    }
}
