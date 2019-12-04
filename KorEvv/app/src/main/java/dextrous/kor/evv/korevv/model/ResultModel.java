package dextrous.kor.evv.korevv.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResultModel {
    @Expose
    @SerializedName("doc_url")
    private String docUrl;
    @Expose
    @SerializedName("doc_expire_date")
    private String docExpireDate;
    @Expose
    @SerializedName("doc_issue_date")
    private String docIssueDate;
    @Expose
    @SerializedName("doc_title")
    private String docTitle;
    @Expose
    @SerializedName("doc_type")
    private String docType;
    @Expose
    @SerializedName("doc_type_id")
    private String docTypeId;
    @Expose
    @SerializedName("doc_id")
    private String docId;

    public String getDocUrl() {
        return docUrl;
    }

    public void setDocUrl(String docUrl) {
        this.docUrl = docUrl;
    }

    public String getDocExpireDate() {
        return docExpireDate;
    }

    public void setDocExpireDate(String docExpireDate) {
        this.docExpireDate = docExpireDate;
    }

    public String getDocIssueDate() {
        return docIssueDate;
    }

    public void setDocIssueDate(String docIssueDate) {
        this.docIssueDate = docIssueDate;
    }

    public String getDocTitle() {
        return docTitle;
    }

    public void setDocTitle(String docTitle) {
        this.docTitle = docTitle;
    }

    public String getDocType() {
        return docType;
    }

    public void setDocType(String docType) {
        this.docType = docType;
    }

    public String getDocTypeId() {
        return docTypeId;
    }

    public void setDocTypeId(String docTypeId) {
        this.docTypeId = docTypeId;
    }

    public String getDocId() {
        return docId;
    }

    public void setDocId(String docId) {
        this.docId = docId;
    }
}
