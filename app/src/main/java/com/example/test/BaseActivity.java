/**
 * Base Activity for printing
 *
 * @author Brother Industries, Ltd.
 * @version 2.2
 */

package com.example.test;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.usb.UsbManager;
import android.view.KeyEvent;

import com.example.test.common.Common;
import com.example.test.common.MsgDialog;
import com.example.test.common.MsgHandle;

public abstract class BaseActivity extends Activity {

    static final String ACTION_USB_PERMISSION = "com.android.example.USB_PERMISSION";
    private final BroadcastReceiver mUsbReceiver = new BroadcastReceiver() {
        @TargetApi(12)
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (ACTION_USB_PERMISSION.equals(action)) {
                synchronized (this) {
                    if (intent.getBooleanExtra(
                            UsbManager.EXTRA_PERMISSION_GRANTED, false))
                        Common.mUsbRequest = 1;
                    else
                        Common.mUsbRequest = 2;
                }
            }
        }
    };
    BasePrint myPrint = null;
    MsgHandle mHandle;
    MsgDialog mDialog;

    public abstract void selectFileButtonOnClick();

    public abstract void printButtonOnClick();


    /**
     * show message when BACK key is clicked
     */
    @Override
    public boolean onKeyDown(final int keyCode, final KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            showTips();
        }
        return false;
    }

    /**
     * show the BACK message
     */
    private void showTips() {

        final AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setTitle("End")
                .setMessage("Back To Launcher")
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(final DialogInterface dialog,
                                                final int which) {

                                finish();
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(final DialogInterface dialog,
                                                final int which) {
                            }
                        }).create();
        alertDialog.show();
    }

    /**
     * get the BluetoothAdapter
     */
    BluetoothAdapter getBluetoothAdapter() {
        final BluetoothAdapter bluetoothAdapter = BluetoothAdapter
                .getDefaultAdapter();
        if (bluetoothAdapter != null && !bluetoothAdapter.isEnabled()) {
            final Intent enableBtIntent = new Intent(
                    BluetoothAdapter.ACTION_REQUEST_ENABLE);
            enableBtIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(enableBtIntent);
        }
        return bluetoothAdapter;
    }

}