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

package org.apache.fop.layoutmgr.inline;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.fop.area.IndexKeyReferenceInfo;
import org.apache.fop.area.IndexPageCitationInfo;
import org.apache.fop.area.IndexPageCitationListProcessor;
import org.apache.fop.area.PageViewport;
import org.apache.fop.area.Trait;
import org.apache.fop.area.inline.InlineArea;
import org.apache.fop.area.inline.TextArea;
import org.apache.fop.fo.flow.IndexKeyReference;
import org.apache.fop.fo.flow.IndexPageCitationList;
import org.apache.fop.fo.flow.IndexPageCitationListSeparator;
import org.apache.fop.fo.flow.IndexPageCitationRangeSeparator;
import org.apache.fop.fonts.Font;
import org.apache.fop.fonts.FontInfo;
import org.apache.fop.fonts.FontTriplet;
import org.apache.fop.layoutmgr.LayoutContext;
import org.apache.fop.layoutmgr.PageSequenceLayoutManager;
import org.apache.fop.layoutmgr.TraitSetter;
import org.apache.fop.traits.MinOptMax;

public class IndexPageCitationListLayoutManager extends LeafNodeLayoutManager {

    /**
     * logging instance
     */
    private static Log log = LogFactory.getLog(IndexPageCitationListLayoutManager.class);

    private InlineLayoutManager separatorLM;
    private InlineLayoutManager rangeLM;

    /**
     * The index page number citation object
     */
    private final IndexPageCitationList indexPageCitationList;

    private Font font;
    private String indexPageCitationsString;

    /**
     * Create a new index page citation list layout manager.
     *
     * @param node the formatting object that creates this area
     */
    public IndexPageCitationListLayoutManager(IndexPageCitationList node) {
        super(node);
        indexPageCitationList = node;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void initialize() {
        FontInfo fi = indexPageCitationList.getFOEventHandler().getFontInfo();
        FontTriplet[] fontkeys = indexPageCitationList.getCommonFont().getFontState(fi);
        font = fi.getFontInstance(fontkeys[0], indexPageCitationList.getCommonFont().fontSize.getValue(this));

        // initialize helper LMs
        IndexPageCitationListSeparator listSeparator = indexPageCitationList.getIndexPageCitationListSeparator();
        IndexPageCitationRangeSeparator rangeSeparator = indexPageCitationList.getIndexPageCitationRangeSeparator();
        separatorLM = listSeparator != null ? new InlineLayoutManager(listSeparator) : null;
        rangeLM = rangeSeparator != null ? new InlineLayoutManager(rangeSeparator) : null;

        List<IndexPageCitationInfo> indexPageCitationInfoList = getIndexPageCitationInfoList();

        if (indexPageCitationInfoList.isEmpty()) {
            indexPageCitationsString = "";
        } else {
            List<String> pageNumberList = new ArrayList<>();
            for (IndexPageCitationInfo indexPageCitationInfo : indexPageCitationInfoList) {
                if (indexPageCitationInfo.getPageIndex() != indexPageCitationInfo.getEndPageIndex()) {
                    pageNumberList.add(
                            indexPageCitationInfo.getPageIndex() + "–" + indexPageCitationInfo.getEndPageIndex());
                } else {
                    pageNumberList.add(String.valueOf(indexPageCitationInfo.getPageIndex()));
                }
            }
            indexPageCitationsString = String.join(", ", pageNumberList);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected MinOptMax getAllocationIPD(int refIPD) {
        int ipd = getStringWidth(indexPageCitationsString);
        return MinOptMax.getInstance(ipd);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected InlineArea getEffectiveArea(LayoutContext layoutContext) {
        InlineArea area = getIndexPageCitationArea();
        if (!layoutContext.treatAsArtifact()) {
            TraitSetter.addStructureTreeElement(area, indexPageCitationList.getStructureTreeElement());
        }
        return area;
    }

    private InlineArea getIndexPageCitationArea() {

        int stringWidth = getStringWidth(indexPageCitationsString);

        TextArea textArea = new TextArea();
        textArea.setBidiLevel(indexPageCitationList.getBidiLevel());
        textArea.addWord(indexPageCitationsString, stringWidth, null, null, null, 0);
        setTraits(textArea, stringWidth);

        return textArea;
    }

    private int getStringWidth(String str) {
        int width = 0;
        for (int count = 0; count < str.length(); count++) {
            width += font.getCharWidth(str.charAt(count));
        }
        return width;
    }

    private void setTraits(TextArea textArea, int width) {
        TraitSetter.setProducerID(textArea, indexPageCitationList.getId());
        textArea.setBidiLevel(indexPageCitationList.getBidiLevel());
        textArea.setIPD(width);
        textArea.setBPD(font.getAscender() - font.getDescender());
        textArea.setBaselineOffset(font.getAscender());
        TraitSetter.addFontTraits(textArea, font);
        textArea.addTrait(Trait.COLOR, indexPageCitationList.getColor());
    }

    private List<IndexPageCitationInfo> getIndexPageCitationInfoList() {

        PageSequenceLayoutManager pslm = getPSLM();

        int indexKeyReferenceIndex = 0;

        Map<Integer, List<IndexPageCitationInfo>> indexPageCitationInfoMap = new HashMap<>();

        for (IndexKeyReference indexKeyReference : indexPageCitationList.getIndexKeyReferenceList()) {

            List<IndexKeyReferenceInfo> indexKeyReferenceInfoList
                    = pslm.getIndexKeyReferenceInfoMap().get(indexKeyReference.getRefIndexKey());
            List<IndexPageCitationInfo> rawIndexPageCitationInfoList = getIndexPageCitationInfoList(
                    pslm, indexKeyReferenceInfoList, indexKeyReferenceIndex);
            List<IndexPageCitationInfo> expandedIndexPageCitationInfoList
                    = IndexPageCitationListProcessor.getExpandedRangesWithDeduplicatedPages(
                            rawIndexPageCitationInfoList);

            Collections.sort(expandedIndexPageCitationInfoList);

            indexPageCitationInfoMap.put(indexKeyReferenceIndex, expandedIndexPageCitationInfoList);

            indexKeyReferenceIndex++;
        }

        return IndexPageCitationListProcessor.process(indexPageCitationInfoMap,
                indexPageCitationList.getMergePagesAcrossIndexKeyReferences(),
                indexPageCitationList.getMergeRangesAcrossIndexKeyReferences(),
                indexPageCitationList.getMergeSequentialPageNumbers());
    }

    private List<IndexPageCitationInfo> getIndexPageCitationInfoList(PageSequenceLayoutManager pslm,
            List<IndexKeyReferenceInfo> indexKeyReferenceInfoList, int indexKeyReferenceIndex) {

        List<IndexPageCitationInfo> indexPageCitationInfoList = new ArrayList<>();

        for (IndexKeyReferenceInfo indexKeyReferenceInfo : indexKeyReferenceInfoList) {
            int nameId = indexKeyReferenceInfo.getNameId();
            switch (nameId) {
                case FO_INDEX_RANGE_BEGIN: {
                    PageViewport pageViewport = pslm.getFirstPVWithID(indexKeyReferenceInfo.getId());
                    indexPageCitationInfoList.add(new IndexPageCitationInfo(
                            pageViewport.getPageIndex(), FO_INDEX_RANGE_BEGIN, indexKeyReferenceInfo.getIndexClass(),
                            indexKeyReferenceIndex));
                }
                case FO_INDEX_RANGE_END: {
                    PageViewport pageViewport = pslm.getLastPVWithID(indexKeyReferenceInfo.getId());
                    indexPageCitationInfoList.add(new IndexPageCitationInfo(
                            pageViewport.getPageIndex(), FO_INDEX_RANGE_END, indexKeyReferenceInfo.getIndexClass(),
                            indexKeyReferenceIndex));
                }
                default: {
                    List<PageViewport> pageViewportList = pslm.getPVWithIDList(indexKeyReferenceInfo.getId());
                    for (PageViewport pageViewport : pageViewportList) {
                        indexPageCitationInfoList.add(new IndexPageCitationInfo(
                                pageViewport.getPageIndex(), nameId, indexKeyReferenceInfo.getIndexClass(),
                                indexKeyReferenceIndex));
                    }
                }
            }
        }

        return indexPageCitationInfoList;
    }

}
