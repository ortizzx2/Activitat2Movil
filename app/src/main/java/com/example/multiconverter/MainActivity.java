package com.example.multiconverter;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
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

    private Spinner spinnerCategoria;
    private ImageView ivCategoria;
    private Button btnContinuar;

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

        ArrayAdapter<String> adaptador = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categorias);
        adaptador.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
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
            return;
        }
        Bitmap bmp = BitmapFactory.decodeResource(getResources(), imagenes[posicion]);
        bmp = Bitmap.createScaledBitmap(bmp, 400, 400, true);
        ivCategoria.setImageBitmap(bmp);
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