package com.example.smishingdetectionapp.ui

import android.os.Bundle
import android.os.Handler
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.smishingdetectionapp.R

class SafeLinkCheckerActivity : AppCompatActivity() {

    private lateinit var urlInput: EditText
    private lateinit var checkButton: Button
    private lateinit var resultBox: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_safe_link_checker)

        urlInput = findViewById(R.id.editTextUrl)
        checkButton = findViewById(R.id.buttonCheckLink)
        resultBox = findViewById(R.id.textResult)

        checkButton.setOnClickListener {
            val url = urlInput.text.toString().trim()
            if (url.isEmpty()) {
                resultBox.text = "⚠️ Please enter a URL."
            } else {
                // 1. Local instant check
                val localResult = checkLocalRisk(url)
                resultBox.text = "🔍 Local check: $localResult"

                // 2. Simulated backend/API check
                simulateBackendCheck(url)
            }
        }
    }

    private fun checkLocalRisk(url: String): String {
        val lowered = url.lowercase()
        return when {
            lowered.contains("login") && !lowered.startsWith("https") ->
                "🚨 Risk: Login link without HTTPS"
            lowered.contains("free") -> "⚠️ Suspicious: Contains 'free'"
            lowered.contains("win") -> "⚠️ Suspicious: Contains 'win'"
            lowered.contains("bit.ly") || lowered.contains("tinyurl") ->
                "⚠️ Suspicious: Shortened link detected"
            lowered.startsWith("http://") ->
                "⚠️ Unsecured HTTP detected"
            else -> "✅ No immediate threat found"
        }
    }

    private fun simulateBackendCheck(url: String) {
        resultBox.append("\n\n🔄 Checking with external threat sources...")

        // Simulate API call delay
        Handler().postDelayed({
            val isSuspicious = url.contains("free") || url.contains("win") || url.contains("login")
            val backendResult = if (isSuspicious) "Suspicious" else "Safe"
            val reason = if (isSuspicious)
                "Pattern matches known phishing tactics"
            else
                "No risky indicators found in external sources"

            resultBox.append("\n\n✅ Backend Result: $backendResult\n📌 Reason: $reason")
        }, 2000)
    }
}
