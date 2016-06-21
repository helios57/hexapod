package ch.sharpsoft.hexapod;

import java.util.List;

public class Main {

	public static void main(final String[] args) {
		final Hexapod hp = new Hexapod();
		final List<Leg> legs = hp.getLegs();
		printLegs(legs);
		for (final Leg leg : legs) {
			leg.setEndpoint(leg.getEndSegment().getEndPoint());
		}
	}

	private static void printLegs(final List<Leg> legs) {
		for (final Leg leg : legs) {
			System.out.println(leg.getId() + " " + leg.getEndSegment().getEndPoint());
		}
	}
}