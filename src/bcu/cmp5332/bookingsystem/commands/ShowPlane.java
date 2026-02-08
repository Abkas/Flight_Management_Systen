package bcu.cmp5332.bookingsystem.commands;

import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.Plane;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;

public class ShowPlane implements Command {

    private final int planeId;

    public ShowPlane(int planeId) {
        this.planeId = planeId;
    }

    @Override
    public void execute(FlightBookingSystem flightBookingSystem) throws FlightBookingSystemException {
        Plane plane = flightBookingSystem.getPlaneByID(planeId);
        System.out.println(plane.getDetailsLong());
    }
}
