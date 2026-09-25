import org.junit.Test;
import static org.junit.Assert.*;

public class PawnJumpTest {
    private static final long START=1_000_000_000L;
    private final PawnJump jump=new PawnJump(1,100,200,180,240,START);

    @Test public void pickupAndLandingAreExact() {
        PawnJump.Pose start=jump.pose(START);
        assertEquals(100,start.x(),0); assertEquals(200,start.y(),0); assertEquals(0,start.lift(),0);
        PawnJump.Pose landing=jump.pose(START+PawnJump.FLIGHT_NANOS);
        assertEquals(180,landing.x(),0); assertEquals(240,landing.y(),0); assertEquals(0,landing.lift(),0);
        assertFalse(jump.finished(START+PawnJump.FLIGHT_NANOS));
        assertTrue(jump.finished(START+PawnJump.FLIGHT_NANOS+PawnJump.LANDING_NANOS));
    }
    @Test public void halfFlightIsAboveTheMidpoint() {
        PawnJump.Pose middle=jump.pose(START+PawnJump.FLIGHT_NANOS/2);
        assertEquals(140,middle.x(),0.001); assertEquals(220,middle.y(),0.001);
        assertEquals(38,middle.lift(),0.001);
    }
    @Test public void everyDirectionStaysBetweenItsLandingAnchors() {
        for(int dx:new int[]{-100,0,100}) for(int dy:new int[]{-100,0,100}) {
            PawnJump motion=new PawnJump(0,500,500,500+dx,500+dy,START);
            for(long elapsed=0;elapsed<=600_000_000L;elapsed+=1_000_000L) {
                var pose=motion.pose(START+elapsed);
                assertTrue(pose.x()>=Math.min(500,500+dx) && pose.x()<=Math.max(500,500+dx));
                assertTrue(pose.y()>=Math.min(500,500+dy) && pose.y()<=Math.max(500,500+dy));
                assertTrue(pose.lift()>=0 && pose.lift()<=PawnJump.HEIGHT);
            }
        }
    }
    @Test public void landingSquashSettlesEvenAfterADelayedFrame() {
        var squash=jump.pose(START+PawnJump.FLIGHT_NANOS+PawnJump.LANDING_NANOS/2);
        assertTrue(squash.scaleX()>1); assertTrue(squash.scaleY()<1);
        var settled=jump.pose(START+2_000_000_000L);
        assertEquals(180,settled.x(),0); assertEquals(240,settled.y(),0);
        assertEquals(1,settled.scaleX(),0.001); assertEquals(1,settled.scaleY(),0.001);
    }
}
