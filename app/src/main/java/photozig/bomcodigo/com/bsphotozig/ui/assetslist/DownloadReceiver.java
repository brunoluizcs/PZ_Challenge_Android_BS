package photozig.bomcodigo.com.bsphotozig.ui.assetslist;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;

import photozig.bomcodigo.com.bsphotozig.database.AssetContract;
import photozig.bomcodigo.com.bsphotozig.database.AssetDbHelper;
import photozig.bomcodigo.com.bsphotozig.database.controller.AssetModelController;

public class DownloadReceiver extends BroadcastReceiver {

    public interface DownloadListener{
        void onDownloadFinished(String assetName);
    }

    public DownloadListener mDownloadListener;

    public void addListener(DownloadListener downloadListener){
        mDownloadListener = downloadListener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        AssetModelController assetModelController = new AssetModelController(new AssetDbHelper(context));


        if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(action)) {
            long downloadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, 0);
            DownloadManager.Query query = new DownloadManager.Query();
            query.setFilterById(downloadId);

            Cursor c = downloadManager.query(query);
            if (c.moveToFirst()) {
                int columnIndex = c.getColumnIndex(DownloadManager.COLUMN_STATUS);
                if (DownloadManager.STATUS_SUCCESSFUL == c.getInt(columnIndex)) {
                    String uriString = c.getString(c.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI));
                    String name = c.getString(c.getColumnIndex(DownloadManager.COLUMN_TITLE));
                    String description = c.getString(c.getColumnIndex(DownloadManager.COLUMN_DESCRIPTION));

                    updateAudioVideoUri(assetModelController, uriString, name, description);
                    notifyDownloadFinished(assetModelController, name);
                }
            }
        }
    }

    private void notifyDownloadFinished(AssetModelController assetModelController, String name) {
        if (mDownloadListener != null){
            @AssetContract.DownloadStatus int status = assetModelController.checkDownloadStatus(name);
            if (status == AssetContract.DOWNLOAD_COMPLETED){
                mDownloadListener.onDownloadFinished(name);
            }
        }
    }

    private void updateAudioVideoUri(AssetModelController assetModelController, String uriString, String name, String description) {
        if ("video".equals(description)){
            assetModelController.updateVideoUri(name,uriString);
        }else if ("audio".equals(description)){
            assetModelController.updateAudioUri(name,uriString);
        }
    }
}
