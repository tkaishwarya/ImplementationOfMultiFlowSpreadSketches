import java.io.*;
import java.util.*;

public class VirtualBitMap {
	
	int Bitmaparray[]=new int[500000];
	int virtualbitmap=0;
	
	int vsize=500;
	
	String[] fids;
	int f[];
	int[] random;
	Random r = new Random();
	int hash_val=Math.abs(r.nextInt(Integer.MAX_VALUE-1));
	HashMap<String,Integer> input = new HashMap();
	HashMap<String,Integer> observed =  new HashMap();
	
	VirtualBitMap(int flows ,String[] flowids){
		
		fids=flowids;	
		random=new int[vsize];
		Set<Integer> randomvals=new HashSet<>();
		 for(int i=0;i<random.length;i++) {
		 	randomvals.add(Math.abs(r.nextInt(Integer.MAX_VALUE - 1)+1));
		 }

		 Iterator<Integer> iterator =randomvals.iterator();
		 int i=0;
		 while(iterator.hasNext()) {
		 	random[i]=iterator.next();	
		 	i++;
		 }
		int obs=0;
		int flowspread=0;
		
		for(i=0;i<500000;i++) {
			Bitmaparray[i]=0;
		}
		
		for(i=0;i<flows;i++) {
			String[] keyvals= fids[i].split("\\s+");
			flowspread=Integer.parseInt(keyvals[1]);;
			input.put(keyvals[0],flowspread);
			insert(keyvals[0],flowspread);
		}
		
		for(i=0;i<500000;i++) {
			if(Bitmaparray[i]==0) {
				virtualbitmap++;
			}
		}
		
	}

	
	public void insert(String f,int s) {
		
		int flows[]=new int[s];
		Set<Integer> vals=new HashSet<>();
		 for(int i=0;i<flows.length;i++) {
		 	vals.add(Math.abs(r.nextInt(Integer.MAX_VALUE - 1)+1));
		 }

		 Iterator<Integer> it =vals.iterator();
		 int i=0;
		 while(it.hasNext()) {
		 	flows[i]=it.next();	
		 	i++;
		 }
		
		for(i=0;i<flows.length;i++) {
			int index=(flows[i]^hash_val)%vsize;
			
			Bitmaparray[(Math.abs(f.hashCode())^random[index])%500000]=1;
		}
		
	}

	public int retrieve(String f) {
		int virtualbitmapflow=0;
		int flow=Math.abs(f.hashCode());
		
		for(int i=0;i<vsize;i++) {
			if(Bitmaparray[(flow^random[i])%500000]==0) {
				virtualbitmapflow++;
			}
		}

        return (int)Math.abs((vsize*Math.log((double)virtualbitmap/500000))-(vsize*Math.log((double)virtualbitmapflow/500)));
	}
	
	public static void main(String[] args) {
		
		try(BufferedReader br = new BufferedReader(new FileReader("C:\\Users\\DELL\\eclipse-workspace\\project5input.txt"))){
			int n = Integer.parseInt(br.readLine());
            String[] flowids=new String[n+1];
            
            for(int i=0;i<n;i++) {
	    		flowids[i]=br.readLine();
	    	}
            
            VirtualBitMap v=new VirtualBitMap(n,flowids);
	  	    File outputFile= new File("C:\\Users\\DELL\\eclipse-workspace\\graphplot.txt");	
	    	FileOutputStream opt= new FileOutputStream(outputFile);
	    	BufferedWriter buff = new BufferedWriter(new OutputStreamWriter(opt));
	    	
	    	for(int i=0;i<n;i++) {
	    		String flow=(v.fids[i]).split("\\s+")[0];
	    		
	    		int obs=v.retrieve(flow);
	    		
	    		v.observed.put(flow,obs);
	    		
	    		buff.write(v.input.get(flow)+" "+v.observed.get(flow));
	            buff.newLine();
	    	}
	         
	         buff.close();    

		}catch (Exception e) {
            e.printStackTrace();
        }

	}

}
