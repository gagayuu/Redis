package com.gaga.redis.command;
import com.gaga.redis.DB.DataBase;

import java.util.List;

public class LPUSHCommand implements Command{

    private List<Object> args;
    @Override
    public void setArgs(List<Object> args) {
        this.args=args;
    }

    @Override
    public void run() {
        DataBase dataBase=DataBase.getInstance();
        String key=new String((byte[]) args.remove(0));
        List<String> list=dataBase.getLists(key);
        for(Object o:args){
            String value=new String((byte[]) o);
            list.add(0,value);
        }
        System.out.println(list.size());
    }
}
