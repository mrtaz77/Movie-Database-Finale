package server;

import DataTransferObjects.StopDTO;

import java.io.IOException;
import java.util.Scanner;

public class InputThreadServer implements Runnable{
    private Server server;
    private Thread thread;

    public InputThreadServer(Server server) {
        //InputThreadServer aims to close server
        this.server = server;
        thread = new Thread(this,"InputThread");
        thread.start();
    }

    @Override
    public void run() {
        try(Scanner scanner = new Scanner(System.in)){
           while(true){
               String next = scanner.nextLine();
               if(next.equalsIgnoreCase("Stop")){
                    var stopDTO = new StopDTO(true);
                    for(String client:server.getCompanyNetworkMap().keySet()){
                        try{
                            server.getCompanyNetworkMap().get(client).write(stopDTO);
                        }catch(IOException e){
                            System.out.println("Exception while closing client : "+client+" from InputThreadServer");
                            System.out.println(e);
                        }
                    }

                   System.out.println("Shutting down server...");
                    try{
                        server.serverSocket.close();
                    }catch (IOException e){
                        System.out.println("Error while closing server...");
                        System.out.println(e);
                    }
                    System.exit(0);
               }
           }
        }
    }
}
