package com.vpdevs.calculator

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.Gravity
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button_one.setOnClickListener {
            setEnterNumberOnEditText(R.id.button_one)
        }

        button_two.setOnClickListener {
            setEnterNumberOnEditText(R.id.button_two)
        }

        button_three.setOnClickListener {
            setEnterNumberOnEditText(R.id.button_three)
        }

        button_four.setOnClickListener {
            setEnterNumberOnEditText(R.id.button_four)
        }

        button_five.setOnClickListener {
            setEnterNumberOnEditText(R.id.button_five)
        }

        button_six.setOnClickListener {
            setEnterNumberOnEditText(R.id.button_six)
        }

        button_seven.setOnClickListener {
            setEnterNumberOnEditText(R.id.button_seven)
        }

        button_eight.setOnClickListener {
            setEnterNumberOnEditText(R.id.button_eight)
        }

        button_nine.setOnClickListener {
            setEnterNumberOnEditText(R.id.button_nine)
        }

        button_zero.setOnClickListener {
            setEnterNumberOnEditText(R.id.button_zero)
        }

        button_plus.setOnClickListener {
            setOperation(R.id.button_plus)
        }

        button_minus.setOnClickListener {
            setOperation(R.id.button_minus)
        }

        button_multiply.setOnClickListener {
            setOperation(R.id.button_multiply)
        }

        button_divide.setOnClickListener {
            setOperation(R.id.button_divide)
        }

        button_modulus.setOnClickListener {
            setOperation(R.id.button_modulus)
        }

        button_equals.setOnClickListener {
            getCalculatorPreferences().run {
                val valueOne = appCompatEditText.text.toString()
                edit { putString(Constants.INPUT_VALUE_TWO, valueOne) }
                val inputOne = this.getString(Constants.INPUT_VALUE_ONE, "")
                val inputTwo = this.getString(Constants.INPUT_VALUE_TWO, "")
                if (inputOne!!.isNotEmpty() && inputTwo!!.isNotEmpty()) {
                    getCalculatedFinalResult()
                }
            }
        }

        button_reset.setOnClickListener {
            clearSharedPreferences()
            appCompatEditText.apply {
                gravity = Gravity.BOTTOM or Gravity.END
                text = "0"
            }
            textView_input.text = ""
        }
    }

    private fun getEnteredNumber(buttonId: Int, text: String): String {
        // high order function and lambda
        val concatText: (String) -> String = { it -> text + it }
        when (buttonId) {
            R.id.button_one -> return addText("1", concatText)
            R.id.button_two -> return addText("2", concatText)
            R.id.button_three -> return addText("3", concatText)
            R.id.button_four -> return addText("4", concatText)
            R.id.button_five -> return addText("5", concatText)
            R.id.button_six -> return addText("6", concatText)
            R.id.button_seven -> return addText("7", concatText)
            R.id.button_eight -> return addText("8", concatText)
            R.id.button_nine -> return addText("9", concatText)
            R.id.button_zero -> return addText("0", concatText)
        }
        return "try again"
    }

    // high order function to concat text as user enter
    private fun addText(it: String, operation: (String) -> String): String {
        return operation(it)
    }

    private fun setEnterNumberOnEditText(buttonId: Int) {
        var input = appCompatEditText.text.toString()
        if (input == "0") {
            input = ""
        }
        appCompatEditText.apply {
            gravity = Gravity.BOTTOM or Gravity.END
            text = getEnteredNumber(buttonId = buttonId, text = input)
        }
    }

    private fun setOperation(operationType: Int) {
        val valueOne = appCompatEditText.text.toString()
        getCalculatorPreferences().edit { putString(Constants.INPUT_VALUE_ONE, valueOne) }
        setOperatorString(operationType)
        appCompatEditText.text = ""
    }

    private fun getCalculatedFinalResult() {
        getCalculatorPreferences().apply {
            val inputOne: String =
                this.getString(Constants.INPUT_VALUE_ONE, "").toString()
            val inputTwo: String =
                this.getString(Constants.INPUT_VALUE_TWO, "").toString()
            val operator: String =
                this.getString(Constants.OPERATOR_TYPE, "").toString()
            val result: Int = calculate(
                valueOne = inputOne.toInt(),
                valueTwo = inputTwo.toInt()
            )
            if (result.toString().length > 5) {
                appCompatEditText.run {
                    text = "0"
                    Snackbar.make(this, "Out of range", Snackbar.LENGTH_LONG).show()
                }
            } else {
                val displayString = "$inputOne $operator $inputTwo = $result"
                textView_input.text = displayString
                appCompatEditText.text = "0"
            }
        }
        clearSharedPreferences()
    }

    private fun calculate(valueOne: Int, valueTwo: Int): Int {
        var result = 0
        val operator = getCalculatorPreferences().getString(
            Constants.OPERATOR_TYPE, ""
        )
        when (operator) {
            "+" -> result = valueOne.plus(valueTwo)
            "-" -> result = valueOne.minus(valueTwo)
            "x" -> result = valueOne.times(valueTwo)
            "/" -> result = valueOne.div(valueTwo)
            "%" -> result = valueOne.rem(valueTwo)
        }
        return result
    }

    private fun setOperatorString(buttonId: Int) {
        when (buttonId) {
            R.id.button_modulus -> getCalculatorPreferences().edit {
                putString(Constants.OPERATOR_TYPE, "%")
            }
            R.id.button_divide -> getCalculatorPreferences().edit {
                putString(Constants.OPERATOR_TYPE, "/")
            }
            R.id.button_minus -> getCalculatorPreferences().edit {
                putString(Constants.OPERATOR_TYPE, "-")
            }
            R.id.button_multiply -> getCalculatorPreferences().edit {
                putString(Constants.OPERATOR_TYPE, "x")
            }
            R.id.button_plus -> getCalculatorPreferences().edit {
                putString(Constants.OPERATOR_TYPE, "+")
            }
        }
    }

    private fun getCalculatorPreferences(): SharedPreferences {
        return getSharedPreferences(
            Constants.PREFERENCES_NAME,
            Context.MODE_PRIVATE
        )
    }

    private fun clearSharedPreferences() {
        getCalculatorPreferences().apply {
            edit { putString(Constants.INPUT_VALUE_ONE, "") }
            edit { putString(Constants.INPUT_VALUE_TWO, "") }
            edit { putString(Constants.OPERATOR_TYPE, "") }
        }
    }

    override fun onStop() {
        super.onStop()
        clearSharedPreferences()
    }
}



