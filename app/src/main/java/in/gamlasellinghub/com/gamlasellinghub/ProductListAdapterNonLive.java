package in.gamlasellinghub.com.gamlasellinghub;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Deepanshu on 07-03-2018.
 */

public class ProductListAdapterNonLive extends RecyclerView.Adapter<ProductListAdapterNonLive.ProductCategoryListViewHolder> {

    public Context context;
    View itemView;
    static onProductclicklistner mlistner;
    ArrayList<ProductlistBean> objArrayList;
    public ProductListAdapterNonLive(Context context, ArrayList<ProductlistBean> objArrayList)
    {
        this.context = context;
        this.objArrayList = objArrayList;
    }
    public void setonClickListner(onProductclicklistner mlistner)
    {
        this.mlistner = mlistner;
    }
    @Override
    public ProductListAdapterNonLive.ProductCategoryListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        itemView = LayoutInflater.
                from(parent.getContext()).
                inflate(R.layout.product_row, parent, false);
        // itemView.setOnClickListener();

        return new ProductListAdapterNonLive.ProductCategoryListViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ProductListAdapterNonLive.ProductCategoryListViewHolder holder, int position) {

        ProductlistBean obj = objArrayList.get(position);
        holder.txtproductname.setText(obj.get_productName());
        holder.txtskuid.setText(obj.get_skuid());
        holder.txtsellingprice.setText(obj.get_sellingPrice()+" Rs.");
        holder.txtquanatity.setText(obj.get_quantatity());
        if(Integer.parseInt(obj.get_quantatity())<=2)
        {
            holder.txtfewleft.setVisibility(View.VISIBLE);
        }
        else
        {
            holder.txtfewleft.setVisibility(View.GONE);
        }
        //holder.ivimage.setImageResource(R.drawable.chrysanthemum);
        Picasso.with(context)
                .load(obj.get_imagepath())
                .placeholder(R.mipmap.ic_launcher) // optional
                .error(R.mipmap.ic_launcher)         // optional
                .into(holder.ivimage);

    }

    @Override
    public int getItemCount() {
        return objArrayList.size();
    }


    public static class ProductCategoryListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        CircleImageView ivimage;
        TextView txtproductname,txtskuid,txtsellingprice,txtquanatity,txtfewleft;


        public ProductCategoryListViewHolder(View v) {


            super(v);
            ivimage = v.findViewById(R.id.productimage);
            txtproductname = v.findViewById(R.id.productname);
            txtquanatity = v.findViewById(R.id.quantatity);
            txtsellingprice = v.findViewById(R.id.sellingprice);
            txtskuid = v.findViewById(R.id.skuid);
            txtfewleft = v.findViewById(R.id.fewleft);

            v.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            mlistner.onProductclick(getAdapterPosition());
            //Toast.makeText(view.getContext(), "position = " + getPosition(), Toast.LENGTH_SHORT).show();
        }
    }

}
