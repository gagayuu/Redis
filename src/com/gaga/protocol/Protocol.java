package com.gaga.protocol;

import com.gaga.command.Command;
import com.gaga.command.CommandFactory;
import com.gaga.exception.MyException;
import com.gaga.exception.RemoteException;

import java.io.IOException;
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
        return readProcess(is);
    }

    public static Command readCommand(MyFilterInputStream is) throws MyException, InstantiationException, IllegalAccessException {
        Object o = read(is);

        return CommandFactory.build((List<Object>)o);
    }


    public static List<Object> readArray(MyFilterInputStream is) throws MyException {
        int len = (int) readInteger(is);
        if (len == -1) {
            return null;
        }
        List<Object> list = new ArrayList<Object>(len);
        for (int i = 0; i < len; i++) {
            list.add(readProcess(is));
        }
        return list;

    }

    public static byte[] readBulkString(MyFilterInputStream is) throws MyException {
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

    public static long readIntegers(MyFilterInputStream is) throws MyException {
        return readInteger(is);
    }

    public static String readERR(MyFilterInputStream is) throws MyException {
        return readLine(is);
    }

    public static String readString(MyFilterInputStream is) throws MyException {
        return readLine(is);
    }

    public static Object readProcess(MyFilterInputStream is) throws MyException {
        int start = 0;
        try {
            start = is.read();
        } catch (IOException e) {
            throw new RemoteException(e);
        }
        switch (start) {
            case '+':
                return readString(is);
            case '-':
                throw new RuntimeException(readERR(is));
            case ':':
                return readIntegers(is);
            case '$':
                return readBulkString(is);
            case '*':
                return readArray(is);
            default:
                throw new RuntimeException("不支持的类型");
        }
    }


    public static void writeString(MyFilterOutputSteam out,String str){
        try {
            out.write('+');
            out.write(str.getBytes(charset()));
            out.writeCRLF();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeError(MyFilterOutputSteam out, String err){
        try {
            out.write('-');
            out.write(err.getBytes(charset()));
            out.writeCRLF();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeIntegers(MyFilterOutputSteam out, long value){
        try {
            out.write(':');
            out.writeInteger(value);
            out.writeCRLF();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//    public static void writeBulkString(MyFilterOutputSteam out,String str){
//        writeBulkString(out,str.getBytes(charset()));
//    }
    public static void writeBulkString(MyFilterOutputSteam out,byte[] bytes){
        try {
            out.write('$');
            out.writeInteger(bytes.length);
            out.writeCRLF();
            out.write(bytes);
            out.writeCRLF();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeArray(MyFilterOutputSteam out,List<?> list){
        try {
            out.write('*');
            out.writeInteger(list.size());
            out.writeCRLF();
            for(Object o:list){

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeNull(MyFilterOutputSteam out){
        try {
            out.write('$');
            out.writeInteger(-1);
            out.writeCRLF();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeObject(MyFilterOutputSteam out,Object o) throws MyException {
        if(o instanceof String){
            writeString(out,(String)o);
        }else if(o instanceof RemoteException){
            writeError(out,((RemoteException) o).getMessage());
        }else if(o instanceof Integer){
            writeIntegers(out,((Integer) o).longValue());
        }else if(o instanceof Long){
            writeIntegers(out,(Long) o);
        }else if(o instanceof byte[]){
            writeBulkString(out,(byte[]) o);
        }else if(o instanceof List){
            writeArray(out,(List<?>) o);
        }else if(o == null){
            writeNull(out);
        }else{
            throw new MyException("写入了不识别的数据类型");
        }
    }


}
