package com.voidaint.intphonenumber.sample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.voidaint.intphonenumber.IntlPhoneInput;
import com.voidaint.intphonenumber.sample.R;



public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final IntlPhoneInput intlPhoneInput = findViewById(R.id.test_id);

        intlPhoneInput.setOnValidityChange(new IntlPhoneInput.IntlPhoneInputListener() {
            @Override
            public void done(View view, boolean isValid) {
                Log.e("TAG", "done: "+isValid );

            }
        });

        intlPhoneInput.setOnKeyboardDone(new IntlPhoneInput.IntlPhoneInputListener() {
            @Override
            public void done(View view, boolean isValid) {
                Log.e("TAG", "done: "+isValid+"  "+  intlPhoneInput.getNumber());
            }
        });
    }
}
