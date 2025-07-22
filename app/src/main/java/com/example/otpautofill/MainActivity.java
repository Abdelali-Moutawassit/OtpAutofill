package com.example.otpautofill;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.auth.api.phone.SmsRetrieverClient;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static final int SMS_CONSENT_REQUEST = 2;
    private static final int REQUEST_SMS_PERMISSION = 1;

    private EditText phoneNumberInput;
    private EditText otpInput;
    private Button startVerificationButton;
    private Button verifyOtpButton;
    private TextView statusText;
    private TextView hashText;
    private TextView instructionsText;

    private String appSignature;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeViews();
        requestSmsPermission();
        getAppSignature();
        setupBroadcastReceiver();
    }

    private void initializeViews() {
        phoneNumberInput = findViewById(R.id.phoneNumberInput);
        otpInput = findViewById(R.id.otpInput);
        startVerificationButton = findViewById(R.id.startVerificationButton);
        verifyOtpButton = findViewById(R.id.verifyOtpButton);
        statusText = findViewById(R.id.statusText);
        hashText = findViewById(R.id.hashText);
        instructionsText = findViewById(R.id.instructionsText);

        startVerificationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSMSListener();
            }
        });

        verifyOtpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifyOTP();
            }
        });
    }

    private void requestSmsPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.RECEIVE_SMS, Manifest.permission.READ_SMS},
                    REQUEST_SMS_PERMISSION);
        }
    }

    private void getAppSignature() {
        AppSignatureHelper appSignatureHelper = new AppSignatureHelper(this);
        appSignature = appSignatureHelper.getAppSignatures().get(0);
        hashText.setText("Hash de l'app: " + appSignature);

        // Instructions pour l'émulateur
        String instructions = "INSTRUCTIONS POUR L'ÉMULATEUR:\n\n" +
                "1. Cliquez sur 'Démarrer Vérification'\n" +
                "2. Dans l'émulateur, allez dans l'app Messages\n" +
                "3. Envoyez un SMS avec ce format exact:\n\n" +
                "Votre code de vérification est: 123456\n\n" +
                appSignature + "\n\n" +
                "4. Le code sera détecté automatiquement";

        instructionsText.setText(instructions);
    }

    private void setupBroadcastReceiver() {
        // Utilisation du listener statique
        SMSReceiver.setOtpReceiveListener(new SMSReceiver.OtpReceiveListener() {
            @Override
            public void onOtpReceived(String otp) {
                Log.d(TAG, "Callback onOtpReceived appelé avec OTP: " + otp);

                // Exécuter sur le thread principal (UI thread)
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Log.d(TAG, "Mise à jour UI avec OTP: " + otp);
                            otpInput.setText(otp);
                            statusText.setText("Code OTP détecté automatiquement: " + otp);
                            verifyOtpButton.setEnabled(true);

                            // Toast pour confirmer
                            Toast.makeText(MainActivity.this,
                                    "Code OTP rempli automatiquement: " + otp,
                                    Toast.LENGTH_SHORT).show();

                            Log.d(TAG, "Interface utilisateur mise à jour avec succès");
                        } catch (Exception e) {
                            Log.e(TAG, "Erreur lors de la mise à jour de l'UI", e);
                        }
                    }
                });
            }

            @Override
            public void onOtpTimeout() {
                Log.d(TAG, "Callback onOtpTimeout appelé");

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        statusText.setText("Timeout - Aucun SMS reçu");
                        Toast.makeText(MainActivity.this,
                                "Timeout - Aucun SMS reçu dans les délais",
                                Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void startSMSListener() {
        Log.d(TAG, "Démarrage du SMS Listener");

        SmsRetrieverClient client = SmsRetriever.getClient(this);

        Task<Void> task = client.startSmsRetriever();
        task.addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "SMS Retriever démarré avec succès");
                statusText.setText("En attente du SMS de vérification...");

                // Simuler l'envoi d'une requête au serveur
                simulateServerRequest();
            }
        });

        task.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                Log.e(TAG, "Erreur démarrage SMS Retriever", e);
                statusText.setText("Erreur: " + e.getMessage());
            }
        });
    }

    private void simulateServerRequest() {
        String phoneNumber = phoneNumberInput.getText().toString();
        if (phoneNumber.isEmpty()) {
            phoneNumber = "(650) 555-1212";
        }

        statusText.setText("Simulation envoi au serveur...\n" +
                "Numéro: " + phoneNumber + "\n" +
                "En attente du SMS...");

        Toast.makeText(this,
                "Maintenant, envoyez un SMS dans l'émulateur avec le format indiqué",
                Toast.LENGTH_LONG).show();
    }

    private void verifyOTP() {
        String otp = otpInput.getText().toString();
        if (otp.isEmpty()) {
            Toast.makeText(this, "Veuillez entrer le code OTP", Toast.LENGTH_SHORT).show();
            return;
        }

        // Simuler la vérification
        if (otp.length() == 6) {
            statusText.setText("✅ Code OTP vérifié avec succès!\n" +
                    "Utilisateur authentifié: " + otp);
            Toast.makeText(this, "Vérification réussie!", Toast.LENGTH_SHORT).show();
        } else {
            statusText.setText("❌ Code OTP invalide");
            Toast.makeText(this, "Code invalide", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Supprimer le listener pour éviter les fuites mémoire
        SMSReceiver.removeOtpReceiveListener();
        Log.d(TAG, "Activity détruite, listener supprimé");
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Rétablir le listener au cas où
        setupBroadcastReceiver();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_SMS_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission SMS accordée", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Permission SMS requise pour le bon fonctionnement",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }
}