package Blizzard1238562.simpleTPA.manager;

import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public final class TpaRequestManager {

    private final Map<UUID, Set<UUID>> pendingRequests = new ConcurrentHashMap<>();
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
        Set<UUID> targets = pendingRequests.get(senderId);
        return targets != null && !targets.isEmpty();
    }

    public boolean hasPendingRequestTo(UUID senderId, UUID targetId) {
        Set<UUID> targets = pendingRequests.get(senderId);
        return targets != null && targets.contains(targetId);
    }

    public void createRequest(UUID senderId, UUID targetId) {
        pendingRequests.computeIfAbsent(senderId, id -> ConcurrentHashMap.newKeySet()).add(targetId);
    }

    public boolean isRequestStillPending(UUID senderId, UUID targetId) {
        Set<UUID> targets = pendingRequests.get(senderId);
        return targets != null && targets.contains(targetId);
    }

    public boolean removeRequest(UUID senderId, UUID targetId) {
        Set<UUID> targets = pendingRequests.get(senderId);
        if (targets == null) {
            return false;
        }
        boolean removed = targets.remove(targetId);
        pendingRequests.computeIfPresent(senderId, (id, remaining) -> remaining.isEmpty() ? null : remaining);
        return removed;
    }

    public Set<UUID> findRequestersForTarget(UUID targetId) {
        Set<UUID> requesters = ConcurrentHashMap.newKeySet();
        for (Map.Entry<UUID, Set<UUID>> entry : pendingRequests.entrySet()) {
            if (entry.getValue().contains(targetId)) {
                requesters.add(entry.getKey());
            }
        }
        return requesters;
    }

    public Set<UUID> findTargetsForSender(UUID senderId) {
        Set<UUID> targets = pendingRequests.get(senderId);
        return targets == null ? Set.of() : Set.copyOf(targets);
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
