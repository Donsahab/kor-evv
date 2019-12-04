package dextrous.kor.evv.korevv.model;

public class CheckOutModel {
    private String scheduleId,userId,check_out_latitude,check_out_longitude,checkout_date,checkout_time,question1_yn,question2_yn,question3_yn,
            question4_yn,question1_note,question2_note,question3_note,question4_note,client_sign,client_sign_name,caregiver_sign,additional_comments,
            checkOutReason, personalCareTime,
     respiteCareTime,
   personalCareActivities,
     respiteCareActivities,
    totalUnit
    ;

    public CheckOutModel(String scheduleId,
                         String userId,
                         String check_out_latitude,
                         String check_out_longitude,
                         String checkout_date,
                         String checkout_time,
                         String question1_yn,
                         String question2_yn,
                         String question3_yn,
                         String question4_yn,
                         String question1_note,
                         String question2_note,
                         String question3_note,
                         String question4_note,
                         String client_sign,
                         String client_sign_name,
                         String caregiver_sign,
                         String additional_comments,
                         String checkOutReason,
                         String personalCareTime,
                         String respiteCareTime,
                         String personalCareActivities,
                         String respiteCareActivities,
                         String totalUnit) {
        this.scheduleId = scheduleId;
        this.userId = userId;
        this.check_out_latitude = check_out_latitude;
        this.check_out_longitude = check_out_longitude;
        this.checkout_date = checkout_date;
        this.checkout_time = checkout_time;
        this.question1_yn = question1_yn;
        this.question2_yn = question2_yn;
        this.question3_yn = question3_yn;
        this.question4_yn = question4_yn;
        this.question1_note = question1_note;
        this.question2_note = question2_note;
        this.question3_note = question3_note;
        this.question4_note = question4_note;
        this.client_sign = client_sign;
        this.client_sign_name = client_sign_name;
        this.caregiver_sign = caregiver_sign;
        this.additional_comments = additional_comments;
        this.checkOutReason = checkOutReason;
        this.personalCareTime = personalCareTime;
        this.respiteCareTime = respiteCareTime;
        this.personalCareActivities = personalCareActivities;
        this.respiteCareActivities = respiteCareActivities;
        this.totalUnit = totalUnit;

    }

    public String getPersonalCareTime() {
        return personalCareTime;
    }

    public void setPersonalCareTime(String personalCareTime) {
        this.personalCareTime = personalCareTime;
    }

    public String getRespiteCareTime() {
        return respiteCareTime;
    }

    public void setRespiteCareTime(String respiteCareTime) {
        this.respiteCareTime = respiteCareTime;
    }

    public String getPersonalCareActivities() {
        return personalCareActivities;
    }

    public void setPersonalCareActivities(String personalCareActivities) {
        this.personalCareActivities = personalCareActivities;
    }

    public String getRespiteCareActivities() {
        return respiteCareActivities;
    }

    public void setRespiteCareActivities(String respiteCareActivities) {
        this.respiteCareActivities = respiteCareActivities;
    }

    public String getTotalUnit() {
        return totalUnit;
    }

    public void setTotalUnit(String totalUnit) {
        this.totalUnit = totalUnit;
    }

    public String getCheckOutReason() {
        return checkOutReason;
    }

    public void setCheckOutReason(String checkOutReason) {
        this.checkOutReason = checkOutReason;
    }

    public CheckOutModel() {

    }

    public String getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(String scheduleId) {
        this.scheduleId = scheduleId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCheck_out_latitude() {
        return check_out_latitude;
    }

    public void setCheck_out_latitude(String check_out_latitude) {
        this.check_out_latitude = check_out_latitude;
    }

    public String getCheck_out_longitude() {
        return check_out_longitude;
    }

    public void setCheck_out_longitude(String check_out_longitude) {
        this.check_out_longitude = check_out_longitude;
    }

    public String getCheckout_date() {
        return checkout_date;
    }

    public void setCheckout_date(String checkout_date) {
        this.checkout_date = checkout_date;
    }

    public String getCheckout_time() {
        return checkout_time;
    }

    public void setCheckout_time(String checkout_time) {
        this.checkout_time = checkout_time;
    }

    public String getQuestion1_yn() {
        return question1_yn;
    }

    public void setQuestion1_yn(String question1_yn) {
        this.question1_yn = question1_yn;
    }

    public String getQuestion2_yn() {
        return question2_yn;
    }

    public void setQuestion2_yn(String question2_yn) {
        this.question2_yn = question2_yn;
    }

    public String getQuestion3_yn() {
        return question3_yn;
    }

    public void setQuestion3_yn(String question3_yn) {
        this.question3_yn = question3_yn;
    }

    public String getQuestion4_yn() {
        return question4_yn;
    }

    public void setQuestion4_yn(String question4_yn) {
        this.question4_yn = question4_yn;
    }

    public String getQuestion1_note() {
        return question1_note;
    }

    public void setQuestion1_note(String question1_note) {
        this.question1_note = question1_note;
    }

    public String getQuestion2_note() {
        return question2_note;
    }

    public void setQuestion2_note(String question2_note) {
        this.question2_note = question2_note;
    }

    public String getQuestion3_note() {
        return question3_note;
    }

    public void setQuestion3_note(String question3_note) {
        this.question3_note = question3_note;
    }

    public String getQuestion4_note() {
        return question4_note;
    }

    public void setQuestion4_note(String question4_note) {
        this.question4_note = question4_note;
    }

    public String getClient_sign() {
        return client_sign;
    }

    public void setClient_sign(String client_sign) {
        this.client_sign = client_sign;
    }

    public String getClient_sign_name() {
        return client_sign_name;
    }

    public void setClient_sign_name(String client_sign_name) {
        this.client_sign_name = client_sign_name;
    }

    public String getCaregiver_sign() {
        return caregiver_sign;
    }

    public void setCaregiver_sign(String caregiver_sign) {
        this.caregiver_sign = caregiver_sign;
    }

    public String getAdditional_comments() {
        return additional_comments;
    }

    public void setAdditional_comments(String additional_comments) {
        this.additional_comments = additional_comments;
    }
}
