package photozig.bomcodigo.com.bsphotozig.player;

import android.content.Context;
import android.net.Uri;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.LoopingMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.MergingMediaSource;
import com.google.android.exoplayer2.source.SingleSampleMediaSource;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.MimeTypes;

import photozig.bomcodigo.com.bsphotozig.database.controller.AssetModelController;
import photozig.bomcodigo.com.bsphotozig.network.api.model.AssetModel;


public class DownloadedMediaSourceBuilder implements MediaSourceBuilder {
    private AssetModelController mAssetModelController;

    public DownloadedMediaSourceBuilder(AssetModelController assetModelController){
        mAssetModelController = assetModelController;
    }

    @Override
    public MediaSource getAudioMediaSource(Context context, String assetsLocation, AssetModel assetModel) {
        AssetModel dbAssetModel = mAssetModelController.get(assetModel.getName());
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(context,"photoZigBs");
        ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();

        Uri audioUri = Uri.parse(assetsLocation + "/" + assetModel.getSg());
        if (mAssetModelController.hasAudioDownloaded(assetModel.getName())) {
            audioUri = Uri.parse(dbAssetModel.getSg());
        }

        MediaSource audioSource = new ExtractorMediaSource(audioUri,
                dataSourceFactory, extractorsFactory, null, null);

        return audioSource;
    }

    @Override
    public MediaSource getVideoMediaSource(Context context, String assetsLocation, AssetModel assetModel) {
        AssetModel dbAssetModel = mAssetModelController.get(assetModel.getName());
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(context,"photoZigBs");
        ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();

        Uri videoUri = Uri.parse(assetsLocation + "/" + assetModel.getBg());
        if (mAssetModelController.hasVideoDownloaded(assetModel.getName())){
            videoUri = Uri.parse(dbAssetModel.getBg());
        }

        MediaSource videoSource = new ExtractorMediaSource(videoUri,
                dataSourceFactory, extractorsFactory, null, null);
        LoopingMediaSource videoSourceLooping = new LoopingMediaSource(videoSource);

        return videoSourceLooping;
    }
}
