package com.example.jonathan.basiccalc;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

//TO DO LATER: BINTODEC and DECTOBIN!
public class MainActivity extends ActionBarActivity {

    private static final String SETTING_CHECK_BOX = "CHECK";
    private static final String SETTING_CHECK1_BOX = "CHECK1";
    boolean padBin = false;
    boolean dispInput = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        padBin = sp.getBoolean(SETTING_CHECK_BOX, false);
        //THIS WILL ONLY UPDATE WHEN THE APP IS FORCEFULLY CLOSED. WHEN SETTINGS IS OPEN,
        //MAIN ACTIVITY IS STILL RUNNING! THIS MEANS ONCREATE IS NOT CALLED AGAIN! OVERRIDE ON RESUME?
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    //I WAS RIGHT ABOUT ONRESUME.
    public void onResume() {
        super.onResume();
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        padBin = sp.getBoolean(SETTING_CHECK_BOX, false); //NEED TO GET SPECIFIC KEY UNDER SHARED PREF "SETTINGS"
        dispInput = sp.getBoolean(SETTING_CHECK1_BOX, false);
    }

    public boolean isBinary(String binStr) {
        for (int i = 0; i < binStr.length(); ++i) {
            if (binStr.charAt(i) != '0' && binStr.charAt(i) != '1')
                return false;
        }
        return true;
    }

    public boolean isDecTooLong(String decStr) {
        return (decStr.length() >= 10);
    }

    public boolean isHexTooLong(String hexStr) {
        return (hexStr.length() >= 7);
    }

    //PADS BINARY NUMBERS WITH 0'S. CHECK IF bin IS BINARY IN OTHER FUNCTIONS
    public String padBin(String bin) {
        StringBuilder sb = new StringBuilder();
        int padSz;
        if (bin.length() % 4 != 0) {
            padSz = 4 - (bin.length() % 4);
            for (int i = 0; i < padSz; ++i)
                sb.append('0');
        }
        sb.append(bin);
        return sb.toString();
    }

    public String binToHex(String bin) {
        String paddedBin = padBin(bin);
        String fourBits;
        StringBuilder hexNum = new StringBuilder();
        for (int i = 0; i < paddedBin.length(); i = i + 4) {
            //end index is exclusive, append attaches to front
            fourBits = paddedBin.substring(i, i + 4);
            switch (fourBits) {
                case "0000":
                    hexNum.append('0');
                    break;
                case "0001":
                    hexNum.append('1');
                    break;
                case "0010":
                    hexNum.append('2');
                    break;
                case "0011":
                    hexNum.append('3');
                    break;
                case "0100":
                    hexNum.append('4');
                    break;
                case "0101":
                    hexNum.append('5');
                    break;
                case "0110":
                    hexNum.append('6');
                    break;
                case "0111":
                    hexNum.append('7');
                    break;
                case "1000":
                    hexNum.append('8');
                    break;
                case "1001":
                    hexNum.append('9');
                    break;
                case "1010":
                    hexNum.append('A');
                    break;
                case "1011":
                    hexNum.append('B');
                    break;
                case "1100":
                    hexNum.append('C');
                    break;
                case "1101":
                    hexNum.append('D');
                    break;
                case "1110":
                    hexNum.append('E');
                    break;
                case "1111":
                    hexNum.append('F');
                    break;
                default:
                    StringBuilder error = new StringBuilder();
                    error.append("Error @ public String binToHex(String bin) function.");
                    return error.toString();
            }
        }
        return hexNum.toString();
    }

    //THIS FUNCTION DOES ALL THE OUTPUT WORK. CREATED TO MAKE CODE MORE READABLE.
    public void outFunc(TextView output, String inputStr, String inType, String outType, boolean dispInput) {
        StringBuilder sb = new StringBuilder();
        if (dispInput)
            sb.append(inType + ": " + inputStr + " => ");

        if (inType == "Dec") {
            if (isDecTooLong(inputStr)) {
                output.setText("Error: Dec Input too large");
                return;
            }
        }
        else if (inType == "Hex" && outType == "Dec") {
            if (isHexTooLong(inputStr)) {
                output.setText("Error: Hex Input Too Large");
                return;
            }
        }

        if((inType == "Bin") && (outType == "Hex"))
            sb.append(outType + ": " + binToHex(inputStr));
        else if ((inType == "Hex") && (outType == "Bin"))
            sb.append(outType + ": " + hexToBin(inputStr));
        else if ((inType == "Dec") && (outType == "Hex"))
            sb.append(outType + ": " + decToHex(inputStr));
        else if ((inType == "Hex") && (outType == "Dec"))
            sb.append(outType + ": " + binToDec(hexToBin(inputStr)));
        else if ((inType == "Dec") && (outType == "Bin"))
            sb.append(outType + ": " + decToBin(inputStr));
        else if ((inType == "Bin") && (outType == "Dec"))
            sb.append(outType + ": " + binToDec(inputStr));
        else {
            output.setText("ERROR: outFunc is broken.");
            return;
        }

        output.setMovementMethod(new ScrollingMovementMethod());
        output.setText(sb.toString());
        output.scrollTo(0,0);
    }

    public void onClick(View v) {
        TextView input = (TextView) findViewById(R.id.myEditText);
        TextView output = (TextView) findViewById(R.id.textView);
        String inputStr = input.getText().toString();

        if (inputStr.matches("")) {
            output.setText("Error: No input entered.");
            output.scrollTo(0,0);
            return;
        }

        if (!isBinary(inputStr)) {
            output.setText("Error: Invalid bin input.");
            output.scrollTo(0,0);
        }
        else
            outFunc(output, inputStr, "Bin", "Hex", dispInput);

        input.setText("");
    }

    //using char arithmetic
    public boolean isCharHex(char c) {
        return ((c >= '0' && c <= '9') || (c >= 'A' && c <= 'F'));
    }

    public boolean isStringHex(String str) {
        for (int i = 0; i < str.length(); ++i) {
            if (!isCharHex(str.charAt(i)))
                return false;
        }
        return true;
    }

    public String hexToBin(String hex) {
        StringBuilder bin = new StringBuilder();
        for (int i = 0; i < hex.length(); ++i) {
            switch (hex.charAt(i)) {
                case '0':
                    bin.append("0000");
                    break;
                case '1':
                    bin.append("0001");
                    break;
                case '2':
                    bin.append("0010");
                    break;
                case '3':
                    bin.append("0011");
                    break;
                case '4':
                    bin.append("0100");
                    break;
                case '5':
                    bin.append("0101");
                    break;
                case '6':
                    bin.append("0110");
                    break;
                case '7':
                    bin.append("0111");
                    break;
                case '8':
                    bin.append("1000");
                    break;
                case '9':
                    bin.append("1001");
                    break;
                case 'A':
                    bin.append("1010");
                    break;
                case 'B':
                    bin.append("1011");
                    break;
                case 'C':
                    bin.append("1100");
                    break;
                case 'D':
                    bin.append("1101");
                    break;
                case 'E':
                    bin.append("1110");
                    break;
                case 'F':
                    bin.append("1111");
                    break;
                default:
                    StringBuilder error = new StringBuilder();
                    error.append("Error @public String hexToBin (String hex) function.");
                    return error.toString();
            }
        }
        return bin.toString();
    }

    public void onClick1(View v) {
        TextView input = (TextView) findViewById(R.id.myEditText);
        TextView output = (TextView) findViewById(R.id.textView);
        String inputStr = input.getText().toString();

        if (inputStr.matches("")) {
            output.setText("Error: No input entered.");
            output.scrollTo(0,0);
            return;
        }

        if (!isStringHex(inputStr)) {
            output.setText("Error: Invalid hex input.");
            output.scrollTo(0,0);
        }
        else
            outFunc(output, inputStr, "Hex", "Bin", dispInput);

        //output.setText("Bin: " + hexToBin(inputStr));

        input.setText("");
    }

    public static boolean isCharDec(char c) {
        return (c >= '0' && c <= '9');
    }

    public static boolean isStringDec(String str) {
        for (int i = 0; i < str.length(); ++i) {
            if (!isCharDec(str.charAt(i)))
                return false;
        }
        return true;

    }

    public char decToHexDigit(int dec) {
        switch (dec) {
            case 0:
                return '0';
            case 1:
                return '1';
            case 2:
                return '2';
            case 3:
                return '3';
            case 4:
                return '4';
            case 5:
                return '5';
            case 6:
                return '6';
            case 7:
                return '7';
            case 8:
                return '8';
            case 9:
                return '9';
            case 10:
                return 'A';
            case 11:
                return 'B';
            case 12:
                return 'C';
            case 13:
                return 'D';
            case 14:
                return 'E';
            case 15:
                return 'F';
            default:
                return 'Z';
        }
    }

    public String decToHex(String dec) {
        int i = 0;
        int decINT = Integer.parseInt(dec);
        while (Math.pow(16, i + 1) <= decINT)
            ++i;
        //above code works

        int hexDigit = 0;
        char c;
        StringBuilder hexNum = new StringBuilder();
        for (; i >= 0; --i) {
            hexDigit = decINT / ((int) Math.pow(16, i));
            decINT = decINT - (hexDigit * ((int) Math.pow(16, i)));
            //The reason I need to do this subtraction is so that I can "shrink" decINT to the
            //next lowest power of 16. *shrink as in get the next digit
            c = decToHexDigit(hexDigit); //need to hold the digit to check if I have Z/Error char
            if (c != 'Z')
                hexNum.append(c);
            else
                return "Error @public String decToHex(String dec) function.";
        }

        return hexNum.toString();
    }

    public void onClick2(View v) {
        TextView input = (TextView) findViewById(R.id.myEditText);
        TextView output = (TextView) findViewById(R.id.textView);
        String inputStr = input.getText().toString();

        if (inputStr.matches("")) {
            output.setText("Error: No input entered.");
            output.scrollTo(0,0);
            return;
        }

        if (!isStringDec(inputStr)) {
            output.setText("Error: Invalid dec input.");
            output.scrollTo(0,0);
        }
        else
            outFunc(output, inputStr, "Dec", "Hex", dispInput);

        input.setText("");
    }

    //I WILL ASSUME THAT ALL PROGRAMS THAT CONTAIN THIS ONE WILL INPUT A PROPER BINARY STRING
    public String binToDec(String bin) {
        int decTotal = 0; //DECIMAL TOTAL
        for (int i = 0; i < bin.length(); ++i) {
            if (bin.charAt(i) == '1')
                //INDEXING FOR JAVA IS BACKWARDS COMPARED TO C++/C <== MESSED ME UP REALLY HARD
                decTotal = decTotal + (int) Math.pow(2, (bin.length() - i - 1));
        }
        return Integer.toString(decTotal);
    }

    //Hex => Bin => Dec
    public void onClick3(View v) {
        TextView input = (TextView) findViewById(R.id.myEditText);
        TextView output = (TextView) findViewById(R.id.textView);
        String inputStr = input.getText().toString();

        if (inputStr.matches("")) {
            output.setText("Error: No input entered.");
            output.scrollTo(0,0);
            return;
        }

        if (!isStringHex(inputStr)) {
            output.setText("Error: Invalid hex input.");
            output.scrollTo(0, 0);
        }
        else {
            outFunc(output, inputStr, "Hex", "Dec", dispInput);
        }

        input.setText("");
    }

    public String decToBin(String dec) {
        int decInt = Integer.parseInt(dec);
        StringBuilder binStr = new StringBuilder();
        int rem;
        if (decInt == 0)
            if (padBin)
                return "0000";
            else
                return "0";

        while (decInt > 0) {
            rem = decInt % 2;
            binStr.append(rem);
            decInt = decInt / 2;
        }
        binStr.reverse(); //NOTE TO SELF: APPEND ATTACHES TO THE RIGHT

        if (padBin)
            return padBin(binStr.toString());
        else
            return binStr.toString();
    }

    public void onClick4(View v) {
        TextView input = (TextView) findViewById(R.id.myEditText);
        TextView output = (TextView) findViewById(R.id.textView);
        String inputStr = input.getText().toString();

        if (inputStr.matches("")) {
            output.setText("Error: No input entered.");
            output.scrollTo(0,0);
            return;
        }

        if (!isStringDec(inputStr)) {
            output.setText("Error: Invalid dec input.");
            output.scrollTo(0,0);
        }
        else
            outFunc(output, inputStr, "Dec", "Bin", dispInput);

        input.setText("");

    }

    public void onClick5(View v) {
        TextView input = (TextView) findViewById(R.id.myEditText);
        TextView output = (TextView) findViewById(R.id.textView);
        String inputStr = input.getText().toString();

        if (inputStr.matches("")) {
            output.setText("Error: No input entered.");
            output.scrollTo(0,0);
            return;
        }

        if (!isBinary(inputStr)) {
            output.setText("Error: Invalid bin input.");
            output.scrollTo(0,0);
        }
        else
            outFunc(output, inputStr, "Bin", "Dec", dispInput);

        input.setText("");
    }

    public void toSettings (View v) {
       startActivity(new Intent(MainActivity.this, Settings.class));
    }

    public void toBitMask (View v) {
        startActivity(new Intent(MainActivity.this, BitMasking.class));
    }

    public void toCalc (View v) {
        startActivity(new Intent(MainActivity.this, Calc.class));
    }

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

