package com.example.myquizzapplication;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.myquizzapplication.QuizContract.*;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.DoubleBinaryOperator;

public class QuizDbHelper extends SQLiteOpenHelper  {
    private static final String DATABASE_NAME = "MyQuizzApplication.V3.db";
    private static final int DATABASE_VERSION = 1;

    private  SQLiteDatabase db;

    public QuizDbHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        this.db = db;

        final String SQL_CREATE_QUESTIONS_TABLE = "CREATE TABLE " +
                QuestionsTable.TABLE_NAME + " ( " +
                QuestionsTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                QuestionsTable.COLUMN_QUESTION + " TEXT, " +
                QuestionsTable.COLUMN_OPTION1 + " TEXT, " +
                QuestionsTable.COLUMN_OPTION2 + " TEXT, " +
                QuestionsTable.COLUMN_OPTION3 + " TEXT, " +
                QuestionsTable.COLUMN_ANSWER_NR + " INTEGER " +
                " ) ";
        db.execSQL(SQL_CREATE_QUESTIONS_TABLE);
        fillQuestionsTable();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + QuestionsTable.TABLE_NAME);
        onCreate(db);
    }

    private void fillQuestionsTable() {
        Question q1 = new Question("¿Cuál es el nombre del río más largo del mundo?", "Río Nilo", "Río Amazonas", "Río Danubio", 2);
        addQuestion(q1);
        Question q2 = new Question("¿En qué continente se encuentra Surinam?", "África", "América del Sur", "Oceanía", 2);
        addQuestion(q2);
        Question q3 = new Question("¿Cuál es la nación más pequeña del mundo?", "Andorra", "Mónaco", "El Vaticano", 3);
        addQuestion(q3);
        Question q4 = new Question("¿Cuál es la única ciudad que está en dos continentes distintos?", "Estambul", "Moscú", "Berlín", 1);
        addQuestion(q4);
        Question q5 = new Question("¿Quién escribió La Odisea?", "Homero", "Virgilio", "Cervantes", 1);
        addQuestion(q5);
        Question q6 = new Question("¿A cuánto equivale el numero Pi?", "3.1614", "3.1416", "3.4161", 2);
        addQuestion(q6);
    }

    private void addQuestion (Question question){
        ContentValues cv = new ContentValues();
        cv.put(QuestionsTable.COLUMN_QUESTION, question.getQuestion());
        cv.put(QuestionsTable.COLUMN_OPTION1, question.getOption1());
        cv.put(QuestionsTable.COLUMN_OPTION2, question.getOption2());
        cv.put(QuestionsTable.COLUMN_OPTION3, question.getOption3());
        cv.put(QuestionsTable.COLUMN_ANSWER_NR, question.getAnswerNr());
        db.insert(QuestionsTable.TABLE_NAME, null, cv);

    }


    @SuppressLint("Range")
    public List<Question> getAllQuestions(){
        List<Question> questionList = new ArrayList<>();
        db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + QuestionsTable.TABLE_NAME, null);

        if(c.moveToFirst()){
            do {
                Question question = new Question();
                question.setQuestion(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_QUESTION)));
                question.setOption1(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_OPTION1)));
                question.setOption2(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_OPTION2)));
                question.setOption3(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_OPTION3)));
                question.setAnswerNr(c.getInt(c.getColumnIndex(QuestionsTable.COLUMN_ANSWER_NR)));
                questionList.add(question);
            }while (c.moveToNext());
        }
        c.close();
        return questionList;
    }
}
