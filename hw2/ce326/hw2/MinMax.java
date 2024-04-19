package ce326.hw2;

//class minmax where the main is and the menu is printed 
//and passed to the rest of the classes

import java.util.ArrayList;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class MinMax {

    public MinMax (){

    }

    public static void main(String []args) {
        OptimalTree tree2 = null;
        Tree tree1 = null;
        int check_call=0;//is a integer that finds witch tree shoukld be printed

        Scanner sc = new Scanner(System.in);
        do {
            System.out.printf( "\n-i <filename>   :  insert tree from file"+
                            "\n-j [<filename>] :  print tree in the specified filename using JSON format"+
                            "\n-d [<filename>] :  print tree in the specified filename using DOT format"+
                            "\n-c              :  calculate tree using min-max algorithm"+
                            "\n-p              :  calculate tree using min-max and alpha-beta pruning optimization"+
                            "\n-q              :  quit this program\n\n$> ");
            
            
            String str = sc.next();
            switch (str){
                case "-i" : {
                    //I do both trees differently so i know kinda where the problem is
                    str = sc.next();
                    File file = new File(str);
                    try{
                        
                        tree1 = new Tree (file);
                    }
                    catch(FileNotFoundException ex){
                        if (!file.exists()) {
                            System.out.println("\nUnable to find '"+str+"'\n");
                            continue;
                        }
                        if (!file.canRead()) {
                            System.out.println("\nUnable to open '"+str+"'\n");
                            continue;
                        }
                    }
                    catch(IllegalArgumentException ex){
                        System.out.println("\nUnable to open '"+str+"'\n");
                    }
                    catch(org.json.JSONException ex){
                        System.out.println ("\nInvalid format\n");
                        continue;
                    }

                    try{
                        tree2 = new OptimalTree (file);
                    }
                    catch(FileNotFoundException ex){
                        if (!file.exists()) {
                            System.out.println("\nUnable to find '"+str+"'\n");
                            continue;
                        }
                        if (!file.canRead()) {
                            System.out.println("\nUnable to open '"+str+"'\n");
                            continue;
                        }
                    }
                    catch(IllegalArgumentException ex){
                        System.out.println("\nUnable to open '"+str+"'\n");
                    }
                    
                    System.out.println("\nOK\n");
                    break;
                }
                case "-j" : {
                    try {
                        //first check if their is a file after and then utilize tostring
                        if (sc.hasNext()){
                            str = sc.next();
                            //str = str.substring(1, str.length()-1);
                            File toStringFile = new File (str);
                            if (toStringFile.exists()){
                                System.out.println("\nFile '"+str+"' already exists\n");
                                continue;
                            }
                            if (check_call == 0){
                                //open tree1
                                tree1.toFile(toStringFile);
                            }
                            else {
                                // open tree2 optimal
                                tree2.toFile(toStringFile);
                            }
                        }
                        else{
                            if (check_call == 0){
                                //open tree1
                                System.out.println(tree1.toString());
                            }
                            else {
                                // open tree2 optimal
                                System.out.println(tree2.toString());
                            }
                        }
                    }
                    catch (IOException ex){
                        System.out.println("\nUnable to write '"+str+"'\n");
                        continue;
                    }

                    System.out.println("\nOK\n");
                    break;
                }
                case "-d" : {
                    try {
                        //open file to print toDotfile
                        str = sc.next();
                        File toDotStringFile = new File (str);
                        if (toDotStringFile.exists()){
                            System.out.println("\nFile '"+str+"' already exists\n");
                            continue;
                        }
                        //again check whitch tree should be printed
                        if (check_call == 0){
                            //open tree1
                            tree1.toDotFile(toDotStringFile);
                        }
                        else {
                            tree2.toDotFile(toDotStringFile);
                        }
                    }
                    catch (IOException ex){
                        System.out.println("\nUnable to write '"+str+"'\n");
                        continue;
                    }

                    System.out.println("\nOK\n");
                    break;
                }
                case "-c" : {
                    //first check whether the tree given is incorrect
                    if (tree1 == null){
                        System.out.println("\nNot OK\n");
                        continue;
                    }
                    check_call=0;

                    tree1.minMax();
                    tree1.Sorted = 1;//this tells to the tree1 if the minmax has been used for the tree
                    ArrayList<Integer> path = tree1.optimalPath();
                    System.out.print("\n"+path.get(0));
                    for (int i=1; i<path.size(); i++){
                        System.out.print(", " + path.get(i));
                    }
                    System.out.println("\n");
                    break;

                } 
                case "-p" : {
                    //first check whether the tree given is incorrect
                    if (tree2 == null){
                        System.out.println("\nNot OK\n");
                        continue;
                    }
                    check_call=1;
                    tree2.minMax();//use alpha beta pruning implementation 
                    System.out.print("\n["+tree2.size+","+tree2.prunedNodes()+"] ");
                    ArrayList<Integer> path = tree2.optimalPath();
                    if (path.size() > 0){
                        System.out.print(path.get(0));
                        for (int i=1; i<path.size(); i++){
                            System.out.print(", " + path.get(i));
                        }
                        System.out.println("\n");
                    }
                    
                    break;
                }
                case "-q" : {
                    //quit
                    sc.close ();//close the scanner that we opened
                    System.exit(0);//smooth termination
                }
            }
        }while(true);
    }
}