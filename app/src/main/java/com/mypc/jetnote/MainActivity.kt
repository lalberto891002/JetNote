package com.mypc.jetnote

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mypc.jetnote.screen.NoteScreen
import com.mypc.jetnote.screen.NoteViewModel
import com.mypc.jetnote.ui.theme.JetNoteTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JetNoteTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    val notesViewModel:NoteViewModel by viewModels()
                    NotesApp(notesViewModel = notesViewModel)

                }
            }
        }

    }
}

@Composable
fun NotesApp(notesViewModel: NoteViewModel = viewModel()){
    val notesList = notesViewModel.getAllNotes()
    NoteScreen(
        notes = notesList,
        onRemoveNote = {
            notesViewModel.removeNote(it)
                    },
        onAddNote = {
            notesViewModel.addNote(it)
        })
}



@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    JetNoteTheme {
        Greeting("Android")
    }
}