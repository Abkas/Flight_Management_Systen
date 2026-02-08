package bcu.cmp5332.bookingsystem.commands;

import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.Plane;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;

/**
 * Command that adds a new aircraft (plane) to the system
 * with its seating layout for all classes.
 *
 * @author Abhis
 */
public class AddPlane implements Command {

    private final String model;
    private final String registrationNumber;
    private final int ecoRows, ecoCols;
    private final int busRows, busCols;
    private final int fstRows, fstCols;

    /**
     * Creates a new AddPlane command.
     *
     * @param model              the plane model name
     * @param registrationNumber the registration code
     * @param ecoRows            rows in economy class
     * @param ecoCols            seats per row in economy
     * @param busRows            rows in business class
     * @param busCols            seats per row in business
     * @param fstRows            rows in first class
     * @param fstCols            seats per row in first
     */
    public AddPlane(String model, String registrationNumber,
                    int ecoRows, int ecoCols, int busRows, int busCols, int fstRows, int fstCols) {
        this.model = model;
        this.registrationNumber = registrationNumber;
        this.ecoRows = ecoRows;
        this.ecoCols = ecoCols;
        this.busRows = busRows;
        this.busCols = busCols;
        this.fstRows = fstRows;
        this.fstCols = fstCols;
    }
    
    @Override
    public void execute(FlightBookingSystem flightBookingSystem) throws FlightBookingSystemException {
        int maxId = 0;
        if (flightBookingSystem.getPlanes().size() > 0) {
            int lastIndex = flightBookingSystem.getPlanes().size() - 1;
            maxId = flightBookingSystem.getPlanes().get(lastIndex).getId();
        }
        
        Plane plane = new Plane(++maxId, model, registrationNumber,
                               ecoRows, ecoCols, busRows, busCols, fstRows, fstCols);
        flightBookingSystem.addPlane(plane);
        System.out.println("Plane #" + plane.getId() + " added: " + model + " (" + registrationNumber + ")");
        System.out.println("Total capacity: " + plane.getTotalCapacity() + " seats");
    }
}
