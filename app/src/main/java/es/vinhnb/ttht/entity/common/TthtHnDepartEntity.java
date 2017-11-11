package es.vinhnb.ttht.entity.common;

public class TthtHnDepartEntity {
    private int id;
    private String madvi;
    private String tenDvi;


    public TthtHnDepartEntity(int id, String madvi, String tenDvi) {
        this.id = id;
        this.madvi = madvi;
        this.tenDvi = tenDvi;
    }

    public int getId() {
        return id;
    }

    public String getMadvi() {
        return madvi;
    }

    public String getTenDvi() {
        return tenDvi;
    }

    @Override
    public String toString() {
        return madvi + " - " + tenDvi;
    }
}