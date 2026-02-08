package bcu.cmp5332.bookingsystem.data;

import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.Plane;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

public class PlaneDataManager implements DataManager {
    
    private final String RESOURCE = "./resources/data/planes.txt";
    
    @Override
    public void loadData(FlightBookingSystem fbs) throws IOException, FlightBookingSystemException {
        try (Scanner sc = new Scanner(new File(RESOURCE))) {
            int line_idx = 1;
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                if (line.trim().isEmpty()) {
                    continue;
                }
                String[] properties = line.split(SEPARATOR, -1);
                try {
                    if (properties.length < 9) {
                        throw new FlightBookingSystemException("Invalid plane data format on line " + line_idx);
                    }
                    
                    int id = Integer.parseInt(properties[0]);
                    String model = properties[1];
                    String registrationNumber = properties[2];
                    int econRows = Integer.parseInt(properties[3]);
                    int econCols = Integer.parseInt(properties[4]);
                    int busRows = Integer.parseInt(properties[5]);
                    int busCols = Integer.parseInt(properties[6]);
                    int firstRows = Integer.parseInt(properties[7]);
                    int firstCols = Integer.parseInt(properties[8]);
                    
                    Plane plane = new Plane(id, model, registrationNumber, 
                                           econRows, econCols, busRows, busCols, firstRows, firstCols);
                    fbs.addPlane(plane);
                } catch (NumberFormatException ex) {
                    throw new FlightBookingSystemException("Unable to parse plane id " + properties[0] + " on line " + line_idx
                        + "\nError: " + ex);
                }
                line_idx++;
            }
        }
    }
    
    @Override
    public void storeData(FlightBookingSystem fbs) throws IOException {
        try (PrintWriter out = new PrintWriter(new FileWriter(RESOURCE))) {
            for (Plane plane : fbs.getPlanes()) {
                out.print(plane.getId() + SEPARATOR);
                out.print(plane.getModel() + SEPARATOR);
                out.print(plane.getRegistrationNumber() + SEPARATOR);
                out.print(plane.getEconomyRows() + SEPARATOR);
                out.print(plane.getEconomyColumns() + SEPARATOR);
                out.print(plane.getBusinessRows() + SEPARATOR);
                out.print(plane.getBusinessColumns() + SEPARATOR);
                out.print(plane.getFirstRows() + SEPARATOR);
                out.print(plane.getFirstColumns());
                out.println();
            }
        }
    }
}
