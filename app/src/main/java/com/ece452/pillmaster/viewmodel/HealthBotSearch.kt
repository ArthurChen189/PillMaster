package com.ece452.pillmaster.viewmodel


import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.aallam.openai.api.completion.CompletionRequest
import com.aallam.openai.api.completion.TextCompletion
import com.aallam.openai.api.http.Timeout
import com.aallam.openai.api.model.ModelId
import com.aallam.openai.client.OpenAI
import com.aallam.openai.client.OpenAIConfig
import com.ece452.pillmaster.model.Message

import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds
import android.util.Log
import java.util.logging.Logger


@HiltViewModel
class HealthBotSearch @Inject constructor(
//    private val _messages: Message
// Message data class has provided no constructor
): ViewModel() {

    // directly use the data class
    var _messages by mutableStateOf(Message())
        private set

//    val PROMPT = "You are a helpful AI assistant that does not hallucinate. You only provide information that is true and factual.\n" +
//            "Generate 10 passages that will answer the following the questions:\n";
    val PROMPT = "You are a helpful AI healthcare assistant that does not hallucinate.\n" +
        "Answer the following the question and give your confident score out of 10 in format of <Confidence: x/10> at the beginning" +
        ", 1 means you are very unsure, 10 means you are very sure about your knowledge:\n";
    val config = OpenAIConfig(
        token = "openai-token"
//        timeout = Timeout(socket = 60.seconds),
        // additional configurations...
    )

    val openAI = OpenAI(config)

    suspend fun getResponse() {
        if (_messages.question != "") {
            val completionRequest = CompletionRequest(
                model = ModelId("text-davinci-003"),
                prompt = PROMPT + _messages.question,
                maxTokens = 200,
                echo = false
            )
            val completion: TextCompletion = openAI.completion(completionRequest)
            Log.d("Justin", completion.choices[0].text)
            _messages = _messages.copy(answer = completion.choices[0].text)
        }
    }

    suspend fun sendMessage(message: String) {
        _messages = _messages.copy(question = message)
        getResponse()
    }

}
