package com.example.udare;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.udare.Adapter.RecyclerViewItemCountAssertion;
import com.example.udare.presentation.PerfilActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class FollowingTest {

    @Rule
    public ActivityScenarioRule<PerfilActivity> activityRule = new ActivityScenarioRule<>(PerfilActivity.class);

    @Test
    public void testFollowingButton() {
        // Asegúrate de que la actividad de perfil esté visible
        onView(withId(R.id.IdConstraintLayoutPerfil)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()));

        // Da clic en el botón de followers
        onView(withId(R.id.buttonFollowing)).perform(ViewActions.click());

        // Verifica que la nueva actividad de seguidores se muestra correctamente
        onView(withId(R.id.recyclerViewFollowing)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()));

        // Verifica que el numero de seguidos es correcto
        onView(withId(R.id.recyclerViewFollowing)).check(new RecyclerViewItemCountAssertion(1));

    }
}
