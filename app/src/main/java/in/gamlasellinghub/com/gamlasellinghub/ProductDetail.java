package in.gamlasellinghub.com.gamlasellinghub;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
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

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProductDetail extends AppCompatActivity {
    LinearLayout lledtquantatity,llexpoectedPayout;
    CircleImageView ivProductImage;
    TextView txtproductName,txtmrp,txtsellingprice,txtqunatatity,txtdescription,txtexpectedPayout;
    Switch swStatus;
    String status= null;
    String totalstock,productid,updatedquantatity;
    Context ctx;
    RequestQueue rQueue;
    Dialog dialog;
    String update = "false";
    boolean statusbool;
    LinearLayout lleditProductdetail;
    ProductlistBean objBean;
    EditText edtProductName;
    EditText edtMrp;
    EditText edtSellingPrice;
    EditText edtQuanatatity;
    EditText edtDescription;
    TextView txtCancel;
    TextView txtSubmit;
    String productname;
    String productMrp;
    String productSellingPrice;
    String productQuantatity;
    String productDescription;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        ctx = this;
        rQueue = Volley.newRequestQueue(this);
        ivProductImage = findViewById(R.id.productimage);
        txtdescription = findViewById(R.id.productdescription);
        txtexpectedPayout = findViewById(R.id.expectedpayout);
        txtproductName = findViewById(R.id.productname);
        txtmrp = findViewById(R.id.mrp);
        txtqunatatity = findViewById(R.id.totalquantatity);
        txtsellingprice = findViewById(R.id.sellingprice);
        swStatus = findViewById(R.id.productstatus);
        lleditProductdetail = findViewById(R.id.editproductdetail);
        Bundle bundle = getIntent().getExtras();
        objBean = (ProductlistBean) bundle.getSerializable("productdetail");

        totalstock = objBean.get_quantatity();
        Picasso.with(ctx)
                .load(objBean.get_imagepath())
                .placeholder(R.mipmap.ic_launcher) // optional
                .error(R.mipmap.ic_launcher)         // optional
                .into(ivProductImage);
        txtsellingprice.setText(objBean.get_sellingPrice());
        txtqunatatity.setText(objBean.get_quantatity());
        txtmrp.setText(objBean.get_mrp());
        txtdescription.setText(objBean.get_desceription());
        txtproductName.setText(objBean.get_productName());
        status = objBean.get_status();
        productid = objBean.get_id();
        if(status.equalsIgnoreCase("non live"))
        {
            swStatus.setChecked(false);
        }
        else
        {
            swStatus.setChecked(true);
        }

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Product Detail");
        lledtquantatity = (LinearLayout) findViewById(R.id.edtquantatity);
        llexpoectedPayout = (LinearLayout) findViewById(R.id.showexpectedpayoutdetail);
        lleditProductdetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowEditProductDetailDiaog();
            }
        });
        llexpoectedPayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent showexpectedPayout = new Intent(ProductDetail.this,ExpectedPayoutDetail.class);
                startActivity(showexpectedPayout);
            }
        });
        lledtquantatity.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                dialog = new Dialog(ctx, R.style.D1NoTitleDim);
                dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
                LayoutInflater inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View dialogView = inflater.inflate(R.layout.edit_quantatity, null);
                dialog.setContentView(dialogView);
                dialog.setCancelable(true);
                TextView txtcancel = (TextView) dialogView.findViewById(R.id.canceldialog);
                TextView txtTotalStock = dialogView.findViewById(R.id.totalstock);
                final EditText edtTotalstock = dialogView.findViewById(R.id.edttotalstock);
                txtTotalStock.setText(totalstock);
                edtTotalstock.setText(totalstock);
                Button btnSave = (Button) dialogView.findViewById(R.id.savediaog);
                btnSave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        updatedquantatity = edtTotalstock.getText().toString();
                        if(updatedquantatity.length()>0) {
                            if(Integer.parseInt(updatedquantatity)>=0) {
                                SetUpdatedQuantatityOfProductOnServer();
                            }
                            else
                            {
                                edtTotalstock.setError("Enter valid quantatity");
                            }
                        }
                        else
                        {
                            edtTotalstock.setError("Enter quantatity");
                        }
                    }
                });
                txtcancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                dialog.show();


            }
        });

        swStatus.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                statusbool = swStatus.isChecked();
                ChangeStatusOnServer();
            }
        });

    }

    private void ChangeStatusOnServer()
    {
        final ProgressDialog loading = ProgressDialog.show(ProductDetail.this, "Gamla", "Please wait...", false,false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Utility.UPDATE_STATUS_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("res------------->"+response);
                        //if the server response is success
                        String res = response+"";
                        if(res.contains("false")){
                            Toast.makeText(ProductDetail.this, "Record not updated", Toast.LENGTH_SHORT).show();
                            loading.dismiss();
                        }else{
                            try{
                                loading.dismiss();
                                update = "true";
                            }
                            catch (Exception e)
                            {
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ProductDetail.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String,String> params = new HashMap<String, String>();
                //Adding the parameters otp and username
                params.put("productid", productid);

                if(statusbool) {
                    params.put("status","In Stock" );
                }
                else
                {
                    params.put("status","Non Live" );
                }
                return params;
            }

        };

        //Adding the request to the queue
        rQueue.add(stringRequest);


    }
    private void SetUpdatedQuantatityOfProductOnServer()
    {
            final ProgressDialog loading = ProgressDialog.show(ProductDetail.this, "Gamla", "Please wait...", false,false);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, Utility.UPDATE_QUANTATIY_URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            System.out.println("res------------->"+response);
                            //if the server response is success
                            String res = response+"";
                            if(res.contains("false")){
                                Toast.makeText(ProductDetail.this, "Record not updated", Toast.LENGTH_SHORT).show();
                                loading.dismiss();
                                dialog.dismiss();
                            }else{
                               try{
                                   loading.dismiss();
                                   txtqunatatity.setText(updatedquantatity);
                                   loading.dismiss();
                                   dialog.dismiss();
                                   update = "true";

                               }
                                catch (Exception e)
                                {

                                }
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(ProductDetail.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }){

                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String,String> params = new HashMap<String, String>();
                    //Adding the parameters otp and username
                    params.put("productid", productid);
                    params.put("quantatity",updatedquantatity);
                    if(Integer.parseInt(updatedquantatity)>0)
                    {
                        params.put("status","In Stock");
                    }
                    else
                    {
                        params.put("status","Out Stock");
                    }
                    return params;
                }

            };

            //Adding the request to the queue
            rQueue.add(stringRequest);


    }

    private void ShowEditProductDetailDiaog()
    {
        dialog = new Dialog(this,android.R.style.Theme_Translucent_NoTitleBar);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation1;
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.dimAmount=0.8f;
        dialog.getWindow().setAttributes(lp);
        //dialog.getWindow().addFlags(WindowManager.LayoutParams.);
       //getWindow().setDimAmount(0.2f);
        dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.edit_product_detail);

        edtProductName = dialog.findViewById(R.id.edtproductname);
        edtMrp = dialog.findViewById(R.id.edtmrp);
        edtSellingPrice = dialog.findViewById(R.id.edtsellingprice);
        edtQuanatatity = dialog.findViewById(R.id.edtquantatity);
        edtDescription = dialog.findViewById(R.id.edtdescription);
        txtCancel = dialog.findViewById(R.id.cancelEditProductDetail);
        txtSubmit = dialog.findViewById(R.id.saveEditProductDetail);
        edtDescription.setText(objBean.get_desceription());
        edtMrp.setText(objBean.get_mrp());
        edtProductName.setText(objBean.get_productName());
        edtSellingPrice.setText(objBean.get_sellingPrice());
        edtQuanatatity.setText(objBean.get_quantatity());
        txtCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        txtSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                productname = edtProductName.getText().toString();
                productMrp = edtMrp.getText().toString();
                productSellingPrice = edtSellingPrice.getText().toString();
                productQuantatity = edtQuanatatity.getText().toString();
                productDescription = edtDescription.getText().toString();
                String var = "true";
                if(productname.length()<=0)
                {
                    var = "false";
                    edtProductName.setError("Enter Product Name");
                }
                if(productMrp.length()<=0)
                {
                    var = "false";
                    edtMrp.setError("Enter MRP");
                }
                if(!isStringInt(productMrp))
                {
                    var = "false";
                    edtMrp.setError("Enter Valid MRP");
                }


                if(productSellingPrice.length()<=0)
                {
                    var = "false";
                    edtSellingPrice.setError("Enter Selling Price");
                }
                if(!isStringInt(productSellingPrice))
                {
                    var = "false";
                    edtSellingPrice.setError("Enter Valid Selling Price");
                }
                if(Integer.parseInt(productSellingPrice)>Integer.parseInt(productMrp))
                {
                    var = "false";
                    Toast.makeText(getApplicationContext(),"Mrp should be greater than selling price",Toast.LENGTH_SHORT).show();
                }

                if(productDescription.length()<0)
                {
                    var = "false";
                    edtDescription.setError("Enter Description");
                }

                if(productQuantatity.length()<=0||Integer.parseInt(productQuantatity)<=0)
                {
                    var="false";
                    edtQuanatatity.setError("Enter valid quantatity");
                }
                if(var.equalsIgnoreCase("true"))
                {
                    EditProductDetailOnServer();
                }



            }
        });

        dialog.show();
    }

    private void EditProductDetailOnServer()
    {
        {
            final ProgressDialog loading = ProgressDialog.show(ProductDetail.this, "Gamla", "Please wait...", false,false);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, Utility.UPDATE_PRODUCT_DETAIL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            System.out.println("res------------->"+response);
                            //if the server response is success
                            String res = response+"";
                            if(res.contains("false")){
                                Toast.makeText(ProductDetail.this, "Record not updated", Toast.LENGTH_SHORT).show();
                                loading.dismiss();
                            }else{
                                try{
                                    loading.dismiss();
                                    dialog.dismiss();
                                    Toast.makeText(ProductDetail.this, "Record updated sucessfully", Toast.LENGTH_SHORT).show();
                                    update = "true";
                                    txtproductName.setText(productname);
                                    txtqunatatity.setText(productQuantatity);
                                    txtdescription.setText(productDescription);
                                    txtsellingprice.setText(productSellingPrice);
                                    txtmrp.setText(productMrp);



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
                            Toast.makeText(ProductDetail.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }){

                @Override
                protected Map<String, String> getParams() throws AuthFailureError {

                    Map<String,String> params = new HashMap<String, String>();
                    //Adding the parameters otp and username
                    params.put("productid", productid);
                    params.put("productname",productname);
                    params.put("productMrp",productMrp);
                    params.put("productSellingPrice",productSellingPrice);
                    params.put("productQuantatity",productQuantatity);
                    params.put("productDescription",productDescription);
                    if(statusbool) {
                        params.put("status","In Stock" );
                    }
                    else
                    {
                        params.put("status","Non Live" );
                    }
                    return params;
                }

            };

            //Adding the request to the queue
            rQueue.add(stringRequest);


        }
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();

        return super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        System.out.println("=======status----------------->"+update);
        if(update.equalsIgnoreCase("false")) {
            super.onBackPressed();
        }
        else
        {
            finish();
        }
    }
    public boolean isStringInt(String s)
    {
        try
        {
            Integer.parseInt(s);
            return true;
        } catch (NumberFormatException ex)
        {
            return false;
        }
    }
}
