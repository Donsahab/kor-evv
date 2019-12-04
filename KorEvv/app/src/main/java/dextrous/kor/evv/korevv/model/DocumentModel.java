package dextrous.kor.evv.korevv.model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DocumentModel {
    @Expose
    @SerializedName("status")
    private int status;
    @Expose
    @SerializedName("result")
    private List<ResultModel> result;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<ResultModel> getResult() {
        return result;
    }

    public void setResult(List<ResultModel> result) {
        this.result = result;
    }
}