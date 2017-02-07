package nodeTest; /**
 * Created by hc2twv on 19/12/16.
 */

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class Node8 {
    public static void main(String args[]) {
        DatagramSocket dsocket = null;
        DatagramPacket packet = null;

        byte[] message = null,message1 = null,message2 = null,message3 = null,message4 = null;
        try {
            String host = "127.0.0.1";
            int port = 25555;
            // Get the internet address of the specified host
            InetAddress address = InetAddress.getByName(host);

            // Create a datagram socket, send the packet through it, close it.
            dsocket = new DatagramSocket();
            while(true){
                message = ("08:08:MAC:T:"+((int)Math.random()*45+41)+":CHK").getBytes();
                packet = new DatagramPacket(message, message.length,address, port);
                dsocket.send(packet);
                message = ("08:08:MAC:X:"+((int)Math.random()*330+300)+":CHK").getBytes();
                packet = new DatagramPacket(message, message.length,address, port);
                dsocket.send(packet);
                message = ("08:08:MAC:P:"+((int)Math.random()*205+135)+":CHK").getBytes();
                packet = new DatagramPacket(message, message.length,address, port);
                dsocket.send(packet);
                message = ("08:08:MAC:V:"+((int)Math.random()*5+0)+":CHK").getBytes();
                packet = new DatagramPacket(message, message.length,address, port);
                dsocket.send(packet);
                message = ("08:08:MAC:C:"+((int)Math.random()*26+20)+":CHK").getBytes();
                packet = new DatagramPacket(message, message.length,address, port);
                dsocket.send(packet);
                System.out.println("message sent");
                Thread.sleep(5000);
            }


        } catch (Exception e) {
            System.err.println(e);
        } finally{
            dsocket.close();
        }
    }
}