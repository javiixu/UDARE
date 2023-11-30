package com.example.udare;

import static org.junit.Assert.assertTrue;

import androidx.test.rule.ActivityTestRule;

import com.example.udare.presentation.Inicio;

import org.junit.Rule;
import org.junit.Test;

public class GetUserByIdTest {

    @Rule
    public ActivityTestRule<Inicio> activityRule = new ActivityTestRule<>(Inicio.class);
    @Test
    public void testPerformance() {
        Inicio inicio = activityRule.getActivity();

        // Punto de medición antes de la operación crítica
        long startTime = System.currentTimeMillis();

        // Llamada a la función o método crítico
        inicio.getUserById("655ce5f1283c613adce3b30c");

        // Punto de medición después de la operación crítica
        long endTime = System.currentTimeMillis();

        // Calcular la duración y evaluar el rendimiento
        long duration = endTime - startTime;
        assertTrue("La operación tardó demasiado tiempo: " + duration + " ms", duration < 1000);
    }
}
