package photozig.bomcodigo.com.bsphotozig.application;

import android.app.Application;

import photozig.bomcodigo.com.bsphotozig.application.module.AppModule;
import photozig.bomcodigo.com.bsphotozig.database.component.DaggerDatabaseComponent;
import photozig.bomcodigo.com.bsphotozig.database.component.DatabaseComponent;
import photozig.bomcodigo.com.bsphotozig.database.module.DatabaseModule;
import photozig.bomcodigo.com.bsphotozig.network.component.DaggerNetworkComponent;
import photozig.bomcodigo.com.bsphotozig.network.component.NetworkComponent;
import photozig.bomcodigo.com.bsphotozig.network.module.NetworkModule;


public class App extends Application {
    private NetworkComponent mNetworkComponent;
    private DatabaseComponent mDatabaseComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        mNetworkComponent = DaggerNetworkComponent.builder()
                .appModule(new AppModule(this))
                .networkModule(new NetworkModule("http://pbmedia.pepblast.com/"))
                .build();

        mDatabaseComponent = DaggerDatabaseComponent.builder()
                .appModule(new AppModule(this))
                .databaseModule(new DatabaseModule())
                .build();

    }

    public NetworkComponent getNetComponent() {
        return mNetworkComponent;
    }

    public DatabaseComponent getDatabaseComponent() {
        return mDatabaseComponent;
    }
}
