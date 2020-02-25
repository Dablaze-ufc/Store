package com.irobot.myapplication.ui.auth;


import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.irobot.myapplication.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class ForgetPasswordFragment extends DialogFragment {
    private FirebaseAuth mFireBaseAuth;
    private FirebaseUser mUser;
    private Button mButtonResetPassword;
    private TextInputLayout mTextInputLayout;

    private String emailForgetPassword;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        MaterialAlertDialogBuilder materialAlertDialogBuilder = new MaterialAlertDialogBuilder(getActivity());
        LayoutInflater layoutInflater = requireActivity().getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.fragment_forget_password, null);
        materialAlertDialogBuilder.setView(view);

        mFireBaseAuth = FirebaseAuth.getInstance();
        mButtonResetPassword = view.findViewById(R.id.button_resetPassword);
        mTextInputLayout = view.findViewById(R.id.textInputLayout_forget_password_email);

        buttonClicked();

        return materialAlertDialogBuilder.create();
    }

    private void buttonClicked() {
        mButtonResetPassword.setOnClickListener(v -> editTextError());
    }


    private void resetPassword() {
        emailForgetPassword = mTextInputLayout.getEditText().getText().toString().trim();
        mFireBaseAuth.sendPasswordResetEmail(emailForgetPassword).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(getContext(), "Check your email to reset your Password", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(requireContext(), task.getException().getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void editTextError() {
        if (ifEmpty()) {
            mTextInputLayout.setError("Please enter an email");
        } else {
            resetPassword();
        }
    }

    private boolean ifEmpty() {
        return emailForgetPassword.length() == 0;
    }
}
