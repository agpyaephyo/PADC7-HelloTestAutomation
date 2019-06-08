package com.padcmyanmar.padc7.ta

import android.os.Parcel
import android.os.Parcelable
import android.util.Pair
import java.util.ArrayList

class LogHistory() : Parcelable {

    companion object {
        @JvmField
        val CREATOR : Parcelable.Creator<LogHistory> = object : Parcelable.Creator<LogHistory> {

            override fun createFromParcel(`in`: Parcel): LogHistory {
                return LogHistory(`in`)
            }

            override fun newArray(size: Int): Array<LogHistory?> {
                return arrayOfNulls(size)
            }
        }
    }

    constructor(`in`: Parcel) : this() {

        // First, read the size of the arrays that contain the data.
        val length = `in`.readInt()

        // Create the arrays to store the data.
        val texts = arrayOfNulls<String>(length)
        val timestamps = LongArray(length)

        // Read the arrays in a specific order.
        `in`.readStringArray(texts)
        `in`.readLongArray(timestamps)

        // The lengths of both arrays should match or the data is corrupted.
        if (texts.size != timestamps.size) {
            throw IllegalStateException("Error reading from saved state.")
        }

        // Reset the data container and update the data.
        mData.clear()
        for (i in texts.indices) {
            val pair = Pair<String, Long>(texts[i], timestamps[i])
            mData.add(pair)
        }
    }

    // Used to store the data to be used by the activity.
    private val mData = ArrayList<Pair<String, Long>>()

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(out: Parcel, flags: Int) {
        // Prepare an array of strings and an array of timestamps.
        val texts = arrayOfNulls<String>(mData.size)
        val timestamps = LongArray(mData.size)

        // Store the data in the arrays.
        for (i in mData.indices) {
            texts[i] = mData[i].first
            timestamps[i] = mData[i].second
        }
        // Write the size of the arrays first.
        out.writeInt(texts.size)

        // Write the two arrays in a specific order.
        out.writeStringArray(texts)
        out.writeLongArray(timestamps)
    }

    /**
     * Returns a copy of the current data used by the activity.
     */
    fun getData(): List<Pair<String, Long>> {
        return ArrayList(mData)
    }

    /**
     * Adds a new entry to the log.
     * @param text the text to be stored in the log
     * @param timestamp the current time in milliseconds since January 1, 1970 00:00:00.0 UTC.
     */
    fun addEntry(text: String, timestamp: Long) {
        mData.add(Pair(text, timestamp))
    }
}