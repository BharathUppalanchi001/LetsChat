package com.example.letschat
import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.letschat.activities.SignInActivity
import com.example.letschat.adapters.UserAdapter
import com.example.letschat.databinding.ActivityMainBinding
import com.example.letschat.model.Users
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var usersList: ArrayList<Users>
    private  lateinit var usersAdapter : UserAdapter
    private lateinit var mAuth : FirebaseAuth
    private lateinit var firebaseDatabase : DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        val toolbar: Toolbar = binding.toolbar
        toolbar.inflateMenu(R.menu.menu)
        mAuth = FirebaseAuth.getInstance()
        firebaseDatabase = FirebaseDatabase.getInstance().reference


        toolbar.setOnMenuItemClickListener { item: MenuItem? ->
            when (item!!.itemId) {
                R.id.logout -> {
                    mAuth.signOut()
                    val intent = Intent(this@MainActivity, SignInActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
            true
        }


        usersList = ArrayList()
        usersAdapter = UserAdapter(this@MainActivity, usersList)
        binding.usersRecyclerView.adapter = usersAdapter

        binding.usersRecyclerView.layoutManager = LinearLayoutManager(this@MainActivity)
        firebaseDatabase.child("users").addValueEventListener(object : ValueEventListener{

            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange(snapshot: DataSnapshot) {
                usersList.clear()
                for (postSnapshot in snapshot.children){
                    val currentUser = postSnapshot.getValue(Users::class.java)
                    if(!mAuth.currentUser?.uid.equals(currentUser?.UID)){
                        usersList.add(currentUser!!)
                    }

                }
                usersAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })

    }


}

