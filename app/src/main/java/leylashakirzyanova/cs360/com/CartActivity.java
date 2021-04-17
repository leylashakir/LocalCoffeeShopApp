package leylashakirzyanova.cs360.com;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class CartActivity extends AppCompatActivity {

    Button placeOrderButton;
    TextView cartTotalView;
    private String cartTotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        cartTotalView = findViewById(R.id.orderTotalCartView);
        cartTotal = getIntent().getStringExtra("Total Price");
        cartTotalView.setText("$ " + cartTotal);

        placeOrderButton = findViewById(R.id.placeOrder);
        placeOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setContentView(R.layout.activity_order_placed);
            }
        });

    }

}
