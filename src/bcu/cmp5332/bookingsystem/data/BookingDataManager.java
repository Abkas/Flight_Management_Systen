package bcu.cmp5332.bookingsystem.data;
import bcu.cmp5332.bookingsystem.model.Booking; 
import bcu.cmp5332.bookingsystem.model.Customer; 
import bcu.cmp5332.bookingsystem.model.Flight; 
import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;
import java.time.LocalDate;

public class BookingDataManager implements DataManager {
    
    public final String RESOURCE = "./resources/data/bookings.txt";

    @Override
    public void loadData(FlightBookingSystem fbs) throws IOException, FlightBookingSystemException {
        // TODO: implementation here
        File file = new File(RESOURCE);
        Scanner sc = new Scanner(file);

        while(sc.hasNextLine()){
            String line = sc.nextLine();
            if (line.trim().isEmpty()) {
                continue;
            }

            String[] parts = line.split(SEPARATOR);
            if (parts.length >= 3) {
                int customerId = Integer.parseInt(parts[0]);
                int flightId = Integer.parseInt(parts[1]);
                LocalDate bookingDate = LocalDate.parse(parts[2]);

                Customer customer = fbs.getCustomerByID(customerId);
                Flight flight = fbs.getFlightByID(flightId);

                Booking booking = new Booking(customer,flight,bookingDate);

                customer.addBooking(booking);
                flight.addPassenger(customer);           
            }
        }
        sc.close();
    }

    @Override
    public void storeData(FlightBookingSystem fbs) throws IOException {
        // TODO: implementation here
        FileWriter fw = new FileWriter(RESOURCE);
        PrintWriter out = new PrintWriter(fw);

       for (Customer customer : fbs.getCustomers()) {

        for (Booking booking : customer.getBookings()) {
                out.print(booking.getCustomer().getId() + SEPARATOR);
                out.print(booking.getFlight().getId() + SEPARATOR);
                out.print(booking.getBookingDate() + SEPARATOR);
                out.println();
            }
        }
        out.close();
    }
}
