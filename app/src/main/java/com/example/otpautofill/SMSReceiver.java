package com.example.otpautofill;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.Status;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SMSReceiver extends BroadcastReceiver {

    private static final String TAG = "SMSReceiver";

    // Interface statique pour communiquer avec MainActivity
    private static OtpReceiveListener otpReceiveListener;

    public interface OtpReceiveListener {
        void onOtpReceived(String otp);
        void onOtpTimeout();
    }

    // Méthode statique pour définir le listener
    public static void setOtpReceiveListener(OtpReceiveListener listener) {
        otpReceiveListener = listener;
        Log.d(TAG, "Listener défini: " + (listener != null ? "Oui" : "Non"));
    }

    // Méthode statique pour supprimer le listener (éviter les fuites mémoire)
    public static void removeOtpReceiveListener() {
        otpReceiveListener = null;
        Log.d(TAG, "Listener supprimé");
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive appelé avec action: " + intent.getAction());
        Log.d(TAG, "Listener disponible: " + (otpReceiveListener != null ? "Oui" : "Non"));

        if (SmsRetriever.SMS_RETRIEVED_ACTION.equals(intent.getAction())) {
            Bundle extras = intent.getExtras();
            if (extras == null) {
                Log.e(TAG, "Extras est null");
                return;
            }

            Status status = (Status) extras.get(SmsRetriever.EXTRA_STATUS);
            if (status == null) {
                Log.e(TAG, "Status est null");
                return;
            }

            Log.d(TAG, "Status code: " + status.getStatusCode());

            switch (status.getStatusCode()) {
                case CommonStatusCodes.SUCCESS:
                    // Récupérer le message SMS
                    String message = (String) extras.get(SmsRetriever.EXTRA_SMS_MESSAGE);
                    Log.d(TAG, "SMS reçu: " + message);

                    if (message != null) {
                        // Extraire l'OTP du message
                        String otp = extractOtpFromMessage(message);
                        Log.d(TAG, "OTP extrait: " + otp);

                        if (otp != null && otpReceiveListener != null) {
                            Log.d(TAG, "Envoi OTP au listener");
                            otpReceiveListener.onOtpReceived(otp);
                        } else {
                            Log.w(TAG, "OTP null ou listener null - OTP: " + otp + ", Listener: " + otpReceiveListener);
                        }
                    } else {
                        Log.e(TAG, "Message SMS est null");
                    }
                    break;

                case CommonStatusCodes.TIMEOUT:
                    Log.d(TAG, "Timeout SMS Retriever");
                    if (otpReceiveListener != null) {
                        otpReceiveListener.onOtpTimeout();
                    }
                    break;

                default:
                    Log.e(TAG, "Erreur SMS Retriever: " + status.getStatusCode());
                    break;
            }
        } else {
            Log.w(TAG, "Action non reconnue: " + intent.getAction());
        }
    }

    private String extractOtpFromMessage(String message) {
        if (message == null || message.trim().isEmpty()) {
            Log.e(TAG, "Message vide ou null");
            return null;
        }

        // Pattern pour extraire un code de 4-8 chiffres
        Pattern pattern = Pattern.compile("\\d{4,8}");
        Matcher matcher = pattern.matcher(message);

        if (matcher.find()) {
            String otp = matcher.group();
            Log.d(TAG, "OTP trouvé: " + otp);
            return otp;
        }

        Log.w(TAG, "Aucun OTP trouvé dans le message: " + message);
        return null;
    }
}