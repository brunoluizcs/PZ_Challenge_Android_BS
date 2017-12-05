package photozig.bomcodigo.com.bsphotozig.network.api;


import photozig.bomcodigo.com.bsphotozig.network.api.model.AssetPayload;
import retrofit2.Call;
import retrofit2.http.GET;

public interface PhotoZigApi {

    @GET("pz_challenge/assets.json")
    Call<AssetPayload> getAssets();
}
