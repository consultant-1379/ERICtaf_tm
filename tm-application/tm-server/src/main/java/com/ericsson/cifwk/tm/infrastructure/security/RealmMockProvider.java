package com.ericsson.cifwk.tm.infrastructure.security;

import org.apache.shiro.config.Ini;
import org.apache.shiro.realm.text.IniRealm;

import javax.inject.Provider;

public class RealmMockProvider implements Provider<IniRealm> {

    @Override
    public IniRealm get() {
        return new IniRealm(Ini.fromResourcePath("classpath:shiro-mock.ini"));
    }

}
