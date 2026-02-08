package bcu.cmp5332.bookingsystem.model;

import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Flight {
    
    private int id;
    private String flightNumber;
    private String origin;
    private String destination;
    private LocalDate departureDate;
    private Plane plane;

    private final Set<Customer> passengers;

    public Flight(int id, String flightNumber, String origin, String destination, LocalDate departureDate, Plane plane) {
        this.id = id;
        this.flightNumber = flightNumber;
        this.origin = origin;
        this.destination = destination;
        this.departureDate = departureDate;
        this.plane = plane;

        passengers = new HashSet<>();
    }
    
    // Default constructor for loading old data where capacity might be standard 
    // or we can just overload/chain if needed.
    // However, to keep it clean, let's stick to the main constructor.

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
    }
    
    public String getOrigin() {
        return origin;
    }
    
    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public LocalDate getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(LocalDate departureDate) {
        this.departureDate = departureDate;
    }

    public Plane getPlane() {
        return plane;
    }

    public void setPlane(Plane plane) {
        this.plane = plane;
    }

    public int getCapacity(BookingClass _class) {
        if (plane == null) {
            return 0;
        }
        return plane.getCapacity(_class);
    }
    
    public List<Customer> getPassengers() {
        return new ArrayList<>(passengers);
    }
	
    public String getDetailsShort() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/YYYY");
        return "Flight #" + id + " - " + flightNumber + " - " + origin + " to " 
                + destination + " on " + departureDate.format(dtf);
    }

    public String getDetailsLong() {
        String details = "---------------------------\n"
                       + "Flight ID:" + id + "\n"
                       + "Flight No: " + flightNumber + "\n"
                       + "Flight Origin: " + origin + "\n"
                       + "Flight Destination: " + destination + "\n"
                       + "Flight Departure Date: " + departureDate + "\n";
        
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
    
    public int getRows(BookingClass _class) {
        if (plane == null) {
            return 0;
        }
        return plane.getRows(_class);
    }
    
    public int getColumns(BookingClass _class) {
        if (plane == null) {
            return 0;
        }
        return plane.getColumns(_class);
    }

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
    
    public boolean isSeatOccupied(BookingClass _class, String seatNumber) {
        return getOccupiedSeats(_class).contains(seatNumber);
    }

    public void addPassenger(Customer passenger) {
        passengers.add(passenger);
    }

    public void removePassenger(Customer passenger) throws FlightBookingSystemException {
        if (!passengers.contains(passenger)) {
            throw new FlightBookingSystemException("Passenger is not on this flight.");
        }
        passengers.remove(passenger);
    }
}
