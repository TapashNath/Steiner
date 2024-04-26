package com.steiner.app.Fragment.Contact;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.steiner.app.Activitys.OtherDetailsActivity;
import com.steiner.app.DataBase.DBHandler;
import com.steiner.app.R;
import com.steiner.app.utils.Config;
import com.steiner.app.utils.MyApplication;
import com.steiner.app.utils.SPDataSave;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class ContactAdapter extends RecyclerView.Adapter implements Filterable {
    List<ContactModel> contactModelList;
    List<ContactModel> contactModelListFilter;
    AppCompatActivity activity;
    ContactOnClickListener contactOnClickListener;

    public ContactAdapter(List<ContactModel> contactModelList, AppCompatActivity activity, ContactOnClickListener contactOnClickListener) {
        this.contactModelList = contactModelList;
        this.contactModelListFilter = contactModelList;
        this.activity = activity;
        this.contactOnClickListener = contactOnClickListener;
    }

    @Override
    public int getItemViewType(int position) {
        switch (contactModelListFilter.get(position).getTYPE()) {
            case 1:
                return ContactModel.SENDER_MSG;
            case -1:
                return ContactModel.RECEIVER_MSG;
            case 2:
                return ContactModel.SENDER_IMAGE_MSG;
            case -2:
                return ContactModel.RECEIVER_IMAGE_MSG;
            case 3:
                return ContactModel.SENDER_WORK_MSG;
            case -3:
                return ContactModel.RECEIVER_WORK_MSG;
            case 4:
                return ContactModel.SENDER_REPLY_MSG;
            case -4:
                return ContactModel.RECEIVER_REPLY_MSG;
            case 22:
                return ContactModel.DATE;
            default:
                return 0;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case ContactModel.SENDER_MSG:
                View SENDER_TXT = LayoutInflater.from(parent.getContext()).inflate(R.layout.sender_txt_layout, parent, false);
                return new SenderMessage(SENDER_TXT);
            case ContactModel.RECEIVER_MSG:
                View RECEIVER_TXT = LayoutInflater.from(parent.getContext()).inflate(R.layout.receiver_layout, parent, false);
                return new ReceiverMessage(RECEIVER_TXT);
            case ContactModel.SENDER_WORK_MSG:
                View SENDER_WORK_MSG = LayoutInflater.from(parent.getContext()).inflate(R.layout.sender_work_txt_layout, parent, false);
                return new SenderWorkMessage(SENDER_WORK_MSG);
            case ContactModel.RECEIVER_WORK_MSG:
                View RECEIVER_WORK_MSG = LayoutInflater.from(parent.getContext()).inflate(R.layout.receiver_work_txt_layout, parent, false);
                return new SenderWorkMessage(RECEIVER_WORK_MSG);
            case ContactModel.SENDER_IMAGE_MSG:
                View SENDER_IMAGE_MSG = LayoutInflater.from(parent.getContext()).inflate(R.layout.sender_image_txt_layout, parent, false);
                return new SenderImageMessage(SENDER_IMAGE_MSG);
            case ContactModel.RECEIVER_IMAGE_MSG:
                View RECEIVER_IMAGE_MSG = LayoutInflater.from(parent.getContext()).inflate(R.layout.receiver_image_txt_layout, parent, false);
                return new ReceiverImageMessage(RECEIVER_IMAGE_MSG);

            case ContactModel.SENDER_REPLY_MSG:
                View SENDER_REPLY_MSG = LayoutInflater.from(parent.getContext()).inflate(R.layout.sender_reply_txt_layout, parent, false);
                return new SenderReplyMessage(SENDER_REPLY_MSG);
            case ContactModel.RECEIVER_REPLY_MSG:
                View RECEIVER_REPLY_MSG = LayoutInflater.from(parent.getContext()).inflate(R.layout.receiver_reply_txt_layout, parent, false);
                return new ReceiverReplyMessage(RECEIVER_REPLY_MSG);

            case ContactModel.DATE:
                View DATE = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_date_layout, parent, false);
                return new Date(DATE);
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (contactModelListFilter.get(position).getTYPE()) {
            case ContactModel.SENDER_MSG:
                String message = contactModelListFilter.get(position).getMessage();
                String time = contactModelListFilter.get(position).getTime();
                String show = contactModelListFilter.get(position).getShow();
                ((SenderMessage) holder).setSenderView(message, time, show);
                break;
            case ContactModel.RECEIVER_MSG:
                String r_message = contactModelListFilter.get(position).getMessage();
                String r_time = contactModelListFilter.get(position).getTime();
                String r_show = contactModelListFilter.get(position).getShow();
                ((ReceiverMessage) holder).setReceiverView(r_message, r_time, r_show);
                break;
            case ContactModel.SENDER_IMAGE_MSG:
                String imageId = contactModelListFilter.get(position).getItemId();
                String imageImg = contactModelListFilter.get(position).getImage();
                String imageTxt = contactModelListFilter.get(position).getMessage();
                String imageTime = contactModelListFilter.get(position).getTime();
                String imageShow = contactModelListFilter.get(position).getShow();
                ((SenderImageMessage) holder).setSenderImageView(imageId, imageImg, imageTxt, imageTime, imageShow);
                break;
            case ContactModel.RECEIVER_IMAGE_MSG:
                String r_imageId = contactModelListFilter.get(position).getItemId();
                String r_imageImg = contactModelListFilter.get(position).getImage();
                String r_imageTxt = contactModelListFilter.get(position).getMessage();
                String r_imageTime = contactModelListFilter.get(position).getTime();
                String r_imageShow = contactModelListFilter.get(position).getShow();
                ((ReceiverImageMessage) holder).setReceiverImageView(r_imageId, r_imageImg, r_imageTxt, r_imageTime, r_imageShow);
                break;
            case ContactModel.SENDER_WORK_MSG:
                String workId = contactModelListFilter.get(position).getItemId();
                String workImg = contactModelListFilter.get(position).getImage();
                String workTitle = contactModelListFilter.get(position).getTitle();
                String workDetails = contactModelListFilter.get(position).getDetails();
                String workTxt = contactModelListFilter.get(position).getMessage();
                String workTime = contactModelListFilter.get(position).getTime();
                String workShow = contactModelListFilter.get(position).getShow();
                ((SenderWorkMessage) holder).setSenderWorkView(workId, workImg, workTitle, workDetails, workTxt, workTime, workShow);
                break;
            case ContactModel.RECEIVER_WORK_MSG:
                String r_workId = contactModelListFilter.get(position).getItemId();
                String r_workImg = contactModelListFilter.get(position).getImage();
                String r_workTitle = contactModelListFilter.get(position).getTitle();
                String r_workDetails = contactModelListFilter.get(position).getDetails();
                String r_workTxt = contactModelListFilter.get(position).getMessage();
                String r_workTime = contactModelListFilter.get(position).getTime();
                String r_workShow = contactModelListFilter.get(position).getShow();
                ((ReceiverWorkMessage) holder).setReceiverWorkView(r_workId, r_workImg, r_workTitle, r_workDetails, r_workTxt, r_workTime, r_workShow);
                break;

            case ContactModel.SENDER_REPLY_MSG:
                String replyId = contactModelListFilter.get(position).getItemId();
                String replyTxt = contactModelListFilter.get(position).getMessage();
                String replyMsgId = contactModelListFilter.get(position).getReplyId();
                String replyTime = contactModelListFilter.get(position).getTime();
                String replyShow = contactModelListFilter.get(position).getShow();
                ((SenderReplyMessage) holder).setSenderReplyView(replyId, replyTxt, replyMsgId, replyTime, replyShow);
                break;
            case ContactModel.RECEIVER_REPLY_MSG:
                String r_replyId = contactModelListFilter.get(position).getItemId();
                String r_replyImg = contactModelListFilter.get(position).getImage();
                String r_replyTxt = contactModelListFilter.get(position).getMessage();
                String r_replyMsgId = contactModelListFilter.get(position).getReplyId();
                String r_replyTime = contactModelListFilter.get(position).getTime();
                String r_replyShow = contactModelListFilter.get(position).getShow();
                ((ReceiverReplyMessage) holder).setReceiverReplyView(r_replyId, r_replyImg, r_replyTxt, r_replyMsgId, r_replyTime, r_replyShow);
                break;

            case ContactModel.DATE:
                String date = contactModelListFilter.get(position).getDate();
                ((Date) holder).setDate(date);
                break;
            default:
        }
    }

    @Override
    public int getItemCount() {
        return contactModelListFilter.size();
    }

    private static class SenderMessage extends RecyclerView.ViewHolder {
        private TextView messageTXT;
        private TextView timeTXT;
        private ImageView showIMG;
        private SPDataSave spDataSave;

        public SenderMessage(@NonNull View itemView) {
            super(itemView);
            messageTXT = itemView.findViewById(R.id.message);
            timeTXT = itemView.findViewById(R.id.time);
            showIMG = itemView.findViewById(R.id.show);
            spDataSave = new SPDataSave();


        }

        private void setSenderView(String message, String time, String show) {
            messageTXT.setText(message);
            timeTXT.setText(time);

            switch (show) {
                case "1":
                    showIMG.setImageResource(R.drawable.ic_baseline_done_24);
                    break;
                case "2":
                    showIMG.setImageResource(R.drawable.ic_baseline_done_all_24);
                    break;
                case "3":
                    showIMG.setImageResource(R.drawable.ic_baseline_done_all_24);
                    showIMG.setColorFilter(ContextCompat.getColor(itemView.getContext(), R.color.ColorGreen));
                    break;
            }


        }


    }

    private static class SenderImageMessage extends RecyclerView.ViewHolder {
        private TextView message;
        private TextView time;
        private ImageView show;
        private ImageView itemImage;
        private SPDataSave spDataSave;

        public SenderImageMessage(@NonNull View itemView) {
            super(itemView);
            message = itemView.findViewById(R.id.message);
            time = itemView.findViewById(R.id.time);
            show = itemView.findViewById(R.id.show);
            itemImage = itemView.findViewById(R.id.itemImage);
            spDataSave = new SPDataSave();

        }

        private void setSenderImageView(String imageId, String imageImg, String imageTxt, String imageTime, String imageShow) {

            message.setText(imageTxt);
            time.setText(imageTime);
            Glide.with(MyApplication.context)
                    .load(imageImg)
                    .placeholder(R.drawable.placeholder)
                    .into(itemImage);

            switch (imageShow) {
                case "1":
                    show.setImageResource(R.drawable.ic_baseline_done_24);
                    break;
                case "2":
                    show.setImageResource(R.drawable.ic_baseline_done_all_24);
                    break;
                case "3":
                    show.setImageResource(R.drawable.ic_baseline_done_all_24);
                    show.setColorFilter(ContextCompat.getColor(itemView.getContext(), R.color.ColorGreen));
                    break;
            }

            itemView.setOnClickListener(v -> {
                Intent intent = new Intent(itemView.getContext(), OtherDetailsActivity.class);
                intent.putExtra("FRAG", "IMAGE_DETAILS");
                spDataSave.setStr("SELECT_IMAGE", "ID", imageId);
                itemView.getContext().startActivity(intent);
            });
        }
    }

    private static class SenderWorkMessage extends RecyclerView.ViewHolder {
        private final TextView message;
        private final TextView time;
        private final TextView title;
        private final TextView details;
        private final ImageView show;
        private final ImageView image;
        private final SPDataSave spDataSave;

        public SenderWorkMessage(@NonNull View itemView) {
            super(itemView);

            message = itemView.findViewById(R.id.message);
            time = itemView.findViewById(R.id.time);
            title = itemView.findViewById(R.id.title);
            details = itemView.findViewById(R.id.details);
            show = itemView.findViewById(R.id.show);
            image = itemView.findViewById(R.id.image);
            spDataSave = new SPDataSave();
        }

        private void setSenderWorkView(String workId, String workImg, String workTitle, String workDetails, String workTxt, String workTime, String workShow) {

            message.setText(workTxt);
            time.setText(workTime);
            title.setText(workTitle);
            details.setText(workDetails);
            Glide.with(MyApplication.context)
                    .load(workImg)
                    .placeholder(R.drawable.placeholder)
                    .into(image);


            switch (workShow) {
                case "1":
                    show.setImageResource(R.drawable.ic_baseline_done_24);
                    break;
                case "2":
                    show.setImageResource(R.drawable.ic_baseline_done_all_24);
                    break;
                case "3":
                    show.setImageResource(R.drawable.ic_baseline_done_all_24);
                    show.setColorFilter(ContextCompat.getColor(itemView.getContext(), R.color.ColorGreen));
                    break;
            }

            itemView.setOnClickListener(v -> {
//                spDataSave.setStr("WORK_CATEGORY", "CATEGORY", workId);
//                Intent intent = new Intent(itemView.getContext(), OtherDetailsActivity.class);
//                intent.putExtra("FRAG", "WORK_DETAILS");
//                itemView.getContext().startActivity(intent);
            });
        }
    }

    private static class ReceiverMessage extends RecyclerView.ViewHolder {
        private TextView messageTXT;
        private TextView timeTXT;
        private ImageView show;
        private SPDataSave spDataSave;

        public ReceiverMessage(@NonNull View itemView) {
            super(itemView);
            messageTXT = itemView.findViewById(R.id.message);
            timeTXT = itemView.findViewById(R.id.time);
            show = itemView.findViewById(R.id.show);
            spDataSave = new SPDataSave();
        }

        private void setReceiverView(String message, String time, String r_show) {
            messageTXT.setText(message);
            timeTXT.setText(time);

            switch (r_show) {
                case "1":
                    show.setImageResource(R.drawable.ic_baseline_done_24);
                    break;
                case "2":
                    show.setImageResource(R.drawable.ic_baseline_done_all_24);
                    break;
                case "3":
                    show.setImageResource(R.drawable.ic_baseline_done_all_24);
                    show.setColorFilter(ContextCompat.getColor(itemView.getContext(), R.color.ColorGreen));
                    break;
            }

        }

    }

    private static class ReceiverImageMessage extends RecyclerView.ViewHolder {
        private TextView message;
        private TextView time;
        private ImageView show;
        private ImageView itemImage;
        private SPDataSave spDataSave;

        public ReceiverImageMessage(@NonNull View itemView) {
            super(itemView);
            message = itemView.findViewById(R.id.message);
            time = itemView.findViewById(R.id.time);
            show = itemView.findViewById(R.id.show);
            itemImage = itemView.findViewById(R.id.itemImage);
            spDataSave = new SPDataSave();
        }

        private void setReceiverImageView(String r_imageId, String r_imageImg, String r_imageTxt, String r_imageTime, String r_imageShow) {

            message.setText(r_imageTxt);
            time.setText(r_imageTime);
            Glide.with(MyApplication.context)
                    .load(r_imageImg)
                    .placeholder(R.drawable.placeholder)
                    .into(itemImage);

            switch (r_imageShow) {
                case "1":
                    show.setImageResource(R.drawable.ic_baseline_done_24);
                    break;
                case "2":
                    show.setImageResource(R.drawable.ic_baseline_done_all_24);
                    break;
                case "3":
                    show.setImageResource(R.drawable.ic_baseline_done_all_24);
                    show.setColorFilter(ContextCompat.getColor(itemView.getContext(), R.color.ColorGreen));
                    break;
            }

            itemView.setOnClickListener(v -> {
                spDataSave.setStr("IMAGE_CATEGORY", "CATEGORY", r_imageId);
                Intent intent = new Intent(itemView.getContext(), OtherDetailsActivity.class);
                intent.putExtra("FRAG", "IMAGE_GALLERY");
                itemView.getContext().startActivity(intent);
            });
        }
    }

    private static class ReceiverWorkMessage extends RecyclerView.ViewHolder {
        private TextView message;
        private TextView time;
        private TextView title;
        private TextView details;
        private ImageView show;
        private ImageView image;
        private SPDataSave spDataSave;

        public ReceiverWorkMessage(@NonNull View itemView) {
            super(itemView);

            message = itemView.findViewById(R.id.message);
            time = itemView.findViewById(R.id.time);
            title = itemView.findViewById(R.id.title);
            details = itemView.findViewById(R.id.details);
            show = itemView.findViewById(R.id.show);
            image = itemView.findViewById(R.id.image);
            spDataSave = new SPDataSave();
        }

        private void setReceiverWorkView(String r_workId, String r_workImg, String r_workTitle, String r_workDetails, String r_workTxt, String r_workTime, String r_workShow) {

            message.setText(r_workTxt);
            time.setText(r_workTime);
            title.setText(r_workTitle);
            details.setText(r_workDetails);
            Glide.with(MyApplication.context)
                    .load(r_workImg)
                    .placeholder(R.drawable.placeholder)
                    .into(image);

            switch (r_workShow) {
                case "1":
                    show.setImageResource(R.drawable.ic_baseline_done_24);
                    break;
                case "2":
                    show.setImageResource(R.drawable.ic_baseline_done_all_24);
                    break;
                case "3":
                    show.setImageResource(R.drawable.ic_baseline_done_all_24);
                    show.setColorFilter(ContextCompat.getColor(itemView.getContext(), R.color.ColorGreen));
                    break;
            }

            itemView.setOnClickListener(v -> {
                spDataSave.setStr("WORK_CATEGORY", "CATEGORY", r_workId);
                Intent intent = new Intent(itemView.getContext(), OtherDetailsActivity.class);
                intent.putExtra("FRAG", "WORK_DETAILS");
                itemView.getContext().startActivity(intent);
            });
        }
    }

    private static class Date extends RecyclerView.ViewHolder {
        private TextView dateTXT;

        public Date(@NonNull View itemView) {
            super(itemView);
            dateTXT = itemView.findViewById(R.id.dateTXT);
        }

        private void setDate(String date) {
            dateTXT.setText(date);
        }
    }

    private static class SenderReplyMessage extends RecyclerView.ViewHolder {
        private TextView replyMsg;
        private TextView message;
        private TextView time;
        private ImageView show;
        private ImageView replyImage;
        private SPDataSave spDataSave;

        public SenderReplyMessage(View itemView) {
            super(itemView);
            replyMsg = itemView.findViewById(R.id.replyMsg);
            message = itemView.findViewById(R.id.message);
            time = itemView.findViewById(R.id.time);
            show = itemView.findViewById(R.id.show);
            replyImage = itemView.findViewById(R.id.replyImage);
            spDataSave = new SPDataSave();
        }

        private void setSenderReplyView(String replyId, String replyTxt, String replyMsgId, String replyTime, String replyShow) {
            DBHandler dbHandler = new DBHandler();

            String txt = null, img = null, id = null;
            try {
                JSONObject json = new JSONObject(dbHandler.getMessageDataWithId(replyMsgId).get(0).getMessage());
                txt = json.getString("txt");
                img = json.getString("img");
                id = json.getString("replyId");


            } catch (JSONException e) {
                e.printStackTrace();
                Config.Log(e.getMessage());
            }

            if (img.length() > 10) {
                replyImage.setVisibility(View.VISIBLE);
                Glide.with(MyApplication.context)
                        .load(img)
                        .placeholder(R.drawable.placeholder)
                        .into(replyImage);
            }else {
                replyImage.setVisibility(View.GONE);
            }
            replyMsg.setText(txt);
            message.setText(replyTxt);
            time.setText(replyTime);

            switch (replyShow) {
                case "1":
                    show.setImageResource(R.drawable.ic_baseline_done_24);
                    break;
                case "2":
                    show.setImageResource(R.drawable.ic_baseline_done_all_24);
                    break;
                case "3":
                    show.setImageResource(R.drawable.ic_baseline_done_all_24);
                    show.setColorFilter(ContextCompat.getColor(itemView.getContext(), R.color.ColorGreen));
                    break;
            }

            String finalId = id;
            itemView.setOnClickListener(v -> {
                Config.showLongToast(finalId);
            });

        }
    }

    private static class ReceiverReplyMessage extends RecyclerView.ViewHolder {
        private TextView replyMsg;
        private TextView message;
        private TextView time;
        private ImageView show;
        private ImageView image;
        private SPDataSave spDataSave;

        public ReceiverReplyMessage(View itemView) {
            super(itemView);
            replyMsg = itemView.findViewById(R.id.replyMsg);
            message = itemView.findViewById(R.id.message);
            time = itemView.findViewById(R.id.time);
            show = itemView.findViewById(R.id.show);
            image = itemView.findViewById(R.id.image);
            spDataSave = new SPDataSave();
        }

        private void setReceiverReplyView(String r_replyId, String r_replyImg, String r_replyTxt, String r_replyMsgId, String r_replyTime, String r_replyShow) {
            DBHandler dbHandler = new DBHandler();

            String txt = null, img = null, id = null;
            try {
                JSONObject json = new JSONObject(dbHandler.getMessageDataWithId(r_replyMsgId).get(0).getMessage());
                txt = json.getString("txt");
                img = json.getString("img");
                id = json.getString("id");

            } catch (JSONException e) {
                e.printStackTrace();
                Config.Log(e.getMessage());
            }
            if (id.length() > 0) {
                Glide.with(MyApplication.context)
                        .load(img)
                        .placeholder(R.drawable.placeholder)
                        .into(image);
            }

            replyMsg.setText(txt);
            message.setText(r_replyTxt);
            time.setText(r_replyTime);

            switch (r_replyShow) {
                case "1":
                    show.setImageResource(R.drawable.ic_baseline_done_24);
                    break;
                case "2":
                    show.setImageResource(R.drawable.ic_baseline_done_all_24);
                    break;
                case "3":
                    show.setImageResource(R.drawable.ic_baseline_done_all_24);
                    show.setColorFilter(ContextCompat.getColor(itemView.getContext(), R.color.ColorGreen));
                    break;
            }

            String finalId = id;
            itemView.setOnClickListener(v -> {
                Config.showLongToast(finalId);
            });

        }
    }

    @Override
    public Filter getFilter() {
        return null;
    }

}
