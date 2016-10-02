package irfandp.task4;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class FragmentThree extends Fragment {

    Button btn_post, btn_pend, btn_sent;
    View mView;
//    DatabaseExpenses myDB;
    DatabasePending myDB3;
    DatabaseSent myDB4;
    TextView tv_cursor, tv_respon;
    ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

//        Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_three, container, false);

//        myDB = new DatabaseExpenses(getActivity());
        myDB3 = new DatabasePending(getActivity());
        myDB4 = new DatabaseSent(getActivity());
        tv_cursor = (TextView) mView.findViewById(R.id.tv_cursor);
        tv_respon = (TextView) mView.findViewById(R.id.tv_response);
        btn_post = (Button) mView.findViewById(R.id.btn_post);
        btn_sent = (Button) mView.findViewById(R.id.btn_sent);
        btn_pend = (Button) mView.findViewById(R.id.btn_pend);

        btn_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                tv_respon.setText("");
                tv_cursor.setText("");

                Gson gson = new GsonBuilder()
                        .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                        .create();
                Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl("http://10.0.2.2:3000")
                        .baseUrl("https://private-7ef5b-task43.apiary-mock.com/users/") //ipan
                        .addConverterFactory(GsonConverterFactory.create(gson))
                        .build();
                final UserApi user_api = retrofit.create(UserApi.class);

                final Cursor expenses = myDB3.list_pending();
                if (expenses.getCount() == 0) {
                    alert_message("Message", "No Data Income Found");
                    return;
                }

                progressDialog = new ProgressDialog(getActivity());
                progressDialog.setTitle("Syncronize on Process");
                progressDialog.setMessage("Loading ...");
                progressDialog.setCancelable(true);
                progressDialog.setProgress(0);
                progressDialog.show();

                expenses.moveToFirst();
                User user_save = new User(expenses.getString(1), expenses.getString(2));
                Call<User> call2 = user_api.saveUser(user_save);
                call2.enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        int status = response.code();
//                            buffer2.append(String.valueOf(status));
//                            buffer2.append(String.valueOf(response.body())+ System.getProperty("line.separator") + System.getProperty("line.separator"));
//                            tv_cursor.setText(String.valueOf(expenses.getPosition()) + " - " + String.valueOf(status) + " - " + String.valueOf(response.body()));
//                            Toast.makeText(getActivity(), String.valueOf(expenses.getPosition()), Toast.LENGTH_SHORT).show();

                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }

                        boolean result2 = myDB4.save_sent(expenses.getString(1), expenses.getString(2));
                        if (result2)
                            tv_cursor.setText("Success Sent");
                        else
                            tv_cursor.setText("Failed Sent");

                        Integer del_result = myDB3.delete_pending(expenses.getString(expenses.getColumnIndex("ID")));
                        if (del_result > 0) {
                            tv_respon.setText("Success delete pending data");
                        }
                        else{
                            tv_respon.setText("Fails delete pending data");
                        }

                        Toast.makeText(getActivity(), "Data success synchronize to server", Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                            Toast.makeText(getActivity(), String.valueOf(t), Toast.LENGTH_LONG).show();
                        }
                    }
                });

            }
        });

        btn_pend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor pend = myDB3.list_pending();
                if (pend.getCount() == 0) {
                    alert_message("Message", "No Data Pending");
                    return;
                }

                //append data student to buffer
                StringBuffer buffer = new StringBuffer();
                while (pend.moveToNext()) {
                    buffer.append("Description : " + pend.getString(1) + "\n");
                    buffer.append("Amount       : " + pend.getString(2) + "\n\n");
                }
                //show data student
                alert_message("List Pending", buffer.toString());
            }
        });

        btn_sent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor send = myDB4.list_sent();
                if (send.getCount() == 0) {
                    alert_message("Message", "No Data Sent");
                    return;
                }

                //append data student to buffer
                StringBuffer buffer = new StringBuffer();
                while (send.moveToNext()) {
                    buffer.append("Description : " + send.getString(1) + "\n");
                    buffer.append("Amount       : " + send.getString(2) + "\n\n");
                }
                //show data student
                alert_message("List Sent", buffer.toString());
            }
        });

        return mView;
    }

    private void alert_message(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }
}
