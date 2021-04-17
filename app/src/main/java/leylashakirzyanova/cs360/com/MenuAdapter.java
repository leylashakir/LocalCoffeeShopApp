package leylashakirzyanova.cs360.com;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.MenuHolder> {

    private ArrayList<ItemData> itemDataArrayList;
    private LayoutInflater layoutInflater;

    public MenuAdapter (Context context, ArrayList<ItemData> itemDataList) {
        layoutInflater = LayoutInflater.from(context);
        itemDataArrayList = itemDataList;
    }

    @NonNull
    @Override
    public MenuAdapter.MenuHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.recycler_view_layout, parent, false);
        return new MenuHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MenuAdapter.MenuHolder holder, int position) {

        ItemData currentItemData = itemDataArrayList.get(position);
        holder.itemName.setText(
                currentItemData.getItemName());
        holder.itemPrice.setText(
                String.valueOf(currentItemData.getItemPrice()));
        holder.itemCount.setText(
                String.valueOf(currentItemData.getCounter()));
        holder.itemTotal.setText(
                String.valueOf(currentItemData.getItemTotal()));
    }

    public int grandTotal() {
        int totalPrice = 0;
        for (int i = 0; i < itemDataArrayList.size(); i++) {
            totalPrice += itemDataArrayList.get(i).getItemTotal();
        }
        return totalPrice;
    }

    @Override
    public int getItemCount() {
        return itemDataArrayList.size();
    }

    public class MenuHolder extends RecyclerView.ViewHolder {

        TextView itemName;
        TextView itemPrice;
        TextView itemCount;
        TextView itemTotal;

        Button addToCart;
        Button removeFromCart;

        private int counter;
        private int price;
        private ItemData editItemData;
        private int totalItemPrice;
        private int cartTotal;

        public MenuHolder(@NonNull final View itemView) {
            super(itemView);

            itemName = itemView.findViewById(R.id.itemName);
            itemPrice = itemView.findViewById(R.id.itemPrice);
            itemCount = itemView.findViewById(R.id.itemCount);
            itemTotal = itemView.findViewById(R.id.totalItemPrice);

            addToCart = itemView.findViewById(R.id.add_button);
            removeFromCart = itemView.findViewById(R.id.sub_button);

            //Use OnClick Listeners to Handle Add Button
            addToCart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    changeItemCount(true);
                }
            });

            //Use OnClick Listeners to Handle Sub Button
            removeFromCart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    changeItemCount(false);
                }
            });

        }

        private void changeItemCount (boolean increaseCount)
        {
            // Get the current ItemData object by using the
            // getAdapterPosition() method
            editItemData = itemDataArrayList.
                    get(getAdapterPosition());

            //Access the current counter and price value from ItemData object
            counter = editItemData.getCounter();
            price = editItemData.getItemPrice();

            //Increment the counter value by one every time
            if (increaseCount) {
                counter++;
                totalItemPrice = counter * price;
            }
            else {
                if (counter > 0){
                    counter--;
                    totalItemPrice = counter * price;
                }
            }

            //Update the new counter value in ItemData object
            editItemData.setCounter(counter);
            editItemData.setItemTotal(totalItemPrice);

            //Update the current object in the ArrayList object too
            itemDataArrayList.set(getAdapterPosition(), editItemData);

            //Notify the adapter about the changes
            // made at the current position
            notifyItemChanged(getAdapterPosition());
        }

    }

}
