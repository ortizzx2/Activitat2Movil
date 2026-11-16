package com.example.multiconverter;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.ColorUtils;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {

    // Clave con la que viaja la categoría a la Activity 2
    public static final String EXTRA_CATEGORIA = "categoria";

    // Posición 0 = "sin elegir". Las dos listas van en paralelo
    private final String[] categorias = {"Elige una categoría...", "Distancia", "Temperatura", "Masa", "Tiempo"};
    private final int[] imagenes = {0, R.drawable.distancia, R.drawable.temperatura, R.drawable.masa, R.drawable.tiempo};

    // Solo visual: texto y color de acento de cada categoría (mismo orden que arriba)
    private final String[] descripciones = {"", "Metros, kilómetros, millas y más", "Celsius, Fahrenheit y Kelvin",
            "Gramos, kilos, libras y onzas", "Segundos, minutos, horas y días"};
    private final int[] colores = {0, R.color.cat_distancia, R.color.cat_temperatura, R.color.cat_masa, R.color.cat_tiempo};

    private Spinner spinnerCategoria;
    private ImageView ivCategoria;
    private Button btnContinuar;
    private View layoutHint;
    private View layoutContenido;
    private View viewHalo;
    private TextView tvNombre;
    private TextView tvDescripcion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        spinnerCategoria = findViewById(R.id.spinnerCategoria);
        ivCategoria = findViewById(R.id.ivCategoria);
        btnContinuar = findViewById(R.id.btnContinuar);

        layoutHint = findViewById(R.id.layoutHint);
        layoutContenido = findViewById(R.id.layoutContenido);
        viewHalo = findViewById(R.id.viewHalo);
        tvNombre = findViewById(R.id.tvNombre);
        tvDescripcion = findViewById(R.id.tvDescripcion);

        ArrayAdapter<String> adaptador = new ArrayAdapter<>(this, R.layout.spinner_item, categorias);
        adaptador.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinnerCategoria.setAdapter(adaptador);

        // Al elegir una categoría se cambia la imagen
        spinnerCategoria.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mostrarImagen(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        btnContinuar.setOnClickListener(v -> continuar());
    }

    private void mostrarImagen(int posicion) {
        if (posicion == 0) {
            ivCategoria.setImageBitmap(null);
            layoutHint.setVisibility(View.VISIBLE);
            layoutContenido.setVisibility(View.GONE);
            return;
        }
        layoutHint.setVisibility(View.GONE);
        layoutContenido.setVisibility(View.VISIBLE);
        Bitmap bmp = BitmapFactory.decodeResource(getResources(), imagenes[posicion]);
        bmp = Bitmap.createScaledBitmap(bmp, 400, 400, true);
        ivCategoria.setImageBitmap(bmp);

        // Halo de color suave detrás de la imagen + nombre y descripción
        int color = ContextCompat.getColor(this, colores[posicion]);
        viewHalo.setBackgroundTintList(ColorStateList.valueOf(ColorUtils.setAlphaComponent(color, 46)));
        tvNombre.setText(categorias[posicion]);
        tvDescripcion.setText(descripciones[posicion]);

        // Pequeña animación de entrada para que el cambio se vea suave
        layoutContenido.setAlpha(0f);
        layoutContenido.setScaleX(0.92f);
        layoutContenido.setScaleY(0.92f);
        layoutContenido.animate().alpha(1f).scaleX(1f).scaleY(1f).setDuration(250).start();
    }

    private void continuar() {
        int posicion = spinnerCategoria.getSelectedItemPosition();
        if (posicion == 0) {
            Snackbar.make(findViewById(R.id.main), "Elige una categoría para continuar", Snackbar.LENGTH_LONG).show();
            return;
        }
        Intent intent = new Intent(this, ConversionActivity.class);
        intent.putExtra(EXTRA_CATEGORIA, categorias[posicion]);
        startActivity(intent);
    }
}