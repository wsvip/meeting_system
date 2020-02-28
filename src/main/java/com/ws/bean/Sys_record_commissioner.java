package com.ws.bean;

import java.io.Serializable;

public class Sys_record_commissioner implements Serializable {
    private static  final long serialVersionUID=1l;

    private String recordId;
    private String userId;

    public String getRecordId() {
        return recordId;
    }

    public void setRecordId(String recordId) {
        this.recordId = recordId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
