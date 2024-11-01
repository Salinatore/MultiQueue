package impl;

import api.MultiQueue;

import java.util.*;

public class MultiQueueImpl<T, Q> implements MultiQueue<T, Q> {

    final Map<Q, Queue<T>> queueMap = new HashMap<>();
    
    @Override
    public Set<Q> availableQueues() {
        return Set.copyOf(queueMap.keySet());
    }

    @Override
    public void openNewQueue(Q queue) {
        Objects.requireNonNull(queue);

        if(!queueMap.containsKey(queue)){
            queueMap.put(queue, new LinkedList<>());
        } else {
            throw new IllegalArgumentException("Queue: " + queue + " is already a queue");
        }
    }

    @Override
    public boolean isQueueEmpty(Q queue) {
        if (queueMap.containsKey(queue)) {
            return queueMap.get(queue).isEmpty();
        } else {
            throw new IllegalArgumentException("Queue: " + queue + " is not in set");
        }
    }

    @Override
    public void enqueue(T elem, Q queue) {
        Objects.requireNonNull(elem);
        Objects.requireNonNull(queue);

        if (queueMap.containsKey(queue)) {
            queueMap.get(queue).add(elem);
        } else {
            throw new IllegalArgumentException("Queue: " + queue + " is not in set");
        }
    }

    @Override
    public T dequeue(Q queue) {
        Objects.requireNonNull(queue);

        if (queueMap.containsKey(queue)) {
            return queueMap.get(queue).poll();
        } else {
            throw new IllegalArgumentException("Queue: " + queue + " is not in set");
        }
    }

    @Override
    public Map<Q, T> dequeueOneFromAllQueues() {
        final Map<Q, T> toReturn = new HashMap<>();

        for (var entry: queueMap.entrySet()) {
            toReturn.put(entry.getKey(), entry.getValue().poll());
        }

        return toReturn;
    }

    @Override
    public Set<T> allEnqueuedElements() {
        final Set<T> toReturn = new HashSet<>();

        for (var entry: queueMap.entrySet()) {
           toReturn.addAll(entry.getValue());
        }

        return toReturn;
    }

    @Override
    public List<T> dequeueAllFromQueue(Q queue) {
        Objects.requireNonNull(queue);

        if (queueMap.containsKey(queue)) {
            final List<T> toReturn = new ArrayList<>(queueMap.get(queue));
            queueMap.get(queue).clear();
            return toReturn;
        } else {
            throw new IllegalArgumentException("Queue: " + queue + " is not in set");
        }
    }

    @Override
    public void closeQueueAndReallocate(Q queue) {
        Objects.requireNonNull(queue);

        if (!queueMap.containsKey(queue)) {
            throw new IllegalArgumentException("Queue: " + queue + " is not in set");
        }
        if (queueMap.keySet().size() == 1) {
            throw new IllegalStateException("Not another queue is available");
        }

        Queue<T> listToMove = queueMap.remove(queue);
        Q receiverQueue = queueMap.keySet().iterator().next();

        queueMap.get(receiverQueue).addAll(listToMove);
    }

}
