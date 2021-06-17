package javaGame;

import java.util.Random;

/**
 * Die Klasse RecursiveBacktracker
 *
 * @author Lukas Schulz
 *
 * @version 1.0
 */
public class RecursiveBacktracker implements MazeGenerator {

    /**
     * The constant HALLWAY_OFFSET.
     */
    private static final int HALLWAY_OFFSET = 2;
    /**
     * The Offsets. (x, y)
     */
    private static int[][] offsets = {
        {-1, 0},
        {1, 0},
        {0, -1},
        {0, 1}
    };
    /**
     * The Random Instance.
     */
    private Random r = new Random();

    /**
     * The Goal set.
     */
    private boolean goalSet = false;

    /**
     * Valid new position.
     *
     * @param maze   the maze
     * @param x      the x
     * @param y      the y
     * @param offset the offset
     *
     * @return the boolean
     */
    private boolean validNewPosition(char[][] maze, int x, int y, int[] offset) {
        int newX = x + HALLWAY_OFFSET * offset[1];
        int newY = y + HALLWAY_OFFSET * offset[0];
        if (newY < 0 || newY >= maze.length || newX < 0 || newX >= maze[0].length) {
            return false;
        }
        return maze[newY][newX] == WALLCHAR;

    }

    /**
     * Init maze.
     *
     * @param height the height
     * @param width  the width
     *
     * @return the map as char[][]
     */
    private char[][] initMaze(int height, int width) {
        assert height % 2 == 0 && width % 2 == 0;
        char[][] map = new char[height][width];
        for (int i = 0; i < map.length; ++i) {
            for (int j = 0; j < map[i].length; ++j) {
                map[i][j] = WALLCHAR;
            }
        }
        return map;
    }

    /**
     * Border char [ ] [ ].
     *
     * @param maze the maze
     *
     * @return the map as char[][]
     */
    private char[][] border(char[][] maze) {
        char[][] borderedMaze = new char[maze.length + 2][maze[0].length + 2];
        for (int i = 0; i < borderedMaze.length; ++i) {
            for (int j = 0; j < borderedMaze[0].length; ++j) {
                if (i == 0 || j == 0 || i == borderedMaze.length - 1 || j == borderedMaze[0].length - 1) {
                    borderedMaze[i][j] = WALLCHAR;
                } else {
                    borderedMaze[i][j] = maze[i - 1][j - 1];
                }
            }
        }
        return borderedMaze;
    }

    /**
     * In maze.
     *
     * @param maze the maze
     * @param x    the x
     * @param y    the y
     *
     * @return true, wenn sich die Koordinatem im Spielfeld befinden
     */
    private boolean inMaze(char[][] maze, int x, int y) {
        return !(y < 0 || y >= maze.length || x < 0 || x >= maze[0].length);
    }

    /**
     * Count visitable neighbors.
     *
     * @param maze the maze
     * @param x    the x
     * @param y    the y
     *
     * @return the neighbors
     */
    private int countVisitableNeighbors(char[][] maze, int x, int y) {
        int n = 0;
        for (int[] offset : offsets) {
            int newX = x + offset[1];
            int newY = y + offset[0];
            if (inMaze(maze, newX, newY) && maze[newY][newX] != WALLCHAR) {
                n++;
            }
        }
        return n;
    }

    /**
     * Place special fields.
     *
     * @param maze the maze
     */
    private void placeSpecialFields(char[][] maze) {
        for (int i = 0; i < maze.length; ++i) {
            for (int j = 0; j < maze[0].length; ++j) {
                if (maze[i][j] == FREECHAR) {
                    int neighbors = countVisitableNeighbors(maze, j, i);
                    if (neighbors >= 3) {
                        maze[i][j] = BATTLECHAR;
                    } else if (neighbors == 1) {
                        if (r.nextDouble() > 0.5) {
                            maze[i][j] = WELLCHAR;
                        } else {
                            maze[i][j] = SMITHYCHAR;
                        }
                    }
                }
            }
        }
    }

    /**
     * Generate char [ ] [ ].
     *
     * @param height the height
     * @param width  the width
     *
     * @return the map as char[][]
     */
    @Override
    public char[][] generate(int height, int width) {
        if (height < 2 || width < 2) {
            throw new IllegalArgumentException("Non-valid maze dimensions");
        }
        goalSet = false;
        char[][] maze = initMaze(height, width);
        int startx = 2 * r.nextInt(width / 2) + 1;
        int starty = 2 * r.nextInt(height / 2) + 1;
        maze = generate(startx, starty, maze);
        maze[starty][startx] = STARTCHAR;
        //maze = border(maze);
        placeSpecialFields(maze);
        placeNPCs(maze);
        return maze;
    }

    /**
     * Build hallway.
     *
     * @param maze   the maze
     * @param curX   the cur x
     * @param curY   the cur y
     * @param offset the offset
     * @param length the length
     */
    private void buildHallway(char[][] maze, int curX, int curY, int[] offset, int length) {

        for (int i = 1; i <= length; ++i) {

            curX += offset[1];
            curY += offset[0];

            if (curY < 0 || curY >= maze.length || curX < 0 || curX >= maze[0].length) {
                return;
            }

            maze[curY][curX] = FREECHAR;
        }
    }

    /**
     * Generate char [ ] [ ].
     *
     * @param curX the cur x
     * @param curY the cur y
     * @param maze the maze
     *
     * @return the map as char [] []
     */
    private char[][] generate(int curX, int curY, char[][] maze) {
        maze[curY][curX] = FREECHAR;
        int[] validPositions = new int[offsets.length];
        int validPositionCount = offsets.length;
        for (int i = 0; i < offsets.length; ++i) {
            validPositions[i] = i;
        }
        boolean deadEnd = true;
        while (validPositionCount != 0) {
            int newPosIndex = r.nextInt(validPositionCount);
            int[] newPosOffset = offsets[validPositions[newPosIndex]];
            int newX = curX + HALLWAY_OFFSET * newPosOffset[1];
            int newY = curY + HALLWAY_OFFSET * newPosOffset[0];

            if (validNewPosition(maze, curX, curY, newPosOffset)) {
                deadEnd = false;
                buildHallway(maze, curX, curY, newPosOffset, HALLWAY_OFFSET);
                generate(newX, newY, maze);
            }

            validPositions = ArrayHelpers.delete(validPositions, newPosIndex);
            validPositionCount--;

        }
        if (!goalSet && deadEnd) {
            goalSet = true;
            maze[curY][curX] = GOALCHAR;
        }
        return maze;
    }

    /**
     * Setzt alle Haendler und Questgeber auf die Map
     *
     * @param maze die Karte
     */
    private void placeNPCs(char[][] maze) {
        int x;
        int y;
        int trader = 0;
        int fields = maze.length * maze[0].length;
        int maxTrader = (int) (2 + fields / 100 * Math.random());
        while (trader < maxTrader) {
            y = (int) (Math.random() * maze.length);
            x = (int) (Math.random() * maze[y].length);

            if (maze[y][x] == FREECHAR) {
                maze[y][x] = TRADECHAR;
                trader++;
            }
        }
        int maxQuestGiver = (int) (1 + fields / 300 * Math.random());
        int questGiver = 0;
        while (questGiver < maxQuestGiver) {
            y = (int) (Math.random() * maze.length);
            x = (int) (Math.random() * maze[y].length);

            if (maze[y][x] == FREECHAR) {
                maze[y][x] = QUESTCHAR;
                questGiver++;
            }
        }
    }
}
