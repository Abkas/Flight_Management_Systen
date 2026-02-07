package bcu.cmp5332.bookingsystem.commands;

import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;

public interface Command {

    public static final String HELP_MESSAGE = "Commands:\n"
        + "\tlistflights                               print all flights\n"
        + "\tlistcustomers                             print all customers\n"
        + "\taddflight                                 add a new flight\n"
        + "\taddcustomer                               add a new customer\n"
        + "\tshowflight                                show flight details\n"
        + "\tshowcustomer                              show customer details\n"
        + "\teditflight                                edit flight details\n"
        + "\tdeleteflight                              delete a flight\n"
        + "\teditcustomer                              edit customer details\n"
        + "\tdeletecustomer                            delete a customer\n"
        + "\taddbooking                                add a new booking\n"
        + "\tcancelbooking                             cancel a booking\n"
        + "\tlistbookings                              print all bookings\n"
        + "\tshowbooking                               show booking details\n"
        + "\teditbooking                               update a booking\n"
        + "\tloadgui                                   loads the GUI version of the app\n"
        + "\thelp                                      prints this help message\n"
        + "\texit                                      exits the program";

    
    public void execute(FlightBookingSystem flightBookingSystem) throws FlightBookingSystemException;
    
}
