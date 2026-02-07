package bcu.cmp5332.bookingsystem.commands;

import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;
import bcu.cmp5332.bookingsystem.model.Flight;
import java.time.LocalDate;

public class EditFlight implements Command {

    private final int flightId;
    private final String flightNumber;
    private final String origin;
    private final String destination;
    private final LocalDate departureDate;
    private final int economyRows;
    private final int economyCols;
    private final int businessRows;
    private final int businessCols;
    private final int firstRows;
    private final int firstCols;

    public EditFlight(int flightId, String flightNumber, String origin, String destination, LocalDate departureDate,
                      int economyRows, int economyCols, int businessRows, int businessCols, int firstRows, int firstCols) {
        this.flightId = flightId;
        this.flightNumber = flightNumber;
        this.origin = origin;
        this.destination = destination;
        this.departureDate = departureDate;
        this.economyRows = economyRows;
        this.economyCols = economyCols;
        this.businessRows = businessRows;
        this.businessCols = businessCols;
        this.firstRows = firstRows;
        this.firstCols = firstCols;
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
        
        // Update seating (-1 means skip)
        if (economyRows >= 0) {
            flight.setEconomyRows(economyRows);
        }
        if (economyCols >= 0) {
            flight.setEconomyColumns(economyCols);
        }
        if (businessRows >= 0) {
            flight.setBusinessRows(businessRows);
        }
        if (businessCols >= 0) {
            flight.setBusinessColumns(businessCols);
        }
        if (firstRows >= 0) {
            flight.setFirstRows(firstRows);
        }
        if (firstCols >= 0) {
            flight.setFirstColumns(firstCols);
        }

        System.out.println("Flight ID:" + flight.getId() + " updated successfully.");
    }
}
