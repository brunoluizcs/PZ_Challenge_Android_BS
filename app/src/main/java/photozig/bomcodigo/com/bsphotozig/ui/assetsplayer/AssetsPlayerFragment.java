package photozig.bomcodigo.com.bsphotozig.ui.assetsplayer;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.PlaybackControlView;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import photozig.bomcodigo.com.bsphotozig.R;
import photozig.bomcodigo.com.bsphotozig.application.App;
import photozig.bomcodigo.com.bsphotozig.database.controller.AssetModelController;
import photozig.bomcodigo.com.bsphotozig.network.api.model.AssetModel;
import photozig.bomcodigo.com.bsphotozig.player.PlayerEngine;

import static photozig.bomcodigo.com.bsphotozig.ui.assetsplayer.AssetsPlayerActivity.EXTRA_ASSET_LOCATION;
import static photozig.bomcodigo.com.bsphotozig.ui.assetsplayer.AssetsPlayerActivity.EXTRA_ASSET_MODEL;


public class AssetsPlayerFragment extends Fragment implements
        AssetsPlayerContract.View{

    
    public static final String TAG = AssetsPlayerFragment.class.getSimpleName();
    private static final String EXTRA_VIDEO_CURRENT_POSITION = "extra-video-current-position";
    private static final String EXTRA_AUDIO_CURRENT_POSITION = "extra-audio-current-position";

    @BindView(R.id.audio_player_display) SimpleExoPlayerView mAudioPlayerView;
    @BindView(R.id.video_player_display) SimpleExoPlayerView mVideoPlayerView;
    @BindView(R.id.loading_indicator)View mLoadingIndicatorView;
    @BindView(R.id.root_container) View mRootContainer;
    @BindView(R.id.controller_view) PlaybackControlView mControllerView;


    private SimpleExoPlayer mAudioPlayer;
    private SimpleExoPlayer mVideoPlayer;

    @Inject
    AssetModelController mAssetModelController;

    private long mAudioCurrentPosition;
    private long mVideoCurrentPostion;

    private String mAssetsLocation;
    private AssetModel mAssetModel;


    public static AssetsPlayerFragment newInstance(String assetsLocation, AssetModel assetModel) {
        AssetsPlayerFragment assetsPlayerFragment = new AssetsPlayerFragment();
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_ASSET_MODEL, assetModel);
        args.putString(EXTRA_ASSET_LOCATION, assetsLocation);
        assetsPlayerFragment.setArguments(args);
        return assetsPlayerFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_assets_player, container, false);
        ButterKnife.bind(this,rootView);
        ((App) getActivity().getApplication()).getDatabaseComponent().inject(this);
        if (getArguments() != null){
            mAssetsLocation = getArguments().getString(EXTRA_ASSET_LOCATION, "");
            if (getArguments().containsKey(EXTRA_ASSET_MODEL))
                mAssetModel = (AssetModel) getArguments().getSerializable(EXTRA_ASSET_MODEL);
        }

        mVideoPlayerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (mControllerView != null) {
                    if (mControllerView.isVisible()){
                        if (getActivity() instanceof AssetsPlayerContract.ActivityView){
                            ((AssetsPlayerContract.ActivityView) getActivity()).hide();
                        }

                        mControllerView.hide();
                    }else{
                        if (getActivity() instanceof AssetsPlayerContract.ActivityView){
                            ((AssetsPlayerContract.ActivityView) getActivity()).show();
                        }
                        mControllerView.show();
                    }
                }
                return false;
            }
        });

        return rootView;
    }

    private void setupAudioPlayer(long position) {
        PlayerEngine playerEngine = new PlayerEngine(getContext(),mAssetModelController);
        mAudioPlayer = playerEngine.createAudioPlayer(mAssetsLocation,mAssetModel);
        mAudioPlayer.setPlayWhenReady(true);
        mAudioPlayer.seekTo(position);
        mAudioPlayer.addListener(new PlayerCallBacks.AudioPlayerListener(this));
        mAudioPlayerView.setPlayer(mAudioPlayer);

        mControllerView.setPlayer(mAudioPlayer);
        mControllerView.show();
    }

    private void setupVideoPlayer(long position){
        PlayerEngine playerEngine = new PlayerEngine(getContext(),mAssetModelController);
        mVideoPlayer = playerEngine.createVideoPlayer(mAssetsLocation,mAssetModel);
        mVideoPlayer.setPlayWhenReady(true);
        mVideoPlayer.seekTo(position);
        mVideoPlayer.addListener(new PlayerCallBacks.VideoPlayerListener(this));
        mVideoPlayerView.setPlayer(mVideoPlayer);
    }

    @Override
    public void setProgressIndicator(boolean active) {
        mLoadingIndicatorView.setVisibility(active ? View.VISIBLE : View.GONE);
    }

    @Override
    public void showMessage(String message) {
        Snackbar.make(mRootContainer,message,Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void setAudioPlayerPlayWhenReady(boolean playWhenReady) {
        if (mAudioPlayer != null){
            mAudioPlayer.setPlayWhenReady(playWhenReady);
        }
    }

    @Override
    public void onPause() {
        mAudioCurrentPosition = mAudioPlayer.getCurrentPosition();
        mVideoCurrentPostion = mVideoPlayer.getCurrentPosition();
        mAudioPlayer.release();
        mVideoPlayer.release();
        super.onPause();

    }

    @Override
    public void onResume() {
        super.onResume();
        setupAudioPlayer(mAudioCurrentPosition);
        setupVideoPlayer(mVideoCurrentPostion);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong(EXTRA_AUDIO_CURRENT_POSITION, mAudioCurrentPosition);
        outState.putLong(EXTRA_VIDEO_CURRENT_POSITION, mVideoCurrentPostion);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null){
            mAudioCurrentPosition = savedInstanceState.getLong(EXTRA_AUDIO_CURRENT_POSITION,0);
            mVideoCurrentPostion = savedInstanceState.getLong(EXTRA_VIDEO_CURRENT_POSITION,0);
        }
    }
}
