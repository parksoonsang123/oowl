package com.ooowl.oowl;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EditDialog2 extends Dialog {

    private Context context;

    public EditText pwd;

    public TextView OK;
    public TextView NO;

    String uid;

    public EditDialog2(@NonNull Context context, String uid) {
        super(context);
        this.context = context;
        this.uid = uid;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_edit_dialog2);

        final FirebaseAuth mAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = mAuth.getCurrentUser();
        uid = user.getUid();

        pwd = findViewById(R.id.pwd2);
        OK = findViewById(R.id.edit_ok_text);
        NO = findViewById(R.id.edit_no_text);

        OK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String newpwd = pwd.getText().toString();
                if(newpwd.equals("") || newpwd == null){
                    Toast.makeText(context, "변경할 비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(newpwd.length()<6){
                    Toast.makeText(context, "비밀번호는 6자리 이상이여야 합니다.", Toast.LENGTH_SHORT).show();
                    return;
                }
                else{
                    user.updatePassword(newpwd);
                    dismiss();
                }
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