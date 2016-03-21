package com.example.jakub.snake2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by Jakub on 07.12.2015.
 */
public class EnterNameActivity extends Activity implements TextView.OnEditorActionListener
{
    public static final String NAME_ID = "highScoreID";
    public static final int GET_NAME = 1;
    public static final int GET_NAME_DONE = 2;
    public static final int NAME_MAX_LENGTH = 10;

    private EditText m_enterNameField;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.enter_name);
        m_enterNameField = (EditText)findViewById(R.id.enterName);
        m_enterNameField.setOnEditorActionListener(this);
        m_enterNameField.setHint("Enter your name:");
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
    {
        boolean nameEntered = false;

        if (EditorInfo.IME_ACTION_DONE == actionId)
        {
            Intent retrieveNameIntent = new Intent();
            String name = m_enterNameField.getText().toString();
            retrieveNameIntent.putExtra(NAME_ID, name);
            setResult(GET_NAME_DONE, retrieveNameIntent);
            finish();

            nameEntered = true;
        }

        return nameEntered;
    }
}
