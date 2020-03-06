package com.irobot.myapplication.ui.auth;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.irobot.myapplication.R;
import com.irobot.myapplication.utils.OnLoginListener;


/**
 * A simple {@link Fragment} subclass.
 */
public class RegisterFragment extends Fragment implements OnLoginListener {

    String registerEmail, registerPassword, registerConfirmPassword;
    private TextInputLayout
            mInputLayoutRegisterEmail,
            mInputLayoutRegisterPassword,
            mInputLayoutRegisterConfirmPassword;
    private FirebaseAuth mFirebaseRegister;
    private MaterialButton mMaterialButtonRegister;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);

        //find View by Id's
        mInputLayoutRegisterEmail = view.findViewById(R.id.textInputLayout_register_email);
        mInputLayoutRegisterPassword = view.findViewById(R.id.textInputLayout_register_password);
        mInputLayoutRegisterConfirmPassword = view.findViewById(R.id.textInputLayout_register_confirm_password);
//        mMaterialButtonRegister = view.findViewById(R.id.button_register);

        //get Instance
        mFirebaseRegister = FirebaseAuth.getInstance();

        //method Calling


        return view;
    }

    private void registerWithStore() {
        registerEmail = mInputLayoutRegisterEmail.getEditText().getText().toString().trim();
        registerPassword = mInputLayoutRegisterPassword.getEditText().getText().toString().trim();
        registerConfirmPassword = mInputLayoutRegisterConfirmPassword.getEditText().getText().toString().trim();

        if (checkingPasswords()) {
            mFirebaseRegister.createUserWithEmailAndPassword(registerEmail, registerPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(getContext(), "Registration Completed", Toast.LENGTH_SHORT).show();
//                        signOut();
                        //signIn successful
                        FirebaseUser user = mFirebaseRegister.getCurrentUser();
                        user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(getContext(), "Check your Email for verification", Toast.LENGTH_SHORT).show();
                                    navigateToSignIn(mMaterialButtonRegister);
                                    signOut();
                                } else
                                    Toast.makeText(getContext(), "Could'nt send verification! " + task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                            Toast.makeText(getContext(), "You are already Registered ", Toast.LENGTH_SHORT).show();
                        } else
                            Toast.makeText(getContext(), "an Error occured", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            Toast.makeText(getContext(), "Password does'nt match", Toast.LENGTH_SHORT).show();
        }


    }

//    private void whenButtonIsClicked() {
//        mMaterialButtonRegister.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                registerWithStore();
//            }
//        });
//    }

    private Boolean checkingPasswords() {
        return registerPassword.length() == registerConfirmPassword.length();
    }

    private void navigateToSignIn(View view) {

    }

    private void signOut() {
        if (mFirebaseRegister.getCurrentUser() != null) {
            mFirebaseRegister.signOut();
        }
    }


    @Override
    public void login() {

    }
}
