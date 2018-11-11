package com.agilya.syc.moodtracker;

//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;

import java.util.List;
import java.util.Vector;

public class MainActivity extends FragmentActivity {

    private PagerAdapter mPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //liste des fragments à faire défiler
        List fragments = new Vector();

        // ajout des fragments
        fragments.add(Fragment.instantiate(this, PageGaucheFragment.class.getName()));
        fragments.add(Fragment.instantiate(this, PageMilieuFragment.class.getName()));
        fragments.add(Fragment.instantiate(this, PageDroiteFragment.class.getName()));

        this.mPagerAdapter = new MyPagerAdapter(getSupportFragmentManager(), fragments);
        ViewPager pager = (ViewPager) findViewById(R.id.pagerprincipal);

        pager.setAdapter(this.mPagerAdapter);

    }
}
