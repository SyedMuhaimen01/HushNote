package com.muhaimen.hushnote.presentation.activity

import android.net.Uri
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.muhaimen.hushnote.R
import com.muhaimen.hushnote.data.dataclass.Note
import com.muhaimen.hushnote.databinding.ActivityAddNoteBinding
import com.muhaimen.hushnote.viewModel.NoteViewModel

class AddNoteActivity : AppCompatActivity() {

    private lateinit var  binding: ActivityAddNoteBinding
    private lateinit var imageUri: Uri
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
            captureImageLauncher.launch(imageUri)
        }

        binding.saveButton.setOnClickListener {
            val title = binding.noteTitle.text.toString()
            val content = binding.noteContent.text.toString()
            val timeStamp = System.currentTimeMillis()
            val  imageUrl = imageUri.toString()

            val note = Note(
                title = title,
                content = content,
                imageUrl = imageUrl,
                timeStamp = timeStamp,
            )
            notesViewModel.addNote(note)
            finish()
        }

    }

    private val pickImageLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            imageUri = it
            binding.noteImage.setImageURI(imageUri)
        }
    }

    private val captureImageLauncher = registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        if (success) {
            binding.noteImage.setImageURI(imageUri)
        }
    }
}