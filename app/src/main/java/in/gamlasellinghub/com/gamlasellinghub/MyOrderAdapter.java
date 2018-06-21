package in.gamlasellinghub.com.gamlasellinghub;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Deepanshu on 03-01-2018.
 */

public class MyOrderAdapter extends RecyclerView.Adapter<MyOrderAdapter.OrderViewHolder> {


    View itemView;
    ArrayList<ProductlistBean> objArrayList;
    Context context;
    onCheckBoxClickListner mlistner;
    ArrayList<String> positionarraylist;
    RequestQueue rQueue;
    public MyOrderAdapter(ArrayList<ProductlistBean> objArraylist, Context context)
    {
        this.objArrayList = objArraylist;
        this.context = context;
        rQueue = Volley.newRequestQueue(context);
    }

    @Override
    public OrderViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        itemView = LayoutInflater.
                from(parent.getContext()).
                inflate(R.layout.order_list_row, parent, false);
        // itemView.setOnClickListener();

        return new OrderViewHolder(itemView);
    }
    public void setonClickListner(onCheckBoxClickListner mlistner)
    {
        this.mlistner = mlistner;
    }
    @Override

    public void onBindViewHolder(final OrderViewHolder holder, final int position) {
        // holder.bind(items.get(position), listener);
        ProductlistBean obj = objArrayList.get(position);
        Picasso.with(context)
                .load(obj.get_imagepath())
                .placeholder(R.mipmap.ic_launcher) // optional
                .error(R.mipmap.ic_launcher)         // optional
                .into(holder.ivImage);
        holder.txtPrice.setText("Rs. "+obj.get_sellingPrice());
        holder.txtname.setText(obj.get_productName());
        holder.txtorderid.setText(obj.get_orderId());
        holder.txtStatus.setText(obj.get_orderStatus());
        holder.txtAdress.setText(obj.get_fullname()+" , "+obj.get_deliveryAdress());
        holder.chkSelected.setChecked(obj.isSelected());
        holder.chkSelected.setTag(obj);
        holder.itemView.setTag(obj);
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy HH:mm a");
        try {
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(Long.parseLong(obj.get_timestamp()));
            String date  = sdf.format(cal.getTime());
            holder.txtdate.setText(date+"");

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        holder.txtAddToShipment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = objArrayList.indexOf(holder.itemView.getTag());
                SetRejectOrShippedStatusOfOrderOnServer(position,"Shipped");
            }
        });
        holder.txtReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int position = objArrayList.indexOf(holder.itemView.getTag());
                SetRejectOrShippedStatusOfOrderOnServer(position,"Rejected");
                //Toast.makeText(context, "hello--->"+objArrayList.get(position).get_productName(), Toast.LENGTH_SHORT).show();
            }
        });
        holder.chkSelected.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton view, boolean b) {
                {
                    positionarraylist = new ArrayList<>();
                    CheckBox cb =  (CheckBox) view;
                    ProductlistBean objbean = (ProductlistBean) cb.getTag();
                    objbean.setSelected(b);
                    objArrayList.get(position).setSelected(b);
                    for(int i = 0;i<objArrayList.size();i++)
                    {
                        ProductlistBean productlistBean = objArrayList.get(i);
                        if(productlistBean.isSelected()) {
                            positionarraylist.add(i+"");
                        }
                    }
                    mlistner.onCheckBoxClick(positionarraylist);

                }
            }
        });
    }
    @Override
    public int getItemCount() {
        return objArrayList.size();
    }

    public static class OrderViewHolder extends RecyclerView.ViewHolder {

        private TextView txtPrice,txtname,txtorderid,txtdate,txtStatus,txtAdress,txtReject,txtAddToShipment;
        public CheckBox chkSelected;
        private CircleImageView ivImage;
        public OrderViewHolder(View v) {
            super(v);
            txtname = (TextView) v.findViewById(R.id.orderproductname);
            txtPrice = (TextView) v.findViewById(R.id.orderrs);
            txtdate = v.findViewById(R.id.orderdate);
            txtorderid = v.findViewById(R.id.ordernumber);
            txtStatus = v.findViewById(R.id.orderstatus);
            ivImage = v.findViewById(R.id.orderimage);
            txtAdress = v.findViewById(R.id.deliverto);
            chkSelected = v.findViewById(R.id.ordercheck);
            txtReject = v.findViewById(R.id.orderreject);
            txtAddToShipment = v.findViewById(R.id.AddToShipment);
            this.setIsRecyclable(false);
          //  v.setOnClickListener(this);
        }

      /*  @Override
        public void onClick(View view) {
            mlistner.onPlantsItemClick(getAdapterPosition());
            //Toast.makeText(view.getContext(), "position = " + getPosition(), Toast.LENGTH_SHORT).show();
        }*/
    }
    public void SetRejectOrShippedStatusOfOrderOnServer(final int position,final String Status)
    {
        final ProgressDialog loading = ProgressDialog.show(context, "Gamla", "Please wait...", false,false);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Utility.REJECT_ORDER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("res------------->"+response);
                        //if the server response is success
                        String res = response+"";
                        if(res.contains("false")){
                            loading.dismiss();

                        }else{

                            Toast.makeText(context, "Item Rejected", Toast.LENGTH_SHORT).show();
                            objArrayList.remove(position);
                            notifyDataSetChanged();
                        }


                        loading.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String,String> params = new HashMap<String, String>();
                //Adding the parameters otp and username
                long timestamp = System.currentTimeMillis();

                SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
                String date = null;
                try {
                    Calendar cal = Calendar.getInstance();
                    cal.setTimeInMillis(timestamp);
                     date  = sdf.format(cal.getTime());
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
                params.put("orderid",objArrayList.get(position).get_orderId());
                params.put("Status",Status);
                params.put("ProductName",objArrayList.get(position).get_productName());
                params.put("BuyerMobile",objArrayList.get(position).get_buyerMobile());
                params.put("timestamp",timestamp+"");
                params.put("timestampforsearch",date);
                return params;
            }

        };

        //Adding the request to the queue
        rQueue.add(stringRequest);
    }
}
