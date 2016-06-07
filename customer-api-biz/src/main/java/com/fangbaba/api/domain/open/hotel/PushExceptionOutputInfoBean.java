package com.fangbaba.api.domain.open.hotel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PushExceptionOutputInfoBean implements Serializable {
	private static final long serialVersionUID = 5669839114908444185L;
	private String id;
	private String updatetime;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUpdatetime() {
		return updatetime;
	}

	public void setUpdatetime(String updatetime) {
		this.updatetime = updatetime;
	}

}
