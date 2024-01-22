package com.chs.bio;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

/**
 * @program: NetPro
 * @description:
 * @author: hons.chang
 * @since: 2024-01-21 14:33
 **/
public class Client {


    /**
     * nio client
     */
    public static void main(String[] args) throws IOException {
        InetSocketAddress inetSocketAddress = new InetSocketAddress("127.0.0.1", 3456);
        try (Socket socket = new Socket()) {
            socket.connect(inetSocketAddress);

            try (ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
                 ObjectInputStream input = new ObjectInputStream(socket.getInputStream())) {
                for (int i = 0; i < 20; i++) {
                    output.writeUTF("hello server" + i);
                    output.flush();

                    System.out.println("message:" + input.readUTF());
                    Thread.sleep(1000);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
