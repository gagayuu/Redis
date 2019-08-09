package com.gaga.redis.command;

import com.gaga.redis.DB.DataBase;
import com.gaga.redis.protocol.MyFilterOutputSteam;
import com.gaga.redis.protocol.Protocol;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HSETCommand  implements Command{
    private List<Object> args;
    @Override
    public void setArgs(List<Object> args) {
        this.args=args;
    }

    @Override
    public void run(OutputStream outputStream) throws IOException {
        DataBase dataBase=DataBase.getInstance();
        String key=new String((byte[]) args.get(0));
        String filed=new String((byte[]) args.get(1));
        String value=new String((byte[]) args.get(2));
        Map<String,String> hash=dataBase.getHashes(key);
        boolean isUpdate=hash.containsKey(filed);
        hash.put(filed,value);
        if(isUpdate){
            Protocol.writeIntegers(new MyFilterOutputSteam(outputStream),0);
        }else{
            Protocol.writeIntegers(new MyFilterOutputSteam(outputStream),1);
        }

    }
}
