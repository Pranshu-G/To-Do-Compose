package com.example.todo.ui.screens.list

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.SnackbarResult
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.todo.R
import com.example.todo.ui.theme.fabBackgroundColor
import com.example.todo.ui.viewmodels.SharedViewModel
import com.example.todo.util.Action
import com.example.todo.util.SearchAppBarState
import kotlinx.coroutines.launch


@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ListScreen(
    navigateToTaskScreen: (taskId: Int) -> Unit,
    sharedViewModel: SharedViewModel
) {

    LaunchedEffect(key1 = true, block = {
        Log.d("List Screen", "LaunchedEffect Triggered")
        sharedViewModel.getAllTasks()
        sharedViewModel.readSortState()
    })

    val action by sharedViewModel.action
    val allTasks by sharedViewModel.allTasks.collectAsState()
    val searchedTasks by sharedViewModel.searchedTasks.collectAsState()
    val sortState by sharedViewModel.sortState.collectAsState()
    val lowPriorityTasks by sharedViewModel.lowPriorityTask.collectAsState()
    val highPriorityTasks by sharedViewModel.highPriorityTask.collectAsState()

    val searchAppBarState: SearchAppBarState
            by sharedViewModel.searchAppBarState
    val searchTextState: String by sharedViewModel.searchTextState
    val scaffoldState = rememberScaffoldState()

    DisplaySnackBar(
        scaffoldState = scaffoldState,
        handleDatabaseActions = { sharedViewModel.handleDatabaseAction(action = action) },
        taskTitle = sharedViewModel.title.value,
        onUndoClicked = {
                        sharedViewModel.action.value=it
        },
        action = action
    )
    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            ListAppBar(
                sharedViewModel = sharedViewModel,
                searchAppBarState = searchAppBarState,
                searchTextState = searchTextState
            )
        },
        content = {
            ListContent(
                allTasks = allTasks,
                searchedTasks=searchedTasks,
                searchAppBarState= searchAppBarState,
                navigateToTaskScreen = navigateToTaskScreen,
                lowPriorityTask=lowPriorityTasks,
                highPriorityTask=highPriorityTasks,
                sortState=sortState,
                onSwipeToDelete = {
                    action,task->
                        sharedViewModel.action.value=action
                    sharedViewModel.updateTaskFields(selectedTask = task)
                }
            )
        },
        floatingActionButton = {
            ListFab(onFabClicked = navigateToTaskScreen)
        }
    )
}

@Composable
fun ListFab(
    onFabClicked: (taskId: Int) -> Unit
) {
    FloatingActionButton(
        onClick = {
            onFabClicked(-1)
        },
        backgroundColor = MaterialTheme.colors.fabBackgroundColor
    ) {
        Icon(
            imageVector = Icons.Filled.Add,
            contentDescription = stringResource(id = R.string.add_button),
            tint = Color.White
        )
    }
}

@Composable
fun DisplaySnackBar(
    scaffoldState: ScaffoldState,
    handleDatabaseActions: () -> Unit,
    taskTitle: String,
    onUndoClicked: (Action) -> Unit,
    action: Action
) {
    handleDatabaseActions()
    val scope = rememberCoroutineScope()
    LaunchedEffect(key1 = action, block = {
        if (action != Action.NO_ACTION) {
            scope.launch {
                val snackBarResult = scaffoldState.snackbarHostState.showSnackbar(
                    message = setMessage(action=action,taskTitle=taskTitle),
                    actionLabel = setActionLabel(action=action)
                )
                undoDeletedTask(
                    action=action,
                    snackBarResult=snackBarResult,
                    onUndoClicked=onUndoClicked
                )
            }
        }
    })
}

private fun setMessage(action: Action, taskTitle:String):String{

    return when(action) {
        Action.DELETE_ALL -> "All Tasks Removed"
        else -> "${action.name}: $taskTitle"
    }

}

private fun setActionLabel(action:Action):String{
    return if(action.name=="DELETE"){
        "UNDO"
    }else{
        "OK"
    }
}

private fun undoDeletedTask(
    action:Action,
    snackBarResult: SnackbarResult,
    onUndoClicked:(Action)-> Unit
){
    if(snackBarResult==SnackbarResult.ActionPerformed
        && action==Action.DELETE){
        onUndoClicked(Action.UNDO)
    }
}
//@Composable
//@Preview
//private fun ListScreenPreview(){
//    ListScreen(navigateToTaskScreen = {})
//}
