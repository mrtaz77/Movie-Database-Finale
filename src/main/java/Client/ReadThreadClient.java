package client;

import DataTransferObjects.*;
import javafx.application.Platform;

import java.io.IOException;

public class ReadThreadClient implements Runnable{
    private final Thread thread;
    private final Main main;

    public ReadThreadClient(Main main) {
        System.out.println("In ReadThreadClient");
        this.main = main;
        thread = new Thread(this, "client");
        thread.start();
    }

    @Override
    public void run() {
        try{
            while(true){
                Object o = main.getNetworkUtil().read();
                if(o!=null){
                    //loginDTO
                    if(o instanceof LoginDTO){
                        var loginDTO = (LoginDTO)o;
                        System.out.println("Getting "+loginDTO.getClass().getName()+" "+getClass().getName());
                        System.out.println("Production company trying to login "+loginDTO.getName());
                        System.out.println(loginDTO.isStatus());

                        //the following updates javafx ui for user created threads
                        Platform.runLater(()->{
                            if(loginDTO.isStatus())System.out.println(loginDTO.getName()+" logged in successfully...");
                            else main.showAlert();
                        });
                    }
                    //logoutDTO
                    else if(o instanceof LogoutDTO){}
                    //productionCompanyDTO
                    else if(o instanceof ProductionCompanyDTO){
                        var productionCompanyDTO = (ProductionCompanyDTO)o;
                        var productionCompany = productionCompanyDTO.getProductionCompany();

                        System.out.println(productionCompany.getName()+" received");
                        System.out.println(productionCompany);

                        Platform.runLater(()->{
                            try{
                                main.showMenuPage(productionCompany,"Home");
                            }catch(Exception e){
                                System.out.println("Exception while showing MenuPage of "+productionCompany.getName());
                                System.out.println(e);
                            }
                        });
                    }
                    //addMovieDTO
                    else if(o instanceof AddMovieDTO){}
                    //transferMovieDTO
                    else if(o instanceof TransferMovieDTO){}
                    //password change DTO
                    else if(o instanceof PasswordChangeDTO){}
                    //movie added DTO
                    else if(o instanceof MovieAddedDTO){}
                    //stop DTO
                    else if(o instanceof StopDTO){}
                }
            }
        }catch(Exception e){
            System.out.println("Exception in ReadThreadClient run while reading object from networkUtil");
            System.out.println(e);
        }finally{
            try{
                System.out.println("Closing from ReadThreadClient...");
                main.getNetworkUtil().closeConnection();
            }catch(IOException e){
                System.out.println("Exception while trying to close from ReadThreadClient");
                System.out.println(e);
            }
        }
    }
}
