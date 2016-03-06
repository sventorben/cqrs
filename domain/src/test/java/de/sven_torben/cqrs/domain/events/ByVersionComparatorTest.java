package de.sven_torben.cqrs.domain.events;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;

import de.sven_torben.cqrs.domain.events.EventDescriptor;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.UUID;

@RunWith(DataProviderRunner.class)
public class ByVersionComparatorTest {

  @DataProvider
  public static Object[][] testcases() {
    EventDescriptor lowVersion = new EventDescriptor(UUID.randomUUID(), 10L, new MockEvent());
    EventDescriptor highVersion = new EventDescriptor(UUID.randomUUID(), 99L, new MockEvent());
    return new Object[][] {
        { lowVersion, highVersion, -1 },
        { highVersion, lowVersion, 1 },
        { lowVersion, lowVersion, 0 },
    };
  }

  @Test
  @UseDataProvider("testcases")
  public void test(EventDescriptor lhs, EventDescriptor rhs, int expectedResult) {
    assertThat(EventDescriptor.BY_VERSION_COMPARATOR.compare(lhs, rhs),
        is(equalTo(expectedResult)));
  }

}
