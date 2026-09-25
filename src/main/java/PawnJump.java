/** Immutable, time-based motion shared by the game loop and JavaFX renderer. */
public record PawnJump(int targetTile, double fromX, double fromY,
                       double toX, double toY, long startedAt) {
    public static final long FLIGHT_NANOS = 360_000_000L;
    public static final long LANDING_NANOS = 100_000_000L;
    public static final double HEIGHT = 38;

    public record Pose(double x, double y, double lift, double scaleX, double scaleY) { }

    public boolean finished(long now) {
        return now-startedAt >= FLIGHT_NANOS+LANDING_NANOS;
    }

    public Pose pose(long now) {
        double elapsed = Math.max(0, now-startedAt);
        if (elapsed < FLIGHT_NANOS) {
            double t = elapsed/FLIGHT_NANOS;
            // Gentle pickup and placement, with one clearly visible arc per board square.
            double travel = t*t*(3-2*t);
            double lift = 4*HEIGHT*t*(1-t);
            return new Pose(fromX+(toX-fromX)*travel, fromY+(toY-fromY)*travel,
                lift, 1-0.04*Math.sin(Math.PI*t), 1+0.06*Math.sin(Math.PI*t));
        }
        double t = Math.min(1, (elapsed-FLIGHT_NANOS)/LANDING_NANOS);
        double squash = Math.sin(Math.PI*t);
        return new Pose(toX, toY, 0, 1+0.10*squash, 1-0.08*squash);
    }
}
