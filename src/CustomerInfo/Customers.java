package CustomerInfo;

public class Customers {
    protected int id;
    protected String name;
    protected String address;
    protected String password;
    protected City city;

    public Customers(){}
    public Customers(int id, String name, String address, String password, City city) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.password = password;
        this.city = city;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }
}


