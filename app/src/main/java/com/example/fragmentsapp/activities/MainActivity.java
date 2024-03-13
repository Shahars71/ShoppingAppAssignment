package com.example.fragmentsapp.activities;

import static android.app.ProgressDialog.show;
import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fragmentsapp.R;
import com.example.fragmentsapp.models.CustomAdapter;
import com.example.fragmentsapp.models.MyData;
import com.example.fragmentsapp.models.Product;
import com.example.fragmentsapp.models.RecyclerViewInterface;
import com.example.fragmentsapp.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements RecyclerViewInterface {

    private FirebaseAuth mAuth;

    private ArrayList<Product> dataset;
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private CustomAdapter adapter;

    private User currentUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // ...
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        dataset = new ArrayList<>();



    }

    public void initRecView(View view)
    {
        layoutManager = new LinearLayoutManager(this);
        recyclerView = view.findViewById(R.id.recView);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        for (int i = 0; i< MyData.getNameArr().length; i++){
            dataset.add(new Product(
                    MyData.getNameArr()[i],
                    0
            ));
        }

        adapter = new CustomAdapter(dataset);
        recyclerView.setAdapter(adapter);
    }

    public void addData()
    {
        EditText userEmail = this.findViewById(R.id.regEmail);
        EditText userPass = this.findViewById(R.id.regPassword);
        EditText userName = this.findViewById(R.id.regUserName);
        EditText userPhone = this.findViewById(R.id.regPhoneNumber);


        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        User user1 = new User(userEmail.getText().toString(),userPass.getText().toString(), userName.getText().toString(), userPhone.getText().toString());



        mAuth.createUserWithEmailAndPassword(user1.getEmail(), user1.getPass())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            DatabaseReference myRef = database.getReference("users").child(user.getUid());
                            myRef.setValue(user1);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void readData()
    {
        EditText email = findViewById(R.id.editTextEmail);
        EditText pass = findViewById(R.id.editTextTextPassword);

        mAuth.signInWithEmailAndPassword(email.getText().toString(), pass.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            fetchCurrentUser(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();}
                    }
                });

    }

    private void fetchCurrentUser(FirebaseUser user){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        if (user != null){
            String uid = user.getUid();
            DatabaseReference myRef = database.getReference("users").child(uid);

            myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    User newUser = snapshot.getValue(User.class);

                    if (newUser != null){
                        currentUser = newUser;
                        TextView userNameDisplay = findViewById(R.id.displayUserNameText);

                        String msg = "Hello "+ currentUser.getUserName() + "!";
                        userNameDisplay.setText(msg);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                Log.d("User not found!","Oh noes!");
                }

            });
        }
    }

    public void addProduct(View view){

        ViewGroup cardRowParent = (ViewGroup) view.getParent().getParent();

        TextView prodName = cardRowParent.findViewById(R.id.productNameText);
        TextView prodAmo = cardRowParent.findViewById(R.id.productAmountText);

        for (Product product :dataset
             ) {
            if (product.getName().equals(prodName.getText().toString()))
            {
                product.setAmount(product.getAmount()+1);
                Integer amm = product.getAmount();
                prodAmo.setText(amm.toString());
            }
        }


    }

    public void removeProduct(View view){
        ViewGroup cardRowParent = (ViewGroup) view.getParent().getParent();

        TextView prodName = cardRowParent.findViewById(R.id.productNameText);
        TextView prodAmo = cardRowParent.findViewById(R.id.productAmountText);

        for (Product product :dataset
        ) {
            if ((product.getName().equals(prodName.getText().toString()) && product.getAmount() > 0))
            {
                product.setAmount(product.getAmount()-1);
                Integer amm = product.getAmount();
                prodAmo.setText(amm.toString());
            }
        }


    }

    @Override
    public void onItemClick(int position) {

    }
}