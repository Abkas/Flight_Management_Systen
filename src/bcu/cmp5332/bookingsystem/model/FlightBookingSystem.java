package bcu.cmp5332.bookingsystem.model;

import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import java.time.LocalDate;
import java.util.*;

public class FlightBookingSystem {
    
    private final LocalDate systemDate = LocalDate.parse("2024-11-11");
    
    private final Map<Integer, Customer> customers = new TreeMap<>();
    private final Map<Integer, Flight> flights = new TreeMap<>();
    private final Map<Integer, Plane> planes = new TreeMap<>();

    public LocalDate getSystemDate() {
        return systemDate;
    }

    public List<Flight> getFlights() {
        List<Flight> out = new ArrayList<>();
        for (Flight flight : flights.values()) {
            if (!flight.isDeleted()) {
                out.add(flight);
            }
        }
        return Collections.unmodifiableList(out);
    }
    
    // Get all flights including deleted ones (for data persistence)
    public List<Flight> getAllFlights() {
        List<Flight> out = new ArrayList<>(flights.values());
        return Collections.unmodifiableList(out);
    }

    public Flight getFlightByID(int id) throws FlightBookingSystemException {
        if (!flights.containsKey(id)) {
            throw new FlightBookingSystemException("There is no flight with that ID.");
        }
        return flights.get(id);
    }
    public List<Customer> getCustomers() {
        List<Customer> out = new ArrayList<>();
        for (Customer customer : customers.values()) {
            if (!customer.isDeleted()) {
                out.add(customer);
            }
        }
        return Collections.unmodifiableList(out);
    }
    
    // Get all customers including deleted ones (for data persistence)
    public List<Customer> getAllCustomers() {
        List<Customer> out = new ArrayList<>(customers.values());
        return Collections.unmodifiableList(out);
    }
    
    public Customer getCustomerByID(int id) throws FlightBookingSystemException {
        // TODO: implementation here
        if(!customers.containsKey(id)){
            throw new FlightBookingSystemException("Customer with ID " + id + " does not exist in the flight.");
        }
        return customers.get(id);
    }

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

    public void addCustomer(Customer customer) {
        // TODO: implementation here
        if(customer == null){
            throw new IllegalArgumentException("Customer cannot be null.");
        }
        if(customers.containsKey(customer.getId())){
            throw new IllegalArgumentException("Customer already exists in the Flight.");
        }
        customers.put(customer.getId(), customer);
    }

    public void removeCustomer(int customerId) throws FlightBookingSystemException {
        if (!customers.containsKey(customerId)) {
            throw new FlightBookingSystemException("Customer with ID " + customerId + " does not exist.");
        }
        Customer customer = customers.get(customerId);
        
        if (customer.isDeleted()) {
            throw new FlightBookingSystemException("Customer with ID " + customerId + " is already deleted.");
        }
        
        // Soft delete: mark as deleted instead of removing from map
        customer.setDeleted(true);
    }

    public void removeFlight(int flightId) throws FlightBookingSystemException {
        if (!flights.containsKey(flightId)) {
            throw new FlightBookingSystemException("Flight with ID " + flightId + " does not exist.");
        }
        Flight flight = flights.get(flightId);
        
        if (flight.isDeleted()) {
            throw new FlightBookingSystemException("Flight with ID " + flightId + " is already deleted.");
        }
        
        // Soft delete: mark as deleted instead of removing from map
        flight.setDeleted(true);
    }

    // Plane management methods
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

    public List<Plane> getPlanes() {
        List<Plane> out = new ArrayList<>(planes.values());
        return Collections.unmodifiableList(out);
    }

    public Plane getPlaneByID(int id) throws FlightBookingSystemException {
        if (!planes.containsKey(id)) {
            throw new FlightBookingSystemException("There is no plane with that ID.");
        }
        return planes.get(id);
    }

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
