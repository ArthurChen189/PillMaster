package com.ece452.pillmaster.utils

import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.io.IOException
import javax.inject.Inject

// The class is injected with Dagger (indicated by the @Inject constructor annotation),
// meaning instances of this class can be provided and used elsewhere in the codebase.
class DrugInfo @Inject constructor() {

    companion object {
        // Constants for the base URL and endpoint URLs of the RxNav API
        val BASE_URL = "https://rxnav.nlm.nih.gov"
        val GET_DRUG_URL = "$BASE_URL/REST/drugs.json?name="
        val GET_INTERACTION_URL = "$BASE_URL/REST/interaction/interaction.json?rxcui="

        // An OkHttpClient instance for making HTTP requests
        val client = OkHttpClient()

        // Method to get a list of incompatible drugs for a given drug.
        // It returns a string representation of the top-k incompatible drugs.
        fun get_incompatible_drug_list(drug_keyword: String, topk: Int = 1): String {
            // Get the rxcui (RxNorm Concept Unique Identifier) of the drug
            val drug_rxcui = get_drug(drug_keyword)
            // If no rxcui is found, return "Unknown"
            if(drug_rxcui.isEmpty()){
                return "Unknown"
            }
            // Otherwise, get the interaction of the drug and return it
            val outputString = get_interaction(drug_rxcui, topk)
            return outputString
        }

        // Method to get the interaction of a drug, given its rxcui
        fun get_interaction(rxcui: String, topk: Int = 1): String {
            // Build the HTTP request
            val request = Request.Builder().url(GET_INTERACTION_URL + rxcui).build()
            // Execute the HTTP request and get the response
            val response = client.newCall(request).execute()
            // Convert the response body to a JSON object
            val results = JSONObject(response.body?.string())
            // Parse the interaction pairs from the JSON object
            val interaction_pairs = results.getJSONArray("interactionTypeGroup").getJSONObject(0).getJSONArray("interactionType").getJSONObject(0).getJSONArray("interactionPair")
            var outputString = ""
            // Loop through the top-k interaction pairs and build the output string
            for (i in 0 until topk) {
                val pair = interaction_pairs.getJSONObject(i)
                outputString += "- " + pair.getString("description") + "\n"
            }
            return outputString
        }

        // Method to get the rxcui of a drug, given its name
        fun get_drug(drug_keyword: String): String {
            // Build the HTTP request
            val request = Request.Builder().url(GET_DRUG_URL + drug_keyword).build()
            // Execute the HTTP request and get the response
            val response = client.newCall(request).execute()
            // If the HTTP request is not successful, throw an IOException
            if (!response.isSuccessful) {
                throw IOException("Unexpected code $response")
            }
            val responseBody = response.body?.string()
            if (responseBody.isNullOrEmpty()) {
                return ""
            }

            try {
                // Convert the response body to a JSONObject
                val responseJson = JSONObject(responseBody)

                // Check if the "drugGroup" key exists and contains "conceptGroup" array
                if (responseJson.has("drugGroup")) {
                    val drugGroup = responseJson.getJSONObject("drugGroup")

                    if (drugGroup.has("conceptGroup")) {
                        val conceptGroupArray = drugGroup.getJSONArray("conceptGroup")

                        // Check if the "conceptGroup" array is not empty
                        if (conceptGroupArray.length() > 0) {
                            // Get the rxcui of the drug from the first element of "conceptProperties"
                            val conceptGroup = conceptGroupArray.getJSONObject(0)
                            val conceptProperties = conceptGroup.getJSONArray("conceptProperties")

                            if (conceptProperties.length() > 0) {
                                return conceptProperties.getJSONObject(0).optString("rxcui", "")
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                return ""
            }

            return ""
        }
    }
}
