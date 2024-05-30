package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class KMeans {
    private int k;
    private List<Pair> pairs;
    private List<Coordinate> centroids;
    private List<List<Pair>> clusters;
    private static final double EPSILON = 1e-4;

    public KMeans(int k, List<Pair> pairs) {
        this.k = k;
        this.pairs = pairs;
        this.centroids = new ArrayList<>(k);
        this.clusters = new ArrayList<>(k);

        for (int i = 0; i < k; i++) {
            clusters.add(new ArrayList<>());
        }
    }

    public void init() {
        Random random = new Random();
        for (int i = 0; i < k; i++) {
            int index = random.nextInt(pairs.size());
            centroids.add(pairs.get(index).getCoordinates());
        }
    }

    public void cluster() {
        boolean centroidsChanged;

        do {
            // Clear clusters
            for (List<Pair> cluster : clusters) {
                cluster.clear();
            }

            // Assign pairs to the nearest centroid
            for (Pair pair : pairs) {
                int closestCentroidIndex = 0;
                double minDistance = distance(pair.getCoordinates(), centroids.get(0));

                for (int i = 1; i < k; i++) {
                    double distance = distance(pair.getCoordinates(), centroids.get(i));
                    if (distance < minDistance) {
                        minDistance = distance;
                        closestCentroidIndex = i;
                    }
                }

                clusters.get(closestCentroidIndex).add(pair);
            }

            centroidsChanged = updateCentroids();

        } while (centroidsChanged);
    }

    private boolean updateCentroids() {
        boolean changed = false;

        for (int i = 0; i < k; i++) {
            List<Pair> cluster = clusters.get(i);

            if (cluster.isEmpty()) {
                continue;
            }

            double sumLongitude = 0;
            double sumLatitude = 0;

            for (Pair pair : cluster) {
                Coordinate coord = pair.getCoordinates();
                sumLongitude += coord.longitude;
                sumLatitude += coord.latitude;
            }

            Coordinate newCentroid = new Coordinate(sumLongitude / cluster.size(), sumLatitude / cluster.size());

            if (!isCloseEnough(newCentroid, centroids.get(i))) {
                centroids.set(i, newCentroid);
                changed = true;
            }
        }

        return changed;
    }

    private boolean isCloseEnough(Coordinate a, Coordinate b) {
        return Math.abs(a.longitude - b.longitude) < EPSILON && Math.abs(a.latitude - b.latitude) < EPSILON;
    }

    private double distance(Coordinate a, Coordinate b) {
        return Math.sqrt(Math.pow((a.longitude - b.longitude), 2) + Math.pow((a.latitude - b.latitude), 2));
    }

    public List<List<Pair>> getClusters() {
        return clusters;
    }
}
