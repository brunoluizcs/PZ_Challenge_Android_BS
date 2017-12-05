package photozig.bomcodigo.com.bsphotozig.database.component;


import javax.inject.Singleton;

import dagger.Component;
import photozig.bomcodigo.com.bsphotozig.application.module.AppModule;
import photozig.bomcodigo.com.bsphotozig.database.module.DatabaseModule;
import photozig.bomcodigo.com.bsphotozig.ui.assetsplayer.AssetsPlayerFragment;

@Singleton
@Component(modules = {AppModule.class, DatabaseModule.class})
public interface DatabaseComponent {
    void inject(AssetsPlayerFragment fragment);


}
