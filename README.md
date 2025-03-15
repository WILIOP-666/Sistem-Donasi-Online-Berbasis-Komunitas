# Community-Based Online Donation System

A Java-based desktop application designed to connect donors with individuals or groups in need of assistance. The system facilitates the donation process, tracks donations, and provides a map-based interface for delivery location management.

## Features

### Dashboard
- Overview of donation statistics
- Quick view of recent donations
- Summary of pending and delivered donations

### Donation Management
- Create new donations with detailed information
- Track donation status (Pending, In Transit, Delivered, Cancelled)
- View donation details including donor and recipient information
- Update donation status throughout the delivery process

### Donor Management
- Register and manage donor information
- Store contact details for communication
- Track donation history by donor

### Recipient Management
- Register and manage recipient information
- Store recipient needs and contact details
- Track received donations by recipient

### Map Integration
- Visual representation of donation delivery locations
- Interactive map for selecting delivery points
- Coordinate tracking for delivery management

## System Requirements

- Java Development Kit (JDK) 8 or higher
- SQLite Database
- Minimum 4GB RAM
- Windows, macOS, or Linux operating system

## Installation

1. Ensure you have JDK 8 or higher installed on your system
2. Clone the repository or download the source code
3. Compile the Java source files:
   ```
   javac -d bin -cp lib/* src/main/java/com/donasi/*.java src/main/java/com/donasi/*/*.java src/main/java/com/donasi/*/*/*.java
   ```
4. Run the application:
   ```
   java -cp bin;lib/* com.donasi.DonasiApp
   ```

## Usage Guide

### Creating a New Donation
1. Navigate to the "Donasi Baru" tab
2. Select donor and recipient from the dropdown menus
3. Enter donation details (item type, description, quantity)
4. Specify delivery location (manually or using the map)
5. Submit the donation

### Tracking Donations
1. Navigate to the "Tracking Donasi" tab
2. Use search and filter options to find specific donations
3. Select a donation to view details
4. Update the status as the donation progresses

### Managing Donors and Recipients
1. Navigate to "Kelola Donatur" or "Kelola Penerima" tabs
2. Add new donors/recipients using the form
3. View and edit existing records
4. Delete records when necessary

### Using the Map
1. Navigate to the "Peta Lokasi" tab
2. Search for specific locations
3. Click on the map to select coordinates
4. Use the coordinates for donation delivery locations

## System Architecture

### Model-View-Controller (MVC) Pattern
- **Model**: Java classes representing data entities (Donation, Donor, Recipient)
- **View**: Swing-based UI components organized in panels
- **Controller**: DAO classes managing database operations

### Database Structure
- SQLite database with tables for donors, recipients, and donations
- Relationships between entities maintained through foreign keys

### UI Components
- Main application frame with tabbed navigation
- Specialized panels for different functionalities
- Form-based data entry and table-based data display

## Technologies Used

- **Java**: Core programming language
- **Swing**: GUI framework for desktop application
- **SQLite**: Lightweight database for data storage
- **JDBC**: Database connectivity

## License

© 2023 Donasi App - All Rights Reserved

---

Developed as a community service application to facilitate donations and help those in need.