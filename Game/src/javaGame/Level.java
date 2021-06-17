package javaGame;

import java.util.Scanner;

/**
 * Die Klasse Level.
 *
 * @author Lukas Schulz
 *
 * @version 1.0
 */
public class Level {
    /**
     * The constant PLAYER_CHAR.
     */
    public static final char PLAYER_CHAR = 'P';
    /**
     * The constant ATKBONUS.
     */
    private static final int ATKBONUS = 10;
    /**
     * The Map data.
     */
    private char[][] mapData;
    /**
     * The Player x coordinate.
     */
    private int playerX;
    /**
     * The Player y coordinate.
     */
    private int playerY;

    /**
     * Liste aller Quests
     */
    private List<Quest> questList;

    /**
     * Instantiates a new Level.
     *
     * @param mapData the map data
     */
    public Level(char[][] mapData) {
        if (mapData.length < 3 || mapData[0].length < 3) {
            throw new IllegalArgumentException("Invalid Map Data");
        }
        this.mapData = mapData;
        if (!findStart()) {
            throw new IllegalArgumentException("Invalid Map Data: No starting position");
        }

        questList = new AVLTree<>();

        for (int i = 0; i < Quest.getDatabase().length(); i++) {
            questList.insert(new Quest(Quest.getDatabase().getItem(i)));
        }
    }

    /**
     * Random monster.
     *
     * @return the monster
     */
    private static Monster randomMonster() {
        Monster[] monsterFarm = {
            new Monster(),
            new ResistantMonster(),
            new WaitingMonster()
        };

        double bucketSize = 1.0 / monsterFarm.length;
        double bucket = Math.random() / bucketSize;
        int selectedMonster = (int) Math.floor(bucket);
        return monsterFarm[selectedMonster];
    }

    /**
     * Find start.
     *
     * @return true, wenn die Startposition gefunden wuerde
     */
    private boolean findStart() {
        for (int y = 0; y < mapData.length; y++) {
            for (int x = 0; x < mapData[0].length; x++) {
                if (mapData[y][x] == MazeGenerator.STARTCHAR) {
                    playerX = x;
                    playerY = y;
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * To string.
     *
     * @return the string
     */
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int y = 0; y < mapData.length; ++y) {
            for (int x = 0; x < mapData[0].length; ++x) {
                if (y == playerY && x == playerX) {
                    sb.append(PLAYER_CHAR);
                } else {
                    sb.append(mapData[y][x]);
                }
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    /**
     * Can move.
     *
     * @param c the direction
     *
     * @return true, wenn die Richtung moeglich ist
     */
    public boolean canMove(char c) {
        switch (c) {
            case 'n':
                return canMoveUp();
            case 's':
                return canMoveDown();
            case 'o':
                return canMoveRight();
            case 'w':
                return canMoveLeft();
            default:
                return false;
        }
    }

    /**
     * Move void.
     *
     * @param c the direction
     */
    public void move(char c) {
        switch (c) {
            case 'n':
                moveUp();
                break;
            case 's':
                moveDown();
                break;
            case 'o':
                moveRight();
                break;
            case 'w':
                moveLeft();
                break;
        }
    }

    /**
     * Is walkable position.
     *
     * @param x the x coordinate
     * @param y the y coordinate
     *
     * @return true, wenn das Feld x,y begehbar ist
     */
    public boolean isWalkablePosition(int x, int y) {
        return (y >= 0) && (x >= 0) && (y < mapData.length) && (x < mapData[0].length)
            && (mapData[y][x] != MazeGenerator.WALLCHAR);
    }

    /**
     * Can move up.
     *
     * @return true, wenn moegliche Bewegung
     */
    public boolean canMoveUp() {
        return isWalkablePosition(playerX, playerY - 1);
    }

    /**
     * Can move down.
     *
     * @return true, wenn moegliche Bewegung
     */
    public boolean canMoveDown() {
        return isWalkablePosition(playerX, playerY + 1);
    }

    /**
     * Can move left.
     *
     * @return true, wenn moegliche Bewegung
     */
    public boolean canMoveLeft() {
        return isWalkablePosition(playerX - 1, playerY);
    }

    /**
     * Can move right.
     *
     * @return true, wenn moegliche Bewegung
     */
    public boolean canMoveRight() {
        return isWalkablePosition(playerX + 1, playerY);
    }

    /**
     * Move up.
     */
    public void moveUp() {
        if (canMoveUp()) {
            playerY--;
        }
    }

    /**
     * Move down.
     */
    public void moveDown() {
        if (canMoveDown()) {
            playerY++;
        }
    }

    /**
     * Move left.
     */
    public void moveLeft() {
        if (canMoveLeft()) {
            playerX--;
        }
    }

    /**
     * Move right.
     */
    public void moveRight() {
        if (canMoveRight()) {
            playerX++;
        }
    }

    /**
     * Show prompt.
     */
    public void showPrompt() {
        System.out.println("------------------------------");
        if (canMoveUp()) {
            System.out.println("n -> Norden");
        }
        if (canMoveDown()) {
            System.out.println("s -> Sueden");
        }
        if (canMoveRight()) {
            System.out.println("o -> Osten");
        }
        if (canMoveLeft()) {
            System.out.println("w -> Westen");
        }
        System.out.println("i -> zeige das Inventar");
        System.out.println("l -> zeige das Questlog");
        System.out.println("k -> zeige abgegebene Quests");
        System.out.println("q -> Speichern");
        System.out.println("------------------------------");
        System.out.print("Befehl? ");
    }

    /**
     * Gets field.
     *
     * @return the field
     */
    private char getField() {
        return mapData[playerY][playerX];
    }

    /**
     * Clear field.
     */
    private void clearField() {
        char field = getField();
        if (field == MazeGenerator.SMITHYCHAR) {
            mapData[playerY][playerX] = MazeGenerator.FREECHAR;
        }
    }

    /**
     * Handle current field event.
     *
     * @param p the player
     */
    public void handleCurrentFieldEvent(Player p) {
        char field = getField();
        switch (field) {
            case MazeGenerator.SMITHYCHAR:
                p.setAtk(p.getAtk() + ATKBONUS);
                System.out.printf("Die ATK des Spielers wurde um %d erhoeht.%n", ATKBONUS);
                break;
            case MazeGenerator.WELLCHAR:
                p.setHp(p.getMaxHp());
                System.out.println("Spieler wurde vollstaendig geheilt!");
                break;
            case MazeGenerator.BATTLECHAR:
                startBattle(p);
                break;
            case MazeGenerator.GOALCHAR:
                if (isLevelCompleat(p)) {
                    System.out.println("Herzlichen Glueckwunsch! Sie haben gewonnen!");
                    System.exit(0);
                } else {
                    System.out.println("Sie haben noch nicht alle Quests abgeschlossen");
                }
                break;
            case MazeGenerator.TRADECHAR:
                System.out.println("Ihr habt einen Haendler getroffen.");
                startTrade(new Trader(), p);
                break;
            case MazeGenerator.QUESTCHAR:
                System.out.println("Ihr habt einen Questgeber getroffen.");
                questMenu(p);
                break;
        }
        clearField();
    }

    /**
     * Testet ob alle Quetes abgeschlossen sind
     *
     * @param p der Spieler
     *
     * @return true, wenn alle Quests abgeschlossen sind
     */
    public boolean isLevelCompleat(Player p) {
        for (int i = 0; i < questList.length(); i++) {
            if (!p.getCompletedQuests().isInList(questList.getItem(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * Menu zum annehmen und Abgeben von Quests
     *
     * @param p der Spieler
     */
    private void questMenu(Player p) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("Waehlen Sie \"q\" zum Verlassen, \"a\" zum Annehmen und \"d\" zum Abgeben von Quests.");
            switch (scanner.nextLine()) {
                case "a":
                    System.out.println("Folgende Quests sind verfuegbar:");
                    List<Quest> available = new AVLTree<>();

                    for (int i = 0; i < questList.length(); i++) {
                        Quest quest = questList.getItem(i);
                        if (quest.isAvailable(p)) {
                            available.insert(quest);
                        }
                    }

                    System.out.println(available);

                    System.out.println();
                    System.out.println("Waehlen Sie die Quest zum Annehmen aus (Position)");
                    try {
                        p.getQuestLog().insert(available.getItem(Integer.parseInt(scanner.nextLine())));
                    } catch (IndexOutOfBoundsException | NumberFormatException e) {
                        System.out.printf("Fehlerhafe Auswahl");
                    }
                    break;
                case "d":
                    System.out.println("Folgende Quests sind abgebbar:");
                    List<Quest> deliverable = new AVLTree<>();

                    for (int i = 0; i < p.getQuestLog().length(); i++) {
                        Quest quest = p.getQuestLog().getItem(i);
                        if (quest.isCompletable(p)) {
                            deliverable.insert(quest);
                        }
                    }

                    System.out.println(deliverable);

                    System.out.println();
                    System.out.println("Waehlen Sie die Quest zum Abgeben aus (Position)");
                    try {
                        deliverable.getItem(Integer.parseInt(scanner.nextLine())).completQuest(p);
                    } catch (IndexOutOfBoundsException | NumberFormatException e) {
                        System.out.println("Fehlerhafe Auswahl");
                    }
                    break;
                case "q":
                    return;
                default:
                    System.out.println("Fehlerhafte Eingabe");
            }
        }
    }

    /**
     * Start Trading
     *
     * @param trader der Haendler
     * @param player der Spieler
     */
    private void startTrade(Trader trader, Player player) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("Waehlen Sie \"q\" zum Verlassen, \"e\" zum Einkaufen und \"v\" zum Verkaufen.");
            switch (scanner.nextLine()) {
                case "e":
                    System.out.printf("Euer Gold: %.2f%n", player.getGold());
                    System.out.println("Angebot:");
                    System.out.println(trader.getInventory());
                    System.out.println();
                    System.out.println("Waehlen Sie das Item zum Kaufen (Position)");
                    try {
                        Item item = trader.getInventory().getItem(Integer.parseInt(scanner.nextLine()));
                        try {
                            player.purchase(trader, item);
                        } catch (IllegalStateException e) {
                            System.out.println(e.getMessage());
                        }
                    } catch (IndexOutOfBoundsException | NumberFormatException e) {
                        System.out.println("Fehlerhafe Auswahl");
                    }
                    break;
                case "v":
                    System.out.printf("Gold des Haendlers: %.2f%n", player.getGold());
                    System.out.println("Euer Inventar:");
                    System.out.println(player.getInventory());
                    System.out.println();
                    System.out.println("Waehlen Sie das Item zum Verkaufen (Position)");
                    try {
                        Item item2 = player.getInventory().getItem(Integer.parseInt(scanner.nextLine()));
                        try {
                            trader.purchase(player, item2);
                        } catch (IllegalStateException e) {
                            System.out.println(e.getMessage());
                        }
                    } catch (IndexOutOfBoundsException | NumberFormatException e) {
                        System.out.println("Fehlerhafe Auswahl");
                    }
                    break;
                case "q":
                    return;
                default:
                    System.out.println("Fehlerhafte Eingabe");
            }
        }
    }


    /**
     * Start battle.
     *
     * @param p the p
     */
    public void startBattle(Player p) {
        Monster m = randomMonster();

        BattleGUI gui = new BattleGUI(p, m);

        gui.startBattle();


        
    }

}
