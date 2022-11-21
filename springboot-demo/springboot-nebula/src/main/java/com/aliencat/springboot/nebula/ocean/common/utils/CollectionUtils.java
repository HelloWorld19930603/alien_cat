
package com.aliencat.springboot.nebula.ocean.common.utils;

import java.util.Collection;
import java.util.Map;

/**
 * Description  CollectionUtils is used for
 *
 * @author Anyzm
 * Date  2021/8/9 - 14:05
 * @version 1.0.0
 */
public class CollectionUtils extends org.springframework.util.CollectionUtils {

    public static int size(Map map) {
        return map == null ? 0 : map.size();
    }

    public static int size(Collection collection) {
        return collection == null ? 0 : collection.size();
    }

    public static <T> int size(T[] array) {
        return array == null ? 0 : array.length;
    }

    public static String[] toStringArray(Collection<String> collection) {
        String[] strings = new String[size(collection)];
        if (isEmpty(collection)) {
            return strings;
        }
        strings = collection.toArray(strings);
        return strings;
    }

}
