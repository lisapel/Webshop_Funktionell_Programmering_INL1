
import CustomerInfo.Customers;
import Products.*;

import javax.swing.*;
import java.awt.event.ActionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class Shop extends JFrame implements ActionListener {
    final Repository repository = new Repository();
    final JFrame frame;
    final JPanel panel = new JPanel();
    final JTextField customerName = new JTextField(10);
    final JPasswordField customerPassword = new JPasswordField(10);
    int id;

    public Shop() throws IOException {

        frame = new JFrame("Login");
        final JButton logIn = new JButton("Log in");
        final JButton cancel = new JButton("Cancel");
        final JLabel cuNa = new JLabel("Customer name: ");
        final JLabel cuPa = new JLabel("Password: ");

        panel.setLayout(new GridLayout(3, 3));
        panel.add(cuNa);
        panel.add(customerName);
        panel.add(cuPa);
        panel.add(customerPassword);
        panel.add(logIn);
        panel.add(cancel);
        cancel.addActionListener(e -> System.exit(0));
        logIn.addActionListener(e -> {
            getCustomerData();
        });
        frame.add(panel);
        frame.setVisible(true);
        frame.pack();
        frame.setDefaultCloseOperation(frame.EXIT_ON_CLOSE);
    }

    void getCustomerData() {
        final String name = customerName.getText();
        final String password = new String(customerPassword.getPassword());
        try {
            loginValidation(name, password);
        } catch (IOException | SQLException ex) {
            message(ex.getMessage());
            ex.printStackTrace();
        }
    }

    void loginValidation(String name, String password) throws IOException, SQLException {
        final List<Customers> customerList = repository.getCustomers();
        final List<Shoe> shoeList;
        Customers customers = customerList.stream().filter(e -> e.getName().equals(name) && e.getPassword().equals(password)).findFirst().orElse(null);
        if (customers != null) {
            id = customers.getId();
            shoeList = repository.getProducts().stream().filter(s -> s.getQuantity() > 0).toList();
            createTable(shoeList);
        } else {
            message("Log in failed");
        }
    }

    void createTable(List<Shoe> shoeList) throws SQLException, IOException {
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("PRICE");
        model.addColumn("QUANTITY");
        model.addColumn("BRAND");
        model.addColumn("COLOR");
        model.addColumn("SIZE");

        Object[] rows = new Object[5];
        for (Shoe shoe : shoeList) {
            rows[0] = shoe.getPrice();
            rows[1] = shoe.getQuantity();
            rows[2] = shoe.getBrand().getBrand();
            rows[3] = shoe.getColor().getColor();
            rows[4] = shoe.getSize().getSize();
            model.addRow(rows);
        }

        JTable jTable = new JTable(model);
        jTable.setCellSelectionEnabled(false);
        JScrollPane jscrollPane = new JScrollPane(jTable);
        productPage(jscrollPane);
    }

    void productPage(JScrollPane m) throws IOException, SQLException {
        final JLabel orderNum = new JLabel("Order number: ");
        final JLabel orderSize = new JLabel("Size: ");
        final JLabel orderBrand = new JLabel("Brand: ");
        final JLabel orderColor = new JLabel("Color: ");
        final JTextField size = new JTextField(10);
        final JTextField brand = new JTextField(10);
        final JTextField number = new JTextField(10);
        final JTextField color = new JTextField(10);
        frame.remove(panel);
        frame.setTitle("Product Page");
        frame.add(m, BorderLayout.NORTH);
        final JPanel panel1 = new JPanel();
        final JButton order = new JButton("ORDER");

        panel1.add(orderNum);
        panel1.add(number);
        panel1.add(orderSize);
        panel1.add(size);
        panel1.add(orderColor);
        panel1.add(color);
        panel1.add(orderBrand);
        panel1.add(brand);
        panel1.add(order);
        frame.add(panel1, BorderLayout.CENTER);
        frame.pack();

        order.addActionListener(e -> {
            try {
                addOrder(size.getText(), color.getText().trim(),
                        brand.getText().trim(), number.getText());
            } catch (SQLException ex) {
                message(ex.getMessage());
                ex.printStackTrace();
            }
        });

    }

    void addOrder(String s, String c, String b, String order) throws SQLException {
        try {
            final List<Shoe> shoeList = repository.getProducts().stream().filter(e -> e.getQuantity() > 0).toList();
            final Shoe shoe = shoeList.stream().filter(e -> e.getSize().getSize() == Integer.parseInt(s) && e.getColor().getColor().equals(c)
                    && e.getBrand().getBrand().equals(b)).findFirst().orElse(null);
            if (shoe != null) {
                final int shoeId = shoe.getId();
                if (order.isEmpty()) {
                    final int num = 0;
                    repository.addToCart(num, shoeId, id);
                    message("Order confirmation. Thank you for your order");
                } else {
                    repository.addToCart(Integer.parseInt(order), shoeId, id);
                    message("Order confirmation. Thank you for your order.");
                }
            } else {
                message("An error occured. The order did not go through. Please check your order and then try again");
            }
        } catch (Exception e) {
            message("Size must be integers");
        }
    }

    void message(String message) {
        JOptionPane.showMessageDialog(null, message);
    }

    public static void main(String[] args) throws IOException {
        new Shop();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
    }
}

