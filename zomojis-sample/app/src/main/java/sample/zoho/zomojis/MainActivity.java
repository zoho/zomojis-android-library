package sample.zoho.zomojis;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Spannable;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zoho.zomojis.Zomojis;


public class MainActivity extends AppCompatActivity {

    EditText inputEdittext;
    TextView zomojisizetext;
    LinearLayout layout;
    View keyboardView;
    TextView previewTextview;
    int size = 50;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        layout = (LinearLayout) findViewById(R.id.zomojiLayout);
        inputEdittext = (EditText) findViewById(R.id.edittext);
        zomojisizetext = (TextView) findViewById(R.id.zomojisize);
        previewTextview = (TextView) findViewById(R.id.text);

        Zomojis.prepareKeyboard(this, inputEdittext, size);
        keyboardView = Zomojis.getKeyBoard();
        layout.addView(keyboardView);

        layout.setVisibility(View.GONE);
    }

    public void go(View v) {
        String input = inputEdittext.getText().toString();
        boolean isSingle =  Zomojis.isSingleZomoji(input);
        Spannable spannable = Zomojis.getSpan(previewTextview, input, size, isSingle);
        previewTextview.setText(spannable);
    }

    public void open(View v)
    {
        layout.setVisibility(View.VISIBLE);
    }

    public void close(View v)
    {
        layout.setVisibility(View.GONE);
    }

    public void add(View v)
    {
        size = Integer.parseInt(zomojisizetext.getText().toString()) + 10;
        zomojisizetext.setText(""+size);
    }

    public void reduce(View v) {
        int value = Integer.parseInt(zomojisizetext.getText().toString());
        if(value>50) {
            size = Integer.parseInt(zomojisizetext.getText().toString()) - 10;
        }
        zomojisizetext.setText(""+size);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Zomojis.unregister();
    }
}
