package bcu.cmp5332.bookingsystem.commands;

import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;
import bcu.cmp5332.bookingsystem.model.Flight;
import bcu.cmp5332.bookingsystem.model.Plane;
import java.time.LocalDate;

public class EditFlight implements Command {

    private final int flightId;
    private final String flightNumber;
    private final String origin;
    private final String destination;
    private final LocalDate departureDate;
    private final int planeId;

    public EditFlight(int flightId, String flightNumber, String origin, String destination, LocalDate departureDate, int planeId) {
        this.flightId = flightId;
        this.flightNumber = flightNumber;
        this.origin = origin;
        this.destination = destination;
        this.departureDate = departureDate;
        this.planeId = planeId;
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

        System.out.println("Flight ID:" + flight.getId() + " updated successfully.");
    }
}
