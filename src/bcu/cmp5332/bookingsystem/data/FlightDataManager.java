package bcu.cmp5332.bookingsystem.data;

import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.Flight;
import bcu.cmp5332.bookingsystem.model.Plane;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.Scanner;

/**
 * Data manager that loads and stores flights
 * from the flights.txt file.
 *
 * @author Abhis
 */
public class FlightDataManager implements DataManager {
    
    private final String RESOURCE = "./resources/data/flights.txt";
    
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
                    int id = Integer.parseInt(properties[0]);
                    String flightNumber = properties[1];
                    String origin = properties[2];
                    String destination = properties[3];
                    LocalDate departureDate = LocalDate.parse(properties[4]);
                    
                    Plane plane = null;
                    if (properties.length >= 6 && !properties[5].isEmpty()) {
                        try {
                            int planeId = Integer.parseInt(properties[5]);
                            plane = fbs.getPlaneByID(planeId);
                        } catch (NumberFormatException | FlightBookingSystemException ex) {
                            System.err.println("Warning: Could not load plane for flight " + id + ": " + ex.getMessage());
                        }
                    }
                    
                    double economyPrice = 0.0;
                    double businessPrice = 0.0;
                    double firstClassPrice = 0.0;
                    
                    if (properties.length >= 7 && !properties[6].isEmpty()) {
                        try {
                            economyPrice = Double.parseDouble(properties[6]);
                        } catch (NumberFormatException ex) {
                            System.err.println("Warning: Could not parse economy price for flight " + id);
                        }
                    }
                    if (properties.length >= 8 && !properties[7].isEmpty()) {
                        try {
                            businessPrice = Double.parseDouble(properties[7]);
                        } catch (NumberFormatException ex) {
                            System.err.println("Warning: Could not parse business price for flight " + id);
                        }
                    }
                    if (properties.length >= 9 && !properties[8].isEmpty()) {
                        try {
                            firstClassPrice = Double.parseDouble(properties[8]);
                        } catch (NumberFormatException ex) {
                            System.err.println("Warning: Could not parse first class price for flight " + id);
                        }
                    }
                    
                    Flight flight = new Flight(id, flightNumber, origin, destination, departureDate, plane, economyPrice, businessPrice, firstClassPrice);
                    
                    // Load deleted flag (10th field)
                    if (properties.length >= 10 && !properties[9].isEmpty()) {
                        try {
                            boolean deleted = Boolean.parseBoolean(properties[9]);
                            flight.setDeleted(deleted);
                        } catch (Exception ex) {
                            System.err.println("Warning: Could not parse deleted flag for flight " + id);
                        }
                    }
                    
                    fbs.addFlight(flight);
                } catch (NumberFormatException ex) {
                    throw new FlightBookingSystemException("Unable to parse flight id " + properties[0] + " on line " + line_idx
                        + "\nError: " + ex);
                }
                line_idx++;
            }
        }
    }
    
    @Override
    public void storeData(FlightBookingSystem fbs) throws IOException {
        try (PrintWriter out = new PrintWriter(new FileWriter(RESOURCE))) {
            // Store all flights including deleted ones
            for (Flight flight : fbs.getAllFlights()) {
                out.print(flight.getId() + SEPARATOR);
                out.print(flight.getFlightNumber() + SEPARATOR);
                out.print(flight.getOrigin() + SEPARATOR);
                out.print(flight.getDestination() + SEPARATOR);
                out.print(flight.getDepartureDate() + SEPARATOR);
                // Store plane ID
                if (flight.getPlane() != null) {
                    out.print(flight.getPlane().getId());
                }
                out.print(SEPARATOR);
                out.print(flight.getEconomyPrice() + SEPARATOR);
                out.print(flight.getBusinessPrice() + SEPARATOR);
                out.print(flight.getFirstClassPrice() + SEPARATOR);
                out.print(flight.isDeleted());
                out.println();
            }
        }
    }
}
