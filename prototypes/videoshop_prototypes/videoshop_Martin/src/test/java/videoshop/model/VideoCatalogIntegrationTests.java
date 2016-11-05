/*
 * Copyright 2013-2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package videoshop.model;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import videoshop.AbstractIntegrationTests;
import videoshop.model.Disc.DiscType;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Integration tests for {@link VideoCatalog}.
 * 
 * @author Oliver Gierke
 * @author Andreas Zaschka
 */
public class VideoCatalogIntegrationTests extends AbstractIntegrationTests {

	@Autowired VideoCatalog catalog;

	@Test
	public void findsAllBluRays() {

		Iterable<Disc> result = catalog.findByType(DiscType.BLURAY);
		assertThat(result, is(iterableWithSize(9)));
	}

	/**
	 * @see #50
	 */
	@Test
	public void discsDontHaveAnyCategoriesAssigned() {

		for (Disc disc : catalog.findByType(DiscType.BLURAY)) {
			assertThat(disc.getCategories(), is(emptyIterable()));
		}
	}
}
