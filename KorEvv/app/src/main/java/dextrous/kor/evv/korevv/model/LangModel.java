package dextrous.kor.evv.korevv.model;

public class LangModel {

    private String id, value, logo;

    public LangModel(String id, String value, String logo) {

        this.id = id;
        this.value = value;
        this.logo = logo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }
}
