package com.example.rahulsharma.bookfinderlayout;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;

import com.example.rahulsharma.bookfinderlayout.CameraSource;
import com.example.rahulsharma.bookfinderlayout.CameraSourcePreview;
import com.example.rahulsharma.bookfinderlayout.GraphicOverlay;
import com.example.rahulsharma.bookfinderlayout.OcrDetectorProcessor;
import com.example.rahulsharma.bookfinderlayout.OcrGraphic;


import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.text.TextBlock;

import java.io.IOException;
import java.util.List;

import info.androidhive.barcode.BarcodeReader;


public class ScannerActivity extends AppCompatActivity implements BarcodeReader.BarcodeReaderListener {

    private static final String LOG_TAG = ScannerActivity.class.getSimpleName();
    private BarcodeReader mBarcodeReader;
    public static final String BarcodeObject = "String";
    private boolean isPaused = false;
    private static final int RC_BAR_CAPTURE = 9005;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);



        mBarcodeReader = (BarcodeReader) getSupportFragmentManager().findFragmentById(
                R.id.barcode_fragment);
    }

    /*@Override
    public void onScanned(Barcode barcode) {
        // Playing barcode reader beep sound
        mBarcodeReader.playBeep();
        // Add to the BlockChain
        // TODO: Implement BlockChain
        String previousHash = "0";
        if (!mBlockChain.isEmpty()) {
            previousHash = mBlockChain.get(mBlockChain.size() - 1).getPreviousHash();
        }
        Log.v(LOG_TAG, "Adding New Block......");
        mBlockChain.add(new Block(previousHash, barcode.rawValue));
        Log.v(LOG_TAG, "Mining Block " + mBlockChain.size() + "...");
        mBlockChain.get(mBlockChain.size() - 1).mineBlock(DIFFICULTY);
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        int notifyId = 1;
        Notification notification = new Notification.Builder(this)
                .setContentTitle("IS CHAIN VALID?")
                .setContentText(String.valueOf(isChainValid()))
                .setSmallIcon(R.drawable.ic_launcher_background)
                .build();
        notificationManager.notify(notifyId, notification);
    }
    */

    /* Helper method to check the validity of the chain
    public static boolean isChainValid() {
        Block mCurrentBlock;
        Block mPreviousBlock;
        String hashTarget = new String(new char[DIFFICULTY]).replace('\0', '0');
        // Loop through the blockchain:
        for (int i = 1; i < mBlockChain.size(); i++) {
            mCurrentBlock = mBlockChain.get(i);
            mPreviousBlock = mBlockChain.get(i - 1);
            // Compare registered hash and calculated hash:
            if (!mCurrentBlock.getBlockHash().equals(mCurrentBlock.calculateHash())) {
                Log.e(LOG_TAG, "Current Hash Invalid");
                return false;
            }
            // Compare previous hash and previous registered hash:
            if (!mPreviousBlock.getBlockHash().equals(mCurrentBlock.getPreviousHash())) {
                Log.e(LOG_TAG, "Previous Hash Invalid");
                return false;
            }
            // Check if the hash was solved with the given difficulty:
            if (!mCurrentBlock.getBlockHash().substring(0, DIFFICULTY).equals(hashTarget)) {
                Log.e(LOG_TAG, "Block Not Yet Mined");
                return false;
            }
        }
        return true;
    } */

    @Override
    protected void onResume() {
        super.onResume();
        mBarcodeReader.resumeScanning();
    }

    @Override
    public void onScanned(final Barcode barcode) {
        mBarcodeReader.playBeep();
        Log.v(LOG_TAG, "Barcode Data Read: " + barcode.displayValue);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (barcode.rawValue != null) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(ScannerActivity.this)
                            .setTitle(R.string.error)
                            .setMessage(barcode.rawValue)
                            .setPositiveButton(R.string.rescan, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int id) {
                                    mBarcodeReader.resumeScanning();
                                }
                            })
                            .setNegativeButton(R.string.dismiss, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int id) {
                                    if (dialogInterface != null) {
                                        dialogInterface.dismiss();
                                    }

                                    ScannerActivity.this.finish();
                                }
                            });

                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                    return;
                }


            }
        });

        mBarcodeReader.pauseScanning();
    }

    @Override
    public void onScannedMultiple(List<Barcode> barcodes) {

    }

    @Override
    public void onBitmapScanned(SparseArray<Barcode> sparseArray) {

    }

    @Override
    public void onScanError(String errorMessage) {
        Log.e(LOG_TAG, errorMessage);
    }

    @Override
    public void onCameraPermissionDenied() {

    }
}
