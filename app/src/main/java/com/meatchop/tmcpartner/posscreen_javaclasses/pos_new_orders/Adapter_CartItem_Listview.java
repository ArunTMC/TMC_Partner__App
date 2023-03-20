package com.meatchop.tmcpartner.posscreen_javaclasses.pos_new_orders;

public class Adapter_CartItem_Listview {

}
        //extends ArrayAdapter<Modal_NewOrderItems>{
/*

    private Context context;
    private String pricetype_of_pos, portionsize;
    Adapter_AutoCompleteMenuItem adapter;
    private int item_total = 0, quantity_of_item, unit_price_of_Item, removed_total_Amount = 0, removed_gst_Amount = 0;
    String Menulist;
    private Handler handler;
    int price_per_kg, taxes_and_charges;
    NewOrders_MenuItem_Fragment newOrders_menuItem_fragment;
    RecyclerView recyclerView;
    boolean isdataFetched = false;
    boolean isWeightCalculated = false;
    List<Modal_NewOrderItems>CartList = new ArrayList<>();

    List<Modal_NewOrderItems>cartItemfromhashmap;
    public static HashMap<String,Modal_NewOrderItems> itemInCart = new HashMap();

        // View lookup cache
        private static class ViewHolder {
            AutoCompleteTextView autoComplete_widget;

            LinearLayout tmcUnitprice_weightAdd_layout, tmcUnitprice_weightMinus_layout;


            TextView itemIndex,itemWeight_widget, itemQuantity_widget;

            TextView itemPrice_Widget;

            EditText  barcode_widget;

            ImageView minus_to_remove_item_widget;
            LinearLayout removeItem_fromCart_widget, addNewItem_layout;
            boolean isTMCproduct = false;

        }


    public Adapter_CartItem_Listview(Context context, HashMap<String, Modal_NewOrderItems> itemInCart, String menuItems, NewOrders_MenuItem_Fragment newOrders_menuItem_fragment) {
        super(context, R.layout.pos_manageorders_listview_child);

        Log.e(TAG, "Auto call adapter itemInCart itemInCart" + itemInCart.size());
        this.newOrders_menuItem_fragment = newOrders_menuItem_fragment;
        this.context = context;

        this.itemInCart = itemInCart;

        this.Menulist = menuItems;
    }
        private int lastPosition = -1;

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder; // view lookup cache stored in tag

            final View result;

            if (convertView == null) {

                holder = new ViewHolder();
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(R.layout.neworders_cart_tem_listview, parent, false);
                holder.autoComplete_widget = convertView.findViewById(R.id.autoComplete_widget);
                holder.itemIndex = convertView.findViewById(R.id.item_NO);
                holder.barcode_widget = convertView.findViewById(R.id.barcode_widget);

                holder.tmcUnitprice_weightAdd_layout = convertView.findViewById(R.id.tmcUnitprice_weightAdd_layout);
                holder.tmcUnitprice_weightMinus_layout = convertView.findViewById(R.id.tmcUnitprice_weightMinus_layout);

                holder.itemWeight_widget = convertView.findViewById(R.id.itemWeight_widget);
                holder.itemQuantity_widget = convertView.findViewById(R.id.itemQuantity_widget);



                holder.itemPrice_Widget = convertView.findViewById(R.id.itemPrice_Widget);

                holder.minus_to_remove_item_widget = convertView.findViewById(R.id.minus_to_remove_item_widget);

                holder.removeItem_fromCart_widget = convertView.findViewById(R.id.removeItem_fromCart_widget);
                holder.addNewItem_layout = convertView.findViewById(R.id.addNewItem_layout);


                result=convertView;

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
                result=convertView;

            }

            Log.e(TAG, "onBindViewHolder: called.");
            Log.e("TAG", "adapter       " + newOrders_menuItem_fragment.cart_Item_List.size());
            Log.e(TAG, "Got barcode isBarcodeEntered onbindview "+isdataFetched);
            Log.e("TAG", "adapter       ");


            holder.itemIndex.setText(String.valueOf(position + 1));

            if (position == (itemInCart.size() - 1)) {
                holder.addNewItem_layout.setVisibility(View.VISIBLE);
            } else {
                holder.addNewItem_layout.setVisibility(View.GONE);

            }
            int length = holder.autoComplete_widget.getText().length();
            holder.autoComplete_widget.setSelection(length);
            Log.e("TAG", "position" + position);
            Modal_NewOrderItems recylerviewPojoClass = NewOrders_MenuItem_Fragment.cartItem_hashmap.get(NewOrders_MenuItem_Fragment.cart_Item_List.get(position));


            Log.i("TAG", "HASHMAP" + recylerviewPojoClass.getItemuniquecode());


            if (recylerviewPojoClass.getItemuniquecode().equals("empty")) {
                Log.e("TAG", "adapter 1" + recylerviewPojoClass.getItemname());
                Log.e("TAG", "adapter 1 " + recylerviewPojoClass.getGrossweight());
                Log.e("TAG", "adapter 1" + recylerviewPojoClass.getPricePerItem());
                Log.e("TAG", "adapter 1" + recylerviewPojoClass.getTmcpriceperkg());

                holder.itemPrice_Widget.setText("");
                holder.itemWeight_widget.setText("");
                holder.itemQuantity_widget.setText("");
                holder.barcode_widget.setText("");

                holder.autoComplete_widget.setText("");
                isdataFetched = false;
                Log.e(TAG, "Got barcode isBarcodeEntered on empty data 2 " + isdataFetched);

            } else {


                newOrders_menuItem_fragment.add_amount_ForBillDetails();

                pricetype_of_pos = String.valueOf(recylerviewPojoClass.getPricetypeforpos());

                isdataFetched = true;

                if (pricetype_of_pos.equals("tmcprice")) {

                    holder.barcode_widget.setText(recylerviewPojoClass.getItemuniquecode());
                    holder.barcode_widget.setKeyListener(null);
                    recylerviewPojoClass.setItemPrice_quantityBased(String.valueOf(recylerviewPojoClass.getTmcprice()));


                    holder.autoComplete_widget.setText(recylerviewPojoClass.getItemname());
                    holder.autoComplete_widget.setKeyListener(null);

                    holder.itemPrice_Widget.setText(String.valueOf(recylerviewPojoClass.getItemFinalPrice()));
                    holder.itemQuantity_widget.setText(String.valueOf(recylerviewPojoClass.getQuantity()));
                    taxes_and_charges = Integer.parseInt(recylerviewPojoClass.getGstpercentage());

                    if (recylerviewPojoClass.getPortionsize().equals("")) {
                        if (recylerviewPojoClass.getNetweight().equals("")) {
                            holder.itemWeight_widget.setText(String.valueOf(recylerviewPojoClass.getGrossweight()));

                        } else {
                            holder.itemWeight_widget.setText(String.valueOf(recylerviewPojoClass.getNetweight()));

                        }

                    } else {
                        holder.itemWeight_widget.setText(String.valueOf(recylerviewPojoClass.getPortionsize()));
                    }

                } else if (pricetype_of_pos.equals("tmcpriceperkg")) {
                    holder.barcode_widget.setText(recylerviewPojoClass.getItemuniquecode());
                    holder.barcode_widget.setKeyListener(null);

                    holder.autoComplete_widget.setText(recylerviewPojoClass.getItemname());
                    holder.autoComplete_widget.setKeyListener(null);
                    holder.itemPrice_Widget.setText(String.valueOf(recylerviewPojoClass.getPricePerItem()));
                    recylerviewPojoClass.setItemPrice_quantityBased(String.valueOf(recylerviewPojoClass.getTmcpriceperkg()));

                    taxes_and_charges = Integer.parseInt(recylerviewPojoClass.getGstpercentage());
                    holder.itemWeight_widget.setText(String.valueOf(recylerviewPojoClass.getGrossweight()));
                    holder.itemQuantity_widget.setText(String.valueOf(recylerviewPojoClass.getQuantity()));
                }
                if (position == (newOrders_menuItem_fragment.cart_Item_List.size() - 1)) {
                    isdataFetched = false;
                }
            }


/*
        Modal_NewOrderItems recylerviewPojoClass = newOrders_menuItem_fragment.cart_Item_List.get(position);


        if(recylerviewPojoClass.getTmcpriceperkg().equals("")&&recylerviewPojoClass.getTmcprice().equals("")){
            Log.e("TAG", "adapter 1" + recylerviewPojoClass.getItemname());
            Log.e("TAG", "adapter 1 " + recylerviewPojoClass.getGrossweight());
            Log.e("TAG", "adapter 1" + recylerviewPojoClass.getPricePerItem());
            Log.e("TAG", "adapter 1" + recylerviewPojoClass.getTmcpriceperkg());

            holder.itemPrice_Widget.setText(recylerviewPojoClass.getSubTotal_perItem());
            holder.itemWeight_widget.setText(recylerviewPojoClass.getGrossweight());
            holder.itemQuantity_widget.setText(recylerviewPojoClass.getPortionsize());
            holder.barcode_widget.setText(recylerviewPojoClass.getItemuniquecode());

            holder.autoComplete_widget.setText(recylerviewPojoClass.getItemname());
            isdataFetched = false;
            Log.e(TAG, "Got barcode isBarcodeEntered on empty data 2 "+isdataFetched);

        }
        else {



            newOrders_menuItem_fragment.add_amount_ForBillDetails();

            pricetype_of_pos = String.valueOf(recylerviewPojoClass.getPricetypeforpos());


            if (pricetype_of_pos.equals("tmcprice")) {

                holder.barcode_widget.setText(recylerviewPojoClass.getItemuniquecode());
                    holder.barcode_widget.setKeyListener(null);
                recylerviewPojoClass.setItemPrice_quantityBased(String.valueOf(recylerviewPojoClass.getTmcprice()));


                holder.autoComplete_widget.setText(recylerviewPojoClass.getItemname());
                holder.autoComplete_widget.setKeyListener(null);

                holder.itemPrice_Widget.setText(String.valueOf(recylerviewPojoClass.getPricePerItem()));
                holder.itemQuantity_widget.setText(String.valueOf(recylerviewPojoClass.getQuantity()));
                taxes_and_charges = Integer.parseInt(recylerviewPojoClass.getGstpercentage());

                if (recylerviewPojoClass.getPortionsize().equals("")) {
                    if (recylerviewPojoClass.getNetweight().equals("")) {
                        holder.itemWeight_widget.setText(String.valueOf(recylerviewPojoClass.getGrossweight()));

                    } else {
                        holder.itemWeight_widget.setText(String.valueOf(recylerviewPojoClass.getNetweight()));

                    }

                } else {
                    holder.itemWeight_widget.setText(String.valueOf(recylerviewPojoClass.getPortionsize()));
                }

            } else if (pricetype_of_pos.equals("tmcpriceperkg")) {
                holder.barcode_widget.setText(recylerviewPojoClass.getItemuniquecode());
                holder.barcode_widget.setKeyListener(null);

                holder.autoComplete_widget.setText(recylerviewPojoClass.getItemname());
                holder.autoComplete_widget.setKeyListener(null);
                holder.itemPrice_Widget.setText(String.valueOf(recylerviewPojoClass.getPricePerItem()));
                recylerviewPojoClass.setItemPrice_quantityBased(String.valueOf(recylerviewPojoClass.getTmcpriceperkg()));

                taxes_and_charges = Integer.parseInt(recylerviewPojoClass.getGstpercentage());
                holder.itemWeight_widget.setText(String.valueOf(recylerviewPojoClass.getGrossweight()));
                holder.itemQuantity_widget.setText(String.valueOf(recylerviewPojoClass.getQuantity()));
            }
            if (position == (newOrders_menuItem_fragment.cart_Item_List.size() - 1)) {
                isdataFetched = false;
            }
        }




            holder.addNewItem_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!holder.autoComplete_widget.getText().toString().equals("") && (!holder.itemWeight_widget.getText().toString().equals("") || !holder.itemQuantity_widget.getText().toString().equals(""))) {


                        Log.e(TAG, "Got barcode isBarcodeEntered on  add new 1" + isdataFetched);

                        isdataFetched = true;

                        Modal_NewOrderItems newOrdersPojoClass = new Modal_NewOrderItems();
                        newOrdersPojoClass.itemname = "";
                        newOrdersPojoClass.tmcpriceperkg = "";
                        newOrdersPojoClass.grossweight = "";
                        newOrdersPojoClass.netweight = "";
                        newOrdersPojoClass.tmcprice = "";
                        newOrdersPojoClass.gstpercentage = "";
                        newOrdersPojoClass.portionsize = "";
                        newOrdersPojoClass.pricetypeforpos = "";
                        newOrdersPojoClass.itemFinalWeight = "";
                        newOrdersPojoClass.pricePerItem = "";
                        newOrdersPojoClass.itemuniquecode = "empty";
                        newOrdersPojoClass.itemPrice_quantityBased = "";

                        NewOrders_MenuItem_Fragment.cart_Item_List.add("empty");
                        NewOrders_MenuItem_Fragment.cartItem_hashmap.put("empty", newOrdersPojoClass);
                        Log.e(TAG, "Got barcode isBarcodeEntered on  add new 2" + isdataFetched);

                        NewOrders_MenuItem_Fragment.adapter_cartItem_listview.notifyDataSetChanged();


                    } else {
                        Toast.makeText(context, "You have to fill this Item First", Toast.LENGTH_LONG).show();
                    }


                }
            });


            holder.removeItem_fromCart_widget.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    isdataFetched = true;
                    Log.e(TAG, "Item" + String.valueOf(NewOrders_MenuItem_Fragment.cartItem_hashmap.size() - 1));

                    String barcode = holder.barcode_widget.getText().toString();
                    Log.i("TAG", "KEY: " + barcode);
                    if ((NewOrders_MenuItem_Fragment.cartItem_hashmap.size() - 1) == 0) {

                        Log.i("TAG", "KEY: " + barcode);

                        NewOrders_MenuItem_Fragment.cartItem_hashmap.remove(barcode);
                        NewOrders_MenuItem_Fragment.cart_Item_List.remove(barcode);
                        NewOrders_MenuItem_Fragment.adapter_cartItem_listview.notifyDataSetChanged();
                        Log.e(TAG, "Item_not_deleted  " + String.valueOf(NewOrders_MenuItem_Fragment.cartItem_hashmap.size() - 1));

                    } else {
                        Log.i("TAG", "KEY: " + barcode);


                        NewOrders_MenuItem_Fragment.cartItem_hashmap.remove(barcode);
                        NewOrders_MenuItem_Fragment.cart_Item_List.remove(barcode);

                        // newOrders_menuItem_fragment.add_amount_ForBillDetails();

                        NewOrders_MenuItem_Fragment.adapter_cartItem_listview.notifyDataSetChanged();
                        Log.e(TAG, "Item_deleted  " + String.valueOf(NewOrders_MenuItem_Fragment.cartItem_hashmap.size() - 1));


                    }


                }
            });

            Log.e(TAG, "Auto menu in cart adapter 1 : " + Menulist);


            holder.tmcUnitprice_weightAdd_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String barcode = NewOrders_MenuItem_Fragment.cart_Item_List.get(position);

                    Modal_NewOrderItems modal_newOrderItems = NewOrders_MenuItem_Fragment.cartItem_hashmap.get(barcode);

                    int quantity = Integer.parseInt(holder.itemQuantity_widget.getText().toString());
                    quantity = quantity + 1;
                    holder.itemQuantity_widget.setText(String.valueOf(quantity));
                    int item_price = Integer.parseInt(modal_newOrderItems.getItemPrice_quantityBased());
                    item_price = item_price * quantity;
                    holder.itemPrice_Widget.setText(String.valueOf(item_price));
                    modal_newOrderItems.setQuantity(String.valueOf(quantity));
                    modal_newOrderItems.setPricePerItem(String.valueOf(item_price));
                    newOrders_menuItem_fragment.add_amount_ForBillDetails();


                }
            });


            holder.tmcUnitprice_weightMinus_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String barcode = NewOrders_MenuItem_Fragment.cart_Item_List.get(position);

                    Modal_NewOrderItems modal_newOrderItems = NewOrders_MenuItem_Fragment.cartItem_hashmap.get(barcode);


                    int quantity = Integer.parseInt(holder.itemQuantity_widget.getText().toString());
                    if (quantity > 1) {
                        quantity = quantity - 1;
                        holder.itemQuantity_widget.setText(String.valueOf(quantity));
                        int item_price = Integer.parseInt(modal_newOrderItems.getItemPrice_quantityBased());
                        item_price = item_price * quantity;
                        holder.itemPrice_Widget.setText(String.valueOf(item_price));
                        modal_newOrderItems.setQuantity(String.valueOf(quantity));
                        modal_newOrderItems.setPricePerItem(String.valueOf(item_price));
                        newOrders_menuItem_fragment.add_amount_ForBillDetails();
                    } else {
                        Toast.makeText(context, "To Remove the Item Click the Delete Icon", Toast.LENGTH_LONG).show();
                    }


                }
            });



            adapter = new Adapter_AutoCompleteMenuItem(context, Menulist);
         //   adapter.setHandler(newHandler());


            holder.autoComplete_widget.setAdapter(adapter);
            holder.autoComplete_widget.clearFocus();


            holder.barcode_widget.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    String s1 = (editable.toString());
                    if(s1.length()==3){
                        if(s1.equals("999")){
                            holder.isTMCproduct=true;
                        }
                        else {
                            holder.isTMCproduct=false;
                        }
                    }
                    Log.e(TAG, "Got barcode isBarcodeEntered in on textchanged"+s1);

                    Log.e(TAG, "Got barcode isBarcodeEntered after text changed"+isdataFetched);

                    if (s1.length() > 3) {
                        if(!isdataFetched) {

                            if ( holder.isTMCproduct) {
                                if ( holder.barcode_widget.getText().toString().length() == 14) {

                                    Log.e(TAG, "Got barcode " +  holder.barcode_widget.getText().length());
                                    Log.e(TAG, "Got barcode isBarcodeEntered in condition check 13/14" + isdataFetched);

                                    String Barcode =  holder.barcode_widget.getText().toString();


                                    getMenuItemUsingBarCode(Barcode);
                                }
                            } else {
                                if ( holder.barcode_widget.getText().toString().length() == 13) {

                                    Log.e(TAG, "Got barcode " +  holder.barcode_widget.getText().length());
                                    Log.e(TAG, "Got barcode isBarcodeEntered in condition check 13/14" + isdataFetched);

                                    String Barcode =  holder.barcode_widget.getText().toString();

                                    getMenuItemUsingBarCode(Barcode);
                                }
                            }
                        }

                    }


                }

                public void getMenuItemUsingBarCode(String barcode) {
                    Log.e(TAG, "Got barcode isBarcodeEntered getMenuItemUsingBarCode"+isdataFetched);
                    isdataFetched=true;

                    if (barcode.length() == 13) {
                        Log.e(TAG, "Got barcode isBarcodeEntered getMenuItemUsingBarCode"+isdataFetched);
                        String itemWeight;
                        Log.e(TAG, "1 barcode " + barcode);
                        Log.e(TAG, "Got barcode isBarcodeEntered getMenuItemUsingBarCode"+isdataFetched);

                        for (int i = 0; i < NewOrders_MenuItem_Fragment.completemenuItem.size(); i++) {

                            Modal_NewOrderItems modal_newOrderItems = NewOrders_MenuItem_Fragment.completemenuItem.get(i);

                            if ((String.valueOf(modal_newOrderItems.getItemuniquecode())).equals(barcode)) {


                                Modal_NewOrderItems newItem_newOrdersPojoClass = new Modal_NewOrderItems();
                                newItem_newOrdersPojoClass.itemname = modal_newOrderItems.getItemname();
                                newItem_newOrdersPojoClass.grossweight = modal_newOrderItems.getGrossweight();
                                newItem_newOrdersPojoClass.netweight = modal_newOrderItems.getNetweight();
                                newItem_newOrdersPojoClass.tmcprice = modal_newOrderItems.getTmcprice();
                                newItem_newOrdersPojoClass.tmcpriceperkg = modal_newOrderItems.getTmcpriceperkg();

                                newItem_newOrdersPojoClass.gstpercentage = modal_newOrderItems.getGstpercentage();
                                newItem_newOrdersPojoClass.portionsize = modal_newOrderItems.getPortionsize();
                                newItem_newOrdersPojoClass.pricetypeforpos = modal_newOrderItems.getPricetypeforpos();
                                newItem_newOrdersPojoClass.itemuniquecode = modal_newOrderItems.getItemuniquecode();
                                newItem_newOrdersPojoClass.itemFinalPrice = modal_newOrderItems.getTmcprice();
                                newItem_newOrdersPojoClass.setItemPrice_quantityBased( modal_newOrderItems.getTmcprice());
                                newItem_newOrdersPojoClass.quantity = "1";
                                newItem_newOrdersPojoClass.subTotal_perItem="";
                                newItem_newOrdersPojoClass.total_of_subTotal_perItem="";
                                newItem_newOrdersPojoClass.totalGstAmount="";

                                if (modal_newOrderItems.getGrossweight().equals("") && modal_newOrderItems.getNetweight().equals("")) {
                                    Log.e(Constants.TAG, "getPortionsize " + (String.format(" %s", modal_newOrderItems.getPortionsize())));
                                    newItem_newOrdersPojoClass.itemFinalWeight = (modal_newOrderItems.getPortionsize());

                                    //     itemWeightTextview_widget.setText(String.valueOf(modal_newOrderItems.getPortionsize()));
                                    newOrders_menuItem_fragment.add_amount_ForBillDetails();
                                    itemWeight = String.valueOf(modal_newOrderItems.getPortionsize());
                                } else if (modal_newOrderItems.getNetweight().equals("")) {

                                    Log.e(Constants.TAG, "getGrossweight " + (String.format(" %s", modal_newOrderItems.getGrossweight())));

                                    newItem_newOrdersPojoClass.itemFinalWeight = (modal_newOrderItems.getGrossweight());
                                    //   itemWeightTextview_widget.setText(String.valueOf(modal_newOrderItems.getGrossweight()));
                                    newOrders_menuItem_fragment.add_amount_ForBillDetails();
                                    itemWeight = String.valueOf(modal_newOrderItems.getGrossweight());


                                } else if (modal_newOrderItems.getGrossweight().equals("")) {
                                    Log.e(Constants.TAG, "getNetweight " + (String.format(" %s", modal_newOrderItems.getNetweight())));
                                    newItem_newOrdersPojoClass.itemFinalWeight = (modal_newOrderItems.getNetweight());

                                    //     itemWeightTextview_widget.setText(String.valueOf(modal_newOrderItems.getNetweight()));
                                    newOrders_menuItem_fragment.add_amount_ForBillDetails();
                                    itemWeight = String.valueOf(modal_newOrderItems.getNetweight());


                                } else {
                                    Log.e(Constants.TAG, "getGrossweight " + (String.format(" %s", modal_newOrderItems.getGrossweight())));
                                    //   itemWeightTextview_widget.setText(String.valueOf(modal_newOrderItems.getGrossweight()));

                                    newItem_newOrdersPojoClass.itemFinalWeight = (modal_newOrderItems.getGrossweight());
                                    newOrders_menuItem_fragment.add_amount_ForBillDetails();
                                    itemWeight = String.valueOf(modal_newOrderItems.getGrossweight());


                                }

                                addItemIntheCart(newItem_newOrdersPojoClass, itemWeight,barcode);


                            }


                        }
                    }
                    if (barcode.length() == 14) {
                        String itemuniquecode = barcode.substring(0, 9);
                        String itemWeight = barcode.substring(9, 14);
                        Log.e(TAG, "1 barcode uniquecode" + itemuniquecode);
                        Log.e(TAG, "1 barcode itemweight" + itemWeight);

                        for (int i = 0; i < NewOrders_MenuItem_Fragment.completemenuItem.size(); i++) {

                            Modal_NewOrderItems modal_newOrderItems = NewOrders_MenuItem_Fragment.completemenuItem.get(i);

                            if (String.valueOf(modal_newOrderItems.getItemuniquecode()).equals(itemuniquecode)) {

                                Modal_NewOrderItems newItem_newOrdersPojoClass = new Modal_NewOrderItems();
                                //  newItem_newOrdersPojoClass.itemname = modal_newOrderItems.getItemname();
                                newItem_newOrdersPojoClass.tmcpriceperkg = modal_newOrderItems.getTmcpriceperkg();
                                newItem_newOrdersPojoClass.grossweight = modal_newOrderItems.getGrossweight();
                                newItem_newOrdersPojoClass.netweight = modal_newOrderItems.getNetweight();
                                newItem_newOrdersPojoClass.tmcprice = modal_newOrderItems.getTmcprice();
                                newItem_newOrdersPojoClass.gstpercentage = modal_newOrderItems.getGstpercentage();
                                newItem_newOrdersPojoClass.portionsize = modal_newOrderItems.getPortionsize();
                                newItem_newOrdersPojoClass.pricetypeforpos = modal_newOrderItems.getPricetypeforpos();
                                newItem_newOrdersPojoClass.itemuniquecode = (modal_newOrderItems.getItemuniquecode());
                                newItem_newOrdersPojoClass.setItemPrice_quantityBased( modal_newOrderItems.getTmcpriceperkg());

                                newItem_newOrdersPojoClass.quantity = "1";
                                newItem_newOrdersPojoClass.subTotal_perItem="";
                                newItem_newOrdersPojoClass.total_of_subTotal_perItem="";
                                newItem_newOrdersPojoClass.totalGstAmount="";
                                newItem_newOrdersPojoClass.itemFinalPrice=modal_newOrderItems.getTmcpriceperkg();



                                if (String.valueOf(modal_newOrderItems.getPricetypeforpos()).equals("tmcpriceperkg")) {
                                    int priceperKg=Integer.parseInt(modal_newOrderItems.getTmcpriceperkg());
                                    int weight = Integer.parseInt(itemWeight);
                                    if (weight < 1000) {
                                        item_total = (priceperKg * weight);
                                        Log.e("TAG", "adapter 9 item_total price_per_kg" + priceperKg);

                                        Log.e("TAG", "adapter 9 item_total weight" + weight);

                                        Log.e("TAG", "adapter 9 item_total " + priceperKg * weight);

                                        item_total = item_total / 1000;
                                        Log.e("TAG", "adapter 9 item_total " + item_total);

                                        Log.e("TAg", "weight2" + weight);


                                        //itemInCart.get(getAdapterPosition()).setPricePerItem(String.valueOf(item_total));
                                        //  itemInCart.get(getAdapterPosition()).setItemFinalWeight(String.valueOf(weight));
                                        Log.e("TAg", "weight item_total" + item_total);

                                        holder.itemPrice_Widget.setText(String.valueOf(item_total));
                                        newOrders_menuItem_fragment.add_amount_ForBillDetails();

                                        NewOrders_MenuItem_Fragment.adapter_cartItem_listview.notifyDataSetChanged();

                                    }

                                    if (weight == 1000) {

                                        holder.itemPrice_Widget.setText(String.valueOf(priceperKg));

                                        // itemInCart.get(getAdapterPosition()).setPricePerItem(String.valueOf(priceperKg));
                                        //  itemInCart.get(getAdapterPosition()).setItemFinalWeight(String.valueOf(weight));

                                        Log.e("TAG", "Cart adapter price_per_kg +" + priceperKg);


                                        newOrders_menuItem_fragment.add_amount_ForBillDetails();
                                        NewOrders_MenuItem_Fragment.adapter_cartItem_listview.notifyDataSetChanged();

                                    }

                                    if (weight > 1000) {
                                        Log.e("TAG", "Cart adapter price_per_kg +" + priceperKg);

                                        Log.e("TAg", "weight3" + weight);

                                        int itemquantity = weight - 1000;
                                        Log.e("TAg", "weight itemquantity" + itemquantity);

                                        item_total = (price_per_kg * itemquantity) / 1000;


                                        Log.e("TAg", "weight item_total" + item_total);

                                        // itemInCart.get(getAdapterPosition()).setPricePerItem(String.valueOf(priceperKg + item_total));
                                        //  itemInCart.get(getAdapterPosition()).setItemFinalWeight(String.valueOf(weight));
                                        Log.e("TAG", "Cart adapter price_per_kg +" + priceperKg);

                                        holder.itemPrice_Widget.setText(String.valueOf(priceperKg + item_total));
                                        Log.e("TAg", "weight item_total+price" + item_total + priceperKg);
                                        newOrders_menuItem_fragment.add_amount_ForBillDetails();
                                        NewOrders_MenuItem_Fragment.adapter_cartItem_listview.notifyDataSetChanged();

                                    }



                                }


                                if (String.valueOf(modal_newOrderItems.getPricetypeforpos()).equals("tmcprice")) {
                                    newItem_newOrdersPojoClass.pricePerItem = modal_newOrderItems.getTmcprice();

                                }

                                Log.e(TAG, "Got barcode getMenuItemUsingBarCode"+isdataFetched);

                                newItem_newOrdersPojoClass.itemFinalWeight = itemWeight;
                                addItemIntheCart(newItem_newOrdersPojoClass, itemWeight,itemuniquecode);


                            }


                        }

                    }
                }


                private void addItemIntheCart(Modal_NewOrderItems newItem_newOrdersPojoClass, String itemWeight,String itemUniquecode) {
                    Log.e(TAG, "Got barcode addItemIntheCart"+isdataFetched);
                    boolean IsItemAlreadyAddedinCart ;
                    IsItemAlreadyAddedinCart = checkforBarcodeInCart(itemUniquecode);
                    if(IsItemAlreadyAddedinCart){
                        Modal_NewOrderItems modal_newOrderItems = NewOrders_MenuItem_Fragment.cartItem_hashmap.get(itemUniquecode);
                        int quantity = Integer.parseInt(modal_newOrderItems.getQuantity());
                        quantity = quantity + 1;

                        int itemPrice_quantityBased = Integer.parseInt(modal_newOrderItems.getItemPrice_quantityBased());
                        int finalprice = quantity * itemPrice_quantityBased;
                        modal_newOrderItems.setItemFinalPrice(String.valueOf(finalprice));
                        modal_newOrderItems.setQuantity(String.valueOf(quantity));
                        NewOrders_MenuItem_Fragment.cartItem_hashmap.put(itemUniquecode,modal_newOrderItems);
                        if(checkforBarcodeInCart("empty")) {
                          //  NewOrders_MenuItem_Fragment.cart_Item_List.remove(getAdapterPosition());

                            NewOrders_MenuItem_Fragment.cartItem_hashmap.remove("empty");
                        }
                        NewOrders_MenuItem_Fragment.adapter_cartItem_listview.notifyDataSetChanged();



                    }
                    else {
                        if(checkforBarcodeInCart("empty")) {
                          //  NewOrders_MenuItem_Fragment.cart_Item_List.remove(getAdapterPosition());

                            NewOrders_MenuItem_Fragment.cartItem_hashmap.remove("empty");
                        }

                        NewOrders_MenuItem_Fragment.cart_Item_List.add(itemUniquecode);
                        NewOrders_MenuItem_Fragment.cartItem_hashmap.put(itemUniquecode,newItem_newOrdersPojoClass);
                        NewOrders_MenuItem_Fragment.adapter_cartItem_listview.notifyDataSetChanged();

                    }
*/

            /*
        for(int i=0; i<NewOrders_MenuItem_Fragment.cart_Item_List.size();i++) {
            String oldItemBarcode = NewOrders_MenuItem_Fragment.cart_Item_List.get(i);
            if (!oldItemBarcode.equals(null)&&!itemUniquecode.equals(null)) {
                if(!itemUniquecode.equals("empty")) {
                    if (oldItemBarcode.equals(itemUniquecode)) {
                        Modal_NewOrderItems modal_newOrderItems = NewOrders_MenuItem_Fragment.cartItem_hashmap.get(itemUniquecode);
                        int quantity = Integer.parseInt(modal_newOrderItems.getQuantity());
                        quantity = quantity + 1;

                        int itemPrice_quantityBased = Integer.parseInt(modal_newOrderItems.getItemPrice_quantityBased());
                        int finalprice = quantity * itemPrice_quantityBased;
                        modal_newOrderItems.setItemFinalPrice(String.valueOf(finalprice));
                        modal_newOrderItems.setQuantity(String.valueOf(quantity));
                        NewOrders_MenuItem_Fragment.cartItem_hashmap.put(itemUniquecode,modal_newOrderItems);
                        NewOrders_MenuItem_Fragment.adapter_cartItem_listview.notifyDataSetChanged();

                    }
                    else{


                            NewOrders_MenuItem_Fragment.cart_Item_List.remove(i);
                            NewOrders_MenuItem_Fragment.cartItem_hashmap.remove("empty");
                            NewOrders_MenuItem_Fragment.adapter_cartItem_listview.notifyDataSetChanged();


                          NewOrders_MenuItem_Fragment.cart_Item_List.add(itemUniquecode);
                        NewOrders_MenuItem_Fragment.cartItem_hashmap.put(itemUniquecode,newItem_newOrdersPojoClass);

                    }
                }
                else{
                    Log.i(TAG,"Barcode is empty");

                }

            }
            else {
                Log.i(TAG,"Barcode cannot be Null");
            }

        }


 */





/*

            Log.e(TAG, "Got barcode addItemIntheCart"+isdataFetched);
            int last_ItemInCart = NewOrders_MenuItem_Fragment.cart_Item_List.size() - 1;
            Log.e(TAG, "barcode uniquecode last_ItemInCart" + last_ItemInCart);
            Log.e(TAG, "barcode uniquecode itemWeight" + itemWeight);
            Log.e(TAG, "barcode uniquecode getItemFinalPrice" + newItem_newOrdersPojoClass.getPricePerItem());
            Log.e(TAG, "barcode uniquecode getItemFinalWeight" + newItem_newOrdersPojoClass.getItemFinalWeight());

            Modal_NewOrderItems modal_newOrderItems = NewOrders_MenuItem_Fragment.cart_Item_List.get(last_ItemInCart);
            if (String.valueOf(modal_newOrderItems.getItemname()).equals("")) {
                Log.e(TAG, "barcode in if  " + modal_newOrderItems.getItemname());

                modal_newOrderItems.setItemname(newItem_newOrdersPojoClass.getItemname());
                modal_newOrderItems.setTmcpriceperkg(newItem_newOrdersPojoClass.getTmcpriceperkg());
                modal_newOrderItems.setGrossweight(newItem_newOrdersPojoClass.getGrossweight());
                modal_newOrderItems.setNetweight(newItem_newOrdersPojoClass.getNetweight());
                modal_newOrderItems.setTmcprice(newItem_newOrdersPojoClass.getTmcprice());
                modal_newOrderItems.setGstpercentage(newItem_newOrdersPojoClass.getGstpercentage());
                modal_newOrderItems.setPortionsize(newItem_newOrdersPojoClass.getPortionsize());
                modal_newOrderItems.setPricetypeforpos(newItem_newOrdersPojoClass.getPricetypeforpos());
                modal_newOrderItems.setPricePerItem(newItem_newOrdersPojoClass.getPricePerItem());
                modal_newOrderItems.setQuantity(newItem_newOrdersPojoClass.getQuantity());
                modal_newOrderItems.setItemuniquecode(newItem_newOrdersPojoClass.getItemuniquecode());
                modal_newOrderItems.setItemFinalWeight(newItem_newOrdersPojoClass.getItemFinalWeight());
                newItem_newOrdersPojoClass.setSubTotal_perItem(newItem_newOrdersPojoClass.getSubTotal_perItem());
                newItem_newOrdersPojoClass.setTotal_of_subTotal_perItem(newItem_newOrdersPojoClass.getTotal_of_subTotal_perItem());
                newItem_newOrdersPojoClass.setGstAmount(newItem_newOrdersPojoClass.getGstAmount());
                newItem_newOrdersPojoClass.setItemPrice_quantityBased(newItem_newOrdersPojoClass.getItemPrice_quantityBased());


                Log.e(TAG, "barcode in if before cart_Item_List.size()" + NewOrders_MenuItem_Fragment.cart_Item_List.size());

                // NewOrders_MenuItem_Fragment.cart_Item_List.add(last_ItemInCart,modal_newOrderItems);
                Log.e(TAG, "barcode in if after cart_Item_List.size()" + NewOrders_MenuItem_Fragment.cart_Item_List.size());
                //    newOrders_menuItem_fragment.add_amount_ForBillDetails();
                // cart_Item_List.add(last_ItemInCart,modal_newOrderItems);
                Log.i(TAG, "barcode in if after cart_Item_List.size()" + NewOrders_MenuItem_Fragment.cart_Item_List.size());
                Log.e(TAG, "Got barcode addItemIntheCart" +
                        "" +
                        ""+isdataFetched);
                NewOrders_MenuItem_Fragment.adapter_cartItem_listview.notifyDataSetChanged();

                Log.i(TAG, "barcode in if after cart_Item_List.size()" + NewOrders_MenuItem_Fragment.cart_Item_List.size());
                Log.i(TAG, "barcode in if after cart_Item_List.size()" + NewOrders_MenuItem_Fragment.cart_Item_List.size());



            }








                }






                private Handler newHandler() {
                    Handler.Callback callback = new Handler.Callback() {

                        @Override
                        public boolean handleMessage(Message msg) {
                            Bundle bundle = msg.getData();
                            String data = bundle.getString("dropdown");

                            if (String.valueOf(data).equalsIgnoreCase("dismissdropdown")) {
                                Log.e(TAG, "dismissDropdown");
                                Log.e(Constants.TAG, "createBillDetails in CartItem 0 ");

                                //sendHandlerMessage("addBillDetails");




                                holder.autoComplete_widget.clearFocus();

                                holder.autoComplete_widget.dismissDropDown();


                            }
                            return false;
                        }
                    };
                    return new Handler(callback);
                }


            private boolean checkforBarcodeInCart(String itemUniquecode) {
                String search = itemUniquecode;
                for(String str: NewOrders_MenuItem_Fragment.cart_Item_List) {
                    if(str.trim().contains(search))
                        return true;
                }
                return false;
            }


        });

            // Return the completed view to render on screen
            return convertView;
        }
    }
    */





    /*
      private final Context context;
    private String pricetype_of_pos, portionsize;
    Adapter_AutoCompleteMenuItem adapter;
    private int item_total = 0, unit_price_of_Item, removed_total_Amount = 0, removed_gst_Amount = 0;
    private int price_per_kg = 0, quantity_of_item, taxes_and_charges;
    List<Modal_NewOrderItems>selectedItem;
    List<Modal_NewOrderItems> Menulist;
    List<Modal_NewOrderItems> itemInCart;
    NewOrders_MenuItem_Fragment NewOrders_MenuItem_Fragment;


    public Adapter_CartItem_Listview(Context context, List<Modal_NewOrderItems> itemInCart, NewOrders_MenuItem_Fragment NewOrders_MenuItem_Fragment) {
        super(context, R.layout.neworders_cart_tem_listview, itemInCart);
        Menulist = new ArrayList<>();
        selectedItem = new ArrayList<>();
        Log.i(TAG,"Auto call adapter itemInCart itemInCart"+itemInCart.size());

        this.context = context;
        this.itemInCart = itemInCart;

        this.NewOrders_MenuItem_Fragment = NewOrders_MenuItem_Fragment;
    }


    @Override
    public int getCount() {
        return super.getCount();
    }

    @Nullable
    @Override
    public Modal_NewOrderItems getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public int getPosition(@Nullable Modal_NewOrderItems item) {
        return super.getPosition(item);
    }

    public View getView(final int pos, View view, ViewGroup v) {
        @SuppressLint("ViewHolder")// final View listViewItem = LayoutInflater.from(context).inflate(R.layout.neworders_cart_tem_listview, (ViewGroup) view, false);

                ViewHolder holder;
        if (view == null) {
            Log.d(TAG, "getView if: ");

            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.neworders_cart_tem_listview, null, true);

            holder.itemWeightEdit_widget = (EditText) view.findViewById(R.id.itemWeightEdit_widget);

            holder.autoCompleteTextView = (AutoCompleteTextView) view.findViewById(R.id.autoComplete_widget);

            holder.itemIndex = (TextView) view.findViewById(R.id.itemIndex);

            holder.itemPrice_Widget = (TextView) view.findViewById(R.id.itemPrice_Widget);

            holder.addNewItem_layout = (LinearLayout) view.findViewById(R.id.addNewItem_layout);

            holder.removeItem_fromCart_widget = (LinearLayout) view.findViewById(R.id.removeItem_fromCart_widget);
            holder.minus_to_remove_item_widget = (ImageView) view.findViewById(R.id.minus_to_remove_item_widget);

            Menulist = getvaluefromSharedPreferences("OrderDetailsFromSharedPreferences","orderDetails");




            view.setTag(holder);
        } else {
            Log.d(TAG, "getView: else ");

            Log.d(TAG, "Auto menu in cart adapter: in alse"+Menulist.size());
            Menulist = getvaluefromSharedPreferences("OrderDetailsFromSharedPreferences","orderDetails");



            // the getTag returns the viewHolder object set as a tag to the view
            holder = (ViewHolder) view.getTag();
        }

        Log.d(TAG, "getView: ");
        final Modal_NewOrderItems modalNewOrders_item = itemInCart.get(pos);
        holder.itemIndex.setText(String.valueOf(pos+1));

if(pos==(itemInCart.size()-1))
{
    holder.addNewItem_layout.setVisibility(View.VISIBLE);
}
else {
    holder.addNewItem_layout.setVisibility(View.GONE);

}
if(!String.valueOf(modalNewOrders_item.getItemname()).equals("")) {
    holder.autoCompleteTextView.setText(modalNewOrders_item.getItemname());
    holder.itemWeightEdit_widget.setText(modalNewOrders_item.getGrossweight());

    holder.itemPrice_Widget.setText(modalNewOrders_item.getTmcpriceperkg());

}
        pricetype_of_pos = String.valueOf(modalNewOrders_item.getPricetypeforpos());


        //    holder.itemNameTextview.setText(modalNewOrders_item.getItemname());
            if(pricetype_of_pos.equals("tmcprice")) {
                unit_price_of_Item =  Integer.parseInt(modalNewOrders_item.getTmcprice());

                holder.itemPrice_Widget.setText(String.valueOf(unit_price_of_Item + " Rs"));
                if(modalNewOrders_item.getGrossweight().equals("")&&modalNewOrders_item.getNetweight().equals("")){
                    Log.i(Constants.TAG,"getPortionsize "+(String.format(" %s", modalNewOrders_item.getPortionsize())));
                    //  NewOrders_MenuItem_Fragment.addBillDetailsFromAdapter(unit_price_of_Item,taxes_and_charges);

                    holder.itemWeightEdit_widget.setText(modalNewOrders_item.getPortionsize());
                }

                else if(modalNewOrders_item.getNetweight().equals("")) {
                    Log.i(Constants.TAG,"getGrossweight "+(String.format(" %s", modalNewOrders_item.getGrossweight())));

                    holder.itemWeightEdit_widget.setText(modalNewOrders_item.getGrossweight());
                    //  NewOrders_MenuItem_Fragment.addBillDetailsFromAdapter(unit_price_of_Item,taxes_and_charges);

                }
                else   if(modalNewOrders_item.getGrossweight().equals("")) {
                    Log.i(Constants.TAG,"getNetweight "+(String.format(" %s", modalNewOrders_item.getNetweight())));

                    holder.itemWeightEdit_widget.setText(modalNewOrders_item.getNetweight());
                    // NewOrders_MenuItem_Fragment.addBillDetailsFromAdapter(unit_price_of_Item,taxes_and_charges);

                }
                else {
                    Log.i(Constants.TAG,"getGrossweight "+(String.format(" %s", modalNewOrders_item.getGrossweight())));

                    holder.itemWeightEdit_widget.setText(modalNewOrders_item.getGrossweight());
                    //  NewOrders_MenuItem_Fragment.addBillDetailsFromAdapter(unit_price_of_Item,taxes_and_charges);

                }
            }
            else if(pricetype_of_pos.equals("tmcpriceperkg")){
                price_per_kg = Integer.parseInt(modalNewOrders_item.getTmcpriceperkg());
                Log.d("TAG", "Cart adapter pricetype_of_pos +" + pricetype_of_pos);
                Log.d("TAG", "Cart adapter price_per_kg +" + price_per_kg);
                holder.itemPrice_Widget.setText(String.valueOf(price_per_kg)+" RS");
                holder.itemWeightEdit_widget.setText(modalNewOrders_item.getGrossweight());



            }
            else{
            //    Toast.makeText(context,"this Ctgy does not have pricetype of pos value",Toast.LENGTH_LONG).show();
            }


            //  menuitem_priceperkg_label_widget.setText(String.format(" %s", modalNewOrders_item.getCategoryname()));


        holder.itemWeightEdit_widget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.itemWeightEdit_widget.requestFocus();

            }
        });
        holder.itemWeightEdit_widget.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                adapter.getFilter().filter(cs);
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

                Toast.makeText(context.getApplicationContext(),"before text change",Toast.LENGTH_LONG).show();
            }

            @Override
            public void afterTextChanged(Editable arg0) {
                Modal_NewOrderItems modal_newOrderItems = new Modal_NewOrderItems();
                Modal_NewOrderItems modal_newOrderItems2 =  NewOrders_MenuItem_Fragment.cart_Item_List.get(pos);

                modal_newOrderItems.setItemname(String.valueOf(modal_newOrderItems2.getItemname()));
                modal_newOrderItems.setTmcpriceperkg(String.valueOf(modal_newOrderItems2.getTmcpriceperkg()));
                modal_newOrderItems.setTmcprice(String.valueOf(modal_newOrderItems2.getTmcprice()));
                modal_newOrderItems.setGstpercentage(String.valueOf(modal_newOrderItems2.getGstpercentage()));
                modal_newOrderItems.setPortionsize(String.valueOf(modal_newOrderItems2.getPortionsize()));

                modal_newOrderItems.setNetweight(String.valueOf(modal_newOrderItems2.getNetweight()));
                modal_newOrderItems.setGrossweight(String.valueOf(arg0.toString()));
                modal_newOrderItems.setPricetypeforpos(String.valueOf(modal_newOrderItems2.getPricetypeforpos()));
                NewOrders_MenuItem_Fragment.cart_Item_List.set(pos,modal_newOrderItems);

                Toast.makeText(context.getApplicationContext(),"after text change   "+arg0.toString(),Toast.LENGTH_LONG).show();
            }
        });
        holder.addNewItem_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!holder.autoCompleteTextView.getText().toString().equals("") && !holder.itemWeightEdit_widget.getText().toString().equals("")){
                   // NewOrders_MenuItem_Fragment.createEmptyRowInListView();
                   // NewOrders_MenuItem_Fragment.calladapterforCartListview();

                }
                else{
                    Toast.makeText(context,"You have to fill this Item First",Toast.LENGTH_LONG).show();
                }

            }
        });
        holder.removeItem_fromCart_widget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(0==(itemInCart.size()-1)){
                   // NewOrders_MenuItem_Fragment.createEmptyRowInListView();
                  //  NewOrders_MenuItem_Fragment.calladapterforCartListview();

                }
                itemInCart.remove(pos);

                NewOrders_MenuItem_Fragment.listViewAdapter.notifyDataSetChanged();


                Log.i(Constants.TAG,"itemCart in Adapter Cart"+itemInCart.size());

            }
        });

        Log.d(TAG, "Auto menu in cart adapter 1 : "+Menulist.size());

        adapter = new Adapter_AutoCompleteMenuItem(context, Menulist,holder.autoCompleteTextView,holder.itemWeightEdit_widget,holder.itemPrice_Widget,NewOrders_MenuItem_Fragment);

        holder.autoCompleteTextView.setAdapter(adapter);



        return view;
    }


    private List<Modal_NewOrderItems> getvaluefromSharedPreferences(String sharedprefrencename,String stringName) {

        SharedPreferences sh
                = context.getSharedPreferences(sharedprefrencename,
                MODE_PRIVATE);

        Gson gson = new Gson();
        String json = sh.getString(stringName, "");
        Type type = new TypeToken<List<Modal_NewOrderItems>>() {}.getType();
        List<Modal_NewOrderItems> arrayList = gson.fromJson(json, type);
        if (json==null) {
            Toast.makeText(context,"First Turn on the OrderSyncing Button in settings",Toast.LENGTH_LONG).show();
        } else {

            Log.d(TAG, "Auto menu in  Menulist=arrayList;: in alse"+Menulist.size());

        }
        return arrayList;
    }

    private class ViewHolder {

        AutoCompleteTextView autoCompleteTextView;

        TextView itemIndex;

        TextView itemPrice_Widget;

        EditText itemWeightEdit_widget;

        ImageView minus_to_remove_item_widget;
        LinearLayout removeItem_fromCart_widget,addNewItem_layout;
    }


}







/*
public class Adapter_CartItem_Listview extends RecyclerView.Adapter<Adapter_CartItem_Listview.MyViewHolder> {
    private LayoutInflater inflater;

    private final Context context;
    private String pricetype_of_pos, portionsize;
    Adapter_AutoCompleteMenuItem adapter;
    private int item_total = 0, unit_price_of_Item, removed_total_Amount = 0, removed_gst_Amount = 0;
    private int price_per_kg = 0, quantity_of_item, taxes_and_charges;
    List<Modal_NewOrderItems>selectedItem;
    List<Modal_NewOrderItems> Menulist;
    List<Modal_NewOrderItems> itemInCart;
    NewOrders_MenuItem_Fragment NewOrders_MenuItem_Fragment;


    public Adapter_CartItem_Listview(Context context, List<Modal_NewOrderItems> itemInCart, NewOrders_MenuItem_Fragment NewOrders_MenuItem_Fragment) {
        Menulist = new ArrayList<>();
        selectedItem = new ArrayList<>();
        Log.i(TAG,"Auto call adapter itemInCart itemInCart"+itemInCart.size());
        inflater = LayoutInflater.from(context);

        this.context = context;
        this.itemInCart = itemInCart;

        this.NewOrders_MenuItem_Fragment = NewOrders_MenuItem_Fragment;
    }


    @Override
    public Adapter_CartItem_Listview.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.neworders_cart_tem_listview, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        Menulist = getvaluefromSharedPreferences("OrderDetailsFromSharedPreferences","orderDetails");

        return holder;
    }

    @Override
    public void onBindViewHolder(final Adapter_CartItem_Listview.MyViewHolder holder, final int pos) {
        final Modal_NewOrderItems modalNewOrders_item = itemInCart.get(pos);
        holder.itemIndex.setText(String.valueOf(pos+1));

        holder.itemIndex.setText(String.valueOf(pos+1));

        if(pos==(itemInCart.size()-1))
        {
            holder.addNewItem_layout.setVisibility(View.VISIBLE);
        }
        else {
            holder.addNewItem_layout.setVisibility(View.GONE);

        }
        if(!modalNewOrders_item.getItemname().equals("")) {
            holder.autoCompleteTextView.setText(modalNewOrders_item.getItemname());
            holder.itemWeightEdit_widget.setText(modalNewOrders_item.getGrossweight());

            holder.itemPrice_Widget.setText(modalNewOrders_item.getTmcpriceperkg());

        }
        pricetype_of_pos = modalNewOrders_item.getPricetypeforpos();


        //    holder.itemNameTextview.setText(modalNewOrders_item.getItemname());
        if(pricetype_of_pos.equals("tmcprice")) {
            unit_price_of_Item =  Integer.parseInt(modalNewOrders_item.getTmcprice());

            holder.itemPrice_Widget.setText(unit_price_of_Item + " Rs");
            if(modalNewOrders_item.getGrossweight().equals("")&&modalNewOrders_item.getNetweight().equals("")){
                Log.i(Constants.TAG,"getPortionsize "+(String.format(" %s", modalNewOrders_item.getPortionsize())));
                //  NewOrders_MenuItem_Fragment.addBillDetailsFromAdapter(unit_price_of_Item,taxes_and_charges);

                holder.itemWeightEdit_widget.setText(modalNewOrders_item.getPortionsize());
            }

            else if(modalNewOrders_item.getNetweight().equals("")) {
                Log.i(Constants.TAG,"getGrossweight "+(String.format(" %s", modalNewOrders_item.getGrossweight())));

                holder.itemWeightEdit_widget.setText(modalNewOrders_item.getGrossweight());
                //  NewOrders_MenuItem_Fragment.addBillDetailsFromAdapter(unit_price_of_Item,taxes_and_charges);

            }
            else   if(modalNewOrders_item.getGrossweight().equals("")) {
                Log.i(Constants.TAG,"getNetweight "+(String.format(" %s", modalNewOrders_item.getNetweight())));

                holder.itemWeightEdit_widget.setText(modalNewOrders_item.getNetweight());
                // NewOrders_MenuItem_Fragment.addBillDetailsFromAdapter(unit_price_of_Item,taxes_and_charges);

            }
            else {
                Log.i(Constants.TAG,"getGrossweight "+(String.format(" %s", modalNewOrders_item.getGrossweight())));

                holder.itemWeightEdit_widget.setText(modalNewOrders_item.getGrossweight());
                //  NewOrders_MenuItem_Fragment.addBillDetailsFromAdapter(unit_price_of_Item,taxes_and_charges);

            }
        }
        else if(pricetype_of_pos.equals("tmcpriceperkg")){
            price_per_kg = Integer.parseInt(modalNewOrders_item.getTmcpriceperkg());
            Log.d("TAG", "Cart adapter pricetype_of_pos +" + pricetype_of_pos);
            Log.d("TAG", "Cart adapter price_per_kg +" + price_per_kg);
            holder.itemPrice_Widget.setText(String.valueOf(price_per_kg)+" RS");
            holder.itemWeightEdit_widget.setText(modalNewOrders_item.getGrossweight());



        }
        else{
            //    Toast.makeText(context,"this Ctgy does not have pricetype of pos value",Toast.LENGTH_LONG).show();
        }


        //  menuitem_priceperkg_label_widget.setText(String.format(" %s", modalNewOrders_item.getCategoryname()));



        holder.addNewItem_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!holder.autoCompleteTextView.getText().toString().equals("") && !holder.itemWeightEdit_widget.getText().toString().equals("")){
                    NewOrders_MenuItem_Fragment.createEmptyRowInListView();
                    NewOrders_MenuItem_Fragment.calladapterforCartListview();

                }
                else{
                    Toast.makeText(context,"You have to fill this Item First",Toast.LENGTH_LONG).show();
                }

            }
        });
        holder.removeItem_fromCart_widget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(0==(itemInCart.size()-1)){
                    NewOrders_MenuItem_Fragment.createEmptyRowInListView();
                    NewOrders_MenuItem_Fragment.calladapterforCartListview();

                }
                itemInCart.remove(pos);

                NewOrders_MenuItem_Fragment.listViewAdapter.notifyDataSetChanged();


                Log.i(Constants.TAG,"itemCart in Adapter Cart"+itemInCart.size());

            }
        });

        Log.d(TAG, "Auto menu in cart adapter 1 : "+Menulist.size());

        adapter = new Adapter_AutoCompleteMenuItem(context, Menulist,holder.autoCompleteTextView,holder.itemWeightEdit_widget,holder.itemPrice_Widget,NewOrders_MenuItem_Fragment);

        holder.autoCompleteTextView.setAdapter(adapter);




        holder.itemWeightEdit_widget.setText(itemInCart.get(pos).getEditTextValue());
        Log.d("print","yes");

    }

    @Override
    public int getItemCount() {
        return itemInCart.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{


        AutoCompleteTextView autoCompleteTextView;

        TextView itemIndex;

        TextView itemPrice_Widget;

        EditText itemWeightEdit_widget;

        ImageView minus_to_remove_item_widget;
        LinearLayout removeItem_fromCart_widget,addNewItem_layout;


        public MyViewHolder(View view) {
            super(view);


          itemWeightEdit_widget = (EditText) view.findViewById(R.id.itemWeightEdit_widget);

            autoCompleteTextView = (AutoCompleteTextView) view.findViewById(R.id.autoComplete_widget);

            itemIndex = (TextView) view.findViewById(R.id.itemIndex);

         itemPrice_Widget = (TextView) view.findViewById(R.id.itemPrice_Widget);

            addNewItem_layout = (LinearLayout) view.findViewById(R.id.addNewItem_layout);

            removeItem_fromCart_widget = (LinearLayout) view.findViewById(R.id.removeItem_fromCart_widget);
            minus_to_remove_item_widget = (ImageView) view.findViewById(R.id.minus_to_remove_item_widget);

            itemWeightEdit_widget.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    itemInCart.get(getAdapterPosition()).setEditTextValue(itemWeightEdit_widget.getText().toString());
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });
            autoCompleteTextView.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    itemInCart.get(getAdapterPosition()).setEditTextValue(autoCompleteTextView.getText().toString());
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });

        }

    }


    private List<Modal_NewOrderItems> getvaluefromSharedPreferences(String sharedprefrencename,String stringName) {

        SharedPreferences sh
                = context.getSharedPreferences(sharedprefrencename,
                MODE_PRIVATE);

        Gson gson = new Gson();
        String json = sh.getString(stringName, "");
        Type type = new TypeToken<List<Modal_NewOrderItems>>() {}.getType();
        List<Modal_NewOrderItems> arrayList = gson.fromJson(json, type);
        if (json==null) {
            Toast.makeText(context,"First Turn on the OrderSyncing Button in settings",Toast.LENGTH_LONG).show();
        } else {

            Log.d(TAG, "Auto menu in  Menulist=arrayList;: in alse"+Menulist.size());

        }
        return arrayList;
    }

}
*/

