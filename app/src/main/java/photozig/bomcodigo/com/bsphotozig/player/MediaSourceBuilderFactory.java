package photozig.bomcodigo.com.bsphotozig.player;


import photozig.bomcodigo.com.bsphotozig.database.controller.AssetModelController;
import photozig.bomcodigo.com.bsphotozig.network.api.model.AssetModel;

public class MediaSourceBuilderFactory {

    static MediaSourceBuilder getMediaSource(AssetModelController assetModelController, AssetModel assetModel){
        return new DownloadedMediaSourceBuilder(assetModelController);
    }

}
