package photozig.bomcodigo.com.bsphotozig.ui.assetsplayer;



public class AssetsPlayerContract {

    interface View {
        void setProgressIndicator(boolean active);
        void showMessage(String message);
        void setAudioPlayerPlayWhenReady(boolean playWhenReady);
    }

    interface ActivityView{
        void hide();
        void show();
        void toggle();
    }

    interface AssetsListActionsListener {


    }
}
