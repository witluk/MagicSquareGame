package mobapp.magicsquaregame;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final int[] BUTTON_IDS = {
            R.id.num1,
            R.id.num2,
            R.id.num3,
            R.id.num4,
            R.id.num5,
            R.id.num6,
            R.id.num7,
            R.id.num8,
            R.id.num9,
    };

    private static final int[] TEXTVIEW_IDS = {
            R.id.result1,
            R.id.result2,
            R.id.result3,
            R.id.result4,
            R.id.result5,
            R.id.result6,
    };

    EditText [][] nums = new EditText[3][3];
    TextView [] results = new TextView[6];
    Button submit, new_game, cont, exit_game;

    boolean ifAllEntered = true;

    TextWatcher tw = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if(!s.toString().equals("") && isSame(s.toString())) {
                s.clear();
            }
            outerloop:
            for(int i=0; i<3; i++) {
                for (int j=0; j<3; j++) {
                    if (nums[i][j].getText().toString().equals("")) {
                        ifAllEntered = false;
                        break outerloop;
                    }
                }
            }
            if(ifAllEntered){
                submit.setEnabled(true);
            }
            else{
                submit.setEnabled(false);
                ifAllEntered = true;
            }
        }
    /* code goes here */
    /* viewOnFocus can be used here */
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        submit = (Button) findViewById(R.id.submit);
        new_game = (Button) findViewById(R.id.new_game);
        cont = (Button) findViewById(R.id.resume);
        exit_game = (Button) findViewById(R.id.exit_game);

        int k = 0;
        for(int id : TEXTVIEW_IDS) {
            results[k] = (TextView) findViewById(id);
            k++;
        }

        int row=0, col=0;
        for(int id : BUTTON_IDS) {
            if(col==3){
                col=0;
                row++;
            }
            nums[row][col] = (EditText) findViewById(id);
            nums[row][col].addTextChangedListener(tw);
            col++;
        }

    }

    public void submit(View v){
        if(isWin()){
            Toast.makeText(getApplicationContext(), "Congrats!!! You win!!!", Toast.LENGTH_LONG).show();
            cont.setEnabled(false);
            new_game.setEnabled(true);
        }
        else{
            Toast.makeText(getApplicationContext(), "Answer is wrong!!!", Toast.LENGTH_LONG).show();
            cont.setEnabled(true);
            new_game.setEnabled(true);
        }
        submit.setEnabled(false);
    }

    public void resume(View v){
        cont.setEnabled(false);
        new_game.setEnabled(false);
        submit.setEnabled(true);
    }

    public void newGame(View v){
        for(int i=0; i<3; i++) {
            for (int j = 0; j < 3; j++) {
                nums[i][j].setText("");
            }
        }
        cont.setEnabled(false);
        new_game.setEnabled(false);
        submit.setEnabled(false);
    }

    public void exitGame(View v){
        finish();
    }



    public boolean isWin(){
        for(int i=0; i<3; i++) {
            int sumRow=0;
            int sumCol=0;
            for (int j = 0; j < 3; j++) {
                sumRow += Integer.parseInt(this.nums[i][j].getText().toString());
                sumCol += Integer.parseInt(this.nums[j][i].getText().toString());
            }

            int resRow = Integer.parseInt(this.results[i].getText().toString());
            int resCol = Integer.parseInt(this.results[i+3].getText().toString());

            if(sumRow!=resRow && sumCol!=resCol){
                return false;
            }
        }
        return true;
    }

    public boolean isSame(String s){
        int l = 0;
        for(int i=0; i<3; i++) {
            for (int j = 0; j < 3; j++) {
                if(s.equals(nums[i][j].getText().toString())){
                    l++;
                }
            }
        }
        if(l>1){
            return true;
        }
        else{
            return false;
        }
    }

}
