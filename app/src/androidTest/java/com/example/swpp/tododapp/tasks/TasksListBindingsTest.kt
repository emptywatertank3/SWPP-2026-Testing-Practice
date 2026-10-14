package com.example.swpp.tododapp.tasks

import android.view.View
import android.widget.TextView
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.swpp.tododapp.data.Task
import java.time.LocalDate
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TasksListBindingsTest {

    private fun onMainThread(block: (TextView) -> Unit) {
        val instrumentation = InstrumentationRegistry.getInstrumentation()
        instrumentation.runOnMainSync {
            block(TextView(instrumentation.targetContext))
        }
    }

    private fun task(
        isCompleted: Boolean = false,
        dueDate: LocalDate? = null
    ) = Task(
        title = "Task",
        description = "Description",
        isCompleted = isCompleted,
        dueDate = dueDate
    )

    @Test
    fun setDueDateLabel_completedTodayFutureAndWithoutDate_showsCLEAR() {
        onMainThread { textView ->
            val today = LocalDate.of(2026, 10, 14)

            setDueDateLabel(textView, task(isCompleted = true, dueDate = today))
            assertEquals("CLEAR", textView.text.toString())
            assertEquals(View.VISIBLE, textView.visibility)

            setDueDateLabel(textView, task(isCompleted = true, dueDate = LocalDate.of(2026, 10, 16)))
            assertEquals("CLEAR", textView.text.toString())
            assertEquals(View.VISIBLE, textView.visibility)

            setDueDateLabel(textView, task(isCompleted = true, dueDate = null))
            assertEquals("CLEAR", textView.text.toString())
            assertEquals(View.VISIBLE, textView.visibility)
        }
    }

    @Test
    fun setDueDateLabel_dueToday_showsDDay() {
        onMainThread { textView ->
            val today = LocalDate.now()
            setDueDateLabel(textView, task(dueDate = today))

            assertEquals("D-Day", textView.text.toString())
            assertEquals(View.VISIBLE, textView.visibility)
        }
    }

    @Test
    fun setDueDateLabel_dueInThreeDays_showsD3() {
        onMainThread { textView ->
            val today = LocalDate.now()
            setDueDateLabel(textView, task(dueDate = today.plusDays(3)))

            assertEquals("D-3", textView.text.toString())
            assertEquals(View.VISIBLE, textView.visibility)
        }
    }

    @Test
    fun setDueDateLabel_activeWithoutDueDate_clearsStaleLabelAndHidesWithGONE() {
        onMainThread { textView ->
            textView.text = "CLEAR"
            textView.visibility = View.VISIBLE

            setDueDateLabel(textView, task(dueDate = null))

            assertEquals("", textView.text.toString())
            assertEquals(View.GONE, textView.visibility)
        }
    }

    @Test
    fun setDueDateLabel_nullTask_clearsStaleLabelAndHidesWithGONE() {
        onMainThread { textView ->
            textView.text = "D-3"
            textView.visibility = View.VISIBLE

            setDueDateLabel(textView, null)

            assertEquals("", textView.text.toString())
            assertEquals(View.GONE, textView.visibility)
        }
    }

    @Test
    fun setDueDateLabel_hiddenViewReusedForTaskWithLabel_showsDDay() {
        onMainThread { textView ->
            textView.text = ""
            textView.visibility = View.GONE
            val today = LocalDate.now()

            setDueDateLabel(textView, task(dueDate = today))

            assertEquals("D-Day", textView.text.toString())
            assertEquals(View.VISIBLE, textView.visibility)
        }
    }

    @Test
    fun setDueDateLabel_dueYesterdayAndTomorrow_showsCorrectSignedLabels() {
        onMainThread { textView ->
            val today = LocalDate.now()

            setDueDateLabel(textView, task(dueDate = today.minusDays(1)))
            assertEquals("D+1", textView.text.toString())
            assertEquals(View.VISIBLE, textView.visibility)

            setDueDateLabel(textView, task(dueDate = today.plusDays(1)))
            assertEquals("D-1", textView.text.toString())
            assertEquals(View.VISIBLE, textView.visibility)
        }
    }
}
