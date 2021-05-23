package com.openlearning.scrumify.dialogues

import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.DatePicker
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.openlearning.scrumify.R
import com.openlearning.scrumify.databinding.ViewDatePcikerDialogueBinding
import com.openlearning.scrumify.databinding.ViewLoadingDialogueBinding
import java.time.Year
import java.util.*

class DatePicker(
    private val activity: Activity,
    private val returnDate: (Date) -> Unit,
    private val minDate: Date? = null,
    private val maxDate: Date? = null,
) : DialogFragment(), DatePickerDialog.OnDateSetListener {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val calendar = Calendar.getInstance()

        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val dialog = DatePickerDialog(activity, this, year, month, day)
        if (minDate != null) {
            dialog.datePicker.minDate = minDate.time
        }
        if (maxDate != null){
            dialog.datePicker.maxDate = maxDate.time
        }
        return dialog
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {

        val calendar = Calendar.getInstance()
        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.MONTH, month)
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
        val date = calendar.time
        returnDate(date)

    }


}