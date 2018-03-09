package code.allen.sdkarticle.annotationIOC.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by allenni on 2018/3/8.
 */
@Target(ElementType.ANNOTATION_TYPE)
@Retention(RetentionPolicy.RUNTIME)
// 加在注解上的注解
public @interface EventBase {

    Class<?> listenerType();
    String listenerSetter();
    String methodName();
}
