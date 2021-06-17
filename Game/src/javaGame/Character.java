package javaGame;

import java.io.Serializable;

/**
 * Die Klasse Character ist die Vaterklasse fuer Player und Monster, wie auch bei weiteren Klassen.
 *
 * @author Lukas Schulz
 *
 * @version 1.0
 */
public class Character implements Serializable {
    /**
     * The constant ATTACK_NORMAL.
     */
    public static final int ATTACK_NORMAL = 0;
    /**
     * The constant ATTACK_SPECIAL.
     */
    public static final int ATTACK_SPECIAL = 1;
    /**
     * The Max hp.
     */
    private int maxHp;
    /**
     * The Hp.
     */
    private int hp;
    /**
     * The Atk.
     */
    private int atk;
    /**
     * The Hit chance.
     */
    private double hitChance;
    /**
     * The Inventar.
     */
    private List<Item> inventory;
    /**
     * The Gold.
     */
    private double gold;
    /**
     * Faktor mit dem eingekauft wird.
     */
    private double buyFactor;

    /**
     * Instantiates a new Character.
     *
     * @param maxHp     the max hp
     * @param atk       the atk
     * @param hitChance the hit chance
     * @param gold      the gold
     * @param buyFactor Faktor mit dem Bezhalt wird
     */
    public Character(int maxHp, int atk, double hitChance, double gold, double buyFactor) {
        this.maxHp = maxHp;
        this.hp = maxHp;
        this.atk = atk;
        this.hitChance = hitChance;
        inventory = new AVLTree<>();
        this.gold = gold;
        this.buyFactor = buyFactor;
    }

    /**
     * Gets hit chance.
     *
     * @return the hit chance
     */
    public double getHitChance() {
        return hitChance;
    }

    /**
     * Sets hit chance.
     *
     * @param hitChance the hit chance
     */
    public void setHitChance(double hitChance) {
        if (hitChance >= 0 && hitChance <= 1) {
            this.hitChance = hitChance;
        }
    }

    /**
     * Gets hp.
     *
     * @return the hp
     */
    public int getHp() {
        return hp;
    }

    /**
     * Sets hp.
     *
     * @param hp the hp
     */
    public void setHp(int hp) {
        if (hp > maxHp) {
            this.hp = maxHp;
        } else if (hp < 0) {
            this.hp = 0;
        } else {
            this.hp = hp;
        }
    }

    /**
     * Gets max hp.
     *
     * @return the max hp
     */
    public int getMaxHp() {
        return maxHp;
    }

    /**
     * Gets atk.
     *
     * @return the atk
     */
    public int getAtk() {
        return atk;
    }

    /**
     * Sets atk.
     *
     * @param atk the atk
     */
    public void setAtk(int atk) {
        this.atk = atk;
    }

    /**
     * Take damage.
     *
     * @param damage the damage
     *
     * @return the int
     */
    public int takeDamage(int damage) {
        return takeDamage(damage, ATTACK_NORMAL);
    }

    /**
     * Take damage.
     *
     * @param damage     the damage
     * @param attackType the attack type
     *
     * @return the damage
     */
    public int takeDamage(int damage, int attackType) {
        setHp(getHp() - damage);
        return damage;
    }

    /**
     * Is defeated.
     *
     * @return true, wenn man besiegt ist
     */
    public boolean isDefeated() {
        return getHp() == 0;
    }

    /**
     * Attack int.
     *
     * @param c the enemy
     *
     * @return -1, fuer Verfehlt, sonst den angerichteten Schaden
     */
    public int attack(Character c) {
        if (Math.random() <= hitChance) {
            int damage = (int) (atk * (Math.random() + 1.0));
            return c.takeDamage(damage);
        } else {
            return -1;
        }
    }

    /**
     * Loot void.
     *
     * @param corpse the corpse
     */
    public void loot(Character corpse) {
        gold += corpse.gold;
        corpse.gold = 0;

        while (!corpse.inventory.isEmpty()) {
            inventory.insert(corpse.inventory.firstItem());
            corpse.inventory.delete();
        }
    }

    /**
     * Gets inventory.
     *
     * @return the inventory
     */
    public List<Item> getInventory() {
        return inventory;
    }

    /**
     * Gets gold.
     *
     * @return the gold
     */
    public double getGold() {
        return gold;
    }

    /**
     * Fill inventory.
     */
    public void fillInventory() {
        int k = (int) (10 * Math.random());
        for (int i = 0; i < k; i++) {
            inventory.insert(new Item());
        }
    }

    /**
     * Kauf ein Item aus dem Inventar des Haendlers und zahlt Ihm das Gold
     *
     * @param trader the trader
     * @param item   the item
     */
    public void purchase(Character trader, Item item) {
        if (!trader.inventory.isInList(item)) {
            throw new IllegalStateException("Dieses Item steht nicht zum Verkauf");
        }
        if (gold < item.getValue() * buyFactor) {
            throw new IllegalStateException("Ihnen fehlt das noetige Gold");
        }
        gold -= item.getValue() * buyFactor;
        trader.gold += item.getValue() * buyFactor;
        inventory.insert(item);
        trader.inventory.delete(item);
    }

}
