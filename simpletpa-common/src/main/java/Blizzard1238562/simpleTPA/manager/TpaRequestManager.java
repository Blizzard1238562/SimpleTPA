package Blizzard1238562.simpleTPA.manager;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public final class TpaRequestManager {

    private final Map<UUID, Map<UUID, RequestType>> pendingRequests = new ConcurrentHashMap<>();
    private final Map<UUID, Long> cooldowns = new ConcurrentHashMap<>();
    private final Set<UUID> optedOutPlayers = ConcurrentHashMap.newKeySet();

    public boolean isOnCooldown(UUID senderId, int cooldownSeconds) {
        return getRemainingCooldownSeconds(senderId, cooldownSeconds) > 0L;
    }

    public long getRemainingCooldownSeconds(UUID senderId, int cooldownSeconds) {
        Long lastRequestTime = cooldowns.get(senderId);
        if (lastRequestTime == null) {
            return 0L;
        }
        long elapsedSeconds = (System.currentTimeMillis() - lastRequestTime) / 1000L;
        return Math.max(0L, cooldownSeconds - elapsedSeconds);
    }

    public void recordCooldown(UUID senderId) {
        cooldowns.put(senderId, System.currentTimeMillis());
    }

    public boolean hasPendingRequest(UUID senderId) {
        Map<UUID, RequestType> targets = pendingRequests.get(senderId);
        return targets != null && !targets.isEmpty();
    }

    public boolean hasPendingRequestTo(UUID senderId, UUID targetId) {
        Map<UUID, RequestType> targets = pendingRequests.get(senderId);
        return targets != null && targets.containsKey(targetId);
    }

    public void createRequest(UUID senderId, UUID targetId, RequestType type) {
        pendingRequests.computeIfAbsent(senderId, id -> new ConcurrentHashMap<>()).put(targetId, type);
    }

    public boolean isRequestStillPending(UUID senderId, UUID targetId) {
        return hasPendingRequestTo(senderId, targetId);
    }

    public boolean removeRequest(UUID senderId, UUID targetId) {
        Map<UUID, RequestType> targets = pendingRequests.get(senderId);
        if (targets == null) {
            return false;
        }
        boolean removed = targets.remove(targetId) != null;
        pendingRequests.computeIfPresent(senderId, (id, remaining) -> remaining.isEmpty() ? null : remaining);
        return removed;
    }

    public Set<PendingRequest> findRequestersForTarget(UUID targetId) {
        Set<PendingRequest> requesters = new HashSet<>();
        for (Map.Entry<UUID, Map<UUID, RequestType>> entry : pendingRequests.entrySet()) {
            RequestType type = entry.getValue().get(targetId);
            if (type != null) {
                requesters.add(new PendingRequest(entry.getKey(), type));
            }
        }
        return requesters;
    }

    public Set<PendingRequest> findTargetsForSender(UUID senderId) {
        Map<UUID, RequestType> targets = pendingRequests.get(senderId);
        if (targets == null) {
            return Set.of();
        }
        Set<PendingRequest> result = new HashSet<>();
        for (Map.Entry<UUID, RequestType> entry : targets.entrySet()) {
            result.add(new PendingRequest(entry.getKey(), entry.getValue()));
        }
        return result;
    }

    public boolean isOptedOut(UUID playerId) {
        return optedOutPlayers.contains(playerId);
    }

    public boolean toggleOptOut(UUID playerId) {
        if (optedOutPlayers.contains(playerId)) {
            optedOutPlayers.remove(playerId);
            return false;
        }
        optedOutPlayers.add(playerId);
        return true;
    }
}
