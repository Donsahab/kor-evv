package dextrous.kor.evv.korevv.model;

public class BillingModel {

    private String id,pic,name,location,call;

    public BillingModel(String id, String pic, String name, String location, String call) {
        this.id = id;
        this.pic = pic;
        this.name = name;
        this.location = location;
        this.call = call;
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
