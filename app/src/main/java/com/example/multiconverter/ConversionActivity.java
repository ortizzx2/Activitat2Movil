package com.example.multiconverter;

import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

/** Activity 2: converts a value between the units of the category received in the Intent. */
public class ConversionActivity extends AppCompatActivity {

    public static final String EXTRA_CATEGORY = "com.example.multiconverter.CATEGORY";

    private View root;
    private Spinner spSource;
    private Spinner spTarget;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_conversion);

        root = findViewById(R.id.main);
        ViewCompat.setOnApplyWindowInsetsListener(root, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        spSource = findViewById(R.id.spSource);
        spTarget = findViewById(R.id.spTarget);
        findViewById(R.id.btnBack).setOnClickListener(v -> finish());

        Category category = Category.fromName(getIntent().getStringExtra(EXTRA_CATEGORY));
        if (category == null) {
            showMissingCategory();
        } else {
            showCategory(category);
        }
    }

    private void showMissingCategory() {
        TextView title = findViewById(R.id.tvCategoryTitle);
        title.setText(R.string.title_unknown_category);
        findViewById(R.id.btnConvert).setEnabled(false);
        findViewById(R.id.btnSwap).setEnabled(false);
        showMessage(getText(R.string.msg_no_category));
    }

    private void showCategory(Category category) {
        TextView title = findViewById(R.id.tvCategoryTitle);
        title.setText(category.getLabel());

        if (category.getNote() != null) {
            TextView note = findViewById(R.id.tvNote);
            note.setText(category.getNote());
            note.setVisibility(View.VISIBLE);
        }

        // Position 0 is the empty choice, so the unit at position p is units.get(p - 1).
        List<String> names = new ArrayList<>();
        names.add(getString(R.string.select_unit));
        for (Unit unit : category.getUnits()) {
            names.add(unit.getName());
        }
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, names);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spSource.setAdapter(adapter);
        spTarget.setAdapter(adapter);
    }

    /** Hides the keyboard first because it would otherwise cover the Snackbar. */
    private void showMessage(CharSequence message) {
        getSystemService(InputMethodManager.class)
                .hideSoftInputFromWindow(root.getWindowToken(), 0);
        Snackbar.make(root, message, Snackbar.LENGTH_LONG).show();
    }
}
