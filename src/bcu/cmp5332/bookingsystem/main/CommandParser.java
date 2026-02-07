package bcu.cmp5332.bookingsystem.main;

import bcu.cmp5332.bookingsystem.commands.*;
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

                return new AddFlight(flighNumber, origin, destination, departureDate);
            } else if (cmd.equals("addcustomer")) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
                String name = readStringWithValidation(reader, "Customer Name: ");
                String phone = readStringWithValidation(reader, "Customer Phone: ");

                return new AddCustomer(name, phone);
            } else if (cmd.equals("addbooking")) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
                int customerId = readIntWithValidation(reader, "Customer ID: ");
                int flightId = readIntWithValidation(reader, "Flight ID: ");
                return new AddBooking(customerId, flightId);
            } else if (cmd.equals("cancelbooking")) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
                int customerId = readIntWithValidation(reader, "Customer ID: ");
                int flightId = readIntWithValidation(reader, "Flight ID: ");
                return new CancelBooking(customerId, flightId);
            } else if (cmd.equals("editbooking")) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
                int customerId = readIntWithValidation(reader, "Customer ID: ");
                int oldFlightId = readIntWithValidation(reader, "Old Flight ID: ");
                int newFlightId = readIntWithValidation(reader, "New Flight ID: ");
                return new EditBooking(customerId, oldFlightId, newFlightId);
            } else if (cmd.equals("listbookings")) {
                return new ListBookings();
            } else if (cmd.equals("showbooking")) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
                int customerId = readIntWithValidation(reader, "Customer ID: ");
                int flightId = readIntWithValidation(reader, "Flight ID: ");
                return new ShowBooking(customerId, flightId);
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
}
