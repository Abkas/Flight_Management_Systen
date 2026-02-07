package bcu.cmp5332.bookingsystem.commands;

import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.Booking;
import bcu.cmp5332.bookingsystem.model.Customer;
import bcu.cmp5332.bookingsystem.model.Flight;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;
import java.time.LocalDate;

public class EditBooking implements Command {

    private final int customerId;
    private final int oldFlightId;
    private final int newFlightId;

    public EditBooking(int customerId, int oldFlightId, int newFlightId) {
        this.customerId = customerId;
        this.oldFlightId = oldFlightId;
        this.newFlightId = newFlightId;
    }

    @Override
    public void execute(FlightBookingSystem flightBookingSystem) throws FlightBookingSystemException {
        Customer customer = flightBookingSystem.getCustomerByID(customerId);
        Flight oldFlight = flightBookingSystem.getFlightByID(oldFlightId);
        Flight newFlight = flightBookingSystem.getFlightByID(newFlightId);

        // Find the old booking to get the date and verify existence
        Booking oldBooking = null;
        for (Booking b : customer.getBookings()) {
            if (b.getFlight().getId() == oldFlightId) {
                oldBooking = b;
                break;
            }
        }

        if (oldBooking == null) {
            throw new FlightBookingSystemException("No booking found for Customer ID " + customerId + " on Flight ID " + oldFlightId);
        }

        // 1. Remove from old flight and customer
        customer.cancelBooking(oldFlight);
        oldFlight.removePassenger(customer);

        // 2. Add to new flight
        LocalDate bookingDate = flightBookingSystem.getSystemDate();
        Booking newBooking = new Booking(customer, newFlight, bookingDate);
        customer.addBooking(newBooking);
        newFlight.addPassenger(customer);

        System.out.println("Booking updated successfully.");
    }
}
