package dev.tcc.caique.medreport.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.client.Firebase;
import com.firebase.ui.FirebaseRecyclerAdapter;

import de.hdodenhof.circleimageview.CircleImageView;
import dev.tcc.caique.medreport.R;
import dev.tcc.caique.medreport.models.ChatMessage;
import dev.tcc.caique.medreport.models.Singleton;
import dev.tcc.caique.medreport.utils.Constants;
import dev.tcc.caique.medreport.utils.Utils;

public class ChatActivity extends AppCompatActivity {
    private EditText textEdit;
    private Button sendButton;
    private Firebase mFirebase;
    private RecyclerView chatMessages;
    private FirebaseRecyclerAdapter<ChatMessage, ViewHolderChat> adapter;
    private Toolbar toolbar;
    private CircleImageView thunbnail;
    private TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        textEdit = (EditText) this.findViewById(R.id.text_edit);
        sendButton = (Button) this.findViewById(R.id.send_button);
        thunbnail = (CircleImageView) this.findViewById(R.id.thumbnail);
        title = (TextView) toolbar.findViewById(R.id.title);
        Bundle b = getIntent().getExtras();
        String chat = b.getString("SALA");
        String user = b.getString("USUARIO");
        String pic = b.getString("FOTO");
        if (pic != null) {
            Glide.with(this).load(pic).into(thunbnail);
        } else {
            thunbnail.setVisibility(View.GONE);
        }
        title.setText(user);
        //TODO: Procurar alternativa para o método abaixo
        //   getSupportActionBar().setIcon(new BitmapDrawable(pic));
        mFirebase = new Firebase(Constants.BASE_URL + "chat/" + chat);
        chatMessages = (RecyclerView) findViewById(R.id.chatMessages);
        chatMessages.setHasFixedSize(true);
        chatMessages.setLayoutManager(new LinearLayoutManager(this));
        adapter = new FirebaseRecyclerAdapter<ChatMessage, ViewHolderChat>(ChatMessage.class,
                android.R.layout.two_line_list_item,
                ViewHolderChat.class,
                mFirebase) {
            @Override
            protected void populateViewHolder(ViewHolderChat viewHolderChat, ChatMessage c, int i) {
                if (Singleton.getInstance().getName().equals(c.getName())) {
                    viewHolderChat.message.setLayoutParams(Utils.getChatUserMessageLayoutParams());
                }
                viewHolderChat.message.setText(c.getText());
            }
        };
        chatMessages.setAdapter(adapter);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = textEdit.getText().toString();
                ChatMessage message = new ChatMessage(Singleton.getInstance().getName(), text);
                mFirebase.push().setValue(message);
                textEdit.setText("");
            }
        });
    }

    public static class ViewHolderChat extends RecyclerView.ViewHolder {
        public TextView message;

        public ViewHolderChat(View v) {
            super(v);
            message = (TextView) v.findViewById(android.R.id.text2);
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        adapter.cleanup();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
