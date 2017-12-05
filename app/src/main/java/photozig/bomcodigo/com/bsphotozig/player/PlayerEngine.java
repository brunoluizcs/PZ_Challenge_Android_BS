package photozig.bomcodigo.com.bsphotozig.player;


import android.content.Context;

import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;

import photozig.bomcodigo.com.bsphotozig.database.controller.AssetModelController;
import photozig.bomcodigo.com.bsphotozig.network.api.model.AssetModel;

public class PlayerEngine {
    private MediaSourceBuilder mMediaSourceBuilder;
    private Context mContext;
    private AssetModelController mAssetModelController;

    public PlayerEngine(Context context, AssetModelController assetModelController){
        mContext = context;
        mAssetModelController = assetModelController;
    }

    public SimpleExoPlayer createAudioPlayer(String assetsLocation, AssetModel assetModel){
        mMediaSourceBuilder = MediaSourceBuilderFactory.getMediaSource(mAssetModelController, assetModel);
        MediaSource mediaSource = mMediaSourceBuilder.getAudioMediaSource(mContext,assetsLocation,assetModel);

        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveTrackSelection.Factory(bandwidthMeter);
        TrackSelector trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);

        SimpleExoPlayer player = ExoPlayerFactory.newSimpleInstance(mContext, trackSelector);
        player.prepare(mediaSource);
        player.setPlayWhenReady(true);
        return player;
    }

    public SimpleExoPlayer createVideoPlayer(String assetsLocation, AssetModel assetModel){
        mMediaSourceBuilder = MediaSourceBuilderFactory.getMediaSource(mAssetModelController, assetModel);
        MediaSource mediaSource = mMediaSourceBuilder.getVideoMediaSource(mContext,assetsLocation,assetModel);

        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveTrackSelection.Factory(bandwidthMeter);
        TrackSelector trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);

        SimpleExoPlayer player = ExoPlayerFactory.newSimpleInstance(mContext, trackSelector);
        player.prepare(mediaSource);
        player.setPlayWhenReady(true);
        return player;
    }
}
