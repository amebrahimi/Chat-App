package tk.mrclover.prince.chatapp

import android.annotation.SuppressLint
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    companion object {
        const val COLLECTION_KEY = "Chat"
        const val DOCUMENT_KEY = "Message"
        const val NAME_FIELD = "Name"
        const val TEXT_FIELD = "Text"
    }

    private val firestoreChat by lazy {
        FirebaseFirestore.getInstance().collection(COLLECTION_KEY).document(DOCUMENT_KEY)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        realtimeUpdateListener()
        send_message_button.setOnClickListener { sendMessage() }
    }

    private fun sendMessage() {
        val newMessage = mapOf(
                NAME_FIELD to name.text.toString(),
                TEXT_FIELD to chat.text.toString())
        firestoreChat.set(newMessage)
                .addOnSuccessListener {
                    Toast.makeText(this, "Message Sent", Toast.LENGTH_LONG).show()
                }.addOnFailureListener {
                    Log.e("Error", it.message)
                }
    }

    @SuppressLint("SetTextI18n")
    private fun realtimeUpdateListener() {
        firestoreChat.addSnapshotListener { documentSnapshot, e ->
            when {
                e != null -> Log.e("Error", e.message)
                documentSnapshot != null && documentSnapshot.exists() -> {
                    with(documentSnapshot) {
                        chat_text.text = "${data!![NAME_FIELD]}:${this.data!![TEXT_FIELD]}"
                    }
                }
            }
        }
    }
}
