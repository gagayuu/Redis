package com.gaga.command;

import com.gaga.exception.MyException;
import com.gaga.protocol.Protocol;

import java.util.List;

public class CommandFactory {

    public static Command build(String commandName) throws MyException, IllegalAccessException, InstantiationException {
        String fullClassName = "com.gaga.command." + commandName.toUpperCase() + "Command";

        try {
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
        if (args.size() == 0) {
            throw new MyException("长度不能为0");
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
