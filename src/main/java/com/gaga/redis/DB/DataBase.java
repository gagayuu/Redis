package com.gaga.redis.DB;

import java.util.*;

public class DataBase {
    private static DataBase instance=new DataBase();

    public static DataBase getInstance(){
        return instance;
    }

    private Map<String,String> strings;
    private Map<String,Map<String,String>> hashes;
    private Map<String, List<String>> lists;
    private Map<String, Set<String>> sets;
    private Map<String, LinkedHashSet<String>> zsets;
    private DataBase() {
        strings=new HashMap<>();
        hashes=new HashMap<>();
        lists=new HashMap<>();
        sets=new HashMap<>();
        zsets=new HashMap<>();
    }

    public List<String> getLists(String key){
        List<String> list=lists.get(key);
        if(list==null){
            list=new ArrayList<>();
            lists.put(key,list);
        }
        return list;
    }

}