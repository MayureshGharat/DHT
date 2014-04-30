package edu.upenn.cis.cis555.youtube;

import java.util.Comparator;

public class CompareClass implements Comparator<Long>{

	@Override
	public int compare(Long o1, Long o2) {
		// TODO Auto-generated method stub
		return (int) (o2-o1);
	}

	

}
