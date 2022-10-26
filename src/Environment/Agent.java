package Environment;

import java.util.Random;

public class Agent {
    int cell;
    public Agent(){
        this.cell = new Random().nextInt(50);
    }
    public int getCell(){
        return cell;
    }
    public void setCell(int cell){
        this.cell = cell;
    }
}
