package com.example.mygame.entitites;

/**
 * Enum to specify the state of the enemy
 */
public enum EntityState {
    IDLE,
    RUNNING,
    ATTACKING,
    DEAD;

    /**
     * Gets the state as integer
     * @param state state to get
     * @return integer representation of the state
     */
    public static int toInt(EntityState state){
        int i = 0;
        for (EntityState s : EntityState.values()){
            if (s == state) return i;
            else i++;
        }
        return 0;
    }
}
