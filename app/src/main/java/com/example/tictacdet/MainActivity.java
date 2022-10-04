package com.example.tictacdet;

//import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

//import android.graphics.drawable.Drawable;
//import android.content.Intent;
//import android.content.res.Configuration;
//import android.graphics.Color;
//import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
//import android.util.Log;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener
{
    private final ImageButton[][] buttons = new ImageButton[3][3];
    private final String[][] strings = new String[3][3];

    private boolean player1Turn = true;

    private int roundCount;

    private int player1Points;
    private int player2Points;

    private TextView textViewPlayer1;
    private TextView textViewPlayer2;

    Drawable tic;
    Drawable tac;
    Drawable empty;

//    private final Drawable startDraw = "@android";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        tic = getResources().getDrawable(R.drawable.tic_tac_tanner);
//        tac = getResources().getDrawable(R.drawable.tic_tac_burton);
//        empty = getResources().getDrawable(R.color.ncm_OrangeDark);
        tic = ResourcesCompat.getDrawable(getResources(), R.drawable.tic_tac_tanner, null);
        tac = ResourcesCompat.getDrawable(getResources(), R.drawable.tic_tac_burton, null);
        empty = ResourcesCompat.getDrawable(getResources(), R.color.ncm_OrangeDark, null);

        textViewPlayer1 = findViewById(R.id.text_view_p1);
        textViewPlayer2 = findViewById(R.id.text_view_p2);

        for (int i = 0; i < 3; i++)
        {
            for (int j = 0; j < 3; j++)
            {
                String buttonID = "button_" + i + j;
                int resID = getResources().getIdentifier(buttonID, "id", getPackageName());
                buttons[i][j] = findViewById(resID);
                buttons[i][j].setOnClickListener(MainActivity.this);
                buttons[i][j].setImageDrawable(empty);
                strings[i][j] = "";
            }
        }
        Button buttonReset = findViewById(R.id.button_reset);
        buttonReset.setOnClickListener(v -> resetGame());

//        switch (getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK)
//        {
//            case Configuration.UI_MODE_NIGHT_YES:
//                textViewPlayer1.setTextColor(Color.WHITE);
//                textViewPlayer2.setTextColor(Color.WHITE);
//                break;
//            case Configuration.UI_MODE_NIGHT_NO:
//                textViewPlayer1.setTextColor(Color.BLACK);
//                textViewPlayer2.setTextColor(Color.BLACK);
//                break;
//        }
    }

    @Override
    public void onClick(View v)
    {
        if(!((ImageButton) v).getDrawable().equals(empty))
        {
            return;
        }

        String eye = ((ImageButton) v).getResources().getResourceEntryName(v.getId());
        char[] eyeRay = eye.toCharArray();
        int i = (int) eyeRay[7] - 48;
        int j = (int) eyeRay[8] - 48;

        if(player1Turn)
        {
            ((ImageButton) v).setImageDrawable(tic);
            strings[i][j] = "X";
//            Toast.makeText(this, i + " & " + j, Toast.LENGTH_SHORT).show();

//            ((Button) v).setText("X");
//            ((Button) v).setCompoundDrawablesWithIntrinsicBounds(null, tic, null, null);
        }
        else
        {
            ((ImageButton) v).setImageDrawable(tac);
            strings[i][j] = "O";
//            ((Button) v).setText("O");
//            ((Button) v).setCompoundDrawablesWithIntrinsicBounds(null, tac, null, null);
        }

        roundCount++;

        if(checkForWin())
        {
            if(player1Turn)
            {
                playerWin(1);
            }
            else
            {
                playerWin(2);
            }
        }
        else if (roundCount == 9)
        {
            draw();
        }
        else
        {
            player1Turn = !player1Turn;
        }
    }

    private boolean checkForWin()
    {

//        Drawable[][] field = new Drawable[3][3];
        String[][] field = new String[3][3];

        for (int i = 0; i < 3; i++)
        {
            //                field[i][j] = buttons[i][j].getDrawable();
            //                field[i][j] = buttons[i][j].getText().toString(); //original
            System.arraycopy(strings[i], 0, field[i], 0, 3);
//            System.arraycopy(strings[i], 0, field[i], 0, 3);
        }

        for (int i = 0; i < 3; i++)
        {
            if(field[i][0].equals(field[i][1]) && field[i][0].equals(field[i][2]) && !field[i][0].equals(""))
            {
                return true;
            }
        }

        for (int i = 0; i < 3; i++)
        {
            if(field[0][i].equals(field[1][i]) && field[0][i].equals(field[2][i]) && !field[0][i].equals(""))
            {
                return true;
            }
        }

        if(field[0][0].equals(field[1][1]) && field[0][0].equals(field[2][2]) && !field[0][0].equals(""))
        {
            return true;
        }

//        if(field[0][2].equals(field[1][1]) && field[0][2].equals(field[2][0]) && !field[0][2].equals(""))
//        {
//            return true;
//        }
        return field[0][2].equals(field[1][1]) && field[0][2].equals(field[2][0]) && !field[0][2].equals("");
    }

    private void playerWin(int playerNum)
    {
        Handler mHandler;
        Runnable mRunnable;

        MediaPlayer winSound = MediaPlayer.create(this, R.raw.airhorn);
        winSound.start();

        switch(playerNum)
        {
            case 1:
                player1Points++;
                Toast.makeText(this, "Player 1 wins!", Toast.LENGTH_SHORT).show();
                updatePointsText();

                mHandler = new Handler(Looper.getMainLooper());
                mRunnable = this::resetBoard;
                mHandler.postDelayed(mRunnable,3000);
//                resetBoard();
                break;
            case 2:
                player2Points++;
                Toast.makeText(this, "Player 2 wins!", Toast.LENGTH_SHORT).show();
                updatePointsText();

                mHandler = new Handler(Looper.getMainLooper());
                mRunnable = this::resetBoard;
                mHandler.postDelayed(mRunnable,3000);
                break;
        }

        if(!winSound.isPlaying())
            winSound.release();
    }

    private void draw()
    {
        Handler mHandler;
        Runnable mRunnable;

        MediaPlayer drawSound = MediaPlayer.create(this, R.raw.error);
        drawSound.start();

        Toast.makeText(this, "Draw!", Toast.LENGTH_SHORT).show();

        mHandler = new Handler(Looper.getMainLooper());
        mRunnable = this::resetBoard;
        mHandler.postDelayed(mRunnable,3000);

        if(!drawSound.isPlaying())
            drawSound.release();
    }

    private void updatePointsText()
    {
        String p1text = "Player 1: " + player1Points;
        String p2text = "Player 2: " + player2Points;

        textViewPlayer1.setText(p1text);
        textViewPlayer2.setText(p2text);
    }

    private void resetBoard()
    {
        for(int i = 0; i < 3; i++)
        {
            for(int j = 0; j < 3; j++)
            {
                buttons[i][j].setImageDrawable(empty);
//                buttons[i][j].setText("");
//                buttons[i][j].setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                strings[i][j] = "";
            }
        }

        roundCount = 0;
        player1Turn = true;
    }

    private void resetGame()
    {
        player1Points = 0;
        player2Points = 0;
        updatePointsText();
        resetBoard();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState)
    {
        super.onSaveInstanceState(outState);

        outState.putInt("roundCount", roundCount);
        outState.putInt("player1Points", player1Points);
        outState.putInt("player2Points", player2Points);
        outState.putBoolean("player1Turn", player1Turn);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState)
    {
        super.onRestoreInstanceState(savedInstanceState);

        roundCount = savedInstanceState.getInt("roundCount");
        player1Points = savedInstanceState.getInt("player1Points");
        player2Points = savedInstanceState.getInt("player2Points");
        player1Turn = savedInstanceState.getBoolean("player1Turn");
    }
}