package com.meatchop.tmcpartner.settings;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.Configuration;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.TextView;

import com.meatchop.tmcpartner.R;

import java.util.ArrayList;
import java.util.List;

public class CreditOrders_MobileNumberwiseTransactionScreen extends AppCompatActivity {
ListView creditTransactionListView;
TextView totalCreditAmountPaidback_textWidget,totalCreditAmount_textWidget;
List<Modal_CreditOrdersTransactionDetails> creditOrdersTransactionDetails_Array = new ArrayList<>();
    Modal_CreditOrderDetails modal_creditOrderDetails ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adjustFontScale( getResources().getConfiguration());

        setContentView(R.layout.activity_credit_orders__mobile_numberwise_transaction_screen);
        creditTransactionListView = findViewById(R.id.creditTransactionListView);
        totalCreditAmount_textWidget = findViewById(R.id.totalCreditAmount_textWidget);
        totalCreditAmountPaidback_textWidget = findViewById(R.id.totalCreditAmountPaidback_textWidget);
        modal_creditOrderDetails = new Modal_CreditOrderDetails();
        creditOrdersTransactionDetails_Array.clear();
        try {
            Bundle bundle = getIntent().getExtras();
            modal_creditOrderDetails = bundle.getParcelable("modal_creditOrderDetails");
            creditOrdersTransactionDetails_Array = (ArrayList<Modal_CreditOrdersTransactionDetails>) bundle.getSerializable("creditOrdersTransactionDetails_Array");
            Adapter_CreditOrdersTransactionList adapter_creditOrdersTransactionList = new Adapter_CreditOrdersTransactionList(CreditOrders_MobileNumberwiseTransactionScreen.this, creditOrdersTransactionDetails_Array, CreditOrders_MobileNumberwiseTransactionScreen.this);
            creditTransactionListView.setAdapter(adapter_creditOrdersTransactionList);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        try {
            totalCreditAmount_textWidget.setText(modal_creditOrderDetails.getTotalAmountGivenAsCredit().toString() + " Rs");
            totalCreditAmountPaidback_textWidget.setText(modal_creditOrderDetails.getTotalPaidAmount().toString() + " Rs");
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public  void adjustFontScale( Configuration configuration) {

        configuration.fontScale = (float) 1.0;
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(metrics);
        metrics.scaledDensity = configuration.fontScale * metrics.density;
        getBaseContext().getResources().updateConfiguration(configuration, metrics);

    }
}