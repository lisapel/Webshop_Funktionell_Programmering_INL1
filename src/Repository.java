

import CustomerInfo.City;
import CustomerInfo.Customers;
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

    List<Customers> getCustomers() {
        try (Connection c = DriverManager.getConnection(p.getProperty("connectionString"),
                p.getProperty("user"),
                p.getProperty("password"));

             Statement statement = c.createStatement();
             ResultSet resultSet = statement.executeQuery("select customer.id, customer.name, customer.address,customer.password,city.city from customer " +
                     " join city on customer.cityID = city.id  " +
                     " order by customer.name ")) {

            List<Customers> customers = new ArrayList<>();

            while (resultSet.next()) {
                Customers customer = new Customers();
                customer.setId(resultSet.getInt("customer.id"));
                customer.setName(resultSet.getString("customer.name"));
                customer.setAddress(resultSet.getString("customer.address"));
                customer.setPassword(resultSet.getString("customer.password"));
                customer.setCity(new City(resultSet.getString("city.city")));
                customers.add(customer);
            }
            return customers;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
    List<Shoe> getProducts() throws SQLException {
        List<Shoe>shoeList=new ArrayList<>();
        String query = "select shoe.id, shoe.price, shoe.quantity, brand.brand, color.color, size.size from shoe" +
                " join brand on shoe.brandID = brand.id " +
                " join color on shoe.colorID = color.id" +
                " join size on shoe.sizeID = size.id "+
                "order by size.size";
        try (Connection c = DriverManager.getConnection(p.getProperty("connectionString"),
                p.getProperty("user"),
                p.getProperty("password"));

             Statement statement = c.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()){
                Shoe shoe = new Shoe();
                shoe.setId(resultSet.getInt("shoe.id"));
                shoe.setQuantity(resultSet.getInt("shoe.quantity"));
                shoe.setPrice(resultSet.getInt("shoe.price"));
                shoe.setBrand(new Brand(resultSet.getString("brand.brand")));
                shoe.setColor(new Color(resultSet.getString("color.color")));
                shoe.setSize(new Size(resultSet.getInt("size.size")));
                shoeList.add(shoe);
            }
        }return shoeList;

    }
    void addToCart(int orderId, int productId, int customerId) throws SQLException {
        try (Connection c = DriverManager.getConnection(p.getProperty("connectionString"),
                p.getProperty("user"),
                p.getProperty("password"));
             CallableStatement callableStatement = c.prepareCall("CALL addToCart(?,?,?)")) {
            callableStatement.setInt(1, orderId);
            callableStatement.setInt(2, productId);
            callableStatement.setInt(3, customerId);
            callableStatement.executeQuery();
        }
    }




}
