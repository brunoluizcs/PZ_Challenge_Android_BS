package photozig.bomcodigo.com.bsphotozig.network.component;


import javax.inject.Singleton;

import dagger.Component;
import photozig.bomcodigo.com.bsphotozig.application.module.AppModule;
import photozig.bomcodigo.com.bsphotozig.network.module.NetworkModule;
import photozig.bomcodigo.com.bsphotozig.ui.assetslist.AssetsListFragment;

@Singleton
@Component(modules = {AppModule.class, NetworkModule.class})
public interface NetworkComponent {
    void inject(AssetsListFragment fragment);
}
