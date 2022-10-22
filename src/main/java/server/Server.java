package server;

import components.Movie;
import util.NetworkUtil;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;



public class Server {
    //Networking members
    ServerSocket serverSocket;
    private ConcurrentHashMap<String,NetworkUtil> companyNetworkMap;

    //Status Info members
    private volatile int clientCount;
    private volatile int updateCount;

    //Database members
    private ConcurrentHashMap<String,Vector<Movie>> movieDatabase;
    private Set<String> companies;
    private ConcurrentHashMap<String,String> trailerMap;
    private ConcurrentHashMap<String,String> passwordMap;

    //File Operation members
    private static final String INPUT_FILE_NAME = "movies.txt";
    private static final String PASSWORD_FILE_NAME = "passwords.txt";
    private static final String TRAILER_FILE_NAME = "trailers.txt";


    //synchronized changes of info members
    public synchronized void increaseClientCount() {clientCount++;}
    public synchronized void decreaseClientCount() {clientCount--;}
    public synchronized void increaseUpdateCount() {updateCount++;}

    public ConcurrentHashMap<String, NetworkUtil> getCompanyNetworkMap() {
        return companyNetworkMap;
    }

    public int getClientCount() {
        return clientCount;
    }

    public int getUpdateCount() {
        return updateCount;
    }

    public ConcurrentHashMap<String, Vector<Movie>> getMovieDatabase() {
        return movieDatabase;
    }

    public Set<String> getCompanies() {
        return companies;
    }

    public ConcurrentHashMap<String, String> getTrailerMap(){
        return trailerMap;
    }

    public ConcurrentHashMap<String, String> getPasswordMap() {
        return passwordMap;
    }

    Server(){
        //initialize info
        clientCount = 0;
        updateCount = 0;

        trailerMap = new ConcurrentHashMap<>();
        passwordMap = new ConcurrentHashMap<>();
        //File Inputs
        System.out.println("Starting File I/O");
        //movies.txt
        Thread movieReadThread = new Thread(this::readMovies);
        //passwords.txt
        Thread passwordReadThread = new Thread(this::readPasswords);
        //trailers.txt
        Thread trailerReadThread = new Thread(this::readTrailers);

        movieReadThread.start();
        passwordReadThread.start();
        trailerReadThread.start();

        try{
            System.out.println("Joining threads");
            movieReadThread.join();
            passwordReadThread.join();
            trailerReadThread.join();
        } catch (InterruptedException e) {
            System.out.println("Interrupted in Server while reading file");
            System.out.println(e);
        }

        //networking operations
        companyNetworkMap = new ConcurrentHashMap<>();

        new FileWriteThreadServer(this);
        new InputThreadServer(this);

        //Server connection
        try{
            serverSocket = new ServerSocket(44444);
            System.out.println("Server is waiting...");
            while(true){
                Socket clientSocket = serverSocket.accept();
                System.out.println("Server accepts a client");
                NetworkUtil networkUtil = new NetworkUtil(clientSocket);
                new ReadThreadServer(clientSocket,this,networkUtil);
            }
        }catch (IOException e){
            System.out.println("Server starts... "+e);
        }
    }




    private void readMovies() {
        System.out.println("Reading movies in server");
        try (BufferedReader reader = new BufferedReader(new FileReader(INPUT_FILE_NAME))) {
            String line = null;
            while (true) {
                try {
                    line = reader.readLine();
                } catch (IOException e) {
                    System.out.println("Error while reading from movies.txt");
                    System.out.println(e);
                }
                if (line == null) break;
                String[] out = line.split(",");
                new Movie(out[0], Integer.parseInt(out[1], 10), out[2], out[3], out[4], Integer.parseInt(out[5], 10), out[6], Long.parseLong(out[7], 10), Long.parseLong(out[8], 10));
            }
        } catch (IOException e) {
            System.out.println(INPUT_FILE_NAME+" not found");
            System.out.println(e);
        }
        movieDatabase = Movie.getCompanyMovieList();
        companies = Movie.getProductionCompanies();
        System.out.println("Number of companies : "+companies.size());
        System.out.println("Number of movies : "+movieDatabase.entrySet().size());
        System.out.println("Movies loaded successfully...");
    }

    private void readPasswords()  {
        System.out.println("Reading passwords in server");
        try(BufferedReader reader = new BufferedReader(new FileReader(PASSWORD_FILE_NAME))){
            String line;
            while(true){
                line = reader.readLine();
                if(line == null)break;
                String[]password = line.split(",");
                passwordMap.put(password[0],password[1]);
            }
        }catch (IOException e) {
            System.out.println(PASSWORD_FILE_NAME+" not found");
            System.out.println(e);
        }
        System.out.println("Number of companies from password : "+passwordMap.keySet().size());
        System.out.println("Passwords loaded successfully...");
    }

    private void readTrailers() {
        System.out.println("Reading trailers in server");
        try(BufferedReader reader = new BufferedReader(new FileReader(TRAILER_FILE_NAME))){
            String line;
            while(true){
                line = reader.readLine();
                if(line == null)break;
                String[]trailer = line.split(",");
                trailerMap.put(trailer[0],trailer[1]);
            }
        }catch (IOException e) {
            System.out.println(TRAILER_FILE_NAME+" not found");
            System.out.println(e);
        }
        System.out.println("Number of movies from trailers : "+trailerMap.keySet().size());
        System.out.println("Trailers loaded successfully...");
    }


    public static void main(String[] args) {
        System.out.println("In main of Server");
        new Server();
    }
}
