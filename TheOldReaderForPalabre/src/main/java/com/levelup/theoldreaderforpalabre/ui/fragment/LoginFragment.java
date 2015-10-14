package com.levelup.theoldreaderforpalabre.ui.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.levelup.theoldreaderforpalabre.LoginReceivedListener;
import com.levelup.theoldreaderforpalabre.R;
import com.levelup.theoldreaderforpalabre.SharedPreferenceKeys;
import com.levelup.theoldreaderforpalabre.TheOldReaderExtension;

/**
 * Created by nicolas on 18/06/15.
 */
public class LoginFragment extends Fragment {

    private View mainView;

    public static final LoginFragment newInstance() {
        return new LoginFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {

        mainView = getActivity().getLayoutInflater().inflate(R.layout.fragment_login, container, false);

        final EditText editLogin = (EditText) mainView.findViewById(R.id.edit_login);
        final EditText editPassword = (EditText) mainView.findViewById(R.id.edit_password);
        final Button signinButton = (Button) mainView.findViewById(R.id.button_login);

        signinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                editLogin.setEnabled(false);
                editPassword.setEnabled(false);

                final String username = editLogin.getText().toString().trim();
                String password = editPassword.getText().toString().trim();

                Ion.with(getActivity())
                        .load("https://theoldreader.com/accounts/ClientLogin")
                        .setBodyParameter("client", "The Old Reader For Palabre")
                        .setBodyParameter("accountType", "HOSTED_OR_GOOGLE")
                        .setBodyParameter("service", "reader")
                        .setBodyParameter("Email", username)
                        .setBodyParameter("Passwd", password)
                        .setBodyParameter("output", "json")
                        .asJsonObject()
                        .setCallback(new FutureCallback<JsonObject>() {
                            @Override
                            public void onCompleted(Exception e, JsonObject result) {
                                if (result != null) {
                                    Log.d("TOR", "Result login: " + result.get("Auth").getAsString());
                                    SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
                                    SharedPreferences.Editor editor = sharedPref.edit();
                                    editor.putString(SharedPreferenceKeys.AUTH, result.get("Auth").getAsString());
                                    editor.apply();

                                    TheOldReaderExtension.fetchCategories(getActivity(), result.get("Auth").getAsString(), new TheOldReaderExtension.OnCategoryAndSourceRefreshed() {
                                        @Override
                                        public void onFinished() {
                                            ((LoginReceivedListener) getActivity()).onLoginReceived();
                                            try {
                                                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("palabre://extauth"));
                                                startActivity(intent);
                                                getActivity().finish();
                                            } catch (Exception e1) {
                                                // palabre is not installed or old version
                                                Snackbar.make(signinButton, R.string.intent_error, Snackbar.LENGTH_LONG).show();
                                            }
                                        }

                                        @Override
                                        public void onFailure(Exception e) {

                                        }

                                        @Override
                                        public void onProgressChanged(int progress) {

                                        }
                                    });


                                } else {
                                    Snackbar.make(signinButton, R.string.login_error, Snackbar.LENGTH_LONG).show();
                                    editLogin.setEnabled(true);
                                    editPassword.setEnabled(true);
                                }
                            }
                        });



            }
        });

        return mainView;
    }
}
