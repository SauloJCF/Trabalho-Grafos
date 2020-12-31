/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author SauloJCF
 */
public class Aresta implements Comparable<Aresta>{
    int u;
    int v;
    int p;
    
    @Override
    public int compareTo(Aresta aresta2){
        if(this.p > (aresta2.p))
            return 1;
        else if(this.p < (aresta2.p))
            return -1;
        else
            return 0;
    }
}
