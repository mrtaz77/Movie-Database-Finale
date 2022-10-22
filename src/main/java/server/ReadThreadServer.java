package server;

import components.Movie;
import components.ProductionCompany;
import components.StringRectifier;
import util.NetworkUtil;
import DataTransferObjects.*;

import java.io.IOException;
import java.net.Socket;
import java.util.Set;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

public class ReadThreadServer implements Runnable {
    //Networking members
    private NetworkUtil networkUtil;
    private  Server server;
    private Thread thread;
    private Socket clientSocket;
    private ConcurrentHashMap<String,NetworkUtil> companyNetworkMap;

    //Data Storage members
    private ConcurrentHashMap<String, Vector<Movie>> movieDatabase;
    private Set<String> companies;
    private ConcurrentHashMap<String,String> trailerMap;
    private ConcurrentHashMap<String,String> passwordMap;

    public ReadThreadServer(Socket clientSocket, Server server, NetworkUtil networkUtil) {
        System.out.println("In ReadThreadServer");
        this.clientSocket = clientSocket;
        this.server = server;
        this.networkUtil = networkUtil;
        movieDatabase = server.getMovieDatabase();
        movieDatabase.forEach((productionCompany,movies)->movies.forEach(Movie::new));
        companyNetworkMap = server.getCompanyNetworkMap();
        companies = server.getCompanies();
        trailerMap = server.getTrailerMap();
        passwordMap = server.getPasswordMap();
        thread = new Thread(this,"ReadThreadServer");
        thread.start();
    }

    @Override
    public void run() {
        System.out.println("In run of ReadThreadServer");
        try{
            while (true) {
                Object o = networkUtil.read();
                if(o!=null){
                    if(o instanceof LoginDTO){
                        //console check
                        System.out.println("Got loginDTO in ReadThreadServer");
                        var loginDTO = (LoginDTO)o;
                        String name = StringRectifier.rectify(loginDTO.getName());

                        //checking login info
                        boolean validity;
                        if(!companies.contains(name)){
                            System.out.println(name+" is not in database");
                            validity = false;
                        }
                        else{
                            validity = loginDTO.getPassword().equals(passwordMap.get(name));
                        }

                        //setting login info
                        loginDTO.setStatus(validity);

                        //writing loginDTO
                        try{
                            System.out.println("Writing loginDTO in ReadThreadServer");
                            networkUtil.write(loginDTO);
                            System.out.println("LoginDTO written successfully...");
                        }catch(IOException e){
                            System.out.println("Error while writing loginDTO from ReadThreadServer");
                            System.out.println(e);
                        }

                        //updates
                        if(validity){
                            //new client added
                            server.increaseClientCount();
                            companyNetworkMap.put(name,networkUtil);

                            //console check
                            System.out.println(name+" successfully connects to server...");
                            System.out.println("Current number of clients "+server.getClientCount());

                            //Production Company
                            var productionCompany = new ProductionCompany(name,movieDatabase.get(name));
                            System.out.println("Production Company in loginDTO");
                            System.out.println(productionCompany);


                            //trailers
                            ConcurrentHashMap<String,String> trailers = new ConcurrentHashMap<>();
                            for(Movie movie:productionCompany.getMovieList()){
                                String title = movie.getName();
                                trailers.put(title, trailerMap.getOrDefault(title, "Dummy"));
                            }
                            productionCompany.setTrailerList(trailers);

                            //console check for trailers
                            System.out.println("Trailers check ");
                            for(String movie:trailers.keySet()) System.out.println(movie+" : "+trailers.get(movie));

                            //Production Company DTO
                            ProductionCompanyDTO productionCompanyDTO = new ProductionCompanyDTO();
                            productionCompanyDTO.setProductionCompany(productionCompany);

                            //writing productionCompanyDTO
                            try{
                                System.out.println("Writing productionCompanyDTO in ReadThreadServer");
                                networkUtil.write(productionCompanyDTO);
                                System.out.println("Production company sent to "+name+" successfully...");
                            }catch(IOException e){
                                System.out.println("Error while writing productionCompanyDTO from ReadThreadServer");
                                System.out.println(e);
                            }
                        }
                    }
                    else if(o instanceof TransferMovieDTO){
                        System.out.println("Transfer request received...");

                        var transferMovieDTO = (TransferMovieDTO)o;
                        var oldCompany = transferMovieDTO.getOldCompany();
                        var newCompany = transferMovieDTO.getNewCompany();
                        var movie = transferMovieDTO.getMovie();

                        //console check
                        System.out.println("Transfer request received to transfer "+movie.getName()+" from "+oldCompany+" to "+newCompany);

                        if(!movieDatabase.containsKey(newCompany)){
                            System.out.println(newCompany+" not found in database");
                            transferMovieDTO.setStatus(false);
                            try{
                                System.out.println("Writing TransferMovieDTO in ReadThreadServer");
                                networkUtil.write(transferMovieDTO);
                            }catch(IOException e){
                                System.out.println("Error while writing TransferMovieDTO");
                                System.out.println(e);
                            }
                        }
                        else{
                            System.out.println(newCompany+" company is valid");

                            //update info
                            server.increaseUpdateCount();

                            movie.changeProductionCompany(newCompany);
                            movieDatabase = Movie.getCompanyMovieList();

                            //check info
                            System.out.println("Old Company info");
                            System.out.println(movieDatabase.get(oldCompany));
                            System.out.println("New Company info");
                            System.out.println(movieDatabase.get(newCompany));


                            transferMovieDTO.setStatus(true);

                            System.out.println(transferMovieDTO.getProductionCompany().getName());

                            ProductionCompany productionCompany = transferMovieDTO.getProductionCompany();
                            productionCompany.removeMovie(movie);
                            System.out.println("Production company after removal");
                            System.out.println(productionCompany);
                            transferMovieDTO.setProductionCompany(productionCompany);

                            try{
                                System.out.println("Writing transferMovieDTO");
                                networkUtil.write(transferMovieDTO);
                            }catch (IOException e){
                                System.out.println("Error while writing transferMovieDTO");
                                System.out.println(e);
                            }

                            if(companyNetworkMap.containsKey(newCompany)){
                                //Production Company
                                var newProductionCompany = new ProductionCompany(newCompany,movieDatabase.get(newCompany));
                                System.out.println("Production Company in MovieAddedDTO");
                                System.out.println(newProductionCompany);


                                //trailers
                                ConcurrentHashMap<String,String> trailers = new ConcurrentHashMap<>();
                                for(Movie temp:newProductionCompany.getMovieList()){
                                    String title = temp.getName();
                                    trailers.put(title, trailerMap.getOrDefault(title, "Dummy"));
                                }
                                productionCompany.setTrailerList(trailers);

                                //console check for trailers
                                System.out.println("Trailers check ");
                                for(String temp:trailers.keySet()) System.out.println(temp+" : "+trailers.get(temp));

                                //networking operations
                                var movieaddedDTO = new MovieAddedDTO(oldCompany,movie,newProductionCompany);
                                var newNetworkUtil = companyNetworkMap.get(newCompany);

                                try{
                                    System.out.println("Writing MovieAddedDTO...");
                                    newNetworkUtil.write(movieaddedDTO);
                                    System.out.println("MovieAddedDTO written successfully...");
                                }catch(IOException e){
                                    System.out.println("Error while writing MovieAddedDTO");
                                    System.out.println(e);
                                }
                            }
                        }
                    }
                    else if(o instanceof AddMovieDTO){
                        System.out.println("Got addMovieDTO in ReadThreadServer");
                        var addMovieDTO = (AddMovieDTO)o;

                        var productionCompany = addMovieDTO.getProductionCompany();
                        var movie = addMovieDTO.getMovie();
                        System.out.println("Add Movie request received");
                        boolean addFlag = movie.addMovie();
                        if(addFlag){
                            System.out.println("Addition successfull...");
                            server.increaseUpdateCount();
                            addMovieDTO.setStatus(true);

                            productionCompany.addMovie(movie);

                            //console check
                            System.out.println(productionCompany);

                            //trailers
                            ConcurrentHashMap<String,String> trailers = new ConcurrentHashMap<>();
                            for(Movie temp:productionCompany.getMovieList()){
                                String title = temp.getName();
                                trailers.put(title, trailerMap.getOrDefault(title, "Dummy"));
                            }
                            productionCompany.setTrailerList(trailers);

                            //console check for trailers
                            System.out.println("Trailers check ");
                            for(String temp:trailers.keySet()) System.out.println(temp+" : "+trailers.get(temp));

                            addMovieDTO.setProductionCompany(productionCompany);
                        }
                        else{
                            System.out.println("Movie name is not unique");
                            addMovieDTO.setStatus(false);
                        }

                        try{
                            System.out.println("Writing AddMovieDTO");
                            networkUtil.write(addMovieDTO);
                        }catch(IOException e){
                            System.out.println("Caught exception in AddMovieDTO");
                            System.out.println(e);
                        }
                    }
                    else if(o instanceof PasswordChangeDTO){
                        server.increaseUpdateCount();
                        System.out.println("Password change request received ReadThreadServer");

                        var passwordChangeDTO = (PasswordChangeDTO)o;
                        var name = passwordChangeDTO.getName();
                        var oldPassword = passwordChangeDTO.getOldPassword();
                        var newPassword = passwordChangeDTO.getNewPassword();

                        if(!passwordMap.get(name).equals(oldPassword)){
                            passwordChangeDTO.setStatus(false);
                        }
                        else{
                            passwordChangeDTO.setStatus(true);
                            passwordMap.put(name,newPassword);
                            server.increaseUpdateCount();
                            System.out.println("Password changed successfully");
                        }

                        try{
                            System.out.println("Writing PasswordChangeDTO");
                            networkUtil.write(passwordChangeDTO);
                        }catch(IOException e){
                            System.out.println("Error while writing passwordChangeDTO");
                            System.out.println(e);
                        }
                    }
                    else if(o instanceof LogoutDTO){
                        server.decreaseClientCount();
                        System.out.println("Current clientCount : "+server.getClientCount());
                        var logoutDTO = (LogoutDTO)o;
                        System.out.println(logoutDTO.getName()+" logs out...");
                        companyNetworkMap.remove(logoutDTO.getName());
                    }
                }
            }
        }catch(IOException | ClassNotFoundException e){
            System.out.println("Caught exception in ReadThreadServer");
            System.out.println(e);
        }finally{
            try{
                System.out.println("Closing connection from ReadThreadServer...");
                networkUtil.closeConnection();
            }catch(IOException e){
                System.out.println("Exception while closing from ReadThreadServer");
                System.out.println(e);
            }
        }
    }
}
