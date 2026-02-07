package bcu.cmp5332.bookingsystem.commands;

import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;
import bcu.cmp5332.bookingsystem.model.Flight;

public class DeleteFlight implements Command {

    private final int flightId;

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
