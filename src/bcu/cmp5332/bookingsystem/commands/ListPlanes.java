package bcu.cmp5332.bookingsystem.commands;

import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.Plane;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;

import java.util.List;

/**
 * Command that prints a short summary line for
 * every plane (aircraft) stored in the system.
 *
 * @author Abhis
 */
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
