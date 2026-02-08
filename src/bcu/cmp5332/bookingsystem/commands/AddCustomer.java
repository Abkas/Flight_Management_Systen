package bcu.cmp5332.bookingsystem.commands;

import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;
import bcu.cmp5332.bookingsystem.model.Customer;

/**
 * Command that adds a new customer to the
 * flight booking system using the text interface.
 *
 * @author Abhis
 */
public class AddCustomer implements Command {

    private final String name;
    private final String phone;
    private final String gender;
    private final int age;
    private final String email;

    /**
     * Creates a new AddCustomer command with all the
     * details needed for the new customer.
     *
     * @param name   the customer's name
     * @param phone  the customer's phone number
     * @param gender the customer's gender
     * @param age    the customer's age
     * @param email  the customer's email address
     */
    public AddCustomer(String name, String phone, String gender, int age, String email) {
        this.name = name;
        this.phone = phone;
        this.gender = gender;
        this.age = age;
        this.email = email;
    }

    @Override
    public void execute(FlightBookingSystem flightBookingSystem) throws FlightBookingSystemException {
        int maxId = 0;
        if (flightBookingSystem.getCustomers().size() > 0) {
            int lastIndex = flightBookingSystem.getCustomers().size() - 1;
            maxId = flightBookingSystem.getCustomers().get(lastIndex).getId();
        }
        
        Customer customer = new Customer(++maxId, name, phone, gender, age, email);
        flightBookingSystem.addCustomer(customer);

        System.out.println("Customer ID:"+customer.getId()+" added successfully.");
    }
}
