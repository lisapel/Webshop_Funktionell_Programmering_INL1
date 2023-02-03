package Orders;

import CustomerInfo.Customers;

import java.util.List;

public class Orders {
    int id;
    List<Customers> customersList;

    public List<Customers> getCustomersList() {
        return customersList;
    }

    public void setCustomersList(List<Customers> customersList) {
        this.customersList = customersList;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public Orders(){}
}
