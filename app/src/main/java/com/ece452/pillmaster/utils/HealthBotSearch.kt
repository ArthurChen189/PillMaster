package com.ece452.pillmaster.utils


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


@HiltViewModel
class HealthBotSearch @Inject constructor(
    private val _messages: Message
): ViewModel() {
    val PROMPT = "You are a helpful AI assistant that does not hallucinate. You only provide information that is true and factual.\n" +
            "Generate 10 documents that will answer the following the questions:\n";
    val config = OpenAIConfig(
        token = "api-token",
        timeout = Timeout(socket = 60.seconds),
        // additional configurations...
    )

    val openAI = OpenAI(config)

    suspend fun getResponse(message: Message) {
        if (_messages!= null && _messages.question != "") {
            val completionRequest = CompletionRequest(
                model = ModelId("text-davinci-003"),
                prompt = PROMPT + _messages.question,
                echo = true
            )
            val completion: TextCompletion = openAI.completion(completionRequest)
            _messages.answer = completion.choices[0].text
        }
    }

    suspend fun sendMessage(message: String) {
        _messages.question = message
    }

}
