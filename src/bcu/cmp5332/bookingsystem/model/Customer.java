package bcu.cmp5332.bookingsystem.model;

import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import java.util.ArrayList;
import java.util.List;

public class Customer {
    
    private int id;
    private String name;
    private String phone;
    private String gender;
    private int age;
    private String email;
    private boolean deleted;
    private final List<Booking> bookings = new ArrayList<>();
    
    public Customer(int id, String name, String phone, String gender, int age, String email) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.gender = gender;
        this.age = age;
        this.email = email;
        this.deleted = false;
    }

    public int getId(){
        return id;
    }

    public void setId(int id){
        this.id = id;
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getPhone(){
        return phone;
    }

    public void setPhone(String phone){
        this.phone = phone;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public void addBooking(Booking booking) throws FlightBookingSystemException {
        if (bookings.contains(booking)) {
            throw new FlightBookingSystemException("Booking already made.");
        }
        bookings.add(booking);
    }

    public List<Booking> getBookings() {
        return new ArrayList<>(bookings);
    }
    
    public String getDetailsShort() {
        return "Customer #" + id + " - " + name + " - " + phone + " - " + age + " (" + gender + ")";
    }

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

    
    public void removeBooking(Booking booking) throws FlightBookingSystemException {
        if (bookings.contains(booking)) {
            bookings.remove(booking);
        } else {
            throw new FlightBookingSystemException("Booking not found for this customer.");
        }
    }
}
