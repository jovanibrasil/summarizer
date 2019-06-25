package summ.nlp.features;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class LocationTests {
	
	@Test
	void testRelativePositionEvenLength() {
		Location loc = new Location();
		assertEquals(1.0, loc.relativeLocation(12, 1), 0.000001);
		assertEquals(0.16666666, loc.relativeLocation(12, 6), 0.000002);
		assertEquals(0.16666666, loc.relativeLocation(12, 7), 0.000002);
		assertEquals(1.0, loc.relativeLocation(4, 4), 0.000001);
		assertEquals(1.0, loc.relativeLocation(4, 1), 0.000001);
		assertEquals(0.5, loc.relativeLocation(4, 2), 0.000002);
		assertEquals(0.5, loc.relativeLocation(4, 3), 0.000002);
		assertEquals(1.0, loc.relativeLocation(4, 4), 0.000001);
	}
	
	@Test
	void testRelativePositionOneSentenceOnly() {
		Location loc = new Location();
		assertEquals(1.0, loc.relativeLocation(1, 1), 0.000001);
	}
	
	@Test
	void testRelativePositionTwoSentenceOnly() {
		Location loc = new Location();
		assertEquals(1.0, loc.relativeLocation(2, 1), 0.000001);
		assertEquals(1.0, loc.relativeLocation(2, 2), 0.000002);
	}

	@Test
	void testRelativePositionOddLength() {
		Location loc = new Location();
		assertEquals(1, loc.relativeLocation(7, 1), 0.000001);
		assertEquals(0.666666, loc.relativeLocation(7, 2), 0.000001);
		assertEquals(0.333333, loc.relativeLocation(7, 3), 0.000001);
		assertEquals(0.25, loc.relativeLocation(7, 4), 0.000001);
		assertEquals(0.5, loc.relativeLocation(7, 5), 0.000001);
		assertEquals(0.75, loc.relativeLocation(7, 6), 0.000001);
		assertEquals(1.0, loc.relativeLocation(7, 7), 0.000001);
	}
	
	
	
}
