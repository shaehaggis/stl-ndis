package com.example.stl_ndis.ui.composables

import android.app.TimePickerDialog
import android.widget.TimePicker
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.stl_ndis.ui.viewmodels.JobCreationViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimePickerComposable(viewModel: JobCreationViewModel){
    val context = LocalContext.current

    val timePickerDialog = TimePickerDialog(context,
        { _: TimePicker, hourOfDay: Int, minute: Int ->
            viewModel.setTime(hourOfDay, minute)
        },
        0,
        0,
        false
    )

    TextField(value = viewModel.startTime, onValueChange = {})
    Spacer(modifier = Modifier.height(12.dp))
    Button(onClick = { timePickerDialog.show() }) {
        Text(text = "Choose Time")
    }
}