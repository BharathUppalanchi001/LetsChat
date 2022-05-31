package com.example.letschat.adapters
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.letschat.R
import com.example.letschat.activities.ChatActivity
import com.example.letschat.databinding.UserLayoutBinding
import com.example.letschat.model.Users

class UserAdapter(private val context: Context,private val userList : ArrayList<Users>) : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {
    private  lateinit var  binding : UserLayoutBinding
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
         binding = UserLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {

        val currentUser = userList[position]
        holder.userName.text = currentUser.name
        holder.itemView.setOnClickListener{
            val intent = Intent(context, ChatActivity::class.java)
            intent.putExtra("name", currentUser.name)
            intent.putExtra("uid", currentUser.UID)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    class UserViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        var userName = itemView.findViewById<TextView>(R.id.userName)!!
    }
}