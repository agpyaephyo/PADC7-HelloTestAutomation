package com.padcmyanmar.padc7.ta

import android.os.Bundle
import android.os.Parcelable
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_log.*
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class LogActivity : AppCompatActivity() {

    companion object {
        private const val KEY_HISTORY_DATA = "KEY_HISTORY_DATA"
    }

    private var mIsHistoryEmpty = true
    private val mSimpleDateFormatter: DateFormat = SimpleDateFormat("HH:mm:ss:SSS", Locale.getDefault())
    private var mLogHistory: LogHistory = LogHistory()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log)

        if (savedInstanceState != null) {
            // We've got a past state, apply it to the UI.
            mLogHistory = savedInstanceState.getParcelable(KEY_HISTORY_DATA)!!
            for (entry in mLogHistory.getData()) {
                appendEntryToView(entry.first, entry.second)
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(KEY_HISTORY_DATA, mLogHistory)
    }

    /**
     * Called when the user wants to append an entry to the history.
     */
    fun updateHistory(view: View) {
        // Get the text to add and timestamp.
        val editText = view.rootView.findViewById<View>(R.id.editText) as EditText
        val textToAdd = editText.text
        val timestamp = System.currentTimeMillis()

        // Show it back to the user.
        appendEntryToView(textToAdd.toString(), timestamp)

        // Update the history.
        mLogHistory.addEntry(textToAdd.toString(), timestamp)

        // Reset the EditText.
        editText.setText("")
    }

    private fun appendEntryToView(text: String, timestamp: Long) {
        val date = Date(timestamp)

        // Add a newline if needed or clear the text view (to get rid of the hint).
        if (!mIsHistoryEmpty) {
            mHistoryTextView.append("\n")
        } else {
            mHistoryTextView.text = ""
        }

        // Add the representation of the new entry to the text view.
        mHistoryTextView.append(String.format("%s [%s]", text, mSimpleDateFormatter.format(date)))

        mIsHistoryEmpty = false
    }
}