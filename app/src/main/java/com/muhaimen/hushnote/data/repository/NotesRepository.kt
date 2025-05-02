package com.muhaimen.hushnote.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.muhaimen.hushnote.data.dataclass.Note

class NotesRepository {

    private val db = FirebaseFirestore.getInstance()
    private val notesCollection = db.collection("notes")

    fun addNote(note: Note, callback: (Boolean, String?) -> Unit) {
        notesCollection.add(note)
            .addOnSuccessListener { documentReference ->
                callback(true, documentReference.id)
            }
            .addOnFailureListener { e ->
                callback(false, e.message)
            }
    }

    fun getNotes(callback: (List<Note>?, String?) -> Unit) {
        notesCollection.get()
            .addOnSuccessListener { querySnapshot ->
                val notes = querySnapshot.toObjects(Note::class.java)
                callback(notes, null)
            }
            .addOnFailureListener { e ->
                callback(null, e.message)
            }
    }

    fun updateNote(noteId: String, updatedNote: Note, callback: (Boolean, String?) -> Unit) {
        notesCollection.document(noteId).set(updatedNote)
            .addOnSuccessListener {
                callback(true, null)
            }
            .addOnFailureListener { e ->
                callback(false, e.message)
            }
    }

    fun deleteNote(noteId: String, callback: (Boolean, String?) -> Unit) {
        notesCollection.document(noteId).delete()
            .addOnSuccessListener {
                callback(true, null)
            }
            .addOnFailureListener { e ->
                callback(false, e.message)
            }
    }



}