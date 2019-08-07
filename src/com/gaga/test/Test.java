package com.gaga.test;

import com.gaga.command.Command;
import com.gaga.exception.MyException;
import com.gaga.protocol.Protocol;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class Test {
    public static void main(String[] args) throws IOException, MyException, IllegalAccessException, InstantiationException {
        //String str = "+ok\r\n";
        //String str="-Err\r\n";
        //String str=":-100\r\n";

       //String str="$6\r\nfoobar\r\n";
        //String str="*2\r\n$3\r\nfoo\r\n$3\r\nbar\r\n";
        String str="*5\r\n$5\r\nlpush\r\n$3\r\nkey\r\n$1\r\n1\r\n$1\r\n2\r\n$1\r\n3\r\n";
        InputStream is = new ByteArrayInputStream(str.getBytes());
        Command command = Protocol.readCommand(is);
        command.run();
    }
}
