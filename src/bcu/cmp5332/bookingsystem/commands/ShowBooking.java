package bcu.cmp5332.bookingsystem.commands;

import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.Booking;
import bcu.cmp5332.bookingsystem.model.Customer;
import bcu.cmp5332.bookingsystem.model.Flight;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;

public class ShowBooking implements Command {

    private final int customerId;
    private final int flightId;

    public ShowBooking(int customerId, int flightId) {
        this.customerId = customerId;
        this.flightId = flightId;
    }

    @Override
    public void execute(FlightBookingSystem flightBookingSystem) throws FlightBookingSystemException {
        Customer customer = flightBookingSystem.getCustomerByID(customerId);
        Flight flight = flightBookingSystem.getFlightByID(flightId);
        
        Booking foundBooking = null;
        for (Booking booking : customer.getBookings()) {
            if (booking.getFlight().getId() == flightId) {
                foundBooking = booking;
                break;
            }
        }
        
        if (foundBooking == null) {
            throw new FlightBookingSystemException("No booking found for Customer ID " + customerId + " on Flight ID " + flightId);
        }
        
        System.out.println("Booking Details:");
        System.out.println("Customer: " + customer.getName() + " (ID: " + customer.getId() + ")");
        System.out.println("Flight: " + flight.getFlightNumber() + " (ID: " + flight.getId() + ")");
        System.out.println("Origin: " + flight.getOrigin());
        System.out.println("Destination: " + flight.getDestination());
        System.out.println("Departure Date: " + flight.getDepartureDate());
        System.out.println("Booking Date: " + foundBooking.getBookingDate());
    }
}
