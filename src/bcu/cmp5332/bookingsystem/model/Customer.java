package bcu.cmp5332.bookingsystem.model;

import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import java.util.ArrayList;
import java.util.List;

public class Customer {
    
    private int id;
    private String name;
    private String phone;
    private final List<Booking> bookings = new ArrayList<>();
    
    // TODO: implement constructor here
    public Customer(int id, String name, String phone) {
        this.id = id;
        this.name = name;
        this.phone = phone;
    }
    // TODO: implementation of Getter and Setter methods
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
        return "Customer #" + id + " - " + name + " - " + phone;
    }

    public String getDetailsLong() {
        StringBuilder sb = new StringBuilder();
        sb.append("Customer #").append(id).append("\n");
        sb.append("Name: ").append(name).append("\n");
        sb.append("Phone: ").append(phone).append("\n");
        sb.append("Number of bookings: ").append(bookings.size()).append("\n");
        for (Booking b : bookings) {
            sb.append("- ").append(b.getFlight().getDetailsShort()).append(", booked on ").append(b.getBookingDate()).append("\n");
        }
        return sb.toString();
    }

    public void cancelBooking(Flight flight) throws FlightBookingSystemException {
        Booking toRemove = null;
        for (Booking b : bookings) {
            if (b.getFlight().equals(flight)) {
                toRemove = b;
                break;
            }
        }
        if (toRemove == null) {
            throw new FlightBookingSystemException("Customer does not have a booking for this flight.");
        }
        bookings.remove(toRemove);
    }
}
