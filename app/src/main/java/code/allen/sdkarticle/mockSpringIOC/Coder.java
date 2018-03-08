package code.allen.sdkarticle.mockSpringIOC;

/**
 * Created by allenni on 2018/3/8.
 */

public class Coder {
    private String name;
    private String address;

    public Coder(String name, String address) {
        this.name = name;
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
