package bcu.cmp5332.bookingsystem.commands;

import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;
import bcu.cmp5332.bookingsystem.model.Customer;

/**
 * Command that updates basic details of a customer
 * such as name, phone, gender, age and email.
 *
 * @author Abhis
 */
public class EditCustomer implements Command {

    private final int customerId;
    private final String name;
    private final String phone;
    private final String gender;
    private final int age;
    private final String email;

    /**
     * Creates a new EditCustomer command.
     *
     * @param customerId the id of the customer to edit
     * @param name       new name (or empty to keep old)
     * @param phone      new phone (or empty to keep old)
     * @param gender     new gender (or empty to keep old)
     * @param age        new age (or &lt;= 0 to keep old)
     * @param email      new email (or empty to keep old)
     */
    public EditCustomer(int customerId, String name, String phone, String gender, int age, String email) {
        this.customerId = customerId;
        this.name = name;
        this.phone = phone;
        this.gender = gender;
        this.age = age;
        this.email = email;
    }

    @Override
    public void execute(FlightBookingSystem flightBookingSystem) throws FlightBookingSystemException {
        Customer customer = flightBookingSystem.getCustomerByID(customerId);
        
        if (name != null && !name.isEmpty()) {
            customer.setName(name);
        }
        if (phone != null && !phone.isEmpty()) {
            customer.setPhone(phone);
        }
        if (gender != null && !gender.isEmpty()) {
            customer.setGender(gender);
        }
        if (age > 0) {
            customer.setAge(age);
        }
        if (email != null && !email.isEmpty()) {
            customer.setEmail(email);
        }

        System.out.println("Customer ID:" + customer.getId() + " updated successfully.");
    }
}
