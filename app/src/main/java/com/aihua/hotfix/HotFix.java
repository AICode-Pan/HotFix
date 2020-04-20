package com.aihua.hotfix;

import android.content.Context;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class HotFix {
    public static void toFix(Context context, File file) {
        ClassLoader classLoader = context.getClassLoader();
        File filesDir = file.getAbsoluteFile();

        try {
            //2 反射获取DexPathList属性对象pathList;
            Field pathListField = getField(classLoader, "pathList");
            Object pathList = pathListField.get(classLoader);

            //3 反射修改pathList的dexElements
            //3.1 把补丁包patch.dex 转化为Element[] (patch)
            List<File> files = new ArrayList<>();
            files.add(file);

            Method makePathElements = getMethod(classLoader, "makePathElements", List.class, File.class, List.class);

            ArrayList<IOException> suppressdeExceptions = new ArrayList<>();
            Object[] patchElements = (Object[]) makePathElements.invoke(null, files, filesDir, suppressdeExceptions);


            //3.2 获得pathList的dexElements属性
            Field dexElementsField = getField(pathList, "dexElements");
            Object[] dexElements = (Object[]) dexElementsField.get(pathList);

            //3.3 patch+old合并
            Object[] newElements = (Object[]) Array.newInstance(dexElements[0].getClass(), patchElements.length + dexElements.length);

            System.arraycopy(patchElements, 0, newElements, 0, patchElements.length);
            System.arraycopy(dexElements, 0, newElements, patchElements.length, dexElements.length);

            //3.4 反射赋值给pathList的dexElements
            pathListField.set("pathList", newElements);

        } catch (Exception e) {

        }
    }

    public static Field getField(Object object, String name) {
        Field field = null;
        Class<?> cls = (Class<?>) object;
        while (cls != Object.class) {
            try {
                field = cls.getField(name);
            } catch (NoSuchFieldException e) {
                cls = cls.getSuperclass();
            }
        }
        return field;
    }

    public static Method getMethod(Object object, String name, Class<?>... parameterTypes) {
        Method method = null;
        Class<?> cls = (Class<?>) object;
        while (cls != Object.class) {
            try {
                method = cls.getMethod(name, parameterTypes);
            } catch (NoSuchMethodException e) {
                cls = cls.getSuperclass();
            }
        }
        return method;
    }
}
