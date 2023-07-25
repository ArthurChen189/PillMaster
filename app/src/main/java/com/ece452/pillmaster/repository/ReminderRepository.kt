package com.ece452.pillmaster.repository

import com.ece452.pillmaster.model.Reminder
import com.ece452.pillmaster.model.Pill
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.firestore.ktx.dataObjects
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.io.IOException

// Interface of the Reminder repository
interface IReminderRepository {
    val reminders: Flow<List<Reminder>>
    suspend fun getReminder(reminderId: String): Reminder?
    suspend fun save(reminder: Reminder): String
    suspend fun update(reminder: Reminder)
    suspend fun delete(reminderId: String)
}

// Implementation of the Reminder repository
class ReminderRepository
@Inject constructor(private val firestore: FirebaseFirestore, private val auth: AuthRepository)
: IReminderRepository {
    val BASE_URL = "https://rxnav.nlm.nih.gov"
    val GET_DRUG_URL = "$BASE_URL/REST/drugs.json?name="
    val GET_INTERACTION_URL = "$BASE_URL/REST/interaction/interaction.json?rxcui="

    val client = OkHttpClient()

    @OptIn(ExperimentalCoroutinesApi::class)
    override val reminders: Flow<List<Reminder>>
        get() =
        auth.currentUserFlow.flatMapLatest { user ->
            firestore.collection(REMINDER_COLLECTION).whereEqualTo(USER_ID_FIELD, user.userId).dataObjects()
        }

    // Get a single reminder from firestore by document id
    override suspend fun getReminder(reminderId: String): Reminder? =
        firestore.collection(REMINDER_COLLECTION).document(reminderId).get().await().toObject()

    // Add a reminder to firestore, if the pill of the reminder name does not exist, add the pill to firestore as well
    override suspend fun save(reminder: Reminder): String {
        val userid = auth.getUserId()
        val pillRef = firestore.collection(PILL_COLLECTION).whereEqualTo(USER_ID_FIELD, userid).whereEqualTo(NAME_FIELD, reminder.name).limit(1).get().await().documents.firstOrNull()
        if (pillRef == null) {
            var newPill = Pill()
            newPill.userId = userid
            newPill.name = reminder.name
            newPill.description = reminder.description
            newPill.incompatibleDrugs = get_incompatible_drug_list(reminder.name, 3)
            firestore.collection(PILL_COLLECTION).add(newPill).await()
        }
        val reminderWithUserId = reminder.copy(userId = userid)
        return firestore.collection(REMINDER_COLLECTION).add(reminderWithUserId).await().id
    }

    override suspend fun update(reminder: Reminder) {
        firestore.collection(REMINDER_COLLECTION).document(reminder.id).set(reminder).await()
    }

    override suspend fun delete(reminderId: String) {
        firestore.collection(REMINDER_COLLECTION).document(reminderId).delete().await()
    }

    companion object {
        private const val USER_ID_FIELD = "userId"
        private const val NAME_FIELD = "name"
        private const val REMINDER_COLLECTION = "reminders"
        private const val PILL_COLLECTION = "pills"
    }

    fun get_drug(drug_keyword: String): String {
        val request = Request.Builder().url(GET_DRUG_URL + drug_keyword).build()
        val response = client.newCall(request).execute()
        if (!response.isSuccessful) {
            throw IOException("Unexpected code $response")
        }
        val responseJson = JSONObject(response.body?.string())
        val potential_drug = responseJson.getJSONObject("drugGroup").getJSONArray("conceptGroup").getJSONObject(1).getJSONArray("conceptProperties").getJSONObject(0).getString("rxcui")
        return potential_drug
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

    fun get_incompatible_drug_list(drug_keyword: String, topk: Int = 1): String {
        val drug_rxcui = get_drug(drug_keyword)
        val outputString = get_interaction(drug_rxcui, topk)
        return outputString
    }
}