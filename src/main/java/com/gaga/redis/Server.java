package com.gaga.redis;


import com.gaga.redis.command.Command;
import com.gaga.redis.exception.MyException;
import com.gaga.redis.protocol.MyFilterOutputSteam;
import com.gaga.redis.protocol.Protocol;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    public void run(int port) throws IOException {

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            while (true) {
                try (Socket socket = serverSocket.accept()) {
                    InputStream inputStream = socket.getInputStream();
                    OutputStream outputStream = socket.getOutputStream();

                    Command command=null;
                    while(true) {
                        try{
                        command = Protocol.readCommand(inputStream);
                        command.run(outputStream);
                        } catch (IOException | MyException | InstantiationException | IllegalAccessException e) {
                            e.printStackTrace();
                            Protocol.writeError(new MyFilterOutputSteam(outputStream),"不识别的命令");
                        }
                    }

                }
            }

        }
    }

    public static void main(String[] args) throws IOException {
        new Server().  run(6379);
    }
}
