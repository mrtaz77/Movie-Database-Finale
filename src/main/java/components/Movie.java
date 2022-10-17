package components;

import java.util.ArrayList;
import java.util.HashMap;
import java.io.Serializable;
import java.util.List;
import java.util.Set;

public class Movie implements Serializable {
    //members
    private String name,productionCompany;
    private String genre1,genre2,genre3;
    private int releaseYear,runningTime;
    private long budget,revenue;

    //static members
    private static final HashMap<String,List<Movie>> companyMovieList = new HashMap<>();

    //methods

    //constructor
    public Movie(String name,int releaseYear,String genre1,String genre2,String genre3,int runningTime, String productionCompany,long budget,long revenue){
        this.name=name;
        this.productionCompany=productionCompany;
        this.genre1=genre1;
        this.genre2=genre2;
        this.genre3=genre3;
        this.releaseYear=releaseYear;
        this.runningTime=runningTime;
        this.budget=budget;
        this.revenue=revenue;
        if(companyMovieList.containsKey(productionCompany))companyMovieList.get(productionCompany).add(this);
        else {
            List<Movie> movies = new ArrayList<>();
            movies.add(this);
            companyMovieList.put(productionCompany,movies);
        }
    }

    public Movie(Movie movie){
        if(companyMovieList.containsKey(productionCompany))companyMovieList.get(productionCompany).add(this);
        else {
            List<Movie> movies = new ArrayList<>();
            movies.add(this);
            companyMovieList.put(productionCompany,movies);
        }
    }

    //set-methods
    public void setName(String name){this.name=name;}
    public void setProductionCompany(String productionCompany){this.productionCompany=productionCompany;}
    public void setReleaseYear(int releaseYear){this.releaseYear=releaseYear;}
    public void setRunningTime(int runningTime){this.runningTime=runningTime;}
    public void setBudget(long budget){this.budget=budget;}
    public void setRevenue(long revenue){this.revenue=revenue;}
    public void setGenre1(String genre){this.genre1=genre;}
    public void setGenre2(String genre){this.genre2=genre;}
    public void setGenre3(String genre){this.genre3=genre;}

    //get-methods
    public String getName(){return name;}
    public String getProductionCompany(){return productionCompany;}
    public int getReleaseYear(){return releaseYear;}
    public int getRunningTime(){return runningTime;}
    public long getBudget(){return budget;}
    public long getRevenue (){return revenue;}
    public String getGenre1(){return genre1;}
    public String getGenre2(){return genre2;}
    public String getGenre3(){return genre3;}
    public static HashMap<String,List<Movie>> getCompanyMovieList(){return companyMovieList;}

    public void showMovieDetails(){
        System.out.println("Title : "+name);
        System.out.println("ReleaseYear : "+releaseYear);
        System.out.print("Genre   : "+genre1);
        if(!genre2.equals(""))System.out.print(", "+genre2);
        if(!genre3.equals(""))System.out.print(", "+genre3);
        System.out.println(".");
        System.out.println("Running Time : "+runningTime+" minutes");
        System.out.println("Production Company : "+productionCompany);
        System.out.println("Budget : "+budget);
        System.out.println("Revenue : "+revenue);
    }

    public long getProfit(){return revenue-budget;}

    public static boolean isName(String name){
        for(String productionCompany : companyMovieList.keySet()){
            for(Movie movie : companyMovieList.get(productionCompany))if(movie.getName().equalsIgnoreCase(name))return true;
        }
        return false;
    }

    public static boolean isGenre(String genre){
        for(String productionCompany : companyMovieList.keySet()) {
            for (Movie movie : companyMovieList.get(productionCompany)) {
                if (movie.getGenre1().equalsIgnoreCase(genre)||movie.getGenre2().equalsIgnoreCase(genre)||movie.getGenre3().equalsIgnoreCase(genre)) return true;
            }
        }
        return false;
    }

    public static boolean isReleaseYear(int year){
        for(String productionCompany : companyMovieList.keySet()){
            for(Movie movie : companyMovieList.get(productionCompany))if(movie.getReleaseYear()==year)return true;
        }
        return false;
    }

    public static boolean isProductionCompany(String productionCompany){
        for(String company : companyMovieList.keySet()){
            if(company.equalsIgnoreCase(productionCompany))return true;
        }
        return false;
    }

    public static boolean isRunningTimeInRange(int low,int high){
        for(String productionCompany : companyMovieList.keySet()) {
            for (Movie movie : companyMovieList.get(productionCompany)) {
                if (movie.getRunningTime()>=low && movie.getRunningTime()<=high )return true;
            }
        }
        return false;
    }

    public static Movie getMovieByName(String name){
        for(String productionCompany : companyMovieList.keySet()){
            for(Movie movie : companyMovieList.get(productionCompany))if(movie.getName().equalsIgnoreCase(name))return movie;
        }
        return null;
    }

    public static List<Movie> getMovieByYear(int year){
        List<Movie>movies = new ArrayList<>();
        for(String productionCompany : companyMovieList.keySet()){
            for(Movie movie : companyMovieList.get(productionCompany))if(movie.getReleaseYear()==year)movies.add(movie);
        }
        return movies;
    }

    public static List<Movie> getMovieByGenre(String genre){
        List<Movie>movies = new ArrayList<>();
        for(String productionCompany : companyMovieList.keySet()) {
            for (Movie movie : companyMovieList.get(productionCompany)) {
                if (movie.getGenre1().equalsIgnoreCase(genre)||movie.getGenre2().equalsIgnoreCase(genre)||movie.getGenre3().equalsIgnoreCase(genre)) movies.add(movie);
            }
        }
        return movies;
    }

    public static List<Movie> getMovieByProductionCompany(String productionCompany){
        return companyMovieList.get(StringRectifier.rectify(productionCompany));
    }

    public static List<Movie> getMovieByRunningTime(int low,int high){
        List<Movie>movies = new ArrayList<>();
        for(String productionCompany : companyMovieList.keySet()) {
            for (Movie movie : companyMovieList.get(productionCompany)) {
                if (movie.getRunningTime()>=low && movie.getRunningTime()<=high )movies.add(movie);
            }
        }
        return movies;
    }

    public static int getMovieCount(String productionCompany){
        if(!isProductionCompany(productionCompany) && companyMovieList.get(productionCompany)==null)return 0;
        else return companyMovieList.get(productionCompany).size();
    }

    public static List<Movie> top10Movies(){
        List<Movie>movieList = new ArrayList<>();
        for(String productionCompany : companyMovieList.keySet()) {
            movieList.addAll(companyMovieList.get(productionCompany));
        }
        for(int i=0;i<10;i++){
            for(int j=movieList.size()-1;j>i;j--){
                if(movieList.get(j).getProfit()>movieList.get(j-1).getProfit()){
                    Movie temp=movieList.get(j);
                    movieList.set(j,movieList.get(j-1));
                    movieList.set(j-1,temp);
                }
            }
        }
        return movieList.subList(0,9);
    }

    public static List<Movie> getMostRecentMovies(String productionCompany){
        List<Movie> movies=Movie.getMovieByProductionCompany(productionCompany);
        int latestYear=0;
        for (Movie movie : movies) {
            if (latestYear < movie.getReleaseYear()) latestYear = movie.getReleaseYear();
        }
        return Movie.getMovieByYear(latestYear);
    }

    public static List<Movie> getMaxRevenue(String productionCompany){
        List<Movie> movies = Movie.getMovieByProductionCompany(productionCompany);
        List<Movie> maxRevenueMovies = new ArrayList<>();
        long maxRevenue=0;
        for (Movie movie : movies) {
            if (maxRevenue < movie.getRevenue()) maxRevenue = movie.getRevenue();
        }
        for(Movie m:movies){
            if(m.getRevenue()==maxRevenue)maxRevenueMovies.add(m);
        }
        return maxRevenueMovies;
    }

    public static long getTotalProfit(String productionCompany){
        long totalProfit=0;
        List<Movie> movies = Movie.getMovieByProductionCompany(productionCompany);
        for(Movie m:movies){
            totalProfit+=m.getProfit();
        }
        return totalProfit;
    }

    public static Set<String> getProductionCompanies(){
        return companyMovieList.keySet();
    }

    public synchronized boolean addMovie(){
        if(isName(name))return false;
        else{
            if(isProductionCompany(productionCompany)){
                getMovieByProductionCompany(productionCompany).add(this);
            }
            else {
                List<Movie> movies = new ArrayList<>();
                movies.add(this);
                companyMovieList.put(productionCompany,movies);
            }
            return true;
        }
    }

    public synchronized void changeProductionCompany(String newProductionCompany){
        List<Movie> oldCompanyMovies = getMovieByProductionCompany(this.productionCompany);
        System.out.println(oldCompanyMovies.size());
        System.out.println("Current movie name : "+this.name);
        oldCompanyMovies.remove(this);
        System.out.println("Size after deletion : "+oldCompanyMovies.size());
        oldCompanyMovies.forEach((movie)->System.out.println(movie.name));
        this.setProductionCompany(newProductionCompany);
        System.out.println("Changed production company : "+this.productionCompany);
        List<Movie> newCompanyMovies = getMovieByProductionCompany(newProductionCompany);
        System.out.println("Old size : "+newCompanyMovies.size());
        newCompanyMovies.add(this);
        System.out.println("New size : "+newCompanyMovies.size());
        newCompanyMovies.forEach((movie)->System.out.println(movie.name));
        System.out.println("Movie added successfully");
    }
}
