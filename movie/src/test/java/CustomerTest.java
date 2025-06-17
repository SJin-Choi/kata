import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.CoreMatchers.*;

class CustomerTest {

    public static final String NAME = "NAME_NOT_IMPORTANT";
    public static final String TITLE = "TITLE_NOT_IMPORTANT";
    Customer customer = new Customer(NAME);

    private static Rental createRentalFor(int priceCode, int daysRented) {
        Movie movie = getMovie(priceCode);
        Rental rental = new Rental(movie, daysRented);
        return rental;
    }

    private static Movie getMovie(int priceCode) {
        switch(priceCode) {
            case Movie.REGULAR:
                return new RegularMovie(TITLE);
            case Movie.NEW_RELEASE:
                return new NewReleaseMovie(TITLE);
            case Movie.CHILDREN:
                return new ChildrenMovie(TITLE);
            default:
                return null;
        }
    }

    @Test
    public void returnNewCustomer() {
        assertThat(customer, is(notNullValue()));
    }

    @Test
    public void statementForNoRental() {
        // act

        // assert
        assertThat(customer.statement(), is("Rental Record for NAME_NOT_IMPORTANT\n"
                + "Amount owed is 0.0\n"
                + "You earned 0 frequent renter points"));
    }

    @Test
    public void statementForRegularMovieRentalForLessThan3Days() {
        // arrange
        customer.addRental(createRentalFor(Movie.REGULAR, 2));

        // act

        // assert
        assertThat(customer.statement(), is("Rental Record for NAME_NOT_IMPORTANT\n"
                + "\tTITLE_NOT_IMPORTANT\t2.0\n"
                + "Amount owed is 2.0\n"
                + "You earned 1 frequent renter points"));
    }

    @Test
    public void statementForNewReleaseMovie() {
        // arrange
        customer.addRental(createRentalFor(Movie.NEW_RELEASE, 1));

        // act

        // assert
        assertThat(customer.statement(), is("Rental Record for NAME_NOT_IMPORTANT\n"
                + "\tTITLE_NOT_IMPORTANT\t3.0\n"
                + "Amount owed is 3.0\n"
                + "You earned 1 frequent renter points"));
    }

    @Test
    public void statementForChildrenMovieRentalMoreThan3Days() {
        // arrange
        customer.addRental(createRentalFor(Movie.CHILDREN, 4));

        // act

        // assert
        assertThat(customer.statement(), is("Rental Record for NAME_NOT_IMPORTANT\n"
                + "\tTITLE_NOT_IMPORTANT\t3.0\n"
                + "Amount owed is 3.0\n"
                + "You earned 1 frequent renter points"));
    }

    @Test
    public void statementForChildrenMovieRentalMoreThan4Days() {
        // arrange
        customer.addRental(createRentalFor(Movie.CHILDREN, 3));

        // act

        // assert
        assertThat(customer.statement(), is("Rental Record for NAME_NOT_IMPORTANT\n"
                + "\tTITLE_NOT_IMPORTANT\t1.5\n"
                + "Amount owed is 1.5\n"
                + "You earned 1 frequent renter points"));
    }

    @Test
    public void statementForNewReleaseMovieRentalMoreThan1Day() {
        // arrange

        customer.addRental(createRentalFor(Movie.NEW_RELEASE, 2));

        // act

        // assert
        assertThat(customer.statement(), is("Rental Record for NAME_NOT_IMPORTANT\n"
                + "\tTITLE_NOT_IMPORTANT\t6.0\n"
                + "Amount owed is 6.0\n"
                + "You earned 2 frequent renter points"));
    }

    @Test
    public void statementForFewMovieRental() {
        // arrange
        Movie regularMovie = getMovie(Movie.REGULAR);
        Movie newReleaseMovie = getMovie(Movie.NEW_RELEASE);
        Movie childrenMovie = getMovie(Movie.CHILDREN);
        customer.addRental(new Rental(regularMovie, 1));
        customer.addRental(new Rental(newReleaseMovie, 4));
        customer.addRental(new Rental(childrenMovie, 4));

        // act

        // assert
        assertThat(customer.statement(), is("Rental Record for NAME_NOT_IMPORTANT\n"
                + "\tTITLE_NOT_IMPORTANT\t2.0\n"
                + "\tTITLE_NOT_IMPORTANT\t12.0\n"
                + "\tTITLE_NOT_IMPORTANT\t3.0\n"
                + "Amount owed is 17.0\n"
                + "You earned 4 frequent renter points"));
    }

    @Test
    public void changePriceCode(){
        Movie newReleaseMovie = getMovie(Movie.NEW_RELEASE);
        newReleaseMovie.setPriceCode(Movie.REGULAR);
        assertThat(newReleaseMovie.getPriceCode(), is(Movie.REGULAR));
    }
}
