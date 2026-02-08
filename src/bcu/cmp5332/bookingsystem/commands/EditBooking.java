package bcu.cmp5332.bookingsystem.commands;

import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.Booking;
import bcu.cmp5332.bookingsystem.model.Customer;
import bcu.cmp5332.bookingsystem.model.Flight;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;
import java.time.LocalDate;

public class EditBooking implements Command {

    private final int customerId;

    public EditBooking(int customerId) {
        this.customerId = customerId;
    }

    @Override
    public void execute(FlightBookingSystem flightBookingSystem) throws FlightBookingSystemException {
        Customer customer = flightBookingSystem.getCustomerByID(customerId);
        java.util.List<Booking> bookings = customer.getBookings();
        
        if (bookings.isEmpty()) {
            System.out.println("Customer #" + customer.getId() + " has no active bookings.");
            return;
        }
        
        // 1. List Bookings
        System.out.println("Bookings for Customer #" + customerId + ":");
        for (int i = 0; i < bookings.size(); i++) {
             Booking b = bookings.get(i);
             System.out.println((i + 1) + ". Flight: " + b.getFlight().getFlightNumber() + 
                 " (" + b.getFlight().getOrigin() + " - " + b.getFlight().getDestination() + ")" +
                 " | Date: " + b.getBookingDate() + 
                 " | Class: " + b.getBookingClass() + 
                 " | Seat: " + (b.getSeatNumber() == null ? "N/A" : b.getSeatNumber()) +
                 " | Price: NPR " + String.format("%.2f", b.getBookedPrice()));
        }
        
        // 2. Select Booking
        java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.InputStreamReader(System.in));
        int index = -1;
        try {
            while (true) {
                System.out.print("Enter the number of the booking to edit (or 0 to abort): ");
                String input = reader.readLine();
                if (input == null) return;
                try {
                    index = Integer.parseInt(input.trim());
                    if (index == 0) {
                        System.out.println("Edit aborted.");
                        return;
                    }
                    if (index > 0 && index <= bookings.size()) {
                        break;
                    } else {
                        System.out.println("Invalid number. Please try again.");
                    }
                } catch (NumberFormatException e) {
                     System.out.println("Invalid input. Please enter a number.");
                }
            }
        
            Booking oldBooking = bookings.get(index - 1);
            
            // 3. Select New Flight
            System.out.print("Enter New Flight ID: ");
            String fId = reader.readLine();
            int newFlightId = Integer.parseInt(fId.trim());
            Flight newFlight = flightBookingSystem.getFlightByID(newFlightId);
            
            // 4. Select New Class
            bcu.cmp5332.bookingsystem.model.BookingClass newClass = bcu.cmp5332.bookingsystem.model.BookingClass.ECONOMY;
            while (true) {
                System.out.print("Enter New Class (Economy/Business/First): ");
                String cInput = reader.readLine();
                if (cInput != null && !cInput.trim().isEmpty()) {
                    try {
                        newClass = bcu.cmp5332.bookingsystem.model.BookingClass.valueOf(cInput.trim().toUpperCase());
                        break;
                    } catch (IllegalArgumentException e) {
                        System.out.println("Invalid class.");
                    }
                }
            }
            
            if (!newFlight.hasSpace(newClass)) {
                 System.out.println("New flight does not have space in " + newClass + " class. Aborting.");
                 return;
            }
            
            // 5. Select New Seat (Note: You can only edit one booking at a time)
            showSeatMap(newFlight, newClass);
            String newSeat = "";
            while (true) {
                System.out.print("Enter New Seat Number (e.g. 1A) - Only one seat allowed: ");
                newSeat = reader.readLine().trim().toUpperCase();
                if (isValidSeat(newSeat, newFlight, newClass)) {
                    if (!newFlight.isSeatOccupied(newClass, newSeat)) {
                        break;
                    } else {
                        System.out.println("Seat " + newSeat + " is already occupied. Please choose another.");
                    }
                } else {
                    System.out.println("Invalid seat format. You can only edit one booking at a time.");
                }
            }
            
            // 6. Execute Swap
            double oldPrice = oldBooking.getBookedPrice();
            double newPrice = newFlight.getPrice(newClass);
            double priceDifference = newPrice - oldPrice;
            
            // Show price message
            if (priceDifference > 0) {
                System.out.println("\n*** PRICE CHANGE: You need to pay additional NPR " + String.format("%.2f", priceDifference) + " ***");
                System.out.println("(Old price: NPR " + String.format("%.2f", oldPrice) + " -> New price: NPR " + String.format("%.2f", newPrice) + ")");
            } else if (priceDifference < 0) {
                System.out.println("\n*** PRICE CHANGE: You will be refunded NPR " + String.format("%.2f", Math.abs(priceDifference)) + " ***");
                System.out.println("(Old price: NPR " + String.format("%.2f", oldPrice) + " -> New price: NPR " + String.format("%.2f", newPrice) + ")");
            } else {
                System.out.println("\nNo price change (NPR " + String.format("%.2f", newPrice) + ")");
            }
            
            // Remove old
            customer.removeBooking(oldBooking);
            
            // Check if we need to remove from old flight passenger list
            boolean hasOtherBookingsOnOldFlight = false;
            for (Booking b : customer.getBookings()) {
                if (b.getFlight().equals(oldBooking.getFlight())) {
                    hasOtherBookingsOnOldFlight = true;
                    break;
                }
            }
            if (!hasOtherBookingsOnOldFlight) {
                oldBooking.getFlight().removePassenger(customer);
            }
            
            // Add new
            Booking newBooking = new Booking(customer, newFlight, flightBookingSystem.getSystemDate(), newClass, newSeat, newPrice);
            customer.addBooking(newBooking);
            newFlight.addPassenger(customer);
            
            System.out.println("Booking updated successfully to Flight #" + newFlightId + ", " + newClass + ", Seat " + newSeat);
            
        } catch (Exception e) { // Catch broadly for IO or Parse errors
             if(e instanceof FlightBookingSystemException) throw (FlightBookingSystemException)e;
             throw new FlightBookingSystemException("Error processing input: " + e.getMessage());
        }
    }
    
    private void showSeatMap(Flight flight, bcu.cmp5332.bookingsystem.model.BookingClass bookingClass) {
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

    private boolean isValidSeat(String seat, Flight flight, bcu.cmp5332.bookingsystem.model.BookingClass bookingClass) {
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
