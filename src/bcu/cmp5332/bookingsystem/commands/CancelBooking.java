package bcu.cmp5332.bookingsystem.commands;

import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.Customer;
import bcu.cmp5332.bookingsystem.model.Flight;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;

/**
 * Command that lets the user cancel one of a
 * customer's active bookings using a small menu.
 *
 * @author Abhis
 */
public class CancelBooking implements Command {

    private final int customerId;

    /**
     * Creates a new CancelBooking command.
     *
     * @param customerId the id of the customer
     */
    public CancelBooking(int customerId) {
        this.customerId = customerId;
    }

    @Override
    public void execute(FlightBookingSystem flightBookingSystem) throws FlightBookingSystemException {
        Customer customer = flightBookingSystem.getCustomerByID(customerId);
        java.util.List<bcu.cmp5332.bookingsystem.model.Booking> bookings = customer.getBookings();
        
        // Filter out cancelled bookings
        java.util.List<bcu.cmp5332.bookingsystem.model.Booking> activeBookings = new java.util.ArrayList<>();
        for (bcu.cmp5332.bookingsystem.model.Booking b : bookings) {
            if (!b.isCancelled()) {
                activeBookings.add(b);
            }
        }
        
        if (activeBookings.isEmpty()) {
            System.out.println("This customer has no active bookings to cancel.");
            return;
        }
        
        System.out.println("Active Bookings for Customer #" + customerId + ":");
        for (int i = 0; i < activeBookings.size(); i++) {
             bcu.cmp5332.bookingsystem.model.Booking b = activeBookings.get(i);
             System.out.println((i + 1) + ". Flight: " + b.getFlight().getFlightNumber() + 
                 " (" + b.getFlight().getOrigin() + " - " + b.getFlight().getDestination() + ")" +
                 " | Date: " + b.getBookingDate() + 
                 " | Class: " + b.getBookingClass() + 
                 " | Seat: " + (b.getSeatNumber() == null ? "N/A" : b.getSeatNumber()));
        }
        
        java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.InputStreamReader(System.in));
        int index = -1;
        try {
            while (true) {
                System.out.print("Enter the number of the booking to cancel (or 0 to abort): ");
                String input = reader.readLine();
                if (input == null) break;
                try {
                    index = Integer.parseInt(input.trim());
                    if (index == 0) {
                        System.out.println("Cancellation aborted.");
                        return;
                    }
                    if (index > 0 && index <= activeBookings.size()) {
                        break;
                    } else {
                        System.out.println("Invalid number. Please try again.");
                    }
                } catch (NumberFormatException e) {
                     System.out.println("Invalid input. Please enter a number.");
                }
            }
        } catch (java.io.IOException e) {
             throw new FlightBookingSystemException("Error reading input.");
        }
        
        bcu.cmp5332.bookingsystem.model.Booking toCancel = activeBookings.get(index - 1);
        
        // Check if already cancelled
        if (toCancel.isCancelled()) {
            System.out.println("This booking is already cancelled.");
            return;
        }
        
        // Soft delete: mark as cancelled instead of removing
        toCancel.setCancelled(true);
        
        System.out.println("Booking cancelled successfully.");
        System.out.println("Refund of NPR " + toCancel.getBookedPrice() + " will be processed.");
    }
}
