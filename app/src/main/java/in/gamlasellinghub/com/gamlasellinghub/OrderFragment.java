package in.gamlasellinghub.com.gamlasellinghub;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CatalogFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CatalogFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OrderFragment extends Fragment   {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    String pagetitle;
    String searchquery = "";
    private OnFragmentInteractionListener mListener;
    int currentitem;
    int newcurrentitem;

    String [] statusarray = {"My Order","Shipped","Delivered"};

    public OrderFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CatalogFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static OrderFragment newInstance(String param1, String param2) {
        OrderFragment fragment = new OrderFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
            searchquery = getArguments().getString("Searchquery");
            newcurrentitem = getArguments().getInt("currentitem",0);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        System.out.println("=-=====================deepanshu-----------------------");
        View v = inflater.inflate(R.layout.fragment_order,container,false);
        viewPager = (ViewPager) v.findViewById(R.id.viewpagerorder);
        setupViewPager(viewPager);
        setHasOptionsMenu(true);
        tabLayout = (TabLayout) v.findViewById(R.id.tabsorder);
        tabLayout.setupWithViewPager(viewPager);

        return v;
    }
    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());

        MyOrder myorder =  new MyOrder();
        Bundle bundlemyorder = new Bundle();
        bundlemyorder.putString("Searchquery",searchquery);
        myorder.setArguments(bundlemyorder);


        Shipped shipped =  new Shipped();
        Bundle bundleshipped = new Bundle();
        bundleshipped.putString("Searchquery",searchquery);
        shipped.setArguments(bundleshipped);


        Delivered delivered =  new Delivered();
        Bundle bundledelivered = new Bundle();
        bundledelivered.putString("Searchquery",searchquery);
        delivered.setArguments(bundledelivered);

        adapter.addFragment(myorder, "My Order");
        adapter.addFragment(shipped, "Shipped");
        adapter.addFragment(delivered, "Delivered");
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(newcurrentitem);
    }
    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()+ " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {

            // pagetitle =  mFragmentTitleList.get(position);
            return mFragmentTitleList.get(position);
        }
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.search, menu);
        super.onCreateOptionsMenu(menu,inflater);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.searchcatalog:
                currentitem = viewPager.getCurrentItem();
                Intent srachIntent = new Intent(getActivity(),SearchOrder.class);
                srachIntent.putExtra("status",statusarray[currentitem]);
                startActivityForResult(srachIntent,102);
                getActivity().overridePendingTransition(R.anim.push_down_in, R.anim.hold);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        System.out.println("--------------------------onavtivity result-------------------------->"+requestCode+"--------result code------>"+resultCode);
        if(requestCode==101)
        {
            Bundle bundle = new Bundle();
            //  String searchquery = data.getStringExtra("Searchquery");
            bundle.putString("Searchquery","");
            Fragment fragment = new CatalogFragment();
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                    android.R.anim.fade_out);
            fragmentTransaction.replace(R.id.frame,fragment);

            fragmentTransaction.commitAllowingStateLoss();


        }
        else if(requestCode==102)
        {
            Bundle bundle = new Bundle();
            String searchquery = data.getStringExtra("Searchquery");
            System.out.println("my custom query-------------->"+searchquery);
            bundle.putString("Searchquery",searchquery);
            bundle.putInt("currentitem",currentitem);
            Fragment fragment = new OrderFragment();
            fragment.setArguments(bundle);
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                    android.R.anim.fade_out);
            fragmentTransaction.replace(R.id.frame,fragment);

            fragmentTransaction.commitAllowingStateLoss();


        }



    }
}

