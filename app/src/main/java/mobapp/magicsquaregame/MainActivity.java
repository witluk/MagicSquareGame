package mobapp.magicsquaregame;

import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int[] EDITTEXT_IDS = {
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
    EditText lvl;
    Button submit, new_game, cont, exit_game, help;
    Chronometer timer;
    TextView score;

    int lvlVal = 0;

    boolean ifAllEntered = true;

    List<Integer> tempInputArray = new ArrayList<>();

    int[][] inputArray = new int[3][3];

    int[] resultArray = new int[6];

    List<Integer> helpArray = new ArrayList<>();
    int helpCount = 0;

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
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getResources().getConfiguration().orientation==1) {
            setContentView(R.layout.activity_main);
        }
        else{
            setContentView(R.layout.landscape_layout);
        }

        score = (TextView) findViewById(R.id.score);

        timer = (Chronometer) findViewById(R.id.timer);
        timer.start();

        lvl = (EditText) findViewById(R.id.lvl);

        submit = (Button) findViewById(R.id.submit);
        new_game = (Button) findViewById(R.id.new_game);
        cont = (Button) findViewById(R.id.resume);
        exit_game = (Button) findViewById(R.id.exit_game);
        help = (Button) findViewById(R.id.help);

        int k = 0;
        for(int id : TEXTVIEW_IDS) {
            results[k] = (TextView) findViewById(id);
            k++;
        }

        int row=0, col=0;
        for(int id : EDITTEXT_IDS) {
            if(col==3){
                col=0;
                row++;
            }
            nums[row][col] = (EditText) findViewById(id);
            nums[row][col].addTextChangedListener(tw);
            col++;
        }

        setFields();
        setHelpArray();
    }

    public void setFields(){
        if(tempInputArray.isEmpty()) {
            for (int i = 1; i < 10; i++) {
                tempInputArray.add(i);
            }
        }

        Collections.shuffle(tempInputArray);

        for(int i=0; i<3; i++) {
            for (int j = 0; j < 3; j++) {
                inputArray[i][j] = tempInputArray.get(i+j+i*2);
            }
        }

        for(int i=0; i<3; i++) {
            int sumRow=0;
            int sumCol=0;
            for (int j = 0; j < 3; j++) {
                sumRow += this.inputArray[i][j];
                sumCol += this.inputArray[j][i];
            }
            results[i].setText(Integer.toString(sumRow));
            results[i+3].setText(Integer.toString(sumCol));
        }
    }

    public int setScore(){
        String mins = timer.getText().toString().substring(0,2);
        String secs = timer.getText().toString().substring(3,5);
        int allSecs = Integer.parseInt(secs) + Integer.parseInt(mins) * 60;
        int score = 1000 - allSecs*2 - helpCount*100 +lvlVal*100;
        if(score<0){
            score=0;
        }
        return score;
    }

    public void submit(View v){
        if(isWin()){
            Toast.makeText(getApplicationContext(), "Congrats!!! You win!!!", Toast.LENGTH_LONG).show();
            score.setText("Score: " + Integer.toString(setScore()));
            cont.setEnabled(false);
            new_game.setEnabled(true);
        }
        else{
            Toast.makeText(getApplicationContext(), "Answer is wrong!!!", Toast.LENGTH_LONG).show();
            cont.setEnabled(true);
            new_game.setEnabled(true);
        }
        for(int i=0; i<3; i++){
            for(int j=0; j<3; j++) {
                nums[i][j].setEnabled(false);
            }
        }
        submit.setEnabled(false);
        help.setEnabled(false);
        timer.stop();
    }

    public void resume(View v){
        for(int i=0; i<3; i++){
            for(int j=0; j<3; j++) {
                nums[i][j].setEnabled(true);
            }
        }
        cont.setEnabled(false);
        new_game.setEnabled(false);
        submit.setEnabled(true);
        help.setEnabled(true);
        timer.start();
    }

    public void setHelpArray(){
        for (int i = 0; i < 9; i++) {
            helpArray.add(i);
        }

        Collections.shuffle(helpArray);
    }

    public void help(View v){
        if(helpCount<9 && !new_game.isEnabled()) {
            int randInt;
            do {
                randInt = helpArray.get(helpCount);
                helpCount++;
            }while(!nums[randInt / 3][randInt % 3].getText().toString().equals("") && nums[randInt / 3][randInt % 3].getText().toString().equals(Integer.toString(inputArray[randInt / 3][randInt % 3])));

            for(int i=0; i<3; i++) {
                for (int j = 0; j < 3; j++) {
                    if(nums[i][j].getText().toString().equals(Integer.toString(inputArray[randInt / 3][randInt % 3]))){
                        nums[i][j].setText("");
                    }
                }
            }

            nums[randInt / 3][randInt % 3].setText(Integer.toString(inputArray[randInt / 3][randInt % 3]));
            nums[randInt / 3][randInt % 3].setEnabled(false);
        }
        else{
            Toast.makeText(getApplicationContext(), "Congrats!!! You win!!!", Toast.LENGTH_LONG).show();
            score.setText("Score: " + Integer.toString(setScore()));
            cont.setEnabled(false);
            submit.setEnabled(false);
            help.setEnabled(false);
            new_game.setEnabled(true);
        }
    }

    public void setLvl(View v){
        setFields();
        for(int i=0; i<3; i++){
            for(int j=0; j<3; j++) {
                nums[i][j].setEnabled(true);
            }
        }
        Collections.shuffle(helpArray);
        score.setText("");
        timer.setBase(SystemClock.elapsedRealtime());
        timer.start();
        helpCount = 0;
        for(int i=0; i<3; i++) {
            for (int j = 0; j < 3; j++) {
                nums[i][j].setText("");
            }
        }
        cont.setEnabled(false);
        new_game.setEnabled(false);
        submit.setEnabled(false);
        help.setEnabled(true);

        lvlVal = Integer.parseInt(lvl.getText().toString());
        for(int i=8;i>=lvlVal;i--) {
            int randInt = helpArray.get(helpCount);
            helpCount++;
            nums[randInt / 3][randInt % 3].setText(Integer.toString(inputArray[randInt / 3][randInt % 3]));
        }
    }

    public void newGame(View v){
        setFields();
        Collections.shuffle(helpArray);
        score.setText("");
        timer.setBase(SystemClock.elapsedRealtime());
        timer.start();
        helpCount = 0;
        for(int i=0; i<3; i++) {
            for (int j = 0; j < 3; j++) {
                nums[i][j].setText("");
            }
        }
        cont.setEnabled(false);
        new_game.setEnabled(false);
        submit.setEnabled(false);
        help.setEnabled(true);
        lvl.setText("");
        for(int i=0; i<3; i++){
            for(int j=0; j<3; j++) {
                nums[i][j].setEnabled(true);
            }
        }
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

    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putLong("Timer", timer.getBase());
        super.onSaveInstanceState(savedInstanceState);
    }

    public void onRestoreInstanceState(Bundle savedInstanceState){
        if((savedInstanceState !=null) && savedInstanceState.containsKey("Timer"))
            timer.setBase(savedInstanceState.getLong("Timer"));
        super.onRestoreInstanceState(savedInstanceState);
    }
}
