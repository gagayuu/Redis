package com.gaga.redis.command;


import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public interface Command {
    void setArgs(List<Object> args);

    void run(OutputStream outputStream) throws IOException;
}