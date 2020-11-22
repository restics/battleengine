import java.util.Random;
import java.util.Scanner;

/**
 * Battle engine created by pixelized and his lack of dopamine
 * make sure to leave a like, upvote, retweet, comment, subscribe, and rate 5 stars
 *
 * Basically pokemon's battle engine but sucks much worse
 *
 * Present Issues:
 * People inputting random shit will fuck up the scanner
 * more scanner errors
 * Defend doesnt work, maybe add BattlerEffects class with time length attribute to solve this + add expandablity (future effects)
 * make attack choice happen before speed comparison
 *
 * Future possible mechanics:
 * - critical hits
 * - more random chance per hit
 * - elemental damage?
 * - status elements (paralysis, burn, poison, frozen, sleep, (i swear im not copying pokemon)
 * - different attacks
 * - abilities and other attributes
 * - strength stat and weapon power
 * - add a more sophisticated effects system (entire new class)
 * - more sophisticated moves system
 * - accuracy mechanics
 * -
 */

public class BattleEngine {

    static Battler battler1;
    static Computer battler2;
    static BattleManager manager;
    static Input input;
    static GameState gm;

    public BattleEngine(){
        battler1 = new Battler();
        battler2 = new Computer();
        manager = new BattleManager();
        input = new Input();
    }

    //will be removing main method later as I want to use this for an rpg project
    public static void main(String[] args){
        BattleEngine be = new BattleEngine();
        be.start();
    }

    //starts up the battle engine (might add arguments later
    static void start(){
        BattleManager.pickBattler();
        battler2.selectRandomBattler();
        while (gm == GameState.IN_GAME){
            perTurn();
        }
    }



    //runs for each turn
    static void perTurn(){
        System.out.println("+-------------+[NEW TURN]+-------------+");
        //check if battlers in play are still alive every turn
        if (manager.arePlayersAlive()){
            endGame(null,null);
        }
        else{
            //reset all modifiers
            manager.resetMods(battler1);
            manager.resetMods(battler2);

            //compare speeds
            if (battler1.calcSpd() > battler2.calcSpd()){
                manager.promptPlayer(battler1,battler2);
                battler2.selectRandomAction(battler1);
            }
            //speed tie: do a coin flip
            else if (battler1.calcSpd() == battler2.calcSpd()){
                int rand = input.getRandomInt(2);
                if (rand == 1){
                    manager.promptPlayer(battler1,battler2);
                    battler2.selectRandomAction(battler1);
                }
                else{
                    battler2.selectRandomAction(battler1);
                    manager.promptPlayer(battler1,battler2);
                }
            }
            else{
                battler2.selectRandomAction(battler1);
                manager.promptPlayer(battler1,battler2);
            }
        }
    }

    //print out winner, that should be it really
    static void endGame(Battler winner, Battler loser){
        manager.setGameState(GameState.END);
        if (winner != null) {
            System.out.println();
            System.out.println(winner.getName() + " wins with " + winner.getCurrentHp() + "hp remaining!");
        }
        else{
            System.out.println("If you're seeing this, something is wrong.");
        }
        System.exit(0); //close the battle engine later on
    }

    //does like stuff in the battle or something
    static class BattleManager{

        Battler[] getBattlers(){
            return new Battler[]{battler1, battler2};
        }
        //is the battler dead or alive?
        boolean isBattlerAlive(Battler battler){
            return battler.isAlive();
        }

        //resets all modifiers (+atk,+def,+hp)
        void resetMods(Battler battler){
            battler.setAtkMod(0);
            battler.setDefMod(0);
            battler.setSpdMod(0);
            battler.resetAttack();
        }

        //checks if both players are alive
        boolean arePlayersAlive(){
            return isBattlerAlive(battler1) && isBattlerAlive(battler2);
        }

        /** Changes the game state.
         * @param index
         * 0 - CHOOSE
         * 1 - IN_GAME
         * 2 - END
         */
        void setGameState(int index){
            switch (index) {
                case 0 -> gm = GameState.CHOOSE;
                case 1 -> gm = GameState.IN_GAME;
                case 2 -> gm = GameState.END;
                default -> throw new IllegalArgumentException("Index has to be between 0 and 2!");
            }
        }

        /** Changes the game state.
         * @param gameState
         *
         * CHOOSE - character selection screen
         *
         * IN_GAME - in game
         *
         * END - game ends
         */
        void setGameState(GameState gameState){
            switch (gameState) {
                case CHOOSE -> gm = GameState.CHOOSE;
                case IN_GAME -> gm = GameState.IN_GAME;
                case END -> gm = GameState.END;
                default -> throw new IllegalArgumentException("Index has to be between 0 and 2!");
            }
        }

        //prompt the player for actions (a- attack, d- defend,
        void promptPlayer(Battler battler, Battler opposingBattler){
            System.out.println();
            ActionType chosenAction = ActionType.values()[-1 + Integer.parseInt(input.getInput("Your HP = " + battler1.getCurrentHp() + "/"+ battler1.getBaseHp() +
                    "     "+ battler2.getName() +" HP = " + battler2.getCurrentHp() + "/" + battler2.getBaseHp() +
                    "\n Choose your action!" +
                    "\n 1 >> Attack - Does damage scaled on your attack stat" +
                    "\n 2 >> Defend - Applies a x2 defense multiplier for 1 turn" +
                    "\n 3 >> Heal - Recover a small portion of your health" +
                    "\n 4 >> Info - Check the information of the battle"))];

            switch (chosenAction){
                case ATTACK:
                    battler.attack(opposingBattler);
                    break;
                case DEFEND:
                    battler.defend();
                    break;
                case HEAL:
                    battler.heal();
                    break;
                //ISSUE: Info should be allowed
                case INFO:
                    battler.getInfo();
                    promptPlayer(battler,opposingBattler);
                    break;
                /*case RUN:
                    System.out.println("");
                    break;
                    (not implemented yet
                 */
                default:
                    System.out.println("That isn't an action! Pick a number 1 - 4.");
                    promptPlayer(battler,opposingBattler);
            }
        }

        static void doAction(ActionType at, Battler user, Battler receiver){
            /*case RUN:
                    System.out.println("");
                    break;
                    (not implemented yet
                 */
            switch (at) {
                case ATTACK -> user.attack(receiver);
                case DEFEND -> user.defend();
                case HEAL -> user.heal();
                case INFO -> user.getInfo();
            }
        }



        //Prompt user to pick their battler
        static void pickBattler(){
            gm = GameState.CHOOSE;
            BattlerType chosenBattler = BattlerType.values()[-1 + Integer.parseInt(input.getInput("Pick your fighter! " +
                    "\n 1 >> Dylan - pick him if you want to lose" +
                    "\n 2 >> Michael - fast attacker" +
                    "\n 3 >> Retard - slow tank"))];
            battler1.setBattlerType(chosenBattler);
            gm = GameState.IN_GAME;
        }

    }


}



class Input{
    Scanner sc;
    Random rand;

    Input(){
        sc = new Scanner(System.in);
        rand = new Random();
    }

    String getInput(){
       return sc.nextLine();
    }

    String getInput(String prompt){
        System.out.println(prompt);
        return sc.nextLine();
    }
    int getRandomInt(int limit){
        return rand.nextInt(limit);
    }
}




