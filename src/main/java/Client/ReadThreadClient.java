package Client;

import DataTransferObjects.*;
import javafx.application.Platform;

import java.io.IOException;

public class ReadThreadClient implements Runnable{
    private final Thread thread;
    private final Main main;

    public ReadThreadClient(Main main) {
        System.out.println("In ReadThreadClient");
        this.main = main;
        thread = new Thread(this,"Client");
        thread.start();
    }

    @Override
    public void run() {
        try{
            while(true){
                Object o = main.getNetworkUtil().read();
                if(o!=null){
                    //loginDTO
                    if(o instanceof LoginDTO){}
                    //logoutDTO
                    if(o instanceof LogoutDTO){}
                    //productionCompanyDTO
                    if(o instanceof ProductionCompanyDTO){}
                    //addMovieDTO
                    if(o instanceof AddMovieDTO){}
                    //transferMovieDTO
                    if(o instanceof TransferMovieDTO){}
                    //password change DTO
                    if(o instanceof PasswordChangeDTO){}
                    //movie added DTO
                    if(o instanceof MovieAddedDTO){}
                    //stop DTO
                    if(o instanceof StopDTO){}
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
