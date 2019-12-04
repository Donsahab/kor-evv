package dextrous.kor.evv.korevv.model;

public class ServiceModel {
    private String id,name,status;

    public ServiceModel(String id, String name, String status) {
        this.id = id;
        this.name = name;
        this.status = status;
    }

    public ServiceModel() {

    }



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
}
