package de.sven_torben.cqrs.infrastructure.events;

import static org.mockito.Mockito.mock;

import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;

import de.sven_torben.cqrs.domain.IStoreAggregates;
import de.sven_torben.cqrs.domain.events.EventBasedAggregateRoot;
import de.sven_torben.cqrs.domain.events.IAmAnEventBasedAggregateRoot;
import de.sven_torben.cqrs.infrastructure.snapshots.EmptySnaphotRepository;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;

@RunWith(DataProviderRunner.class)
public class EventSourcingRepositoryConstructorTest {

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  @DataProvider
  public static Object[][] constructorParams() {
    return new Object[][] {
        { null, null },
        { mock(IStoreEvents.class), null },
        { null, new EmptySnaphotRepository<EventBasedAggregateRoot>() },
    };
  }

  @Test
  @UseDataProvider("constructorParams")
  public void testInvalidConstructorParams(IStoreEvents eventStore,
      IStoreAggregates<IAmAnEventBasedAggregateRoot> snapshotRepository) {
    thrown.expect(NullPointerException.class);
    new EventSourcingRepository<IAmAnEventBasedAggregateRoot>(eventStore, snapshotRepository) {
    };
  }

  @Test
  @SuppressWarnings("unchecked")
  public void testConstructorDoesNotThrow() {
    new EventSourcingRepository<EventBasedAggregateRoot>(mock(IStoreEvents.class)) {
    };
    new EventSourcingRepository<EventBasedAggregateRoot>(mock(IStoreEvents.class),
        mock(IStoreAggregates.class)) {
    };
  }
}
