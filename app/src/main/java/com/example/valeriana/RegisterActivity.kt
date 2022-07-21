package com.example.valeriana

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import com.example.valeriana.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class RegisterActivity : AppCompatActivity() {

    //view binding
    private lateinit var binding:ActivityRegisterBinding

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //init firebase auth
        firebaseAuth = FirebaseAuth.getInstance()
        progressDialog = ProgressDialog(this)
        progressDialog.setTitle(resources.getString(R.string.wait))
        progressDialog.setCanceledOnTouchOutside(false)

        binding.yesAccountBtn.setOnClickListener{
            startActivity(Intent(this, LoginActivity::class.java))
        }

        binding.registerBtn.setOnClickListener {
            validateData()
        }
    }

    private var name = ""
    private var secondName = ""
    private var email = ""
    private var password = ""

    private fun validateData(){
        name = binding.nombreEt.text.toString().trim()
        secondName = binding.apellidoEt.text.toString().trim()
        email = binding.emailEt.text.toString().trim()
        password = binding.passwordEt.text.toString().trim()

        if(name.isEmpty()){
            Toast.makeText(this,resources.getString(R.string.EnterName), Toast.LENGTH_SHORT ).show()
        }
        else if (secondName.isEmpty()){
            Toast.makeText(this,resources.getString(R.string.EnterSecondName), Toast.LENGTH_SHORT ).show()
        }
        else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            //invalid email pattern
            Toast.makeText(this,resources.getString(R.string.EnterEmail), Toast.LENGTH_SHORT ).show()
        }
        else if (password.isEmpty()){
            Toast.makeText(this,resources.getString(R.string.EnterPassword), Toast.LENGTH_SHORT ).show()
        }
        else{
            createUserAccount()
        }
    }

    private fun createUserAccount() {
        progressDialog.setMessage("Creando cuenta....")
        progressDialog.show()

        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                updateUserInfo()
            }
            .addOnFailureListener { e->
                progressDialog.dismiss()
                Toast.makeText(this,"Error al crear la cuenta por ${e.message}", Toast.LENGTH_SHORT ).show()
            }
    }

    private fun updateUserInfo() {
        progressDialog.setMessage("Guardando datos")

        val timeStamp = System.currentTimeMillis()
        val uid = firebaseAuth.uid
        val hashMap: HashMap<String, Any?> = HashMap()
        hashMap["uid"] = uid
        hashMap["name"] = name
        hashMap["secondName"] = secondName
        hashMap["email"] = email
        hashMap["profileImage"] = ""
        hashMap["userType"] = "Dentista"
        hashMap["aboutMe"] = ""
        hashMap["direccion"] = ""
        hashMap["timesStamp"] = timeStamp

        val ref = FirebaseDatabase.getInstance().getReference("users")
        ref.child(uid!!)
            .setValue(hashMap)
            .addOnSuccessListener {
                progressDialog.dismiss()
                Toast.makeText(this,"Usuario creado con exito", Toast.LENGTH_SHORT ).show()
                startActivity(Intent(this@RegisterActivity, MainActivity::class.java))
                finish()
            }
            .addOnFailureListener { e->
                progressDialog.dismiss()
                Toast.makeText(this,"Error al guardar los datos por ${e.message}", Toast.LENGTH_SHORT ).show()
            }
    }
}