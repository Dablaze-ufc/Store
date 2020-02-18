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
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;

import java.util.zip.Inflater;


/**
 * A simple {@link Fragment} subclass.
 */
public class RegisterDialogFragment extends DialogFragment {

    private TextInputLayout
            mInputLayoutRegisterEmail,
            mInputLayoutRegisterPassword,
            mInputLayoutRegisterConfirmPassword;
    private FirebaseAuth mFirebaseRegister;
    private MaterialButton mMaterialButtonRegister;
    String registerEmail, registerPassword, registerConfirmPassword;


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        MaterialAlertDialogBuilder materialAlertDialogBuilder = new MaterialAlertDialogBuilder(getActivity());
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View view = inflater.inflate(R.layout.fragment_register_dialog, null);
        materialAlertDialogBuilder.setView(view);

        //find View by Id's
        mInputLayoutRegisterEmail = view.findViewById(R.id.textInputLayout_register_email);
        mInputLayoutRegisterPassword = view.findViewById(R.id.textInputLayout_register_password);
        mInputLayoutRegisterConfirmPassword = view.findViewById(R.id.textInputLayout_register_confirm_password);
        mMaterialButtonRegister = view.findViewById(R.id.button_register);

        //get Instance
        mFirebaseRegister = FirebaseAuth.getInstance();

        //method Calling
        whenButtonIsClicked();

        return materialAlertDialogBuilder.create();
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
                        navigateToSignIn(mMaterialButtonRegister);
                        signOut();
                        //signIn successful
                        FirebaseUser user = mFirebaseRegister.getCurrentUser();
                        user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(getContext(), "Check your Email for verification", Toast.LENGTH_SHORT).show();
                                    navigateToSignIn(mMaterialButtonRegister);
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

    private void whenButtonIsClicked() {
        mMaterialButtonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerWithStore();
            }
        });
    }

    private Boolean checkingPasswords() {
        return registerPassword.length() == registerConfirmPassword.length();
    }

    private void navigateToSignIn(View view) {
        Navigation.findNavController(view).navigate(R.id.action_registerDialogFragment_to_signInDialogFragment);
    }

    private void signOut() {
        if (mFirebaseRegister.getCurrentUser() != null) {
            mFirebaseRegister.signOut();
        }
    }


}
