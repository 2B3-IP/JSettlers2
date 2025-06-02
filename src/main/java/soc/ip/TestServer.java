import java.io.*;
import java.net.*;

public class TestServer {
    public static void main(String[] args) throws IOException {
        ServerSocket server = new ServerSocket(6969);
        System.out.println("Listening on port 6969...");

        while (true) {
            Socket socket = server.accept();
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                System.out.println("Received from LogHandler: " + line);
            }
            socket.close();
        }
    }
}