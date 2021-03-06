package edu.jhu.cs.damsl.engine.storage.iterator.file.header;

import java.util.Iterator;

import edu.jhu.cs.damsl.catalog.identifiers.FileId;
import edu.jhu.cs.damsl.catalog.identifiers.PageId;
import edu.jhu.cs.damsl.engine.storage.file.StorageFile;
import edu.jhu.cs.damsl.engine.storage.page.SlottedPage;
import edu.jhu.cs.damsl.engine.storage.page.SlottedPageHeader;

public class SlottedFileHeaderIterator
              implements StorageFileHeaderIterator<SlottedPageHeader>
{
  StorageFile<SlottedPageHeader, SlottedPage> file;
  FileId fileId;
  int filePages;
  PageId currentPageId;
  
  public SlottedFileHeaderIterator(StorageFile<SlottedPageHeader, SlottedPage> f) {
    file = f;
    fileId = f.fileId();
    reset();
  }
  
  public FileId getFileId() { return fileId; }

  public PageId getPageId() { return currentPageId; }
  
  public void reset() {
    filePages = file.numPages();
    currentPageId = ( filePages > 0? new PageId(fileId, 0) : null );
  }
  
  public void nextPageId() {
    if ( currentPageId != null ) {
      currentPageId = ( currentPageId.pageNum()+1 < filePages?
          new PageId(fileId, currentPageId.pageNum()+1) : null );
    }
  }

  public boolean hasNext() {
    return ( currentPageId == null ?
        false : currentPageId.pageNum() < filePages);
  }

  public SlottedPageHeader next() {
    SlottedPageHeader r = null;
    if ( currentPageId == null ) return r;
    r = (SlottedPageHeader) file.readPageHeader(currentPageId);
    nextPageId();
    return r;
  }

  public void remove() {
    throw new UnsupportedOperationException("cannot remove page headers");
  }

}
