package org.jboss.aeropad.vo;

import org.jboss.aerogear.android.RecordId;

public class PadDiff {

    private String md5;
    private String diff;

    @RecordId
    private String stubId;

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public String getDiff() {
        return diff;
    }

    public void setDiff(String diff) {
        this.diff = diff;
    }


    public String getStubId() {
        return stubId;
    }

    public void setStubId(String stubId) {
        this.stubId = stubId;
    }
}
