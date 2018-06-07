package com.example.rahulsharma.bookfinderlayout;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.CompoundButton;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.example.rahulsharma.bookfinderlayout.R;

public class MainActivity extends AppCompatActivity {

    private EditText textValue;
    ImageButton Camera, search, Barcode;
    private static final int RC_OCR_CAPTURE = 9003;
    private static final int RC_BAR_CAPTURE = 9005;
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        Barcode = (ImageButton) findViewById (R.id.barcode);
        textValue = (EditText) findViewById(R.id.text_value);

        Camera = (ImageButton) findViewById (R.id.camera);
        search = (ImageButton) findViewById (R.id.search);
        Camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // launch Ocr capture activity.
                // Intent intent = new Intent(getApplicationContext(), OcrCaptureActivity.class);
                // intent.putExtra(OcrCaptureActivity.AutoFocus, true);
                // intent.putExtra(OcrCaptureActivity.UseFlash, useFlash.isChecked());

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Flash Required?")
                        .setMessage("Yes or No")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int id) {
                                Intent intent = new Intent(getApplicationContext(), OcrCaptureActivity.class);
                                intent.putExtra(OcrCaptureActivity.AutoFocus, true);
                                intent.putExtra(OcrCaptureActivity.UseFlash, true);
                                startActivityForResult(intent, RC_OCR_CAPTURE);
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int id) {
                                Intent intent = new Intent(getApplicationContext(), OcrCaptureActivity.class);
                                intent.putExtra(OcrCaptureActivity.AutoFocus, true);
                                startActivityForResult(intent, RC_OCR_CAPTURE);
                            }
                        });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();
                return;
            }
        });

        Barcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // launch Ocr capture activity.
                Intent intent = new Intent(getApplicationContext(), ScannerActivity.class);
                startActivityForResult(intent, RC_BAR_CAPTURE);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (textValue.getText().toString().isEmpty()) {

            textValue.setVisibility(View.GONE);

        } else {

            textValue.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Called when an activity you launched exits, giving you the requestCode
     * you started it with, the resultCode it returned, and any additional
     * data from it.  The <var>resultCode</var> will be
     * {@link #RESULT_CANCELED} if the activity explicitly returned that,
     * didn't return any result, or crashed during its operation.
     * <p/>
     * <p>You will receive this call immediately before onResume() when your
     * activity is re-starting.
     * <p/>
     *
     * @param requestCode The integer request code originally supplied to
     *                    startActivityForResult(), allowing you to identify who this
     *                    result came from.
     * @param resultCode  The integer result code returned by the child activity
     *                    through its setResult().
     * @param data        An Intent, which can return result data to the caller
     *                    (various data can be attached to Intent "extras").
     * @see #startActivityForResult
     * @see #createPendingResult
     * @see #setResult(int)
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == RC_OCR_CAPTURE) {
            if (resultCode == CommonStatusCodes.SUCCESS) {
                if (data != null) {
                    String text = data.getStringExtra(OcrCaptureActivity.TextBlockObject);

                    textValue.setText(text);
                    Log.d(TAG, "Text read: " + text);
                } else {

                    Log.d(TAG, "No Text captured, intent data is null");
                }
            }
        }
        else if(requestCode == RC_BAR_CAPTURE)
        {
            if(resultCode == CommonStatusCodes.SUCCESS)
            {
                if(data != null)
                {
                    String text = data.getStringExtra(ScannerActivity.BarcodeObject);
                    textValue.setText(text);
                    Log.d(TAG, "Text read: " + text);
                } else {

                    Log.d(TAG, "No Text captured, intent data is null");
                }
            }
                }
            }
}