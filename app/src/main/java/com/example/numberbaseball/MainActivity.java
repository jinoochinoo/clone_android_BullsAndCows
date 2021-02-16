package com.example.numberbaseball;

import androidx.appcompat.app.AppCompatActivity;

import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    int[] targetNumbers = new int[3];
    int inputCount;
    int hitCount;

    TextView[] inputArray = new TextView[3];
    Button[] buttonArray = new Button[10];

    ImageButton backSpaceBtn;
    ImageButton hitButton;

    TextView resultTextView;
    ScrollView scrollView;

    SoundPool soundPool;
    int[] buttonSound = new int[5];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        targetNumbers = getNumbers();

        for (int i = 0; i < inputArray.length; i++) {
            inputArray[i] = findViewById(R.id.input_text_0 + i);
        }

        for (int i = 0; i < buttonArray.length; i++) {
            buttonArray[i] = findViewById(R.id.num_button_0 + i);
        }

        backSpaceBtn = findViewById(R.id.back_space_button);
        hitButton = findViewById(R.id.hit_button);
        resultTextView = findViewById(R.id.resultTextView);
        scrollView = findViewById(R.id.scroll_view);

        for(Button getButton : buttonArray){
            getButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    numButtonClick(v);
                }
            });
        }

        backSpaceBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                backSpaceClick();
            }
        });

        hitButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(inputCount < 3){
                    Toast.makeText(getApplicationContext(), "숫자를 입력해주세요.", Toast.LENGTH_SHORT).show();
                } else{
                    int[] userNumbers = new int[3];
                    for (int i = 0; i < userNumbers.length; i++) {
                        userNumbers[i] = Integer.parseInt(inputArray[i].getText().toString());
                    }
                    int[] countCheck = new int[2];
                    countCheck = getCountCheck(targetNumbers, userNumbers);
                    Log.e("hitButton", "countCheck ---- S : " + countCheck[0] + " B : " + countCheck[1]);

                    String resultText = getCountString(userNumbers, countCheck);

                    if(hitCount == 1){
                        resultTextView.setText(resultText);
                    } else{
                        resultTextView.append(resultText);
                    }
                    resultTextView.append("\n");

                    if(countCheck[0] == 3){
                        hitCount = 1;
                        targetNumbers = getNumbers();
                    } else{
                        hitCount++;
                    }

                    // 포커스 내리기
                    scrollView.fullScroll(View.FOCUS_DOWN);

                    // TextView, Button, inputCount 초기화
                    for(TextView textView : inputArray){
                        textView.setText("");
                    }
                    for(Button button : buttonArray){
                        button.setEnabled(true);
                    }
                    inputCount = 0;
                }
            }
        });
    }

    private String getCountString(int[] userNumbers, int[] countCheck) {
        String resultText;// 아웃
        if(countCheck[0] == 3){
            resultText = hitCount + " [" + userNumbers[0] + " " + userNumbers[1] + " " +
                    userNumbers[2] + " ]  아웃! 게임 끝! ";

            // SoundPool 세팅
            soundPool.play(buttonSound[0], 1, 1, 1, 0, 1);
        } else {
            resultText = hitCount + " [" + userNumbers[0] + " " + userNumbers[1] + " " +
                    userNumbers[2] + " ]  S : " + countCheck[0] + " / B : "
                    + countCheck[1];
        }
        return resultText;
    }

    private void backSpaceClick() {
        if (inputCount > 0) {
            // 버튼 enable 상태로 되돌리기
            int buttonRefresh = Integer.parseInt(inputArray[inputCount -1].getText().toString());
            buttonArray[buttonRefresh].setEnabled(true);
            // 입력된 값 삭제
            inputArray[inputCount - 1].setText("");
            inputCount--;
            // SoundPool 세팅
            soundPool.play(buttonSound[3], 1, 1, 1, 0, 1);
        } else{
            Toast.makeText(getApplicationContext(), "숫자를 입력해주세요.", Toast.LENGTH_SHORT).show();
        }
    }

    private void numButtonClick(View v) {
        if(inputCount < 3){
            Button button = findViewById(v.getId());
            inputArray[inputCount].setText(button.getText().toString());
            button.setEnabled(false);
            inputCount++;

            // SoundPool 세팅
            soundPool.play(buttonSound[2], 1, 1, 1, 0, 1);
        } else{
            Toast.makeText(getApplicationContext(), "hit 버튼을 클릭하세요", Toast.LENGTH_SHORT).show();
        }
    }

    private int[] getCountCheck(int[] targetNumbers, int[] userNumbers) {
        int[] setCount = new int[2];
        for (int i = 0; i < targetNumbers.length; i++) {
            for (int j = 0; j < userNumbers.length; j++) {

                if(targetNumbers[i] == userNumbers[j]){
                    // 스트라이크
                    if(i == j){
                        setCount[0]++;
                    } else{ // 볼
                        setCount[1]++;
                    }
                }
            }
        }
        return setCount;
    }


    // 무작위로 숫자 3개 뽑아서 target 설정
    public int[] getNumbers(){
        int[] setNumbers = new int[3];

        for(int i=0; i< setNumbers.length; i++){
            setNumbers[i] = new Random().nextInt(10);
            for (int j = 0; j < i; j++) {
                if(setNumbers[i] == setNumbers[j]){
                        i--;
                        break;
                }
            }
        }
        Log.e("setNumbers", "setNumbers = " + setNumbers[0] + ", " + setNumbers[1] + ", " + setNumbers[2]);
        return setNumbers;
    }

    // SoundPool, ButtonSound 세팅
    @Override
    protected void onStart() {
        super.onStart();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_GAME).
                            setContentType(AudioAttributes.CONTENT_TYPE_MUSIC).build();

            soundPool = new SoundPool.Builder().setAudioAttributes(audioAttributes)
                    .setMaxStreams(6).build();
        } else{
            soundPool = new SoundPool(6, AudioManager.STREAM_MUSIC, 0);
        }

        for (int i = 0; i < buttonSound.length; i++) {
            buttonSound[i] = soundPool.load(getApplicationContext(), R.raw.button_1 + i, 1);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        soundPool.release();
    }
}