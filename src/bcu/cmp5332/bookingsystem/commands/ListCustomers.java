package bcu.cmp5332.bookingsystem.commands;

import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.Customer;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;

public class ListCustomers implements Command {

    @Override
    public void execute(FlightBookingSystem flightBookingSystem) throws FlightBookingSystemException {
        for (Customer customer : flightBookingSystem.getCustomers()) {
            System.out.println(customer.getDetailsShort());
        }
        System.out.println(flightBookingSystem.getCustomers().size() + " customer(s)");
    }   
}