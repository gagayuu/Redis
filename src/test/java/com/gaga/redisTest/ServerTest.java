package com.gaga.redisTest;

import com.gaga.redis.Server;

import java.io.IOException;

public class ServerTest {
    public static void main(String[] args) throws IOException {
        new Server().  run(6379);

    }
}
