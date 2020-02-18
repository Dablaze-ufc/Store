package com.irobot.myapplication;


import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * A simple {@link Fragment} subclass.
 */
public class SignInDialogFragment extends DialogFragment {
    private Button mButtonSignIn;
    private TextInputLayout mSignInEmail, mSignInPassword ;
    private TextView mTextViewSignUp;

    String emailSignIn, passwordSignIn;
    FirebaseAuth mAuth;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        final MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getActivity());
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        final View view = inflater.inflate(R.layout.fragment_sign_in_dialog, null);
        builder.setView(view);

        //Firebase instance
        mAuth = FirebaseAuth.getInstance();

        //find views with id's
        mButtonSignIn = view.findViewById(R.id.button_signIn);
        mSignInEmail = view.findViewById(R.id.textInputLayout_signIn_email);
        mSignInPassword = view.findViewById(R.id.textInputLayout_signIn_password);
        mTextViewSignUp = view.findViewById(R.id.textView_signUp);


        //calling method
        onButtonPressed();
        mTextViewSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Navigation.findNavController(v).navigateUp();
                Navigation.findNavController(v).navigate(R.id.registerDialogFragment);
            }
        });


        return builder.create();
    }

    private void signInwithStore(){
        emailSignIn = mSignInEmail.getEditText().getText().toString().trim();
        passwordSignIn = mSignInPassword.getEditText().getText().toString().trim();

        mAuth.signInWithEmailAndPassword(emailSignIn, passwordSignIn).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    if (!isUserVerified(mAuth.getCurrentUser())){
                        Toast.makeText(getContext(), "Please verify your Email to continue to Store" , Toast.LENGTH_SHORT).show();
                        signOut();
                    }else{
                        Toast.makeText(getContext(), "Sign in successful", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(getContext(), task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void onButtonPressed(){
        mButtonSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInwithStore();
            }
        });
    }

    private Boolean isUserVerified(FirebaseUser user){
        return user.isEmailVerified();
    }

    private void signOut(){
        if (mAuth.getCurrentUser() != null){
            mAuth.signOut();
        }
    }



}
