package bcu.cmp5332.bookingsystem.commands;

import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;
import bcu.cmp5332.bookingsystem.model.Customer;

public class EditCustomer implements Command {

    private final int customerId;
    private final String name;
    private final String phone;
    private final String gender;
    private final int age;
    private final String email;

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
