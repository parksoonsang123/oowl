package com.ooowl.oowl;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EditDialog extends Dialog {

    private Context context;


    public EditText nick;

    public TextView OK;
    public TextView NO;
    String uid;
    public EditDialog(@NonNull Context context, String uid) {
        super(context);
        this.context = context;
        this.uid = uid;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_edit_dialog);

        final FirebaseAuth mAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = mAuth.getCurrentUser();
        uid = user.getUid();

        nick = findViewById(R.id.nick2);
        OK = findViewById(R.id.edit_ok_text);
        NO = findViewById(R.id.edit_no_text);

        OK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String newnickname = nick.getText().toString();
                if(newnickname.equals("") || newnickname == null){
                    Toast.makeText(context, "변경할 닉네임을 입력해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }

                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        int flag = -1;
                        for(DataSnapshot snapshot1 : snapshot.getChildren()){
                            UsersItem item = snapshot1.getValue(UsersItem.class);
                            if(item.getNickname().equals(newnickname)){
                                flag = 1;
                                break;
                            }
                        }

                        if(flag == 1){
                            Toast.makeText(context, "중복되는 닉네임입니다.", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        else{
                            final DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("Users").child(uid);
                            reference2.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    UsersItem item = snapshot.getValue(UsersItem.class);
                                    item.setNickname(newnickname);
                                    reference2.setValue(item);
                                    dismiss();
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });

        NO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });




    }
}