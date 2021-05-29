package com.openlearning.scrumify.customs

import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.databinding.BindingAdapter
import androidx.lifecycle.MutableLiveData
import com.google.android.material.textfield.TextInputLayout
import com.openlearning.scrumify.R
import com.openlearning.scrumify.models.ROLES
import com.openlearning.scrumify.models.TaskPriority
import com.openlearning.scrumify.models.TaskStatus
import com.openlearning.scrumify.utils.InputValidator
import com.openlearning.scrumify.utils.ValidationStatus
import com.openlearning.scrumify.utils.common.getDateString
import com.openlearning.scrumify.utils.extensions.value
import java.util.*


private const val TAG = "EditTextCustoms"

@BindingAdapter("app:fixPrefix")
fun fixPrefix(view: EditText, prefix: String) {

    view.addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            if (s != null) {
                if (s.length < prefix.length) {
                    view.value = prefix
                    view.setSelection(prefix.length)
                }
            }
        }

        override fun afterTextChanged(s: Editable?) {}

    })
}

@BindingAdapter("app:userRoleFormatted")
fun projectStatusFormat(editText: TextView, roles: ROLES) {

    editText.text = roles.name.replace("_", " ")!!
}

@BindingAdapter("app:dateString")
fun setDate(textView: TextView, date: Date?) {

    if (date == null) return

    textView.text = getDateString(date)
}

@BindingAdapter("app:buttonVisible")
fun buttonVisibility(button: Button, visible: Boolean) {

    button.visibility = when (visible) {
        true -> View.VISIBLE
        false -> View.INVISIBLE
    }
}

@BindingAdapter("app:taskPriority")
fun taskPriority(textView: TextView, taskPriority: TaskPriority) {

    val context = textView.context
    textView.text = taskPriority.name
    textView.visibility = View.VISIBLE

    when (taskPriority) {

        TaskPriority.LOW -> {

            textView.backgroundTintList = context.getColorStateList(R.color.colorPriorityLow)

        }
        TaskPriority.NORMAL -> {

            textView.backgroundTintList = context.getColorStateList(R.color.colorPriorityNormal)

        }
        TaskPriority.URGENT -> {

            textView.backgroundTintList = context.getColorStateList(R.color.colorPriorityUrgent)

        }
    }
}

@BindingAdapter("app:taskStatusIcon")
fun sprintTaskStatusIcon(imageView: ImageView, taskStatus: TaskStatus) {

    when (taskStatus) {

        TaskStatus.PENDING -> {

            imageView.setImageResource(R.drawable.ic_pending_actions)

        }
        TaskStatus.CLOSED -> {

            imageView.setImageResource(R.drawable.ic_backspace)

        }
        TaskStatus.COMPLETE -> {

            imageView.setImageResource(R.drawable.ic_done)

        }
    }
}

@BindingAdapter("app:taskStatusColor")
fun sprintTaskStatusColor(textView: TextView, taskStatus: TaskStatus) {

    textView.text = taskStatus.name
    val context = textView.context

    when (taskStatus) {

        TaskStatus.PENDING -> {

            textView.setTextColor(context.getColor(R.color.colorFull))

        }
        TaskStatus.CLOSED -> {

            textView.setTextColor(context.getColor(R.color.gray))

        }
        TaskStatus.COMPLETE -> {

            textView.setTextColor(context.getColor(R.color.colorPriorityLow))

        }
    }
}

@BindingAdapter("app:taskPriorityBackground")
fun taskPriorityBackground(
    constraintLayout: ConstraintLayout,
    taskPriority: TaskPriority
) {

    val context = constraintLayout.context

    when (taskPriority) {

        TaskPriority.LOW -> {

            constraintLayout.background =
                ContextCompat.getDrawable(context, R.color.colorPriorityLow)


        }
        TaskPriority.NORMAL -> {

            constraintLayout.background =
                ContextCompat.getDrawable(context, R.color.colorPriorityNormal)


        }
        TaskPriority.URGENT -> {

            constraintLayout.background =
                ContextCompat.getDrawable(context, R.color.colorPriorityUrgent)


        }
    }
}


@BindingAdapter(
    value = ["app:inputValidator", "app:errorView", "app:validArray", "app:myIndex"],
    requireAll = true
)
fun addInPutValidator(
    editText: EditText,
    inputValidator: InputValidator,
    errorInput: TextInputLayout,
    validArray: MutableLiveData<MutableList<Boolean>>,
    myIndex: Int
) {

    var inputStarted = false
    Log.d(TAG, "addInPutValidator: intial valie ${editText.value}")

    editText.addTextChangedListener {
        inputStarted = true
        validateInput(
            inputValidator,
            errorInput,
            editText.value,
            editText.isFocused,
            validArray,
            myIndex
        )

    }

    editText.onFocusChangeListener = View.OnFocusChangeListener { _, _ ->
        if (inputStarted) {
            validateInput(
                inputValidator,
                errorInput,
                editText.value,
                editText.isFocused,
                validArray,
                myIndex
            )
        }
    }
}


private fun validateInput(
    inputValidator: InputValidator,
    errorInput: TextInputLayout,
    input: String,
    focused: Boolean,
    validArray: MutableLiveData<MutableList<Boolean>>,
    myIndex: Int
) {

    Log.d(TAG, "validateInput: validating")
    for (scheme in inputValidator.validationSchemes) {

        when (val validationStatus = scheme.validate(input, focused)) {

            is ValidationStatus.Error -> {
                if (validArray.value!![myIndex]) {
                    validArray.value!![myIndex] = false
                    validArray.value = validArray.value!!
                }

                errorInput.error = validationStatus.reason

                return
            }

            is ValidationStatus.LateError -> {

                if (validArray.value!![myIndex]) {
                    validArray.value!![myIndex] = false
                    validArray.value = validArray.value!!
                }

                errorInput.error = null
                return
            }
            else -> {
                errorInput.error = null
            }
        }
    }


    if (!validArray.value!![myIndex]) {
        validArray.value!![myIndex] = true
        validArray.value = validArray.value!!

    }
}
