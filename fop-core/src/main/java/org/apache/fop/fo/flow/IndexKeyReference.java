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
import org.apache.fop.datatypes.Length;
import org.apache.fop.fo.FONode;
import org.apache.fop.fo.PropertyList;
import org.apache.fop.fo.ValidationException;
import org.xml.sax.Locator;

public class IndexKeyReference extends InlineLevel {

    // The value of properties relevant for fo:index-key-reference.
    private Length alignmentAdjust;
    private int alignmentBaseline;
    private Length baselineShift;
    private int dominantBaseline;

    private int pageNumberTreatment;
    private String refIndexKey;

    private IndexPageNumberPrefix indexPageNumberPrefix;
    private IndexPageNumberSuffix indexPageNumberSuffix;

    /**
     * Base constructor
     *
     * @param parent FONode that is the parent of this object
     */
    public IndexKeyReference(FONode parent) {
        super(parent);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void bind(PropertyList pList) throws FOPException {
        super.bind(pList);

        alignmentAdjust = pList.get(PR_ALIGNMENT_ADJUST).getLength();
        alignmentBaseline = pList.get(PR_ALIGNMENT_BASELINE).getEnum();
        baselineShift = pList.get(PR_BASELINE_SHIFT).getLength();
        dominantBaseline = pList.get(PR_DOMINANT_BASELINE).getEnum();

        // keep-with-next.within-line should be overridden to ALWAYS

        pageNumberTreatment = pList.get(PR_PAGE_NUMBER_TREATMENT).getEnum();
        refIndexKey = pList.get(PR_REF_INDEX_KEY).getString();

        if (refIndexKey == null || refIndexKey.equals("")) {
            missingPropertyError("ref-index-key");
        }
    }

    /**
     * {@inheritDoc}
     * <br>XSL Content Model: (index-page-number-prefix?,index-page-number-suffix?)
     */
    @Override
    protected void validateChildNode(Locator loc, String nsURI, String localName) throws ValidationException {
        if (FO_URI.equals(nsURI)) {
            if (!localName.equals("index-page-number-prefix")
                    && !localName.equals("index-page-number-suffix")) {
                invalidChildError(loc, nsURI, localName);
            }
        }
    }

    /** {@inheritDoc} */
    @Override
    public void addChildNode(FONode child) {
        if (child.getNameId() == FO_INDEX_PAGE_NUMBER_PREFIX) {
            indexPageNumberPrefix = (IndexPageNumberPrefix) child;
        } else if (child.getNameId() == FO_INDEX_PAGE_NUMBER_SUFFIX) {
            indexPageNumberSuffix = (IndexPageNumberSuffix) child;
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

    public int getPageNumberTreatment() {
        return pageNumberTreatment;
    }

    public String getRefIndexKey() {
        return refIndexKey;
    }

    public IndexPageNumberPrefix getIndexPageNumberPrefix() {
        return indexPageNumberPrefix;
    }

    public IndexPageNumberSuffix getIndexPageNumberSuffix() {
        return indexPageNumberSuffix;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getLocalName() {
        return "index-key-reference";
    }

    /**
     * {@inheritDoc}
     * @return {@link org.apache.fop.fo.Constants#FO_INDEX_KEY_REFERENCE}
     */
    public int getNameId() {
        return FO_INDEX_KEY_REFERENCE;
    }

}
