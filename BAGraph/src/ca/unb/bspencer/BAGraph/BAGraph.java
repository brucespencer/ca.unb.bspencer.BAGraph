
package ca.unb.bspencer.BAGraph;

import java.util.*;

public class BAGraph {
		
	class Edge {
		Integer from, to;
		Object value;
		
		Edge(Integer f, Integer t, Object v){
			from = f;
			to = t;
			value = v;
		}
		
		Edge(Integer f, Integer t){
			from = f;
			to = t;
			value = null;
		}
		
		
		public String toString(){
			if (value == null)
				return "edge(" + from + " -> " + to + ")";
			else
				return "edge(" + from + " -> " + to + ": " + value + ")";
		}	
	}
	
	HashMap nodes = new HashMap();
	int edgeCount;
	Random r = new Random();
	
	void insertEdge(Edge e){
		HashMap nodeList = (HashMap) nodes.get(e.from);
		if (nodeList == null) {
			nodes.put(e.from, new HashMap());
			nodeList = (HashMap) nodes.get(e.from);
		}
		nodeList.put(e.to, e);
		edgeCount++;
	}
	
	BAGraph (int nNodes){
		edgeCount = 0;
		if (nNodes <= 1)
			return;
		nodes = new HashMap();
		insertEdge(new Edge(new Integer(0), new Integer(1)));
	    edgeCount++;
		
		for(int i=2; i<nNodes; i++){			
//			insert nodes by preferential attachment
			int probe = r.nextInt(edgeCount); 

			Iterator nIt = nodes.entrySet().iterator();
			Map.Entry pair = (Map.Entry) nIt.next(); //we know there is at least one edge
			HashMap nodeList = (HashMap) pair.getValue();
			int partSum = nodeList.size();
			while (probe > partSum){
				pair = (Map.Entry) nIt.next();
				nodeList = (HashMap) pair.getValue();
				partSum += nodeList.size();
			}
			Integer from = new Integer(i);
			Integer to = new Integer( (Integer) pair.getKey() );
			insertEdge(new Edge(from, to));
			insertEdge(new Edge(to, from));
		}
	}
	
	public String toString(){
		String result = "";
		Iterator nIt = nodes.entrySet().iterator();
		while(nIt.hasNext()){
			Map.Entry pair = (Map.Entry) nIt.next();
			HashMap nodeList = (HashMap) pair.getValue();
			int size = nodeList.size();
			result += pair.getKey() + " -> " + pair.getValue() + "<size = " + size + ">" + "; ";
		}	
		return result;
	}
	
	class Counter{
		int val;
		Counter (int i){
			val = i;
		}
		void inc(){
			val++;
		}
		int value(){
			return val;
		}
	}
	
	public String BAStats(){
		Iterator nIt = nodes.entrySet().iterator();
		ArrayList l = new ArrayList();
		l.add(new Counter(0));
		while(nIt.hasNext()){
			Map.Entry pair = (Map.Entry) nIt.next();
			HashMap nodeList = (HashMap) pair.getValue();
			int count = nodeList.size();
			if(l.size() <= count)
				l.add(new Counter(1));
			else
				((Counter) l.get(count)).inc();
		}
		String result = "";

		for(int i = 1; i<l.size(); i++){
			for(int j= 5; j> Math.log10(i); j--){
				result += " ";	
			}
			result += i + ":";
			for(int j= 5; j> Math.log10( ((Counter) l.get(i)).value() ); j--){
				result += " ";	
			}
			result += ((Counter) l.get(i)).value() + ":";
			for(int j = 0; j< Math.log(((Counter) l.get(i)).value()); j++)
				result += "*";
			result += System.getProperty("line.separator"); 
		}
		return result;
	}

	public static void main(String[] args) {
		System.out.println("Hello BAGraph");
		System.out.println("Enter size of BAGraph: ");
		Scanner in = new Scanner(System.in);
		int graphSize = in.nextInt();
		BAGraph g = new BAGraph(graphSize);
//		System.out.println("Graph is " + g);
		System.out.println(g.BAStats());
		System.out.println("end");
	}
	
	
	
}

