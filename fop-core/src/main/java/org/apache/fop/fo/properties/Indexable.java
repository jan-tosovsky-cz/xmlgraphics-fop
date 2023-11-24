/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/* $Id$ */

package org.apache.fop.fo.properties;

public interface Indexable {

    /**
     * Returns the index-class property.
     *
     * @return the index-class property
     */
    public String getIndexClass();

    /**
     * Returns the index-key property.
     *
     * @return the index-key property
     */
    public String getIndexKey();

    /**
     * Returns true if index key is specified.
     *
     * @return true if index key is specified
     */
    public boolean hasIndexKey();

}

/*
fo:root
fo:page-sequence
fo:page-sequence-wrapper
fo:flow
fo:static-content
fo:block
fo:block-container
fo:bidi-override
fo:character
fo:external-graphic
fo:instream-foreign-object
fo:inline
fo:inline-container
fo:leader
fo:page-number
fo:page-number-citation
fo:page-number-citation-last
fo:scaling-value-citation
fo:table-and-caption
fo:table
fo:table-caption
fo:table-header
fo:table-footer
fo:table-body
fo:table-row
fo:table-cell
fo:list-block
fo:list-item
fo:list-item-body
fo:list-item-label
fo:basic-link
fo:multi-switch
fo:multi-case
fo:multi-toggle
fo:multi-property-set
fo:index-range-begin
fo:float
fo:footnote
fo:footnote-body
fo:wrapper
*/
