public class Battler{

    String name;
    private int attack;
    private int defense;
    private int speed;
    private double hp, currentHp;
    private double atkMod, defMod, spdMod;
    private boolean aliveState; //true = alive
    private boolean hasAttacked;


    void setBattlerType(BattlerType type){
        switch (type) {
            //dont use this guy
            case Dylan -> {
                attack = 1;
                defense = 1;
                speed = 1;
                hp = 10;
                currentHp = hp;
                name = "Dylan";
            }
            //fast attacker
            case Michael -> {
                attack = 6;
                defense = 2;
                speed = 6;
                hp = 30;
                currentHp = hp;
                name = "Michael";
            }
            //tank
            case Retard -> {
                attack = 2;
                defense = 6;
                speed = 2;
                hp = 70;
                currentHp = hp;
                name = "Retard";
            }
        }
        currentHp = hp;
    }
    //creates a battler
    public Battler(){
        currentHp = hp;
    }

    //Creates a battler and sets it's stats
    public Battler(String name, int attack, int def, int speed, double hp){
        this.name = name;
        this.attack = attack;
        this.defense = def;
        this.speed = speed;
        this.hp = hp;
        currentHp = hp;
    }
    void resetAttack(){
        hasAttacked = false;
    }

    //attacks the enemy
    void attack(Battler victim){
        if (hasAttacked){
            System.out.println(name + "is too tired to do anything!");
            return;
        }
        double damageAmount = Math.ceil((calcAtk() + calcAtk())/victim.calcDef());
        victim.setCurrentHp(victim.getCurrentHp() - damageAmount); //obviously need to create a better damage algorithm

        System.out.println(name + " damaged " + victim.getName() + " by " + damageAmount + " damage!");
        System.out.println();

        //if opponents hp is 0, end game
        if (victim.getCurrentHp() <= 0){
            BattleEngine.endGame(this, victim);
        }

        hasAttacked = true;
    }


    //heals hp scaling on total hp
    void heal(){
        double healAmount = 0.125 * hp;
        currentHp += healAmount;
        //if current hp is over hp, full health
        if (currentHp > hp){
            currentHp = hp;
            healAmount = 0;
        }
        System.out.println(name + " was healed by " + healAmount + "hp");
        System.out.println();
    }

    //status move
    void defend(){
        defMod = 2;
        System.out.println(name + " reinforces his defenses");
        System.out.println();
    }

   void getInfo(){
        System.out.println("Battler name: " + name);
        System.out.println("Your current hp: " + currentHp + " / " + hp);
        System.out.println("Your stats: ");
        System.out.println("Attack: " + calcAtk());
        System.out.println("Defense: " + calcDef());
        System.out.println("Speed: " + calcSpd());
        System.out.println();

    }



    String getName(){
        return name;
    }
    //calculates attack given modifiers
    double calcAtk(){
        return attack * (atkMod + 1);
    }
    double calcDef(){
        return defense * (defMod + 1);
    }
    double calcSpd(){
        return speed * (spdMod + 1);
    }

    //what do you think these do dumbass
    int getBaseAttack(){
        return attack;
    }
    int getBaseDefense(){
        return defense;
    }
    int getBaseSpeed(){
        return speed;
    }
    void setBaseAttack(int attack){
        this.attack = attack;
    }
    void setBaseDefense(int defense){
        this.defense = defense;
    }
    void setBaseSpeed(int speed){
        this.speed = speed;
    }

    double getAtkMod(){
        return atkMod;
    }
    double getDefMod(){
        return defMod;
    }
    double getSpdMod(){
        return spdMod;
    }
    void setAtkMod(double atkMod){
        this.atkMod = atkMod;
    }
    void setDefMod(int defMod){
        this.defMod = defMod;
    }
    void setSpdMod(int spdMod){
        this.spdMod = spdMod;
    }


    double getModifiedAttack(){
        return atkMod;
    }
    double getModifiedDefense(){
        return defMod;
    }
    double getModifiedSpeed(){
        return spdMod;
    }

    double getBaseHp(){
        return hp;
    }
    double getCurrentHp(){
        return currentHp;
    }
    void setCurrentHp(double HP){
        currentHp = HP;
    }
    void setBaseHP(double HP){
        hp = HP;
    }

    //returns whether or not battler is alive
    boolean isAlive(){
        return aliveState;
    }

    //kills the battler
    void kill(){
        aliveState = false;
    }


}