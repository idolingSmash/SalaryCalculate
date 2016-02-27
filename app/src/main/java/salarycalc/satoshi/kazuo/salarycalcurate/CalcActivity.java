package salarycalc.satoshi.kazuo.salarycalcurate;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CalcActivity extends AppCompatActivity implements OnClickListener {

    private Button submit;
    private TextView money;
    private TextView partTime;
    private TextView overTime;

    private TextView day;
    private TextView week;
    private TextView month;

    private RelativeLayout submitLayout;

    private TimePickerDialog timePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.show();
        setContentView(R.layout.activity_calc);
        setLayout();
        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

    @Override
    public void onClick(View v) {

        if(v.equals(submit)){
            calculate();
        }else if(v.equals(partTime)){
            timePicker = new TimePickerDialog(this, new TimePickerListener(partTime) , 0, 0, true);
            timePicker.show();
        }else if(v.equals(overTime)){
            timePicker = new TimePickerDialog(this, new TimePickerListener(overTime) , 0, 0, true);
            timePicker.show();
        }

    }

    private void setLayout(){
        money = (TextView)findViewById(R.id.money);
        partTime = (TextView)findViewById(R.id.parttime);

        overTime = (TextView)findViewById(R.id.overtime);
        submit = (Button)findViewById(R.id.submitbutton);

        day = (TextView)findViewById(R.id.daysalary);
        week = (TextView)findViewById(R.id.weeksalary);
        month = (TextView)findViewById(R.id.monthsalary);

        submitLayout = (RelativeLayout)findViewById(R.id.submitLayout);

        money.setOnClickListener(this);
        partTime.setOnClickListener(this);
        overTime.setOnClickListener(this);
        submit.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuItem target = menu.add(0, 0, 0, "Edit");
        target.setIcon(android.R.drawable.ic_menu_set_as);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        Intent intent = new Intent(this, SettingsActivity.class);
        switch (item.getItemId()){
            case 0:
//                Toast.makeText(this,"click on Option Item", Toast.LENGTH_LONG).show();
                startActivity(intent);
                break;
            default:
                Log.e("click error","error!!");
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void calculate(){

        submitLayout.setVisibility(View.VISIBLE);

        int timeMoney = 0;
        int dayMoney = 0;
        int overMoney = 0;

        String[] srclist = null;
        boolean flag = true;

        Time pTime = null;
        Time oTime = null;

        if(!checkMoneyValue(money.getText().toString())){
            Toast.makeText(this, "金額の値が不正です。", Toast.LENGTH_LONG).show();
            flag = false;
        }

        if(flag) {
            if(!checkTimeValue(partTime.getText().toString().trim())){
                Toast.makeText(this, "時間の値が不正です。", Toast.LENGTH_LONG).show();
                flag = false;
            }
            if(!flag||!checkTimeValue(overTime.getText().toString().trim())){
                Toast.makeText(this, "時間の値が不正です。", Toast.LENGTH_LONG).show();
                flag = false;
            }
        }

        if(flag) {
            srclist = partTime.getText().toString().split(":");
            pTime = new Time(Integer.valueOf(srclist[0].trim()).intValue(),
                    Integer.valueOf(srclist[1].trim()).intValue());

            srclist = overTime.getText().toString().split(":");
            oTime = new Time(Integer.valueOf(srclist[0].trim()).intValue(),
                    Integer.valueOf(srclist[1].trim()).intValue());
        }

        if(flag){
           if( pTime.getHour() < 0 || 12 < pTime.getHour() || pTime.getMinute() < 0 || 60 < pTime.getMinute()
                   || oTime.getHour() < 0 || 12 < oTime.getHour() || oTime.getMinute() < 0 || 60 < oTime.getMinute()){
               Toast.makeText(this, "時間の値が不正です。", Toast.LENGTH_LONG).show();
               flag = false;
           }
        }

        if(flag) {
            timeMoney = Integer.parseInt(money.getText().toString());
            dayMoney = timeMoney * (pTime.getHour() + pTime.getMinute() / 60);
            overMoney = (int) (timeMoney * 1.25 * (oTime.getHour() + oTime.getMinute() / 60));

            day.setText(String.valueOf(dayMoney + overMoney));
            week.setText(String.valueOf((dayMoney + overMoney) * 5));
            month.setText(String.valueOf((dayMoney + overMoney) * 20));
        }
    }

    boolean checkMoneyValue(String s){
        boolean flag = false;
        Pattern p = Pattern.compile("^[0-9]*$");
        Matcher m = p.matcher(s);
        flag = m.find();
        Log.d("check money", String.valueOf(flag));
        return flag;
    }

    boolean checkTimeValue(String s){
        boolean flag = false;
        Pattern p = Pattern.compile("^[0-9]{1,2}:[0-9]{1,2}$");
        Matcher m = p.matcher(s);
        flag = m.find();
        Log.d("check time",  s + ":" + String.valueOf(flag));
        return flag;
    }
}

class Time{

    private int hour;
    private int minute;

    public Time(int hour, int minute){
        this.hour = hour;
        this.minute = minute;
    }

    public int getHour(){
        return hour;
    }

    public int getMinute(){
        return minute;
    }
}


class SubmitButtonClickListener implements View.OnClickListener{

    private Context context;

    public SubmitButtonClickListener(Context context){
        this.context = context;
    }

    @Override
    public void onClick(View v) {
        Toast.makeText(context, "Test", Toast.LENGTH_LONG).show();
    }
}

class TimePickerListener implements TimePickerDialog.OnTimeSetListener{

    private TextView text;

    public TimePickerListener(TextView text){
        this.text = text;
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        String timerString = String.format("%2d:%02d", hourOfDay, minute);
        text.setText(timerString);
    }
}
