package javaGame;

/**
 * Die Klasse ResistantMonser mit der Vaterklasse Monster.
 *
 * @author Lukas Schulz
 *
 * @version 1.0
 */
public class ResistantMonster extends Monster {

    /**
     * Instantiates a new Resistant monster.
     */
    public ResistantMonster() {
        this(80, 8, 0.9);
    }

    /**
     * Instantiates a new Resistant monster.
     *
     * @param hp        the hp
     * @param atk       the atk
     * @param hitChance the hit chance
     */
    public ResistantMonster(int hp, int atk, double hitChance) {
        super("Resistenter Gegner", hp, atk, hitChance);
    }

    /**
     * Instantiates a new Resistant monster.
     *
     * @param name      the name
     * @param hp        the hp
     * @param atk       the atk
     * @param hitChance the hit chance
     */
    public ResistantMonster(String name, int hp, int atk, double hitChance) {
        super(name, hp, atk, hitChance);
    }

    /**
     * Take damage.
     *
     * @param damage     the damage
     * @param attackType the attack type
     *
     * @return the damage
     */
    @Override
    public int takeDamage(int damage, int attackType) {
        if (attackType == ATTACK_NORMAL) {
            return super.takeDamage((int) (0.5 * damage), attackType);
        } else if (attackType == ATTACK_SPECIAL) {
            return super.takeDamage(2 * damage, attackType);
        } else {
            return super.takeDamage(damage, attackType);
        }
    }
}
