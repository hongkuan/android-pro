package com.jinxin.kuange.annotations;

import android.view.View;

import androidx.annotation.IdRes;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 通过此注解给view添加OnClick监听
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@EvenType(listenerType = View.OnClickListener.class, listenerSetter = "setOnClickListener")
public @interface OnClick {
    @IdRes int[] value();
}
