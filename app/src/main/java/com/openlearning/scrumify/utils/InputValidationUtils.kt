package com.openlearning.scrumify.utils

import android.util.Patterns
import kotlin.math.max

private const val TAG = "InputValidationUtils"

sealed class ValidationStatus {

    data class Success(val enteredData: String) : ValidationStatus()
    data class LateError(val reason: String) : ValidationStatus()
    data class Error(val reason: String) : ValidationStatus()
}

abstract class ValidationScheme(val catchLive: Boolean) {

    internal abstract fun validate(input: String, focused: Boolean): ValidationStatus
}

class InputValidator(val validationSchemes: List<ValidationScheme>)

class InputValidatorBuilder(
        private val allSchemes: MutableList<ValidationScheme> = arrayListOf(),
) {

    fun addValidationScheme(validationScheme: ValidationScheme): InputValidatorBuilder {

        if (!allSchemes.contains(validationScheme)) {
            allSchemes.add(validationScheme)
        }

        return this
    }

    fun build(): InputValidator {
        return InputValidator(allSchemes);
    }

}

// Validators
class MinLengthValidator(private val minLength: Int, liveError: Boolean) : ValidationScheme(liveError) {

    override fun validate(input: String, focused: Boolean): ValidationStatus {


        if (input.trim().length < minLength) {

            val error = ValidationStatus.Error("input less than $minLength char")

            return if (focused) {
                if (catchLive) {
                    error
                } else {
                    ValidationStatus.LateError("input less than $minLength char")
                }
            } else {
                error
            }
        }

        return ValidationStatus.Success(input.trim())
    }

}

class MaxLengthValidator(private val maxLength: Int, liveError: Boolean) : ValidationScheme(liveError) {

    override fun validate(input: String, focused: Boolean): ValidationStatus {

        val error = ValidationStatus.Error("input greater than $maxLength char")
        if (input.length > maxLength) {

            return if (focused) {
                if (catchLive) {
                    error
                } else {
                    ValidationStatus.LateError("input less than ${maxLength} char")
                }
            } else {
                return error
            }
        }

        return ValidationStatus.Success(input.trim())
    }

}

class EmailValidator(liveError: Boolean) : ValidationScheme(liveError) {

    override fun validate(input: String, focused: Boolean): ValidationStatus {

        val error = ValidationStatus.Error("email not valid")

        if (!Patterns.EMAIL_ADDRESS.matcher(input).matches()) {

            return if (focused) {
                if (catchLive) {
                    error
                } else {
                    ValidationStatus.LateError("email not valid")
                }
            } else {
                return error
            }
        }

        return ValidationStatus.Success(input.trim())
    }

}






