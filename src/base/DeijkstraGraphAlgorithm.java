package base;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveAction;

public class DeijkstraGraphAlgorithm {

    public static class Node {
        Integer id;

        Map<Node, Integer> neighbours = new HashMap();

        public Node(Integer id) {
            this.id = id;
        }

        public Node attache(int weight, Node neighbour) {
            neighbours.put(neighbour, weight);

            neighbour.neighbours.put(this, weight);

            return this;
        }

        public Integer lengthTo(Node node) {
            assert neighbours.containsKey(node);

            return neighbours.get(node);
        }

        /**
         * @return Neighbours.
         */
        public Map<Node, Integer> neighbours() {
            return neighbours;
        }

        @Override public String toString() {
            return "{ " + id + " }";
        }
    }

    public static void main(String[] args) {
        Node startNode = new Node(0);
        Node node1 = new Node(1);
        Node node2 = new Node(2);
        Node node3 = new Node(3);
        Node node4 = new Node(4);
        Node node5 = new Node(5);
        Node node6 = new Node(6);
        Node node7 = new Node(7);
        Node node8 = new Node(8);

        startNode.attache(4, node1);
        startNode.attache(8, node7);

        node1.attache(11, node7);
        node1.attache(8, node2);

        node2.attache(2, node8);
        node2.attache(7, node3);
        node2.attache(4, node5);

        node3.attache(14, node5);
        node3.attache(9, node4);

        node4.attache(10, node5);

        node5.attache(2, node6);

        node6.attache(6, node8);
        node6.attache(1, node7);

        node7.attache(7, node8);

        //--------------------------------------------------------

        Map<Node, Integer> shortestWays = new ConcurrentHashMap<>();
        shortestWays.put(startNode, 0);

        ForkJoinPool fjp = new ForkJoinPool();

        fjp.invoke(new DeijkstraAction(startNode, shortestWays));

        //MapUtils.debugPrint(System.out, "Shortest ways from zero-node", shortestWays);

        System.out.println(shortestWays);
    }

    public static class DeijkstraAction extends RecursiveAction {

        private Node startNode;
        private Map<Node, Integer> shortestWays;
        private Set<Node> nodesToIgnore;

        public DeijkstraAction(Node node, Map<Node, Integer> ways) {
            startNode = node;
            shortestWays = ways;

            nodesToIgnore = ConcurrentHashMap.newKeySet();
            nodesToIgnore.add(startNode);
        }

        public DeijkstraAction(Node node, Map<Node, Integer> ways, Set<Node> nodesToIgnore) {
            startNode = node;
            shortestWays = ways;
            this.nodesToIgnore = nodesToIgnore;
        }

        @Override protected void compute() {
            Set<ForkJoinTask> tasks = new HashSet<>(startNode.neighbours().size());

            startNode.neighbours().forEach((node, len) -> {

                Integer lenToNode = shortestWays.get(startNode) + len;

                if (shortestWays.putIfAbsent(node, lenToNode) != null)
                    shortestWays.computeIfPresent(node, (node1, oldLength) -> {
                        if (oldLength < lenToNode)
                            return oldLength;
                        else
                            return lenToNode;
                    });

                if (nodesToIgnore.contains(node))
                    return;

                tasks.add(new DeijkstraAction(node, shortestWays, nodesToIgnore).fork());
            });

            nodesToIgnore.add(startNode);

            tasks.forEach(ForkJoinTask::join);
        }
    }
}
