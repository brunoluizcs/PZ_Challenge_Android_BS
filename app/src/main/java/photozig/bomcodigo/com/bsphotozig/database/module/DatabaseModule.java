package photozig.bomcodigo.com.bsphotozig.database.module;


import android.app.Application;
import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import photozig.bomcodigo.com.bsphotozig.database.AssetDbHelper;
import photozig.bomcodigo.com.bsphotozig.database.controller.AssetModelController;

@Module
public class DatabaseModule {

    public DatabaseModule() {

    }

    @Provides
    @Singleton
    AssetDbHelper provideDbHelper(Application application) {
        return new AssetDbHelper(application);
    }

    @Provides
    @Singleton
    AssetModelController provideOkhttpClient(AssetDbHelper dbHelper) {
        return new AssetModelController(dbHelper);
    }


}




