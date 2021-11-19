package com.example.whatsappclone.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.whatsappclone.Model.MessageModel;
import com.example.whatsappclone.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ChatAdapter extends RecyclerView.Adapter{
    ArrayList<MessageModel> messageModels;
    Context context;
    String recId;

    public ChatAdapter(ArrayList<MessageModel> messageModels, Context context, String recId) {
        this.messageModels = messageModels;
        this.context = context;
        this.recId = recId;
    }

    int SENDER_VIEW_TYPE=1;
    int RECEIVER_VIEW_TYPE=2;
    public ChatAdapter(ArrayList<MessageModel> messageModels1, Context context) {
        this.messageModels = messageModels1;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType==SENDER_VIEW_TYPE)
        {
            View view= LayoutInflater.from(context).inflate(R.layout.sample_sender,parent,false);
            return new SenderViewHolder(view);
        }
        else{
            View view= LayoutInflater.from(context).inflate(R.layout.sample_reciever,parent,false);
            return new ReceiverViewHolder(view);
        }

    }

    @Override
    public int getItemViewType(int position){
        if(messageModels.get(position).getuId().equals(FirebaseAuth.getInstance().getUid())){
            return SENDER_VIEW_TYPE;
        }
        else{
            return RECEIVER_VIEW_TYPE;
        }

    }
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
    MessageModel msgModel= messageModels.get(position);

    holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View view) {

            new AlertDialog.Builder(context)
                    .setTitle("Delete")
                    .setMessage("Are you sure")
                    .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            FirebaseDatabase database=FirebaseDatabase.getInstance();
                            String senderRoom=FirebaseAuth.getInstance().getUid()+recId;
                            database.getReference().child("chats").child(senderRoom)
                                    .child(msgModel.getMessageId())
                                    .setValue(null);
                        }
                    }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            }).show();

            return false;
        }
    });
    if(holder.getClass()==SenderViewHolder.class){
        ((SenderViewHolder)holder).senderMsg.setText(msgModel.getMessage());
    }
    else{
        ((ReceiverViewHolder)holder).receiverMsg.setText(msgModel.getMessage());
    }
    }

    @Override
    public int getItemCount() {
        return messageModels.size();
    }

    public class ReceiverViewHolder extends RecyclerView.ViewHolder{

        TextView receiverMsg, receiverTime;
        public ReceiverViewHolder(@NonNull View itemView) {

            super(itemView);
            receiverMsg=itemView.findViewById(R.id.receiverText);
            receiverTime=itemView.findViewById(R.id.receiveTime);

        }
    }
    public class SenderViewHolder extends RecyclerView.ViewHolder{

        TextView senderMsg,senderTime;
        public SenderViewHolder(@NonNull View itemView) {
            super(itemView);
            senderMsg=itemView.findViewById(R.id.senderText);
            senderTime=itemView.findViewById(R.id.senderTime);
        }
    }
}
