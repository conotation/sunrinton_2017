package cf.connota.sunrinton.Activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;

import cf.connota.sunrinton.Adapter.PagerAdapter;
import cf.connota.sunrinton.R;
import cf.connota.sunrinton.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        binding.mainTabBar.addTab(binding.mainTabBar.newTab().setText("친구목록"));
        binding.mainTabBar.addTab(binding.mainTabBar.newTab().setText("추가정보"));
        binding.mainTabBar.setTabGravity(TabLayout.GRAVITY_CENTER);

        final PagerAdapter adapter = new PagerAdapter
                (getSupportFragmentManager(), binding.mainTabBar.getTabCount());
        binding.mainViewPager.setAdapter(adapter);
        binding.mainViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(binding.mainTabBar));
        binding.mainTabBar.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                binding.mainViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
