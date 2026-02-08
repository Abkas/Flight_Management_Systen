package bcu.cmp5332.bookingsystem.commands;

import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.Booking;
import bcu.cmp5332.bookingsystem.model.Customer;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;

public class ListBookings implements Command {

    @Override
    public void execute(FlightBookingSystem flightBookingSystem) throws FlightBookingSystemException {
        int bookingCount = 0;
        for (Customer customer : flightBookingSystem.getCustomers()) {
            for (Booking booking : customer.getBookings()) {
                // Only show active (non-cancelled) bookings
                if (!booking.isCancelled()) {
                    System.out.println("Customer: " + customer.getName() + " (ID: " + customer.getId() + ") " +
                                       "- Flight: " + booking.getFlight().getFlightNumber() + " (ID: " + booking.getFlight().getId() + ") " +
                                       "- Date: " + booking.getBookingDate());
                    bookingCount++;
                }
            }
        }
        System.out.println(bookingCount + " active booking(s) found.");
    }
}
