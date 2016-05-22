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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {

    private static final String TAG = "Wear";

    private ViewPager pager;
    private MyPagerAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CardFragment card = CardFragment.create("First Card", "This is the first card.");

        adapter = new MyPagerAdapter(getFragmentManager());
        adapter.addFragment(new ButtonFragment());
        adapter.addFragment(card);
        adapter.addFragment(CardFragment.create("Second Card", "Another Card."));
        adapter.addFragment(CardFragment.create("Card #3", "Third time's a charm?"));

        pager = (ViewPager)findViewById(R.id.pager);
        pager.setAdapter(adapter);

        Intent intent = getIntent(); //what started us?
        if(intent.getCategories().contains("com.google.android.voicesearch.SELF_NOTE")){
            Log.v(TAG, intent.toString());
        }


//        FragmentManager fragmentManager = getFragmentManager();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        fragmentTransaction.add(R.id.container, card);
//        fragmentTransaction.commit();


//        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
//        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
//            @Override
//            public void onLayoutInflated(WatchViewStub stub) {
//                mTextView = (TextView) stub.findViewById(R.id.text);
//            }
//        });


    }


    public class MyPagerAdapter extends FragmentPagerAdapter {

        private ArrayList<Fragment> fragments;

        public MyPagerAdapter(FragmentManager fm){
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

        public void addFragment(Fragment fragment){
            fragments.add(fragment);

            // Update the pager when adding a fragment.
            notifyDataSetChanged();
        }
    }

    public static class ButtonFragment extends Fragment {

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            return inflater.inflate(R.layout.fragment_button, container, false);
        }

    }

    public void speakNow(View v) {
        Log.v("TAG","speaking");
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);

        startActivityForResult(intent, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0 && resultCode == RESULT_OK) {
            List<String> results = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            String text = results.get(0);

            Log.v("TAG", text);

            CardFragment card = CardFragment.create("A note", text);
            adapter.addFragment(card);
            pager.setCurrentItem(adapter.getCount()-1, true);
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}
