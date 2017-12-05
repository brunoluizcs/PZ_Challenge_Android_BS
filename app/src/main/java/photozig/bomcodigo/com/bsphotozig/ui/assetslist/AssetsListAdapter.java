package photozig.bomcodigo.com.bsphotozig.ui.assetslist;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import photozig.bomcodigo.com.bsphotozig.R;
import photozig.bomcodigo.com.bsphotozig.database.AssetContract;
import photozig.bomcodigo.com.bsphotozig.database.controller.AssetModelController;
import photozig.bomcodigo.com.bsphotozig.network.api.model.AssetModel;

public class AssetsListAdapter extends RecyclerView.Adapter<AssetsListAdapter.AssetsListViewHolder>{

    private List<AssetModel> mDataSet;
    private String mAssetsLocation;
    private final OnAssetItemListener mAssetListener;
    private AssetModelController mAssetModelController;


    public interface OnAssetItemListener{
        void onDownloadClick(String assetsLocation, AssetModel assetModel);
        void onPlayClick(String assetsLocation, AssetModel assetModel);
    }

    public AssetsListAdapter(@NonNull List<AssetModel> dataSet,
                             @NonNull String assetsLocation,
                             @Nullable OnAssetItemListener assetListener,
                             AssetModelController assetModelController) {

        this.mDataSet = dataSet;
        this.mAssetsLocation = assetsLocation;
        this.mAssetListener = assetListener;
        this.mAssetModelController = assetModelController;
    }


    public void swapDataSet(List<AssetModel> dataSet){
        mDataSet.clear();
        mDataSet.addAll(dataSet);
        notifyDataSetChanged();
    }

    public void setAssetsLocation(String assetsLocation){
        this.mAssetsLocation = assetsLocation;
        notifyDataSetChanged();
    }

    @Override
    public AssetsListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.content_asset_item,parent,false);
        return new AssetsListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AssetsListViewHolder holder, int position) {
        holder.bind(mDataSet.get(position));
    }

    @Override
    public int getItemCount() {
        return mDataSet != null ? mDataSet.size() : 0;
    }

    public class AssetsListViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.asset_image) ImageView mAssetImageView;
        @BindView(R.id.asset_name_textView) TextView mAssetText;
        @BindView(R.id.asset_play_button) ImageView mPlayButton;
        @BindView(R.id.asset_download_button) ImageView mDownloadButton;

        private Context context;


        public AssetsListViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            context = itemView.getContext();

        }

        @OnClick(R.id.asset_download_button)
        public void downloadClick(View v){
            if (mAssetListener != null){
                mAssetListener.onDownloadClick(mAssetsLocation,mDataSet.get(getAdapterPosition()));
            }
        }

        @OnClick(R.id.asset_play_button)
        public void playClick(View v){
            if (mAssetListener != null){
                mAssetListener.onPlayClick(mAssetsLocation,mDataSet.get(getAdapterPosition()));
            }
        }

        public void bind(AssetModel model){
            setuDownloadStatus(model);

            String url = String.format("%s/%s",mAssetsLocation,model.getIm());
            Glide.with(context)
                    .load(url)
                    .into(mAssetImageView);

            mAssetText.setText(model.getName());
        }

        private void setuDownloadStatus(AssetModel model) {
            @AssetContract.DownloadStatus int status = mAssetModelController.checkDownloadStatus(model.getName());
            switch (status) {
                case AssetContract.DOWNLOAD_COMPLETED:
                    mDownloadButton.setImageResource(android.R.drawable.stat_sys_download_done);
                    break;
                case AssetContract.DOWNLOAD_IDLE:
                    mDownloadButton.setImageResource(android.R.drawable.stat_sys_download);
                    mDownloadButton.setEnabled(true);
                    break;
                case AssetContract.DOWNLOAD_IN_PROGRESS:
                    mDownloadButton.setVisibility(View.VISIBLE);
                    mDownloadButton.setEnabled(false);
                    break;
                case AssetContract.DOWNLOAD_PARTIAL:
                    mDownloadButton.setVisibility(View.VISIBLE);
                    mDownloadButton.setEnabled(false);
                    break;
            }
        }
    }
}
