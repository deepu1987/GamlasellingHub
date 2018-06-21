package in.gamlasellinghub.com.gamlasellinghub;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class SellerInformation extends AppCompatActivity {
    EditText edtstpe1merchantid,edtstpe1name,edtstep1mobile,edtstpe1emailid,edtstep2displayname,edtstep2buisnessname,edtstep2buisnessadress,edtstep2pincode,edtstep2state,edtstep2city;
    EditText edtstep3pancardname,edtstep3pancardnumber,edtstep3benificiaryname,edtstep3bankname,edtstep3bankaccountnumber,edtstep3ifsccode;
    EditText edtstep4warehousename,edtstep4warehouseadress,edtstep4pincode,edtstep4city,edtstep4gstin;
    RadioGroup radiogroup;
    RadioButton radiobutton;
    Spinner spnSpinnere,spnSpinnerestep4;
    ArrayAdapter adapter;
    Button step2Save,step3save,step4save;
    String state,statestep4;
    String email,name,mobileno,warehousename,warehouseadress,warehousepincode,warehousestate,warehousecity,warehousegstin;
    RequestQueue rQueue;
    String userinfo;
    LinearLayout llstep2form,llstep3form,llstep4form;
    String selctedBUisnessType,displayname,buisnessname,buisnessadress,pincode,city,pancardname,pancardnumber,benificiaryname,bankname,benificiaryAccountnumber,ifsccode;
    JSONObject objUserinfo,objpersonalInformation,objbuisnessinformation,objwarehouseinformation,objfinancialinformation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_information);
        rQueue = Volley.newRequestQueue(SellerInformation.this);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Gamla seller hub");
        final LinearLayout llstep1 = findViewById(R.id.step1_ll);
        final LinearLayout llstep1form = findViewById(R.id.step1);
        final LinearLayout llstep2 = findViewById(R.id.step2_ll);
        llstep2form = findViewById(R.id.step2);
        final LinearLayout llstep3 = findViewById(R.id.step3_ll);
        llstep3form = findViewById(R.id.step3);
        final LinearLayout llstep4 = findViewById(R.id.step4_ll);
        llstep4form = findViewById(R.id.step4);
        Bundle bundle = getIntent().getExtras();
        userinfo = bundle.getString("userinformation");
        try {
             objUserinfo = new JSONObject(userinfo);
             objbuisnessinformation = objUserinfo.getJSONObject("buisnessinformation");
             objfinancialinformation = objUserinfo.getJSONObject("financialinformation");
             objpersonalInformation = objUserinfo.getJSONObject("personalInformation");
             objwarehouseinformation = objUserinfo.getJSONObject("warehouseinformation");
             mobileno =objpersonalInformation.getString("mobileno");
             email = objpersonalInformation.getString("email");
        }
        catch (Exception e)
        {

        }

        Random rnd = new Random();
        final int merchantid = 10000000 + rnd.nextInt(90000000);
        final Button btnstep4 = findViewById(R.id.btnStep4);
        final Animation slideUpAnimation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slide_up_animation);

        final Animation slideDownAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_down_animation);
        btnstep4.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent i = new Intent(SellerInformation.this,Home.class);
                startActivityForResult(i,101);
                overridePendingTransition(R.anim.slide_in_left, R.anim.hold);
            }
        });
        llstep1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(llstep1form.getVisibility()==View.GONE)

                {
                    llstep1form.setVisibility(View.VISIBLE);
                    llstep2form.setVisibility(View.GONE);
                    llstep3form.setVisibility(View.GONE);
                    llstep4form.setVisibility(View.GONE);
                    edtstep1mobile = findViewById(R.id.step1mobileno);
                    edtstpe1emailid = findViewById(R.id.stpe1emailid);
                    edtstpe1merchantid = findViewById(R.id.merchanid);
                    edtstpe1name = findViewById(R.id.stpep1name);
                    try {

                        edtstpe1name.setText(objpersonalInformation.getString("name"));
                        // edtstpe1merchantid.setText(merchantid+"");
                        edtstpe1emailid.setText(objpersonalInformation.getString("email"));
                        edtstep1mobile.setText(objpersonalInformation.getString("mobileno"));
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();;
                    }



                   // llstep1form.startAnimation(slideDownAnimation);

                }
                else{
                    llstep1form.setVisibility(View.GONE);
                  //  llstep1form.startAnimation(slideUpAnimation);
                }
            }
        });
        llstep2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(llstep2form.getVisibility()==View.GONE)

                {
                    spnSpinnere = findViewById(R.id.step2state);
                    adapter =ArrayAdapter.createFromResource(SellerInformation.this, R.array.state_name, android.R.layout.simple_spinner_item);

                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spnSpinnere.setAdapter(adapter);

                    radiogroup = findViewById(R.id.buisnesstyperg);
                    edtstep2displayname = findViewById(R.id.step2name);
                    edtstep2buisnessadress =findViewById(R.id.step2buisnessadress);
                    edtstep2buisnessname = findViewById(R.id.step2buisnessname);
                    edtstep2pincode = findViewById(R.id.step2pincode);
                    edtstep2city = findViewById(R.id.step2city);
                    step2Save = findViewById(R.id.step2save);
                    llstep2form.setVisibility(View.VISIBLE);
                    llstep1form.setVisibility(View.GONE);
                    llstep3form.setVisibility(View.GONE);
                    llstep4form.setVisibility(View.GONE);

                    try{
                        String status = objbuisnessinformation.getString("status");
                        if(status.equalsIgnoreCase("true"))
                        {
                            edtstep2city.setText(objbuisnessinformation.getString("city"));
                            edtstep2pincode.setText(objbuisnessinformation.getString("PinCode"));
                            edtstep2buisnessadress.setText(objbuisnessinformation.getString("RegisteredBuisnessAdress"));
                            edtstep2buisnessname.setText(objbuisnessinformation.getString("regBuisnessName"));
                            edtstep2displayname.setText(objbuisnessinformation.getString("displayname"));
                            if(objbuisnessinformation.getString("buisnestype").equalsIgnoreCase("Sole Proprietorship")) {
                                ((RadioButton) radiogroup.getChildAt(0)).setChecked(true);
                            }
                            else if(objbuisnessinformation.getString("buisnestype").equalsIgnoreCase("Pvt Ltd./Publuic Ltd"))
                            {
                                ((RadioButton) radiogroup.getChildAt(1)).setChecked(true);
                            }
                            else
                            {
                                ((RadioButton) radiogroup.getChildAt(2)).setChecked(true);
                            }
                            int spinnerPosition = adapter.getPosition(objbuisnessinformation.getString("State"));
                            spnSpinnere.setSelection(spinnerPosition);

                        }
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }


                    spnSpinnere.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            state = adapter.getItem(i).toString();
                            Toast.makeText(getApplicationContext(),"satate->"+state,Toast.LENGTH_SHORT).show();

                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });
                    step2Save.setOnClickListener(new View.OnClickListener(){
                        @Override
                        public void onClick(View view) {

                            int selectedId = radiogroup.getCheckedRadioButtonId();
                            radiobutton = findViewById(selectedId);
                            selctedBUisnessType = radiobutton.getText().toString();
                            displayname = edtstep2displayname.getText().toString();
                            buisnessname = edtstep2buisnessname.getText().toString();
                            buisnessadress = edtstep2buisnessadress.getText().toString();
                            pincode = edtstep2pincode.getText().toString();
                            city = edtstep2city.getText().toString();
                            String var = "true";
                            if(displayname.length()<=0)
                            {
                                var="false";
                                edtstep2displayname.setError("Enter Display Name");
                            }
                            if(buisnessname.length()<=0)
                            {
                                var="false";
                                edtstep2buisnessname.setError("Enter Buisness Name");
                            }
                            if(buisnessadress.length()<=0)
                            {
                                var="false";
                                edtstep2buisnessadress.setError("Enter Buisness Address");
                            }
                            if(pincode.length()<=0)
                            {
                                var="false";
                                edtstep2pincode.setError("Enter Pincode");
                            }
                            if(state.equalsIgnoreCase("Select State"))
                            {
                                var="false";
                                Toast.makeText(getApplicationContext(),"Please select state",Toast.LENGTH_SHORT).show();
                            }
                            if(city.length()<=0)
                            {
                                var="false";
                                edtstep2city.setError("Enter city");
                            }
                            if(var.equalsIgnoreCase("true")) {
                                SaveStep2Information();
                            }




                        }
                    });

                   // llstep2form.startAnimation(slideDownAnimation);
                }
                else{
                    llstep2form.setVisibility(View.GONE);
                  //  llstep2form.startAnimation(slideUpAnimation);
                }
            }
        });
        llstep3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(llstep3form.getVisibility()==View.GONE)

                {
                    llstep3form.setVisibility(View.VISIBLE);
                    llstep2form.setVisibility(View.GONE);
                    llstep1form.setVisibility(View.GONE);
                    llstep4form.setVisibility(View.GONE);

                    edtstep3bankaccountnumber = findViewById(R.id.step3bankAccountNumber);
                    edtstep3bankname = findViewById(R.id.step3bankAccountName);
                    edtstep3benificiaryname = findViewById(R.id.step3benificiaryname);
                    edtstep3ifsccode = findViewById(R.id.step3ifsccode);
                    edtstep3pancardname = findViewById(R.id.step3pancardname);
                    edtstep3pancardnumber = findViewById(R.id.step3pancardnumber);
                    step3save = findViewById(R.id.step3Save);

                    try{
                        String status = objfinancialinformation.getString("status");
                        if(status.equalsIgnoreCase("true"))
                        {
                            edtstep3ifsccode.setText(objfinancialinformation.getString("BankIFSCCode"));
                            edtstep3bankname.setText(objfinancialinformation.getString("BenificaryBankName"));
                            edtstep3benificiaryname.setText(objfinancialinformation.getString("BenificaryName"));
                            edtstep3bankaccountnumber.setText(objfinancialinformation.getString("BenificiarayBankAccountNumber"));
                            edtstep3pancardname.setText(objfinancialinformation.getString("PanCardName"));
                            edtstep3pancardnumber.setText(objfinancialinformation.getString("PanCardNumber"));
                        }

                    }
                    catch (Exception e)
                    {

                    }

                    step3save.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            pancardname = edtstep3pancardname.getText().toString();
                            pancardnumber = edtstep3bankaccountnumber.getText().toString();
                            benificiaryname = edtstep3benificiaryname.getText().toString();
                            benificiaryAccountnumber = edtstep3bankaccountnumber.getText().toString();
                            bankname = edtstep3bankname.getText().toString();
                            ifsccode = edtstep3ifsccode.getText().toString();
                            String var = "true";
                            if(pancardnumber.length()<=0)
                            {
                                var = "false";
                                edtstep3pancardnumber.setError("Enter Pancard number");
                            }
                            if(pancardname.length()<=0)
                            {
                                var = "false";
                                edtstep3pancardname.setError("Enter Pancard Name");
                            }
                            if(benificiaryAccountnumber.length()<=0)
                            {
                                var = "false";
                                edtstep3bankaccountnumber.setError("Enter Account number");
                            }
                            if(benificiaryname.length()<=0)
                            {
                                var = "false";
                                edtstep3benificiaryname.setError("Enter Benificiray Bame");
                            }
                            if(bankname.length()<=0)
                            {
                                var = "false";
                                edtstep3bankname.setError("Enter Bank name");
                            }
                            if(ifsccode.length()<=0)
                            {
                                var = "false";
                                edtstep3ifsccode.setError("Enter IFSC code");
                            }
                            if(var.equalsIgnoreCase("true"))
                            {
                                SaveStep3RecordOnServer();
                            }


                        }
                    });



                   // llstep3form.startAnimation(slideDownAnimation);
                }
                else{
                    llstep3form.setVisibility(View.GONE);
                   // llstep3form.startAnimation(slideUpAnimation);
                }
            }
        });
        llstep4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(llstep4form.getVisibility()==View.GONE)
                {
                    spnSpinnerestep4 = findViewById(R.id.step4state);
                    adapter =ArrayAdapter.createFromResource(SellerInformation.this, R.array.state_name, android.R.layout.simple_spinner_item);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spnSpinnerestep4.setAdapter(adapter);
                    spnSpinnerestep4.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            warehousestate = adapter.getItem(i).toString();
                            Toast.makeText(getApplicationContext(),"state->"+warehousestate,Toast.LENGTH_SHORT).show();

                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });

                    llstep4form.setVisibility(View.VISIBLE);
                    llstep2form.setVisibility(View.GONE);
                    llstep3form.setVisibility(View.GONE);
                    llstep1form.setVisibility(View.GONE);

                    edtstep4city = findViewById(R.id.step4city);
                    edtstep4gstin = findViewById(R.id.step4gstin);
                    edtstep4pincode = findViewById(R.id.step4pincode);
                    edtstep4warehouseadress = findViewById(R.id.step4warehouseadress);
                    edtstep4warehousename = findViewById(R.id.step4warehousename);
                    step4save = findViewById(R.id.btnStep4);

                    try{
                        String status = objwarehouseinformation.getString("status");
                        if(status.equalsIgnoreCase("true"))
                        {
                            edtstep4warehouseadress.setText(objwarehouseinformation.getString("WarehouseAdress"));
                            edtstep4city.setText(objwarehouseinformation.getString("WarehouseCity"));
                            edtstep4gstin.setText(objwarehouseinformation.getString("GstIn"));
                            edtstep4warehousename.setText(objwarehouseinformation.getString("WarehouseName"));
                            edtstep4pincode.setText(objwarehouseinformation.getString("WarehousePincode"));
                            int spinnerPosition = adapter.getPosition(objwarehouseinformation.getString("WarehouseState"));
                            spnSpinnerestep4.setSelection(spinnerPosition);
                        }

                    }
                    catch (Exception e)
                    {

                    }

                    btnstep4.setOnClickListener(new View.OnClickListener(){
                        @Override
                        public void onClick(View view) {
                            warehouseadress = edtstep4warehouseadress.getText().toString();
                            warehousecity = edtstep4city.getText().toString();
                            warehousegstin = edtstep4gstin.getText().toString();
                            warehousename = edtstep4warehousename.getText().toString();
                            warehousepincode = edtstep4pincode.getText().toString();
                            String var = "true";
                            if(warehousepincode.length()<=0)
                            {
                                var = "false";
                                edtstep4pincode.setError("Enter Pincode");
                            }
                            if(warehousename.length()<=0)
                            {
                                var = "false";
                                edtstep4warehousename.setError("Enter Warehouse NAme");
                            }
                            if(warehousegstin.length()<=0)
                            {
                                var = "false";
                                edtstep4gstin.setError("Enter GSTIN");
                            }
                            if(warehousecity.length()<=0)
                            {
                                var = "false";
                                edtstep4city.setError("Enter City");
                            }
                            if(warehouseadress.length()<=0)
                            {
                                var = "false";
                                edtstep4warehouseadress.setError("Enter Warehouse Address");
                            }
                            if(warehousestate.equalsIgnoreCase("Slect State"))
                            {
                                var = "false";
                                Toast.makeText(getApplicationContext(),"Please select city",Toast.LENGTH_SHORT).show();
                            }
                            if(var.equalsIgnoreCase("true"))
                            {
                                SaveStep4RecordOnServer();
                            }
                        }
                    });

                    //llstep4form.startAnimation(slideDownAnimation);



                }
                else{
                    llstep4form.setVisibility(View.GONE);
                  //  llstep4form.startAnimation(slideUpAnimation);
                }
            }
        });

    }

    private void  SaveStep2Information()
    {
        final ProgressDialog loading = ProgressDialog.show(SellerInformation.this, "Submit", "Please wait...", false,false);


        StringRequest stringRequest = new StringRequest(Request.Method.POST, Utility.SAVE_BUISNESS_INFORMATION_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("res------------->"+response);
                        //if the server response is success
                        String res = response+"";
                        if(res.contains("false")){
                            //dismissing the progressbar
                            loading.dismiss();
                            Toast.makeText(SellerInformation.this,"Something went wrong",Toast.LENGTH_SHORT).show();
                            //Starting a new activity


                        }else if(res.equalsIgnoreCase("Record Saved already")) {

                            loading.dismiss();
                            Toast.makeText(SellerInformation.this,"Record Saved already",Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            loading.dismiss();
                            llstep2form.setVisibility(View.GONE);
                            try {

                            }
                            catch (Exception e)
                            {
                                Toast.makeText(SellerInformation.this,"Record Saved",Toast.LENGTH_SHORT).show();
                            }
                        }
                            //Displaying a toast if the otp entered is wrong




                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(SellerInformation.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                JSONObject obj = new JSONObject();
                try {
                    obj.put("email", email);
                    obj.put("mobileno",mobileno);
                    obj.put("buisnesstype",selctedBUisnessType);
                    obj.put("displayname",displayname);
                    obj.put("buisnessname",buisnessname);
                    obj.put("buisnessadress",buisnessadress);
                    obj.put("pincode",pincode);
                    obj.put("state",state);
                    obj.put("city",city);
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

    private void  SaveStep3RecordOnServer()
    {
        final ProgressDialog loading = ProgressDialog.show(SellerInformation.this, "Submit", "Please wait...", false,false);


        StringRequest stringRequest = new StringRequest(Request.Method.POST, Utility.SAVE_FINANCIAL_INFORMATION_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("res------------->"+response);
                        //if the server response is success
                        String res = response+"";
                        if(res.contains("false")){
                            //dismissing the progressbar
                            loading.dismiss();
                            Toast.makeText(SellerInformation.this,"Something went wrong",Toast.LENGTH_SHORT).show();
                            //Starting a new activity


                        }else if(res.equalsIgnoreCase("Record Saved already")) {

                            loading.dismiss();
                            Toast.makeText(SellerInformation.this,"Record Saved already",Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            loading.dismiss();
                            llstep3form.setVisibility(View.GONE);
                            Toast.makeText(SellerInformation.this,"Record Saved",Toast.LENGTH_SHORT).show();
                            llstep4form.setVisibility(View.VISIBLE);
                        }
                        //Displaying a toast if the otp entered is wrong




                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(SellerInformation.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                JSONObject obj = new JSONObject();
                try {
                    obj.put("email", email);
                    obj.put("mobileno",mobileno);
                    obj.put("pancardnumber",pancardnumber);
                    obj.put("pancardname",pancardname);
                    obj.put("benificiaryname",benificiaryname);
                    obj.put("accountnumber",benificiaryAccountnumber);
                    obj.put("bankname",bankname);
                    obj.put("ifsccode",ifsccode);

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
    private void  SaveStep4RecordOnServer()
    {
        final ProgressDialog loading = ProgressDialog.show(SellerInformation.this, "Submit", "Please wait...", false,false);


        StringRequest stringRequest = new StringRequest(Request.Method.POST, Utility.SAVE_WAREHOUSE_INFORMATION_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("res------------->"+response);
                        //if the server response is success
                        String res = response+"";
                        if(res.contains("false")){
                            //dismissing the progressbar
                            loading.dismiss();
                            Toast.makeText(SellerInformation.this,"Something went wrong",Toast.LENGTH_SHORT).show();
                            //Starting a new activity


                        }else if(res.equalsIgnoreCase("Record Saved already")) {

                            loading.dismiss();
                            Toast.makeText(SellerInformation.this,"Record Saved already",Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            loading.dismiss();

                            Toast.makeText(SellerInformation.this,"Record Saved",Toast.LENGTH_SHORT).show();
                            llstep4form.setVisibility(View.GONE);
                            startActivity(new Intent(SellerInformation.this,Home.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK));
                        }
                        //Displaying a toast if the otp entered is wrong




                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(SellerInformation.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                JSONObject obj = new JSONObject();
                try {
                    obj.put("email", email);
                    obj.put("mobileno",mobileno);
                    obj.put("warehouseadress",warehouseadress);
                    obj.put("warehousecity",warehousecity);
                    obj.put("warehousegstin",warehousegstin);
                    obj.put("warehousename",warehousename);
                    obj.put("warehousepincode",warehousepincode);
                    obj.put("warehousestate",warehousestate);

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
    @Override
    public boolean onSupportNavigateUp() {

        onBackPressed();

        return super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //overridePendingTransition(R.anim.slide_in_left, R.anim.hold);
    }
}
