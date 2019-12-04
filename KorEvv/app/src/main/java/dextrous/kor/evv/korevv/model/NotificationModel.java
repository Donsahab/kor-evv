package dextrous.kor.evv.korevv.model;

public class NotificationModel {
    String notifi,view_status,id,job_id,noti_rele,provider,seeker,isDoc,date,docId,docTypeId;

    public String getDocId() {
        return docId;
    }

    public void setDocId(String docId) {
        this.docId = docId;
    }

    public String getDocTypeId() {
        return docTypeId;
    }

    public void setDocTypeId(String docTypeId) {
        this.docTypeId = docTypeId;
    }

    public NotificationModel(String notifi, String view_status, String id, String job_id, String noti_rele,
                             String seeker, String provider, String isDoc, String date,String docId, String docTypeId) {
        this.notifi = notifi;
        this.view_status = view_status;
        this.id = id;
        this.job_id=job_id;
        this.noti_rele=noti_rele;
        this.provider=provider;
        this.seeker=seeker;
        this.isDoc = isDoc;
        this.date = date;
        this.docId = docId;
        this.docTypeId = docTypeId;
    }

    public String getIsDoc() {
        return isDoc;
    }

    public void setIsDoc(String isDoc) {
        this.isDoc = isDoc;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getSeeker() {
        return seeker;
    }

    public void setSeeker(String seeker) {
        this.seeker = seeker;
    }

    public String getNoti_rele() {
        return noti_rele;
    }

    public void setNoti_rele(String noti_rele) {
        this.noti_rele = noti_rele;
    }

    public String getJob_id() {
        return job_id;
    }

    public void setJob_id(String job_id) {
        this.job_id = job_id;
    }

    public String getNotifi() {
        return notifi;
    }

    public void setNotifi(String notifi) {
        this.notifi = notifi;
    }

    public String getView_status() {
        return view_status;
    }

    public void setView_status(String view_status) {
        this.view_status = view_status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
