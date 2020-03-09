package com.irobot.myapplication.ui.auth;


import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.FoldingCube;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.irobot.myapplication.MainActivity;
import com.irobot.myapplication.R;
import com.irobot.myapplication.utils.OnLoginListener;

/**
 * A simple {@link Fragment} subclass.
 */
public class SignInFragment extends Fragment implements OnLoginListener {
    private FirebaseAuth mAuth;

    private TextInputLayout mSignInEmail, mSignInPassword;
    private TextView mTextViewForgetPassword;
    private ProgressBar mprogressBar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_in, container, false);

        //Firebase instance
        mAuth = FirebaseAuth.getInstance();

        //find views with id's
        mSignInEmail = view.findViewById(R.id.email_editText);
        mSignInPassword = view.findViewById(R.id.password_editText);
        mTextViewForgetPassword = view.findViewById(R.id.textView_forgetPassword);

        mprogressBar = view.findViewById(R.id.spin_kit);
        Sprite foldingCube = new FoldingCube();
        mprogressBar.setIndeterminateDrawable(foldingCube);
//        mTextViewForgetPassword.setOnClickListener();
        TextWatcher watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                validateFields(mSignInEmail, mSignInPassword);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
        mSignInEmail.getEditText().addTextChangedListener(watcher);
        mSignInPassword.getEditText().addTextChangedListener(watcher);

        return view;
    }

    private void signInwithStore() {
        String emailSignIn = mSignInEmail.getEditText().getText().toString().trim();
        String passwordSignIn = mSignInPassword.getEditText().getText().toString().trim();

        mAuth.signInWithEmailAndPassword(emailSignIn, passwordSignIn).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                if (!isUserVerified(mAuth.getCurrentUser())) {
                    Toast.makeText(getContext(), "Please verify your Email to continue to Store", Toast.LENGTH_SHORT).show();
                    signOut();
                } else {
                    mprogressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(getContext(), "Sign in successful", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    startActivity(intent);
                    enableViews();
                }
            } else {
                Toast.makeText(getContext(), task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                enableViews();
            }
        });
    }


    private Boolean isUserVerified(FirebaseUser user) {
        return user.isEmailVerified();
    }

    private void signOut() {
        if (mAuth.getCurrentUser() != null) {
            mAuth.signOut();
        }
    }

    private void validateFields(TextInputLayout emailInput, TextInputLayout passwordInput) {
        String email = emailInput.getEditText().getText().toString();
        String password = emailInput.getEditText().getText().toString();
        if (Patterns.EMAIL_ADDRESS.matcher(email).matches() && password.length() == 6) {
            disableViews();
            mprogressBar.setVisibility(View.VISIBLE);
            login();
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailInput.setError("Please Enter A valid Email Address");
        } else if (password.length() == 0) {
            passwordInput.setError("Password Must be 6 characters long");
        } else {
            emailInput.setError("Please Enter A valid Email Address");
            passwordInput.setError("Password Must be 6 characters long");
        }

    }

    private void disableViews() {
        mSignInEmail.setEnabled(false);
        mSignInPassword.setEnabled(false);
    }

    private void enableViews() {
        mSignInEmail.setEnabled(true);
        mSignInPassword.setEnabled(true);
    }

    @Override
    public void login() {
        signInwithStore();

    }
}
