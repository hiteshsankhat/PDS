package hiteshsankhat.github.com.pds;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthSettings;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

import hiteshsankhat.github.com.pds.Models.User;

public class SignUp extends AppCompatActivity {

	private static final String TAG = "SignUp";
	private EditText editTextname, editTextemail, editTextpassword, editTextcon_pass;
	private TextView link_login;
	private Button register_btn;
	private ProgressBar progressBar;


	private FirebaseAuth mAuth;
	private FirebaseFirestore mDB;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sign_up);
		editTextname = findViewById(R.id.input_name);
		editTextemail = findViewById(R.id.input_email);
		editTextpassword = findViewById(R.id.input_password);
		editTextcon_pass = findViewById(R.id.input_password_confirm);
		register_btn = findViewById(R.id.btn_signup);
		link_login = findViewById(R.id.link_login);
		progressBar = findViewById(R.id.progressBar);


		mAuth = FirebaseAuth.getInstance();
		mDB = FirebaseFirestore.getInstance();


		register_btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				register_user();
			}
		});

		link_login.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				redirectLoginScreen();
			}
		});
	}

	public void register_user(){
		String email, password, cn_pass, name ;
		email = editTextemail.getText().toString();
		password =editTextpassword.getText().toString();
		cn_pass = editTextcon_pass.getText().toString();
		name = editTextname.getText().toString();

		if(TextUtils.isEmpty(name)){
			Toast.makeText(this, "Please enter name...", Toast.LENGTH_SHORT).show();
			return;
		}
		if(TextUtils.isEmpty(email)){
			Toast.makeText(this, "Please enter email...", Toast.LENGTH_SHORT).show();
			return;
		}
		if(TextUtils.isEmpty(password)){
			Toast.makeText(this, "Please enter password...", Toast.LENGTH_SHORT).show();
			return;
		}
		if(TextUtils.isEmpty(cn_pass)){
			Toast.makeText(this, "Please enter confirm password...", Toast.LENGTH_SHORT).show();
			return;
		}
		if(password.equals(cn_pass)){
			addUser(email, password, name);
		}
		else{
			Toast.makeText(this, "Password Didn't Match", Toast.LENGTH_SHORT).show();
		}
	}

	public  void addUser(final String email, String pass, final String Name){
		showDialog();
		mAuth.createUserWithEmailAndPassword(email, pass)
				.addOnCompleteListener(new OnCompleteListener<AuthResult>() {
					@Override
					public void onComplete(@NonNull Task<AuthResult> task) {
						Log.d(TAG, "onComplete: "+task.isSuccessful());

						if(task.isSuccessful()){
							Log.d(TAG, "onComplete: "+mAuth.getCurrentUser().getUid());

							User user = new User();
							user.setEmail(email);
							user.setUser_id(mAuth.getUid());
							user.setName(Name);

							FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
									.setTimestampsInSnapshotsEnabled(true)
									.build();
							mDB.setFirestoreSettings(settings);

							DocumentReference newUser = mDB.collection(getString(R.string.firebase_user))
									.document(mAuth.getUid());

							newUser.set(user).addOnCompleteListener(new OnCompleteListener<Void>() {
								@Override
								public void onComplete(@NonNull Task<Void> task) {
									Log.d(TAG, "onComplete: DONE");
									hideDialog();
									if(task.isSuccessful()){
										redirectLoginScreen();
									}
									else{
										Log.d(TAG, "onComplete: Failure "+task.getException());
										View view = findViewById(android.R.id.content);
										Snackbar.make(view, "Something went Wrong.", Snackbar.LENGTH_SHORT).show();
									}
								}
							});
						}
						else{
							View view = findViewById(android.R.id.content);
							Snackbar.make(view, "Something went Wrong.", Snackbar.LENGTH_SHORT).show();
							hideDialog();
						}
					}
				});
	}

	private void showDialog(){
		progressBar.setVisibility(View.VISIBLE);
	}

	private void hideDialog(){
		if(progressBar.getVisibility() == View.VISIBLE){
			progressBar.setVisibility(View.INVISIBLE);
		}
	}

	private void redirectLoginScreen(){
		Intent intent = new Intent(SignUp.this, LoginActivity.class);
		startActivity(intent);
		finish();
	}
}
