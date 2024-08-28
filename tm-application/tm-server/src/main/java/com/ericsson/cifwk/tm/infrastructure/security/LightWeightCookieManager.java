package com.ericsson.cifwk.tm.infrastructure.security;

import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.servlet.SimpleCookie;

/**
 * Created by egergle on 13/01/2016.
 */
public class LightWeightCookieManager extends CookieRememberMeManager {
    public LightWeightCookieManager() {
        super();
        SimpleCookie cookie = new SimpleCookie("rememberMe");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(604800); // 1 week
        super.setCookie(cookie);

        setSerializer(new CookieSimpleSerializer());
    }
}
