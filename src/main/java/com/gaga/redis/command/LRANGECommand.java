package com.gaga.redis.command;

import com.gaga.redis.DB.DataBase;
import com.gaga.redis.protocol.MyFilterOutputSteam;
import com.gaga.redis.protocol.Protocol;
import org.omg.PortableInterceptor.INACTIVE;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public class LRANGECommand implements Command{
    private List<Object> args;

    @Override
    public void setArgs(List<Object> args) {
        this.args=args;
    }

    @Override
    public void run(OutputStream outputStream) throws IOException {
        DataBase dataBase=DataBase.getInstance();
        String key=new String((byte[])args.get(0));
        int start=Integer.parseInt(new String((byte[]) args.get(1)));
        int end=Integer.parseInt(new String((byte[]) args.get(2)));
        List<String> list=dataBase.getLists(key);
        if(list==null){
            Protocol.writeNull(new MyFilterOutputSteam(outputStream));
        }
        if(end<0){
            end=end+list.size();
        }
        if(start>end+1){
            Protocol.writeNull(new MyFilterOutputSteam(outputStream));
        }
        if(end>list.size()-1){
            end=list.size()-1;
        }
        List<String> subList=list.subList(start,end+1);
        Protocol.writeArray(new MyFilterOutputSteam(outputStream),subList);
    }
}
