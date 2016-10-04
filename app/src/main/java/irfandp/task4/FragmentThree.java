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

    Button btn_post, btn_pend, btn_edit;
    View mView;
    DatabasePending myDB3;
    DatabaseEdit myDB4;
    TextView tv_cursor, tv_respon;
    ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

//        Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_three, container, false);

        myDB3 = new DatabasePending(getActivity());
        myDB4 = new DatabaseEdit(getActivity());
        tv_cursor = (TextView) mView.findViewById(R.id.tv_cursor);
        tv_respon = (TextView) mView.findViewById(R.id.tv_response);
        btn_post = (Button) mView.findViewById(R.id.btn_post);
        btn_pend = (Button) mView.findViewById(R.id.btn_pend);
        btn_edit = (Button) mView.findViewById(R.id.btn_edit);

        btn_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                tv_respon.setText("");
                tv_cursor.setText("");

                final Cursor pending_data = myDB3.list_pending();
                final Cursor edit_data = myDB4.list_edit();
                if (pending_data.getCount() == 0 && edit_data.getCount() == 0) {
                    alert_message("Message", "No Data To Synchronize");
                    return;
                }

                Gson gson = new GsonBuilder()
                        .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                        .create();
                Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl("http://10.0.2.2:3000")
                        .baseUrl("https://private-7ef5b-task43.apiary-mock.com/users/") //ipan
                        .addConverterFactory(GsonConverterFactory.create(gson))
                        .build();
                final UserApi user_api = retrofit.create(UserApi.class);

//                progressDialog = new ProgressDialog(getActivity());
//                progressDialog.setTitle("Syncronize on Process");
//                progressDialog.setMessage("Loading ...");
//                progressDialog.setCancelable(true);
//                progressDialog.setProgress(0);
//                progressDialog.show();

                if (pending_data.getCount() != 0) {
                    // post data
                    pending_data.moveToFirst();
                    User user_save = new User(pending_data.getString(1), pending_data.getString(2));
                    Call<User> call2 = user_api.saveUser(user_save);
                    call2.enqueue(new Callback<User>() {
                        @Override
                        public void onResponse(Call<User> call, Response<User> response) {
//                            int status = response.code();
//                            tv_cursor.setText(String.valueOf(expenses.getPosition()) + " - " + String.valueOf(status) + " - " + String.valueOf(response.body()));
//                            Toast.makeText(getActivity(), String.valueOf(expenses.getPosition()), Toast.LENGTH_SHORT).show();
                            tv_cursor.setText("Success post data");
                            Integer del_result = myDB3.delete_pending(pending_data.getString(pending_data.getColumnIndex("ID")));
                            Toast.makeText(getActivity(), "Data success synchronize to server", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailure(Call<User> call, Throwable t) {
//                            if (progressDialog.isShowing()) {
//                                progressDialog.dismiss();
//                            }
                            Toast.makeText(getActivity(), String.valueOf(t), Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                if (edit_data.getCount() != 0) {
                    edit_data.moveToFirst();
                    if (edit_data.getString(3).equals("Delete")) {
                        // delete data
                        Call<User> call_del = user_api.deleteUser(edit_data.getString(0));
                        call_del.enqueue(new Callback<User>() {
                            @Override
                            public void onResponse(Call<User> call, Response<User> response) {
                                tv_respon.setText("Success delete data");
                                Integer del_result = myDB4.delete_edit(edit_data.getString(edit_data.getColumnIndex("ID")));
                                Toast.makeText(getActivity(), "Data success synchronize to server", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onFailure(Call<User> call, Throwable t) {
//                                if (progressDialog.isShowing()) {
//                                    progressDialog.dismiss();
//                                }
                                Toast.makeText(getActivity(), String.valueOf(t), Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        // update data
                        Call<User> call_updt = user_api.updateUser(Integer.parseInt(edit_data.getString(0)), new User(edit_data.getString(1), edit_data.getString(2)));
                        call_updt.enqueue(new Callback<User>() {
                            @Override
                            public void onResponse(Call<User> call, Response<User> response) {
                                tv_respon.setText("Success update data");
                                Integer del_result = myDB4.delete_edit(edit_data.getString(edit_data.getColumnIndex("ID")));
                                Toast.makeText(getActivity(), "Data success synchronize to server", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onFailure(Call<User> call, Throwable t) {
//                                if (progressDialog.isShowing()) {
//                                    progressDialog.dismiss();
//                                }
                                Toast.makeText(getActivity(), String.valueOf(t), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }

//                if (progressDialog.isShowing()) {
//                    progressDialog.dismiss();
//                }
//                Toast.makeText(getActivity(), "Data success synchronize to server", Toast.LENGTH_SHORT).show();

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

        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor edit = myDB4.list_edit();
                if (edit.getCount() == 0) {
                    alert_message("Message", "No Edit");
                    return;
                }

                //append data student to buffer
                StringBuffer buffer = new StringBuffer();
                while (edit.moveToNext()) {
                    buffer.append("Description : " + edit.getString(1) + "\n");
                    buffer.append("Amount       : " + edit.getString(2) + "\n");
                    buffer.append("Type             : " + edit.getString(3) + "\n\n");
                }
                //show data student
                alert_message("List Edit", buffer.toString());
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
