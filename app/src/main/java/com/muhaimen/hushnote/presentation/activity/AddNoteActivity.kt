package com.muhaimen.hushnote.presentation.activity

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.chip.Chip
import com.muhaimen.hushnote.R
import com.muhaimen.hushnote.data.dataclass.Note
import com.muhaimen.hushnote.databinding.ActivityAddNoteBinding
import com.muhaimen.hushnote.viewModel.NoteViewModel

class AddNoteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddNoteBinding
    private var imageUri: Uri? = null
    private val notesViewModel: NoteViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAddNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.galleryButton.setOnClickListener {
            pickImageLauncher.launch("image/*")
        }

        binding.cameraButton.setOnClickListener {
            imageUri = createImageUri(this)
            imageUri?.let {
                captureImageLauncher.launch(it)
            }
        }

        binding.addTagButton.setOnClickListener {
            val tag = binding.tagInput.text.toString().trim()
            if (tag.isNotEmpty()) {
                addTag(tag)
                binding.tagInput.text?.clear()
            }
        }


        binding.saveButton.setOnClickListener {
            val title = binding.noteTitle.text.toString()
            val content = binding.noteContent.text.toString()
            val timeStamp = System.currentTimeMillis()
            val imageUrl = imageUri?.toString() ?: ""
            val tags = mutableListOf<String>()

            for (i in 0 until binding.tagChipGroup.childCount) {
                val chip = binding.tagChipGroup.getChildAt(i) as Chip
                tags.add(chip.text.toString())
            }
            val note = Note(
                title = title,
                content = content,
                imageUrl = imageUrl,
                timeStamp = timeStamp,
                tags = tags,
            )
            notesViewModel.addNote(note)
            finish()
        }
    }

    private fun addTag(tag: String) {
        val chip = Chip(this)
        chip.text = tag
        chip.isCloseIconVisible = true
        chip.setOnCloseIconClickListener {
            binding.tagChipGroup.removeView(chip)
        }
        binding.tagChipGroup.addView(chip)
    }

    private val pickImageLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            contentResolver.takePersistableUriPermission(
                it,
                Intent.FLAG_GRANT_READ_URI_PERMISSION
            )
            imageUri = it
            displayImage(it)
        }
    }

    private val captureImageLauncher = registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        if (success && imageUri != null) {
            displayImage(imageUri!!)
        } else {
            Toast.makeText(this, "Failed to capture image.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun displayImage(uri: Uri) {
        binding.noteImage.apply {
            visibility = View.VISIBLE
            try {
                val source = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    ImageDecoder.createSource(contentResolver, uri)
                } else {
                    null
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    setImageDrawable(ImageDecoder.decodeDrawable(source!!))
                }
            } catch (e: Exception) {
                e.printStackTrace()
                setImageURI(uri)
            }
        }
    }


    private fun createImageUri(context: Context): Uri? {
        val contentValues = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, "hushnote_${System.currentTimeMillis()}.jpg")
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
        }
        return context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
    }
}
