package photozig.bomcodigo.com.bsphotozig.player;


import android.content.Context;

import com.google.android.exoplayer2.source.MediaSource;

import photozig.bomcodigo.com.bsphotozig.network.api.model.AssetModel;

public interface MediaSourceBuilder {
    MediaSource getAudioMediaSource(Context context, String assetsLocation, AssetModel assetModel);
    MediaSource getVideoMediaSource(Context context, String assetsLocation, AssetModel assetModel);
}
