package com.example.editimages.ui

import android.content.ContentValues
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import com.example.editimages.databinding.FragmentEditBinding
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

class editFragment : Fragment() {
    private lateinit var binding: FragmentEditBinding

    private val photoPickerLauncher = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        uri?.let { uri ->
            val bitmap = MediaStore.Images.Media.getBitmap(requireContext().contentResolver, uri)
            binding.image.setInitialBitmap(bitmap)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEditBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnselect.setOnClickListener {
            photoPickerLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        binding.seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                val strokeWidth = progress.toFloat()
                binding.image.updateStrokeWidth(strokeWidth)
                binding.masksize.text = strokeWidth.toString()


            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })

        binding.btnsave.setOnClickListener {
            saveImageToStorage()
        }

    }

    private fun saveImageToStorage() {
        val bitmap = binding.image.bitmap
        if (bitmap != null) {
            val filename = "edited_image_${System.currentTimeMillis()}.png"
            val fos: OutputStream? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val resolver = requireContext().contentResolver
                val contentValues = ContentValues().apply {
                    put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
                    put(MediaStore.MediaColumns.MIME_TYPE, "image/png")
                    put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
                }
                val imageUri: Uri? = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
                resolver.openOutputStream(imageUri!!)
            } else {
                val imagesDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString()
                val image = File(imagesDir, filename)
                FileOutputStream(image)
            }
            fos?.use {
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, it)
            }
        }
    }



}

