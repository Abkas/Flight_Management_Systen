package bcu.cmp5332.bookingsystem.commands;

import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.Booking;
import bcu.cmp5332.bookingsystem.model.BookingClass;
import bcu.cmp5332.bookingsystem.model.Customer;
import bcu.cmp5332.bookingsystem.model.Flight;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;
import java.time.LocalDate;

public class AddBooking implements Command {

    private final int customerId;
    private final int flightId;
    private final BookingClass bookingClass;

    public AddBooking(int customerId, int flightId, BookingClass bookingClass) {
        this.customerId = customerId;
        this.flightId = flightId;
        this.bookingClass = bookingClass;
    }

    @Override
    public void execute(FlightBookingSystem flightBookingSystem) throws FlightBookingSystemException {
        Customer customer = flightBookingSystem.getCustomerByID(customerId);
        Flight flight = flightBookingSystem.getFlightByID(flightId);
        
        // Prevent booking on deleted flights
        if (flight.isDeleted()) {
            throw new FlightBookingSystemException("Cannot book a deleted flight.");
        }
        
        // Prevent booking for deleted customers
        if (customer.isDeleted()) {
            throw new FlightBookingSystemException("Cannot create booking for deleted customer.");
        }
        
        if (!flight.hasSpace(bookingClass)) {
            throw new FlightBookingSystemException("No space available for " + bookingClass + " class on this flight.");
        }
        
        // Show seat map
        showSeatMap(flight, bookingClass);
        
        // Ask for seat selection
        java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.InputStreamReader(System.in));
        String[] seats = null;
        try {
            while (true) {
                System.out.print("Enter Seat Number(s) (comma separated, e.g. 1A, 1B): ");
                String input = reader.readLine();
                if (input == null || input.trim().isEmpty()) {
                    System.out.println("No seats entered. Please try again.");
                    continue;
                }
                
                String[] parts = input.split(",");
                boolean allValid = true;
                java.util.List<String> validSeats = new java.util.ArrayList<>();
                
                for (String part : parts) {
                    String seat = part.trim().toUpperCase();
                    if (isValidSeat(seat, flight, bookingClass)) {
                         if (flight.isSeatOccupied(bookingClass, seat)) {
                             System.out.println("Seat " + seat + " is already occupied. Please choose another set.");
                             allValid = false;
                             break;
                         }
                         if (validSeats.contains(seat)) {
                             System.out.println("Duplicate seat " + seat + " in your list.");
                             allValid = false;
                             break;
                         }
                         validSeats.add(seat);
                    } else {
                        System.out.println("Invalid seat format: " + seat + ". Please try again.");
                        allValid = false;
                        break;
                    }
                }
                
                if (allValid && !validSeats.isEmpty()) {
                    seats = validSeats.toArray(new String[0]);
                    break;
                }
            }
        } catch (java.io.IOException e) {
             throw new FlightBookingSystemException("Error reading input.");
        }

        LocalDate bookingDate = flightBookingSystem.getSystemDate();
        double bookedPrice = flight.getPrice(bookingClass);
        
        for (String seatNumber : seats) {
            Booking booking = new Booking(customer, flight, bookingDate, bookingClass, seatNumber, bookedPrice);
            customer.addBooking(booking);
            // Important: also add the customer to the flight's passenger list
            flight.addPassenger(customer);
        }
        
        System.out.println("Bookings added successfully for " + bookingClass + " class, Seats: " + String.join(", ", seats));
        System.out.println("Total Price: NPR " + String.format("%.2f", bookedPrice * seats.length) + " (" + seats.length + " seat(s) x NPR " + String.format("%.2f", bookedPrice) + ")");
    }
    
    private void showSeatMap(Flight flight, BookingClass bookingClass) {
        int rows = flight.getRows(bookingClass);
        int cols = flight.getColumns(bookingClass);
        java.util.Set<String> occupied = flight.getOccupiedSeats(bookingClass);
        
        System.out.println("\nSeat Map for " + bookingClass + ":");
        System.out.print("   ");
        for (int c = 0; c < cols; c++) {
            System.out.print((char)('A' + c) + "  ");
        }
        System.out.println();
        
        for (int r = 1; r <= rows; r++) {
            System.out.printf("%2d ", r);
            for (int c = 0; c < cols; c++) {
                String seat = r + "" + (char)('A' + c);
                if (occupied.contains(seat)) {
                    System.out.print("X  ");
                } else {
                    System.out.print("*  ");
                }
            }
             System.out.println();
        }
        System.out.println("(* = Available, X = Occupied)");
    }

    private boolean isValidSeat(String seat, Flight flight, BookingClass bookingClass) {
        if (!seat.matches("\\d+[A-Z]")) return false;
        try {
            int row = Integer.parseInt(seat.substring(0, seat.length() - 1));
            char colChar = seat.charAt(seat.length() - 1);
            int col = colChar - 'A';
            
            return row >= 1 && row <= flight.getRows(bookingClass) &&
                   col >= 0 && col < flight.getColumns(bookingClass);
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
