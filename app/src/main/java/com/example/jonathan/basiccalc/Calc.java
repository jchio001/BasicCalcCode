package com.example.jonathan.basiccalc;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Vector;

public class Calc extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calc);
        TextView myTextView = (TextView) findViewById(R.id.textView2);
        myTextView.setInputType(0); //disables keyboard?
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_calc, menu);
        return true;
    }

    int numCnt = 0;
    int opCnt = 0; //only compute if numCnt - opCnt = 1;
    String prevString = "";
    //I'd much rather use an array, but I'm continuously expanding this word, so I need to use a
    //vector, despite how inefficient the code will be.
    public void readNumber(View v) {
        Button button = (Button) v;
        String bText = (String) button.getText();
        //int value = Integer.parseInt(bText);
        TextView myTextView = (TextView) findViewById(R.id.textView2);
        //Having text cutoff to the right is hard, so I just prepend the text to reverse it
        //Integer.toString(value)
        if (!MainActivity.isStringDec(bText)) {
            if (!prevString.equals("")) {
                ++numCnt;
                ++opCnt;
                myTextView.append(" " + bText + " ");
                prevString = "";
            }
        }
        //if the input is a number for this else statement
        else {
            myTextView.append(bText);
            prevString += bText;
        }
    }

    //atm, compute and clear are the name. I need to include a function to do computations later
    public void compute(View v) {
        TextView myTextView = (TextView) findViewById(R.id.textView2);
        TextView output = (TextView) findViewById(R.id.textView3); //use this to write to "output"
        if (!prevString.equals(""))
            ++numCnt;

        if (numCnt - opCnt == 1) {
            numCnt = opCnt = 0; //move this reset to later in the code
            output.setText(""); //placeholder
        }
        else
            output.setText("Error: Invalid input");

    }

    public void clear(View v) {
        numCnt = opCnt = 0;
        TextView myTextView = (TextView) findViewById(R.id.textView2);
        myTextView.setText(""); //tried to have all buttons use same function, it failed
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
