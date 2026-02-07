package bcu.cmp5332.bookingsystem.commands;

import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.Customer;
import bcu.cmp5332.bookingsystem.model.Flight;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;

public class CancelBooking implements Command {

    private final int customerId;

    public CancelBooking(int customerId) {
        this.customerId = customerId;
    }

    @Override
    public void execute(FlightBookingSystem flightBookingSystem) throws FlightBookingSystemException {
        Customer customer = flightBookingSystem.getCustomerByID(customerId);
        java.util.List<bcu.cmp5332.bookingsystem.model.Booking> bookings = customer.getBookings();
        
        if (bookings.isEmpty()) {
            System.out.println("This customer has no bookings to cancel.");
            return;
        }
        
        System.out.println("Bookings for Customer #" + customerId + ":");
        for (int i = 0; i < bookings.size(); i++) {
             bcu.cmp5332.bookingsystem.model.Booking b = bookings.get(i);
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
                    if (index > 0 && index <= bookings.size()) {
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
        
        bcu.cmp5332.bookingsystem.model.Booking toCancel = bookings.get(index - 1);
        
        customer.removeBooking(toCancel);
        
        boolean hasOtherBookingsOnFlight = false;
        for (bcu.cmp5332.bookingsystem.model.Booking b : customer.getBookings()) {
            if (b.getFlight().equals(toCancel.getFlight()) && !b.equals(toCancel)) {
                hasOtherBookingsOnFlight = true;
                break;
            }
        }
        
        if (!hasOtherBookingsOnFlight) {
            toCancel.getFlight().removePassenger(customer);
        }
        
        System.out.println("Booking cancelled successfully.");
    }
}
