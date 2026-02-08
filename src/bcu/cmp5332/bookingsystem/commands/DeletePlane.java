package bcu.cmp5332.bookingsystem.commands;

import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;

public class DeletePlane implements Command {

    private final int planeId;

    public DeletePlane(int planeId) {
        this.planeId = planeId;
    }

    @Override
    public void execute(FlightBookingSystem flightBookingSystem) throws FlightBookingSystemException {
        flightBookingSystem.removePlane(planeId);
        System.out.println("Plane #" + planeId + " has been deleted.");
    }
}
