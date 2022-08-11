package com.example.assignment1;

import static android.net.http.SslCertificate.restoreState;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;

import com.example.assignment1.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private static final String NUMBER_KEY = "number";
    private ActivityMainBinding mMainLayout;
    private Button[] mButtons;
    private String mNumber = "";

    // Prints dialed numbers
    private void showNum() {
        mMainLayout.textView.setText(mNumber);
    }

    // Saves current information when other app is called or change in resolution
    @Override
    public void onSaveInstanceState(Bundle outState){
        outState.putString(NUMBER_KEY, mNumber);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        // Recovering the instance state
        if(savedInstanceState!=null) {
            mNumber = savedInstanceState.getString(NUMBER_KEY);
        }

        mMainLayout = ActivityMainBinding.inflate(getLayoutInflater());
        mButtons = new Button[]{
                mMainLayout.button11, mMainLayout.button12, mMainLayout.button13,
                mMainLayout.button21, mMainLayout.button22, mMainLayout.button23,
                mMainLayout.button31, mMainLayout.button32, mMainLayout.button33,
                mMainLayout.button41, mMainLayout.button42, mMainLayout.button43,
                mMainLayout.button51, mMainLayout.buttonDel
        };
        setContentView(mMainLayout.getRoot());

        // Run time permission for call intent
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CALL_PHONE}, 0);
            return;
        }

        // OnClickListener for button
        for (Button button:mButtons) {
            button.setOnClickListener(view -> buttonClick((Button)view));
        }
        showNum();
    }

    // Whenever button is pressed this function called
    private void buttonClick(Button b){
        String val = (String)b.getText();
        // If Delete button is pressed
        if (val.equals("Del")){
            // If mNumber is empty do nothing (to prevent program from crashing)
            if(mNumber.equals("")){
                return;
            }
            // Else delete last digit
            else {
                StringBuffer sb = new StringBuffer(mNumber);
                sb.deleteCharAt(sb.length() - 1);
                mNumber = sb.toString();
            }
        }
        // If Call button is pressed call the number
        else if (val.equals("CALL")){
            Intent sendIntent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+mNumber));
            startActivity(sendIntent);
        }
        // Else add number to mNumber
        else {
            mNumber += val;
        }
        showNum();
    }

    // OnRequest Permissions
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==0 && grantResults.length>0 &&
                grantResults[0]==PackageManager.PERMISSION_GRANTED);
    }
}