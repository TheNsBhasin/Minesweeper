package in.codingninjas.minesweeper.util;

import java.util.Random;

/**
 * Created by nsbhasin on 18/06/17.
 */

public class Generator {

    public static int[][] generate(int bombNumber, final int height, final int width) {

        int xStep[] = {-1,-1,0,1,1,1,0,-1};
        int yStep[] = {0,1,1,1,0,-1,-1,-1};

        Random r = new Random();

        int [][] grid = new int[height][width];
        for (int x = 0; x < height; x++) {
            grid[x] = new int[width];
        }

        while (bombNumber > 0) {
            int x = r.nextInt(height);
            int y = r.nextInt(width);

            if (grid[x][y]!=-1) {
                grid[x][y]=-1;
                bombNumber--;
                for (int i = 0; i < 8 ; i++) {
                    int X = x + xStep[i];
                    int Y = y + yStep[i];
                    if((X >= 0 && X < height) && (Y >= 0 && Y < width) && grid[X][Y]!=-1)
                        grid[X][Y]++;
                }
            }
        }
        return grid;
    }
}
