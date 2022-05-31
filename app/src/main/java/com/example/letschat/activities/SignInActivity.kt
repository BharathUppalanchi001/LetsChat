package com.example.letschat.activities

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.letschat.MainActivity
import com.example.letschat.databinding.ActivitySignInBinding
import com.google.firebase.auth.FirebaseAuth


class SignInActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignInBinding
    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        clickListeners()
        mAuth = FirebaseAuth.getInstance()
    }

    private fun clickListeners(){
        binding.signUpHere.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
            binding.signUpHere.isEnabled = false

        }

        binding.logIn.setOnClickListener {
            val email = binding.emailTv.text.toString()
            val password = binding.passwordTv.text.toString()
            
            login(email, password)
        }

    }

    private fun login(email: String, password: String) {
        if (validate()){
            mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        val intent = Intent(this@SignInActivity, MainActivity::class.java)
                        Toast.makeText(this@SignInActivity, "Login Successful", Toast.LENGTH_SHORT).show()
                        startActivity(intent)
                        finish()
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithEmail:failure", task.exception)
                        Toast.makeText(baseContext, "User does not exist!!",
                            Toast.LENGTH_SHORT).show()

                    }
                }
        }
    }

    override fun onResume() {
        super.onResume()
        binding.signUpHere.isEnabled = true
    }

    private fun validate() : Boolean{
        if (binding.emailTv.text.isNullOrEmpty() || binding.passwordTv.text.isNullOrEmpty()){
            Toast.makeText(this@SignInActivity, "Invalid Credentials", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = mAuth.currentUser
        if(currentUser != null){
            val intent = Intent(this@SignInActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}