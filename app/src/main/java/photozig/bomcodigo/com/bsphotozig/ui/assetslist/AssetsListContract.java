package photozig.bomcodigo.com.bsphotozig.ui.assetslist;


import android.content.Context;

import java.util.List;

import photozig.bomcodigo.com.bsphotozig.database.controller.AssetModelController;
import photozig.bomcodigo.com.bsphotozig.network.api.model.AssetModel;
import retrofit2.Retrofit;

public class AssetsListContract {

    interface View {

        void setProgressIndicator(boolean active);

        void showAssets(String assetsLocation, List<AssetModel> assets);

        void showPlayerUi(String assetsLocation, AssetModel assetModel);

        void showMessage(String message);

    }

    interface AssetsListActionsListener {

        void init(AssetsListContract.View view, Retrofit retrofit, AssetModelController assetModelController);

        void loadAssets();

        void startDownload(Context context, String assetsLocation, AssetModel assetModel);

        void playAsset(String assetsLocation, AssetModel assetModel);
    }
}
