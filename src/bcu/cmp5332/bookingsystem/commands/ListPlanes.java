package bcu.cmp5332.bookingsystem.commands;

import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.Plane;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;

import java.util.List;

public class ListPlanes implements Command {

    @Override
    public void execute(FlightBookingSystem flightBookingSystem) throws FlightBookingSystemException {
        List<Plane> planes = flightBookingSystem.getPlanes();
        for (Plane plane : planes) {
            System.out.println(plane.getDetailsShort());
        }
        System.out.println(planes.size() + " plane(s)");
    }
}
