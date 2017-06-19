package in.codingninjas.minesweeper;

import android.content.Context;
import android.widget.ImageButton;

/**
 * Created by nsbhasin on 18/06/17.
 */

public class Cell extends android.support.v7.widget.AppCompatImageButton {
    private int value;
    private boolean isBomb;
    private boolean isFlagged;
    private boolean isClicked;
    private boolean isRevealed;

    private int x,y;

    public Cell(Context context) {
        super(context);
        isBomb = false;
        isFlagged = false;
        isClicked = false;
        isRevealed = false;

        setImageResource(R.drawable.button);
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        if (value == -1) {
            setBomb();
        }
        this.value = value;
    }

    public boolean isBomb() {
        return isBomb;
    }

    public void setBomb() {
        isBomb = true;
    }

    public boolean isFlagged() {
        return isFlagged;
    }

    public void setFlagged(boolean flagged) {
        isFlagged = flagged;
    }

    public boolean isClicked() {
        return isClicked;
    }

    public void setClicked() {
        isClicked = true;
        setRevealed();
    }

    public boolean isRevealed() {
        return isRevealed;
    }

    public void setRevealed() {
        isRevealed = true;
    }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getXPos() {
        return x;
    }

    public int getYPos() {
        return y;
    }
}
