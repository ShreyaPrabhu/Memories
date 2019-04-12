package com.example.shreya.makememories.fragments

import android.content.Intent
import android.graphics.Bitmap
import android.media.MediaScannerConnection
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.shreya.makememories.R
import com.example.shreya.makememories.databinding.FragmentAddMemoryBinding
import com.example.shreya.makememories.room.MemoryEntity
import com.example.shreya.makememories.viewmodel.MemoryViewModel
import timber.log.Timber
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*

class AddMemoryFragment : Fragment() {

    // Image selection criteria variables
    private val GALLERY = 1
    private val CAMERA = 2
    private var imagePath: String = R.string.image_path.toString()
    private lateinit var memoryViewModel: MemoryViewModel

    private lateinit var binding: FragmentAddMemoryBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {


        binding = DataBindingUtil.inflate<FragmentAddMemoryBinding>(
                inflater, R.layout.fragment_add_memory, container, false)

        binding.imageUploaded.setOnClickListener{
            showPictureDialog()
        }

        binding.addImageButton.setOnClickListener{
            // TODO: Save data into database
            //if(binding.imageCaption.text.toString().equals("") || binding.imageCaption.equals(null)  || binding.imageCaption.text.toString().equals("Image Caption") || binding.imageDescription.text.toString().equals("") || binding.imageDescription.equals(null) || binding.imageDescription.text.toString().equals("Image Description")){
            if(binding.imageCaption.text.toString().isEmpty()  || binding.imageCaption.text.toString().equals("Image Caption")
                    || binding.imageDescription.text.toString().isEmpty() || binding.imageDescription.text.toString().equals("Image Description")
                    || imagePath.equals(R.string.image_path.toString())){
                // Show Alert
                Toast.makeText(requireContext(), "All fields required!!!", Toast.LENGTH_SHORT).show()
            }
            else{
                // Save into DB
                val memoryEntity: MemoryEntity = MemoryEntity(imagePath,binding.imageCaption.text.toString(), binding.imageDescription.text.toString())
                Timber.i("""Memory: memoryentity: ${memoryEntity.id}$imagePath${memoryEntity.imageCaption}${memoryEntity.imageDescription}""")
                memoryViewModel = ViewModelProviders.of(this).get(MemoryViewModel::class.java)
                memoryViewModel.insert(memoryEntity)
                Toast.makeText(requireContext(), "Memory Saved!!!", Toast.LENGTH_SHORT).show()
                Handler().postDelayed({
                    fragmentManager?.popBackStack()
                }, 200)

            }
        }

        return binding.root

    }

    private fun showPictureDialog() {
        val pictureDialog = AlertDialog.Builder(requireContext())
        pictureDialog.setTitle("Select Action")
        val pictureDialogItems = arrayOf("Select photo from gallery", "Capture photo from camera")
        pictureDialog.setItems(pictureDialogItems
        ) { dialog, which ->
            when (which) {
                0 -> choosePhotoFromGallary()
                1 -> takePhotoFromCamera()
            }
        }
        pictureDialog.show()
    }

    private fun choosePhotoFromGallary() {
        val galleryIntent = Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI)

        startActivityForResult(galleryIntent, GALLERY)
    }

    private fun takePhotoFromCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, CAMERA)
    }

    public override fun onActivityResult(requestCode:Int, resultCode:Int, data: Intent?) {

        super.onActivityResult(requestCode, resultCode, data)
        /* if (resultCode == this.RESULT_CANCELED)
         {
         return
         }*/
        if (requestCode == GALLERY)
        {
            if (data != null)
            {
                val contentURI = data.data
                try
                {
                    val bitmap = MediaStore.Images.Media.getBitmap(requireContext().contentResolver, contentURI)
                    val path = saveImage(bitmap)
                    imagePath = path
                    binding.invalidateAll()
                    binding.imageUploaded.setImageBitmap(bitmap)
                }
                catch (e: IOException) {
                    e.printStackTrace()
                    Toast.makeText(requireContext(), "Failed!", Toast.LENGTH_SHORT).show()
                }

            }

        }
        else if (requestCode == CAMERA)
        {
            val thumbnail = data!!.extras!!.get("data") as Bitmap
            binding.invalidateAll()
            binding.imageUploaded.setImageBitmap(thumbnail)
            //move to fragment
            val path =  saveImage(thumbnail)
            imagePath = path
        }
    }

    fun saveImage(myBitmap: Bitmap):String {
        val bytes = ByteArrayOutputStream()
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes)
        val wallpaperDirectory = File(
                (Environment.getExternalStorageDirectory()).toString() + IMAGE_DIRECTORY)
        // have the object build the directory structure, if needed.
        Timber.i(wallpaperDirectory.toString())
        if (!wallpaperDirectory.exists())
        {

            wallpaperDirectory.mkdirs()
        }

        try
        {
            Timber.i(wallpaperDirectory.toString())
            val f = File(wallpaperDirectory, ((Calendar.getInstance()
                    .getTimeInMillis()).toString() + ".jpg"))
            f.createNewFile()
            val fo = FileOutputStream(f)
            fo.write(bytes.toByteArray())
            MediaScannerConnection.scanFile(context,
                    arrayOf(f.getPath()),
                    arrayOf("image/jpeg"), null)
            fo.close()
            Timber.i("File Saved::--->%s", f.getAbsolutePath())


            return f.getAbsolutePath()
        }
        catch (e1: IOException) {
            e1.printStackTrace()
        }

        return ""
    }

    companion object {
        private val IMAGE_DIRECTORY = "/memories"
    }
}
