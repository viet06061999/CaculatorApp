package com.example.caculator

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.caculator.ultils.DoubleUltils
import com.example.caculator.ultils.StringUltils
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    lateinit var edt: EditText
    lateinit var txtView: TextView
    private var mTemp: String = ""
    private var mCheck: Boolean = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        edt = edit_expression
        txtView = text_result
        setEditText()
    }

    /**
     * set text for editText
     * @param c text need to set for editText
     */
    fun setText(c: Char) {
        if (!StringUltils.isOperator(c) && mCheck) {
            if (c != '.') {
                var value = edt.text.append(c).toString()
                if (!value.contains('.')) {
                    var doubleValue = StringUltils.stringToDouble(value)
                    edt.setText(StringUltils.doubleToString(doubleValue))
                } else edt.setText(value)
            } else if (!edt.text.toString().contains('.')) {
                edt.text = edt.text.append(".")
            }
        } else if (StringUltils.isOperator(c)) {
            processWithOperator(c)
        } else {
            var tg = edt.text.append(c).toString()
            var second: Double = StringUltils.getOperand(tg)[1]
            var value = StringUltils.doubleToString(second)
            if (second == 0.0) value = "0"
            var doubleValue = StringUltils.stringToDouble(value)
            if (c != '.') {
                if (!value.contains('.') || !DoubleUltils.isInteger(second))
                    edt.setText(mTemp + StringUltils.doubleToString(doubleValue))
                else edt.setText(tg)
            }
            if (c == '.' && !value.contains('.')) edt.setText(tg)
        }
    }

    /**
     * set text for editText when click to button
     *
     */
    fun setEditText() {
        button_zero.setOnClickListener { setText('0') }
        button_one.setOnClickListener { setText('1') }
        button_two.setOnClickListener { setText('2') }
        button_three.setOnClickListener { setText('3') }
        button_four.setOnClickListener { setText('4') }
        button_five.setOnClickListener { setText('5') }
        button_six.setOnClickListener { setText('6') }
        button_seven.setOnClickListener { setText('7') }
        button_eight.setOnClickListener { setText('8') }
        button_nine.setOnClickListener { setText('9') }
        button_mod.setOnClickListener { setText('%') }
        button_device.setOnClickListener { setText('÷') }
        button_multi.setOnClickListener { setText('x') }
        button_sub.setOnClickListener { setText('-') }
        button_plus.setOnClickListener { setText('+') }
        button_dot.setOnClickListener { setText('.') }
        button_equal.setOnClickListener { text_result.setText(process(edt.text.toString())) }
        button_clear.setOnClickListener {
            edt.setText("")
            mTemp = ""
            mCheck = true
        }
        img_button_delete.setOnClickListener {
            var text: String = edt.text.toString()
            text = text.substring(0, text.length - 1)
            if (!StringUltils.isExpression(text)) {
                mCheck = true
                if (mTemp.length > 0) {
                    mTemp = mTemp.substring(0, mTemp.length - 1)
                }

            }
            edt.setText(text)
        }
    }

    /**
     * process when click to operator
     */
    fun processWithOperator(operator: Char) {
        var expression: String = edt.text.toString()
        if (StringUltils.isExpression(expression)) {
            txtView.setText(process(expression))
            mTemp = process(expression) + operator
            edt.setText(mTemp)

        } else if (expression.length == 0) {
            mTemp = txtView.text.toString() + operator
            edt.setText(mTemp)
        } else {
            mCheck = false
            edt.text = edt.text.append(operator)
            mTemp = edt.text.toString()
        }
    }

    /**
     * process calculation
     *
     * @param s a string is calculation
     * @return result of calculation
     */
    fun process(s: String): String {
        var result: Double = StringUltils.calculate(s)
        if (result.isNaN()) {
            Toast.makeText(this, "Không thể chia cho 0", Toast.LENGTH_SHORT).show()
            return "0"
        }
        return StringUltils.doubleToString(result)
    }
}
