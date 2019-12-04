package dextrous.kor.evv.korevv.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DocumentUploadModel {

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
}
