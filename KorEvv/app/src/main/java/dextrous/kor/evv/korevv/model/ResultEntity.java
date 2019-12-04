package dextrous.kor.evv.korevv.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResultEntity {
    @Expose
    @SerializedName("message")
    private String message;
    @Expose
    @SerializedName("insert_id")
    private int insertId;
    @Expose
    @SerializedName("status")
    private boolean status;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getInsertId() {
        return insertId;
    }

    public void setInsertId(int insertId) {
        this.insertId = insertId;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
