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

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    // Clave con la que viaja la categoría a la Activity 2
    public static final String EXTRA_CATEGORIA = "categoria";

    /** Aspecto de cada categoría: imagen, color de acento y descripción corta. */
    private static final class Aspecto {
        final int imagen;
        final int color;
        final String descripcion;

        Aspecto(int imagen, int color, String descripcion) {
            this.imagen = imagen;
            this.color = color;
            this.descripcion = descripcion;
        }
    }

    private static final Map<Category, Aspecto> ASPECTOS = new EnumMap<>(Category.class);

    static {
        ASPECTOS.put(Category.DISTANCE, new Aspecto(R.drawable.distancia, R.color.cat_distancia,
                "Metros, kilómetros, millas y más"));
        ASPECTOS.put(Category.TEMPERATURE, new Aspecto(R.drawable.temperatura, R.color.cat_temperatura,
                "Celsius, Fahrenheit y Kelvin"));
        ASPECTOS.put(Category.VOLUME, new Aspecto(R.drawable.volumen, R.color.cat_volumen,
                "Litros, galones, tazas y más"));
        ASPECTOS.put(Category.MASS, new Aspecto(R.drawable.masa, R.color.cat_masa,
                "Gramos, kilos, libras y onzas"));
        ASPECTOS.put(Category.TIME, new Aspecto(R.drawable.tiempo, R.color.cat_tiempo,
                "Segundos, minutos, horas y días"));
        ASPECTOS.put(Category.DIGITAL, new Aspecto(R.drawable.informacion_digital, R.color.cat_digital,
                "Bytes, megabytes, gigabytes y más"));
        ASPECTOS.put(Category.CURRENCY, new Aspecto(R.drawable.moneda, R.color.cat_moneda,
                "Euros, dólares, libras y yenes"));
        ASPECTOS.put(Category.AREA, new Aspecto(R.drawable.area, R.color.cat_area,
                "Metros cuadrados, hectáreas y acres"));
        ASPECTOS.put(Category.SPEED, new Aspecto(R.drawable.velocidad, R.color.cat_velocidad,
                "Km/h, millas por hora y nudos"));
        ASPECTOS.put(Category.PRESSURE, new Aspecto(R.drawable.presion, R.color.cat_presion,
                "Pascales, bares, atmósferas y PSI"));
    }

    // Posición 0 = "sin elegir"; la posición p >= 1 es categorias.get(p - 1)
    private final List<Category> categorias = new ArrayList<>();

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

        // Las categorías salen de la tabla Category: añadir una allí la añade aquí
        List<String> nombres = new ArrayList<>();
        nombres.add(getString(R.string.elige_categoria));
        for (Category categoria : Category.values()) {
            categorias.add(categoria);
            nombres.add(categoria.getLabel());
        }

        ArrayAdapter<String> adaptador = new ArrayAdapter<>(this, R.layout.spinner_item, nombres);
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
        Category categoria = categorias.get(posicion - 1);
        Aspecto aspecto = ASPECTOS.get(categoria);

        layoutHint.setVisibility(View.GONE);
        layoutContenido.setVisibility(View.VISIBLE);
        Bitmap bmp = BitmapFactory.decodeResource(getResources(), aspecto.imagen);
        bmp = Bitmap.createScaledBitmap(bmp, 400, 400, true);
        ivCategoria.setImageBitmap(bmp);

        // Halo de color suave detrás de la imagen + nombre y descripción
        int color = ContextCompat.getColor(this, aspecto.color);
        viewHalo.setBackgroundTintList(ColorStateList.valueOf(ColorUtils.setAlphaComponent(color, 46)));
        tvNombre.setText(categoria.getLabel());
        tvDescripcion.setText(aspecto.descripcion);

        // Pequeña animación de entrada para que el cambio se vea suave
        layoutContenido.setAlpha(0f);
        layoutContenido.setScaleX(0.92f);
        layoutContenido.setScaleY(0.92f);
        layoutContenido.animate().alpha(1f).scaleX(1f).scaleY(1f).setDuration(250).start();
    }

    private void continuar() {
        int posicion = spinnerCategoria.getSelectedItemPosition();
        if (posicion == 0) {
            Snackbar.make(findViewById(R.id.main), R.string.msg_elige_categoria, Snackbar.LENGTH_LONG).show();
            return;
        }
        Intent intent = new Intent(this, ConversionActivity.class);
        intent.putExtra(EXTRA_CATEGORIA, categorias.get(posicion - 1).getLabel());
        startActivity(intent);
    }
}
