import java.util.Random;

public class Computer extends Battler {
    private Random rand = new Random();
    void selectRandomBattler(){
        setBattlerType(BattlerType.values()[rand.nextInt(3)]);
        name = "CPU - " + name;
    }
    void selectRandomAction(Battler target){
        BattleEngine.BattleManager.doAction(ActionType.values()[rand.nextInt(3)],this,target);
    }
}
