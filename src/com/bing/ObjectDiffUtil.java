package com.bing;

import java.lang.reflect.Field;

public class ObjectDiffUtil {
    public static <T> T getDiffObject(T latest, T beforeChange) throws IllegalAccessException, InstantiationException {
        Class c = latest.getClass();
        T newLatest = (T) c.newInstance();

        Field[] fields = c.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            if(field.getType().isPrimitive()){
                if(!field.get(latest).equals(field.get(beforeChange))){
                    field.set(newLatest, field.get(latest));
                }
            }else {
                if(!field.get(latest).equals(field.get(beforeChange))){
                    field.set(newLatest, field.get(latest));
                }else {
                    field.set(newLatest, null);
                }
            }
        }
        return  newLatest;
    }
}
