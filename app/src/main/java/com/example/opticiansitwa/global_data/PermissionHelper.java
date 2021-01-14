package com.example.opticiansitwa.global_data;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.pm.PackageManager;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.opticiansitwa.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PermissionHelper {
    public boolean value,All;
    public int AllP=0;


    private static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 100;
    List<String> listPermissionsNeeded = new ArrayList<>();

    public void checkAndRequestPermissions(Activity activity, String... permissions) {

        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(permission);
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(activity, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS);
        }
    }

    public void onRequestPermissionsResult(Activity activity, int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_ID_MULTIPLE_PERMISSIONS: {
                Map<String, Integer> perms = new HashMap<>();

                for (String permission : permissions) {
                    perms.put(permission, PackageManager.PERMISSION_GRANTED);
                }

                if (grantResults.length > 0) {
                    for (int i = 0; i < permissions.length; i++)
                        perms.put(permissions[i], grantResults[i]);

                    boolean allPermissionsGranted = true;
                    for (String permission1 : permissions) {
                        allPermissionsGranted = allPermissionsGranted && (perms.get(permission1) == PackageManager.PERMISSION_GRANTED);
                    }

                    if (allPermissionsGranted) {


                        Log.d(PermissionHelper.class.getSimpleName(), "onRequestPermissionsResult: all permissions granted");
                       // Log.w("Tag", "Value of AllP :",+AllP);
                        AllP=1;
                        Toast.makeText(activity, "Value of ALLP"+AllP, Toast.LENGTH_SHORT).show();
                        // AllP=1;
                    } else {
                        ActivityCompat.requestPermissions(activity, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS);

                    }
                }
            }
        }

    }
}

