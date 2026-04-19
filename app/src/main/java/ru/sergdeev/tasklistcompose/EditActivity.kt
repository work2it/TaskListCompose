package ru.sergdeev.tasklistcompose

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.icu.util.Calendar
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel
import ru.sergdeev.tasklistcompose.domain.DataItem
import ru.sergdeev.tasklistcompose.domain.UserCase
import ru.sergdeev.tasklistcompose.ui.theme.TaskListComposeTheme
import java.text.SimpleDateFormat

class EditViewModel(val userCase: UserCase) : ViewModel() {
    val title = mutableStateOf<String>("")
    val titleErr = mutableStateOf<Boolean>(true)
    val desc = mutableStateOf<String>("")
    val dtText = mutableStateOf<String>("----------")
    val calendar = Calendar.getInstance()
    var _datetime: Long = 0
    var id: Int = 0
    var isInit = false

    fun save() {
        val item: DataItem = DataItem(id, title.value, desc.value, _datetime, true)
        this.viewModelScope.launch {
            userCase.addDataItem(item)
        }
    }
}

class EditActivity : ComponentActivity() {
    @SuppressLint("SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val vm: EditViewModel = koinViewModel()
            if (!vm.isInit) {
                vm.isInit = true
                val bundle: Bundle? = intent.getBundleExtra("DATA")
                if (bundle != null) {
                    vm.id = bundle.getInt("ID")
                    vm.title.value = bundle.getString("TITLE").toString()
                    vm.titleErr.value = vm.title.value.length < 2
                    vm.desc.value = bundle.getString("DESC").toString()
                    vm._datetime = bundle.getLong("DT")
                    if (vm._datetime.toInt() != 0) {
                        val format = SimpleDateFormat("dd.MM.yyyy HH:mm")
                        vm.dtText.value = format.format(vm._datetime)
                    }
                }
            }

            TaskListComposeTheme {
                Column(
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.Start,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp)
                )
                {
                    TextField(
                        value = vm.title.value,
                        onValueChange = {
                            vm.title.value = it
                            vm.titleErr.value = vm.title.value.length < 2 },
                        isError = vm.titleErr.value,
                        enabled = true,
                        label = { Text(stringResource(R.string.title) + "  ("+stringResource(R.string.required)+")") },
                        singleLine = true,
                        textStyle = MaterialTheme.typography.titleLarge,
                        modifier = Modifier
                            .padding(vertical = 10.dp)
                            .fillMaxWidth()
                            .border(1.dp, Color.Black, RoundedCornerShape(15.dp))
                            .clip(RoundedCornerShape(15.dp))
                            .background(colorResource(R.color.teal_700))
                            .padding(10.dp)
                    )
                    TextField(
                        value = vm.desc.value,
                        onValueChange = { vm.desc.value = it },
                        label = { Text(stringResource(R.string.description)) },
                        textStyle = MaterialTheme.typography.titleLarge,
                        modifier = Modifier
                            .padding(vertical = 10.dp)
                            .fillMaxWidth()
                            .border(1.dp, Color.Black, RoundedCornerShape(15.dp))
                            .clip(RoundedCornerShape(15.dp))
                            .background(colorResource(R.color.teal_700))
                            .padding(10.dp)
                    )
                    Text(
                        text = vm.dtText.value,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .padding(vertical = 10.dp)
                            .fillMaxWidth()
                            .border(1.dp, Color.Black, RoundedCornerShape(15.dp))
                            .clip(RoundedCornerShape(15.dp))
                            .background(colorResource(R.color.teal_700))
                            .padding(10.dp)
                    )
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Button(onClick = {
                            val datePickerDialog = DatePickerDialog(this@EditActivity,
                                DatePickerDialog.OnDateSetListener { _, yearSelected, monthOfYear, dayOfMonth ->
                                    vm.calendar.set(java.util.Calendar.YEAR, yearSelected)
                                    vm.calendar.set(java.util.Calendar.MONTH, monthOfYear)
                                    vm.calendar.set(java.util.Calendar.DAY_OF_MONTH, dayOfMonth)
                                    val format = SimpleDateFormat("dd.MM.yyyy HH:mm")
                                    vm.dtText.value = format.format(vm.calendar.time)
                                    vm._datetime = vm.calendar.timeInMillis
                                },
                                vm.calendar.get(java.util.Calendar.YEAR),
                                vm.calendar.get(java.util.Calendar.MONTH),
                                vm.calendar.get(java.util.Calendar.DAY_OF_MONTH)
                            )
                            datePickerDialog.show()
                        },
                            modifier = Modifier.padding(horizontal = 5.dp)) {
                            Text(
                                stringResource(R.string.date),
                                style = MaterialTheme.typography.titleSmall,
                            )
                        }
                        Button(onClick = {
                            val timePickerDialog = TimePickerDialog(this@EditActivity,
                                TimePickerDialog.OnTimeSetListener { _, hourSelected, minuteSelected ->
                                    vm.calendar.set(java.util.Calendar.HOUR_OF_DAY, hourSelected)
                                    vm.calendar.set(java.util.Calendar.MINUTE, minuteSelected)
                                    val format = SimpleDateFormat("dd.MM.yyyy HH:mm")
                                    vm.dtText.value = format.format(vm.calendar.time)
                                    vm._datetime = vm.calendar.timeInMillis
                                },
                                vm.calendar.get(java.util.Calendar.HOUR_OF_DAY),
                                vm.calendar.get(java.util.Calendar.MINUTE),
                                true
                            )
                            timePickerDialog.show()
                        }, modifier = Modifier.padding(horizontal = 5.dp)) {
                            Text(stringResource(R.string.time),
                                style = MaterialTheme.typography.titleSmall,
                            )
                        }
                    }
                    Button(onClick = {
                        if (!vm.titleErr.value) {
                            vm.save()
                            finish()
                        }},
                        modifier = Modifier
                            .padding(top = 20.dp)
                            .align(Alignment.CenterHorizontally)
                    ) {
                        Text(
                            stringResource(R.string.save),
                            style = MaterialTheme.typography.titleLarge,
                        )
                    }
                    Button(onClick = { finish() },
                        modifier = Modifier
                            .padding(top = 5.dp)
                            .align(Alignment.CenterHorizontally)
                    ) {
                        Text(
                            stringResource(R.string.back),
                            style = MaterialTheme.typography.titleLarge,
                        )
                    }
                }
            }
        }
    }
}