package com.example.otpautofill;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.util.Base64;
import android.util.Log;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;

public class AppSignatureHelper {

    private static final String TAG = "AppSignatureHelper";
    private static final String HASH_TYPE = "SHA-256";
    private static final int NUM_HASHED_BYTES = 9;
    private static final int NUM_BASE64_CHAR = 11;

    private Context context;

    public AppSignatureHelper(Context context) {
        this.context = context;
    }

    /**
     * Obtient les signatures de l'application pour SMS Retriever API
     */
    public ArrayList<String> getAppSignatures() {
        ArrayList<String> appCodes = new ArrayList<>();

        try {
            // Obtenir le nom du package
            String packageName = context.getPackageName();

            // Obtenir les signatures du package
            PackageManager packageManager = context.getPackageManager();
            Signature[] signatures = packageManager.getPackageInfo(packageName,
                    PackageManager.GET_SIGNATURES).signatures;

            for (Signature signature : signatures) {
                String hash = hash(packageName, signature.toCharsString());
                if (hash != null) {
                    appCodes.add(String.format("%s", hash));
                    Log.d(TAG, "Package: " + packageName + " -- Hash: " + hash);
                }
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, "Impossible de trouver le package", e);
        }

        return appCodes;
    }

    private String hash(String packageName, String signature) {
        String appInfo = packageName + " " + signature;
        try {
            MessageDigest messageDigest = MessageDigest.getInstance(HASH_TYPE);
            messageDigest.update(appInfo.getBytes(StandardCharsets.UTF_8));

            byte[] hashSignature = messageDigest.digest();

            // Tronquer le hash à NUM_HASHED_BYTES et encoder en base64
            hashSignature = Arrays.copyOfRange(hashSignature, 0, NUM_HASHED_BYTES);
            String base64Hash = Base64.encodeToString(hashSignature, Base64.NO_PADDING | Base64.NO_WRAP);
            base64Hash = base64Hash.substring(0, NUM_BASE64_CHAR);

            Log.d(TAG, "Hash généré: " + base64Hash);
            return base64Hash;

        } catch (NoSuchAlgorithmException e) {
            Log.e(TAG, "Algorithme de hashage non disponible", e);
        }

        return null;
    }
}