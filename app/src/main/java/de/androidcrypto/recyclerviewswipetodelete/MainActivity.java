package de.androidcrypto.recyclerviewswipetodelete;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    // source: https://github.com/journaldev/journaldev/tree/master/Android/AndroidRecyclerViewSwipeToDelete
    // https://www.journaldev.com/23164/android-recyclerview-swipe-to-delete-undo
    // funktioniert
    RecyclerView recyclerView;
    RecyclerViewAdapter mAdapter;
    //ArrayList<String> stringArrayList = new ArrayList<>();
    CoordinatorLayout coordinatorLayout;
    ArrayList<StockModel> stockModelArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        coordinatorLayout = findViewById(R.id.coordinatorLayout);

        populateRecyclerView();
        enableSwipeToDeleteAndUndo();
    }

    private void populateRecyclerView() {
/*
        stringArrayList.add("Item 1");
        stringArrayList.add("Item 2");
        stringArrayList.add("Item 3");
        stringArrayList.add("Item 4");
        stringArrayList.add("Item 5");
        stringArrayList.add("Item 6");
        stringArrayList.add("Item 7");
        stringArrayList.add("Item 8");
        stringArrayList.add("Item 9");
        stringArrayList.add("Item 10");*/

        StockModel stockModel = new StockModel("IE123", "ETF Europe", true, "");
        stockModelArrayList.add(stockModel);
        stockModel = new StockModel("IE345", "ETF World", true, "");
        stockModelArrayList.add(stockModel);
        stockModel = new StockModel("LU111222333", "ETF Emerging Markets", true, "");
        stockModelArrayList.add(stockModel);

        //mAdapter = new RecyclerViewAdapter(stringArrayList);
        //recyclerView.setAdapter(mAdapter);

        mAdapter = new RecyclerViewAdapter(stockModelArrayList);
        recyclerView.setAdapter(mAdapter);
    }

    private void enableSwipeToDeleteAndUndo() {
        SwipeToDeleteCallback swipeToDeleteCallback = new SwipeToDeleteCallback(this) {
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {


                //final int position = viewHolder.getAdapterPosition(); // getAdapterPosition is deprecated
                final int position = viewHolder.getBindingAdapterPosition();
                final StockModel item = mAdapter.getData().get(position);
                //final String item = mAdapter.getData().get(position);

                mAdapter.removeItem(position);

                Snackbar snackbar = Snackbar
                        .make(coordinatorLayout, "Item was removed from the list.", Snackbar.LENGTH_LONG);
                snackbar.setAction("UNDO", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mAdapter.restoreItem(item, position);
                        recyclerView.scrollToPosition(position);
                        System.out.println("actual contents of the arraylist");
                        System.out.println("stockModelArrayList size: " + stockModelArrayList.size());
                        printStockModelArrayList(stockModelArrayList);
                    }
                });

                snackbar.setActionTextColor(Color.YELLOW);
                snackbar.show();

                System.out.println("actual contents of the arraylist");
                System.out.println("stockModelArrayList size: " + stockModelArrayList.size());
                printStockModelArrayList(stockModelArrayList);
            }
        };

        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeToDeleteCallback);
        itemTouchhelper.attachToRecyclerView(recyclerView);
    }

    private void printStockModelArrayList(ArrayList<StockModel> list) {
        int size = list.size();
        for (int i = 0; i < size; i++) {
            System.out.println("nr " + i + " isin: " + list.get(i).getIsin() +
                    " isinName: " + list.get(i).getIsinName());
        }
    }
}