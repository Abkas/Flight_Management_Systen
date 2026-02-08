package bcu.cmp5332.bookingsystem.model;

/**
 * Represents an aircraft used by flights,
 * with seat layout for each travel class.
 *
 * @author Abhis
 */
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

    /**
     * Creates a new plane with the given seating configuration.
     *
     * @param id                 the unique id of the plane
     * @param model              the plane model name
     * @param registrationNumber the plane registration code
     * @param economyRows        number of rows in economy
     * @param economyColumns     number of seats per row in economy
     * @param businessRows       number of rows in business
     * @param businessColumns    number of seats per row in business
     * @param firstRows          number of rows in first class
     * @param firstColumns       number of seats per row in first class
     */
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

    /**
     * Gets the id of this plane.
     *
     * @return the plane id
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the id of this plane.
     *
     * @param id the new id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Gets the model name of the plane.
     *
     * @return the model
     */
    public String getModel() {
        return model;
    }

    /**
     * Sets the model name of the plane.
     *
     * @param model the new model name
     */
    public void setModel(String model) {
        this.model = model;
    }

    /**
     * Gets the registration number of the plane.
     *
     * @return the registration number
     */
    public String getRegistrationNumber() {
        return registrationNumber;
    }

    /**
     * Sets the registration number of the plane.
     *
     * @param registrationNumber the new registration code
     */
    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    /**
     * Gets the number of rows in economy class.
     *
     * @return economy rows
     */
    public int getEconomyRows() {
        return economyRows;
    }

    /**
     * Sets the number of rows in economy class.
     *
     * @param economyRows the new number of rows
     */
    public void setEconomyRows(int economyRows) {
        this.economyRows = economyRows;
    }

    /**
     * Gets the number of seats per row in economy class.
     *
     * @return economy columns
     */
    public int getEconomyColumns() {
        return economyColumns;
    }

    /**
     * Sets the number of seats per row in economy class.
     *
     * @param economyColumns the new number of seats per row
     */
    public void setEconomyColumns(int economyColumns) {
        this.economyColumns = economyColumns;
    }

    /**
     * Gets the number of rows in business class.
     *
     * @return business rows
     */
    public int getBusinessRows() {
        return businessRows;
    }

    /**
     * Sets the number of rows in business class.
     *
     * @param businessRows the new number of rows
     */
    public void setBusinessRows(int businessRows) {
        this.businessRows = businessRows;
    }

    /**
     * Gets the number of seats per row in business class.
     *
     * @return business columns
     */
    public int getBusinessColumns() {
        return businessColumns;
    }

    /**
     * Sets the number of seats per row in business class.
     *
     * @param businessColumns the new number of seats per row
     */
    public void setBusinessColumns(int businessColumns) {
        this.businessColumns = businessColumns;
    }

    /**
     * Gets the number of rows in first class.
     *
     * @return first class rows
     */
    public int getFirstRows() {
        return firstRows;
    }

    /**
     * Sets the number of rows in first class.
     *
     * @param firstRows the new number of rows
     */
    public void setFirstRows(int firstRows) {
        this.firstRows = firstRows;
    }

    /**
     * Gets the number of seats per row in first class.
     *
     * @return first class columns
     */
    public int getFirstColumns() {
        return firstColumns;
    }

    /**
     * Sets the number of seats per row in first class.
     *
     * @param firstColumns the new number of seats per row
     */
    public void setFirstColumns(int firstColumns) {
        this.firstColumns = firstColumns;
    }

    /**
     * Gets the seating capacity for the given booking class.
     *
     * @param bookingClass the booking class
     * @return the capacity for that class
     */
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

    /**
     * Gets the total number of seats across all classes.
     *
     * @return total capacity
     */
    public int getTotalCapacity() {
        return getCapacity(BookingClass.ECONOMY) 
             + getCapacity(BookingClass.BUSINESS) 
             + getCapacity(BookingClass.FIRST);
    }

    /**
     * Gets the number of rows for the given booking class.
     *
     * @param bookingClass the booking class
     * @return number of rows
     */
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

    /**
     * Gets the number of seat columns for the given class.
     *
     * @param bookingClass the booking class
     * @return number of columns
     */
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

    /**
     * Gets a short one-line description of the plane.
     *
     * @return short details string
     */
    public String getDetailsShort() {
        return "Plane #" + id + " - " + model + " (" + registrationNumber + ") - Capacity: " + getTotalCapacity();
    }

    /**
     * Gets a long, multi-line description of the plane,
     * including seating configuration and capacities.
     *
     * @return long details string
     */
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
