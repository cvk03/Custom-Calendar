package com.example.calendar

import MonthPagerAdapter
import android.app.Activity
import android.graphics.Color
import android.opengl.Visibility
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.view.children
import androidx.viewpager.widget.ViewPager
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.CalendarMonth
import com.kizitonwose.calendar.core.DayPosition
import com.kizitonwose.calendar.core.daysOfWeek
import com.kizitonwose.calendar.core.firstDayOfWeekFromLocale
import com.kizitonwose.calendar.core.nextMonth
import com.kizitonwose.calendar.core.previousMonth
import com.kizitonwose.calendar.view.CalendarView
import com.kizitonwose.calendar.view.MonthDayBinder
import com.kizitonwose.calendar.view.MonthHeaderFooterBinder
import java.text.SimpleDateFormat
import java.time.DayOfWeek
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Calendar
import java.util.Date
import java.util.Locale

class MainActivity : AppCompatActivity(){

    lateinit var calendarView : CalendarView
    lateinit var monthTextView : TextView
    lateinit var nextMonthImage : ImageView
    lateinit var previousMonthImage : ImageView


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        calendarView = findViewById(R.id.CalendarView)
        monthTextView = findViewById(R.id.monthTextView)
        nextMonthImage = findViewById(R.id.iv_nextMonth)
        previousMonthImage = findViewById(R.id.iv_previousMonth)


        val currentMonth = YearMonth.now()
        val startMonth = currentMonth.minusMonths(1000)
        val endMonth = currentMonth.plusMonths(1000)

        val daysOfWeek = daysOfWeek(firstDayOfWeek = DayOfWeek.MONDAY)
        calendarView.setup(startMonth, endMonth, daysOfWeek().first())
        calendarView.scrollToMonth(currentMonth)

        calendarView.monthScrollListener = { month ->
            monthTextView.text = getString(R.string.month_year,month.yearMonth.month.toString(),month.yearMonth.year.toString())

        }

        calendarView.dayBinder = object : MonthDayBinder<DayViewContainer> {

            override fun create(view: View) = DayViewContainer(view)

            override fun bind(container: DayViewContainer, data: CalendarDay) {

                val curr_date = data.date.dayOfMonth.toString()

                container.textView.text = curr_date
                if (data.position == DayPosition.MonthDate) {
                    container.textView.setTextColor(ContextCompat.getColor(applicationContext,R.color.black))

                    if(curr_date == "13" || curr_date == "15")
                    {
                        container.taskTextView.text = "Call Customer"
                        container.taskTextView.visibility = VISIBLE
                        container.taskTextView.setBackgroundColor(ContextCompat.getColor(applicationContext,R.color.green))
                    }

                } else {
                    container.textView.setTextColor(Color.GRAY)
                    container.view.setBackgroundColor(ContextCompat.getColor(applicationContext,R.color.light_blue))

                }
            }
        }


        calendarView.monthHeaderBinder = object : MonthHeaderFooterBinder<MonthViewContainer> {
                override fun create(view: View) = MonthViewContainer(view)

                override fun bind(container: MonthViewContainer, data: CalendarMonth) {

                    val titlesContainer = container.titlesContainer

                    titlesContainer.children
                        .map { it as TextView }
                        .forEachIndexed { index, textView ->
                            val dayOfWeek = daysOfWeek[index]
                            val title = dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault())
                            textView.text = title
                        }
                }
            }

        nextMonthImage.setOnClickListener {
           calendarView.findFirstVisibleMonth()?.let {
                calendarView.smoothScrollToMonth(it.yearMonth.nextMonth)
            }
        }

        previousMonthImage.setOnClickListener {
            calendarView.findFirstVisibleMonth()?.let {
               calendarView.smoothScrollToMonth(it.yearMonth.previousMonth)
            }
        }


    }

}