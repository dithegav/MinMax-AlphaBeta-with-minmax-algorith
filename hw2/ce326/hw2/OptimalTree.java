package ce326.hw2;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.FileAlreadyExistsException;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONObject;

public class OptimalTree extends Tree{

    public OptimalTree (File file) throws FileNotFoundException, IllegalArgumentException{
        //use everything from the tree but be a different one for more implementantions
        super(file);
    }

    public double minMax (){

        if (size > 1){
            //if the rrot is not alone initialize everything and start alpha beta pruning
            ((InnerNode)getroot()).alpha = -java.lang.Double.MAX_VALUE;
            ((InnerNode)getroot()).beta = java.lang.Double.MAX_VALUE;
            ((InnerNode)getroot()).Notpruned = 1;
            minMaxRecursiveOptimal(getroot(),((InnerNode)getroot()).alpha,((InnerNode)getroot()).beta);
        }
        else {
            getroot().Notpruned = 1;
        }

        return getroot().getValue();
    }

    public BasicNode minMaxRecursiveOptimal (BasicNode Node, double alpha, double beta) {

        Node.Notpruned = 1;
    
        if (Node instanceof InnerNode){

            for (int i=0; i < ((InnerNode)Node).size; i++){
                
                minMaxRecursiveOptimal(((InnerNode)Node).ArrayLeafNode[i], alpha, beta);
                //go to every inner Node

                if (Node instanceof maximizer){
                    
                    if (i == 0){
                        //if first time fillig the first Node put the value of the child as it is
                        Node.putValue(((InnerNode)Node).ArrayLeafNode[i].getValue());
                    }
                    else {
                        //check if this child has better value for the Node
                        Node.putValue(findmax(Node.getValue(), ((InnerNode)Node).ArrayLeafNode[i].getValue()));
                    }

                    //change if needed the alpha according to every recurse
                    alpha = findmax(alpha, ((InnerNode)Node).ArrayLeafNode[i].getValue());
                    ((InnerNode)Node).alpha = alpha;

                    //only if this is false continue checking the rest childs
                    if (alpha >= beta){
                        break;
                    }
                }
                else if (Node instanceof minimizer){

                    if (i == 0){
                         //if first time fillig the first Node put the value of the child as it is
                        Node.putValue(((InnerNode)Node).ArrayLeafNode[i].getValue());
                    }
                    else {
                        //check if this child has better value for the Node
                        Node.putValue(findmin(Node.getValue(), ((InnerNode)Node).ArrayLeafNode[i].getValue()));
                    }

                    //change if needed the beta according to every recurse
                    beta = findmin(beta, ((InnerNode)Node).ArrayLeafNode[i].getValue());
                    ((InnerNode)Node).beta = beta;

                    //only if this is false continue checking the rest childs
                    if (alpha >= beta){
                        break;   
                    }
                }
            }
        }

        return (Node);
    }

    public ArrayList<Integer> optimalPath(){
        ArrayList<Integer> path = new ArrayList<>();
        
        //find the path that the upper class used to put the input in parent Node untill root
        path = FindPath (getroot(), path);

        return path;
    }

    public int prunedNodes (){
        int pruned =0;
        BasicNode Node = getroot();

        //count how many Nodes have been pruned from the algorithm
        if (getroot() == null){
            return 0;
        }
        
        pruned = findpruned(Node, pruned);

        return pruned;
    }

    public int findpruned (BasicNode Node, int pruned){
        //search every Node and lead and find out if they are prunned
        //if value is 0 then its pruned
        if (Node.Notpruned == 0){
            pruned += 1;
        }

        if (Node instanceof InnerNode){  
            for (int i=0; i<((InnerNode)Node).size;i++){
                pruned = findpruned(((InnerNode)Node).ArrayLeafNode[i], pruned);
                
            }
        }

        return pruned;
    }

    public int size () {
        return size;
    }

    public String toString (){
        JSONObject rootObj = toStringImplementation(getroot());
        return rootObj.toString(2);
    }

    public String toDotString (){
        StringBuffer tostr = new StringBuffer();
        String toDotString="";

        System.out.println ("graph MinMaxPruning {");

        //toDotString = toDotStringTemp(((InnerNode)getroot()), tostr);

        System.out.println (toDotString + "}");

        return toDotString;
    }

    // public String toDotStringTemp (BasicNode Node, StringBuffer tostr){
    //     //with the append method in an str i stack that string with the message that the dot file should have
    //     //following the excersises instractions

    //     if (Node.Notpruned == 1){
    //         tostr.append("\t" + Node.hashCode() + " [label=\"" + Node.getValue() + "\"]\n");
    //     }
    //     else if (Node.Notpruned == 0){
    //         tostr.append("\t" + Node.hashCode() + " [label=\"" + Node.getValue() + "\"]"  + " [color=\"" + "red" + "\"]\n");
    //     }

    //     if (Node instanceof InnerNode){
    //         for (int i=0; i<((InnerNode)Node).size;i++){

    //             toDotStringTemp(((InnerNode)Node).ArrayLeafNode[i], tostr); 
                
    //             tostr.append ("\t" + Node.hashCode() + " -- " + ((InnerNode)Node).ArrayLeafNode[i].hashCode() + "\n");
    //         }
    //     }

    //     return tostr.toString();
    // }

    public void toFile (File file) throws IOException, FileAlreadyExistsException{
        try {
            //use the ready writer class to acces the file
            PrintWriter writer = new PrintWriter(file, "UTF-8");
            writer.println(toString());
            writer.close();
        } finally{}
    }

    public void toDotFile (File file) throws IOException, FileAlreadyExistsException{
        try {
            //use the ready writer class to acces the file
            PrintWriter writer = new PrintWriter(file, "UTF-8");
            writer.println(toDotString());
            writer.close();
        } finally{}
    }

    public double findmax (double a, double b){
        //only check if the value is greater or equal
        if (a >= b){
            return a;
        }
        else {
            return b;
        }
    }

    public double findmin (double a, double b){
        //only check if the value is less or equal
        if (a <= b){
            return a;
        }
        else {
            return b;
        }
    }

    public JSONObject toStringImplementation(BasicNode Node) {
        JSONObject toStringObj = new JSONObject();
        
        if (Node instanceof InnerNode) {

            JSONArray children = new JSONArray();
            for (int i = 0; i < ((InnerNode)Node).size; i++) {
                BasicNode child = ((InnerNode)Node).ArrayLeafNode[i];
                children.put(toStringImplementation(child));
            }

            if (Node instanceof maximizer){
                toStringObj.put("type", "max");
            }
            else if (Node instanceof minimizer) {
                toStringObj.put("type", "max");
            }

            //only if the node has been changed of its value print it
            //if not print different message
            if (Node.Notpruned == 1){
                toStringObj.put("value", ((InnerNode)Node).getValue());
            }
            else if (Node.Notpruned == 0){
                toStringObj.put("pruned", "true");
            }
            toStringObj.put("children", children);
           
        } else if (Node instanceof BasicNode) {

            toStringObj.put("type", "leaf");
            toStringObj.put("value", Node.getValue());

            //same here if leaf is pruned print different message
            if (Node.Notpruned == 0){
                toStringObj.put("pruned", "true");
            }
        }

        return toStringObj;
    }

}
