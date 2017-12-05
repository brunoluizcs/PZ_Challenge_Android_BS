package photozig.bomcodigo.com.bsphotozig.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import photozig.bomcodigo.com.bsphotozig.database.AssetContract;

public class AssetDbHelper extends SQLiteOpenHelper{

    private static final String DATABASE_NAME = "assetsDb.db";
    private static final int VERSION = 1;

    public AssetDbHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        final String CREATE_TABLE_ASSET = "CREATE TABLE "  + AssetContract.AssetModelEntry.TABLE_NAME + " (" +
                AssetContract.AssetModelEntry._ID + " INTEGER PRIMARY KEY, " +
                AssetContract.AssetModelEntry.COLUMN_NAME + " TEXT, " +
                AssetContract.AssetModelEntry.COLUMN_VIDEO + " TEXT, " +
                AssetContract.AssetModelEntry.COLUMN_AUDIO + " TEXT, " +
                AssetContract.AssetModelEntry.COLUMN_IMAGE + " TEXT, " +
                AssetContract.AssetModelEntry.COLUMN_DOWNLOAD_STATUS + " INTEGER);";

        final String CREATE_TABLE_SUBTITLE = "CREATE TABLE "  + AssetContract.AssetSubtitleEntry.TABLE_NAME + " (" +
                AssetContract.AssetSubtitleEntry._ID + " INTEGER PRIMARY KEY, " +
                AssetContract.AssetSubtitleEntry.COLUMN_ASSET_MODEL_ID + " INTEGER NOT NULL, " +
                AssetContract.AssetSubtitleEntry.COLUMN_TXT + " TEXT, " +
                AssetContract.AssetSubtitleEntry.COLUMN_TIME + " REAL); ";

        db.execSQL(CREATE_TABLE_ASSET);
        db.execSQL(CREATE_TABLE_SUBTITLE);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + AssetContract.AssetModelEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + AssetContract.AssetSubtitleEntry.TABLE_NAME);
        onCreate(db);
    }
}
