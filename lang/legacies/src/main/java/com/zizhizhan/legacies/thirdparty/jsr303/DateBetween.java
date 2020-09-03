package com.zizhizhan.legacies.thirdparty.jsr303;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zizhi.zhzzh
 * Date: 11/27/13
 * Time: 8:40 PM
 */

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.ANNOTATION_TYPE, ElementType.FIELD, ElementType.METHOD})
@Constraint(validatedBy = {})
public @interface DateBetween {

    public String message();

    public String startDate() default "null";

    public String endDate() default "null";

    public String format() default "yyyy-MM-dd";

    public Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
