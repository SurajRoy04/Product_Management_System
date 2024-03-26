import java.sql.*;
import java.util.Scanner;
public class OnlineMarket{
static final String JDBC_URL = "jdbc:mysql://localhost:3306/Inventory_Management_System";
static final String USER = "root";
static final String PASSWORD = "Wb@758004nak";
static Connection connection;
static Statement statement;
static ResultSet resultSet;
public static void main(String[] args) {
try {
connection = DriverManager.getConnection(JDBC_URL, USER, PASSWORD);
statement = connection.createStatement();
Scanner scanner = new Scanner(System.in);
int choice;
do {
System.out.println("Menu:");
System.out.println("1. Customer - Purchase Items");
System.out.println("2. Seller - Update Products (Insert/Delete)");
System.out.println("3. Exit");
System.out.print("Enter your choice: ");
choice = scanner.nextInt();
switch (choice) {
case 1:
customerPurchase(scanner);
break;
case 2:
sellerUpdateProducts(scanner);
break;
case 3:
System.out.println("Exiting program. Goodbye!");
break;
default:
System.out.println("Invalid choice. Please enter a valid option.");
}
} while (choice != 3);
if (resultSet != null) resultSet.close();
if (statement != null) statement.close();
if (connection != null) connection.close();
} catch (SQLException e) {
e.printStackTrace();
}
}
private static void customerPurchase(Scanner scanner) throws SQLException {
System.out.println("Customer Purchase:");
displayAvailableProducts();
System.out.print("Enter customer ID: ");
int customerId = scanner.nextInt();
System.out.print("Enter product ID to purchase: ");
int productId = scanner.nextInt();
System.out.print("Enter quantity: ");
int quantity = scanner.nextInt();
String purchaseQuery = "INSERT INTO Purchase (product_id, customer_id, quantity, total_price) " +
"VALUES (?, ?, ?, ?)";
try (PreparedStatement purchaseStatement = connection.prepareStatement(purchaseQuery)) {
String getProductPriceQuery = "SELECT price FROM Product WHERE product_id = ?";
try (PreparedStatement priceStatement = connection.prepareStatement(getProductPriceQuery)) {
priceStatement.setInt(1, productId);
resultSet = priceStatement.executeQuery();
if (resultSet.next()) {
double productPrice = resultSet.getDouble("price");
double totalPrice = productPrice * quantity;
// Insert purchase record
purchaseStatement.setInt(1, productId);
purchaseStatement.setInt(2, customerId);
purchaseStatement.setInt(3, quantity);
purchaseStatement.setDouble(4, totalPrice);
int rowsAffected = purchaseStatement.executeUpdate();
if (rowsAffected > 0) {
System.out.println("Purchase successful!");
} else {
System.out.println("Error occurred during purchase. Please try again.");
}
} else {
System.out.println("Product not found with the given ID.");
}
}
}
}
private static void sellerUpdateProducts(Scanner scanner) throws SQLException {
System.out.println("Seller Update Products:");
displayAvailableProducts();
System.out.println("1. Insert new product");
System.out.println("2. Delete a product");
System.out.println("3. Add product quantity");
System.out.print("Enter your choice: ");
int sellerChoice = scanner.nextInt();
switch (sellerChoice) {
case 1:
insertNewProduct(scanner);
break;
case 2:
deleteProduct(scanner);
break;
case 3:
updateProductQuantity(scanner);
break;
default:
System.out.println("Invalid choice. Please enter 1, 2, or 3.");
}
}
private static void displayAvailableProducts() throws SQLException {
System.out.println("Available Products:");
String productQuery = "SELECT * FROM Product";
resultSet = statement.executeQuery(productQuery);
while (resultSet.next()) {
int productId = resultSet.getInt("product_id");
String productName = resultSet.getString("product_name");
double price = resultSet.getDouble("price");
System.out.println(productId + ". " + productName + " - $" + price);
}
}
private static void insertNewProduct(Scanner scanner) throws SQLException {
System.out.print("Enter product id: ");
int pid=scanner.nextInt();
System.out.print("Enter product name: ");
String productName = scanner.next();
System.out.print("Enter product price: ");
double productPrice = scanner.nextDouble();
System.out.print("Enter seller id: ");
int sid = scanner.nextInt();
System.out.print("Enter quantity ");
int pq = scanner.nextInt();
String insertProductQuery = "INSERT INTO Product (product_id,product_name, price,seller_id,quantity) VALUES (?,?,?,?,?)";
try (PreparedStatement insertProductStatement = connection.prepareStatement(insertProductQuery)) {
insertProductStatement.setInt(1, pid);
insertProductStatement.setString(2, productName);
insertProductStatement.setDouble(3, productPrice);
insertProductStatement.setInt(4, sid);
insertProductStatement.setInt(5, pq);
int rowsAffected = insertProductStatement.executeUpdate();
if (rowsAffected > 0) {
System.out.println("New product added successfully!");
} else {
System.out.println("Error occurred while adding a new product. Please try again.");
}
}
}
private static void updateProductQuantity(Scanner scanner) throws SQLException {
System.out.print("Enter product ID to update quantity: ");
int productId = scanner.nextInt();
System.out.print("Enter quantity to add: ");
int quantityToAdd = scanner.nextInt();
String updateQuantityQuery = "UPDATE Product SET quantity = quantity + ? WHERE product_id = ?";
try (PreparedStatement updateQuantityStatement = connection.prepareStatement(updateQuantityQuery)) {
updateQuantityStatement.setInt(1, quantityToAdd);
updateQuantityStatement.setInt(2, productId);
int rowsAffected = updateQuantityStatement.executeUpdate();
if (rowsAffected > 0) {
System.out.println("Product quantity updated successfully!");
} else {
System.out.println("Error occurred while updating product quantity. Please check the product ID and try again.");
}
}
}
private static void deleteProduct(Scanner scanner) throws SQLException {
System.out.print("Enter product ID to delete: ");
int productId = scanner.nextInt();
String deleteProductQuery = "DELETE FROM Product WHERE product_id = ?";
try (PreparedStatement deleteProductStatement = connection.prepareStatement(deleteProductQuery)) {
deleteProductStatement.setInt(1, productId);
int rowsAffected = deleteProductStatement.executeUpdate();
if (rowsAffected > 0) {
System.out.println("Product deleted successfully!");
} else {
System.out.println("Error occurred while deleting the product. Please check the product ID and try again.");
}
}
}
}
