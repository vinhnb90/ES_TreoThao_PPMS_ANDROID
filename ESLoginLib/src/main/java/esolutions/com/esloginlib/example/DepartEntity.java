package esolutions.com.esloginlib.example;

/**
 * Created by VinhNB on 10/26/2017.
 */

public class DepartEntity {
    private int id;
    private String madvi;
    private String tenDvi;


    public DepartEntity(int id, String madvi, String tenDvi) {
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
