package Orders;

import Products.Shoe;

import java.util.List;

public class Order_Includes {
    int id;
    List <Orders> ordersList;
    List<Shoe> shoeList;

    public Order_Includes(int id, List<Orders> ordersList, List<Shoe> shoeList) {
        this.id = id;
        this.ordersList = ordersList;
        this.shoeList = shoeList;
    }

    public Order_Includes(){}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Orders> getOrdersList() {
        return ordersList;
    }

    public void setOrdersList(List<Orders> ordersList) {
        this.ordersList = ordersList;
    }

    public List<Shoe> getShoeList() {
        return shoeList;
    }

    public void setShoeList(List<Shoe> shoeList) {
        this.shoeList = shoeList;
    }
}
