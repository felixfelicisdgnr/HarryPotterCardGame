package com.doganur.demohpgame.ui

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import com.doganur.demohpgame.R
import com.doganur.demohpgame.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var database : FirebaseFirestore
    private lateinit var loginScreen : ActivityLoginBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loginScreen = DataBindingUtil.setContentView(this, R.layout.activity_login)

        auth = Firebase.auth

        val registerText = loginScreen.haventAccountRegisterTextView
        val loginButton = loginScreen.buttonLogin

        registerText.setOnClickListener{
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
        //let do login now
        loginButton.setOnClickListener {
            loginFunc()
        }

        resetPasswordBttn.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Forgot Password")
            val view = layoutInflater.inflate(R.layout.reset_password,null)
            val username = view.findViewById<EditText>(R.id.currentPasswordET)
            builder.setView(view)
            builder.setPositiveButton("Reset", DialogInterface.OnClickListener { _, _ ->
                forgotPassword(username)
            })
            builder.setNegativeButton("Reset", DialogInterface.OnClickListener { _, _ ->  })
        }
    }

    private fun forgotPassword(username : EditText){
            if (username.text.toString().isEmpty()){
                return
            }
            if (!Patterns.EMAIL_ADDRESS.matcher(username.text.toString()).matches()) {
                return
            }
            auth.sendPasswordResetEmail(username.text.toString())
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d("DOGA","Email Sent.")
                    }
                }
    }


    private fun loginFunc() {
        //lets get input from the user
        val username = loginScreen.loginUsernameEditText.text.toString()
        val password = loginScreen.loginPasswordEditText.text.toString()

        //null checks on inputs
        if (username.isEmpty() || password.isEmpty()){
            Toast.makeText(this,"Please fill all the fields", Toast.LENGTH_SHORT).show()
            return
        }

        auth.signInWithEmailAndPassword(username,password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    //Sign in success, navigate to the Main Activity
                    val intent = Intent(this, SelectionScreenActivity::class.java)
                    startActivity(intent)

                    Toast.makeText(
                        baseContext,"Success.",Toast.LENGTH_SHORT).show()

                }else {
                    //If sign in fails, display a message to the user.

                    Toast.makeText(baseContext,"Authentication failed.",Toast.LENGTH_SHORT).show()

                }
            }
            .addOnFailureListener {
                Toast.makeText(baseContext,"Authentication failed.${it.localizedMessage}",
                    Toast.LENGTH_SHORT).show()
            }

    }
}
