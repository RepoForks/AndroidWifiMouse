package com.nuhiara.nezspencer.androidwifimouse.view;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.nuhiara.nezspencer.androidwifimouse.R;
import com.nuhiara.nezspencer.androidwifimouse.presenter.MainActivityPresenter;
import com.nuhiara.nezspencer.androidwifimouse.utility.Utils;
import com.nuhiara.nezspencer.androidwifimouse.view.interfaces.MainActivityInterface;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements MainActivityInterface{

    @Bind(R.id.edit_server_ip) EditText editServerIp;
    @Bind(R.id.edit_port_number) EditText editPortNumber;
    @Bind(R.id.button_connect) Button connectSocketButton;

    MainActivityPresenter presenter;
    private ProgressDialog progressDialog;
    private static final String SERVER_IP="192.168.43.201";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Connecting...");
        progressDialog.setCancelable(false);

        Log.e("IPAddress is: ",Utils.getIPAddress(true));
        editServerIp.setText(SERVER_IP);

        Log.e("IPAddress2 is ",getIPAddressOverWifi());



        presenter=new MainActivityPresenter(this);
    }

    @Override
    public String getIPaddress() {
        return editServerIp.getText().toString();
    }

    @Override
    public void showSuccessMessage(final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MainActivity.this,message,Toast.LENGTH_LONG).show();
            }
        });


    }

    @Override
    public int getPortNumber() {
        if (TextUtils.isEmpty(editPortNumber.getText().toString()))
            return 0;

        return Integer.parseInt(editPortNumber.getText().toString());
    }

    @Override
    public void showPortNumberError(int resID) {
        editPortNumber.setError(getString(resID));
    }

    @Override
    public void showIPaddressError(int resID) {
        editServerIp.setError(getString(resID));
    }

    @Override
    public void showLoadingProgress() {
        progressDialog.show();
    }

    @Override
    public void stopLoadingProgress() {
        progressDialog.dismiss();
    }

    @Override
    public void startMouseActivity() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(MainActivity.this,Mouse.class));
            }
        });

    }

    @OnClick(R.id.button_connect)
    public void connectSocket(){
        presenter.onConnectButtonClicked();
    }

    public  String getIPAddressOverWifi(){
        WifiManager manager=(WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);

        return Formatter.formatIpAddress(manager.getConnectionInfo().getIpAddress());
    }
}
