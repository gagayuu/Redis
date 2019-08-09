package com.gaga.redis.command;

import com.gaga.redis.DB.DataBase;
import com.gaga.redis.protocol.MyFilterOutputSteam;
import com.gaga.redis.protocol.Protocol;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

public class HGETCommand implements Command {
    private List<Object> args;

    @Override
    public void setArgs(List<Object> args) {
        this.args = args;
    }

    @Override
    public void run(OutputStream outputStream) throws IOException {
        DataBase dataBase = DataBase.getInstance();
        String key = new String((byte[]) args.get(0));
        String field = new String((byte[]) args.get(1));
        Map<String,String> hash=dataBase.getHashes(key);
        String value=hash.get(field);
        if(value!=null){
            Protocol.writeBulkString(new MyFilterOutputSteam(outputStream),value.getBytes());
        }else{
            Protocol.writeNull(new MyFilterOutputSteam(outputStream));
        }
    }
}
