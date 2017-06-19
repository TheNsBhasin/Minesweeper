package in.codingninjas.minesweeper;

import android.content.Intent;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import in.codingninjas.minesweeper.util.Generator;
import in.codingninjas.minesweeper.util.PrintGrid;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener {

    public static int WIDTH = 10;
    public static int HEIGHT = 15;
    public static int BOMB_NUMBER = 25;
    public static int bombCount;
    boolean gameOver = false;
    LinearLayout mainLayout;
    LinearLayout rowLayout[];
    Cell[][] MinesweeperGrid;
    ImageButton imageButton;
    TextView bombTextView;
    String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        userName = intent.getStringExtra("user_name");
        Toast.makeText(this, "Name: " + userName,Toast.LENGTH_SHORT);

        mainLayout = (LinearLayout) findViewById(R.id.mainLayout);
        imageButton = (ImageButton) findViewById(R.id.smile);
        bombTextView = (TextView) findViewById(R.id.bombText);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetBoard();
            }
        });
        createGrid();
    }

    private void resetBoard() {
        imageButton.setBackgroundResource(R.drawable.smile);
        createGrid();
    }

    public void createGrid() {
        int[][] generateGrid = Generator.generate(BOMB_NUMBER,HEIGHT,WIDTH);
        PrintGrid.print(generateGrid,HEIGHT,WIDTH);
        bombCount = BOMB_NUMBER;
        bombTextView.setText(String.format("%03d",bombCount));
        setGrid(generateGrid);
    }

    public void setGrid(final int[][] grid) {
        MinesweeperGrid = new Cell[HEIGHT][WIDTH];
        rowLayout = new LinearLayout[HEIGHT];
        mainLayout.removeAllViews();
        gameOver = false;
        for (int i = 0; i < HEIGHT; i++) {
            rowLayout[i] = new LinearLayout(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,0,1f);
            rowLayout[i].setLayoutParams(params);
            rowLayout[i].setOrientation(LinearLayout.HORIZONTAL);
            mainLayout.addView(rowLayout[i]);
        }

        for(int i = 0; i < HEIGHT; i++) {
            for (int j = 0; j < WIDTH; j++) {
                MinesweeperGrid[i][j] = new Cell(this);
                MinesweeperGrid[i][j].setBackground(ContextCompat.getDrawable(this,R.drawable.square));
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0,ViewGroup.LayoutParams.MATCH_PARENT,1f);
                MinesweeperGrid[i][j].setLayoutParams(params);
                MinesweeperGrid[i][j].setOnClickListener(this);
                MinesweeperGrid[i][j].setOnLongClickListener(this);
                MinesweeperGrid[i][j].setValue(grid[i][j]);
                MinesweeperGrid[i][j].setPosition(i,j);
                MinesweeperGrid[i][j].setScaleType(ImageView.ScaleType.CENTER_CROP);
                rowLayout[i].addView(MinesweeperGrid[i][j]);
            }
        }
    }

    @Override
    public void onClick(View view) {
        Cell cell = (Cell) view;
        click(cell.getXPos(), cell.getYPos());
    }

    @Override
    public boolean onLongClick(View view) {
        Cell cell = (Cell) view;
        Log.d("onLongClick",cell.getXPos() + " " + cell.getYPos());
        if(!cell.isClicked())
            flag(cell.getXPos(), cell.getYPos());
        return true;
    }

    public Cell getCellAt(int x, int y) {
        return MinesweeperGrid[x][y];
    }

    public void click(int x, int y) {
        if(gameOver)
            return;
        if(x >= 0 && y >= 0 && x < HEIGHT && y < WIDTH && !getCellAt(x,y).isClicked() && !getCellAt(x,y).isFlagged()) {
            Log.d("onClick","clicked " + x + " " + y);
            getCellAt(x,y).setClicked();
            setCellImage(getCellAt(x,y));
            if (getCellAt(x,y).getValue() == 0) {
                for (int xt = -1; xt <= 1; xt++) {
                    for (int yt = -1; yt <= 1; yt++) {
                        if(xt != yt) {
                            click(x + xt, y + yt);
                        }
                    }
                }
            }
            if (getCellAt(x,y).isBomb()) {
                onGameLost();
                return;
            }
        }
        checkEnd();
    }

    private void setCellImage(Cell cell) {
        if (cell.isFlagged()) {
            drawFlag(cell);
        } else if (cell.isRevealed() && cell.isBomb() && !cell.isClicked()) {
            drawNormalBomb(cell);
        } else {
            if (cell.isClicked()) {
                if (cell.getValue() == -1) {
                    drawExplodedBomb(cell);
                } else {
                    drawNumber(cell);
                }
            } else {
                drawButton(cell);
            }
        }
    }

    private void drawButton(Cell cell) {
        cell.setImageResource(R.drawable.button);
    }
    private void drawFlag(Cell cell) {
        cell.setImageResource(R.drawable.flag);
    }
    private void drawNormalBomb(Cell cell) {
        cell.setImageResource(R.drawable.bomb_normal);
    }
    private void drawExplodedBomb(Cell cell) {
        cell.setImageResource(R.drawable.bomb_exploded);
    }
    private void drawNumber(Cell cell) {

        cell.setClicked();

        switch (cell.getValue()) {
            case 0:
                cell.setImageResource(R.drawable.number_0);
                break;
            case 1:
                cell.setImageResource(R.drawable.number_1);
                break;
            case 2:
                cell.setImageResource(R.drawable.number_2);
                break;
            case 3:
                cell.setImageResource(R.drawable.number_3);
                break;
            case 4:
                cell.setImageResource(R.drawable.number_4);
                break;
            case 5:
                cell.setImageResource(R.drawable.number_5);
                break;
            case 6:
                cell.setImageResource(R.drawable.number_6);
                break;
            case 7:
                cell.setImageResource(R.drawable.number_7);
                break;
            case 8:
                cell.setImageResource(R.drawable.number_8);
                break;
        }
    }

    public void flag(int x, int y) {
        boolean isFlagged = getCellAt(x,y).isFlagged();
        getCellAt(x,y).setFlagged(!isFlagged);
        Log.d("Flag", x + " " + y);
        if (getCellAt(x,y).isFlagged())
            bombCount--;
        else
            bombCount++;
        bombTextView.setText(String.format("%03d",bombCount));
        setCellImage(getCellAt(x,y));
        checkEnd();
    }

    private boolean checkEnd() {
        int bombNotFound = BOMB_NUMBER;
        int notRevealed = WIDTH * HEIGHT;

        for (int x = 0; x < HEIGHT; x++) {
            for (int y = 0; y < WIDTH; y++) {
                if (getCellAt(x,y).isRevealed() || getCellAt(x,y).isFlagged()) {
                    notRevealed--;
                }
                if (getCellAt(x,y).isFlagged() && getCellAt(x,y).isBomb()) {
                    bombNotFound--;
                }
            }
        }

        if (bombNotFound == 0 || notRevealed == BOMB_NUMBER) {
            Toast.makeText(this,"Game Won!",Toast.LENGTH_SHORT).show();
            gameOver = true;
            imageButton.setBackgroundResource(R.drawable.shades);
        }
        return false;
    }

    private void onGameLost() {
        Toast.makeText(this, "Game Lost!", Toast.LENGTH_SHORT).show();
        gameOver = true;
        imageButton.setBackgroundResource(R.drawable.dead);
        for (int x = 0; x < HEIGHT; x++) {
            for (int y = 0; y < WIDTH; y++) {
                getCellAt(x,y).setRevealed();
                setCellImage(getCellAt(x,y));
            }
        }
    }
}
