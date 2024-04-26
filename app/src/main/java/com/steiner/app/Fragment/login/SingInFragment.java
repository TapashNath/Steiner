package com.steiner.app.Fragment.login;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;

import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.messaging.FirebaseMessaging;
import com.steiner.app.DataBase.DBHandler;
import com.steiner.app.Models.UserModel;
import com.steiner.app.R;
import com.steiner.app.VolleyService.IResult;
import com.steiner.app.VolleyService.VolleyService;
import com.steiner.app.utils.Config;
import com.steiner.app.utils.ProgressDialogView;
import com.steiner.app.utils.SPDataSave;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Objects;

public class SingInFragment extends BottomSheetDialogFragment {
    private static final int RC_SIGN_IN = 123;

    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;
    private SPDataSave spDataSave;
    private ProgressDialogView progressDialogView;
    private IResult mResultCallback = null;
    private VolleyService mVolleyService;
    private DBHandler dbHandler;

    private static LoginInterface loginInterface;
    private AlertDialog.Builder builder;

    public static SingInFragment newInstance(LoginInterface i) {
        SingInFragment fragment = new SingInFragment();
        loginInterface = i;
        return fragment;
    }

    @Override
    public void setupDialog(Dialog dialog, int style) {
        View contentView = View.inflate(getContext(), R.layout.fragment_sing_in, null);
        dialog.setContentView(contentView);
        ((View) contentView.getParent()).setBackgroundColor(getResources().getColor(android.R.color.transparent));
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }


    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sing_in, container, false);

        spDataSave = new SPDataSave();
        progressDialogView = new ProgressDialogView(getContext());
        mAuth = FirebaseAuth.getInstance();
        initVolleyCallback();
        mVolleyService = new VolleyService(mResultCallback, getContext());
        dbHandler = new DBHandler();
        builder = new AlertDialog.Builder(requireContext());
        ImageView close = view.findViewById(R.id.close2);
        ImageView imageViw2 = view.findViewById(R.id.imageViw2);
        ImageView imageView2 = view.findViewById(R.id.imageView2);
        TextView textView = view.findViewById(R.id.textView);


        textView.setText("Welcome to " + getString(R.string.app_name));
        Glide.with(requireContext()).load(R.drawable.google).into(imageView2);

        GoogleLogin(view);
        close.setOnClickListener(v -> dismiss());


        return view;
    }


    private void GoogleLogin(View view) {
        LinearLayout googleBtn = view.findViewById(R.id.GoogleBtn);
        GoogleSignInOptions gso = new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(requireContext(), gso);

        googleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });
    }

    private void signIn() {
        progressDialogView.show();
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Config.Log("firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                Config.Log(e.getMessage());
                Config.showLongToast(getActivity(), "f " + e.getMessage());
                Config.Log(String.valueOf(e.getStatus()));
                progressDialogView.dismiss();
            }
        }
        Config.Log(String.valueOf(requestCode));

    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(requireActivity(), new OnCompleteListener<AuthResult>() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Config.Log("signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Config.Log(Objects.requireNonNull(task.getException()).getMessage());
                            updateUI(null);
                        }

                        // ...
                    }
                });
    }


    @SuppressLint("ApplySharedPref")
    private void updateUI(FirebaseUser user) {
        if (user != null) {
            String type = "user";
            String userName = Objects.requireNonNull(user.getDisplayName()).toUpperCase();
            String image = String.valueOf(user.getPhotoUrl());
            String email = user.getEmail();

            FirebaseMessaging.getInstance().getToken().addOnSuccessListener(token -> {
                if (!TextUtils.isEmpty(token)) {
                    Config.Log("retrieve token successful : " + token);
                    spDataSave.setStr("USER_DATA", "email", email);
                    dismiss();
                    progressDialogView.dismiss();
                    ChalkUserData(type,userName,image,email,token);


                } else {
                    Config.Log("token should not be null...");
                }
            }).addOnFailureListener(e -> {
                //handle e
            }).addOnCanceledListener(() -> {
                //handle cancel
            }).addOnCompleteListener(task -> Config.Log(""));


        }
    }

    private void ChalkUserData(String type, String userName, String image, String email, String token) {
        Log.d("LOGIN_ACTIVITY", "get user data");
        HashMap<String, String> params = new HashMap<>();
        params.put("type", type);
        params.put("userName", userName);
        params.put("image", image);
        params.put("email", email);
        params.put("token", token);
        mVolleyService.CallDataVolley("ChalkUserData", "Steiner/Login/ChalkUserData.php", params);
    }


    private void storeUserDataInLocalDatabase(String email) {
        spDataSave.setStr("USER_DATA", "email", email);
        loginInterface.Login(true);
    }

    private void initVolleyCallback() {
        mResultCallback = new IResult() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void notifySuccess(String requestType, String response) {

                if (requestType.equals("ChalkUserData")) {
                    Log.d("LOGIN_ACTIVITY", "gating user data complete ");
                    progressDialogView.dismiss();
                    try {
                        JSONObject obj = new JSONObject(response);
                        String response_user = obj.getString("response_user");
                        switch (response_user) {
                            case "2": {
                                dbHandler.clearUserData();
                                JSONArray response_user_data = obj.getJSONArray("response_user_data");
                                JSONObject jsonDataObject = response_user_data.getJSONObject(0);

                                String type = jsonDataObject.getString("type");
                                String username = jsonDataObject.getString("username");
                                String image = jsonDataObject.getString("image");
                                String email = jsonDataObject.getString("email");
                                String token = jsonDataObject.getString("token");



                                dbHandler.addUserData(new UserModel(type,username,image,email,token));
                                Log.d("LOGIN_ACTIVITY", "gating user data complete old user ");
                                Config.showLongToast("WellCome Back " + username);
                                storeUserDataInLocalDatabase(email);
                                loginInterface.Login(true);
                                break;
                            }
                            case "1": {
                                JSONArray response_user_data = obj.getJSONArray("response_user_data");
                                JSONObject jsonDataObject = response_user_data.getJSONObject(0);

                                String type = jsonDataObject.getString("type");
                                String username = jsonDataObject.getString("username");
                                String image = jsonDataObject.getString("image");
                                String email = jsonDataObject.getString("email");
                                String token = jsonDataObject.getString("token");



                                dbHandler.addUserData(new UserModel(type,username,image,email,token));
                                Config.showLongToast("WellCome " + username);
                                storeUserDataInLocalDatabase(email);
                                loginInterface.Login(true);
                                Log.d("LOGIN_ACTIVITY", "gating user data complete with add new ");
                                break;
                            }
                            case "3":
                                Log.d("LOGIN_ACTIVITY", "gating user data error with add new ");
                                dbHandler.clearUserData();
                                break;

                            case "4":{
                                Log.d("LOGIN_ACTIVITY", "gating user data error with add new ");
                                dbHandler.clearUserData();
                                break;
                            }

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        progressDialogView.dismiss();

                        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
                        builder.setCancelable(false);
                        builder.setMessage("Database Error!");
                        builder.setPositiveButton("Try Again", (dialog, which) -> {

                        });
                        builder.setNegativeButton("Exit", (dialog, which) -> {
                            requireActivity().finish();
                        });
                        AlertDialog alert = builder.create();
                        alert.show();
                    }
                }
            }


            @Override
            public void notifyError(String requestType, VolleyError error) {
                ///////Call Admin Data Start
                progressDialogView.dismiss();
                builder.setCancelable(false);
                builder.setTitle("Network Error!");
                builder.setMessage(error.getMessage());
                builder.setPositiveButton("Try Again", (dialog, which) -> {
                    if (requestType.equals("getDataFromServer")) {
                        Config.showLongToast("response");
                    }
                });
                builder.setNegativeButton("Exit", (dialog, which) -> {
                    requireActivity().finish();
                });
                AlertDialog alert = builder.create();
                alert.show();
                /////////Call Admin Data End
            }
        };
    }

//    private void signOut() {
//        GoogleSignInClient mGoogleSignInClient;
//        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                .requestIdToken(getString(R.string.default_web_client_id))
//                .requestEmail()
//                .build();
//        mGoogleSignInClient = GoogleSignIn.getClient(requireActivity(), gso);
//        mGoogleSignInClient.signOut().addOnCompleteListener(requireActivity(),
//                task -> {
//                    Log.d("APP_ACTIVITY", "sing out");
//                    FirebaseAuth.getInstance().signOut();
//                    boolean l = dbHandler.clearAll();
//                    if (l) {
//
//                    }
//                });
//    }

}