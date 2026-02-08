package bcu.cmp5332.bookingsystem.data;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Scanner;

import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;
import bcu.cmp5332.bookingsystem.model.Customer;

import java.io.IOException;

public class CustomerDataManager implements DataManager {

    private final String RESOURCE = "./resources/data/customers.txt";
    
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

            String[] parts = line.split(SEPARATOR, -1);
            if (parts.length >= 2) {
                int id = Integer.parseInt(parts[0]);
                String name = parts[1];
                // Use phone if available, otherwise empty string
                String phone = parts.length >= 3 ? parts[2] : "";
                String gender = parts.length >= 4 ? parts[3] : "Unknown";
                int age = (parts.length >= 5 && !parts[4].isEmpty()) ? Integer.parseInt(parts[4]) : 0;
                String email = parts.length >= 6 ? parts[5] : "";
                
                Customer customer = new Customer(id, name, phone, gender, age, email);
                
                // Load deleted flag (7th field)
                if (parts.length >= 7 && !parts[6].isEmpty()) {
                    try {
                        boolean deleted = Boolean.parseBoolean(parts[6]);
                        customer.setDeleted(deleted);
                    } catch (Exception ex) {
                        System.err.println("Warning: Could not parse deleted flag for customer " + id);
                    }
                }
                
                fbs.addCustomer(customer);
            }
        }
        sc.close();
    }

    @Override
    public void storeData(FlightBookingSystem fbs) throws IOException {
        // TODO: implementation here
        FileWriter fw = new FileWriter(RESOURCE);
        PrintWriter out = new PrintWriter(fw);

        // Store all customers including deleted ones
        for(Customer c: fbs.getAllCustomers()){
            out.print(c.getId() + SEPARATOR + c.getName() + SEPARATOR + c.getPhone() 
                + SEPARATOR + c.getGender() + SEPARATOR + c.getAge() + SEPARATOR + c.getEmail()
                + SEPARATOR + c.isDeleted());
            out.println();
        }
        out.close();
    }
}
