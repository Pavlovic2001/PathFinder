// PROG2 VT2022, Inlämningsuppgift, del 2
// Grupp 007 
// Mihajlo Radotic mira0355
// Shaza Alkharat shal8055
// Alexander Pavlovic alpa7946
import java.util.*;


public class ListGraph<T> implements Graph<T> {

    private final Map<T, Set<Edge<T>>> nodes = new HashMap<>();

    @Override
    public void add(T node) {
        nodes.putIfAbsent(node, new HashSet<>());
    }

    @Override
    public void connect(T node1, T node2, String name, int weight) {

        if (!nodes.containsKey(node1) || !nodes.containsKey(node2)) {
            throw new NoSuchElementException();
        }

        if (weight < 0) {
            throw new IllegalArgumentException();
        }

        Set<Edge<T>> node1Edges = nodes.get(node1);
        Set<Edge<T>> node2Edges = nodes.get(node2);

        if(getEdgeBetween(node1,node2) == null){
            node1Edges.add(new Edge(node2, name, weight));
            node2Edges.add(new Edge(node1, name, weight));
        } else {
            throw new IllegalStateException();
        }

    }

    @Override
    public void setConnectionWeight(T node1, T node2, int weight) {
        if (!nodes.containsKey(node1) || !nodes.containsKey(node2)) {
            throw new NoSuchElementException();
        }
        if (weight < 0) {
            throw new IllegalArgumentException();
        }

        getEdgeBetween(node1,node2).setWeight(weight);
        getEdgeBetween(node2,node1).setWeight(weight);

    }

    @Override
    public Set<T> getNodes() {
        Set<T> nodesSet = Set.copyOf(nodes.keySet());
        return nodesSet;
    }

    @Override
    public Collection<Edge<T>> getEdgesFrom(T node) {
        if(!nodes.containsKey(node)){
            throw new NoSuchElementException();
        }

        Set<Edge<T>> edges = nodes.get(node);
        return edges;
    }

    @Override
    public void disconnect(T node1, T node2) {



        if (!nodes.containsKey(node1) || !nodes.containsKey(node2)) {
            throw new NoSuchElementException();
        }
        if (getEdgeBetween(node1,node2) == null){
            throw new IllegalStateException();
        }

        Set<Edge<T>> set1 = nodes.get(node1);
        set1.remove(getEdgeBetween(node1,node2));

        Set<Edge<T>> set2 = nodes.get(node2);
        set2.remove(getEdgeBetween(node2,node1));

    }

    @Override
    public void remove(T node) {
        if(!nodes.containsKey(node)){
            throw new NoSuchElementException();
        }

        for(Edge<T> edge : nodes.get(node)){
            for(Edge<T> edgeOfReferenceNode : nodes.get(edge.getDestination())){
                if(edgeOfReferenceNode.getDestination().equals(node)){
                    nodes.get(edge.getDestination()).remove(edgeOfReferenceNode);
                    break;
                }
            }
        }

        nodes.remove(node);

    }

    @Override
    public boolean pathExists(T from, T to) {
        if (!nodes.containsKey(from) || !nodes.containsKey(to)){
            return false;
        }

        Set<T> visited = new HashSet<>();
        depthFirstVisitAll(from, visited);
        return visited.contains(to);
    }

    private void depthFirstVisitAll(T current, Set<T> visited) {
        visited.add(current);
        for (Edge<T> edge : nodes.get(current)) {
            if (!visited.contains(edge.getDestination())) {
                depthFirstVisitAll((T) edge.getDestination(), visited);
            }
        }
    }

    @Override
    public List<Edge<T>> getPath(T from, T to) {
        Map<T, T> connections = new HashMap<>();
        connections.put(from, null);

        LinkedList<T> queue = new LinkedList<>();
        queue.add(from);
        while (!queue.isEmpty()) {
            T first = queue.pollFirst();
            for (Edge<T> edge : nodes.get(first)) {
                T destination = (T) edge.getDestination();
                if (!connections.containsKey(destination)) {
                    connections.put(destination, first);
                    queue.add(destination);
                }
            }
        }

        if (!connections.containsKey(to)) {
            return null;
        }

        return gatherPath(from, to, connections);
    }

    private List<Edge<T>> gatherPath(T from, T to, Map<T,T> connection) {
        LinkedList<Edge<T>> path = new LinkedList<>();
        T current = to;
        while (!current.equals(from)) {
            T next = connection.get(current);
            Edge<T> edge = getEdgeBetween(next, current);
            path.addFirst(edge);
            current = next;
        }
        return Collections.unmodifiableList(path);
    }

    @Override
    public Edge<T> getEdgeBetween(T node1, T node2) {
        if (!nodes.containsKey(node1) || !nodes.containsKey(node2)) {
            throw new NoSuchElementException();
        }

        for (Edge<T> edge : nodes.get(node1)) {
            if (edge.getDestination().equals(node2)) {
                return edge;
            }
        }

        return null;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (T node : nodes.keySet()) {
            sb.append(node).append(": ").append(nodes.get(node)).append("\n");
        }
        return sb.toString();
    }
}