package bcu.cmp5332.bookingsystem.commands;

import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;
import bcu.cmp5332.bookingsystem.model.Customer;

public class DeleteCustomer implements Command {

    private final int customerId;

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
