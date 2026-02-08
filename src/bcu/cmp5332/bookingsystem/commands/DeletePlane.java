package bcu.cmp5332.bookingsystem.commands;

import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;

/**
 * Command that deletes a plane from the system
 * using its id.
 *
 * @author Abhis
 */
public class DeletePlane implements Command {

    private final int planeId;

    /**
     * Creates a new DeletePlane command.
     *
     * @param planeId the id of the plane to remove
     */
    public DeletePlane(int planeId) {
        this.planeId = planeId;
    }

    @Override
    public void execute(FlightBookingSystem flightBookingSystem) throws FlightBookingSystemException {
        flightBookingSystem.removePlane(planeId);
        System.out.println("Plane #" + planeId + " has been deleted.");
    }
}
