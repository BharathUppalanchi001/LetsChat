package com.example.letschat.activities
import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ScrollView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.letschat.adapters.MessageAdapter
import com.example.letschat.databinding.ActivityChatBinding
import com.example.letschat.model.Message
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.util.*
import kotlin.collections.ArrayList

class ChatActivity : AppCompatActivity() {
    private  lateinit var binding: ActivityChatBinding
    private lateinit var messageAdapter : MessageAdapter
    private lateinit var messageList : ArrayList<Message>
    private  lateinit var firebaseDatabase : DatabaseReference

    private var senderRoom: String? = null
    private var receiverRoom : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        val  view = binding.root
        setContentView(view)
        firebaseDatabase = FirebaseDatabase.getInstance().reference
        val name: String?
        var receiverUID: String? = null
        val senderUID: String? = FirebaseAuth.getInstance().currentUser?.uid
        if (intent.extras != null){
            name = intent.getStringExtra("name")
            receiverUID = intent.getStringExtra("uid")
            binding.toolbar.title = name
        }

        senderRoom = receiverUID + senderUID
        receiverRoom = senderUID + receiverUID

        messageList = ArrayList()
        messageAdapter = MessageAdapter(this@ChatActivity, messageList)
        val linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)





        binding.sendMessage.setOnClickListener {
            binding.sendMessage.isClickable = false
            val message = binding.messageInput.text.toString()
            val messageObject = Message(message, senderUID)
            firebaseDatabase.child("chats").child(senderRoom!!).child("messages").push()
                .setValue(messageObject).addOnSuccessListener {
                    firebaseDatabase.child("chats").child(receiverRoom!!).child("messages").push()
                        .setValue(messageObject)
                    binding.messageInput.text.clear()
                    binding.sendMessage.isClickable = true
                }
        }

        firebaseDatabase.child("chats").child(senderRoom!!).child("messages")
            .addValueEventListener(object: ValueEventListener{
                @SuppressLint("NotifyDataSetChanged")
                override fun onDataChange(snapshot: DataSnapshot) {
                    messageList.clear()
                    for (postSnapshot in snapshot.children){
                        val message = postSnapshot.getValue(Message::class.java)
                        messageList.add(message!!)
                    }

                    binding.chatRecyclerView.layoutManager = linearLayoutManager
                    binding.chatRecyclerView.adapter = messageAdapter
                    binding.scrollView.post(){
                       binding.scrollView.fullScroll(ScrollView.FOCUS_DOWN);
                   }
                }
                override fun onCancelled(error: DatabaseError) {

                }
            })


    }
}