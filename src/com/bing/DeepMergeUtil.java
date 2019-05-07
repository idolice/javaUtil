package com.bing;

import com.sun.org.apache.xpath.internal.operations.Bool;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

public class DeepMergeUtil {

    public static <T> T deepMerge(T highPrior, T lowPrior) throws IllegalAccessException, InstantiationException {
        Object target = highPrior;
        Object backUp = lowPrior;
        Class c = target.getClass();
        T result = (T) c.newInstance();
        Field[] fields = c.getDeclaredFields();

        for (Field field : fields) {
            field.setAccessible(true);
            if (field.getType().isPrimitive()) {
                if (checkIfEmpty(field.get(target))) {
                    field.set(result, field.get(backUp));
                } else {
                    field.set(result, field.get(target));
                }
            } else if (checkIfPrimitiveObject(field.getType())) {
                if(field.get(target) == null){
                    field.set(result, field.get(backUp));
                }else {
                    field.set(result, field.get(target));
                }
            } else {

                if (field.get(target) != null) {
                    field.set(result, deepMerge(field.get(target), field.get(lowPrior)));
                } else {
                    field.set(result, field.get(lowPrior));
                }
            }
        }
        return result;
    }

    private static boolean checkIfPrimitiveObject(Class<?> type) {
        List<Class<?>> classes = Arrays.asList(String.class, Bool.class, Boolean.class, Byte.class, Character.class, Double.class,
                Float.class, Integer.class, Long.class, Short.class);
        if (classes.contains(type)) {
            return true;
        }
        return false;

    }

    private static boolean checkIfEmpty(Object o) {
        if (o.getClass().equals(Long.class) || o.getClass().equals(Byte.class) || o.getClass().equals(Short.class) || o.getClass().equals(Integer.class)) {
            if (o.equals(0)) {
                return true;
            }
        } else if (o.getClass().equals(Float.class)) {
            if (o.equals(0.0f)) {
                return true;
            }
        } else if (o.getClass().equals(Double.class)) {
            if (o.equals(0.0d)) {
                return true;
            }
        } else if (o.getClass().equals(Character.class)) {
            if (o.equals("\u0000")) {
                return true;
            }
        }
        return false;
    }


}
