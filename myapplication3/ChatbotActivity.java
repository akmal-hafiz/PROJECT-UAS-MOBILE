package com.example.myapplication3;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ChatbotActivity extends AppCompatActivity {

    EditText userInput;
    Button sendButton;
    LinearLayout chatContainer;
    ScrollView scrollView;

    OkHttpClient client = new OkHttpClient();

    String OPENROUTER_API = "https://openrouter.ai/api/v1/chat/completions";
    String API_KEY = "Bearer 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";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatbot);

        userInput = findViewById(R.id.userInput);
        sendButton = findViewById(R.id.sendButton);
        chatContainer = findViewById(R.id.chatContainer);
        scrollView = findViewById(R.id.scrollView);

        sendButton.setOnClickListener(v -> {
            String message = userInput.getText().toString().trim();
            if (!message.isEmpty()) {
                addMessageBubble(message, true);
                userInput.setText("");

                String lowerMsg = message.toLowerCase();

                // Cek apakah user bertanya tentang jual produk
                if (lowerMsg.contains("jual produk") || lowerMsg.contains("jual barang")
                        || lowerMsg.contains("jual preloved")) {
                    addMessageBubble("Hubungi media sosial Flacko & Fashion. Nanti kamu dapat akses dashboard untuk jualan.", false);
                    return;
                }

                // Cek apakah user bertanya tentang cara membeli
                if (lowerMsg.contains("cara beli") || lowerMsg.contains("cara membeli")
                        || lowerMsg.contains("bayar") || lowerMsg.contains("pembayaran")) {
                    addMessageBubble("Klik 'Buy Now' di halaman produk. Bayar lewat Midtrans Snap pakai e-wallet, bank, atau kartu.", false);
                    return;
                }

                // Kirim ke AI jika tidak cocok dengan respons manual
                sendToAI(message);
            }
        });

    }

    void sendToAI(String message) {
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");

        JSONObject json = new JSONObject();
        JSONArray messages = new JSONArray();
        try {
            json.put("model", "mistralai/mistral-7b-instruct:free");

            JSONObject systemMsg = new JSONObject();
            systemMsg.put("role", "system");
            systemMsg.put("content", "Kamu adalah chatbot khusus aplikasi e-commerce. " +
                    "Fitur aplikasi ini meliputi login, register, melihat produk, membeli produk via Midtrans Snap, " +
                    "dan admin dapat menambah produk melalui Firebase. Jawablah hanya seputar aplikasi ini. " +
                    "Jika pertanyaan tidak berhubungan dengan fitur aplikasi e-commerce ini, katakan: 'Maaf, saya hanya bisa membantu seputar aplikasi ini.'");

            JSONObject userMsg = new JSONObject();
            userMsg.put("role", "user");
            userMsg.put("content", message);

            messages.put(systemMsg);
            messages.put(userMsg);
            json.put("messages", messages);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(JSON, json.toString());
        Request request = new Request.Builder()
                .url(OPENROUTER_API)
                .post(body)
                .addHeader("Authorization", API_KEY)
                .addHeader("Content-Type", "application/json")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(() -> addMessageBubble("Gagal menghubungi AI.", false));
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    String result = response.body().string();
                    JSONObject obj = new JSONObject(result);
                    String reply = obj.getJSONArray("choices")
                            .getJSONObject(0)
                            .getJSONObject("message")
                            .getString("content");

                    runOnUiThread(() -> addMessageBubble(reply.trim(), false));
                } catch (Exception e) {
                    runOnUiThread(() -> addMessageBubble("AI tidak bisa membalas.", false));
                }
            }
        });
    }

    void addMessageBubble(String text, boolean isUser) {
        LinearLayout bubbleLayout = new LinearLayout(this);
        bubbleLayout.setOrientation(LinearLayout.VERTICAL);
        bubbleLayout.setPadding(16, 8, 16, 8);

        TextView bubble = new TextView(this);
        bubble.setText(text);
        bubble.setTextSize(16f);
        bubble.setPadding(24, 16, 24, 16);
        bubble.setTextColor(isUser ? 0xFFFFFFFF : 0xFF000000);
        bubble.setBackgroundResource(isUser ? R.drawable.bubble_user : R.drawable.bubble_bot);

        LinearLayout.LayoutParams bubbleParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        bubbleParams.setMargins(0, 0, 0, 4);
        bubbleParams.gravity = isUser ? Gravity.END : Gravity.START;
        bubble.setLayoutParams(bubbleParams);

        // Tambahkan timestamp
        TextView timestamp = new TextView(this);
        timestamp.setText(getCurrentTime());
        timestamp.setTextSize(12f);
        timestamp.setTextColor(0xFF888888);

        LinearLayout.LayoutParams timeParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        timeParams.gravity = isUser ? Gravity.END : Gravity.START;
        timestamp.setLayoutParams(timeParams);

        bubbleLayout.setGravity(isUser ? Gravity.END : Gravity.START);
        bubbleLayout.addView(bubble);
        bubbleLayout.addView(timestamp);

        chatContainer.addView(bubbleLayout);
        scrollView.post(() -> scrollView.fullScroll(View.FOCUS_DOWN));
    }

    private String getCurrentTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
        return sdf.format(new Date());
    }
}
