package bcu.cmp5332.bookingsystem.commands;

import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.Flight;
import bcu.cmp5332.bookingsystem.model.Plane;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;
import java.time.LocalDate;

/**
 * Command that adds a new flight to the system
 * with route, date, plane and ticket prices.
 *
 * @author Abhis
 */
public class AddFlight implements Command {

    private final String flightNumber;
    private final String origin;
    private final String destination;
    private final LocalDate departureDate;
    private final int planeId;
    private final double economyPrice;
    private final double businessPrice;
    private final double firstClassPrice;

    /**
     * Creates a new AddFlight command with all flight details.
     *
     * @param flightNumber    the flight number code
     * @param origin          the starting airport
     * @param destination     the destination airport
     * @param departureDate   the departure date
     * @param planeId         the id of the plane to use
     * @param economyPrice    price for economy class
     * @param businessPrice   price for business class
     * @param firstClassPrice price for first class
     */
    public AddFlight(String flightNumber, String origin, String destination, LocalDate departureDate, int planeId, double economyPrice, double businessPrice, double firstClassPrice) {
        this.flightNumber = flightNumber;
        this.origin = origin;
        this.destination = destination;
        this.departureDate = departureDate;
        this.planeId = planeId;
        this.economyPrice = economyPrice;
        this.businessPrice = businessPrice;
        this.firstClassPrice = firstClassPrice;
    }
    
    @Override
    public void execute(FlightBookingSystem flightBookingSystem) throws FlightBookingSystemException {
        int maxId = 0;
        if (flightBookingSystem.getFlights().size() > 0) {
            int lastIndex = flightBookingSystem.getFlights().size() - 1;
            maxId = flightBookingSystem.getFlights().get(lastIndex).getId();
        }
        
        Plane plane = flightBookingSystem.getPlaneByID(planeId);
        
        Flight flight = new Flight(++maxId, flightNumber, origin, destination, departureDate, plane, economyPrice, businessPrice, firstClassPrice);
        flightBookingSystem.addFlight(flight);
        System.out.println("Flight #" + flight.getId() + " added.");
        System.out.println("Prices: Economy NPR " + String.format("%.2f", economyPrice) 
            + ", Business NPR " + String.format("%.2f", businessPrice)
            + ", First NPR " + String.format("%.2f", firstClassPrice));
        System.out.println("Aircraft: " + plane.getModel() + " (" + plane.getRegistrationNumber() + ")");
        System.out.println("Capacities: " 
            + "Economy(" + flight.getCapacity(bcu.cmp5332.bookingsystem.model.BookingClass.ECONOMY) + "), "
            + "Business(" + flight.getCapacity(bcu.cmp5332.bookingsystem.model.BookingClass.BUSINESS) + "), "
            + "First(" + flight.getCapacity(bcu.cmp5332.bookingsystem.model.BookingClass.FIRST) + ").");
    }
}
