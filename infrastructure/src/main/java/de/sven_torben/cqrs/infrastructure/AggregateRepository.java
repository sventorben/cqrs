package de.sven_torben.cqrs.infrastructure;

import java.util.UUID;

import de.sven_torben.cqrs.domain.ConcurrencyException;
import de.sven_torben.cqrs.domain.IAmAnAggregateRoot;
import de.sven_torben.cqrs.domain.IStoreAggregates;
import de.sven_torben.cqrs.domain.IAmAnEvent;
import de.sven_torben.cqrs.domain.IStoreEvents;

public class AggregateRepository<TRoot extends IAmAnAggregateRoot> implements IStoreAggregates<TRoot> {

	private final IStoreEvents eventStore;
	private final Generic<TRoot> genericInstanceCreator;
	
	public AggregateRepository(final IStoreEvents eventStore, final Class<TRoot> clazz) {
		if (eventStore == null)  {
			throw new IllegalArgumentException("Argument 'eventStore' must not be a null reference.");
		}
		if (clazz == null)  {
			throw new IllegalArgumentException("Argument 'clazz' must not be a null reference.");
		}
		this.eventStore = eventStore;
		this.genericInstanceCreator = new Generic<TRoot>(clazz);
	}
	
	@Override
	public final void store(final TRoot root) throws ConcurrencyException {
		if (root == null) {
			throw new IllegalArgumentException("Argument 'root' must not be a null reference.");
		}
		eventStore.save(root.getId(), root.getUncommittedEvents(), root.getVersion());
		root.markEventsAsCommitted();
	}

	@Override
	public final TRoot retrieveWithId(final UUID aggregateRootId) {

			final TRoot root = genericInstanceCreator.getNewInstance();
			if (root != null)
			{
				root.setId(aggregateRootId);
				Iterable<IAmAnEvent> history = eventStore.getEventsForAggregate(aggregateRootId);
				root.rebuildFromHistory(history);	
			}
			return root;
			
	}
	
    private static class Generic<T>
    {

        private Class<T> clazz;

        public Generic(final Class<T> clazz)
        {
            this.clazz = clazz;
        }

        public T getNewInstance()
        {
        	T instance;
            try
            {
                instance = clazz.newInstance();
            }
            catch (final Exception e)
            {
                e.printStackTrace();
                instance = null;
            }
            return instance;
        }
    }

}
