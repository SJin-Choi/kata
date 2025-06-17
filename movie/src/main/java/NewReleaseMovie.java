public class NewReleaseMovie extends Movie {
    public NewReleaseMovie(String title) {
        super(title, Movie.NEW_RELEASE);
    }
    @Override
    public double getChargeFor(int daysRented) {
        return daysRented * 3;
    }

    @Override
    public int getFrequentRenterPointsFor(int daysRented) {
        if (daysRented > 1) {
            return 2;
        } else {
            return 1;
        }
    }
}
