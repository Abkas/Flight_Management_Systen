package bcu.cmp5332.bookingsystem.model;

import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a customer in the booking system
 * with contact details and a list of bookings.
 *
 * @author Abhis
 */
public class Customer {
    
    private int id;
    private String name;
    private String phone;
    private String gender;
    private int age;
    private String email;
    private boolean deleted;
    private final List<Booking> bookings = new ArrayList<>();

    /**
     * Creates a new customer with the given details.
     *
     * @param id     the unique id of the customer
     * @param name   the customer's name
     * @param phone  the customer's phone number
     * @param gender the customer's gender
     * @param age    the customer's age
     * @param email  the customer's email address
     */
    public Customer(int id, String name, String phone, String gender, int age, String email) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.gender = gender;
        this.age = age;
        this.email = email;
        this.deleted = false;
    }

    /**
     * Gets the id of this customer.
     *
     * @return the customer id
     */
    public int getId(){
        return id;
    }

    /**
     * Sets the id of this customer.
     *
     * @param id the new id
     */
    public void setId(int id){
        this.id = id;
    }

    /**
     * Gets the customer's name.
     *
     * @return the name
     */
    public String getName(){
        return name;
    }

    /**
     * Sets the customer's name.
     *
     * @param name the new name
     */
    public void setName(String name){
        this.name = name;
    }

    /**
     * Gets the customer's phone number.
     *
     * @return the phone number
     */
    public String getPhone(){
        return phone;
    }

    /**
     * Sets the customer's phone number.
     *
     * @param phone the new phone number
     */
    public void setPhone(String phone){
        this.phone = phone;
    }

    /**
     * Gets the customer's gender.
     *
     * @return the gender
     */
    public String getGender() {
        return gender;
    }

    /**
     * Sets the customer's gender.
     *
     * @param gender the new gender
     */
    public void setGender(String gender) {
        this.gender = gender;
    }

    /**
     * Gets the customer's age.
     *
     * @return the age
     */
    public int getAge() {
        return age;
    }

    /**
     * Sets the customer's age.
     *
     * @param age the new age
     */
    public void setAge(int age) {
        this.age = age;
    }

    /**
     * Gets the customer's email address.
     *
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the customer's email address.
     *
     * @param email the new email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Checks if this customer is marked as deleted.
     *
     * @return true if deleted, false otherwise
     */
    public boolean isDeleted() {
        return deleted;
    }

    /**
     * Sets the deleted flag for this customer.
     *
     * @param deleted true to mark as deleted
     */
    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    /**
     * Adds a booking to this customer.
     *
     * @param booking the booking to add
     * @throws FlightBookingSystemException if the booking is already in the list
     */
    public void addBooking(Booking booking) throws FlightBookingSystemException {
        if (bookings.contains(booking)) {
            throw new FlightBookingSystemException("Booking already made.");
        }
        bookings.add(booking);
    }

    /**
     * Gets a copy of the list of bookings of this customer.
     *
     * @return list of bookings
     */
    public List<Booking> getBookings() {
        return new ArrayList<>(bookings);
    }
    
    /**
     * Gets a short one-line description of the customer.
     *
     * @return short details string
     */
    public String getDetailsShort() {
        return "Customer #" + id + " - " + name + " - " + phone + " - " + age + " (" + gender + ")";
    }

    /**
     * Gets a long, multi-line description of the customer,
     * including basic details and active bookings.
     *
     * @return long details string
     */
    public String getDetailsLong() {
        StringBuilder sb = new StringBuilder();
        sb.append("Customer #").append(id).append("\n");
        sb.append("Name: ").append(name).append("\n");
        sb.append("Phone: ").append(phone).append("\n");
        sb.append("Gender: ").append(gender).append("\n");
        sb.append("Age: ").append(age).append("\n");
        sb.append("Email: ").append(email).append("\n");
        sb.append("Status: ").append(deleted ? "DELETED" : "Active").append("\n");
        
        // Count only active bookings
        int activeBookingCount = 0;
        for (Booking b : bookings) {
            if (!b.isCancelled()) {
                activeBookingCount++;
            }
        }
        sb.append("Number of active bookings: ").append(activeBookingCount).append("\n");
        
        // Show only non-cancelled bookings
        for (Booking b : bookings) {
            if (!b.isCancelled()) {
                sb.append("- ").append(b.getFlight().getDetailsShort()).append(", booked on ").append(b.getBookingDate()).append("\n");
            }
        }
        return sb.toString();
    }

    
    /**
     * Removes a booking from this customer.
     *
     * @param booking the booking to remove
     * @throws FlightBookingSystemException if the booking is not found
     */
    public void removeBooking(Booking booking) throws FlightBookingSystemException {
        if (bookings.contains(booking)) {
            bookings.remove(booking);
        } else {
            throw new FlightBookingSystemException("Booking not found for this customer.");
        }
    }
}
