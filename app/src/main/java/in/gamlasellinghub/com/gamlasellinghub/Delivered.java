package in.gamlasellinghub.com.gamlasellinghub;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
import com.github.pwittchen.infinitescroll.library.InfiniteScrollListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Delivered extends Fragment implements onPlantsItemClickListner{
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    RequestQueue rQueue;
    ArrayList<String> objArrayListUser;
    ArrayList<ProductlistBean> objArrayList = new ArrayList<>();
    ProductlistBean objBean;
    RecyclerView recList;
    onPlantsItemClickListner mClicklistner;
    LinearLayoutManager llm;
    int limitstart =0;
   // int limitend = 15;
    int rowcount;
    String searchquery;
    public Delivered()
    {

    }
    public static Delivered newInstance(String param1, String param2) {
        Delivered fragment = new Delivered();
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
            System.out.println("----------------------------helloooooo--------------------------------"+searchquery);
        }
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_delivered, container, false);
        rQueue = Volley.newRequestQueue(getActivity());
       /* getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("My Orders");*/
        mClicklistner = this;
        // dh = new DatabaseHandler(this);
        //  objArrayListUser = dh.getUserRecord();
        recList = (RecyclerView) v.findViewById(R.id.deliveredorderlist);
        recList.setHasFixedSize(true);
        llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);
        recList.addOnScrollListener(createInfiniteScrollListener());
        FetchOrderListFromServer(limitstart,Utility.LIMIT_INTERVAL);
        return v;
    }

    private void FetchOrderListFromServer(final int limitstart1, final int limitend1)
    {

        final ProgressDialog loading = ProgressDialog.show(getActivity(), "My Order", "Please wait...", false,false);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Utility.ORDER_LIST,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //if the server response is success

                        if(response.contains("Record Not found"))
                        {
                            loading.dismiss();
                           // Toast.makeText(getActivity(), "Record Not found", Toast.LENGTH_LONG).show();
                        }
                        else
                        {
                            try {
                                JSONArray objJsonArray = new JSONArray(response);
                                for(int i = 0;i<objJsonArray.length();i++)
                                {
                                    JSONObject obj = objJsonArray.getJSONObject(i);
                                    objBean = new ProductlistBean();
                                    objBean.set_orderId(obj.getString("OrderId"));
                                    objBean.set_imagepath(obj.getString("ImagePath"));
                                    objBean.set_productCategory(obj.getString("CatagoryName"));
                                    objBean.set_skuid(obj.getString("MerchantSkuId"));
                                    objBean.set_mrp(obj.getString("MRP"));
                                    objBean.set_sellingPrice(obj.getString("SellingPrice"));
                                    objBean.set_weight(obj.getString("Weight"));
                                    objBean.set_shipinDays(obj.getString("Shipindays"));
                                    objBean.set_desceription(obj.getString("Description"));
                                    objBean.set_mobilenumber(obj.getString("SellerMobile"));
                                    objBean.set_productName(obj.getString("ProductName"));
                                    objBean.set_imageName(obj.getString("ImageName"));
                                    objBean.set_productPurchaseQuantatity(obj.getString("Quantatity"));
                                    objBean.set_orderStatus(obj.getString("Status"));
                                    objBean.set_timestamp(obj.getString("Timestamp"));
                                    objBean.set_buyerMobile(obj.getString("BuyerMobile"));
                                    objBean.set_id(obj.getString("ProductID"));
                                    objBean.set_deliveryAdress(obj.getString("DeliveryAdress"));
                                    objBean.set_UserMobileNumber(obj.getString("UserAdressMobileNumber"));
                                    objBean.set_fullname(obj.getString("UserName"));
                                    rowcount = Integer.parseInt(obj.getString("rowcount"));
                                    objArrayList.add(objBean);
                                }

                            }
                            catch (Exception e)
                            {
                                e.printStackTrace();
                                loading.dismiss();
                            }
                        }
                        limitstart = limitstart+Utility.LIMIT_INTERVAL;

                        DeliveredOrderAdapter ca = new DeliveredOrderAdapter(objArrayList, getActivity());
                        ca.setonClickListner(mClicklistner);
                        recList.setAdapter(ca);


                        loading.dismiss();

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
                params.put("mobilenumber", Login.mobileno);
                // params.put("email",Login.email);
                params.put("limitstart",limitstart1+"");
                params.put("limitend",limitend1+"");
                params.put("flag","Seller");
                params.put("searchquery",searchquery);
                params.put("Status","Delivered");
                return params;
            }
        };

        //Adding the request to the queue
        rQueue.add(stringRequest);
    }

    private InfiniteScrollListener createInfiniteScrollListener() {
        return new InfiniteScrollListener(Utility.LIMIT_INTERVAL, llm) {

            @Override public void onScrolledToEnd(final int firstVisibleItemPosition) {
                System.out.println("firdtvisibleitem------------>"+firstVisibleItemPosition);
                // load your items here
                // logic of loading items will be different depending on your specific use case

                // when new items are loaded, combine old and new items, pass them to your adapter
                // and call refreshView(...) method from InfiniteScrollListener class to refresh RecyclerView
                if(limitstart<rowcount) {
                    FetchOrderListFromServer(limitstart, Utility.LIMIT_INTERVAL);
                    refreshView(recList, null, firstVisibleItemPosition);
                }
            }
        };
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
       /* FetchOrderListFromServer();
        System.out.println("====================finish--------------------------");
        super.onActivityResult(requestCode, resultCode, data);*/
    }
    @Override
    public void onPlantsItemClick(int position) {

      /*  ProductlistBean obj = objArrayList.get(position);
        Bundle bundle = new Bundle();
        bundle.putSerializable("itemdetail",obj);
        startActivityForResult(new Intent(MyOrder.this,ItemDetail.class).putExtras(bundle),101);
        overridePendingTransition(R.anim.left_to_right,R.anim.hold);*/

    }
}
