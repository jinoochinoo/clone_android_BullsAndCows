package com.example.numberbaseball;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    int[] targetNumbers = new int[3];

    TextView[] inputArray = new TextView[3];
    int inputCount;
    Button[] buttonArray = new Button[10];

    ImageButton backSpaceBtn;
    ImageButton hitButton;

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

        for(Button getButton : buttonArray){
            getButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(inputCount < 3){
                        Button button = findViewById(v.getId());
                        inputArray[inputCount].setText(button.getText().toString());
                        button.setEnabled(false);
                        inputCount++;
                    } else{
                        Toast.makeText(getApplicationContext(), "hit 버튼을 클릭하세요", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

        backSpaceBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if (inputCount > 0) {
                    // 버튼 enable 상태로 되돌리기
                    int buttonRefresh = Integer.parseInt(inputArray[inputCount -1].getText().toString());
                    buttonArray[buttonRefresh].setEnabled(true);
                    // 입력된 값 삭제
                    inputArray[inputCount - 1].setText("");
                    inputCount--;
                } else{
                    Toast.makeText(getApplicationContext(), "숫자를 입력해주세요.", Toast.LENGTH_SHORT).show();
                }
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

    private int[] getCountCheck(int[] targetNumbers, int[] userNumbers) {
        int[] setCount = new int[2];
        for (int i = 0; i < targetNumbers.length; i++) {
            for (int j = 0; j < userNumbers.length; j++) {
                // 스트라이크
                if(targetNumbers[i] == userNumbers[j] && i == j){
                    setCount[0]++;
                    // 볼
                } else if(targetNumbers[i] == userNumbers[j] && i != j){
                    setCount[1]++;
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
}