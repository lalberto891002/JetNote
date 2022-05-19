package com.mypc.jetnote.screen

import android.util.Log
import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.mypc.jetnote.R
import com.mypc.jetnote.components.NoteButton
import com.mypc.jetnote.components.NoteInputText
import com.mypc.jetnote.data.NotesDataSource
import com.mypc.jetnote.model.Note
import com.mypc.jetnote.util.formatDate
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.format.DateTimeFormatter


@OptIn(ExperimentalAnimationApi::class, ExperimentalMaterialApi::class,
    ExperimentalComposeUiApi::class
)
@Composable
fun NoteScreen(
    notes:List<Note>,
    onAddNote:(Note) -> Unit,
    onRemoveNote:(Note) -> Unit,
    onUpdateNote:(Note) -> Unit
){
    var title by remember{
        mutableStateOf(" ")
    }

    var description by remember {
        mutableStateOf("")
    }

    var updating by remember {
        mutableStateOf(false)
    }

    var note_id by remember {
        mutableStateOf("")
    }

    var noteMem  by remember {
        mutableStateOf(Note(title = "", description = ""))
    }

    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current
    Column(modifier = Modifier.padding(6.dp)) {
        TopAppBar(title = {
                          Text(text = stringResource(id = R.string.app_name))

        },actions = {
            Icon(imageVector = Icons.Rounded.Notifications, contentDescription = "Icon")
        },
        backgroundColor = Color(0XFFDADFE3)
        )

        //content
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally) {
                NoteInputText(text = title,label = "Title",
                    onTextChange = {
                        if(it.all { char->
                                char.isLetter() || char.isWhitespace()
                            })  title = it
                    },
                    onImeAction = {}
                )

            NoteInputText(text = description,label = "Add a Note",
                onTextChange = {
                      if(it.all { char->
                              char.isLetter() || char.isWhitespace()
                          })  description = it
                },
                onImeAction = {
                    keyboardController?.hide()
                },
                modifier = Modifier.padding(
                    top = 9.dp,
                    bottom = 8.dp
                )
            )
            if(!updating) {
                NoteButton(text = "Save", onClick = {
                    if (title.isNotEmpty() && description.isNotEmpty()) {
                        onAddNote(Note(title = title, description = description))
                        Toast.makeText(
                            context,
                            "Added note with title:$title",
                            Toast.LENGTH_LONG
                        ).show()
                        title = ""
                        description = ""
                        keyboardController?.hide()
                    }
                }, enabled = true)
            }else {
                NoteButton(text = "Update", onClick = {
                    if (title.isNotEmpty() && description.isNotEmpty()) {

                        noteMem.title = title
                        noteMem.description = description
                        onUpdateNote(noteMem)
                        Toast.makeText(
                            context,
                            "Updated note with title:$title",
                            Toast.LENGTH_LONG
                        ).show()
                        title = ""
                        description = ""
                        keyboardController?.hide()
                    }
                    updating = false
                }, enabled = true)
            }
        }

        Divider(modifier = Modifier.padding(10.dp) )

        LazyColumn {

            itemsIndexed(items = notes,key = {
                index,item ->
                item.hashCode()
            }){ index,note ->
                val coroutineScope = rememberCoroutineScope()
                var showed by remember {
                    mutableStateOf(false)
                }
                var deleted by remember {
                    mutableStateOf(false)
                }

                val dismissState = rememberDismissState(
                    confirmStateChange = {
                        if(it == DismissValue.DismissedToEnd){
                            showed = false
                            deleted = true
                            onRemoveNote(note)
                            Log.d("removed note","Note ${note.title} removed")
                            true
                        }else
                            false

                    }
                )

                SwipeToDismiss(
                    state = dismissState,
                    directions = setOf(DismissDirection.StartToEnd),
                    background = {
                        val direction = dismissState.dismissDirection?:return@SwipeToDismiss
                        val color by animateColorAsState(targetValue = when(dismissState.targetValue){
                            DismissValue.Default -> Color.LightGray
                            DismissValue.DismissedToEnd -> Color.Blue
                            DismissValue.DismissedToStart ->Color.LightGray
                        })
                        val icon = when(direction){
                            DismissDirection.StartToEnd -> Icons.Default.Done
                            DismissDirection.EndToStart -> Icons.Default.Delete

                        }
                        val scale by animateFloatAsState(
                            targetValue =
                            if(dismissState.targetValue == DismissValue.Default) 0.8f else 1.2f)
                        val alignment = when(direction){
                            DismissDirection.EndToStart -> Alignment.CenterEnd
                            DismissDirection.StartToEnd -> Alignment.CenterStart
                        }
                        Box(modifier = Modifier
                            .fillMaxSize()
                            .background(color)
                            .padding(start = 12.dp, end = 12.dp),
                            contentAlignment = alignment){
                            Icon(icon, contentDescription ="Icon",Modifier.scale(scale))
                        }
                    },
                    dismissContent = {
                        NoteRow(note = note, onNoteClicked = {
                           /* showed = false
                            coroutineScope.launch {
                                delay(100)
                                onRemoveNote(note)
                            }*/

                        }, visible = showed, onNoteLongPressed = {
                            noteMem = note
                            title = noteMem.title
                            description = noteMem.description
                            note_id = noteMem.id.toString()
                            updating = true

                        })


                    })

                coroutineScope.launch { //to show the note content delayed

                    delay(100)
                    if(!deleted)
                        showed = true
                }
            }
        }

    }

}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun NoteRow(
    modifier:Modifier = Modifier,
    note:Note,
    visible:Boolean,
    onNoteClicked: (Note) -> Unit,
    onNoteLongPressed: (Note) -> Unit){
    AnimatedVisibility(
        visible = visible,
        enter = slideIn(initialOffset = {fullSize->
            IntOffset(x = fullSize.width/2,y = 0)
        },
            animationSpec = tween(durationMillis = 500, easing = LinearOutSlowInEasing)
        ),
        exit = slideOut(
            targetOffset = {
                fullSize ->
                IntOffset(-fullSize.width/2,0)
            },
            animationSpec = tween(durationMillis = 500, easing = LinearOutSlowInEasing)
        )

    ) {
        Surface(
            modifier
                .padding(4.dp)
                .clip(RoundedCornerShape(topEnd = 33.dp, bottomStart = 33.dp))
                .fillMaxWidth()
                .animateEnterExit(enter = slideInHorizontally(), exit = slideOutHorizontally()),
            color = Color(color = 0xFFDFE6EB),
            elevation = 6.dp) {
            Column(
                modifier
                    .clickable {
                        //onNoteClicked(note)

                    }
                    .padding(
                        horizontal = 14.dp,
                        vertical = 6.dp
                    )
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onPress = { /* Called when the gesture starts */ },
                            onDoubleTap = { /* Called on Double Tap */ },
                            onLongPress = {
                                Log.d("noteLongPress", "Long pressed note ${note.title}")
                                onNoteLongPressed(note)
                            },
                            onTap = { /* Called on Tap */ }
                        )
                    }
                ,
                horizontalAlignment = Alignment.Start) {
                Text(text = note.title,style = MaterialTheme.typography.subtitle2)
                Text(text = note.description,style = MaterialTheme.typography.subtitle1)
                Text(text = formatDate(note.entryDate.time),
                    style = MaterialTheme.typography.caption)
            }

        }
    }

    }


@Preview(showBackground = true)
@Composable
fun NotesScreenPreview() {
    NoteScreen(notes = NotesDataSource().loadNotes(), onAddNote = {}, onRemoveNote = {}, onUpdateNote = {})

}
