package irfandp.task4;

import android.app.AlertDialog;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class FragmentThree extends Fragment {

    Button btn_post,btn_get;
    View mView;
    DatabaseIncome myDB2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

//        Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_three, container, false);


        myDB2 = new DatabaseIncome(getActivity());

        btn_get = (Button) mView.findViewById(R.id.btn_get);
        btn_post = (Button) mView.findViewById(R.id.btn_post);
        btn_post.setOnClickListener(new View.OnClickListener() {
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

                Cursor incomes = myDB2.list_income();
                if (incomes.getCount() == 0) {
                    alert_message("Message", "No Data Income Found");
                    return;
                }

                final StringBuffer buffer2 = new StringBuffer();

                while (incomes.moveToNext()) {
                    // post data
                    User user_save = new User(incomes.getString(1),incomes.getString(2));
                    Call<User> call2 = user_api.saveUser(user_save);
                    call2.enqueue(new Callback<User>() {
                        @Override
                        public void onResponse(Call<User> call, Response<User> response) {
                            int status = response.code();
//                            buffer2.append(String.valueOf(status));
//                            buffer2.append(String.valueOf(response.body())+ System.getProperty("line.separator") + System.getProperty("line.separator"));
                            Toast.makeText(getActivity(), String.valueOf(status)+" - "+String.valueOf(response.body()), Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onFailure(Call<User> call, Throwable t) {
//                            buffer2.append(String.valueOf(t)+ System.getProperty("line.separator") + System.getProperty("line.separator"));
//                            tv_respond2.setText(String.valueOf(t));
                            Toast.makeText(getActivity(), String.valueOf(t), Toast.LENGTH_LONG).show();
                        }
                    });
                }
//                alert_message("Post Data Income", buffer2.toString());
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
