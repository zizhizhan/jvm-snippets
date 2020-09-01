package com.zizhizhan.util;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;

public abstract class TypeUtils {    
   
    public static boolean isAssignable(Type lhsType, Type rhsType) {
        assert (lhsType != null);
        assert (rhsType != null);
        if (lhsType.equals(rhsType)) {
            return true;
        }
        if (lhsType instanceof Class<?> && rhsType instanceof Class<?>) {
            return ClassUtils.isAssignable((Class<?>) lhsType, (Class<?>) rhsType);
        }
        if (lhsType instanceof ParameterizedType && rhsType instanceof ParameterizedType) {
            return isAssignable((ParameterizedType) lhsType, (ParameterizedType) rhsType);
        }
        if (lhsType instanceof WildcardType) {
            return isAssignable((WildcardType) lhsType, rhsType);
        }
        return false;
    }

    private static boolean isAssignable(ParameterizedType lhsType, ParameterizedType rhsType) {
        if (lhsType.equals(rhsType)) {
            return true;
        }
        Type[] lhsTypeArguments = lhsType.getActualTypeArguments();
        Type[] rhsTypeArguments = rhsType.getActualTypeArguments();
        if (lhsTypeArguments.length != rhsTypeArguments.length) {
            return false;
        }
        for (int size = lhsTypeArguments.length, i = 0; i < size; ++i) {
            Type lhsArg = lhsTypeArguments[i];
            Type rhsArg = rhsTypeArguments[i];
            if (!lhsArg.equals(rhsArg) &&
                    !(lhsArg instanceof WildcardType && isAssignable((WildcardType) lhsArg, rhsArg))) {
                return false;
            }
        }
        return true;
    }

    private static boolean isAssignable(WildcardType lhsType, Type rhsType) {
        Type[] upperBounds = lhsType.getUpperBounds();
        Type[] lowerBounds = lhsType.getLowerBounds();
        for (int size = upperBounds.length, i = 0; i < size; ++i) {
            if (!isAssignable(upperBounds[i], rhsType)) {
                return false;
            }
        }
        for (int size = lowerBounds.length, i = 0; i < size; ++i) {
            if (!isAssignable(rhsType, lowerBounds[i])) {
                return false;
            }
        }
        return true;
    }

}
