package dextrous.kor.evv.korevv.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DocumentCategoryListModel {

    @Expose
    @SerializedName("status")
    private int status;
    @Expose
    @SerializedName("result")
    private List<ResultDocCategoryListModel> result;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<ResultDocCategoryListModel> getResult() {
        return result;
    }

    public void setResult(List<ResultDocCategoryListModel> result) {
        this.result = result;
    }
}
