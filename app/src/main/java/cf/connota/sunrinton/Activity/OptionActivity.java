package cf.connota.sunrinton.Activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import cf.connota.sunrinton.R;
import cf.connota.sunrinton.databinding.ActivityOptionBinding;

public class OptionActivity extends AppCompatActivity {
    ActivityOptionBinding binding;
    int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_option);

        Intent i = getIntent();
        id = i.getIntExtra("_id", 0);

        if (id == 0) {
            Toast.makeText(this, "에러", Toast.LENGTH_SHORT).show();
            finish();
        }

        Toast.makeText(this, "id: " + id, Toast.LENGTH_SHORT).show();

        startActivity(new Intent(getApplicationContext(), ResponsePlz.class));
        finish();
    }
}
