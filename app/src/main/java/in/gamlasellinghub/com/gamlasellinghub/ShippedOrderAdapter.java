package in.gamlasellinghub.com.gamlasellinghub;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Deepanshu on 03-01-2018.
 */

public class ShippedOrderAdapter extends RecyclerView.Adapter<ShippedOrderAdapter.ShippedOrderViewHolder> {

    static onPlantsItemClickListner mlistner;
    View itemView;
    ArrayList<ProductlistBean> objArrayList;
    Context context;

    public ShippedOrderAdapter(ArrayList<ProductlistBean> objArraylist, Context context)
    {
        this.objArrayList = objArraylist;
        this.context = context;
    }

    @Override
    public ShippedOrderViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        itemView = LayoutInflater.
                from(parent.getContext()).
                inflate(R.layout.order_shipped_list_row, parent, false);
        // itemView.setOnClickListener();

        return new ShippedOrderViewHolder(itemView);
    }
    public void setonClickListner(onPlantsItemClickListner mlistner)
    {
        this.mlistner = mlistner;
    }
    @Override

    public void onBindViewHolder(ShippedOrderViewHolder holder, int position) {
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
        holder.txtadress.setText(obj.get_fullname()+" , "+obj.get_deliveryAdress());
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



    }

    @Override
    public int getItemCount() {
        return objArrayList.size();
    }

    public static class ShippedOrderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView txtPrice,txtname,txtorderid,txtdate,txtStatus,txtadress;
        private CircleImageView ivImage;
        public ShippedOrderViewHolder(View v) {


            super(v);
            txtname = (TextView) v.findViewById(R.id.orderproductname);
            txtPrice = (TextView) v.findViewById(R.id.orderrs);
            txtdate = v.findViewById(R.id.orderdate);
            txtorderid = v.findViewById(R.id.ordernumber);
            txtStatus = v.findViewById(R.id.orderstatus);
            ivImage = v.findViewById(R.id.orderimage);
            txtadress = v.findViewById(R.id.delivertoshipped);


            v.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            mlistner.onPlantsItemClick(getAdapterPosition());
            //Toast.makeText(view.getContext(), "position = " + getPosition(), Toast.LENGTH_SHORT).show();
        }
    }
}
