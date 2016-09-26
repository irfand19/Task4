package irfandp.task4;

import android.app.AlertDialog;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

public class FragmentOne extends Fragment {

    TextView total_out;
    View mView;
    DatabaseExpenses myDB;
    RecyclerView rv_products;
    RecyclerView.Adapter rv_adapter;
    RecyclerView.LayoutManager rv_layout_manager;
    Button list;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_one, container, false);

        myDB = new DatabaseExpenses(getActivity());
        total_out = (TextView) mView.findViewById(R.id.tv_total_out);
        rv_products = (RecyclerView) mView.findViewById(R.id.rv_products);
        rv_products.setHasFixedSize(true);
        rv_layout_manager = new LinearLayoutManager(getActivity());
        rv_products.setLayoutManager(rv_layout_manager);
        rv_adapter = new MyAdapter(loadDataExpense());
        rv_products.setAdapter(rv_adapter);
        calculateDataExpense();

        list = (Button) mView.findViewById(R.id.btn_liat);
        list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor expenses = myDB.list_expense();
                if (expenses.getCount() == 0) {
                    alert_message("Message", "No Data Expense Found");
                    return;
                }

                //append data student to buffer
                StringBuffer buffer = new StringBuffer();
                while (expenses.moveToNext()) {
                    buffer.append("Description : " + expenses.getString(1) + "\n");
                    buffer.append("Amount       : " + expenses.getString(2) + "\n\n");
                }
                //show data student
                alert_message("List Expenses", buffer.toString());
            }
        });

        // Inflate the layout for this fragment
        return mView;
    }

    private void calculateDataExpense() {
        int total_expense = 0;
        Cursor expenses = myDB.list_expense();

        while (expenses.moveToNext()) {
            total_expense = total_expense + Integer.parseInt(expenses.getString(2));
        }
        total_out.setText("TOTAL                                              $ "+total_expense);
    }

    private void alert_message(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }

    private String[] loadDataExpense() {
        Cursor expenses = myDB.list_expense();
        String[] products = new String[expenses.getCount()];

        while (expenses.moveToNext()) {
            products[expenses.getPosition()]=expenses.getString(1)+"\t\t"+expenses.getString(2);
        }
        return products;
    }

    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
        private String[] ds_products;
        public class ViewHolder extends RecyclerView.ViewHolder {
            public FrameLayout cv_products;
            public TextView tv_product;
            public ViewHolder(View v) {
                super(v);
                cv_products = (FrameLayout) v.findViewById(R.id.cv_products);
                tv_product = (TextView) v.findViewById(R.id.tv_product);
            }
        }

        public MyAdapter(String[] dataset) {
            ds_products = dataset;
        }

        @Override
        public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_product, parent, false);
            ViewHolder vh = new ViewHolder(v);
            return vh;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.tv_product.setText(ds_products[position]);
        }

        @Override
        public int getItemCount() {
            return ds_products.length;
        }
    }

}
