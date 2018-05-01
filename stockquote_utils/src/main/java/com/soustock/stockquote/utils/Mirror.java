package com.soustock.stockquote.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.util.*;

/**
 *  包装 Class<?>， 提供了更多的反射方法
 * Created by xuyufei on 2015/06/12.
 * 
 */
public class Mirror<T> {
    /**
     * 包装一个类
     *
     * @param classOfT
     *            类
     * @return Mirror
     */
    public static <T> Mirror<T> me(Class<T> classOfT) {
        return null == classOfT ? null : new Mirror<T>(classOfT);
    }

    /**
     * 生成一个对象的 Mirror
     *
     * @param obj
     *            对象。
     * @return Mirror， 如果 对象 null，则返回 null
     */
    @SuppressWarnings("unchecked")
    public static <T> Mirror<T> me(T obj) {
        return null == obj ? null : (Mirror<T>) me(obj.getClass());
    }

    /**
     * 类
     */
    private Class<T> clazz;

    /**
     * 私有构造函数
     *
     * @param classOfT
     *            类
     */
    private Mirror(Class<T> classOfT) {
        clazz = classOfT;
    }

    /**
     * 根据名称获取一个 Getter。<br />
     * 比如，你想获取 abc 的 getter ，那么优先查找 getAbc()，如果没有则查找isAbc()，最后才是查找 abc()。
     *
     * @param fieldName
     *            方法名
     * @return 方法对象
     */
    public Method getGetter(String fieldName) throws NoSuchMethodException {
        try {
            String fn = StringUtity.firstUpperCase(fieldName);
            try {
                try {
                    return clazz.getMethod("get" + fn);
                } catch (NoSuchMethodException e) {
                    Method m = clazz.getMethod("is" + fn);
                    if (!Mirror.me(m.getReturnType()).isBoolean())
                        throw new NoSuchMethodException();
                    return m;
                }
            } catch (NoSuchMethodException e) {
                return clazz.getMethod(fieldName);
            }
        } catch (RuntimeException e) {
            throw new NoSuchMethodException(String.format(
                    "Fail to find getter for [%s]->[%s]", clazz.getName(),
                    fieldName));
        }

    }

    /**
     * 根据字段获取一个 Getter。<br />
     * 比如，你想获取 abc 的 getter ，那么优先查找 getAbc()，如果 没有，则查找 abc()。
     *
     * @param field
     *            字段对象
     * @return 方法对象
     * @throws NoSuchMethodException
     *             没有找到 Getter
     */
    public Method getGetter(Field field) throws NoSuchMethodException {
        try {
            try {
                String fn = StringUtity.firstUpperCase(field.getName());
                if (Mirror.me(field.getType()).isBoolean())
                    return clazz.getMethod("is" + fn);
                else
                    return clazz.getMethod("get" + fn);
            } catch (NoSuchMethodException e) {
                return clazz.getMethod(field.getName());
            }
        } catch (Exception e) {
            throw new NoSuchMethodException(String.format(
                    "Fail to find getter for [%s]->[%s]", clazz.getName(),
                    field.getName()));
        }
    }

    /**
     * 根据一个字段获取 Setter
     * <p>
     * 比如，你想获取 abc 的 setter ，那么优先查找 setAbc(T abc)，如果 没有，则查找 abc(T abc)。
     *
     * @param field
     *            字段
     * @return 方法
     * @throws NoSuchMethodException
     *             没找到 Setter
     */
    public Method getSetter(Field field) throws NoSuchMethodException {
        try {
            try {
                return clazz.getMethod(
                        "set" + StringUtity.firstUpperCase(field.getName()),
                        field.getType());
            } catch (RuntimeException e) {
                try {
                    if (field.getName().startsWith("is")
                            && Mirror.me(field.getType()).isBoolean())
                        return clazz
                                .getMethod(
                                        "set" + field.getName().substring(2),
                                        field.getType());
                } catch (RuntimeException e1) {
                }
                return clazz.getMethod(field.getName(), field.getType());
            }
        } catch (RuntimeException e) {
            throw new NoSuchMethodException(String.format(
                    "Fail to find setter for [%s]->[%s]", clazz.getName(),
                    field.getName()));
        }
    }

    /**
     * 根据一个字段名和字段类型获取 Setter
     *
     * @param fieldName
     *            字段名
     * @param paramType
     *            字段类型
     * @return 方法
     * @throws NoSuchMethodException
     *             没找到 Setter
     */
    public Method getSetter(String fieldName, Class<?> paramType)
            throws NoSuchMethodException {
        try {
            String setterName = "set" + StringUtity.firstUpperCase(fieldName);
            try {
                return clazz.getMethod(setterName, paramType);
            } catch (Throwable e) {
                try {
                    return clazz.getMethod(fieldName, paramType);
                } catch (Throwable e1) {
                    Mirror<?> type = Mirror.me(paramType);
                    for (Method method : clazz.getMethods()) {
                        if (method.getParameterTypes().length == 1)
                            if (method.getName().equals(setterName)
                                    || method.getName().equals(fieldName)) {
                                if (null == paramType
                                        || type.canCastToDirectly(method
                                        .getParameterTypes()[0]))
                                    return method;
                            }
                    }
                    // 还是没有? 会不会是包装类型啊?
                    if (!paramType.isPrimitive()) {
                        Class<?> p = unWrapper();
                        if (null != p)
                            return getSetter(fieldName, p);
                    }
                    throw new RuntimeException();
                }
            }
        } catch (Throwable e) {
            throw new NoSuchMethodException(String.format(
                    "Fail to find setter for [%s]->[%s(%s)]", clazz.getName(),
                    fieldName, paramType.getName()));
        }
    }

    /**
     * 根据一个字段名，获取一组有可能成为 Setter 函数
     *
     * @param fieldName
     *            字段名
     * @return 函数数组
     */
    public Method[] findSetters(String fieldName) {
        String mName = "set" + StringUtity.firstUpperCase(fieldName);
        List<Method> ms = new ArrayList<Method>();
        for (Method m : this.clazz.getMethods()) {
            if (!Modifier.isStatic(m.getModifiers())
                    && m.getParameterTypes().length == 1
                    && m.getName().equals(mName))
                ms.add(m);
        }
        return ms.toArray(new Method[ms.size()]);
    }

    /**
     * 获取一个字段。这个字段可以是当前类型或者其父类的私有字段。
     *
     * @param name
     *            字段名
     * @return 字段
     * @throws NoSuchFieldException
     *             没找到字段
     */
    public Field getField(String name) throws NoSuchFieldException {
        Class<?> cc = clazz;
        while (null != cc && cc != Object.class) {
            try {
                return cc.getDeclaredField(name);
            } catch (NoSuchFieldException e) {
                cc = cc.getSuperclass();
            }
        }
        throw new NoSuchFieldException(
                String.format(
                        "Can not find field [%s] in class [%s] and it's parents classes",
                        name, clazz.getName()));
    }

    /**
     * 获取一个字段。这个字段必须声明特殊的注解，第一遇到的对象会被返回
     *
     * @param ann
     *            注解
     * @return 字段
     * @throws NoSuchFieldException
     *             没找到字段
     */
    public <AT extends Annotation> Field getField(Class<AT> ann)
            throws NoSuchFieldException {
        for (Field field : this.getFields()) {
            if (field.isAnnotationPresent(ann))
                return field;
        }
        throw new NoSuchFieldException(
                String.format(
                        "Can NOT find field [@%s] in class [%s] and it's parents classes",
                        ann.getName(), clazz.getName()));
    }

    /**
     * 获取一组声明了特殊注解的字段
     *
     * @param ann
     *            注解类型
     * @return 字段数组
     */
    public <AT extends Annotation> Field[] getFields(Class<AT> ann) {
        List<Field> fields = new LinkedList<Field>();
        for (Field f : this.getFields()) {
            if (f.isAnnotationPresent(ann))
                fields.add(f);
        }
        return fields.toArray(new Field[fields.size()]);
    }

    /**
     * 获得所有的属性，包括私有属性。不包括 Object 的属性
     *
     * @return 字段列表
     */
    public Field[] getFields() {
        return _getFields(true, false, true, true);
    }

    /**
     * 获得所有的静态变量属性
     *
     * @param noFinal
     *            是否包括 final 修饰符的字段
     *
     * @return 字段列表
     */
    public Field[] getStaticField(boolean noFinal) {
        return _getFields(false, true, noFinal, true);
    }

    private Field[] _getFields(boolean noStatic, boolean noMember,
                               boolean noFinal, boolean noInner) {
        Class<?> cc = clazz;
        Map<String, Field> map = new LinkedHashMap<String, Field>();
        while (null != cc && cc != Object.class) {
            Field[] fs = cc.getDeclaredFields();
            for (int i = 0; i < fs.length; i++) {
                Field f = fs[i];
                int m = f.getModifiers();
                if (noStatic && Modifier.isStatic(m))
                    continue;
                if (noFinal && Modifier.isFinal(m))
                    continue;
                if (noInner && f.getName().startsWith("this$"))
                    continue;
                if (noMember && !Modifier.isStatic(m))
                    continue;
                if (map.containsKey(fs[i].getName()))
                    continue;

                map.put(fs[i].getName(), fs[i]);
            }
            cc = cc.getSuperclass();
        }
        return map.values().toArray(new Field[map.size()]);
    }

    /**
     * 向父类递归查找某一个运行时注解
     *
     * @param <A>
     *            注解类型参数
     * @param annType
     *            注解类型
     * @return 注解
     */
    public <A extends Annotation> A getAnnotation(Class<A> annType) {
        Class<?> cc = clazz;
        A ann;
        do {
            ann = cc.getAnnotation(annType);
            cc = cc.getSuperclass();
        } while (null == ann && cc != Object.class);
        return ann;
    }

    /**
     * 获取一个方法上的指定运行时注解<br />
     * 会遍历父类和接口
     *
     * @param <A>
     * @param method
     * @param annType
     * @return
     */
    public <A extends Annotation> A getAnnotation(Method method,
                                                  Class<A> annType) {
        if (method == null) {
            return null;
        }
        // 检查当前方法上是否存在此注解，存在直接返回
        if (method.isAnnotationPresent(annType)) {
            return method.getAnnotation(annType);
        } else {// 不存在，则要匹配父类，接口，值得注意的是方法参数也要一样
            A ann = null;
            // 先从方法类的接口里找
            Class<?>[] interfaces = method.getDeclaringClass().getInterfaces();
            for (int i = 0; i < interfaces.length; i++) {
                ann = getAnnotation(getMethod(interfaces[i], method), annType);
                if (ann != null) {
                    return ann;
                }
            }
            // 超类方法
            Class<?> cc = clazz;
            while (null != cc && cc != Object.class) {
                ann = getAnnotation(getMethod(cc, method), annType);
                cc = cc.getSuperclass();
            }
            return ann;
        }
    }

    private static Method getMethod(Class<?> cls, Method method) {
        try {
            return cls.getMethod(method.getName(), method.getParameterTypes());
        } catch (Exception e) {
        }
        return null;
    }

    /**
     * 获取本类型所有的方法，包括私有方法。不包括 Object 的方法
     *
     * @return 方法
     */
    public Method[] getMethods() {
        Class<?> cc = clazz;
        List<Method> list = new LinkedList<Method>();
        while (null != cc && cc != Object.class) {
            Method[] ms = cc.getDeclaredMethods();
            for (int i = 0; i < ms.length; i++) {
                list.add(ms[i]);
            }
            cc = cc.getSuperclass();
        }
        return list.toArray(new Method[list.size()]);
    }

    /**
     * 获取当前对象，所有的方法，包括私有方法。递归查找至自己某一个父类为止 。
     * <p>
     * 并且这个按照名称，消除重复的方法。子类方法优先
     *
     * @param top
     *            截至的父类
     * @return 方法数组
     */
    public Method[] getAllDeclaredMethods(Class<?> top) {
        Class<?> cc = clazz;
        Map<String, Method> map = new LinkedHashMap<String, Method>();
        while (null != cc && cc != Object.class) {
            Method[] fs = cc.getDeclaredMethods();
            for (int i = 0; i < fs.length; i++) {
                String key = fs[i].getName()
                        + Mirror.getParamDescriptor(fs[i].getParameterTypes());
                if (!map.containsKey(key))
                    map.put(key, fs[i]);
            }
            cc = cc.getSuperclass() == top ? null : cc.getSuperclass();
        }
        return map.values().toArray(new Method[map.size()]);
    }

    /**
     * 相当于 getAllDeclaredMethods(Object.class)
     *
     * @return 方法数组
     */
    public Method[] getAllDeclaredMethodsWithoutTop() {
        return getAllDeclaredMethods(Object.class);
    }

    /**
     * 为对象的一个字段设值。 优先调用对象的 setter，如果没有，直接设置字段的值<br />
     * 值必须与字段类型匹配
     *
     * @param obj
     *            对象
     * @param field
     *            字段
     * @param value
     *            值。如果为 null，字符和数字字段，都会设成 0
     */
    public void setValue(Object obj, Field field, Object value) {
        if (!field.isAccessible())
            field.setAccessible(true);
        Class<?> ft = field.getType();
        // 非 null 值，进行转换
        if (null != value) {
            value = castTo(value, field.getType());
        }
        // 如果是原生类型，转换成默认值
        if (null == value && ft.isPrimitive()) {
            if (boolean.class == ft) {
                value = false;
            } else if (char.class == ft) {
                value = (char) 0;
            } else {
                value = (byte) 0;
            }
        }
        try {
            this.getSetter(field).invoke(obj, value);
        } catch (Exception e1) {
            try {
                field.set(obj, value);
            } catch (Exception e) {
                throw new RuntimeException(String.format(
                        "Fail to set value [%s] to [%s]->[%s] because '%s'",
                        value, obj.getClass().getName(), field.getName(),
                        e.getMessage()), e);
            }
        }
    }

    /**
     * 为对象的一个字段设值。优先调用 setter 方法。
     *
     * @param obj
     *            对象
     * @param fieldName
     *            字段名
     * @param value
     *            值
     */
    public void setValue(Object obj, String fieldName, Object value) {
        if (null == value) {
            try {
                setValue(obj, this.getField(fieldName), value);
            } catch (Exception e) {
                throw new RuntimeException(String.format(
                        "Fail to set value [%s] to [%s]->[%s] because '%s'",
                        value, obj.getClass().getName(), fieldName,
                        e.getMessage()), e);
            }
        } else {
            try {
                this.getSetter(fieldName, value.getClass()).invoke(obj, value);
            } catch (Exception e) {
                try {
                    setValue(obj, this.getField(fieldName), value);
                } catch (Exception e1) {
                    throw new RuntimeException(
                            String.format(
                                    "Fail to set value [%s] to [%s]->[%s] because '%s'",
                                    value, obj.getClass().getName(), fieldName,
                                    e.getMessage()), e1);
                }
            }
        }
    }

    /**
     * 不调用 getter，直接获得字段的值
     *
     * @param obj
     *            对象
     * @param file
     *            字段
     * @return 字段的值
     */
    public Object getValue(Object obj, Field file) {
        if (!file.isAccessible())
            file.setAccessible(true);
        try {
            return file.get(obj);
        } catch (Exception e) {
            throw new RuntimeException(String.format(
                    "Fail to get value for [%s]->[%s]", obj.getClass(),
                    file.getName()), e);
        }
    }

    /**
     * 优先通过 getter 获取字段值，如果没有，则直接获取字段值
     *
     * @param obj
     *            对象
     * @param name
     *            字段名
     * @return 字段值
     */
    public Object getValue(Object obj, String name) {
        try {
            return this.getGetter(name).invoke(obj);
        } catch (Exception e) {
            try {
                return getValue(obj, getField(name));
            } catch (NoSuchFieldException e1) {
                throw new RuntimeException(String.format(
                        "Fail to get value for [%s]->[%s]", obj.getClass(),
                        name), e);
            }
        }
    }

    /**
     * 返回类型
     *
     * @return 对象类型
     */
    public Class<T> getType() {
        return clazz;
    }

    /**
     * @return 获得外覆类
     *
     */
    public Class<?> getWrapperClass() {
        if (!clazz.isPrimitive()) {
            if (this.isPrimitiveNumber() || this.is(Boolean.class)
                    || this.is(Character.class))
                return clazz;
            return null;
        }
        // 用散列能快一点
        if (is(int.class))
            return Integer.class;
        if (is(char.class))
            return Character.class;
        if (is(boolean.class))
            return Boolean.class;
        if (is(long.class))
            return Long.class;
        if (is(float.class))
            return Float.class;
        if (is(byte.class))
            return Byte.class;
        if (is(short.class))
            return Short.class;
        if (is(double.class))
            return Double.class;

        return null;
    }

    /**
     * @return 获得外覆类，如果没有外覆类，则返回自身的类型
     */
    public Class<?> getWrapper() {
        if (clazz.isPrimitive())
            return getWrapperClass();
        return clazz;
    }

    /**
     * 判断当前对象是否为一个类型。精确匹配，即使是父类和接口，也不相等
     *
     * @param type
     *            类型
     * @return 是否相等
     */
    public boolean is(Class<?> type) {
        return null != type && clazz == type;
    }

    /**
     * 判断当前对象是否为一个类型。精确匹配，即使是父类和接口，也不相等
     *
     * @param className
     *            类型名称
     * @return 是否相等
     */
    public boolean is(String className) {
        return clazz.getName().equals(className);
    }

    /**
     * @param type
     *            类型或接口名
     * @return 当前对象是否为一个类型的子类，或者一个接口的实现类
     */
    public boolean isOf(Class<?> type) {
        return type.isAssignableFrom(clazz);
    }

    /**
     * @return 当前对象是否为字符串
     */
    public boolean isString() {
        return is(String.class);
    }

    /**
     * @return 当前对象是否为CharSequence的子类
     */
    public boolean isStringLike() {
        return CharSequence.class.isAssignableFrom(clazz);
    }

    /**
     * @return 当前对象是否为字符
     */
    public boolean isChar() {
        return is(char.class) || is(Character.class);
    }

    /**
     * @return 当前对象是否为枚举
     */
    public boolean isEnum() {
        return clazz.isEnum();
    }

    /**
     * @return 当前对象是否为布尔
     */
    public boolean isBoolean() {
        return is(boolean.class) || is(Boolean.class);
    }

    /**
     * @return 当前对象是否为浮点
     */
    public boolean isFloat() {
        return is(float.class) || is(Float.class);
    }

    /**
     * @return 当前对象是否为双精度浮点
     */
    public boolean isDouble() {
        return is(double.class) || is(Double.class);
    }

    /**
     * @return 当前对象是否为整型
     */
    public boolean isInt() {
        return is(int.class) || is(Integer.class);
    }

    /**
     * @return 当前对象是否为整数（包括 int, long, short, byte）
     */
    public boolean isIntLike() {
        return isInt() || isLong() || isShort() || isByte()
                || is(BigDecimal.class);
    }

    /**
     * @return 当前对象是否为小数 (float, dobule)
     */
    public boolean isDecimal() {
        return isFloat() || isDouble();
    }

    /**
     * @return 当前对象是否为长整型
     */
    public boolean isLong() {
        return is(long.class) || is(Long.class);
    }

    /**
     * @return 当前对象是否为短整型
     */
    public boolean isShort() {
        return is(short.class) || is(Short.class);
    }

    /**
     * @return 当前对象是否为字节型
     */
    public boolean isByte() {
        return is(byte.class) || is(Byte.class);
    }

    /**
     * @param type
     *            类型
     * @return 否为一个对象的外覆类
     */
    public boolean isWrapperOf(Class<?> type) {
        try {
            return Mirror.me(type).getWrapperClass() == clazz;
        } catch (Exception e) {
        }
        return false;
    }

    /**
     * @param type
     *            目标类型
     * @return 判断当前对象是否能直接转换到目标类型，而不产生异常
     */
    public boolean canCastToDirectly(Class<?> type) {
        if (clazz == type || type.isAssignableFrom(clazz))
            return true;
        if (clazz.isPrimitive() && type.isPrimitive()) {
            if (this.isPrimitiveNumber() && Mirror.me(type).isPrimitiveNumber())
                return true;
        }
        try {
            return Mirror.me(type).getWrapperClass() == this.getWrapperClass();
        } catch (Exception e) {
        }
        return false;
    }

    /**
     * @return 当前对象是否为原生的数字类型 （即不包括 boolean 和 char）
     */
    public boolean isPrimitiveNumber() {
        return isInt() || isLong() || isFloat() || isDouble() || isByte()
                || isShort();
    }

    /**
     * @return 当前对象是否为数字
     */
    public boolean isNumber() {
        return Number.class.isAssignableFrom(clazz) || clazz.isPrimitive()
                && !is(boolean.class) && !is(char.class);
    }

    /**
     * @return 当前对象是否在表示日期或时间
     */
    public boolean isDateTimeLike() {
        return Calendar.class.isAssignableFrom(clazz)
                || Date.class.isAssignableFrom(clazz)
                || java.sql.Date.class.isAssignableFrom(clazz)
                || java.sql.Time.class.isAssignableFrom(clazz);
    }

    public String toString() {
        return clazz.getName();
    }

    /**
     * 返回参数的描述符
     *
     * @param parameterTypes
     *            函数的参数类型数组
     * @return 描述符
     */
    private static String getParamDescriptor(Class<?>[] parameterTypes) {
        StringBuilder sb = new StringBuilder();
        sb.append('(');
        for (Class<?> pt : parameterTypes)
            sb.append(getTypeDescriptor(pt));
        sb.append(')');
        String s = sb.toString();
        return s;
    }

    /**
     * 返回类对应的包路径
     *
     * @param clazz
     *            类型
     * @return 包路径
     */
    public static String getPath(Class<?> clazz) {
        return clazz.getName().replace('.', '/');
    }

    /**
     * @param clazz
     *            类型
     * @return 获得一个类型的描述符
     */
    private static String getTypeDescriptor(Class<?> clazz) {
        if (clazz.isPrimitive()) {
            if (clazz == void.class)
                return "V";
            else if (clazz == int.class)
                return "I";
            else if (clazz == long.class)
                return "J";
            else if (clazz == byte.class)
                return "B";
            else if (clazz == short.class)
                return "S";
            else if (clazz == float.class)
                return "F";
            else if (clazz == double.class)
                return "D";
            else if (clazz == char.class)
                return "C";
            else
				/* if(clazz == boolean.class) */
                return "Z";
        }
        StringBuilder sb = new StringBuilder();
        if (clazz.isArray()) {
            return sb.append('[')
                    .append(getTypeDescriptor(clazz.getComponentType()))
                    .toString();
        }
        return sb.append('L').append(Mirror.getPath(clazz)).append(';')
                .toString();
    }

    /**
     * 查找包含某一个特殊注解的字段
     *
     * @param type
     *            类型
     * @param ann
     *            注解类型
     * @return 字段，null 表示没有找到
     */
    public static Field findField(Class<?> type, Class<? extends Annotation> ann) {
        Mirror<?> mirror = Mirror.me(type);
        for (Field f : mirror.getFields())
            if (f.isAnnotationPresent(ann))
                return f;
        return null;
    }

    /**
     * 自动给传入对象属性赋值
     *
     * @param obj
     *            赋值对象
     * @param properties
     *            属性值
     */
    public static void populateBean(Object obj, Map<String, Object> properties) {
        if (properties == null) {
            return;
        }
        Mirror<?> mirror = Mirror.me(obj);
        for (Field f : mirror.getFields()) {
            if (properties.get(f.getName()) != null
                    && !properties.get(f.getName()).equals("")) {// 如果为NULL或者空字符串则为初始值
                mirror.setValue(obj, f, properties.get(f.getName()));
            }
        }
    }

    /**
     * 转换一个 POJO 到另外的类型
     *
     * @param src
     *            源对象
     * @param toType
     *            目标类型
     * @return 目标对象
     */
    @SuppressWarnings("hiding")
    public <T> T castTo(Object src, Class<T> toType) {
        return cast(src, null == src ? null : src.getClass(), toType);
    }

    /**
     * 转换一个 POJO 从一个指定的类型到另外的类型
     *
     * @param src
     *            源对象
     * @param fromType
     *            源对象类型
     * @param toType
     *            目标类型
     * @return 目标对象
     */
    @SuppressWarnings("unchecked")
    public static <F, T> T cast(Object src, Class<F> fromType, Class<T> toType) {
        if (null == src) {
            // 原生数据的默认值
            if (toType.isPrimitive()) {
                if (toType == int.class)
                    return (T) Integer.valueOf(0);
                else if (toType == long.class)
                    return (T) Long.valueOf(0L);
                else if (toType == byte.class)
                    return (T) Byte.valueOf((byte) 0);
                else if (toType == short.class)
                    return (T) Short.valueOf((short) 0);
                else if (toType == float.class)
                    return (T) Float.valueOf(.0f);
                else if (toType == double.class)
                    return (T) Double.valueOf(.0);
                else if (toType == boolean.class)
                    return (T) Boolean.FALSE;
                else if (toType == char.class)
                    return (T) Character.valueOf(' ');
                throw new RuntimeException("类型转换出错");
            }
            // 是对象，直接返回 null
            return null;
        }
        if (fromType == toType || toType == null || fromType == null)
            return (T) src;
        if (toType == BigDecimal.class && fromType == String.class)
            return (T) new BigDecimal(src.toString());
        if (toType == Integer.class && fromType == String.class)
            return (T) Integer.valueOf(src.toString());
        if (toType == Long.class && fromType == String.class)
            return (T) Long.valueOf(src.toString());
        if (toType == Date.class && fromType == String.class)
            return null;
        if (toType == Double.class && fromType == String.class)
            return (T) Double.valueOf(src.toString());
        if (toType == BigDecimal.class && fromType == Integer.class)
            return (T) new BigDecimal(src.toString());
        if (toType == Long.class && fromType == Integer.class)
            return (T) Long.valueOf(src.toString());
        if (toType == BigDecimal.class && fromType == Double.class)
            return (T) new BigDecimal(src.toString());
        if (toType == BigDecimal.class && fromType == Long.class)
            return (T) new BigDecimal(src.toString());
        if (toType == Integer.class && fromType == BigDecimal.class)
            return (T) Integer.valueOf(((BigDecimal) src).intValue());
        if (toType == Long.class && fromType == BigDecimal.class)
            return (T) Long.valueOf(((BigDecimal) src).longValue());
        if (fromType.getName().equals(toType.getName()))
            return (T) src;
        if (toType.isAssignableFrom(fromType))
            return (T) src;
        Mirror<?> from = Mirror.me(fromType);
        if (from.canCastToDirectly(toType))
            return (T) src;
        return null;
    }

    /**
     * 复制同一个类的对象的属性值到另一个对象中
     *
     * @param fromObject
     *            源对象
     * @return toObject 目标对象
     */
    public static Object clone(Object fromObject) {
        Object toObject;
        try {
            Class<?> fromClassType = fromObject.getClass();
            toObject = fromClassType.newInstance();
            Class<?> toClassType = toObject.getClass();
            Field[] fields = fromClassType.getDeclaredFields();
            for (int j = 0; j < fields.length; j++) {
                Field field = fields[j];
                String fieldName = field.getName();
                if ("serialVersionUID".equals(fieldName))
                    continue;
                String stringLetter = fieldName.substring(0, 1).toUpperCase();
                // 得到get,set属性方法的名字
                String getMethodName = "get" + stringLetter
                        + fieldName.substring(1);
                String setMethodName = "set" + stringLetter
                        + fieldName.substring(1);

                Method getMethod = fromClassType.getMethod(getMethodName,
                        new Class[] {});
                Method setMethod = toClassType.getMethod(setMethodName,
                        new Class[] { field.getType() });
                Object value = getMethod.invoke(fromObject, new Object[] {});
                // 通过反射给指定方法名赋值
                System.out.println(fieldName);
                setMethod.invoke(toObject, new Object[] { value });
            }
        } catch (Exception e) {
            throw new RuntimeException("转换对象时发生异常" + e.getMessage());
        }

        return toObject;

    }

    /**
     * 返回包装类对应的基本类型
     *
     * @return 基本类型
     */
    public Class<?> unWrapper() {
        return TypeMapping2.get(clazz);
    }

    private static final Map<Class<?>, Class<?>> TypeMapping2 = new HashMap<Class<?>, Class<?>>();
    static {

        TypeMapping2.put(Short.class, short.class);
        TypeMapping2.put(Integer.class, int.class);
        TypeMapping2.put(Long.class, long.class);
        TypeMapping2.put(Double.class, double.class);
        TypeMapping2.put(Float.class, float.class);
        TypeMapping2.put(Byte.class, byte.class);
        TypeMapping2.put(Character.class, char.class);
        TypeMapping2.put(Boolean.class, boolean.class);
    }

    /**
     * 将对象转换为Map
     *
     * @param obj
     *            对象
     * @return 转换后的Map
     */
    public static Map<String, Object> objectToMap(Object obj) {
        Mirror<Object> mir = Mirror.me(obj);
        Map<String, Object> m = new HashMap<String, Object>();
        for (Field f : mir.getFields()) {
            m.put(f.getName(), mir.getValue(obj, f));
        }
        return m;
    }

    /**
     * 深克隆对象
     *
     * @param obj
     *            对象
     * @return 对象
     */
    @SuppressWarnings("unchecked")
    public static <T> T deepClone(T obj) {
        Object newObj = null;
        try {
            ByteArrayOutputStream bo = new ByteArrayOutputStream();
            ObjectOutputStream oo = new ObjectOutputStream(bo);
            oo.writeObject(obj);
            ByteArrayInputStream bi = new ByteArrayInputStream(bo.toByteArray());
            ObjectInputStream oi = new ObjectInputStream(bi);
            newObj = oi.readObject();
        } catch (Exception e) {
            newObj = null;
            throw new RuntimeException("深克隆对象出错", e);
        }
        return (T) newObj;
    }

    /**
     * 根据Class创建对象
     *
     * @param <F>
     * @param type
     * @return
     */
    public static <F> F newInstance(Class<F> type) {
        try {
            return type.newInstance();
        } catch (Exception e) {
            throw new RuntimeException("根据Class创建对象出错", e);
        }
    }

    /**
     * 获取非Object的父类
     *
     * @param <F>
     * @param type
     * @return
     */
    public static <F> Class<?> getParentClass(Class<F> type,
                                              Class<?> parentClass) {
        Class<?> cc = type;
        while (null != cc && cc.getSuperclass() != parentClass) {
            cc = cc.getSuperclass();
        }
        return cc;
    }

    /**
     * 简单属性解析
     *
     * @param clazz
     * @param o
     * @return
     */
    public static Object parseObj(Class<?> clazz, Object o) throws Exception {

        if (clazz == BigDecimal.class) {
            return new BigDecimal(o.toString());
        }

        if (clazz == Date.class) {
            return DateUtity.StrToDateTime(o.toString());
        }
        if (clazz == Long.class) {
            return Long.valueOf(o.toString());
        }
        if(clazz == Integer.class){
            return Integer.valueOf(o.toString());
        }

        if(o.getClass().isArray() && clazz.isArray()){//如果值是数组，目标属性类型也是数组
            return o;//直接返回
        }

        if(o.getClass().isArray() && clazz.isAssignableFrom(List.class)){//如果值是对象，目标属性是List
            List<Object> value = new ArrayList<Object>();
            int size = Array.getLength(o);
            for (int i = 0; i < size; i++) {
                value.add(Array.get(o, i));
            }
            return value;
        }
        return o;
   }

     /**
     *  循环获取对象下的所有属性，包括继承自父类的
     * @param clazz
     * @param noStatic
     * @param noMember
     * @param noFinal
     * @param noInner
     * @return
     */
    public static Field[] getAllFields(Class<?> clazz, boolean noStatic,
                                       boolean noMember, boolean noFinal, boolean noInner) {

        Map<String, Field> map = new LinkedHashMap<String, Field>();
        while (null != clazz && clazz != Object.class) {
            Field[] fs = clazz.getDeclaredFields();
            for (int i = 0; i < fs.length; i++) {
                Field f = fs[i];
                int m = f.getModifiers();
                if (noStatic && Modifier.isStatic(m))
                    continue;
                if (noFinal && Modifier.isFinal(m))
                    continue;
                if (noInner && f.getName().startsWith("this$"))
                    continue;
                if (noMember && !Modifier.isStatic(m))
                    continue;
                if (map.containsKey(fs[i].getName()))
                    continue;

                map.put(fs[i].getName(), fs[i]);
            }
            clazz = clazz.getSuperclass();
        }
        return map.values().toArray(new Field[map.size()]);
    }

    /**
     * 循环获取对象下的所有非static非final非inner的属性，包括继承自父类的
     * @param clazz
     * @return
     */
    public static Field[] getAllFields(Class<?> clazz) {

        return getAllFields(clazz, true, false, true, true);
    }

}