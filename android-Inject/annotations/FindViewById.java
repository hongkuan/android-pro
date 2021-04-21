package com.jinxin.kuange.annotations;

import androidx.annotation.IdRes;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by hongkuan on 2021-04-14 0014.
 */
@Target(ElementType.FIELD)//该标签作用在成员上
@Retention(RetentionPolicy.RUNTIME)//注解保留时间在到运行时
public @interface FindViewById {
    @IdRes int value();//设置view的res Id.
}
