package in.codingninjas.minesweeper.util;

/**
 * Created by nsbhasin on 18/06/17.
 */

public class PrintGrid {

    public static void print(final int[][] grid, final int height, final int width) {

        for(int x = 0; x < height; x++) {
            String printedText = "| ";
            for (int y = 0; y < width; y++) {
                printedText+=String.valueOf(grid[x][y]).replace("-1","B")+" | ";
            }
            System.out.println(printedText);
        }
    }
}
