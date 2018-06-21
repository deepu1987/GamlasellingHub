package in.gamlasellinghub.com.gamlasellinghub;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {
    EditText edtmail,edtpassword;
    static String email,password,mobileno;
    RequestQueue rQueue;
    TextView txtForgotpassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        edtmail = findViewById(R.id.loginEdtmail);
        edtpassword = findViewById(R.id.android_hide_show_edittext_password_login);
        txtForgotpassword = findViewById(R.id.forgotpassword);
        TextView txtgalmlasellerhub = (TextView) findViewById(R.id.txtgamlasellerhub);
        Button login = findViewById(R.id.btnLogin);
        FontsUtils fu = new FontsUtils(this);
        txtgalmlasellerhub.setTypeface(fu.setFont("Grand_Aventure.otf"));
        LinearLayout llcretestore = (LinearLayout) findViewById(R.id.cretestore);
        rQueue = Volley.newRequestQueue(Login.this);
        txtForgotpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ShowForgotPasswortDialog();
            }
        });
        llcretestore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Login.this,Registration.class);
                startActivityForResult(i,101);
                overridePendingTransition(R.anim.push_up_in, R.anim.hold);
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email = edtmail.getText().toString();
                password = edtpassword.getText().toString();
                if(email.length()<=0)
                {
                    edtmail.setError("Enter EmailId");
                }
                if(password.length()<=0)
                {
                    edtpassword.setError("Enter password");
                }
                if(email.length()>0&&password.length()>0)
                {
                    final ProgressDialog loading = ProgressDialog.show(Login.this, "Authenticating", "Please wait while we check the credential", false,false);
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, Utility.LOGIN_URL,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    //if the server response is success
                                    if(response.equalsIgnoreCase("true")){
                                        //dismissing the progressbar
                                        loading.dismiss();

                                        //Starting a new activity
                                        FetchSellerPersinalinformation();
                                      /*  startActivity(new Intent(Login.this, SellerInformation.class));
                                        overridePendingTransition(R.anim.slide_in_left, R.anim.hold);*/
                                    }else{
                                        //Displaying a toast if the otp entered is wrong
                                        loading.dismiss();
                                        if(response.equalsIgnoreCase("your account not verified by admin, contact to administration"))
                                        {
                                            CustomAlert customAlert = new CustomAlert(Login.this);
                                            customAlert.ShowDialog("Verification",response,false,"false");
                                        }
                                        else {
                                            Toast.makeText(Login.this, response, Toast.LENGTH_LONG).show();
                                        }

                                    }
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    loading.dismiss();
                                    Toast.makeText(Login.this, error.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            }){
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String,String> params = new HashMap<String, String>();
                            //Adding the parameters otp and username
                            params.put("email", email);
                            params.put("password", password);
                            return params;
                        }
                    };

                    //Adding the request to the queue
                    rQueue.add(stringRequest);
                }


            }
        });


    }
public void FetchSellerPersinalinformation()
{
    StringRequest stringRequest = new StringRequest(Request.Method.POST, Utility.PERSONAL_URL,
            new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    System.out.println("res------------->"+response);
                    //if the server response is success
                    String res = response+"";
                    if(res.contains("false")){
                        Bundle bundle = new Bundle();
                        bundle.putString("userinformation",response);
                        startActivity(new Intent(Login.this, SellerInformation.class).putExtras(bundle));
                        overridePendingTransition(R.anim.slide_in_left, R.anim.hold);
                    }else{
                        try {
                            mobileno = new JSONObject(response).getJSONObject("personalInformation").getString("mobileno");
                            System.out.println("heloo======================>"+mobileno);
                        }
                        catch (Exception e)
                        {

                        }
                        startActivity(new Intent(Login.this, Home.class));
                        overridePendingTransition(R.anim.slide_in_left, R.anim.hold);


                    }
                }
            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    Toast.makeText(Login.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }){

        @Override
        protected Map<String, String> getParams() throws AuthFailureError {
            JSONObject obj = new JSONObject();
            try {
                obj.put("email", email);
            }
            catch (Exception e)
            {

            }
            Map<String,String> params = new HashMap<String, String>();
            //Adding the parameters otp and username
            params.put("email", obj+"");

            return params;
        }

    };

    //Adding the request to the queue
    rQueue.add(stringRequest);

}
    private void ShowForgotPasswortDialog()
    {
        LayoutInflater li = LayoutInflater.from(this);
        View confirmDialog = li.inflate(R.layout.forgotpassword, null);
        AppCompatButton buttonConfirm = (AppCompatButton) confirmDialog.findViewById(R.id.buttonSubmitForgot);
        final  EditText editTextMObileNUmber = (EditText) confirmDialog.findViewById(R.id.editMobileNumber);
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setView(confirmDialog);
        final AlertDialog alertDialog = alert.create();
        alertDialog.show();
        buttonConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                final ProgressDialog loading = ProgressDialog.show(Login.this, "Authenticating", "Please wait...", false,false);
                final String mobilenumber = editTextMObileNUmber.getText().toString().trim();
                StringRequest stringRequest = new StringRequest(Request.Method.POST, Utility.FORGOT_PASSWORD,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                if(response.equalsIgnoreCase("true")){
                                    //dismissing the progressbar
                                    loading.dismiss();
                                    Toast.makeText(Login.this,"Your password sent on your mobile number",Toast.LENGTH_LONG).show();
                                    // finish();
                                }else{
                                    //Displaying a toast if the otp entered is wrong
                                    loading.dismiss();
                                    Toast.makeText(Login.this,"Mobile number not registered",Toast.LENGTH_LONG).show();

                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                alertDialog.dismiss();
                                loading.dismiss();
                                Toast.makeText(Login.this, error.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String,String> params = new HashMap<String, String>();
                        params.put("mobileno", mobilenumber);
                        params.put("flag","seller");
                        return params;
                    }
                };

                //Adding the request to the queue
                rQueue.add(stringRequest);
            }
        });
    }


}
