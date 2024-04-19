package ce326.hw2;

//this class can only describe a leaf node on the tree
public class BasicNode {
    private double dValue;
    public int Notpruned;

    public BasicNode (){

    }

    public BasicNode (double inti_numValue){
        dValue = inti_numValue;
    }
    
    public double getValue (){
        return this.dValue;
    }

    public void putValue (double value){
        this.dValue = value;
    }
}
