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
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import org.apache.fop.fo.Constants;

public class IndexPageCitationListProcessor {

    public static List<IndexPageCitationInfo> process(
            Map<Integer, List<IndexPageCitationInfo>> indexPageCitationInfoMap,
            int mergePagesAcrossIndexKeyReferences, int mergeRangesAcrossIndexKeyReferences,
            int mergeSequentialPageNumbers) {

        if (mergePagesAcrossIndexKeyReferences == Constants.EN_MERGE) {
            indexPageCitationInfoMap = getMergedPagesAcrossIndexKeyReferences(indexPageCitationInfoMap);
        }

        if (mergeSequentialPageNumbers == Constants.EN_MERGE) {
            if (mergeRangesAcrossIndexKeyReferences == Constants.EN_LEAVE_SEPARATE) {
                return getSeparatedRangesAcrossIndexKeyReferences(indexPageCitationInfoMap);
            } else {
                return getMergedRangesAcrossIndexKeyReferences(indexPageCitationInfoMap);
            }
        } else {
            List<IndexPageCitationInfo> indexPageCitationInfoList = new ArrayList<>();
            for (Map.Entry<Integer, List<IndexPageCitationInfo>> indexPageCitationInfoMapEntry
                    : indexPageCitationInfoMap.entrySet()) {
                for (IndexPageCitationInfo indexPageCitationInfo : indexPageCitationInfoMapEntry.getValue()) {
                    indexPageCitationInfoList.add(indexPageCitationInfo);
                }
            }
            Collections.sort(indexPageCitationInfoList);

            return indexPageCitationInfoList;
        }
    }

    public static List<IndexPageCitationInfo> getExpandedRangesWithDeduplicatedPages(
            List<IndexPageCitationInfo> indexPageCitationList) {

        Map<Integer, IndexPageCitationInfo> indexPageCitationInfoMap = new HashMap<>();
        for (IndexPageCitationInfo indexPageCitationInfo : indexPageCitationList) {
            int nameId = indexPageCitationInfo.getNameId();
            if (nameId != Constants.FO_INDEX_RANGE_BEGIN && nameId != Constants.FO_INDEX_RANGE_END) {
                indexPageCitationInfoMap.put(indexPageCitationInfo.getPageIndex(), indexPageCitationInfo);
            }
        }

        int indexRangeBegin = 0;
        for (IndexPageCitationInfo indexPageCitationInfo : indexPageCitationList) {
            String indexClass = indexPageCitationInfo.getIndexClass();
            int indexKeyReferenceIndex = indexPageCitationInfo.getIndexKeyReferenceIndex();
            int nameId = indexPageCitationInfo.getNameId();
            if (nameId == Constants.FO_INDEX_RANGE_BEGIN) {
                indexRangeBegin = indexPageCitationInfo.getPageIndex();
            } else if (nameId == Constants.FO_INDEX_RANGE_END) {
                int indexRangeEnd = indexPageCitationInfo.getPageIndex();
                if (indexRangeEnd == indexRangeBegin) {
                    indexPageCitationInfoMap.put(indexPageCitationInfo.getPageIndex(), new IndexPageCitationInfo(
                            indexRangeBegin, Constants.FO_WRAPPER, indexClass,
                            indexKeyReferenceIndex));
                } else {
                    indexPageCitationInfoMap.put(indexRangeBegin, new IndexPageCitationInfo(
                            indexRangeBegin, Constants.FO_INDEX_RANGE_BEGIN, indexClass,
                            indexKeyReferenceIndex));
                    for (int pageIndex = indexRangeBegin + 1; pageIndex < indexRangeEnd; pageIndex++) {
                        indexPageCitationInfoMap.put(pageIndex, new IndexPageCitationInfo(
                                pageIndex, Constants.FO_UNKNOWN_NODE, indexClass,
                                indexKeyReferenceIndex));
                    }
                    indexPageCitationInfoMap.put(indexRangeEnd, new IndexPageCitationInfo(
                            indexRangeEnd, Constants.FO_INDEX_RANGE_END, indexClass,
                            indexKeyReferenceIndex));
                }
            }
        }

        return new ArrayList<>(indexPageCitationInfoMap.values());
    }

    protected static Map<Integer, List<IndexPageCitationInfo>> getMergedPagesAcrossIndexKeyReferences(
            Map<Integer, List<IndexPageCitationInfo>> indexPageCitationInfoMap) {

        Map<Integer, IndexPageCitationInfo> pageInfoMap = new HashMap<>();
        List<IndexPageCitationInfo> duplicateList = new ArrayList<>();

        for (Map.Entry<Integer, List<IndexPageCitationInfo>> pageInfoEntry : indexPageCitationInfoMap.entrySet()) {
            for (IndexPageCitationInfo pageInfo : pageInfoEntry.getValue()) {
                if (pageInfoMap.containsKey(pageInfo.getPageIndex())) {
                    IndexPageCitationInfo duplicatePageInfo = pageInfoMap.get(pageInfo.getPageIndex());
                    if (pageInfo.getNameId() == Constants.FO_UNKNOWN_NODE) {
                        duplicateList.add(pageInfo);
                    } else {
                        duplicateList.add(duplicatePageInfo);
                        pageInfoMap.put(pageInfo.getPageIndex(), pageInfo);
                    }

                } else {
                    pageInfoMap.put(pageInfo.getPageIndex(), pageInfo);
                }
            }
        }

        for (IndexPageCitationInfo info : duplicateList) {
            indexPageCitationInfoMap.get(info.getIndexKeyReferenceIndex()).remove(info);
        }

        return indexPageCitationInfoMap;
    }

    protected static List<IndexPageCitationInfo> getSeparatedRangesAcrossIndexKeyReferences(
            Map<Integer, List<IndexPageCitationInfo>> sourceIndexPageCitationInfoMap) {

        List<IndexPageCitationInfo> indexPageCitationInfoList = new ArrayList<>();

        for (Map.Entry<Integer, List<IndexPageCitationInfo>> indexPageCitationInfoMapEntry
                : sourceIndexPageCitationInfoMap.entrySet()) {
            Map<Integer, IndexPageCitationInfo> indexPageCitationInfoMap = new TreeMap<>();
            for (IndexPageCitationInfo indexPageCitationInfo : indexPageCitationInfoMapEntry.getValue()) {
                indexPageCitationInfoMap.put(indexPageCitationInfo.getPageIndex(), indexPageCitationInfo);
            }

            indexPageCitationInfoList.addAll(getMergedRangesIndexPageCitationInfoList(indexPageCitationInfoMap));
        }

        return indexPageCitationInfoList;
    }

    protected static List<IndexPageCitationInfo> getMergedRangesAcrossIndexKeyReferences(
            Map<Integer, List<IndexPageCitationInfo>> sourceIndexPageCitationInfoMap) {

        Map<Integer, IndexPageCitationInfo> indexPageCitationInfoMap = new TreeMap<>();

        for (Map.Entry<Integer, List<IndexPageCitationInfo>> indexPageCitationInfoMapEntry
                : sourceIndexPageCitationInfoMap.entrySet()) {
            for (IndexPageCitationInfo indexPageCitationInfo : indexPageCitationInfoMapEntry.getValue()) {
                indexPageCitationInfoMap.put(indexPageCitationInfo.getPageIndex(), indexPageCitationInfo);
            }
        }

        return getMergedRangesIndexPageCitationInfoList(indexPageCitationInfoMap);
    }

    protected static List<IndexPageCitationInfo> getMergedRangesIndexPageCitationInfoList(
            Map<Integer, IndexPageCitationInfo> indexPageCitationInfoMap) {

        List<IndexPageCitationInfo> mergedRangesIndexPageCitationInfoList = new ArrayList<>();
        List<Integer> unusedPageList = new ArrayList<>();

        int startPage = indexPageCitationInfoMap.keySet().iterator().next();
        int lastPage = startPage;

        for (int currentPage : indexPageCitationInfoMap.keySet()) {
            if (currentPage - lastPage != 1) {
                if (lastPage - startPage > 2) {
                    IndexPageCitationInfo indexPageCitationInfo = indexPageCitationInfoMap.get(startPage);
                    for (int unusedPage = startPage; unusedPage <= lastPage; unusedPage++) {
                        unusedPageList.add(unusedPage);
                    }
                    mergedRangesIndexPageCitationInfoList.add(new IndexPageCitationInfo(
                            startPage, lastPage, indexPageCitationInfo.getNameId(),
                            indexPageCitationInfo.getIndexClass(), indexPageCitationInfo.getIndexKeyReferenceIndex()));
                }
                startPage = currentPage;
            }
            lastPage = currentPage;
        }

        for (int unusedPage : unusedPageList) {
            indexPageCitationInfoMap.remove(unusedPage);
        }

        for (IndexPageCitationInfo indexPageCitationInfo : indexPageCitationInfoMap.values()) {
            mergedRangesIndexPageCitationInfoList.add(indexPageCitationInfo);
        }

        return mergedRangesIndexPageCitationInfoList;
    }

    protected static List<IndexPageCitationInfo> mergePagesAcrossIndexKeyReferences(
            List<IndexPageCitationInfo> indexPageCitationList) {

        List<IndexPageCitationInfo> mergedIndexPageCitationList = new ArrayList<>();

        Set<Integer> reservedPageIndexSet = new HashSet<>();

        for (IndexPageCitationInfo indexPageCitationInfo : indexPageCitationList) {
            if (isProtected(indexPageCitationInfo)) {
                reservedPageIndexSet.add(indexPageCitationInfo.getPageIndex());
            }
        }

        for (IndexPageCitationInfo indexPageCitationInfo : indexPageCitationList) {
            if (reservedPageIndexSet.contains(indexPageCitationInfo.getPageIndex())
                    && !isProtected(indexPageCitationInfo)) {
                continue;
            }
            mergedIndexPageCitationList.add(indexPageCitationInfo);
        }

        return mergedIndexPageCitationList;
    }

    private static List<IndexPageCitationInfo> mergeSequentialPageNumbers(
            List<IndexPageCitationInfo> indexPageCitationList, int mergePagesAcrossIndexKeyReferences) {

        List<IndexPageCitationInfo> mergedIndexPageCitationList = new ArrayList<>();

        if (indexPageCitationList.size() > 2) {
            Map<Integer, List<Map<Integer, Integer>>> pageNumbersMap = new HashMap<>();

            int lastInterruptedPageIndex = -10;
            int lastPageIndex = -10;

            for (int i = 0; i < indexPageCitationList.size(); i++) {
                IndexPageCitationInfo indexPageCitationInfo = indexPageCitationList.get(i);
                int pageIndex = indexPageCitationInfo.getPageIndex();
                Map<Integer, Integer> entryMap = new HashMap<>();
                entryMap.put(pageIndex, i);

                if (pageIndex - lastPageIndex != 1) {
                    pageNumbersMap.put(pageIndex, new ArrayList<Map<Integer, Integer>>());
                    pageNumbersMap.get(pageIndex).add(entryMap);
                    lastInterruptedPageIndex = pageIndex;
                } else {
                    pageNumbersMap.get(lastInterruptedPageIndex).add(entryMap);
                }
                lastPageIndex = pageIndex;
            }

            for (List<Map<Integer, Integer>> list : pageNumbersMap.values()) {
                if (list.size() > 2) {
                    for (Integer listIndex : list.get(0).values()) {
                        mergedIndexPageCitationList.add(indexPageCitationList.get(listIndex));
                    }
                    for (Integer listIndex : list.get(list.size() - 1).values()) {
                        mergedIndexPageCitationList.add(indexPageCitationList.get(listIndex));
                    }
                } else {
                    for (Map<Integer, Integer> listMap : list) {
                        for (Integer listIndex : listMap.values()) {
                            mergedIndexPageCitationList.add(indexPageCitationList.get(listIndex));
                        }
                    }
                }
            }

        } else {
            mergedIndexPageCitationList = new ArrayList<>(indexPageCitationList);
        }

        return mergedIndexPageCitationList;
    }

    protected static boolean isProtected(IndexPageCitationInfo indexPageCitationInfo) {
        int nameId = indexPageCitationInfo.getNameId();
        return (nameId == Constants.FO_INDEX_RANGE_BEGIN
                || nameId == Constants.FO_INDEX_RANGE_END
                || nameId == Constants.FO_UNKNOWN_NODE);
    }
}
