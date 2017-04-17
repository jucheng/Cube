package com.zqy.cube;

import java.io.*;
import java.net.Socket;

/**
 * Created by ZhengQinyu on 2016/5/12.
 */
public class CubeClient {

    private final static String IP = "172.18.12.165";

    private final static int PORT = 8888;

    private Socket socket;

    public CubeClient() {
        this(IP, PORT);
    }

    public CubeClient(String ip, int port) {
        try {
            socket =new Socket(ip,port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String send(String message){
        String str = null;
        try {
            OutputStream os=socket.getOutputStream();
            PrintWriter pw=new PrintWriter(os);
            InputStream is=socket.getInputStream();
            BufferedReader br=new BufferedReader(new InputStreamReader(is));
            pw.write(message);
            pw.flush();
            socket.shutdownOutput();
            StringBuilder reply = new StringBuilder();
            while ((str = br.readLine()) != null) {
                reply.append(str);
            }
            str = reply.toString();
            //4.关闭资源
            br.close();
            is.close();
            pw.close();
            os.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            return str;
        }
    }

    public static void main(String[] args) {
        String result = new CubeClient().send("UBUDUDDFDLLRFRRRBLBUFRFRBDFDFDLDLUBURRLLLULURBDFBBFBUF");
        System.out.println("解决步骤：" + result);
    }
}
