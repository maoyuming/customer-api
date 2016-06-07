package com.fangbaba.api.domain;

import java.io.Serializable;

import com.alibaba.fastjson.annotation.JSONField;

public class OmsResponse<T> implements Serializable {
	@JSONField(name = "_MSG_")
	private String _MSG_; // :" "OK, ",
	@JSONField(name = "_ERROR_")
	private String _ERROR_; // ":"",
	@JSONField(name = "_ERRORMESSAGE_")
	private String _ERRORMESSAGE_; // ":"",

	@JSONField(name = "_DATA_")
	private T _DATA_;

	public String get_MSG_() {
		return _MSG_;
	}

	public void set_MSG_(String _MSG_) {
		this._MSG_ = _MSG_;
	}

	public String get_ERROR_() {
		return _ERROR_;
	}

	public void set_ERROR_(String _ERROR_) {
		this._ERROR_ = _ERROR_;
	}

	public String get_ERRORMESSAGE_() {
		return _ERRORMESSAGE_;
	}

	public void set_ERRORMESSAGE_(String _ERRORMESSAGE_) {
		this._ERRORMESSAGE_ = _ERRORMESSAGE_;
	}

	public T get_DATA_() {
		return _DATA_;
	}

	public void set_DATA_(T _DATA_) {
		this._DATA_ = _DATA_;
	}
}
