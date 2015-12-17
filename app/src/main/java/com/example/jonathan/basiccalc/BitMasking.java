package com.example.jonathan.basiccalc;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.preference.Preference;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.CheckBox;
import android.widget.Toast;

import java.util.Arrays;

public class BitMasking extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bit_masking);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_bit_masking, menu);
        return true;
    }

    public void doMasking(TextView input, char maskBit, char notMaskBit) {
        String inputStr = input.getText().toString();
        input.setText("");
        if (inputStr.matches("")) {
            Toast.makeText(getApplicationContext(), "Error: No input provided", Toast.LENGTH_LONG).show();
            return;
        }
        String[] splitInput = inputStr.split("\\s");
        int max = 4; //DEFAULT SIZE OF THE MASK. LARGEST NUMBER IN INPUT = MASK SIZE OTHERWISE.
        int currentNum;
        int sz = splitInput.length;
        for (int i = 0; i < sz; ++i) {
            //Checking each input element to see if it's an integer
            if (!(MainActivity.isStringDec(splitInput[i]))) {
                Toast.makeText(getApplicationContext(), "Error: Invalid input", Toast.LENGTH_LONG).show();
                return;
            }

            currentNum = Integer.parseInt(splitInput[i]);
            if (currentNum > 99) {
                Toast.makeText(getApplicationContext(), "Error: Mask too large", Toast.LENGTH_LONG).show();
                return;
            }

            if (currentNum >= max) //IF 4 IS THE BIGGEST NUMBER STUFF CRASHES
                max = currentNum + 1;

        }
        char[] chr = new char[max];
        Arrays.fill(chr, notMaskBit);
        for (int j = 0; j < sz; ++j) {
            chr[max - Integer.parseInt(splitInput[j]) - 1] = maskBit;
        }

        EditText out = (EditText) findViewById(R.id.mainInput);
        String outTxt = new String(chr);
        out.setText(outTxt);
        Toast.makeText(getApplicationContext(), "Done!", Toast.LENGTH_LONG).show();
    }

    public void onMakeMask(View v) {
        Button b = (Button) v;
        String text = (String) b.getText();
        TextView input;
        if (text.matches("OR Mask"))
            input = (TextView) findViewById(R.id.mask);
        else
            input = (TextView) findViewById(R.id.mask2); //OR MASK AND STATUS MASK ARE THE SAME

        doMasking(input, '1', '0');
    }

    public void onMakeMask1(View v) {
        TextView input = (TextView) findViewById(R.id.mask1);
        doMasking(input, '0', '1');
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
