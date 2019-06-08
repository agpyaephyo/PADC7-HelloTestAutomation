package com.padcmyanmar.padc7.ta

import android.os.Parcel
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@SmallTest
class LogHistoryAndroidUnitTest {
    companion object {
        private const val TEST_STRING = "This is a string"
        private const val TEST_LONG = 12345678L
    }

    lateinit var mLogHistory: LogHistory

    @Before
    fun createLogHistory() {
        mLogHistory = LogHistory()
    }

    @Test
    fun logHistory_ParcelableWriteRead() {
        // Set up the Parcelable object to send and receive.
        mLogHistory.addEntry(TEST_STRING, TEST_LONG)

        // Write the data
        val parcel = Parcel.obtain()
        mLogHistory.writeToParcel(parcel, mLogHistory.describeContents())

        // After you're done with writing, you need to reset the parcel for reading.
        parcel.setDataPosition(0)

        // Read the data
        val createdFromParcel = LogHistory.CREATOR.createFromParcel(parcel)
        val createdFromParcelData = createdFromParcel.getData()

        // Verify that the received data is correct.
        assertThat(createdFromParcelData.size).isEqualTo(1)
        assertThat(createdFromParcelData[0].first).isEqualTo(TEST_STRING)
        assertThat(createdFromParcelData[0].second).isEqualTo(TEST_LONG)
    }
}