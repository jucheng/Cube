package com.zqy.cube;

import org.kociemba.twophase.Search;

import java.io.*;
import java.net.Socket;

/**
 * Created by ZhengQinyu on 2016/5/12.
 */
public class CubeHandler extends Thread {

    private Socket socket;

    public CubeHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            InputStream is = socket.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            OutputStream os = socket.getOutputStream();
            PrintWriter pw = new PrintWriter(os);
            String str;
            StringBuilder message = new StringBuilder();
            while ((str = br.readLine()) != null) {
                message.append(str);
            }
            str = message.toString();
            System.out.println("魔方序列：" + str);
            str = Search.solution(str, 21, 300, false);
            System.out.println("解决步骤：" + str);
            pw.write(str);
            pw.flush();
            pw.close();
            os.close();
            br.close();
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (socket != null) socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
