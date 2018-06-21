package in.gamlasellinghub.com.gamlasellinghub;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SearchCatalog extends AppCompatActivity {


  // String[] status = {"Sekect Status","In Stock","Out of stock","Non live"};
   String[] catagory = {"Select Catagory","Indoor Plants","Outdoor Plants","Pots","Antiques With Plants","Fruit Plants","Fertilizer and Pestisides","Seeds","Landscapers","Gardening Tools"};
   EditText edtProductid,edtweight,edtproductname,edtskuid;
   String var = "false";
   String status;
   Button submit;
   RequestQueue rQueue;
   String searchquery="";

   //ArrayList<HashMap<String,String>> objArrayList = new ArrayList<>();
   @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_catalog);
        rQueue = Volley.newRequestQueue(SearchCatalog.this);
        status = getIntent().getStringExtra("status");
        System.out.println("status------->"+status);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Search Catalog");
        final Spinner spncategory = findViewById(R.id.searchCategory);
        edtProductid = findViewById(R.id.searchProductid);
        edtproductname = findViewById(R.id.searchproductname);
        edtskuid = findViewById(R.id.searchskuid);
        edtweight = findViewById(R.id.searchweight);
        submit = findViewById(R.id.searchsubmit);
        //shipday.setOnItemSelectedListener(this);
        ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,catagory);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spncategory.setAdapter(aa);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HashMap<String,String> map = new HashMap();
                if(spncategory.getSelectedItem().toString().equalsIgnoreCase("Select Catagory")&&edtProductid.getText().toString().length()<=0&&edtproductname.getText().toString().length()<=0&&edtskuid.getText().toString().length()<=0&&edtweight.getText().toString().length()<=0){
                    Toast.makeText(SearchCatalog.this, "Enter at least one record", Toast.LENGTH_SHORT).show();
                }
                else
                {
                  //  map.put("Status",status);
                    if(!spncategory.getSelectedItem().toString().equalsIgnoreCase("Select Catagory"))
                    {
                        map.put("Categoryname",spncategory.getSelectedItem().toString());
                    }
                    if(edtProductid.getText().toString().length()>0)
                    {
                        map.put("ProuctId",edtProductid.getText().toString());
                    }
                    if(edtproductname.getText().toString().length()>0)
                    {
                        map.put("ProductName",edtproductname.getText().toString());
                    }
                    if(edtskuid.getText().toString().length()>0)
                    {
                        map.put("MerchantSkuId",edtskuid.getText().toString());
                    }
                    if(edtweight.getText().toString().length()>0)
                    {
                        map.put("weight",edtweight.getText().toString());
                    }
                   // objArrayList.add(map);
                    int i = 1;
                    for(Map.Entry<String,String> entry : map.entrySet())
                    {
                        if(i<map.size()) {
                            searchquery = searchquery + entry.getKey() + "='" + entry.getValue() + "' and ";
                            i++;
                        }
                        else
                        {
                            searchquery = searchquery + entry.getKey() + "='" + entry.getValue() + "'";
                            i++;
                        }
                    }

                }
                System.out.println("searchquery------------->"+searchquery);

                Intent i = new Intent();
                i.putExtra("Searchquery",searchquery);
                setResult(RESULT_OK,i);
                finish();

            }
        });


    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent();
        i.putExtra("Searchquery",searchquery);
        setResult(RESULT_OK,i);
        finish();
    }
}
