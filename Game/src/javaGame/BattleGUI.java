package javaGame;

import javax.swing.*;
import javax.swing.text.DefaultCaret;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Die GUI fuer das Kampfsystem
 *
 * @author Lukas Schulz
 *
 * @version 1.0
 */
public class BattleGUI extends JFrame {
    /**
     * Der Spieler
     */
    private final Player p;
    /**
     * Der Gegner
     */
    private final Monster m;

    /**
     * Label fuer den Spieler
     */
    private final JLabel playerLabel;
    /**
     * Label fuer den Spieler
     */
    private final JLabel monsterLabel;
    /**
     * Element auf der "Actionbar" fuer das Item
     */
    private final JButton item;
    /**
     * Element auf der "Actionbar" fuer "Harterschlag"
     */
    private final JButton special1;
    /**
     * Element auf der "Actionbar" fuer "Feuerball"
     */
    private final JButton special2;
    /**
     * Element auf der "Actionbar" fuer "ATK auswuerfeln"
     */
    private final JButton special3;
    /**
     * Das Kampflog
     */
    private final JTextArea log;
    /**
     * Zustand ob der Kampf noch laeuft
     */
    private boolean run = true;
    /**
     * Das erstellte Regeneration der Ap
     */
    private Reg regeneration;
    /**
     * Der erstellte Angriff vom Monster auf den Spieler
     */
    private AngriffMonster am;
    /**
     * Ein erstellter Boolean
     */
    private boolean flag = false; 

    /**
     * Erstellt ein neues Fenester fuer den Kampf aber laesst es noch unsichtbar
     *
     * @param p der Spieler
     * @param m der Gegner
     */
    BattleGUI(Player p, Monster m) {
        super("Battle Window");
        setSize(650, 530);
        this.p = p;
        this.m = m;
        playerLabel = new JLabel();
        playerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        playerLabel.setVerticalAlignment(SwingConstants.TOP);
        playerLabel.setOpaque(true);
        monsterLabel = new JLabel();
        monsterLabel.setHorizontalAlignment(SwingConstants.CENTER);
        monsterLabel.setVerticalAlignment(SwingConstants.TOP);
        monsterLabel.setOpaque(true);
        log = new JTextArea(25, 50);
        log.setEnabled(false);
        DefaultCaret caret = (DefaultCaret) log.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        item = new JButton(String.format("Heiltrank (%d)", p.getRemainingItemUses()));
        special1 = new JButton("Harterschlag");
        special2 = new JButton("Feuerball");
        special3 = new JButton("ATK auswuerfeln");

        setAutoRequestFocus(true);
        setup();
        updateGUI();
        regeneration = new Reg(p);
        am = new AngriffMonster(m);
        regeneration.start();
        am.start();
    }

    /**
     * Uebernimmt weitere Erstellungsaufgaben
     */
    private void setup() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        // top
        JPanel top = new JPanel(new GridLayout(0, 2, 40, 40));
        top.add(playerLabel);
        top.add(monsterLabel);
        add(top, BorderLayout.PAGE_START);

        // center
        JPanel center = new JPanel();
        center.add(new JScrollPane(log), BorderLayout.CENTER);
        add(center, BorderLayout.CENTER);

        // bottom
        JPanel bottom = new JPanel(new GridLayout(0, 5, 0, 5));
        ActionListener listener = new Listener();

        JButton attack = new JButton("Angriff");
        attack.setActionCommand("1");
        attack.setToolTipText(String.format("Angriff mit %d Angriffskraft", p.getAtk()));
        attack.addActionListener(listener);
        bottom.add(attack);

        if (p.getRemainingItemUses() == 0) {
            item.setEnabled(false);
        }
        item.setActionCommand("2");
        item.setToolTipText(String.format("Heiltrank welcher um %d heilt", p.getHealingPower()));
        item.addActionListener(listener);
        bottom.add(item);

        special1.setActionCommand("3");
        special1.setToolTipText(String.format("Harter Schlag (%d AP, %d%% Selbstschaden)", Player.HARD_HIT_COST,
            Player.HARD_HIT_SELF_DAMAGE_PERCENT));
        special1.addActionListener(listener);
        bottom.add(special1);

        special2.setActionCommand("4");
        special2.setToolTipText(String.format("Feuerball (%d AP)", Player.FIREBALL_COST));
        special2.addActionListener(listener);
        bottom.add(special2);

        special3.setActionCommand("5");
        special3.setToolTipText(String.format("ATK auswuerfeln (%d AP)", Player.REROLL_COST));
        special3.addActionListener(listener);
        bottom.add(special3);

        add(bottom, BorderLayout.PAGE_END);
    }

    /**
     * Wandelt einen Text in einen HTML Text mit <br> als Zeilenumbruch um (Labels koennen kein \n verarbeiten)
     *
     * @param text      der Eingabetext
     * @param linebreak die Sequenz des Zeilenumbruchs
     *
     * @return der HTML Text
     */
    private static String text2HTML(String text, String linebreak) {
        return String.format("<html>%s</html>", text.replaceAll(linebreak, "<br>"));
    }

    /**
     * Eine "Kampfrunde" mit den jeweiligen Aktionen Ist ein Refactoring aus der Kampfmethode von Level
     *
     * @param action die Aktion
     */
    private void round(String action) {
        int playerDamage;
        switch (action) {
            case "1":
                playerDamage = p.attack(m);
                if (playerDamage == -1) {
                    log.append("Spieler verfehlt!\n");
                } else {
                    log.append(String.format("Spieler trifft und macht %d Schaden!%n", playerDamage));
                }
                break;
            case "2":
                if (p.heal()) {
                    flag = true;
                    log.append("Spieler heilt sich!\n");
                    item.setText(String.format("Heiltrank (%d)", p.getRemainingItemUses()));
                    if (p.getRemainingItemUses() == 0) {
                        item.setEnabled(false);
                    }
                }
                break;
            case "3":
                playerDamage = p.hardHit(m);
                if (playerDamage != -1) {
                    log.append("Spieler schlaegt hart zu!\n");
                    log.append(String.format("Spieler verursacht %d Schaden!%n", playerDamage));
                    log.append(String.format("Spieler verursacht %d Selbstschaden!%n",
                        (int) (Player.HARD_HIT_SELF_DAMAGE_PERCENT / 100.0 * playerDamage)));
                }
                break;
            case "4":
                playerDamage = p.fireball(m);
                if (playerDamage != -1) {
                    log.append("Spieler schiesst einen Feuerball!\n");
                    log.append(String.format("Spieler verursacht %d Schaden!%n", playerDamage));
                }
                break;
            case "5":
                if (p.reroll()) {
                    log.append("ATK neu ausgewuerfelt!\n");
                }
                break;
        }

        updateGUI();

        if (p.isDefeated()) {
            log.append("Game Over!\n");
            regeneration.stop();
            am.stop();
            run = false;
        } else if (m.isDefeated()) {
            log.append("Spieler gewinnt!\n");
            run = false;
            regeneration.stop();
            am.stop();
            p.loot(m);
            Sync.battleFinished();
            setDefaultCloseOperation(DISPOSE_ON_CLOSE);
            return;
        }

        updateGUI();
    }

    /**
     * Updatet die GUI Elemente und zeigt die neusten Informationen an
     */
    private void updateGUI() {
        if ((p.getHp() / (double) p.getMaxHp()) < .5) {
            playerLabel.setBackground(Color.RED);
        } else {
            playerLabel.setBackground(null);
        }
        if ((m.getHp() / (double) m.getMaxHp()) < .5) {
            monsterLabel.setBackground(Color.GREEN);
        }
        playerLabel.setText(text2HTML(p.toString(), "--"));
        monsterLabel.setText(text2HTML(m.toString(), "--"));

        special1.setEnabled(p.getAp() >= Player.HARD_HIT_COST);
        special2.setEnabled(p.getAp() >= Player.FIREBALL_COST);
        special3.setEnabled(p.getAp() >= Player.REROLL_COST);
    }

    /**
     * Action Listener fuer die "Actionbar"
     */
    class Listener implements ActionListener {

        /**
         * Invoked when an action occurs.
         *
         * @param e the event
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            if (run) {
                round(e.getActionCommand());
            }
        }
    }

    /**
     * Startet den Kampf
     */
    public void startBattle() {
        setVisible(true);
        toFront();
        log.append("                 Kampf Start                    \n");
        Sync.waitForBattleEnd();
    }

    /**
     * Klasse zum Angreifen des Spielers
     */
    class AngriffMonster extends Thread {
    
        /**
         * Erzeugtes Monster
         */
        private final Monster monster;
        
        /**
         * Konstruktor der Klasse AngriffMonster 
         *
         * @param m das Monster
         */
        public AngriffMonster(Monster m) {
            monster = m; 
        }

        /**
         * Wird ausgeloest, wenn die Klasse gestartet wird.
         */
        @Override
        public void run() {
            angriff();
        }
        
        /**
         * Methode um den Angriff auf den Spieler auszufuehren.
         */
        public synchronized void angriff() {
            while (true) {
                try {
                    sleep(3000);
                    if (flag == true) {
                        try {
                            sleep(100);
                            flag = false;
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    log.append("Monster greift an!\n");
                    int monsterDamage = monster.attack(p);
                    if (monsterDamage == -1) {
                        log.append("Monster verfehlt!\n");
                    } else if (monsterDamage == -2) {
                        log.append("Monster tut nichts.\n");
                    } else {
                        log.append(String.format("Monster trifft und macht %d Schaden!%n", monsterDamage));
                    }

                    updateGUI();

                    if (p.isDefeated()) {
                        log.append("Game Over!\n");
                        regeneration.stop();
                        am.stop();
                        run = false;
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } 
    }


   /**
    * Klasse zum aufladen der Ap.
    */
    class Reg extends Thread {
        
        /**
         * Erzeugter Spieler
         */
        private final Player player;

        /**
         * Konstruktor der Klasse Reg
         *
         * @param p der Spieler
         */
        public Reg(Player p) {
            player = p;
        }

        /**
         * Wird ausgeloest, wenn die Klasse gestartet wird
         */
        @Override
        public void run() {
            apReg();
        }
    
        /**
         * Methode fuegt dem Spieler Ap im geregelter Zeit hinzu
         */
        public synchronized void apReg() { 
            while (true) {
                try {
                    sleep(5000);
                    if (flag == true) {
                        try {
                            sleep(100);
                            flag = false;
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    player.regenerateAp();
                
                } catch (InterruptedException e) {
                    System.out.print("l");
                }    
                updateGUI();
            }
        }
    }

    /**
     * Klasse zur Synchronisation zwischen dem Terminal und der GUI
     */
    static class Sync {
        /**
         * Das Lock Element dessen Monitor benutzt wird
         */
        private static final Object LOCK = new Object();

        /**
         * Startet alle am Lock Element wartenen Threads wieder
         */
        public static void battleFinished() {
            synchronized (LOCK) {
                LOCK.notifyAll();
            }
        }

        /**
         * Pausiert den momentan aktiven Thread am Lock Element
         */
        public static void waitForBattleEnd() {
            synchronized (LOCK) {
                try {
                    LOCK.wait();
                } catch (InterruptedException ignored) {
                    ignored.printStackTrace();
                }
            }
        }
    }
}
