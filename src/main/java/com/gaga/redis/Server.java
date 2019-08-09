package com.gaga.redis;

import ch.qos.logback.core.OutputStreamAppender;
import com.gaga.redis.command.Command;
import com.gaga.redis.exception.MyException;
import com.gaga.redis.exception.RemoteException;
import com.gaga.redis.protocol.MyFilterInputStream;
import com.gaga.redis.protocol.MyFilterOutputSteam;
import com.gaga.redis.protocol.Protocol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static Logger logger = LoggerFactory.getLogger(Server.class);

    public void run(int port) throws IOException {

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            while (true) {
                try (Socket socket = serverSocket.accept()) {
                    logger.info("连接成功...");
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
}
