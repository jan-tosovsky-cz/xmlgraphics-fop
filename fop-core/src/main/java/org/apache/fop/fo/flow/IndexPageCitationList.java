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

import java.util.ArrayList;
import java.util.List;
import org.apache.fop.apps.FOPException;
import org.apache.fop.fo.Constants;
import org.apache.fop.fo.FONode;
import org.apache.fop.fo.PropertyList;
import org.apache.fop.fo.ValidationException;
import org.xml.sax.Locator;

public class IndexPageCitationList extends InlineLevel {

    private int mergeSequentialPageNumbers;
    private int mergeRangesAcrossIndexKeyReferences;
    private int mergePagesAcrossIndexKeyReferences;

    private final List<IndexKeyReference> indexKeyReferenceList = new ArrayList();
    private IndexPageCitationListSeparator indexPageCitationListSeparator;
    private IndexPageCitationRangeSeparator indexPageCitationRangeSeparator;

    // used for testing mandatory child node
    private boolean hasIndexKeyReference;

    /**
     * Base constructor
     *
     * @param parent FONode that is the parent of this object
     */
    public IndexPageCitationList(FONode parent) {
        super(parent);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void bind(PropertyList pList) throws FOPException {
        super.bind(pList);

        mergeSequentialPageNumbers = pList.get(
                Constants.PR_MERGE_SEQUENTIAL_PAGE_NUMBERS).getEnum();
        mergeRangesAcrossIndexKeyReferences = pList.get(
                Constants.PR_MERGE_RANGES_ACROSS_INDEX_KEY_REFERENCES).getEnum();
        mergePagesAcrossIndexKeyReferences = pList.get(
                Constants.PR_MERGE_PAGES_ACROSS_INDEX_KEY_REFERENCES).getEnum();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void startOfNode() throws FOPException {
        super.startOfNode();
        getFOEventHandler().startIndexPageCitationList(this);
    }

    /**
     * Make sure content model satisfied, if so then tell the {@link org.apache.fop.fo.FOEventHandler} that we are at
     * the end of the IndexPageCitationList.
     *
     * {@inheritDoc}
     */
    @Override
    public void endOfNode() throws FOPException {
        super.endOfNode();
        if (!hasIndexKeyReference) {
            missingChildElementError("(index-key-reference)");
        }
        getFOEventHandler().endIndexPageCitationList(this);
    }

    /**
     * {@inheritDoc}
     * <br>XSL Content Model:
     * (index-page-citation-list-separator?,index-page-citation-range-separator?,index-key-reference+)
     */
    @Override
    protected void validateChildNode(Locator loc, String nsURI, String localName) throws ValidationException {
        if (FO_URI.equals(nsURI)) {
            if (localName.equals("index-key-reference")) {
                hasIndexKeyReference = true;

            } else if (!localName.equals("index-page-citation-list-separator")
                    && !localName.equals("index-page-citation-range-separator")) {
                invalidChildError(loc, nsURI, localName);
            }
        }
    }

    /** {@inheritDoc} */
    @Override
    public void addChildNode(FONode child) {
        if (child.getNameId() == FO_INDEX_KEY_REFERENCE) {
            indexKeyReferenceList.add((IndexKeyReference) child);
        } else if (child.getNameId() == FO_INDEX_PAGE_CITATION_LIST_SEPARATOR) {
            indexPageCitationListSeparator = (IndexPageCitationListSeparator) child;
        } else if (child.getNameId() == FO_INDEX_PAGE_CITATION_RANGE_SEPARATOR) {
            indexPageCitationRangeSeparator = (IndexPageCitationRangeSeparator) child;
        }
    }

    public int getMergeSequentialPageNumbers() {
        return mergeSequentialPageNumbers;
    }

    public int getMergeRangesAcrossIndexKeyReferences() {
        return mergeRangesAcrossIndexKeyReferences;
    }

    public int getMergePagesAcrossIndexKeyReferences() {
        return mergePagesAcrossIndexKeyReferences;
    }

    public List<IndexKeyReference> getIndexKeyReferenceList() {
        return indexKeyReferenceList;
    }

    public IndexPageCitationListSeparator getIndexPageCitationListSeparator() {
        return indexPageCitationListSeparator;
    }

    public IndexPageCitationRangeSeparator getIndexPageCitationRangeSeparator() {
        return indexPageCitationRangeSeparator;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getLocalName() {
        return "index-page-citation-list";
    }

    /**
     * {@inheritDoc}
     * @return {@link org.apache.fop.fo.Constants#FO_INDEX_PAGE_CITATION_LIST}
     */
    public int getNameId() {
        return FO_INDEX_PAGE_CITATION_LIST;
    }

}
