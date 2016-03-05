package de.sven_torben.cqrs.domain.events;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.UUID;

@RunWith(DataProviderRunner.class)
public class ByVersionComparatorTest {

  @DataProvider
  public static Object[][] testcases() {
    IAmAnEvent lowVersion = new Event(UUID.randomUUID(), 10L) {
    };
    IAmAnEvent highVersion = new Event(UUID.randomUUID(), 99L) {
    };
    return new Object[][] {
        { lowVersion, highVersion, -1 },
        { highVersion, lowVersion, 1 },
        { lowVersion, lowVersion, 0 },
    };
  }

  @Test
  @UseDataProvider("testcases")
  public void test(IAmAnEvent lhs, IAmAnEvent rhs, int expectedResult) {
    assertThat(IAmAnEvent.BY_VERSION_COMPARATOR.compare(lhs, rhs), is(equalTo(expectedResult)));
  }

}
