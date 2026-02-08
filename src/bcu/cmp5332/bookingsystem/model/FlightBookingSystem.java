package bcu.cmp5332.bookingsystem.model;

import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import java.time.LocalDate;
import java.util.*;

/**
 * Main class that stores all flights, customers and planes
 * for the flight booking system.
 * @author 
 */
public class FlightBookingSystem {
    
    private final LocalDate systemDate = LocalDate.parse("2024-11-11");
    
    private final Map<Integer, Customer> customers = new TreeMap<>();
    private final Map<Integer, Flight> flights = new TreeMap<>();
    private final Map<Integer, Plane> planes = new TreeMap<>();

    /**
     * Gets the fixed system date used by the program.
     *
     * @return the system date
     */
    public LocalDate getSystemDate() {
        return systemDate;
    }

    /**
     * Gets all flights that are not marked as deleted.
     *
     * @return list of active flights
     */
    public List<Flight> getFlights() {
        List<Flight> out = new ArrayList<>();
        for (Flight flight : flights.values()) {
            if (!flight.isDeleted()) {
                out.add(flight);
            }
        }
        return Collections.unmodifiableList(out);
    }

    /**
     * Gets all flights in the system, including deleted ones.
     *
     * @return list of all flights
     */
    public List<Flight> getAllFlights() {
        List<Flight> out = new ArrayList<>(flights.values());
        return Collections.unmodifiableList(out);
    }

    /**
     * Finds a flight by ID.
     *
     * @param id the flight ID
     * @return the flight with the given ID
     * @throws FlightBookingSystemException if no flight exists with that ID
     */
    public Flight getFlightByID(int id) throws FlightBookingSystemException {
        if (!flights.containsKey(id)) {
            throw new FlightBookingSystemException("There is no flight with that ID.");
        }
        return flights.get(id);
    }

    /**
     * Gets all customers that are not marked as deleted.
     *
     * @return list of active customers
     */
    public List<Customer> getCustomers() {
        List<Customer> out = new ArrayList<>();
        for (Customer customer : customers.values()) {
            if (!customer.isDeleted()) {
                out.add(customer);
            }
        }
        return Collections.unmodifiableList(out);
    }

    /**
     * Gets all customers in the system, including deleted ones.
     *
     * @return list of all customers
     */
    public List<Customer> getAllCustomers() {
        List<Customer> out = new ArrayList<>(customers.values());
        return Collections.unmodifiableList(out);
    }

    /**
     * Finds a customer by ID.
     *
     * @param id the customer ID
     * @return the customer with the given ID
     * @throws FlightBookingSystemException if no customer exists with that ID
     */
    public Customer getCustomerByID(int id) throws FlightBookingSystemException {
        if (!customers.containsKey(id)) {
            throw new FlightBookingSystemException("Customer with ID " + id + " does not exist in the flight.");
        }
        return customers.get(id);
    }

    /**
     * Adds a new flight to the system.
     *
     * @param flight the flight to add
     * @throws IllegalArgumentException     if a flight with the same ID already exists
     * @throws FlightBookingSystemException if a similar flight already exists
     */
    public void addFlight(Flight flight) throws FlightBookingSystemException {
        if (flights.containsKey(flight.getId())) {
            throw new IllegalArgumentException("Duplicate flight ID.");
        }
        for (Flight existing : flights.values()) {
            if (existing.getFlightNumber().equals(flight.getFlightNumber()) 
                && existing.getDepartureDate().isEqual(flight.getDepartureDate())) {
                throw new FlightBookingSystemException("There is a flight with same "
                        + "number and departure date in the system");
            }
        }
        flights.put(flight.getId(), flight);
    }

    /**
     * Adds a new customer to the system.
     *
     * @param customer the customer to add (must not be {@code null})
     * @throws IllegalArgumentException if the customer is null or the ID is already used
     */
    public void addCustomer(Customer customer) {
        if (customer == null) {
            throw new IllegalArgumentException("Customer cannot be null.");
        }
        if (customers.containsKey(customer.getId())) {
            throw new IllegalArgumentException("Customer already exists in the Flight.");
        }
        customers.put(customer.getId(), customer);
    }

    /**
     * Marks a customer as deleted (soft delete).
     *
     * @param customerId the ID of the customer to delete
     * @throws FlightBookingSystemException if the customer does not exist or is already deleted
     */
    public void removeCustomer(int customerId) throws FlightBookingSystemException {
        if (!customers.containsKey(customerId)) {
            throw new FlightBookingSystemException("Customer with ID " + customerId + " does not exist.");
        }
        Customer customer = customers.get(customerId);
        
        if (customer.isDeleted()) {
            throw new FlightBookingSystemException("Customer with ID " + customerId + " is already deleted.");
        }
        
        customer.setDeleted(true);
    }

    /**
     * Marks a flight as deleted (soft delete).
     *
     * @param flightId the ID of the flight to delete
     * @throws FlightBookingSystemException if the flight does not exist or is already deleted
     */
    public void removeFlight(int flightId) throws FlightBookingSystemException {
        if (!flights.containsKey(flightId)) {
            throw new FlightBookingSystemException("Flight with ID " + flightId + " does not exist.");
        }
        Flight flight = flights.get(flightId);
        
        if (flight.isDeleted()) {
            throw new FlightBookingSystemException("Flight with ID " + flightId + " is already deleted.");
        }
        
        flight.setDeleted(true);
    }

    /**
     * Adds a new plane to the system.
     *
     * @param plane the plane to add
     * @throws IllegalArgumentException     if a plane with the same ID already exists
     * @throws FlightBookingSystemException if a plane with the same registration number exists
     */
    public void addPlane(Plane plane) throws FlightBookingSystemException {
        if (planes.containsKey(plane.getId())) {
            throw new IllegalArgumentException("Duplicate plane ID.");
        }
        for (Plane existing : planes.values()) {
            if (existing.getRegistrationNumber().equals(plane.getRegistrationNumber())) {
                throw new FlightBookingSystemException("A plane with registration number " 
                        + plane.getRegistrationNumber() + " already exists in the system");
            }
        }
        planes.put(plane.getId(), plane);
    }

    /**
     * Gets all planes stored in the system.
     *
     * @return list of planes
     */
    public List<Plane> getPlanes() {
        List<Plane> out = new ArrayList<>(planes.values());
        return Collections.unmodifiableList(out);
    }

    /**
     * Finds a plane by ID.
     *
     * @param id the plane ID
     * @return the plane with the given ID
     * @throws FlightBookingSystemException if no plane exists with that ID
     */
    public Plane getPlaneByID(int id) throws FlightBookingSystemException {
        if (!planes.containsKey(id)) {
            throw new FlightBookingSystemException("There is no plane with that ID.");
        }
        return planes.get(id);
    }

    /**
     * Removes a plane from the system.
     * Can only remove a plane that is not used by any flight.
     *
     * @param planeId the ID of the plane to remove
     * @throws FlightBookingSystemException if the plane does not exist or is still assigned
     */
    public void removePlane(int planeId) throws FlightBookingSystemException {
        if (!planes.containsKey(planeId)) {
            throw new FlightBookingSystemException("Plane with ID " + planeId + " does not exist.");
        }
        
        // Check if any flights are using this plane
        for (Flight flight : flights.values()) {
            if (flight.getPlane() != null && flight.getPlane().getId() == planeId) {
                throw new FlightBookingSystemException("Cannot delete plane. It is assigned to flight #" 
                        + flight.getId() + " (" + flight.getFlightNumber() + ")");
            }
        }
        
        planes.remove(planeId);
    }
}
