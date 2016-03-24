/*******************************************************************************
 * Copyright (c) 2012-2016 Codenvy, S.A.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Codenvy, S.A. - initial API and implementation
 *******************************************************************************/
package org.eclipse.che.api.vfs.search.impl;

import org.eclipse.che.api.core.ServerException;
import org.eclipse.che.api.vfs.VirtualFileFilter;
import org.eclipse.che.api.vfs.VirtualFileFilters;
import org.eclipse.che.api.vfs.VirtualFileSystem;
import org.eclipse.che.api.vfs.search.MediaTypeFilter;
import org.eclipse.che.api.vfs.search.Searcher;
import org.eclipse.che.api.vfs.search.SearcherProvider;

import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

import static com.google.common.collect.Lists.newArrayList;

public abstract class AbstractLuceneSearcherProvider implements SearcherProvider {
    protected final VirtualFileFilter fileIndexFilter;
    protected final AtomicReference<Searcher> searcherReference = new AtomicReference<>();

    /**
     * @param fileIndexFilters
     *         set filter for files that should not be indexed
     */
    protected AbstractLuceneSearcherProvider(Set<VirtualFileFilter> fileIndexFilters) {
        this.fileIndexFilter = mergeFileIndexFilters(fileIndexFilters);
    }

    private VirtualFileFilter mergeFileIndexFilters(Set<VirtualFileFilter> fileIndexFilters) {
        final VirtualFileFilter filter;
        if (fileIndexFilters.isEmpty()) {
            filter = new MediaTypeFilter();
        } else {
            final List<VirtualFileFilter> myFilters = newArrayList(new MediaTypeFilter());
            myFilters.addAll(fileIndexFilters);
            filter = VirtualFileFilters.createAndFilter(myFilters);
        }
        return filter;
    }

    @Override
    public Searcher getSearcher(VirtualFileSystem virtualFileSystem, boolean create) throws ServerException {
        Searcher cachedSearcher = searcherReference.get();
        if (cachedSearcher == null && create) {
            LuceneSearcher searcher = createLuceneSearcher(() -> searcherReference.set(null));
            if (searcherReference.compareAndSet(null, searcher)) {
                searcher.init(virtualFileSystem);
            }
            cachedSearcher = searcherReference.get();
        }
        return cachedSearcher;
    }

    @Override
    public Searcher getSearcher(VirtualFileSystem virtualFileSystem) throws ServerException {
        return getSearcher(virtualFileSystem, true);
    }

    protected abstract LuceneSearcher createLuceneSearcher(CloseCallback closeCallback);

    @Override
    public void close() throws ServerException {
        Searcher searcher = searcherReference.get();
        if (searcher != null) {
            searcher.close();
        }
        searcherReference.set(null);
    }

    public interface CloseCallback {
        void onClose();
    }
}
