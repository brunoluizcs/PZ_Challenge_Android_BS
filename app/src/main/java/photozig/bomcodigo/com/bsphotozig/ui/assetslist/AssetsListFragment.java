package photozig.bomcodigo.com.bsphotozig.ui.assetslist;


import android.app.DownloadManager;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import photozig.bomcodigo.com.bsphotozig.R;
import photozig.bomcodigo.com.bsphotozig.application.App;
import photozig.bomcodigo.com.bsphotozig.database.AssetDbHelper;
import photozig.bomcodigo.com.bsphotozig.database.controller.AssetModelController;
import photozig.bomcodigo.com.bsphotozig.network.api.model.AssetModel;
import photozig.bomcodigo.com.bsphotozig.ui.assetsplayer.AssetsPlayerActivity;
import retrofit2.Retrofit;

public class AssetsListFragment extends Fragment implements AssetsListContract.View{
    @BindView(R.id.assets_list_recycler) RecyclerView mAssetsListRecyclerView;
    @BindView(R.id.root_container) View mRootContainerView;
    @BindView(R.id.loading_indicator) View mLoadingIndicatorView;

    @Inject Retrofit mRetrofit;

    private AssetModelController mAssetModelController;
    private AssetsListAdapter mAdapter;
    private AssetsListContract.AssetsListActionsListener mActionListener;
    private DownloadReceiver mDownloadReceiver;


    private final AssetsListAdapter.OnAssetItemListener mOnAssetItemListener = new AssetsListAdapter.OnAssetItemListener() {
        @Override
        public void onDownloadClick(String assetsLocation, AssetModel assetModel) {
            mActionListener.startDownload(getContext(),assetsLocation,assetModel);
        }

        @Override
        public void onPlayClick(String assetsLocation, AssetModel assetModel) {
            mActionListener.playAsset(assetsLocation,assetModel);
        }
    };

    private final DownloadReceiver.DownloadListener mDownloadListener = new DownloadReceiver.DownloadListener() {
        @Override
        public void onDownloadFinished(String assetName) {
            AssetModel model = mAssetModelController.get(assetName);
            showPlayerUi("",model);
        }
    };

    public static AssetsListFragment newInstance() {
        return new AssetsListFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_assets_list, container, false);
        ButterKnife.bind(this,rootView);

        ((App) getActivity().getApplication()).getNetComponent().inject(this);

        mDownloadReceiver = new DownloadReceiver();
        mDownloadReceiver.addListener(mDownloadListener);

        mAssetModelController = new AssetModelController(new AssetDbHelper(getContext()));
        mAdapter = new AssetsListAdapter(new ArrayList<AssetModel>(),"",mOnAssetItemListener,mAssetModelController);
        mAssetsListRecyclerView.setHasFixedSize(true);
        mAssetsListRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mAssetsListRecyclerView.setAdapter(mAdapter);

        mActionListener = new AssetsListPresenter();
        mActionListener.init(this,mRetrofit,mAssetModelController);
        mActionListener.loadAssets();

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        getContext().registerReceiver(mDownloadReceiver,
                new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }

    @Override
    public void onPause() {
        getContext().unregisterReceiver(mDownloadReceiver);
        super.onPause();
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public void setProgressIndicator(boolean active) {
        mLoadingIndicatorView.setVisibility(active ? View.VISIBLE : View.GONE);
        mAssetsListRecyclerView.setVisibility(! active ? View.VISIBLE : View.GONE);
    }

    @Override
    public void showAssets(String assetsLocation, List<AssetModel> assets) {
        mAdapter.setAssetsLocation(assetsLocation);
        mAdapter.swapDataSet(assets);
    }

    @Override
    public void showPlayerUi(String assetsLocation, AssetModel assetModel) {
        AssetsPlayerActivity.launch(getContext(),assetsLocation,assetModel);
    }

    @Override
    public void showMessage(String message) {
        Snackbar.make(mRootContainerView,message,Snackbar.LENGTH_LONG).show();
    }
}

