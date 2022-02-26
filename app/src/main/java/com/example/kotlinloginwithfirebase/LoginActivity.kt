package com.example.kotlinloginwithfirebase

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*


class LoginActivity: AppCompatActivity() {
    lateinit var mAuthStateListener : FirebaseAuth.AuthStateListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initAuthStateListener()


        tv_kayit.setOnClickListener{

            var intent=Intent(this,RegisterActivity::class.java)
            startActivity(intent)
        }


        button_girisyap.setOnClickListener {
            if (et_Mail.text.isNotEmpty()&&et_Sifre.text.isNotEmpty()){
                progressBarGoster()
                FirebaseAuth.getInstance().signInWithEmailAndPassword(et_Mail.text.toString(),et_Sifre.text.toString())
                    .addOnCompleteListener(object: OnCompleteListener<AuthResult>{
                        override fun onComplete(p0: Task<AuthResult>) {
                            if (p0.isSuccessful){
                                progressBarGizle()
                                  Toast.makeText(this@LoginActivity,"Başarılı Giriş: "+FirebaseAuth.getInstance().currentUser?.email, Toast.LENGTH_SHORT).show()
                            //                                FirebaseAuth.getInstance().signOut()
                                var homeIntent=Intent(this@LoginActivity,MainActivity::class.java)
                                startActivity(homeIntent)
                            }
                            else{
                                progressBarGizle()
                                Toast.makeText(this@LoginActivity,"Hatalı giriş:"+ p0.exception?.message,Toast.LENGTH_LONG
                                    ).show()
                            }
                        }
                    })
            }
            else{
                Toast.makeText(this@LoginActivity,"Boş alanları doldurunuz",Toast.LENGTH_LONG).show()
            }
        }


    }
    private fun initAuthStateListener(){
        mAuthStateListener= FirebaseAuth.AuthStateListener {
             fun onAuthStateChanged(p0:FirebaseAuth){
                var user=p0.currentUser
                if (user!=null){
                    if (user.isEmailVerified){
                        Toast.makeText(this@LoginActivity,"Mail onaylandı giriş yap",Toast.LENGTH_LONG).show()
                        var intent= Intent(this@LoginActivity,MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                    else{
                        Toast.makeText(this@LoginActivity,"Mail adresinizi onaylamadan giriş yapamazsınız", Toast.LENGTH_SHORT).show()

                    }
                }
            }
        }

    }
    private fun progressBarGoster(){
        progress_Login.visibility = View.VISIBLE
    }
    private fun progressBarGizle(){
        progress_Login.visibility = View.INVISIBLE
    }
    override fun onStart() {

        super.onStart()


        FirebaseAuth.getInstance().addAuthStateListener(mAuthStateListener)
    }

    override fun onStop() {
        super.onStop()
        FirebaseAuth.getInstance().removeAuthStateListener {mAuthStateListener}
    }
}
