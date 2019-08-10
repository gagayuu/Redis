package com.gaga.redis.command;
import com.gaga.redis.DB.DataBase;
import com.gaga.redis.protocol.MyFilterOutputSteam;
import com.gaga.redis.protocol.Protocol;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public class LPUSHCommand implements Command{

    private List<Object> args;
    @Override
    public void setArgs(List<Object> args) {
        this.args=args;
    }

    @Override
    public void run(OutputStream outputStream) throws IOException {
        DataBase dataBase=DataBase.getInstance();
        String key=new String((byte[]) args.remove(0));
        List<String> list=dataBase.getLists(key);
        for(Object o:args){
            String value=new String((byte[]) o);
            list.add(0,value);
        }
        System.out.println("list元素个数" +list.size());
        Protocol.writeIntegers(new MyFilterOutputSteam(outputStream),list.size());

    }
}
