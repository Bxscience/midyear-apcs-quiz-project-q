package com.example.tony_chen.apcsquiz;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DisplayQuestions extends AppCompatActivity {
    DataBaseHelper db;
    TextView questionId, correctIncorrect, choiceId;
    List<TextView> choicesId = new ArrayList<>();
    Button prevId, nextId, submitId, restartId;
    List<Question> questionsList;
    int currentIndex;
    final int WHITE = Color.parseColor("#CCFFFF");
    final int BLACK = Color.parseColor("#000000");
    final int GREEN = Color.parseColor("#00B200");
    final int RED = Color.parseColor("#990000");
    final String CORRECT = "Correct";
    final String INCORRECT = "Incorrect";
    public static final String EXTRA_MESSAGE = "com.example.tony_chen.apcsquiz.DisplayQuestions";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_questions);

        Intent intent = getIntent();
        currentIndex = 0;
        db = new DataBaseHelper(this);
        questionId = findViewById(R.id.question);
        choiceId = findViewById(R.id.choice);
        correctIncorrect = findViewById(R.id.correctIncorrect);
        choicesId.add((TextView)findViewById(R.id.c1));
        choicesId.add((TextView)findViewById(R.id.c2));
        choicesId.add((TextView)findViewById(R.id.c3));
        choicesId.add((TextView)findViewById(R.id.c4));
        choicesId.add((TextView)findViewById(R.id.c5));
        prevId = findViewById(R.id.previous);
        nextId = findViewById(R.id.next);
        submitId = findViewById(R.id.submit);
        restartId = findViewById(R.id.restart);

        if(intent.getParcelableArrayListExtra(MainActivity.QUESTIONSLIST)==null){
            questionsList = db.getQuestions(Integer.valueOf(intent.getStringExtra(MainActivity.getWhich())));
            restartId.setVisibility(View.GONE);
            Collections.shuffle(questionsList);
            displayQuestion();

            prevId.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    transition(BLACK, WHITE);
                    currentIndex -= 1;
                    displayQuestion();
                    transition(WHITE, BLACK);
                }
            });

            nextId.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    transition(BLACK, WHITE);
                    currentIndex += 1;
                    displayQuestion();
                    transition(WHITE, BLACK);
                }
            });

            choicesId.get(0).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickChoice(0);
                }
            });
            choicesId.get(1).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickChoice(1);
                }
            });
            choicesId.get(2).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickChoice(2);
                }
            });
            choicesId.get(3).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickChoice(3);
                }
            });

            choicesId.get(4).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickChoice(4);
                }
            });
        } else if (intent.getStringExtra(MainActivity.getWhich()).equals(MainActivity.getAnswers())){
            submitId.setVisibility(View.GONE);
            restartId.setVisibility(View.VISIBLE);
            questionsList = intent.getParcelableArrayListExtra(MainActivity.QUESTIONSLIST);
            Collections.shuffle(questionsList);
            displayAnswer();
            prevId.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    answerTransition();
                    currentIndex -= 1;
                    displayAnswer();
                }
            });

            nextId.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    answerTransition();
                    currentIndex += 1;
                    displayAnswer();
                }
            });
        } else if (intent.getStringExtra(MainActivity.getWhich()).equals(MainActivity.getRetry())){
            submitId.setVisibility(View.VISIBLE);
            restartId.setVisibility(View.INVISIBLE);
            questionsList = intent.getParcelableArrayListExtra(MainActivity.QUESTIONSLIST);
            Collections.shuffle(questionsList);
            displayRetry();
            prevId.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    answerTransition();
                    currentIndex -= 1;
                    displayRetry();
                }
            });

            nextId.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    answerTransition();
                    currentIndex += 1;
                    displayRetry();
                }
            });

            choicesId.get(0).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickChoice(0);
                }
            });
            choicesId.get(1).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickChoice(1);
                }
            });
            choicesId.get(2).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickChoice(2);
                }
            });
            choicesId.get(3).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickChoice(3);
                }
            });
            choicesId.get(4).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickChoice(4);
                }
            });
        }
    }

    public void submit(View v){
        Intent intent = new Intent(this, MainActivity.class);
        intent.putParcelableArrayListExtra(EXTRA_MESSAGE, (ArrayList<? extends Parcelable>) questionsList);
        startActivity(intent);
    }

    public void restart(View v){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
    // answer choice clicked
    public void clickChoice(int i) {
        Question question = questionsList.get(currentIndex);
        TextView clicked = choicesId.get(i);

        // if it's clicked already
        if (i == question.getSelectedIndex()) {
            question.setSelectedChoice("null");
            clicked.setTextColor(BLACK);
            clicked.setBackgroundColor(WHITE);
        } else {
            transition(BLACK, WHITE);
            question.setSelectedChoice(clicked.getText().toString());
            clicked.setTextColor(WHITE);
            clicked.setBackgroundColor(BLACK);
        }
    }
    // displays question
    public void displayQuestion(){
        updateButtons();
        Question question = questionsList.get(currentIndex);
        List<String> choices = question.getChoices();
        questionId.setText(formatString(question.getQuestion()));
        for(int i = 0; i < choicesId.size(); i++){
            choicesId.get(i).setText(formatString(choices.get(i)));
        }
        choiceId.setText(currentIndex+1 + "/" + questionsList.size());
    }

    //display correct answer
    public void displayAnswer(){
        updateButtons();
        choiceId.setText(currentIndex+1 + "/" + questionsList.size());
        Question question = questionsList.get(currentIndex);
        int selectedIndex = question.getSelectedIndex();
        List<String> choices = question.getChoices();
        questionId.setText(formatString(question.getQuestion()));
        for(int i = 0; i < choicesId.size(); i++){
            choicesId.get(i).setText(formatString(choices.get(i)));
        }
        if(selectedIndex==-1 || selectedIndex == question.getCorrectIndex()){
            choicesId.get(question.getCorrectIndex()).setBackgroundColor(GREEN);
        } else {
            choicesId.get(question.getCorrectIndex()).setBackgroundColor(GREEN);
            choicesId.get(selectedIndex).setBackgroundColor(RED);
        }
    }

    //display retry
    public void displayRetry(){
        updateButtons();
        choiceId.setText(currentIndex+1 + "/" + questionsList.size());
        Question question = questionsList.get(currentIndex);
        int selectedIndex = question.getSelectedIndex();
        List<String> choices = question.getChoices();
        questionId.setText(formatString(question.getQuestion()));
        for(int i = 0; i < choicesId.size(); i++){
            choicesId.get(i).setText(formatString(choices.get(i)));
        }

        if(question.getSelectedIndex()==-1){
            correctIncorrect.setText(INCORRECT);
            correctIncorrect.setTextColor(RED);
        }else if(question.getCorrectIndex() == selectedIndex){
            choicesId.get(selectedIndex).setBackgroundColor(GREEN);
            correctIncorrect.setText(CORRECT);
            correctIncorrect.setTextColor(GREEN);
        }else{
            choicesId.get(selectedIndex).setBackgroundColor(RED);
            correctIncorrect.setText(INCORRECT);
            correctIncorrect.setTextColor(RED);
        }
    }

    // sets visibility and enability of buttons
    public void updateButtons() {
        if (currentIndex == 0) {
            prevId.setVisibility(View.GONE);
            nextId.setVisibility(View.VISIBLE);
        } else if (currentIndex == questionsList.size() - 1) {
            prevId.setVisibility(View.VISIBLE);
            nextId.setVisibility(View.GONE);
        } else {
            prevId.setVisibility(View.VISIBLE);
            nextId.setVisibility(View.VISIBLE);
        }
    }

    // makes sure answer choices are not highlighted if not clicked due to previous question
    public void transition (int fontColor, int backgroundColor){
        int clickedIndex = questionsList.get(currentIndex).getSelectedIndex();
        if (clickedIndex != -1) {
            choicesId.get(clickedIndex).setTextColor(fontColor);
            choicesId.get(clickedIndex).setBackgroundColor(backgroundColor);
        }
    }

    public void answerTransition(){
        Log.d("UHOH", Integer.toString(currentIndex));
        Log.d("UHOH", questionsList.get(currentIndex).getQuestion());
        Log.d("UHOH", Integer.toString(questionsList.get(currentIndex).getChoices().size()));
        int clickedIndex = questionsList.get(currentIndex).getSelectedIndex();
        int correctIndex = questionsList.get(currentIndex).getCorrectIndex();
        if (clickedIndex != -1){
            choicesId.get(clickedIndex).setBackgroundColor(WHITE);
        }
        choicesId.get(correctIndex).setBackgroundColor(WHITE);
    }

    public String formatString(String s){
        String newS = s.replaceAll("\\\\n", "\n").replaceAll("\\\\t", "\t");
        return newS;
    }
}
