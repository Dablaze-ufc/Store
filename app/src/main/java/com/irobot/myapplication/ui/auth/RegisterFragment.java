package com.irobot.myapplication.ui.auth;


import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.FoldingCube;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.irobot.myapplication.MainActivity;
import com.irobot.myapplication.R;
import com.irobot.myapplication.utils.OnSignUpListener;


/**
 * A simple {@link Fragment} subclass.
 */
public class RegisterFragment extends Fragment implements OnSignUpListener {

    String registerEmail, registerPassword, registerConfirmPassword;
    private TextInputLayout
            mInputLayoutRegisterEmail,
            mInputLayoutRegisterPassword,
            mInputLayoutRegisterConfirmPassword;
    private GoogleSignInClient mGoogleClient;
    private FirebaseAuth mFirebaseAuth;
    private int RC_SIGN_IN = 0;
    private MaterialButton mMaterialButtonRegister;
    private ProgressBar progressBar;
    private RegisterFragment registerFragment;
    private SignInFragment signInFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);

        //find View by Id's
        mInputLayoutRegisterEmail = view.findViewById(R.id.textInputLayout_register_email);
        mInputLayoutRegisterPassword = view.findViewById(R.id.textInputLayout_register_password);
        mInputLayoutRegisterConfirmPassword = view.findViewById(R.id.textInputLayout_register_confirm_password);
        mMaterialButtonRegister = view.findViewById(R.id.googleSignInButton);
        progressBar = view.findViewById(R.id.spin_kit);
        Sprite foldingCube = new FoldingCube();
        progressBar.setIndeterminateDrawable(foldingCube);
        mMaterialButtonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                googleSignIn();
            }
        });
        //get Instance
        mFirebaseAuth = FirebaseAuth.getInstance();
        if (mInputLayoutRegisterEmail.hasFocus()) {

        }
        googleSignInOption();

        validate();


        return view;
    }

    private void validate() {
        if (mInputLayoutRegisterEmail.getEditText().getText().toString() == null &&
                mInputLayoutRegisterPassword.getEditText().getText().toString() == null) {
            progressBar.setVisibility(View.VISIBLE);
            registerWithStore();
        } else {

        }
    }

    private void registerWithStore() {
        registerEmail = mInputLayoutRegisterEmail.getEditText().getText().toString().trim();
        registerPassword = mInputLayoutRegisterPassword.getEditText().getText().toString().trim();
        registerConfirmPassword = mInputLayoutRegisterConfirmPassword.getEditText().getText().toString().trim();

        if (registerPassword.equals(registerConfirmPassword)) {
            mFirebaseAuth.createUserWithEmailAndPassword(registerEmail, registerPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        //signIn successful
                        FirebaseUser user = mFirebaseAuth.getCurrentUser();
                        user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(getContext(), "Check your Email for verification", Toast.LENGTH_SHORT).show();
                                    progressBar.setVisibility(View.GONE);
                                    signOut();
                                    signUp();
                                } else
                                    Toast.makeText(getContext(), "Couldn't send verification! " + task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(getContext(), "You are already Registered ", Toast.LENGTH_SHORT).show();
                        } else
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(getContext(), "an Error occurred", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            progressBar.setVisibility(View.GONE);
            mInputLayoutRegisterConfirmPassword.setHelperText("Passwords do not match");
            mInputLayoutRegisterConfirmPassword.setHelperTextColor(ColorStateList.valueOf(getResources().getColor(R.color.helperTextError)));
        }


    }

    private void signOut() {
        if (mFirebaseAuth.getCurrentUser() != null) {
            mFirebaseAuth.signOut();
        }
    }

    private void googleSignInOption() {
        GoogleSignInOptions mGoogleOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleClient = GoogleSignIn.getClient(getActivity(), mGoogleOptions);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> tasks = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = tasks.getResult(ApiException.class);
                Toast.makeText(getContext(), "Sign In Successfully", Toast.LENGTH_SHORT).show();
                firebaseGoogleAuth(account);
            } catch (ApiException e) {
                Toast.makeText(getContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
                Log.d("onActivityResul", "Sign In failed ", e);

            }
        }
    }

    private void googleSignOut() {
        mGoogleClient.signOut()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
//                        mTextViewSignOut.setVisibility(View.INVISIBLE);
                        Toast.makeText(getContext(), "you are logged out", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void firebaseGoogleAuth(GoogleSignInAccount accounts) {
        AuthCredential mAC = GoogleAuthProvider.getCredential(accounts.getIdToken(), null);
        mFirebaseAuth.signInWithCredential(mAC)
                .addOnCompleteListener(getActivity(), task -> {
                    if (task.isSuccessful()) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getContext(), "Successfully", Toast.LENGTH_SHORT).show();
                        FirebaseUser user = mFirebaseAuth.getCurrentUser();
                    } else {
                        Toast.makeText(getContext(), "Failed", Toast.LENGTH_SHORT).show();
                    }
                });

    }

    private void googleSignIn() {
        progressBar.setVisibility(View.VISIBLE);
        Intent signInIntent = mGoogleClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void signUp() {
        Intent intent = new Intent(getActivity(), MainActivity.class);
        startActivity(intent);
    }
}
