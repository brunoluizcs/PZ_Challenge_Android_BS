package photozig.bomcodigo.com.bsphotozig.network.api.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;


public class AssetModel implements Serializable {

    @SerializedName("name") private String name;
    @SerializedName("bg") private String bg;
    @SerializedName("im") private String im;
    @SerializedName("sg") private String sg;
    @SerializedName("txts") private List<AssetSubtitle> txts;

    public AssetModel() {
    }

    public AssetModel(String name, String bg, String im, String sg, List<AssetSubtitle> txts) {
        this.name = name;
        this.bg = bg;
        this.im = im;
        this.sg = sg;
        this.txts = txts;
    }

    public String getName() {
        return name;
    }

    public String getBg() {
        return bg;
    }

    public String getIm() {
        return im;
    }

    public String getSg() {
        return sg;
    }

    public List<AssetSubtitle> getTxts() {
        return txts;
    }
}
