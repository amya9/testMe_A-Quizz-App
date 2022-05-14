package com.example.testme2;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testme2.adapters.MasterAdapter;
import com.example.testme2.models.MasterModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MasterActivity extends AppCompatActivity {
//    private Button getin_btn;

    private Dialog loadingDialog , addMasterDialog;
    private RecyclerView recyclerView ;
    private CircleImageView addImage ;
    private Button addMasterBtn , getin_btn , add_master;
    private EditText addMasterName;
    private EditText addMasterPlace;
    private  String downloadUrl;
    private Uri image;

    FirebaseDatabase database;
    DatabaseReference reference;
    public static List<MasterModel> list;
    private MasterAdapter masterAdapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_master);

//        Spinner master_spinner = findViewById(R.id.master_spinner);
//        //create a list of items for the spinner.
//        String[] items = new String[]{"teacher_1", "teacher_2", "teacher_3" , "teacher_4" , "teacher_5"};
//        //create an adapter to describe how the items are displayed, adapters are used in several places in android.
//        //There are multiple variations of this, but this is the basic variant.
//        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
//        //set the spinners adapter to the previously created one.
//        master_spinner.setAdapter(adapter);


//        main intent
//        getin_btn = findViewById(R.id.login_btn);
//        getin_btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent mainIntent = new Intent(MasterActivity.this , MainActivity.class);
//                startActivity(mainIntent);
//            }
//        });

        Toolbar toolbar = findViewById(R.id.toolbar);
        database = FirebaseDatabase.getInstance();
        reference = database.getReference();
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Educator's");
        //loading Dialog
        loadingDialog = new Dialog(this);
        loadingDialog.setContentView(R.layout.loading);
        loadingDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.rounded_corner));
        loadingDialog.getWindow().setLayout(LinearLayout.LayoutParams.WRAP_CONTENT , LinearLayout.LayoutParams.WRAP_CONTENT);
        loadingDialog.setCancelable(false);

        recyclerView = findViewById(R.id.master_rv);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        loadingDialog.show();
        list = new ArrayList<>();

        masterAdapter = new MasterAdapter(list);
        recyclerView.setAdapter(masterAdapter);
        loadingDialog.show();
        reference.child("masters").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot1 : snapshot.getChildren()){
                    List<String> categories = new ArrayList<>();
                    List<String> sets = new ArrayList<>();
                    for (DataSnapshot dataSnapshot2 : dataSnapshot1.child("categories").getChildren()){
                        categories.add(dataSnapshot2.getKey());
                    }
                    for (DataSnapshot dataSnapshot2 : dataSnapshot1.child("sets").getChildren()){
                        sets.add(dataSnapshot2.getKey());
                    }
                    list.add(new MasterModel(dataSnapshot1.child("name").getValue().toString(),
                            dataSnapshot1.child("place").getValue().toString(),
                            categories,
                            sets,
                            dataSnapshot1.child("url").getValue().toString() ,
                            dataSnapshot1.getKey()));
                }
                masterAdapter.notifyDataSetChanged();
                loadingDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(MasterActivity.this , error.getMessage() , Toast.LENGTH_LONG).show();
                loadingDialog.dismiss();
                finish();
            }
        });

    }
}
