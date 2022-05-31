package com.example.letschat.activities
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.letschat.databinding.ActivitySignUpBinding
import com.example.letschat.model.Users
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class SignUpActivity : AppCompatActivity() {

    private  lateinit var binding: ActivitySignUpBinding
    private lateinit var mAuth: FirebaseAuth
    private lateinit var firebaseDatabase : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        val  view = binding.root
        setContentView(view)
        clickListeners()
        mAuth = FirebaseAuth.getInstance()
    }

    private fun clickListeners() {
        binding.signInHere.setOnClickListener {
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
            binding.signInHere.isEnabled = false
        }
        
        binding.signUp.setOnClickListener {
            val email = binding.emailTv.text.toString()
            val password = binding.passwordTv.text.toString()
            val name = binding.nameTv.text.toString()
            
            signUp(name,email,password)
        }
    }

    private fun signUp(name:String, email: String, password: String) {
        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    addUserToDatabase(name, email, mAuth.currentUser?.uid!!)
                    val intent = Intent(this@SignUpActivity, SignInActivity::class.java)
                    Toast.makeText(this@SignUpActivity, "Successfully Signed up", Toast.LENGTH_SHORT).show()
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this@SignUpActivity, "Some error occurred", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun addUserToDatabase(name: String, email: String, uid: String?) {
        firebaseDatabase = FirebaseDatabase.getInstance().reference
        if (uid != null) {
            firebaseDatabase.child("users").child(uid).setValue(Users(name, email, uid))
        }
    }


    override fun onResume() {
        super.onResume()
        binding.signInHere.isEnabled = true
    }
}