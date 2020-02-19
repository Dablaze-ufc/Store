package com.irobot.myapplication.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.irobot.myapplication.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment {

    private static final String TAG = "LoginFragment";
    private int RC_SIGN_IN = 0;
    private Button mButtonGoogleSignin, mButtomEmail;
    private FirebaseAuth mAuth;
    private TextView mTextViewSignOut;
    private GoogleSignInClient mGoogleClient;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_login, container, false);
        mButtonGoogleSignin = root.findViewById(R.id.button_google);
        mButtomEmail = root.findViewById(R.id.button_email);
        mTextViewSignOut = root.findViewById(R.id.textView);
        mAuth = FirebaseAuth.getInstance();

        googleSignInOption();

        mButtomEmail.setOnClickListener(view -> {
            Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_signInDialogFragment);

        });

        return root;

    }

    private void googleSignInOption(){
        GoogleSignInOptions mGoogleOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleClient = GoogleSignIn.getClient(getParentFragment().getContext(), mGoogleOptions);

        mButtonGoogleSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });
        mTextViewSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
            }
        });
    }

    private void signOut() {
        mGoogleClient.signOut()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        mTextViewSignOut.setVisibility(View.INVISIBLE);
                        Toast.makeText(getContext(), "you are logged out", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void signIn() {
        Intent signInIntent = mGoogleClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN){
            Task<GoogleSignInAccount> tasks = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(tasks);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> CompletedTasks) {
        try {
            GoogleSignInAccount account = CompletedTasks.getResult(ApiException.class);
            Toast.makeText(getContext(), "Sign In Successfully", Toast.LENGTH_SHORT).show();
            assert account != null;
            FirebaseGoogleAuth(account);
        }
        catch (ApiException e){
            Toast.makeText(getContext(), "Sign In Failed", Toast.LENGTH_SHORT).show();
            FirebaseGoogleAuth(null);
        }
    }

    private void FirebaseGoogleAuth(GoogleSignInAccount accounts) {
        AuthCredential mAC = GoogleAuthProvider.getCredential(accounts.getIdToken(), null);
        mAuth.signInWithCredential(mAC)
                .addOnCompleteListener(getParentFragment().getActivity(), new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Toast.makeText(getContext(), "Successfully", Toast.LENGTH_SHORT).show();
                    FirebaseUser user = mAuth.getCurrentUser();
                    updateUI(user);
                }
                else {
                    Toast.makeText(getContext(), "Failed", Toast.LENGTH_SHORT).show();
                    updateUI(null);
                }
            }
        });

    }

    private void updateUI(FirebaseUser fUsers) {
        mTextViewSignOut.setVisibility(View.VISIBLE);
        GoogleSignInAccount inAccount = GoogleSignIn.getLastSignedInAccount(getContext());
        if (inAccount != null){
            String personName = fUsers.getDisplayName();
            String PersonEmail = fUsers.getEmail();
            Toast.makeText(getContext(), personName + PersonEmail, Toast.LENGTH_SHORT).show();
        }
    }


}

