package com.pika.gstore.auth.utils;

import java.util.Set;

/**
 * Desc:
 *
 * @author pikachu
 * @since 2023/2/25 12:51
 */
public class UrlUtils {
    /**
     * 是否放行
     */
    private static boolean urlPass(String url, Set<String> set) {
        for (String s : set) {
            if (url.matches(s)) {
                return true;
            }
        }
        return false;
    }
}
