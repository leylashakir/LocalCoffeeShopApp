package leylashakirzyanova.cs360.com;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MenuActivityUpdate extends AppCompatActivity {

    private String itemName[];
    private int itemPrice[];
    private int counter[];
    private int itemTotal[];
    private int cartTotal;

    private RecyclerView recyclerViewLayout;
    private MenuAdapter adapter;

    private ArrayList<ItemData> itemData;

    Button submitOrder;

    private static final String ITEM_KEY = "save_key";
    private SharedPreferences SharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        itemName = getResources().getStringArray(R.array.item_name_array);
        itemPrice = getResources().getIntArray(R.array.price_array);
        counter = getResources().getIntArray(R.array.counter_array);
        itemTotal = getResources().getIntArray(R.array.item_total_array);

        SharedPref = getPreferences(MODE_PRIVATE);

        itemData = loadItems();

        if (itemData.size() == 0) {
            setupData(itemName, itemPrice, counter, itemTotal);
        }

        submitOrder = findViewById(R.id.submitOrder);
        adapter = new MenuAdapter(this, itemData);

        submitOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cartTotal = adapter.grandTotal();
                Intent intent = new Intent(MenuActivityUpdate.this, CartActivity.class);
                intent.putExtra("Total Price", String.valueOf(cartTotal));
                startActivity(intent);
            }
        });

        setUpRecyclerView();
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveData();
    }

    protected void onStop() {
        super.onStop();
        deleteData();
    }

    public void setupData(String itemName[], int itemPrice[], int counter[], int itemTotal[]) {

        itemData = new ArrayList<ItemData>();

        for (int i = 0; i < itemName.length; i++) {
            ItemData instance = new ItemData();
            instance.setItemName(itemName[i]);
            instance.setItemPrice(itemPrice[i]);
            instance.setCounter(counter[i]);
            instance.setItemTotal(itemTotal[i]);
            itemData.add(instance);
        }
    }

    private void setUpRecyclerView(){

        //Initialize RecyclerView object
        recyclerViewLayout = findViewById(R.id.menuItemsView);

        //Set up a line after each row, so it looks like a list
        recyclerViewLayout.addItemDecoration(new DividerItemDecoration(
                this,DividerItemDecoration.VERTICAL));

        //Set up the LayoutManager for RecyclerView
        recyclerViewLayout.setLayoutManager(new
                LinearLayoutManager(this));

        //Attach adapter object with RecyclerView
        recyclerViewLayout.setAdapter(adapter);
    }

    private void saveData(){
        //Editor object to save or update data
        SharedPreferences.Editor editor = SharedPref.edit();
        //Convert data in to Json String
        Gson gson = new Gson();
        String jsonStamps = gson.toJson(itemData);

        //Save Data inside the SharedPreferences using putString
        editor.putString(ITEM_KEY,jsonStamps);

        //Confirm the changes
        editor.apply();
    }

    private ArrayList<ItemData> loadItems(){
        //Fetch the data from the SharedPreferences object
        String jsonStampString = SharedPref.getString(ITEM_KEY,"");

        Gson gson = new Gson();
        Type type = new TypeToken<List<ItemData>>(){}.getType();
        ArrayList<ItemData> loadData =
                gson.fromJson(jsonStampString, type);
        if(loadData == null) {
            loadData = new ArrayList<>();
        }
        return loadData;
    }

    private void deleteData() {
        SharedPreferences.Editor editor = SharedPref.edit();
        editor.clear();
        editor.apply();
    }

}
