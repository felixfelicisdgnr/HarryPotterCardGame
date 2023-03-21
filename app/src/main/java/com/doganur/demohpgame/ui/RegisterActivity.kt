package com.doganur.demohpgame.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.doganur.demohpgame.R
import com.doganur.demohpgame.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class RegisterActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var registerScreen : ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        registerScreen = DataBindingUtil.setContentView(this, R.layout.activity_register)

        auth = Firebase.auth


        val loginText = registerScreen.haveanAccountLoginTextView
        val registerButton = registerScreen.buttonRegister

        loginText.setOnClickListener{
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        registerButton.setOnClickListener {
            performSignUp()
        }
    }

    private fun performSignUp(){
        val email = registerScreen.emailRegisterEditText.text.toString()
        val password = registerScreen.registerPasswordEditText.text.toString()
        val username = registerScreen.registerUsernameEditText.text.toString()

        if (email.isEmpty() || password.isEmpty() || username.isEmpty()){
            Toast.makeText(this,"Please fill all fields", Toast.LENGTH_SHORT).show()
            return
        }

        //kullanıcıdan email ile parola bilgisini alıyoruz, addOnCompleteListener başarılı olup olmadığını kontrol ediyor.
        auth.createUserWithEmailAndPassword(email,password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information

                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)

                    Toast.makeText(
                        baseContext,"Success.",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    // If sign in fails, display a message to the user.

                    Toast.makeText(baseContext,"Authentication failde.",
                        Toast.LENGTH_SHORT).show()
                }
            }

    }
}