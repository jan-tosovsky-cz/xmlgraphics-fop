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

import java.util.Objects;

public class IndexPageCitationInfo implements Comparable<IndexPageCitationInfo>{

    private final int pageIndex;
    private final int endPageIndex;
    private final int nameId;
    private final String indexClass;
    private final int indexKeyReferenceIndex;

    public IndexPageCitationInfo(
            int beginPageIndex, int endPageIndex, int nameId, String indexClass, int indexKeyReferenceIndex) {

        this.pageIndex = beginPageIndex;
        this.endPageIndex = endPageIndex;
        this.nameId = nameId;
        this.indexClass = indexClass;
        this.indexKeyReferenceIndex = indexKeyReferenceIndex;
    }

    public IndexPageCitationInfo(
            int pageIndex, int nameId, String indexClass, int indexKeyReferenceIndex) {

        this.pageIndex = pageIndex;
        this.endPageIndex = pageIndex;
        this.nameId = nameId;
        this.indexClass = indexClass;
        this.indexKeyReferenceIndex = indexKeyReferenceIndex;
    }

    @Override
    public int compareTo(IndexPageCitationInfo other) {
        int result = Integer.compare(this.pageIndex, other.getPageIndex());
        if (result == 0) {
            result = Integer.compare(this.indexKeyReferenceIndex, other.getIndexKeyReferenceIndex());
        }
        return result;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 47 * hash + this.pageIndex;
        hash = 47 * hash + this.endPageIndex;
        hash = 47 * hash + this.nameId;
        hash = 47 * hash + Objects.hashCode(this.indexClass);
        hash = 47 * hash + this.indexKeyReferenceIndex;
        return hash;
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
        final IndexPageCitationInfo other = (IndexPageCitationInfo) obj;
        if (this.pageIndex != other.pageIndex) {
            return false;
        }
        if (this.endPageIndex != other.endPageIndex) {
            return false;
        }
        if (this.nameId != other.nameId) {
            return false;
        }
        if (this.indexKeyReferenceIndex != other.indexKeyReferenceIndex) {
            return false;
        }
        return Objects.equals(this.indexClass, other.indexClass);
    }

    public int getPageIndex() {
        return pageIndex;
    }

    public int getEndPageIndex() {
        return endPageIndex;
    }

    public int getNameId() {
        return nameId;
    }

    public String getIndexClass() {
        return indexClass;
    }

    public int getIndexKeyReferenceIndex() {
        return indexKeyReferenceIndex;
    }

}
