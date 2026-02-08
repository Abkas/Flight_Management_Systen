package bcu.cmp5332.bookingsystem.main;

/**
 * Custom checked exception used across the system
 * to report validation errors and invalid commands.
 *
 * @author Abhis
 */
public class FlightBookingSystemException extends Exception {

    public FlightBookingSystemException(String message) {
        super(message);
    }
}
