package com.example.tony_chen.apcsquiz;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    static final ArrayList<String> allTopics = new ArrayList<>(Arrays.asList("Arithmetic and Boolean Expressions", "Loops", "Arrays", "ArrayList", "Strings", "Recursion", "Algorithms", "Class, Interface, Inheritance, Polymorphism", "Fields, Methods, Parameter Passing", "Searching and Sorting Algorithms"));
    public static final String WHICH = "which";
    public static final String QUESTIONSLIST = "list";
    public static final String ANSWERS = "answers";
    public static final String RETRY = "retry";
    List<Question> questionsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView score = findViewById(R.id.text);
        Button answers = findViewById(R.id.viewAnswers);
        Button retry = findViewById(R.id.retry);
        Intent intent = getIntent();

        ArrayList<Button> topicButtons = new ArrayList<>();
        for(int i = 0; i < allTopics.size(); i++){
            int resID = getResources().getIdentifier(allTopics.get(i),
                    "id", getPackageName());
            topicButtons.add((Button)findViewById(resID));
        }

        if(intent.getParcelableArrayListExtra(DisplayQuestions.EXTRA_MESSAGE)==null){
            score.setText("AP Computer Science Quiz");
            answers.setVisibility(View.GONE);
            retry.setVisibility(View.GONE);
        } else{
            questionsList = intent.getParcelableArrayListExtra(DisplayQuestions.EXTRA_MESSAGE);

            // determines how many questions user got correct
            int count = 0;
            for(int i = 0; i < questionsList.size(); i++){
                Log.d("correctA", Integer.toString(questionsList.get(i).getCorrectIndex()));
                if(questionsList.get(i).getSelectedIndex()==questionsList.get(i).getCorrectIndex()){
                    count+=1;
                }
            }

            score.setText("Score: " + count + "/" + questionsList.size());
            answers.setVisibility(View.VISIBLE);
            retry.setVisibility(View.VISIBLE);
        }
    }

    public void start(View v){
        Button button = (Button) v;
        int index = allTopics.indexOf(button.getText().toString());
        Intent intent = new Intent(this, DisplayQuestions.class);
        intent.putExtra(WHICH, Integer.toString(index));
        startActivity(intent);
    }

    public void answers(View v){
        Intent intent = new Intent(this, DisplayQuestions.class);
        intent.putExtra(WHICH, ANSWERS);
        intent.putParcelableArrayListExtra(QUESTIONSLIST, (ArrayList<? extends Parcelable>) questionsList);
        startActivity(intent);
    }

    public void retry(View v){
        Intent intent = new Intent(this, DisplayQuestions.class);
        intent.putExtra(WHICH, RETRY);
        intent.putParcelableArrayListExtra(QUESTIONSLIST, (ArrayList<? extends Parcelable>) questionsList);
        startActivity(intent);
    }

    public static String getWhich(){
        return WHICH;
    }

    public static String getAnswers(){
        return ANSWERS;
    }

    public static String getRetry(){
        return RETRY;
    }
}
