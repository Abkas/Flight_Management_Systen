package bcu.cmp5332.bookingsystem.commands;

import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.Plane;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;

/**
 * Command that edits an existing plane in the system,
 * including model, registration and seating layout.
 *
 * @author Abhis
 */
public class EditPlane implements Command {

    private final int planeId;
    private final String model;
    private final String registrationNumber;
    private final int ecoRows, ecoCols;
    private final int busRows, busCols;
    private final int fstRows, fstCols;

    /**
     * Creates a new EditPlane command.
     *
     * @param planeId           the id of the plane to edit
     * @param model             new model name (or empty to keep old)
     * @param registrationNumber new registration code (or empty to keep old)
     * @param ecoRows           new economy rows (or &lt; 0 to keep old)
     * @param ecoCols           new economy columns (or &lt; 0 to keep old)
     * @param busRows           new business rows (or &lt; 0 to keep old)
     * @param busCols           new business columns (or &lt; 0 to keep old)
     * @param fstRows           new first rows (or &lt; 0 to keep old)
     * @param fstCols           new first columns (or &lt; 0 to keep old)
     */
    public EditPlane(int planeId, String model, String registrationNumber,
                     int ecoRows, int ecoCols, int busRows, int busCols, int fstRows, int fstCols) {
        this.planeId = planeId;
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
        Plane plane = flightBookingSystem.getPlaneByID(planeId);
        
        // Update fields only if new values are provided
        if (model != null && !model.isEmpty()) {
            plane.setModel(model);
        }
        if (registrationNumber != null && !registrationNumber.isEmpty()) {
            plane.setRegistrationNumber(registrationNumber);
        }
        
        // Update seating configuration (-1 means skip)
        if (ecoRows >= 0) {
            plane.setEconomyRows(ecoRows);
        }
        if (ecoCols >= 0) {
            plane.setEconomyColumns(ecoCols);
        }
        if (busRows >= 0) {
            plane.setBusinessRows(busRows);
        }
        if (busCols >= 0) {
            plane.setBusinessColumns(busCols);
        }
        if (fstRows >= 0) {
            plane.setFirstRows(fstRows);
        }
        if (fstCols >= 0) {
            plane.setFirstColumns(fstCols);
        }
        
        System.out.println("Plane #" + planeId + " has been updated.");
        System.out.println(plane.getDetailsShort());
    }
}
