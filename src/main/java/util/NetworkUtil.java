package util;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class NetworkUtil {
    private final Socket socket;
    private final ObjectOutputStream oos;
    private final ObjectInputStream ois;

    public NetworkUtil(String s, int port) throws IOException {
        System.out.println("In networkUtil of client");
        this.socket = new Socket(s, port);
        System.out.println("Connecting socket through string and port");
        oos = new ObjectOutputStream(socket.getOutputStream());
        ois = new ObjectInputStream(socket.getInputStream());
    }

    public NetworkUtil(Socket s) throws IOException {
        System.out.println("In networkUtil of server");
        this.socket = s;
        System.out.println("Connecting socket through socket");
        oos = new ObjectOutputStream(socket.getOutputStream());
        ois = new ObjectInputStream(socket.getInputStream());
    }

    public Object read() throws IOException, ClassNotFoundException {
        System.out.println("In read of NetworkUtil");
        return ois.readUnshared();
    }

    public void write(Object o) throws IOException {
        System.out.println("In write of NetworkUtil");
        oos.writeUnshared(o);
    }

    public void closeConnection() throws IOException {
        System.out.println("In closeConnection of NetworkUtil");
        ois.close();
        oos.close();
    }
}
