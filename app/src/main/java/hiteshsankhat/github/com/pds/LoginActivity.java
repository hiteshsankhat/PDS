package hiteshsankhat.github.com.pds;

import android.content.Intent;
import android.support.annotation.NonNull;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

import hiteshsankhat.github.com.pds.Models.User;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

	private EditText editTextEmail, editTextPassword;
	private Button buttonLogin;
	private TextView textViewLink;

	private static final String TAG = "LoginActivity";

	private ProgressBar progressBar;

	private FirebaseAuth.AuthStateListener authStateListener;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		editTextEmail = findViewById(R.id.input_email_login);
		editTextPassword = findViewById(R.id.input_password_login);
		buttonLogin = findViewById(R.id.btn_login);
		textViewLink = findViewById(R.id.link_signup);
		progressBar = findViewById(R.id.progressBar);

		setUpFirebaseAuth();

		buttonLogin.setOnClickListener(this);
		textViewLink.setOnClickListener(this);
	}


	private void showDialog() {
		progressBar.setVisibility(View.VISIBLE);

	}

	private void hideDialog() {
		if (progressBar.getVisibility() == View.VISIBLE) {
			progressBar.setVisibility(View.INVISIBLE);
		}
	}

	private void setUpFirebaseAuth() {
		authStateListener = new FirebaseAuth.AuthStateListener() {
			@Override
			public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
				FirebaseUser user = firebaseAuth.getCurrentUser();
				if (user != null) {
					Toast.makeText(LoginActivity.this, "Authenticated with: " + user.getEmail(), Toast.LENGTH_SHORT).show();

					FirebaseFirestore db = FirebaseFirestore.getInstance();
					FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
							.setTimestampsInSnapshotsEnabled(true)
							.build();
					db.setFirestoreSettings(settings);

					DocumentReference userRef = db.collection(getString(R.string.firebase_user)).document(user.getUid());

					userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
						@Override
						public void onComplete(@NonNull Task<DocumentSnapshot> task) {
							if (task.isSuccessful()) {
								Log.d(TAG, "onComplete: DONE DONE");

								User user = task.getResult().toObject(User.class);

							}
						}
					});

					Intent intent = new Intent(LoginActivity.this, MainActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
					startActivity(intent);
					finish();
				} else {
					Log.d(TAG, "onAuthStateChanged: Signed_Out");
				}
			}
		};
	}

	@Override
	protected void onStart() {
		super.onStart();
		FirebaseAuth.getInstance().addAuthStateListener(authStateListener);
	}

	@Override
	protected void onStop() {
		super.onStop();

		if (authStateListener != null) {
			FirebaseAuth.getInstance().removeAuthStateListener(authStateListener);
		}
	}

	private void signIn() {
		String email, password;
		email = editTextEmail.getText().toString();
		password = editTextPassword.getText().toString();

		if (TextUtils.isEmpty(email)) {
			Toast.makeText(this, "Please enter email....", Toast.LENGTH_SHORT).show();
			return;
		}
		if (TextUtils.isEmpty(email)) {
			Toast.makeText(this, "Please enter email....", Toast.LENGTH_SHORT).show();
			return;
		}
		showDialog();

		FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
				.addOnCompleteListener(new OnCompleteListener<AuthResult>() {
					@Override
					public void onComplete(@NonNull Task<AuthResult> task) {
						hideDialog();
					}
				}).addOnFailureListener(new OnFailureListener() {
			@Override
			public void onFailure(@NonNull Exception e) {
				Toast.makeText(LoginActivity.this, "Authentication Failed", Toast.LENGTH_SHORT).show();
				hideDialog();
			}
		});
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.btn_login:
				signIn();
				break;
			case R.id.link_signup:
				Intent intent = new Intent(LoginActivity.this, SignUp.class);
				startActivity(intent);
				break;
		}
	}
}
