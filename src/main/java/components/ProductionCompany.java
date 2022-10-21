package components;

import java.io.Serializable;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

public class ProductionCompany implements Serializable {
    private String name;
    private int movieCount;
    private long profit;
    private Vector<Movie> movieList = new Vector<>();
    private ConcurrentHashMap<String,String> trailerList = new ConcurrentHashMap<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMovieCount() {
        return movieCount;
    }

    public void setMovieCount(int movieCount) {
        this.movieCount = movieCount;
    }

    public long getProfit() {
        return profit;
    }

    public void setProfit(long profit) {
        this.profit = profit;
    }

    public Vector<Movie> getMovieList() {
        return movieList;
    }

    public void setMovieList(Vector<Movie> movieList) {
        this.movieList = movieList;
    }

    public ConcurrentHashMap<String, String> getTrailerList() {
        return trailerList;
    }

    public void setTrailerList(ConcurrentHashMap<String, String> trailerList) {
        this.trailerList = trailerList;
    }

    public void incrementMovieCount() { movieCount++;}

    public ProductionCompany(String name, Vector<Movie> movieList) {
        this.name = name;
        this.movieList = movieList;
        this.movieCount = movieList.size();
        movieList.forEach((movie ->profit+=movie.getProfit()));
    }

    public ProductionCompany(String name, int movieCount) {
        this.name = StringRectifier.rectify(name);
        this.movieCount = movieCount;
    }

    @Override
    public  String toString(){
        return name+" owns "+movieCount+" movies and has profit $"+profit;
    }

    public void addMovie(Movie movie){
        movie.addMovie();
        movieList.addElement(movie);
        incrementMovieCount();
        profit+=movie.getProfit();

        //console check
        System.out.println(this);
    }

    public boolean removeMovie(Movie movie){
        Movie target = null;
        for(Movie m : movieList){
            if(m.getName().equalsIgnoreCase(movie.getName()))target = m;
        }
        //console check
        System.out.println("Before removing "+movie.getName()+" from "+this.name);
        System.out.println(this);

        if(target == null){return false;}
        else movieList.removeElement(target);
        movieCount--;
        profit-=target.getProfit();

        //console check
        System.out.println("After removing "+movie.getName()+" from "+this.name);
        System.out.println(this);

        return true;
    }
}
