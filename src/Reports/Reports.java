package Reports;
import CustomerInfo.City;
import CustomerInfo.Customers;
import Orders.Order_Includes;
import Products.Shoe;
import java.io.IOException;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Reports {
    final Repository repository = new Repository();
    final List<Order_Includes> order_includes = repository.getOrders();
    final SearchCustomerByInt searchBySize = (s, b) -> s.getSize().getSize() == b;
    final SearchCustomerByString searchByBrand = (s, b) -> s.getBrand().getBrand().equalsIgnoreCase(b);
    final SearchCustomerByString searchByColor = (s, c) -> s.getColor().getColor().equalsIgnoreCase(c);

    Reports() throws IOException, SQLException {
        while (true) {
            System.out.println("\nMENU");
            System.out.println("---------------------------" +
                    "\n(1) ORDERS BY SHOE SIZE/BRAND/COLOR " +
                    "\n(2) NUMBER OF ORDERS PER CUSTOMER " +
                    "\n(3) TOTAL ORDER AMOUNT PER CUSTOMER" +
                    "\n(4) TOTAL ORDER AMOUNT PER CITY" +
                    "\n(5) MOST POPULAR PRODUCTS" +
                    "\n(6) QUIT"
            );
            System.out.println("Select number: 1-5");
            menu();
        }
    }
    void menu()  {
        final Scanner sc = new Scanner(System.in);
        final int number = sc.nextInt();

        switch (number) {

            case 1 -> {

                System.out.println("Select to search by size/brand/color");
                final Scanner scanner = new Scanner(System.in);
                final String choice = scanner.nextLine().trim().toLowerCase();
                if (choice.equalsIgnoreCase("size")) {
                    System.out.print("\nSize: ");
                    final int size = scanner.nextInt();
                    searchCustomersBySize(size, searchBySize);
                } else if (choice.equalsIgnoreCase("brand")) {
                    System.out.print("\nBrand: ");
                    final String brand = scanner.nextLine();
                    searchCustomersByBrandOrColor(brand, searchByBrand);
                } else {
                    System.out.println("\nColor: ");
                    final String color = scanner.nextLine();
                    searchCustomersByBrandOrColor(color, searchByColor);
                }
            }
            case 2 -> getNumberOfOrdersPerCustomer();
            case 3 -> getTotalOrderAmountPerCostumer();
            case 4 -> getTotalOrderAmountPerCity();
            case 5 -> printTableOfMapWithObjects();
            case 6 -> System.exit(0);
        }
    }

    void searchCustomersBySize(int size, SearchCustomerByInt shoeSearch) {
        final List<Shoe> shoeList = order_includes.stream().flatMap(m -> m.getShoeList().stream().filter(s -> shoeSearch.searchInt(s, size))).toList();
         printCustomerList(getCustomers(shoeList));
    }

    void searchCustomersByBrandOrColor(String search, SearchCustomerByString shoeSearch) {
        final List<Shoe> shoeList = order_includes.stream().flatMap(m -> m.getShoeList().stream().filter(s -> shoeSearch.searchString(s, search))).toList();
       printCustomerList(getCustomers(shoeList));
    }

    Set<Customers>  getCustomers(List<Shoe> shoeList) {
        return order_includes.stream()
                .filter(m -> m.getShoeList().stream().anyMatch(shoeList::contains))
                .flatMap(c -> c.getOrdersList().stream()).flatMap(g -> g.getCustomersList().stream()).collect(Collectors.toSet());
    }

    void getNumberOfOrdersPerCustomer() {
        final List<Customers> customersList = order_includes.stream()
                .flatMap(e -> e.getOrdersList().stream())
                .flatMap(c -> c.getCustomersList().stream()).toList();

        final Map<String, Long> countCustomer = customersList.stream()
                .collect(Collectors.groupingBy(Customers::getName, Collectors.counting()));

        printMapOfStringLongToTable(countCustomer);
    }

    void getTotalOrderAmountPerCostumer() {
        final List<Customers> customersList = order_includes.stream()
                .flatMap(e -> e.getOrdersList().stream())
                .flatMap(c -> c.getCustomersList().stream()).toList();
        final List<Integer> prices = order_includes.stream().flatMap(m -> m.getShoeList().stream()).map(Shoe::getPrice).toList();

        final Map<String, Integer> totalAmountPerCustomer = IntStream.range(0, customersList.size())
                .boxed()
                .collect(Collectors.toMap(
                        i -> customersList.get(i).getName(),
                        prices::get,
                        Integer::sum));

        printMapOfStringIntegerToTable(totalAmountPerCustomer,"Customer");
    }

    void getTotalOrderAmountPerCity() {
        final List<City> cities = order_includes.stream().flatMap(m -> m.getOrdersList().stream()).flatMap(o -> o.getCustomersList().stream())
                .map(Customers::getCity).toList();
        final List<Integer> shoePrices = order_includes.stream().flatMap(m -> m.getShoeList().stream().map(Shoe::getPrice)).toList();
        final Map<String, Integer> totalAmountPerCity = IntStream.range(0, cities.size()).boxed()
                .collect(Collectors.toMap(i -> cities.get(i).getCity(), shoePrices::get, Integer::sum
                ));
        printMapOfStringIntegerToTable(totalAmountPerCity,"City");
    }

    Map<Object,Long> getMostPopularProduct() {
        return order_includes.stream().flatMap(m -> m.getShoeList().stream()).collect(Collectors.groupingBy(
                shoe -> shoe.getBrand().getBrand() + " " + shoe.getSize().getSize() + " " + shoe.getColor().getColor(), Collectors.counting()));
    }
    public static void main(String[] args) throws IOException, SQLException {
        new Reports();
    }

    void printMapOfStringIntegerToTable(Map<String,Integer>map, String columnName){
        System.out.println("\n");
        String num = "N";
        System.out.printf("%-15s %12s %n",columnName,num);
        System.out.println("----------------------------");
        map.forEach((k,v)->System.out.printf("%-15s %12d %n", k,v));
    }
    void printMapOfStringLongToTable(Map<String,Long>map){
        String num="N";
        System.out.println("\n");
        System.out.printf("%-15s %12s %n", "Customer",num);
        System.out.println("----------------------------");
        map.forEach((k,v)->System.out.printf("%-15s %12d %n", k,v));
    }
    void printTableOfMapWithObjects(){
        Map <Object,Long> map = getMostPopularProduct();
        System.out.println("----MOST POPULAR PRODUCTS, SORTED IN NUMBER OF SALES----");
        System.out.println("N\tPRODUCT");
        System.out.println("----------------------------");
        map.entrySet().stream()
                .sorted((entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue()))
                .forEach(entry -> System.out.println(entry.getValue() + "\t" + entry.getKey()));
    }
    void printCustomerList(Set<Customers>customersList){
        System.out.printf("%-20s %-20s %n","NAME","ADDRESS");
        System.out.println("----------------------------");
        customersList.forEach(e->System.out.printf("%-20s %-20s %n",e.getName(), e.getAddress()));

    }

}

