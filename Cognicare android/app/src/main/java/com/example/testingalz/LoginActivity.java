package com.example.testingalz;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;
import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;

public class LoginActivity extends AppCompatActivity {

    private static final String DEFAULT_USERNAME = "testuser";
    private static final String DEFAULT_PASSWORD = "testpass";

    private HashMap<String, String> userCredentials = new HashMap<>();

    private ViewSwitcher viewSwitcher;
    private EditText loginUsername, loginPassword;
    private EditText registerUsername, registerPassword, registerConfirmPassword;
    private TextView loginErrorMessage, registerErrorMessage;
    private Button loginButton, registerButton;
    private TextView switchToRegister, switchToLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        viewSwitcher = findViewById(R.id.viewSwitcher);

        // Initialize default user
        userCredentials.put(DEFAULT_USERNAME, DEFAULT_PASSWORD);

        // Login View Elements
        loginUsername = findViewById(R.id.loginUsername);
        loginPassword = findViewById(R.id.loginPassword);
        loginErrorMessage = findViewById(R.id.loginErrorMessage);
        loginButton = findViewById(R.id.loginButton);
        switchToRegister = findViewById(R.id.switchToRegister);

        // Register View Elements
        registerUsername = findViewById(R.id.registerUsername);
        registerPassword = findViewById(R.id.registerPassword);
        registerConfirmPassword = findViewById(R.id.registerConfirmPassword);
        registerErrorMessage = findViewById(R.id.registerErrorMessage);
        registerButton = findViewById(R.id.registerButton);
        switchToLogin = findViewById(R.id.switchToLogin);

        // Set listeners to switch views
        switchToRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewSwitcher.showNext();
            }
        });

        switchToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewSwitcher.showPrevious();
            }
        });

        // Login button listener
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = loginUsername.getText().toString();
                String password = loginPassword.getText().toString();
                if (username.isEmpty() || password.isEmpty()) {
                    loginErrorMessage.setText("Please fill in both fields");
                    loginErrorMessage.setVisibility(View.VISIBLE);
                } else {
                    // Add your login logic here, e.g., authentication with server
                    if (authenticateUser(username, password)) {
                        loginErrorMessage.setVisibility(View.GONE);
                        // Navigate to HomeActivity upon successful login
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish(); // Optional: call finish() if you don't want to return to the login screen
                    } else {
                        loginErrorMessage.setText("Invalid username or password");
                        loginErrorMessage.setVisibility(View.VISIBLE);
                    }
                }
            }
        });

        // Register button listener
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = registerUsername.getText().toString();
                String password = registerPassword.getText().toString();
                String confirmPassword = registerConfirmPassword.getText().toString();

                if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                    registerErrorMessage.setText("Please fill in all fields");
                    registerErrorMessage.setVisibility(View.VISIBLE);
                } else if (!password.equals(confirmPassword)) {
                    registerErrorMessage.setText("Passwords do not match");
                    registerErrorMessage.setVisibility(View.VISIBLE);
                } else if (userCredentials.containsKey(username)) {
                    registerErrorMessage.setText("Username already exists");
                    registerErrorMessage.setVisibility(View.VISIBLE);
                } else {
                    // Add your registration logic here, e.g., saving to database
                    registerUser(username, password);
                    Toast.makeText(LoginActivity.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                    registerErrorMessage.setVisibility(View.GONE);
                    viewSwitcher.showPrevious();
                }
            }
        });
    }

    // Example authentication method with default username and password
    private boolean authenticateUser(String username, String password) {
        // Replace with your actual authentication logic
        return userCredentials.containsKey(username) && userCredentials.get(username).equals(password);
    }

    // Example registration method
    private void registerUser(String username, String password) {
        // Replace with your actual registration logic
        userCredentials.put(username, password);
    }
}
