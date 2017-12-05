package photozig.bomcodigo.com.bsphotozig.database.controller;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import java.util.List;

import photozig.bomcodigo.com.bsphotozig.database.AssetContract;
import photozig.bomcodigo.com.bsphotozig.database.AssetDbHelper;
import photozig.bomcodigo.com.bsphotozig.network.api.model.AssetModel;
import photozig.bomcodigo.com.bsphotozig.network.api.model.AssetSubtitle;

import static photozig.bomcodigo.com.bsphotozig.database.AssetContract.DOWNLOAD_IDLE;

public class AssetModelController {

    private AssetDbHelper mDbHelper;

    public AssetModelController(AssetDbHelper dbHelper) {
        this.mDbHelper = dbHelper;
    }

    public void insert(String name,String image ,List<AssetSubtitle> subtitles){
        if (contains(name) != -1){
            delete(name);
        }
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(AssetContract.AssetModelEntry.COLUMN_NAME,name);
        contentValues.put(AssetContract.AssetModelEntry.COLUMN_IMAGE, image);
        contentValues.put(AssetContract.AssetModelEntry.COLUMN_DOWNLOAD_STATUS,DOWNLOAD_IDLE);

        long insertedRow = db.insert(AssetContract.AssetModelEntry.TABLE_NAME, null, contentValues);
        db.close();

        int _id = contains(name);
        if (insertedRow != -1){
            new AssetSubtitleController(mDbHelper).insert(_id,subtitles);
        }
    }


    public void updateAudioUri(String modelName, String audioUri){
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(AssetContract.AssetModelEntry.COLUMN_AUDIO, audioUri);

        db.update(AssetContract.AssetModelEntry.TABLE_NAME,
                contentValues,
                AssetContract.AssetModelEntry.COLUMN_NAME + "=?",
                new String[]{modelName});
        db.close();
        updateDownloadStatus(modelName);

    }

    public void updateVideoUri(String modelName, String videoUri){
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(AssetContract.AssetModelEntry.COLUMN_VIDEO, videoUri);

        db.update(AssetContract.AssetModelEntry.TABLE_NAME,
                contentValues,
                AssetContract.AssetModelEntry.COLUMN_NAME + "=?",
                new String[]{modelName});
        db.close();
        updateDownloadStatus(modelName);
    }

    public void insert(AssetModel model){
        if (contains(model.getName()) != -1){
            delete(model.getName());
        }
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(AssetContract.AssetModelEntry.COLUMN_NAME,model.getName());
        contentValues.put(AssetContract.AssetModelEntry.COLUMN_VIDEO,model.getBg());
        contentValues.put(AssetContract.AssetModelEntry.COLUMN_AUDIO,model.getSg());
        contentValues.put(AssetContract.AssetModelEntry.COLUMN_IMAGE,model.getIm());
        contentValues.put(AssetContract.AssetModelEntry.COLUMN_DOWNLOAD_STATUS,DOWNLOAD_IDLE);
        long insertedRow = db.insert(AssetContract.AssetModelEntry.TABLE_NAME, null, contentValues);
        db.close();

        int _id = contains(model.getName());

        if (insertedRow != -1){
            new AssetSubtitleController(mDbHelper).insert(_id,model.getTxts());
        }
    }

    public int contains(String name){
        int _id = -1;
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        AssetSubtitleController assetSubtitleController = new AssetSubtitleController(mDbHelper);

        Cursor c = db.query(AssetContract.AssetModelEntry.TABLE_NAME,
                new String[]{AssetContract.AssetModelEntry._ID},
                AssetContract.AssetModelEntry.COLUMN_NAME + " = ?",
                new String[]{name},null,null,null);

        if (c != null){
            if (c.moveToFirst()){
                _id = c.getInt(c.getColumnIndex(AssetContract.AssetModelEntry._ID));
            }
        }
        db.close();
        return _id;
    }

    @Nullable
    public AssetModel get(String name){
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        AssetSubtitleController assetSubtitleController = new AssetSubtitleController(mDbHelper);

        Cursor c = db.query(AssetContract.AssetModelEntry.TABLE_NAME,
                            null,
                            AssetContract.AssetModelEntry.COLUMN_NAME + " = ?",
                            new String[]{name},null,null,null);

        AssetModel assetModel = null;
        if (c != null && c.moveToFirst()){
            do {
                int _id = c.getInt(c.getColumnIndex(AssetContract.AssetModelEntry._ID));

                assetModel = new AssetModel(c.getString(c.getColumnIndex(AssetContract.AssetModelEntry.COLUMN_NAME)),
                        c.getString(c.getColumnIndex(AssetContract.AssetModelEntry.COLUMN_VIDEO)),
                        c.getString(c.getColumnIndex(AssetContract.AssetModelEntry.COLUMN_IMAGE)),
                        c.getString(c.getColumnIndex(AssetContract.AssetModelEntry.COLUMN_AUDIO)),
                        assetSubtitleController.get(_id));
            }while(c.moveToNext());
            c.close();
        }
        db.close();
        return assetModel;
    }


    public void updateDownloadStatus(String assetName){
        AssetModel assetModel = get(assetName);
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        if (assetModel == null){
            return;
        }

        @AssetContract.DownloadStatus
        int status;

        if (! TextUtils.isEmpty(assetModel.getBg()) &&
                ! TextUtils.isEmpty(assetModel.getSg())){
            status = AssetContract.DOWNLOAD_COMPLETED;
        }else if(TextUtils.isEmpty(assetModel.getBg()) &&
                TextUtils.isEmpty(assetModel.getSg())){
            status = AssetContract.DOWNLOAD_IN_PROGRESS;
        }else{
            status = AssetContract.DOWNLOAD_PARTIAL;
        }

        ContentValues contentValues = new ContentValues();
        contentValues.put(AssetContract.AssetModelEntry.COLUMN_DOWNLOAD_STATUS,status);
        db.update(AssetContract.AssetModelEntry.TABLE_NAME,
                contentValues,
                AssetContract.AssetModelEntry.COLUMN_NAME +  " = ?",
                new String[]{assetName});

        db.close();
    }

    @SuppressWarnings("WrongConstant")
    public @AssetContract.DownloadStatus int checkDownloadStatus(String name){
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        Cursor c = db.query(AssetContract.AssetModelEntry.TABLE_NAME,
                new String[]{AssetContract.AssetModelEntry.COLUMN_DOWNLOAD_STATUS},
                AssetContract.AssetModelEntry.COLUMN_NAME + " = ?",
                new String[]{name},null,null,null);

        @AssetContract.DownloadStatus
        int downloadStatus = DOWNLOAD_IDLE;
        if (c != null && c.moveToFirst()){
            do {
                downloadStatus = c.getInt(c.getColumnIndex(AssetContract.AssetModelEntry.COLUMN_DOWNLOAD_STATUS));
            }while(c.moveToNext());
            c.close();
        }
        db.close();
        return downloadStatus;
    }

    public boolean hasAudioDownloaded(String assetName){
        AssetModel model = get(assetName);
        return model != null && ! TextUtils.isEmpty(model.getSg());
    }

    public boolean hasVideoDownloaded(String assetName){
        AssetModel model = get(assetName);
        return model != null && ! TextUtils.isEmpty(model.getBg());
    }

    public boolean delete(String name){
        int _id = contains(name);
        int rowsDeleted = -1;
        if (_id != -1){
            SQLiteDatabase db = mDbHelper.getWritableDatabase();
            rowsDeleted = db.delete(AssetContract.AssetModelEntry.TABLE_NAME,
                    AssetContract.AssetModelEntry.COLUMN_NAME + " = ?",
                    new String[]{name});

            if (rowsDeleted > 0){
                new AssetSubtitleController(mDbHelper).delete(_id);
            }
            db.close();
        }
        return rowsDeleted > 0;
    }




}
