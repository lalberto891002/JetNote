package com.mypc.jetnote.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mypc.jetnote.R
import com.mypc.jetnote.components.NoteButton
import com.mypc.jetnote.components.NoteInputText


@Composable
fun NoteScreen(){
    var title by remember{
        mutableStateOf(" ")
    }

    var description by remember {
        mutableStateOf("")
    }

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
                onImeAction = {},
                modifier = Modifier.padding(
                    top = 9.dp,
                    bottom = 8.dp
                )
            )
            NoteButton(text = "Save", onClick = { /*TODO*/ }, enabled = true )
        }


    }

}


@Preview(showBackground = true)
@Composable
fun NotesScreenPreview() {
    NoteScreen()
}
