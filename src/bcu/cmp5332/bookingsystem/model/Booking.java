package bcu.cmp5332.bookingsystem.model;

import java.time.LocalDate;

/**
 * Represents a booking made by a customer
 * for a specific flight, class and seat.
 *
 * @author Abhis
 */
public class Booking {
    
    private Customer customer;
    private Flight flight;
    private LocalDate bookingDate;
    private BookingClass bookingClass;
    private String seatNumber;
    private double bookedPrice;
    private boolean cancelled;

    /**
     * Creates a new booking with the given details.
     *
     * @param customer     the customer who made the booking
     * @param flight       the flight that was booked
     * @param bookingDate  the date when the booking was made
     * @param bookingClass the travel class of the booking
     * @param seatNumber   the seat number (for example, 1A)
     * @param bookedPrice  the price paid at the time of booking
     */
    public Booking(Customer customer, Flight flight, LocalDate bookingDate, BookingClass bookingClass, String seatNumber, double bookedPrice) {
        this.customer = customer;
        this.flight = flight;
        this.bookingDate = bookingDate;
        this.bookingClass = bookingClass;
        this.seatNumber = seatNumber;
        this.bookedPrice = bookedPrice;
        this.cancelled = false;
    }
    
    /**
     * Gets the customer for this booking.
     *
     * @return the customer
     */
    public Customer getCustomer(){
        return customer;
    }

    /**
     * Sets the customer for this booking.
     *
     * @param customer the new customer
     */
    public void setCustomer(Customer customer){
        this.customer = customer;
    }

    /**
     * Gets the flight for this booking.
     *
     * @return the flight
     */
    public Flight getFlight(){
        return flight;
    }

    /**
     * Sets the flight for this booking.
     *
     * @param flight the new flight
     */
    public void setFlight(Flight flight){
        this.flight = flight;
    }

    /**
     * Gets the date when this booking was made.
     *
     * @return the booking date
     */
    public LocalDate getBookingDate(){
        return bookingDate;
    }

    /**
     * Sets the date when this booking was made.
     *
     * @param bookingDate the new booking date
     */
    public void setBookingDate(LocalDate bookingDate){
        this.bookingDate = bookingDate;
    }

    /**
     * Checks if this booking has been cancelled.
     *
     * @return true if cancelled, false otherwise
     */
    public boolean isCancelled() {
        return cancelled;
    }

    /**
     * Sets the cancelled flag for this booking.
     *
     * @param cancelled true to mark as cancelled
     */
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    /**
     * Gets the seat number for this booking.
     *
     * @return the seat number
     */
    public String getSeatNumber() {
        return seatNumber;
    }

    /**
     * Sets the seat number for this booking.
     *
     * @param seatNumber the new seat number
     */
    public void setSeatNumber(String seatNumber) {
        this.seatNumber = seatNumber;
    }

    /**
     * Gets the booking class (economy, business, first).
     *
     * @return the booking class
     */
    public BookingClass getBookingClass() {
        return bookingClass;
    }

    /**
     * Sets the booking class.
     *
     * @param bookingClass the new booking class
     */
    public void setBookingClass(BookingClass bookingClass) {
        this.bookingClass = bookingClass;
    }

    /**
     * Gets the price that was paid at booking time.
     *
     * @return the booked price
     */
    public double getBookedPrice() {
        return bookedPrice;
    }

    /**
     * Sets the price for this booking.
     *
     * @param bookedPrice the new booked price
     */
    public void setBookedPrice(double bookedPrice) {
        this.bookedPrice = bookedPrice;
    }
}
