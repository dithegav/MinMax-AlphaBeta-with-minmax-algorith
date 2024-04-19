package ce326.hw2;

public class InnerNode extends BasicNode{
    BasicNode[] ArrayLeafNode;
    double alpha;
    double beta;
    int size;

    public InnerNode (){ 
        
    }

    public InnerNode(BasicNode[] init_ArrayLeafNode){

    }

    public void setChildrenSize (int size) {
        this.size = size;
    }

    public int getChildrenSize () {
        return this.ArrayLeafNode.length;
    }

    public void insertChild (int pos, BasicNode ChildNode){
        this.ArrayLeafNode[pos] = ChildNode;
    }

    public BasicNode getChild (int pos){
        return this.ArrayLeafNode[pos];
    }

    public void ChangeSize () {
        //because i dont have the size before i create the object for the array 
        //so i allocate new object small patenda
        this.ArrayLeafNode = new BasicNode[size];
    }
}
