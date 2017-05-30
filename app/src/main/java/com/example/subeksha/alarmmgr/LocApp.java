package com.example.subeksha.alarmmgr;

import android.app.Application;

import com.facebook.stetho.Stetho;
import com.uphyca.stetho_realm.RealmInspectorModulesProvider;

import io.realm.Realm;

/**
 * Created by Subeksha on 5/29/17.
 */

public class LocApp extends Application {
    @Override
    public void onCreate(){
        super.onCreate();
        Realm.init(this);
        if (BuildConfig.DEBUG){
            Stetho.initialize(
                    Stetho.newInitializerBuilder(this)
                    .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                    .enableWebKitInspector(RealmInspectorModulesProvider.builder(this).build())
                    .build()
            );
        }

    }

}
