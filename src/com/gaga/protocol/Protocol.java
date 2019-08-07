package com.gaga.protocol;

import com.gaga.command.Command;
import com.gaga.command.CommandFactory;
import com.gaga.exception.MyException;
import com.gaga.exception.RemoteException;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import static com.gaga.protocol.MyFilterInputStream.readInteger;
import static com.gaga.protocol.MyFilterInputStream.readLine;

//协议解析
public class Protocol {

    public static Charset charset() {
        return Charset.forName("UTF-8");
    }

    private static Object read(MyFilterInputStream is) throws MyException {
        return process(is);
    }

    public static Command readCommand(MyFilterInputStream is) throws MyException, InstantiationException, IllegalAccessException {
        Object o = read(is);

        return CommandFactory.build((List<Object>)o);
    }


    public static List<Object> processArray(MyFilterInputStream is) throws MyException {
        int len = (int) readInteger(is);
        if (len == -1) {
            return null;
        }
        List<Object> list = new ArrayList<Object>(len);
        for (int i = 0; i < len; i++) {
            list.add(process(is));
        }
        return list;

    }

    public static byte[] processBulkString(MyFilterInputStream is) throws MyException {
        int len = (int) readInteger(is);
        if (len == -1) {
            throw new RuntimeException("not -1");
        }
        byte[] bytes = new byte[len];
        for (int i = 0; i < len; i++) {
            try {
                int b = is.read();
                bytes[i] = (byte) b;
            } catch (IOException e) {

            }
        }
        try {
            is.read();
            is.read();
        } catch (IOException e) {

        }
        return bytes;
    }

    public static long processInteger(MyFilterInputStream is) throws MyException {
        return readInteger(is);
    }

    public static String processERR(MyFilterInputStream is) throws MyException {
        return readLine(is);
    }

    public static String processString(MyFilterInputStream is) throws MyException {
        return readLine(is);
    }

    public static Object process(MyFilterInputStream is) throws MyException {
        int start = 0;
        try {
            start = is.read();
        } catch (IOException e) {
            throw new RemoteException(e);
        }
        switch (start) {
            case '+':
                return processString(is);
            case '-':
                throw new RuntimeException(processERR(is));
            case ':':
                return processInteger(is);
            case '$':
                return processBulkString(is);
            case '*':
                return processArray(is);
            default:
                throw new RuntimeException("不支持的类型");
        }
    }



}
