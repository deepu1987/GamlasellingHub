package in.gamlasellinghub.com.gamlasellinghub;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DahboardFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DahboardFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DahboardFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    PieChart catalogpieChart ;
    PieChart orderpieChart ;
    ArrayList<Entry> catalogentries ;
    ArrayList<String> catalogPieEntryLabels ;
    ArrayList<Entry> orderentries ;
    ArrayList<String> orderPieEntryLabels ;
    PieDataSet catalogpieDataSet ;
    PieData catalogpieData ;
    PieDataSet orderpieDataSet ;
    PieData orderpieData ;
    RequestQueue rQueue;
    int totalCatalog;
    int instockcount;
    int outstockcount;
    int nonlivecount;
    int orderplacedcount;
    int shippedcount;
    int deliveredcount;
    int totalorder;
    public static final int[] CustomCOLORS = {
            Color.rgb(117, 223, 4), Color.rgb(234, 231, 0), Color.rgb(255, 72, 72),

    };
    public DahboardFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DahboardFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DahboardFragment newInstance(String param1, String param2) {
        DahboardFragment fragment = new DahboardFragment();
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
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_dahboard, container, false);
        catalogpieChart = (PieChart) v.findViewById(R.id.chart1);
        orderpieChart = (PieChart) v.findViewById(R.id.chart2);
        rQueue = Volley.newRequestQueue(getActivity());
        FetchrecordFromServer();




        return v;
    }

    private void FetchrecordFromServer()
    {
        final ProgressDialog loading = ProgressDialog.show(getActivity(), "Loading", "Please wait...", false,false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Utility.DASHBOARDCOUNTER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //if the server response is success
                        loading.dismiss();
                        if(!response.contains("false")) {
                            try {
                                JSONArray objarray = new JSONArray(response);
                                JSONObject objcatalog = objarray.getJSONObject(0);
                                instockcount = Integer.parseInt(objcatalog.getString("In Stock"));
                                outstockcount = Integer.parseInt(objcatalog.getString("Out Stock"));
                                nonlivecount = Integer.parseInt(objcatalog.getString("Non Live"));
                                totalCatalog = instockcount+outstockcount+nonlivecount;

                                JSONObject objorder = objarray.getJSONObject(1);
                                orderplacedcount = Integer.parseInt(objorder.getString("Order Placed"));
                                shippedcount = Integer.parseInt(objorder.getString("Shipped"));
                                deliveredcount = Integer.parseInt(objorder.getString("Delivered"));
                                totalorder = orderplacedcount+shippedcount+deliveredcount;
                                System.out.println("total count-------------------<"+totalorder);
                                new LoadGraph().execute();
                            }
                            catch (Exception e)
                            {
                                e.printStackTrace();
                            }


                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loading.dismiss();
                        Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                //Adding the parameters otp and username
                params.put("email", Login.email);
                params.put("mobile", Login.mobileno);
                return params;
            }
        };

        //Adding the request to the queue
        rQueue.add(stringRequest);
    }

    public void AddValuesToPIEENTRYCatalog(){

        catalogentries.add(new BarEntry(instockcount, 0));
        catalogentries.add(new BarEntry(outstockcount, 1));
        catalogentries.add(new BarEntry(nonlivecount, 2));


    }

    public void AddValuesToPieEntryLabelsCatalog(){

        catalogPieEntryLabels.add("In Stock");
        catalogPieEntryLabels.add("Out Of Stock");
        catalogPieEntryLabels.add("Non Live");


    }

    public void AddValuesToPIEENTRYOrder(){

        orderentries.add(new BarEntry(orderplacedcount, 0));
        orderentries.add(new BarEntry(shippedcount, 1));
        orderentries.add(new BarEntry(deliveredcount, 2));


    }

    public void AddValuesToPieEntryLabelsOrder(){

        orderPieEntryLabels.add("Order Placed");
        orderPieEntryLabels.add("Shipped");
        orderPieEntryLabels.add("Delivered");


    }
    private class LoadGraph extends AsyncTask<Void,Void,String>
    {
        @Override
        protected void onPreExecute() {

            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... voids) {

            catalogentries = new ArrayList<>();
            catalogPieEntryLabels = new ArrayList<String>();
            AddValuesToPIEENTRYCatalog();
            AddValuesToPieEntryLabelsCatalog();
            catalogpieDataSet = new PieDataSet(catalogentries, "Catalog");
            catalogpieData = new PieData(catalogPieEntryLabels, catalogpieDataSet);
            catalogpieDataSet.setColors(CustomCOLORS);


            orderentries = new ArrayList<>();
            orderPieEntryLabels = new ArrayList<String>();
            AddValuesToPIEENTRYOrder();
            AddValuesToPieEntryLabelsOrder();
            orderpieDataSet= new PieDataSet(orderentries, "Order");
            orderpieData = new PieData(orderPieEntryLabels, orderpieDataSet);
            orderpieDataSet.setColors(ColorTemplate.JOYFUL_COLORS);

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            catalogpieChart.setData(catalogpieData);
            catalogpieChart.animateY(1000);
            orderpieChart.setData(orderpieData);
            orderpieChart.animateY(1000);

            super.onPostExecute(s);
        }
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
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
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
}
