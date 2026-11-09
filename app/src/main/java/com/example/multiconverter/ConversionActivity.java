package com.example.multiconverter;

import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
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
import java.util.Locale;

/** Activity 2: converts a value between the units of the category received in the Intent. */
public class ConversionActivity extends AppCompatActivity {

    private static final int NO_UNIT = 0;

    private View root;
    private EditText etValue;
    private Spinner spSource;
    private Spinner spTarget;
    private TextView tvResult;
    private Category category;

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

        etValue = findViewById(R.id.etValue);
        spSource = findViewById(R.id.spSource);
        spTarget = findViewById(R.id.spTarget);
        tvResult = findViewById(R.id.tvResult);
        findViewById(R.id.btnConvert).setOnClickListener(v -> convert());
        findViewById(R.id.btnSwap).setOnClickListener(v -> swapUnits());
        findViewById(R.id.btnBack).setOnClickListener(v -> finish());

        category = Category.fromLabel(getIntent().getStringExtra(MainActivity.EXTRA_CATEGORIA));
        if (category == null) {
            showMissingCategory();
        } else {
            showCategory();
        }
    }

    private void showMissingCategory() {
        TextView title = findViewById(R.id.tvCategoryTitle);
        title.setText(R.string.title_unknown_category);
        findViewById(R.id.btnConvert).setEnabled(false);
        findViewById(R.id.btnSwap).setEnabled(false);
        showMessage(getText(R.string.msg_no_category));
    }

    private void showCategory() {
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

    private void swapUnits() {
        int source = spSource.getSelectedItemPosition();
        int target = spTarget.getSelectedItemPosition();
        if (source == NO_UNIT || target == NO_UNIT) {
            showMessage(getText(R.string.msg_select_units));
            return;
        }
        spSource.setSelection(target);
        spTarget.setSelection(source);
    }

    private void convert() {
        tvResult.setText(R.string.result_placeholder);

        // Some keyboards type a decimal comma.
        String text = etValue.getText().toString().trim().replace(',', '.');
        int source = spSource.getSelectedItemPosition();
        int target = spTarget.getSelectedItemPosition();

        if (text.isEmpty()) {
            showMessage(getText(R.string.msg_empty_value));
            return;
        }
        Double value = parseNumber(text);
        if (value == null) {
            showMessage(getText(R.string.msg_invalid_value));
            return;
        }
        if (source == NO_UNIT || target == NO_UNIT) {
            showMessage(getText(R.string.msg_select_units));
            return;
        }
        if (source == target) {
            showMessage(getText(R.string.msg_same_units));
            return;
        }

        Unit from = category.getUnits().get(source - 1);
        Unit to = category.getUnits().get(target - 1);
        if (!category.isInRange(value, from)) {
            showMessage(category.getRangeError());
            return;
        }

        double result = from.convertTo(value, to);
        if (Double.isInfinite(result)) {
            showMessage(getText(R.string.msg_too_large));
            return;
        }
        tvResult.setText(getString(R.string.result_format,
                text, from.getName(), format(result), to.getName()));
        showMessage(getText(R.string.msg_success));
    }

    /** Returns the number written in the text, or null if it is not a finite number. */
    private static Double parseNumber(String text) {
        try {
            double number = Double.parseDouble(text);
            return Double.isFinite(number) ? number : null;
        } catch (NumberFormatException e) {
            return null;
        }
    }

    /** Two decimals, or scientific notation when two decimals would hide the value. */
    private static String format(double number) {
        double size = Math.abs(number);
        boolean scientific = size != 0 && (size < 0.01 || size >= 1e12);
        return String.format(Locale.US, scientific ? "%.4e" : "%.2f", number);
    }

    /** Hides the keyboard first because it would otherwise cover the Snackbar. */
    private void showMessage(CharSequence message) {
        getSystemService(InputMethodManager.class)
                .hideSoftInputFromWindow(root.getWindowToken(), 0);
        Snackbar.make(root, message, Snackbar.LENGTH_LONG).show();
    }
}
