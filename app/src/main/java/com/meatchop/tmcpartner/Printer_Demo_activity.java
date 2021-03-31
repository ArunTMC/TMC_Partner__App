package com.meatchop.tmcpartner;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.pos.printer.PrinterFunctions;

public class Printer_Demo_activity extends AppCompatActivity {
Button button;
    String portName = "USB";
    int portSettings=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_printer__demo_activity);
        button = findViewById(R.id.button);
        Printer_POJO_Class[] Printer_POJO_ClassArray = new Printer_POJO_Class[3];

       // Printer_POJO_ClassArray[0] = new Printer_POJO_Class(quantity, "12123123","Chicken Drumsticks","0.350","112.00","0.00","0.00","112.00");
       // Printer_POJO_ClassArray[1] = new Printer_POJO_Class(quantity, "12123123","Chicken BreastBoneless","0.290","129.00","0.00","0.00","129.00");
      //  Printer_POJO_ClassArray[2] = new Printer_POJO_Class(quantity, "12123123","Chicken leg","0.500","302.00","0.00","0.00","302.00");
     //   Printer_POJO_Class Printer_POJO_ClassArraytotal = new Printer_POJO_Class("543.00","0.00","0.00","543.00","");

        {
            System.loadLibrary("PosPPdrv");
        }


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                PrinterFunctions.PortDiscovery(portName,portSettings);
                PrinterFunctions.OpenCashDrawer(portName,portSettings,0,4);

                // PrinterFunctions.OpenPort( portName, portSettings);
            //    PrinterFunctions.CheckStatus( portName, portSettings,2);
              // PrinterFunctions.SelectPrintMode(portName,portSettings,0);
                PrinterFunctions.SetLineSpacing(portName,portSettings,180);
                PrinterFunctions.SelectCharacterFont(portName,portSettings,0);
                PrinterFunctions. PrintText(portName,portSettings,0, 0,0,0,2, 1,0,1,"The MeatChop"+"\n");


                PrinterFunctions.SetLineSpacing(portName,portSettings,60);
                PrinterFunctions.SelectCharacterFont(portName,portSettings,0);
                PrinterFunctions. PrintText(portName,portSettings,0, 0,0,0,0, 0,0,1,"N0:9,CLC Works Road,Chrompet,"+"\n");

                PrinterFunctions.SetLineSpacing(portName,portSettings,60);
                PrinterFunctions.SelectCharacterFont(portName,portSettings,0);
                PrinterFunctions. PrintText(portName,portSettings,0, 0,0,0,0, 0,0,1,"Chennai-600044"+"\n");



                PrinterFunctions.SetLineSpacing(portName,portSettings,80);
                PrinterFunctions.SelectCharacterFont(portName,portSettings,0);
                PrinterFunctions. PrintText(portName,portSettings,0, 0,0,0,0, 0,0,1,"9543754375"+"\n");


                PrinterFunctions.SetLineSpacing(portName,portSettings,80);
                PrinterFunctions.SelectCharacterFont(portName,portSettings,0);
                PrinterFunctions. PrintText(portName,portSettings,0, 0,1,0,0, 0,0,1,"WPI1912848468344329010"+"\n");


                PrinterFunctions.SetLineSpacing(portName,portSettings,80);
                PrinterFunctions.SelectCharacterFont(portName,portSettings,0);
                PrinterFunctions. PrintText(portName,portSettings,0, 0,0,0,0, 0,0,1,"Tuesday,December 22,2020,7:41:55AM"+"\n");


                PrinterFunctions.SetLineSpacing(portName,portSettings,40);
                PrinterFunctions.SelectCharacterFont(portName,portSettings,0);
                PrinterFunctions. PrintText(portName,portSettings,0, 0,1,0,0, 0,30,0,"----------------------------------------"+"\n");

                PrinterFunctions.SetLineSpacing(portName,portSettings,80);
                PrinterFunctions.SelectCharacterFont(portName,portSettings,0);
                PrinterFunctions. PrintText(portName,portSettings,0, 0,1,0,0, 0,30,0,"ITEM NAME * QTY"+"\n");

                PrinterFunctions.SetLineSpacing(portName,portSettings,70);
                PrinterFunctions.SelectCharacterFont(portName,portSettings,0);
                PrinterFunctions. PrintText(portName,portSettings,0, 0,1,0,0, 0,30,0,"RATE           DISC      GST    SUBTOTAL"+"\n");

                PrinterFunctions.SetLineSpacing(portName,portSettings,60);
                PrinterFunctions.SelectCharacterFont(portName,portSettings,0);
                PrinterFunctions. PrintText(portName,portSettings,0, 0,1,0,0, 0,30,0,"----------------------------------------"+"\n");
                for(int i=0; i < Printer_POJO_ClassArray.length; i++)
                {

                    PrinterFunctions.SetLineSpacing(portName, portSettings, 80);
                    PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
                    PrinterFunctions.PrintText(portName, portSettings, 0, 0, 0, 0, 0, 0, 30, 0, Printer_POJO_ClassArray[i].getItemName() + " * " + Printer_POJO_ClassArray[i].getItemWeight() + "\n");

                    PrinterFunctions.SetLineSpacing(portName, portSettings, 80);
                    PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
                    PrinterFunctions.PrintText(portName, portSettings, 0, 0, 0, 0, 0, 0, 30, 0, "Rs." + Printer_POJO_ClassArray[i].getItemRate() + "      Rs." + Printer_POJO_ClassArray[i].getDiscount() + "    Rs." + Printer_POJO_ClassArray[i].getGST() + "  Rs." + Printer_POJO_ClassArray[i].getSubTotal() + "\n");
                }

                PrinterFunctions.SetLineSpacing(portName,portSettings,60);
                PrinterFunctions.SelectCharacterFont(portName,portSettings,0);
                PrinterFunctions. PrintText(portName,portSettings,0, 0,1,0,0, 0,30,0,"----------------------------------------"+"\n");


                PrinterFunctions.SetLineSpacing(portName, portSettings, 60);
                PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
               // PrinterFunctions.PrintText(portName, portSettings, 0, 0, 1, 0, 0, 0, 30, 0, "Rs." + Printer_POJO_ClassArraytotal.getTotalRate() + "      Rs." + Printer_POJO_ClassArraytotal.getTotaldiscount() + "    Rs." + Printer_POJO_ClassArraytotal.getTotalGST() + "  Rs." + Printer_POJO_ClassArraytotal.getTotalsubtotal() + "\n");

                PrinterFunctions.SetLineSpacing(portName,portSettings,50);
                PrinterFunctions.SelectCharacterFont(portName,portSettings,0);
                PrinterFunctions. PrintText(portName,portSettings,0, 0,1,0,0, 0,30,0,"----------------------------------------"+"\n");

                PrinterFunctions.SetLineSpacing(portName, portSettings, 50);
                PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
                PrinterFunctions.PrintText(portName, portSettings, 0, 0, 0, 0, 0, 0, 30, 0, "NET TOTAL                      " );

               // PrinterFunctions.PrintText(portName, portSettings, 0, 0, 1, 0, 0, 0, 0, 1,  "Rs."+Printer_POJO_ClassArraytotal.getTotalsubtotal() + "\n");

                PrinterFunctions.SetLineSpacing(portName,portSettings,60);
                PrinterFunctions.SelectCharacterFont(portName,portSettings,0);
                PrinterFunctions. PrintText(portName,portSettings,0, 0,1,0,0, 0,30,0,"----------------------------------------"+"\n");


                PrinterFunctions.SetLineSpacing(portName,portSettings,60);
                PrinterFunctions.SelectCharacterFont(portName,portSettings,0);
                PrinterFunctions. PrintText(portName,portSettings,0, 0,1,0,0, 0,30,0,"Payment Mode: ");


                PrinterFunctions.SetLineSpacing(portName,portSettings,90);
                PrinterFunctions.SelectCharacterFont(portName,portSettings,0);
                PrinterFunctions. PrintText(portName,portSettings,0, 0,0,0,0, 0,30,0,"CARD"+"\n");


                PrinterFunctions.SetLineSpacing(portName,portSettings,60);
                PrinterFunctions.SelectCharacterFont(portName,portSettings,0);
                PrinterFunctions. PrintText(portName,portSettings,0, 0,1,0,0, 0,30,0,"Customer : ");


                PrinterFunctions.SelectCharacterFont(portName,portSettings,0);
                PrinterFunctions. PrintText(portName,portSettings,0, 0,0,0,0, 0,0,0,"Arun            ");

                PrinterFunctions.SetLineSpacing(portName,portSettings,120);
                PrinterFunctions.SelectCharacterFont(portName,portSettings,0);
                PrinterFunctions. PrintText(portName,portSettings,0, 0,1,0,0, 0,0,30,"ID : ");


                PrinterFunctions.SelectCharacterFont(portName,portSettings,0);
                PrinterFunctions. PrintText(portName,portSettings,0, 0,0,0,0, 0,0,50,"6739"+"\n");

                PrinterFunctions.SelectCharacterFont(portName,portSettings,0);
                PrinterFunctions. PrintText(portName,portSettings,0, 0,1,0,0, 0,0,1,"Thank you for choosing us !!! : "+"\n");



                PrinterFunctions.PreformCut(portName,portSettings,1);
              //  PrinterFunctions.PrintSampleReceipt(portName,portSettings);
                Log.i("tag","printer Log    "+                PrinterFunctions.PortDiscovery(portName,portSettings));

                Log.i("tag","printer Log    "+                PrinterFunctions.OpenPort( portName, portSettings));

                Log.i("tag","printer Log    "+        PrinterFunctions.CheckStatus( portName, portSettings,2));


            }
        });




           }
}