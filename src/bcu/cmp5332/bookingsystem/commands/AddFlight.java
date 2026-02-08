package bcu.cmp5332.bookingsystem.commands;

import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.Flight;
import bcu.cmp5332.bookingsystem.model.Plane;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;
import java.time.LocalDate;

public class AddFlight implements Command {

    private final String flightNumber;
    private final String origin;
    private final String destination;
    private final LocalDate departureDate;
    private final int planeId;

    public AddFlight(String flightNumber, String origin, String destination, LocalDate departureDate, int planeId) {
        this.flightNumber = flightNumber;
        this.origin = origin;
        this.destination = destination;
        this.departureDate = departureDate;
        this.planeId = planeId;
    }
    
    @Override
    public void execute(FlightBookingSystem flightBookingSystem) throws FlightBookingSystemException {
        int maxId = 0;
        if (flightBookingSystem.getFlights().size() > 0) {
            int lastIndex = flightBookingSystem.getFlights().size() - 1;
            maxId = flightBookingSystem.getFlights().get(lastIndex).getId();
        }
        
        Plane plane = flightBookingSystem.getPlaneByID(planeId);
        
        Flight flight = new Flight(++maxId, flightNumber, origin, destination, departureDate, plane);
        flightBookingSystem.addFlight(flight);
        System.out.println("Flight #" + flight.getId() + " added.");
        System.out.println("Aircraft: " + plane.getModel() + " (" + plane.getRegistrationNumber() + ")");
        System.out.println("Capacities: " 
            + "Economy(" + flight.getCapacity(bcu.cmp5332.bookingsystem.model.BookingClass.ECONOMY) + "), "
            + "Business(" + flight.getCapacity(bcu.cmp5332.bookingsystem.model.BookingClass.BUSINESS) + "), "
            + "First(" + flight.getCapacity(bcu.cmp5332.bookingsystem.model.BookingClass.FIRST) + ").");
    }
}
