package ce326.hw2;

public class minimizer extends InnerNode {
    public minimizer (){
        super();
    }

    public double FindMinimum (InnerNode ParentNode){
        double minValue=0;

        minValue = ParentNode.ArrayLeafNode[0].getValue();

        for (int i=1; i < ParentNode.ArrayLeafNode.length; i++){
            if (minValue > ParentNode.ArrayLeafNode[i].getValue()){
                minValue = ParentNode.ArrayLeafNode[i].getValue();
            }
        }

        return minValue;
    }
}
