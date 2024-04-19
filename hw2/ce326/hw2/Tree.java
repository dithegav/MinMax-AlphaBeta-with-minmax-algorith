package ce326.hw2;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.FileAlreadyExistsException;
import java.util.ArrayList;
import java.util.Scanner;
import org.json.JSONArray;
import org.json.JSONObject;

public class Tree {
    private BasicNode root;
    //sorry if i was out of the projects implementation
    int Sorted;//has to create this field cause i cant keep track wether the aloritm has been used or not
    int size;//discovers immediately the size of the tree while creating it to avoid confusion while json file decomposing


    public Tree (String JSONstr) throws IllegalArgumentException, org.json.JSONException{

    }

    public Tree (File file) throws FileNotFoundException, IllegalArgumentException, org.json.JSONException{
        StringBuilder strBuilder = new StringBuilder();
        try(Scanner sc = new Scanner(new File(file.getAbsolutePath()))) {
            while( sc.hasNextLine() ) {
                String str = sc.nextLine();
                strBuilder.append(str);
                strBuilder.append("\n");
            }
        }

        JSONObject JSONObj = new  JSONObject(strBuilder.toString());
        root = CreateTree (JSONObj);

    }

    public BasicNode CreateTree (JSONObject JSONObj) {
        String type;

        type = JSONObj.getString("type");

        if (type.equals("leaf")){
            //create only a leaf if needed
            BasicNode LeafNode = new BasicNode (JSONObj.getDouble("value"));
            size++;

            return LeafNode;
        }
        else
            if (type.equals("max")){
                //create a maximizer Node
                InnerNode NewNode = new maximizer();
                size++;

                JSONArray FindChildren = JSONObj.getJSONArray("children");

                NewNode.setChildrenSize(FindChildren.length());
                NewNode.ChangeSize();

                for (int i=0; i < FindChildren.length(); i++){
                    JSONObject NewChildObj = FindChildren.getJSONObject(i);
                    BasicNode NewChild = CreateTree (NewChildObj);
                    NewNode.insertChild(i, NewChild);
                }

                return NewNode;
            }
            else {
                //create a minimizer Node
                InnerNode NewNode = new minimizer();
                size++;

                JSONArray FindChildren = JSONObj.getJSONArray("children");
                
                NewNode.setChildrenSize(FindChildren.length());
                NewNode.ChangeSize();

                for (int i=0; i < FindChildren.length(); i++){
                    JSONObject NewChildObj = FindChildren.getJSONObject(i);
                    BasicNode NewChild = CreateTree (NewChildObj);
                    NewNode.insertChild(i, NewChild);
                }

                return NewNode;

            } 
    }

    public double minMax(){

        minMaxRecursive(root);

        return 0;
    }

    public BasicNode minMaxRecursive (BasicNode Node){
        //had to do many typecasts because root maybe inner Node and innernode from array may be
        //either innenode or basicnode so avoid the confusion
        double value=0;

        if (Node instanceof InnerNode){

            for (int i=0; i < ((InnerNode)Node).size; i++){
                
                minMaxRecursive(((InnerNode)Node).ArrayLeafNode[i]);

                if (Node instanceof maximizer){
                    value = ((maximizer)Node).FindMaximum(((InnerNode)Node));
                    Node.putValue(value);
                }
                else if (Node instanceof minimizer){
                    value = ((minimizer)Node).FindMinimum(((InnerNode)Node));
                    Node.putValue(value);
                }
            }
        }

        return (Node);
    }

    public BasicNode getroot (){
        //help all the classes to have access to the root
        return root;
    }


    public int size (){
        return size;
    }

    public ArrayList<Integer> optimalPath(){
        ArrayList<Integer> path = new ArrayList<>();
        
        path = FindPath (root, path);
        

        return path;
    }

    public ArrayList<Integer> FindPath (BasicNode Node, ArrayList<Integer> path){
        //recursively find the path that the tree used to change value in the basic inner Node untill root

        if (Node instanceof InnerNode){
            for (int i=0; i<((InnerNode)Node).size;i++){
                if (Node.getValue() == ((InnerNode)Node).ArrayLeafNode[i].getValue()){
                    path.add(i);
                    path = FindPath(((InnerNode)Node).ArrayLeafNode[i], path);
                   
                    break;
                }
            }
        }

        return path;
    }

    public String toString (){
        JSONObject rootObj = toJsonRecursive(root);
        return rootObj.toString(2);
    }

    public String toDotString (){
        StringBuffer tostr = new StringBuffer();
        String toDotString="";

        System.out.println ("graph MinMax {");

        //toDotString = toDotStringTemp(root, tostr);

        System.out.println (toDotString + "}");

        return toDotString;
    }

    // public String toDotStringTemp (BasicNode Node, StringBuffer tostr){
    //     //with the append method in an str i stack that string with th emessage that the dot file should have
    //     //following the excersises instractions

    //     tostr.append("\t" + Node.hashCode() + " [label=\"" + Node.getValue() + "\"]\n");

    //     if (Node instanceof InnerNode){
    //         for (int i=0; i<((InnerNode)Node).size;i++){

    //             toDotStringTemp(((InnerNode)Node).ArrayLeafNode[i], tostr); 
                
    //             tostr.append ("\t" + ((InnerNode)Node).hashCode() + " -- " + ((InnerNode)Node).ArrayLeafNode[i].hashCode() + "\n");
    //         }
    //     }

    //     return tostr.toString();
    // }

    public void toFile (File file) throws IOException, FileAlreadyExistsException{
        try {
            PrintWriter writer = new PrintWriter(file, "UTF-8");
            writer.println(toString());
            writer.close();
        } finally{}
    }

    public void toDotFile (File file) throws IOException, FileAlreadyExistsException{
        try {
            PrintWriter writer = new PrintWriter(file, "UTF-8");
            writer.println(toDotString());
            writer.close();
        } finally{}
    }

    public JSONObject toJsonRecursive(BasicNode Node) {
        JSONObject toStringObj = new JSONObject();
        
        if (Node instanceof InnerNode) {
            JSONArray childrenArr = new JSONArray();
            for (int i = 0; i < ((InnerNode)Node).size; i++) {
                BasicNode child = ((InnerNode)Node).ArrayLeafNode[i];
                childrenArr.put(toJsonRecursive(child));
            }
            
            //find witch node is included in the tree
            if (Node instanceof maximizer){
                toStringObj.put("type", "max");
            }
            else if (Node instanceof minimizer) {
                toStringObj.put("type", "max");
            }
            if (Sorted == 1){
                toStringObj.put("value", ((InnerNode)Node).getValue());
            }
            toStringObj.put("children", childrenArr);
           
        } else if (Node instanceof BasicNode) {
            toStringObj.put("type", "leaf");
            toStringObj.put("value", Node.getValue());
        }

        return toStringObj;
    }
    
}
