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

    public EditFlight(int flightId, String flightNumber, String origin, String destination, LocalDate departureDate) {
        this.flightId = flightId;
        this.flightNumber = flightNumber;
        this.origin = origin;
        this.destination = destination;
        this.departureDate = departureDate;
    }

    @Override
    public void execute(FlightBookingSystem flightBookingSystem) throws FlightBookingSystemException {
        Flight flight = flightBookingSystem.getFlightByID(flightId);
        
        // Update fields only if new values are provided (non-empty for strings, non-null for date)
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

        System.out.println("Flight ID:" + flight.getId() + " updated successfully.");
    }
}
