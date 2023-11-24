package org.kopi.ebics.enumeration.h005;

public enum EbicsUserStatusEnum {
    CREATED, // Ebics User without private/public keys
    NEW, // Ebics User with freshly generated private/public keys
    LOCKED, // User locked with SPR, can be newly initialized
    PARTLY_INITIALIZED_INI,
    PARTLY_INITIALIZED_HIA,
    INITIALIZED, // INI + HIA
    READY; // INI + HIA + HPB

    public EbicsUserStatusEnum updateStatus(EbicsUserAction action) {
        checkAction(action);
        switch (action) {
            case CREATE_KEYS:
                switch (this) {
                    case CREATED:
                    case NEW:
                        return EbicsUserStatusEnum.NEW;
                    default:
                        throw new IllegalStateException(action + " action can't be executed at user state: " + this);
                }

            case INI:
                switch (this) {
                    case NEW:
                    case LOCKED:
                        return EbicsUserStatusEnum.PARTLY_INITIALIZED_INI;
                    case PARTLY_INITIALIZED_HIA:
                        return EbicsUserStatusEnum.INITIALIZED;
                    default:
                        throw new IllegalStateException(action + " action can't be executed at user state: " + this);
                }

            case HIA:
                switch (this) {
                    case NEW:
                    case LOCKED:
                        return EbicsUserStatusEnum.PARTLY_INITIALIZED_HIA;
                    case PARTLY_INITIALIZED_INI:
                        return EbicsUserStatusEnum.INITIALIZED;
                    default:
                        throw new IllegalStateException(action + " action can't be executed at user state: " + this);
                }

            case HPB:
                switch (this) {
                    case INITIALIZED:
                    case READY:
                        return EbicsUserStatusEnum.READY;
                    default:
                        throw new IllegalStateException(action + " action can't be executed at user state: " + this);
                }

            case SPR:
                if (this == EbicsUserStatusEnum.READY) {
                    return EbicsUserStatusEnum.LOCKED;
                }
                throw new IllegalStateException(action + " action can't be executed at user state: " + this);

            case RESET:
                return EbicsUserStatusEnum.CREATED;

            case CREATE_LETTERS:
                return this;
        }

        throw new IllegalArgumentException("Unsupported action: " + action);
    }

    public void checkAction(EbicsUserAction action) {
        switch (action) {
            case CREATE_KEYS:
                if (!(this == CREATED || this == NEW)) {
                    throw new IllegalArgumentException("CREATE_KEYS action can't be executed at user state: " + this);
                }
                break;
            case CREATE_LETTERS:
                if (this == CREATED) {
                    throw new IllegalArgumentException("CREATE_LETTERS action can't be executed at user state: " + this);
                }
                break;
            case INI:
                if (!(this == NEW || this == LOCKED || this == PARTLY_INITIALIZED_HIA)) {
                    throw new IllegalArgumentException("INI action can't be executed at user state: " + this);
                }
                break;
            case HIA:
                if (!(this == NEW || this == LOCKED || this == PARTLY_INITIALIZED_INI)) {
                    throw new IllegalArgumentException("HIA action can't be executed at user state: " + this);
                }
                break;
            case HPB:
                if (!(this == INITIALIZED || this == READY)) {
                    throw new IllegalArgumentException("HPB action can't be executed at user state: " + this);
                }
                break;
            case SPR:
                if (!(this == READY)) {
                    throw new IllegalArgumentException("SPR action can't be executed at user state: " + this);
                }
                break;
            default:
                throw new IllegalArgumentException("Unsupported action: " + action);
        }
    }
}
