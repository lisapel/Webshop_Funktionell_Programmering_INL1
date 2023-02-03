package Reports;

import CustomerInfo.City;
import CustomerInfo.Customers;
import Orders.Order_Includes;
import Orders.Orders;
import Products.Brand;
import Products.Color;
import Products.Shoe;
import Products.Size;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class Repository {
    private final Properties p = new Properties();

    Repository() throws IOException {
        p.load(new FileInputStream("src/settings.properties"));
    }

    List<Order_Includes> getOrders() throws SQLException {
        List<Order_Includes> order_includes = new ArrayList<>();
        String query = "select customer.name as name, customer.address as address," +
                " customer.id as id, brand.brand, order_includes.orderID as orderID," +
                " color.color, size.size, shoe.price, city.city, shoe.id from customer " +
                "join orders on customer.id = orders.customerID " +
                "join order_includes on orders.id = order_includes.orderID " +
                "join shoe on order_includes.shoeID = shoe.id " +
                "join brand on shoe.brandID = brand.id " +
                "join color on shoe.colorID = color.id " +
                "join size on shoe.sizeID = size.id " +
                "join city on city.id = customer.cityID;";
        try (Connection c = DriverManager.getConnection(p.getProperty("connectionString"),
                p.getProperty("user"),
                p.getProperty("password"));

             Statement statement = c.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()){
                Order_Includes orderIncludes = new Order_Includes();

                //Hämta skodata
                Shoe shoe = new Shoe();
                shoe.setBrand(new Brand(resultSet.getString("brand.brand")));
                shoe.setId(resultSet.getInt("shoe.id"));
                shoe.setSize(new Size(resultSet.getInt("size.size")));
                shoe.setColor(new Color(resultSet.getString("color.color")));
                shoe.setPrice(resultSet.getInt("shoe.price"));
                orderIncludes.setShoeList(List.of(shoe));

                //Hämta orderdata
                Orders orders = new Orders();
                int id = resultSet.getInt("order_includes.orderID");
                orders.setId(id);
                orderIncludes.setOrdersList(List.of(orders));

                //Hämta kunddata
                Customers customers = new Customers();
                customers.setAddress(resultSet.getString("address"));
                customers.setName(resultSet.getString("name"));
                customers.setCity(new City(resultSet.getString("city.city")));
                orders.setCustomersList(List.of(customers));
                order_includes.add(orderIncludes);
            }

        }
        return order_includes;
    }
   }