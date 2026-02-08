package bcu.cmp5332.bookingsystem.data;

import bcu.cmp5332.bookingsystem.model.Booking; 
import bcu.cmp5332.bookingsystem.model.BookingClass;
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
        File file = new File(RESOURCE);
        Scanner sc = new Scanner(file);

        while(sc.hasNextLine()){
            String line = sc.nextLine();
            if (line.trim().isEmpty()) {
                continue;
            }

            String[] parts = line.split(SEPARATOR, -1);
            if (parts.length >= 3) {
                int customerId = Integer.parseInt(parts[0]);
                int flightId = Integer.parseInt(parts[1]);
                LocalDate bookingDate = LocalDate.parse(parts[2]);
                
                BookingClass bookingClass = BookingClass.ECONOMY; // Default to Economy if missing
                if (parts.length >= 4 && !parts[3].isEmpty()) {
                     try {
                        bookingClass = BookingClass.valueOf(parts[3]);
                     } catch (IllegalArgumentException e) {
                        // Keep default economy if parsing fails
                     }
                }

                Customer customer = fbs.getCustomerByID(customerId);
                Flight flight = fbs.getFlightByID(flightId);

                String seatNumber = "";
                // Load seat number if available (new 5th field)
                if (parts.length >= 5) {
                    seatNumber = parts[4];
                }
                
                double bookedPrice = 0.0;
                // Load booked price if available (new 6th field)
                if (parts.length >= 6 && !parts[5].isEmpty()) {
                    try {
                        bookedPrice = Double.parseDouble(parts[5]);
                    } catch (NumberFormatException e) {
                        // Default to flight's current price for this class if not found
                        bookedPrice = flight.getPrice(bookingClass);
                    }
                } else {
                    // For old bookings without price, use current flight price
                    bookedPrice = flight.getPrice(bookingClass);
                }

                Booking booking = new Booking(customer, flight, bookingDate, bookingClass, seatNumber, bookedPrice);

                // Load cancelled flag (7th field)
                if (parts.length >= 7 && !parts[6].isEmpty()) {
                    try {
                        boolean cancelled = Boolean.parseBoolean(parts[6]);
                        booking.setCancelled(cancelled);
                    } catch (Exception ex) {
                        System.err.println("Warning: Could not parse cancelled flag for booking");
                    }
                }

                customer.addBooking(booking);
                flight.addPassenger(customer);           
            }
        }
        sc.close();
    }

    @Override
    public void storeData(FlightBookingSystem fbs) throws IOException {
        FileWriter fw = new FileWriter(RESOURCE);
        PrintWriter out = new PrintWriter(fw);

        // Store all bookings including those from deleted customers
        for (Customer customer : fbs.getAllCustomers()) {
            for (Booking booking : customer.getBookings()) {
                out.print(booking.getCustomer().getId() + SEPARATOR);
                out.print(booking.getFlight().getId() + SEPARATOR);
                out.print(booking.getBookingDate() + SEPARATOR);
                out.print(booking.getBookingClass() + SEPARATOR); 
                out.print((booking.getSeatNumber() == null ? " " : booking.getSeatNumber()) + SEPARATOR);
                out.print(booking.getBookedPrice() + SEPARATOR);
                out.print(booking.isCancelled());
                out.println();
            }
        }
        out.close();
    }
}
