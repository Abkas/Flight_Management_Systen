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

            String[] parts = line.split(SEPARATOR);
            if (parts.length >= 2) {
                int id = Integer.parseInt(parts[0]);
                String name = parts[1];
                // Use phone if available, otherwise empty string
                String phone = parts.length >= 3 ? parts[2] : "";
                
                Customer customer = new Customer(id, name, phone);
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

        for(Customer c: fbs.getCustomers()){
            out.print(c.getId() + SEPARATOR + c.getName() + SEPARATOR + c.getPhone());
            out.println();
        }
        out.close();
    }
}
