package com.androiddevs.shoppinglisttestingyt.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.filters.MediumTest
import com.androiddevs.shoppinglisttestingyt.R
import com.androiddevs.shoppinglisttestingyt.data.local.ShoppingItem
import com.androiddevs.shoppinglisttestingyt.getOrAwaitValue
import com.androiddevs.shoppinglisttestingyt.launchFragmentInHiltContainer
import com.androiddevs.shoppinglisttestingyt.repositories.FakeShoppingRepositoryAndroidTest
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import com.google.common.truth.Truth.assertThat
import javax.inject.Inject

/**
 * Created by karim on 22,November,2020
 */
@MediumTest
@ExperimentalCoroutinesApi
@HiltAndroidTest
class AddShoppingFragmentTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var fragmentFactory: ShoppingFragmentFactory

    @Before
    fun setup() {
        hiltRule.inject()
    }

    @Test
    fun clickInsertIntoDB_shoppingItemShouldBeInserted(){
        val fakeViewModel = ShoppingViewModel(FakeShoppingRepositoryAndroidTest())
        launchFragmentInHiltContainer<AddShoppingFragment>(fragmentFactory = fragmentFactory) {
            mViewModel = fakeViewModel
        }
        onView(withId(R.id.etShoppingItemName)).perform(replaceText("Name"))
        onView(withId(R.id.etShoppingItemAmount)).perform(replaceText("5"))
        onView(withId(R.id.etShoppingItemPrice)).perform(replaceText("5.5"))
        onView(withId(R.id.btnAddShoppingItem)).perform(click())

        assertThat(fakeViewModel.shoppingItems.getOrAwaitValue())
            .contains(ShoppingItem("Name",5,5.5f,""))
    }

    @Test
    fun pressBackButton_popBackStack() {
        val navController = mock(NavController::class.java)
        launchFragmentInHiltContainer<AddShoppingFragment> {
            Navigation.setViewNavController(requireView(), navController)
        }
        pressBack()

        verify(navController).popBackStack()
    }

    @Test
    fun clickShoppingImageView_goToPickImageFragment() {
        val navController = mock(NavController::class.java)

        launchFragmentInHiltContainer<AddShoppingFragment> {
            Navigation.setViewNavController(requireView(), navController)
        }

        onView(withId(R.id.ivShoppingImage)).perform(click())

        verify(navController).navigate(
            AddShoppingFragmentDirections.actionAddShoppingFragmentToImagePickFragment()
        )
    }

    @Test
    fun pressBackButton_shouldHaveEmptyImageURL(){
        val navController = mock(NavController::class.java)
        val fakeViewModel = ShoppingViewModel(FakeShoppingRepositoryAndroidTest())
        launchFragmentInHiltContainer<AddShoppingFragment> {
            Navigation.setViewNavController(requireView(),navController)
            fakeViewModel.setCurrentImageURL("https://fakeImageURL")
            mViewModel = fakeViewModel
        }
        pressBack()
        val currentImageURL = fakeViewModel.currentImageURLLiveData.getOrAwaitValue()
        assertThat(currentImageURL).isEmpty()
    }
}