package com.example.todo.ui.task

import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.room.Update
import com.example.todo.R
import com.example.todo.components.DisplayAlertDialog
import com.example.todo.data.models.Priority
import com.example.todo.data.models.ToDoTask
import com.example.todo.ui.theme.topAppBarBackgroundColor
import com.example.todo.ui.theme.topAppBarContentColor
import com.example.todo.util.Action

@Composable
fun TaskAppBar(selectedTask:ToDoTask?,navigateToListScreen: (Action) -> Unit){
    if(selectedTask==null){
        NewTaskAppBar(navigateToListScreen = navigateToListScreen)
    }
    else{
        ExistingTaskAppBar(selectedTask = selectedTask ,
            navigateToListScreen = navigateToListScreen)
    }

}

@Composable
fun NewTaskAppBar(
    navigateToListScreen: (Action)->Unit
){
    TopAppBar(
        navigationIcon = {
            BackAction(onBackClicked = navigateToListScreen)
        },
        title={
            Text(text="Add Task",
            color=MaterialTheme.colors.topAppBarContentColor
            )},
        backgroundColor = MaterialTheme.colors.topAppBarBackgroundColor,
        actions = {
            AddAction(onAddClicked = navigateToListScreen)
        }

    )
}

@Composable
fun ExistingTaskAppBar(
    selectedTask:ToDoTask,
    navigateToListScreen: (Action)->Unit
){
    TopAppBar(
        navigationIcon = {
            CloseAction(onCloseClicked = navigateToListScreen)
        },
        title={
            Text(text=selectedTask.title,
                color=MaterialTheme.colors.topAppBarContentColor,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )},
        backgroundColor = MaterialTheme.colors.topAppBarBackgroundColor,
        actions = {
            ExistingTaskAppBarAction(
                selectedTask=selectedTask,
                navigateToListScreen=navigateToListScreen
            )

        }

    )
}

@Composable
fun ExistingTaskAppBarAction(
    selectedTask:ToDoTask,
    navigateToListScreen: (Action)->Unit
){
    var openDialog by remember {
        mutableStateOf(false)
    }
    DisplayAlertDialog(
        title = stringResource(id = R.string.delete_task, selectedTask.title),
        message = stringResource(id = R.string.delete_task_confirmation,
            selectedTask.title
        ),
        openDialog = openDialog,
        closeDialog = { openDialog = false },
        onYesClicked = {navigateToListScreen(Action.DELETE)}
        )
    DeleteAction(onDeleteClicked ={
        openDialog=true
    })
    UpdateAction(onUpdateClicked = navigateToListScreen)
}

@Composable
fun BackAction(
    onBackClicked:(Action)->Unit
){
        IconButton(onClick = {
            onBackClicked(Action.NO_ACTION)
        }) {
            Icon(imageVector = Icons.Filled.ArrowBack,
                contentDescription = "Back Arrow",
                tint=MaterialTheme.colors.topAppBarContentColor
                )
        }
}

@Composable
fun CloseAction(
    onCloseClicked:(Action)->Unit
){
    IconButton(onClick = {
        onCloseClicked(Action.NO_ACTION)
    }) {
        Icon(imageVector = Icons.Filled.Close,
            contentDescription = "Close Icon",
            tint=MaterialTheme.colors.topAppBarContentColor
        )
    }
}

@Composable
fun DeleteAction(
    onDeleteClicked:()->Unit
){
    IconButton(onClick = {
        onDeleteClicked()
    }) {
        Icon(imageVector = Icons.Filled.Delete,
            contentDescription = "Delete Icon",
            tint=MaterialTheme.colors.topAppBarContentColor
        )
    }
}

@Composable
fun UpdateAction(
    onUpdateClicked:(Action)->Unit
){
    IconButton(onClick = {
        onUpdateClicked(Action.UPDATE)
    }) {
        Icon(imageVector = Icons.Filled.Check,
            contentDescription = "Update Icon",
            tint=MaterialTheme.colors.topAppBarContentColor
        )
    }
}


@Composable
fun AddAction(
    onAddClicked:(Action)->Unit
){
    IconButton(onClick = {
        onAddClicked(Action.ADD)
    }) {
        Icon(imageVector = Icons.Filled.Check,
            contentDescription = "Add Task",
            tint=MaterialTheme.colors.topAppBarContentColor
        )
    }
}


@Composable
@Preview
fun newTaskAppBarPreview(){
    NewTaskAppBar(navigateToListScreen = {})
}

@Composable
@Preview
fun ExistingTaskAppBarPreview(){
    ExistingTaskAppBar(navigateToListScreen = {},
        selectedTask=ToDoTask(
            id=0,
        title="some title",
        description="Random title",
        priority=Priority.LOW
    ))
}