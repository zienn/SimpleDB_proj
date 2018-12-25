
package simpledb;

import java.util.*;
import java.io.*;

/**
 * The interface for database files on disk. Each table is represented by a
 * single DbFile. DbFiles can fetch pages and iterate through tuples. Each
 * file has a unique id used to store metadata about the table in the Catalog.
 * DbFiles are generally accessed through the buffer pool, rather than directly
 * by operators.
 * 磁盘上数据库文件的接口。每个表由一个DbFile表示。
 * dbfile可以获取页面并遍历元组。每个文件都有一个惟一的id，用于在目录中存储关于表的元数据。
 * dbfile通常通过缓冲池访问，而不是由操作人员直接访问。
 * <p>
 * table由DBFile组成，dbfile可以获取页面并遍历元组，每个file有一个惟一的id用于在目录中存储关于表的元数据
 * DbFile有id号、访问页、删除元组、getTupleDesc
 */

public interface DbFile {
    /**
     * Read the specified page from disk.
     *
     * @throws IllegalArgumentException if the page does not exist in this file.
     */
    public Page readPage(PageId id);

    /**
     * Push the specified page to disk.
     * This page must have been previously read from this file via a call to
     * readPage.
     *
     * @throws IOException if the write fails
     */
    public void writePage(Page p) throws IOException;

    /**
     * Adds the specified tuple to the file on behalf of transaction.
     * This method will acquire a lock on the affected pages of the file, and
     * may block until the lock can be acquired.
     *
     * @param tid The transaction performing the update
     * @param t   The tuple to add.  This tuple should be updated to reflect that
     *            it is now stored in this file.
     * @return An ArrayList contain the pages that were modified
     * @throws DbException if the tuple cannot be added
     * @throws IOException if the needed file can't be read/written
     */
    public ArrayList<Page> addTuple(TransactionId tid, Tuple t)
            throws DbException, IOException, TransactionAbortedException;

    /**
     * Removes the specifed tuple from the file on behalf of the specified
     * transaction.代表指定的事务从文件中删除指定的元组
     * This method will acquire a lock on the affected pages of the file, and
     * may block until the lock can be acquired.
     *
     * @throws DbException if the tuple cannot be deleted or is not a member
     *                     of the file
     */
    public Page deleteTuple(TransactionId tid, Tuple t)
            throws DbException, TransactionAbortedException;

    /**
     * Returns an iterator over all the tuples stored in this DbFile. The
     * iterator must use {@link BufferPool#getPage}, rather than
     * {@link #readPage} to iterate through the pages.
     *
     * @return an iterator over all the tuples stored in this DbFile.
     */
    public DbFileIterator iterator(TransactionId tid);

    /**
     * Returns a unique ID used to identify this DbFile in the Catalog. This id
     * can be used to look up the table via {@link Catalog#getDbFile} and
     * {@link Catalog#getTupleDesc}.
     * <p>
     * Implementation note:  you will need to generate this tableid somewhere,
     * ensure that each HeapFile has a "unique id," and that you always
     * return the same value for a particular HeapFile. A simple implementation
     * is to use the hash code of the absolute path of the file underlying
     * the HeapFile, i.e. <code>f.getAbsoluteFile().hashCode()</code>.
     *
     * @return an ID uniquely identifying this HeapFile.
     */
    public int getId();
    // Returns a unique ID used to identify this DbFile in the Catalog


    /**
     * Returns the TupleDesc of the table stored in this DbFile.
     *
     * @return TupleDesc of this DbFile.
     */
    public TupleDesc getTupleDesc();
}
