package de.sven_torben.cqrs.domain;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.mockito.Mockito.*;

import org.mockito.runners.MockitoJUnitRunner;

import de.sven_torben.cqrs.domain.AggregateRoot;
import de.sven_torben.cqrs.infrastructure.IAmAnEvent;

@RunWith(MockitoJUnitRunner.class)
public class AggregateRootTest {

	private AggregateRootMock mock; 
	private EventMockA eventA;
	private EventMockB eventB;
	
	@Before
	public void setUp()
	{
		mock = new AggregateRootMock(UUID.randomUUID(), 1);
		eventA = new EventMockA();
		eventB = new EventMockB();
	}
	
	@Test
	public void testConstructor() {
		final AggregateRoot root = new AggregateRoot() {
			@Override
			protected void apply(IAmAnEvent event) {
			}
		};

		assertEquals(AggregateRoot.DEFAULT_ID, root.getId());
		assertEquals(AggregateRoot.DEFAULT_VERSION, root.getVersion());
		assertNotNull(root.getUncommittedEvents());
		assertEquals(0, root.getUncommittedEvents().size());
	}
	
	@Test
	public void testEventApplicationWithMultiDispatch() {

		assertFalse(mock.aHasBeenCalled);
		assertFalse(mock.bHasBeenCalled);
		mock.applyEvent(eventA);
		assertTrue(mock.aHasBeenCalled);
		assertFalse(mock.bHasBeenCalled);
		mock.applyEvent(eventB);
		assertTrue(mock.aHasBeenCalled);
		assertTrue(mock.bHasBeenCalled);

	}

	@Test
	public void testIdSetterGetter() {
		AggregateRoot root = mock(AggregateRoot.class, CALLS_REAL_METHODS);
		final UUID uuid = UUID.randomUUID();
		root.setId(uuid);
		assertEquals(uuid, root.getId());
	}

	@Test
	public void testVersionSetterGetter() {
		AggregateRoot root = mock(AggregateRoot.class, CALLS_REAL_METHODS);
		final int version = new Random().nextInt(1000);
		root.setVersion(version);
		assertEquals(version, root.getVersion());
	}
	
	@Test
	public void testUncommittedChanges() {
		mock.applyEvent(eventA);
		mock.applyEvent(eventB);
		
		assertEquals(2, mock.getUncommittedEvents().size());
		assertTrue(mock.getUncommittedEvents().contains(eventA));
		assertTrue(mock.getUncommittedEvents().contains(eventB));
		
		mock.markEventsAsCommitted();
		assertEquals(0, mock.getUncommittedEvents().size());
	}
	
	@Test(expected=IllegalStateException.class)
	public void testThatEventsCanOnlyOccureOnce()
	{
		mock.applyEvent(eventA);
		mock.applyEvent(eventA);
	}
	
	@Test
	public void testRebuildFromHistory()
	{
		ArrayList<IAmAnEvent> history = new ArrayList<IAmAnEvent>();
		history.add(eventA);
		history.add(eventB);
		mock.rebuildFromHistory(history);
		
		assertEquals(0, mock.getUncommittedEvents().size());
		assertTrue(mock.aHasBeenCalled);
		assertTrue(mock.bHasBeenCalled);
	}
	
}
