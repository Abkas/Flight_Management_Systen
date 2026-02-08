package bcu.cmp5332.bookingsystem.commands;

import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;
import bcu.cmp5332.bookingsystem.model.Flight;
import bcu.cmp5332.bookingsystem.model.Plane;
import java.time.LocalDate;

/**
 * Command that updates details of an existing flight,
 * including route, date, plane and ticket prices.
 *
 * @author Abhis
 */
public class EditFlight implements Command {

    private final int flightId;
    private final String flightNumber;
    private final String origin;
    private final String destination;
    private final LocalDate departureDate;
    private final int planeId;
    private final double economyPrice;
    private final double businessPrice;
    private final double firstClassPrice;

    /**
     * Creates a new EditFlight command.
     *
     * @param flightId        the id of the flight to edit
     * @param flightNumber    new flight number (or empty to keep old)
     * @param origin          new origin (or empty to keep old)
     * @param destination     new destination (or empty to keep old)
     * @param departureDate   new departure date (or null to keep old)
     * @param planeId         id of new plane (or &lt;= 0 to keep old)
     * @param economyPrice    new economy price (or &lt; 0 to keep old)
     * @param businessPrice   new business price (or &lt; 0 to keep old)
     * @param firstClassPrice new first class price (or &lt; 0 to keep old)
     */
    public EditFlight(int flightId, String flightNumber, String origin, String destination, LocalDate departureDate, int planeId, double economyPrice, double businessPrice, double firstClassPrice) {
        this.flightId = flightId;
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
        Flight flight = flightBookingSystem.getFlightByID(flightId);
        
        // Update fields only if new values are provided
        if (flightNumber != null && !flightNumber.isEmpty()) {
            flight.setFlightNumber(flightNumber);
        }
        if (origin != null && !origin.isEmpty()) {
            flight.setOrigin(origin);
        }
        if (destination != null && !destination.isEmpty()) {
            flight.setDestination(destination);
        }
        if (departureDate != null) {
            flight.setDepartureDate(departureDate);
        }
        
        // Update plane assignment if a valid plane ID is provided
        if (planeId > 0) {
            Plane plane = flightBookingSystem.getPlaneByID(planeId);
            flight.setPlane(plane);
            System.out.println("Flight assigned to plane: " + plane.getModel() + " (" + plane.getRegistrationNumber() + ")");
        }
        
        // Update prices if valid values are provided (>= 0)
        if (economyPrice >= 0) {
            flight.setEconomyPrice(economyPrice);
            System.out.println("Economy price updated to: NPR " + String.format("%.2f", economyPrice));
        }
        if (businessPrice >= 0) {
            flight.setBusinessPrice(businessPrice);
            System.out.println("Business price updated to: NPR " + String.format("%.2f", businessPrice));
        }
        if (firstClassPrice >= 0) {
            flight.setFirstClassPrice(firstClassPrice);
            System.out.println("First class price updated to: NPR " + String.format("%.2f", firstClassPrice));
        }

        System.out.println("Flight ID:" + flight.getId() + " updated successfully.");
    }
}
