package ru.sergdeev.tasklistcompose

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel
import ru.sergdeev.tasklistcompose.domain.DataItem
import ru.sergdeev.tasklistcompose.domain.UserCase
import ru.sergdeev.tasklistcompose.ui.theme.TaskListComposeTheme
import java.text.SimpleDateFormat
import java.util.Date

class MainViewModel(val userCase: UserCase) : ViewModel() {
    fun itemChangeEnabled(item: DataItem) {
        val newItem: DataItem = item.copy()
        newItem.enabled = !newItem.enabled
        this.viewModelScope.launch {
            userCase.addDataItem(newItem)
        }
    }
    fun itemDelete(item: DataItem) {
        this.viewModelScope.launch {
            userCase.deleteItem(item)
        }
    }
}

class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "UnrememberedMutableState")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
        setContent {
            val vm: MainViewModel = koinViewModel()
            val dataItems = mutableStateOf<List<DataItem>>(emptyList())
            vm.userCase.getItems().observe(this, Observer {
                dataItems.value = it
                Log.d("SQL", dataItems.value.toString())
            })
            TaskListComposeTheme {
                Scaffold(
                    floatingActionButtonPosition = FabPosition.End,
                    floatingActionButton = {
                        FloatingActionButton(onClick =
                            { startActivity(Intent(this, EditActivity::class.java))},
                            modifier = Modifier
                                .padding(30.dp)
                                .size(80.dp))
                        {
                            Image(
                                painter = painterResource(id = R.drawable.ic_add_foreground),
                                contentDescription = stringResource(id = R.string.add),
                                contentScale = ContentScale.Fit,
                            )
                        }
                    }
                )
                {
                    LazyColumn(Modifier.fillMaxWidth()
                    ) {
                        items(dataItems.value) { it ->
                            DrawItem(
                                it,
                                onClick = { itemDetail(this@MainActivity, it) },
                                onLongClick = { vm.itemChangeEnabled(it) },
                                onSwipe = {vm.itemDelete(it)},
                            )
                        }
                    }
                }
            }
        }
    }

    @SuppressLint("UnsafeIntentLaunch")
    fun itemDetail(context: Context, item: DataItem) {
        intent = Intent(context, ShowActivity::class.java)
        val bundle = Bundle()
        bundle.putInt("ID", item.id)
        bundle.putString("TITLE", item.title)
        bundle.putString("DESC", item.desc)
        bundle.putLong("DT", item.dt)
        intent.putExtra("DATA", bundle)

        startActivity(intent)
    }
}


@SuppressLint("SimpleDateFormat", "UnrememberedMutableState")
@Composable
fun DrawItem(item: DataItem,
             onClick:(item: DataItem)-> Unit,
             onLongClick:(item: DataItem)-> Unit,
             onSwipe:(item: DataItem)->Unit
) {
    var bgColor: Color = colorResource(R.color.teal_700)
    if (!item.enabled) bgColor = colorResource(R.color.purple_200)
    var txtDateTime = ""
    if (item.dt > 0) {
        val format = SimpleDateFormat("dd.MM.yy HH:mm")
        txtDateTime = format.format(Date(item.dt))
    }
    val offsetX = mutableIntStateOf(0)
    Row (
        modifier = Modifier
            .offset { IntOffset(offsetX.value, 0) }
            .fillMaxWidth()
            .padding(5.dp)
            .border(1.dp, Color.Black, RoundedCornerShape(15.dp))
            .clip(RoundedCornerShape(15.dp))
            .background(bgColor)
            .padding(10.dp)
            .combinedClickable(
                true,
                onClick = { onClick(item) },
                onLongClick = { onLongClick(item) }
            )
            .draggable(
                state = rememberDraggableState { delta ->offsetX.value += delta.toInt() },
                onDragStopped = { onSwipe(item)},
                startDragImmediately = false,
                enabled = true,
                orientation = Orientation.Horizontal,
            )
            ) {
                Box(contentAlignment = Alignment.TopStart, modifier = Modifier.weight(3f)) {
                    Text(item.title, style = MaterialTheme.typography.titleLarge)
                }
                Box(contentAlignment = Alignment.Center, modifier = Modifier.weight(1f)) {
                    Text(txtDateTime, style = MaterialTheme.typography.titleSmall, softWrap = false)
                }
            }
}

