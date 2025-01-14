import java.awt.*;
import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;
import javax.swing.*;

/*
Francisco Mustico 
Final Edition: Sunday December 15th, 2024 
Code created, compiled and tested on MAC.
Detailed video explanation linked in the assignment thread.

Materials used:
- Class Notes
- Class Presentations
- w3schools 
- GeeksforGeeks
- ChatGPT
 */

// Class product with a name, code, description, price, and icon
class Product {
    // Holds the product name
    private String name;
    // Holds the product code
    private int code;
    // Short description of the product
    private String description;
    // Product price
    private double price;
    // File path of the product icon
    private String iconPath;

    // Constructor to set product fields
    public Product(String name, int code, String description, double price, String iconPath) {
        this.name = name; 
        this.code = code;
        this.description = description;
        this.price = price;
        this.iconPath = iconPath;
    }

    // Returns the product name
    public String getName() {
        return name;
    }

    // Returns the product code
    public int getCode() {
        return code;
    }

    // Returns the product description
    public String getDescription() {
        return description;
    }

    // Returns the product price
    public double getPrice() {
        return price;
    }

    // Returns the path to the product icon
    public String getIconPath() {
        return iconPath;
    }
}

// Represents a customer with ID, name, last transaction date, and last purchase value
class Customer {
    // Unique ID for the customer
    private String customerId;
    // Customer's first name
    private String firstName;
    // Customer's last name
    private String lastName;
    // Date of the customer's last transaction
    private String lastTransactionDate;
    // Amount of the most recent purchase
    private double totalValue;

    // Constructor to set customer data
    public Customer(String customerId, String firstName, String lastName, String lastTransactionDate, double totalValue) {
        this.customerId = customerId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.lastTransactionDate = lastTransactionDate;
        this.totalValue = totalValue;
    }

    // Returns the customer's ID
    public String getCustomerId() {
        return customerId;
    }

    // Returns the customer's first name
    public String getFirstName() {
        return firstName;
    }

    // Returns the customer's last name
    public String getLastName() {
        return lastName;
    }

    // Returns the last transaction date
    public String getLastTransactionDate() {
        return lastTransactionDate;
    }

    // Returns the value of the last transaction
    public double getTotalValue() {
        return totalValue;
    }

    // Updates the customer's most recent transaction value and date
    public void updateTransaction(double amount, String date) {
        this.totalValue = amount;
        this.lastTransactionDate = date;
    }
}

// Main GUI class
public class Assignment4GUI extends JFrame {
    // List of products
    private ArrayList<Product> catalog;
    // List of customers
    private ArrayList<Customer> customers;

    // Text area showing items in the cart
    private JTextArea cartArea;
    // Labels for subtotal, discount, tax, and total
    private JLabel subtotalLabel;
    private JLabel discountLabel;
    private JLabel taxLabel;
    private JLabel totalLabel;
    // Label showing the current customer's details
    private JLabel customerDetailsLabel;

    // Array tracking quantity of each product in the cart
    private int[] quantities;
    // Holds the total cost of the cart (including tax)
    private double total;

    // Holds the currently loaded customer object
    private Customer currentCustomer;

    // Constructor sets up the GUI
    public Assignment4GUI() {
        // Set the title of the GUI window
        setTitle("Store Catalog");
        // Set the size of the GUI window
        setSize(900, 600);
        // Close the program when the window is closed
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        // Use a BorderLayout for the main layout
        setLayout(new BorderLayout());

        // Load product catalog and customer data from files
        catalog = loadCatalog("products.txt");
        customers = loadCustomers("customers.txt");

        // Create an array to store quantities for each product
        quantities = new int[catalog.size()];

        // Create the left panel for the customer & cart section
        JPanel leftPanel = new JPanel(new BorderLayout());

        // Panel for customer inputs
        JPanel customerPanel = new JPanel();
        customerPanel.setLayout(new BoxLayout(customerPanel, BoxLayout.Y_AXIS));

        // Label prompting user to enter customer ID
        JLabel customerLabel = new JLabel("Enter Customer ID:");
        // Text field for entering the customer ID
        JTextField customerIdField = new JTextField();
        // set maximum size in order to avoid astethic issue's 
        customerIdField.setMaximumSize(new Dimension(Integer.MAX_VALUE, customerIdField.getPreferredSize().height));

        // Buttons to load or add a customer
        JButton loadCustomerButton = new JButton("Load Customer");
        JButton addCustomerButton = new JButton("Add Customer");

        // Label to show details of the current customer
        customerDetailsLabel = new JLabel("Customer Details: None");
        customerDetailsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Panel to hold the two customer-related buttons
        JPanel customerButtonPanel = new JPanel(new FlowLayout());
        customerButtonPanel.add(loadCustomerButton);
        customerButtonPanel.add(addCustomerButton);

        // Add elements to the customer panel
        customerPanel.add(customerLabel);
        customerPanel.add(customerIdField);
        customerPanel.add(customerButtonPanel);
        customerPanel.add(customerDetailsLabel);

        // Load customer button listener
        loadCustomerButton.addActionListener(e -> {
            String customerId = customerIdField.getText().trim();
            if (customerId.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter a Customer ID.", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                loadCustomerDetails(customerId);
            }
        });

        // Add customer button listener
        addCustomerButton.addActionListener(e -> {
            String customerId = customerIdField.getText().trim();
            if (customerId.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter a Customer ID.", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                addCustomer(customerId);
            }
        });

        // Create a panel for the cart
        JPanel cartPanel = new JPanel(new BorderLayout());
        JLabel cartLabel = new JLabel("Cart:");
        cartArea = new JTextArea(10, 20);
        cartArea.setEditable(false);
        cartPanel.add(cartLabel, BorderLayout.NORTH);
        cartPanel.add(new JScrollPane(cartArea), BorderLayout.CENTER);

        // Summary panel for subtotal, discount, tax, and total
        JPanel summaryPanel = new JPanel(new GridLayout(4, 1));
        subtotalLabel = new JLabel("Subtotal: 0.00");
        discountLabel = new JLabel("Discount: 0.00");
        taxLabel = new JLabel("Tax: 0.00");
        totalLabel = new JLabel("Total: 0.00");
        summaryPanel.add(subtotalLabel);
        summaryPanel.add(discountLabel);
        summaryPanel.add(taxLabel);
        summaryPanel.add(totalLabel);

        // Panel for installments, full payment, and invoice buttons
        JPanel buttonPanel = new JPanel(new GridLayout(1, 3));
        JButton installmentsButton = new JButton("Installments");
        JButton fullPaymentButton = new JButton("Full Payment");
        JButton saveInvoiceButton = new JButton("Save Invoice");

        // Listeners for these three buttons
        installmentsButton.addActionListener(e -> handleInstallments());
        fullPaymentButton.addActionListener(e -> handleFullPayment());
        saveInvoiceButton.addActionListener(e -> saveInvoiceToFile());

        buttonPanel.add(installmentsButton);
        buttonPanel.add(fullPaymentButton);
        buttonPanel.add(saveInvoiceButton);

        // Add customer panel (top) and cart panel (center) to the left panel
        leftPanel.add(customerPanel, BorderLayout.NORTH);
        leftPanel.add(cartPanel, BorderLayout.CENTER);

        // Panel that holds summary and bottom buttons
        JPanel summaryAndButtonsPanel = new JPanel(new BorderLayout());
        summaryAndButtonsPanel.add(summaryPanel, BorderLayout.CENTER);
        summaryAndButtonsPanel.add(buttonPanel, BorderLayout.SOUTH);
        leftPanel.add(summaryAndButtonsPanel, BorderLayout.SOUTH);

        // Panel on the right for products
        JPanel rightPanel = new JPanel(new GridLayout(0, 2, 10, 10));

        // For each product, create a sub-panel with icon, description, and add/remove
        for (int i = 0; i < catalog.size(); i++) {
            Product product = catalog.get(i);
            JPanel productPanel = new JPanel(new BorderLayout());

            JLabel iconLabel = new JLabel();
            iconLabel.setHorizontalAlignment(JLabel.CENTER);

            // Load the product icon and scale it
            ImageIcon icon = new ImageIcon(product.getIconPath());
            Image scaledImage = icon.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
            iconLabel.setIcon(new ImageIcon(scaledImage));

            // Text area for the product description
            JTextArea descriptionArea = new JTextArea(product.getDescription());
            descriptionArea.setWrapStyleWord(true);
            descriptionArea.setLineWrap(true);
            descriptionArea.setEditable(false);

            // Buttons for adding and removing products
            JPanel buttonPanelProduct = new JPanel(new GridLayout(1, 2));
            JButton addButton = new JButton("Add");
            JButton removeButton = new JButton("Remove");

            int index = i;

            // "Add" button logic (positive quantity only)
            addButton.addActionListener(e -> {
                String quantityInput = JOptionPane.showInputDialog(this,
                        "Enter quantity for " + product.getName() + " (positive integer only):");
                try {
                    int qty = Integer.parseInt(quantityInput);
                    if (qty <= 0) {
                        JOptionPane.showMessageDialog(this,
                            "Please enter a positive quantity.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    addToCart(index, qty);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this,
                        "Invalid quantity entered.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                }
            });

            // "Remove" button removes one item
            removeButton.addActionListener(e -> removeFromCart(index, 1));

            buttonPanelProduct.add(addButton);
            buttonPanelProduct.add(removeButton);

            // Put icon, description, and buttons together
            productPanel.add(iconLabel, BorderLayout.NORTH);
            productPanel.add(new JScrollPane(descriptionArea), BorderLayout.CENTER);
            productPanel.add(buttonPanelProduct, BorderLayout.SOUTH);

            rightPanel.add(productPanel);
        }

        // Add the left (cart) and right (products) panels to the frame
        add(leftPanel, BorderLayout.WEST);
        add(rightPanel, BorderLayout.CENTER);
    }

    // Adds quantity of a product to the cart
    private void addToCart(int index, int quantity) {
        quantities[index] += quantity;
        updateCart();
    }

    // Removes a certain quantity from the cart
    private void removeFromCart(int index, int quantityToRemove) {
        if (quantities[index] <= 0) {
            JOptionPane.showMessageDialog(this,
                    "No items of this product in the cart to remove.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (quantityToRemove > quantities[index]) {
            quantities[index] = 0;
        } else {
            quantities[index] -= quantityToRemove;
        }
        updateCart();
    }

    // Updates the cart display and summary labels
    private void updateCart() {
        StringBuilder cartDetails = new StringBuilder();
        double subtotal = 0.0;
        double discount = 0.0;

        // Loop through each product and build the cart details
        for (int i = 0; i < catalog.size(); i++) {
            if (quantities[i] > 0) {
                Product product = catalog.get(i);
                double lineTotal = quantities[i] * product.getPrice();
                // 10% discount if buying 10 or more of the same item
                if (quantities[i] >= 10) {
                    double productDiscount = lineTotal * 0.1;
                    discount += productDiscount;
                    lineTotal -= productDiscount;
                }
                cartDetails.append(product.getName())
                            .append(" x ")
                            .append(quantities[i])
                            .append(" = $")
                            .append(String.format("%.2f", lineTotal))
                            .append("\n");
                subtotal += lineTotal;
            }
        }

        // Calculate 6% tax on the subtotal
        double tax = subtotal * 0.06;
        // Calculate total with tax
        total = subtotal + tax;

        // Show the cart details in the text area
        cartArea.setText(cartDetails.toString());
        // Update labels
        subtotalLabel.setText(String.format("Subtotal: %.2f", subtotal));
        discountLabel.setText(String.format("Discount: %.2f", discount));
        taxLabel.setText(String.format("Tax: %.2f", tax));
        totalLabel.setText(String.format("Total: %.2f", total));
    }

    // Handle installments payment option
    private void handleInstallments() {
        if (total > 0) {
            JOptionPane.showMessageDialog(this,
                "Installments selected. Total will be divided into equal payments.",
                "Installments", JOptionPane.INFORMATION_MESSAGE);
            savePayment(total);
        } else {
            JOptionPane.showMessageDialog(this,
                "No items in the cart to process payment.",
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Handle full payment (5% discount on total)
    private void handleFullPayment() {
        if (total > 0) {
            double discountedTotal = total * 0.95;
            JOptionPane.showMessageDialog(this,
                String.format("Full Payment selected. Total after 5%% discount: $%.2f", discountedTotal),
                "Full Payment", JOptionPane.INFORMATION_MESSAGE);
            savePayment(discountedTotal);
        } else {
            JOptionPane.showMessageDialog(this,
                "No items in the cart to process payment.",
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Saves the payment for the currently loaded customer
    private void savePayment(double paymentAmount) {
        // Check if a customer is loaded
        if (currentCustomer == null) {
            JOptionPane.showMessageDialog(this,
                "No customer is loaded. Please load a customer before making a payment.",
                "Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Update the last transaction for the loaded customer
        String currentDate = LocalDate.now().toString();
        currentCustomer.updateTransaction(paymentAmount, currentDate);

        // Write all customers back to the file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("customers.txt"))) {
            for (Customer c : customers) {
                writer.write(String.format("%s,%s,%s,%s,%.2f%n",
                        c.getCustomerId(),
                        c.getFirstName(),
                        c.getLastName(),
                        c.getLastTransactionDate(),
                        c.getTotalValue()));
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this,
                "Error saving payment: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        JOptionPane.showMessageDialog(this,
            "Payment saved successfully!",
            "Success",
            JOptionPane.INFORMATION_MESSAGE);
    }

    // Save the invoice to a file
    private void saveInvoiceToFile() {
        if (total > 0) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter("invoice.txt", true))) {
                writer.write(cartArea.getText());
                writer.write(String.format("Total: $%.2f\n\n", total));
                JOptionPane.showMessageDialog(this,
                    "Invoice saved successfully!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this,
                    "Error saving invoice: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this,
                "No items in the cart to save as an invoice.",
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    // Load customer details by ID and set currentCustomer
    private void loadCustomerDetails(String customerId) {
        currentCustomer = null;
        for (Customer customer : customers) {
            if (customer.getCustomerId().equals(customerId)) {
                currentCustomer = customer;
                customerDetailsLabel.setText(
                    String.format("Customer: %s %s, Last Transaction: %s, Total: $%.2f",
                        customer.getFirstName(),
                        customer.getLastName(),
                        customer.getLastTransactionDate(),
                        customer.getTotalValue())
                );
                return;
            }
        }
        JOptionPane.showMessageDialog(this,
            "Customer ID not found.",
            "Error",
            JOptionPane.ERROR_MESSAGE);
    }

    // Add a new customer if the ID is not in use
    private void addCustomer(String customerId) {
        for (Customer customer : customers) {
            if (customer.getCustomerId().equals(customerId)) {
                JOptionPane.showMessageDialog(this,
                    "Customer ID already exists.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        String firstName = JOptionPane.showInputDialog(this, "Enter First Name:");
        String lastName = JOptionPane.showInputDialog(this, "Enter Last Name:");
        if (firstName == null || lastName == null) {
            JOptionPane.showMessageDialog(this,
                "Customer creation canceled.",
                "Info",
                JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        String lastTransactionDate = "N/A";
        double totalValue = 0.0;

        // Create and store the new customer
        Customer newCustomer = new Customer(customerId, firstName, lastName, lastTransactionDate, totalValue);
        customers.add(newCustomer);

        // Append new customer to file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("customers.txt", true))) {
            writer.write(String.format("%s,%s,%s,%s,%.2f%n",
                customerId, firstName, lastName, lastTransactionDate, totalValue));
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this,
                "Error adding customer: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }

        JOptionPane.showMessageDialog(this,
            "Customer added successfully!",
            "Success",
            JOptionPane.INFORMATION_MESSAGE);
    }

    // Load all products from a file
    protected ArrayList<Product> loadCatalog(String fileName) {
        ArrayList<Product> products = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] details = line.split(",");
                // Expect 5 values: name, code, description, price, iconPath
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
            JOptionPane.showMessageDialog(this,
                "Error loading catalog: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
        return products;
    }

    // Load all customers from a file
    protected ArrayList<Customer> loadCustomers(String fileName) {
        ArrayList<Customer> customers = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] details = line.split(",");
                // Expect 5 values: ID, firstName, lastName, lastTransactionDate, totalValue
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
            JOptionPane.showMessageDialog(this,
                "Error loading customers: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
        return customers;
    }

    // Main method to run either GUI or console mode
    public static void main(String[] args) {
        // If "console" is an argument, run console version
        if (args.length > 0 && args[0].equalsIgnoreCase("console")) {
            runConsoleVersion();
        } else {
            // Otherwise, run the Swing GUI
            SwingUtilities.invokeLater(() -> {
                Assignment4GUI gui = new Assignment4GUI();
                gui.setVisible(true);
            });
        }
    }

    // Console-based version (extra credit)
    private static void runConsoleVersion() {
        Scanner scanner = new Scanner(System.in);

        Assignment4GUI tempGUI = new Assignment4GUI();
        ArrayList<Product> products = tempGUI.loadCatalog("products.txt");
        ArrayList<Customer> customers = tempGUI.loadCustomers("customers.txt");

        System.out.print("Enter Customer ID: ");
        String customerId = scanner.nextLine().trim();

        Customer currentCustomer = null;
        for (Customer c : customers) {
            if (c.getCustomerId().equals(customerId)) {
                currentCustomer = c;
                break;
            }
        }
        if (currentCustomer == null) {
            System.out.print("Customer not found. Do you want to add a new customer? (y/n): ");
            String choice = scanner.nextLine().trim().toLowerCase();
            if (choice.equals("y")) {
                System.out.print("Enter first name: ");
                String firstName = scanner.nextLine().trim();
                System.out.print("Enter last name: ");
                String lastName = scanner.nextLine().trim();
                currentCustomer = new Customer(customerId, firstName, lastName, "N/A", 0.0);
                customers.add(currentCustomer);
                try (BufferedWriter writer = new BufferedWriter(new FileWriter("customers.txt", true))) {
                    writer.write(String.format("%s,%s,%s,%s,%.2f%n",
                            customerId, firstName, lastName, "N/A", 0.0));
                } catch (IOException e) {
                    System.out.println("Error adding customer: " + e.getMessage());
                }
                System.out.println("New customer added.\n");
            } else {
                System.out.println("Exiting console version...");
                scanner.close();
                return;
            }
        }

        System.out.println("\nCurrent Customer:");
        System.out.printf("ID: %s, Name: %s %s, Last Transaction: %s, Total: $%.2f\n\n",
                currentCustomer.getCustomerId(),
                currentCustomer.getFirstName(),
                currentCustomer.getLastName(),
                currentCustomer.getLastTransactionDate(),
                currentCustomer.getTotalValue());

        int[] cartQuantities = new int[products.size()];

        boolean done = false;
        while (!done) {
            System.out.println("\n--- Product Catalog ---");
            for (int i = 0; i < products.size(); i++) {
                Product p = products.get(i);
                System.out.printf("%d) %s (Code: %d, Price: $%.2f)\n   Description: %s\n",
                        i + 1, p.getName(), p.getCode(), p.getPrice(), p.getDescription());
            }
            System.out.println("\nEnter the product number to modify (or 0 to finish): ");

            int productIndex = -1;
            try {
                productIndex = Integer.parseInt(scanner.nextLine().trim()) - 1;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input, please try again.");
                continue;
            }

            if (productIndex == -1) {
                done = true;
                break;
            }
            if (productIndex < 0 || productIndex >= products.size()) {
                System.out.println("Invalid product number. Please try again.");
                continue;
            }

            System.out.print("Enter a positive number to add or a negative number to remove: ");
            String qtyStr = scanner.nextLine().trim();
            int qty = 0;
            try {
                qty = Integer.parseInt(qtyStr);
            } catch (NumberFormatException e) {
                System.out.println("Invalid quantity, returning to menu...");
                continue;
            }

            if (qty == 0) {
                System.out.println("Quantity 0, no changes made to this product.");
            } else if (qty > 0) {
                cartQuantities[productIndex] += qty;
                System.out.println("Added " + qty + " of " + products.get(productIndex).getName());
            } else {
                int removeQty = Math.abs(qty);
                if (cartQuantities[productIndex] == 0) {
                    System.out.println("No items to remove for that product.");
                } else if (removeQty >= cartQuantities[productIndex]) {
                    cartQuantities[productIndex] = 0;
                    System.out.println("Removed all items of " + products.get(productIndex).getName());
                } else {
                    cartQuantities[productIndex] -= removeQty;
                    System.out.println("Removed " + removeQty + " of " + products.get(productIndex).getName());
                }
            }

            System.out.println("\n** Current Cart **");
            double tempSubtotal = 0.0;
            for (int i = 0; i < products.size(); i++) {
                if (cartQuantities[i] > 0) {
                    Product p = products.get(i);
                    double line = p.getPrice() * cartQuantities[i];
                    tempSubtotal += line;
                    System.out.printf("%s x %d = $%.2f\n", p.getName(), cartQuantities[i], line);
                }
            }
            System.out.printf("Subtotal so far: $%.2f\n", tempSubtotal);
        }

        double subtotal = 0.0;
        double discount = 0.0;
        StringBuilder invoiceBuilder = new StringBuilder();

        invoiceBuilder.append("Invoice\n");
        invoiceBuilder.append("Customer: ")
                    .append(currentCustomer.getFirstName())
                    .append(" ")
                    .append(currentCustomer.getLastName())
                    .append("\n\nOrder Details:\n");

        for (int i = 0; i < products.size(); i++) {
            if (cartQuantities[i] > 0) {
                Product p = products.get(i);
                double lineTotal = p.getPrice() * cartQuantities[i];
                if (cartQuantities[i] >= 10) {
                    double lineDiscount = lineTotal * 0.1;
                    lineTotal -= lineDiscount;
                    discount += lineDiscount;
                }
                invoiceBuilder.append(String.format("%s x %d = $%.2f\n", p.getName(), cartQuantities[i], lineTotal));
                subtotal += lineTotal;
            }
        }

        double tax = subtotal * 0.06;
        double prePaymentTotal = subtotal + tax;

        invoiceBuilder.append(String.format("\nSubtotal: $%.2f", subtotal));
        invoiceBuilder.append(String.format("\nDiscount from bulk items: $%.2f", discount));
        invoiceBuilder.append(String.format("\nTax (6%%): $%.2f", tax));

        if (subtotal > 0) {
            System.out.print("\nPayment method (full/installments): ");
            String paymentMethod = scanner.nextLine().trim().toLowerCase();

            double finalTotal = prePaymentTotal;
            if (paymentMethod.equals("full")) {
                finalTotal = prePaymentTotal * 0.95;
                invoiceBuilder.append(String.format("\nPayment Method: Full (5%% discount applied)"));
            } else {
                invoiceBuilder.append("\nPayment Method: Installments (no extra discount)");
            }

            invoiceBuilder.append(String.format("\nTotal to Pay: $%.2f\n", finalTotal));
            System.out.println("\n" + invoiceBuilder.toString());

            System.out.print("Would you like to save this invoice to 'invoice.txt'? (y/n): ");
            String saveChoice = scanner.nextLine().trim().toLowerCase();
            if (saveChoice.equals("y")) {
                try (BufferedWriter writer = new BufferedWriter(new FileWriter("invoice.txt", true))) {
                    writer.write(invoiceBuilder.toString());
                    writer.write("----------------------------------------\n\n");
                    System.out.println("Invoice saved to invoice.txt.\n");
                } catch (IOException e) {
                    System.out.println("Error saving invoice: " + e.getMessage());
                }
            }

            currentCustomer.updateTransaction(finalTotal, LocalDate.now().toString());

            try (BufferedWriter writer = new BufferedWriter(new FileWriter("customers.txt"))) {
                for (Customer c : customers) {
                    writer.write(String.format("%s,%s,%s,%s,%.2f%n",
                            c.getCustomerId(),
                            c.getFirstName(),
                            c.getLastName(),
                            c.getLastTransactionDate(),
                            c.getTotalValue()));
                }
            } catch (IOException e) {
                System.out.println("Error saving customer data: " + e.getMessage());
            }

            System.out.println("Transaction completed. Updated customer record saved.\n");
        } else {
            System.out.println("\nNo items in the cart. Exiting without payment.\n");
        }

        scanner.close();
    }
}
