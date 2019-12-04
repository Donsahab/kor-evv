package dextrous.kor.evv.korevv.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResultDocCategoryListModel {
    @Expose
    @SerializedName("doc_type")
    private String docType;
    @Expose
    @SerializedName("doc_type_id")
    private int docTypeId;

    public String getDocType() {
        return docType;
    }

    public void setDocType(String docType) {
        this.docType = docType;
    }

    public int getDocTypeId() {
        return docTypeId;
    }

    public void setDocTypeId(int docTypeId) {
        this.docTypeId = docTypeId;
    }

    @Override
    public String toString() {
        return docType;
    }
}
