import java.net.*;
import java.util.Scanner;

public class EchoServer {
    public static void main(String[] args) throws Exception {
        DatagramSocket socket = new DatagramSocket(5000);
        byte[] buffer = new byte[1024];

        System.out.println("Quote Proxy Server listening on port 5000...");

        while (true) {
            // Receive the incoming UDP packet from the client
            DatagramPacket inPacket = new DatagramPacket(buffer, buffer.length);
            socket.receive(inPacket);

            String received = new String(inPacket.getData(), 0, inPacket.getLength());
            System.out.println("Received request: " + received);

            // Fetch random technology quote from the web
            Scanner s = new Scanner(
                new URL("https://zenquotes.io/api/random").openStream()
            );
            String webData = s.useDelimiter("\\A").next();
            s.close();

            System.out.println("Fetched quote: " + webData);

            // Send web data back to the original client's IP and port
            byte[] responseBytes = webData.getBytes();
            DatagramPacket outPacket = new DatagramPacket(
                responseBytes,
                responseBytes.length,
                inPacket.getAddress(),   // client's IP address
                inPacket.getPort()       // client's port
            );
            socket.send(outPacket);
        }
    }
}