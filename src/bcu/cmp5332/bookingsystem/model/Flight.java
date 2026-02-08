package bcu.cmp5332.bookingsystem.model;

import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Represents a flight in the system, with route, date,
 * prices, plane and list of passengers.
 *
 * @author Abhis
 */
public class Flight {
    
    private int id;
    private String flightNumber;
    private String origin;
    private String destination;
    private LocalDate departureDate;
    private Plane plane;
    private double economyPrice;
    private double businessPrice;
    private double firstClassPrice;
    private boolean deleted;

    private final Set<Customer> passengers;

    /**
     * Creates a new flight with all required details.
     *
     * @param id              the unique id of the flight
     * @param flightNumber    the flight number code
     * @param origin          the starting airport
     * @param destination     the destination airport
     * @param departureDate   the departure date
     * @param plane           the plane used for this flight
     * @param economyPrice    price for economy class
     * @param businessPrice   price for business class
     * @param firstClassPrice price for first class
     */
    public Flight(int id, String flightNumber, String origin, String destination, LocalDate departureDate, Plane plane, double economyPrice, double businessPrice, double firstClassPrice) {
        this.id = id;
        this.flightNumber = flightNumber;
        this.origin = origin;
        this.destination = destination;
        this.departureDate = departureDate;
        this.plane = plane;
        this.economyPrice = economyPrice;
        this.businessPrice = businessPrice;
        this.firstClassPrice = firstClassPrice;
        this.deleted = false;

        passengers = new HashSet<>();
    }
    
    // Default constructor for loading old data where capacity might be standard 
    // or we can just overload/chain if needed.
    // However, to keep it clean, let's stick to the main constructor.

    /**
     * Gets the id of this flight.
     *
     * @return the flight id
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the id of this flight.
     *
     * @param id the new id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Gets the flight number.
     *
     * @return the flight number
     */
    public String getFlightNumber() {
        return flightNumber;
    }

    /**
     * Sets the flight number.
     *
     * @param flightNumber the new flight number
     */
    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
    }
    
    /**
     * Gets the origin airport.
     *
     * @return the origin
     */
    public String getOrigin() {
        return origin;
    }
    
    /**
     * Sets the origin airport.
     *
     * @param origin the new origin
     */
    public void setOrigin(String origin) {
        this.origin = origin;
    }

    /**
     * Gets the destination airport.
     *
     * @return the destination
     */
    public String getDestination() {
        return destination;
    }

    /**
     * Sets the destination airport.
     *
     * @param destination the new destination
     */
    public void setDestination(String destination) {
        this.destination = destination;
    }

    /**
     * Gets the departure date.
     *
     * @return the departure date
     */
    public LocalDate getDepartureDate() {
        return departureDate;
    }

    /**
     * Sets the departure date.
     *
     * @param departureDate the new departure date
     */
    public void setDepartureDate(LocalDate departureDate) {
        this.departureDate = departureDate;
    }

    /**
     * Gets the plane used for this flight.
     *
     * @return the plane, or null if not set
     */
    public Plane getPlane() {
        return plane;
    }

    /**
     * Sets the plane used for this flight.
     *
     * @param plane the new plane
     */
    public void setPlane(Plane plane) {
        this.plane = plane;
    }

    /**
     * Gets the economy ticket price.
     *
     * @return the economy price
     */
    public double getEconomyPrice() {
        return economyPrice;
    }

    /**
     * Sets the economy ticket price.
     *
     * @param economyPrice the new economy price
     */
    public void setEconomyPrice(double economyPrice) {
        this.economyPrice = economyPrice;
    }

    /**
     * Gets the business ticket price.
     *
     * @return the business price
     */
    public double getBusinessPrice() {
        return businessPrice;
    }

    /**
     * Sets the business ticket price.
     *
     * @param businessPrice the new business price
     */
    public void setBusinessPrice(double businessPrice) {
        this.businessPrice = businessPrice;
    }

    /**
     * Gets the first class ticket price.
     *
     * @return the first class price
     */
    public double getFirstClassPrice() {
        return firstClassPrice;
    }

    /**
     * Sets the first class ticket price.
     *
     * @param firstClassPrice the new first class price
     */
    public void setFirstClassPrice(double firstClassPrice) {
        this.firstClassPrice = firstClassPrice;
    }

    /**
     * Checks if this flight is marked as deleted.
     *
     * @return true if deleted, false otherwise
     */
    public boolean isDeleted() {
        return deleted;
    }

    /**
     * Sets the deleted flag for this flight.
     *
     * @param deleted true to mark as deleted
     */
    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    /**
     * Gets the ticket price for the given booking class.
     *
     * @param bookingClass the booking class
     * @return the price for that class
     */
    public double getPrice(BookingClass bookingClass) {
        switch (bookingClass) {
            case ECONOMY:
                return economyPrice;
            case BUSINESS:
                return businessPrice;
            case FIRST:
                return firstClassPrice;
            default:
                return economyPrice;
        }
    }

    /**
     * Gets the seating capacity for the given booking class.
     *
     * @param _class the booking class
     * @return the capacity, or 0 if no plane is set
     */
    public int getCapacity(BookingClass _class) {
        if (plane == null) {
            return 0;
        }
        return plane.getCapacity(_class);
    }
    
    /**
     * Gets a copy of the list of passengers on this flight.
     *
     * @return list of passengers
     */
    public List<Customer> getPassengers() {
        return new ArrayList<>(passengers);
    }
	
    /**
     * Gets a short one-line description of the flight.
     *
     * @return short details string
     */
    public String getDetailsShort() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/YYYY");
        return "Flight #" + id + " - " + flightNumber + " - " + origin + " to " 
                + destination + " on " + departureDate.format(dtf) 
                + " (Eco: NPR " + String.format("%.2f", economyPrice) 
                + ", Bus: NPR " + String.format("%.2f", businessPrice)
                + ", First: NPR " + String.format("%.2f", firstClassPrice) + ")";
    }

    /**
     * Gets a long, multi-line description of the flight
     * including plane details and passengers.
     *
     * @return long details string
     */
    public String getDetailsLong() {
        String details = "---------------------------\n"
                       + "Flight ID:" + id + "\n"
                       + "Flight No: " + flightNumber + "\n"
                       + "Flight Origin: " + origin + "\n"
                       + "Flight Destination: " + destination + "\n"
                       + "Flight Departure Date: " + departureDate + "\n"
                       + "Status: " + (deleted ? "DELETED" : "Active") + "\n"
                       + "Prices:\n"
                       + "  Economy: NPR " + String.format("%.2f", economyPrice) + "\n"
                       + "  Business: NPR " + String.format("%.2f", businessPrice) + "\n"
                       + "  First Class: NPR " + String.format("%.2f", firstClassPrice) + "\n";
        
        if (plane != null) {
            details += "Aircraft: " + plane.getModel() + " (" + plane.getRegistrationNumber() + ")\n"
                    + "Seating:\n"
                    + "  Economy: " + plane.getEconomyRows() + "x" + plane.getEconomyColumns() + " (" + getCapacity(BookingClass.ECONOMY) + ")\n"
                    + "  Business: " + plane.getBusinessRows() + "x" + plane.getBusinessColumns() + " (" + getCapacity(BookingClass.BUSINESS) + ")\n"
                    + "  First: " + plane.getFirstRows() + "x" + plane.getFirstColumns() + " (" + getCapacity(BookingClass.FIRST) + ")\n";
        } else {
            details += "Aircraft: Not assigned\n";
        }
        
        details += "---------------------------\n"
                + "Passengers Present in the flight:\n";
                       
        for (Customer passenger : passengers) {
            String pInfo = passenger.getId() + " - " + passenger.getName() + " - " + passenger.getPhone();
            details += "* Id: " + pInfo + "\n";
        }
        
        details += passengers.size() + " passenger(s)";
        return details;
    }
    
    /**
     * Gets the number of seat rows for the given class.
     *
     * @param _class the booking class
     * @return number of rows, or 0 if no plane is set
     */
    public int getRows(BookingClass _class) {
        if (plane == null) {
            return 0;
        }
        return plane.getRows(_class);
    }
    
    /**
     * Gets the number of seat columns for the given class.
     *
     * @param _class the booking class
     * @return number of columns, or 0 if no plane is set
     */
    public int getColumns(BookingClass _class) {
        if (plane == null) {
            return 0;
        }
        return plane.getColumns(_class);
    }

    /**
     * Checks if there is free space left in the given class.
     *
     * @param _class the booking class
     * @return true if there is at least one free seat
     */
    public boolean hasSpace(BookingClass _class) {
        int booked = 0;
        for (Customer p : passengers) {
            for (Booking b : p.getBookings()) {
                if (b.getFlight().equals(this) && b.getBookingClass() == _class && !b.isCancelled()) {
                    booked++;
                }
            }
        }
        return booked < getCapacity(_class);
    }
    
    /**
     * Gets the set of occupied seat numbers for the given class.
     *
     * @param _class the booking class
     * @return set of occupied seat codes
     */
    public Set<String> getOccupiedSeats(BookingClass _class) {
        Set<String> occupied = new HashSet<>();
        for (Customer p : passengers) {
            for (Booking b : p.getBookings()) {
                if (b.getFlight().equals(this) && b.getBookingClass() == _class && !b.isCancelled()) {
                    // Assuming Booking stores specific seat number now or will do
                    if (b.getSeatNumber() != null) {
                         occupied.add(b.getSeatNumber());
                    }
                }
            }
        }
        return occupied;
    }
    
    /**
     * Checks if a specific seat is already occupied.
     *
     * @param _class     the booking class
     * @param seatNumber the seat code (for example, 1A)
     * @return true if that seat is taken
     */
    public boolean isSeatOccupied(BookingClass _class, String seatNumber) {
        return getOccupiedSeats(_class).contains(seatNumber);
    }

    /**
     * Adds a passenger to this flight.
     *
     * @param passenger the passenger to add
     */
    public void addPassenger(Customer passenger) {
        passengers.add(passenger);
    }

    /**
     * Removes a passenger from this flight.
     *
     * @param passenger the passenger to remove
     * @throws FlightBookingSystemException if the passenger is not on this flight
     */
    public void removePassenger(Customer passenger) throws FlightBookingSystemException {
        if (!passengers.contains(passenger)) {
            throw new FlightBookingSystemException("Passenger is not on this flight.");
        }
        passengers.remove(passenger);
    }
}
