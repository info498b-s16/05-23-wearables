package edu.uw.weardemo;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.wearable.view.CardFragment;
import android.support.wearable.view.WatchViewStub;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {

    private static final String TAG = "Wear";

    private TextView mTextView;
    private ViewPager pager;
    private MyPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        CardFragment card = CardFragment.create("Card 1", "this is a card!");

//        FragmentManager fragmentManager = getFragmentManager();
//        fragmentManager.beginTransaction().add(R.id.container, card).commit();

        adapter = new MyPagerAdapter(getFragmentManager());
        adapter.addFragment(new ButtonFragment());
//        adapter.addFragment(card);
//        adapter.addFragment(CardFragment.create("Card 2", "Another card"));
//        adapter.addFragment(CardFragment.create("Card 3", "Still more!"));

        pager = (ViewPager)findViewById(R.id.pager);
        pager.setAdapter(adapter);

    }

    class MyPagerAdapter extends FragmentPagerAdapter {

        private ArrayList<Fragment> fragments;

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
            fragments = new ArrayList<Fragment>();
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        public void addFragment(Fragment frag) {
            fragments.add(frag);
            notifyDataSetChanged();
        }
    }

    public void handleClick(View v){
        Log.v(TAG, "Clicky");

        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        startActivityForResult(intent, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 0 && resultCode == RESULT_OK){
            List<String> results = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

            String spokenText = results.get(0);
            Log.v(TAG, "You said: "+spokenText);

            for(String text : results){
                Log.v(TAG, text);
            }

            CardFragment card = CardFragment.create("A note", spokenText);
            adapter.addFragment(card);
            pager.setCurrentItem(adapter.getCount()-1);

        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    public static class ButtonFragment extends Fragment {

        public ButtonFragment() {
            // Required empty public constructor
        }


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            // Inflate the layout for this fragment
            return inflater.inflate(R.layout.fragment_button, container, false);
        }


    }

}
