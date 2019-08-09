package com.gaga.redis.DB;

import java.util.*;

/**
 * 真正管理数据存储
 */
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

    public Map<String,String> getHashes(String key){
        Map<String,String> map=hashes.get(key);
        if(map==null){
            map=new HashMap<>();
            hashes.put(key,map);
        }
        return map;
    }
}