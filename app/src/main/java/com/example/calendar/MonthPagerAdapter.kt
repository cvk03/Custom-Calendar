import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.viewpager.widget.PagerAdapter
import com.example.calendar.R
import com.kizitonwose.calendar.view.CalendarView
import java.text.SimpleDateFormat
import java.time.DayOfWeek
import java.time.YearMonth
import java.util.Calendar
import java.util.Locale

class MonthPagerAdapter(private val context: Context) : PagerAdapter() {

    private val months = mutableListOf<Calendar>()

    fun setMonths(newMonths: List<Calendar>) {
        months.clear()
        months.addAll(newMonths)
        notifyDataSetChanged()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val inflater = LayoutInflater.from(context)
        val layout = inflater.inflate(R.layout.activity_main, container, false)

        // Get a reference to the CalendarView in your layout
        val calendarView : CalendarView = layout.findViewById(R.id.CalendarView)

        // Update the CalendarView with the corresponding month
        val calendar = months[position]
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)

        val startMonth = YearMonth.of(year, month)
        val endMonth = startMonth.plusMonths(1)
        val firstDayOfWeek = DayOfWeek.SUNDAY // or any other DayOfWeek enum value

        calendarView.setup(startMonth, endMonth, firstDayOfWeek)

        container.addView(layout)
        return layout
    }


    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    override fun getCount(): Int = months.size

    override fun isViewFromObject(view: View, `object`: Any): Boolean = view === `object`
}
