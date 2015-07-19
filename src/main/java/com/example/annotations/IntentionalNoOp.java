package com.example.annotations;

public @interface IntentionalNoOp {
    String reason() default "";
}
