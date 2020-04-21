package com.aihua.hotfix.utils;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import dalvik.system.PathClassLoader;

public class HotFix {
    public static final String TAG = "HotFix";
    public static void toFix(Context context, File file) {
        ClassLoader classLoader = context.getClassLoader();
        File filesDir = context.getCacheDir();
        try {
            //2 反射获取DexPathList属性对象pathList;
            Field pathListField = getField(classLoader, "pathList");
            pathListField.setAccessible(true);
            Object pathList = pathListField.get(classLoader);
            pathListField.setAccessible(false);

            //3 反射修改pathList的dexElements
            //3.1 把补丁包patch.dex 转化为Element[] (patch)
            List<File> files = new ArrayList<>();
            files.add(file);

            Method makePathElements = getMethod(pathList, "makePathElements", List.class, File.class, List.class);

            ArrayList<IOException> suppressdeExceptions = new ArrayList<>();
            makePathElements.setAccessible(true);
            Object[] patchElements = (Object[]) makePathElements.invoke(null, files, filesDir, suppressdeExceptions);
            makePathElements.setAccessible(false);


            //3.2 获得pathList的dexElements属性
            Field dexElementsField = getField(pathList, "dexElements");
            dexElementsField.setAccessible(true);
            Object[] dexElements = (Object[]) dexElementsField.get(pathList);
            dexElementsField.setAccessible(false);

            //3.3 patch+old合并
            Object[] newElements = (Object[]) Array.newInstance(dexElements[0].getClass(), patchElements.length + dexElements.length);
            Log.i(TAG, "patchElements.size=" + patchElements.length);
            Log.i(TAG, "dexElements.size=" + dexElements.length);
            Log.i(TAG, "newElements.size=" + newElements.length);
            System.arraycopy(patchElements, 0, newElements, 0, patchElements.length);
            System.arraycopy(dexElements, 0, newElements, patchElements.length, dexElements.length);

            //3.4 反射赋值给pathList的dexElements
            Field  elementsField=getField(pathList, "dexElements");;
            elementsField.setAccessible(true);
            elementsField.set(pathList, newElements);
            elementsField.setAccessible(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Field getField(Object object, String name) {
        Field field = null;
        Class<?> cls = object.getClass();
        while (cls != Object.class) {
            Log.i(TAG, cls.getSimpleName());
            try {
                field = cls.getDeclaredField(name);
                break;
            } catch (NoSuchFieldException e) {
                cls = cls.getSuperclass();
            }
        }
        return field;
    }

    public static Method getMethod(Object object, String name, Class<?>... parameterTypes) {
        Method method = null;
        Class<?> cls = object.getClass();
        while (cls != Object.class) {
            try {
                Log.i(TAG, cls.getSimpleName());
                method = cls.getDeclaredMethod(name, parameterTypes);
                break;
            } catch (NoSuchMethodException e) {
                cls = cls.getSuperclass();
            }
        }
        return method;
    }
}
