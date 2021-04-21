package com.jinxin.kuange.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by hongkuan on 2021-04-20 0020.
 * 作用在注解上的注解
 * 保存事件类型和事件的接口class
 */
@Target(ElementType.ANNOTATION_TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface EvenType {
    /**
     * 监听的接口class
     * @return
     */
    Class listenerType();

    /**
     * 通过什么方法设置上面的接口类
     * @return
     */
    String listenerSetter();
}
