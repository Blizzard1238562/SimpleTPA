package Blizzard1238562.simpleTPA.manager;

import java.util.UUID;

public record PendingRequest(UUID otherPlayerId, RequestType type) {
}
