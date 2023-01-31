import java.io.*;
import java.util.*;


class setProp{
	String fid;
    int actualSpread;
    int observedSpread;

    public setProp(String fid, int actualSpread,int observedSpread) {
        this.fid = fid;
        this.actualSpread = actualSpread;
        this.observedSpread=observedSpread;
    }
}
class Compare implements Comparator<setProp>{
	
	
	public int compare(setProp e1, setProp e2) {
return (e1.observedSpread<e2.observedSpread) ? e1.observedSpread : e2.observedSpread;

		
	}
}
public class BloomSketchHLL {
	 static int number_of_flows = 0;
	 static int e=4000;
	 static int r=128;
	 static int k=3;
	 static int[][] b =new int[e][r];

	
	public static int generategeomhash(double flow){
	    int element=0;
	    while(flow<=1){
	        element++;
	        flow=flow*2;
	    }
	    return element;
	}
	
		public static int generatehash(String  input, int val) {
		  long hash = 0;
		  long power = 1;
		    for (int i=0;i<input.length();i++) {
		        hash = (hash + (input.charAt(i) - ' ' + 1) * power) %Integer.MAX_VALUE;
		        power = (power * val)%Integer.MAX_VALUE;
		    }
		    return (int) (hash % e);
		}
	public static Map<String, Integer> byBufferedReader() {
	        Random r = new Random();
		    HashMap<String, Integer> inputmap = new HashMap<>();
		    String l;
		    String filepath="C:\\Users\\DELL\\eclipse-workspace\\project5input.txt";
		    File file = new File(filepath);
		    try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
		    	number_of_flows = Integer.parseInt(reader.readLine());
               System.out.println("line: "+ number_of_flows);
		        while ((l = reader.readLine()) != null) {
		            String[] kv = l.split("\\s+", 2);
		            if (kv.length > 1) {
		                String k =kv[0];
		                int val = Integer.parseInt(kv[1]);
	                    inputmap.put(k, val); 
		            } else {
		                System.out.println("Nothing found in this line or line is invalid" + l);
		            }
		        }
		    } catch (IOException e) {
		        e.printStackTrace();
		    }
		    
		    return inputmap;
		}


	public static void main(String[] args) throws IOException {
		PriorityQueue<setProp> q = new
		        PriorityQueue<setProp>(new Compare());
        int [] registers=new int[3];

		for(int i=0;i<registers.length;i++) {
	       	    registers[i] = (int)(Math.random()*(Integer.MAX_VALUE));
	    }
		
		List<Double> observedList=new ArrayList<Double>();
		Map<String,Integer> hashmapflow=byBufferedReader();
		for(Map.Entry<String, Integer> f : hashmapflow.entrySet()) {
			for(int i=0;i<f.getValue();i++) {
				int number = (int)(Math.random()*(Integer.MAX_VALUE));
				if(number<0) {
					number=-number;
				}
				int hashednumber = (int) (Math.floor (((long)13*number) % 256)/(256/r));
				int geometrichash=generategeomhash((double)number/Integer.MAX_VALUE);
				 for(int j=0;j<k;j++){
					 
					 int hashedflow=generatehash(f.getKey(),registers[j]);
					 b[hashedflow][hashednumber]=Math.max(b[hashedflow][hashednumber],geometrichash);
					 
	                }
				
			}
		}
		List<Integer> flowids=new ArrayList<Integer>();
		List<setProp> normallist = new ArrayList<setProp>();
		for(Map.Entry<String, Integer> f : hashmapflow.entrySet()) {	
			flowids.add(f.getValue());
			int valueofflow=f.getValue();
			String fid=f.getKey();
			 double obs=Integer.MAX_VALUE;

			 for(int j=0;j<k;j++){
		          
				 int hashflow=generatehash(f.getKey(),registers[j]);

				 double t=0.0;
		            for(int l=0;l<r;l++){
		                t=t+(1/Math.pow(2, b[hashflow][l]));
		            }
			 
		            obs=Math.min (obs,  (  (0.7213) / (1+ (1.079/r) )  * r * r * (1/t) ) );
		           
			 }  
				setProp s=new setProp(fid, valueofflow,(int)obs);
				normallist.add(s);
                q.add(s);
			 		
		}
		Collections.sort(normallist, new Comparator<setProp>() {
			  public int compare(setProp p1, setProp p2) {
			    return (p2.observedSpread>p1.observedSpread)?p2.observedSpread:p1.observedSpread;			    
			  }
			});
		File outputFile= new File("C:\\Users\\DELL\\eclipse-workspace\\outputproject5.txt");	
    	FileOutputStream opt= new FileOutputStream(outputFile);
    	BufferedWriter buff = new BufferedWriter(new OutputStreamWriter(opt));
		for(int i=0;i<25;i++) {
			setProp p1 =normallist.get(i);
			
			buff.write("Actual True Spread: "+ p1.fid+ " Observed Estimated spread: "+ p1.observedSpread);
			buff.newLine();
			System.out.println("Actual True Spread: "+ p1.fid+ " Observed Estimated spread: "+ p1.observedSpread);
		}
	      
	      buff.close();
	}

}
;