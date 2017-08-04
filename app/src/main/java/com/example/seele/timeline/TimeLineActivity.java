package com.example.seele.timeline;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Arrays;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class TimeLineActivity extends AppCompatActivity implements TimeLine.OnPointClickListener {

    @Bind(R.id.time_line)
    TimeLine timeLine;

    List<String> datas = Arrays.asList("Item1", "Item2", "Item3", "Item4",
            "Item5", "Item6", "Item7", "Item8", "Item9", "Item10", "Item11", "Item12");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_line);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        timeLine.setOnPointClickListener(this);
        timeLine.setListNodeText(Arrays.asList("1day", "2day", "3day", "4day", "5day", "6day", "7day"));

    }

    @Override
    public void onPointerClick(int index, int x, int y) {
        timeLine.perturbNode(index, false);
    }
}
