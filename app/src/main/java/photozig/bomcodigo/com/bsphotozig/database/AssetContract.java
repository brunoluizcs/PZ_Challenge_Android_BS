package photozig.bomcodigo.com.bsphotozig.database;

import android.provider.BaseColumns;
import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class AssetContract {

    public static final int DOWNLOAD_COMPLETED = 200;
    public static final int DOWNLOAD_PARTIAL = 100;
    public static final int DOWNLOAD_IN_PROGRESS = 300;
    public static final int DOWNLOAD_IDLE = 0;

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({DOWNLOAD_COMPLETED,DOWNLOAD_PARTIAL,DOWNLOAD_IN_PROGRESS,DOWNLOAD_IDLE})
    public @interface DownloadStatus{}


    public static final class AssetModelEntry implements BaseColumns{
        public static final String TABLE_NAME = "assetModel";

        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_VIDEO = "bg";
        public static final String COLUMN_IMAGE = "im";
        public static final String COLUMN_AUDIO = "sg";
        public static final String COLUMN_DOWNLOAD_STATUS = "ds";
    }

    public static final class AssetSubtitleEntry implements BaseColumns{
        public static final String TABLE_NAME = "assetSubtitle";

        public static final String COLUMN_TXT = "txt";
        public static final String COLUMN_TIME = "time";
        public static final String COLUMN_ASSET_MODEL_ID = "assetId";
    }

}
