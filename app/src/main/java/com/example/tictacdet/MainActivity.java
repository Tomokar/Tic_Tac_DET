package com.example.tictacdet;

//import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

//import android.graphics.drawable.Drawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
//import android.util.Log;
import android.view.View;
import android.widget.Button;
//import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener
{
    private final Button[][] buttons = new Button[3][3];
//    private final String[][] strings = new String[3][3];

    private boolean player1Turn = true;

    private int roundCount;

    private int player1Points;
    private int player2Points;

    private TextView textViewPlayer1;
    private TextView textViewPlayer2;

    Drawable tic;
    Drawable tac;

//    private final Drawable startDraw = "@android";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tic = getResources().getDrawable(R.drawable.tic_tac_tanner);
        tac = getResources().getDrawable(R.drawable.tic_tac_burton);

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
            }
        }
        Button buttonReset = findViewById(R.id.button_reset);
        buttonReset.setOnClickListener(v -> resetGame());
    }

    @Override
    public void onClick(View v)
    {
        if(!((Button) v).getText().toString().equals(""))
        {
            return;
        }

        if(player1Turn)
        {
//            ((ImageButton) v).setImageDrawable(tic);
            ((Button) v).setText("X");
            ((Button) v).setCompoundDrawablesWithIntrinsicBounds(null, tic, null, null);
        }
        else
        {
//            ((ImageButton) v).setImageDrawable(tac);
            ((Button) v).setText("O");
            ((Button) v).setCompoundDrawablesWithIntrinsicBounds(null, tac, null, null);
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
            for (int j = 0; j < 3; j++)
            {
//                field[i][j] = buttons[i][j].getDrawable();
                field[i][j] = buttons[i][j].getText().toString(); //original
//                field[i][j] = strings[i][j];
            }
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
        MediaPlayer winSound = MediaPlayer.create(this, R.raw.airhorn);
        winSound.start();

        switch(playerNum)
        {
            case 1:
                player1Points++;
                Toast.makeText(this, "Player 1 wins!", Toast.LENGTH_SHORT).show();
                updatePointsText();
                resetBoard();
                break;
            case 2:
                player2Points++;
                Toast.makeText(this, "Player 2 wins!", Toast.LENGTH_SHORT).show();
                updatePointsText();
                resetBoard();
                break;
        }

        if(!winSound.isPlaying())
            winSound.release();
    }

    private void draw()
    {
        MediaPlayer drawSound = MediaPlayer.create(this, R.raw.error);
        drawSound.start();

        Toast.makeText(this, "Draw!", Toast.LENGTH_SHORT).show();
        resetBoard();

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
//                buttons[i][j].setImageResource(android.R.color.transparent);
                buttons[i][j].setText("");
                buttons[i][j].setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
//                strings[i][j] = "";
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