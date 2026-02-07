package bcu.cmp5332.bookingsystem.data;

import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.Flight;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.Scanner;

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
                    
                    if (properties.length < 11) {
                         throw new FlightBookingSystemException("Invalid flight data format on line " + line_idx);
                    }
                    int econRows = Integer.parseInt(properties[5]);
                    int econCols = Integer.parseInt(properties[6]);
                    int busRows = Integer.parseInt(properties[7]);
                    int busCols = Integer.parseInt(properties[8]);
                    int firstRows = Integer.parseInt(properties[9]);
                    int firstCols = Integer.parseInt(properties[10]);
                    
                    Flight flight = new Flight(id, flightNumber, origin, destination, departureDate, 
                                               econRows, econCols, busRows, busCols, firstRows, firstCols);
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
            for (Flight flight : fbs.getFlights()) {
                out.print(flight.getId() + SEPARATOR);
                out.print(flight.getFlightNumber() + SEPARATOR);
                out.print(flight.getOrigin() + SEPARATOR);
                out.print(flight.getDestination() + SEPARATOR);
                out.print(flight.getDepartureDate() + SEPARATOR);
                // New Fields
                out.print(flight.getEconomyRows() + SEPARATOR);
                out.print(flight.getEconomyColumns() + SEPARATOR);
                out.print(flight.getBusinessRows() + SEPARATOR);
                out.print(flight.getBusinessColumns() + SEPARATOR);
                out.print(flight.getFirstRows() + SEPARATOR);
                out.print(flight.getFirstColumns());
                
                out.println();
            }
        }
    }
}
