package bcu.cmp5332.bookingsystem.commands;

import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;
import bcu.cmp5332.bookingsystem.model.Customer;

/**
 * Command that deletes a customer from the system
 * (and reports how many bookings they had).
 *
 * @author Abhis
 */
public class DeleteCustomer implements Command {

    private final int customerId;

    /**
     * Creates a new DeleteCustomer command.
     *
     * @param customerId the id of the customer to remove
     */
    public DeleteCustomer(int customerId) {
        this.customerId = customerId;
    }

    @Override
    public void execute(FlightBookingSystem flightBookingSystem) throws FlightBookingSystemException {

        Customer customer = flightBookingSystem.getCustomerByID(customerId);
        String customerName = customer.getName();
        int bookingsCount = customer.getBookings().size();
        
        flightBookingSystem.removeCustomer(customerId);

        System.out.println("Customer \"" + customerName + "\" (ID:" + customerId + ") deleted successfully.");
        if (bookingsCount > 0) {
            System.out.println("  - " + bookingsCount + " booking(s) were also removed.");
        }
    }
}
