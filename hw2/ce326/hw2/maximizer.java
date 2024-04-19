package ce326.hw2;

public class maximizer extends InnerNode{
    public maximizer (){
        super();
    }

    public double FindMaximum (InnerNode ParentNode){
        double maxValue=0;

        maxValue = ParentNode.ArrayLeafNode[0].getValue();

        for (int i=1; i < ParentNode.ArrayLeafNode.length; i++){
            if (maxValue < ParentNode.ArrayLeafNode[i].getValue()){
                maxValue = ParentNode.ArrayLeafNode[i].getValue();
            }
        }

        return maxValue;
    }
}
