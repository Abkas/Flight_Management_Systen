package bcu.cmp5332.bookingsystem.model;

public class Plane {
    
    private int id;
    private String model;
    private String registrationNumber;
    
    // Seating capacities
    private int economyRows;
    private int economyColumns;
    private int businessRows;
    private int businessColumns;
    private int firstRows;
    private int firstColumns;

    public Plane(int id, String model, String registrationNumber,
                 int economyRows, int economyColumns, 
                 int businessRows, int businessColumns, 
                 int firstRows, int firstColumns) {
        this.id = id;
        this.model = model;
        this.registrationNumber = registrationNumber;
        this.economyRows = economyRows;
        this.economyColumns = economyColumns;
        this.businessRows = businessRows;
        this.businessColumns = businessColumns;
        this.firstRows = firstRows;
        this.firstColumns = firstColumns;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public int getEconomyRows() {
        return economyRows;
    }

    public void setEconomyRows(int economyRows) {
        this.economyRows = economyRows;
    }

    public int getEconomyColumns() {
        return economyColumns;
    }

    public void setEconomyColumns(int economyColumns) {
        this.economyColumns = economyColumns;
    }

    public int getBusinessRows() {
        return businessRows;
    }

    public void setBusinessRows(int businessRows) {
        this.businessRows = businessRows;
    }

    public int getBusinessColumns() {
        return businessColumns;
    }

    public void setBusinessColumns(int businessColumns) {
        this.businessColumns = businessColumns;
    }

    public int getFirstRows() {
        return firstRows;
    }

    public void setFirstRows(int firstRows) {
        this.firstRows = firstRows;
    }

    public int getFirstColumns() {
        return firstColumns;
    }

    public void setFirstColumns(int firstColumns) {
        this.firstColumns = firstColumns;
    }

    public int getCapacity(BookingClass bookingClass) {
        switch (bookingClass) {
            case ECONOMY:
                return economyRows * economyColumns;
            case BUSINESS:
                return businessRows * businessColumns;
            case FIRST:
                return firstRows * firstColumns;
            default:
                return 0;
        }
    }

    public int getTotalCapacity() {
        return getCapacity(BookingClass.ECONOMY) 
             + getCapacity(BookingClass.BUSINESS) 
             + getCapacity(BookingClass.FIRST);
    }

    public int getRows(BookingClass bookingClass) {
        switch (bookingClass) {
            case ECONOMY:
                return economyRows;
            case BUSINESS:
                return businessRows;
            case FIRST:
                return firstRows;
            default:
                return 0;
        }
    }

    public int getColumns(BookingClass bookingClass) {
        switch (bookingClass) {
            case ECONOMY:
                return economyColumns;
            case BUSINESS:
                return businessColumns;
            case FIRST:
                return firstColumns;
            default:
                return 0;
        }
    }

    public String getDetailsShort() {
        return "Plane #" + id + " - " + model + " (" + registrationNumber + ") - Capacity: " + getTotalCapacity();
    }

    public String getDetailsLong() {
        return "---------------------------\n"
             + "Plane ID: " + id + "\n"
             + "Model: " + model + "\n"
             + "Registration: " + registrationNumber + "\n"
             + "Seating Configuration:\n"
             + "  Economy: " + economyRows + "x" + economyColumns + " (" + getCapacity(BookingClass.ECONOMY) + " seats)\n"
             + "  Business: " + businessRows + "x" + businessColumns + " (" + getCapacity(BookingClass.BUSINESS) + " seats)\n"
             + "  First: " + firstRows + "x" + firstColumns + " (" + getCapacity(BookingClass.FIRST) + " seats)\n"
             + "Total Capacity: " + getTotalCapacity() + " seats\n"
             + "---------------------------";
    }

    @Override
    public String toString() {
        return getDetailsShort();
    }
}
