package dextrous.kor.evv.korevv.model;

public class ScheduleModel {
    private String id,pic,name,status,location,start_time,end_time,lat,longi,call,unitRate,clientId,user_id,additional_note,startDate,endDate,personalCareTime,respiteCareTime;

    public ScheduleModel(String clientId,String unitRate,String id, String pic, String name,
                         String status, String location, String start_time, String end_time,
                         String lat, String longi, String call,String user_id,String additional_note,
                         String startDate,String endDate,
                         String personalCareTime,
                         String respiteCareTime) {
        this.unitRate=unitRate;
        this.id = id;
        this.pic = pic;
        this.name = name;
        this.status = status;
        this.location = location;
        this.start_time = start_time;
        this.end_time = end_time;
        this.lat = lat;
        this.longi = longi;
        this.call = call;
        this.clientId=clientId;
        this.user_id=user_id;
        this.additional_note=additional_note;
        this.startDate = startDate;
        this.endDate = endDate;
        this.personalCareTime = personalCareTime;
        this.respiteCareTime = respiteCareTime;
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

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public ScheduleModel() {

    }

    public String getAdditional_note() {
        return additional_note;
    }

    public void setAdditional_note(String additional_note) {
        this.additional_note = additional_note;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getUnitRate() {
        return unitRate;
    }

    public void setUnitRate(String unitRate) {
        this.unitRate = unitRate;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLongi() {
        return longi;
    }

    public void setLongi(String longi) {
        this.longi = longi;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }



    public String getCall() {
        return call;
    }

    public void setCall(String call) {
        this.call = call;
    }


}
