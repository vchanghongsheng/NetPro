package com.chs.bio;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.*;

/**
 * @program: NetPro
 * @description:
 * @author: hons.chang
 * @since: 2024-01-21 14:33
 **/
public class Server {


//    public static void main(String[] args) throws IOException {
//        InetSocketAddress inetSocketAddress = new InetSocketAddress(3456);
//
//        try (ServerSocket serverSocket = new ServerSocket()) {
//            serverSocket.bind(inetSocketAddress);
//
//            System.out.println("server start");
//
//            do {
//                Socket accept = serverSocket.accept();
//                try (ObjectInputStream input = new ObjectInputStream(accept.getInputStream());
//                     ObjectOutputStream output = new ObjectOutputStream(accept.getOutputStream())) {
//
//                    String inputStr = input.readUTF();
//                    System.out.println("message:" + inputStr);
//
//                    output.writeUTF("hello client:" + inputStr);
//                    output.flush();
//
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            } while (true);
//        }
//    }


    private static final int core = Runtime.getRuntime().availableProcessors();

    private static final ThreadPoolExecutor executor = new ThreadPoolExecutor(core, core, 60, TimeUnit.SECONDS, new LinkedBlockingDeque<>(), r -> new Thread(r, "server-thread"));

    public static void main(String[] args) {

        try (ServerSocket serverSocket = new ServerSocket()) {
            serverSocket.bind(new InetSocketAddress(3456));
            System.out.println("Start Server ....");
            do {
                Socket socket = serverSocket.accept();
                executor.execute(new ServerTask(socket));
            }
            while (true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

class ServerTask implements Runnable {
    private final Socket socket;

    public ServerTask(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try (ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
             ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream())) {

            while (true) {
                String inputStr;
                try {
                    inputStr = input.readUTF();
                } catch (EOFException e) {
                    break;
                }

                System.out.println(Thread.currentThread().getId() + "====> Accept message:" + inputStr);
                output.writeUTF("hello client:" + inputStr);
                output.flush();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
