package irfandp.task4;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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

    Button btn_post, btn_get;
    View mView;
    DatabaseExpenses myDB;
    TextView tv_cursor, tv_respon;
    ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

//        Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_three, container, false);

        myDB = new DatabaseExpenses(getActivity());

        tv_cursor = (TextView) mView.findViewById(R.id.tv_cursor);
        tv_respon = (TextView) mView.findViewById(R.id.tv_response);
        btn_get = (Button) mView.findViewById(R.id.btn_get);
        btn_post = (Button) mView.findViewById(R.id.btn_post);

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

                final Cursor expenses = myDB.list_expense();
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
                while (expenses.moveToNext()) {
                    // post data
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

                            if (expenses.getPosition()==expenses.getCount()) {
                                if (progressDialog.isShowing()){
                                    progressDialog.dismiss();
                                }
                                Toast.makeText(getActivity(), "Data success synchronize to server", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<User> call, Throwable t) {

                            if (progressDialog.isShowing()) {
                                progressDialog.dismiss();
                            }

                            boolean update_temp = myDB.update_temp(expenses.getString(0),String.valueOf(expenses.getPosition()));

                            if (update_temp){
                                AlertDialog.Builder kk = new AlertDialog.Builder(getActivity());
                                kk.setTitle(String.valueOf(expenses.getPosition()))
                                        .setMessage("Fails Synchronize")
                                        .setCancelable(true)
                                        .setPositiveButton("Skip", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {

                                                dialog.cancel();
                                            }

                                        })
                                        .setNegativeButton("Retry", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int item) {

                                                dialog.cancel();
                                            }
                                        })
                                        .show();
                            } else {
                                Toast.makeText(getActivity(), "Fails update temporary data",
                                        Toast.LENGTH_LONG).show();
                            }

//                            buffer2.append(String.valueOf(t)+ System.getProperty("line.separator") + System.getProperty("line.separator"));
//                            Toast.makeText(getActivity(), String.valueOf(t), Toast.LENGTH_LONG).show();
                        }
                    });
//                    tv_cursor.setText(String.valueOf(expenses.getPosition()));
                }

            }
        });

        btn_get.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Gson gson = new GsonBuilder()
                        .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                        .create();
                Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl("http://10.0.2.2:3000")
                        .baseUrl("https://private-7ef5b-task43.apiary-mock.com/users/") //ipan
                        .addConverterFactory(GsonConverterFactory.create(gson))
                        .build();
                final UserApi user_api = retrofit.create(UserApi.class);

                // get data
                Call<Users> call = user_api.getUsers();
                call.enqueue(new Callback<Users>() {

                                 @Override
                                 public void onResponse(Call<Users> call, Response<Users> response) {

                                     StringBuffer buffer = new StringBuffer();

                                     int status = response.code();
                                     buffer.append(String.valueOf(status) + System.getProperty("line.separator") + System.getProperty("line.separator"));

                                     //this extract data from retrofit with for() loop
                                     for (Users.UserItem user : response.body().getUsers()) {

                                         buffer.append(
                                                 "Id = " + String.valueOf(user.getId()) +
                                                         System.getProperty("line.separator") +
                                                         "Description = " + user.getDescription() +
                                                         System.getProperty("line.separator") +
                                                         "Amount = " + user.getAmount() +
                                                         System.getProperty("line.separator") +
                                                         System.getProperty("line.separator")
                                         );
                                     }

                                     alert_message("List Expenses", buffer.toString());

                                 }

                                 @Override
                                 public void onFailure(Call<Users> call, Throwable t) {
                                     alert_message("List Expenses", String.valueOf(t));
//                                     Toast.makeText(getActivity(),String.valueOf(t),Toast.LENGTH_LONG).show();
                                 }
                             }
                );
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
