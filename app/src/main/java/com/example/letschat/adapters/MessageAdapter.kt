package com.example.letschat.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.letschat.R
import com.example.letschat.model.Message
import com.google.firebase.auth.FirebaseAuth

class MessageAdapter(private  val context: Context,private val messageList : ArrayList<Message>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    private val itemSent = 1
    private val itemReceived = 2

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
     return  if (viewType == 1){
           val view:View = LayoutInflater.from(context).inflate(R.layout.sent_layout, parent,false)
         SentViewHolder(view)
       }
        else{
         val view:View = LayoutInflater.from(context).inflate(R.layout.receive_layout, parent,false)
         ReceiveViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val currentMessage = messageList[position]
       if (holder.javaClass == SentViewHolder::class.java){
            val viewHolder = holder as SentViewHolder
           viewHolder.sentMessage.text = currentMessage.message
       }
        else{
           val viewHolder = holder as ReceiveViewHolder
           viewHolder.receivedMessage.text = currentMessage.message
        }
    }

    override fun getItemCount(): Int {
        return  messageList.size
    }

    override fun getItemViewType(position: Int): Int {
        val currentMessage = messageList[position]
        return if (FirebaseAuth.getInstance().currentUser?.uid.equals(currentMessage.senderId)){
            itemSent
        } else{
            itemReceived
        }
    }



    class SentViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        var sentMessage = itemView.findViewById<TextView>(R.id.sentMessage)!!
    }

    class ReceiveViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        var receivedMessage = itemView.findViewById<TextView>(R.id.receivedMessage)!!
    }
}