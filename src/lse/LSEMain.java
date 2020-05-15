package lse;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class LSEMain {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		LittleSearchEngine r=new LittleSearchEngine();
		r.makeIndex("main","noisewords.txt");
		ArrayList<String>joe=new ArrayList<String>();
		joe=r.top5search("red","orange");
		System.out.println(joe);
	}

}
