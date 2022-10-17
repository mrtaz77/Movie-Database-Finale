package components;

import java.util.Comparator;

public class MovieComparator implements Comparator<Movie> {
    @Override
    public int compare(Movie a, Movie b) {
        if(a.getReleaseYear()==b.getReleaseYear())return a.getRunningTime()-b.getRunningTime();
        else return a.getReleaseYear()- b.getReleaseYear();
    }
}
