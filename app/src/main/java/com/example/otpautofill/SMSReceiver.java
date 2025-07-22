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
    private OtpReceiveListener otpReceiveListener;

    public interface OtpReceiveListener {
        void onOtpReceived(String otp);
        void onOtpTimeout();
    }

    public void setOtpReceiveListener(OtpReceiveListener otpReceiveListener) {
        this.otpReceiveListener = otpReceiveListener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (SmsRetriever.SMS_RETRIEVED_ACTION.equals(intent.getAction())) {
            Bundle extras = intent.getExtras();
            Status status = (Status) extras.get(SmsRetriever.EXTRA_STATUS);

            switch (status.getStatusCode()) {
                case CommonStatusCodes.SUCCESS:
                    // Récupérer le message SMS
                    String message = (String) extras.get(SmsRetriever.EXTRA_SMS_MESSAGE);
                    Log.d(TAG, "SMS reçu: " + message);

                    if (message != null) {
                        // Extraire l'OTP du message
                        String otp = extractOtpFromMessage(message);
                        if (otp != null && otpReceiveListener != null) {
                            otpReceiveListener.onOtpReceived(otp);
                        }
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
        }
    }

    private String extractOtpFromMessage(String message) {
        // Pattern pour extraire un code de 4-8 chiffres
        Pattern pattern = Pattern.compile("\\d{4,8}");
        Matcher matcher = pattern.matcher(message);

        if (matcher.find()) {
            String otp = matcher.group();
            Log.d(TAG, "OTP extrait: " + otp);
            return otp;
        }

        Log.e(TAG, "Aucun OTP trouvé dans le message: " + message);
        return null;
    }
}