package test.villagepeople.util.graph.search;

import java.awt.Graphics2D;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import villagepeople.navigation.ManhattanHeuristic;
import explicitlib.graph.Graph;
import explicitlib.graph.IGraph;
import explicitlib.graph.SimpleWeight;
import explicitlib.graph.Graph.Edge;
import explicitlib.graph.Graph.Node;
import explicitlib.graph.IGraph.Weight;
import explicitlib.graph.search.AStarSearch;
import explicitlib.graph.search.FindTargetCondition;
import explicitlib.graph.search.Heuristic;
import explicitlib.graph.search.NoPathFoundException;
import explicitlib.graph.search.Path;
import explicitlib.graph.search.TerminationCondition;


public class AStarSearchTest {

    @Before public void setUp() {
        map = new Graph<Node<String>, Edge>();

        Node<String> Aveiro = new Node<String>("Aveiro");
        Node<String> Braga = new Node<String>("Braga");
        Node<String> Braganca = new Node<String>("Braganca");
        Node<String> Beja = new Node<String>("Beja");
        Node<String> CasteloBranco = new Node<String>("Castelo-branco");
        Node<String> Coimbra = new Node<String>("Coimbra");
        Node<String> Evora = new Node<String>("Evora");
        Node<String> Faro = new Node<String>("Faro");
        Node<String> Guarda = new Node<String>("Guarda");
        Node<String> Leiria = new Node<String>("Leiria");
        Node<String> Lisboa = new Node<String>("Lisboa");
        Node<String> Portalegre = new Node<String>("Portalegre");
        Node<String> Porto = new Node<String>("Porto");
        Node<String> Santarem = new Node<String>("Santarem");
        Node<String> Setubal = new Node<String>("Setubal");
        Node<String> VianaCastelo = new Node<String>("Viana do castelo");
        Node<String> VilaReal = new Node<String>("Vila-real");
        Node<String> Viseu = new Node<String>("Viseu");

        map.add(Aveiro);
        map.add(Braga);
        map.add(Braganca);
        map.add(Beja);
        map.add(CasteloBranco);
        map.add(Coimbra);
        map.add(Evora);
        map.add(Faro);
        map.add(Guarda);
        map.add(Leiria);
        map.add(Lisboa);
        map.add(Portalegre);
        map.add(Porto);
        map.add(Santarem);
        map.add(Setubal);
        map.add(VianaCastelo);
        map.add(VilaReal);
        map.add(Viseu);
        
        map.addEdge(new Edge(Aveiro, Porto, new SimpleWeight(68)));
        map.addEdge(new Edge(Aveiro, Viseu, new SimpleWeight(95)));
        map.addEdge(new Edge(Aveiro, Coimbra, new SimpleWeight(58)));
        map.addEdge(new Edge(Aveiro, Leiria, new SimpleWeight(115)));

        map.addEdge(new Edge(Braga, VianaCastelo, new SimpleWeight(48)));
        map.addEdge(new Edge(Braga, VilaReal, new SimpleWeight(106)));
        map.addEdge(new Edge(Braga, Porto, new SimpleWeight(53)));
               
        map.addEdge(new Edge(Braganca, VilaReal, new SimpleWeight(137)));
        map.addEdge(new Edge(Braganca, Guarda, new SimpleWeight(202)));

        map.addEdge(new Edge(Beja, Evora, new SimpleWeight(78)));
        map.addEdge(new Edge(Beja, Faro, new SimpleWeight(152)));
        map.addEdge(new Edge(Beja, Setubal, new SimpleWeight(142)));

        map.addEdge(new Edge(CasteloBranco, Coimbra, new SimpleWeight(159)));
        map.addEdge(new Edge(CasteloBranco, Guarda, new SimpleWeight(106)));
        map.addEdge(new Edge(CasteloBranco, Portalegre, new SimpleWeight(80)));
        map.addEdge(new Edge(CasteloBranco, Evora, new SimpleWeight(203)));

        map.addEdge(new Edge(Evora, Lisboa, new SimpleWeight(150)));
        map.addEdge(new Edge(Evora, Santarem, new SimpleWeight(117)));
        map.addEdge(new Edge(Evora, Portalegre, new SimpleWeight(131)));
        map.addEdge(new Edge(Evora, Setubal, new SimpleWeight(103)));
        
        map.addEdge(new Edge(Coimbra, Viseu, new SimpleWeight(96)));
        map.addEdge(new Edge(Coimbra, Leiria, new SimpleWeight(67)));
        
        map.addEdge(new Edge(Faro, Setubal, new SimpleWeight(249)));
        map.addEdge(new Edge(Faro, Lisboa, new SimpleWeight(299)));
        map.addEdge(new Edge(Guarda, VilaReal, new SimpleWeight(157)));
        map.addEdge(new Edge(Guarda, Viseu, new SimpleWeight(85)));
        map.addEdge(new Edge(Leiria, Lisboa, new SimpleWeight(129)));
        map.addEdge(new Edge(Leiria, Santarem, new SimpleWeight(70)));
        map.addEdge(new Edge(Lisboa, Santarem, new SimpleWeight(78)));
        map.addEdge(new Edge(Porto, VianaCastelo, new SimpleWeight(71)));
        map.addEdge(new Edge(Porto, VilaReal, new SimpleWeight(116)));
        map.addEdge(new Edge(Porto, Viseu, new SimpleWeight(133)));
        map.addEdge(new Edge(VilaReal, Viseu, new SimpleWeight(110)));
        
        final Map<String, Integer> distancesToFaro = new HashMap<String, Integer>();
        distancesToFaro.put("Aveiro", 363);
        distancesToFaro.put("Braga", 454);
        distancesToFaro.put("Braganca", 487);
        distancesToFaro.put("Beja", 99);
        distancesToFaro.put("Castelo-branco", 280);
        distancesToFaro.put("Coimbra", 319);
        distancesToFaro.put("Evora", 157);
        distancesToFaro.put("Faro", 0);
        distancesToFaro.put("Guarda", 352);
        distancesToFaro.put("Leiria", 278);
        distancesToFaro.put("Lisboa", 195);
        distancesToFaro.put("Portalegre", 228);
        distancesToFaro.put("Porto", 418);
        distancesToFaro.put("Santarem", 228);
        distancesToFaro.put("Setubal", 168);
        distancesToFaro.put("Viana do castelo", 473);
        distancesToFaro.put("Vila-real", 429);
        distancesToFaro.put("Viseu", 363);
                  
        euclidianDistanceHeuristic = new Heuristic() {

            public Weight estimate(IGraph graph, IGraph.Node current, TerminationCondition condition, Weight costSoFar) {
                return costSoFar.plus(new SimpleWeight(distancesToFaro.get(current.getKey())));
            }
        };
    }
    
    @Test
    public void shortestDistanceCoimbraFaro() {
        AStarSearch<String> searcher = new AStarSearch<String>(map, "Coimbra", new FindTargetCondition(map, "Faro"), euclidianDistanceHeuristic);
        try {
            List<String> cities = searcher.search(new SimpleWeight(0)).getWayPoints();
            List<String> result = Arrays.asList(new String[] { "Coimbra", "Leiria", "Santarem", "Evora", "Beja", "Faro" });
            assertThat(cities, is(result));
        } catch (NoPathFoundException e) {
            e.printStackTrace();
        }
    }
    
    private Heuristic euclidianDistanceHeuristic;
    private Graph<Node<String>, Edge> map;
}
