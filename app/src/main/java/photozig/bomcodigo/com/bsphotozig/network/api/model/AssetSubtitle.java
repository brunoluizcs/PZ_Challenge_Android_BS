package photozig.bomcodigo.com.bsphotozig.network.api.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;



public class AssetSubtitle implements Serializable{
    @SerializedName("txt") private String txt;
    @SerializedName("time") private  double time;

    public AssetSubtitle(String txt, double time) {
        this.txt = txt;
        this.time = time;
    }

    public AssetSubtitle() {
    }

    public String getTxt() {
        return txt;
    }

    public double getTime() {
        return time;
    }
}
