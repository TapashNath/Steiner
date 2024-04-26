package com.steiner.app.Fragment.Contact;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;
import com.steiner.app.DataBase.DBHandler;
import com.steiner.app.R;
import com.steiner.app.Slider.SlideModel;
import com.steiner.app.VolleyService.IResult;
import com.steiner.app.VolleyService.VolleyService;
import com.steiner.app.databinding.FragmentContactBinding;
import com.steiner.app.utils.Config;
import com.steiner.app.utils.MyApplication;
import com.steiner.app.utils.Notification.NotificationInterface;
import com.steiner.app.utils.SPDataSave;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

import static com.steiner.app.utils.Content.NOTIFY_ACTIVITY_ACTION;


public class ContactFragment extends Fragment implements ContactOnClickListener, NotificationInterface {

    private AppCompatActivity activity;
    private SPDataSave spDataSave;
    private DBHandler dbHandler;


    private IResult mResultCallback = null;
    private VolleyService mVolleyService;

    private List<ContactModel> contactModelList;
    private ContactAdapter contactAdapter;

    private Random random;
    private String USER_ID;
    private String date;

    private int y;
    private String d;

    private FragmentContactBinding fragmentContactBinding;
    private AlertDialog.Builder builder1;
    private NotificationInterface notificationInterface;
    private FirebaseAuth mAuth;

    private BroadcastReceiver broadcastReciver;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragmentContactBinding = FragmentContactBinding.inflate(inflater, container, false);
        setHasOptionsMenu(true);
        activity = ((AppCompatActivity) getActivity());
        spDataSave = new SPDataSave();
        dbHandler = new DBHandler();
        random = new Random();
        Config.addToolbar(fragmentContactBinding.getRoot(), activity, fragmentContactBinding.ContactToolbar.getId());
        mAuth = FirebaseAuth.getInstance();
        USER_ID = mAuth.getCurrentUser().getEmail();
        builder1 = new AlertDialog.Builder(requireActivity());
        date = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        initVolleyCallback();
        notificationInterface = this;
        mVolleyService = new VolleyService(mResultCallback, getContext());

        LocalBroadcastManager manager = LocalBroadcastManager.getInstance(requireActivity());
        IntentFilter filter = new IntentFilter(NOTIFY_ACTIVITY_ACTION);
        manager.registerReceiver(broadcastReciver, filter);
        broadcastReciver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(NOTIFY_ACTIVITY_ACTION)) {
                    Config.Log("UPDATE");
                }
            }
        };


        fragmentContactBinding.refresh.setOnRefreshListener(() -> {
            spDataSave.setStr("MSG", "TYPE", "NORMAL");
            refreshMessage();

        });
        Config.Log(spDataSave.getStr("MSG", "TYPE"));
        refreshMessage();
        ShowMessage();
        return fragmentContactBinding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        requireContext().registerReceiver(mMessageReceiver, new IntentFilter("unique_name"));
    }

    @Override
    public void onPause() {
        super.onPause();
        requireContext().unregisterReceiver(mMessageReceiver);
    }

    private final BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("receiver", "Got message");
           refreshMessage();
        }
    };

    private void sendMsg() {
        String id = spDataSave.getStr("MSG", "ID");
        Map<String, String> map = new HashMap<>();
        String message = null;

        switch (spDataSave.getStr("MSG", "TYPE")) {
            case "NORMAL":
                map.put("type", "msg");
                map.put("txt", fragmentContactBinding.MessageTXT.getText().toString());
                map.put("img", "");
                map.put("replyId", "");
                map.put("user", "U");
                map.put("title", "");
                map.put("details", "");
                map.put("id", "");
                break;
            case "IMAGE":
                String link = Config.ImageUrl + dbHandler.getGalleryDataWithId(id).get(0).getImage();
                map.put("type", "imageMsg");
                map.put("txt", fragmentContactBinding.ItemMessageTXT.getText().toString());
                map.put("img", link);
                map.put("replyId", "");
                map.put("user", "U");
                map.put("title", "");
                map.put("details", "");
                map.put("id", id);
                break;
            case "WORK":
                List<SlideModel> slideModelList = new ArrayList<>();
                try {
                    String imageList = dbHandler.getItemsDataWithId(id).get(0).getImages();
                    String workLink = Config.ImageUrl;
                    JSONArray jsonArray = new JSONArray(imageList);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        slideModelList.add(new SlideModel(workLink + jsonArray.getString(i), "", ""));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                map.put("type", "workMsg");
                map.put("txt", fragmentContactBinding.WorkMessageTXT.getText().toString());
                map.put("img", slideModelList.get(0).getImage());
                map.put("replyId", "");
                map.put("user", "U");
                map.put("title", dbHandler.getItemsDataWithId(id).get(0).getTitle());
                map.put("details", dbHandler.getItemsDataWithId(id).get(0).getDetails());
                map.put("id", id);
                break;
        }

        Gson gson = new Gson();
        message = gson.toJson(map);
        String time = new SimpleDateFormat("h:mm:ss a", Locale.getDefault()).format(new Date());
        String show = "1";
        if (message != null) {
            HashMap<String, String> params = new HashMap<>();
            params.put("id", String.valueOf(new Random().nextInt(900000 + 1)));
            params.put("email", USER_ID);
            params.put("type", "U");
            params.put("message", message);
            params.put("time", time);
            params.put("date", date);
            params.put("show", show);
            mVolleyService.CallDataVolley("sendMsg", "Steiner/Message/addMessage.php", params);
        } else {
            Config.Log("null data pass");
        }


    }


    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
    private void ShowMessage() {
        contactModelList = new ArrayList<>();
        List<MsgModel> msgModels = new ArrayList<>();
        msgModels = dbHandler.getMessageData();

        Config.Log(String.valueOf(msgModels.size()));

        for (int i = 0; i < msgModels.size(); i++) {
            String id = msgModels.get(i).getId();
            String userId = msgModels.get(i).getUserId();
            String message = msgModels.get(i).getMessage();
            String time = msgModels.get(i).getTime();
            String date = msgModels.get(i).getDate();
            String show = msgModels.get(i).getShow();

            @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
            Date date1 = new Date();
            String today = dateFormat.format(date1);

            Date newDate = new Date(date1.getTime() - 24 * 60 * 60 * 1000); // 7 * 24 * 60 * 60 * 1000
            String yesterday = dateFormat.format(newDate);
            String img = null, title = null, details = null, itemId = null, txt = null, user = null, type = null, replyId = null;

            try {
                JSONObject json = new JSONObject(message);
                txt = json.getString("txt");
                type = json.getString("type");
                user = json.getString("user");


                if (!json.getString("img").isEmpty()) {
                    img = json.getString("img");
                }
                if (!json.getString("title").isEmpty()) {
                    title = json.getString("title");
                }
                if (!json.getString("details").isEmpty()) {
                    details = json.getString("details");
                }
                if (!json.getString("id").isEmpty()) {
                    itemId = json.getString("id");
                }
                if (!json.getString("replyId").isEmpty()) {
                    replyId = json.getString("replyId");
                }

                if (!date.equals(d)) {
                    d = date;
                    if (date.equals(today)) {
                        contactModelList.add(new ContactModel(22, id, "Today"));
                    } else if (date.equals(yesterday)) {
                        contactModelList.add(new ContactModel(22, id, "Yesterday"));
                    } else {
                        contactModelList.add(new ContactModel(22, id, date));
                    }

                }


                switch (type) {
                    case "msg":
                        if (user.equals("U")) {
                            contactModelList.add(new ContactModel(1, id, userId, txt, time, show));
                        } else {
                            contactModelList.add(new ContactModel(-1, id, userId, txt, time, show));
                        }
                        break;
                    case "imageMsg":
                        if (user.equals("U")) {
                            contactModelList.add(new ContactModel(2, id, userId, txt, itemId, replyId, img, time, show));
                        } else {
                            contactModelList.add(new ContactModel(-2, id, userId, txt, itemId, replyId, img, time, show));
                        }
                        break;
                    case "workMsg":
                        if (user.equals("U")) {
                            contactModelList.add(new ContactModel(3, id, userId, txt, itemId, replyId, img, title, details, time, show));
                        } else {
                            contactModelList.add(new ContactModel(-3, id, userId, txt, itemId, replyId, img, title, details, time, show));
                        }
                        break;
                    case "replyMsg":
                        if (user.equals("U")) {
                            contactModelList.add(new ContactModel(4, id, userId, txt, itemId, replyId, img, time, show));
                        } else {
                            contactModelList.add(new ContactModel(-4, id, userId, txt, itemId, replyId, img, time, show));
                        }
                        break;
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Config.Log(e.getMessage());
            }
        }

        LinearLayoutManager AllViewViewLayout = new LinearLayoutManager(getContext());
        AllViewViewLayout.setOrientation(RecyclerView.VERTICAL);
        fragmentContactBinding.ContactListView.setLayoutManager(AllViewViewLayout);

        contactAdapter = new ContactAdapter(contactModelList, activity, this);
        fragmentContactBinding.ContactListView.setAdapter(contactAdapter);
        contactAdapter.notifyDataSetChanged();
        fragmentContactBinding.ContactListView.scrollToPosition(contactAdapter.getItemCount() - 1);

        fragmentContactBinding.goDownBTN.setOnClickListener(v -> {
            fragmentContactBinding.goDownBTN.setVisibility(View.GONE);
            fragmentContactBinding.ContactListView.scrollToPosition(contactAdapter.getItemCount() - 1);
        });

        fragmentContactBinding.itemClose.setOnClickListener(v -> {
            spDataSave.setStr("MSG", "TYPE", "NORMAL");
            showMsgType();
        });
        fragmentContactBinding.workClose.setOnClickListener(v -> {
            spDataSave.setStr("MSG", "TYPE", "NORMAL");
            showMsgType();
        });
        fragmentContactBinding.replyClose.setOnClickListener(v -> {
            spDataSave.setStr("MSG", "TYPE", "NORMAL");
            showMsgType();
        });

        switch (spDataSave.getStr("MSG", "TYPE")) {
            case "REPLY":
                fragmentContactBinding.ReplySendBTN.setOnClickListener(v -> {
                    if (!TextUtils.isEmpty(fragmentContactBinding.ReplyMessageTXT.getText())) {
                        sendMsg();
                    } else {
                        Config.showLongToast("Please Type Something");
                    }

                });
                break;
            case "NORMAL":
                fragmentContactBinding.SendBTN.setOnClickListener(v -> {
                    if (!TextUtils.isEmpty(fragmentContactBinding.MessageTXT.getText())) {
                        sendMsg();
                    } else {
                        Config.showLongToast("Please Type Something");
                    }

                });
                break;
            case "IMAGE":
                fragmentContactBinding.ItemSendBTN.setOnClickListener(v -> {
                    if (!TextUtils.isEmpty(fragmentContactBinding.ItemMessageTXT.getText())) {
                        sendMsg();
                    } else {
                        Config.showLongToast("Please Type Something");
                    }

                });
                break;
            case "WORK":
                fragmentContactBinding.WorkSendBTN.setOnClickListener(v -> {
                    if (!TextUtils.isEmpty(fragmentContactBinding.WorkMessageTXT.getText())) {
                        sendMsg();
                    } else {
                        Config.showLongToast("Please Type Something");
                    }

                });
                break;
        }


        showMsgType();
    }

    private void showMsgType() {
        Config.Log(spDataSave.getStr("MSG", "TYPE"));
        switch (spDataSave.getStr("MSG", "TYPE")) {

            case "NORMAL":
                fragmentContactBinding.normalMsg.setVisibility(View.VISIBLE);
                fragmentContactBinding.itemMsg.setVisibility(View.GONE);
                fragmentContactBinding.workMsg.setVisibility(View.GONE);
                fragmentContactBinding.replyMsg.setVisibility(View.GONE);
                break;
            case "IMAGE":
                String id = spDataSave.getStr("MSG", "ID");
                String link = Config.ImageUrl + dbHandler.getGalleryDataWithId(id).get(0).getImage();
                Glide.with(MyApplication.context)
                        .load(link)
                        .placeholder(R.drawable.placeholder)
                        .into(fragmentContactBinding.itemImage);
                fragmentContactBinding.itemMsg.setVisibility(View.VISIBLE);
                fragmentContactBinding.normalMsg.setVisibility(View.GONE);
                fragmentContactBinding.workMsg.setVisibility(View.GONE);
                fragmentContactBinding.replyMsg.setVisibility(View.GONE);
                break;
            case "WORK":
                String workId = spDataSave.getStr("MSG", "ID");
                List<SlideModel> slideModelList = new ArrayList<>();
                try {
                    String imageList = dbHandler.getItemsDataWithId(workId).get(0).getImages();
                    String workLink = Config.ImageUrl + "Images/ItemsImage/";
                    JSONArray jsonArray = new JSONArray(imageList);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        slideModelList.add(new SlideModel(workLink + jsonArray.getString(i), "", ""));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Glide.with(MyApplication.context)
                        .load(slideModelList.get(0).getImage())
                        .placeholder(R.drawable.placeholder)
                        .into(fragmentContactBinding.workImage);
                fragmentContactBinding.title.setText(dbHandler.getItemsDataWithId(workId).get(0).getTitle());
                fragmentContactBinding.details.setText(dbHandler.getItemsDataWithId(workId).get(0).getDetails());
                fragmentContactBinding.workMsg.setVisibility(View.VISIBLE);
                fragmentContactBinding.normalMsg.setVisibility(View.GONE);
                fragmentContactBinding.itemMsg.setVisibility(View.GONE);
                fragmentContactBinding.replyMsg.setVisibility(View.GONE);
                break;
        }
    }

    public void refreshMessage() {
        HashMap<String, String> params = new HashMap<>();
        params.put("email", USER_ID);
        mVolleyService.CallDataVolley("refreshMessage", "Steiner/Message/refreshMessage.php", params);
    }

    private void initVolleyCallback() {
        mResultCallback = new IResult() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void notifySuccess(String requestType, String response) {
                if ("sendMsg".equals(requestType)) {
                    Config.Log(response);
                    spDataSave.setStr("MSG", "TYPE", "NORMAL");
                    showMsgType();
                    fragmentContactBinding.MessageTXT.getText().clear();
                    try {
                        JSONObject obj = new JSONObject(response);

                        String response_message = obj.getString("response_message");
                        switch (response_message) {
                            case "1":
                                dbHandler.clearMessageData();
                                JSONArray response_message_data = obj.getJSONArray("response_message_data");
                                for (int i = 0; i < response_message_data.length(); i++) {
                                    JSONObject jsonDataObject = response_message_data.getJSONObject(i);

                                    String id = jsonDataObject.getString("id");
                                    String userId = jsonDataObject.getString("email");
                                    String message = jsonDataObject.getString("message");
                                    String time = jsonDataObject.getString("time");
                                    String date = jsonDataObject.getString("date");
                                    String show = jsonDataObject.getString("show");

                                    dbHandler.addMessageData(new MsgModel(id, userId, message, time, date, show));
                                }
                                Config.Log("message data get complete");

                                break;
                            case "2":
                            case "3":
                            case "4":
                                dbHandler.clearMessageData();
                                Config.Log("message data get error");
                                break;

                        }
                        ShowMessage();
                    } catch (JSONException e) {
                        e.printStackTrace();
                        builder1.setTitle("Alert Message");
                        builder1.setMessage("Database error !");
                        builder1.setIcon(android.R.drawable.stat_notify_error);
                        builder1.setCancelable(false);
                        builder1.setPositiveButton(
                                "Try Again",
                                (dialog, id) -> {
                                    sendMsg();
                                });
                        builder1.setNegativeButton(
                                "No",
                                (dialog, id) -> dialog.cancel());
                        AlertDialog alert11 = builder1.create();
                        alert11.show();
                    }
                }
                if ("refreshMessage".equals(requestType)) {
                    fragmentContactBinding.refresh.setRefreshing(false);
                    try {
                        JSONObject obj = new JSONObject(response);
                        String response_message = obj.getString("response_message");
                        switch (response_message) {
                            case "1":
                                dbHandler.clearMessageData();
                                JSONArray response_message_data = obj.getJSONArray("response_message_data");
                                for (int i = 0; i < response_message_data.length(); i++) {
                                    JSONObject jsonDataObject = response_message_data.getJSONObject(i);

                                    String id = jsonDataObject.getString("id");
                                    String userId = jsonDataObject.getString("email");
                                    String message = jsonDataObject.getString("message");
                                    String time = jsonDataObject.getString("time");
                                    String date = jsonDataObject.getString("date");
                                    String show = jsonDataObject.getString("show");

                                    dbHandler.addMessageData(new MsgModel(id, userId, message, time, date, show));
                                }
                                Config.Log("message data get complete");
                                break;
                            case "2":
                                dbHandler.clearMessageData();
                                Config.Log("message data get error");
                                break;
                            case "3":
                                dbHandler.clearMessageData();
                                Config.Log("message data error");
                                break;
                        }
                        ShowMessage();
                    } catch (JSONException e) {
                        e.printStackTrace();
                        builder1.setTitle("Alert Message");
                        builder1.setMessage("Database error !");
                        builder1.setIcon(android.R.drawable.stat_notify_error);
                        builder1.setCancelable(false);
                        builder1.setPositiveButton(
                                "Try Again",
                                (dialog, id) -> {
                                    refreshMessage();
                                });
                        builder1.setNegativeButton(
                                "No",
                                (dialog, id) -> dialog.cancel());
                        AlertDialog alert11 = builder1.create();
                        alert11.show();
                    }
                }
            }

            @Override
            public void notifyError(String requestType, VolleyError error) {
                error.printStackTrace();
                builder1.setTitle("Alert Message");
                builder1.setMessage("Network error !");
                builder1.setCancelable(false);
                builder1.setPositiveButton(
                        "Try Again",
                        (dialog, id) -> {
                            if ("refreshMessage".equals(requestType)) {
                                refreshMessage();
                            } else if ("sendMsg".equals(requestType)) {
                                sendMsg();
                            }
                        });
                builder1.setNegativeButton(
                        "No",
                        (dialog, id) -> dialog.cancel());

                AlertDialog alert11 = builder1.create();
                alert11.show();
            }
        };
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            requireActivity().onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onItemAddRemove(String TYPE, String ID, int position) {

    }

    @Override
    public void Update(boolean IsUpdate) {
        if (IsUpdate) {
            Config.Log("UPDATE");
        }
    }
}