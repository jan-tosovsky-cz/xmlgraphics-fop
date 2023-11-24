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

package org.apache.fop.fo.flow;

import org.apache.fop.apps.FOPException;
import org.apache.fop.fo.Constants;
import org.apache.fop.fo.FONode;
import org.apache.fop.fo.FObj;
import org.apache.fop.fo.PropertyList;
import org.apache.fop.fo.ValidationException;
import org.apache.fop.fo.properties.Indexable;
import org.xml.sax.Locator;

public class IndexRangeBegin extends FObj implements Indexable {

    private String indexClass;
    private String indexKey;

    /**
     * Base constructor
     *
     * @param parent FONode that is the parent of this object
     */
    public IndexRangeBegin(FONode parent) {
        super(parent);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void bind(PropertyList pList) throws FOPException {
        super.bind(pList);

        indexKey = pList.get(Constants.PR_INDEX_KEY).getString();
        indexClass = pList.get(Constants.PR_INDEX_CLASS).getString();

        if (indexKey == null || indexKey.equals("")) {
            missingPropertyError("index-key");
        }

        if (indexClass == null || indexClass.equals("")) {
            missingPropertyError("index-class");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getIndexClass() {
        return indexClass;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getIndexKey() {
        return indexKey;
    }

    /** {@inheritDoc} */
    @Override
    public boolean hasIndexKey() {
        return indexKey != null && !indexKey.isEmpty();
    }

    /**
     * {@inheritDoc}
     * <br>XSL Content Model: EMPTY
     */
    @Override
    protected void validateChildNode(Locator loc, String nsURI, String localName) throws ValidationException {
        invalidChildError(loc, nsURI, localName);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getLocalName() {
        return "index-range-begin";
    }

    /**
     * {@inheritDoc}
     * @return {@link org.apache.fop.fo.Constants#FO_INDEX_RANGE_BEGIN}
     */
    public int getNameId() {
        return FO_INDEX_RANGE_BEGIN;
    }

}
