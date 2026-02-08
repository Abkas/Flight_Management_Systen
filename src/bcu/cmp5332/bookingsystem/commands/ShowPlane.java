package bcu.cmp5332.bookingsystem.commands;

import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.Plane;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;

/**
 * Command that shows detailed information about a
 * single plane, including seating configuration.
 *
 * @author Abhis
 */
public class ShowPlane implements Command {

    private final int planeId;

    /**
     * Creates a new ShowPlane command.
     *
     * @param planeId the id of the plane to show
     */
    public ShowPlane(int planeId) {
        this.planeId = planeId;
    }

    @Override
    public void execute(FlightBookingSystem flightBookingSystem) throws FlightBookingSystemException {
        Plane plane = flightBookingSystem.getPlaneByID(planeId);
        System.out.println(plane.getDetailsLong());
    }
}
