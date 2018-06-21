package in.gamlasellinghub.com.gamlasellinghub;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link InStock.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link InStock#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OutStock extends Fragment implements onProductclicklistner{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private onProductclicklistner mClicklistner;
    ArrayList<ProductlistBean> objArrayList;
    ProductlistBean objBean;
    RequestQueue rQueue;
    RecyclerView rccategoryList;
    String searchquery;
    public OutStock() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment InStock.
     */
    // TODO: Rename and change types and number of parameters
    public static InStock newInstance(String param1, String param2) {
        InStock fragment = new InStock();
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
            searchquery = getArguments().getString("Searchquery","");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        System.out.println("----------------------------helloooooo--------------------------------");
        View v = inflater.inflate(R.layout.fragment_in_stock, container, false);

        rQueue = Volley.newRequestQueue(getActivity());
        //init();
        rccategoryList = v.findViewById(R.id.productList);
        rccategoryList.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        rccategoryList.setLayoutManager(llm);
        mClicklistner = this;


        //====================================fetch record from server==============================//

        fetchrecordfromserver();
        //==========================================================================================//




        return v;
    }

    private void fetchrecordfromserver()
    {
        objArrayList = new ArrayList<>();
        final ProgressDialog loading = ProgressDialog.show(getActivity(), "Gamla", "Please wait...", false,false);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Utility.CATALOG_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("res------------->"+response);
                        //if the server response is success
                        String res = response+"";
                        if(res.contains("false")){
                            loading.dismiss();
                           // Toast.makeText(getActivity(), "Record not avilable", Toast.LENGTH_SHORT).show();


                        }else{
                            try {
                                JSONArray objArray = new JSONArray(res);
                                JSONObject obj;
                                for(int i =0;i<objArray.length();i++)
                                {
                                    obj = new JSONObject();
                                    obj = objArray.getJSONObject(i);
                                    objBean = new ProductlistBean();
                                    objBean.set_productName(obj.getString("ProductName"));
                                    objBean.set_quantatity(obj.getString("Quantatity"));
                                    objBean.set_weight(obj.getString("weight"));
                                    objBean.set_skuid(obj.getString("MerchantSkuId"));
                                    objBean.set_shipinDays(obj.getString("shipindays"));
                                    objBean.set_sellingPrice(obj.getString("SellingPrice"));
                                    objBean.set_productCategory(obj.getString("Categoryname"));
                                    objBean.set_mrp(obj.getString("MRP"));
                                    objBean.set_id(obj.getString("ProuctId"));
                                    objBean.set_desceription(obj.getString("description"));
                                    objBean.set_imageName(obj.getString("ImageName"));
                                    objBean.set_imagepath(obj.getString("ImagePath"));
                                    objBean.set_mobilenumber(obj.getString("mobileno"));
                                    objBean.set_status(obj.getString("Status"));
                                    objArrayList.add(objBean);


                                }

                            }
                            catch (Exception e)
                            {

                            }



                        }
                        ProductListAdapterOutStock productListAdapter = new ProductListAdapterOutStock(getActivity().getApplicationContext(), objArrayList);
                        productListAdapter.setonClickListner(mClicklistner);
                        // ca.setonClickListner(mClicklistner);
                        rccategoryList.setAdapter(productListAdapter);
                        loading.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                JSONObject obj = new JSONObject();
                try {
                    obj.put("email", Login.email);
                    obj.put("mobileno",Login.mobileno);
                    obj.put("status","Out Stock");
                    obj.put("searchquery",searchquery);
                }
                catch (Exception e)
                {

                }
                Map<String,String> params = new HashMap<String, String>();
                //Adding the parameters otp and username
                params.put("data", obj+"");

                return params;
            }

        };

        //Adding the request to the queue
        rQueue.add(stringRequest);

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
       /* if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }*/
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onProductclick(int position) {

        //Toast.makeText(getActivity(),"position--------------->"+position,Toast.LENGTH_SHORT).show();

        Intent i = new Intent(getActivity(),ProductDetail.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("productdetail",objArrayList.get(position));
        i.putExtras(bundle);

        startActivityForResult(i,101);
        getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.hold);

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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        fetchrecordfromserver();

        super.onActivityResult(requestCode, resultCode, data);
    }
}
