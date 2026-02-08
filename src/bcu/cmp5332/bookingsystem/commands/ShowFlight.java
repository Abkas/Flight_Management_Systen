package bcu.cmp5332.bookingsystem.commands;

import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.Flight;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;

/**
 * Command that shows detailed information about
 * a single flight, including prices and seats.
 *
 * @author Abhis
 */
public class ShowFlight implements Command {

    private final int id;

    /**
     * Creates a new ShowFlight command.
     *
     * @param id the id of the flight to show
     */
    public ShowFlight(int id) {
        this.id = id;
    }

    @Override
    public void execute(FlightBookingSystem flightBookingSystem) throws FlightBookingSystemException {
        Flight flight = flightBookingSystem.getFlightByID(id);
        System.out.println(flight.getDetailsLong());
    }
}
