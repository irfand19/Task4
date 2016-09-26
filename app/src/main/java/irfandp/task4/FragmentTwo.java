package irfandp.task4;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class FragmentTwo extends Fragment implements View.OnClickListener {

    View mView;
    DatabaseExpenses myDB;
    EditText amount_out, des_out;
    Button add_out, list_out;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_two, container, false);

        myDB = new DatabaseExpenses(getActivity());
        des_out = (EditText) mView.findViewById(R.id.et_des_out);
        amount_out = (EditText) mView.findViewById(R.id.et_amount_out);
        add_out = (Button) mView.findViewById(R.id.btn_add_out);
        list_out = (Button) mView.findViewById(R.id.btn_list_out);
        add_out.setOnClickListener(this);
        list_out.setOnClickListener(this);

        return mView;

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_add_out :
                boolean result = myDB.save_expense(des_out.getText().toString(),
                        amount_out.getText().toString()
                );
                if (result)
                    Toast.makeText(getActivity(), "Success Add Expense", Toast.LENGTH_LONG).show();
                else
                    Toast.makeText(getActivity(), "Fails Add Expense", Toast.LENGTH_LONG).show();
                break;

            case R.id.btn_list_out :
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
                break;
        }
    }

    private void alert_message(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }
}
