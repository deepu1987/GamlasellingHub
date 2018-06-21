package in.gamlasellinghub.com.gamlasellinghub;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

public class AddProduct extends AppCompatActivity implements CatalogFragment.OnFragmentInteractionListener {
    String[] days = {"Select days", "2", "3", "4" };
    Spinner spnSpinnerCategory;
    ArrayAdapter adapter;
    String category,productname,skuid,mrp,sellingprice,weight,description,encodedString,qunatity;
    ImageView productphoto;
    EditText edtProductname,edtmerchantskuid,edtmrp,edtsellingprice,edtweight,edtdescription,edtquantatity;
    Button submit;
    RequestQueue rQueue;
    String imagename;
    String shipindays="";
    TextView txtRemaing;
    //commewnt
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
        // Test changes
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Add New Product");
        boolean result=Utility.checkPermission(AddProduct.this);
        rQueue = Volley.newRequestQueue(AddProduct.this);
       // Spinner shipday = findViewById(R.id.shipdayspinner);
        //shipday.setOnItemSelectedListener(this);
        final ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,days);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        //shipday.setAdapter(aa);
        productphoto = findViewById(R.id.productphoto);
        edtdescription = findViewById(R.id.description);
        edtmerchantskuid = findViewById(R.id.skuid);
        edtmrp = findViewById(R.id.mrp);
        edtProductname = findViewById(R.id.productname);
        edtsellingprice = findViewById(R.id.sellingprice);
        edtweight = findViewById(R.id.weight);
        spnSpinnerCategory = findViewById(R.id.selectcategory);
        submit = findViewById(R.id.addSubmit);
        edtquantatity = findViewById(R.id.quantatity);
        txtRemaing = findViewById(R.id.textremainig);
        adapter =ArrayAdapter.createFromResource(AddProduct.this, R.array.category_name, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnSpinnerCategory.setAdapter(adapter);
        spnSpinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                category = adapter.getItem(i).toString();


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        edtdescription.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                txtRemaing.setText("Text Remaining : "+String.valueOf(400-s.length()));
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
       /* shipday.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                shipindays = aa.getItem(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });*/

        productphoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                productname = edtProductname.getText().toString();
                skuid = edtmerchantskuid.getText().toString();
                mrp = edtmrp.getText().toString();
                sellingprice = edtsellingprice.getText().toString();
                weight = edtweight.getText().toString();
                description = edtdescription.getText().toString();
                qunatity = edtquantatity.getText().toString();
                String var = "true";
                if(productname.length()<=0)
                {
                    var = "false";
                    edtProductname.setError("Enter Product Name");
                }
                if(skuid.length()<=0)
                {
                    var = "false";
                    edtmerchantskuid.setError("Enter SKU Id");
                }
                if(mrp.length()<=0)
                {
                    var = "false";
                    edtmrp.setError("Enter MRP");
                }
                if(!isStringInt(mrp))
                {
                    var = "false";
                    edtmrp.setError("Enter Valid MRP");
                }


                if(sellingprice.length()<=0)
                {
                    var = "false";
                    edtsellingprice.setError("Enter Selling Price");
                }
                if(!isStringInt(sellingprice))
                {
                    var = "false";
                    edtsellingprice.setError("Enter Valid Selling Price");
                }
                if(Integer.parseInt(sellingprice)>Integer.parseInt(mrp))
                {
                    var = "false";
                    Toast.makeText(getApplicationContext(),"Mrp should be greater than selling price",Toast.LENGTH_SHORT).show();
                }
                if(weight.length()<=0)
                {
                    var = "false";
                    edtweight.setError("Enter Weight");
                }
                if(!isStringInt(weight))
                {
                    var = "false";
                    edtweight.setError("Enter Valid Weight");
                }
                if(description.length()<0)
                {
                    var = "false";
                    edtdescription.setError("Enter Description");
                }
                if(category.equalsIgnoreCase("Select Category"))
                {
                    var = "false";
                    Toast.makeText(getApplicationContext(),"Please Select Category",Toast.LENGTH_SHORT).show();
                }
                if(shipindays.equalsIgnoreCase("Select days"))
                {
                    var = "false";
                    Toast.makeText(getApplicationContext(),"Please Select Shiping days",Toast.LENGTH_SHORT).show();
                }
                if(qunatity.length()<=0||Integer.parseInt(qunatity)<=0)
                {
                    var="false";
                    edtquantatity.setError("Enter valid quantatity");
                }
                if(var.equalsIgnoreCase("true"))
                {
                    SaveRecordOnServer();
                }


            }
        });
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
    private void selectImage() {
        final CharSequence[] options = { "Take Photo", "Choose from Gallery","Cancel" };
        AlertDialog.Builder builder = new AlertDialog.Builder(AddProduct.this);
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo"))
                {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    File f = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                    startActivityForResult(intent, 1);
                }
                else if (options[item].equals("Choose from Gallery"))
                {
                    Intent intent = new   Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, 2);
                }
                else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void SaveRecordOnServer(){
        final ProgressDialog loading = ProgressDialog.show(AddProduct.this, "Submit", "Please wait...", false,false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Utility.SAVE_PRODUCT_INFORMATION_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("res------------->"+response);
                        String res = response+"";
                        if(res.contains("false")){
                            loading.dismiss();
                            Toast.makeText(AddProduct.this,"Something went wrong",Toast.LENGTH_SHORT).show();
                        }

                        else if(res.equalsIgnoreCase("Sku Id already exist"))
                        {
                            loading.dismiss();
                            Toast.makeText(AddProduct.this,res,Toast.LENGTH_SHORT).show();
                        }
                        else if(res.equalsIgnoreCase("Record Saved already")) {
                            loading.dismiss();
                            Toast.makeText(AddProduct.this,"Record Saved already",Toast.LENGTH_SHORT).show();


                        }
                        else
                        {

                            Toast.makeText(AddProduct.this,"Record Saved Sucessfully",Toast.LENGTH_SHORT).show();
                            finish();
                            loading.dismiss();

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(AddProduct.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                JSONObject obj = new JSONObject();
                try {




                    Drawable d = getResources().getDrawable(R.drawable.gamla); // the drawable (Captain Obvious, to the rescue!!!)
                    Bitmap bitmap = ((BitmapDrawable)d).getBitmap();
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                    byte[] bitmapdata = stream.toByteArray();

                    encodedString = Base64.encodeToString(bitmapdata, 0);

                    obj.put("imagename","test");
                    obj.put("image",encodedString);
                    obj.put("email", Login.email);
                    obj.put("mobileno",Login.mobileno);
                    obj.put("category",category);
                    obj.put("productname",productname);
                    obj.put("skuid",skuid);
                    obj.put("mrp",mrp);
                    obj.put("sellingprice",sellingprice);
                    obj.put("weight",weight);
                    obj.put("description",description);
                    obj.put("shipindays",shipindays);
                    obj.put("quantatity",qunatity);
                    obj.put("status","In Stock");
                }
                catch (Exception e)
                {

                }
                Map<String,String> params = new HashMap<String, String>();
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

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                File f = new File(Environment.getExternalStorageDirectory().toString());
                for (File temp : f.listFiles()) {
                    if (temp.getName().equals("temp.jpg")) {
                        f = temp;
                        break;
                    }

                }
                try {
                    Bitmap bitmap;
                    BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
                    bitmap = BitmapFactory.decodeFile(f.getAbsolutePath(), bitmapOptions);
                    productphoto.setImageBitmap(bitmap);



                    String path = android.os.Environment.getExternalStorageDirectory()+ File.separator+ "Gamla" + File.separator + "GamlaHub";
                    f.delete();
                    OutputStream outFile = null;
                    imagename = String.valueOf(System.currentTimeMillis());
                    File file = new File(path, imagename + ".jpg");

                    try {
                        outFile = new FileOutputStream(file);

                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 85, stream);
                        outFile.write(stream.toByteArray());
                        outFile.close();
                        byte[] byte_arr = stream.toByteArray();

                        encodedString = Base64.encodeToString(byte_arr, 0);


                        outFile.flush();
                        outFile.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else if (requestCode == 2) {
                Uri selectedImage = data.getData();
                String[] filePath = { MediaStore.Images.Media.DATA };
                Cursor c = getContentResolver().query(selectedImage,filePath, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                String picturePath = c.getString(columnIndex);
                String fileNameSegments[] = picturePath.split("/");

                imagename = fileNameSegments[fileNameSegments.length - 1];
                c.close();
                Bitmap thumbnail = (BitmapFactory.decodeFile(picturePath));
                Log.w("path of image  gallery", picturePath+"");
                productphoto.setImageBitmap(thumbnail);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                thumbnail.compress(Bitmap.CompressFormat.JPEG, 85, stream);
                byte[] byte_arr = stream.toByteArray();
                encodedString = Base64.encodeToString(byte_arr, 0);

            }

        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case Utility.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    //code for deny
                }
                break;
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
