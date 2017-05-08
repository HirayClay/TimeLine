package com.example.seele.timeline;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import butterknife.Bind;
import butterknife.ButterKnife;

public class TimeLineActivity extends AppCompatActivity implements TimeLine.OnPointClickListener {

    @Bind(R.id.time_line)
    TimeLine timeLine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_line);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        timeLine.setOnPointClickListener(this);
        timeLine.setNodeText(new String[]{"1day", "2day", "3day", "4day", "5day", "6day", "7day"});
    }

    @Override
    public void onPointerClick(int index, int x, int y) {
        timeLine.perturbNode(index,false);
    }
}
