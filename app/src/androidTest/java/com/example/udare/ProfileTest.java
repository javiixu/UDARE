package com.example.udare;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.udare.presentation.PerfilActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class ProfileTest {

    @Rule
    public ActivityScenarioRule<PerfilActivity> activityRule = new ActivityScenarioRule<>(PerfilActivity.class);

    @Test
    public void testDataProfile() {
        // Asegúrate de que la actividad de perfil esté visible
        onView(withId(R.id.IdConstraintLayoutPerfil)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()));


        //Comprobación de los puntos
        onView(withId(R.id.tvCookingPoints)).check(ViewAssertions.matches(ViewMatchers.withText("200")));
        onView(withId(R.id.tvCulturePoints)).check(ViewAssertions.matches(ViewMatchers.withText("400")));
        onView(withId(R.id.tvSportPoints)).check(ViewAssertions.matches(ViewMatchers.withText("400")));
        onView(withId(R.id.tvGrowthPoints)).check(ViewAssertions.matches(ViewMatchers.withText("300")));
        onView(withId(R.id.tvSocialPoints)).check(ViewAssertions.matches(ViewMatchers.withText("400")));

        //comprobación username
        onView(withId(R.id.tvUserName)).check(ViewAssertions.matches(ViewMatchers.withText("usuarioa")));

        //comprobación bio usuario
        onView(withId(R.id.tvUserBio)).check(ViewAssertions.matches(ViewMatchers.withText("Esta es una bio de prueba para ver si se muestra correctamente")));




    }
}

