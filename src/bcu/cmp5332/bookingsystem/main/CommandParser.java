package bcu.cmp5332.bookingsystem.main;

import bcu.cmp5332.bookingsystem.commands.*;
import bcu.cmp5332.bookingsystem.model.BookingClass;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class CommandParser {
    
    public static Command parse(String line) throws IOException, FlightBookingSystemException {
        try {
            String[] parts = line.split(" ", 3);
            String cmd = parts[0];

            if (cmd.equals("addflight")) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
                String flighNumber = readStringWithValidation(reader, "Flight Number: ");
                String origin = readStringWithValidation(reader, "Origin: ");
                String destination = readStringWithValidation(reader, "Destination: ");

                LocalDate departureDate = parseDateWithAttempts(reader);
                
                int planeId = readIntWithValidation(reader, "Plane ID: ");
                double economyPrice = readDoubleWithValidation(reader, "Economy Price (NPR): ");
                double businessPrice = readDoubleWithValidation(reader, "Business Price (NPR): ");
                double firstClassPrice = readDoubleWithValidation(reader, "First Class Price (NPR): ");

                return new AddFlight(flighNumber, origin, destination, departureDate, planeId, economyPrice, businessPrice, firstClassPrice);
            } else if (cmd.equals("addcustomer")) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
                String name = readStringWithValidation(reader, "Customer Name: ");
                String phone = readStringWithValidation(reader, "Customer Phone: ");
                String gender = readStringOptional(reader, "Gender (optional): ");
                int age = readIntOptional(reader, "Age (optional, 0 to skip): ");
                String email = readStringOptional(reader, "Email (optional): ");

                return new AddCustomer(name, phone, gender, age, email);
            } else if (cmd.equals("addbooking")) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
                int customerId = readIntWithValidation(reader, "Customer ID: ");
                int flightId = readIntWithValidation(reader, "Flight ID: ");
                BookingClass bookingClass = readBookingClassWithValidation(reader, "Class (Economy/Business/First): ");
                return new AddBooking(customerId, flightId, bookingClass);
            } else if (cmd.equals("cancelbooking")) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
                int customerId = readIntWithValidation(reader, "Customer ID: ");
                return new CancelBooking(customerId);
            } else if (cmd.equals("editbooking")) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
                int customerId = readIntWithValidation(reader, "Customer ID: ");
                return new EditBooking(customerId);
            } else if (cmd.equals("listbookings")) {
                return new ListBookings();
            } else if (cmd.equals("showbooking")) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
                int customerId = readIntWithValidation(reader, "Customer ID: ");
                return new ShowBooking(customerId);
            } else if (cmd.equals("showflight")) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
                int flightId = readIntWithValidation(reader, "Flight ID: ");
                return new ShowFlight(flightId);
            } else if (cmd.equals("showcustomer")) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
                int customerId = readIntWithValidation(reader, "Customer ID: ");
                return new ShowCustomer(customerId);
            } else if (cmd.equals("loadgui")) {
                return new LoadGUI();
            } else if (cmd.equals("listflights")) {
                return new ListFlights();
            } else if (cmd.equals("listcustomers")) {
                return new ListCustomers();
            } else if (cmd.equals("editcustomer")) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
                int customerId = readIntWithValidation(reader, "Customer ID: ");
                System.out.println("Leave blank to keep current value.");
                String name = readStringOptional(reader, "New Name: ");
                String phone = readStringOptional(reader, "New Phone: ");
                String gender = readStringOptional(reader, "New Gender: ");
                int age = readIntOptional(reader, "New Age (0 to skip): ");
                String email = readStringOptional(reader, "New Email: ");
                return new EditCustomer(customerId, name, phone, gender, age, email);
            } else if (cmd.equals("deletecustomer")) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
                int customerId = readIntWithValidation(reader, "Customer ID to delete: ");
                return new DeleteCustomer(customerId);
            } else if (cmd.equals("editflight")) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
                int flightId = readIntWithValidation(reader, "Flight ID: ");
                System.out.println("Leave blank to keep current value.");
                String flightNumber = readStringOptional(reader, "New Flight Number: ");
                String origin = readStringOptional(reader, "New Origin: ");
                String destination = readStringOptional(reader, "New Destination: ");
                LocalDate departureDate = parseDateOptional(reader, "New Departure Date (YYYY-MM-DD, blank to skip): ");
                int planeId = readIntOptional(reader, "New Plane ID (0 to skip): ");
                double economyPrice = readDoubleOptional(reader, "New Economy Price (-1 to skip): ");
                double businessPrice = readDoubleOptional(reader, "New Business Price (-1 to skip): ");
                double firstClassPrice = readDoubleOptional(reader, "New First Class Price (-1 to skip): ");
                return new EditFlight(flightId, flightNumber, origin, destination, departureDate, planeId, economyPrice, businessPrice, firstClassPrice);
            } else if (cmd.equals("deleteflight")) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
                int flightId = readIntWithValidation(reader, "Flight ID to delete: ");
                return new DeleteFlight(flightId);
            } else if (cmd.equals("addplane")) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
                String model = readStringWithValidation(reader, "Plane Model: ");
                String registrationNumber = readStringWithValidation(reader, "Registration Number: ");
                System.out.println("Enter Seating Configuration:");
                int ecoRows = readIntWithValidation(reader, "Economy Rows: ");
                int ecoCols = readIntWithValidation(reader, "Economy Cols: ");
                int busRows = readIntWithValidation(reader, "Business Rows: ");
                int busCols = readIntWithValidation(reader, "Business Cols: ");
                int fstRows = readIntWithValidation(reader, "First Class Rows: ");
                int fstCols = readIntWithValidation(reader, "First Class Cols: ");
                return new AddPlane(model, registrationNumber, ecoRows, ecoCols, busRows, busCols, fstRows, fstCols);
            } else if (cmd.equals("listplanes")) {
                return new ListPlanes();
            } else if (cmd.equals("showplane")) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
                int planeId = readIntWithValidation(reader, "Plane ID: ");
                return new ShowPlane(planeId);
            } else if (cmd.equals("editplane")) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
                int planeId = readIntWithValidation(reader, "Plane ID: ");
                System.out.println("Leave blank to keep current value.");
                String model = readStringOptional(reader, "New Model: ");
                String registrationNumber = readStringOptional(reader, "New Registration Number: ");
                System.out.println("Seat Configuration (enter -1 to keep current):");
                int ecoRows = readIntOptionalNegative(reader, "Economy Rows: ");
                int ecoCols = readIntOptionalNegative(reader, "Economy Cols: ");
                int busRows = readIntOptionalNegative(reader, "Business Rows: ");
                int busCols = readIntOptionalNegative(reader, "Business Cols: ");
                int fstRows = readIntOptionalNegative(reader, "First Class Rows: ");
                int fstCols = readIntOptionalNegative(reader, "First Class Cols: ");
                return new EditPlane(planeId, model, registrationNumber, ecoRows, ecoCols, busRows, busCols, fstRows, fstCols);
            } else if (cmd.equals("deleteplane")) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
                int planeId = readIntWithValidation(reader, "Plane ID to delete: ");
                return new DeletePlane(planeId);
            } else if (cmd.equals("help")) {
                return new Help();
            }
        } catch (NumberFormatException ex) {
            // Handle number format exception if needed
        }

        throw new FlightBookingSystemException("Invalid command.");
    }
    
    private static LocalDate parseDateWithAttempts(BufferedReader br, int attempts) throws IOException, FlightBookingSystemException {
        if (attempts < 1) {
            throw new IllegalArgumentException("Number of attempts should be higher that 0");
        }
        while (attempts > 0) {
            attempts--;
            System.out.print("Departure Date (\"YYYY-MM-DD\" format): ");
            try {
                LocalDate departureDate = LocalDate.parse(br.readLine());
                return departureDate;
            } catch (DateTimeParseException dtpe) {
                System.out.println("Date must be in YYYY-MM-DD format. " + attempts + " attempts remaining...");
            }
        }
        
        throw new FlightBookingSystemException("Incorrect departure date provided. Cannot create flight.");
    }
    
    private static LocalDate parseDateWithAttempts(BufferedReader br) throws IOException, FlightBookingSystemException {
        return parseDateWithAttempts(br, 3);
    }

    private static String readStringWithValidation(BufferedReader reader, String prompt) throws IOException {
        String input = "";
        while (input.isEmpty()) {
            System.out.print(prompt);
            input = reader.readLine();
            if (input == null) {
                throw new IOException("Input stream closed.");
            }
            input = input.trim();
            if (input.isEmpty()) {
                System.out.println("Field cannot be empty. Please try again.");
            }
        }
        return input;
    }
    
    private static String readStringOptional(BufferedReader reader, String prompt) throws IOException {
        System.out.print(prompt);
        String input = reader.readLine();
        if (input == null) {
            return "";
        }
        return input.trim();
    }

    private static LocalDate parseDateOptional(BufferedReader reader, String prompt) throws IOException {
        System.out.print(prompt);
        String input = reader.readLine();
        if (input == null || input.trim().isEmpty()) {
            return null;
        }
        try {
            return LocalDate.parse(input.trim());
        } catch (DateTimeParseException e) {
            System.out.println("Invalid date format, skipping date update.");
            return null;
        }
    }
    
    private static int readIntOptional(BufferedReader reader, String prompt) throws IOException {
        System.out.print(prompt);
        String input = reader.readLine();
        if (input == null || input.trim().isEmpty()) {
            return 0;
        }
        try {
            return Integer.parseInt(input.trim());
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private static int readIntOptionalNegative(BufferedReader reader, String prompt) throws IOException {
        System.out.print(prompt);
        String input = reader.readLine();
        if (input == null || input.trim().isEmpty()) {
            return -1;  // -1 means "skip, keep current value"
        }
        try {
            return Integer.parseInt(input.trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private static double readDoubleOptional(BufferedReader reader, String prompt) throws IOException {
        System.out.print(prompt);
        String input = reader.readLine();
        if (input == null || input.trim().isEmpty()) {
            return -1.0;  // -1 means "skip, keep current value"
        }
        try {
            return Double.parseDouble(input.trim());
        } catch (NumberFormatException e) {
            return -1.0;
        }
    }

    private static int readIntWithValidation(BufferedReader reader, String prompt) throws IOException {
        while (true) {
            String input = readStringWithValidation(reader, prompt);
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid number. Please enter a numeric value.");
            }
        }
    }
    
    private static double readDoubleWithValidation(BufferedReader reader, String prompt) throws IOException {
        while (true) {
            String input = readStringWithValidation(reader, prompt);
            try {
                double value = Double.parseDouble(input);
                if (value < 0) {
                    System.out.println("Price must be a positive number.");
                    continue;
                }
                return value;
            } catch (NumberFormatException e) {
                System.out.println("Invalid number. Please enter a numeric value.");
            }
        }
    }
    
    private static BookingClass readBookingClassWithValidation(BufferedReader reader, String prompt) throws IOException {
        while (true) {
            System.out.print(prompt);
            String input = reader.readLine();
            if (input != null && !input.trim().isEmpty()) {
                try {
                    return BookingClass.valueOf(input.trim().toUpperCase());
                } catch (IllegalArgumentException e) {
                    System.out.println("Invalid class. Please enter Economy, Business, or First.");
                }
            } else {
                 System.out.println("Field cannot be empty. Please try again.");
            }
        }
    }
}
