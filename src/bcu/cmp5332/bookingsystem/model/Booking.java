package bcu.cmp5332.bookingsystem.model;

import java.time.LocalDate;

public class Booking {
    
    private Customer customer;
    private Flight flight;
    private LocalDate bookingDate;
    private BookingClass bookingClass;
    private String seatNumber;
    private double bookedPrice;
    private boolean cancelled;

    public Booking(Customer customer, Flight flight, LocalDate bookingDate, BookingClass bookingClass, String seatNumber, double bookedPrice) {
        this.customer = customer;
        this.flight = flight;
        this.bookingDate = bookingDate;
        this.bookingClass = bookingClass;
        this.seatNumber = seatNumber;
        this.bookedPrice = bookedPrice;
        this.cancelled = false;
    }
    
    public Customer getCustomer(){
        return customer;
    }

    public void setCustomer(Customer customer){
        this.customer = customer;
    }

    public Flight getFlight(){
        return flight;
    }

    public void setFlight(Flight flight){
        this.flight = flight;
    }

    public LocalDate getBookingDate(){
        return bookingDate;
    }

    public void setBookingDate(LocalDate bookingDate){
        this.bookingDate = bookingDate;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public String getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(String seatNumber) {
        this.seatNumber = seatNumber;
    }

    public BookingClass getBookingClass() {
        return bookingClass;
    }

    public void setBookingClass(BookingClass bookingClass) {
        this.bookingClass = bookingClass;
    }

    public double getBookedPrice() {
        return bookedPrice;
    }

    public void setBookedPrice(double bookedPrice) {
        this.bookedPrice = bookedPrice;
    }
}
