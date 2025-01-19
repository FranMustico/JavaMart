# Store Catalog and Customer Management System

This project is a Java-based desktop application designed for efficient management of a store's catalog, customer database, and transactions. It features a robust graphical user interface (GUI) alongside a text-based console mode for flexible usage scenarios.

The application focuses on creating a seamless user experience for both the store staff and customers by providing an intuitive platform for managing products, customers, and sales.

---

## Features

### 1. **Product Management**
- **Catalog Display**: Displays a comprehensive list of products, including images, descriptions, and prices.
- **Cart Operations**:
  - Add items to the cart with the desired quantity.
  - Remove specific items or clear the entire cart.
- **Bulk Purchase Discounts**: Automatically applies a 10% discount for orders of 10 or more units of the same product.
- **Sales Tax Calculation**: Includes a 6% tax calculation applied to the subtotal at checkout.

### 2. **Customer Management**
- **Customer Lookup**: Quickly load existing customer details by providing a unique customer ID.
- **New Customer Creation**: Add new customers directly into the system.
- **Transaction Records**:
  - Track the date and amount of the last transaction for each customer.
  - Maintain a history of total purchase value for every customer.
- **Persistent Data Storage**: Reads customer data from `customers.txt` at startup and saves updates to the same file.

### 3. **Invoice and Payment Handling**
- **Payment Options**:
  - Full payment: Offers a 5% discount for upfront payment.
  - Installment payment: Allows flexible partial payments.
- **Invoice Generation**:
  - Generates detailed invoices summarizing purchases, discounts, tax, and payment details.
  - Saves invoices to `invoice.txt` for record-keeping.

### 4. **Console Mode **
- A text-based interface replicating all features available in the GUI:
  - Customer and product management.
  - Cart operations.
  - Payment handling and invoice generation.

---

## Technologies Used
- **Java Swing**: For building the graphical user interface.
- **Java File I/O**: For reading and writing data to and from files.
- **Object-Oriented Programming (OOP)**: To model products, customers, and transactions effectively.
- **Error Handling**: Robust handling of edge cases like invalid inputs and empty cart scenarios.

---

## How It Works

### 1. **File Integration**
- `products.txt`:
  - Contains the product catalog with fields for product code, name, description, price, and icon image path.
  - Updates dynamically as products are added or removed.
- `customers.txt`:
  - Stores customer details such as ID, name, total purchases, and last transaction date.
  - Supports creating and updating customer records.
- `invoice.txt`:
  - Outputs a detailed invoice for each transaction, ensuring records of all sales are kept.

### 2. **GUI Mode**
- **Main Window**:
  - Displays the product catalog with product images, descriptions, and prices.
  - Allows users to add/remove items from the cart.
- **Cart Panel**:
  - Shows items added to the cart with quantities, prices, and total cost.
  - Handles discounts for bulk orders and applies sales tax.
- **Customer Panel**:
  - Supports loading or adding customer details for personalized service.
- **Payment Panel**:
  - Offers full payment or installment options.
  - Generates invoices and saves them for future reference.

### 3. **Console Mode**
- Mirrors the GUI functionality via a command-line interface.
- Provides step-by-step prompts to manage customers, products, and transactions.
- Offers a simpler yet efficient alternative for text-based environments.

---

## How to Build and Run

1. **Compile the Program**:
   ```bash
   javac Assignment4GUI.java
   ```

2. **Run the Program**:
   - **GUI Mode**:
     ```bash
     java Assignment4GUI
     ```
   - **Console Mode**:
     ```bash
     java Assignment4GUI console
     ```

---

## Project Structure

- `Assignment4GUI.java`: Main program containing GUI implementation and logic for managing the catalog, customers, and transactions.
- `products.txt`: Stores product information, including name, price, description, and associated icons.
- `customers.txt`: Maintains customer records, including IDs, names, and purchase history.
- `invoice.txt`: Saves detailed invoices for each completed transaction.

---

## Usage Scenarios

### Store Staff
- Use the GUI to quickly process customer transactions.
- Keep track of inventory and customer records seamlessly.

### Customers
- Enjoy faster checkouts with personalized service.
- Receive detailed invoices for each purchase.

---

## Future Enhancements
- **Search Functionality**: Add a search bar to filter products by name or category.
- **Advanced Analytics**: Generate sales reports and customer insights.
- **Database Integration**: Transition from file-based storage to a database for enhanced scalability.

---

## License
This project is intended for educational purposes and is not licensed for commercial use.

---
