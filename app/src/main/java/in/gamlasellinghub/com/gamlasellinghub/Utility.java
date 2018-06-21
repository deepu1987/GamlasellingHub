package in.gamlasellinghub.com.gamlasellinghub;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

/**
 * Created by Deepanshu on 21-03-2018.
 */

public class Utility {

    public static final String BaseURl = "http://10.0.2.2:8081/GamlaHub/rest/GamlaService/";

  //  public static final String BaseURl = "http://www.gamlahub.com/GamlaHub/rest/GamlaService/";
    public static final String CONFIRM_URL = BaseURl+"confirmUser";
    public static final String LOGIN_URL = BaseURl+"Login";
    public static final String PERSONAL_URL = BaseURl+"PersonalInformation";
    public static final String SAVE_BUISNESS_INFORMATION_URL= BaseURl+"SaveBuisnessInformation";
    public static final String SAVE_FINANCIAL_INFORMATION_URL = BaseURl+"SaveFinancialInformation";
    public static final String SAVE_WAREHOUSE_INFORMATION_URL = BaseURl+"SaveWarehouseInformation";
    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;
    public static final String SAVE_PRODUCT_INFORMATION_URL  = BaseURl+"SaveProductList";
    public static final String CATALOG_URL = BaseURl+"GetCatalog";
    public static final String UPDATE_QUANTATIY_URL = BaseURl+"UpdateQuantatity";
    public static final String UPDATE_STATUS_URL = BaseURl+"UpdateStatus";
    public static final String ORDER_LIST = BaseURl+"OrderList";
    public static final String DASHBOARDCOUNTER = BaseURl+"DashboardCounter";
    public static final String FORGOT_PASSWORD = BaseURl+"ForgotPassword";
    public static final String UPDATE_PRODUCT_DETAIL = BaseURl+"UpdateProductDetail";
    public static final String REGISTRATIONIDFORFCM = BaseURl+"RegistrationForFCM";
    public static final String REJECT_ORDER = BaseURl+"RejectOrderFromSeller";
    public static final int LIMIT_INTERVAL = 15;

    public static boolean checkPermission(final Context context)
    {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if(currentAPIVersion>=android.os.Build.VERSION_CODES.M)
        {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
                    alertBuilder.setCancelable(true);
                    alertBuilder.setTitle("Permission necessary");
                    alertBuilder.setMessage("External storage permission is necessary");
                    alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                        }
                    });
                    AlertDialog alert = alertBuilder.create();
                    alert.show();
                } else {
                    ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                }
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }
}
