package ru.sergdeev.tasklistcompose

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import ru.sergdeev.tasklistcompose.domain.DataItem
import ru.sergdeev.tasklistcompose.ui.theme.TaskListComposeTheme
import java.text.SimpleDateFormat

class ShowActivity : ComponentActivity() {
    @SuppressLint("SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val title = mutableStateOf<String>("")
        val desc = mutableStateOf<String>("")
        val dtText = mutableStateOf<String>("----------")

        val data = DataItem(0, "", "", 0, true)
        val bundle: Bundle? = intent.getBundleExtra("DATA")
        if (bundle != null) {
            data.id = bundle.getInt("ID")
            data.title = bundle.getString("TITLE").toString()
            title.value = data.title
            data.desc = bundle.getString("DESC").toString()
            desc.value = data.desc
            data.dt = bundle.getLong("DT")
            if (data.dt.toInt() != 0) {
                val format = SimpleDateFormat("dd.MM.yyyy HH:mm")
                dtText.value = format.format(data.dt)
            }
        }
        setContent {
            TaskListComposeTheme {
                Column(
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.Start,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp)
                ) {
                    Text(title.value, style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .padding(vertical = 10.dp)
                            .fillMaxWidth()
                            .border(1.dp, Color.Black, RoundedCornerShape(15.dp))
                            .clip(RoundedCornerShape(15.dp))
                            .background(colorResource(R.color.teal_700))
                            .padding(10.dp)
                    )
                    Text(desc.value, style = MaterialTheme.typography.titleLarge,
                        fontStyle = FontStyle.Italic,
                        modifier = Modifier
                            .padding(vertical = 10.dp)
                            .fillMaxWidth()
                            .border(1.dp, Color.Black, RoundedCornerShape(15.dp))
                            .clip(RoundedCornerShape(15.dp))
                            .background(colorResource(R.color.teal_700))
                            .padding(10.dp)
                    )
                    Text(dtText.value, style = MaterialTheme.typography.titleLarge,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .padding(vertical = 10.dp)
                            .fillMaxWidth()
                            .border(1.dp, Color.Black, RoundedCornerShape(15.dp))
                            .clip(RoundedCornerShape(15.dp))
                            .background(colorResource(R.color.teal_700))
                            .padding(10.dp)
                    )
                    Button(onClick = { itemEdit(this@ShowActivity, data) },
                        modifier = Modifier
                            .padding(top = 20.dp)
                            .align(Alignment.CenterHorizontally)
                    ) {
                        Text(
                            stringResource(R.string.edit),
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

    fun itemEdit(context: Context, item: DataItem) {
        intent = Intent(context, EditActivity::class.java)
        val bundle = Bundle()
        bundle.putInt("ID", item.id)
        bundle.putString("TITLE", item.title)
        bundle.putString("DESC", item.desc)
        bundle.putLong("DT", item.dt)
        intent.putExtra("DATA", bundle)

        startActivity(intent)
    }
}