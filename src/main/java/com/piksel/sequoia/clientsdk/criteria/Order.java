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

import com.piksel.sequoia.annotations.PublicEvolving;

/**
 * Indicates the order in which results should be returned.
 */
@PublicEvolving
class Order {

    private final String propertyName;
    private final boolean ascending;

    public Order(String propertyName, boolean ascending) {
        this.propertyName = propertyName;
        this.ascending = ascending;
    }

    public static Order asc(String propertyName) {
        return new Order(propertyName, true);
    }

    public static Order desc(String propertyName) {
        return new Order(propertyName, false);
    }

    public String getPropertyName() {
        return propertyName;
    }

    public boolean isAscending() {
        return ascending;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((propertyName == null) ? 0 : propertyName.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Order other = (Order) obj;
        if (propertyName == null && other.propertyName != null) {
            return false;
        } else {
            return propertyName == null || propertyName.equals(other.propertyName);
        }
    }

    @Override
    public String toString() {
        return (this.isAscending() ? this.propertyName
                : "-" + this.propertyName);
    }
}
