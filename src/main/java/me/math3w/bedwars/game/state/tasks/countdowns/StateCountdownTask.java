package me.math3w.bedwars.game.state.tasks.countdowns;

import me.math3w.bedwars.game.Game;

public abstract class StateCountdownTask extends CountdownTask {
    protected final Game game;

    public StateCountdownTask(Game game, int duration) {
        super(duration);
        this.game = game;
    }

    @Override
    protected void onFinish() {
        super.onFinish();
        game.setNextState();
    }
}
