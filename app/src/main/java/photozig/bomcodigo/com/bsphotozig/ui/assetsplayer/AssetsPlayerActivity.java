package photozig.bomcodigo.com.bsphotozig.ui.assetsplayer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import photozig.bomcodigo.com.bsphotozig.R;
import photozig.bomcodigo.com.bsphotozig.network.api.model.AssetModel;

public class AssetsPlayerActivity extends AppCompatActivity implements
        AssetsPlayerContract.ActivityView{

    public static String EXTRA_ASSET_MODEL = "extra-asset-model";
    public static String EXTRA_ASSET_LOCATION = "extra-asset-location";
    @BindView(R.id.content_frame) View mContentView;


    private static final boolean AUTO_HIDE = true;
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;
    private static final int UI_ANIMATION_DELAY = 300;
    private final Handler mHideHandler = new Handler();


    private final Runnable mHidePart2Runnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
            mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    };

    private final Runnable mShowPart2Runnable = new Runnable() {
        @Override
        public void run() {
            // Delayed display of UI elements
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.show();
            }
            mContentView.setVisibility(View.VISIBLE);
        }
    };

    private boolean mVisible;
    private final Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            hide();
        }
    };

    private final View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (AUTO_HIDE) {
                delayedHide(AUTO_HIDE_DELAY_MILLIS);
            }
            return false;
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assets_player);
        ButterKnife.bind(this);
        AssetModel assetModel = null;
        if (null == savedInstanceState ) {
            Intent intent = getIntent();
            if (intent.hasExtra(EXTRA_ASSET_MODEL) && intent.hasExtra(EXTRA_ASSET_LOCATION)) {
                assetModel = (AssetModel) intent.getExtras().getSerializable(EXTRA_ASSET_MODEL);
                String assetLocation = (String) intent.getExtras().getString(EXTRA_ASSET_LOCATION);
                if (savedInstanceState == null)
                    initFragment(AssetsPlayerFragment.newInstance(assetLocation, assetModel));
            } else {
                throw new RuntimeException(getString(R.string.error_parametro_invalido));
            }
        }

        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            if (assetModel != null)
                getSupportActionBar().setTitle(assetModel.getName());
        }

    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        delayedHide(100);
    }

    public static void launch(Context context, String assetsLocation, AssetModel assetModel){
        Intent intent = new Intent(context, AssetsPlayerActivity.class);
        intent.putExtra(EXTRA_ASSET_MODEL,assetModel);
        intent.putExtra(EXTRA_ASSET_LOCATION,assetsLocation);
        context.startActivity(intent);
    }

    private void initFragment(Fragment assetsPlayerFragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.content_frame,assetsPlayerFragment);
        transaction.commit();
    }

    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }

    @Override
    public void hide() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        mVisible = false;

        mHideHandler.removeCallbacks(mShowPart2Runnable);
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);
    }

    @Override
    public void show(){
        mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        mVisible = true;

        mHideHandler.removeCallbacks(mHidePart2Runnable);
        mHideHandler.postDelayed(mShowPart2Runnable, UI_ANIMATION_DELAY);
    }

    @Override
    public void toggle(){
        if (mVisible) {
            hide();
        } else {
            show();
        }
    }
}

