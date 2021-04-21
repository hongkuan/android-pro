package com.jinxin.kuange.utils;

import android.app.Activity;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextUtils;
import android.view.View;

import com.jinxin.kuange.annotations.Autowired;
import com.jinxin.kuange.annotations.EvenType;
import com.jinxin.kuange.annotations.FindViewById;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;

/**
 * Created by hongkuan on 2021-04-14 0014.
 */
public class InjectUtil {

    public static void findViewAll(Activity activity){
        Class<? extends Activity> cls = activity.getClass();
        Field[] fields = cls.getDeclaredFields();//获取所有当前类中成员包括private
        //cls.getFields();//获取所有类中成员包括父类不包括private

        for (Field field: fields) {
            if (field.isAnnotationPresent(FindViewById.class)){
                int resId = field.getAnnotation(FindViewById.class).value();
                View view = activity.findViewById(resId);
                try {
                    field.setAccessible(true);
                    field.set(activity,view);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }

                //通过反射调用对象方法
                /*try {
                    Method method = cls.getMethod("findViewById", int.class);
                    method.setAccessible(true);
                    Object view = method.invoke(activity, resId);
                    field.setAccessible(true);
                    field.set(activity, view);
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }*/
            }
        }
    }

    public static void getIntentData(Activity activity){
        Class<? extends Activity> clzActivity = activity.getClass();
        Field[] fields = clzActivity.getDeclaredFields();
        //获取到数据
        Bundle extras = activity.getIntent().getExtras();
        if (null == extras) return;

        for (Field field : fields) {
            if (field.isAnnotationPresent(Autowired.class)){
                Autowired annotation = field.getAnnotation(Autowired.class);
                //获取到key
                String key = TextUtils.isEmpty(annotation.value())? field.getName() : annotation.value();

                if (extras.containsKey(key)){
                    //获取到数据
                    Object obj = extras.get(key);
                    //针对特殊的Parcelable数组处理
                    Class<?> componentType = field.getType().getComponentType();
                    if (field.getType().isArray() && Parcelable.class.isAssignableFrom(componentType)){
                        Object[] objs = (Object[]) obj;
                        Object[] objects = Arrays.copyOf(objs, objs.length, (Class<? extends Object[]>) field.getType());
                        obj = objects;
                    }

                    //设置属性值
                    field.setAccessible(true);
                    try {
                        field.set(activity, obj);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public static void injectEven(Activity activity){
        Class<? extends Activity> activityClz = activity.getClass();
        Method[] methods = activityClz.getDeclaredMethods();
        for (Method method : methods) {//变量activity所有方法
            //获取方法上所有注解
            Annotation[] annotations = method.getAnnotations();
            for (Annotation annotation : annotations) {
                Class<? extends Annotation> annotationType = annotation.annotationType();
                if (annotationType.isAnnotationPresent(EvenType.class)){
                    EvenType evenType = annotationType.getAnnotation(EvenType.class);
                    Class clz = evenType.listenerType();
                    String setter = evenType.listenerSetter();

                    try {
                        Method annotationMethod = annotationType.getDeclaredMethod("value");
                        int[] viewIds = (int[]) annotationMethod.invoke(annotation);

                        method.setAccessible(true);
                        //绑定实现类和接口方法
                        ListenerInvocationHandler<Activity> listenerInvocationHandler = new ListenerInvocationHandler<>(activity, method);
                        //获取到实现接口动态代理对象
                        Object listenerProxyObj = Proxy.newProxyInstance(clz.getClassLoader(),
                                new Class[]{clz}, listenerInvocationHandler);
                        //遍历每一个view设置代理接口
                        for (int viewId : viewIds) {
                            //获取到当前activity的view
                            View view = activity.findViewById(viewId);
                            //获取view的setter方法
                            //如果是setter是setOnClickListener 则clz为对应的OnClickListener
                            //如果是setter是setOnLongClickListener 则clz为对应的OnLongClickListener
                            Method setterMethod = view.getClass().getMethod(setter, clz);
                            //调用view的set方法设置动态代理
                            setterMethod.invoke(view, listenerProxyObj);
                        }

                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    static class ListenerInvocationHandler<T> implements InvocationHandler {

        private final T target;
        private final Method method;

        public ListenerInvocationHandler(T target, Method method){
            this.target = target;
            this.method = method;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            return this.method.invoke(target, args);
        }
    }
}
