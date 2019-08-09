package com.gaga.redis.command;

import com.gaga.redis.exception.MyException;
import com.gaga.redis.protocol.Protocol;

import java.util.List;

public class CommandFactory {

    public static Command build(String commandName) throws MyException, IllegalAccessException, InstantiationException {
        //找到对应的的类名
        String fullClassName = "com.gaga.redis.command." + commandName.toUpperCase() + "Command";

        try {
            //反射得到对象
            Class<?> cls = Class.forName(fullClassName);
            if (!Command.class.isAssignableFrom(cls)) {
                throw new MyException("不是合法命令");
            }
            return (Command) cls.newInstance();
        } catch (ClassNotFoundException e) {
            throw new MyException("不是合法命令" + e);
        }
    }

    public static Command build(List<Object> args) throws MyException, InstantiationException, IllegalAccessException {
        if (args.size() < 1) {
            throw new MyException("长度不能 < 1");
        }


        Object obj = args.remove(0);
        if (!(obj instanceof byte[])) {
            throw new MyException("命令不是bulkString形式");
        }

        String commandName = new String((byte[]) obj, Protocol.charset());
        Command command = build(commandName);
        command.setArgs(args);
        return command;
    }
}