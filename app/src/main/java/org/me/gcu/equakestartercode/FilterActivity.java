package org.me.gcu.equakestartercode;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class FilterActivity extends AppCompatActivity {

    TextView SpecifiedDate, PeriodDate1, PeriodDate2;
    Button search_btn;
    RadioButton SpecifiedD_rbtn, Period_rbtn;
    RecyclerView recyclerView;
    FilterAdapter filterAdapter;
    List<card> cardList = new ArrayList<>();
    String TAG = "theH";

    int highestLatitudeId, lowestLatitudeId, highestLongitudeId, lowestLongitudeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        cardList.addAll(MainActivity.cardList);



        SpecifiedD_rbtn = findViewById(R.id.specified_d_rbtn);
        Period_rbtn = findViewById(R.id.period_rbtn);
        SpecifiedDate = findViewById(R.id.specified_date);
        PeriodDate1 = findViewById(R.id.period_date1);
        PeriodDate2 = findViewById(R.id.period_date2);
        search_btn = findViewById(R.id.btn_apply);
        recyclerView = findViewById(R.id.recycler_view);

        if (cardList.size() > 0) {
            arrangeList();
            filterAdapter = new FilterAdapter(FilterActivity.this, cardList, getHighestIndex(), getLowestIndex(), highestLatitudeId, lowestLatitudeId, highestLongitudeId, lowestLongitudeId);
            recyclerView.setLayoutManager(new LinearLayoutManager(FilterActivity.this, LinearLayoutManager.VERTICAL, false));
            recyclerView.setAdapter(filterAdapter);
        }

        SpecifiedD_rbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Period_rbtn.setChecked(false);
            }
        });
        Period_rbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SpecifiedD_rbtn.setChecked(false);
            }
        });

        SpecifiedDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePickerDialog(0);
            }
        });
        PeriodDate1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePickerDialog(1);
            }
        });
        PeriodDate2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePickerDialog(2);
            }
        });

        search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SpecifiedD_rbtn.isChecked()) {
                    if (TextUtils.isEmpty(SpecifiedDate.getText().toString())) {
                        Toast.makeText(FilterActivity.this, "Choose date", Toast.LENGTH_SHORT).show();
                    } else {
//                        get specific date data
                        getSingleDateData();
                    }
                } else if (Period_rbtn.isChecked()) {
                    if (TextUtils.isEmpty(PeriodDate1.getText().toString())) {
                        Toast.makeText(FilterActivity.this, "Choose start date", Toast.LENGTH_SHORT).show();
                    } else if (TextUtils.isEmpty(PeriodDate2.getText().toString())) {
                        Toast.makeText(FilterActivity.this, "Choose end date", Toast.LENGTH_SHORT).show();
                    } else {
//                        get range data
                        getRangeDateData();
                    }
                }
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void datePickerDialog(int index) {
        DatePickerDialog mDatePicker;
        final Calendar mCalendar = Calendar.getInstance();

        mDatePicker = new DatePickerDialog(FilterActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                mCalendar.set(Calendar.YEAR, year);
                mCalendar.set(Calendar.MONTH, monthOfYear);
                mCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String formattedDate = new SimpleDateFormat("EEE, d MMM yyyy", Locale.getDefault()).format(mCalendar.getTime());
                if (index == 0) {
                    SpecifiedDate.setText(formattedDate);
                } else if (index == 1) {
                    PeriodDate1.setText(formattedDate);
                } else if (index == 2) {
                    PeriodDate2.setText(formattedDate);
                }
            }
        }, mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH), mCalendar.get(Calendar.DAY_OF_MONTH));
        mDatePicker.show();
    }

    public boolean isDateEquals(String eqDateStr, String newDateStr) {
        boolean result = false;
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, d MMM yyyy");
        try {
            Date eqDate = sdf.parse(eqDateStr);
            Date newDate = sdf.parse(newDateStr);
            result = newDate.equals(eqDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return result;
    }

    public boolean isDateEqualToRange(String eqDateStr, String startDateStr, String endDateStr) {
        boolean result = false;
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, d MMM yyyy");
        try {
            Date eqDate = sdf.parse(eqDateStr);
            Date startDate = sdf.parse(startDateStr);
            Date endDate = sdf.parse(endDateStr);
            result = (eqDate.equals(startDate) || eqDate.after(startDate)) && (eqDate.equals(endDate) || eqDate.before(endDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return result;
    }

    public void getSingleDateData() {
        String eqDateStr = SpecifiedDate.getText().toString();
        cardList.clear();
        cardList.addAll(MainActivity.cardList);
        List<card> newCardList = new ArrayList<>();
        for (card card : cardList) {
            if (isDateEquals(card.getPubDate(), eqDateStr)) {
                newCardList.add(card);
            }
        }
        if (newCardList.size() > 0) {
            cardList.clear();
            cardList.addAll(newCardList);
            arrangeList();
            filterAdapter.reloadData(getHighestIndex(), getLowestIndex(), highestLatitudeId, lowestLatitudeId, highestLongitudeId, lowestLongitudeId);
        } else {
            AlertDialog alertDialog = new AlertDialog.Builder(FilterActivity.this).create();
            alertDialog.setTitle("Alert");
            alertDialog.setMessage("No specific information on " + eqDateStr);
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();
            cardList.clear();
        }

        filterAdapter.notifyDataSetChanged();

    }

    public void getRangeDateData() {
        String startDateStr = PeriodDate1.getText().toString();
        String endDateStr = PeriodDate2.getText().toString();
        cardList.clear();
        cardList.addAll(MainActivity.cardList);
        List<card> newCardList = new ArrayList<>();

        for (card card : cardList) {
            if (isDateEqualToRange(card.getPubDate(), startDateStr, endDateStr)) {
                newCardList.add(card);
            }
        }
        if (newCardList.size() > 0) {
            cardList.clear();
            cardList.addAll(newCardList);
            arrangeList();
            filterAdapter.reloadData(getHighestIndex(), getLowestIndex(), highestLatitudeId, lowestLatitudeId, highestLongitudeId, lowestLongitudeId);

        } else {
            AlertDialog alertDialog = new AlertDialog.Builder(FilterActivity.this).create();
            alertDialog.setTitle("Alert");
            alertDialog.setMessage("No specific information between " + startDateStr + " & " + endDateStr);
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();
            cardList.clear();
        }

        filterAdapter.notifyDataSetChanged();

    }

    public void arrangeList() {
        Collections.sort(cardList, new Comparator<card>() {
            public int compare(card obj1, card obj2) {
                // ## Descending order
                return Double.compare(obj2.getMagnitude(), obj1.getMagnitude());
            }
        });

        getLatitudeId();

        getLongitudeId();
    }

    public int getHighestIndex() {
        double highest = cardList.get(0).getDepth();
        int highestIndex = 0;

        for (int s = 1; s < cardList.size(); s++) {
            double curValue = cardList.get(s).getDepth();
            if (curValue > highest) {
                highest = curValue;
                highestIndex = s;
            }
        }
        return highestIndex;
    }

    public int getLowestIndex() {
        double lowest = cardList.get(0).getDepth();
        int lowestIndex = 0;

        for (int s = 1; s < cardList.size(); s++) {
            double curValue = cardList.get(s).getDepth();
            if (curValue < lowest) {
                lowest = curValue;
                lowestIndex = s;
            }
        }
        return lowestIndex;
    }

    public void getLatitudeId() {
        double highest = cardList.get(0).getLatitude();
        double lowest = cardList.get(0).getLatitude();
        highestLatitudeId = cardList.get(0).getId();
        lowestLatitudeId = cardList.get(0).getId();

        for (int s = 1; s < cardList.size(); s++) {
            double curValue = cardList.get(s).getLatitude();
            if (curValue > highest) {
                highest = curValue;
                highestLatitudeId = cardList.get(s).getId();
            }
            if (curValue < lowest) {
                lowest = curValue;
                lowestLatitudeId = cardList.get(s).getId();
            }
        }
    }

    public void getLongitudeId() {

        double highest = cardList.get(0).getLongitude();
        double lowest = cardList.get(0).getLongitude();
        highestLongitudeId = cardList.get(0).getId();
        lowestLongitudeId = cardList.get(0).getId();

        for (int s = 1; s < cardList.size(); s++) {
            double curValue = cardList.get(s).getLongitude();
            if (curValue > highest) {
                highest = curValue;
                highestLongitudeId = cardList.get(s).getId();
            }
            if (curValue < lowest) {
                lowest = curValue;
                lowestLongitudeId = cardList.get(s).getId();
            }
        }
    }
}