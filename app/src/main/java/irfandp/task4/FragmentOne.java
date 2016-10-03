package irfandp.task4;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

public class FragmentOne extends Fragment {

    TextView total_out, total_in, total_bal;
    View mView;
    DatabaseExpenses myDB;
    DatabaseIncome myDB2;
    DatabasePending myDB3;
    DatabaseEdit myDB5;
    RecyclerView rv_products, rv_products2;
    RecyclerView.Adapter rv_adapter, rv_adapter2;
    RecyclerView.LayoutManager rv_layout_manager, rv_layout_manager2;
//    Button list, list2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_one, container, false);

        myDB = new DatabaseExpenses(getActivity());
        myDB2 = new DatabaseIncome(getActivity());
        myDB3 = new DatabasePending(getActivity());
        myDB5 = new DatabaseEdit(getActivity());
        total_out = (TextView) mView.findViewById(R.id.tv_total_out);
        total_in = (TextView) mView.findViewById(R.id.tv_total_in);
        total_bal = (TextView) mView.findViewById(R.id.tv_balance);

        rv_products = (RecyclerView) mView.findViewById(R.id.rv_products);
        rv_products.setHasFixedSize(true);
        rv_layout_manager = new LinearLayoutManager(getActivity());
        rv_products.setLayoutManager(rv_layout_manager);
        rv_adapter = new MyAdapter(loadDataExpense(), 1);
        rv_products.setAdapter(rv_adapter);

        rv_products2 = (RecyclerView) mView.findViewById(R.id.rv_products2);
        rv_products2.setHasFixedSize(true);
        rv_layout_manager2 = new LinearLayoutManager(getActivity());
        rv_products2.setLayoutManager(rv_layout_manager2);
        rv_adapter2 = new MyAdapter(loadDataIncome(), 2);
        rv_products2.setAdapter(rv_adapter2);

        total_out.setText("TOTAL                                              $ " + calculateDataExpense());
        total_in.setText("TOTAL                                              $ " + calculateDataIncome());
        total_bal.setText(" BALANCE                                        $ " + calculateBalance(calculateDataIncome(), calculateDataExpense()));

        // Inflate the layout for this fragment
        return mView;
    }

    private int calculateBalance(int in, int out) {
        int balance = 0;
        balance = in - out;
        return balance;
    }

    private int calculateDataIncome() {
        int total_income = 0;
        Cursor incomes = myDB2.list_income();

        while (incomes.moveToNext()) {
            total_income = total_income + Integer.parseInt(incomes.getString(2));
        }
        return total_income;
    }

    private String[] loadDataIncome() {
        Cursor incomes = myDB2.list_income();
        String[] products2 = new String[incomes.getCount()];

        while (incomes.moveToNext()) {
            products2[incomes.getPosition()] = incomes.getString(1) + "\t\t\t\t\t\t\t\t\t $ " + incomes.getString(2);
        }
        return products2;
    }

    private int calculateDataExpense() {
        int total_expense = 0;
        Cursor expenses = myDB.list_expense();

        while (expenses.moveToNext()) {
            total_expense = total_expense + Integer.parseInt(expenses.getString(2));
        }
        return total_expense;
    }

    private String[] loadDataExpense() {
        Cursor expenses = myDB.list_expense();
        String[] products = new String[expenses.getCount()];

        while (expenses.moveToNext()) {
            products[expenses.getPosition()] = expenses.getString(1) + "\t\t\t\t\t\t\t\t\t $ " + expenses.getString(2);
        }
        return products;
    }

    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
        private String[] ds_products;
        private int DB;

        public class ViewHolder extends RecyclerView.ViewHolder {
            public CardView cv_products;
            public TextView tv_product;

            public ViewHolder(View v) {
                super(v);
                cv_products = (CardView) v.findViewById(R.id.cv_products);
                tv_product = (TextView) v.findViewById(R.id.tv_product);
            }
        }

        public MyAdapter(String[] dataset, int datadb) {
            ds_products = dataset;
            DB = datadb;
        }

        @Override
        public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_product, parent, false);
            ViewHolder vh = new ViewHolder(v);
            return vh;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {
            holder.tv_product.setText(ds_products[position]);

            holder.tv_product.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    Toast.makeText(getActivity(), "Recycle Click " + position, Toast.LENGTH_SHORT).show();

                    if (DB == 1) {
                        final Cursor data = myDB.list_expense();
                        data.moveToPosition(position);

                        //                    costum dialog
                        final Dialog dialog = new Dialog(getActivity());
                        dialog.setContentView(R.layout.dialog_out);
                        dialog.setTitle("Update or Delete");

                        final EditText out = (EditText) dialog.findViewById(R.id.et_out);
                        out.setText(data.getString(data.getColumnIndex("DESCRIPTION")));

                        final EditText out_update = (EditText) dialog.findViewById(R.id.et_out_update);
                        out_update.setText(data.getString(data.getColumnIndex("AMOUNT")));

                        Button update = (Button) dialog.findViewById(R.id.btn_updt_out);
                        update.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                boolean update_result = myDB.update_expense(data.getString(data.getColumnIndex("ID")),
                                        out.getText().toString(),
                                        out_update.getText().toString());
                                if (update_result) {

                                    boolean result = myDB5.save_edit(out.getText().toString(), out_update.getText().toString(),"Update");

                                    dialog.cancel();
                                    FragmentManager fm = getFragmentManager();
                                    fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                                    FragmentTransaction ft = fm.beginTransaction();
                                    ft.replace(R.id.fragment_place, new FragmentOne());
                                    ft.commit();
                                } else {
                                    Toast.makeText(getActivity(), "Fails update data expense",
                                            Toast.LENGTH_LONG).show();
                                }
                            }
                        });

                        Button delete = (Button) dialog.findViewById(R.id.btn_del_out);
                        delete.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                boolean result = myDB5.save_edit(out.getText().toString(), out_update.getText().toString(),"Delete");
                                Integer del_result = myDB.delete_expense(data.getString(data.getColumnIndex("ID")));
                                if (del_result > 0) {
                                    dialog.cancel();
                                    FragmentManager fm = getFragmentManager();
                                    fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                                    FragmentTransaction ft = fm.beginTransaction();
                                    ft.replace(R.id.fragment_place, new FragmentOne());
                                    ft.commit();
                                }
                                else{
                                    Toast.makeText(getActivity(), "Fails delete data expense", Toast.LENGTH_LONG).show();
                                }
                            }
                        });

                        dialog.show();

                    } else {
                        final Cursor data = myDB2.list_income();
                        data.moveToPosition(position);
                        //                    costum dialog
                        final Dialog dialog = new Dialog(getActivity());
                        dialog.setContentView(R.layout.dialog_out);
                        dialog.setTitle("Update or Delete");

                        final EditText out = (EditText) dialog.findViewById(R.id.et_out);
                        String des_ins = data.getString(data.getColumnIndex("DESCRIPTION"));
                        out.setText(des_ins);

                        final EditText out_update = (EditText) dialog.findViewById(R.id.et_out_update);
                        String des_amos = data.getString(data.getColumnIndex("AMOUNT"));
                        out_update.setText(des_amos);

                        Button update = (Button) dialog.findViewById(R.id.btn_updt_out);
                        update.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                boolean update_result = myDB2.update_income(data.getString(data.getColumnIndex("ID")),
                                        out.getText().toString(),
                                        out_update.getText().toString());
                                if (update_result) {
                                    dialog.cancel();
                                    FragmentManager fm = getFragmentManager();
                                    fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                                    FragmentTransaction ft = fm.beginTransaction();
                                    ft.replace(R.id.fragment_place, new FragmentOne());
                                    ft.commit();
                                } else {
                                    Toast.makeText(getActivity(), "Fails update data income",
                                            Toast.LENGTH_LONG).show();
                                }
                            }
                        });

                        Button delete = (Button) dialog.findViewById(R.id.btn_del_out);
                        delete.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Integer del_result = myDB2.delete_income(data.getString(data.getColumnIndex("ID")));
                                if (del_result > 0){
                                    dialog.cancel();
                                    FragmentManager fm = getFragmentManager();
                                    fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                                    FragmentTransaction ft = fm.beginTransaction();
                                    ft.replace(R.id.fragment_place, new FragmentOne());
                                    ft.commit();
                                }
                                else
                                    Toast.makeText(getActivity(), "Fails delete data income", Toast.LENGTH_LONG).show();
                            }
                        });

                        dialog.show();
                    }

                }
            });
        }

        @Override
        public int getItemCount() {
            return ds_products.length;
        }
    }

}
