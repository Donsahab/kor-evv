package dextrous.kor.evv.korevv.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RespiteUnitCalModel {


    @Expose
    @SerializedName("status")
    private int status;
    @Expose
    @SerializedName("result")
    private ResultEntity result;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public ResultEntity getResult() {
        return result;
    }

    public void setResult(ResultEntity result) {
        this.result = result;
    }

    public static class ResultEntity {
        @Expose
        @SerializedName("extra_respite_time_date")
        private String extraRespiteTimeDate;
        @Expose
        @SerializedName("extra_respite_time")
        private String extraRespiteTime;

        public String getExtraRespiteTimeDate() {
            return extraRespiteTimeDate;
        }

        public void setExtraRespiteTimeDate(String extraRespiteTimeDate) {
            this.extraRespiteTimeDate = extraRespiteTimeDate;
        }

        public String getExtraRespiteTime() {
            return extraRespiteTime;
        }

        public void setExtraRespiteTime(String extraRespiteTime) {
            this.extraRespiteTime = extraRespiteTime;
        }
    }
}
