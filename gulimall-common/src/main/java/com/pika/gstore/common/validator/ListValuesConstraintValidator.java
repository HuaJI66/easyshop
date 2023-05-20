package com.pika.gstore.common.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.HashSet;
import java.util.Set;

/**
 * Desc:    自定义校验器
 * <p>限制列出范围内的数字</p>
 *
 * @author pikachu
 * @since 2022/11/28 9:27
 */
public class ListValuesConstraintValidator implements ConstraintValidator<ListValues, Integer> {
    private final Set<Integer> set = new HashSet<>();

    @Override
    public void initialize(ListValues constraintAnnotation) {
        for (int value : constraintAnnotation.values()) {
            set.add(value);
        }
    }

    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        return set.contains(value);
    }
}
