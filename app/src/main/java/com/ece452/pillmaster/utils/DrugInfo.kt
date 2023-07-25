package com.ece452.pillmaster.utils

import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.io.IOException
import javax.inject.Inject

class DrugInfo @Inject constructor() {

    companion object {
        val BASE_URL = "https://rxnav.nlm.nih.gov"
        val GET_DRUG_URL = "$BASE_URL/REST/drugs.json?name="
        val GET_INTERACTION_URL = "$BASE_URL/REST/interaction/interaction.json?rxcui="

        val client = OkHttpClient()
        fun get_incompatible_drug_list(drug_keyword: String, topk: Int = 1): String {
            val drug_rxcui = get_drug(drug_keyword)
            if(drug_rxcui.isEmpty()){
                return "Unknown"
            }
            val outputString = get_interaction(drug_rxcui, topk)
            return outputString
        }

        fun get_interaction(rxcui: String, topk: Int = 1): String {
            val request = Request.Builder().url(GET_INTERACTION_URL + rxcui).build()
            val response = client.newCall(request).execute()
            val results = JSONObject(response.body?.string())
            val interaction_pairs = results.getJSONArray("interactionTypeGroup").getJSONObject(0).getJSONArray("interactionType").getJSONObject(0).getJSONArray("interactionPair")
            var outputString = ""
            for (i in 0 until topk) {
                val pair = interaction_pairs.getJSONObject(i)
                outputString += "- " + pair.getString("description") + "\n"
            }
            return outputString
        }

        fun get_drug(drug_keyword: String): String {
            val request = Request.Builder().url(GET_DRUG_URL + drug_keyword).build()
            val response = client.newCall(request).execute()
            if (!response.isSuccessful) {
                throw IOException("Unexpected code $response")
            }
            val responseJson = JSONObject(response.body?.string())
            if(responseJson.getJSONObject("drugGroup").getJSONArray("conceptGroup").length() != 0){
                val potential_drug = responseJson.getJSONObject("drugGroup").getJSONArray("conceptGroup").getJSONObject(1).getJSONArray("conceptProperties").getJSONObject(0).getString("rxcui")
                return potential_drug
            }
            return "";

        }
    }

}