package photozig.bomcodigo.com.bsphotozig.database.controller;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import photozig.bomcodigo.com.bsphotozig.database.AssetContract;
import photozig.bomcodigo.com.bsphotozig.database.AssetDbHelper;
import photozig.bomcodigo.com.bsphotozig.network.api.model.AssetSubtitle;

class AssetSubtitleController {

    private AssetDbHelper mDbHelper;

    public AssetSubtitleController(AssetDbHelper dbHelper) {
        this.mDbHelper = dbHelper;
    }

    public void insert(int modelId, @Nullable List<AssetSubtitle> subtitles){
        if (subtitles != null) {
            SQLiteDatabase db = mDbHelper.getWritableDatabase();
            for (AssetSubtitle subtitle : subtitles) {
                ContentValues contentValues = new ContentValues();
                contentValues.put(AssetContract.AssetSubtitleEntry.COLUMN_TXT,subtitle.getTxt());
                contentValues.put(AssetContract.AssetSubtitleEntry.COLUMN_TIME,subtitle.getTime());
                contentValues.put(AssetContract.AssetSubtitleEntry.COLUMN_ASSET_MODEL_ID,modelId);
                db.insert(AssetContract.AssetSubtitleEntry.TABLE_NAME, null, contentValues);
            }
            db.close();
        }
    }

    public List<AssetSubtitle> get(int modelId){
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        Cursor c = db.query(AssetContract.AssetSubtitleEntry.TABLE_NAME,
                null,
                AssetContract.AssetSubtitleEntry.COLUMN_ASSET_MODEL_ID + " = ?",
                new String[]{String.valueOf(modelId)},null,null,null);

        List<AssetSubtitle> subtitleList = new ArrayList<>();
        if (c != null && c.moveToFirst()){
            do {
                AssetSubtitle subtitle = new AssetSubtitle(c.getString(c.getColumnIndex(AssetContract.AssetSubtitleEntry.COLUMN_TXT)),
                        c.getDouble(c.getColumnIndex(AssetContract.AssetSubtitleEntry.COLUMN_TIME)));
                subtitleList.add(subtitle);
            }while(c.moveToNext());
            c.close();
        }
        db.close();
        return subtitleList;
    }

    public void delete(int modelId) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        db.delete(AssetContract.AssetSubtitleEntry.TABLE_NAME,
                AssetContract.AssetSubtitleEntry.COLUMN_ASSET_MODEL_ID + " = ?",
                new String[]{String.valueOf(modelId)});
    }
}
