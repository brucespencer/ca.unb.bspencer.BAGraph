
package ca.unb.bspencer.BAGraph;

import java.util.*;

public class BAGraph {
	
	static final boolean UNDIRECTED = true;
		
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
	
	HashMap<Integer,HashMap<Integer, Edge>> nodes = new HashMap<Integer, HashMap<Integer, Edge>>();
	int edgeCount;
	Random r = new Random();
	
	void insertEdge(Edge e){
		HashMap<Integer, Edge> edgeHash = nodes.get(e.from);
		if (edgeHash == null) {
			nodes.put(e.from, new HashMap<Integer, Edge>());
			edgeHash = nodes.get(e.from);
		}
		edgeHash.put(e.to, e);
		edgeCount++;
	}
	
	BAGraph (int nNodes){
		edgeCount = 0;
		if (nNodes <= 1)
			return;
		nodes = new HashMap<Integer, HashMap<Integer, Edge>>();
		insertEdge(new Edge(new Integer(0), new Integer(1)));
	    if(BAGraph.UNDIRECTED){
	    	insertEdge(new Edge(new Integer(1), new Integer(0)));
	    }
		for(int i=2; i<nNodes; i++){	
//			System.out.println("Graph is " + this);
//			insert nodes by preferential attachment
			int probe = r.nextInt(edgeCount); 
//			System.out.println("probe " + probe);
//			System.out.println("edgeCount " + edgeCount);
			//we know there are at least two nodes and one edge
			Iterator nIt = nodes.entrySet().iterator();
			Map.Entry<Integer, HashMap> pair = (Map.Entry<Integer, HashMap>) nIt.next(); 
			HashMap<Integer, Edge> edgeHash = pair.getValue();
			int partSum = edgeHash.size();
//			System.out.println("node " + pair.getKey());
//			System.out.println("edgeHash " + edgeHash);
//			System.out.println("probe " + probe);
//			System.out.println("partSum " + partSum);
			while (probe > partSum){
				pair = (Map.Entry<Integer,HashMap>) nIt.next();
				edgeHash = pair.getValue();
				partSum += edgeHash.size();
//				System.out.println("node " + pair.getKey());
//				System.out.println("edgeHash " + edgeHash);
//				System.out.println("probe " + probe);
//				System.out.println("partSum " + partSum);
			}
			Integer from = new Integer(i);
			Integer to = new Integer( pair.getKey() );
			insertEdge(new Edge(from, to));
			if(BAGraph.UNDIRECTED){
				insertEdge(new Edge(to, from));
		    }
			
		}
	}
	
	public String toString(){
		String result = "";
		Iterator nIt = nodes.entrySet().iterator();
		while(nIt.hasNext()){
			Map.Entry pair = (Map.Entry) nIt.next();
			HashMap edgeHash = (HashMap) pair.getValue();
			int size = edgeHash.size();
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
		ArrayList<Counter> l = new ArrayList<Counter>();
		l.add(new Counter(0));
		while(nIt.hasNext()){
			Map.Entry pair = (Map.Entry) nIt.next();
			HashMap edgeHash = (HashMap) pair.getValue();
			int count = edgeHash.size();
			if(l.size() <= count)
				l.add(new Counter(1));
			else
				l.get(count).inc();
		}
		String result = "";

		for(int i = 1; i<l.size(); i++){
			for(int j= 5; j> Math.log10(i); j--){
				result += " ";	
			}
			result += i + ":";
			for(int j= 5; j> Math.log10( l.get(i).value() ); j--){
				result += " ";	
			}
			result += l.get(i).value() + ":";
			for(int j = 0; j< Math.log(l.get(i).value()); j++)
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
//		System.out.println("end"); 
	}
	
	
	
}

