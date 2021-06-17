package javaGame;

/**
 * Die Klasse Player
 *
 * @author Lukas Schulz
 *
 * @version 1.0
 */
public class Player extends Character {
    /**
     * The constant HARD_HIT_COST.
     */
    public static final int HARD_HIT_COST = 20;
    /**
     * The constant HARD_HIT_SELF_DAMAGE_PERCENT.
     */
    public static final int HARD_HIT_SELF_DAMAGE_PERCENT = 20;
    /**
     * The constant FIREBALL_COST.
     */
    public static final int FIREBALL_COST = 20;
    /**
     * The constant REROLL_COST.
     */
    public static final int REROLL_COST = 35;
    /**
     * The Max ap.
     */
    private int maxAp;
    /**
     * The Ap regen.
     */
    private int apRegen;
    /**
     * The Ap.
     */
    private int ap;
    /**
     * The Healing power.
     */
    private int healingPower;
    /**
     * The Remaining item uses.
     */
    private int remainingItemUses;
    /**
     * Das Questlog des Spielers
     */
    private List<Quest> questLog;
    /**
     * Lister aller abgeschlossenen Quests
     */
    private List<Quest> completedQuests;

    /**
     * Instantiates a new Player.
     */
    public Player() {
        this(130, 20, 70, 3, 0.8);
    }

    /**
     * Instantiates a new Player.
     *
     * @param maxHp             the max hp
     * @param atk               the atk
     * @param healingPower      the healing power
     * @param remainingItemUses the remaining item uses
     * @param hitChance         the hit chance
     */
    public Player(int maxHp, int atk, int healingPower, int remainingItemUses, double hitChance) {
        this(maxHp, atk, healingPower, remainingItemUses, hitChance, 70, 10);
    }

    /**
     * Instantiates a new Player.
     *
     * @param maxHp             the max hp
     * @param atk               the atk
     * @param healingPower      the healing power
     * @param remainingItemUses the remaining item uses
     * @param hitChance         the hit chance
     * @param maxAp             the max ap
     * @param apRegen           the ap regen
     */
    public Player(int maxHp, int atk, int healingPower, int remainingItemUses,
                  double hitChance, int maxAp, int apRegen) {
        super(maxHp, atk, hitChance, 0, 1);
        this.healingPower = healingPower;
        this.remainingItemUses = remainingItemUses;
        this.maxAp = maxAp;
        this.ap = maxAp;
        this.apRegen = apRegen;
        questLog = new AVLTree<>();
        completedQuests = new AVLTree<>();

    }

    /**
     * Get Ap
     * @return the ap
     */
    public int getAp() {
        return ap;
    }

    /**
     * Gets remaining item uses.
     *
     * @return the remaining item uses
     */
    public int getRemainingItemUses() {
        return remainingItemUses;
    }

    /**
     * Heal boolean.
     *
     * @return true, wenn die Heilung erfolgreich war
     */
    public boolean heal() {
        if (remainingItemUses >= 1) {
            setHp(getHp() + healingPower);
            remainingItemUses--;
            return true;
        } else {
            return false;
        }
    }

    /**
     * Regenerate ap.
     *
     * @return die regenerierten AP
     */
    public int regenerateAp() {
        int oldAp = this.ap;
        this.ap = Math.min(this.ap + this.apRegen, this.maxAp);
        return this.ap - oldAp;
    }

    /**
     * To string.
     *
     * @return the string
     */
    public String toString() {
        return String.format("Spieler -- HP %d -- ATK %d -- AP %d", getHp(), getAtk(), ap);
    }

    /**
     * Use ap.
     *
     * @param cost the cost
     *
     * @return true, wenn die AP erfolgreich verbraucht wurden
     */
    private boolean useAp(int cost) {
        if (cost > this.ap) {
            return false;
        } else {
            this.ap -= cost;
            return true;
        }
    }

    /**
     * get HealingPower
     * @return healingPower
     */
    public int getHealingPower() {
        return healingPower;
    }

    /**
     * Hard hit.
     *
     * @param m the enemy
     *

     * @return the damage
     */
    public int hardHit(Character m) {
        if (!useAp(HARD_HIT_COST)) {
            return -1;
        } else {
            // 2-facher bis 4-facher Schaden
            int damage = (int) (getAtk() * (Math.random() * 2.0 + 2.0));
            damage = m.takeDamage(damage, ATTACK_SPECIAL);
            this.takeDamage((int) (HARD_HIT_SELF_DAMAGE_PERCENT / 100.0 * damage), ATTACK_SPECIAL);
            return damage;
        }
    }

    /**
     * Fireball int.
     *
     * @param m the enemy
     *
     * @return the damage
     */
    public int fireball(Character m) {
        if (!useAp(FIREBALL_COST)) {
            return -1;
        } else {
            // 1.5-facher Schaden
            int damage = (int) (getAtk() * 1.5);
            damage = m.takeDamage(damage, ATTACK_SPECIAL);
            return damage;
        }
    }

    /**
     * Reroll boolean.
     *
     * @return true, wenn erfolgreich
     */
    public boolean reroll() {
        if (!useAp(REROLL_COST)) {
            return false;
        } else {
            // ATK * Zufallsfaktor zwischen 0 und 2
            setAtk((int) (Math.random() * 2 * this.getAtk()));
            return true;
        }
    }

    /**
     * Gets completed quests.
     *
     * @return the completed quests
     */
    public List<Quest> getCompletedQuests() {
        return completedQuests;
    }

    /**
     * Gets quest log.
     *
     * @return the quest log
     */
    public List<Quest> getQuestLog() {
        return questLog;
    }

}
