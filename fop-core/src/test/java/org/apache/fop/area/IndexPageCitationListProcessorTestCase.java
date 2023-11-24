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
package org.apache.fop.area;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.fop.fo.Constants;
import org.junit.Assert;
import org.junit.Test;

/**
 * Tests the {@linkplain IndexPageCitationListProcessor} class.
 */
public class IndexPageCitationListProcessorTestCase {

    private static final int XB = 0;
    private static final int XI = 1;
    private static final int XX = 2;

    private static final Map<Integer, List<IndexPageCitationInfo>> RAW_MAP
            = new HashMap<Integer, List<IndexPageCitationInfo>>() {
        {
            put(XB, new ArrayList<IndexPageCitationInfo>() {
                {
                    add(new IndexPageCitationInfo(19, Constants.FO_WRAPPER, "chapter", XB));
                    add(new IndexPageCitationInfo(23, Constants.FO_WRAPPER, "chapter", XB));
                }
            });
            put(XI, new ArrayList<IndexPageCitationInfo>() {
                {
                    add(new IndexPageCitationInfo(15, Constants.FO_WRAPPER, "chapter", XI));
                    add(new IndexPageCitationInfo(25, Constants.FO_WRAPPER, "chapter", XI));
                }
            });
            put(XX, new ArrayList<IndexPageCitationInfo>() {
                {
                    add(new IndexPageCitationInfo(9, Constants.FO_WRAPPER, "preface", XX));
                    add(new IndexPageCitationInfo(10, Constants.FO_WRAPPER, "preface", XX));
                    add(new IndexPageCitationInfo(11, Constants.FO_WRAPPER, "chapter", XX));
                    add(new IndexPageCitationInfo(13, Constants.FO_INDEX_RANGE_BEGIN, "chapter", XX));
                    add(new IndexPageCitationInfo(16, Constants.FO_INDEX_RANGE_END, "chapter", XX));
                    add(new IndexPageCitationInfo(18, Constants.FO_INDEX_RANGE_BEGIN, "chapter", XX));
                    add(new IndexPageCitationInfo(21, Constants.FO_INDEX_RANGE_END, "chapter", XX));
                    add(new IndexPageCitationInfo(27, Constants.FO_WRAPPER, "chapter", XX));
                    add(new IndexPageCitationInfo(29, Constants.FO_WRAPPER, "chapter", XX));
                    add(new IndexPageCitationInfo(30, Constants.FO_WRAPPER, "chapter", XX));
                    add(new IndexPageCitationInfo(31, Constants.FO_WRAPPER, "chapter", XX));
                    add(new IndexPageCitationInfo(32, Constants.FO_WRAPPER, "chapter", XX));
                    add(new IndexPageCitationInfo(33, Constants.FO_WRAPPER, "chapter", XX));
                    add(new IndexPageCitationInfo(34, Constants.FO_WRAPPER, "chapter", XX));
                    add(new IndexPageCitationInfo(37, Constants.FO_WRAPPER, "glossary", XX));
                }
            });
        }
    };

    private static final Map<Integer, List<IndexPageCitationInfo>> EXPANDED_RANGES_WITH_DEDUPLICATED_PAGES_MAP
            = new HashMap<Integer, List<IndexPageCitationInfo>>() {
        {
            put(XB, new ArrayList<IndexPageCitationInfo>() {
                {
                    add(new IndexPageCitationInfo(19, Constants.FO_WRAPPER, "chapter", XB));
                    add(new IndexPageCitationInfo(23, Constants.FO_WRAPPER, "chapter", XB));
                }
            });
            put(XI, new ArrayList<IndexPageCitationInfo>() {
                {
                    add(new IndexPageCitationInfo(15, Constants.FO_WRAPPER, "chapter", XI));
                    add(new IndexPageCitationInfo(25, Constants.FO_WRAPPER, "chapter", XI));
                }
            });
            put(XX, new ArrayList<IndexPageCitationInfo>() {
                {
                    add(new IndexPageCitationInfo(9, Constants.FO_WRAPPER, "preface", XX));
                    add(new IndexPageCitationInfo(10, Constants.FO_WRAPPER, "preface", XX));
                    add(new IndexPageCitationInfo(11, Constants.FO_WRAPPER, "chapter", XX));
                    add(new IndexPageCitationInfo(13, Constants.FO_INDEX_RANGE_BEGIN, "chapter", XX));
                    add(new IndexPageCitationInfo(14, Constants.FO_UNKNOWN_NODE, "chapter", XX));
                    add(new IndexPageCitationInfo(15, Constants.FO_UNKNOWN_NODE, "chapter", XX));
                    add(new IndexPageCitationInfo(16, Constants.FO_INDEX_RANGE_END, "chapter", XX));
                    add(new IndexPageCitationInfo(18, Constants.FO_INDEX_RANGE_BEGIN, "chapter", XX));
                    add(new IndexPageCitationInfo(19, Constants.FO_UNKNOWN_NODE, "chapter", XX));
                    add(new IndexPageCitationInfo(20, Constants.FO_UNKNOWN_NODE, "chapter", XX));
                    add(new IndexPageCitationInfo(21, Constants.FO_INDEX_RANGE_END, "chapter", XX));
                    add(new IndexPageCitationInfo(27, Constants.FO_WRAPPER, "chapter", XX));
                    add(new IndexPageCitationInfo(29, Constants.FO_WRAPPER, "chapter", XX));
                    add(new IndexPageCitationInfo(30, Constants.FO_WRAPPER, "chapter", XX));
                    add(new IndexPageCitationInfo(31, Constants.FO_WRAPPER, "chapter", XX));
                    add(new IndexPageCitationInfo(32, Constants.FO_WRAPPER, "chapter", XX));
                    add(new IndexPageCitationInfo(33, Constants.FO_WRAPPER, "chapter", XX));
                    add(new IndexPageCitationInfo(34, Constants.FO_WRAPPER, "chapter", XX));
                    add(new IndexPageCitationInfo(37, Constants.FO_WRAPPER, "glossary", XX));
                }
            });
        }
    };

    private static final Map<Integer, List<IndexPageCitationInfo>> SEPARATED_PAGES_ACROSS_INDEX_KEY_REFERENCES_MAP
            = EXPANDED_RANGES_WITH_DEDUPLICATED_PAGES_MAP;

    private static final Map<Integer, List<IndexPageCitationInfo>> MERGED_PAGES_ACROSS_INDEX_KEY_REFERENCES_MAP
            = new HashMap<Integer, List<IndexPageCitationInfo>>() {
        {
            put(XB, new ArrayList<IndexPageCitationInfo>() {
                {
                    add(new IndexPageCitationInfo(19, Constants.FO_WRAPPER, "chapter", XB));
                    add(new IndexPageCitationInfo(23, Constants.FO_WRAPPER, "chapter", XB));
                }
            });
            put(XI, new ArrayList<IndexPageCitationInfo>() {
                {
                    add(new IndexPageCitationInfo(15, Constants.FO_WRAPPER, "chapter", XI));
                    add(new IndexPageCitationInfo(25, Constants.FO_WRAPPER, "chapter", XI));
                }
            });
            put(XX, new ArrayList<IndexPageCitationInfo>() {
                {
                    add(new IndexPageCitationInfo(9, Constants.FO_WRAPPER, "preface", XX));
                    add(new IndexPageCitationInfo(10, Constants.FO_WRAPPER, "preface", XX));
                    add(new IndexPageCitationInfo(11, Constants.FO_WRAPPER, "chapter", XX));
                    add(new IndexPageCitationInfo(13, Constants.FO_INDEX_RANGE_BEGIN, "chapter", XX));
                    add(new IndexPageCitationInfo(14, Constants.FO_UNKNOWN_NODE, "chapter", XX));
                    add(new IndexPageCitationInfo(16, Constants.FO_INDEX_RANGE_END, "chapter", XX));
                    add(new IndexPageCitationInfo(18, Constants.FO_INDEX_RANGE_BEGIN, "chapter", XX));
                    add(new IndexPageCitationInfo(20, Constants.FO_UNKNOWN_NODE, "chapter", XX));
                    add(new IndexPageCitationInfo(21, Constants.FO_INDEX_RANGE_END, "chapter", XX));
                    add(new IndexPageCitationInfo(27, Constants.FO_WRAPPER, "chapter", XX));
                    add(new IndexPageCitationInfo(29, Constants.FO_WRAPPER, "chapter", XX));
                    add(new IndexPageCitationInfo(30, Constants.FO_WRAPPER, "chapter", XX));
                    add(new IndexPageCitationInfo(31, Constants.FO_WRAPPER, "chapter", XX));
                    add(new IndexPageCitationInfo(32, Constants.FO_WRAPPER, "chapter", XX));
                    add(new IndexPageCitationInfo(33, Constants.FO_WRAPPER, "chapter", XX));
                    add(new IndexPageCitationInfo(34, Constants.FO_WRAPPER, "chapter", XX));
                    add(new IndexPageCitationInfo(37, Constants.FO_WRAPPER, "glossary", XX));
                }
            });
        }
    };

    private static final List<IndexPageCitationInfo> SEPARATED_PAGES_WITH_SEPARATED_RANGES_LIST
            = new ArrayList<IndexPageCitationInfo>() {
        {
            add(new IndexPageCitationInfo(9, Constants.FO_WRAPPER, "preface", XX));
            add(new IndexPageCitationInfo(10, Constants.FO_WRAPPER, "preface", XX));
            add(new IndexPageCitationInfo(11, Constants.FO_WRAPPER, "chapter", XX));
            add(new IndexPageCitationInfo(13, 16, Constants.FO_INDEX_RANGE_BEGIN, "chapter", XX));
            add(new IndexPageCitationInfo(15, Constants.FO_WRAPPER, "chapter", XI));
            add(new IndexPageCitationInfo(18, 21, Constants.FO_INDEX_RANGE_BEGIN, "chapter", XX));
            add(new IndexPageCitationInfo(19, Constants.FO_WRAPPER, "chapter", XB));
            add(new IndexPageCitationInfo(23, Constants.FO_WRAPPER, "chapter", XB));
            add(new IndexPageCitationInfo(25, Constants.FO_WRAPPER, "chapter", XI));
            add(new IndexPageCitationInfo(27, Constants.FO_WRAPPER, "chapter", XX));
            add(new IndexPageCitationInfo(29, 34, Constants.FO_WRAPPER, "chapter", XX));
            add(new IndexPageCitationInfo(37, Constants.FO_WRAPPER, "glossary", XX));
        }
    };

    private static final List<IndexPageCitationInfo> SEPARATED_PAGES_WITH_MERGED_RANGES_LIST
            = new ArrayList<IndexPageCitationInfo>() {
        {
            add(new IndexPageCitationInfo(9, Constants.FO_WRAPPER, "preface", XX));
            add(new IndexPageCitationInfo(10, Constants.FO_WRAPPER, "preface", XX));
            add(new IndexPageCitationInfo(11, Constants.FO_WRAPPER, "chapter", XX));
            add(new IndexPageCitationInfo(13, 16, Constants.FO_INDEX_RANGE_BEGIN, "chapter", XX));
            add(new IndexPageCitationInfo(18, 21, Constants.FO_INDEX_RANGE_BEGIN, "chapter", XX));
            add(new IndexPageCitationInfo(23, Constants.FO_WRAPPER, "chapter", XB));
            add(new IndexPageCitationInfo(25, Constants.FO_WRAPPER, "chapter", XI));
            add(new IndexPageCitationInfo(27, Constants.FO_WRAPPER, "chapter", XX));
            add(new IndexPageCitationInfo(29, 34, Constants.FO_WRAPPER, "chapter", XX));
            add(new IndexPageCitationInfo(37, Constants.FO_WRAPPER, "glossary", XX));
        }
    };

    private static final List<IndexPageCitationInfo> MERGED_PAGES_WITH_SEPARATED_RANGES_LIST
            = new ArrayList<IndexPageCitationInfo>() {
        {
            add(new IndexPageCitationInfo(9, Constants.FO_WRAPPER, "preface", XX));
            add(new IndexPageCitationInfo(10, Constants.FO_WRAPPER, "preface", XX));
            add(new IndexPageCitationInfo(11, Constants.FO_WRAPPER, "chapter", XX));
            add(new IndexPageCitationInfo(13, Constants.FO_INDEX_RANGE_BEGIN, "chapter", XX));
            add(new IndexPageCitationInfo(14, Constants.FO_UNKNOWN_NODE, "chapter", XX));
            add(new IndexPageCitationInfo(15, Constants.FO_WRAPPER, "chapter", XI));
            add(new IndexPageCitationInfo(16, Constants.FO_INDEX_RANGE_END, "chapter", XX));
            add(new IndexPageCitationInfo(18, Constants.FO_INDEX_RANGE_BEGIN, "chapter", XX));
            add(new IndexPageCitationInfo(19, Constants.FO_WRAPPER, "chapter", XB));
            add(new IndexPageCitationInfo(20, Constants.FO_UNKNOWN_NODE, "chapter", XX));
            add(new IndexPageCitationInfo(21, Constants.FO_INDEX_RANGE_END, "chapter", XX));
            add(new IndexPageCitationInfo(23, Constants.FO_WRAPPER, "chapter", XB));
            add(new IndexPageCitationInfo(25, Constants.FO_WRAPPER, "chapter", XI));
            add(new IndexPageCitationInfo(27, Constants.FO_WRAPPER, "chapter", XX));
            add(new IndexPageCitationInfo(29, 34, Constants.FO_WRAPPER, "chapter", XX));
            add(new IndexPageCitationInfo(37, Constants.FO_WRAPPER, "glossary", XX));
        }
    };

    private static final List<IndexPageCitationInfo> MERGED_PAGES_WITH_MERGED_RANGES_LIST
            = new ArrayList<IndexPageCitationInfo>() {
        {
            add(new IndexPageCitationInfo(9, Constants.FO_WRAPPER, "preface", XX));
            add(new IndexPageCitationInfo(10, Constants.FO_WRAPPER, "preface", XX));
            add(new IndexPageCitationInfo(11, Constants.FO_WRAPPER, "chapter", XX));
            add(new IndexPageCitationInfo(13, 16, Constants.FO_INDEX_RANGE_BEGIN, "chapter", XX));
            add(new IndexPageCitationInfo(18, 21, Constants.FO_INDEX_RANGE_BEGIN, "chapter", XX));
            add(new IndexPageCitationInfo(23, Constants.FO_WRAPPER, "chapter", XB));
            add(new IndexPageCitationInfo(25, Constants.FO_WRAPPER, "chapter", XI));
            add(new IndexPageCitationInfo(27, Constants.FO_WRAPPER, "chapter", XX));
            add(new IndexPageCitationInfo(29, 34, Constants.FO_WRAPPER, "chapter", XX));
            add(new IndexPageCitationInfo(37, Constants.FO_WRAPPER, "glossary", XX));
        }
    };

    @Test
    public void testExpandedRangesWithDeduplicatedPages() throws Exception {

        for (Map.Entry<Integer, List<IndexPageCitationInfo>> entry : RAW_MAP.entrySet()) {
            List<IndexPageCitationInfo> expandedList
                    = IndexPageCitationListProcessor.getExpandedRangesWithDeduplicatedPages(
                            entry.getValue());
            Collections.sort(expandedList);

            Assert.assertEquals(EXPANDED_RANGES_WITH_DEDUPLICATED_PAGES_MAP.get(entry.getKey()), expandedList);
        }
    }

    @Test
    public void testSeparatedPagesWithSeparatedRanges() throws Exception {

        List<IndexPageCitationInfo> list
                = IndexPageCitationListProcessor.getSeparatedRangesAcrossIndexKeyReferences(
                        SEPARATED_PAGES_ACROSS_INDEX_KEY_REFERENCES_MAP);
        Collections.sort(list);

        Assert.assertEquals(SEPARATED_PAGES_WITH_SEPARATED_RANGES_LIST, list);
    }

    @Test
    public void testSeparatedPagesWithMergedRanges() throws Exception {

        List<IndexPageCitationInfo> list
                = IndexPageCitationListProcessor.getMergedRangesAcrossIndexKeyReferences(
                        SEPARATED_PAGES_ACROSS_INDEX_KEY_REFERENCES_MAP);
        Collections.sort(list);

        Assert.assertEquals(SEPARATED_PAGES_WITH_MERGED_RANGES_LIST, list);
    }

    @Test
    public void testMergedPagesAcrossIndexKeyReferences() throws Exception {

        Map<Integer, List<IndexPageCitationInfo>> mergedMap
                = IndexPageCitationListProcessor.getMergedPagesAcrossIndexKeyReferences(
                        EXPANDED_RANGES_WITH_DEDUPLICATED_PAGES_MAP);

        for (Map.Entry<Integer, List<IndexPageCitationInfo>> entry
                : MERGED_PAGES_ACROSS_INDEX_KEY_REFERENCES_MAP.entrySet()) {
            List<IndexPageCitationInfo> expandedList = mergedMap.get(entry.getKey());
            Collections.sort(expandedList);

            Assert.assertEquals(entry.getValue(), expandedList);
        }
    }

    @Test
    public void testMergedPagesWithSeparateRanges() throws Exception {

        List<IndexPageCitationInfo> list
                = IndexPageCitationListProcessor.getSeparatedRangesAcrossIndexKeyReferences(
                        MERGED_PAGES_ACROSS_INDEX_KEY_REFERENCES_MAP);
        Collections.sort(list);

        Assert.assertEquals(MERGED_PAGES_WITH_SEPARATED_RANGES_LIST, list);
    }

    @Test
    public void testMergedPagesWithMergedRanges() throws Exception {

        List<IndexPageCitationInfo> list
                = IndexPageCitationListProcessor.getMergedRangesAcrossIndexKeyReferences(
                        MERGED_PAGES_ACROSS_INDEX_KEY_REFERENCES_MAP);
        Collections.sort(list);

        Assert.assertEquals(MERGED_PAGES_WITH_MERGED_RANGES_LIST, list);
    }

}
