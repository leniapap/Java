import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.*;


class Square {


       private int i,j;
       private Square prev;
       private char move;
       private float time_corona,time_sotiris;
       private boolean visited_corona,visited_sotiris;
       private Deque<Square> neighborhood ;

       public Square(int i,int j,Square prev,char move,float time_corona,float time_sotiris,boolean visited_sotiris,boolean visited_corona,Deque<Square> neighborhood)
       {
            this.i = i;
            this.j = j;
            this.prev = prev;
            this.move = move;
            this.time_sotiris = time_sotiris;
            this.time_corona = time_corona;
            this.visited_sotiris = visited_sotiris;
            this.visited_corona = visited_corona;
            this.neighborhood=new LinkedList<Square>();
       }

    public Square getPrev() {
        return prev;
    }

    public void setPrev(Square prev) {
        this.prev = prev;
    }

    public char getMove() {
        return move;
    }

    public void setMove(char c) {
        this.move = c;
    }


    public Deque<Square> getNeighborhood() {
        return neighborhood;
    }

    public void setNeighborhood(Deque<Square> neighborhood) {
        this.neighborhood = neighborhood;
    }

    public  Deque<Square> findTheNeighbors(char[][] worldMap,int n,int m,Square[][] pinClass) {
            if ((this.i!=n-1) &&( worldMap[this.i+1][this.j] != 'X'))
                neighborhood.add(pinClass[this.i+1][this.j]);
            if ((this.j != 0) && (worldMap[this.i][this.j-1] != 'X'))
                neighborhood.add(pinClass[this.i][this.j-1]);
            if ((this.j != m-1) && (worldMap[this.i][this.j + 1] != 'X'))
                neighborhood.add(pinClass[this.i][this.j+1]);
            if ((this.i != 0) && (worldMap[this.i-1][this.j] != 'X'))
                neighborhood.add(pinClass[this.i-1][this.j]);
            return neighborhood;
       }

    public float getTime_corona() {
        return time_corona;
    }

    public void setTime_corona(float time_corona) {
        this.time_corona = time_corona;
    }

    public float getTime_sotiris() {
        return time_sotiris;
    }

    public void setTime_sotiris(float time_sotiris) {
        this.time_sotiris = time_sotiris;
    }

    public boolean hasVisited_corona() {
        return visited_corona;
    }

    public void setVisited_corona(boolean visited_corona) {
        this.visited_corona = visited_corona;
    }

    public int getI() {
        return i;
    }

    public void setI(int i) {
        this.i = i;
    }

    public int getJ() {
        return j;
    }

    public void setJ(int j) {
        this.j = j;
    }

    public boolean hasVisited_sotiris() {
        return visited_sotiris;
    }

    public void setVisited_sotiris(boolean visited_sotiris) {
        this.visited_sotiris = visited_sotiris;
    }


}

public class StayHome {



    public static void main(String[] args) throws IOException {


        Deque<Square> sotiris=new LinkedList<Square>();
        Deque<Square> corona=new LinkedList<Square>();
            List<Integer> airports = new ArrayList<Integer>();



        try {
            //file scanning
                Scanner scan = new Scanner(new File(args[0]));
        		    ArrayList<String> lines = new ArrayList<String>();
        		    while (scan.hasNextLine()) {
            			    lines.add(scan.nextLine());
       			    }
                int n;
                int m;
        		    String[] inputStrings = lines.toArray(new String[0]);
        		    n = inputStrings.length;
        		    m = inputStrings[0].length();

                char[][] worldMap= new char[n][m];
                            for(int i = 0; i < inputStrings.length; i++)
                            worldMap[i] = inputStrings[i].toCharArray();
               	    scan.close();
                Square pinclass[][]= new Square[n][m];
                for(int i=0;i<n;i++) {
                    for(int j=0;j<m;j++){
                  	    pinclass[i][j]=new Square(0,0,null,' ',1000000,1000000,false,false,null);
               	       }
                   }
                Square endValue=null;
                    char character=' ';

                for (int r=0;r<n;r++) {
                    for (int c1=0;c1<m;c1++) {
                    	character = worldMap[r][c1];
                    	pinclass[r][c1].setI(r);
                    	pinclass[r][c1].setJ(c1);
                        if (character == 'S') {
                            pinclass[r][c1].setTime_sotiris(0);
                            pinclass[r][c1].setVisited_sotiris(true);
                            sotiris.add(pinclass[r][c1]);
                        }
                        else if( character == 'W') {
                            pinclass[r][c1].setTime_corona(0);
                            corona.add(pinclass[r][c1]);
                            }
                        else if ( character == 'T')
                            endValue = pinclass[r][c1];
                        else if( character == 'A')
                            {
                        airports.add(r);
                            airports.add(c1);
                            }

                    }
                }

                	boolean flag = false;
                    Square obj=null;
                    Square aux=null;
                    while (!corona.isEmpty())
                    {
                        obj = corona.removeFirst();
                        if(!obj.hasVisited_corona()) {
                            obj.setVisited_corona(true);
                            if ((worldMap[obj.getI()][obj.getJ()] == 'A') && ( flag == false)) {
                                flag = true;
                                for (int i=0;i<(airports.size()/2);i++ ) {
                                    aux = pinclass[airports.get(2*i)][airports.get(2*i+1)];
                                    aux.setTime_corona(Math.min(aux.getTime_corona(), obj.getTime_corona() + 5));
                                    corona.add(aux);
                                    }
                             }
                              for(Square object:obj.findTheNeighbors(worldMap, n, m, pinclass)) {
                                object.setTime_corona(Math.min(object.getTime_corona(),obj.getTime_corona()+2)) ;
                                corona.add(object);
                            }


                        }
                   }


                    while (!sotiris.isEmpty()) {
                        obj = sotiris.removeFirst();
                        for(Square object: obj.findTheNeighbors(worldMap, n, m, pinclass)) {
                        if (!(object.hasVisited_sotiris()) && (object.getTime_corona() > (obj.getTime_sotiris() + 1))) {
                                object.setTime_sotiris(obj.getTime_sotiris()+1);
                                object.setPrev(obj);
                                object.setVisited_sotiris(true);
                                if (object.getJ() == (obj.getJ() + 1))
                                    object.setMove('R');
                                else if ( object.getJ() == (obj.getJ() - 1))
                                    	object.setMove('L');
                                else if( object.getI()== (obj.getI() - 1))
                                    	object.setMove('U');
                                else
                                	object.setMove('D');
                                sotiris.addLast(object);
                            }
                        }
                    }


                    Deque<String> kinhseis=new LinkedList<String>();
                    int total_time = 0;

                    while( !(endValue.getPrev()==null) )
                    {

                        kinhseis.addFirst(Character.toString(endValue.getMove()));
                        total_time++;
                        endValue=endValue.getPrev();
                    }

                    if (kinhseis.isEmpty())
                       System.out.println("IMPOSSIBLE\n");
                    else {
                        System.out.println(Integer.toString(total_time));
                    for(String str: kinhseis)
                        		System.out.print(str );
                    	System.out.println("\n");
                    }





        } catch (FileNotFoundException e) {
            e.printStackTrace();

        }

        }
}
