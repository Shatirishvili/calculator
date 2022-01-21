package com.example.mysecondcalculator_cal

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var canAddOperation = false
    private var canAddDecimal = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }


    fun numberAction(view: android.view.View)
    {
        if(view is Button)
        {
            if(view.text == ".")
            {
                if (canAddDecimal)
                    workingsTV.append(view.text)

                canAddDecimal = false
            }
            else
                workingsTV.append(view.text)

            canAddOperation = true
        }
    }

    fun operationAction(view: android.view.View)
    {
        if(view is Button && canAddOperation)
        {
            workingsTV.append(view.text)
            canAddOperation = false
            canAddDecimal = true
        }
    }

    fun allClearAction(view: android.view.View)
    {
        workingsTV.text = ""
        resultsTV.text = ""
    }

    fun backspaceAction(view: android.view.View)
    {
        val length = workingsTV.length()
        if (length > 0)
            workingsTV.text = workingsTV.text.subSequence(0, length - 1)
    }
    fun equalsAction(view: android.view.View)
    {
        resultsTV.text = calculateResults()
    }

    private fun calculateResults(): String
    {
        val digitsOperators = digitsOperators()
        if (digitsOperators.isEmpty()) return ""

        val timesdivision = timesDivisionCalculate(digitsOperators)
        if (timesdivision.isEmpty()) return ""

        val result = addSubtractCalculate(timesdivision)

        return result.toString()
    }

    private fun addSubtractCalculate(passedLIst: MutableList<Any>): Float {
        var result = passedLIst[0] as Float

        for(i in passedLIst.indices)
        {
            if(passedLIst[i] is Char && i != passedLIst.lastIndex)
            {
                val operator = passedLIst[i]
                val nextDigit = passedLIst[i + 1] as Float
                if(operator == '+')
                    result += nextDigit
                if(operator == '-')
                    result -= nextDigit



            }
        }



        return result

    }

    private fun timesDivisionCalculate( passedlist : MutableList<Any>): MutableList<Any>
    {
        var list = passedlist
        while (list.contains('x') || list.contains('÷'))
        {
            list = calcTimesDiv(list)
        }
        return list
    }

    private fun calcTimesDiv( passedList: MutableList<Any>): MutableList<Any>
    {
        val newList = mutableListOf<Any>()
        var restartIndex = passedList.size

        for(i in passedList.indices)
        {
            if (passedList[i] is Char && i != passedList.lastIndex && i < restartIndex)
            {
                val operator  = passedList[i]
                val prevDigit  = passedList[i - 1] as Float
                val nextDigit  = passedList[i + 1] as Float
                when(operator)
                {
                    'x' ->
                    {
                        newList.add(prevDigit * nextDigit)
                        restartIndex = i + 1

                    }
                    '÷' ->
                    {
                        newList.add(prevDigit / nextDigit)
                        restartIndex = i + 1
                    }
                    else ->
                    {
                        newList.add(prevDigit)
                        newList.add(operator)
                    }
                }
            }

            if(i > restartIndex)
                newList.add(passedList[i])
        }

        return newList


    }

    private fun digitsOperators(): MutableList<Any>
    {
        val list = mutableListOf<Any>()
        var currentDigit = ""
        for(character in workingsTV.text)
        {
            if(character.isDigit() || character == '.')
                currentDigit += character
            else
            {
                list.add(currentDigit.toFloat())
                currentDigit = ""
                list.add(character)
            }
        }
        if (currentDigit != "")
            list.add(currentDigit.toFloat())


        return list
    }
}