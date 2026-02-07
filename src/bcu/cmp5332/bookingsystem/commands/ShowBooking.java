package bcu.cmp5332.bookingsystem.commands;

import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.Booking;
import bcu.cmp5332.bookingsystem.model.Customer;
import bcu.cmp5332.bookingsystem.model.Flight;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;

public class ShowBooking implements Command {

    private final int customerId;

    public ShowBooking(int customerId) {
        this.customerId = customerId;
    }

    @Override
    public void execute(FlightBookingSystem flightBookingSystem) throws FlightBookingSystemException {
        Customer customer = flightBookingSystem.getCustomerByID(customerId);
        java.util.List<Booking> bookings = customer.getBookings();
        
        if (bookings.isEmpty()) {
            System.out.println("Customer #" + customer.getId() + " has no active bookings.");
        } else {
            System.out.println("Bookings for Customer: " + customer.getName() + " (ID: " + customer.getId() + ")");
            System.out.println("--------------------------------------------------");
            for (Booking booking : bookings) {
                Flight flight = booking.getFlight();
                System.out.println("* Booking Date: " + booking.getBookingDate());
                System.out.println("  Flight: " + flight.getFlightNumber() + " (ID: " + flight.getId() + ")");
                System.out.println("  Route: " + flight.getOrigin() + " to " + flight.getDestination());
                System.out.println("  Departure: " + flight.getDepartureDate());
                System.out.println("  Class: " + booking.getBookingClass());
                System.out.println("  Seat: " + (booking.getSeatNumber() == null ? "N/A" : booking.getSeatNumber()));
                System.out.println("--------------------------------------------------");
            }
            System.out.println(bookings.size() + " booking(s) found.");
        }
    }
}
