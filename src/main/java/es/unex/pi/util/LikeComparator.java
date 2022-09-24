package es.unex.pi.util;

import java.util.Comparator;

import es.unex.pi.model.Hosting;

public class LikeComparator implements Comparator<Hosting> {
	
	public int compare(Hosting h1, Hosting h2) {
	  
		if (h1.getLikes() == h2.getLikes()) {
            return 0;
        }
        else if (h1.getLikes() < h2.getLikes()) {
            return 1;
        }
        else {
            return -1;
        }
	}
	    
}
