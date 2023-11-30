package com.example.udare;

import static org.junit.Assert.assertTrue;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.test.rule.ActivityTestRule;

import com.example.udare.presentation.Inicio;
import com.example.udare.presentation.PerfilActivity;

import org.junit.Rule;
import org.junit.Test;

public class UploadProfilePictureTest {
    @Rule
    public ActivityTestRule<PerfilActivity> activityRule = new ActivityTestRule<>(PerfilActivity.class);
    @Test
    public void testPerformance() {
        PerfilActivity perfil = activityRule.getActivity();

        // Punto de medición antes de la operación crítica
        long startTime = System.currentTimeMillis();

        Bitmap bitmap = getBitmapFromResource(perfil, R.drawable.foto1);

        // Llamada a la función o método crítico
        perfil.uploadProfilePicture(bitmap,"655ce5f1283c613adce3b30c");

        // Punto de medición después dbe la operación crítica
        long endTime = System.currentTimeMillis();

        // Calcular la duración y evaluar el rendimiento
        long duration = endTime - startTime;
        assertTrue("La operación tardó demasiado tiempo: " + duration + " ms", duration < 1000);
    }

    public static Bitmap getBitmapFromResource(Context context, int resourceId) {
        // Decodificar la imagen de recursos en un objeto Bitmap
        return BitmapFactory.decodeResource(context.getResources(), resourceId);
    }
}
