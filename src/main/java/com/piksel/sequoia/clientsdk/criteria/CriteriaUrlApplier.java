package com.piksel.sequoia.clientsdk.criteria;

/*-
 * #%L
 * Sequoia Java Client SDK
 * %%
 * Copyright (C) 2018 Piksel
 * %%
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
 * #L%
 */

import com.google.api.client.http.GenericUrl;
import com.piksel.sequoia.annotations.Internal;

import lombok.extern.slf4j.Slf4j;

/**
 * Applies the {@link Criteria} to an URL by creating .
 */
@Slf4j
@Internal
public class CriteriaUrlApplier {

    private final QueryStringFactory queryStringFactory = new QueryStringFactory();

    /**
     * Apply the criteria to the {@link GenericUrl url}.
     */
    public GenericUrl applyCriteria(GenericUrl url, Criteria<?> criteria) {
        log.debug("Applying criteria to URL [{}]", url);
        processQueryString(url, criteria);
        log.debug("Converted criteria to URL [{}]", url);
        return url;
    }

    private void processQueryString(GenericUrl url, Criteria<?> criteria) {
        QueryString qs = queryStringFactory
                .createQueryString((DefaultCriteria<?>) criteria);
        qs.asMap().forEach((key, value) -> url.set(key, value));
    }

}
