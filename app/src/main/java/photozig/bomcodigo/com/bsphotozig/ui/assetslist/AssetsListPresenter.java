package photozig.bomcodigo.com.bsphotozig.ui.assetslist;


import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.webkit.MimeTypeMap;

import java.io.IOException;
import java.util.List;

import photozig.bomcodigo.com.bsphotozig.database.controller.AssetModelController;
import photozig.bomcodigo.com.bsphotozig.network.api.PhotoZigApi;
import photozig.bomcodigo.com.bsphotozig.network.api.model.AssetModel;
import photozig.bomcodigo.com.bsphotozig.network.api.model.AssetPayload;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class AssetsListPresenter implements AssetsListContract.AssetsListActionsListener {
    private AssetsListContract.View mView;
    private Retrofit mRetrofit;
    private AssetModelController mAssetModelController;

    @Override
    public void init(AssetsListContract.View view,Retrofit retrofit, AssetModelController assetModelController) {
        this.mView = view;
        this.mRetrofit = retrofit;
        this.mAssetModelController = assetModelController;
    }

    @Override
    public void loadAssets() {
        mView.setProgressIndicator(true);
        Call<AssetPayload> assetPayloadCall = mRetrofit.create(PhotoZigApi.class).getAssets();
        assetPayloadCall.enqueue(new Callback<AssetPayload>() {
            @Override
            public void onResponse(Call<AssetPayload> call, Response<AssetPayload> response) {
                if (response.isSuccessful()){
                    mView.setProgressIndicator(false);

                    String assetsLocation = response.body().getAssetsLocation();
                    List<AssetModel> list = response.body().getObjects();
                    mView.showAssets(assetsLocation,list);
                }else{
                    try {
                        mView.showMessage(response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void onFailure(Call<AssetPayload> call, Throwable t) {
                mView.setProgressIndicator(false);
                mView.showMessage(t.getMessage());

            }
        });
    }

    @Override
    public void startDownload(Context context, String assetsLocation, AssetModel assetModel) {
        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        DownloadManager.Request videoRequest = getVideoRequest(context, assetsLocation, assetModel);
        DownloadManager.Request audioRequest = getAudioRequest(context,assetsLocation,assetModel);

        downloadManager.enqueue(videoRequest);
        downloadManager.enqueue(audioRequest);

        mAssetModelController.insert(assetModel.getName(),
                assetModel.getIm(),
                assetModel.getTxts());
    }

    private DownloadManager.Request getVideoRequest(Context context, String assetsLocation, AssetModel assetModel){
        String videoUrl = assetsLocation + "/" + assetModel.getBg();
        String extension = MimeTypeMap.getFileExtensionFromUrl(videoUrl);

        return new DownloadManager.Request(Uri.parse(videoUrl))
                .setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI| DownloadManager.Request.NETWORK_MOBILE)
                .setTitle(assetModel.getName())
                .setMimeType(MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension))
                .setDescription("video")
                .setDestinationInExternalFilesDir(context, Environment.DIRECTORY_DOWNLOADS,assetModel.getBg());
    }

    private DownloadManager.Request getAudioRequest(Context context, String assetsLocation, AssetModel assetModel) {
        String audioUrl = assetsLocation + "/" + assetModel.getSg();
        String extension = MimeTypeMap.getFileExtensionFromUrl(audioUrl);

        return new DownloadManager.Request(Uri.parse(audioUrl))
                .setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI| DownloadManager.Request.NETWORK_MOBILE)
                .setTitle(assetModel.getName())
                .setMimeType(MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension))
                .setDescription("audio")
                .setDestinationInExternalFilesDir(context, Environment.DIRECTORY_DOWNLOADS,assetModel.getSg());
    }

    @Override
    public void playAsset(String assetsLocation, AssetModel assetModel) {
        mView.showPlayerUi(assetsLocation,assetModel);

    }


}
