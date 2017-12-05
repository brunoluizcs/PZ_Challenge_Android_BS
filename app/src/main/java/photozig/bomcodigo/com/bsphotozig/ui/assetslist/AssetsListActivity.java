package photozig.bomcodigo.com.bsphotozig.ui.assetslist;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import photozig.bomcodigo.com.bsphotozig.R;

public class AssetsListActivity extends AppCompatActivity {




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assets_list);



        if (null == savedInstanceState) {
            initFragment(AssetsListFragment.newInstance());
        }
    }

    private void initFragment(Fragment assetsListFragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.content_frame,assetsListFragment);
        transaction.commit();
    }
}
