package com.example.valeriana.fragments

import android.app.Activity
import android.app.Dialog
import android.app.ProgressDialog
import android.content.ContentValues
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.renderscript.Sampler
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import com.bumptech.glide.Glide
import com.example.valeriana.Add_Paciente
import com.example.valeriana.ConnectionLiveData
import com.example.valeriana.LoginActivity
import com.example.valeriana.R
import com.example.valeriana.databinding.ActivityLoginBinding
import com.example.valeriana.databinding.FragmentCalendarBinding
import com.example.valeriana.databinding.FragmentHomeBinding
import com.example.valeriana.databinding.FragmentProfileBinding
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.database.*

class ProfileFragment : Fragment(), AdapterView.OnItemClickListener {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var binding: FragmentProfileBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private var imageUri:Uri? = null
    private lateinit var progressDialog: ProgressDialog
    private lateinit var cld : ConnectionLiveData


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        progressDialog = ProgressDialog(context)
        progressDialog.setTitle("Please wait")
        progressDialog.setCanceledOnTouchOutside(false)
        firebaseAuth = FirebaseAuth.getInstance()
        checkNetworkConnection()

    }

    private fun checkNetworkConnection(){
        cld = ConnectionLiveData(requireActivity().application)
        cld.observe(this) { isConnected ->
            if (isConnected) {
                //Toast.makeText(context, "Si hay internet", Toast.LENGTH_SHORT).show()
            } else {
                val dialogBinding = layoutInflater.inflate(R.layout.alert_internet, null)
                val myDialog = Dialog(requireContext())
                myDialog.setContentView(dialogBinding)
                myDialog.setCancelable(true)
                myDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                myDialog.show()
                val yesbtn = dialogBinding.findViewById<Button>(R.id.alert_yes)
                yesbtn.setOnClickListener {
                    myDialog.dismiss()
                }

            }
        }
    }

    private var name = ""
    private var aboutMe = ""
    private var direccion = ""
    private var category = ""

    private fun validateData() {
        //get data
        name = binding.tieNombre.text.toString().trim()
        aboutMe = binding.tieaboutMe.text.toString().trim()
        direccion = binding.tiedireccion.text.toString().trim()
        category = binding.actvTag.text.toString().trim()
        if (name.isEmpty()){
            Toast.makeText(context,"Ingresa tu nombre",Toast.LENGTH_SHORT).show()
        } else if (aboutMe.isEmpty()){
            Toast.makeText(context,"Ingresa una reseña de ti",Toast.LENGTH_SHORT).show()
        } else if (direccion.isEmpty()){
            Toast.makeText(context,"Ingresa tu direccion",Toast.LENGTH_SHORT).show()
        }
        else{
            if (imageUri == null){
                updateProfile("")
            }
            else{
                uploadImage()
            }
        }
    }

    private fun uploadImage() {
        progressDialog.setMessage("Uploading profile image")
        progressDialog.show()
        val filePathAndName = "ProfileImages/"+firebaseAuth.uid
        val reference = FirebaseStorage.getInstance().getReference(filePathAndName)
        reference.putFile(imageUri!!)
            .addOnSuccessListener { taskSnapshot->
                //obtener url de la imagen
                val uriTask: Task<Uri> = taskSnapshot.storage.downloadUrl
                while(!uriTask.isSuccessful);
                val uploadedImageUrl = "${uriTask.result}"
                updateProfile(uploadedImageUrl)
            }
            .addOnFailureListener{
                progressDialog.dismiss()
                Toast.makeText(context,"Error al subir la imagen",Toast.LENGTH_SHORT).show()
            }
    }

    private fun updateProfile(uploadImageUri: String) {
        progressDialog.setMessage("Perfil actualizado")
        //setup info
        val hashMmap: HashMap<String, Any> = HashMap()
        hashMmap["name"] = "$name"
        hashMmap["aboutMe"] = "$aboutMe"
        hashMmap["direccion"] = "$direccion"
        hashMmap["userType"] = "$category"
        if (imageUri != null){
            hashMmap["profileImage"] = uploadImageUri
        }
        val reference = FirebaseDatabase.getInstance().getReference("users")
        reference.child(firebaseAuth.uid!!)
            .updateChildren(hashMmap)
            .addOnSuccessListener {
                //profile update
                progressDialog.dismiss()
                Toast.makeText(context,"Perfil actualizado",Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                progressDialog.dismiss()
                Toast.makeText(context,"Error al actualizar perfil",Toast.LENGTH_SHORT).show()
            }
    }

    private fun loadUserInfo() {
        val uid = firebaseAuth.uid
        val ref = FirebaseDatabase.getInstance().getReference("users")
        ref.child(uid!!)
            .addValueEventListener(object: ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    val email = "${snapshot.child("email").value}"
                    val name = "${snapshot.child("name").value}"
                    val secondName = "${snapshot.child("secondName").value}"
                    val profileImage = "${snapshot.child("profileImage").value}"
                    val userType = "${snapshot.child("userType").value}"
                    val aboutMe = "${snapshot.child("aboutMe").value}"
                    val direccion = "${snapshot.child("direccion").value}"
                    val uid = "${snapshot.child("uid").value}"
                    val timestamp = "${snapshot.child("timesStamp").value}"

                    binding.tieNombre.setText(name)
                    binding.tieaboutMe.setText(aboutMe)
                    binding.tiedireccion.setText(direccion)
                    binding.actvTag.setText(userType)

                    try {
                        Glide.with(this@ProfileFragment)
                            .load(profileImage)
                            .placeholder(R.drawable.ic_profiledefault)
                            .into(binding.profileImage)
                    }
                    catch (e: Exception){

                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
    }

    private fun showImageAttachMenu(){
        val popupMenu = PopupMenu(context, binding.profileImage)
        popupMenu.menu.add(Menu.NONE, 0, 0, "Camera")
        popupMenu.menu.add(Menu.NONE, 1, 1, "Gallery")
        popupMenu.show()
        popupMenu.setOnMenuItemClickListener { item->
            val id = item.itemId
            if (id == 0){
                //Camera
                pickImageCamera()
            }
            else if (id == 1){
                pickImageGallery()
            }
            true
        }
    }

    private fun pickImageGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        galleryActivityResultLauncher.launch(intent)
    }

    private fun pickImageCamera() {
        val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE, "Temp_Title")
        values.put(MediaStore.Images.Media.DESCRIPTION, "Temp_Description")

        imageUri = requireActivity().contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
        cameraActivityResultLauncher.launch(intent)

    }

    private val cameraActivityResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult(),
        ActivityResultCallback<ActivityResult> { result ->
            if (result.resultCode == Activity.RESULT_OK){
                val data = result.data
                //imageUri = data!!.data

                binding.profileImage.setImageURI(imageUri)
            }
            else{
                Toast.makeText(context, "Cancelled", Toast.LENGTH_SHORT).show()
            }
        }
    )

    private val galleryActivityResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult(),
        ActivityResultCallback<ActivityResult> { result ->
            if (result.resultCode == Activity.RESULT_OK){
                val data = result.data
                imageUri = data!!.data

                binding.profileImage.setImageURI(imageUri)
            }
            else{
                Toast.makeText(context, "Cancelled", Toast.LENGTH_SHORT).show()
            }
        }
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater , container ,false)
        binding.btnLogout.setOnClickListener {
            firebaseAuth.signOut()
            val intent = Intent(this@ProfileFragment.requireContext(), LoginActivity::class.java)
            startActivity(intent)
            val manager = requireActivity().supportFragmentManager
            manager.beginTransaction().remove(this).commit()

            //finish()
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadUserInfo()
        val tags = resources.getStringArray(R.array.tags_category)
        val adapter = ArrayAdapter(view.context, R.layout.items_category, tags)
        binding.profileImage.setOnClickListener {
            showImageAttachMenu()
        }
        binding.guardarBtn.setOnClickListener {
            validateData()
        }

        with(binding.actvTag){
            setAdapter(adapter)
            onItemClickListener = this@ProfileFragment
        }
    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        //val item = parent?.getItemAtPosition(position).toString()
        //Toast.makeText(context,item, Toast.LENGTH_SHORT).show()
    }

}