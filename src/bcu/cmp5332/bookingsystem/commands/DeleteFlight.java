package bcu.cmp5332.bookingsystem.commands;

import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;
import bcu.cmp5332.bookingsystem.model.Flight;

/**
 * Command that deletes a flight from the system and
 * reports how many passengers had bookings on it.
 *
 * @author Abhis
 */
public class DeleteFlight implements Command {

    private final int flightId;

    /**
     * Creates a new DeleteFlight command.
     *
     * @param flightId the id of the flight to remove
     */
    public DeleteFlight(int flightId) {
        this.flightId = flightId;
    }

    @Override
    public void execute(FlightBookingSystem flightBookingSystem) throws FlightBookingSystemException {
        // Get flight first to show details in message
        Flight flight = flightBookingSystem.getFlightByID(flightId);
        String flightNumber = flight.getFlightNumber();
        int passengersCount = flight.getPassengers().size();
        
        // Remove flight (this also removes all bookings for this flight)
        flightBookingSystem.removeFlight(flightId);

        System.out.println("Flight \"" + flightNumber + "\" (ID:" + flightId + ") deleted successfully.");
        if (passengersCount > 0) {
            System.out.println("  - Bookings for " + passengersCount + " passenger(s) were also removed.");
        }
    }
}
