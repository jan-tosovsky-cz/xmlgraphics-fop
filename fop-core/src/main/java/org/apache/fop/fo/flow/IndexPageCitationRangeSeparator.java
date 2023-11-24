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

import org.apache.fop.datatypes.Length;
import org.apache.fop.fo.FONode;
import org.apache.fop.fo.ValidationException;
import org.xml.sax.Locator;

public class IndexPageCitationRangeSeparator extends InlineLevel {

    // The value of properties relevant for fo:index-key-reference.
    private Length alignmentAdjust;
    private int alignmentBaseline;
    private Length baselineShift;
    private int dominantBaseline;

    /**
     * Base constructor
     *
     * @param parent FONode that is the parent of this object
     */
    public IndexPageCitationRangeSeparator(FONode parent) {
        super(parent);
    }

    /**
     * {@inheritDoc}
     * <br>XSL Content Model: (#PCDATA|%inline;)*
     */
    @Override
    protected void validateChildNode(Locator loc, String nsURI, String localName) throws ValidationException {
        if (FO_URI.equals(nsURI)) {
            if (!isInlineItem(nsURI, localName)) {
                invalidChildError(loc, nsURI, localName);
            }
        }
    }

    /** @return the "alignment-adjust" property */
    public Length getAlignmentAdjust() {
        return alignmentAdjust;
    }

    /** @return the "alignment-baseline" property */
    public int getAlignmentBaseline() {
        return alignmentBaseline;
    }

    /** @return the "baseline-shift" property */
    public Length getBaselineShift() {
        return baselineShift;
    }

    /** @return the "dominant-baseline" property */
    public int getDominantBaseline() {
        return dominantBaseline;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getLocalName() {
        return "index-page-citation-range-separator";
    }

    /**
     * {@inheritDoc}
     * @return {@link org.apache.fop.fo.Constants#FO_INDEX_PAGE_CITATION_RANGE_SEPARATOR}
     */
    public int getNameId() {
        return FO_INDEX_PAGE_CITATION_RANGE_SEPARATOR;
    }

}
