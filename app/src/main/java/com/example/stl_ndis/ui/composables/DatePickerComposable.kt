package com.example.stl_ndis.ui.composables

import android.app.DatePickerDialog
import android.widget.DatePicker
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
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerComposable(viewModel: JobCreationViewModel){
    val context = LocalContext.current
    val calendar = Calendar.getInstance()
    val day = calendar.get(Calendar.DAY_OF_MONTH)
    val month = calendar.get(Calendar.MONTH)
    val year = calendar.get(Calendar.YEAR)

    val datePickerDialog = DatePickerDialog(
        context,
        { _: DatePicker,
          mYear: Int,
          mMonth: Int,
          mDay: Int -> viewModel.setDate(mDay, mMonth, mYear)
        },
        year,
        month,
        day
    )

    TextField(value = viewModel.dateOfJob, onValueChange = {})
    Spacer(modifier = Modifier.height(12.dp))
    Button(onClick = { datePickerDialog.show() }) {
        Text(text = "Choose Date")
    }
}