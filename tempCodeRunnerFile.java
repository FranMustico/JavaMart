import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import javax.swing.*;

// Product class to represent individual products
class Product {
    private String name;
    private int code;
    private String description;
    private double price;
    private String iconPath;

    public Product(String name, int code, String description, double price, String iconPath) {
        this.name = name;
        this.code = code;
        this.description = description;
        this.price = price;
        this.iconPath = iconPath;
    }

    public String getName() {
        return name;
    }

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public double getPrice() {
        return price;
    }

    public String getIconPath() {
        return iconPath;
    }
}

// Customer class to represent individual customers
class Customer {
    private String customerId;
    private String firstName;
    private String lastName;
    private String lastTransactionDate;
    private double totalValue;

    public Customer(String customerId, String firstName, String lastName, String lastTransactionDate, double totalValue) {
        this.customerId = customerId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.lastTransactionDate = lastTransactionDate;
        this.totalValue = totalValue;
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getLastTransactionDate() {
        return lastTransactionDate;
    }

    public double getTotalValue() {
        return totalValue;
    }
}

public class Assignment4GUI extends JFrame {
    private ArrayList<Product> catalog;
    private ArrayList<Customer> customers;
    private JTextArea cartArea;
    private JLabel subtotalLabel, discountLabel, taxLabel, totalLabel, customerDetailsLabel;
    private int[] quantities;
    private double total;

    public Assignment4GUI() {
        setTitle("Store Catalog");
        setSize(900, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Load catalog and customers from file
        catalog = loadCatalog("products.txt");
        customers = loadCustomers("customers.txt");
        quantities = new int[catalog.size()];

        // Left Section - Cart and Summary
        JPanel leftPanel = new JPanel(new BorderLayout());

        // Customer Section
        JPanel customerPanel = new JPanel();
        customerPanel.setLayout(new BoxLayout(customerPanel, BoxLayout.Y_AXIS));
        JLabel customerLabel = new JLabel("Enter Customer ID:");
        JTextField customerIdField = new JTextField();
        JButton loadCustomerButton = new JButton("Load Customer");
        JButton addCustomerButton = new JButton("Add Customer");
        customerDetailsLabel = new JLabel("Customer Details: None");
        
        // Add components vertically
        customerPanel.add(customerLabel);
        customerPanel.add(customerIdField);
        customerPanel.add(loadCustomerButton);
        customerPanel.add(addCustomerButton);
        customerPanel.add(customerDetailsLabel);
        

        // Cart Section
        JPanel cartPanel = new JPanel(new BorderLayout());
        JLabel cartLabel = new JLabel("Cart:");
        cartArea = new JTextArea(10, 20);
        cartArea.setEditable(false);
        cartPanel.add(cartLabel, BorderLayout.NORTH);
        cartPanel.add(cartArea, BorderLayout.CENTER);

        // Summary Section
        JPanel summaryPanel = new JPanel(new GridLayout(4, 1));
        subtotalLabel = new JLabel("Subtotal: 0.00");
        discountLabel = new JLabel("Discount: 0.00");
        taxLabel = new JLabel("Tax: 0.00");
        totalLabel = new JLabel("Total: 0.00");
        summaryPanel.add(subtotalLabel);
        summaryPanel.add(discountLabel);
        summaryPanel.add(taxLabel);
        summaryPanel.add(totalLabel);

        // Buttons for payment options
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2));
        JButton installmentsButton = new JButton("Installments");
        JButton fullPaymentButton = new JButton("Full Payment");
        buttonPanel.add(installmentsButton);
        buttonPanel.add(fullPaymentButton);

        // Adding sections to left panel
        leftPanel.add(customerPanel, BorderLayout.NORTH);
        leftPanel.add(cartPanel, BorderLayout.CENTER);

        JPanel summaryAndButtonsPanel = new JPanel(new BorderLayout());
        summaryAndButtonsPanel.add(summaryPanel, BorderLayout.CENTER);
        summaryAndButtonsPanel.add(buttonPanel, BorderLayout.SOUTH);
        leftPanel.add(summaryAndButtonsPanel, BorderLayout.SOUTH);

        // Right Section - Product Grid with Icons and Description
        JPanel rightPanel = new JPanel(new GridLayout(0, 2, 10, 10));

        for (int i = 0; i < catalog.size(); i++) {
            Product product = catalog.get(i);
            JPanel productPanel = new JPanel(new BorderLayout());

            // Product icon
            JLabel iconLabel = new JLabel();
            iconLabel.setHorizontalAlignment(JLabel.CENTER);
            ImageIcon icon = new ImageIcon(product.getIconPath());
            Image scaledImage = icon.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
            iconLabel.setIcon(new ImageIcon(scaledImage));

            // Product description
            JTextArea descriptionArea = new JTextArea(product.getDescription());
            descriptionArea.setWrapStyleWord(true);
            descriptionArea.setLineWrap(true);
            descriptionArea.setEditable(false);

            // Buttons to Add and Remove Products
            JPanel buttonPanelProduct = new JPanel(new GridLayout(1, 2));
            JButton addButton = new JButton("Add");
            JButton removeButton = new JButton("Remove");

            int index = i;
            addButton.addActionListener(e -> addToCart(index));
            removeButton.addActionListener(e -> removeFromCart(index));

            buttonPanelProduct.add(addButton);
            buttonPanelProduct.add(removeButton);

            // Assemble product panel
            productPanel.add(iconLabel, BorderLayout.NORTH);
            productPanel.add(descriptionArea, BorderLayout.CENTER);
            productPanel.add(buttonPanelProduct, BorderLayout.SOUTH);

            rightPanel.add(productPanel);
        }

        // Add both panels to the frame
        add(leftPanel, BorderLayout.WEST);
        add(rightPanel, BorderLayout.CENTER);
    }

    private void addToCart(int index) {
        quantities[index]++;
        updateCart();
    }

    private void removeFromCart(int index) {
        if (quantities[index] > 0) {
            quantities[index]--;
            updateCart();
        } else {
            JOptionPane.showMessageDialog(this, "No items of this product in the cart to remove.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateCart() {
        StringBuilder cartDetails = new StringBuilder();
        double subtotal = 0.0;
        double discount = 0.0;

        for (int i = 0; i < catalog.size(); i++) {
            if (quantities[i] > 0) {
                Product product = catalog.get(i);
                double lineTotal = quantities[i] * product.getPrice();

                if (quantities[i] > 10) {
                    double productDiscount = lineTotal * 0.1; // 10% discount for >10 same items
                    discount += productDiscount;
                    lineTotal -= productDiscount;
                }

                cartDetails.append(product.getName())
                           .append(" x ").append(quantities[i])
                           .append(" = $").append(String.format("%.2f", lineTotal))
                           .append("\n");
                subtotal += lineTotal;
            }
        }

        double tax = subtotal * 0.06;
        total = subtotal + tax;

        cartArea.setText(cartDetails.toString());
        subtotalLabel.setText(String.format("Subtotal: %.2f", subtotal));
        discountLabel.setText(String.format("Discount: %.2f", discount));
        taxLabel.setText(String.format("Tax: %.2f", tax));
        totalLabel.setText(String.format("Total: %.2f", total));
    }

    private void loadCustomerDetails(String customerId) {
        for (Customer customer : customers) {
            if (customer.getCustomerId().equals(customerId)) {
                customerDetailsLabel.setText(String.format("Customer: %s %s, Last Transaction: %s, Total: $%.2f",
                        customer.getFirstName(), customer.getLastName(), customer.getLastTransactionDate(), customer.getTotalValue()));
                return;
            }
        }
        JOptionPane.showMessageDialog(this, "Customer ID not found.", "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void addCustomer(String customerId) {
        for (Customer customer : customers) {
            if (customer.getCustomerId().equals(customerId)) {
                JOptionPane.showMessageDialog(this, "Customer ID already exists.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        String firstName = JOptionPane.showInputDialog(this, "Enter First Name:");
        String lastName = JOptionPane.showInputDialog(this, "Enter Last Name:");
        String lastTransactionDate = "N/A";
        double totalValue = 0.0;

        Customer newCustomer = new Customer(customerId, firstName, lastName, lastTransactionDate, totalValue);
        customers.add(newCustomer);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("customers.txt", true))) {
            writer.write(String.format("%s,%s,%s,%s,%.2f%n", customerId, firstName, lastName, lastTransactionDate, totalValue));
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error adding customer: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

        JOptionPane.showMessageDialog(this, "Customer added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    private ArrayList<Product> loadCatalog(String fileName) {
        ArrayList<Product> products = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] details = line.split(",");
                if (details.length == 5) {
                    products.add(new Product(
                            details[0],
                            Integer.parseInt(details[1]),
                            details[2],
                            Double.parseDouble(details[3]),
                            details[4]
                    ));
                }
            }
        } catch (IOException | NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Error loading catalog: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        return products;
    }

    private ArrayList<Customer> loadCustomers(String fileName) {
        ArrayList<Customer> customers = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] details = line.split(",");
                if (details.length == 5) {
                    customers.add(new Customer(
                            details[0],
                            details[1],
                            details[2],
                            details[3],
                            Double.parseDouble(details[4])
                    ));
                }
            }
        } catch (IOException | NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Error loading customers: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        return customers;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Assignment4GUI gui = new Assignment4GUI();
            gui.setVisible(true);
        });
    }
}
