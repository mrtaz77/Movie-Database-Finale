package server;

import components.Movie;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

public class FileWriteThreadServer implements Runnable{
    //Runtime file update
    private static final String OUTPUT_FILE_NAME = "movies.txt";
    private static final String PASSWORD_FILE_NAME = "passwords.txt";
    private Server server;
    private Thread thread;
    private ConcurrentHashMap<String, Vector<Movie>> movieDatabase;
    private ConcurrentHashMap<String,String> passwordMap;
    int updateCount = 0;

    public FileWriteThreadServer(Server server) {
        System.out.println("In FileWriteThreadServer");
        this.server = server;
        movieDatabase = server.getMovieDatabase();
        passwordMap = server.getPasswordMap();
        thread = new Thread(this,"FileWriteThreadServer");
        thread.start();
    }

    @Override
    public void run() {
        System.out.println("In run of FileWriteThreadServer");
        while(true){
            //console check
            System.out.println("Checking for updates by clients...");
            System.out.println("Current Update Count : "+server.getUpdateCount());

            //update check
            if(updateCount<server.getUpdateCount()){
                updateCount = server.getUpdateCount();

                //File Output
                //movies.txt
                Thread movieWriteThread = new Thread(this::writeMovies);
                Thread passwordWriteThread = new Thread(this::writePasswords);

                movieWriteThread.start();
                passwordWriteThread.start();
                try{
                    System.out.println("Joining file output threads");
                    movieWriteThread.join();
                    passwordWriteThread.join();
                }catch (InterruptedException e){
                    System.out.println("Interrupted while writing files");
                    System.out.println(e);
                }
                System.out.println("Updates written to files from FileWriteThreadServer successfully...");
            }
        }

    }

    private void writePasswords() {
        System.out.println("Writing movies to file...");
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(PASSWORD_FILE_NAME))){
            for(String productionCompany:passwordMap.keySet()){
                String password = productionCompany+","+passwordMap.get(productionCompany);
                try{
                    writer.write(password);
                    writer.write(System.lineSeparator());
                }catch(IOException e){
                    System.out.println("Error while writing passwords to "+PASSWORD_FILE_NAME);
                    System.out.println(e);
                }
            }
        }catch (IOException e){
            System.out.println(OUTPUT_FILE_NAME+" cannot be opened");
            System.out.println(e);
        }
    }

    private void writeMovies() {
        System.out.println("Writing movies to file...");
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(OUTPUT_FILE_NAME))){
            for(String productionCompany:movieDatabase.keySet()){
                movieDatabase.get(productionCompany).forEach((temp)->{
                    String movie=temp.getName()+","+temp.getReleaseYear()+","+temp.getGenre1()+","+temp.getGenre2()+","+temp.getGenre3()+","+temp.getRunningTime()+","+temp.getProductionCompany()+","+temp.getBudget()+","+temp.getRevenue();
                    try {
                        writer.write(movie);
                        writer.write(System.lineSeparator());
                    } catch (IOException e) {
                        System.out.println("Error while writing movies to "+OUTPUT_FILE_NAME);
                        System.out.println(e);
                    }
                });
            }
        }catch(IOException e){
            System.out.println(OUTPUT_FILE_NAME+" cannot be opened");
            System.out.println(e);
        }finally{
            System.out.println("Movies written to "+OUTPUT_FILE_NAME+" from FileWriteThreadServer successfully...");
        }
    }
}
