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

import org.apache.fop.area.LinkResolver;
import org.apache.fop.area.Trait;
import org.apache.fop.area.inline.BasicLinkArea;
import org.apache.fop.area.inline.InlineArea;
import org.apache.fop.area.inline.InlineParent;
import org.apache.fop.datatypes.URISpecification;
import org.apache.fop.fo.Constants;
import org.apache.fop.fo.flow.BasicLink;
import org.apache.fop.fo.flow.IndexKeyReference;
import org.apache.fop.fonts.Font;
import org.apache.fop.fonts.FontInfo;
import org.apache.fop.fonts.FontTriplet;
import org.apache.fop.layoutmgr.PageSequenceLayoutManager;
import org.apache.fop.layoutmgr.TraitSetter;

public class IndexKeyReferenceLayoutManager extends InlineLayoutManager {

    private final int pageIndex;
    private InlineLayoutManager prefixLM;
    private InlineLayoutManager suffixLM;

    /** The index key reference object */
    private final IndexKeyReference indexKeyReference;

    /** Font for the index-key-reference */
    private Font font;

    /**
     * Constructor
     *
     * @param node the formatting object that creates this area
     */
    public IndexKeyReferenceLayoutManager(IndexKeyReference node, int pageIndex) {
        super(node);
        indexKeyReference = node;
        this.pageIndex = pageIndex;
    }

    /** {@inheritDoc} */
    @Override
    public void initialize() {
        FontInfo fi = indexKeyReference.getFOEventHandler().getFontInfo();
        FontTriplet[] fontkeys = indexKeyReference.getCommonFont().getFontState(fi);
        font = fi.getFontInstance(fontkeys[0], indexKeyReference.getCommonFont().fontSize.getValue(this));

        // initialize helper LMs
        prefixLM = new InlineLayoutManager(indexKeyReference.getIndexPageNumberPrefix());
        suffixLM = new InlineLayoutManager(indexKeyReference.getIndexPageNumberSuffix());
    }

    /** {@inheritDoc} */
    protected InlineArea createArea(boolean bInlineParent) {
        InlineArea area = super.createArea(bInlineParent);
        setupBasicLinkArea(area);
        return area;
    }

    /*
     * Detect internal or external link and add it as an area trait
     *
     * @param area the page number area
     */
    private void setupBasicLinkArea(InlineArea area) {
        BasicLink fobj = (BasicLink) this.fobj;
        // internal destinations take precedence:
        TraitSetter.addStructureTreeElement(area, fobj.getStructureTreeElement());
        if (fobj.hasInternalDestination()) {
            String idref = fobj.getInternalDestination();
            PageSequenceLayoutManager pslm = getPSLM();
            // the INTERNAL_LINK trait is added by the LinkResolver
            // if and when the link is resolved:
            LinkResolver res = new LinkResolver(idref, area);
            res.resolveIDRef(idref, pslm.getFirstPVWithID(idref));
            if (!res.isResolved()) {
                pslm.addUnresolvedArea(idref, res);
                if (area instanceof BasicLinkArea) {
                    // establish back-pointer from BasicLinkArea to LinkResolver to
                    // handle inline area unflattening during line bidi reordering;
                    // needed to create internal link trait on synthesized basic link area
                    ((BasicLinkArea)area).setResolver(res);
                }
            }
        } else if (fobj.hasExternalDestination()) {
            String url = URISpecification.getURL(fobj.getExternalDestination());
            boolean newWindow = (fobj.getShowDestination() == Constants.EN_NEW);
            if (url.length() > 0) {
                area.addTrait(Trait.EXTERNAL_LINK,
                        new Trait.ExternalLink(url, newWindow));
            }
        }
    }

    @Override
    protected InlineParent createInlineParent() {
        return new BasicLinkArea();
    }

}
