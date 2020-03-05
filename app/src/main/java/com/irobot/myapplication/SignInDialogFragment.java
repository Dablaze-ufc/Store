package com.irobot.myapplication;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * A simple {@link Fragment} subclass.
 */
public class SignInDialogFragment extends Fragment {
    private Button mButtonSignIn;
    private TextInputLayout mSignInEmail, mSignInPassword ;
    private TextView mTextViewSignUp, mTextViewForgetPassword;

    String emailSignIn, passwordSignIn;
    FirebaseAuth mAuth;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_in_dialog, container, false);

        //Firebase instance
        mAuth = FirebaseAuth.getInstance();

        //find views with id's
        mButtonSignIn = view.findViewById(R.id.button_signIn);
        mSignInEmail = view.findViewById(R.id.textInputLayout_signIn_email);
        mSignInPassword = view.findViewById(R.id.textInputLayout_signIn_password);
        mTextViewSignUp = view.findViewById(R.id.textView_signUp);
        mTextViewForgetPassword = view.findViewById(R.id.textView_forgetPassword);


        //calling method
        onButtonPressed();
        mTextViewSignUp.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_signInDialogFragment_to_registerFragment));
        mTextViewForgetPassword.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_signInDialogFragment_to_forgetPasswordFragment));

        return view;
    }

    private void signInwithStore(){
        emailSignIn = mSignInEmail.getEditText().getText().toString().trim();
        passwordSignIn = mSignInPassword.getEditText().getText().toString().trim();

        mAuth.signInWithEmailAndPassword(emailSignIn, passwordSignIn).addOnCompleteListener(task -> {
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
        });
    }

    private void onButtonPressed(){
        mButtonSignIn.setOnClickListener(v ->
                signInwithStore());
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
