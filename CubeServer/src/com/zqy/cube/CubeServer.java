package com.zqy.cube;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by ZhengQinyu on 2016/5/12.
 */
public class CubeServer {
    private final static String IP = "172.18.12.165";

    private final static int PORT = 8888;

    private ServerSocket serverSocket;

    public CubeServer() {
        this(IP, PORT);
    }

    public CubeServer(String ip, int port) {
        try {
            serverSocket = new ServerSocket();
            serverSocket.bind(new InetSocketAddress(ip, port));
            System.out.println(ip + ":" + port + "监听开始...");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void listen() {
        try {
            while (true) {
                Socket socket;
                socket = serverSocket.accept();
                new CubeHandler(socket).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        new CubeServer().listen();
    }
}
