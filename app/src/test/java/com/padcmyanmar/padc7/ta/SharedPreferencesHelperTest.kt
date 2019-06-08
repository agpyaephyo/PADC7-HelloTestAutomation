package com.padcmyanmar.padc7.ta

import android.content.SharedPreferences
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Matchers
import org.mockito.Matchers.*
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.runners.MockitoJUnitRunner
import java.util.*

@RunWith(MockitoJUnitRunner::class)
class SharedPreferencesHelperTest {

    companion object {
        private const val TEST_NAME = "Test name"
        private const val TEST_EMAIL = "test@email.com"
        private val TEST_DATE_OF_BIRTH = Calendar.getInstance()
    }

    private lateinit var mSharedPreferenceEntry: SharedPreferenceEntry
    private lateinit var mMockSharedPreferencesHelper: SharedPreferencesHelper
    private lateinit var mMockBrokenSharedPreferencesHelper: SharedPreferencesHelper

    init {
        TEST_DATE_OF_BIRTH.set(1980, 1, 1)
    }

    @Mock
    internal var mMockSharedPreferences: SharedPreferences? = null

    @Mock
    internal var mMockBrokenSharedPreferences: SharedPreferences? = null

    @Mock
    internal var mMockEditor: SharedPreferences.Editor? = null

    @Mock
    internal var mMockBrokenEditor: SharedPreferences.Editor? = null

    @Before
    fun initMocks() {
        // Create SharedPreferenceEntry to persist.
        mSharedPreferenceEntry = SharedPreferenceEntry(
            TEST_NAME, TEST_DATE_OF_BIRTH,
            TEST_EMAIL
        )

        // Create a mocked SharedPreferences.
        mMockSharedPreferencesHelper = createMockSharedPreference()

        // Create a mocked SharedPreferences that fails at saving data.
        mMockBrokenSharedPreferencesHelper = createBrokenMockSharedPreference()
    }

    @Test
    fun sharedPreferencesHelper_SaveAndReadPersonalInformation() {
        // Save the personal information to SharedPreferences
        val success = mMockSharedPreferencesHelper!!.savePersonalInfo(mSharedPreferenceEntry)

        assertThat<Boolean>(
            "Checking that SharedPreferenceEntry.save... returns true",
            success, `is`<Boolean>(true)
        )

        // Read personal information from SharedPreferences
        val savedSharedPreferenceEntry = mMockSharedPreferencesHelper!!.getPersonalInfo()

        // Make sure both written and retrieved personal information are equal.
        assertThat(
            "Checking that SharedPreferenceEntry.name has been persisted and read correctly",
            mSharedPreferenceEntry.name,
            `is`(equalTo(savedSharedPreferenceEntry.name))
        )
        assertThat(
            "Checking that SharedPreferenceEntry.dateOfBirth has been persisted and read " + "correctly",
            mSharedPreferenceEntry.dateOfBirth,
            `is`(equalTo(savedSharedPreferenceEntry.dateOfBirth))
        )
        assertThat(
            "Checking that SharedPreferenceEntry.email has been persisted and read " + "correctly",
            mSharedPreferenceEntry.email,
            `is`(equalTo(savedSharedPreferenceEntry.email))
        )
    }

    @Test
    fun sharedPreferencesHelper_SavePersonalInformationFailed_ReturnsFalse() {
        // Read personal information from a broken SharedPreferencesHelper
        val success = mMockBrokenSharedPreferencesHelper.savePersonalInfo(mSharedPreferenceEntry)
        assertThat<Boolean>(
            "Makes sure writing to a broken SharedPreferencesHelper returns false", success,
            `is`<Boolean>(false)
        )
    }

    /**
     * Creates a mocked SharedPreferences.
     */
    private fun createMockSharedPreference(): SharedPreferencesHelper {
        // Mocking reading the SharedPreferences as if mMockSharedPreferences was previously written
        // correctly.
        `when`<String>(mMockSharedPreferences!!.getString(eq(SharedPreferencesHelper.KEY_NAME), anyString()))
            .thenReturn(mSharedPreferenceEntry.name)

        `when`<String>(mMockSharedPreferences!!.getString(eq(SharedPreferencesHelper.KEY_EMAIL), anyString()))
            .thenReturn(mSharedPreferenceEntry.email)

        `when`<Long>(mMockSharedPreferences!!.getLong(eq(SharedPreferencesHelper.KEY_DOB), anyLong()))
            .thenReturn(mSharedPreferenceEntry.dateOfBirth.timeInMillis)

        // Mocking a successful commit.
        `when`<Boolean>(mMockEditor!!.commit()).thenReturn(true)

        // Return the MockEditor when requesting it.
        `when`<SharedPreferences.Editor>(mMockSharedPreferences!!.edit()).thenReturn(mMockEditor)

        return SharedPreferencesHelper(mMockSharedPreferences!!)
    }

    /**
     * Creates a mocked SharedPreferences that fails when writing.
     */
    private fun createBrokenMockSharedPreference(): SharedPreferencesHelper {
        // Mocking a commit that fails.
        `when`<Boolean>(mMockBrokenEditor!!.commit()).thenReturn(false)

        // Return the broken MockEditor when requesting it.
        `when`<SharedPreferences.Editor>(mMockBrokenSharedPreferences!!.edit()).thenReturn(mMockBrokenEditor)

        return SharedPreferencesHelper(mMockBrokenSharedPreferences!!)
    }
}