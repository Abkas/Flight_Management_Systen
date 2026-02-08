package bcu.cmp5332.bookingsystem.commands;

import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.Plane;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;

public class EditPlane implements Command {

    private final int planeId;
    private final String model;
    private final String registrationNumber;
    private final int ecoRows, ecoCols;
    private final int busRows, busCols;
    private final int fstRows, fstCols;

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
