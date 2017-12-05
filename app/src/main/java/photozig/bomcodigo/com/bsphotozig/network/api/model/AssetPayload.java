package photozig.bomcodigo.com.bsphotozig.network.api.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;


public class AssetPayload implements Serializable {

    @SerializedName("assetsLocation") private String assetsLocation;
    @SerializedName("objects") private List<AssetModel> objects;

    public AssetPayload() {
    }

    public AssetPayload(String assetsLocation, List<AssetModel> objects) {
        this.assetsLocation = assetsLocation;
        this.objects = objects;
    }

    public String getAssetsLocation() {
        return assetsLocation;
    }

    public List<AssetModel> getObjects() {
        return objects;
    }
}
