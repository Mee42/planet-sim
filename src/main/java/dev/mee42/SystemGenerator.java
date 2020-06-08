package dev.mee42;

import java.util.Random;

public class SystemGenerator {


  public static int[][] genGalaxy(int seed){
    Random rnd = new Random(seed);

    int numSystems = 0;
    int numClusters = rnd.nextInt(6) + 10;
    int counter = 0;
    int[][] tempStarLoc = new int[150][2];

    for(int C=0; C<numClusters;C++){
      int temp = rnd.nextInt(9)+2;
      numSystems+=temp;
      int[][] tempLoc = genCluster(rnd, numSystems/numClusters);
      for (int[] ints : tempLoc) {
        tempStarLoc[counter] = ints;
        counter++;
      }
    }
    int[][] starLoc = new int[numSystems][2];

    System.arraycopy(tempStarLoc, 0, starLoc, 0, numSystems);

    return starLoc;
  }

  public static int[][] genCluster(Random rnd, int numSystems){
    int[][] starLoc = new int[numSystems][2];

    int[] clusterCntr = new int[2];
    clusterCntr[0] = rnd.nextInt(1991)+10;
    clusterCntr[1] = rnd.nextInt(1991)+10;

    for(int S = 0; S<numSystems; S++){
      starLoc[S] = genSystem(rnd, clusterCntr);
    }


    return starLoc;
  }

  public static int[] genSystem(Random rnd,int[] clusterCntr){
    int[] starLoc = new int[2];

    starLoc[0] = rnd.nextInt(13) + clusterCntr[0] + 2;
    starLoc[1] = rnd.nextInt(13) + clusterCntr[1] + 2;

    return starLoc;
  }
}
