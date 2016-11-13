package com.studypact.studypact.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.google.zxing.qrcode.encoder.ByteMatrix;
import com.google.zxing.qrcode.encoder.QRCode;
import com.studypact.studypact.R;
import com.studypact.studypact.service.UsageTrackerService;
import com.studypact.studypact.util.Util;

import org.json.JSONArray;
import org.json.JSONObject;

import static android.graphics.Color.BLACK;
import static android.graphics.Color.WHITE;

public class MakePactActivity extends AppCompatActivity {
    private static final String TAG = "MAKEPACTACT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_pact);
        ImageView qrCode = (ImageView) findViewById(R.id.qr_code_img);
        try {
            qrCode.setImageBitmap(encodeAsBitmap(Util.getFromStore("user", "arvind")));
        } catch (Exception e) {
            Log.e(TAG, "exception in qr code gen", e);
        }
        findViewById(R.id.qr_code_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new IntentIntegrator(MakePactActivity.this).initiateScan();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                //add pact here
                addPact(result.getContents());
                Toast.makeText(this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void addPact(String pactee) {
        try {
            JSONObject object = Util.getJSONFromStore("pacts");
            if (!object.has("pacts")) {
                object.put("pacts", new JSONArray());
            }
            JSONArray array = object.getJSONArray("pacts");
            JSONObject pactInfo = new JSONObject();
            pactInfo.put("name", pactee + "'s New Pact");
            pactInfo.put("start_time", System.currentTimeMillis());
            pactInfo.put("user_name", pactee);
            pactInfo.put("end_time", System.currentTimeMillis() + UsageTrackerService.DAY_MILLIS);
            array.put(pactInfo);
            object.put("pacts", array);
            Util.putIntoStore("pacts", object);
        } catch (Exception e) {
            Log.e(TAG, "exception in lazy remove", e);
        }
    }

    Bitmap encodeAsBitmap(String str) {
        BitMatrix result;

        try {
            result = new MultiFormatWriter().encode(str, BarcodeFormat.QR_CODE, 101, 101, null);
        } catch (Exception iae) {
            // Unsupported format
            return null;
        }
        int w = result.getWidth();
        int h = result.getHeight();
        int[] pixels = new int[w * h];
        for (int y = 0; y < h; y++) {
            int offset = y * w;
            for (int x = 0; x < w; x++) {
                pixels[offset + x] = result.get(x, y) ? BLACK : WHITE;
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, 101, 0, 0, w, h);
        return bitmap;
    }
}
