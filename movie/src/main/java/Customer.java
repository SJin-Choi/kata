import java.util.ArrayList;
import java.util.List;

public class Customer {

    private String _name;
    private List<Rental> _rentals = new ArrayList<Rental>();

    public Customer(String name) {
        _name = name;
    }

    public void addRental(Rental arg) {
        _rentals.add(arg);
    }

    public String getName() {
        return _name;
    }

    public String statement() {
        String result = getStatementHeader();
        result += getRentalReport();
        result += getStatementFooter();
        return result;
    }

    private String getStatementFooter() {
        String result = "";
        result += "Amount owed is " + String.valueOf(getTotalAmount()) + "\n";
        result += "You earned " + String.valueOf(getFrequentRenterPoints()) + " frequent renter points";
        return result;
    }

    private String getStatementHeader() {
        return "Rental Record for " + getName() + "\n";
    }

    private String getRentalReport() {
        String result = "";
        for (Rental each : _rentals) {
            // show figures for this rental
            result += "\t" + each.getMovie().getTitle() + "\t" + String.valueOf(each.getCharge()) + "\n";
        }
        return result;
    }

    private int getFrequentRenterPoints() {
        int frequentRenterPoints = 0;
        for (Rental each : _rentals) {
            // add frequent renter points
            frequentRenterPoints += each._movie.getFrequentRenterPointsFor(each.getDaysRented());
        }
        return frequentRenterPoints;
    }

    private double getTotalAmount() {
        double totalAmount = 0;
        for (Rental each : _rentals) {
            totalAmount += each.getCharge();
        }
        return totalAmount;
    }

}
