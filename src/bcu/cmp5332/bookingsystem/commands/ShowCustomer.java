package bcu.cmp5332.bookingsystem.commands;

import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.Customer;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;

/**
 * Command that shows detailed information about
 * a single customer and their bookings.
 *
 * @author Abhis
 */
public class ShowCustomer implements Command {

    private final int id;

    /**
     * Creates a new ShowCustomer command.
     *
     * @param id the id of the customer to show
     */
    public ShowCustomer(int id) {
        this.id = id;
    }

    @Override
    public void execute(FlightBookingSystem flightBookingSystem) throws FlightBookingSystemException {
        Customer customer = flightBookingSystem.getCustomerByID(id);
        System.out.println(customer.getDetailsLong());
    }
}
